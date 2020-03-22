using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using ndBase;

namespace ndString
{
 
 
 public class Formula
 {
  //Copyright© 1996-2012 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties, You may use, modify, distribute this code under GNU public licence for NON-Commercial use as long as this notice remains unchanged.
  private static bool selfTested  = false;  private static string ass(bool expr) { if (!expr) throw new Exception(ass(true) + " SelfTest Failure"); return "Formula"; } private void init() { if (!selfTested) selfTest(); }
 
  private static bool     useComma = false;
  private static string   namesCharacters = "[]\"'´`$§,.0123456789«_abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
  private static string   numCharacters = ",.0123456789";
  private static string   signCharacters = "+-!";
  private static string   operatorCharacters = "~&|^><=°\\%/*+-";

  private Pile<Reach> terms = new Pile<Reach>();
  private Zone braces = new Zone(new Pile<string>("", true, "\r", "\n"), new Pile<string>("", true, "(", ")"));
  public readonly KeyPile<string, Reach> defaultValues = new KeyPile<string, Reach>();

  private Pile<Reach> splitBraces(Reach formula)
  {
   while (formula.at(1, utl.dmyBool("al(occur, token) is planned"), "(").len > 0)
   {
    Reach part = braces.upon(formula);
    Reach lastPart = formula;
    while (part.startsWith("("))
    {
     lastPart = part;
     part = braces.upon(part.from(2));
    }
    if (lastPart.startsWith("(")) terms.Add(braces.on(lastPart).Trim()); else terms.Add(lastPart.Trim());
    formula = formula.RPW("«" + terms.Len + "»").at(lastPart);
   }
   if (formula.startsWith("(")) terms.Add(braces.on(formula).Trim()); else terms.Add(formula.Trim());
   return terms;
  }

  private string splitAtOperators(string operators, Reach term)
  {
   Reach newTerm = term.upto(0);
   string ret = "";
   term  = term.Trim();
   while (term.len > 0)
   {
    Reach sign = term.upto(1); sign = sign.upto(true, -1, signCharacters); if (sign.len > 0) sign = term.before(false, 1, signCharacters);
    term = term.after(sign).Trim();
    Reach num = term.upto(true, 1, namesCharacters).Trim();
    if (num.startsWith("«")) num = term.upto(1, utl.dmyBool("al(occur, token) is planned"), "»");
    else
    {
     if (num.upto(true, -1, numCharacters).len == 0) num = term.before(false, 1, namesCharacters); 
     else 
     {
      num = term.before(false, 1, numCharacters);
      if (term.after(num).StartsWith("e"))
      {
       num = num + term.after(num).upto(1);
       while (term.after(num).startsWith(true, "+-")) num = num + term.after(num).upto(1);
       num = num + term.after(num).before(false, 1, numCharacters);
      }
     }
     if (num.before(-1, utl.dmyBool("al(occur, token) is planned"), "«").len > 0) { sign = sign + num.before(-1, utl.dmyBool("al(occur, token) is planned"), "«"); term = term.after(sign).Trim(); num = term.upto(1, utl.dmyBool("al(occur, token) is planned"), "»").Trim(); }
    }
    term = term.after(num).Trim();
    Reach optor = term.upto(1).upto(true, -1, operatorCharacters);
    term = term.after(optor).Trim();
    if (term.len == 0) optor = optor.upto(0);

    if (optor.upto(true, -1, operators).len > 0) newTerm = newTerm + sign + num + optor; else { if (newTerm.len > 0) { terms.Add(newTerm + sign + num); newTerm = term.upto(0); ret += "«" + (terms.Len) + "»"; } else ret += (sign + num).text; if (optor.len > 0) ret += optor.text; else optor = ""; }
   }
   return ret;
  }

  private Pile<Reach> splitFormula(Reach formula)
  {
   terms = splitBraces(formula);

   Pile<Reach> Terms         = new Pile<Reach>();
   long        closePending  = 0;
   bool        addClose      = false;
   int         termsCount    = terms.Len;
   for (int i = 1; i <= termsCount; i++)
   {
    Reach term = terms[i].Trim();
    Terms.Add(term.upto(0));
    while (term.len > 0)
    {
     Reach sign = term.upto(1);
     Reach num = ""; 
     if (sign.equals("'")) 
     {
      num = term.from(sign).upto(2, utl.dmyBool("al(occur, token) is planned"), "'");
      term = term.Trim();
      sign = sign.upto(0);
     }
     else
      if (sign.equals("["))
      {
       num = term.from(sign).upto(1, utl.dmyBool("al(occur, token) is planned"), "]");
       term = term.Trim();
       sign = sign.upto(0);
      }
      else
      { 
       sign = sign.upto(true, -1, signCharacters); 
       if (sign.len > 0) sign = term.before(false, 1, signCharacters);
       term = term.after(sign).Trim();
       num = term.upto(true, 1, namesCharacters).Trim();
      }
     addClose = false;
     if (num.startsWith("«")) num = term.upto(1, utl.dmyBool("al(occur, token) is planned"), "»").Trim();
     else if (num.startsWith("[")) {}
     else if (num.startsWith("'")) {}
     else
     {
      if (num.upto(true, -1, numCharacters).len == 0) num = term.before(false, 1, namesCharacters); 
      else 
      {
       num = term.before(false, 1, numCharacters);
       if (term.after(num).StartsWith("e"))
       {
        num = num + term.after(num).upto(1);
        while (term.after(num).startsWith(true, "+-")) num = num + term.after(num).upto(1);
        num = num + term.after(num).before(false, 1, numCharacters);
       }
      }
      if (num.before(-1, utl.dmyBool("al(occur, token) is planned"), "«").len > 0) { sign = sign + num.before(-1, utl.dmyBool("al(occur, token) is planned"), "«"); term = term.after(sign).Trim(); num = term.upto(1, utl.dmyBool("al(occur, token) is planned"), "»").Trim(); }
      if (closePending > 0) { closePending--; if (closePending == 0) addClose = true; }
     }
     term = term.after(num).Trim();
     Reach optor = term.upto(1).upto(true, -1, operatorCharacters);
     term = term.after(optor).Trim();
     if (optor.len == 0) if (term.startsWith(true, numCharacters))
      {
       optor = sign;
       if (optor.len == 0) optor = optor + "*"; else optor = "*"; //if (optor.len == 0) optor = optor + "+"; else optor = "*";
       sign = "(" + sign;
       closePending = 2;
      } else
      {
       optor = optor + "*";
      }
     if (addClose) num = num + ")";
     Terms[-1] = Terms[-1] + (sign + num);
     if (term.len > 0) Terms[-1] = Terms[-1] + new Reach(optor.text); //AttGeTr: if using the same sign reach twice within Term[-1] some following Reach Subtractions will create unexpected results!
    }
    if (Terms[-1].at(1, utl.dmyBool("al(occur, token) is planned"), "(").len > 0) Terms[-1] = splitBraces(Terms[-1])[-1];
   }
   for (int i = 1; i <= Terms.Len; i++) terms[i] = Terms[i];
   terms.Add(terms[termsCount]);

   termsCount = terms.Len;
   for (int i = 1; i <= termsCount; i++) terms[i] = splitAtOperators("&", terms[i]);
   terms.Add(terms[termsCount]);

   termsCount = terms.Len;
   for (int i = 1; i <= termsCount; i++) terms[i] = splitAtOperators("|^", terms[i]);
   terms.Add(terms[termsCount]);

   termsCount = terms.Len;
   for (int i = 1; i <= termsCount; i++) terms[i] = splitAtOperators("><=", terms[i]);
   terms.Add(terms[termsCount]);

   termsCount = terms.Len;
   for (int i = 1; i <= termsCount; i++) terms[i] = splitAtOperators("°", terms[i]);
   terms.Add(terms[termsCount]);

   termsCount = terms.Len;
    
   for (int i = 1; i <= termsCount; i++) terms[i] = splitAtOperators("*/%\\", terms[i]);
   terms.Add(terms[termsCount]);

   return terms;
  }


  private Reach detailled(Reach term)
  {
   string ret = "";
   term  = term.Trim();
   while (term.len > 0)
   {
    Reach sign = term.upto(1); sign = sign.upto(true, -1, signCharacters); if (sign.len > 0) sign = term.before(false, 1, signCharacters);
    term = term.after(sign).Trim();
    Reach num = term.upto(true, 1, namesCharacters).Trim();
    if (num.startsWith("«")) num = term.upto(1, utl.dmyBool("al(occur, token) is planned"), "»");
    else
    {
     if (num.upto(true, -1, numCharacters).len == 0) num = term.before(false, 1, namesCharacters); 
     else 
     {
      num = term.before(false, 1, numCharacters);
      if (term.after(num).StartsWith("e"))
      {
       num = num + term.after(num).upto(1);
       while (term.after(num).startsWith(true, "+-")) num = num + term.after(num).upto(1);
       num = num + term.after(num).before(false, 1, numCharacters);
      }
     }
     if (num.before(-1, utl.dmyBool("al(occur, token) is planned"), "«").len > 0) { sign = sign + num.before(-1, utl.dmyBool("al(occur, token) is planned"), "«"); term = term.after(sign).Trim(); num = term.upto(1, utl.dmyBool("al(occur, token) is planned"), "»").Trim(); }
    }
    term = term.after(num).Trim();
    Reach optor = term.upto(1).upto(true, -1, operatorCharacters);
    term = term.after(optor).Trim();
    ret += "\r\n" + (sign + num).text;
    if (term.len > 0) ret += "\r\n" + optor.text; else optor = "";
   }
   return ret.Substring(2);
  }

  private string format(Reach term, KeyPile<string, Reach> values)
  {
   term = term.Trim();
   if (term.startsWith("+")) return format(term.from(2), values);
   if (term.startsWith("-")) return "-" + format(term.from(2), values);
   if (term.startsWith("!")) return "!" + format(term.from(2), values);
   if (term.startsWith("«")) return format(Int32.Parse(term.from(2).before(1, utl.dmyBool("al(occur, token) is planned"), "»").text), values);
   if (term.startsWith(true, numCharacters)) return "" + parseDouble(term);
   if (term.at(1, utl.dmyBool("al(occur, token) is planned"), "«").len == 0)
    try { return "" + parseDouble(values[term.text]); } catch (Exception ex) { return term.text.Trim(); }
   Reach function = term.XTR().before(1, utl.dmyBool("al(occur, token) is planned"), "«");
   string ret = format(Int32.Parse(term.from(2).before(1, utl.dmyBool("al(occur, token) is planned"), "»").text), values);
   if (function.equals("abs")) return "abs(" + ret + ")";
   if (function.equals("sign")) return "sign(" + ret + ")";
   if (function.equals("round")) return "round(" + ret + ")";
   if (function.equals("ceiling")) return "ceiling(" + ret + ")";
   if (function.equals("floor")) return "floor(" + ret + ")";
   if (function.equals("sqrt")) return "sqrt(" + ret + ")";
   if (function.equals("exp")) return "exp(" + ret + ")";
   if (function.equals("log")) return "log(" + ret + ")";
   if (function.equals("sin")) return "sin(" + ret + ")";
   if (function.equals("asin")) return "asin(" + ret + ")";
   if (function.equals("cos")) return "cos(" + ret + ")";
   if (function.equals("acos")) return "acos(" + ret + ")";
   if (function.equals("tan")) return "tan(" + ret + ")";
   if (function.equals("atan")) return "atan(" + ret + ")";
   if (function.equals("sinh")) return "sinh(" + ret + ")";
   if (function.equals("cosh")) return "cosh(" + ret + ")";
   if (function.equals("tanh")) return "tanh(" + ret + ")";
   return "0";
  }

  private string format(int inx, KeyPile<string, Reach> values)
  {
   if (terms[inx].startsWith("'") || terms[inx].startsWith("[")) return terms[inx];
   Pile<Reach> term = detailled(terms[inx]).split("\r\n");
   string ret = format(term[1], values);
   Reach optor = "";
   Reach lastOptor = "";
   for (int i = 2; i <= term.Len; i++)
   {
    if ((i % 2) == 0)
    {
     optor = term[i];
    } else
    {
     if (term[i].len == 0)
     {
      lastOptor = lastOptor + optor;
     } else
     {
      if (lastOptor.len > 0) { optor = lastOptor + optor; lastOptor = ""; }
      string res = format(term[i], values);
      if (optor.equals("+"))        ret = "(" + ret + " + " + res + ")";
      else if (optor.equals("-"))   ret = "(" + ret + " - " + res + ")";
      else if (optor.equals("*"))   ret = "(" + ret + " * " + res + ")";
      else if (optor.equals("/"))   ret = "(" + ret + " / " + res + ")";
      else if (optor.equals("\\"))  ret = "(" + ret + " \\ " + res + ")";
      else if (optor.equals("%"))   ret = "(" + ret + " % " + res + ")";
      else if (optor.equals("°"))   ret = "(" + ret + " ° " + res + ")";
      else if (optor.equals("="))   ret = "(" + ret + " = " + res + ")";
      else if (optor.equals("=="))  ret = "(" + ret + " == " + res + ")";
      else if (optor.equals(">"))   ret = "(" + ret + " > " + res + ")";
      else if (optor.equals(">="))  ret = "(" + ret + " >= " + res + ")";
      else if (optor.equals("<"))   ret = "(" + ret + " < " + res + ")";
      else if (optor.equals("<="))  ret = "(" + ret + " <= " + res + ")";
      else if (optor.equals("&"))   ret = "(" + ret + " & " + res + ")";
      else if (optor.equals("&&"))  ret = "(" + ret + " && " + res + ")";
      else if (optor.equals("|"))   ret = "(" + ret + " | " + res + ")";
      else if (optor.equals("||"))  ret = "(" + ret + " || " + res + ")";
      else if (optor.equals("~"))   ret = "(" + ret + " ~ " + res + ")";
      else if (optor.equals("°"))   ret = "(" + ret + " ~ " + res + ")";
      else if (optor.equals("^"))   ret = "(" + ret + " ° " + res + ")";
      else if (optor.equals("***")) ret = "((" + ret + " * " + ret + ") * " + res + ")";    //just to demonstrate that any thinkable operation can be implemented ....
      else ret = ret;
     }
    }
   }
   return ret;
  }


  private double not(double x) { return (Math.Abs(x) < 0.0000000000001) ? 1 : 0; }

  private double parseDouble(Reach term)
  {
   if (term.StartsWith(".e")) return 0;
   if (term.endsWith(true, ".,")) term = term + new Reach("0");
   return useComma ? Double.Parse(term.text.Replace(".", ",")) : Double.Parse(term.text.Replace(",", "."));
  }

  private double eval(Reach term, KeyPile<string, Reach> values)
  {
   term = term.Trim();
   if (term.startsWith("+")) return eval(term.from(2), values);
   if (term.startsWith("-")) return -eval(term.from(2), values);
   if (term.startsWith("!")) return not(eval(term.from(2), values));
   if (term.startsWith("«")) return eval(Int32.Parse(term.from(2).before(1, utl.dmyBool("al(occur, token) is planned"), "»").text), values);
   if (term.startsWith(true, numCharacters)) return parseDouble(term);
   if (term.at(1, utl.dmyBool("al(occur, token) is planned"), "«").len == 0) return parseDouble(values[term.text]);
   Reach function = term.XTR().before(1, utl.dmyBool("al(occur, token) is planned"), "«");
   double ret = eval(Int32.Parse(term.from(2).before(1, utl.dmyBool("al(occur, token) is planned"), "»").text), values);
   if (function.equals("abs")) return Math.Abs(ret);
   if (function.equals("sign")) return Math.Sign(ret);
   if (function.equals("round")) return Math.Round(ret);
   if (function.equals("ceiling")) return Math.Ceiling(ret);
   if (function.equals("floor")) return Math.Floor(ret);
   if (function.equals("sqrt")) return Math.Sqrt(ret);
   if (function.equals("exp")) return Math.Exp(ret);
   if (function.equals("log")) return Math.Log(ret);
   if (function.equals("sin")) return Math.Sin(ret);
   if (function.equals("asin")) return Math.Asin(ret);
   if (function.equals("cos")) return Math.Cos(ret);
   if (function.equals("acos")) return Math.Acos(ret);
   if (function.equals("tan")) return Math.Tan(ret);
   if (function.equals("atan")) return Math.Atan(ret);
   if (function.equals("sinh")) return Math.Sinh(ret);
   if (function.equals("cosh")) return Math.Cosh(ret);
   if (function.equals("tanh")) return Math.Tanh(ret);
   return 0;
  }

  private double eval(int inx, KeyPile<string, Reach> values)
  {
   Pile<Reach> term = detailled(terms[inx]).split("\r\n");
   double ret = eval(term[1], values);
   Reach optor = "";
   Reach lastOptor = "";
   for (int i = 2; i <= term.Len; i++)
   {
    if ((i % 2) == 0)
    {
     optor = term[i];
    } else
    {
     if (term[i].len == 0)
     {
      lastOptor = lastOptor + optor;
     } else
     {
      if (lastOptor.len > 0) { optor = lastOptor + optor; lastOptor = ""; }
      double res = eval(term[i], values);
      if (optor.equals("+")) ret = ret + res;
      else if (optor.equals("-")) ret = ret - res;
      else if (optor.equals("*")) ret = ret * res;
      else if (optor.equals("/")) ret = ret / res;
      else if (optor.equals("\\")) ret = (long)(ret / res);
      else if (optor.equals("%")) ret = ret % res;
      else if (optor.equals("°")) ret = Math.Pow(ret, res);
      else if (optor.equals("=")) if (Math.Abs(ret - res) < 0.00000000001) ret = 1; else ret = 0;
      else if (optor.equals(">")) if (ret > res) ret = 1; else ret = 0;
      else if (optor.equals(">=")) if (ret >= res) ret = 1; else ret = 0;
      else if (optor.equals("<")) if (ret < res) ret = 1; else ret = 0;
      else if (optor.equals("<=")) if (ret <= res) ret = 1; else ret = 0;
      else if (optor.equals("&")) if ((not(not(ret)) == 1) && (not(not(res)) == 1)) ret = 1; else ret = 0;
      else if (optor.equals("|")) if ((not(not(ret)) == 1) || (not(not(res)) == 1)) ret = 1; else ret = 0;
      else if (optor.equals("^")) if ((not(not(ret)) == 1) ^ (not(not(res)) == 1)) ret = 1; else ret = 0;
      else if (optor.equals("***")) ret = (ret * ret) * res;   //just to demonstrate that any thinkable operation can be implemented ....
      else ret = ret;
     }
    }
   }
   return ret;
  }



  private static void selfTest()
  {
   selfTested = true;
   ass(new Formula("1e+2*1e-2").eval() == 1);
   ass(new Formula("1e2").eval() == 100);
   ass(new Formula("-./2").eval() == 0);
   ass(new Formula(".-2").eval() == -2);
   ass(new Formula(".e7/,5E5").eval() == 0);
   ass(new Formula(".5e7/-.5E5").eval() == -100);
   ass(new Formula("1e7/1e5").eval() == 100);
   ass(new Formula("3°2").eval() == 9);
   ass(new Formula("(3<2)^(5>4)").eval() == 1);
   ass(new Formula("3>2").eval() == 1);
   ass(new Formula("5<=2").eval() == 0);
   ass(new Formula("2+3* *  *5").eval() == 47);  //AttGetr: The operator "***" sqares the first and multiplies the result with the second. this operator is defined for testing and debugging purposes only
   ass(new Formula("!!3.14159").eval() == 1);
   ass(new Formula("!!0/12").eval() == 0);
   //ass(new Formula("2 - 1 1/2").eval() == .5);
   ass(Math.Abs(new Formula("0,5+sin(45*asin(1)/90)°2").eval() -1) < 0.0000001);
   KeyPile<string, Reach> values = new KeyPile<string, Reach>();
   values.Add("num", "4");
   values.Add("x", "-1");
   values.Add("n", "3");
   //ass(Math.Abs(new Formula(" (((2 * num --sin(-.7x)) + 2°3°-(1/2)- 2n + 1 5/6) +--1/3n * 2 1/3 * - 3 1/2 + 5(+2n+1))/-3").eval(-1, values) + 10.5548125814992) < 0.0000000001);
  }

 
  private void init(Reach expression)
  {
   try { Double.Parse(".1"); } catch (Exception ex) { useComma = true; }
   if (!selfTested) selfTest();
   
   splitFormula(expression);
  }

  public Formula(Reach expression)
  {
   init(expression);
  }

  public Formula(Reach expression, KeyPile<string, Reach> defaultValues)
  {
   this.defaultValues = defaultValues;
   init(expression);
  }

  public double eval   (KeyPile<string, Reach> values) { return eval(-1, values); }
  public string format (KeyPile<string, Reach> values) { return format(-1, values); }
  public double eval   (                             ) { return eval(-1, defaultValues); }
  public string format (                             ) { return format(-1, defaultValues); }


 }
 
 
}

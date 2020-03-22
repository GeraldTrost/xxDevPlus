/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.xxdevplus.frmlng;

import org.xxdevplus.chain.Chain;
import org.xxdevplus.struct.KeyPile;
import org.xxdevplus.struct.Pile;
import org.xxdevplus.utl.utl;

/**
 *
 * @author GeTr
 */

public class Formula
{

 private static boolean selfTested = false;
 private static boolean useComma = false;
 private static String namesCharacters = ",.0123456789«_abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
 private static String numCharacters = ",.0123456789";
 private static String signCharacters = "+-!";
 private static String operatorCharacters = "&|^><=°\\%/*+-";


 private Pile<Chain> terms = new Pile<Chain>(0);
 private Zone braces = new Zone(new Pile<String>("", 0, "\r", "\n"), new Pile<String>("", 0, "(", ")"));
 public  KeyPile<String, Chain> defaultValues = new KeyPile<String, Chain>();


  private Pile<Chain> splitBraces(Chain formula) throws Exception
  {
   while (formula.at(1, "(").len() > 0)
   {
    Chain part = braces.upon(formula);
    Chain lastPart = formula;
    while (part.startsWith("("))
    {
     lastPart = part;
     part = braces.upon(part.from(2));
    }
    if (lastPart.startsWith("(")) terms.Add(braces.on(lastPart).Trim()); else terms.Add(lastPart.Trim());

    formula = new Chain(""); //AttGetr some Chain Impl do not support destructive Operators
    //formula = formula.RPW("«" + terms.Len() + "»").at(lastPart);
    
   }
   if (formula.startsWith("(")) terms.Add(braces.on(formula).Trim()); else terms.Add(formula.Trim());
   return terms;
  }

  private String splitAtOperators(String operators, Chain term) throws Exception
  {
   Chain newTerm = term.upto(0);
   String ret = "";
   term  = term.Trim();
   while (term.len() > 0)
   {
    Chain sign = term.upto(1); sign = sign.upto(-1, true, signCharacters); if (sign.len() > 0) sign = term.before(1, false, signCharacters);
    term = term.after(sign).Trim();
    Chain num = term.upto(1, true, namesCharacters).Trim();
    if (num.startsWith("«")) num = term.upto(1, "»");
    else
    {
     if (num.upto(-1, true, numCharacters).len() == 0) num = term.before(1, false, namesCharacters);
     else
     {
      num = term.before(1, false, numCharacters);
      if (term.after(num).StartsWith("e"))
      {
       num = num.plus(term.after(num).upto(1));
       while (term.after(num).startsWith(true, "+-")) num = num.plus(term.after(num).upto(1));
       num = num.plus(term.after(num).before(1, false, numCharacters));
      }
     }
     if (num.before(-1, "«").len() > 0) { sign = sign.plus(num.before(-1, "«")); term = term.after(sign).Trim(); num = term.upto(1, "»").Trim(); }
    }
    term = term.after(num).Trim();
    Chain optor = term.upto(1).upto(-1, true, operatorCharacters);
    term = term.after(optor).Trim();
    if (term.len() == 0) optor = optor.upto(0);

    if (optor.upto(-1, true, operators).len() > 0) newTerm = newTerm.plus(sign).plus(num).plus(optor); else { if (newTerm.len() > 0) { terms.Add(newTerm.plus(sign).plus(num)); newTerm = term.upto(0); ret += "«" + (terms.Len()) + "»"; } else ret += (sign.plus(num)).text(); if (optor.len() > 0) ret += optor.text(); else optor = new Chain(""); }
   }
   return ret;
  }

  private Pile<Chain> splitFormula(Chain formula) throws Exception
  {
   splitBraces(formula);

   Pile<Chain> Terms = new Pile<Chain>(0);
   int closePending = 0;
   boolean addClose = false;
   int termsCount = terms.Len();
   for (int i = 1; i <= termsCount; i++)
   {
    Chain term = terms.g(i).Trim();
    Terms.Add(term.upto(0));
    while (term.len() > 0)
    {
     Chain sign = term.upto(1); sign = sign.upto(-1, true, signCharacters); if (sign.len() > 0) sign = term.before(1, false, signCharacters);
     term = term.after(sign).Trim();
     Chain num = term.upto(1, true, namesCharacters).Trim();
     addClose = false;
     if (num.startsWith("«")) num = term.upto(1, "»").Trim();
     else
     {
      if (num.upto(-1, true, numCharacters).len() == 0) num = term.before(1, false, namesCharacters);
      else
      {
       num = term.before(1, false, numCharacters);
       if (term.after(num).StartsWith("e"))
       {
        num = num.plus(term.after(num).upto(1));
        while (term.after(num).startsWith(true, "+-")) num = num.plus(term.after(num).upto(1));
        num = num.plus(term.after(num).before(1, false, numCharacters));
       }
      }
      if (num.before(-1, "«").len() > 0) { sign = sign.plus(num.before(-1, "«")); term = term.after(sign).Trim(); num = term.upto(1, "»").Trim(); }
      if (closePending > 0) { closePending--; if (closePending == 0) addClose = true; }
     }
     term = term.after(num).Trim();
     Chain optor = term.upto(1).upto(-1, true, operatorCharacters);
     term = term.after(optor).Trim();
     if (optor.len() == 0) if (term.startsWith(true, numCharacters))
      {
       optor = sign;
       if (optor.len() == 0) optor = optor.plus("+");
       sign = new Chain("(").plus(sign);
       closePending = 2;
      } else
      {
       optor = optor.plus("*");
      }
     if (addClose) num = num.plus(")");
     Terms.s(Terms.g(-1).plus((sign.plus(num))), -1);
     if (term.len() > 0) Terms.s(Terms.g(-1).plus(optor.text()), -1); // attention: if using the same sign reach twice within Term[-1] some following Chain Subrtactions will create unexpected results!
    }
    if (Terms.g(-1).at(1, "(").len() > 0) Terms.s(splitBraces(Terms.g(-1)).g(-1), -1);
   }
   for (int i = 1; i <= Terms.Len(); i++) terms.s(Terms.g(i), i);
   terms.Add(terms.g(termsCount));

   termsCount = terms.Len();
   for (int i = 1; i <= termsCount; i++) terms.s(new Chain(splitAtOperators("&", terms.g(i))), i);
   terms.Add(terms.g(termsCount));

   termsCount = terms.Len();
   for (int i = 1; i <= termsCount; i++) terms.s(new Chain(splitAtOperators("|^", terms.g(i))), i);
   terms.Add(terms.g(termsCount));

   termsCount = terms.Len();
   for (int i = 1; i <= termsCount; i++) terms.s(new Chain(splitAtOperators("><=", terms.g(i))), i);
   terms.Add(terms.g(termsCount));

   termsCount = terms.Len();
   for (int i = 1; i <= termsCount; i++) terms.s(new Chain(splitAtOperators("°", terms.g(i))), i);
   terms.Add(terms.g(termsCount));

   termsCount = terms.Len();
   for (int i = 1; i <= termsCount; i++) terms.s(new Chain(splitAtOperators("*/%\\", terms.g(i))), i);
   terms.Add(terms.g(termsCount));

   return terms;
  }


  private Chain detailled(Chain term) throws Exception
  {
   String ret = "";
   term  = term.Trim();
   while (term.len() > 0)
   {
    Chain sign = term.upto(1); sign = sign.upto(-1, true, signCharacters); if (sign.len() > 0) sign = term.before(1, false, signCharacters);
    term = term.after(sign).Trim();
    Chain num = term.upto(1, true, namesCharacters).Trim();
    if (num.startsWith("«")) num = term.upto(1, "»");
    else
    {
     if (num.upto(-1, true, numCharacters).len() == 0) num = term.before(1, false, namesCharacters);
     else
     {
      num = term.before(1, false, numCharacters);
      if (term.after(num).StartsWith("e"))
      {
       num = num.plus(term.after(num).upto(1));
       while (term.after(num).startsWith(true, "+-")) num = num.plus(term.after(num).upto(1));
       num = num.plus(term.after(num).before(1, false, numCharacters));
      }
     }
     if (num.before(-1, "«").len() > 0) { sign = sign.plus(num.before(-1, "«")); term = term.after(sign).Trim(); num = term.upto(1, "»").Trim(); }
    }
    term = term.after(num).Trim();
    Chain optor = term.upto(1).upto(-1, true, operatorCharacters);
    term = term.after(optor).Trim();
    ret += "\r\n" + (sign.plus(num)).text();
    if (term.len() > 0) ret += "\r\n" + optor.text(); else optor = new Chain("");
   }
   return new Chain(ret.substring(2));
  }

  private double eval(int inx, KeyPile<String, Chain> values) throws Exception
  {
   Pile<Chain> term = detailled(terms.g(inx)).split("\r\n");
   double ret = eval(term.g(1), values);
   Chain optor = new Chain("");
   Chain lastOptor = new Chain("");
   for (int i = 2; i <= term.Len(); i++)
   {
    if ((i % 2) == 0)
    {
     optor = term.g(i);
    } else
    {
     if (term.g(i).len() == 0)
     {
      lastOptor = lastOptor.plus(optor);
     }
     else
     {
      if (lastOptor.len() > 0) { optor = lastOptor.plus(optor); lastOptor = new Chain(""); }
      double res = eval(term.g(i), values);
      if (optor.equals("+")) ret = ret + res;
      else if (optor.equals("-")) ret = ret - res;
      else if (optor.equals("*")) ret = ret * res;
      else if (optor.equals("/")) ret = ret / res;
      else if (optor.equals("\\")) ret = (int)(ret / res);
      else if (optor.equals("%")) ret = ret % res;
      else if (optor.equals("°")) ret = Math.pow(ret, res);
      else if (optor.equals("=")) if (Math.abs(ret - res) < 0.00000000001) ret = 1; else ret = 0;
      else if (optor.equals(">")) if (ret > res) ret = 1; else ret = 0;
      else if (optor.equals(">=")) if (ret >= res) ret = 1; else ret = 0;
      else if (optor.equals("<")) if (ret < res) ret = 1; else ret = 0;
      else if (optor.equals("<=")) if (ret <= res) ret = 1; else ret = 0;
      else if (optor.equals("&")) if ((not(not(ret)) == 1) && (not(not(res)) == 1)) ret = 1; else ret = 0;
      else if (optor.equals("|")) if ((not(not(ret)) == 1) || (not(not(res)) == 1)) ret = 1; else ret = 0;
      else if (optor.equals("^")) if ((not(not(ret)) == 1) ^ (not(not(res)) == 1)) ret = 1; else ret = 0;
      else if (optor.equals("***")) ret = (ret * ret) * res;
      else ret = ret;
     }
    }
   }
   return ret;
  }

  private double not(double x) { return (Math.abs(x) < 0.0000000000001) ? 1 : 0; }

  private double parseDouble(Chain term) throws Exception
  {
   if (term.StartsWith(".e")) return 0;
   if (term.endsWith(true, ".,")) term = term.plus("0");
   return useComma ? Double.parseDouble(term.text().replace(".", ",")) : Double.parseDouble(term.text().replace(",", "."));
  }

  private double eval(Chain term, KeyPile<String, Chain> values) throws Exception
  {
   term = term.Trim();
   if (term.startsWith("+")) return eval(term.from(2), values);
   if (term.startsWith("-")) return -eval(term.from(2), values);
   if (term.startsWith("!")) return not(eval(term.from(2), values));
   if (term.startsWith("«")) return eval(Integer.parseInt(term.from(2).before(1, "»").text()), values);
   if (term.startsWith(true, numCharacters)) return parseDouble(term);
   if (term.at(1, "«").len() == 0) return parseDouble(values.g(term.text()));

   Chain function = new Chain(""); //AttGetr some Chain Impl do not support destructive Operators
   //Chain function = term.XTR().before(1, "«");

   double ret = eval(Integer.parseInt(term.from(2).before(1, "»").text()), values);
   if (function.equals("abs")) return Math.abs(ret);
   if (function.equals("sign")) return Math.signum(ret);
   if (function.equals("round")) return Math.round(ret);
   if (function.equals("ceiling")) return Math.ceil(ret);
   if (function.equals("floor")) return Math.floor(ret);
   if (function.equals("sqrt")) return Math.sqrt(ret);
   if (function.equals("exp")) return Math.exp(ret);
   if (function.equals("log")) return Math.log(ret);
   if (function.equals("sin")) return Math.sin(ret);
   if (function.equals("asin")) return Math.asin(ret);
   if (function.equals("cos")) return Math.cos(ret);
   if (function.equals("acos")) return Math.acos(ret);
   if (function.equals("tan")) return Math.tan(ret);
   if (function.equals("atan")) return Math.atan(ret);
   if (function.equals("sinh")) return Math.sinh(ret);
   if (function.equals("cosh")) return Math.cosh(ret);
   if (function.equals("tanh")) return Math.tanh(ret);
   return 0;
  }



  private void ass(boolean  expr) throws Exception { if (!expr) throw new Exception("Formula SelfText Failure"); }
  private void selftest() throws Exception
  {
   selfTested = true;
   ass(new Formula(new Chain("1e+2*1e-2")).eval() == 1);
   ass(new Formula(new Chain("1e2")).eval() == 100);
   ass(new Formula(new Chain("-./2")).eval() == 0);
   ass(new Formula(new Chain(".-2")).eval() == -2);
   ass(new Formula(new Chain(".e7/,5E5")).eval() == 0);
   ass(new Formula(new Chain(".5e7/-.5E5")).eval() == -100);
   ass(new Formula(new Chain("1e7/1e5")).eval() == 100);
   ass(new Formula(new Chain("3°2")).eval() == 9);
   ass(new Formula(new Chain("(3<2)^(5>4)")).eval() == 1);
   ass(new Formula(new Chain("3>2")).eval() == 1);
   ass(new Formula(new Chain("5<=2")).eval() == 0);
   ass(new Formula(new Chain("2+3* *  *5")).eval() == 47);  //AttGetr: The operator "***" sqares the first and multiplies the result with the second. this operator is defined for testing and debugging purposes only
   ass(new Formula(new Chain("!!3.14159")).eval() == 1);
   ass(new Formula(new Chain("!!0/12")).eval() == 0);
   ass(new Formula(new Chain("2 - 1 1/2")).eval() == .5);
   ass(Math.abs(new Formula(new Chain("0,5+sin(45*asin(1)/90)°2")).eval() -1) < 0.0000001);
   KeyPile<String, Chain> values = new KeyPile<String, Chain>();
   values.Add("num", new Chain("4"));
   values.Add("x", new Chain("-1"));
   values.Add("n", new Chain("3"));
   ass(Math.abs(new Formula(new Chain(" (((2 * num --sin(-.7x)) + 2°3°-(1/2)- 2n + 1 5/6) +--1/3n * 2 1/3 * - 3 1/2 + 5(+2n+1))/-3")).eval(-1, values) + 10.5548125814992) < 0.0000000001);
  }


  private void init(Chain expression) throws Exception
  {
   try { Double.parseDouble(".1"); } catch (Exception ex) { useComma = true; }
   if (!selfTested) selftest();
   splitFormula(expression);
  }

  public Formula(Chain expression) throws Exception
  {
   init(expression);
  }

  public Formula(Chain expression, KeyPile<String, Chain> defaultValues) throws Exception
  {
   this.defaultValues = defaultValues;
   init(expression);
  }

  public double eval(KeyPile<String, Chain> values) throws Exception
  {
   return eval(-1, values);
  }

  public double eval() throws Exception
  {
   return eval(-1, defaultValues);
  }


 }



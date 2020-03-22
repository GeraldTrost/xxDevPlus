using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using ndBase;
using ndString;


namespace ndData
{
 public class Trm : Xpn<object>
 {
  //Copyright© 1996-2012 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties, You may use, modify, distribute this code under GNU public licence for NON-Commercial use as long as this notice remains unchanged.
  private static bool selfTested  = false;  private static string ass(bool expr) { if (!expr) throw new Exception(ass(true) + " SelfTest Failure"); return "Trm"; }


  private static Pile<string> ops = new Pile<string>("ops", true, "\"", "'", "(", "[", "{", ",");


  private string optorForSymbolicOptor(string optor)
  {
   foreach (string key in Dtv.Keys) if (new Reach(Dtv[key]).StartsWith(optor + "``")) return key;
   return " <n/a> ";
  }

  private Pile<string> operators = null;

  private void splitparts()
  {
   if (operators == null) { operators = ops.Clone(); foreach (string key in Dtv.Keys) operators.Push(new Reach(Dtv[key]).before(1, "``")); }
   int inx = 1;
   while (inx <= Len)
   {
    Reach rest = null;
    object lOp = null;
    object rOp = null;
    object tOp = null;
    //if (this[inx].GetType() ==  typeof(string)) rest = new Reach((string)this[inx]).Trim();   //AttGeTr SEVERE BUGS HERE!!  the middle chars of "word" will be detected as Operator "or" !
    if (rest == null) inx++;
    else
    {
     Reach firstOptorSmb = rest.At(1, false, operators.array()).Trim(); 
     if (firstOptorSmb.At(1, "(").len == 0) if (operators.Contains((firstOptorSmb + "(").uText)) if (rest.after(firstOptorSmb).startsWith("(")) firstOptorSmb = firstOptorSmb + rest.after(firstOptorSmb).upto(1); // both the infix OR and the prefix OR( may be defined as directive
     String firstOptor = optorForSymbolicOptor(firstOptorSmb.text); 
     if (rest.startsWith(firstOptorSmb))
     {
     }
     else
     { 
     }

     if (firstOptorSmb.len == 0) 
     {
      Reach fieldVal = new Reach((string)this[inx]).Trim();
      this[inx] = new Reach((string)this[inx]); 
      rest = ""; 

     } // this term has no operand, it is a literal like 3 or a Symbol like a DbField Name or a composite field definition

     else
     {
      if (rest.startsWith(firstOptorSmb))
      {
       if (firstOptorSmb.Equals("(")) { this[inx] = new Trm(Zone.bracelet.on(rest).text); optor = ""; rest = rest.after(Zone.bracelet.upon(rest)).Trim(); }
       else 
       {
        string newOptor = firstOptorSmb.Trim().text.Replace("(", "");
        bool prefix = (firstOptorSmb.at(1, "(").len > 0);
        if (prefix) 
        {
         this[inx] = new Trm(Zone.bracelet.on(rest.after(1, optor).Trim()).text); rest = rest.after(Zone.bracelet.upon(rest.after(1, optor))).Trim(); 
        }
        else
        {
         rest = "";
         if (optor.Length > 0) 
         { 
          this[1] = new Trm(optor, this[1]); 
          for (int i = 2; i < inx; i++) ((Trm)this[1]).Push(this[i]); 
          for (int i = 2; i <= Len - (inx - 2); i++) this[i] = this[i + (inx - 2)]; 
          for (int i = 1; i <= (inx - 2); i++) Del(Len); 
          inx = 2; 
         }
         this[inx] = new Trm(rest.after(1, optor).Trim().text); rest = ""; 
        }
        if ((((Trm)this[inx]).Len == 1 && ((Trm)this[inx]).optor.Length == 0)) this[inx] = ((Trm)this[inx]).lOpnd;
        optor = optorForSymbolicOptor(firstOptorSmb.Trim().text); 
       }
      }
      else
      {
       if ((Len == 1) && (optor.Length == 0)) { optor = optorForSymbolicOptor(firstOptorSmb.Trim().text); this[inx] = rest.before(firstOptorSmb).Trim(); Push(rest.after(firstOptorSmb).Trim().text); }
       else this[inx] = new Trm(optorForSymbolicOptor(firstOptorSmb.Trim().text), new object[] { rest.before(firstOptorSmb).Trim(), rest.after(firstOptorSmb).Trim().text });
       rest = "";
      }
     }
     if (rest.len > 0) Push(rest.text);
     rest = "";
     inx++;
    }
   }
  }

  
  private void init() 
  {

   if (!selfTested) selfTest();
   if (Len == 0) return;
   splitparts(); //maybe one Operand is a formula that must be split into a network of operators and Trms

  }

  private static void selfTest()
  {
   selfTested = true;
   _dtv.Set(" _ ", "APD``¹``>¹²``, ²``");                 // we could define AP rather than APD for the append operator, this Dtv is only a sample of how could it be done ...
   _dtv.Set(" & ", "CCT``¹``>´°(¹´, ²´)````");            // we could define CONCAT rather than CCT for the append operator, this Dtv is only a sample of how could it be done ... 
   _dtv.Set(" >$ ", "UPPER``°(¹)``>°(´¹²)´``, ²``");       // Convert To String this Dtv is only a sample of how could it be done ... 

   _dtv.Set(" + ", "+``¹``>(´¹ ²)´`` ° ²``");             // +
   _dtv.Set(" * ", "*``¹``>(´¹ ²)´`` ° ²``");             // *
   _dtv.Set(" - ", "-``¹``>(´¹ ²)´`` ° ²``");             // -
   _dtv.Set(" / ", "/``¹``>(´¹ ²)´`` ° ²``");             // /
   _dtv.Set(" sin ", "sin``¹``>(´¹ ²)´`` ° ²``");           // sin
   _dtv.Set(" cos ", "sin``¹``>(´¹ ²)´`` ° ²``");           // cos
   _dtv.Set(" tan ", "sin``¹``>(´¹ ²)´`` ° ²``");           // tan
   _dtv.Set(" cot ", "sin``¹``>(´¹ ²)´`` ° ²``");           // cot
   _dtv.Set(" ln ", "ln``¹``>(´¹ ²)´`` ° ²``");            // ln
   _dtv.Set(" log ", "log``¹``>(´¹ ²)´`` ° ²``");           // log
   _dtv.Set(" exp ", "exp``¹``>(´¹ ²)´`` ° ²``");           // exp

   _dtv.Set(" sin ", "sin````<°(´¹´, ²)´````");

   _dtv.Del(" && ");                                        // Dtv.Set(" && " , "AN``¹``>(´¹ ²)´`` ° ²````");        // AND is not used within Terms
   _dtv.Del(" || ");                                        // Dtv.Set(" || " , "OR``¹``>(´¹ ²)´`` ° ²``");          // OR  is not used within Terms
   _dtv.Del(" ^^ ");                                        // Dtv.Set(" ^^ " , "XR``¹``>(´¹ ²)´`` ° ²``");          // XOR is not used within Terms 
   _dtv.Del(" °° ");                                        // Dtv.Set(" °° " , "XS``¹``>(´¹ ²)´`` ° ²``");          // EXIST is not used within Terms
   _dtv.Del(" !°° ");                                       // Dtv.Set(" !°° ", "NX``¹``>(´¹ ²)´`` ° ²``");          // NOT EXIST is not used within Terms
   _dtv.Del(" == ");                                        // Dtv.Set(" == " , "IS``¹``>(´¹ ²)´`` ° ²``");          // IS is not used within Terms
   _dtv.Del(" !== ");                                       // Dtv.Set(" !== ", "NS``¹``>(´¹ ²)´`` ° ²``");          // NOT IS is not used within Terms
   _dtv.Del(" = ");                                         // Dtv.Set(" = "  , "EQ``¹``>(´¹ ²)´`` ° ²``");          // EQUALS is not used within Terms
   _dtv.Del(" != ");                                        // Dtv.Set(" != " , "NE``¹``>(´¹ ²)´`` ° ²``");          // NOT EQUALS is not used within Terms
   _dtv.Del(" > ");                                         // Dtv.Set(" > "  , "GT``¹``>(´¹ ²)´`` ° ²``");          // GREATER THAN is not used within Terms
   _dtv.Del(" < ");                                         // Dtv.Set(" < "  , "LT``¹``>(´¹ ²)´`` ° ²``");          // LESS THAN is not used within Terms
   _dtv.Del(" >= ");                                        // Dtv.Set(" >= " , "GE``¹``>(´¹ ²)´`` ° ²``");          // GREATER OR EQUAL is not used within Terms
   _dtv.Del(" <= ");                                        // Dtv.Set(" <= " , "LE``¹``>(´¹ ²)´`` ° ²``");          // LESS OR EQUAL is not used within Terms
   _dtv.Del(" ~ ");                                         // Dtv.Set(" ~ "  , "LK``¹``>(´¹ ²)´`` ° ²``");          // LIKE is not used within Terms
   _dtv.Del(" !~ ");                                        // Dtv.Set(" !~ " , "NL``¹``>(´¹ ²)´`` ° ²``");          // NOT LIKE is not used within Terms
   _dtv.Del(" °  ");                                        // Dtv.Set(" ° "  , "IN``¹``>(´¹ ²)´`` ° ²``");          // IN is not used within Terms
   _dtv.Del(" !° ");                                        // Dtv.Set(" !° " , "NI``¹``>(´¹ ²)´`` ° ²``");          // NOT IN is not used within Terms
   _dtv.Del(" ≡ ");                                         // Dtv.Set(" ≡ "  , "MV``¹``>(´¹ ²)´`` ° ²``");          // MATCHES VALUES is not used within Terms
   _dtv.Del(" !≡ ");                                        // Dtv.Set(" !≡ " , "NV``¹``>(´¹ ²)´`` ° ²``");          // NOT MATCHES VALUES is not used within Terms
   _dtv.Del(" ≡> ");                                        // Dtv.Set(" ≡> " , "MU``¹``>(´¹ ²)´`` ° ²``");          // MATCHES UPPERCASE VALUES is not used within Terms
   _dtv.Del(" !≡> ");                                       // Dtv.Set(" !≡> ", "NU``¹``>(´¹ ²)´`` ° ²``");          // NOT MATCHES UPPERCASE VALUES is not used within Terms
   _dtv.Del(" ≡< ");                                        // Dtv.Set(" ≡< " , "ML``¹``>(´¹ ²)´`` ° ²``");          // MATCHES LOWERCASE VALUES is not used within Terms
   _dtv.Del(" !≡< ");                                       // Dtv.Set(" !≡< ", "NL``¹``>(´¹ ²)´`` ° ²``");          // NOT MATCHES LOWERCASE VALUES is not used within Terms
   _dtv.Del(" <|> ");                                       // Dtv.Set(" <|> ", "UNION``¹``>(´¹´²)´``) ° (²``");     // UNION is not used within Terms
   _dtv.Del(" <&> ");                                       // Dtv.Set(" <&> ", "INTERSECT``¹``>(´¹)´ ° ²``(²)``");  // INTERSECT is not used within Terms
   _dtv.Del(" <-> ");                                       // Dtv.Set(" <-> ", "EXCEPT``¹``>(´¹)´ ° ²``(²)``");     // EXCEPT is not used within Terms

   _dtv.Set("   ", " + ' ' + ``¹``>¹²``°²");           //composite field operator
   _dtv.Set(" |. ", "lTrim``°(¹)``>°(´¹ ²)´``");       // lTrim
   _dtv.Set(" .| ", "rTrim``°(¹)``>°(´¹ ²)´``");       // rTrim
   _dtv.Set(" |.| ", "trim``°(¹)``>°(´¹ ²)´``");       // trim

   _dtv.Set(" , ", ",``¹``>¹²``, ²");
   _dtv.Set(" \" ", "\"``°¹°``>°¹²°`` ²");
   _dtv.Set(" ' ", "'``°¹°``>°¹²°`` ²");
   _dtv.Set(" ( ", "(``(¹)``>(¹²)``, ²");
   _dtv.Set(" + ", "+``¹``>´¹ ²´`` ° ²``");
   _dtv.Set(" || ", "OR``¹``>´¹ ²´`` ° ²``");
   _dtv.Set(" ||( ", "OR(``¹``>°(´¹ ²)´``, ²``");

   ass(new Trm("abc").sql().equals("abc"));
   ass(new Trm((Reach)"x").cct("y").sql().equals("CCT(x, y)"));
   ass(new Trm((Reach)"name").cct(1).sql().equals("CCT(name, '1')"));
   ass(new Trm((Reach)"name").cct(1).apd("id").sql().equals("CCT(name, '1'), id"));
   ass(new Trm((Reach)"nm").apd("id").apd(new Trm((Reach)"idnm").cct("'XXX'")).apd("0.5").sql().equals("nm, id, CCT(idnm, 'XXX'), 0.5")); // ass(b._sql().equals("nm, id, idnm + 'XXX', 0.5"));  //ass(b._sql().equals("nm, id, idnm || 'XXX', 0.5")); 
   ass(new Trm((Reach)"nm").apd("id").apd(new Trm((Reach)"idnm").cct("'XXX'")).apd(0.5).sql().equals("nm, id, CCT(idnm, 'XXX'), 0.5")); // ass(b._sql().equals("nm, id, idnm + 'XXX', 0.5"));  //ass(b._sql().equals("nm, id, idnm || 'XXX', 0.5")); 

  }

  public Trm Clone() { Trm ret = new Trm(); ret.optor = optor; ret.Add(this); return ret; } //the super-super class Pile already provides Clone but its result cannot be casted to (Trm) in c#

  public  Trm (Trm     lOpnd                                ) : base("",                      lOpnd) { init(); }
  public  Trm (string  lOpnd                                ) : base("",                      lOpnd) { init(); } //only for jave, c# does implicit type-conversion to Reach
  public  Trm (Reach   lOpnd                                ) : base("",                      lOpnd) { init(); }
  public  Trm (object  lOpnd, String cptor,     object rOpnd) : base(cptor,            lOpnd, rOpnd) { init(); } //only for jave, c# does implicit type-conversion to Reach
  public  Trm (object  lOpnd, Reach cptor,      object rOpnd) : base(cptor.text,       lOpnd, rOpnd) { init(); }
  public  Trm (                                             ) : base(                              ) { init(); }
  public  Trm (long count                                   ) : base(count                         ) { init(); }
  //public  Trm (params object[] opnd                         ) : base(opnd                          ) { init(); }
  public  Trm (Pile<object> opnd                            ) : base(opnd                          ) { init(); }
  public  Trm (string optor, params object[] opnd           ) : base(optor,                    opnd) { init(); }
  public  Trm (string optor, Pile<object> opnd              ) : base(optor,                    opnd) { init(); }

  public override string val(int i, EvalExpert evx)
  {
   Type typ = this[i].GetType();
   if (false && (dbg))
   {
    if (typ == typeof(DbField)) return "[" + ((DbField)(this[i])).val(evx) + "]";
    if (typ == typeof(Trm))     return "[" + ((Trm)(this[i])).val(evx) + "]";
    if (typ == typeof(Reach))   return "[" + ((Reach)(this[i])).text + "]";
    if (typ == typeof(string))  return "[" + ((string)(this[i])) + "]";
    if (typ == typeof(long))    return "[" + ((Db)evx).ds("" + ((long)(this[i]))) + "]";
    if (typ == typeof(double))  return "[" + ((Db)evx).ds("" + ((double)(this[i]))).Replace(",", ".") + "]";
    throw new Exception("Err: unknown Type in Trm.val()");
   }
   else
   { 
    if (typ == typeof(DbField)) return ((DbField)(this[i])).val(evx);
    if (typ == typeof(Trm))     return ((Trm)(this[i])).val(evx);
    if (typ == typeof(Reach))   return ((Reach)(this[i])).text;
    if (typ == typeof(string))  return ((string)(this[i]));
    if (typ == typeof(long))    return ((Db)evx).ds("" + ((long)(this[i])));
    if (typ == typeof(double))  return ((Db)evx).ds("" + ((double)(this[i]))).Replace(",", ".");
    throw new Exception("Err: unknown Type in Trm.val()");
   }
  }

  public static Pile<Trm> fromTxtDef(string def)
  {
   //AttGeTr TBD TO BE DONE !!!
   return new Pile<Trm>();
  }

  public static Pile<Trm> fromSymbolicDef(Reach def)
  {
   return new Pile<Trm>();
  }

  public virtual Trm cvs()             { if (optor.Length > 0) return new Trm(this).cvs();                                                              optor = utl.OPR("CVS"); return this; }
  public virtual Trm cct(string rOpnd) { if (optor.Length > 0) return new Trm(this).cct(rOpnd); this.Push((Reach)rOpnd);                                optor = utl.OPR("CCT"); return this; }
  public virtual Trm cct(Reach  rOpnd) { if (optor.Length > 0) return new Trm(this).cct(rOpnd); this.Push(rOpnd);                                       optor = utl.OPR("CCT"); return this; }
  public virtual Trm cct(Trm    rOpnd) { if (optor.Length > 0) return new Trm(this).cct(rOpnd); this.Push(rOpnd.Clone());                               optor = utl.OPR("CCT"); return this; }
  public virtual Trm cct(long   rOpnd) { if (optor.Length > 0) return new Trm(this).cct(rOpnd); this.Push(utl.ds(false, "" + rOpnd));                   optor = utl.OPR("CCT"); return this; }
  public virtual Trm cct(double rOpnd) { if (optor.Length > 0) return new Trm(this).cct(rOpnd); this.Push(utl.ds(false, "" + rOpnd).Replace(",", ".")); optor = utl.OPR("CCT"); return this; }
  public virtual Trm cct(DbSlc  rOpnd) { if (optor.Length > 0) return new Trm(this).cct(rOpnd); this.Push(rOpnd);                                       optor = utl.OPR("CCT"); return this; }
  public virtual Trm apd(string rOpnd) { if (optor.Length > 0) return new Trm(this).apd(rOpnd); this.Push((Reach)rOpnd);                                optor = utl.OPR("APD"); return this; }
  public virtual Trm apd(Reach  rOpnd) { if (optor.Length > 0) return new Trm(this).apd(rOpnd); this.Push(rOpnd);                                       optor = utl.OPR("APD"); return this; }
  public virtual Trm apd(Trm    rOpnd) { if (optor.Length > 0) return new Trm(this).apd(rOpnd); this.Push(rOpnd.Clone());                               optor = utl.OPR("APD"); return this; }
  public virtual Trm apd(long   rOpnd) { if (optor.Length > 0) return new Trm(this).apd(rOpnd); this.Push((Reach)("" + rOpnd));                         optor = utl.OPR("APD"); return this; }
  public virtual Trm apd(double rOpnd) { if (optor.Length > 0) return new Trm(this).apd(rOpnd); this.Push((Reach)("" + rOpnd).Replace(",", "."));       optor = utl.OPR("APD"); return this; }
  public virtual Trm apd(DbSlc  rOpnd) { if (optor.Length > 0) return new Trm(this).apd(rOpnd); this.Push(rOpnd);                                       optor = utl.OPR("APD"); return this; }

  public Reach sql() { return sql(this); } //return sql(new Db(_o.DbDrivers)); }
  public Reach smb() { return sql(this); } //AttGeTr: must be revised, is sql-Syntax and symbolic Syntax always equal in terms of DbFields or Terms?

  public virtual Reach sql(EvalExpert evx)
  {
   if (evx.Dtv.hasKey(optor)) return val(evx);
   if (Dtv.hasKey(optor))     return val(this);
   if (optor.Equals(""))      return val(this); //(Reach)lOpnd;
   Reach ret = "";
   Reach retClose = "";

   if (lOpnd.GetType() == typeof(Trm)) 
    ret += ((Trm)lOpnd).sql(evx); 
   else 
    if (lOpnd.GetType() == typeof(DbField)) 
     ret += ((DbField)lOpnd).sql(evx); 
    else 
     if (lOpnd.GetType() == typeof(DbSlc)) 
      ret += "( " + evx.val(((DbSlc)lOpnd)) + " )"; 
     else 
      ret += (Reach)lOpnd;
   if      (optor.Equals(utl.OPR("APD")))                  ret += ", ";

    /*
   else if (optor.Equals(utl.OPR("CPF")))                  //composite field
   {
    //return "lTrim(rTrim(" + lOpnd + ")) + ' ' + lTrim(rTrim(" + rOpnd + "))";
    ret = "(";
    foreach (object op in this)
    {
     ret += ((DbField)op).sql(evx) + " + ' ' + ";
     //return ret.upto(-10);
     retClose = ")";
    }
   }
   */

   else if (optor.Equals(utl.OPR("CCT")))                  { ret = "CONCAT(" + ret + ", "; retClose  = ")"; }
   else if (optor.Equals(utl.OPR("CVS")))                  { ret = "CAST(" + ret + " as text"; retClose  = ")"; }

   if (rOpnd != null) 
    if (rOpnd.GetType() == typeof(Trm)) 
     ret += ((Trm)rOpnd).sql(evx); 
    else 
     if (rOpnd.GetType() == typeof(DbField)) 
      ret += ((DbField)rOpnd).sql(evx); 
     else 
      if (rOpnd.GetType() == typeof(DbSlc)) 
        ret += "( " + evx.val(((DbSlc)rOpnd)) + " )"; 
      else ret += (Reach)rOpnd;

   ret += retClose;
   return ret;
  }

  /*
  public Reach _sql()
  {
   if (Dtv.hasKey(optor)) return val(this);
   if (optor.Equals("")) return (Reach)lOpnd;
   Reach ret = "";
   Reach retClose = "";
   if (lOpnd.GetType() == typeof(Trm)) ret += ((Trm)lOpnd)._sql(); else if (lOpnd.GetType() == typeof(DbSlc)) ret += "( " + ((DbSlc)lOpnd).sql() + " )"; else ret += (Reach)lOpnd;
   if      (optor.Equals(utl.OPR("APD")))                  ret += ", ";
   else if (optor.Equals(utl.OPR("CCT")))                  { ret = "CONCAT(" + ret + ", "; retClose  = ")"; }
   else if (optor.Equals(utl.OPR("CVS")))                  { ret = "CAST(" + ret + " as text"; retClose  = ")"; }
   if (rOpnd != null) if (rOpnd.GetType() == typeof(Trm)) ret += ((Trm)rOpnd)._sql(); else if (rOpnd.GetType() == typeof(DbSlc)) ret += "( " + ((DbSlc)rOpnd).sql() + " )"; else ret += (Reach)rOpnd;
   ret += retClose;
   return ret;
  }
  */

  public Reach _smb()
  {
   if (optor.Equals("")) return "";
   Reach ret = "cd(";
   if ((optor.Equals(utl.OPR("OR"))) || (optor.Equals(utl.OPR("AND"))) || (optor.Equals(utl.OPR("XOR")))) ret  = "";
   if (lOpnd.GetType() == typeof(Trm)) ret += ((Trm)lOpnd)._smb(); else if (lOpnd.GetType() == typeof(DbSlc)) ret += "( " + ((DbSlc)lOpnd).smb() + " )"; else if (((Reach)lOpnd).startsWith("'")) ret += "ds(\"" + ((Reach)lOpnd).from(2).upto(-2) + "\")"; else ret += "\"" + (Reach)lOpnd + "\"";
   if      (optor.Equals(utl.OPR("APD")))                  ret += ").APD(";
   else if (optor.Equals(utl.OPR("CCT")))                  ret += ").CCT(";
   else if (optor.Equals(utl.OPR("CVS")))                  ret += ").CVS(";
   if (rOpnd.GetType() == typeof(double)) rOpnd = rOpnd;
   if (rOpnd.GetType() == typeof(float)) rOpnd = rOpnd;
   if (rOpnd.GetType() == typeof(Trm)) ret += ((Trm)rOpnd)._smb(); else if (rOpnd.GetType() == typeof(DbSlc)) ret += "( " + ((DbSlc)rOpnd).smb() + " )"; else if (((Reach)rOpnd).startsWith("'")) ret += "ds(\"" + ((Reach)rOpnd).from(2).upto(-2) + "\")"; else ret += "\""  + (Reach)rOpnd + "\"";
   //if ((optor.equals(utl.OPR("OR"))) || (optor.equals(utl.OPR("AND"))) || (optor.equals(utl.OPR("XOR")))) ret  += ")";
   ret += ")";
   return ret;
  }


 }
}

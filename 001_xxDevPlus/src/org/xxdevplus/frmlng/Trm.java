/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.xxdevplus.frmlng;

import org.xxdevplus.frmlng.EvalExpert;
import org.xxdevplus.struct.Pile;
import org.xxdevplus.frmlng.Xpn;
import org.xxdevplus.utl.utl;
import org.xxdevplus.chain.Chain;
import org.xxdevplus.data.Db;
import org.xxdevplus.data.DbField;
import org.xxdevplus.data.DbSlc;
import org.xxdevplus.frmlng.Zone;

/**
 *
 * @author GeTr
 */
 public class Trm extends Xpn<Object>
 {
  //Copyright© 1996-2020 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties, You may use, modify, distribute this code under GNU public licence for NON-Commercial use as long as this notice remains unchanged.
  //Copyright© 1996-2020 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties, You may use, modify, distribute this code under GNU public licence for NON-Commercial use as long as this notice remains unchanged.
  private static boolean selfTested  = false;  private static String ass(boolean expr) throws Exception { if (!expr) throw new Exception(ass(true) + " SelfTest Failure"); return "Trm"; }


  private static Pile<String> ops = new Pile<String>("ops", 0, "\"", "'", "(", "[", "{", ",");


  private String optorForSymbolicOptor(String optor) throws Exception
  {
   for (String key : Dtv().Keys()) if (new Chain(Dtv().g(key)).startsWith(optor + "``")) return key;
   return " <n/a> ";
  }
  private Pile<String> operators = null;

  private void splitparts() throws Exception
  {
   if (operators == null) { operators = ops.Clone(); for (String key : Dtv().Keys()) operators.Push(new Chain(Dtv().g(key)).before(1, "``").text()); }
   int inx = 1;
   while (inx <= Len())
   {
    Chain rest = null;
    Object lOp = null;
    Object rOp = null;
    Object tOp = null;
    //if (this.g(inx) instanceof String) rest = new Chain((String)this.g(inx)).Trim(); //AttGeTr SEVERE BUGS HERE!!  the middle chars of "word" will be detected as Operator "or" !
    if (rest == null) inx++;
    else
    {

     Chain firstOptorSmb = rest.At(1, false, Db.cloneStringArray(operators)).Trim();
     if (firstOptorSmb.At(1, "(").len() == 0)
      if (operators.Contains(false, (firstOptorSmb.plus("(")).uText()))
       if (rest.after(firstOptorSmb).startsWith("("))
        firstOptorSmb = firstOptorSmb.plus(rest.after(firstOptorSmb).upto(1)); // both the infix OR and the prefix OR( may be defined as directive

     String firstOptor = optorForSymbolicOptor(firstOptorSmb.text());
     if (rest.startsWith(firstOptorSmb.text()))
     {
      //if
     }
     else
     {
     }

     if (firstOptorSmb.len() == 0) { this.s(new Chain((String)this.g(inx)), inx); rest = new Chain(""); } // this term has no operand, it is a literal like 3 or a Symbol like a DbField Name
     else
     {
      if (rest.startsWith(firstOptorSmb.text()))
      {
       if (firstOptorSmb.Equals("(")) { this.s(new Trm(Zone.bracelet.on(rest).text()), inx); optor(""); rest = rest.after(Zone.bracelet.upon(rest)).Trim(); }
       else
       {
        String newOptor = firstOptorSmb.Trim().text().replace("(", "");
        boolean prefix = (firstOptorSmb.at(1, "(").len() > 0);
        if (prefix)
        {
         this.s(new Trm(Zone.bracelet.on(rest.after(1, optor()).Trim()).text()), inx); rest = rest.after(Zone.bracelet.upon(rest.after(1, optor()))).Trim();
        }
        else
        {
         rest = new Chain("");
         if (optor().length() > 0)
         {
          this.s(new Trm(optor(), this.g(1)), 1);
          for (int i = 2; i < inx; i++) ((Trm)this.g(1)).Push(this.g(i));
          for (int i = 2; i <= Len() - (inx - 2); i++) this.s(this.g(i + (inx - 2)), i);
          for (int i = 1; i <= (inx - 2); i++) Del(Len());
          inx = 2;
         }
         this.s(new Trm(rest.after(1, optor()).Trim().text()), inx); rest = new Chain("");
        }
        if ((((Trm)this.g(inx)).Len() == 1 && ((Trm)this.g(inx)).optor().length() == 0)) this.s(((Trm)this.g(inx)).lOpnd(), inx);
        optor(optorForSymbolicOptor(firstOptorSmb.Trim().text()));
       }
      }
      else
      {
       if ((Len() == 1) && (optor().length() == 0)) { optor(optorForSymbolicOptor(firstOptorSmb.Trim().text())); this.s(rest.before(firstOptorSmb).Trim(), inx); Push(rest.after(firstOptorSmb).Trim().text()); }
       else this.s(new Trm(optorForSymbolicOptor(firstOptorSmb.Trim().text()), new Object[] { rest.before(firstOptorSmb).Trim(), rest.after(firstOptorSmb).Trim().text() }), inx);
       rest = new Chain("");
      }
     }
     if (rest.len() > 0) Push(rest.text());
     rest = new Chain("");
     inx++;
    }
   }
  }
  
  protected void init() throws Exception
  {
   if (!selfTested) selfTest();
   if (Len() == 0) return;
   splitparts(); //maybe one Operand is a formula that must be split into a network of operators and Trms

  }

  private static void selfTest() throws Exception
  {
   selfTested = true;

   _dtv.Set(" _ "  , "AP``¹``>¹²``, ²``");                  // we could define AP rather than APD for the append operator, this Dtv is only a sample of how could it be done ...
   _dtv.Set(" & "  , "CCT``¹``>´°(¹´, ²´)````");            // we could define CONCAT rather than CCT for the append operator, this Dtv is only a sample of how could it be done ...
   _dtv.Set(" >$ " , "UPPER``°(¹)``>°(´¹²)´``, ²``");       // Convert To String this Dtv is only a sample of how could it be done ...
   _dtv.Del(" && ");                                        // Dtv().Set(" && " , "AN``¹``>(´¹ ²)´`` ° ²````");        // AND is not used within Terms
   _dtv.Del(" || ");                                        // Dtv().Set(" || " , "OR``¹``>(´¹ ²)´`` ° ²``");          // OR  is not used within Terms
   _dtv.Del(" ^^ ");                                        // Dtv().Set(" ^^ " , "XR``¹``>(´¹ ²)´`` ° ²``");          // XOR is not used within Terms
   _dtv.Del(" °° ");                                        // Dtv().Set(" °° " , "XS``¹``>(´¹ ²)´`` ° ²``");          // EXIST is not used within Terms
   _dtv.Del(" !°° ");                                       // Dtv().Set(" !°° ", "NX``¹``>(´¹ ²)´`` ° ²``");          // NOT EXIST is not used within Terms
   _dtv.Del(" == ");                                        // Dtv().Set(" == " , "IS``¹``>(´¹ ²)´`` ° ²``");          // IS is not used within Terms
   _dtv.Del(" !== ");                                       // Dtv().Set(" !== ", "NS``¹``>(´¹ ²)´`` ° ²``");          // NOT IS is not used within Terms
   _dtv.Del(" = ");                                         // Dtv().Set(" = "  , "EQ``¹``>(´¹ ²)´`` ° ²``");          // EQUALS is not used within Terms
   _dtv.Del(" != ");                                        // Dtv().Set(" != " , "NE``¹``>(´¹ ²)´`` ° ²``");          // NOT EQUALS is not used within Terms
   _dtv.Del(" > ");                                         // Dtv().Set(" > "  , "GT``¹``>(´¹ ²)´`` ° ²``");          // GREATER THAN is not used within Terms
   _dtv.Del(" < ");                                         // Dtv().Set(" < "  , "LT``¹``>(´¹ ²)´`` ° ²``");          // LESS THAN is not used within Terms
   _dtv.Del(" >= ");                                        // Dtv().Set(" >= " , "GE``¹``>(´¹ ²)´`` ° ²``");          // GREATER OR EQUAL is not used within Terms
   _dtv.Del(" <= ");                                        // Dtv().Set(" <= " , "LE``¹``>(´¹ ²)´`` ° ²``");          // LESS OR EQUAL is not used within Terms
   _dtv.Del(" ~ ");                                         // Dtv().Set(" ~ "  , "LK``¹``>(´¹ ²)´`` ° ²``");          // LIKE is not used within Terms
   _dtv.Del(" !~ ");                                        // Dtv().Set(" !~ " , "NL``¹``>(´¹ ²)´`` ° ²``");          // NOT LIKE is not used within Terms
   _dtv.Del(" ° ");                                         // Dtv().Set(" ° "  , "IN``¹``>(´¹ ²)´`` ° ²``");          // IN is not used within Terms
   _dtv.Del(" !° ");                                        // Dtv().Set(" !° " , "NI``¹``>(´¹ ²)´`` ° ²``");          // NOT IN is not used within Terms
   _dtv.Del(" ≡ ");                                         // Dtv().Set(" ≡ "  , "MV``¹``>(´¹ ²)´`` ° ²``");          // MATCHES VALUES is not used within Terms
   _dtv.Del(" !≡ ");                                        // Dtv().Set(" !≡ " , "NV``¹``>(´¹ ²)´`` ° ²``");          // NOT MATCHES VALUES is not used within Terms
   _dtv.Del(" ≡> ");                                        // Dtv().Set(" ≡> " , "MU``¹``>(´¹ ²)´`` ° ²``");          // MATCHES UPPERCASE VALUES is not used within Terms
   _dtv.Del(" !≡> ");                                       // Dtv().Set(" !≡> ", "NU``¹``>(´¹ ²)´`` ° ²``");          // NOT MATCHES UPPERCASE VALUES is not used within Terms
   _dtv.Del(" ≡< ");                                        // Dtv().Set(" ≡< " , "ML``¹``>(´¹ ²)´`` ° ²``");          // MATCHES LOWERCASE VALUES is not used within Terms
   _dtv.Del(" !≡< ");                                       // Dtv().Set(" !≡< ", "NL``¹``>(´¹ ²)´`` ° ²``");          // NOT MATCHES LOWERCASE VALUES is not used within Terms
   _dtv.Del(" <|> ");                                       // Dtv().Set(" <|> ", "UNION``¹``>(´¹´²)´``) ° (²``");     // UNION is not used within Terms
   _dtv.Del(" <&> ");                                       // Dtv().Set(" <&> ", "INTERSECT``¹``>(´¹)´ ° ²``(²)``");  // INTERSECT is not used within Terms
   _dtv.Del(" <-> ");                                       // Dtv().Set(" <-> ", "EXCEPT``¹``>(´¹)´ ° ²``(²)``");     // EXCEPT is not used within Terms

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
   ass(new Trm(new Chain("x")).cct("y").sql().equals("CCT(x, y)"));
   ass(new Trm(new Chain("name")).cct(1).sql().equals("CCT(name, '1')"));
   ass(new Trm(new Chain("name")).cct(1).apd("id").sql().equals("CCT(name, '1'), id"));
   ass(new Trm(new Chain("nm")).apd("id").apd(new Trm(new Chain("idnm")).cct("'XXX'")).apd("0.5").sql().equals("nm, id, CCT(idnm, 'XXX'), 0.5")); // ass(b._sql().equals("nm, id, idnm + 'XXX', 0.5"));  //ass(b._sql().equals("nm, id, idnm || 'XXX', 0.5"));
   ass(new Trm(new Chain("nm")).apd("id").apd(new Trm(new Chain("idnm")).cct("'XXX'")).apd(0.5).sql().equals("nm, id, CCT(idnm, 'XXX'), 0.5"));   // ass(b._sql().equals("nm, id, idnm + 'XXX', 0.5"));  //ass(b._sql().equals("nm, id, idnm || 'XXX', 0.5"));
  }

  public Trm Clone() throws Exception { Trm ret = new Trm(); ret.optor(optor()); ret.Add(this); return ret; } //the super-super class Pack already provides Clone but its result cannot be casted to (Trm) in c#

  public  Trm (Trm                                     lOpnd) throws Exception { super("",                        lOpnd); init(); }
  public  Trm (String                                  lOpnd) throws Exception { super("",             new Chain(lOpnd)); init(); }  //only for jave, c# does implicit type-conversion to Chain
  public  Trm (Chain                                   lOpnd) throws Exception { super("",                        lOpnd); init(); }
  public  Trm (Object  lOpnd,   String cptor,   Object rOpnd) throws Exception { super(cptor,        lOpnd,       rOpnd); init(); }  //only for jave, c# does implicit type-conversion to Chain
  public  Trm (Object  lOpnd,    Chain cptor,   Object rOpnd) throws Exception { super(cptor.text(), lOpnd,       rOpnd); init(); }
  public  Trm (                                             ) throws Exception { super(                                ); init(); }
  public  Trm (long count                                   ) throws Exception { super(count                           ); init(); }
  //public  Trm (Object...                              opnd) throws Exception { super(opnd                            ); init(); }
  public  Trm (Pile<Object>                             opnd) throws Exception { super(opnd                            ); init(); }
  public  Trm (String optor,                  Object... opnd) throws Exception { super(optor,                      opnd); init(); }
  public  Trm (String optor,               Pile<Object> opnd) throws Exception { super(optor,                      opnd); init(); }

  @Override
  public String val(int i, EvalExpert evx) throws Exception
  {
   Object item = g(i);
   if (false && (dbg))
   {
    if (item instanceof DbField)  return "[" + ((DbField)item).val(evx) + "]";
    if (item instanceof Trm)      return "[" + ((Trm)item).val(evx) + "]";
    if (item instanceof Chain)    return "[" + ((Chain)item).text() + "]";
    if (item instanceof String)   return "[" + (String)item + "]";
    if (item instanceof Long)     return "[" + ((Db)evx).ds("" + ((Long)(item))) + "]";
    if (item instanceof Double)   return "[" + ((Db)evx).ds("" + ((Double)(item))).replace(",", ".") + "]";
    throw new Exception("Err: unknown Type in Trm.val()");
   }
   else
   {
    if (item instanceof DbField)  return ((DbField)item).val(evx);
    if (item instanceof Trm)      return ((Trm)item).val(evx);
    if (item instanceof Chain)    return ((Chain)item).text();
    if (item instanceof String)   return (String)item;
    if (item instanceof Long)     return ((Db)evx).ds("" + ((Long)(item)));
    if (item instanceof Double)   return ((Db)evx).ds("" + ((Double)(item))).replace(",", ".");
    throw new Exception("Err: unknown Type in Trm.val()");
   }
  }

  public static Pile<Trm> fromTxtDef(String def)
  {
   //AttGeTr TBD TO BE DONE !!!
   return new Pile<Trm>();
  }

  public static Pile<Trm> fromSymbolicDef(Chain def)
  {
   return new Pile<Trm>();
  }

  public Trm cvs() throws Exception             { if (optor().length() > 0) return new Trm(this).cvs();                                                                  optor(utl.OPR("CVS")); return this; }
  public Trm cct(String rOpnd) throws Exception { if (optor().length() > 0) return new Trm(this).cct(rOpnd); this.Push(new Chain(rOpnd));                                optor(utl.OPR("CCT")); return this; }
  public Trm cct(Chain  rOpnd) throws Exception { if (optor().length() > 0) return new Trm(this).cct(rOpnd); this.Push(rOpnd);                                           optor(utl.OPR("CCT")); return this; }
  public Trm cct(Trm    rOpnd) throws Exception { if (optor().length() > 0) return new Trm(this).cct(rOpnd); this.Push((Object)rOpnd.Clone());                           optor(utl.OPR("CCT")); return this; }
  public Trm cct(long   rOpnd) throws Exception { if (optor().length() > 0) return new Trm(this).cct(rOpnd); this.Push(utl.ds(false, "" + rOpnd));                       optor(utl.OPR("CCT")); return this; }
  public Trm cct(double rOpnd) throws Exception { if (optor().length() > 0) return new Trm(this).cct(rOpnd); this.Push(utl.ds(false, "" + rOpnd).replace(",", "."));     optor(utl.OPR("CCT")); return this; }
  public Trm cct(DbSlc  rOpnd) throws Exception { if (optor().length() > 0) return new Trm(this).cct(rOpnd); this.Push(rOpnd);                                           optor(utl.OPR("CCT")); return this; }
  public Trm apd(String rOpnd) throws Exception { if (optor().length() > 0) return new Trm(this).apd(rOpnd); this.Push(new Chain(rOpnd));                                optor(utl.OPR("APD")); return this; }
  public Trm apd(Chain  rOpnd) throws Exception { if (optor().length() > 0) return new Trm(this).apd(rOpnd); this.Push(rOpnd);                                           optor(utl.OPR("APD")); return this; }
  public Trm apd(Trm    rOpnd) throws Exception { if (optor().length() > 0) return new Trm(this).apd(rOpnd); this.Push((Object)rOpnd.Clone());                           optor(utl.OPR("APD")); return this; }
  public Trm apd(long   rOpnd) throws Exception { if (optor().length() > 0) return new Trm(this).apd(rOpnd); this.Push(new Chain("" + rOpnd));                           optor(utl.OPR("APD")); return this; }
  public Trm apd(double rOpnd) throws Exception { if (optor().length() > 0) return new Trm(this).apd(rOpnd); this.Push(new Chain("" + rOpnd).text().replace(",", "."));  optor(utl.OPR("APD")); return this; }
  public Trm apd(DbSlc  rOpnd) throws Exception { if (optor().length() > 0) return new Trm(this).apd(rOpnd); this.Push(rOpnd);                                           optor(utl.OPR("APD")); return this; }

  public Chain sql() throws Exception { return sql(this); } //return sql(new Db(_o.DbDrivers)); }
  public Chain smb() throws Exception { return sql(this); } //AttGeTr: must be revised, is sql-Syntax and symbolic Syntax always equal in terms of DbFields ?

  public Chain sql(EvalExpert evx) throws Exception
  {
   if (evx.Dtv().hasKey(optor())) return new Chain(val(evx));
   if (Dtv().hasKey(optor()))     return new Chain(val(this));
   if (optor().equals(""))        return new Chain(val(this)); //return new Chain((String)lOpnd());
   Chain ret = new Chain("");
   Chain retClose = new Chain("");
   

   if (lOpnd() instanceof Trm)
    ret = ret.plus(((Trm)lOpnd()).sql(evx));
   else
    if (lOpnd() instanceof DbField)
     ret = ret.plus(((DbField)lOpnd()).sql(evx));
    else
     if (lOpnd() instanceof DbSlc)
      ret = ret.plus("( ").plus(evx.val(((DbSlc)lOpnd()))).plus(" )");
     else
     if (lOpnd() instanceof Chain)
      ret = ret.plus((Chain)lOpnd());
      else
       ret = ret.plus((String)lOpnd());

   if      (optor().equals(utl.OPR("APD")))                  ret = ret.plus(", ");
   else if (optor().equals(utl.OPR("CCT")))                  { ret = new Chain("CONCAT(").plus(ret).plus(", "); retClose  = new Chain(")"); }
   else if (optor().equals(utl.OPR("CVS")))                  { ret = new Chain("CAST(").plus(ret).plus(" as text"); retClose  = new Chain(")"); }

   if (rOpnd() != null) 
    if (rOpnd() instanceof Trm)
     ret = ret.plus(((Trm)rOpnd()).sql(evx));
    else
     if (rOpnd() != null) if (rOpnd() instanceof DbField)
      ret = ret.plus(((DbField)rOpnd()).sql(evx));
     else
      if (rOpnd() instanceof DbSlc)
       ret = ret.plus("( ").plus(evx.val(((DbSlc)rOpnd()))).plus(" )");
      else
     if (rOpnd() instanceof Chain)
       ret = ret.plus((Chain)rOpnd());
      else
       ret = ret.plus((String)rOpnd());

   ret = ret.plus(retClose);
   return ret;
  }

  public Chain _smb() throws Exception
  {
   if (optor().equals("")) return new Chain("");
   Chain ret = new Chain("cd(");
   if ((optor().equals(utl.OPR("OR"))) || (optor().equals(utl.OPR("AND"))) || (optor().equals(utl.OPR("XOR")))) ret  = new Chain("");
   if (lOpnd() instanceof Trm) ret = ret.plus(((Trm)lOpnd())._smb()); else if (lOpnd() instanceof DbSlc) ret = ret.plus("( ").plus(((DbSlc)lOpnd()).smb()).plus(" )"); else if (((Chain)lOpnd()).startsWith("'")) ret = ret.plus("ds(\"").plus(((Chain)lOpnd()).from(2).upto(-2) + "\")"); else ret = ret.plus("\"").plus((Chain)lOpnd()).plus("\"");
   if      (optor().equals(utl.OPR("APD")))                  ret = ret.plus(").APD(");
   else if (optor().equals(utl.OPR("CCT")))                  ret = ret.plus(").CCT(");
   else if (optor().equals(utl.OPR("CVS")))                  ret = ret.plus(").CVS(");
   if (rOpnd() instanceof Trm) ret = ret.plus(((Trm)rOpnd())._smb()); else if (rOpnd() instanceof DbSlc) ret = ret.plus("( ").plus(((DbSlc)rOpnd()).smb()).plus(" )"); else if (((Chain)rOpnd()).startsWith("'")) ret = ret.plus("ds(\"").plus(((Chain)rOpnd()).from(2).upto(-2)).plus("\")");  else ret = ret.plus("\"").plus((Chain)rOpnd()).plus("\"");
   //if ((optor.equals(utl.OPR("OR"))) || (optor.equals(utl.OPR("AND"))) || (optor.equals(utl.OPR("XOR")))) ret  += ")";
   ret = ret.plus(")");
   return ret;
  }



 }



















//** @author  GeTr Copyright© 1996-2020 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties
//** @license You may use, modify, distribute this code under GNU public dual licence free for NON-Commercial use as long as this notice remains unchanged.
//** @comment Database Condition


package org.xxdevplus.data;

import org.xxdevplus.frmlng.Trm;
import org.xxdevplus.utl.utl;
import org.xxdevplus.struct.Pile;
import org.xxdevplus.frmlng.EvalExpert;
import org.xxdevplus.frmlng.Xpn;
import org.xxdevplus.chain.Chain;
import org.xxdevplus.frmlng.Zone;

 public class DbCnd extends Xpn<Object>
 {
  private static boolean selfTested  = false;  private static String ass(boolean expr) throws Exception { if (!expr) throw new Exception(ass(true) + " SelfTest Failure"); return "DbCnd"; }
    
  public void init1() throws Exception 
  {
 //Dtv().Set(" _ "    , "AP``¹``>¹²``, ²``");               // Append not used within Conditions
 //Dtv().Set(" & "    , "CC``¹``>´°(¹´, ²´)````");          // Concat not used within Conditions
 //Dtv().Set(" >$ "   , "CS``¹``>(´¹ ²)´`` ° ²``");         // ConvertString not used within Conditions
   Dtv().Set(" && "   , "&&``¹``>(´¹ ²)´`` ° ²````");       // AND
   Dtv().Set(" || "   , "||``¹``>(´¹ ²)´`` ° ²``");         // OR
   Dtv().Set(" \\\\ " , "\\\\``¹``>(´¹ ²)´`` ° ²``");       // MNS
   Dtv().Set(" ^^ "   , "^^``¹``>(´¹ ²)´`` ° ²``");         // XOR
   Dtv().Set(" °° "   , "XS``¹``>(´¹ ²)´`` ° ²``");         // exist
   Dtv().Set(" !°° "  , "NX``¹``>(´¹ ²)´`` ° ²``");         // not exist
   Dtv().Set(" == "   , "IS``¹``>(´¹ ²)´`` ° ²``");         // is
   Dtv().Set(" !== "  , "NS``¹``>(´¹ ²)´`` ° ²``");         // not is
   Dtv().Set(" = "    , "=``¹``>(´¹ ²)´`` ° ²``");          // equals
   Dtv().Set(" != "   , "<>``¹``>(´¹ ²)´`` ° ²``");         // not equals
   Dtv().Set(" > "    , ">``¹``>(´¹ ²)´`` ° ²``");          // GT
   Dtv().Set(" < "    , "<``¹``>(´¹ ²)´`` ° ²``");          // LT
   Dtv().Set(" >= "   , ">=``¹``>(´¹ ²)´`` ° ²``");         // GE
   Dtv().Set(" <= "   , "<=``¹``>(´¹ ²)´`` ° ²``");         // LE
   Dtv().Del(" ~ ");                                        // Dtv().Set(" ~ ",   "LIKE``¹``>(´¹ ²)´`` ° ²``");     //"IN" is used within Conditions BUT it must betreated special in sql() funtion
   Dtv().Del(" !~ ");                                       // Dtv().Set(" !~ ",  "NOT LIKE``¹``>(´¹ ²)´`` ° ²``"); //"NOT IN" is used within Conditions BUT it must betreated special in sql() funtion
   Dtv().Del(" ° ");                                        // Dtv().Set(" ° ",   "IN``¹``>(´¹ ²)´`` ° ²``");       //"LIKE" is used within Conditions BUT it must betreated special in sql() funtion
   Dtv().Del(" !° ");                                       // Dtv().Set(" !° ",  "NOT IN``¹``>(´¹ ²)´`` ° ²``");   //"NOT LIKE" is used within Conditions BUT it must betreated special in sql() funtion
   Dtv().Del(" ≡ ");                                        // Dtv().Set(" ≡ ",   "MV``¹``>(´¹ ²)´`` ° ²``");       //"Matches Values" is used within Conditions BUT it must betreated special in sql() funtion
   Dtv().Del(" !≡ ");                                       // Dtv().Set(" !≡ ",  "NV``¹``>(´¹ ²)´`` ° ²``");       //"Not Matches Values" is used within Conditions BUT it must betreated special in sql() funtion
   Dtv().Del(" ≡> ");                                       // Dtv().Set(" ≡> ",  "MU``¹``>(´¹ ²)´`` ° ²``");       //"Matches UpperCase Values" is used within Conditions BUT it must betreated special in sql() funtion
   Dtv().Del(" !≡> ");                                      // Dtv().Set(" !≡> ", "NU``¹``>(´¹ ²)´`` ° ²``");       //"Not Matches UpperCase Values" is used within Conditions BUT it must betreated special in sql() funtion
   Dtv().Del(" ≡< ");                                       // Dtv().Set(" ≡< ",  "ML``¹``>(´¹ ²)´`` ° ²``");       //"Matches LowerCase Values" is used within Conditions BUT it must betreated special in sql() funtion
   Dtv().Del(" !≡< ");                                      // Dtv().Set(" !≡< ", "NL``¹``>(´¹ ²)´`` ° ²``");       //"Not Matches Uppercase Values" is used within Conditions BUT it must betreated special in sql() funtion
 //Dtv().Set(" <|> "  , "UNION``¹``>(´¹´²)´``) ° (²``");    // UNION is not used within Conditions
 //Dtv().Set(" <&> "  , "INTERSECT``¹``>(´¹)´ ° ²``(²)``"); // INTERSECT is not used within Conditions
 //Dtv().Set(" <-> "  , "EXCEPT``¹``>(´¹)´ ° ²``(²)``");    // EXCEPT is not used within Conditions
   if (!selfTested) selfTest();
  }

  private static void selfTest() throws Exception
  {
   selfTested = true;
   //ass(new Trm("x").cct("y")._sql().equals("CONCAT(x, y)"));
   //ass(new Trm("name").cct(1)._sql().equals("CONCAT(name, '1')"));
   //ass(new Trm("name").cct(1).apd("id")._sql().equals("CONCAT(name, '1'), id"));
   //ass(new Trm("nm").apd("id").apd(new Trm("idnm").cct("'XXX'")).apd("0.5")._sql().equals("nm, id, CONCAT(idnm, 'XXX'), 0.5")); // ass(b._sql().equals("nm, id, idnm + 'XXX', 0.5"));  //ass(b._sql().equals("nm, id, idnm || 'XXX', 0.5")); 
   //ass(new Trm("nm").apd("id").apd(new Trm("idnm").cct("'XXX'")).apd(0.5)._sql().equals("nm, id, CONCAT(idnm, 'XXX'), 0.5")); // ass(b._sql().equals("nm, id, idnm + 'XXX', 0.5"));  //ass(b._sql().equals("nm, id, idnm || 'XXX', 0.5")); 

   _dtv.Set(" :dbg:_ " , "APPEND``¹``>¹²``²``");             ass(new DbCnd(" :dbg:_ " , "a", "b", "c").val().equals("abc"));                           ass(new DbCnd(" :dbg:_ " , new String[] { "a", "b" }).val().equals("ab"));           ass(new DbCnd(" :dbg:_ " , "a").val().equals("a"));
   _dtv.Set(" :dbg:& " , "CONCAT``¹``>°´(¹²´)``, ²``");      ass(new DbCnd(" :dbg:& " , "a", "b", "c").val().equals("CONCAT(a, b, c)"));               ass(new DbCnd(" :dbg:& " , new String[] { "a", "b" }).val().equals("CONCAT(a, b)")); ass(new DbCnd(" :dbg:& " , "a").val().equals("a"));
   _dtv.Set(" :dbg:& " , "CONCAT``¹``>´°(¹´, ²´)````");      ass(new DbCnd(" :dbg:& " , "a", "b", "c").val().equals("CONCAT(CONCAT(a, b), c)"));       ass(new DbCnd(" :dbg:& " , new String[] { "a", "b" }).val().equals("CONCAT(a, b)")); ass(new DbCnd(" :dbg:& " , "a").val().equals("a"));
   _dtv.Set(" :dbg:& " , "CONCAT``¹``<´°(¹´, ²´)````");      ass(new DbCnd(" :dbg:& " , "a", "b", "c").val().equals("CONCAT(a, CONCAT(b, c))"));       ass(new DbCnd(" :dbg:& " , new String[] { "a", "b" }).val().equals("CONCAT(a, b)")); ass(new DbCnd(" :dbg:& " , "a").val().equals("a"));
   _dtv.Set(" :dbg:>$ ", "CAST````<°(´¹´, ² AS TEXT)´````"); ass(new DbCnd(" :dbg:>$ ", "a", "b", "c", "d").val().equals("CAST(a, b, c, d AS TEXT)")); ass(new DbCnd(" :dbg:>$ ", "a", "b", "c").val().equals("CAST(a, b, c AS TEXT)"));    ass(new DbCnd(" :dbg:>$ ", new String[] { "a", "b" }).val().equals("CAST(a, b AS TEXT)")); ass(new DbCnd(" :dbg:>$ ", "a").val().equals("CAST(a AS TEXT)"));
   _dtv.Set(" :dbg:>$ ", "UPPER(````<°(´¹´, ²)´````");       ass(new DbCnd(" :dbg:>$ ", "a", "b", "c", "d").val().equals("UPPER(a, b, c, d)"));        ass(new DbCnd(" :dbg:>$ ", "a", "b", "c").val().equals("UPPER(a, b, c)"));           ass(new DbCnd(" :dbg:>$ ", new String[] { "a", "b" }).val().equals("UPPER(a, b)"));        ass(new DbCnd(" :dbg:>$ ", "a").val().equals("UPPER(a)"));
   _dtv.Set(" :dbg:+ " , "+``¹,``>¹´,²`` ² °``");            ass(new DbCnd(" :dbg:+ " , "a", "b", "c").val().equals("a, b + c +"));                    ass(new DbCnd(" :dbg:+ " , new String[] {"a", "b"}).val().equals("a, b +"));         ass(new DbCnd(" :dbg:+ " , "a").val().equals("a,"));
   _dtv.Set(" :dbg:+ " , "+``¹,``>´(¹´,²´)`` ² °``");        ass(new DbCnd(" :dbg:+ " , "a", "b", "c").val().equals("(a, b + c +)"));                  ass(new DbCnd(" :dbg:+ " , new String[] {"a", "b"}).val().equals("(a, b +)"));       ass(new DbCnd(" :dbg:+ " , "a").val().equals("a,"));
   _dtv.Set(" :dbg:+ " , "+``¹,``>´(¹, ²´) ° ````");         ass(new DbCnd(" :dbg:+ " , "a", "b", "c", "d").val().equals("(((a, b) + c) + d) + "));    ass(new DbCnd(" :dbg:+ " , "a", "b", "c").val().equals("((a, b) + c) + "));          ass(new DbCnd(" :dbg:+ " , new String[] {"a", "b"}).val().equals("(a, b) + "));            ass(new DbCnd(" :dbg:+ ", "a").val().equals("a,")); //here we want to get the postfix notation with brackets like follows: "(((a, b) + c) + d) + "
   _dtv.Set(" :dbg:|| ", "OR``¹``>(´¹ ²)´`` ° ²``");         ass(new DbCnd(" :dbg:|| ", "a", "b", "c", "d").val().equals("(a OR b OR c OR d)"));       ass(new DbCnd(" :dbg:|| ", "a", "b", "c").val().equals("(a OR b OR c)"));            ass(new DbCnd(" :dbg:|| ", new String[] { "a", "b" }).val().equals("(a OR b)"));           ass(new DbCnd(" :dbg:|| ", "a").val().equals("a"));
   _dtv.Set(" :dbg:|| ", "OR``¹``>°(´¹²)´``, ²``");          ass(new DbCnd(" :dbg:|| ", "a", "b", "c", "d").val().equals("OR(a, b, c, d)"));           ass(new DbCnd(" :dbg:|| ", "a", "b", "c").val().equals("OR(a, b, c)"));              ass(new DbCnd(" :dbg:|| ", new String[] { "a", "b" }).val().equals("OR(a, b)"));           ass(new DbCnd(" :dbg:|| ", "a").val().equals("a"));
   _dtv.Set(" :dbg:|| ", "OR``¹``>´°(¹´, ²´)````");          ass(new DbCnd(" :dbg:|| ", "a", "b", "c", "d").val().equals("OR(OR(OR(a, b), c), d)"));   ass(new DbCnd(" :dbg:|| ", "a", "b", "c").val().equals("OR(OR(a, b), c)"));          ass(new DbCnd(" :dbg:|| ", new String[] { "a", "b" }).val().equals("OR(a, b)"));           ass(new DbCnd(" :dbg:|| ", "a").val().equals("a"));
   _dtv.Set(" :dbg:|| ", "OR``¹``<´°(¹´, ²´)````");          ass(new DbCnd(" :dbg:|| ", "a", "b", "c", "d").val().equals("OR(a, OR(b, OR(c, d)))"));   ass(new DbCnd(" :dbg:|| ", "a", "b", "c").val().equals("OR(a, OR(b, c))"));          ass(new DbCnd(" :dbg:|| ", new String[] { "a", "b" }).val().equals("OR(a, b)"));           ass(new DbCnd(" :dbg:|| ", "a").val().equals("a"));
   _dtv.Set(" :dbg:|| ", "OR``¹, ``>´¹,´ ²´ °````");         ass(new DbCnd(" :dbg:|| ", "a", "b", "c", "d").val().equals("a, b OR c OR d OR"));        ass(new DbCnd(" :dbg:|| ", "a", "b", "c").val().equals("a, b OR c OR"));             ass(new DbCnd(" :dbg:|| ", new String[] { "a", "b" }).val().equals("a, b OR"));            ass(new DbCnd(" :dbg:|| ", "a").val().equals("a, "));
   _dtv.Set(" :dbg:|| ", "OR``¹, ``<´¹´, ²´ °````");         ass(new DbCnd(" :dbg:|| ", "a", "b", "c", "d").val().equals("a, b, c, d OR OR OR"));      ass(new DbCnd(" :dbg:|| ", "a", "b", "c").val().equals("a, b, c OR OR"));            ass(new DbCnd(" :dbg:|| ", new String[] { "a", "b" }).val().equals("a, b OR"));            ass(new DbCnd(" :dbg:|| ", "a").val().equals("a, "));
   _dtv.Set(" :dbg:|| ", "OR````>°(¹²)``, ²``");             ass(new DbCnd(" :dbg:|| ", "a", "b", "c", "d").val().equals("OR(a, b, c, d)"));           ass(new DbCnd(" :dbg:|| ", "a", "b", "c").val().equals("OR(a, b, c)"));              ass(new DbCnd(" :dbg:|| ", new String[] { "a", "b" }).val().equals("OR(a, b)"));           ass(new DbCnd(" :dbg:|| ", "a").val().equals("OR(a)"));
   _dtv.Set(" :dbg:|| ", "OR````<°(¹²)``, ²``");             ass(new DbCnd(" :dbg:|| ", "a", "b", "c", "d").val().equals("OR(a, b, c, d)"));           ass(new DbCnd(" :dbg:|| ", "a", "b", "c").val().equals("OR(a, b, c)"));              ass(new DbCnd(" :dbg:|| ", new String[] { "a", "b" }).val().equals("OR(a, b)"));           ass(new DbCnd(" :dbg:|| ", "a").val().equals("OR(a)"));
     //init the static directives Pack:
   //_dtv.clear();



  }

  public DbCnd(Chain lOpnd                               ) throws Exception { super("",                   lOpnd); init1(); }
  public DbCnd(Object lOpnd, Chain cptor, Object rOpnd   ) throws Exception { super(cptor.text(), lOpnd,  rOpnd); init1(); }
  public DbCnd(                                          ) throws Exception { super(                           ); init1(); }
  public DbCnd(long                                 count) throws Exception { super(count                      ); init1(); }
  public DbCnd(Object...                             opnd) throws Exception { super(opnd                       ); init1(); }
  public DbCnd(Pile<Object>                          opnd) throws Exception { super(opnd                       ); init1(); }
  public DbCnd(String optor,               Object... opnd) throws Exception { super(optor,                 opnd); init1(); }
  public DbCnd(String optor,            Pile<Object> opnd) throws Exception { super(optor,                 opnd); init1(); }
  
  public String val(int i, EvalExpert evx) throws Exception
  {
   Object item = g(i);
   if (item instanceof Trm)      return ((Trm)item).val(evx);
   if (item instanceof DbCnd)    return (evx instanceof Db) ? ((DbCnd)item).sql((Db)evx).text() : ((DbCnd)item).sql().text();
   if (item instanceof DbSlc)    return (evx instanceof Db) ? ((DbSlc)item).sql((Db)evx)        : ((DbSlc)item).sql();
   if (item instanceof Chain)    return ((Chain)item).text();
   if (item instanceof String)   return (String)item;
   if (item instanceof Long)     return ((Db)evx).ds("" + ((Long)(item)));
   if (item instanceof Double)   return ((Db)evx).ds("" + ((Double)(item))).replace(",", ".");
   throw new Exception("Err: unknown Type in Trm.val()");
  }

  public static Pile<DbCnd> fromTxtDef(String def)
  {
   //AttGeTr TBD TO BE DONE !!!
   return new Pile<DbCnd>();
  }
  
  public static Pile<DbCnd> fromSymbolicDef(Chain def) throws Exception
  {
   if (def.at(".OR").len() > 0) def = def;
   Pile<DbCnd> ret = new Pile<DbCnd>();
   Zone   bktFilter   = new Zone(new Pile<String>(), new Pile<String>("", 0, "(", ")", "||:1"));
   Zone   strFilter   = new Zone(new Pile<String>(), new Pile<String>("", 0, "\"", "\"", "||:1"));
   DbCnd c = null;
   while(def.len() > 0)
   {
    Chain left    = bktFilter.on(def.after(1, "cd"));
    if (left.startsWith("\"")) left    = strFilter.on(bktFilter.on(def.after(1, "cd")));
    Chain mid     = def.after(left).after(1, ".").before(1, "(");
    Chain right   = bktFilter.on(def.after(mid));
    if (right.startsWith("\"")) right   = strFilter.on(bktFilter.on(def.after(mid)));
    if (right.endsWith(".SLD"))
    {
     if (mid.equals("LK"))  ret.Push(new DbCnd(left).LK (new DbJoin(right).SLD()));
     if (mid.equals("nLK")) ret.Push(new DbCnd(left).nLK(new DbJoin(right).SLD()));
     if (mid.equals("IS"))  ret.Push(new DbCnd(left).IS (new DbJoin(right).SLD()));
     if (mid.equals("nIS")) ret.Push(new DbCnd(left).nIS(new DbJoin(right).SLD()));
     if (mid.equals("IN"))  ret.Push(new DbCnd(left).IN (new DbJoin(right).SLD()));
     if (mid.equals("nIN")) ret.Push(new DbCnd(left).nIN(new DbJoin(right).SLD()));
     if (mid.equals("MV"))  ret.Push(new DbCnd(left).MV (new DbJoin(right).SLD()));
     if (mid.equals("nMV")) ret.Push(new DbCnd(left).nMV(new DbJoin(right).SLD()));
     if (mid.equals("MU"))  ret.Push(new DbCnd(left).MU (new DbJoin(right).SLD()));
     if (mid.equals("nMU")) ret.Push(new DbCnd(left).nMU(new DbJoin(right).SLD()));
     if (mid.equals("ML"))  ret.Push(new DbCnd(left).ML (new DbJoin(right).SLD()));
     if (mid.equals("nML")) ret.Push(new DbCnd(left).nML(new DbJoin(right).SLD()));
     if (mid.equals("XS"))  ret.Push(new DbCnd(left).XS (new DbJoin(right).SLD()));
     if (mid.equals("nXS")) ret.Push(new DbCnd(left).nXS(new DbJoin(right).SLD()));
     if (mid.equals("EQ"))  ret.Push(new DbCnd(left).EQ (new DbJoin(right).SLD()));
     if (mid.equals("nEQ")) ret.Push(new DbCnd(left).nEQ(new DbJoin(right).SLD()));
     if (mid.equals("GT"))  ret.Push(new DbCnd(left).GT (new DbJoin(right).SLD()));
     if (mid.equals("LT"))  ret.Push(new DbCnd(left).LT (new DbJoin(right).SLD()));
     if (mid.equals("LE"))  ret.Push(new DbCnd(left).LE (new DbJoin(right).SLD()));
     if (mid.equals("GE"))  ret.Push(new DbCnd(left).GE (new DbJoin(right).SLD()));
     //if (mid.equals("AND")) ret.Push(new DbCnd(left.text).nIN(new DbJoin(right).SLD));
     //if (mid.equals("OR")) ret.Push(new DbCnd(left.text).nIN(new DbJoin(right).SLD));
    }
    else
    if (right.endsWith(".SLC"))
    {
     if (mid.equals("LK"))  ret.Push(new DbCnd(left).LK (new DbJoin(right).SLC()));
     if (mid.equals("nLK")) ret.Push(new DbCnd(left).nLK(new DbJoin(right).SLC()));
     if (mid.equals("IS"))  ret.Push(new DbCnd(left).IS (new DbJoin(right).SLC()));
     if (mid.equals("nIS")) ret.Push(new DbCnd(left).nIS(new DbJoin(right).SLC()));
     if (mid.equals("IN"))  ret.Push(new DbCnd(left).IN (new DbJoin(right).SLC()));
     if (mid.equals("nIN")) ret.Push(new DbCnd(left).nIN(new DbJoin(right).SLC()));
     if (mid.equals("MV"))  ret.Push(new DbCnd(left).MV (new DbJoin(right).SLC()));
     if (mid.equals("nMV")) ret.Push(new DbCnd(left).nMV(new DbJoin(right).SLC()));
     if (mid.equals("MU"))  ret.Push(new DbCnd(left).MU (new DbJoin(right).SLC()));
     if (mid.equals("nMU")) ret.Push(new DbCnd(left).nMU(new DbJoin(right).SLC()));
     if (mid.equals("ML"))  ret.Push(new DbCnd(left).ML (new DbJoin(right).SLC()));
     if (mid.equals("nML")) ret.Push(new DbCnd(left).nML(new DbJoin(right).SLC()));
     if (mid.equals("XS"))  ret.Push(new DbCnd(left).XS (new DbJoin(right).SLC()));
     if (mid.equals("nXS")) ret.Push(new DbCnd(left).nXS(new DbJoin(right).SLC()));
     if (mid.equals("EQ"))  ret.Push(new DbCnd(left).EQ (new DbJoin(right).SLC()));
     if (mid.equals("nEQ")) ret.Push(new DbCnd(left).nEQ(new DbJoin(right).SLC()));
     if (mid.equals("GT"))  ret.Push(new DbCnd(left).GT (new DbJoin(right).SLC()));
     if (mid.equals("LT"))  ret.Push(new DbCnd(left).LT (new DbJoin(right).SLC()));
     if (mid.equals("LE"))  ret.Push(new DbCnd(left).LE (new DbJoin(right).SLC()));
     if (mid.equals("GE"))  ret.Push(new DbCnd(left).GE (new DbJoin(right).SLC()));
     //if (mid.equals("AND")) ret.Push(new DbCnd(left.text).nIN(new DbJoin(right).SLC));
     //if (mid.equals("OR")) ret.Push(new DbCnd(left.text).nIN(new DbJoin(right).SLC));
    }
    else
    {
     if (right.startsWith("ds(")) right = new Chain(utl.ds(false, strFilter.on(bktFilter.on(right)).text()));
     if (right.startsWith("dS(")) right = new Chain(utl.dS(false, strFilter.on(bktFilter.on(right)).text()));
     if (right.startsWith("Ds(")) right = new Chain(utl.Ds(false, strFilter.on(bktFilter.on(right)).text()));
     if (mid.equals("LK"))  ret.Push(new DbCnd(left).LK (right));
     if (mid.equals("nLK")) ret.Push(new DbCnd(left).nLK(right));
     if (mid.equals("IS"))  ret.Push(new DbCnd(left).IS (right));
     if (mid.equals("nIS")) ret.Push(new DbCnd(left).nIS(right));
     if (mid.equals("IN"))  ret.Push(new DbCnd(left).IN (right));
     if (mid.equals("nIN")) ret.Push(new DbCnd(left).nIN(right));
     if (mid.equals("MV"))  ret.Push(new DbCnd(left).MV (right));
     if (mid.equals("nMV")) ret.Push(new DbCnd(left).nMV(right));
     if (mid.equals("MU"))  ret.Push(new DbCnd(left).MU (right));
     if (mid.equals("nMU")) ret.Push(new DbCnd(left).nMU(right));
     if (mid.equals("ML"))  ret.Push(new DbCnd(left).ML (right));
     if (mid.equals("nML")) ret.Push(new DbCnd(left).nML(right));
     if (mid.equals("XS"))  ret.Push(new DbCnd(left).XS (right));
     if (mid.equals("nXS")) ret.Push(new DbCnd(left).nXS(right));
     if (mid.equals("EQ"))  ret.Push(new DbCnd(left).EQ (right));
     if (mid.equals("nEQ")) ret.Push(new DbCnd(left).nEQ(right));
     if (mid.equals("GT"))  ret.Push(new DbCnd(left).GT (right));
     if (mid.equals("LT"))  ret.Push(new DbCnd(left).LT (right));
     if (mid.equals("LE"))  ret.Push(new DbCnd(left).LE (right));
     if (mid.equals("GE"))  ret.Push(new DbCnd(left).GE (right));
     //if (mid.equals("AND")) ret.Push(new DbCnd(left.text).nIN(right));
     //if (mid.equals("OR")) ret.Push(new DbCnd(left.text).nIN(right));
    }
    def = def.after(right).after(1, ")").Trim();
    if (def.startsWith(")")) def = new Chain(""); else def = def.after(1, ",").Trim();
   }
   return ret;
  }


  public DbCnd LK    (Chain  rOpnd) throws Exception  { this.Push(rOpnd);                                       optor(utl.CPR("LIKE"));      return this; }
  public DbCnd LK    (String rOpnd) throws Exception  { return LK(new Chain(rOpnd));}
  public DbCnd LK    (long   rOpnd) throws Exception  { this.Push(new Chain("" + rOpnd));                       optor(utl.CPR("LIKE"));      return this; }
  public DbCnd LK    (double rOpnd) throws Exception  { this.Push(new Chain(("" + rOpnd).replace(",", ".")));   optor(utl.CPR("LIKE"));      return this; }
  public DbCnd LK    (DbSlc  rOpnd) throws Exception  { this.Push(rOpnd);                                       optor(utl.CPR("LIKE"));      return this; }
  public DbCnd LK    (DbJoin rOpnd) throws Exception  { this.Push(new DbSlc(rOpnd));                            optor(utl.CPR("LIKE"));      return this; }
  public DbCnd nLK   (Chain  rOpnd) throws Exception  { this.Push(rOpnd);                                       optor(utl.CPR("NOT_LIKE"));  return this; }
  public DbCnd nLK   (String rOpnd) throws Exception  { return nLK(new Chain(rOpnd));}
  public DbCnd nLK   (long   rOpnd) throws Exception  { this.Push(new Chain("" + rOpnd));                       optor(utl.CPR("NOT_LIKE"));  return this; }
  public DbCnd nLK   (double rOpnd) throws Exception  { this.Push(new Chain(("" + rOpnd).replace(",", ".")));   optor(utl.CPR("NOT_LIKE"));  return this; }
  public DbCnd nLK   (DbSlc  rOpnd) throws Exception  { this.Push(rOpnd);                                       optor(utl.CPR("NOT_LIKE"));  return this; }
  public DbCnd nLK   (DbJoin rOpnd) throws Exception  { this.Push(new DbSlc(rOpnd));                            optor(utl.CPR("NOT_LIKE"));  return this; }
 
  public DbCnd IS    ()             throws Exception  { this.Push("NULL");                                      optor(utl.CPR("IS"));        return this; }
  public DbCnd IS    (Chain  rOpnd) throws Exception  { this.Push(rOpnd);                                       optor(utl.CPR("IS"));        return this; }
  public DbCnd IS    (String rOpnd) throws Exception  { return IS(new Chain(rOpnd));}
  public DbCnd IS    (long   rOpnd) throws Exception  { this.Push(new Chain("" + rOpnd));                       optor(utl.CPR("IS"));        return this; }
  public DbCnd IS    (double rOpnd) throws Exception  { this.Push(new Chain(("" + rOpnd).replace(",", ".")));   optor(utl.CPR("IS"));        return this; }
  public DbCnd IS    (DbSlc  rOpnd) throws Exception  { this.Push(rOpnd);                                       optor(utl.CPR("IS"));        return this; }
  public DbCnd nIS   ()             throws Exception  { this.Push("NULL");                                      optor(utl.CPR("NOT_IS"));    return this; }
  public DbCnd nIS   (Chain  rOpnd) throws Exception  { this.Push(rOpnd);                                       optor(utl.CPR("NOT_IS"));    return this; }
  public DbCnd nIS   (String rOpnd) throws Exception  { return nIS(new Chain(rOpnd));}
  public DbCnd nIS   (long   rOpnd) throws Exception  { this.Push(new Chain("" + rOpnd));                       optor(utl.CPR("NOT_IS"));    return this; }
  public DbCnd nIS   (double rOpnd) throws Exception  { this.Push(new Chain(("" + rOpnd).replace(",", ".")));   optor(utl.CPR("NOT_IS"));    return this; }
  public DbCnd nIS   (DbSlc  rOpnd) throws Exception  { this.Push(rOpnd);                                       optor(utl.CPR("NOT_IS"));    return this; }

  public DbCnd IN    (Chain  rOpnd) throws Exception  { this.Push(new Chain("(" + rOpnd.text() + ")"));         optor(utl.CPR("IN"));        return this; }
  public DbCnd IN    (String rOpnd) throws Exception  { return IN(new Chain(rOpnd));                                                                      }
  public DbCnd IN    (long   rOpnd) throws Exception  { this.Push(new Chain("" + rOpnd));                       optor(utl.CPR("IN"));        return this; }
  public DbCnd IN    (double rOpnd) throws Exception  { this.Push(new Chain(("" + rOpnd).replace(",", ".")));   optor(utl.CPR("IN"));        return this; }
  public DbCnd IN    (DbSlc  rOpnd) throws Exception  { this.Push(rOpnd);                                       optor(utl.CPR("IN"));        return this; }
  public DbCnd nIN   (Chain  rOpnd) throws Exception  { this.Push(new Chain("(" + rOpnd.text() + ")"));         optor(utl.CPR("NOT_IN"));    return this; }
  public DbCnd nIN   (String rOpnd) throws Exception  { return nIN(new Chain(rOpnd));}
  public DbCnd nIN   (long   rOpnd) throws Exception  { this.Push(new Chain("" + rOpnd));                       optor(utl.CPR("NOT_IN"));    return this; }
  public DbCnd nIN   (double rOpnd) throws Exception  { this.Push(new Chain(("" + rOpnd).replace(",", ".")));   optor(utl.CPR("NOT_IN"));    return this; }
  public DbCnd nIN   (DbSlc  rOpnd) throws Exception  { this.Push(rOpnd);                                       optor(utl.CPR("NOT_IN"));    return this; }

  // MV = "matches values"
  public DbCnd MV    (Chain  rOpnd) throws Exception  { this.Push(rOpnd);                                       optor(utl.CPR("MV"));        return this; }
  public DbCnd MV    (String rOpnd) throws Exception  { return MV(new Chain(rOpnd));}
  public DbCnd MV    (long   rOpnd) throws Exception  { this.Push(new Chain("" + rOpnd));                       optor(utl.CPR("MV"));        return this; }
  public DbCnd MV    (double rOpnd) throws Exception  { this.Push(new Chain(("" + rOpnd).replace(",", ".")));   optor(utl.CPR("MV"));        return this; }
  public DbCnd MV    (DbSlc  rOpnd) throws Exception  { this.Push(rOpnd);                                       optor(utl.CPR("MV"));        return this; }
  public DbCnd nMV   (Chain  rOpnd) throws Exception  { this.Push(rOpnd);                                       optor(utl.CPR("NOT_MV"));    return this; }
  public DbCnd nMV   (String rOpnd) throws Exception  { return nMV(new Chain(rOpnd));}
  public DbCnd nMV   (long   rOpnd) throws Exception  { this.Push(new Chain("" + rOpnd));                       optor(utl.CPR("NOT_MV"));    return this; }
  public DbCnd nMV   (double rOpnd) throws Exception  { this.Push(new Chain(("" + rOpnd).replace(",", ".")));   optor(utl.CPR("NOT_MV"));    return this; }
  public DbCnd nMV   (DbSlc  rOpnd) throws Exception  { this.Push(rOpnd);                                       optor(utl.CPR("NOT_MV"));    return this; }

  // MU = "matches uppercase values"
  public DbCnd MU    (Chain  rOpnd) throws Exception  { this.Push(rOpnd);                                       optor(utl.CPR("MU"));        return this; }
  public DbCnd MU    (String rOpnd) throws Exception  { return MU(new Chain(rOpnd));}
  public DbCnd MU    (long   rOpnd) throws Exception  { this.Push(new Chain("" + rOpnd));                       optor(utl.CPR("MU"));        return this; }
  public DbCnd MU    (double rOpnd) throws Exception  { this.Push(new Chain(("" + rOpnd).replace(",", ".")));   optor(utl.CPR("MU"));        return this; }
  public DbCnd MU    (DbSlc  rOpnd) throws Exception  { this.Push(rOpnd);                                       optor(utl.CPR("MU"));        return this; }
  public DbCnd nMU   (Chain  rOpnd) throws Exception  { this.Push(rOpnd);                                       optor(utl.CPR("NOT_MU"));    return this; }
  public DbCnd nMU   (String rOpnd) throws Exception  { return nMU(new Chain(rOpnd));}
  public DbCnd nMU   (long   rOpnd) throws Exception  { this.Push(new Chain("" + rOpnd));                       optor(utl.CPR("NOT_MU"));    return this; }
  public DbCnd nMU   (double rOpnd) throws Exception  { this.Push(new Chain(("" + rOpnd).replace(",", ".")));   optor(utl.CPR("NOT_MU"));    return this; }
  public DbCnd nMU   (DbSlc  rOpnd) throws Exception  { this.Push(rOpnd);                                       optor(utl.CPR("NOT_MU"));    return this; }

  // ML = "matches lowercase values"
  public DbCnd ML    (Chain  rOpnd) throws Exception  { this.Push(rOpnd);                                       optor(utl.CPR("ML"));        return this; }
  public DbCnd ML    (String rOpnd) throws Exception  { return ML(new Chain(rOpnd));}
  public DbCnd ML    (long   rOpnd) throws Exception  { this.Push(new Chain("" + rOpnd));                       optor(utl.CPR("ML"));        return this; }
  public DbCnd ML    (double rOpnd) throws Exception  { this.Push(new Chain(("" + rOpnd).replace(",", ".")));   optor(utl.CPR("ML"));        return this; }
  public DbCnd ML    (DbSlc  rOpnd) throws Exception  { this.Push(rOpnd);                                       optor(utl.CPR("ML"));        return this; }
  public DbCnd nML   (Chain  rOpnd) throws Exception  { this.Push(rOpnd);                                       optor(utl.CPR("NOT_ML"));    return this; }
  public DbCnd nML   (String rOpnd) throws Exception  { return nML(new Chain(rOpnd));}
  public DbCnd nML   (long   rOpnd) throws Exception  { this.Push(new Chain("" + rOpnd));                       optor(utl.CPR("NOT_ML"));    return this; }
  public DbCnd nML   (double rOpnd) throws Exception  { this.Push(new Chain(("" + rOpnd).replace(",", ".")));   optor(utl.CPR("NOT_ML"));    return this; }
  public DbCnd nML   (DbSlc  rOpnd) throws Exception  { this.Push(rOpnd);                                       optor(utl.CPR("NOT_ML"));    return this; }

  public DbCnd XS    (Chain  rOpnd) throws Exception  { this.Push("(" + rOpnd + ")");                           optor(utl.CPR("XS"));        return this; }
  public DbCnd XS    (String rOpnd) throws Exception  { return XS(new Chain(rOpnd));}
  public DbCnd XS    (long   rOpnd) throws Exception  { this.Push(new Chain("" + rOpnd));                       optor(utl.CPR("XS"));        return this; }
  public DbCnd XS    (double rOpnd) throws Exception  { this.Push(new Chain(("" + rOpnd).replace(",", ".")));   optor(utl.CPR("XS"));        return this; }
  public DbCnd XS    (DbSlc  rOpnd) throws Exception  { this.Push(rOpnd);                                       optor(utl.CPR("XS"));        return this; }
  public DbCnd nXS   (Chain  rOpnd) throws Exception  { this.Push("(" + rOpnd + ")");                           optor(utl.CPR("NOT_XS"));    return this; }
  public DbCnd nXS   (String rOpnd) throws Exception  { return nXS(new Chain(rOpnd));}
  public DbCnd nXS   (long   rOpnd) throws Exception  { this.Push(new Chain("" + rOpnd));                       optor(utl.CPR("NOT_XS"));    return this; }
  public DbCnd nXS   (double rOpnd) throws Exception  { this.Push(new Chain(("" + rOpnd).replace(",", ".")));   optor(utl.CPR("NOT_XS"));    return this; }
  public DbCnd nXS   (DbSlc  rOpnd) throws Exception  { this.Push(rOpnd);                                       optor(utl.CPR("NOT_XS"));    return this; }

  public DbCnd EQ    ()             throws Exception  { this.Push("''");                                        optor(utl.CPR("EQ"));        return this; }
  public DbCnd EQ    (Chain  rOpnd) throws Exception  { this.Push(rOpnd);                                       optor(utl.CPR("EQ"));        return this; }
  public DbCnd EQ    (String rOpnd) throws Exception  { return EQ(new Chain(rOpnd));}
  public DbCnd EQ    (long   rOpnd) throws Exception  { this.Push(new Chain("" + rOpnd));                       optor(utl.CPR("EQ"));        return this; }
  public DbCnd EQ    (double rOpnd) throws Exception  { this.Push(new Chain(("" + rOpnd).replace(",", ".")));   optor(utl.CPR("EQ"));        return this; }
  public DbCnd EQ    (DbSlc  rOpnd) throws Exception  { this.Push(rOpnd);                                       optor(utl.CPR("EQ"));        return this; }
  public DbCnd nEQ   ()             throws Exception  { this.Push("''");                                        optor(utl.CPR("NOT_EQ"));    return this; }
  public DbCnd nEQ   (Chain  rOpnd) throws Exception  { this.Push(rOpnd);                                       optor(utl.CPR("NOT_EQ"));    return this; }
  public DbCnd nEQ   (String rOpnd) throws Exception  { return nEQ(new Chain(rOpnd));}
  public DbCnd nEQ   (long   rOpnd) throws Exception  { this.Push(new Chain("" + rOpnd));                       optor(utl.CPR("NOT_EQ"));    return this; }
  public DbCnd nEQ   (double rOpnd) throws Exception  { this.Push(new Chain(("" + rOpnd).replace(",", ".")));   optor(utl.CPR("NOT_EQ"));    return this; }
  public DbCnd nEQ   (DbSlc  rOpnd) throws Exception  { this.Push(rOpnd);                                       optor(utl.CPR("NOT_EQ"));    return this; }

  public DbCnd GT    ()             throws Exception  { this.Push(0);                                           optor(utl.CPR("GT"));        return this; }
  public DbCnd GT    (Chain  rOpnd) throws Exception  { this.Push(rOpnd);                                       optor(utl.CPR("GT"));        return this; }
  public DbCnd GT    (String rOpnd) throws Exception  { return GT(new Chain(rOpnd));}
  public DbCnd GT    (long   rOpnd) throws Exception  { this.Push(new Chain("" + rOpnd));                       optor(utl.CPR("GT"));        return this; }
  public DbCnd GT    (double rOpnd) throws Exception  { this.Push(new Chain(("" + rOpnd).replace(",", ".")));   optor(utl.CPR("GT"));        return this; }
  public DbCnd GT    (DbSlc  rOpnd) throws Exception  { this.Push(rOpnd);                                       optor(utl.CPR("GT"));        return this; }

  public DbCnd LT    ()             throws Exception  { this.Push(0);                                           optor(utl.CPR("LT"));        return this; }
  public DbCnd LT    (Chain  rOpnd) throws Exception  { this.Push(rOpnd);                                       optor(utl.CPR("LT"));        return this; }
  public DbCnd LT    (String rOpnd) throws Exception  { return LT(new Chain(rOpnd));}
  public DbCnd LT    (long   rOpnd) throws Exception  { this.Push(new Chain("" + rOpnd));                       optor(utl.CPR("LT"));        return this; }
  public DbCnd LT    (double rOpnd) throws Exception  { this.Push(new Chain(("" + rOpnd).replace(",", ".")));   optor(utl.CPR("LT"));        return this; }
  public DbCnd LT    (DbSlc  rOpnd) throws Exception  { this.Push(rOpnd);                                       optor(utl.CPR("LT"));        return this; }

  public DbCnd LE    ()             throws Exception  { this.Push(0);                                           optor(utl.CPR("LE"));        return this; }
  public DbCnd LE    (Chain  rOpnd) throws Exception  { this.Push(rOpnd);                                       optor(utl.CPR("LE"));        return this; }
  public DbCnd LE    (String rOpnd) throws Exception  { return LE(new Chain(rOpnd));}
  public DbCnd LE    (long   rOpnd) throws Exception  { this.Push(new Chain("" + rOpnd));                       optor(utl.CPR("LE"));        return this; }
  public DbCnd LE    (double rOpnd) throws Exception  { this.Push(new Chain(("" + rOpnd).replace(",", ".")));   optor(utl.CPR("LE"));        return this; }
  public DbCnd LE    (DbSlc  rOpnd) throws Exception  { this.Push(rOpnd);                                       optor(utl.CPR("LE"));        return this; }

  public DbCnd GE    ()             throws Exception  { this.Push(0);                                           optor(utl.CPR("GE"));        return this; }
  public DbCnd GE    (Chain  rOpnd) throws Exception  { this.Push(rOpnd);                                       optor(utl.CPR("GE"));        return this; }
  public DbCnd GE    (String rOpnd) throws Exception  { return GE(new Chain(rOpnd));}
  public DbCnd GE    (long   rOpnd) throws Exception  { this.Push(new Chain("" + rOpnd));                       optor(utl.CPR("GE"));        return this; }
  public DbCnd GE    (double rOpnd) throws Exception  { this.Push(new Chain(("" + rOpnd).replace(",", ".")));   optor(utl.CPR("GE"));        return this; }
  public DbCnd GE    (DbSlc  rOpnd) throws Exception  { this.Push(rOpnd);                                       optor(utl.CPR("GE"));        return this; }

  //public DbCnd OR    (DbCnd theOther) throws Exception { return new DbCnd(this, utl.OPR("OR")    , theOther); }
  //public DbCnd AND   (DbCnd theOther) throws Exception { return new DbCnd(this, utl.OPR("AND")   , theOther); }

  public DbCnd OR    (DbCnd... other) throws Exception { DbCnd ret = this; for (int i = 0; i < other.length; i++) ret = new DbCnd(ret, new Chain(utl.OPR("OR"))  , other[i]); return ret; }
  public DbCnd AND   (DbCnd... other) throws Exception { DbCnd ret = this; for (int i = 0; i < other.length; i++) ret = new DbCnd(ret, new Chain(utl.OPR("AND"))  , other[i]); return ret; }
  public DbCnd MNS   (DbCnd... other) throws Exception { DbCnd ret = this; for (int i = 0; i < other.length; i++) ret = new DbCnd(ret, new Chain(utl.OPR("MNS"))  , other[i]); return ret; }

  public Chain sql() throws Exception { return sql(this); } //return sql(new Db(_o.DbDrivers)); }

  public Chain sql(EvalExpert evx) throws Exception
  {
   //if (evc.Dtv.Keys.Contains(optor)) return val(evc);  //AttGeTr: the evaluation should be automated with the val() Method. but before all the Operalnds Directives (dtv) must be defined ...
   if (evx.Dtv().hasKey(optor())) return new Chain(val(evx));
   //if (Dtv.hasKey(optor)) return val(this);
   if (optor().equals("")) return new Chain("");
   Chain ret = new Chain("(");
   if (lOpnd() instanceof DbCnd) ret = ret.plus(((DbCnd)lOpnd()).sql(evx)); else if (lOpnd() instanceof DbSlc) ret = ret.plus("( ").plus(evx.val(((DbSlc)lOpnd()))).plus(" )"); else ret = ret.plus((Chain)lOpnd());
   if      (optor().equals(utl.CPR("LIKE")))     ret = ret.plus(" LIKE ");
   else if (optor().equals(utl.CPR("NOT_LIKE"))) ret = ret.plus(" NOT LIKE ");
   else if (optor().equals(utl.CPR("IS")))       ret = ret.plus(optor());
   else if (optor().equals(utl.CPR("IN")))       ret = ret.plus(" IN ");
   else if (optor().equals(utl.CPR("NOT_IN")))   ret = ret.plus(" NOT IN ");
   else if (optor().equals(utl.CPR("MV")))       return ((Db)evx).lot (ret.from(2).text(), new Chain(((Chain)rOpnd()).text().replace("\r\n", "\r").replace("\n", "\r")), "\r").sql(evx);
   else if (optor().equals(utl.CPR("NOT_MV")))   ret = ret.plus(" NOT MV ");
   else if (optor().equals(utl.CPR("MU")))       return ((Db)evx).loT(ret.from(2).text(), new Chain(((Chain)rOpnd()).text().replace("\r\n", "\r").replace("\n", "\r")), "\r").sql(evx);
   else if (optor().equals(utl.CPR("NOT_MU")))   ret = ret.plus(" NOT MU ");
   else if (optor().equals(utl.CPR("ML")))       return ((Db)evx).Lot(ret.from(2).text(), new Chain(((Chain)rOpnd()).text().replace("\r\n", "\r").replace("\n", "\r")), "\r").sql(evx);
   else if (optor().equals(utl.CPR("NOT_ML")))   ret = ret.plus(" NOT ML ");
   else if (optor().equals(utl.CPR("XS")))       ret = ret.plus(" EXISTS ");
   else if (optor().equals(utl.CPR("NOT_XS")))   ret = ret.plus(" NOT EXISTS ");
   else if (optor().equals(utl.CPR("NOT_IS")))   ret = ret.plus(optor());
   else if (optor().equals(utl.CPR("NOT_EQ")))   ret = ret.plus(" <> ");
   else if (optor().equals(utl.CPR("EQ")))       ret = ret.plus(optor());
   else if (optor().equals(utl.CPR("GT")))       ret = ret.plus(" > ");
   else if (optor().equals(utl.CPR("LT")))       ret = ret.plus(" < ");
   else if (optor().equals(utl.CPR("LE")))       ret = ret.plus(" <= ");
   else if (optor().equals(utl.CPR("GE")))       ret = ret.plus(" >= ");
   else if (optor().equals(utl.OPR("APD")))      ret = ret.plus(" _ ");
   else if (optor().equals(utl.OPR("CCT")))      ret = ret.plus(" & ");
   else if (optor().equals(utl.OPR("CVS")))      ret = ret.plus(" >$ ");
   else if (optor().equals(utl.OPR("AND")))      ret = ret.plus(" AND ");
   else if (optor().equals(utl.OPR("MNS")))      ret = ret.plus(" AND NOT ");
   else if (optor().equals(utl.OPR("OR")))       ret = ret.plus(" OR ");
   else if (optor().equals(utl.OPR("XOR")))      ret = ret.plus(" XOR ");
   if (rOpnd() instanceof DbCnd)
    ret = ret.plus(((DbCnd)rOpnd()).sql(evx));
   else
    if (rOpnd() instanceof DbSlc)
     ret = ret.plus("( ").plus(evx.val(((DbSlc)rOpnd()))).plus(" )");
    else
     ret = ret.plus((Chain)rOpnd());
   ret = ret.plus(")");
   return ret;
  }

  
  public Chain smb() throws Exception
  {
   if (optor().equals("")) return new Chain("");

   Chain ret = new Chain("cd(");
   if ((optor().equals(utl.OPR("OR"))) || (optor().equals(utl.OPR("AND"))) || (optor().equals(utl.OPR("XOR")))) ret  = new Chain("");
   if (lOpnd() instanceof DbCnd) ret = ret.plus(((DbCnd)lOpnd()).smb()); else if (lOpnd()  instanceof DbSlc) ret = ret.plus("( ").plus(((DbSlc)lOpnd()).smb()).plus(" )"); else if (((Chain)lOpnd()).startsWith("'")) ret = ret.plus("ds(\"").plus((Chain)lOpnd()).from(2).upto(-2).plus("\")"); else ret = ret.plus("\"").plus((Chain)lOpnd()).plus("\"");
   if      (optor().equals(utl.CPR("LIKE")))      ret = ret.plus(").LK(");
   else if (optor().equals(utl.CPR("NOT_LIKE")))  ret = ret.plus(").nLK(");
   else if (optor().equals(utl.CPR("IS")))        ret = ret.plus(").IS(");
   else if (optor().equals(utl.CPR("IN")))        ret = ret.plus(").IN(");
   else if (optor().equals(utl.CPR("NOT_IN")))    ret = ret.plus(").nIN(");
   else if (optor().equals(utl.CPR("MV")))        ret = ret.plus(").MV(");
   else if (optor().equals(utl.CPR("NOT_MV")))    ret = ret.plus(").nMV(");
   else if (optor().equals(utl.CPR("MU")))        ret = ret.plus(").MU(");
   else if (optor().equals(utl.CPR("NOT_MU")))    ret = ret.plus(").nMU(");
   else if (optor().equals(utl.CPR("XS")))        ret = ret.plus(").XS(");
   else if (optor().equals(utl.CPR("NOT_XS")))    ret = ret.plus(").nXS(");
   else if (optor().equals(utl.CPR("NOT_IS")))    ret = ret.plus(").IS(");
   else if (optor().equals(utl.CPR("NOT_EQ")))    ret = ret.plus(").nEQ(");
   else if (optor().equals(utl.CPR("EQ")))        ret = ret.plus(").EQ(");
   else if (optor().equals(utl.CPR("GT")))        ret = ret.plus(").GT(");
   else if (optor().equals(utl.CPR("LT")))        ret = ret.plus(").LT(");
   else if (optor().equals(utl.CPR("LE")))        ret = ret.plus(").LE(");
   else if (optor().equals(utl.CPR("GE")))        ret = ret.plus(").GE(");
   else if (optor().equals(utl.OPR("APD")))       ret = ret.plus(").APD(");
   else if (optor().equals(utl.OPR("CCT")))       ret = ret.plus(").CCT(");
   else if (optor().equals(utl.OPR("CVS")))       ret = ret.plus(").CVS(");
   else if (optor().equals(utl.OPR("AND")))       ret = ret.plus(".AND(");
   else if (optor().equals(utl.OPR("OR")))        ret = ret.plus(".OR(");
   else if (optor().equals(utl.OPR("XOR")))       ret = ret.plus(".XOR(");

   if (rOpnd()  instanceof DbCnd) ret = ret.plus(((DbCnd)rOpnd()).smb()); else if (rOpnd()  instanceof DbSlc) ret = ret.plus("( ").plus(((DbSlc)rOpnd()).smb()).plus(" )"); else if (((Chain)rOpnd()).startsWith("'")) ret = ret.plus("ds(\"").plus(((Chain)rOpnd()).from(2).upto(-2)).plus("\")"); else ret = ret.plus("\"").plus((Chain)rOpnd()).plus("\"");
   //if ((optor.equals(utl.OPR("OR"))) || (optor.equals(utl.OPR("AND"))) || (optor.equals(utl.OPR("XOR")))) ret  += ")";
   ret = ret.plus(")");
   return ret;
  }
  
  
  
 }

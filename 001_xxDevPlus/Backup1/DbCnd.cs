using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using ndBase;
using ndString;


namespace ndData
{
 public class DbCnd : Xpn<object>
 {
  //Copyright© 1996-2012 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties, You may use, modify, distribute this code under GNU public licence for NON-Commercial use as long as this notice remains unchanged.
  private static bool selfTested  = false; private static string ass(bool expr) { if (!expr) throw new Exception(ass(true) + " SelfTest Failure"); return "DbCnd"; } 
  
  
  private void init() 
  {
 //Dtv.Set(" _ "    , "AP``¹``>¹²``, ²``");                //Append not used within Conditions
 //Dtv.Set(" & "    , "CC``¹``>´°(¹´, ²´)````");           //Concat not used within Conditions
 //Dtv.Set(" >$ "   , "CS``¹``>(´¹ ²)´`` ° ²``");          //ConvertString not used within Conditions
   Dtv.Set(" && "   , "&&``¹``>(´¹ ²)´`` ° ²````");        // AND
   Dtv.Set(" || "   , "||``¹``>(´¹ ²)´`` ° ²``");          // OR
   Dtv.Set(" \\\\ " , "\\\\``¹``>(´¹ ²)´`` ° ²``");        // MNS
   Dtv.Set(" ^^ "   , "^^``¹``>(´¹ ²)´`` ° ²``");          // XOR
   Dtv.Set(" °° "   , "XS``¹``>(´¹ ²)´`` ° ²``");          // exist
   Dtv.Set(" !°° "  , "NX``¹``>(´¹ ²)´`` ° ²``");          // not exist
   Dtv.Set(" == "   , "IS``¹``>(´¹ ²)´`` ° ²``");          // is
   Dtv.Set(" !== "  , "NS``¹``>(´¹ ²)´`` ° ²``");          // not is
   Dtv.Set(" = "    , "=``¹``>(´¹ ²)´`` ° ²``");           // equals
   Dtv.Set(" != "   , "<>``¹``>(´¹ ²)´`` ° ²``");          // not equals
   Dtv.Set(" > "    , ">``¹``>(´¹ ²)´`` ° ²``");           // GT
   Dtv.Set(" < "    , "<``¹``>(´¹ ²)´`` ° ²``");           // LT
   Dtv.Set(" >= "   , ">=``¹``>(´¹ ²)´`` ° ²``");          // GE
   Dtv.Set(" <= "   , "<=``¹``>(´¹ ²)´`` ° ²``");          // LE
   Dtv.Del(" ~ ");                                         // Dtv.Set(" ~ ",   "LIKE``¹``>(´¹ ²)´`` ° ²``");     //"IN" is used within Conditions BUT it must betreated special in sql() funtion
   Dtv.Del(" !~ ");                                        // Dtv.Set(" !~ ",  "NOT LIKE``¹``>(´¹ ²)´`` ° ²``"); //"NOT IN" is used within Conditions BUT it must betreated special in sql() funtion
   Dtv.Del(" ° ");                                         // Dtv.Set(" ° ",   "IN``¹``>(´¹ ²)´`` ° ²``");       //"LIKE" is used within Conditions BUT it must betreated special in sql() funtion
   Dtv.Del(" !° ");                                        // Dtv.Set(" !° ",  "NOT IN``¹``>(´¹ ²)´`` ° ²``");   //"NOT LIKE" is used within Conditions BUT it must betreated special in sql() funtion
   Dtv.Del(" ≡ ");                                         // Dtv.Set(" ≡ ",   "MV``¹``>(´¹ ²)´`` ° ²``");       //"Matches Values" is used within Conditions BUT it must be treated special in sql() funtion
   Dtv.Del(" !≡ ");                                        // Dtv.Set(" !≡ ",  "NV``¹``>(´¹ ²)´`` ° ²``");       //"Not Matches Values" is used within Conditions BUT it must betreated special in sql() funtion
   Dtv.Del(" ≡> ");                                        // Dtv.Set(" ≡> ",  "MU``¹``>(´¹ ²)´`` ° ²``");       //"Matches UpperCase Values" is used within Conditions BUT it must betreated special in sql() funtion
   Dtv.Del(" !≡> ");                                       // Dtv.Set(" !≡> ", "NU``¹``>(´¹ ²)´`` ° ²``");       //"Not Matches UpperCase Values" is used within Conditions BUT it must betreated special in sql() funtion
   Dtv.Del(" ≡< ");                                        // Dtv.Set(" ≡< ",  "ML``¹``>(´¹ ²)´`` ° ²``");       //"Matches LowerCase Values" is used within Conditions BUT it must betreated special in sql() funtion
   Dtv.Del(" !≡< ");                                       // Dtv.Set(" !≡< ", "NL``¹``>(´¹ ²)´`` ° ²``");       //"Not Matches Uppercase Values" is used within Conditions BUT it must betreated special in sql() funtion
 //Dtv.Set(" <|> "  , "UNION``¹``>(´¹´²)´``) ° (²``");     // UNION is not used within Conditions
 //Dtv.Set(" <&> "  , "INTERSECT``¹``>(´¹)´ ° ²``(²)``");  // INTERSECT is not used within Conditions
 //Dtv.Set(" <-> "  , "EXCEPT``¹``>(´¹)´ ° ²``(²)``");     // EXCEPT is not used within Conditions
   if (!selfTested) selfTest();
  }

  private static void selfTest()
  {
   selfTested = true;
   //ass(new Trm("x").cct("y")._sql().equals("CONCAT(x, y)"));
   //ass(new Trm("name").cct(1)._sql().equals("CONCAT(name, '1')"));
   //ass(new Trm("name").cct(1).apd("id")._sql().equals("CONCAT(name, '1'), id"));
   //ass(new Trm("nm").apd("id").apd(new Trm("idnm").cct("'XXX'")).apd("0.5")._sql().equals("nm, id, CONCAT(idnm, 'XXX'), 0.5")); // ass(b._sql().equals("nm, id, idnm + 'XXX', 0.5"));  //ass(b._sql().equals("nm, id, idnm || 'XXX', 0.5")); 
   //ass(new Trm("nm").apd("id").apd(new Trm("idnm").cct("'XXX'")).apd(0.5)._sql().equals("nm, id, CONCAT(idnm, 'XXX'), 0.5")); // ass(b._sql().equals("nm, id, idnm + 'XXX', 0.5"));  //ass(b._sql().equals("nm, id, idnm || 'XXX', 0.5")); 


   _dtv.Set(" :dbg:_ " , "APPEND(``¹``>¹²``²``");             ass(new DbCnd(" :dbg:_ " , "a", "b", "c").val().Equals("abc"));                           ass(new DbCnd(" :dbg:_ " , new object[] { "a", "b" }).val().Equals("ab"));           ass(new DbCnd(" :dbg:_ " , "a").val().Equals("a"));
   _dtv.Set(" :dbg:& " , "CONCAT(``¹``>°´(¹²´)``, ²``");      ass(new DbCnd(" :dbg:& " , "a", "b", "c").val().Equals("CONCAT(a, b, c)"));               ass(new DbCnd(" :dbg:& " , new object[] { "a", "b" }).val().Equals("CONCAT(a, b)")); ass(new DbCnd(" :dbg:& " , "a").val().Equals("a"));
   _dtv.Set(" :dbg:& " , "CONCAT(``¹``>´°(¹´, ²´)````");      ass(new DbCnd(" :dbg:& " , "a", "b", "c").val().Equals("CONCAT(CONCAT(a, b), c)"));       ass(new DbCnd(" :dbg:& " , new object[] { "a", "b" }).val().Equals("CONCAT(a, b)")); ass(new DbCnd(" :dbg:& " , "a").val().Equals("a"));
   _dtv.Set(" :dbg:& " , "CONCAT(``¹``<´°(¹´, ²´)````");      ass(new DbCnd(" :dbg:& " , "a", "b", "c").val().Equals("CONCAT(a, CONCAT(b, c))"));       ass(new DbCnd(" :dbg:& " , new object[] { "a", "b" }).val().Equals("CONCAT(a, b)")); ass(new DbCnd(" :dbg:& " , "a").val().Equals("a"));
   _dtv.Set(" :dbg:>$ ", "CAST(````<°(´¹´, ² AS TEXT)´````"); ass(new DbCnd(" :dbg:>$ ", "a", "b", "c", "d").val().Equals("CAST(a, b, c, d AS TEXT)")); ass(new DbCnd(" :dbg:>$ ", "a", "b", "c").val().Equals("CAST(a, b, c AS TEXT)"));    ass(new DbCnd(" :dbg:>$ ", new object[] { "a", "b" }).val().Equals("CAST(a, b AS TEXT)")); ass(new DbCnd(" :dbg:>$ ", "a").val().Equals("CAST(a AS TEXT)"));
   _dtv.Set(" :dbg:>$ ", "UPPER(````<°(´¹´, ²)´````");        ass(new DbCnd(" :dbg:>$ ", "a", "b", "c", "d").val().Equals("UPPER(a, b, c, d)"));        ass(new DbCnd(" :dbg:>$ ", "a", "b", "c").val().Equals("UPPER(a, b, c)"));           ass(new DbCnd(" :dbg:>$ ", new object[] { "a", "b" }).val().Equals("UPPER(a, b)"));        ass(new DbCnd(" :dbg:>$ ", "a").val().Equals("UPPER(a)"));
   _dtv.Set(" :dbg:+ " , "+``¹,``>¹´,²`` ² °``");             ass(new DbCnd(" :dbg:+ " , "a", "b", "c").val().Equals("a, b + c +"));                    ass(new DbCnd(" :dbg:+ " , new object[] {"a", "b"}).val().Equals("a, b +"));         ass(new DbCnd(" :dbg:+ " , "a").val().Equals("a,"));
   _dtv.Set(" :dbg:+ " , "+``¹,``>´(¹´,²´)`` ² °``");         ass(new DbCnd(" :dbg:+ " , "a", "b", "c").val().Equals("(a, b + c +)"));                  ass(new DbCnd(" :dbg:+ " , new object[] {"a", "b"}).val().Equals("(a, b +)"));       ass(new DbCnd(" :dbg:+ " , "a").val().Equals("a,"));
   _dtv.Set(" :dbg:+ " , "+``¹,``>´(¹, ²´) ° ````");          ass(new DbCnd(" :dbg:+ " , "a", "b", "c", "d").val().Equals("(((a, b) + c) + d) + "));    ass(new DbCnd(" :dbg:+ " , "a", "b", "c").val().Equals("((a, b) + c) + "));          ass(new DbCnd(" :dbg:+ " , new object[] {"a", "b"}).val().Equals("(a, b) + "));            ass(new DbCnd(" :dbg:+ ", "a").val().Equals("a,")); //here we want to get the postfix notation with brackets like follows: "(((a, b) + c) + d) + "
   _dtv.Set(" :dbg:|| ", "OR``¹``>(´¹ ²)´`` ° ²``");          ass(new DbCnd(" :dbg:|| ", "a", "b", "c", "d").val().Equals("(a OR b OR c OR d)"));       ass(new DbCnd(" :dbg:|| ", "a", "b", "c").val().Equals("(a OR b OR c)"));            ass(new DbCnd(" :dbg:|| ", new object[] { "a", "b" }).val().Equals("(a OR b)"));           ass(new DbCnd(" :dbg:|| ", "a").val().Equals("a"));
   _dtv.Set(" :dbg:|| ", "OR``¹``>°(´¹²)´``, ²``");           ass(new DbCnd(" :dbg:|| ", "a", "b", "c", "d").val().Equals("OR(a, b, c, d)"));           ass(new DbCnd(" :dbg:|| ", "a", "b", "c").val().Equals("OR(a, b, c)"));              ass(new DbCnd(" :dbg:|| ", new object[] { "a", "b" }).val().Equals("OR(a, b)"));           ass(new DbCnd(" :dbg:|| ", "a").val().Equals("a"));
   _dtv.Set(" :dbg:|| ", "OR``¹``>´°(¹´, ²´)````");           ass(new DbCnd(" :dbg:|| ", "a", "b", "c", "d").val().Equals("OR(OR(OR(a, b), c), d)"));   ass(new DbCnd(" :dbg:|| ", "a", "b", "c").val().Equals("OR(OR(a, b), c)"));          ass(new DbCnd(" :dbg:|| ", new object[] { "a", "b" }).val().Equals("OR(a, b)"));           ass(new DbCnd(" :dbg:|| ", "a").val().Equals("a"));
   _dtv.Set(" :dbg:|| ", "OR``¹``<´°(¹´, ²´)````");           ass(new DbCnd(" :dbg:|| ", "a", "b", "c", "d").val().Equals("OR(a, OR(b, OR(c, d)))"));   ass(new DbCnd(" :dbg:|| ", "a", "b", "c").val().Equals("OR(a, OR(b, c))"));          ass(new DbCnd(" :dbg:|| ", new object[] { "a", "b" }).val().Equals("OR(a, b)"));           ass(new DbCnd(" :dbg:|| ", "a").val().Equals("a"));
   _dtv.Set(" :dbg:|| ", "OR``¹, ``>´¹,´ ²´ °````");          ass(new DbCnd(" :dbg:|| ", "a", "b", "c", "d").val().Equals("a, b OR c OR d OR"));        ass(new DbCnd(" :dbg:|| ", "a", "b", "c").val().Equals("a, b OR c OR"));             ass(new DbCnd(" :dbg:|| ", new object[] { "a", "b" }).val().Equals("a, b OR"));            ass(new DbCnd(" :dbg:|| ", "a").val().Equals("a, "));
   _dtv.Set(" :dbg:|| ", "OR``¹, ``<´¹´, ²´ °````");          ass(new DbCnd(" :dbg:|| ", "a", "b", "c", "d").val().Equals("a, b, c, d OR OR OR"));      ass(new DbCnd(" :dbg:|| ", "a", "b", "c").val().Equals("a, b, c OR OR"));            ass(new DbCnd(" :dbg:|| ", new object[] { "a", "b" }).val().Equals("a, b OR"));            ass(new DbCnd(" :dbg:|| ", "a").val().Equals("a, "));
   _dtv.Set(" :dbg:|| ", "OR````>°(¹²)``, ²``");              ass(new DbCnd(" :dbg:|| ", "a", "b", "c", "d").val().Equals("OR(a, b, c, d)"));           ass(new DbCnd(" :dbg:|| ", "a", "b", "c").val().Equals("OR(a, b, c)"));              ass(new DbCnd(" :dbg:|| ", new object[] { "a", "b" }).val().Equals("OR(a, b)"));           ass(new DbCnd(" :dbg:|| ", "a").val().Equals("OR(a)"));
   _dtv.Set(" :dbg:|| ", "OR````<°(¹²)``, ²``");              ass(new DbCnd(" :dbg:|| ", "a", "b", "c", "d").val().Equals("OR(a, b, c, d)"));           ass(new DbCnd(" :dbg:|| ", "a", "b", "c").val().Equals("OR(a, b, c)"));              ass(new DbCnd(" :dbg:|| ", new object[] { "a", "b" }).val().Equals("OR(a, b)"));           ass(new DbCnd(" :dbg:|| ", "a").val().Equals("OR(a)"));
   //init the static directives Pile:
   //_dtv.clear();
  
  
  }

  public DbCnd(Reach                                lOpnd) : base("",                lOpnd) { init(); }

  public DbCnd(object lOpnd, Reach cptor,    object rOpnd) : base(cptor.text, lOpnd, rOpnd) { init();}

  public DbCnd(                                          ) : base(                        ) { init(); }
  public DbCnd(long                                 count) : base(count                   ) { init(); }
  public DbCnd(params object[]                       opnd) : base(opnd                    ) { init(); }
  public DbCnd(Pile<object>                          opnd) : base(opnd                    ) { init(); }
  public DbCnd(string optor,         params object[] opnd) : base(optor,              opnd) { init(); }
  public DbCnd(string optor,            Pile<object> opnd) : base(optor,              opnd) { init(); }

  public override string val(int i, EvalExpert evx)
  {
   Type typ = this[i].GetType();
   if (typ == typeof(Trm))    return ((Trm)(this[i])).val(evx);
   if (typ == typeof(DbCnd))  return (evx.GetType() == typeof(Db)) ? ((DbCnd)this[i]).sql((Db)evx) : ((DbCnd)this[i]).sql();
   if (typ == typeof(DbSlc))  return (evx.GetType() == typeof(Db)) ? ((DbSlc)this[i]).sql((Db)evx) : ((DbSlc)this[i]).sql();
   if (typ == typeof(Reach))  return ((Reach)(this[i])).text;
   if (typ == typeof(string)) return ((string)(this[i]));
   if (typ == typeof(long))   return ((Db)evx).ds("" + ((long)(this[i])));
   if (typ == typeof(double)) return ((Db)evx).ds("" + ((double)(this[i]))).Replace(",", ".");
   throw new Exception("Err: unknown Type in Cnd.val()");
  }

  public static Pile<DbCnd> fromTxtDef(string def)
  {
   //AttGeTr TBD TO BE DONE !!!
   return new Pile<DbCnd>();
  }
  
  public static Pile<DbCnd> fromSymbolicDef(Reach def)
  {
   if (def.at(".OR").len > 0) def = def;
   Pile<DbCnd> ret = new Pile<DbCnd>();
   Zone   bktFilter   = new Zone(new Pile<string>(), new Pile<string>("", true, "(", ")", "||:1"));
   Zone   strFilter   = new Zone(new Pile<string>(), new Pile<string>("", true, "\"", "\"", "||:1"));
   DbCnd c = null;
   while(def.len > 0)
   {
    Reach left    = bktFilter.on(def.after(1, "cd"));
    if (left.startsWith("\"")) left    = strFilter.on(bktFilter.on(def.after(1, "cd")));
    Reach mid     = def.after(left).after(1, ".").before(1, "(");
    Reach right   = bktFilter.on(def.after(mid));
    if (right.startsWith("\"")) right   = strFilter.on(bktFilter.on(def.after(mid)));
    if (right.endsWith(".SLD"))
    {
     if (mid.equals("LK"))  ret.Push(new DbCnd(left).LK (new DbGrid(right).SLD));
     if (mid.equals("nLK")) ret.Push(new DbCnd(left).nLK(new DbGrid(right).SLD));
     if (mid.equals("IS"))  ret.Push(new DbCnd(left).IS (new DbGrid(right).SLD));
     if (mid.equals("nIS")) ret.Push(new DbCnd(left).nIS(new DbGrid(right).SLD));
     if (mid.equals("IN"))  ret.Push(new DbCnd(left).IN (new DbGrid(right).SLD));
     if (mid.equals("nIN")) ret.Push(new DbCnd(left).nIN(new DbGrid(right).SLD));
     if (mid.equals("MV"))  ret.Push(new DbCnd(left).MV (new DbGrid(right).SLD));
     if (mid.equals("nMV")) ret.Push(new DbCnd(left).nMV(new DbGrid(right).SLD));
     if (mid.equals("MU"))  ret.Push(new DbCnd(left).MU (new DbGrid(right).SLD));
     if (mid.equals("nMU")) ret.Push(new DbCnd(left).nMU(new DbGrid(right).SLD));
     if (mid.equals("ML"))  ret.Push(new DbCnd(left).ML (new DbGrid(right).SLD));
     if (mid.equals("nML")) ret.Push(new DbCnd(left).nML(new DbGrid(right).SLD));
     if (mid.equals("XS"))  ret.Push(new DbCnd(left).XS (new DbGrid(right).SLD));
     if (mid.equals("nXS")) ret.Push(new DbCnd(left).nXS(new DbGrid(right).SLD));
     if (mid.equals("EQ"))  ret.Push(new DbCnd(left).EQ (new DbGrid(right).SLD));
     if (mid.equals("nEQ")) ret.Push(new DbCnd(left).nEQ(new DbGrid(right).SLD));
     if (mid.equals("GT"))  ret.Push(new DbCnd(left).GT (new DbGrid(right).SLD));
     if (mid.equals("LT"))  ret.Push(new DbCnd(left).LT (new DbGrid(right).SLD));
     if (mid.equals("LE"))  ret.Push(new DbCnd(left).LE (new DbGrid(right).SLD));
     if (mid.equals("GE"))  ret.Push(new DbCnd(left).GE (new DbGrid(right).SLD));
     //if (mid.equals("AND")) ret.Push(new Cnd(left.text).nIN(new DbGrid(right).SLD));
     //if (mid.equals("OR")) ret.Push(new Cnd(left.text).nIN(new DbGrid(right).SLD));
    }
    else
    if (right.endsWith(".SLC"))
    {
     if (mid.equals("LK"))  ret.Push(new DbCnd(left).LK (new DbGrid(right).SLC));
     if (mid.equals("nLK")) ret.Push(new DbCnd(left).nLK(new DbGrid(right).SLC));
     if (mid.equals("IS"))  ret.Push(new DbCnd(left).IS (new DbGrid(right).SLC));
     if (mid.equals("nIS")) ret.Push(new DbCnd(left).nIS(new DbGrid(right).SLC));
     if (mid.equals("IN"))  ret.Push(new DbCnd(left).IN (new DbGrid(right).SLC));
     if (mid.equals("nIN")) ret.Push(new DbCnd(left).nIN(new DbGrid(right).SLC));
     if (mid.equals("MV"))  ret.Push(new DbCnd(left).MV (new DbGrid(right).SLC));
     if (mid.equals("nMV")) ret.Push(new DbCnd(left).nMV(new DbGrid(right).SLC));
     if (mid.equals("MU"))  ret.Push(new DbCnd(left).MU (new DbGrid(right).SLC));
     if (mid.equals("nMU")) ret.Push(new DbCnd(left).nMU(new DbGrid(right).SLC));
     if (mid.equals("ML"))  ret.Push(new DbCnd(left).ML (new DbGrid(right).SLC));
     if (mid.equals("nML")) ret.Push(new DbCnd(left).nML(new DbGrid(right).SLC));
     if (mid.equals("XS"))  ret.Push(new DbCnd(left).XS (new DbGrid(right).SLC));
     if (mid.equals("nXS")) ret.Push(new DbCnd(left).nXS(new DbGrid(right).SLC));
     if (mid.equals("EQ"))  ret.Push(new DbCnd(left).EQ (new DbGrid(right).SLC));
     if (mid.equals("nEQ")) ret.Push(new DbCnd(left).nEQ(new DbGrid(right).SLC));
     if (mid.equals("GT"))  ret.Push(new DbCnd(left).GT (new DbGrid(right).SLC));
     if (mid.equals("LT"))  ret.Push(new DbCnd(left).LT (new DbGrid(right).SLC));
     if (mid.equals("LE"))  ret.Push(new DbCnd(left).LE (new DbGrid(right).SLC));
     if (mid.equals("GE"))  ret.Push(new DbCnd(left).GE (new DbGrid(right).SLC));
     //if (mid.equals("AND")) ret.Push(new Cnd(left.text).nIN(new DbGrid(right).SLC));
     //if (mid.equals("OR")) ret.Push(new Cnd(left.text).nIN(new DbGrid(right).SLC));
    }
    else
    {
     if (right.startsWith("ds(")) right = utl.ds(false, strFilter.on(bktFilter.on(right)));
     if (right.startsWith("dS(")) right = utl.dS(false, strFilter.on(bktFilter.on(right)));
     if (right.startsWith("Ds(")) right = utl.Ds(false, strFilter.on(bktFilter.on(right)));
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
     //if (mid.equals("AND")) ret.Push(new Cnd(left.text).nIN(right));
     //if (mid.equals("OR")) ret.Push(new Cnd(left.text).nIN(right));
    }
    def = def.after(right).after(1, ")").Trim();
    if (def.startsWith(")")) def = ""; else def = def.after(1, ",").Trim();
   }
   return ret;
  }


  public DbCnd LK    (Reach  rOpnd)  { this.Push(rOpnd);                                   optor = utl.CPR("LIKE");      return this; }
  public DbCnd LK    (long   rOpnd)  { this.Push((Reach)("" + rOpnd));                     optor = utl.CPR("LIKE");      return this; }
  public DbCnd LK    (double rOpnd)  { this.Push((Reach)("" + rOpnd).Replace(",", "."));   optor = utl.CPR("LIKE");      return this; }
  public DbCnd LK    (DbSlc  rOpnd)  { this.Push(rOpnd);                                   optor = utl.CPR("LIKE");      return this; }
  public DbCnd LK    (DbGrid rOpnd)  { this.Push(new DbSlc(rOpnd));                        optor = utl.CPR("LIKE");      return this; }
  public DbCnd nLK   (Reach  rOpnd)  { this.Push(rOpnd);                                   optor = utl.CPR("NOT_LIKE");  return this; }
  public DbCnd nLK   (long   rOpnd)  { this.Push((Reach)("" + rOpnd));                     optor = utl.CPR("NOT_LIKE");  return this; }
  public DbCnd nLK   (double rOpnd)  { this.Push((Reach)("" + rOpnd).Replace(",", "."));   optor = utl.CPR("NOT_LIKE");  return this; }
  public DbCnd nLK   (DbSlc  rOpnd)  { this.Push(rOpnd);                                   optor = utl.CPR("NOT_LIKE");  return this; }
  public DbCnd nLK   (DbGrid rOpnd)  { this.Push(new DbSlc(rOpnd));                        optor = utl.CPR("NOT_LIKE");  return this; }

  public DbCnd IS    ()              { this.Push("NULL");                                  optor = utl.CPR("IS");        return this; }
  public DbCnd IS    (Reach  rOpnd)  { this.Push(rOpnd);                                   optor = utl.CPR("IS");        return this; }
  public DbCnd IS    (long   rOpnd)  { this.Push((Reach)("" + rOpnd));                     optor = utl.CPR("IS");        return this; }
  public DbCnd IS    (double rOpnd)  { this.Push((Reach)("" + rOpnd).Replace(",", "."));   optor = utl.CPR("IS");        return this; }
  public DbCnd IS    (DbSlc  rOpnd)  { this.Push(rOpnd);                                   optor = utl.CPR("IS");        return this; }
  public DbCnd nIS   ()              { this.Push("NULL");                                  optor = utl.CPR("NOT_IS");    return this; }
  public DbCnd nIS   (Reach  rOpnd)  { this.Push(rOpnd);                                   optor = utl.CPR("NOT_IS");    return this; }
  public DbCnd nIS   (long   rOpnd)  { this.Push((Reach)("" + rOpnd));                     optor = utl.CPR("NOT_IS");    return this; }
  public DbCnd nIS   (double rOpnd)  { this.Push((Reach)("" + rOpnd).Replace(",", "."));   optor = utl.CPR("NOT_IS");    return this; }
  public DbCnd nIS   (DbSlc  rOpnd)  { this.Push(rOpnd);                                   optor = utl.CPR("NOT_IS");    return this; }

  public DbCnd IN    (Reach  rOpnd)  { this.Push((Reach)("(" + rOpnd.text + ")"));         optor = utl.CPR("IN");        return this; }
  public DbCnd IN    (long   rOpnd)  { this.Push((Reach)("" + rOpnd));                     optor = utl.CPR("IN");        return this; }
  public DbCnd IN    (double rOpnd)  { this.Push((Reach)("" + rOpnd).Replace(",", "."));   optor = utl.CPR("IN");        return this; }
  public DbCnd IN    (DbSlc  rOpnd)  { this.Push(rOpnd);                                   optor = utl.CPR("IN");        return this; }
  public DbCnd nIN   (Reach  rOpnd)  { this.Push((Reach)("(" + rOpnd.text + ")"));         optor = utl.CPR("NOT_IN");    return this; }
  public DbCnd nIN   (long   rOpnd)  { this.Push((Reach)("" + rOpnd));                     optor = utl.CPR("NOT_IN");    return this; }
  public DbCnd nIN   (double rOpnd)  { this.Push((Reach)("" + rOpnd).Replace(",", "."));   optor = utl.CPR("NOT_IN");    return this; }
  public DbCnd nIN   (DbSlc  rOpnd)  { this.Push(rOpnd);                                   optor = utl.CPR("NOT_IN");    return this; }

  // MV = "matches values"
  public DbCnd MV    (Reach  rOpnd)  { this.Push(rOpnd);                                   optor = utl.CPR("MV");        return this; }
  public DbCnd MV    (long   rOpnd)  { this.Push((Reach)("" + rOpnd));                     optor = utl.CPR("MV");        return this; }
  public DbCnd MV    (double rOpnd)  { this.Push((Reach)("" + rOpnd).Replace(",", "."));   optor = utl.CPR("MV");        return this; }
  public DbCnd MV    (DbSlc  rOpnd)  { this.Push(rOpnd);                                   optor = utl.CPR("MV");        return this; }
  public DbCnd nMV   (Reach  rOpnd)  { this.Push(rOpnd);                                   optor = utl.CPR("NOT_MV");    return this; }
  public DbCnd nMV   (long   rOpnd)  { this.Push((Reach)("" + rOpnd));                     optor = utl.CPR("NOT_MV");    return this; }
  public DbCnd nMV   (double rOpnd)  { this.Push((Reach)("" + rOpnd).Replace(",", "."));   optor = utl.CPR("NOT_MV");    return this; }
  public DbCnd nMV   (DbSlc  rOpnd)  { this.Push(rOpnd);                                   optor = utl.CPR("NOT_MV");    return this; }

  // MU = "matches uppercase values"
  public DbCnd MU    (Reach  rOpnd)  { this.Push(rOpnd);                                   optor = utl.CPR("MU");        return this; }
  public DbCnd MU    (long   rOpnd)  { this.Push((Reach)("" + rOpnd));                     optor = utl.CPR("MU");        return this; }
  public DbCnd MU    (double rOpnd)  { this.Push((Reach)("" + rOpnd).Replace(",", "."));   optor = utl.CPR("MU");        return this; }
  public DbCnd MU    (DbSlc  rOpnd)  { this.Push(rOpnd);                                   optor = utl.CPR("MU");        return this; }
  public DbCnd nMU   (Reach  rOpnd)  { this.Push(rOpnd);                                   optor = utl.CPR("NOT_MU");    return this; }
  public DbCnd nMU   (long   rOpnd)  { this.Push((Reach)("" + rOpnd));                     optor = utl.CPR("NOT_MU");    return this; }
  public DbCnd nMU   (double rOpnd)  { this.Push((Reach)("" + rOpnd).Replace(",", "."));   optor = utl.CPR("NOT_MU");    return this; }
  public DbCnd nMU   (DbSlc  rOpnd)  { this.Push(rOpnd);                                   optor = utl.CPR("NOT_MU");    return this; }

  // ML = "matches lowercase values"
  public DbCnd ML    (Reach  rOpnd)  { this.Push(rOpnd);                                   optor = utl.CPR("ML");        return this; }
  public DbCnd ML    (long   rOpnd)  { this.Push((Reach)("" + rOpnd));                     optor = utl.CPR("ML");        return this; }
  public DbCnd ML    (double rOpnd)  { this.Push((Reach)("" + rOpnd).Replace(",", "."));   optor = utl.CPR("ML");        return this; }
  public DbCnd ML    (DbSlc  rOpnd)  { this.Push(rOpnd);                                   optor = utl.CPR("ML");        return this; }
  public DbCnd nML   (Reach  rOpnd)  { this.Push(rOpnd);                                   optor = utl.CPR("NOT_ML");    return this; }
  public DbCnd nML   (long   rOpnd)  { this.Push((Reach)("" + rOpnd));                     optor = utl.CPR("NOT_ML");    return this; }
  public DbCnd nML   (double rOpnd)  { this.Push((Reach)("" + rOpnd).Replace(",", "."));   optor = utl.CPR("NOT_ML");    return this; }
  public DbCnd nML   (DbSlc  rOpnd)  { this.Push(rOpnd);                                   optor = utl.CPR("NOT_ML");    return this; }

  public DbCnd XS    (Reach  rOpnd)  { this.Push("(" + rOpnd + ")");                       optor = utl.CPR("XS");        return this; }
  public DbCnd XS    (long   rOpnd)  { this.Push((Reach)("" + rOpnd));                     optor = utl.CPR("XS");        return this; }
  public DbCnd XS    (double rOpnd)  { this.Push((Reach)("" + rOpnd).Replace(",", "."));   optor = utl.CPR("XS");        return this; }
  public DbCnd XS    (DbSlc  rOpnd)  { this.Push(rOpnd);                                   optor = utl.CPR("XS");        return this; }
  public DbCnd nXS   (Reach  rOpnd)  { this.Push("(" + rOpnd + ")");                       optor = utl.CPR("NOT_XS");    return this; }
  public DbCnd nXS   (long   rOpnd)  { this.Push((Reach)("" + rOpnd));                     optor = utl.CPR("NOT_XS");    return this; }
  public DbCnd nXS   (double rOpnd)  { this.Push((Reach)("" + rOpnd).Replace(",", "."));   optor = utl.CPR("NOT_XS");    return this; }
  public DbCnd nXS   (DbSlc  rOpnd)  { this.Push(rOpnd);                                   optor = utl.CPR("NOT_XS");    return this; }

  public DbCnd EQ    ()              { this.Push("''");                                    optor = utl.CPR("EQ");        return this; }
  public DbCnd EQ    (Reach  rOpnd)  { this.Push(rOpnd);                                   optor = utl.CPR("EQ");        return this; }
  public DbCnd EQ    (long   rOpnd)  { this.Push((Reach)("" + rOpnd));                     optor = utl.CPR("EQ");        return this; }
  public DbCnd EQ    (double rOpnd)  { this.Push((Reach)("" + rOpnd).Replace(",", "."));   optor = utl.CPR("EQ");        return this; }
  public DbCnd EQ    (DbSlc  rOpnd)  { this.Push(rOpnd);                                   optor = utl.CPR("EQ");        return this; }
  public DbCnd nEQ   ()              { this.Push("''");                                    optor = utl.CPR("NOT_EQ");    return this; }
  public DbCnd nEQ   (Reach  rOpnd)  { this.Push(rOpnd);                                   optor = utl.CPR("NOT_EQ");    return this; }
  public DbCnd nEQ   (long   rOpnd)  { this.Push((Reach)("" + rOpnd));                     optor = utl.CPR("NOT_EQ");    return this; }
  public DbCnd nEQ   (double rOpnd)  { this.Push((Reach)("" + rOpnd).Replace(",", "."));   optor = utl.CPR("NOT_EQ");    return this; }
  public DbCnd nEQ   (DbSlc  rOpnd)  { this.Push(rOpnd);                                   optor = utl.CPR("NOT_EQ");    return this; }

  public DbCnd GT    ()              { this.Push(0);                                       optor = utl.CPR("GT");        return this; }
  public DbCnd GT    (Reach  rOpnd)  { this.Push(rOpnd);                                   optor = utl.CPR("GT");        return this; }
  public DbCnd GT    (long   rOpnd)  { this.Push((Reach)("" + rOpnd));                     optor = utl.CPR("GT");        return this; }
  public DbCnd GT    (double rOpnd)  { this.Push((Reach)("" + rOpnd).Replace(",", "."));   optor = utl.CPR("GT");        return this; }
  public DbCnd GT    (DbSlc  rOpnd)  { this.Push(rOpnd);                                   optor = utl.CPR("GT");        return this; }

  public DbCnd LT    ()              { this.Push(0);                                       optor = utl.CPR("LT");        return this; }
  public DbCnd LT    (Reach  rOpnd)  { this.Push(rOpnd);                                   optor = utl.CPR("LT");        return this; }
  public DbCnd LT    (long   rOpnd)  { this.Push((Reach)("" + rOpnd));                     optor = utl.CPR("LT");        return this; }
  public DbCnd LT    (double rOpnd)  { this.Push((Reach)("" + rOpnd).Replace(",", "."));   optor = utl.CPR("LT");        return this; }
  public DbCnd LT    (DbSlc  rOpnd)  { this.Push(rOpnd);                                   optor = utl.CPR("LT");        return this; }

  public DbCnd LE    ()              { this.Push(0);                                       optor = utl.CPR("LE");        return this; }
  public DbCnd LE    (Reach  rOpnd)  { this.Push(rOpnd);                                   optor = utl.CPR("LE");        return this; }
  public DbCnd LE    (long   rOpnd)  { this.Push((Reach)("" + rOpnd));                     optor = utl.CPR("LE");        return this; }
  public DbCnd LE    (double rOpnd)  { this.Push((Reach)("" + rOpnd).Replace(",", "."));   optor = utl.CPR("LE");        return this; }
  public DbCnd LE    (DbSlc  rOpnd)  { this.Push(rOpnd);                                   optor = utl.CPR("LE");        return this; }

  public DbCnd GE    ()              { this.Push(0);                                       optor = utl.CPR("GE");        return this; }
  public DbCnd GE    (Reach  rOpnd)  { this.Push(rOpnd);                                   optor = utl.CPR("GE");        return this; }
  public DbCnd GE    (long   rOpnd)  { this.Push((Reach)("" + rOpnd));                     optor = utl.CPR("GE");        return this; }
  public DbCnd GE    (double rOpnd)  { this.Push((Reach)("" + rOpnd).Replace(",", "."));   optor = utl.CPR("GE");        return this; }
  public DbCnd GE    (DbSlc  rOpnd)  { this.Push(rOpnd);                                   optor = utl.CPR("GE");        return this; }

  //public DbCnd OR    (DbCnd theOther)  { return new DbCnd(this, utl.OPR("OR")    , theOther); }
  //public DbCnd AND   (DbCnd theOther)  { return new DbCnd(this, utl.OPR("AND")   , theOther); }

  public DbCnd OR    (params DbCnd[] other) 
  {
   DbCnd ret = this;
   for (int i = 0; i < other.Length; i++)
    ret = new DbCnd(ret, utl.OPR("OR"), other[i]);
   return ret;
  }

  public DbCnd AND   (params DbCnd[] other) { DbCnd ret = this; for (int i = 0; i < other.Length; i++) ret = new DbCnd(ret, utl.OPR("AND") , other[i]);  return ret; }
  public DbCnd MNS   (params DbCnd[] other) { DbCnd ret = this; for (int i = 0; i < other.Length; i++) ret = new DbCnd(ret, utl.OPR("MNS") , other[i]);  return ret; }

  public Reach sql() { return sql(this); } //return sql(new Db(_o.DbDrivers)); }

  public Reach sql(EvalExpert evx)
  {
   //if (evc.Dtv.Keys.Contains(optor)) return val(evc);  //AttGeTr: the evaluation should be automated with the val() Method. but before all the Operalnds Directives (dtv) must be defined ...
   if (evx.Dtv.hasKey(optor)) return val(evx);
   //if (Dtv.hasKey(optor)) return val(this);
   if (optor.Equals("")) return "";
   Reach ret = "(";
   if (lOpnd.GetType() == typeof(DbCnd)) ret += ((DbCnd)lOpnd).sql(evx); else if (lOpnd.GetType() == typeof(DbSlc)) ret += "( " + evx.val(((DbSlc)lOpnd)) + " )"; else ret += (Reach)lOpnd;
   if      (optor.Equals(utl.CPR("LIKE")))     ret += " LIKE ";
   else if (optor.Equals(utl.CPR("NOT_LIKE"))) ret += " NOT LIKE ";
   else if (optor.Equals(utl.CPR("IS")))       ret += optor;
   else if (optor.Equals(utl.CPR("IN")))       ret += " IN ";
   else if (optor.Equals(utl.CPR("NOT_IN")))   ret += " NOT IN ";
   else if (optor.Equals(utl.CPR("MV")))       return ((Db)evx).lot(ret.from(2).text, (Reach)((Reach)rOpnd).text.Replace("\r\n", "\r").Replace("\n", "\r"), "\r").sql(evx);
   else if (optor.Equals(utl.CPR("NOT_MV")))   ret += " NOT MV ";
   else if (optor.Equals(utl.CPR("MU")))       return ((Db)evx).loT(ret.from(2).text, (Reach)((Reach)rOpnd).text.Replace("\r\n", "\r").Replace("\n", "\r"), "\r").sql(evx);
   else if (optor.Equals(utl.CPR("NOT_MU")))   ret += " NOT MU ";
   else if (optor.Equals(utl.CPR("ML")))       return ((Db)evx).Lot(ret.from(2).text, (Reach)((Reach)rOpnd).text.Replace("\r\n", "\r").Replace("\n", "\r"), "\r").sql(evx);
   else if (optor.Equals(utl.CPR("NOT_ML")))   ret += " NOT ML ";
   else if (optor.Equals(utl.CPR("XS")))       ret += " EXISTS ";
   else if (optor.Equals(utl.CPR("NOT_XS")))   ret += " NOT EXISTS ";
   else if (optor.Equals(utl.CPR("NOT_IS")))   ret += optor;
   else if (optor.Equals(utl.CPR("NOT_EQ")))   ret += " <> ";
   else if (optor.Equals(utl.CPR("EQ")))       ret += optor;
   else if (optor.Equals(utl.CPR("GT")))       ret += " > ";
   else if (optor.Equals(utl.CPR("LT")))       ret += " < ";
   else if (optor.Equals(utl.CPR("LE")))       ret += " <= ";
   else if (optor.Equals(utl.CPR("GE")))       ret += " >= ";
   else if (optor.Equals(utl.OPR("APD")))      ret += " _ ";
   else if (optor.Equals(utl.OPR("CCT")))      ret += " & ";
   else if (optor.Equals(utl.OPR("CVS")))      ret += " >$ ";
   else if (optor.Equals(utl.OPR("AND")))      ret += " AND ";
   else if (optor.Equals(utl.OPR("MNS")))      ret += " AND NOT ";
   else if (optor.Equals(utl.OPR("OR")))       ret += " OR ";
   else if (optor.Equals(utl.OPR("XOR")))      ret += " XOR ";
   if (rOpnd.GetType() == typeof(DbCnd)) ret += ((DbCnd)rOpnd).sql(evx); else if (rOpnd.GetType() == typeof(DbSlc)) ret += "( " + evx.val(((DbSlc)rOpnd)) + " )"; else ret += (Reach)rOpnd;
   ret += ")";
   return ret;
  }

  /*
  public Reach _sql()
  {
   //if (Dtv.Keys.Contains(optor)) return val(this);    //AttGeTr: the evaluation should be automated with the val() Method. but before all the Operalnds Directives (dtv) must be defined ...
   if (optor.Equals("")) return "";
   Reach ret = "(";
   if (lOpnd.GetType() == typeof(Cnd)) ret += ((Cnd)lOpnd)._sql(); else if (lOpnd.GetType() == typeof(DbSlc)) ret += "( " + ((DbSlc)lOpnd).sql() + " )"; else ret += (Reach)lOpnd;
   if      (optor.Equals(utl.CPR("LIKE")))     ret += " LIKE ";
   else if (optor.Equals(utl.CPR("NOT_LIKE"))) ret += " NOT LIKE ";
   else if (optor.Equals(utl.CPR("IS")))       ret += optor;
   else if (optor.Equals(utl.CPR("IN")))       ret += " IN ";
   else if (optor.Equals(utl.CPR("NOT_IN")))   ret += " NOT IN ";
   else if (optor.Equals(utl.CPR("MV")))       return Db._lot(ret.from(2).text, (Reach)((Reach)rOpnd).text.Replace("\r\n", "\r").Replace("\n", "\r"), "\r")._sql();
   else if (optor.Equals(utl.CPR("NOT_MV")))   ret += " NOT MV ";
   else if (optor.Equals(utl.CPR("MU")))       return Db._loT(ret.from(2).text, (Reach)((Reach)rOpnd).text.Replace("\r\n", "\r").Replace("\n", "\r"), "\r")._sql();
   else if (optor.Equals(utl.CPR("NOT_MU")))   ret += " NOT MU ";
   else if (optfor.Equals(utl.CPR("ML")))       return Db._Lot(ret.from(2).text, (Reach)((Reach)rOpnd).text.Replace("\r\n", "\r").Replace("\n", "\r"), "\r")._sql();
   else if (optor.Equals(utl.CPR("NOT_ML")))   ret += " NOT ML ";
   else if (optor.Equals(utl.CPR("XS")))       ret += " EXISTS ";
   else if (optor.Equals(utl.CPR("NOT_XS")))   ret += " NOT EXISTS ";
   else if (optor.Equals(utl.CPR("NOT_IS")))   ret += optor;
   else if (optor.Equals(utl.CPR("NOT_EQ")))   ret += " <> ";
   else if (optor.Equals(utl.CPR("EQ")))       ret += optor;
   else if (optor.Equals(utl.CPR("GT")))       ret += " > ";
   else if (optor.Equals(utl.CPR("LT")))       ret += " < ";
   else if (optor.Equals(utl.CPR("LE")))       ret += " <= ";
   else if (optor.Equals(utl.CPR("GE")))       ret += " >= ";
   else if (optor.Equals(utl.OPR("APD")))      ret += " _ ";
   else if (optor.Equals(utl.OPR("CCT")))      ret += " & ";
   else if (optor.Equals(utl.OPR("CVS")))      ret += " >$ ";
   else if (optor.Equals(utl.OPR("AND")))      ret += " AND ";
   else if (optor.Equals(utl.OPR("MNS")))      ret += " AND NOT ";
   else if (optor.Equals(utl.OPR("OR")))       ret += " OR ";
   else if (optor.Equals(utl.OPR("XOR")))      ret += " XOR ";
   if (rOpnd.GetType() == typeof(Cnd)) ret += ((Cnd)rOpnd)._sql(); else if (rOpnd.GetType() == typeof(DbSlc)) ret += "( " + ((DbSlc)rOpnd).sql() + " )"; else ret += (Reach)rOpnd;
   ret += ")";
   return ret;
  }
  */

  public Reach smb()
  {
   if (optor.Equals("")) return "";

   Reach ret = "cd(";
   if ((optor.Equals(utl.OPR("OR"))) || (optor.Equals(utl.OPR("AND"))) || (optor.Equals(utl.OPR("XOR")))) ret  = "";

   if (lOpnd.GetType() == typeof(DbCnd)) ret += ((DbCnd)lOpnd).smb(); else if (lOpnd.GetType() == typeof(DbSlc)) ret += "( " + ((DbSlc)lOpnd).smb() + " )"; else if (((Reach)lOpnd).startsWith("'")) ret += "ds(\"" + ((Reach)lOpnd).from(2).upto(-2) + "\")"; else ret += "\"" + (Reach)lOpnd + "\"";

   if (optor.Equals(utl.CPR("LIKE"))) ret += ").LK(";
   else if (optor.Equals(utl.CPR("NOT_LIKE"))) ret += ").nLK(";
   else if (optor.Equals(utl.CPR("IS"))) ret += ").IS(";
   else if (optor.Equals(utl.CPR("IN"))) ret += ").IN(";
   else if (optor.Equals(utl.CPR("NOT_IN"))) ret += ").nIN(";
   else if (optor.Equals(utl.CPR("MV"))) ret += ").MV(";
   else if (optor.Equals(utl.CPR("NOT_MV"))) ret += ").nMV(";
   else if (optor.Equals(utl.CPR("MU"))) ret += ").MU(";
   else if (optor.Equals(utl.CPR("NOT_MU"))) ret += ").nMU(";
   else if (optor.Equals(utl.CPR("XS"))) ret += ").XS(";
   else if (optor.Equals(utl.CPR("NOT_XS"))) ret += ").nXS(";
   else if (optor.Equals(utl.CPR("NOT_IS"))) ret += ").IS(";
   else if (optor.Equals(utl.CPR("NOT_EQ"))) ret += ").nEQ(";
   else if (optor.Equals(utl.CPR("EQ"))) ret += ").EQ(";
   else if (optor.Equals(utl.CPR("GT"))) ret += ").GT(";
   else if (optor.Equals(utl.CPR("LT"))) ret += ").LT(";
   else if (optor.Equals(utl.CPR("LE"))) ret += ").LE(";
   else if (optor.Equals(utl.CPR("GE"))) ret += ").GE(";
   else if (optor.Equals(utl.OPR("APD"))) ret += ").APD(";
   else if (optor.Equals(utl.OPR("CCT"))) ret += ").CCT(";
   else if (optor.Equals(utl.OPR("CVS"))) ret += ").CVS(";
   else if (optor.Equals(utl.OPR("AND"))) ret += ".AND(";
   else if (optor.Equals(utl.OPR("OR"))) ret += ".OR(";
   else if (optor.Equals(utl.OPR("XOR"))) ret += ".XOR(";

   if (rOpnd.GetType() == typeof(double)) rOpnd = rOpnd;
   if (rOpnd.GetType() == typeof(float)) rOpnd = rOpnd;

   if (rOpnd.GetType() == typeof(DbCnd)) ret += ((DbCnd)rOpnd).smb(); else if (rOpnd.GetType() == typeof(DbSlc)) ret += "( " + ((DbSlc)rOpnd).smb() + " )"; else if (((Reach)rOpnd).startsWith("'")) ret += "ds(\"" + ((Reach)rOpnd).from(2).upto(-2) + "\")"; else ret += "\""  + (Reach)rOpnd + "\"";
   //if ((optor.equals(utl.OPR("OR"))) || (optor.equals(utl.OPR("AND"))) || (optor.equals(utl.OPR("XOR")))) ret  += ")";
   ret += ")";
   return ret;
  }

  
  
  
 }
}

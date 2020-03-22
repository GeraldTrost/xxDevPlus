/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.xxdevplus.data;

import org.xxdevplus.frmlng.Trm;
import org.xxdevplus.frmlng.EvalExpert;
import org.xxdevplus.struct.KeyPile;
import org.xxdevplus.utl.utl;
import org.xxdevplus.chain.Chain;

public class DbField extends Trm
{
 //Copyright© 1996-2020 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties, You may use, modify, distribute this code under GNU public licence for NON-Commercial use as long as this notice remains unchanged.
 private static boolean selfTested  = false; private static String ass(boolean expr) throws Exception { if (!expr) throw new Exception(ass(true) + " SelfTest Failure"); return "DbField"; }

 protected void init() throws Exception { super.init(); if (!selfTested) selfTest(); }

 private static void selfTest() throws Exception
 {
  selfTested = true;
  DbField dbf = new DbField(new Chain("abc"));
  ass(dbf.sql().equals("abc"));
  DbField dbf1 = dbf.pp("sbj");
  ass(dbf1.sql().equals("sbj.abc"));
  dbf.optor(" >$ ");
  ass(dbf.sql().equals("UPPER(abc)"));
  dbf1 = dbf.pp("sbj");
  ass(dbf1.sql().equals("UPPER(sbj.abc)"));
 }

 public  String  def           = ""; // short definition for composite fields
 private String  dataType      = "";
 public  String  DataType   () { return dataType; }

 private Chain   prePosition   = new Chain("");
 private Chain   alias         = null;
 private Chain   key           = new Chain("");
 public  Chain   Key        () { return key; }   //alias

 //public  DbField Clone() throws Exception { DbField ret = new DbField(); ret.optor(optor()); ret.Add(this); return ret; } //the super-super class Pack already provides Clone but its result cannot be casted to (DbField) in c#

 public  DbField (DbField      lOpnd                                             ) throws Exception { super("",                      lOpnd);                           init();                           }
 public  DbField (Chain        lOpnd                                             ) throws Exception { super("",                      lOpnd); key = lOpnd             ; init();                           }
 public  DbField (String       lOpnd                                             ) throws Exception { this(new Chain(lOpnd))                                        ; }   //only for jave, c# does implicit type-conversion to Chain

 public  DbField (Object       lOpnd, String optor,                  Object rOpnd) throws Exception { super(optor,            lOpnd, rOpnd); init();                                                     }
 public  DbField (Object       lOpnd, Chain  optor,                  Object rOpnd) throws Exception { super(optor.text(),     lOpnd, rOpnd); init();                                                     }
 public  DbField (                                                               ) throws Exception { super(                              ); init();                                                     }

 public  DbField (boolean      dummy, String lOpnd, String alias, String dataType) throws Exception { this(dummy, lOpnd, new Chain(alias),     dataType) ; }
 public  DbField (boolean      dummy, Chain  lOpnd, String alias, String dataType) throws Exception { this(dummy, lOpnd, new Chain(alias),     dataType) ; }
 public  DbField (boolean      dummy, String lOpnd,               String dataType) throws Exception { this(dummy, lOpnd, new Chain(""),        dataType) ; }
 public  DbField (boolean      dummy, Chain  lOpnd,               String dataType) throws Exception { this(dummy, lOpnd, new Chain(""),        dataType) ; }

 @Override
 public String val(int i, EvalExpert evx) throws Exception
 {
  Object item = g(i);
  if (false && (dbg))
  {
   if (item instanceof DbField)  return "[" + ((DbField)item).val(evx) + "]";
   if (item instanceof Trm)      return "[" + ((Trm)item).val(evx) + "]";
   if (item instanceof Chain)    return (prePosition.len() == 0) ? "[" + ((Chain)(this.g(i))).text()  + "]" : "[" + (prePosition.plus(new Chain(".")).plus((Chain)(this.g(i)))).text() + "]";
   if (item instanceof String)   return "[" + (String)item + "]";
   if (item instanceof Long)     return "[" + ((Db)evx).ds("" + ((Long)(item))) + "]";
   if (item instanceof Double)   return "[" + ((Db)evx).ds("" + ((Double)(item))).replace(",", ".") + "]";
   throw new Exception("Err: unknown Type in Trm.val()");
  }
  else
  {
   if (item instanceof DbField)  return ((DbField)item).val(evx);
   if (item instanceof Trm)      return ((Trm)item).val(evx);
   if (item instanceof Chain)    return (prePosition.len() == 0) ? ((Chain)(this.g(i))).text() : (prePosition.plus(new Chain(".")).plus((Chain)(this.g(i)))).text();
   if (item instanceof String)   return (String)item;
   if (item instanceof Long)     return ((Db)evx).ds("" + ((Long)(item)));
   if (item instanceof Double)   return ((Db)evx).ds("" + ((Double)(item))).replace(",", ".");
   throw new Exception("Err: unknown Type in Trm.val()");
  }
 }


 public  DbField (boolean      dummy, String lOpnd, Chain alias,  String dataType) throws Exception 
 {
  super("", lOpnd);
  key = alias;
  this.alias = key;
  init();
  this.dataType = dataType;
 }

 public  DbField (boolean      dummy, Chain  lOpnd, Chain alias,  String dataType) throws Exception { super("", lOpnd); key = alias; this.alias = key; init(); this.dataType = dataType; }
 
 public DbField Clone() throws Exception
 {
  DbField ret      = new DbField();
  ret.optor        ( optor() );
  ret.key          = key;
  ret.prePosition  = prePosition;
  for (Object o : this)
  {
   if (o instanceof String) ret.Push("" + (String)o);
   else
    if (o instanceof  Chain) ret.Push(((Chain)o).from(1));
    else
     if (o instanceof DbField) ret.Push((Object)((DbField)o).Clone());
     else
      if (o instanceof  Trm) ret.Push((Object)((Trm)o).Clone());
      else
       ret.Push(o);
  }
  return ret;
 } //the super-super class Pack already provides Clone but its result cannot be casted to (DbField) in c#


 public static DbField fromDef(Chain def, Db source, String table) throws Exception
 {
  DbField ret      = new DbField();
  ret.def = def.before(1, ":").Trim() + " : " + def.after(1, ":").Trim();
  def = def.after(1, ":").Trim();
  ret.optor("   ");
  while (def.len() > 0)
  {
   Chain fName = def.before(1, " ").Trim();
   boolean isNumeric = false;
   for (int i = 1; i <= source.Tables().g(table).Fields().Len(); i++) if (source.Tables().g(table).Fields().g(i).sql().equals(fName)) isNumeric = ",LONG,INT,INTEGER,DECIMAL,INT4,INT8,BIGINT,REAL,FLOAT8,DOUBLE PRECISION,FLOAT,BIT,".indexOf("," + source.Tables().g(table).Fields().g(i).DataType().toUpperCase() + ",") > -1;
   if (isNumeric) ret.Push((Object)new DbField(fName).cvs()); else ret.Push((Object)new DbField(fName).ltm().rtm());
   def = def.after(1, " ").Trim();
  }
  return ret;
 }

 public DbField pp(String prePosition) throws Exception
 {
  DbField ret = Clone();
  if (prePosition.length() == 0) return ret;
  if (Len() == 1)
  {
   if (lOpnd() instanceof Chain) if (((Chain)lOpnd()).at(1, "'").len() == 0) if (!((Chain)lOpnd()).Trim().startsWith("0123456789-+")) ret.prePosition = new Chain(prePosition);
  }
  else
  {
   for (int i = 1; i <= Len(); i++) if (this.g(i) instanceof DbField) ret.s(((DbField)this.g(i)).pp(prePosition), i);
  }
  if (ret.sql().equals("sbj.UPPER(sbj.id)"))
  {
   String s = ret.sql().text();
  }
  return ret;
 }

 public DbField tm  (             ) throws Exception { if (optor().length() > 0) return new DbField(this).tm();                                                                optor(utl.OPR("TM"));   return this; }
 public DbField ltm (             ) throws Exception { if (optor().length() > 0) return new DbField(this).ltm();                                                               optor(utl.OPR("LTM"));  return this; }
 public DbField rtm (             ) throws Exception { if (optor().length() > 0) return new DbField(this).rtm();                                                               optor(utl.OPR("RTM"));  return this; }
 public DbField cvs (             ) throws Exception { if (optor().length() > 0) return new DbField(this).cvs();                                                               optor(utl.OPR("CVS"));  return this; }
 public DbField cct (String  rOpnd) throws Exception { if (optor().length() > 0) return new DbField(this).cct(rOpnd); this.Push(new Chain(rOpnd));                             optor(utl.OPR("CCT"));  return this; }
 public DbField cct (Chain   rOpnd) throws Exception { if (optor().length() > 0) return new DbField(this).cct(rOpnd); this.Push(rOpnd);                                        optor(utl.OPR("CCT"));  return this; }
 public DbField cct (Trm     rOpnd) throws Exception { if (optor().length() > 0) return new DbField(this).cct(rOpnd); this.Push((Object)rOpnd.Clone());                        optor(utl.OPR("CCT"));  return this; }
 public DbField cct (long    rOpnd) throws Exception { if (optor().length() > 0) return new DbField(this).cct(rOpnd); this.Push(utl.ds(false, "" + rOpnd));                    optor(utl.OPR("CCT"));  return this; }
 public DbField cct (double  rOpnd) throws Exception { if (optor().length() > 0) return new DbField(this).cct(rOpnd); this.Push(utl.ds(false, "" + rOpnd).replace(",", "."));  optor(utl.OPR("CCT"));  return this; }
 public DbField cct (DbSlc   rOpnd) throws Exception { if (optor().length() > 0) return new DbField(this).cct(rOpnd); this.Push(rOpnd);                                        optor(utl.OPR("CCT"));  return this; }
 public DbField apd (String  rOpnd) throws Exception { if (optor().length() > 0) return new DbField(this).apd(rOpnd); this.Push(new Chain(rOpnd));                             optor(utl.OPR("APD"));  return this; }
 public DbField apd (Chain   rOpnd) throws Exception { if (optor().length() > 0) return new DbField(this).apd(rOpnd); this.Push(rOpnd);                                        optor(utl.OPR("APD"));  return this; }
 public DbField apd (Trm     rOpnd) throws Exception { if (optor().length() > 0) return new DbField(this).apd(rOpnd); this.Push((Object)rOpnd.Clone());                        optor(utl.OPR("APD"));  return this; }
 public DbField apd (long    rOpnd) throws Exception { if (optor().length() > 0) return new DbField(this).apd(rOpnd); this.Push(new Chain(("" + rOpnd)));                      optor(utl.OPR("APD"));  return this; }
 public DbField apd (double  rOpnd) throws Exception { if (optor().length() > 0) return new DbField(this).apd(rOpnd); this.Push(new Chain(("" + rOpnd).replace(",", ".")));    optor(utl.OPR("APD"));  return this; }
 public DbField apd (DbSlc   rOpnd) throws Exception { if (optor().length() > 0) return new DbField(this).apd(rOpnd); this.Push(rOpnd);                                        optor(utl.OPR("APD"));  return this; }

 @Override
 public Chain sql(EvalExpert evx) throws Exception { if (alias == null) return super.sql(evx); else return super.sql(evx); } //.plus(" AS ").plus(alias);

}















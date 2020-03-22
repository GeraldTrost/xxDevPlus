using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using ndBase;
using ndString;

namespace ndData
{

 public class DbField : Trm
 {
  //Copyright© 1996-2012 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties, You may use, modify, distribute this code under GNU public licence for NON-Commercial use as long as this notice remains unchanged.
  private static bool selfTested  = false; private static string ass(bool expr) { if (!expr) throw new Exception(ass(true) + " SelfTest Failure"); return "DbField"; }

  private void init() { if (!selfTested) selfTest(); }

  private static void selfTest()
  {
   selfTested = true;
   DbField dbf = new DbField(new Reach("abc"));
   ass(dbf.sql().equals("abc"));
   DbField dbf1 = dbf.pp("sbj");
   ass(dbf1.sql().equals("sbj.abc"));
   dbf.optor = " >$ ";
   ass(dbf.sql().equals("UPPER(abc)"));
   dbf1 = dbf.pp("sbj");
   ass(dbf1.sql().equals("UPPER(sbj.abc)"));
  }

  public  string def         = ""; // short definition for composite fields
  private string dataType    = "";
  public  string DataType    { get { return dataType; } }


  private Reach  prePosition = "";
  private Reach  alias       = null;
  private Reach  key         = ""; 
  public  Reach  Key         { get { return key; } }  //alias

  public  DbField (DbField lOpnd                                             ) : base("",                      lOpnd) { init(); }
  public  DbField (Reach   lOpnd                                             ) : base("",                      lOpnd) { key = lOpnd; init(); }
  public  DbField (string  lOpnd                                             ) : base("",           new Reach(lOpnd)) { key = lOpnd; init(); } //only for jave, c# does implicit type-conversion to Reach

  public  DbField (object  lOpnd, String  optor,                 object rOpnd) : base(optor,            lOpnd, rOpnd) { init(); } //only for jave, c# does implicit type-conversion to Reach
  public  DbField (object  lOpnd, Reach   optor,                 object rOpnd) : base(optor.text,       lOpnd, rOpnd) { init(); }
  public  DbField (                                                          ) : base(                              ) { init(); }

  public  DbField (bool    dummy, string lOpnd, string alias, string dataType) : this(dummy, lOpnd,  new Reach(alias),  dataType) {}
  public  DbField (bool    dummy, Reach  lOpnd, string alias, string dataType) : this(dummy, lOpnd,  new Reach(alias),  dataType) {}
  public  DbField (bool    dummy, string lOpnd,               string dataType) : this(dummy, lOpnd,  new Reach(""),     dataType) {}
  public  DbField (bool    dummy, Reach  lOpnd,               string dataType) : this(dummy, lOpnd,  new Reach(""),     dataType) {}

  public DbField(bool dummy, String lOpnd, Reach alias, string dataType) : base("", lOpnd) { key = alias; this.alias = key; init(); this.dataType = dataType; }
  public DbField(bool dummy, Reach  lOpnd, Reach alias, string dataType) : base("", lOpnd) { key = alias; this.alias = key; init(); this.dataType = dataType; }

  public override string val(int i, EvalExpert evx)
  {
   Type typ = this[i].GetType();
   if (false && (dbg))
   {
    if (typ == typeof(DbField)) return "[" + ((DbField)(this[i])).val(evx) + "]";
    if (typ == typeof(Trm))     return "[" + ((Trm)(this[i])).val(evx) + "]";
    if (typ == typeof(Reach))   return (prePosition.len == 0) ? "[" + ((Reach)(this[i])).text  + "]": "[" + (prePosition + "." + (Reach)(this[i])).text  + "]"; 
    if (typ == typeof(string))  return "[" + ((string)(this[i])) + "]";
    if (typ == typeof(long))    return "[" + ((Db)evx).ds("" + ((long)(this[i]))) + "]";
    if (typ == typeof(double))  return "[" + ((Db)evx).ds("" + ((double)(this[i]))).Replace(",", ".") + "]";
    throw new Exception("Err: unknown Type in DbField.val()");
   }
   else
   {
    if (typ == typeof(DbField)) return ((DbField)(this[i])).val(evx);
    if (typ == typeof(Trm))     return ((Trm)(this[i])).val(evx);
    if (typ == typeof(Reach))   return (prePosition.len == 0) ? ((Reach)(this[i])).text : (prePosition + "." + (Reach)(this[i])).text; 
    if (typ == typeof(string))  return ((string)(this[i]));
    if (typ == typeof(long))    return ((Db)evx).ds("" + ((long)(this[i])));
    if (typ == typeof(double))  return ((Db)evx).ds("" + ((double)(this[i]))).Replace(",", ".");
    throw new Exception("Err: unknown Type in DbField.val()");
   }
  }


  public DbField Clone()
  {
   DbField ret      = new DbField();
   ret.optor        = optor;
   ret.key          = key;
   ret.prePosition  = prePosition;
   foreach (object o in this)
   {
    if (o.GetType() == typeof(string)) ret.Push("" + (string)o);
    else
     if (o.GetType() == typeof(Reach)) ret.Push(((Reach)o).from(1));
     else
      if (o.GetType() == typeof(DbField)) ret.Push(((DbField)o).Clone());
      else
       if (o.GetType() == typeof(Trm)) ret.Push(((Trm)o).Clone());
       else
        ret.Push(o);
   }
   return ret;
  } //the super-super class Pile already provides Clone but its result cannot be casted to (DbField) in c#


  public static DbField fromDef(Reach def, Db source, string table)
  {
   DbField ret      = new DbField();
   ret.def = def.before(1, ":").Trim() + " : " + def.after(1, ":").Trim();
   def = def.after(1, ":").Trim();
   ret.optor = "   ";
   while (def.len > 0)
   {
    Reach fName = def.before(1, " ").Trim();
    bool isNumeric = false;
    for (int i = 1; i <= source.Tables[table].Fields.Len; i++) if (source.Tables[table].Fields[i].sql().equals(fName)) isNumeric = ",LONG,INT,INTEGER,DECIMAL,INT4,INT8,BIGINT,REAL,FLOAT8,DOUBLE PRECISION,FLOAT,BIT,".IndexOf("," + source.Tables[table].Fields[i].DataType.ToUpper() + ",") > -1;
    if (isNumeric) ret.Push(new DbField(fName).cvs()); else ret.Push(new DbField(fName).ltm().rtm());
    def = def.after(1, " ").Trim();
   }
   return ret;
  } 

  public DbField pp(string prePosition) 
  {
   DbField ret = Clone();
   if (prePosition.Length == 0) return ret;
   if (Len == 1)
   {
    if (lOpnd.GetType() == typeof(Reach)) if (((Reach)lOpnd).at(1, "'").len == 0) if (!((Reach)lOpnd).Trim().startsWith("0123456789-+")) ret.prePosition = new Reach(prePosition);
   }
   else
   {
    for (int i = 1; i <= Len; i++) if (this[i].GetType() == typeof(DbField)) ret[i] = ((DbField)this[i]).pp(prePosition);
   }
   return ret;
  }

  public DbField tm  (             ) { if (optor.Length > 0) return new DbField(this).tm();                                                               optor = utl.OPR("TM");  return this; }
  public DbField ltm (             ) { if (optor.Length > 0) return new DbField(this).ltm();                                                              optor = utl.OPR("LTM"); return this; }
  public DbField rtm (             ) { if (optor.Length > 0) return new DbField(this).rtm();                                                              optor = utl.OPR("RTM"); return this; }

  public DbField cvs(             ) { if (optor.Length > 0) return new DbField(this).cvs();                                                               optor = utl.OPR("CVS"); return this; }
  public DbField cct(string  rOpnd) { if (optor.Length > 0) return new DbField(this).cct(rOpnd); this.Push((Reach)rOpnd);                                 optor = utl.OPR("CCT"); return this; }
  public DbField cct(Reach   rOpnd) { if (optor.Length > 0) return new DbField(this).cct(rOpnd); this.Push(rOpnd);                                        optor = utl.OPR("CCT"); return this; }
  public DbField cct(Trm     rOpnd) { if (optor.Length > 0) return new DbField(this).cct(rOpnd); this.Push(rOpnd.Clone());                                optor = utl.OPR("CCT"); return this; }
  public DbField cct(long    rOpnd) { if (optor.Length > 0) return new DbField(this).cct(rOpnd); this.Push(utl.ds(false, "" + rOpnd));                    optor = utl.OPR("CCT"); return this; }
  public DbField cct(double  rOpnd) { if (optor.Length > 0) return new DbField(this).cct(rOpnd); this.Push(utl.ds(false, "" + rOpnd).Replace(",", "."));  optor = utl.OPR("CCT"); return this; }
  public DbField cct(DbSlc   rOpnd) { if (optor.Length > 0) return new DbField(this).cct(rOpnd); this.Push(rOpnd);                                        optor = utl.OPR("CCT"); return this; }
  public DbField apd(string  rOpnd) { if (optor.Length > 0) return new DbField(this).apd(rOpnd); this.Push((Reach)rOpnd);                                 optor = utl.OPR("APD"); return this; }
  public DbField apd(Reach   rOpnd) { if (optor.Length > 0) return new DbField(this).apd(rOpnd); this.Push(rOpnd);                                        optor = utl.OPR("APD"); return this; }
  public DbField apd(Trm     rOpnd) { if (optor.Length > 0) return new DbField(this).apd(rOpnd); this.Push(rOpnd.Clone());                                optor = utl.OPR("APD"); return this; }
  public DbField apd(long    rOpnd) { if (optor.Length > 0) return new DbField(this).apd(rOpnd); this.Push((Reach)("" + rOpnd));                          optor = utl.OPR("APD"); return this; }
  public DbField apd(double  rOpnd) { if (optor.Length > 0) return new DbField(this).apd(rOpnd); this.Push((Reach)("" + rOpnd).Replace(",", "."));        optor = utl.OPR("APD"); return this; }
  public DbField apd(DbSlc   rOpnd) { if (optor.Length > 0) return new DbField(this).apd(rOpnd); this.Push(rOpnd);                                        optor = utl.OPR("APD"); return this; }

  public override Reach sql(EvalExpert evx) { if (alias == null) return base.sql(evx); else return base.sql(evx); } // + " AS " + alias; 

 }

}









using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using ndBase;
using ndString;

namespace ndData
{
 public class DbUpd
 {
  //Copyright© 1996-2012 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties, You may use, modify, distribute this code under GNU public licence for NON-Commercial use as long as this notice remains unchanged.
  private static bool selfTested  = false;  private static string ass(bool expr) { if (!expr) throw new Exception(ass(true) + " SelfTest Failure"); return "DbUpd"; } private static void selfTest() { selfTested = true; } private void init() { if (!selfTested) selfTest(); }

  private  string                      update       = "";
  private  Pile<DbField>               fields       = new Pile<DbField>();
  public   Pile<DbField>               Fields       { get { return fields; } }
  private  Pile<object>                values       = new Pile<object>();
  internal Pile<DbCnd>                 where        = new Pile<DbCnd>();

  public   string                      Update       { get { return update;  } }
  public   Pile<object>                Values       { get { return values;  } }

  internal DbUpd(string update, Pile<DbField> fields, object[] values, Pile<DbCnd> where)
  {
   this.where.Add(where);
   this.fields = fields.Clone();
   foreach (object o in values) if (o.GetType() == typeof(DbSlc)) this.values.Add(o); else if (o.GetType() == typeof(string)) this.values.Add((string)o); else if (o.GetType() == typeof(double)) this.values.Add(("" + o).Replace(",", ".")); else this.values.Add("" + o);
   if (this.values.Len < this.fields.Len) this.values.Add("");
   this.update   = update.Trim();
   string tShort = utl.cutl(ref update, ",").Trim().ToLower();
   string tName  = utl.cutl(ref tShort, " ").Trim();
   tShort = tShort.Trim(); if (tShort.Length == 0) tShort = tName;
   update = tName.Trim() + " " + tShort.Trim();
  }

  public string sql() { return sql(new Db(new ctx().DbDrivers)); }

  public string sql(Db db)
  {
   string ret = "UPDATE " + db.dbTable(Update) + " SET ";
   for (int i = 1; i <= Fields.Len; i++) if ((Values[i]).GetType() == typeof(DbSlc)) ret += Fields[i].sql(db) + " = ( " + ((DbSlc)Values[i]).sql(db) + "), "; else ret += Fields[i].sql(db) + " = " + Values[i] + ", ";
   ret = ret.Substring(0, ret.Length - 2);
   string where = "\r\n WHERE "; if (this.where.Len > 0) { where  += "( "; foreach (DbCnd j in this.where) where += "(" + j.sql(db) + ") AND "; where = where.Substring(0, where.Length - 4) + " ) "; }
   if (this.where.Len > 0) ret += where;
   return ret;
  }


  /*
  public string _sql()
  {
   string ret = "UPDATE " + Update + " SET ";
   for (int i = 1; i <= Fields.Len; i++)
    if ((Values[i]).GetType() == typeof(DbSlc)) ret += Fields[i] + " = ( " + ((DbSlc)Values[i]).sql() + "), "; else ret += Fields[i] + " = " + Values[i] + ", ";
   ret = ret.Substring(0, ret.Length - 2);
   string where = "\r\n WHERE "; if (this.where.Len > 0) { where  += "( "; foreach (Cnd j in this.where) where += "(" + j._sql() + ") AND "; where = where.Substring(0, where.Length - 4) + " ) "; }
   if (this.where.Len > 0) ret += where;
   return ret;
  }
  */
  
  
  public string smb()
  {
   //TBD
   return "";
  }

  internal DbUpd(Reach smb)
  {
   //TBD
   Db db = new Db(new ctx().DbDrivers);
   smb = smb;
  }
  
 }
}

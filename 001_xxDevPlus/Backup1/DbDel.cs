
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using ndBase;
using ndString;


namespace ndData
{
 public class DbDel
 {
  //Copyright© 1996-2012 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties, You may use, modify, distribute this code under GNU public licence for NON-Commercial use as long as this notice remains unchanged.
  private static bool selfTested  = false;  private static string ass(bool expr) { if (!expr) throw new Exception(ass(true) + " SelfTest Failure"); return "DbDel"; } private static void selfTest() { selfTested = true; } private void init() { if (!selfTested) selfTest(); }

  private  string                      from             = "";
  private  string                      where            = "";

  public   string                      From             { get { return from;    } }
  internal Pile<DbCnd>                   deleteConditions = new Pile<DbCnd>();


  public DbDel(string from, params DbCnd[] deleteConditions)
  {
   this.from = from.Trim().ToLower();
   this.deleteConditions.Add(deleteConditions);
  }

  public string sql() { return sql(new Db(new ctx().DbDrivers)); }

  public string sql(Db db)
  {
   string ret = "DELETE FROM " + db.dbTable(From);
   if (deleteConditions.Len > 0)
   {
    string where = "\r\n WHERE ";
    where  += "( "; foreach (DbCnd j in deleteConditions) where += "(" + j.sql(db) + ") AND ";
    where = where.Substring(0, where.Length - 4) + " ) ";
    ret += where;
   }
   return ret;
  }

  /*
  public string _sql()
  {
   string ret = "DELETE FROM " + From;
   if (deleteConditions.Len > 0)
   {
    string where = "\r\n WHERE ";
    where  += "( "; foreach (Cnd j in deleteConditions) where += "(" + j._sql() + ") AND ";
    where = where.Substring(0, where.Length - 4) + " ) ";
    ret += where;
   }
   return ret;
  }
  */

  public string smb()
  {
   //TBD
   return "";
  }

  public DbDel(Reach smb)
  {
   Db db = new Db(new ctx().DbDrivers);
   //TBD
  }
  
  
 }
}

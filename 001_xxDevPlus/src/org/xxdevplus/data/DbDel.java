

//** @author  GeTr Copyright© 1996-2020 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties
//** @license You may use, modify, distribute this code under GNU public dual licence free for NON-Commercial use as long as this notice remains unchanged.
//** @comment Database Delete Command

package org.xxdevplus.data;

import org.xxdevplus.data.Db;
import org.xxdevplus.struct.Pile;
import org.xxdevplus.utl.ctx;
import org.xxdevplus.chain.Chain;
import org.xxdevplus.data.DbCnd;

 public class DbDel
 {
  private static boolean selfTested  = false;  private static String ass(boolean expr) throws Exception { if (!expr) throw new Exception(ass(true) + " SelfTest Failure"); return "DbDel"; } private static void selfTest() { selfTested = true; } private void init() { if (!selfTested) selfTest(); }

  private  String                      from             = "";
  private  String                      where            = "";

  public   String                      From()           { return from;    } 
           Pile<DbCnd>                   deleteConditions = new Pile<DbCnd>();

  public DbDel(String from, DbCnd... deleteConditions) throws Exception
  {
   this.from = from.trim().toLowerCase();
   this.deleteConditions.Add(deleteConditions);
  }

  public String sql() throws Exception { return sql(new Db(new ctx().DbDrivers())); }

  public String sql(Db db) throws Exception
  {
   String ret = "DELETE FROM " + db.dbTable(From());
   if (deleteConditions.Len() > 0)
   {
    String where = "\r\n WHERE ";
    where  += "( "; 
    for (DbCnd j : deleteConditions)
     where += "(" + j.sql(db).text() + ") AND ";
    where = where.substring(0, where.length() - 4) + " ) ";
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
    where  += "( "; foreach (DbCnd j in deleteConditions) where += "(" + j._sql() + ") AND ";
    where = where.Substring(0, where.Length - 4) + " ) ";
    ret += where;
   }
   return ret;
  }
  */

  public String smb()
  {
   //TBD
   return "";
  }

  public DbDel(Chain smb) throws Exception
  {
   Db db = new Db(new ctx().DbDrivers());
   //TBD
  }
  
  
 }

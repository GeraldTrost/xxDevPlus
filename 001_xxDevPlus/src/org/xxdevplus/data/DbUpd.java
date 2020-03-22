/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.xxdevplus.data;

import org.xxdevplus.struct.Pile;
import org.xxdevplus.utl.ctx;
import org.xxdevplus.utl.utl;
import org.xxdevplus.chain.Chain;

/**
 *
 * @author GeTr
 */
 public class DbUpd
 {
  //Copyright© 1996-2020 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties, You may use, modify, distribute this code under GNU public licence for NON-Commercial use as long as this notice remains unchanged.
  private static boolean selfTested  = false;  private static String ass(boolean expr) throws Exception { if (!expr) throw new Exception(ass(true) + " SelfTest Failure"); return "DbUpd"; } private static void selfTest() { selfTested = true; } private void init() { if (!selfTested) selfTest(); }

  private  String                      update       = "";
  private  Pile<DbField>               fields       = new Pile<DbField>();
  public   Pile<DbField>               Fields (   ) { return fields; }
  private  Pile<Object>                values       = new Pile<Object>();
           Pile<DbCnd>                 where        = new Pile<DbCnd>();

  public   String                      Update()     { return update;  }
  public   Pile<Object>                Values()     { return values;  }

  DbUpd(String upd, Pile<DbField> fields, Object[] values, Pile<DbCnd> where) throws Exception
  {
   String[] update = new String[] {upd};
   this.where.Add(where);
   for (DbField field : fields) this.fields.Add(field);
   for (Object o : values) if (o instanceof DbSlc) this.values.Add(o); else if (o instanceof String) this.values.Add((String)o); else if (o instanceof Double) this.values.Add(("" + o).replace(",", ".")); else this.values.Add("" + o);
   if (this.values.Len() < this.fields.Len()) this.values.Add("");
   this.update   = update[0].trim();
   String[] tShort = new String[] { utl.cutl(update, ",").trim().toLowerCase() } ;
   String[] tName  = new String[] { utl.cutl(tShort, " ").trim() };
   tShort[0] = tShort[0].trim(); if (tShort[0].length() == 0) tShort[0] = tName[0];
   update[0] = tName[0].trim() + " " + tShort[0].trim();
  }

  public String sql() throws Exception { return sql(new Db(new ctx().DbDrivers())); }

  public String sql(Db db) throws Exception
  {
   String ret = "UPDATE " + db.dbTable(Update()) + " SET ";
   for (int i = 1; i <= Fields().Len(); i++)
   if ((Values().g(i)) instanceof DbSlc) ret += Fields().g(i).sql(db).text() + " = ( " + ((DbSlc)Values().g(i)).sql(db) + "), "; else ret += Fields().g(i).sql(db).text() + " = " + Values().g(i) + ", ";
   ret = ret.substring(0, ret.length() - 2);
   String where = "\r\n WHERE "; if (this.where.Len() > 0) { where  += "( "; for (DbCnd j : this.where) where += "(" + j.sql(db).text() + ") AND "; where = where.substring(0, where.length() - 4) + " ) "; }
   if (this.where.Len() > 0) ret += where;
   return ret;
  }

  /*
  public string _sql()
  {
   string ret = "UPDATE " + Update + " SET ";
   for (int i = 1; i <= Fields.Len; i++)
    if ((Values[i]).GetType() == typeof(DbSlc)) ret += Fields[i] + " = ( " + ((DbSlc)Values[i]).sql() + "), "; else ret += Fields[i] + " = " + Values[i] + ", ";
   ret = ret.Substring(0, ret.Length - 2);
   string where = "\r\n WHERE "; if (this.where.Len > 0) { where  += "( "; foreach (DbCnd j in this.where) where += "(" + j._sql() + ") AND "; where = where.Substring(0, where.Length - 4) + " ) "; }
   if (this.where.Len > 0) ret += where;
   return ret;
  }
  */
  
  
  public String smb()
  {
   //TBD
   return "";
  }
  

  DbUpd(Chain smb) throws Exception
  {
   //TBD
   Db db = new Db(new ctx().DbDrivers());
   smb = smb;
  }
  
 }

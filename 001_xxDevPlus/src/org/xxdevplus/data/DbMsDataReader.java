/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.xxdevplus.data;

import java.sql.ResultSet;
import org.xxdevplus.struct.ObjPile;
import org.xxdevplus.utl.utl;
import org.xxdevplus.chain.Chain;

/**
 *
 * @author GeTr
 */


 public class DbMsDataReader 

 {
  //Copyright© 1996-2020 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties, You may use, modify, distribute this code under GNU public licence for NON-Commercial use as long as this notice remains unchanged.
  private static boolean selfTested  = false; private static String ass(boolean expr) throws Exception { if (!expr) throw new Exception(ass(true) + " SelfTest Failure"); return "DbMsDataReader"; } private static void selfTest() { selfTested = true; }
  
  public DbDataReader dataReader = null;
  private Db db = null;
  
                     DbMsDataReader   (Db db, DbDataReader dataReader) {this.db = db; this.dataReader = dataReader;}
  public   void      Dispose          (                         ) throws Exception { dataReader.Dispose(); }
  public   void      Close            (                         ) throws Exception { dataReader.Close(); }
  public   String    GetDataTypeName  (int                   inx) throws Exception { return dataReader.GetDataTypeName(inx-1); }
  public   String    GetName          (int                   inx) throws Exception { return dataReader.GetName(inx-1); }
  public   boolean   Read             (                         ) throws Exception 
  { 
      try 
      {
      return dataReader.Read();
      }
      catch (Exception ex)
      {
       dataReader = dataReader;
       return false;
       
      }
  }
  
  public   String    GetString        (int                   inx) throws Exception { return dataReader.GetString   (inx-1); }
  //public   long      GetInt64         (int                   inx) throws Exception { try {return dataReader.GetInt64  (inx); } catch (Exception ex) { return 0L + dataReader.GetInt32(inx);     } }
  
  public   long      GetInt64         (int                   inx) throws Exception { try { return dataReader.GetInt64(inx-1); } catch (Exception ex) { try { return 0L + dataReader.GetInt32(inx); } catch (Exception exp) { return 0L + dataReader.GetInt16(inx); } } }

  
  public   double    GetDouble        (int                   inx) throws Exception { try {return dataReader.GetDouble (inx-1); } catch (Exception ex) { return (double)dataReader.GetFloat(inx);  } }
  
  public DatSet      all()                                        throws Exception { return db.all(this); }
  public ResultSet   result()                                                      {return dataReader.resultset;}
}


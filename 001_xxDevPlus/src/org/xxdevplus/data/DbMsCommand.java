/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.xxdevplus.data;

import java.sql.Statement;
import org.xxdevplus.utl.utl;

/**
 *
 * @author GeTr
 */


 public class DbMsCommand
 {
  //Copyright© 1996-2020 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties, You may use, modify, distribute this code under GNU public licence for NON-Commercial use as long as this notice remains unchanged.
  private static boolean selfTested  = false; private static String ass(boolean expr) throws Exception { if (!expr) throw new Exception(ass(true) + " SelfTest Failure"); return "DbMsCommand"; } private static void selfTest() { selfTested = true; }
   
  int dbtyp = 0;

  private String      DUP_ROW = "";
          DbCommand   command = null;

  public String CommandText() { return command.CommandText();} public void CommandText(String value) {command.CommandText(value);} 
  private Db db = null;
  
  DbMsCommand(Db db, String sql, DbMsConnection connection) throws Exception 
  {
   this.db = db;   
   this.DUP_ROW = connection.DUP_ROW; 
   command = new DbCommand  (sql, connection.connection);
  }
 
  public   DbMsTransaction  Transaction() { return new DbMsTransaction(command.Transaction()); } public void Transaction(DbMsTransaction value) {command.Transaction(value.transaction);}
  public   long             ExecuteNonQuery  (                                 ) throws Exception 
  {
   boolean singleStatementTransaction = command.Transaction() == null;
   DbTransaction tract = command.Transaction();
   //if (singleStatementTransaction) try {tract = command.Connection.BeginTransaction(IsolationLevel.Chaos); } catch (Exception ex) {tract = command.Connection.BeginTransaction(); }
   try
   {
    command.Transaction(tract);
    long ret = command.ExecuteNonQuery();
    if  (singleStatementTransaction && (tract != null)) { tract.Commit(); tract.Dispose(); }
    return ret;
   } catch (Exception e)
   {
    if (singleStatementTransaction && (tract != null)) { tract.Rollback(); tract.Dispose(); }
    if (e.getMessage().startsWith(DUP_ROW)) return -1; else throw (e);
   }
  }  
  
  
  public   DbMsDataReader   ExecuteReader    (                                   ) throws Exception 
  { 
   return new DbMsDataReader(db, command.ExecuteReader()); 
  }

                            DbMsCommand      (String DUP_ROW, DbCommand command) { this.DUP_ROW = DUP_ROW; this.command = command; }

 }









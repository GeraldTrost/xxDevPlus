/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.xxdevplus.data;

import java.sql.Statement;

/**
 *
 * @author GeTr
 */

public class DbCommand
{

 public Statement        statement   = null;
 public String           sql         = "";
 public DbConnection     connection  = null;
 public DbTransaction    transaction = null;

 public  String           CommandText     (                                     )                  { throw new UnsupportedOperationException("Not yet implemented"); }
 public  void             CommandText     (String                          value)                  { throw new UnsupportedOperationException("Not yet implemented"); }

 public  void             Transaction     (DbTransaction                   value)                  { transaction = value; }
 public  DbTransaction    Transaction()                                                            { return transaction;  }

 public  long             ExecuteNonQuery (                                     ) throws Exception { return (long)(statement.executeUpdate(sql)); }
 public  DbDataReader     ExecuteReader   (                                     ) throws Exception 
 { 
  try 
  {
   return new DbDataReader(statement. executeQuery(sql)); 
  }
  catch (Exception ex)
  {
   if (!ex.getMessage().startsWith("No results were returned by the query")) // postgres workaround
   throw ex; // postgres specific message can be ignored
  }
  
  /* old workaround for postgres specific message 
  if (sql.toUpperCase().indexOf("SELECT ") > -1) return new DbDataReader(statement. executeQuery(sql)); 
  statement.execute(sql); 
  */
  
  return null;
 }

 public                   DbCommand     (String sql, DbConnection connection) throws Exception
 {
  this.connection = connection;
  this.sql        = sql;
  statement       = connection.connection.createStatement();
 }


}


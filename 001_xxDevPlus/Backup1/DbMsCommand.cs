

using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using System.Data;
using System.Data.Common;
using System.Data.Odbc;
using System.Data.OleDb;

#if DOTNET_MYS
using MySql.Data.MySqlClient;
#endif
#if DOTNET_2MYS
#endif
#if DOTNET_PGS
using Npgsql;
#endif
#if DOTNET_2PGS
#endif
#if DOTNET_FBD
#endif
#if DOTNET_SQL
#endif
#if DOTNET_MSS
using System.Data.SqlClient;
#endif
#if DOTNET_2MSS
#endif
#if DOTNET_ORA
using Oracle.DataAccess.Client;
#endif
#if DOTNET_2ORA
#endif
#if DOTNET_DB2
#endif
#if DOTNET_2DB2
#endif
#if DOTNET_DBY
#endif
#if DOTNET_2DBY
#endif
#if DOTNET_HSQ
#endif
#if DOTNET_2HSQ
#endif
#if DOTNET_H2S
#endif



namespace ndData
{

 public class DbMsCommand
 {
  //Copyright© 1996-2012 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties, You may use, modify, distribute this code under GNU public licence for NON-Commercial use as long as this notice remains unchanged.
  private static bool selfTested  = false; private static string ass(bool expr) { if (!expr) throw new Exception(ass(true) + " SelfTest Failure"); return "DbMsCommand"; } private static void selfTest() { selfTested = true; }
   
  int dbtyp = 0;

  private  string     DUP_ROW = "";
  internal DbCommand  command = null;

  public string CommandText {get{return command.CommandText;} set{command.CommandText = value;} }

  public DbMsCommand(string sql, DbMsConnection connection) 
  {
   this.DUP_ROW = connection.DUP_ROW; 
   Type connectionType = connection.connection.GetType();
 //if (connectionType == typeof(JdbcConnection))    command = new JdbcCommand    (sql, (JdbcConnection)   connection.connection);
   if (connectionType == typeof(OdbcConnection))    command = new OdbcCommand    (sql, (OdbcConnection)   connection.connection);
   if (connectionType == typeof(OleDbConnection))   command = new OleDbCommand   (sql, (OleDbConnection)  connection.connection);


#if DOTNET_MYS
   if (connectionType == typeof(MySqlConnection)) command = new MySqlCommand(sql, (MySqlConnection)connection.connection);
#endif
#if DOTNET_2MYS
#endif
#if DOTNET_PGS
   if (connectionType == typeof(NpgsqlConnection)) command = new NpgsqlCommand(sql, (NpgsqlConnection)connection.connection);
#endif
#if DOTNET_2PGS
#endif
#if DOTNET_FBD
#endif
#if DOTNET_SQL
#endif
#if DOTNET_MSS
   if (connectionType == typeof(SqlConnection)) command = new SqlCommand(sql, (SqlConnection)connection.connection);
#endif
#if DOTNET_2MSS
#endif
#if DOTNET_ORA
   if (connectionType == typeof(OracleConnection)) command = new OracleCommand(sql, (OracleConnection)connection.connection);
#endif
#if DOTNET_2ORA
#endif
#if DOTNET_DB2
#endif
#if DOTNET_2DB2
#endif
#if DOTNET_DBY
#endif
#if DOTNET_2DBY
#endif
#if DOTNET_HSQ
#endif
#if DOTNET_2HSQ
#endif
#if DOTNET_H2S
#endif




  }
 
  public   DbMsTransaction  Transaction                                          { get{return new DbMsTransaction(command.Transaction); } set{command.Transaction = value.transaction;}}
  public   long             ExecuteNonQuery  (                                 ) 
  {
   bool singleStatementTransaction = command.Transaction == null;
   DbTransaction tract = command.Transaction;
   //if (singleStatementTransaction) try {tract = command.Connection.BeginTransaction(IsolationLevel.Chaos); } catch (Exception ex) {tract = command.Connection.BeginTransaction(); }
   try
   {
    command.Transaction = tract;
    long ret = command.ExecuteNonQuery();
    if  (singleStatementTransaction && (tract != null)) { tract.Commit(); tract.Dispose(); }
    return ret;
   } catch (Exception e)
   {
    if (singleStatementTransaction && (tract != null)) { tract.Rollback(); tract.Dispose(); }
    if (e.Message.StartsWith(DUP_ROW)) return -1; else throw (e);
   }
  }  
  
  
  public   DbMsDataReader   ExecuteReader    (                                 ) { return new DbMsDataReader((DbDataReader)(command.ExecuteReader())); }

  internal                  DbMsCommand      (string DUP_ROW, DbCommand command) { this.DUP_ROW = DUP_ROW; this.command = command; }

 }
}











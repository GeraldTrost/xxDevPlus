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

 public class DbMsDataAdapter
 {
  //Copyright© 1996-2012 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties, You may use, modify, distribute this code under GNU public licence for NON-Commercial use as long as this notice remains unchanged.
  private static bool selfTested  = false; private static string ass(bool expr) { if (!expr) throw new Exception(ass(true) + " SelfTest Failure"); return "DbMsDataAdapter"; } private static void selfTest() { selfTested = true; }
 
 
  internal DbDataAdapter adapter = null;
  
  public void Fill(DataSet ds) { adapter.Fill(ds); }

  public      DbMsDataAdapter(DbMsCommand command) 
  {
   Type commandType = command.command.GetType();
 //if (commandType == typeof(JdbcCommand))       adapter = new JdbcDataAdapter   ((JdbcCommand)command.command); 
   if (commandType == typeof(OdbcCommand))       adapter = new OdbcDataAdapter   ((OdbcCommand)command.command); 
   if (commandType == typeof(OleDbCommand))      adapter = new OleDbDataAdapter  ((OleDbCommand)command.command);


#if DOTNET_MYS
   if (commandType == typeof(MySqlCommand)) adapter = new MySqlDataAdapter((MySqlCommand)command.command); 
#endif
#if DOTNET_2MYS
#endif
#if DOTNET_PGS
   if (commandType == typeof(NpgsqlCommand)) adapter = new NpgsqlDataAdapter((NpgsqlCommand)command.command);
#endif
#if DOTNET_2PGS
#endif
#if DOTNET_FBD
#endif
#if DOTNET_SQL
#endif
#if DOTNET_MSS
   if (commandType == typeof(SqlCommand)) adapter = new SqlDataAdapter((SqlCommand)command.command); 
#endif
#if DOTNET_2MSS
#endif
#if DOTNET_ORA
   if (commandType == typeof(OracleCommand)) adapter = new OracleDataAdapter((OracleCommand)command.command); 
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


 }
}
























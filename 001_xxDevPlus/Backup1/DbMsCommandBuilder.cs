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


//detnet:using System.Data.SqlClient;
//detnet:using MySql.Data.MySqlClient;
//detnet:using Oracle.DataAccess.Client;
//detnet:using Npgsql;


namespace ndData
{
 public class DbMsCommandBuilder
 {
  //Copyright© 1996-2012 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties, You may use, modify, distribute this code under GNU public licence for NON-Commercial use as long as this notice remains unchanged.
  private static bool selfTested  = false; private static string ass(bool expr) { if (!expr) throw new Exception(ass(true) + " SelfTest Failure"); return "DbMsCommandBuilder"; } private static void selfTest() { selfTested = true; }
  
  private DbCommandBuilder commandBuilder = null;
  
  public            DbMsCommandBuilder(DbMsDataAdapter adapter) 
  {
   Type adapterType = adapter.adapter.GetType();
 //if (adapterType == typeof(JdbcDataAdapter))       commandBuilder = new JdbcCommandBuilder   ((JdbcDataAdapter)   adapter.adapter);
   if (adapterType == typeof(OdbcDataAdapter))       commandBuilder = new OdbcCommandBuilder   ((OdbcDataAdapter)   adapter.adapter);
   if (adapterType == typeof(OleDbDataAdapter))      commandBuilder = new OleDbCommandBuilder  ((OleDbDataAdapter)  adapter.adapter);



#if DOTNET_MYS
   if (adapterType == typeof(MySqlDataAdapter)) commandBuilder = new MySqlCommandBuilder((MySqlDataAdapter)adapter.adapter);
#endif
#if DOTNET_2MYS
#endif
#if DOTNET_PGS
   if (adapterType == typeof(NpgsqlDataAdapter)) commandBuilder = new NpgsqlCommandBuilder((NpgsqlDataAdapter)adapter.adapter);
#endif
#if DOTNET_2PGS
#endif
#if DOTNET_FBD
#endif
#if DOTNET_SQL
#endif
#if DOTNET_MSS
   if (adapterType == typeof(SqlDataAdapter)) commandBuilder = new SqlCommandBuilder((SqlDataAdapter)adapter.adapter);
#endif
#if DOTNET_2MSS
#endif
#if DOTNET_ORA
   if (adapterType == typeof(OracleDataAdapter)) commandBuilder = new OracleCommandBuilder((OracleDataAdapter)adapter.adapter);
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













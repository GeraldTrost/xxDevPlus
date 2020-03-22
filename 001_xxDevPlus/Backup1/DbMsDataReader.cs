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

 public class DbMsDataReader
 {
  //Copyright© 1996-2012 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties, You may use, modify, distribute this code under GNU public licence for NON-Commercial use as long as this notice remains unchanged.
  private static bool selfTested  = false; private static string ass(bool expr) { if (!expr) throw new Exception(ass(true) + " SelfTest Failure"); return "DbMsDataReader"; } private static void selfTest() { selfTested = true; }
  
  private DbDataReader dataReader = null;
  
  public   string    GetDataTypeName  (int inx) { return dataReader.GetDataTypeName(inx); }
  public   string    GetName          (int inx) { return dataReader.GetName(inx); }
  public   bool      Read             (       ) { return dataReader.Read();}
  public   void      Dispose          (       ) { dataReader.Dispose(); }
  public   void      Close            (       ) { dataReader.Close(); }
  public   string    GetString        (int inx) { return dataReader.GetString   (inx); }
  public   long      GetInt64         (int inx) { try { return dataReader.GetInt64(inx); } catch (Exception ex) { try { return 0L + dataReader.GetInt32(inx); } catch (Exception exp) { return 0L + dataReader.GetInt16(inx); } } }

  public   double    GetDouble        (int inx) { try {return dataReader.GetDouble (inx); } catch (Exception ex) { return (double)dataReader.GetFloat(inx);  } }

  internal           DbMsDataReader(DbDataReader dataReader) {this.dataReader = dataReader;}
  
 }
}



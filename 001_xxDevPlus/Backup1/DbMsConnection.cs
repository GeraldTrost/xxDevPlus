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

using ndBase;

namespace ndData
{

 public class DbMsConnection
 {
  //Copyright© 1996-2012 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties, You may use, modify, distribute this code under GNU public licence for NON-Commercial use as long as this notice remains unchanged.
  private static bool selfTested  = false; private static string ass(bool expr) { if (!expr) throw new Exception(ass(true) + " SelfTest Failure"); return "DbMsConnection"; } private static void selfTest() { selfTested = true; }

  internal DbConnection connection = null;

  public int                ConnectionTimeout  
  { 
   get { return connection.ConnectionTimeout; 
  } 
   set 
   {
    Type connectionType = connection.GetType();
  //if (connectionType == typeof(JdbcConnection))               ((JdbcConnection)   connection).ConnectionTimeout = value; 
    if (connectionType == typeof(OdbcConnection))               ((OdbcConnection)   connection).ConnectionTimeout = value; 
    if (connectionType == typeof(OleDbConnection))   return; // ((OleDbConnection)  connection).ConnectionTimeout = value; 


#if DOTNET_MYS
    if (connectionType == typeof(MySqlConnection)) return; // ((MySqlConnection)  connection).ConnectionTimeout = value; 
#endif
#if DOTNET_2MYS
#endif
#if DOTNET_PGS
    if (connectionType == typeof(NpgsqlConnection)) return; // ((NpgsqlConnection) connection).ConnectionTimeout = value; 
#endif
#if DOTNET_2PGS
#endif
#if DOTNET_FBD
#endif
#if DOTNET_SQL
#endif
#if DOTNET_MSS
    if (connectionType == typeof(SqlConnection)) return; // ((SqlConnection)    connection).ConnectionTimeout = value; 
#endif
#if DOTNET_2MSS
#endif
#if DOTNET_ORA
    if (connectionType == typeof(OracleConnection)) return; // ((OracleConnection) connection).ConnectionTimeout = value; 
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
  
  public void               Dispose            (                    ) { connection.Dispose(); }   
  public void               Close              (                    ) { connection.Close(); }
  
  public DbMsCommand        CreateCommand      (                    ) { return new DbMsCommand(DUP_ROW, connection.CreateCommand());  }

  public DbMsTransaction    BeginTransaction() { try { return new DbMsTransaction(connection.BeginTransaction(IsolationLevel.Chaos)); } catch (Exception ex) { return new DbMsTransaction(connection.BeginTransaction()); } }
  public void               ChangeDatabase     (string dbName       ) {connection.ChangeDatabase(dbName); }
  public void               Open               (                    ) {connection.Open(); }

  /*
  public string pdf(string prefix, string val                        ) { if (val.Trim().Length == 0) return "";                          return prefix.Trim() + val.Trim(); }
  public string pdf(string prefix, string val,            string dflt) { if (val.Trim().Length == 0) return prefix.Trim() + dflt.Trim(); return prefix.Trim() + val.Trim(); }
  public string pdf(string prefix, long   val                        ) { if (val               == 0) return "";                          return prefix.Trim() + val; }
  public string pdf(string prefix, long   val,            long   dflt) { if (val               == 0) return prefix.Trim() + dflt;        return prefix.Trim() + val; }
  public string pdf(string prefix, bool   val                        ) {                                                                 return prefix.Trim() + val; }
  public string pdf(bool cnd, string prefix, string val              ) { if (!cnd) return ""; if (val.Trim().Length == 0) return "";                          return prefix.Trim() + val.Trim(); }
  public string pdf(bool cnd, string prefix, string val,  string dflt) { if (!cnd) return ""; if (val.Trim().Length == 0) return prefix.Trim() + dflt.Trim(); return prefix.Trim() + val.Trim(); }
  public string pdf(bool cnd, string prefix, long   val              ) { if (!cnd) return ""; if (val               == 0) return "";                          return prefix.Trim() + val; }
  public string pdf(bool cnd, string prefix, long   val,  long   dflt) { if (!cnd) return ""; if (val               == 0) return prefix.Trim() + dflt; return prefix.Trim() + val; }
  public string pdf(bool cnd, string prefix, bool   val              ) { if (!cnd) return ""; return prefix.Trim() + val; }
  */
  
  internal string DUP_ROW = "";

  public string  dbtec    = "";
  public string  dbms     = "";
  public string  dsn      = "";
  public string  drv      = "";
  public string  svr      = "";
  public string  inst     = "";
  public long    port     = 0;
  public string  db       = "";
  public string  sm       = "";
  public string  usr      = "";
  public string  pwd      = "";
  public long    cnnliftm = 0;
  public long    cmdliftm = 0;
  public string  ptcl     = "";
  public string  optn     = "";
  public bool    pool     = false;
  public long    minpool  = 0;
  public long    maxpool  = 0;


  public DbMsConnection(Db db, string dbtec, string dbms, string dsn, string drv, string svr, string inst, long port, string dbName, string sm, string usr, string pwd, long cnnliftm, long cmdliftm, string ptcl, string optn, bool pool, long minpool, long maxpool) 
  {

   this.dbtec = dbtec; this.dbms = dbms; this.dsn = dsn; this.drv = drv; this.svr = svr; this.inst = inst; this.port = port; this.db = dbName; this.sm = sm; this.usr = usr; this.pwd = pwd; this.cnnliftm = cnnliftm; this.cmdliftm = cmdliftm; this.ptcl = ptcl; this.optn = optn; this.pool = pool; this.minpool = minpool; this.maxpool = maxpool;

   //string _dsn      =  utl.pV(                 ";DSN="                  , dsn     );   // DSN.Trim().Length         == 0 ? ""   :   ";DSN="                   + DSN.Trim();
   //string _drv      =  utl.pV(                 ";Driver="               , drv     );   // Driver.Trim().Length      == 0 ? ""   :   ";Driver="                + Driver.Trim();
   //string _svr      =  utl.pV(dsn.Length == 0, ";Server="               , svr     );   // DSN.Trim().Length > 0     ? "" : Server.Trim().Length == 0 ? "" : ";Server=" + Server.Trim();
   //string _inst     =  utl.pV(                 "\\"                     , inst    );   // Instance.Trim().Length    == 0 ? ""   :   "\\"                     + Instance.Trim();
   //string _port     =  utl.pV("\\", inst);   // Port                      == 0 ? ""   :   ":"                      + Port;
   //string _db       =  utl.pV(                 ";DataBase="             , db      );   // DataBase.Trim().Length    == 0 ? ""   :   ";DataBase="             + DataBase.Trim();
   //string _usr      =  utl.pV(                 ";UID="                  , usr     );   // UID.Trim().Length         == 0 ? ""   :   ";UID="                  + UID.Trim();
   //string _pwd      =  utl.pV(                 ";PWD="                  , pwd     );   // PWD.Trim().Length         == 0 ? ""   :   ";PWD="                  + PWD.Trim();
   //string _cnnliftm =  utl.pV(                 ";Connection Lifetime="  , cnnliftm);   // ";Connection Lifetime="  + ConnectionLifetime;
   //string _pool     =  utl.pV(                 ";Pooling="              , pool    );   //                                           ";Pooling="              + Pooling;
   //string _minpool  =  utl.pV(pool,            ";Min Pool Size="        , minpool );   // !Pooling                       ? ""   :   ";Min Pool Size="        + MinPoolSize;
   //string _maxpool  =  utl.pV(pool,            ";Max Pool Size="        , maxpool );   // !Pooling                       ? ""   :   ";Max Pool Size="        + MaxPoolSize;
   //string _optn     =  utl.pV(                 ";Option="               , optn    );   // Option.Trim().Length      == 0 ? ""   :   ";Option="               + Option.Trim();
   //string connectString = _dsn + _drv + _svr + _inst + _port + _db + _usr + _pwd + _cnnliftm + _optn + _pool + _minpool + _maxpool;

   string connectstring = utl.pV(";DSN=", dsn) + utl.pV(";Driver=", drv) + utl.pV(dsn.Trim().Length == 0, ";Server=", svr) + utl.pV("\\", inst) + utl.pV(":", port) + utl.pV(";DataBase=", dbName) + utl.pV(";UID=", usr) + utl.pV(";PWD=", pwd) + utl.pV(";Connection Lifetime=", cnnliftm) + utl.pV(";Pooling=", pool) + utl.pV(pool, ";Min Pool Size=", minpool) + utl.pV(pool, ";Max Pool Size=", maxpool) + utl.pV(";Option=", optn);

   dbtec = dbtec.Trim().ToLower(); dbms  = dbms.Trim();
   if (dbtec.Equals("jdbc")) 
   {
    DUP_ROW = "JdbcException: Duplicate Rows";
    if (dbms.EndsWith("h2s")) { } //H2Sql
    if (dbms.EndsWith("mys")) { dsn = "mysql"; connectstring = utl.pV("jdbc:", dsn) + utl.pV("://", svr) + utl.pV("\\", inst) + utl.pV(":", port) + utl.pV("/", dbName) + utl.pV(";user=", usr) + utl.pV(";password=", pwd); DUP_ROW = "ERROR: duplicate key value violates unique constraint"; }
    if (dbms.EndsWith("pgs")) { dsn = "postgresql"; connectstring = utl.pV("jdbc:", dsn) + utl.pV("://", svr) + utl.pV("\\", inst) + utl.pV(":", port) + utl.pV("/", dbName) + utl.pV(";user=", usr) + utl.pV(";password=", pwd); DUP_ROW = "ERROR: duplicate key value violates unique constraint"; }
    if (dbms.EndsWith("fbd")) { } //FireBirdSql
    if (dbms.EndsWith("sql")) { } //SqLite
    if (dbms.EndsWith("mss")) { dsn = "sqlserver"; connectstring = utl.pV("jdbc:", dsn) + utl.pV("://", svr) + utl.pV("\\", inst) + utl.pV(":", port) + utl.pV(";databaseName=", dbName) + utl.pV(";user=", usr) + utl.pV(";password=", pwd); if (drv.StartsWith("{SQL Server Native Client 11")) DUP_ROW = "System.Data.Odbc.OdbcException (0x80131937)"; else DUP_ROW = "System.Data.Odbc.OdbcException: ERROR [23000]"; DUP_ROW = "Cannot insert duplicate key row in object"; }
    //if (dbms.EndsWith("syb")) { dsn = "sqlserver"; connectstring = utl.pV("jdbc:", dsn) + utl.pV("://", svr) + utl.pV("\\", inst) + utl.pV(":", port) + utl.pV(";databaseName=", db) + utl.pV(";user=", usr) + utl.pV(";password=", pwd); if (drv.StartsWith("{SQL Server Native Client 11")) DUP_ROW = "System.Data.Odbc.OdbcException (0x80131937)"; else DUP_ROW = "System.Data.Odbc.OdbcException: ERROR [23000]"; DUP_ROW = "Cannot insert duplicate key row in object"; }
    if (dbms.EndsWith("syb")) { dsn = "sybase"; connectstring = utl.pV("jdbc:", dsn) + utl.pV("://", svr) + utl.pV("\\", inst) + utl.pV(":", port) + utl.pV(";databaseName=", dbName) + utl.pV(";user=", usr) + utl.pV(";password=", pwd); if (drv.StartsWith("{SQL Server Native Client 11")) DUP_ROW = "System.Data.Odbc.OdbcException (0x80131937)"; else DUP_ROW = "System.Data.Odbc.OdbcException: ERROR [23000]"; DUP_ROW = "Cannot insert duplicate key row in object"; }
    if (dbms.EndsWith("ora")) { dsn = "oracle"; connectstring = utl.pV("jdbc:", dsn) + utl.pV("://", svr) + utl.pV("\\", inst) + utl.pV(":", port) + utl.pV("/", dbName) + utl.pV(";user=", usr) + utl.pV(";password=", pwd); DUP_ROW = "ERROR: duplicate key value violates unique constraint"; } 
    if (dbms.EndsWith("db2")) { } //DB2 or H2 in DB2 mode
    if (dbms.EndsWith("dby")) { } //Derby or H2 in Derby mode
    if (dbms.EndsWith("hsq")) { } //HSQLDB or H2 in HSQLDB mode
    if (dbms.EndsWith("tda")) { } //TeraData
    //connectstring = utl.pV("jdbc:", dsn) + utl.pV("://", svr) + utl.pV("\\", inst) + utl.pV(":", port) + utl.pV("/", db) + utl.pV(";user=", usr) + utl.pV(";pasword=", pwd) + utl.pV(";Connection Lifetime=", cnnliftm) + utl.pV(";Pooling=", pool) + utl.pV(pool, ";Min Pool Size=", minpool) + utl.pV(pool, ";Max Pool Size=", maxpool) + utl.pV(";Option=", optn);
    //connection = new JdbcConnection(connectstring);
   }
   else
    if (dbtec.Equals("odbc")) 
    {
     if (dbms.EndsWith("h2s")) { } //H2Sql
     if (dbms.EndsWith("mys")) { DUP_ROW = "System.Data.Odbc.OdbcException MySxxx"; } //MySql or H2 in MySql mode
     if (dbms.EndsWith("pgs")) { DUP_ROW = "System.Data.Odbc.OdbcException: ERROR [23505] ERROR: duplicate key"; DUP_ROW = "ERROR [23505] ERROR: duplicate key value violates unique constraint"; } //PostGreSql or H2 in Postgres mode
     if (dbms.EndsWith("fbd")) { } //FireBirdSql
     if (dbms.EndsWith("sql")) { } //SqLite
     if (dbms.EndsWith("mss")) { if (drv.StartsWith("{SQL Server Native Client 11")) DUP_ROW = "System.Data.Odbc.OdbcException (0x80131937)"; else DUP_ROW = "ERROR [23000] [Microsoft][ODBC SQL Server Driver][SQL Server]Cannot insert duplicate key row"; } //MsSql or H2 in MsSql mode
     if (dbms.EndsWith("syb")) { if (drv.StartsWith("{SQL Server Native Client 11")) DUP_ROW = "System.Data.Odbc.OdbcException (0x80131937)"; else DUP_ROW = "ERROR [23000] [Microsoft][ODBC SQL Server Driver][SQL Server]Cannot insert duplicate key row"; } //MsSql or H2 in MsSql mode
     if (dbms.EndsWith("ora")) { DUP_ROW = "System.Data.Odbc.OdbcException ORAxxx"; } //Oracle or H2 in Oracle mode
     if (dbms.EndsWith("db2")) { } //DB2 or H2 in DB2 mode
     if (dbms.EndsWith("dby")) { } //Derby or H2 in Derby mode
     if (dbms.EndsWith("hsq")) { } //HSQLDB or H2 in HSQLDB mode
     if (dbms.EndsWith("tda")) { } //TeraData

     //connection = new OdbcConnection(utl.pV(";DSN=", dsn) + utl.pV(";Driver=", drv) + utl.pV(dsn.Trim().Length == 0, ";Server=", svr) + utl.pV("\\", inst) + utl.pV(";DataBase=", db) + utl.pV(";UID=", usr) + utl.pV(";PWD=", pwd) + utl.pV(";Connection Lifetime=", cnnliftm) + utl.pV(";Pooling=", pool) + utl.pV(pool, ";Min Pool Size=", minpool) + utl.pV(pool, ";Max Pool Size=", maxpool) + utl.pV(";Option=", optn));
     connection = new OdbcConnection(connectstring);
    }
   else if (dbtec.Equals("dotnet")) 
   {
    connectstring = utl.pV(";Server=", svr) + utl.pV("\\", inst) +  utl.pV(";Port=", port) + utl.pV(";DataBase=", dbName) + utl.pV(";User Id=", usr) + utl.pV(";Password=", pwd) + utl.pV(";Protocol=", ptcl) + utl.pV(";Timeout=", cnnliftm) + utl.pV(";CommandTimeout=", cmdliftm) + utl.pV(";Pooling=", pool) + utl.pV(pool, ";Min Pool Size=", minpool) + utl.pV(pool, ";Max Pool Size=", maxpool);
#if DOTNET_H2S
    if (dbms.Equals("h2s"))
    {
     DUP_ROW = "System.Data.Odbc.OdbcException ORAxxx";  //to be found: correct Exception string
     //connectstring = connectstring.Replace("Server=", "Host=");
     connection = new OracleConnection(connectstring);
     return;
    }
#endif
#if DOTNET_MYS
    if (dbms.Equals("mys"))
    {
     DUP_ROW = "System.Data.Odbc.OdbcException MySxxx";  //to be found: correct Exception string
     connection = new MySqlConnection(connectstring);
     return;
    }
#endif
#if DOTNET_2MYS
    if (dbms.Equals("2mys"))
    {
     DUP_ROW = "System.Data.Odbc.OdbcException MySxxx";  //to be found: correct Exception string
     connection = new MySqlConnection(connectstring);
     return;
    }
#endif
#if DOTNET_PGS
    if (dbms.Equals("pgs"))
    {
     DUP_ROW = "ERROR: 23505: duplicate key value violates unique constraint";
     connection = new NpgsqlConnection(utl.pV(";Server=", svr) + utl.pV("\\", inst) +  utl.pV(";Port=", port) + utl.pV(";DataBase=", dbName) + utl.pV(";User Id=", usr) + utl.pV(";Password=", pwd) + utl.pV(";Protocol=", ptcl) + utl.pV(";Timeout=", cnnliftm) + utl.pV(";CommandTimeout=", cmdliftm) + utl.pV(";Pooling=", pool) + utl.pV(pool, ";Min Pool Size=", minpool) + utl.pV(pool, ";Max Pool Size=", maxpool));
     return;
    }
#endif
#if DOTNET_2PGS
    if (dbms.Equals("2pgs"))
    {
     DUP_ROW = "ERROR: 23505: duplicate key value violates unique constraint";
     connection = new NpgsqlConnection(utl.pV(";Server=", svr) + utl.pV("\\", inst) +  utl.pV(";Port=", port) + utl.pV(";DataBase=", dbName) + utl.pV(";User Id=", usr) + utl.pV(";Password=", pwd) + utl.pV(";Protocol=", ptcl) + utl.pV(";Timeout=", cnnliftm) + utl.pV(";CommandTimeout=", cmdliftm) + utl.pV(";Pooling=", pool) + utl.pV(pool, ";Min Pool Size=", minpool) + utl.pV(pool, ";Max Pool Size=", maxpool));
     return;
    }
#endif
#if DOTNET_FBD
    if (dbms.Equals("fbd"))
    {
     DUP_ROW = "ERROR: 23505: duplicate key value violates unique constraint";
     connection = new NpgsqlConnection(utl.pV(";Server=", svr) + utl.pV("\\", inst) +  utl.pV(";Port=", port) + utl.pV(";DataBase=", dbName) + utl.pV(";User Id=", usr) + utl.pV(";Password=", pwd) + utl.pV(";Protocol=", ptcl) + utl.pV(";Timeout=", cnnliftm) + utl.pV(";CommandTimeout=", cmdliftm) + utl.pV(";Pooling=", pool) + utl.pV(pool, ";Min Pool Size=", minpool) + utl.pV(pool, ";Max Pool Size=", maxpool));
     return;
    }
#endif
#if DOTNET_SQL
    if (dbms.Equals("sql"))
    {
     DUP_ROW = "ERROR: 23505: duplicate key value violates unique constraint";
     connection = new NpgsqlConnection(utl.pV(";Server=", svr) + utl.pV("\\", inst) +  utl.pV(";Port=", port) + utl.pV(";DataBase=", dbName) + utl.pV(";User Id=", usr) + utl.pV(";Password=", pwd) + utl.pV(";Protocol=", ptcl) + utl.pV(";Timeout=", cnnliftm) + utl.pV(";CommandTimeout=", cmdliftm) + utl.pV(";Pooling=", pool) + utl.pV(pool, ";Min Pool Size=", minpool) + utl.pV(pool, ";Max Pool Size=", maxpool));
     return;
    }
#endif
#if DOTNET_MSS
    if (dbms.Equals("mss"))
    {
     DUP_ROW = "Cannot insert duplicate key row"; //to be found: correct Exception string
     connection = new SqlConnection(utl.pV(";Server=", svr) + utl.pV("\\", inst) +  utl.pV(";Port=", port) + utl.pV(";DataBase=", dbName) + utl.pV(";User Id=", usr) + utl.pV(";Password=", pwd) + utl.pV(";Timeout=", cnnliftm) + utl.pV(";CommandTimeout=", cmdliftm) + utl.pV(";Pooling=", pool) + utl.pV(pool, ";Min Pool Size=", minpool) + utl.pV(pool, ";Max Pool Size=", maxpool));
     return;
    }
#endif
#if DOTNET_2MSS
    if (dbms.Equals("2mss"))
    {
     DUP_ROW = "Cannot insert duplicate key row"; //to be found: correct Exception string
     connection = new SqlConnection(utl.pV(";Server=", svr) + utl.pV("\\", inst) +  utl.pV(";Port=", port) + utl.pV(";DataBase=", dbName) + utl.pV(";User Id=", usr) + utl.pV(";Password=", pwd) + utl.pV(";Timeout=", cnnliftm) + utl.pV(";CommandTimeout=", cmdliftm) + utl.pV(";Pooling=", pool) + utl.pV(pool, ";Min Pool Size=", minpool) + utl.pV(pool, ";Max Pool Size=", maxpool));
     return;
    }
#endif
#if DOTNET_ORA
    if (dbms.Equals("ora"))
    {
     DUP_ROW = "System.Data.Odbc.OdbcException ORAxxx";  //to be found: correct Exception string
     //connectstring = connectstring.Replace("Server=", "Host=");
     connection = new OracleConnection(connectstring);
     return;
    }
#endif
#if DOTNET_2ORA
    if (dbms.Equals("2ora"))
    {
     DUP_ROW = "System.Data.Odbc.OdbcException ORAxxx";  //to be found: correct Exception string
     //connectstring = connectstring.Replace("Server=", "Host=");
     connection = new OracleConnection(connectstring);
     return;
    }
#endif
#if DOTNET_DB2
    if (dbms.Equals("db2"))
    {
     DUP_ROW = "System.Data.Odbc.OdbcException ORAxxx";  //to be found: correct Exception string
     //connectstring = connectstring.Replace("Server=", "Host=");
     connection = new OracleConnection(connectstring);
     return;
    }
#endif
#if DOTNET_2DB2
    if (dbms.Equals("2db2"))
    {
     DUP_ROW = "System.Data.Odbc.OdbcException ORAxxx";  //to be found: correct Exception string
     //connectstring = connectstring.Replace("Server=", "Host=");
     connection = new OracleConnection(connectstring);
     return;
    }
#endif
#if DOTNET_DBY
    if (dbms.Equals("dby"))
    {
     DUP_ROW = "System.Data.Odbc.OdbcException ORAxxx";  //to be found: correct Exception string
     //connectstring = connectstring.Replace("Server=", "Host=");
     connection = new OracleConnection(connectstring);
     return;
    }
#endif
#if DOTNET_2DBY
    if (dbms.Equals("2dby"))
    {
     DUP_ROW = "System.Data.Odbc.OdbcException ORAxxx";  //to be found: correct Exception string
     //connectstring = connectstring.Replace("Server=", "Host=");
     connection = new OracleConnection(connectstring);
     return;
    }
#endif
#if DOTNET_HSQ
    if (dbms.Equals("hsq"))
    {
     DUP_ROW = "System.Data.Odbc.OdbcException ORAxxx";  //to be found: correct Exception string
     //connectstring = connectstring.Replace("Server=", "Host=");
     connection = new OracleConnection(connectstring);
     return;
    }
#endif
#if DOTNET_2HSQ
    if (dbms.Equals("2hsq"))
    {
     DUP_ROW = "System.Data.Odbc.OdbcException ORAxxx";  //to be found: correct Exception string
     //connectstring = connectstring.Replace("Server=", "Host=");
     connection = new OracleConnection(connectstring);
     return;
    }
#if DOTNET_TDA
    if (dbms.Equals("tda"))
    {
     DUP_ROW = "System.Data.Odbc.OdbcException Tdaxxx";  //to be found: correct Exception string
     connection = new MySqlConnection(connectstring);
     return;
    }
#endif
#endif
    throw new Exception("Err: unknown DBMS " + dbms);            
   } 
   else throw new Exception("Err: unknown DbConnect Technology " + dbtec);            

   //((SqlConnection)connection).GetLifetimeService;
  }
 }
}





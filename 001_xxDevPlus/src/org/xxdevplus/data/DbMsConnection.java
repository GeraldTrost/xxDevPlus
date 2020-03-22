/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.xxdevplus.data;

import java.sql.Connection;
import org.xxdevplus.struct.KeyPile;
import org.xxdevplus.utl.utl;

/**
 *
 * @author GeTr
 */


 public class DbMsConnection
 {
  //Copyright© 1996-2020 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties, You may use, modify, distribute this code under GNU public licence for NON-Commercial use as long as this notice remains unchanged.
  private static boolean selfTested  = false; private static String ass(boolean expr) throws Exception { if (!expr) throw new Exception(ass(true) + " SelfTest Failure"); return "DbMsConnection"; } private static void selfTest() { selfTested = true; }

  DbConnection connection = null;

  public int ConnectionTimeout () { return connection.ConnectionTimeout();  } public void ConnectionTimeout (int value) { connection.sConnectionTimeout(value); }
  
  public void               Dispose            (                    ) throws Exception { connection.Dispose(); }
  public void               Close              (                    ) throws Exception { connection.Close(); }
  
  public DbMsCommand        CreateCommand      (                    ) throws Exception { return new DbMsCommand(DUP_ROW, connection.CreateCommand());  }

  public DbMsTransaction    BeginTransaction   (                    ) throws Exception { try { return new DbMsTransaction(connection.BeginTransaction(IsolationLevel.ReadUnCommitted)); } catch (Exception ex) { return new DbMsTransaction(connection.BeginTransaction()); } }

  public void               ChangeDatabase     (String dbName       ) throws Exception {connection.ChangeDatabase(dbName);}
  public void               Open               (                    ) throws Exception {connection.Open(); }

  /*
  public String pdf(String  prefix, String   val                              ) { if (val.trim().length() == 0) return "";                          return prefix.trim() + val.trim(); }
  public String pdf(String  prefix, String   val    ,              String dflt) { if (val.trim().length() == 0) return prefix.trim() + dflt.trim(); return prefix.trim() + val.trim(); }
  public String pdf(String  prefix, long     val                              ) { if (val                 == 0) return "";                          return prefix.trim() + val; }
  public String pdf(String  prefix, long     val    ,              long   dflt) { if (val                 == 0) return prefix.trim() + dflt;        return prefix.trim() + val; }
  public String pdf(String  prefix, boolean  val                              ) {                                                                   return prefix.trim() + val; }
  public String pdf(boolean cnd   , String   prefix , String  val             ) { if (!cnd) return ""; if (val.trim().length() == 0) return "";                          return prefix.trim() + val.trim(); }
  public String pdf(boolean cnd   , String   prefix , String  val, String dflt) { if (!cnd) return ""; if (val.trim().length() == 0) return prefix.trim() + dflt.trim(); return prefix.trim() + val.trim(); }
  public String pdf(boolean cnd   , String   prefix , long    val             ) { if (!cnd) return ""; if (val                 == 0) return "";                          return prefix.trim() + val; }
  public String pdf(boolean cnd   , String   prefix , long    val, long   dflt) { if (!cnd) return ""; if (val                 == 0) return prefix.trim() + dflt; return prefix.trim() + val; }
  public String pdf(boolean cnd   , String   prefix , boolean val             ) { if (!cnd) return ""; return prefix.trim() + val; }
  */

  String DUP_ROW = "";

  public String  dbtec    = "";
  public String  dbms     = "";
  public String  dsn      = "";
  public String  drv      = "";
  public String  svr      = "";
  public String  inst     = "";
  public long    port     = 0;
  public String  db       = "";
  public String  sm       = "";
  public String  usr      = "";
  public String  pwd      = "";
  public long    cnnliftm = 0;
  public long    cmdliftm = 0;
  public String  ptcl     = "";
  public String  optn     = "";
  public boolean pool     = false;
  public long    minpool  = 0;
  public long    maxpool  = 0;

  DbMsConnection(Db db, String dbtec, String dbms, String dsn, String drv, String svr, String inst, long port, String dbName, String sm, String usr, String pwd, long cnnliftm, long cmdliftm, String ptcl, String optn, boolean pool, long minpool, long maxpool) throws Exception
  {

   this.dbtec = dbtec; this.dbms = dbms; this.dsn = dsn; this.drv = drv; this.svr = svr; this.inst = inst; this.port = port; this.db = dbName;  this.sm = sm; this.usr = usr; this.pwd = pwd; this.cnnliftm = cnnliftm; this.cmdliftm = cmdliftm; this.ptcl = ptcl; this.optn = optn; this.pool = pool; this.minpool = minpool; this.maxpool = maxpool;

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

   String connectstring = utl.pV(";DSN=", dsn) + utl.pV(";Driver=", drv) + utl.pV(dsn.trim().length() == 0, ";Server=", svr) + utl.pV("\\", inst) + utl.pV(":", port) + utl.pV(";DataBase=", dbName) + utl.pV(";UID=", usr) + utl.pV(";PWD=", pwd) + utl.pV(";Connection Lifetime=", cnnliftm) + utl.pV(";Pooling=", pool) + utl.pV(pool, ";Min Pool Size=", minpool) + utl.pV(pool, ";Max Pool Size=", maxpool) + utl.pV(";Option=", optn);

   dbtec = dbtec.trim().toLowerCase(); dbms  = dbms.trim();
   if (dbtec.equals("jdbc")) 
   {
    DUP_ROW = "JdbcException: Duplicate Rows";
    if (dbms.endsWith("h2s"))  { dsn = "h2:"              ; connectstring = utl.pV("jdbc:", dsn) + utl.pV("//", svr) + utl.pV("\\", inst) + utl.pV(":", port) + utl.pV("/", dbName) + utl.pV(";user=", usr) + utl.pV(";password=", pwd); DUP_ROW = "ERROR: duplicate key value violates unique constraint";} //H2Sql
    if (dbms.endsWith("mys"))  { dsn = "mysql:"           ; connectstring = utl.pV("jdbc:", dsn) + utl.pV("//", svr) + utl.pV("\\", inst) + utl.pV(":", port) + utl.pV("/", dbName) + utl.pV(";user=", usr) + utl.pV(";password=", pwd); DUP_ROW = "ERROR: duplicate key value violates unique constraint";}
    if (dbms.endsWith("pgs"))  { dsn = "postgresql:"      ; connectstring = utl.pV("jdbc:", dsn) + utl.pV("//", svr) + utl.pV("\\", inst) + utl.pV(":", port) + utl.pV("/", dbName) + utl.pV(";user=", usr) + utl.pV(";password=", pwd); DUP_ROW = "ERROR: duplicate key value violates unique constraint";}
    if (dbms.endsWith("fbd"))  { } //FireBirdSql
    if (dbms.endsWith("sql"))  { } //SqLite
    if (dbms.endsWith("mss"))  { dsn = "sqlserver:"       ; connectstring = utl.pV("jdbc:", dsn) + utl.pV("//", svr) + utl.pV("\\", inst) + utl.pV(":", port) + utl.pV(";databaseName=", dbName) + utl.pV(";user=", usr) + utl.pV(";password=", pwd); if (drv.startsWith("{SQL Server Native Client 11")) DUP_ROW = "System.Data.Odbc.OdbcException (0x80131937)"; else DUP_ROW = "System.Data.Odbc.OdbcException: ERROR [23000]"; DUP_ROW = "Cannot insert duplicate key row in object"; }
    //if (dbms.endsWith("syb")){ dsn = "jtds:sqlserver"   ; connectstring = utl.pV("jdbc:", dsn) + utl.pV("//", svr) + utl.pV("\\", inst) + utl.pV(":", port) + utl.pV(";databaseName=", db) + utl.pV(";user=", usr) + utl.pV(";password=", pwd); if (drv.startsWith("{SQL Server Native Client 11")) DUP_ROW = "System.Data.Odbc.OdbcException (0x80131937)"; else DUP_ROW = "System.Data.Odbc.OdbcException: ERROR [23000]"; DUP_ROW = "Cannot insert duplicate key row in object"; }
    if (dbms.endsWith("syb"))  { dsn = "jtds:sybase:"     ; connectstring = utl.pV("jdbc:", dsn) + utl.pV("//", svr) + utl.pV("\\", inst) + utl.pV(":", port) + utl.pV(";databaseName=", dbName) + utl.pV(";user=", usr) + utl.pV(";password=", pwd); if (drv.startsWith("{SQL Server Native Client 11")) DUP_ROW = "System.Data.Odbc.OdbcException (0x80131937)"; else DUP_ROW = "System.Data.Odbc.OdbcException: ERROR [23000]"; DUP_ROW = "Cannot insert duplicate key row in object"; }
    if (dbms.endsWith("ora"))  { dsn = "oracle:thin";     if (port == 0) port = 1521; connectstring = utl.pV("jdbc:", dsn) + utl.pV(":", usr) + utl.pV("/", pwd) + "@" + svr + utl.pV(":", port) + utl.pV(":", inst) + utl.pV(";user=", usr) + utl.pV(";password=", pwd); DUP_ROW = "ERROR: duplicate key value violates unique constraint"; }
    if (dbms.endsWith("db2"))  { } //DB2 or H2 in DB2 mode
    if (dbms.endsWith("dby"))  { } //Derby or H2 in Derby mode
    if (dbms.endsWith("hsq"))  { } //HSQLDB or H2 in HSQLDB mode
    if (dbms.endsWith("tda"))  { } //Novell teraData
    //connectstring = utl.pV("jdbc:", dsn) + utl.pV("://", svr) + utl.pV("\\", inst) + utl.pV(":", port) + utl.pV("/", db) + utl.pV(";user=", usr) + utl.pV(";pasword=", pwd) + utl.pV(";Connection Lifetime=", cnnliftm) + utl.pV(";Pooling=", pool) + utl.pV(pool, ";Min Pool Size=", minpool) + utl.pV(pool, ";Max Pool Size=", maxpool) + utl.pV(";Option=", optn);
    connection = new JdbcConnection(db, connectstring);
   }
   else
    if (dbtec.equals("odbc"))
    {
     if (dbms.endsWith("h2s"))  { } //H2Sql
     if (dbms.endsWith("mys"))  { DUP_ROW = "System.Data.Odbc.OdbcException MySxxx"; } //MySql or H2 in MySql mode
     if (dbms.endsWith("pgs"))  { DUP_ROW = "System.Data.Odbc.OdbcException: ERROR [23505] ERROR: duplicate key"; DUP_ROW = "ERROR [23505] ERROR: duplicate key value violates unique constraint"; } //PostGreSql or H2 in Postgres mode
     if (dbms.endsWith("fbd"))  { } //FireBirdSql
     if (dbms.endsWith("sql"))  { } //SqLite
     if (dbms.endsWith("mss"))  { if (drv.startsWith("{SQL Server Native Client 11")) DUP_ROW = "System.Data.Odbc.OdbcException (0x80131937)"; else DUP_ROW = "ERROR [23000] [Microsoft][ODBC SQL Server Driver][SQL Server]Cannot insert duplicate key row"; } //MsSql or H2 in MsSql mode
     if (dbms.endsWith("syb"))  { if (drv.startsWith("{SQL Server Native Client 11")) DUP_ROW = "System.Data.Odbc.OdbcException (0x80131937)"; else DUP_ROW = "ERROR [23000] [Microsoft][ODBC SQL Server Driver][SQL Server]Cannot insert duplicate key row"; } //MsSql or H2 in MsSql mode
     if (dbms.endsWith("ora"))  { DUP_ROW = "System.Data.Odbc.OdbcException ORAxxx"; } //Oracle or H2 in Oracle mode
     if (dbms.endsWith("db2"))  { } //DB2 or H2 in DB2 mode
     if (dbms.endsWith("dby"))  { } //Derby or H2 in Derby mode
     if (dbms.endsWith("hsq"))  { } //HSQLDB or H2 in HSQLDB mode
     if (dbms.endsWith("tda"))  { } //Novell teraData
     //connection = new OdbcConnection(utl.pV(";DSN=", dsn) + utl.pV(";Driver=", drv) + utl.pV(dsn.Trim().Length == 0, ";Server=", svr) + utl.pV("\\", inst) + utl.pV(";DataBase=", db) + utl.pV(";UID=", usr) + utl.pV(";PWD=", pwd) + utl.pV(";Connection Lifetime=", cnnliftm) + pdf(";Pooling=", pool) + pdf(pool, ";Min Pool Size=", minpool) + pdf(pool, ";Max Pool Size=", maxpool) + pdf(";Option=", optn));

     
     connection = new OdbcConnection(db, connectstring);



    }
   else throw new Exception("Err: unknown DbConnect Technology " + dbtec);            
   //((SqlConnection)connection).GetLifetimeService;
  }
  
 }



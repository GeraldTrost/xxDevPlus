




//** @author  GeTr Copyright© 1996-2020 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties
//** @license You may use, modify, distribute this code under GNU public dual licence free for NON-Commercial use as long as this notice remains unchanged.
//** @comment Database Object providing access to different DBMS Products


package org.xxdevplus.data;


import org.xxdevplus.utl.ctx;
import org.xxdevplus.utl.utl;
import org.xxdevplus.struct.Pile;
import org.xxdevplus.struct.ObjPile;
import org.xxdevplus.sys.CancelledByUser;
import org.xxdevplus.utl.Registry;
import org.xxdevplus.struct.KeyPile;
import org.xxdevplus.frmlng.EvalExpert;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.lang.reflect.Array;
import java.sql.Timestamp;
import java.util.Locale;
import java.util.StringTokenizer;
import javax.swing.JButton;
import javax.swing.JComponent;
import org.xxdevplus.chain.Chain;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author GeTr
 */

 public class Db implements ActionListener, EvalExpert   //DocGetr: dbaccess is short for KnowledgeBaseDataBase, convention: class names in lower case indicate static classes
 {
  //Copyright© 1996-2020 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties, You may use, modify, distribute this code under GNU public licence for NON-Commercial use as long as this notice remains unchanged.
  private static boolean selfTested  = false;  private static String ass(boolean expr) throws Exception { if (!expr) throw new Exception(ass(true) + " SelfTest Failure"); return "Db"; } 

  private KeyPile<String, String[]> drivers = new KeyPile<String, String[]>();
  public KeyPile<String, String[]> Drivers() { return drivers; } public void Drivers(KeyPile<String, String[]> value) { drivers = value; }
 
  protected KeyPile<String, File> tmpDatFiles = new KeyPile<String, File>();
  
  public boolean tmpDatFileExists(String table) throws Exception
  {
   return tmpDatFiles.hasKey(table);
  }
    
  public void DelTmpDatFile(String table) throws Exception
  {
   if (!tmpDatFileExists(table)) return;
   tmpDatFiles.g(table).delete();
   tmpDatFiles.Del(table);
  }

  public File tmpDatFile(String table) throws Exception
  {
   if (!tmpDatFileExists(table)) 
   {
    tmpDatFiles.Add(table, new File(System.getenv("TMP") + "/$" + table + ".txt"));
    utl.s2f("", tmpDatFile(table).getAbsolutePath(), false);
   }
   return tmpDatFiles.g(table);
  }

  private static void selfTest() throws Exception
  {
   Locale.setDefault(Locale.ENGLISH);  //Note: for english exceptions; Important: PostgreSQL install with ENGLISH as default Locale
   try { Class.forName( "sun.jdbc.odbc.JdbcOdbcDriver"                   ); } catch (Exception ex) {if (false) utl.say("Info: sun.jdbc.odbc.JdbcOdbcDriver not found: "                 + ex.getMessage());} // Connection cn = DriverManager.getConnection( "jdbc:odbc:" + sDsn, dbUsr, dbPwd );

   /*
   try { Class.forName( "org.h2.Driver"                                  ); } catch (Exception ex) { if (false) utl.say("Info: org.h2.Driver: "                                         + ex.getMessage()); } // Connection cn = DriverManager.getConnection( "jdbc:odbc:" + sDsn, dbUsr, dbPwd );
   try { Class.forName( "com.mysql.jdbc.Driver"                          ); } catch (Exception ex) {if (false) utl.say("Info: com.mysql.jdbc.Driver not found: "                        + ex.getMessage());} // Connection cn = DriverManager.getConnection( "jdbc:mysql://MyDbComputerNameOrIP:3306/myDatabaseName", dbUsr, dbPwd );
   try { Class.forName( "org.postgresql.Driver"                          ); } catch (Exception ex) {if (false) utl.say("Info: org.postgresql.Driver not found: "                        + ex.getMessage());} // Connection cn = DriverManager.getConnection( "jdbc:postgresql://MyDbComputerNameOrIP/myDatabaseName", dbUsr, dbPwd );
   try { Class.forName( "oracle.jdbc.OracleDriver"                       ); } catch (Exception ex) {if (false) utl.say("Info: oracle.jdbc.OracleDriver not found: "                     + ex.getMessage());} // Connection cn = DriverManager.getConnection( "jdbc:oracle:thin:@MyDbComputerNameOrIP:1521:ORCL", dbUsr, dbPwd );
   try { Class.forName( "COM.ibm.db2.jdbc.app.DB2Driver"                 ); } catch (Exception ex) {if (false) utl.say("Info: COM.ibm.db2.jdbc.app.DB2Driver not found: "               + ex.getMessage());} // Connection cn = DriverManager.getConnection( "jdbc:db2:myDatabaseName", dbUsr, dbPwd );
   try { Class.forName( "com.ibm.as400.access.AS400JDBCDriver"           ); } catch (Exception ex) {if (false) utl.say("Info: com.ibm.as400.access.AS400JDBCDriver not found: "         + ex.getMessage());} // Connection cn = DriverManager.getConnection( "jdbc:as400://MyDbComputerNameOrIP/MyLib", dbUsr, dbPwd );   cn = DriverManager.getConnection( "jdbc:as400://MyDbComputerNameOrIP;naming=system;libraries=,lib1,lib2,lib3", dbUsr, dbPwd );
   try { Class.forName( "interbase.interclient.Driver"                   ); } catch (Exception ex) {if (false) utl.say("Info: interbase.interclient.Driver not found: "                 + ex.getMessage());} // Connection cn = DriverManager.getConnection( "jdbc:interbase://MyDbComputerNameOrIP/myDatabasePath/myDatabaseFile", dbUsr, dbPwd );
   try { Class.forName( "com.sybase.jdbc2.jdbc.SybDriver"                ); } catch (Exception ex) {if (false) utl.say("Info: com.sybase.jdbc2.jdbc.SybDriver not found: "              + ex.getMessage());} // Connection cn = DriverManager.getConnection( "jdbc:sybase:Tds:MyDbComputerNameOrIP:2638", dbUsr, dbPwd );
   try { Class.forName( "net.sourceforge.jtds.jdbc.Driver"               ); } catch (Exception ex) {if (false) utl.say("Info: net.sourceforge.jtds.jdbc.Driver not found: "             + ex.getMessage());} // Connection cn = DriverManager.getConnection( "jdbc:jtds:sqlserver://MyDbComputerNameOrIP:1433/master", dbUsr, dbPwd );
   try { Class.forName( "com.microsoft.sqlserver.jdbc.SQLServerDriver"   ); } catch (Exception ex) {if (false) utl.say("Info: com.microsoft.sqlserver.jdbc.SQLServerDriver not found: " + ex.getMessage());} // Connection cn = DriverManager.getConnection( "jdbc:microsoft:sqlserver://MyDbComputerNameOrIP:1433", dbUsr, dbPwd );
   */

   selfTested = true; 
  }

  private Exception ActionResult = null;

  private void btnOk_Click(Object sender, ActionEvent e)
  {
   try
   {
    frmDbConnect f = ((frmDbConnect)((JComponent)sender).getParent().getParent().getParent().getParent());
    try {init(f.Url().ConnectString(false));} catch (Exception ex) { utl.say("Error connecting to Database\r\n" + ex.getMessage() + "\r\n" + ex.getStackTrace()); }
    if (reusedDbMsConnection != null) f.dispose();
    //if (reusedDbMsConnection == null) throw new Exception("Database connection failed!");
   }
   catch (Exception ex) { ActionResult  = ex; }
  }

  private void btnCancel_Click(Object sender, ActionEvent e) 
  {
   try
   {
    ((frmDbConnect)((JComponent)sender).getParent().getParent().getParent().getParent()).dispose();
    throw new CancelledByUser();
   }
   catch (Exception ex) { ActionResult  = ex; }
  }

  private void btnTest_Click(Object sender, ActionEvent e)
  {
   try
   {
    Db dba = null;
    String Dbms = utl.cutl(((frmDbConnect)((JComponent)sender).getParent().getParent().getParent().getParent()).pupDbms.getSelectedItem().toString().trim(), " ");
    String DbTec = ((frmDbConnect)((JComponent)sender).getParent().getParent().getParent().getParent()).pupDbTec.getSelectedItem().toString().trim();
    try { dba = new Db(DbTec + ":" + Dbms + "::" + ((frmDbConnect)((JComponent)sender).getParent().getParent().getParent().getParent()).txtInstance.getText().trim() + "," + ((frmDbConnect)((JComponent)sender).getParent().getParent().getParent().getParent()).txtDatabase.getText().trim() + "," + ((frmDbConnect)((JComponent)sender).getParent().getParent().getParent().getParent()).txtUser.getText().trim() + "," + ((frmDbConnect)((JComponent)sender).getParent().getParent().getParent().getParent()).txtPassword.getText().trim(), Drivers()); }
    catch (Exception ex) { utl.say("Error connecting to Database\r\n" + ex.getMessage() + "\r\n" + ex.getStackTrace()); }
    if (dba != null) utl.say("Successfully connected!");
   }
   catch (Exception ex) { ActionResult  = ex; }
  }

  public void actionPerformed(ActionEvent e)
  {
   if (((JButton)(e.getSource())).getName().equals("btnCancel")) btnCancel_Click (e.getSource(), e); 
   if (((JButton)(e.getSource())).getName().equals("btnOk"))     btnOk_Click     (e.getSource(), e); 
   if (((JButton)(e.getSource())).getName().equals("btnTest"))   btnTest_Click   (e.getSource(), e); 
  }

  public Db(KeyPile<String, String[]> dbDrivers) throws Exception { this.Drivers(dbDrivers); init(""); }

  public Db(String connectstring, KeyPile<String, String[]> dbDrivers) throws Exception 
  { 
   this.Drivers(dbDrivers); 
   init(connectstring); 
  }
  
  public Db(Object owner, KeyPile<String, String[]> dbDrivers) throws Exception
  {
   ActionResult = null;
   this.Drivers(dbDrivers);
   frmDbConnect f = new frmDbConnect(this);
   f.btnCancel.addActionListener ((ActionListener) this);      //f.btnCancel.Click += btnCancel_Click;
   f.btnOk.addActionListener     ((ActionListener) this);      //f.btnOk.Click += btnOk_Click;
   f.btnTest.addActionListener   ((ActionListener) this);      //f.btnTest.Click += btnTest_Click;
   if (url == null) url = new DbUrl("", Drivers());
   if (lastUrl == null) lastUrl = url;
   f.Url(lastUrl);
   //f.ConnectString(lastDbTec + ":" + lastDbms + "::" + lastHost + "\\" + lastInstance + "," + lastDatabase + "," + lastUser + "," + lastPassword);
   /*
   for(int i = 1; i <= f.pupDbms().getModel().getSize(); i++) if (((String)(f.pupDbms().getModel().getElementAt(i - 1))).startsWith(lastDbms + " ")) f.pupDbms().setSelectedIndex(i - 1);
   f.txtInstance().setText   ( lastHost + "\\" + lastInstance );
   f.txtDatabase().setText   ( lastDatabase );
   f.txtUser().setText       ( lastUser );
   f.txtPassword().setText   ( lastPassword );
   */
   try
   {
    f.setModal(true);
    f.setVisible(true);
    if (f != f) throw new CancelledByUser();
    if (ActionResult != null) throw ActionResult ;
   }
   catch (CancelledByUser ex) {throw ex;}
  }

  public static Object[] cloneObjectArray(Pile<Object> pile) throws Exception
  {
   return pile.array();
  }

  public static DbCnd[] cloneCndArray(Pile<DbCnd> pile) throws Exception
  {
   Object[] objArray = pile.array();
   DbCnd[] ret = (DbCnd[]) Array.newInstance(DbCnd.class, objArray.length);
   System.arraycopy(objArray, 0, ret, 0, objArray.length);
   return ret;
  }

  public static DbSlc[] cloneSlcArray(Pile<DbSlc> pile) throws Exception
  {
   Object[] objArray = pile.array();
   DbSlc[] ret = (DbSlc[]) Array.newInstance(DbSlc.class, objArray.length);
   System.arraycopy(objArray, 0, ret, 0, objArray.length);
   return ret;
  }

  public static String[] cloneStringArray(Pile<String> pile) throws Exception
  {
   Object[] objArray = pile.array();
   String[] ret = (String[]) Array.newInstance(String.class, objArray.length);
   System.arraycopy(objArray, 0, ret, 0, objArray.length);
   return ret;
  }

  public static DbField[] cloneDbFieldArray(Pile<DbField> pile) throws Exception
  {
   Object[] objArray = pile.array();
   DbField[] ret = (DbField[]) Array.newInstance(DbField.class, objArray.length);
   System.arraycopy(objArray, 0, ret, 0, objArray.length);
   return ret;
  }


  private KeyPile<String, DbJoin> tables       = new KeyPile<String, DbJoin>();
  private KeyPile<String, String> convFormats  = new KeyPile<String, String>();
  private KeyPile<String, String> dtv          = new KeyPile<String, String>();                                                   //the directive an EvalExpert supplies for the Term's or Expression's val() function
  public  KeyPile<String, String> Dtv()        { return dtv;} public void Dtv(KeyPile<String, String> value) { dtv  = value; }    //the directive an EvalExpert supplies for the Term's or Expression's val() function

  
  //private String                DUP_ROW = "System.Data.Odbc.OdbcException: ERROR [23000]"; //this is valid only for Odbc drivers {SQL Server} and {SQL Native Client} !! for all others adaption must take place in inti() method !

  /**
  *
  * @param dbtec
  * @param dbms
  * @param dsn
  * @param drv
  * @param svr
  * @param port
  * @param inst
  * @param port
  * @param dbName
  * @param sm
  * @param usr
  * @param pwd
  * @param cnnliftm
  * @param cmdliftm
  * @param ptcl
  * @param optn
  * @param pool
  * @param minpool
  * @param maxpool
  * @return
  * @throws Exception
  */
  public static DbMsConnection  _newDbMsConnection  (Db db, String dbtec, String dbms, String dsn, String drv, String svr, String inst, long port, String dbName, String sm, String usr, String pwd, long cnnliftm, long cmdliftm, String ptcl, String optn, boolean pool, long minpool, long maxpool) throws Exception { return new DbMsConnection(db, dbtec, dbms, dsn, drv, svr, inst, port, dbName, sm, usr, pwd, cnnliftm, cmdliftm, ptcl, optn, pool, minpool, maxpool); }
  public static DbMsCommand     _newDbMsCommand     (Db db, String cmd, DbMsConnection conn) throws Exception { return new DbMsCommand(db, cmd, conn); }
  
  private DbMsConnection        reusedDbMsConnection;      //The DatabaseDriverConnection (odbc or jdbc resp.)


  private DbUrl                 url        = null;
  private DbUrl                 lastUrl    = null;
  //public  String              dbtec      = "";
  //public  String              dbms       = "";
  //private String              host       = "";
  //private long                port       =  0;
  //private String              instance   = "";
  //private String              database   = "";
  //private String              schema     = "";
  //private String              user       = "";
  //private String              password   = "";
  //private String              tfilter    = "";
  //private String              vfilter    = "";

  public  DbUrl                 Url    ()  {return url;}

  public  String                NullStgy          = ""; // "+" = autoAdd (f <> NULL) conditions to every select query, "-" = ignore selected rows with null values, "0" = convert selected NULL to empty string and to integer 0 resp, "" - do nothing, selected null will cause Exception, this is DEFAULT because WELL DESIGNED DATABASE MODELS SHOULD NOT ALLOW NULLS AT ALL!

  public KeyPile<String, DbJoin> Tables() { return tables; } 

  //private static String lastDbTec       = "";
  //private static String lastDbms        = "";
  //private static String lastHost        = "";
  //private static String lastInstance    = "";
  //private static String lastDatabase    = "";
  //private static String lastSchema      = "";
  //private static String lastUser        = "";
  //private static String lastPassword    = "";

  public String  Dbtec()     { return  url.dbtec        ;}
  public String  Dbms()      { return  url.dbms         ;}
  public String  Host()      { return  url.host         ;}
  public long    Port()      { return  url.port         ;}
  public String  Instance()  { return  url.instance     ;}
  public String  Database()  { return  url.database     ;}
  public String  Schema()    { return  url.schema       ;}
  public String  User()      { return  url.user         ;}
  public String  Password()  { return  url.password     ;}
  public String  TFilter()   { return  url.tfilter      ;}
  public String  VFilter()   { return  url.vfilter      ;}
  public String  Name()      { return  url.Name()       ;}

  private DbMsTransaction  transaction = null;
  private Pile<Object>     transactions = new Pile<Object>();

  public Chain r(String s) throws Exception { return new Chain(s); }

  public String val(Object obj) throws Exception
  {
   if (obj instanceof DbSlc) return ((DbSlc)obj).sql(this);
   throw new Exception("Err: invalid Type in Db.val()");
  }
  
  public DbCnd lot(String field, String... param) throws Exception
  {
   Pile<DbCnd>      res         = new Pile<DbCnd>();
   Pile<DbCnd>      LkTerns     = new Pile<DbCnd>();
   Pile<DbCnd>      CmpTerns    = new Pile<DbCnd>();
   Pile<String>   EqTerns     = new Pile<String>();
   for (String pp : param)
   {
    Chain p = new Chain(pp.trim());
    if ((p.StartsWith("(>")) || (p.StartsWith("(<")))
    {
     Chain op = p.upto(1, ")");
     p = p.after(op).Trim();
     if (op.at("(>=)").len() > 0) CmpTerns.Push(cd(field).GE(ds(p)));
     if (op.at("(<=)").len() > 0) CmpTerns.Push(cd(field).LE(ds(p)));
     if (op.at("(>)").len()  > 0) CmpTerns.Push(cd(field).GT(ds(p)));
     if (op.at("(<)").len()  > 0) CmpTerns.Push(cd(field).LT(ds(p)));
     if (op.at("(<>)").len() > 0) CmpTerns.Push(cd(field).nEQ(ds(p)));
    } else
    {
     if (p.at("*").len() == 0) EqTerns.Push(p.text()); else LkTerns.Push(cd(field).LK(ds(p.text().replace("*", "%"))));
    }
   }
   String l = ""; for (String p : EqTerns) l += ds(p) + ", "; if (l.length() > 0) l = l.substring(0, l.length() - 2);
   if (l.length() > 0) res.Push(cd(field).IN(l));
   res.Add(CmpTerns);
   res.Add(LkTerns);
   return OR(res);
  }

 public String toDB(String s, int maxLen)
 {
  if (maxLen > 0) if (s.length() > maxLen) s = s.substring(0, maxLen);
  return s.replace("'","''");
 }
 
 public String fromDB(String s)
 {
  //return s.replace("`", "'");
  return s;
 }
 

  
  public DbCnd lot(String field, Chain lines, String delim) throws Exception
  {
   Pile<String> res = new Pile<String>();
   while (lines.len() > 0) {res.Push(lines.before(1, delim).text()); lines = lines.after(1, delim).Trim(); }
   return lot(field, Db.cloneStringArray(res));
  }
  
  public DbCnd loT(String field, String... param) throws Exception
  {
   Pile<DbCnd>      res         = new Pile<DbCnd>();
   Pile<DbCnd>      LkTerns     = new Pile<DbCnd>();
   Pile<DbCnd>      CmpTerns    = new Pile<DbCnd>();
   Pile<String>   EqTerns     = new Pile<String>();
   for (String pp : param)
   {
    Chain p = new Chain(pp.trim());
    if ((p.startsWith(">")) || (p.startsWith("<")))
    {
     Chain op = p.before(1, " ");
     p = p.after(op).Trim();
     if (op.at(">=").len() > 0) CmpTerns.Push(cd(field).GE(dS(p)));
     if (op.at("<=").len() > 0) CmpTerns.Push(cd(field).LE(dS(p)));
     if (op.at(">").len()  > 0) CmpTerns.Push(cd(field).GT(dS(p)));
     if (op.at("<").len()  > 0) CmpTerns.Push(cd(field).LT(dS(p)));
     if (op.at("<>").len() > 0) CmpTerns.Push(cd(field).nEQ(dS(p)));
    } else
    {
     if (p.at("*").len() == 0) EqTerns.Push(p.text()); else LkTerns.Push(cd(field).LK(dS(p.text().replace("*", "%"))));
    }
   }
   String l = ""; for (String  p : EqTerns) l += dS(p) + ", "; if (l.length() > 0) l = l.substring(0, l.length() - 2);
   if (l.length() > 0) res.Push(cd(field).IN(l));
   res.Add(CmpTerns);
   res.Add(LkTerns);
   return OR(res);
  }

  public DbCnd loT(String field, Chain lines, String delim) throws Exception
  {
   Pile<String> res = new Pile<String>();
   while (lines.len() > 0) { res.Push(lines.before(1, delim).text()); lines = lines.after(1, delim).Trim(); }
   return loT(field, Db.cloneStringArray(res));
  }

  public DbCnd Lot(String field, String... param) throws Exception
  {
   Pile<DbCnd>      res         = new Pile<DbCnd>();
   Pile<DbCnd>      LkTerns     = new Pile<DbCnd>();
   Pile<DbCnd>      CmpTerns    = new Pile<DbCnd>();
   Pile<String>   EqTerns     = new Pile<String>();
   for (String pp : param)
   {
    Chain p = new Chain(pp.trim());
    if ((p.StartsWith(">")) || (p.StartsWith("<")))
    {
     Chain op = p.before(1, " ");
     p = p.after(op).Trim();
     if (op.at(">=").len() > 0) CmpTerns.Push(cd(field).GE(Ds(p)));
     if (op.at("<=").len() > 0) CmpTerns.Push(cd(field).LE(Ds(p)));
     if (op.at(">").len()  > 0) CmpTerns.Push(cd(field).GT(Ds(p)));
     if (op.at("<").len()  > 0) CmpTerns.Push(cd(field).LT(Ds(p)));
     if (op.at("<>").len() > 0) CmpTerns.Push(cd(field).nEQ(Ds(p)));
    } else
    {
     if (p.at("*").len() == 0) EqTerns.Push(p.text()); else LkTerns.Push(cd(field).LK(Ds(p.text().replace("*", "%"))));
    }
   }
   String l = ""; for (String p : EqTerns) l += Ds(p) + ", "; if (l.length() > 0) l = l.substring(0, l.length() - 2);
   if (l.length() > 0) res.Push(cd(field).IN(l));
   res.Add(CmpTerns);
   res.Add(LkTerns);
   return OR(res);
  }
  
  public DbCnd Lot(String field, Chain lines, String delim) throws Exception
  {
   Pile<String> res = new Pile<String>();
   while (lines.len() > 0) { res.Push(lines.before(1, delim).text()); lines = lines.after(1, delim).Trim(); }
   return Lot(field, Db.cloneStringArray(res));
  }

  public DbCnd                  lot (String field, Pile<String>   parm) throws Exception { return lot(field, Db.cloneStringArray(parm));}
  public DbCnd                  loT (String field, Pile<String>   parm) throws Exception { return loT(field, Db.cloneStringArray(parm));}
  public DbCnd                  Lot (String field, Pile<String>   parm) throws Exception { return Lot(field, Db.cloneStringArray(parm));}


  /// <summary> cd ("Condition") generates a new DbCnd ("Condition")</summary>
  public DbCnd                  cd  (String                    operand) throws Exception { return new DbCnd(new Chain(operand)); }
  public DbCnd                  OR  (DbCnd...                      cnd) throws Exception { if (cnd.length == 0) return cd(""); DbCnd ret = cnd[0]; for(int i = 1; i < cnd.length; i++) ret = ret.OR(cnd[i]) ; return ret; }
  public DbCnd                  OR  (Pile<DbCnd>                   cnd) throws Exception { return OR(Db.cloneCndArray(cnd));  }
  public DbCnd                  AND (DbCnd...                      cnd) throws Exception
  { 
   if (cnd.length == 0) return cd(""); DbCnd ret = cnd[0]; for(int i = 1; i < cnd.length; i++) ret = ret.AND(cnd[i]); return ret; }
  public DbCnd                  AND (Pile<DbCnd>                   cnd              ) throws Exception { return AND(Db.cloneCndArray(cnd)); }


  public static String ds(Object... values)
  {
   String ret = "";
   for (Object o : values) if (o instanceof String) ret += ", " + (String)o; else if (o instanceof Double) ret += ", " + ("" + o).replace(",", "."); else ret += ", " + o;
   return (ret.length() == 0) ? ret : ret.substring(2);
  }

  public boolean avoidEmptyStrings = false;
  
  public String                 fromDb(String x                       ) throws Exception { return ((avoidEmptyStrings) && (x.length() == 1) && (x.charAt(0) == ' ')) ? "" : x.replace("``", "'");}
  
  public DbField                dF  (String                    operand) throws Exception { return new DbField(new Chain(operand)); }

  public String                 ds  (String                          x) throws Exception { return utl.ds(avoidEmptyStrings, x); }
  public DbField                dsF (String                          x) throws Exception { return new DbField(ds(x)); }
  public String                 ds  (Timestamp                       d) throws Exception { return utl.ds(avoidEmptyStrings, utl.stdDateTimeStamp(d, false)); }
  public DbField                dsF (Timestamp                       d) throws Exception { return new DbField(ds(d)); }
  public String                 dS  (String                          x) throws Exception { return utl.dS(avoidEmptyStrings, x); }
  public DbField                dSF (String                          x) throws Exception { return new DbField(dS(x)); }
  public String                 Ds  (String                          x) throws Exception { return utl.Ds(avoidEmptyStrings, x); }
  public DbField                DsF (String                          x) throws Exception { return new DbField(Ds(x)); }
  public String                 dst (String                          x) throws Exception { return utl.ds(avoidEmptyStrings, x.trim()); }
  public DbField                dstF(String                          x) throws Exception { return new DbField(dst(x)); }
  public String                 dSt (String                          x) throws Exception { return utl.dS(avoidEmptyStrings, x.trim()); }
  public DbField                dStF(String                          x) throws Exception { return new DbField(dSt(x)); }
  public String                 Dst (String                          x) throws Exception { return utl.Ds(avoidEmptyStrings, x.trim()); }
  public DbField                DstF(String                          x) throws Exception { return new DbField(Dst(x)); }
  public String                 ds  (Chain                           x) throws Exception { return ds (x.text());   }
  public DbField                dsF (Chain                           x) throws Exception { return new DbField(ds(x)); }
  public String                 dS  (Chain                           x) throws Exception { return dS (x.text());   }
  public DbField                dSF (Chain                           x) throws Exception { return new DbField(dS(x)); }
  public String                 Ds  (Chain                           x) throws Exception { return Ds (x.text());   }
  public DbField                DsF (Chain                           x) throws Exception { return new DbField(Ds(x)); }
  public String                 dst (Chain                           x) throws Exception { return dst(x.text());   }
  public DbField                dstF(Chain                           x) throws Exception { return new DbField(dst(x)); }
  public String                 dSt (Chain                           x) throws Exception { return dSt(x.text());   }
  public DbField                dStF(Chain                           x) throws Exception { return new DbField(dSt(x)); }
  public String                 Dst (Chain                           x) throws Exception { return Dst(x.text());   }
  public DbField                DstF(Chain                           x) throws Exception { return new DbField(Dst(x)); }

  public DbJoin                 Join (String tables,       DbCnd... joinConditions) throws Exception { return new DbJoin(this, tables, joinConditions); }
  public DbJoin                 Join (String                                tables) throws Exception { return new DbJoin(this, tables); }
  public DbJoin                 Join (Chain                                    smb) throws Exception { return new DbJoin(smb); }

  public DbCnd CndOR  (DbCnd... cnd) throws Exception { if (cnd.length == 0) return cd(""); return cnd[0].OR  (Db.cloneCndArray(new DbCndBlock(cnd).from(2))); }
  public DbCnd CndAND (DbCnd... cnd) throws Exception { if (cnd.length == 0) return cd(""); return cnd[0].AND (Db.cloneCndArray(new DbCndBlock(cnd).from(2))); }
  public DbCnd CndMNS (DbCnd... cnd) throws Exception { if (cnd.length == 0) return cd(""); return cnd[0].MNS (Db.cloneCndArray(new DbCndBlock(cnd).from(2))); }

  public DbSlc SlcOR  (DbSlc... sel) throws Exception { if (sel.length == 0) return Db.this.Join("").SLC(); return sel[0].OR  (Db.cloneSlcArray(new DbSlcBlock(sel).from(2))); }
  public DbSlc SlcAND (DbSlc... sel) throws Exception { if (sel.length == 0) return Db.this.Join("").SLC(); return sel[0].AND (Db.cloneSlcArray(new DbSlcBlock(sel).from(2))); }
  public DbSlc SlcMNS (DbSlc... sel) throws Exception { if (sel.length == 0) return Db.this.Join("").SLC(); return sel[0].MNS (Db.cloneSlcArray(new DbSlcBlock(sel).from(2))); }


  public DbMsDataAdapter dataAdapter(String sql) throws Exception
  {
   DbMsCommand cmd = newDbMsConnection().CreateCommand();
   cmd.CommandText(sql);
   DbMsDataAdapter ret = new DbMsDataAdapter(cmd);
   new DbMsCommandBuilder(ret);
   return ret;
  }

  public void clearTransaction() throws Exception
  {
   transactions.Clear();
   if (transaction != null) try {transaction.Rollback();} catch (Exception ex) {};
   transaction = null;
  }

  public void push(DbSlc select) throws Exception { if (transaction == null) transaction = reusedDbMsConnection.BeginTransaction(); transactions.Push(select); }
  public void push(DbUpd update) throws Exception { if (transaction == null) transaction = reusedDbMsConnection.BeginTransaction(); transactions.Push(update); }
  public void push(DbDel delete) throws Exception { if (transaction == null) transaction = reusedDbMsConnection.BeginTransaction(); transactions.Push(delete); }


  public void push(DbIns ins) throws Exception // This also implements BulkInsert whenever applicable
  { 
   if (transaction == null) transaction = reusedDbMsConnection.BeginTransaction();
   if (transactions.Len() > 0)
   {
    if (transactions.g(-1) instanceof DbIns)
    {
     boolean mergeable = false; 
     DbIns master = (DbIns)transactions.g(-1);
     if ((master.Fields().Len() == ins.Fields().Len()) && (master.into.equals(ins.into)) && (master.slc != null) && (ins.slc != null)) { mergeable = true; for (int i = 1; i <= master.Fields().Len(); i++) if (!ins.Fields().g(i).sql(this).equals(master.Fields().g(i).sql(this))) mergeable = false; }
     if (mergeable) 
     { 
      master.slc = master.slc.OR(ins.slc); 
      return; 
     }
    }
   }
   transactions.Push(ins); 
  }

  public void dbwDelForgottenConcepts() throws Exception
  {
   exec(Tables().g("cpt").sR
   (
    cd("id").GT(1),
    cd("id").nIN(Tables().g("itmsyn").sC("wkr").SLC()),
    cd("id").nIN(Tables().g("itmsyn").sC("sgr").SLC()),
    cd("id").nIN(Tables().g("kndrln").sC("ccls").SLC()),
    cd("id").nIN(Tables().g("kndrln").sC("pcls").SLC()),
    cd("id").nIN(Tables().g("prdrln").sC("sbj").SLC()),
    cd("id").nIN(Tables().g("prdrln").sC("obj").SLC()),
     cd("id").nIN(Tables().g("prdrul").sC("sbjcls").SLC()),
    cd("id").nIN(Tables().g("prdrul").sC("objcls").SLC()),
    cd("id").nIN(Tables().g("itmrln").sC("id").SLC()),
    cd("id").nIN(Tables().g("atrrln").sC("itm").SLC()),
    cd("id").nIN(Tables().g("atrrln").sC("atrb").SLC()),
    cd("id").nIN(Tables().g("atrrul").sC("itmcls").SLC()),
    cd("id").nIN(Tables().g("atrrul").sC("atrb").SLC())
   ).DEL());
   exec(Tables().g("cpt").sC("id, nm, idnm").INS(0, ds(""), dS("")));
   exec(Tables().g("cpt").sC("id, nm, idnm").INS(1, ds("Name"), dS("Name")));
   exec(Tables().g("cpt").sC("id, nm, idnm").INS(100000, ds("<void>"), dS("<void>")));
  }

  public void dbwShrinkDb()
  {
   //TBD
   /*
   dbwDelForgottenConcepts();
   try 
   {
    new OdbcCommand("DBCC SHRINKFILE(" + database + "_Log, 10)",  reusedDbMsConnection()).ExecuteNonQuery();
    new OdbcCommand("DBCC SHRINKFILE(" + database + "_Data, 10)", reusedDbMsConnection()).ExecuteNonQuery();
   }
   catch {}
   */
  }

  public Pile<DatSet> exec() throws Exception
  {
   Pile<DatSet> ret = new Pile<DatSet>();
   try 
   {
    for (Object o : transactions)
    {
     if (o instanceof DbSlc)
     {
      DatSet res = new DatSet();
      DbMsCommand cmd = Db._newDbMsCommand(this, ((DbSlc)o).sql(this), reusedDbMsConnection);
      cmd.Transaction(transaction);
      DbMsDataReader rdr = cmd.ExecuteReader();
      long i = 1; while (i > 0) try { res.typ.Add(r(rdr.GetDataTypeName((int)i))); res.nam.Add(r(rdr.GetName((int)i))); i++; } catch (Exception ex) { i = 0; }
      while (rdr.Read()) { ObjPile raw = new ObjPile(); i = 1; for (int fld = 1; fld <= res.typ.Len(); fld++) raw.Push(fromReader(rdr, res, fld, false)); res.Raws.Add(raw); }
      rdr.Dispose();
      rdr.Close();
      ret.Push(res);
     }
     if (o instanceof DbDel) { DbMsCommand cmd = Db._newDbMsCommand(this, ((DbDel)o).sql(this), reusedDbMsConnection); cmd.Transaction(transaction); cmd.ExecuteNonQuery(); }
     if (o instanceof DbIns) { DbMsCommand cmd = Db._newDbMsCommand(this, ((DbIns)o).sql(this), reusedDbMsConnection); cmd.Transaction(transaction); cmd.ExecuteNonQuery(); }
     if (o instanceof DbUpd) { DbMsCommand cmd = Db._newDbMsCommand(this, ((DbUpd)o).sql(this), reusedDbMsConnection); cmd.Transaction(transaction); cmd.ExecuteNonQuery(); }
    }
    transaction.Commit();
    clearTransaction();
   }
   catch(Exception ex)
   {
    try
    {
     transaction.Rollback();
     clearTransaction();
    }
    catch (Exception e) {}
    throw ex;
   }
   return ret;
  }

  /*
  public void showStatements(DbSlcBlock dbs) throws Exception
  {
   frmSelectView f = new frmSelectView(this, dbs);
   try
   {
    f.setModal(true);
    f.setVisible(true);
    if (f != f) throw new CancelledByUser();
   }
   catch (CancelledByUser ex) {}
  }
  */

  //public DatSet exec(DbJoin g) { return exec(new DbSelect(g)); }

  private Object fromReader(DbMsDataReader rdr, DatSet dat, int i, boolean asReach) throws Exception
  {
   String typ = dat.typ.g(i).text().toUpperCase();
   if (typ.equals("LONG"))                          if (asReach) return  new Chain(""  + rdr.GetInt64((int)i))                     ; else return rdr.GetInt64((int)i);
   if (typ.equals("INT"))                           if (asReach) return  new Chain(""  + rdr.GetInt64((int)i))                     ; else return rdr.GetInt64((int)i);
   if (typ.equals("INTEGER"))                       if (asReach) return  new Chain(""  + rdr.GetInt64((int)i))                     ; else return rdr.GetInt64((int)i);
   if (typ.equals("DECIMAL"))                       if (asReach) return  new Chain(""  + rdr.GetInt64((int)i))                     ; else return rdr.GetInt64((int)i);
   if (typ.equals("INT4"))                          if (asReach) return  new Chain(""  + rdr.GetInt64((int)i))                     ; else return rdr.GetInt64((int)i);
   if (typ.equals("_INT4"))                         if (asReach) return  new Chain(""  + rdr.GetString((int)i))                    ; else return rdr.GetString((int)i);
   if (typ.equals("INT8"))                          if (asReach) return  new Chain(""  + rdr.GetInt64((int)i))                     ; else return rdr.GetInt64((int)i);
   if (typ.equals("BIGINT"))                        if (asReach) return  new Chain(""  + rdr.GetInt64((int)i))                     ; else return rdr.GetInt64((int)i);
   if (typ.equals("REAL"))                          if (asReach) return  new Chain(("" + rdr.GetDouble((int)i)).replace(",", ".")) ; else return rdr.GetDouble((int)i);
   if (typ.equals("FLOAT8"))                        if (asReach) return  new Chain(("" + rdr.GetDouble((int)i)).replace(",", ".")) ; else return rdr.GetDouble((int)i);
   if (typ.equals("DOUBLE PRECISION"))              if (asReach) return  new Chain(("" + rdr.GetDouble((int)i)).replace(",", ".")) ; else return rdr.GetDouble((int)i);
   if (typ.equals("FLOAT"))                         if (asReach) return  new Chain(("" + rdr.GetDouble((int)i)).replace(",", ".")) ; else return rdr.GetDouble((int)i);
   if (typ.equals("BIT"))                           if (asReach) return  new Chain(fromDb(rdr.GetString((int)i)))                  ; else return fromDb(rdr.GetString((int)i));
   if (typ.equals("VARCHAR"))                       if (asReach) return  new Chain(fromDb(rdr.GetString((int)i)))                  ; else return fromDb(rdr.GetString((int)i));
   if (typ.equals("VARCHAR2"))                      if (asReach) return  new Chain(fromDb(rdr.GetString((int)i)))                  ; else return fromDb(rdr.GetString((int)i));
   if (typ.equals("NCHAR"))                         if (asReach) return  new Chain(fromDb(rdr.GetString((int)i)))                  ; else return fromDb(rdr.GetString((int)i));
   if (typ.equals("NVARCHAR"))                      if (asReach) return  new Chain(fromDb(rdr.GetString((int)i)))                  ; else return fromDb(rdr.GetString((int)i));
   if (typ.equals("TEXT"))                          if (asReach) return  new Chain(fromDb(rdr.GetString((int)i)))                  ; else return fromDb(rdr.GetString((int)i));
   if (typ.equals("SERIAL"))                        if (asReach) return  new Chain(fromDb(rdr.GetString((int)i)))                  ; else return fromDb(rdr.GetString((int)i));
   if (typ.equals("TIMESTAMP WITHOUT TIME ZONE"))   if (asReach) return  new Chain(fromDb(rdr.GetString((int)i)))                  ; else return fromDb(rdr.GetString((int)i));
   if (typ.equals("DATETIME"))                      if (asReach) return  new Chain(fromDb(rdr.GetString((int)i)))                  ; else return fromDb(rdr.GetString((int)i));
   utl.say("unknown DB-DataType detected: " +  typ);
   return null;
  }

  /*
  public DatSet execSlc(String smt) throws Exception
  {
   DatSet ret = new DatSet();
   DbMsConnection cnn = newDbMsConnection();
   try
   {
    DbMsDataReader rdr = new DbMsCommand(smt, newDbMsConnection()).ExecuteReader();
    long i = 1; while (i > 0) try { ret.typ.Add(r(rdr.GetDataTypeName((int)i - 1))); ret.nam.Add(r(rdr.GetName((int)i - 1))); i++; } catch (Exception ex) { i = 0; }
    while (rdr.Read()) { ObjPack raw = new ObjPack(); i = 1; for (int fld = 1; fld <= ret.typ.Len(); fld++) raw.Push(fromReader(rdr, ret, fld, false)); ret.Raws.Push(raw); }
    rdr.Dispose(); rdr.Close(); // cnn.Dispose(); cnn.Close();
   }
   catch (Exception ex) { setConnectionState(false, cnn); throw ex; }
   setConnectionState(false, cnn);
   return ret;
  }
  */
    
  public DatSet all(DbMsDataReader rdr) throws Exception
  {
   DatSet ret = new DatSet();
   long i = 1; while (i > 0) 
   try 
   { 
    ret.typ.Add(new Chain(rdr.GetDataTypeName((int)i))); 
    ret.nam.Add(new Chain(rdr.GetName((int)i))); i++; 
   } 
   catch (Exception ex) { i = 0; }
   while (rdr.Read()) 
   { 
    ObjPile raw = new ObjPile(); 
    i = 1; 
    for (int fld = 1; fld <= ret.typ.Len(); fld++) raw.Push(fromReader(rdr, ret, fld, false)); 
    ret.Raws.Push(raw); 
   }
   rdr.Dispose(); rdr.Close(); // cnn.Dispose(); cnn.Close();
   return ret;
  }

  public DbMsDataReader exec(DbSlc s) throws Exception
  {
   DbMsConnection cnn = newDbMsConnection();
   if ((s.type.length() == 0) && (s.From().Len() == 0)) return null;
   try { return new DbMsCommand(this, s.sql(this), newDbMsConnection()).ExecuteReader(); } catch (Exception ex) { setConnectionState(false, cnn); throw ex; }
  }
  
  /*
  public DatSet exec(DbSlc s) throws Exception
  {
   DatSet ret = new DatSet();
   if ((s.type.length() == 0) && (s.From().Len() == 0)) return ret;
   DbMsConnection cnn = newDbMsConnection();
   try
   {
    DbMsDataReader rdr = new DbMsCommand(s.sql(this), newDbMsConnection()).ExecuteReader();
    long i = 1; while (i > 0) 
     try 
     { 
      ret.typ.Add(r(rdr.GetDataTypeName((int)i - 1))); 
      ret.nam.Add(r(rdr.GetName((int)i - 1))); i++; 
     } 
     catch (Exception ex) { i = 0; }
    while (rdr.Read()) 
    { 
     ObjPack raw = new ObjPack(); 
     i = 1; 
     for (int fld = 1; fld <= ret.typ.Len(); fld++) raw.Push(fromReader(rdr, ret, fld, false)); ret.Raws.Push(raw); 
    }
    rdr.Dispose(); rdr.Close(); // cnn.Dispose(); cnn.Close();
   }
   catch (Exception ex) { setConnectionState(false, cnn); throw ex; }
   setConnectionState(false, cnn);
   return ret;
  }
  */
  

  public DbCnd naturalCnd(DbCndBlock include, DbCndBlock restrict, DbCndBlock exclude) throws Exception  // the natural way of logic set-building: INCLUDE some (fulfilling some patterns) and RESTRICT them to a subset and EXCLUDE from this subset others (fulfilling other criteria)
  {
   return CndOR(cloneCndArray(include)).AND(cloneCndArray(restrict)).MNS(cloneCndArray(exclude));
  }

  public DbSlc naturalSlc(DbSlcBlock include, DbSlcBlock restrict, DbSlcBlock exclude) throws Exception  // the natural way of logic set-building: INCLUDE some (fulfilling some patterns) and RESTRICT them to a subset and EXCLUDE from this subset others (fulfilling other criteria)
  {
   return SlcOR(cloneSlcArray(include)).AND(cloneSlcArray(restrict)).MNS(cloneSlcArray(exclude));
  }
  
  public Chain sql(String[][] list, String alias, int arrayLvl) throws Exception
  {
   if ((list.length == 0) && (list[0].length == 0)) throw new Exception("empty String arrays cannot be converted into sql statements");
   Chain ret = Chain.Empty;
   for (String[] line : list) 
   {
    String l = "SELECT "; if (arrayLvl > 0) l += " ARRAY[";
    for (String s : line) l += "'" + toDB(s, s.length()) + "'::text, ";
    l = l.substring(0, l.length() - 8);
    if (arrayLvl > 0) l += "]";
    ret = ret.plus("\nUNION ALL " + l);
   }
   return alias.length() > 0 ? new Chain("(").plus(ret.from(12)).plus(") AS " + alias) : ret.from(12);
  }

  public Chain sql(String[] list, String delim, String alias, int arrayLvl) throws Exception
  {
   if ((list.length == 0) && (list[0].trim().length() == 0)) throw new Exception("empty String arrays cannot be converted into sql statements");
   Pile<Pile<String>> lines = new Pile<Pile<String>>();
   for (String line : list) 
   {
    lines.Push(new Pile<String>());
    StringTokenizer st = new StringTokenizer(line.trim(), delim);
    while (st.hasMoreElements()) lines.g(-1).Push(((String) st.nextElement()).trim());
   }
   String[][] lins = new String[lines.Len()][lines.g(-1).Len()];
   for (int i = 1; i <= lines.Len(); i++) for (int j = 1; j <= lines.g(-1).Len(); j++) lins[i-1][j-1] = lines.g(i).g(j);
   return(sql(lins, alias, arrayLvl));
  }
  
  public void bulkInsert(String table) throws Exception
  {
   exec("COPY " + table + " FROM '" + tmpDatFile(table).getAbsolutePath() + "';");
  }

  public DbMsDataReader exec(String sql) throws Exception
  {
   return new DbMsCommand(this, sql, reusedDbMsConnection).ExecuteReader();
  }

  public DbMsDataReader exec(Chain sql) throws Exception
  {
   return new DbMsCommand(this, sql.text(), reusedDbMsConnection).ExecuteReader();
  }

  public long execIns (String   smt) throws Exception { clearTransaction();                                                                   return Db._newDbMsCommand (this, smt         , reusedDbMsConnection).ExecuteNonQuery(); }
  public long exec    (DbIns      i) throws Exception 
  { 
   clearTransaction(); 
   String sql = i.sql(this).trim(); 
   if (sql.indexOf("NaN") > -1)
    sql = sql;
   if (sql.length() == 0) return 0; return Db._newDbMsCommand (this, sql         , reusedDbMsConnection).ExecuteNonQuery(); 
  }
  public long execUpd (String   smt) throws Exception { clearTransaction();                                                                   return Db._newDbMsCommand (this, smt         , reusedDbMsConnection).ExecuteNonQuery(); }
  
  public long exec    (DbUpd      u) throws Exception
  {
   clearTransaction();
   String s = u.sql(this);
   return Db._newDbMsCommand (this, u.sql(this) , reusedDbMsConnection).ExecuteNonQuery();
  }
  public long execDel (String   smt) throws Exception { clearTransaction();                                                                   return Db._newDbMsCommand (this, smt         , reusedDbMsConnection).ExecuteNonQuery(); }
  public long exec    (DbDel      d) throws Exception  { clearTransaction();                                                                  return Db._newDbMsCommand (this, d.sql(this), reusedDbMsConnection ).ExecuteNonQuery(); }

  private KeyPile<String, String> masterTable  = new KeyPile<String, String>();
  private KeyPile<String, String> masterView   = new KeyPile<String, String>();
  private KeyPile<String, String> tnField      = new KeyPile<String, String>();
  private KeyPile<String, String> vnField      = new KeyPile<String, String>();
  private KeyPile<String, DbCnd>    schemaCnd    = new KeyPile<String, DbCnd>();
  private KeyPile<String, DbCnd>    tableCnd     = new KeyPile<String, DbCnd>();
  private KeyPile<String, DbCnd>    viewCnd      = new KeyPile<String, DbCnd>();

  private void init() throws Exception
  {
   if (!selfTested) selfTest();

   dtv.Set(" >$ " , "UPPER(````<°(´¹´, ²)´````");
   dtv.Set(" <|> ", "UNION``¹``>(´¹´²)´``) ° (²``");
   dtv.Set(" <&> ", "INTERSECT``¹``>(´¹)´ ° ²``(²)``");
   dtv.Set(" <-> ", "EXCEPT``¹``>(´¹)´ ° ²``(²)``");

   if (url == null) { url = new DbUrl("", Drivers()); return; }

   if (Dbms().endsWith("h2s")) { } //H2Sql
   if (Dbms().endsWith("mys")) { }
   if (Dbms().endsWith("pgs")) { dtv.Set(" & ", "CONCAT``¹``>°´(¹²´)``, ²"); dtv.Set(" >$ ", "CAST````<°(´¹´, ² AS TEXT)´``"); }
   if (Dbms().endsWith("fbd")) { }
   if (Dbms().endsWith("sql")) { }
   if (Dbms().endsWith("mss")) { dtv.Set(" & ", "+``¹``>¹´ ° ²``²``"); }
   if (Dbms().endsWith("syb")) { dtv.Set(" & ", "+``¹``>¹´ ° ²``²``"); }
   if (Dbms().endsWith("ora")) { dtv.Set(" & ", "||``¹``>¹´ ° ²``²``"); }
   if (Dbms().endsWith("db2")) { }
   if (Dbms().endsWith("dby")) { }
   if (Dbms().endsWith("hsq")) { }
   if (Dbms().endsWith("tda")) { }

   if (Dbms().endsWith("h2s")) { } //H2Sql
   if (Dbms().endsWith("mys")) { }
   if (Dbms().endsWith("pgs")) { }
   if (Dbms().endsWith("fbd")) { }
   if (Dbms().endsWith("sql")) { }
   if (Dbms().endsWith("mss")) { convFormats.Add("YYYY", "LEFT(CONVERT(VARCHAR(23), %FIELD%, 121), 4)"); convFormats.Add("YY",   "LEFT(RIGHT(CONVERT(VARCHAR(23), %FIELD%, 121), 21), 2)"); convFormats.Add("MM",   "LEFT(RIGHT(CONVERT(VARCHAR(23), %FIELD%, 121), 18), 2)"); convFormats.Add("DD",   "LEFT(RIGHT(CONVERT(VARCHAR(23), %FIELD%, 121), 15), 2)"); convFormats.Add("hh",   "LEFT(RIGHT(CONVERT(VARCHAR(23), %FIELD%, 121), 12), 2)"); convFormats.Add("mm",   "LEFT(RIGHT(CONVERT(VARCHAR(23), %FIELD%, 121), 9), 2)"); convFormats.Add("ss",   "LEFT(RIGHT(CONVERT(VARCHAR(23), %FIELD%, 121), 6), 2)"); convFormats.Add("mmm",  "RIGHT(CONVERT(VARCHAR(23), %FIELD%, 121), 3)"); }
   if (Dbms().endsWith("syb")) { }
   if (Dbms().endsWith("ora")) { }
   if (Dbms().endsWith("db2")) { }
   if (Dbms().endsWith("dby")) { }
   if (Dbms().endsWith("hsq")) { }
   if (Dbms().endsWith("tda")) { }

   DbMsDataReader    rdr = null;

   /*
   if (Dbms().endsWith("h2s")) { rdr = Db._newDbMsCommand("SELECT DISTINCT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE ((TABLE_TYPE = 'TABLE') OR (TABLE_TYPE = 'VIEW'))"                                    , reusedDbMsConnection).ExecuteReader(); } //H2Sql
   if (Dbms().endsWith("mys")) { rdr = Db._newDbMsCommand("Select Distinct table_name From INFORMATION_SCHEMA.TABLES Where (table_schema = '" + Database() + "')"                                                , reusedDbMsConnection).ExecuteReader(); reusedDbMsConnection.ChangeDatabase(Database()); }
   if (Dbms().endsWith("pgs")) { rdr = Db._newDbMsCommand("Select Distinct table_name, table_schema from information_schema.tables where table_schema NOT IN ('pg_catalog', 'information_schema')"               , reusedDbMsConnection).ExecuteReader(); }
   if (Dbms().endsWith("fbd")) { rdr = Db._newDbMsCommand("SELECT DISTINCT * FROM (SELECT DISTINCT rdb$relation_name FROM rdb$relations WHERE ((rdb$view_blr IS null) AND ((rdb$system_flag IS null) or (rdb$system_flag = 0))) UNION SELECT DISTINCT rdb$relation_name FROM rdb$relations WHERE ((rdb$view_blr IS NOT null) AND ((rdb$system_flag IS null) OR (rdb$system_flag = 0)))) ORDER BY 1", reusedDbMsConnection).ExecuteReader(); }
   if (Dbms().endsWith("sql")) { rdr = Db._newDbMsCommand("SELECT DISTINCT name FROM sqlite_master WHERE ((type = 'table') OR (type = 'view')) ORDER BY 1", reusedDbMsConnection).ExecuteReader(); }
   if (Dbms().endsWith("mss")) { rdr = Db._newDbMsCommand("Select Distinct table_schema + '.' + table_name From information_schema.tables Where (table_catalog = '" + Database() + "' AND table_schema = 'dbo')"   , reusedDbMsConnection).ExecuteReader(); }
   //if (dbms.endsWith("syb")) { rdr = Db._newDbMsCommand("SELECT DISTINCT name FROM sysobjects WHERE ((type='U') OR (type='V'))", reusedDbMsConnection).ExecuteReader();}
   if (Dbms().endsWith("syb")) { rdr = Db._newDbMsCommand("SELECT DISTINCT name FROM sysobjects WHERE ((type='U') OR (type='V')) ORDER BY name) ", reusedDbMsConnection).ExecuteReader();}
   //if (dbms.endsWith("ora")) { rdr = Db._newDbMsCommand("Select Distinct table_name From all_tables Where (owner = '" + database.toUpperCase() + "')"                                                          , reusedDbMsConnection).ExecuteReader(); }
   if (Dbms().endsWith("ora")) { rdr = Db._newDbMsCommand ( "Select Distinct * from (Select Distinct table_name From all_tables Where (owner = '" + User().toUpperCase() + "') UNION Select Distinct view_name From all_views Where (owner = '" + User().toUpperCase() + "')) ORDER BY 1" , reusedDbMsConnection).ExecuteReader(); }
   if (Dbms().endsWith("db2")) { rdr = Db._newDbMsCommand("SELECT DISTINCT name FROM sysibm.systables WHERE (owner = 'SCHEMA') AND ((type = 'T') OR (type = 'V')) ORDER BY 1", reusedDbMsConnection).ExecuteReader(); }
   if (Dbms().endsWith("dby")) { rdr = Db._newDbMsCommand("SELECT DISTINCT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE ((TABLETYPE = 'T') OR (TABLETYPE = 'V'))", reusedDbMsConnection).ExecuteReader(); } //HSqlDb
   if (Dbms().endsWith("hsq")) { rdr = Db._newDbMsCommand("SELECT DISTINCT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE ((TABLE_TYPE = 'TABLE') OR (TABLE_TYPE = 'VIEW'))", reusedDbMsConnection).ExecuteReader(); } //HSqlDb
   if (Dbms().endsWith("tda")) { rdr = Db._newDbMsCommand("Select Distinct table_name From INFORMATION_SCHEMA.TABLES Where (table_schema = '" + Database() + "')"                                                , reusedDbMsConnection).ExecuteReader(); reusedDbMsConnection.ChangeDatabase(Database()); }
   */

   masterTable.Add("h2s", "information_schema.tables");
   masterTable.Add("mys", "information_schema.tables");
   masterTable.Add("pgs", "information_schema.tables");
   masterTable.Add("fbd", "rdb$relations");
   masterTable.Add("sql", "sqlite_master");
   masterTable.Add("mss", "information_schema.tables");
   masterTable.Add("syb", "sysobjects");
   masterTable.Add("ora", ".all_tables");
   masterTable.Add("db2", "sysibm.systables");
   masterTable.Add("dby", "information_schema.tables");
   masterTable.Add("hsq", "information_schema.tables");
   masterTable.Add("tda", "information_schema.tables");

   masterView.Add("h2s", "information_schema.tables");
   masterView.Add("mys", "information_schema.tables");
   //masterView.Add("pgs", "information_schema.views");
   masterView.Add("pgs", "information_schema.tables");
   masterView.Add("fbd", "rdb$relations");
   masterView.Add("sql", "sqlite_master");
   masterView.Add("mss", "information_schema.views");
   masterView.Add("syb", "sysobjects");
   masterView.Add("ora", ".all_views");
   masterView.Add("db2", "sysibm.systables");
   masterView.Add("dby", "information_schema.tables");
   masterView.Add("hsq", "information_schema.tables");
   masterView.Add("tda", "information_schema.tables");

   tnField.Add("h2s", "table_name");
   tnField.Add("mys", "table_name");
   //tnField.Add("pgs", "table_schema + '.' + table_name");
   tnField.Add("pgs", "table_name");
   tnField.Add("fbd", "rdb$relation_name");
   tnField.Add("sql", "name");
   //tnField.Add("mss", "table_schema + '.' + table_name");
   tnField.Add("mss", "table_name");
   tnField.Add("syb", "name");
   tnField.Add("ora", "table_name");
   tnField.Add("db2", "name");
   tnField.Add("dby", "table_name");
   tnField.Add("hsq", "table_name");
   tnField.Add("tda", "table_name");

   vnField.Add("h2s", "table_name");
   vnField.Add("mys", "table_name");
   //vnField.Add("pgs", "table_schema + '.' + table_name");
   vnField.Add("pgs", "table_name");
   vnField.Add("fbd", "rdb$relation_name");
   vnField.Add("sql", "name");
   //vnField.Add("mss", "table_schema + '.' + table_name");
   vnField.Add("mss", "table_name");
   vnField.Add("syb", "name");
   vnField.Add("ora", "view_name");
   vnField.Add("db2", "name");
   vnField.Add("dby", "table_name");
   vnField.Add("hsq", "table_name");
   vnField.Add("tda", "table_name");

   schemaCnd.Add("h2s", cd(""));
   schemaCnd.Add("mys", cd("table_schema").EQ(ds(utl.cutr(Database(), "."))));
   //schemaCnd.Add("pgs", cd("table_schema").nIN("'pg_catalog', 'information_schema'"));
   schemaCnd.Add("pgs", cd("table_schema").IN("'public'"));
   schemaCnd.Add("fbd", OR(cd("rdb$system_flag").IS("NULL"), cd("rdb$system_flag").EQ(0)));
   schemaCnd.Add("sql", cd(""));
   schemaCnd.Add("mss", AND(cd("table_catalog").EQ(ds(utl.cutr(Database(), "."))), cd("table_schema").EQ(ds("dbo"))));
   schemaCnd.Add("syb", cd(""));
   schemaCnd.Add("ora", cd("owner").EQ(ds(User().toUpperCase())));
   schemaCnd.Add("db2", cd("owner").EQ(ds("SCHEMA")));
   schemaCnd.Add("dby", cd(""));
   schemaCnd.Add("hsq", cd(""));
   schemaCnd.Add("tda", cd("table_schema").EQ(ds(utl.cutr(Database(), "."))));

   tableCnd.Add("h2s", cd("TABLE_TYPE").EQ(ds("TABLE")));
   tableCnd.Add("mys", cd("TABLE_TYPE").EQ(ds("BASE TABLE")));
   tableCnd.Add("pgs", cd("TABLE_TYPE").LK(ds("%TABLE")));
   tableCnd.Add("fbd", cd("rdb$view_blr").IS("NULL"));
   tableCnd.Add("sql", cd("TYPE").EQ(ds("table")));
   tableCnd.Add("mss", cd(""));
   tableCnd.Add("syb", cd("TYPE").EQ(ds("U")));
   tableCnd.Add("ora", cd(""));
   tableCnd.Add("db2", cd("TYPE").EQ(ds("T")));
   tableCnd.Add("dby", cd("TABLE_TYPE").EQ(ds("T")));
   tableCnd.Add("hsq", cd("TABLE_TYPE").EQ(ds("TABLE")));
   tableCnd.Add("tda", cd("TABLE_TYPE").EQ(ds("BASE TABLE")));

   viewCnd.Add("h2s", cd("TABLE_TYPE").EQ(ds("VIEW")));
   viewCnd.Add("mys", cd("TABLE_TYPE").EQ(ds("VIEW")));
   viewCnd.Add("pgs", cd("TABLE_TYPE").EQ(ds("VIEW")));
   viewCnd.Add("fbd", cd("rdb$view_blr").nIS("NULL"));
   viewCnd.Add("sql", cd("TYPE").EQ(ds("view")));
   viewCnd.Add("mss", cd(""));
   viewCnd.Add("syb", cd("TYPE").EQ(ds("V")));
   viewCnd.Add("ora", cd(""));
   viewCnd.Add("db2", cd("TYPE").EQ(ds("V")));
   viewCnd.Add("dby", cd("TABLE_TYPE").EQ(ds("V")));
   viewCnd.Add("hsq", cd("TABLE_TYPE").EQ(ds("VIEW")));
   viewCnd.Add("tda", cd("TABLE_TYPE").EQ(ds("VIEW")));

   DbCndBlock all     = new DbCndBlock();
   DbCndBlock ic      = new DbCndBlock();
   DbCndBlock rs      = new DbCndBlock();
   DbCndBlock xc      = new DbCndBlock();
   DbCndBlock current = ic;

   DbJoin tg = Db.this.Join(masterTable.g(Dbms())).sC(tnField.g(Dbms())).sR(schemaCnd.g(Dbms())).sR(tableCnd.g(Dbms()));
   all       = new DbCndBlock();
   ic        = new DbCndBlock();
   rs        = new DbCndBlock();
   xc        = new DbCndBlock();
   current   = ic;

   String[] tf = new String[] {TFilter()};
   while (tf[0].length() > 0)
   {
    String s = utl.cutl(tf, ",");
    if (s.startsWith("&"))
     current = rs;
    else
     if (s.startsWith("\\")) current = xc;
     else
     {
      if (current != ic)
      {
       all.Add(naturalCnd(ic, rs, xc));
       ic = new DbCndBlock();
       rs = new DbCndBlock();
       xc = new DbCndBlock();
      }
      if (!s.startsWith("+")) s = "+" + s;
      current = ic;
     }
    current.Add(cd(tnField.g(Dbms())).LK(ds(s.substring(1).replace("*", "%"))));
   }

   String text = CndOR(cloneCndArray(ic)).sql().text();

   all.Add(naturalCnd(ic, rs, xc));
   tg = tg.sR(CndOR(cloneCndArray(all)));

   DbJoin vg = Db.this.Join(masterView.g(Dbms())).sC(vnField.g(Dbms())).sR(schemaCnd.g(Dbms())).sR(viewCnd.g(Dbms()));
   all       = new DbCndBlock();
   ic        = new DbCndBlock();
   rs        = new DbCndBlock();
   xc        = new DbCndBlock();
   current   = ic;

   String[] vf = new String[] {VFilter()};
   while (vf[0].length() > 0)
   {
    String s = utl.cutl(vf, ",");
    if (s.startsWith("&")) current = rs; else if (s.startsWith("\\")) current = xc; else { if (current != ic) { all.Add(naturalCnd(ic, rs, xc)); ic = new DbCndBlock(); rs = new DbCndBlock(); xc = new DbCndBlock(); } if (!s.startsWith("+")) s = "+" + s; current = ic; }
    current.Add(cd(tnField.g(Dbms())).LK(ds(s.substring(1).replace("*", "%"))));
   }
   all.Add(naturalCnd(ic, rs, xc));
   vg = vg.sR(CndOR(cloneCndArray(all)));

   String sqlStatement = tg.SLD().OR(vg.SLD()).sql(this);   //String sqlStatement = "SELECT DISTINCT * FROM (" + tg.SLD().ORD("1").OR(vg.SLD().ORD("1")).sql(this) + ") ORDER BY 1";
   rdr = Db._newDbMsCommand(this, sqlStatement + " ORDER BY 1", reusedDbMsConnection).ExecuteReader();


   while (rdr.Read()) 
   {
    String tableName = fromDb(rdr.GetString(1)).trim().toLowerCase();
    if (Dbms().endsWith("mss")) if (tableName.startsWith("dbo.")) tableName = tableName.substring(4);
    if (Dbms().endsWith("syb")) if (tableName.startsWith("dbo.")) tableName = tableName.substring(4);
    tables.Add(tableName, new DbJoin(this, tableName));
   }
   //utl.say(utl.l2s(tableNames,"\r\n"));
   rdr.Dispose();
   rdr.Close();

   String loadErrors = "";
   for (String table : tables.Keys())
   {
    try
    {
     if (Dbms().endsWith("h2s")) { rdr = Db._newDbMsCommand(this, "Select * From "       + dbTable(table) + " Limit 0"             , reusedDbMsConnection).ExecuteReader(); }
     if (Dbms().endsWith("mys")) { rdr = Db._newDbMsCommand(this, "Select * From "       + dbTable(table) + " Limit 0, 0"          , reusedDbMsConnection).ExecuteReader(); }
     if (Dbms().endsWith("pgs")) { rdr = Db._newDbMsCommand(this, "Select * From "       + dbTable(table) + " Limit 0"             , reusedDbMsConnection).ExecuteReader(); }
     if (Dbms().endsWith("fbd")) { }
     if (Dbms().endsWith("sql")) { }
     if (Dbms().endsWith("mss")) { rdr = Db._newDbMsCommand(this, "Select Top 0 * From " + dbTable(table)                          , reusedDbMsConnection).ExecuteReader(); }
     if (Dbms().endsWith("syb")) { rdr = Db._newDbMsCommand(this, "Select Top 0 * From " + dbTable(table)                          , reusedDbMsConnection).ExecuteReader(); }
     if (Dbms().endsWith("ora")) { rdr = Db._newDbMsCommand(this, "Select * From "       + dbTable(table) + " Where (rownum <= 1)" , reusedDbMsConnection).ExecuteReader(); }
     if (Dbms().endsWith("db2")) { }
     if (Dbms().endsWith("dby")) { }
     if (Dbms().endsWith("hsq")) { }
     if (Dbms().endsWith("tda")) { }
     long i = 1;
     while (i > 0) 
      try
      {
       tables.g(table).Fields().Add(new DbField(false, rdr.GetName((int)i).toLowerCase(), rdr.GetDataTypeName((int)i).toLowerCase())); i++;
      }
      catch (Exception ex)
      {
       i = 0;
      }
     rdr.Dispose();
     rdr.Close();
    }
    catch (Exception ex)
    {
     loadErrors += "\r\n" + table;
     reusedDbMsConnection = newDbMsConnection();
    }
   }
   lastUrl = new DbUrl(url);
   if (loadErrors.length() > 0) utl.say("Incompletely loaded Table(s) / View(s):" + loadErrors);
   //lastDbTec = Dbtec(); lastDbms = Dbms(); lastHost = host; lastInstance = instance; lastDatabase = database; lastSchema = schema; lastUser = user; lastPassword = password;
  }

  public String ConnectString(boolean hidePassword) { return url.ConnectString(hidePassword); }

  private void addDbMru(DbUrl url) throws Exception
  {
   ctx cx = new ctx();
   String connect = url.ConnectString(true);
   KeyPile<String, String> mruList = new KeyPile<String, String>();
   for (long i = 1; i < 10; i++)
   {
    String val = "";
    try { Registry.createKey(Registry.HKEY_CURRENT_USER, "SOFTWARE\\" + cx.Name() + "\\DataBases"); val = Registry.readString(Registry.HKEY_CURRENT_USER, "SOFTWARE\\" + cx.Name() + "\\DataBases", "MRU" + i); } catch (Exception ex) { val = ""; }
    if (val == null) val = "";
    if (val.length() > 0) if (!val.trim().toLowerCase().equals(connect.trim().toLowerCase())) mruList.Add("MRU" + i, val);
   }
   mruList.Add("MRU" + 0, connect.trim());
   long j = 0;
   for (String key : mruList.kAsc())
   {
    j++;
    if (j > 9) break;
    try { Registry.writeStringValue(Registry.HKEY_CURRENT_USER, "SOFTWARE\\" + cx.Name() + "\\DataBases", "MRU" + j, mruList.g(key));  }   catch (Exception ex)  { }
   }
  }  
  
  private void init(String connectstring) throws Exception
  {
   if (!selfTested) selfTest();
   if (connectstring.length() == 0) {init(); return;}
   url = new DbUrl(connectstring, Drivers());
   if (Dbms().endsWith("ora")) avoidEmptyStrings = true;
   reusedDbMsConnection = newDbMsConnection();
   init();
   addDbMru(url);
  }

  /*
  public String buildSelectFields(String... fields) throws Exception
  {
   String ret = "";
   String sql = "";
   for (String f : fields)
   {
    if (f.startsWith("[["))
    {
     Chain format = new Chain(utl.cutl(f.substring(2), "]]"));
     Pack<Reach> tokens = new Pack<Reach>();
     Chain field = new Chain(f).after("]]");
     while (format.len() > 0)
     {
      Chain toks = format.before(1, "YYYY", "YY", "MM", "DD", "hh", "mm", "ss", "mmm");
      if (toks.len() > 0) tokens.Push(new Chain(ds(toks)));
      toks = format.at(1, "YYYY", "YY", "MM", "DD", "hh", "mm", "ss", "mmm");
      if (toks.len() == 0) break;
      tokens.Push(toks);
      format = format.after(toks);
     }
     sql = "";
     for (Chain tok : tokens) if (tok.startsWith("'")) sql += tok.text() + " + "; else sql += convFormats.g(tok.text()) + " + ";
     if (sql.length() > 0) sql = sql.substring(0, sql.length() - 3);
     sql = sql.replace("%FIELD%", field.text());
    }
    else sql = f;
    ret += sql + ", ";
   }
   return ret.substring(0, ret.length() - 2);
  }
  */

  public String buildSelectFields(DbField... fields) throws Exception
  {
   String ret = "";
   String sql = "";
   for (DbField dbf : fields)
   {
    String f = dbf.sql(this).text();
    if (f.startsWith("[["))
    {
     Chain format = new Chain(utl.cutl(f.substring(2), "]]"));
     Pile<Chain> tokens = new Pile<Chain>();
     Chain field = new Chain(f).after("]]");
     while (format.len() > 0)
     {
      Chain tok = format.before(1, true, "YYYY", "YY", "MM", "DD", "hh", "mm", "ss", "mmm");
      if (tok.len() > 0) tokens.Push(new Chain(ds(tok)));
      tok = format.at(1, true, "YYYY", "YY", "MM", "DD", "hh", "mm", "ss", "mmm");
      if (tok.len() == 0) break;
      tokens.Push(tok);
      format = format.after(tok);
     }
     sql = "";
     for (Chain tok : tokens) if (tok.startsWith("'")) sql += tok.text() + " + "; else sql += convFormats.g(tok.text()) + " + ";
     if (sql.length() > 0) sql = sql.substring(0, sql.length() - 3);
     sql = sql.replace("%FIELD%", field.text());
    }
    else sql = f;
    ret += sql + ", ";
   }
   return ret.substring(0, ret.length() - 2);
  }



  public String dbTable(String table) throws Exception
  {
   if (Dbms().endsWith("h2s")) { return (table.indexOf(".") > -1) ? (table.indexOf(".") == 0) ? table.substring(1) : table : "PUBLIC."                  + table   ; }
   if (Dbms().endsWith("mys")) { return (table.indexOf(".") > -1) ? (table.indexOf(".") == 0) ? table.substring(1) : table : Database() + "."           + table   ; }
   if (Dbms().endsWith("pgs")) { return (table.indexOf(".") > -1) ? (table.indexOf(".") == 0) ? table.substring(1) : table :                              table   ; }
   if (Dbms().endsWith("fbd")) { return (table.indexOf(".") > -1) ? (table.indexOf(".") == 0) ? table.substring(1) : table : Database() + "."           + table   ; }
   if (Dbms().endsWith("sql")) { return (table.indexOf(".") > -1) ? (table.indexOf(".") == 0) ? table.substring(1) : table : Database() + "."           + table   ; }
   if (Dbms().endsWith("mss")) { return (table.indexOf(".") > -1) ? (table.indexOf(".") == 0) ? table.substring(1) : table : "dbo."                     + table   ; }
   if (Dbms().endsWith("ora")) { return (table.indexOf(".") > -1) ? (table.indexOf(".") == 0) ? table.substring(1) : table : User().toUpperCase() + "." + table   ; }
   if (Dbms().endsWith("db2")) { return (table.indexOf(".") > -1) ? (table.indexOf(".") == 0) ? table.substring(1) : table : Database() + "."           + table   ; }
   if (Dbms().endsWith("dby")) { return (table.indexOf(".") > -1) ? (table.indexOf(".") == 0) ? table.substring(1) : table : Database() + "."           + table   ; }
   if (Dbms().endsWith("hsq")) { return (table.indexOf(".") > -1) ? (table.indexOf(".") == 0) ? table.substring(1) : table : Database() + "."           + table   ; }
   if (Dbms().endsWith("tda")) { return (table.indexOf(".") > -1) ? (table.indexOf(".") == 0) ? table.substring(1) : table : "PUBLIC."                  + table   ; }
   return table;
  }
  
  private Pile<DbMsConnection> freeConnections = new Pile<DbMsConnection>();
  private Pile<DbMsConnection> usedConnections = new Pile<DbMsConnection>();

  public void setConnectionState(boolean used, DbMsConnection cnn) throws Exception
  {
   if (used) for(int i = 1; i <= freeConnections.Len(); i++) if (cnn == freeConnections.g(i)) { freeConnections.Del(i); usedConnections.Push(cnn); return;}
   for (int i = 1; i <= usedConnections.Len(); i++) if (cnn == usedConnections.g(i)) { usedConnections.Del(i); freeConnections.Push(cnn); return; }
  }
  
  public DbMsConnection newDbMsConnection() throws Exception  //A new DatabaseDriverConnection (odbc or jdbc resp.)
  {
   DbMsConnection ret = null;
   if (freeConnections.Len() > 0) { ret = freeConnections.g(1); setConnectionState(true, ret); return ret; }
   String DSN                 = Host().startsWith("[")  ? Host().substring(1, Host().length() - 3) : "";
   String Driver              = DSN.trim().length() > 0 ? "" : url == null ? "" : url.Driver();
   String Server              = DSN.trim().length() > 0 ? "" : Host().trim().length()    == 0 ? "" : Host().trim();
   ret = Db._newDbMsConnection(this, Dbtec(), Dbms(), DSN, Driver, Server, Instance().trim(), Port(), Database().trim(), Schema().trim(), User().trim(), Password().trim(), 0, 0, "3", "16834", false, 0, 200);  //odbc:MyS seems to want this Option "16834", no idea what it is good for
   ret.ConnectionTimeout(300);
   ret.Open();
   usedConnections.Add(ret);
   return ret;
  }
  
  public String copyTo(Db target, String tableName)
  {
   String ret = "";
   String copied = "successfully copied (";
   String failed = "failed to copy: (";
   for (DbJoin tbl : Tables()) try { if ((tableName.trim().equals("*")) || (tbl.Name().toUpperCase().trim().equals(tableName.toUpperCase().trim()))) tbl.copyTo(target); copied += tbl.Name() + ", "; } catch (Exception ex) { failed += tbl.Name() + ", "; }
   if (copied.length() > 21) ret += copied.substring(0, copied.length() - 2) + ") \r\n";
   if (failed.length() > 17) ret += failed.substring(0, failed.length() - 2) + ")";
   if (ret.endsWith("\r\n")) return ret.substring(0, ret.length() - 2); else return ret;
  }
  
 }








 


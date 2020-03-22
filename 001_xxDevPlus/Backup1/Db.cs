
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Text;
using System.Windows.Forms;
using Microsoft.Win32;

using ndBase;
using ndString;

// AttGetr ToDo
// Die stets geöffnete Datenbankverbindung m_dbc sollte öfter genützt werden und nicht immer eine neue dbc() verwendet werden.
// Allerdings müsste dann ein Mechanismus gefunden werden´, der m_dbc wiederverwendet solbald das über m_dbc aufgerufene DbMsCommand seine Artbeit erledigt hat.
// Idealerweise sollten Commands ohne DB-Result über m_dbc abgewickelt werden.
namespace ndData
{
 public class Db : EvalExpert   //DocGetr: dbaccess is short for KnowledgeBaseDataBase, convention: class names in lower case indicate static classes
 {
  //Copyright© 1996-2012 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties, You may use, modify, distribute this code under GNU public licence for NON-Commercial use as long as this notice remains unchanged.
  private static bool selfTested  = false;  private static string ass(bool expr) { if (!expr) throw new Exception(ass(true) + " SelfTest Failure"); return "Db"; } private static void selfTest() { selfTested = true; }


  private KeyPile<string, string[]> drivers = new KeyPile<string, string[]>();
  public  KeyPile<string, string[]> Drivers { get { return drivers; } set { drivers = value; } }

  private Exception ActionResult = null;

  private void btnOk_Click(object sender, EventArgs e)
  {
   try
   {
    frmDbConnect f = ((frmDbConnect)((Control)sender).Parent);
    try { init(f.Url.ConnectString(false)); }
    catch (Exception ex) { utl.say("Error connecting to Database\r\n" + ex.Message + "\r\n" + ex.StackTrace); }
    if (reusedDbMsConnection != null) f.Close();
    //if (reusedDbMsConnection == null) throw new Exception("Database connection failed!");
   }
   catch (Exception ex) { ActionResult  = ex; }
  }

  private void btnCancel_Click(object sender, EventArgs e)
  {
   try
   {
    ((frmDbConnect)((Control)sender).Parent).Close();
    throw new CancelledByUser();
   }
   catch (Exception ex) { ActionResult  = ex; }
  }

  private void btnTest_Click(object sender, EventArgs e)
  {
   try
   {
    Db dba = null;
    string Dbms = utl.cutl(((frmDbConnect)((Control)sender).Parent).pupDbms.Text.Trim(), " ");
    string DbTec = ((frmDbConnect)((Control)sender).Parent).pupDbTec.Text.Trim();
    try { dba = new Db(DbTec + ":" + Dbms + "::" + ((frmDbConnect)((Control)sender).Parent).txtInstance.Text.Trim() + "," + ((frmDbConnect)((Control)sender).Parent).txtDatabase.Text.Trim() + "," + ((frmDbConnect)((Control)sender).Parent).txtUser.Text.Trim() + "," + ((frmDbConnect)((Control)sender).Parent).txtPassword.Text.Trim(), Drivers); }
    catch (Exception ex) { utl.say("Error connecting to Database\r\n" + ex.Message + "\r\n" + ex.StackTrace); }
    if (dba != null) utl.say("Successfully connected!");
   }
   catch (Exception ex) { ActionResult  = ex; }
  }

  public Db(KeyPile<string, string[]> dbDrivers) { this.Drivers = dbDrivers; init(""); }
  public Db(string connectstring, KeyPile<string, string[]> dbDrivers) { this.Drivers = dbDrivers; init(connectstring); }
  public Db(System.Windows.Forms.IWin32Window owner, KeyPile<string, string[]> dbDrivers)
  {
   ActionResult = null;
   this.Drivers = dbDrivers;
   frmDbConnect f = new frmDbConnect(this);
   f.btnCancel.Click += btnCancel_Click;
   f.btnOk.Click += btnOk_Click;
   f.btnTest.Click += btnTest_Click;
   if (url == null) url = new DbUrl("", Drivers);
   if (lastUrl == null) lastUrl = url;
   f.Url = lastUrl;
   //f.ConnectString = lastDbTec + ":" + lastDbms + "::" + lastHost + "\\" + lastInstance + "," + lastDatabase + "," + lastUser + "," + lastPassword;
   /*
   for (int i = 1; i <= f.pupDbTec.Items.Count; i++) if (((string)(f.pupDbTec.Items[i - 1])).StartsWith(lastDbTec + " ")) f.pupDbms.SelectedIndex = i - 1;
   for (int i = 1; i <= f.pupDbms.Items.Count; i++) if (((string)(f.pupDbms.Items[i - 1])).StartsWith(lastDbms + " ")) f.pupDbms.SelectedIndex = i - 1;
   f.txtInstance.Text   = lastHost + "\\" + lastInstance;
   f.txtDatabase.Text   = lastDatabase;
   f.txtUser.Text       = lastUser;
   f.txtPassword.Text   = lastPassword;
   */
   try
   {
    f.ShowDialog(owner);
    if (ActionResult != null) throw ActionResult;
   }
   catch (CancelledByUser ex) { throw ex; }
  }

  private KeyPile<string, DbGrid> tables       = new KeyPile<string, DbGrid>();
  private KeyPile<string, string> convFormats  = new KeyPile<string, string>();
  private KeyPile<string, string> dtv          = new KeyPile<string, string>();                //the directive an EvalExpert supplies for the Term's or Expression's val() function
  public  KeyPile<string, string> Dtv          { get { return dtv ; } set { dtv  = value; } }  //the directive an EvalExpert supplies for the Term's or Expression's val() function
  
  //private string                DUP_ROW = "System.Data.Odbc.OdbcException: ERROR [23000]"; //this is valid only for Odbc drivers {SQL Server} and {SQL Native Client} !! for all others adaption must take place in inti() method !

  public static DbMsConnection  _newDbMsConnection  (Db db, string dbtec, string dbms, string dsn, string drv, string svr, string inst, long port, string dbName, string sm, string usr, string pwd, long cnnliftm, long cmdliftm, string ptcl, string optn, bool pool, long minpool, long maxpool) { return new DbMsConnection(db, dbtec, dbms, dsn, drv, svr, inst, port, dbName, sm, usr, pwd, cnnliftm, cmdliftm, ptcl, optn, pool, minpool, maxpool); }
  public static DbMsCommand     _newDbMsCommand     (string cmd, DbMsConnection conn) { return new DbMsCommand(cmd, conn); }

  private DbMsConnection        reusedDbMsConnection;      //The DatabaseDriverConnection (odbc or jdbc resp.)

  private DbUrl                 url               = null;
  private DbUrl                 lastUrl           = null;
  //public  string              dbtec             = "";
  //public  string              dbms              = "";
  //private string              host              = "";
  //private long                port              =  0; 
  //private string              instance          = "";
  //private string              database          = "";
  //private string              schema            = "";
  //private string              user              = "";
  //private string              password          = "";
  //private string              tfilter           = "";
  //private string              vfilter           = "";

  public DbUrl                  Url               { get { return url; } }

  public  string                NullStgy          = ""; // "+" = autoAdd (f <> NULL) conditions to every select query, "-" = ignore selected rows with null values, "0" = convert selected NULL to empty string and to integer 0 resp, "" - do nothing, selected null will cause Exception, this is DEFAULT because WELL DESIGNED DATABASE MODELS SHOULD NOT ALLOW NULLS AT ALL!

  //public  string                driver            = "";
  //public KeyPile<string, string[]> dbDrivers = new KeyPile<string, string[]>();

  public KeyPile<string, DbGrid> Tables { get { return tables; } }

  //private static string lastDbTec       = "";
  //private static string lastDbms        = "";
  //private static string lastHost        = "";
  //private static string lastInstance    = "";
  //private static string lastDatabase    = "";
  //private static string lastSchema      = "";
  //private static string lastUser        = "";
  //private static string lastPassword    = "";

  public string  Dbtec     { get { return url.dbtec     ;}}
  public string  Dbms      { get { return url.dbms      ;}}
  public string  Host      { get { return url.host      ;}}
  public long    Port      { get { return url.port      ;}}
  public string  Instance  { get{ return  url.instance  ;}}
  public string  Database  { get{ return  url.database  ;}}
  public string  Schema    { get{ return  url.schema    ;}}
  public string  User      { get{ return  url.user      ;}}
  public string  Password  { get{ return  url.password  ;}}
  public string  TFilter   { get { return url.tfilter   ;}}
  public string  VFilter   { get { return url.vfilter   ;}}
  public string  Name      { get{ return  url.Name()    ;}}

  private DbMsTransaction  transaction = null;
  private Pile<object>     transactions = new Pile<object>();

  public Reach r(string s) { return new Reach(s); }

  public string val(object obj)
  {
   Type typ = obj.GetType();
   if (typ == typeof(DbSlc)) return ((DbSlc)obj).sql(this);
   throw new Exception("Err: invalid Type in Db.val()");
  }

  public DbCnd lot(string field, params string[] param)
  {
   Pile<DbCnd>      res         = new Pile<DbCnd>();
   Pile<DbCnd>      LkTerns     = new Pile<DbCnd>();
   Pile<DbCnd>      CmpTerns    = new Pile<DbCnd>();
   Pile<string>   EqTerns     = new Pile<string>();
   foreach (string pp in param)
   {
    Reach p = pp.Trim();
    if ((p.StartsWith("(>")) || (p.StartsWith("(<")))
    {
     Reach op = p.upto(1, ")");
     p = p.after(op).Trim();
     if (op.at("(>=)").len > 0) CmpTerns.Push(cd(field).GE(ds(p)));
     if (op.at("(<=)").len > 0) CmpTerns.Push(cd(field).LE(ds(p)));
     if (op.at("(>)").len  > 0) CmpTerns.Push(cd(field).GT(ds(p)));
     if (op.at("(<)").len  > 0) CmpTerns.Push(cd(field).LT(ds(p)));
     if (op.at("(<>)").len > 0) CmpTerns.Push(cd(field).nEQ(ds(p)));
    } else
    {
     if (p.at("*").len == 0) EqTerns.Push(p); else LkTerns.Push(cd(field).LK(ds(p.text.Replace("*", "%"))));
    }
   }
   string l = ""; foreach (string p in EqTerns) l += ds(p) + ", "; if (l.Length > 0) l = l.Substring(0, l.Length - 2);
   if (l.Length > 0) res.Push(cd(field).IN(l));
   res.Add(CmpTerns);
   res.Add(LkTerns);
   return OR(res);
  }

  public DbCnd lot(string field, Reach lines, string delim)
  {
   Pile<string> res = new Pile<string>();
   while (lines.len > 0) { res.Push(lines.before(1, delim)); lines = lines.after(1, delim).Trim(); }
   return lot(field, res.array());
  }

  public DbCnd loT(string field, params string[] param)
  {
   Pile<DbCnd>      res         = new Pile<DbCnd>();
   Pile<DbCnd>      LkTerns     = new Pile<DbCnd>();
   Pile<DbCnd>      CmpTerns    = new Pile<DbCnd>();
   Pile<string>   EqTerns     = new Pile<string>();
   foreach (string pp in param)
   {
    Reach p = pp.Trim();
    if ((p.StartsWith(">")) || (p.StartsWith("<")))
    {
     Reach op = p.before(1, " ");
     p = p.after(op).Trim();
     if (op.at(">=").len > 0) CmpTerns.Push(cd(field).GE(dS(p)));
     if (op.at("<=").len > 0) CmpTerns.Push(cd(field).LE(dS(p)));
     if (op.at(">").len  > 0) CmpTerns.Push(cd(field).GT(dS(p)));
     if (op.at("<").len  > 0) CmpTerns.Push(cd(field).LT(dS(p)));
     if (op.at("<>").len > 0) CmpTerns.Push(cd(field).nEQ(dS(p)));
    } else
    {
     if (p.at("*").len == 0) EqTerns.Push(p); else LkTerns.Push(cd(field).LK(dS(p.text.Replace("*", "%"))));
    }
   }
   string l = ""; foreach (string p in EqTerns) l += dS(p) + ", "; if (l.Length > 0) l = l.Substring(0, l.Length - 2);
   if (l.Length > 0) res.Push(cd(field).IN(l));
   res.Add(CmpTerns);
   res.Add(LkTerns);
   return OR(res);
  }
  
  public DbCnd loT(string field, Reach lines, string delim)
  {
   Pile<string> res = new Pile<string>();
   while (lines.len > 0) { res.Push(lines.before(1, delim)); lines = lines.after(1, delim).Trim(); }
   return loT(field, res.array());
  }

  
  public DbCnd Lot(string field, params string[] param)
  {
   Pile<DbCnd>      res         = new Pile<DbCnd>();
   Pile<DbCnd>      LkTerns     = new Pile<DbCnd>();
   Pile<DbCnd>      CmpTerns    = new Pile<DbCnd>();
   Pile<string>   EqTerns     = new Pile<string>();
   foreach (string pp in param)
   {
    Reach p = pp.Trim();
    if ((p.StartsWith(">")) || (p.StartsWith("<")))
    {
     Reach op = p.before(1, " ");
     p = p.after(op).Trim();
     if (op.at(">=").len > 0) CmpTerns.Push(cd(field).GE(Ds(p)));
     if (op.at("<=").len > 0) CmpTerns.Push(cd(field).LE(Ds(p)));
     if (op.at(">").len  > 0) CmpTerns.Push(cd(field).GT(Ds(p)));
     if (op.at("<").len  > 0) CmpTerns.Push(cd(field).LT(Ds(p)));
     if (op.at("<>").len > 0) CmpTerns.Push(cd(field).nEQ(Ds(p)));
    } else
    {
     if (p.at("*").len == 0) EqTerns.Push(p); else LkTerns.Push(cd(field).LK(Ds(p.text.Replace("*", "%"))));
    }
   }
   string l = ""; foreach (string p in EqTerns) l += Ds(p) + ", "; if (l.Length > 0) l = l.Substring(0, l.Length - 2);
   if (l.Length > 0) res.Push(cd(field).IN(l));
   res.Add(CmpTerns);
   res.Add(LkTerns);
   return OR(res);
  }
  
  public DbCnd Lot(string field, Reach lines, string delim)
  {
   Pile<string> res = new Pile<string>();
   while (lines.len > 0) { res.Push(lines.before(1, delim)); lines = lines.after(1, delim).Trim(); }
   return Lot(field, res.array());
  }

  public DbCnd                lot(string field, Pile<string>    parm) { return lot(field, parm.array());}
  public DbCnd                loT(string field, Pile<string>    parm) { return loT(field, parm.array());}
  public DbCnd                Lot(string field, Pile<string>    parm) { return Lot(field, parm.array());}

  /// <summary> cd ("Condition") generates a new Cnd ("Condition")</summary>
  public DbCnd                cd  (string                    operand) { return new DbCnd((Reach)operand); }
  public DbCnd                OR  (params DbCnd[]  cnd              ) { if (cnd.Length == 0) return cd(""); DbCnd ret = cnd[0]; for(int i = 1; i < cnd.Length; i++) ret = ret.OR(cnd[i]) ; return ret; }
  public DbCnd                OR  (Pile<DbCnd>     cnd              ) { return OR(cnd.array());  }
  public DbCnd                AND (params DbCnd[]  cnd              ) { if (cnd.Length == 0) return cd(""); DbCnd ret = cnd[0]; for(int i = 1; i < cnd.Length; i++) ret = ret.AND(cnd[i]); return ret; }
  public DbCnd                AND (Pile<DbCnd>     cnd              ) { return AND(cnd.array()); }

  public static string ds(params object[] values)
  {
   string ret = "";
   foreach (object o in values) if (o.GetType() == typeof(string)) ret += ", " + (string)o; else if (o.GetType() == typeof(double)) ret += ", " + ("" + o).Replace(",", "."); else ret += ", " + o;
   return (ret.Length == 0) ? ret : ret.Substring(2);
  }

  public bool avoidEmptyStrings = false;
  
  public string               fromDb(string                        x) { return ((avoidEmptyStrings) && (x.Length == 1) && (x[0] == ' ')) ? "" : x.Replace("``", "'");}
  
  public DbField              dF  (string                    operand) 
  { 
   return new DbField((Reach)operand); 
  }

  public string               ds  (string                          x) { return utl.ds(avoidEmptyStrings, x); }
  public DbField              dsF (string                          x) { return new DbField(ds(x)); }
  public string               ds  (DateTime                        d) { return utl.ds(avoidEmptyStrings, utl.stdDateTimeStamp(d, false)); }
  public DbField              dsF (DateTime                        d) { return new DbField(ds(d)); }
  public string               dS  (string                          x) { return utl.dS(avoidEmptyStrings, x); }
  public DbField              dSF (string                          x) { return new DbField(dS(x)); }
  public string               Ds  (string                          x) { return utl.Ds(avoidEmptyStrings, x); }
  public DbField              DsF (string                          x) { return new DbField(Ds(x)); }
  public string               dst (string                          x) { return utl.ds(avoidEmptyStrings, x.Trim()); }
  public DbField              dstF(string                          x) { return new DbField(dst(x)); }
  public string               dSt (string                          x) { return utl.dS(avoidEmptyStrings, x.Trim()); }
  public DbField              dStF(string                          x) { return new DbField(dSt(x)); }
  public string               Dst (string                          x) { return utl.Ds(avoidEmptyStrings, x.Trim()); }
  public DbField              DstF(string                          x) { return new DbField(Dst(x)); }

  public DbGrid               Grid (string tables, params DbCnd[] joinConditions) { return new DbGrid(this, tables, joinConditions); }
  public DbGrid               Grid (string                                tables) { return new DbGrid(this, tables); }
  public DbGrid               Grid (Reach                                    smb) { return new DbGrid(smb); }

  public DbCnd CndOR(params DbCnd[] cnd)   { if (cnd.Length == 0) return cd(""); return cnd[0].OR     (new DbCndBlock(cnd).from(2).array()); }
  public DbCnd CndAND(params DbCnd[] cnd)  { if (cnd.Length == 0) return cd(""); return cnd[0].AND    (new DbCndBlock(cnd).from(2).array()); }
  public DbCnd CndMNS (params DbCnd[] cnd) { if (cnd.Length == 0) return cd(""); return cnd[0].MNS    (new DbCndBlock(cnd).from(2).array()); }

  public DbSlc SlcOR  (params DbSlc[] sel) { if (sel.Length == 0) return Grid("").SLC; return sel[0].OR  (new DbSlcBlock(sel).from(2).array()); }
  public DbSlc SlcAND (params DbSlc[] sel) { if (sel.Length == 0) return Grid("").SLC; return sel[0].AND (new DbSlcBlock(sel).from(2).array()); }
  public DbSlc SlcMNS (params DbSlc[] sel) { if (sel.Length == 0) return Grid("").SLC; return sel[0].MNS (new DbSlcBlock(sel).from(2).array()); } 

  public DbMsDataAdapter dataAdapter(string sql)
  {
   DbMsCommand cmd = newDbMsConnection().CreateCommand();
   cmd.CommandText = sql;
   DbMsDataAdapter ret = new DbMsDataAdapter(cmd);
   new DbMsCommandBuilder(ret);
   return ret;
  }

  public void clearTransaction()
  {
   transactions.Clear();
   if (transaction != null) try {transaction.Rollback();} catch {};
   transaction = null;
  }

  //public void push(DbGrid     grid)  { if (transaction == null) transaction = reusedDbMsConnection.BeginTransaction(); transactions.Push(grid);   }
  public void push(DbSlc select) { if (transaction == null) transaction = reusedDbMsConnection.BeginTransaction(); transactions.Push(select); }
  public void push(DbUpd update) { if (transaction == null) transaction = reusedDbMsConnection.BeginTransaction(); transactions.Push(update); }
  public void push(DbDel delete) { if (transaction == null) transaction = reusedDbMsConnection.BeginTransaction(); transactions.Push(delete); }

  public void push(DbIns ins) // This also implements BulkInsert whenever applicable
  { 
   if (transaction == null) transaction = reusedDbMsConnection.BeginTransaction();
   if (transactions.Len > 0)
   {
    if (transactions[-1].GetType() == typeof(DbIns))
    {
     bool mergeable = false; 
     DbIns master = (DbIns)transactions[-1];
     if ((master.Fields.Len == ins.Fields.Len) && (master.into.Equals(ins.into)) && (master.slc != null) && (ins.slc != null)) { mergeable = true; for (int i = 1; i <= master.Fields.Len; i++) if (!ins.Fields[i].sql(this).Equals(master.Fields[i].sql(this))) mergeable = false; }
     if (mergeable) { master.slc = master.slc.OR(ins.slc); return; }
    }
   }
   transactions.Push(ins); 
  }

  public void dbwDelForgottenConcepts()
  {
   exec(Tables["cpt"].sR(
   cd("id").GT(1),
   cd("id").nIN(Tables["itmsyn"].sC("wkr").SLC),
   cd("id").nIN(Tables["itmsyn"].sC("sgr").SLC),
   cd("id").nIN(Tables["kndrln"].sC("ccls").SLC),
   cd("id").nIN(Tables["kndrln"].sC("pcls").SLC),
   cd("id").nIN(Tables["prdrln"].sC("sbj").SLC),
   cd("id").nIN(Tables["prdrln"].sC("obj").SLC),
   cd("id").nIN(Tables["prdrul"].sC("sbjcls").SLC),
   cd("id").nIN(Tables["prdrul"].sC("objcls").SLC),
   cd("id").nIN(Tables["itmrln"].sC("id").SLC),
   cd("id").nIN(Tables["atrrln"].sC("itm").SLC),
   cd("id").nIN(Tables["atrrln"].sC("atrb").SLC),
   cd("id").nIN(Tables["atrrul"].sC("itmcls").SLC),
   cd("id").nIN(Tables["atrrul"].sC("atrb").SLC)).DEL);
   exec(Tables["cpt"].sC("id, nm, idnm").INS(0, ds(""), dS("")));
   exec(Tables["cpt"].sC("id, nm, idnm").INS(1, ds("Name"), dS("Name")));
   exec(Tables["cpt"].sC("id, nm, idnm").INS(100000, ds("<void>"), dS("<void>")));
  }

  public void dbwShrinkDb()
  {
   dbwDelForgottenConcepts();
   try 
   {
    Db._newDbMsCommand("DBCC SHRINKFILE(" + Database + "_Log, 10)" , reusedDbMsConnection).ExecuteNonQuery();
    Db._newDbMsCommand("DBCC SHRINKFILE(" + Database + "_Data, 10)", reusedDbMsConnection).ExecuteNonQuery();
   }
   catch {}
  }

  public Pile<DatSet> exec()
  {
   Pile<DatSet> ret = new Pile<DatSet>();
   try 
   {
    foreach (Object o in transactions)
    {
     if (o.GetType() == typeof(DbSlc))
     {
      DatSet res = new DatSet();
      DbMsCommand cmd = Db._newDbMsCommand(((DbSlc)o).sql(this), reusedDbMsConnection);
      cmd.Transaction = transaction;
      DbMsDataReader rdr = cmd.ExecuteReader();
      long i = 1; while (i > 0) try { res.typ.Add(rdr.GetDataTypeName((int)i - 1)); res.nam.Add(rdr.GetName((int)i - 1)); i++; } catch (Exception ex) { i = 0; }
      while (rdr.Read()) { ObjPile raw = new ObjPile(); i = 1; for (int fld = 1; fld <= res.typ.Len; fld++) raw.Push(fromReader(rdr, res, fld, false)); res.Raws.Add(raw); }
      rdr.Dispose();
      rdr.Close();
      ret.Push(res);
     }
     if (o.GetType() == typeof(DbDel)) { DbMsCommand cmd = Db._newDbMsCommand(((DbDel)o).sql(this), reusedDbMsConnection); cmd.Transaction = transaction; cmd.ExecuteNonQuery(); }
     if (o.GetType() == typeof(DbIns)) { DbMsCommand cmd = Db._newDbMsCommand(((DbIns)o).sql(this), reusedDbMsConnection); cmd.Transaction = transaction; cmd.ExecuteNonQuery(); }
     if (o.GetType() == typeof(DbUpd)) { DbMsCommand cmd = Db._newDbMsCommand(((DbUpd)o).sql(this), reusedDbMsConnection); cmd.Transaction = transaction; cmd.ExecuteNonQuery(); }
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
    catch {}
    throw ex;
   }
   return ret;
  }


  //public DatSet exec(DbGrid g) { return exec(new DbSelect(g)); }

  private object fromReader(DbMsDataReader rdr, DatSet dat, int i, bool asReach)
  {
   string typ = dat.typ[i].text.ToUpper();
   if (typ.Equals("LONG"))                          if (asReach) return  new Reach("" + rdr.GetInt64((int)i - 1))                      ; else return rdr.GetInt64((int)i - 1); 
   if (typ.Equals("INT"))                           if (asReach) return  new Reach("" + rdr.GetInt64((int)i - 1))                      ; else return rdr.GetInt64((int)i - 1);
   if (typ.Equals("INTEGER")) if (asReach) return new Reach("" + rdr.GetInt64((int)i - 1)); else return rdr.GetInt64((int)i - 1); 
   if (typ.Equals("DECIMAL"))                       if (asReach) return  new Reach("" + rdr.GetInt64((int)i - 1))                      ; else return rdr.GetInt64((int)i - 1); 
   if (typ.Equals("INT4"))                          if (asReach) return  new Reach("" + rdr.GetInt64((int)i - 1))                      ; else return rdr.GetInt64((int)i - 1); 
   if (typ.Equals("INT8"))                          if (asReach) return  new Reach("" + rdr.GetInt64((int)i - 1))                      ; else return rdr.GetInt64((int)i - 1); 
   if (typ.Equals("BIGINT"))                        if (asReach) return  new Reach("" + rdr.GetInt64((int)i - 1))                      ; else return rdr.GetInt64((int)i - 1); 
   if (typ.Equals("REAL"))                          if (asReach) return  new Reach(("" + rdr.GetDouble((int)i - 1)).Replace(",", ".")) ; else return rdr.GetDouble((int)i - 1); 
   if (typ.Equals("FLOAT8"))                        if (asReach) return  new Reach(("" + rdr.GetDouble((int)i - 1)).Replace(",", ".")) ; else return rdr.GetDouble((int)i - 1);
   if (typ.Equals("DOUBLE PRECISION"))              if (asReach) return  new Reach(("" + rdr.GetDouble((int)i - 1)).Replace(",", ".")) ; else return rdr.GetDouble((int)i - 1); 
   if (typ.Equals("FLOAT"))                         if (asReach) return  new Reach(("" + rdr.GetDouble((int)i - 1)).Replace(",", ".")) ; else return rdr.GetDouble((int)i - 1); 
   if (typ.Equals("BIT"))                           if (asReach) return  new Reach(fromDb(rdr.GetString((int)i - 1)))                  ; else return fromDb(rdr.GetString((int)i - 1)); 
   if (typ.Equals("VARCHAR"))                       if (asReach) return  new Reach(fromDb(rdr.GetString((int)i - 1)))                  ; else return fromDb(rdr.GetString((int)i - 1)); 
   if (typ.Equals("VARCHAR2"))                      if (asReach) return  new Reach(fromDb(rdr.GetString((int)i - 1)))                  ; else return fromDb(rdr.GetString((int)i - 1)); 
   if (typ.Equals("NCHAR"))                         if (asReach) return  new Reach(fromDb(rdr.GetString((int)i - 1)))                  ; else return fromDb(rdr.GetString((int)i - 1)); 
   if (typ.Equals("NVARCHAR"))                      if (asReach) return  new Reach(fromDb(rdr.GetString((int)i - 1)))                  ; else return fromDb(rdr.GetString((int)i - 1)); 
   if (typ.Equals("TEXT"))                          if (asReach) return  new Reach(fromDb(rdr.GetString((int)i - 1)))                  ; else return fromDb(rdr.GetString((int)i - 1)); 
   if (typ.Equals("TIMESTAMP WITHOUT TIME ZONE"))   if (asReach) return  new Reach(fromDb(rdr.GetString((int)i - 1)))                  ; else return fromDb(rdr.GetString((int)i - 1));
   if (typ.Equals("DATETIME"))                      if (asReach) return new Reach(fromDb(rdr.GetString((int)i - 1))); else return fromDb(rdr.GetString((int)i - 1));
   utl.say("unknown DB-DataType detected: " + typ);
   return null;
  }

  public DatSet execSlc(string smt)
  {
   DatSet ret = new DatSet();
   DbMsConnection cnn = newDbMsConnection();
   try
   {
    DbMsDataReader rdr = Db._newDbMsCommand(smt, cnn).ExecuteReader();
    long i = 1; while (i > 0) try { ret.typ.Add(rdr.GetDataTypeName((int)i - 1)); ret.nam.Add(rdr.GetName((int)i - 1)); i++; } catch (Exception ex) { i = 0; }
    while (rdr.Read()) { ObjPile raw = new ObjPile(); i = 1; for (int fld = 1; fld <= ret.typ.Len; fld++) raw.Push(fromReader(rdr, ret, fld, false)); ret.Raws.Push(raw); }
    rdr.Dispose(); rdr.Close(); //cnn.Dispose(); cnn.Close();
   }
   catch (Exception ex) { setConnectionState(false, cnn); throw ex; }
   setConnectionState(false, cnn);
   return ret;
  }
  
  public DbMsDataReader execReader(DbSlc s) 
  {
   DbMsConnection cnn = newDbMsConnection();
   try
   {
    if ((s.type.Length == 0) && (s.From.Len == 0)) return null;
    return new DbMsCommand(s.sql(this), newDbMsConnection()).ExecuteReader();
   }
   catch (Exception ex) { setConnectionState(false, cnn); throw ex; }
  }

  public DatSet exec(DbSlc s)
  {
   DatSet ret = new DatSet();
   if ((s.type.Length == 0) && (s.From.Len == 0)) return ret;
   DbMsConnection cnn = newDbMsConnection();
   try 
   {
    DbMsDataReader rdr = Db._newDbMsCommand(s.sql(this), cnn).ExecuteReader();
    long i = 1; while (i > 0) try { ret.typ.Add(rdr.GetDataTypeName((int)i - 1)); ret.nam.Add(rdr.GetName((int)i - 1)); i++; } catch (Exception ex) { i = 0; }
    while (rdr.Read()) { ObjPile raw = new ObjPile(); i = 1; for (int fld = 1; fld <= ret.typ.Len; fld++) raw.Push(fromReader(rdr, ret, fld, false)); ret.Raws.Push(raw); }
    rdr.Dispose(); rdr.Close(); // cnn.Dispose(); cnn.Close();
   } catch (Exception ex) { setConnectionState(false, cnn); throw ex; }
   setConnectionState(false, cnn);
   return ret;
  }

  public DbCnd naturalCnd(DbCndBlock include, DbCndBlock restrict, DbCndBlock exclude) // the natural way of logic set-building: INCLUDE some (fulfilling some patterns) and RESTRICT them to a subset and EXCLUDE from this subset others (fulfilling other criteria)
  {
   return CndOR(include.array()).AND(restrict.array()).MNS(exclude.array());
  }

  public DbSlc naturalSlc(DbSlcBlock select, DbSlcBlock restrict, DbSlcBlock exclude) // the natural way of logic set-building: INCLUDE some (fulfilling some patterns) and RESTRICT them to a subset and EXCLUDE from this subset others (fulfilling other criteria)
  {
   return SlcOR(select.array()).AND(restrict.array()).MNS(exclude.array());
  }

  public long execIns (string   smt) { clearTransaction();                                                                 return Db._newDbMsCommand (smt         , reusedDbMsConnection).ExecuteNonQuery(); }
  public long exec    (DbIns      i) { clearTransaction(); string sql = i.sql(this).Trim(); if (sql.Length == 0) return 0; return Db._newDbMsCommand (sql         , reusedDbMsConnection).ExecuteNonQuery(); }
  public long execUpd (string   smt) { clearTransaction();                                                                 return Db._newDbMsCommand (smt         , reusedDbMsConnection).ExecuteNonQuery(); }
  
  public long exec    (DbUpd      u) 
  { 
   clearTransaction();
   string s = u.sql(this);
   return Db._newDbMsCommand(u.sql(this), reusedDbMsConnection).ExecuteNonQuery(); 
  }

  public long execDel (string   smt) { clearTransaction();                                                                 return Db._newDbMsCommand (smt         , reusedDbMsConnection).ExecuteNonQuery(); }
  public long exec    (DbDel      d) { clearTransaction();                                                                 return Db._newDbMsCommand (d.sql(this) , reusedDbMsConnection).ExecuteNonQuery(); }

  private KeyPile<String, String> masterTable  = new KeyPile<String, String>();
  private KeyPile<String, String> masterView   = new KeyPile<String, String>();
  private KeyPile<String, String> tnField      = new KeyPile<String, String>();
  private KeyPile<String, String> vnField      = new KeyPile<String, String>();
  private KeyPile<String, DbCnd>    schemaCnd    = new KeyPile<String, DbCnd>();
  private KeyPile<String, DbCnd>    tableCnd     = new KeyPile<String, DbCnd>();
  private KeyPile<String, DbCnd>    viewCnd      = new KeyPile<String, DbCnd>();

  private void init()
  {
   if (!selfTested) selfTest();
   dtv.Set(" >$ " , "UPPER(````<°(´¹´, ²)´````");
   dtv.Set(" <|> ", "UNION``¹``>(´¹´²)´``) ° (²``");
   dtv.Set(" <&> ", "INTERSECT``¹``>(´¹)´ ° ²``(²)``");
   dtv.Set(" <-> ", "EXCEPT``¹``>(´¹)´ ° ²``(²)``");

   if (url == null) { url = new DbUrl("", Drivers); lastUrl = url; return; } 

   if (Dbms.EndsWith("h2s")) { } //H2Sql
   if (Dbms.EndsWith("mys")) { }
   if (Dbms.EndsWith("pgs")) { dtv.Set(" & ", "CONCAT``¹``>°´(¹²´)``, ²"); dtv.Set(" >$ ", "CAST````<°(´¹´, ² AS TEXT)´``"); }
   if (Dbms.EndsWith("fbd")) { }
   if (Dbms.EndsWith("sql")) { }
   if (Dbms.EndsWith("mss")) { dtv.Set(" & ", "+``¹``>¹´ ° ²``²``"); }
   if (Dbms.EndsWith("syb")) { dtv.Set(" & ", "+``¹``>¹´ ° ²``²``"); }
   if (Dbms.EndsWith("ora")) { dtv.Set(" & ", "||``¹``>¹´ ° ²``²``"); }
   if (Dbms.EndsWith("db2")) { }
   if (Dbms.EndsWith("dby")) { }
   if (Dbms.EndsWith("hsq")) { }
   if (Dbms.EndsWith("tda")) { }

   if (Dbms.EndsWith("h2s")) { } //H2Sql
   if (Dbms.EndsWith("mys")) { }
   if (Dbms.EndsWith("pgs")) { }
   if (Dbms.EndsWith("fbd")) { }
   if (Dbms.EndsWith("sql")) { }
   if (Dbms.EndsWith("mss")) { convFormats.Add("YYYY", "LEFT(CONVERT(VARCHAR(23), %FIELD%, 121), 4)"); convFormats.Add("YY", "LEFT(RIGHT(CONVERT(VARCHAR(23), %FIELD%, 121), 21), 2)"); convFormats.Add("MM", "LEFT(RIGHT(CONVERT(VARCHAR(23), %FIELD%, 121), 18), 2)"); convFormats.Add("DD", "LEFT(RIGHT(CONVERT(VARCHAR(23), %FIELD%, 121), 15), 2)"); convFormats.Add("hh", "LEFT(RIGHT(CONVERT(VARCHAR(23), %FIELD%, 121), 12), 2)"); convFormats.Add("mm", "LEFT(RIGHT(CONVERT(VARCHAR(23), %FIELD%, 121), 9), 2)"); convFormats.Add("ss", "LEFT(RIGHT(CONVERT(VARCHAR(23), %FIELD%, 121), 6), 2)"); convFormats.Add("mmm", "RIGHT(CONVERT(VARCHAR(23), %FIELD%, 121), 3)"); }
   if (Dbms.EndsWith("syb")) { }
   if (Dbms.EndsWith("ora")) { }
   if (Dbms.EndsWith("db2")) { }
   if (Dbms.EndsWith("dby")) { }
   if (Dbms.EndsWith("hsq")) { }
   if (Dbms.EndsWith("tda")) { }

   DbMsDataReader    rdr = null;

   if (Dbms.EndsWith("h2s")) { rdr = Db._newDbMsCommand("SELECT DISTINCT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE ((TABLE_TYPE = 'TABLE') OR (TABLE_TYPE = 'VIEW'))", reusedDbMsConnection).ExecuteReader(); } //H2Sql
   if (Dbms.EndsWith("mys")) { rdr = Db._newDbMsCommand("SELECT DISTINCT table_name From INFORMATION_SCHEMA.TABLES Where (table_schema = '" + Database + "')", reusedDbMsConnection).ExecuteReader(); reusedDbMsConnection.ChangeDatabase(Database); }
   if (Dbms.EndsWith("pgs")) { rdr = Db._newDbMsCommand("SELECT DISTINCT table_name, table_schema from information_schema.tables where table_schema NOT IN ('pg_catalog', 'information_schema')", reusedDbMsConnection).ExecuteReader(); }
   if (Dbms.EndsWith("fbd")) { rdr = Db._newDbMsCommand("SELECT DISTINCT * FROM (SELECT DISTINCT rdb$relation_name FROM rdb$relations WHERE ((rdb$view_blr IS null) AND ((rdb$system_flag IS null) or (rdb$system_flag = 0))) UNION SELECT DISTINCT rdb$relation_name FROM rdb$relations WHERE ((rdb$view_blr IS NOT null) AND ((rdb$system_flag IS null) OR (rdb$system_flag = 0)))) ORDER BY 1", reusedDbMsConnection).ExecuteReader(); }
   if (Dbms.EndsWith("sql")) { rdr = Db._newDbMsCommand("SELECT DISTINCT name FROM sqlite_master WHERE ((type = 'table') OR (type = 'view')) ORDER BY 1", reusedDbMsConnection).ExecuteReader(); }
   if (Dbms.EndsWith("mss")) { rdr = Db._newDbMsCommand("SELECT Distinct table_schema + '.' + table_name From information_schema.tables Where (table_catalog = '" + Database + "' AND table_schema = 'dbo')", reusedDbMsConnection).ExecuteReader(); }
   //if (dbms.EndsWith("syb")) { rdr = Db._newDbMsCommand("Select Distinct table_schema + '.' + table_name From information_schema.tables Where (table_catalog = '" + database + "' AND table_schema = 'dbo')", reusedDbMsConnection).ExecuteReader(); }
   if (Dbms.EndsWith("syb")) { rdr = Db._newDbMsCommand("SELECT DISTINCT name FROM sysobjects WHERE ((type='U') OR (type='V')) ORDER BY name)", reusedDbMsConnection).ExecuteReader(); }
   //if (dbms.EndsWith("ora")) { rdr = Db._newDbMsCommand("Select Distinct table_name From all_tables Where (owner = '" + database.ToUpper() + "')", reusedDbMsConnection).ExecuteReader(); }

   if (Dbms.EndsWith("ora"))
   {
    rdr = Db._newDbMsCommand
            (
            "Select Distinct * from (Select Distinct table_name From all_tables Where (owner = '" + User.ToUpper() + "') UNION Select Distinct view_name From all_views Where (owner = '" + User.ToUpper() + "')) ORDER BY 1"
            ,
            reusedDbMsConnection).ExecuteReader();
   }



   if (Dbms.EndsWith("db2")) 
   {
    
    rdr = Db._newDbMsCommand("SELECT name FROM PRODUCT", reusedDbMsConnection).ExecuteReader();

    //rdr = Db._newDbMsCommand("SELECT DISTINCT name FROM sysibm.systables WHERE (owner = 'SCHEMA') AND ((type = 'T') OR (type = 'V')) ORDER BY 1", reusedDbMsConnection).ExecuteReader(); 
   
   }

   if (Dbms.EndsWith("dby")) { rdr = Db._newDbMsCommand("SELECT DISTINCT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE ((TABLETYPE = 'T') OR (TABLETYPE = 'V'))", reusedDbMsConnection).ExecuteReader(); } //HSqlDb
   if (Dbms.EndsWith("hsq")) { rdr = Db._newDbMsCommand("SELECT DISTINCT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE ((TABLE_TYPE = 'TABLE') OR (TABLE_TYPE = 'VIEW'))", reusedDbMsConnection).ExecuteReader(); } //HSqlDb
   if (Dbms.EndsWith("tda")) { }
   while (rdr.Read()) 
   {
    string tableName = fromDb(rdr.GetString(0)).Trim().ToLower();
    if (Dbms.EndsWith("mss")) if (tableName.StartsWith("dbo.")) tableName = tableName.Substring(4);
    if (Dbms.EndsWith("syb")) if (tableName.StartsWith("dbo.")) tableName = tableName.Substring(4);
    tables.Add(tableName, new DbGrid(this, tableName));
   }
   //utl.say(utl.l2s(tableNames,"\r\n"));
   rdr.Dispose();
   rdr.Close();
   string loadErrors = "";
   foreach (string table in tables.Keys)
   {
    try
    {
     if (Dbms.EndsWith("h2s")) { rdr = Db._newDbMsCommand("Select * From "       + dbTable(table) + " Limit 0", reusedDbMsConnection).ExecuteReader(); }
     if (Dbms.EndsWith("mys")) { rdr = Db._newDbMsCommand("Select * From "       + dbTable(table) + " Limit 0, 0", reusedDbMsConnection).ExecuteReader(); }
     if (Dbms.EndsWith("pgs")) { rdr = Db._newDbMsCommand("Select * From "       + dbTable(table) + " Limit 0", reusedDbMsConnection).ExecuteReader(); }
     if (Dbms.EndsWith("fbd")) { }
     if (Dbms.EndsWith("sql")) { }
     if (Dbms.EndsWith("mss")) { rdr = Db._newDbMsCommand("Select Top 0 * From " + dbTable(table), reusedDbMsConnection).ExecuteReader(); }
     if (Dbms.EndsWith("syb")) { rdr = Db._newDbMsCommand("Select Top 0 * From " + dbTable(table), reusedDbMsConnection).ExecuteReader(); }
     if (Dbms.EndsWith("ora")) { rdr = Db._newDbMsCommand("Select * From "       + dbTable(table) + " Where (rownum <= 1)", reusedDbMsConnection).ExecuteReader(); }
     if (Dbms.EndsWith("db2")) { }
     if (Dbms.EndsWith("dby")) { }
     if (Dbms.EndsWith("hsq")) { }
     if (Dbms.EndsWith("tda")) { }
     long i = 1;
     while (i > 0) 
      try 
      { 
       tables[table].Fields.Add(new DbField(false, rdr.GetName((int)i - 1).ToLower(), rdr.GetDataTypeName((int)i - 1).ToLower())); i++; 
      }
      catch (Exception ex) 
      { 
       i = 0; 
      }
     rdr.Dispose();
     rdr.Close();
    }
    catch (Exception ex) { loadErrors += "\r\n" + table; reusedDbMsConnection = newDbMsConnection(); }
   }
   lastUrl = new DbUrl(url);
   if (loadErrors.Length > 0) utl.say("Incompletely loaded Table(s) / View(s):" + loadErrors);
   
   //lastDbTec = dbtec; lastDbms = dbms; lastHost = host; lastInstance = instance; lastDatabase = database; lastSchema = schema; lastUser = user; lastPassword = password;
  }

  public string ConnectString(bool hidePassword) { return url.ConnectString(hidePassword); }

  private void addDbMru(DbUrl url)
  {
   ctx cx = new ctx();
   string connect = url.ConnectString(true);
   KeyPile<string, string> mruList = new KeyPile<string, string>();
   for (long i = 1; i < 10; i++)
   {
    string val = "";
    try { val = (string)(Registry.CurrentUser.CreateSubKey("SOFTWARE\\" + cx.Name + "\\DataBases").GetValue("MRU" + i)); } catch (Exception ex) { val = ""; }
    if (val == null) val = "";
    if (val.Length > 0) if (!val.Trim().ToLower().Equals(connect.Trim().ToLower())) mruList.Add("MRU" + i, val);
   }
   mruList.Add("MRU" + 0, connect.Trim());
   long j = 0;
   foreach (string key in mruList.kAsc)
   {
    j++;
    if (j > 9) break;
    try { Registry.CurrentUser.CreateSubKey("SOFTWARE\\" + cx.Name + "\\DataBases").SetValue("MRU" + j, mruList[key]); } catch (Exception ex) { }    
   }
  }



  private void init(string connectstring)
  {
   if (!selfTested) selfTest();
   if (connectstring.Length == 0) { init(); return; }
   url = new DbUrl(connectstring, Drivers);
   if (Dbms.EndsWith("ora")) avoidEmptyStrings = true;
   reusedDbMsConnection = newDbMsConnection();
   init();
   addDbMru(url);
  }

  /*
  public string buildSelectFields(params string[] fields)
  {
   string ret = "";
   string sql = "";
   foreach (string f in fields)
   {
    if (f.StartsWith("[["))
    {
     Reach format = new Reach(utl.cutl(f.Substring(2), "]]"));
     Pile<Reach> terms = new Pile<Reach>();
     Reach field = new Reach(f).after("]]");
     while (format.len > 0)
     {
      Reach term = format.before(1, "YYYY", "YY", "MM", "DD", "hh", "mm", "ss", "mmm");
      if (term.len > 0) terms.Push(new Reach(ds(term)));
      term = format.at(1, "YYYY", "YY", "MM", "DD", "hh", "mm", "ss", "mmm");
      if (term.len == 0) break;
      terms.Push(term);
      format = format.after(term);
     }
     sql = "";
     foreach (Reach trm in terms) if (trm.startsWith("'")) sql += trm.text + " + "; else sql += convFormats[trm.text] + " + ";
     if (sql.Length > 0) sql = sql.Substring(0, sql.Length - 3);
     sql = sql.Replace("%FIELD%", field.text);
    }
    else sql = f;
    ret += sql + ", ";
   }
   return ret.Substring(0, ret.Length - 2);
  }
  */

  public string buildSelectFields(params DbField[] fields)
  {
   string ret = "";
   string sql = "";
   foreach (DbField dbf in fields)
   {
    string f = dbf.sql(this);
    if (f.StartsWith("[["))
    {
     Reach format = new Reach(utl.cutl(f.Substring(2), "]]"));
     Pile<Reach> tokens = new Pile<Reach>();
     Reach field = new Reach(f).after("]]");
     while (format.len > 0)
     {
      Reach tok = format.before(1, true, "YYYY", "YY", "MM", "DD", "hh", "mm", "ss", "mmm");
      if (tok.len > 0) tokens.Push(new Reach(ds(tok)));
      tok = format.at(1, true, "YYYY", "YY", "MM", "DD", "hh", "mm", "ss", "mmm");
      if (tok.len == 0) break;
      tokens.Push(tok);
      format = format.after(tok);
     }
     sql = "";
     foreach (Reach tok in tokens) if (tok.startsWith("'")) sql += tok.text + " + "; else sql += convFormats[tok.text] + " + ";
     if (sql.Length > 0) sql = sql.Substring(0, sql.Length - 3);
     sql = sql.Replace("%FIELD%", field.text);
    }
    else sql = f;
    ret += sql + ", ";
   }
   //utl.say(ret.Substring(0, ret.Length - 2));
   return ret.Substring(0, ret.Length - 2);
  }

  public string dbTable(string table)
  {
   if (Dbms.EndsWith("h2s")) { return (table.IndexOf(".") > -1) ? (table.IndexOf(".") == 0) ? table.Substring(1) : table : "PUBLIC."            + table; }
   if (Dbms.EndsWith("mys")) { return (table.IndexOf(".") > -1) ? (table.IndexOf(".") == 0) ? table.Substring(1) : table : Database + "."       + table; }
   if (Dbms.EndsWith("pgs")) { return (table.IndexOf(".") > -1) ? (table.IndexOf(".") == 0) ? table.Substring(1) : table :                        table; }
   if (Dbms.EndsWith("fbd")) { return (table.IndexOf(".") > -1) ? (table.IndexOf(".") == 0) ? table.Substring(1) : table : Database + "."       + table; }
   if (Dbms.EndsWith("sql")) { return (table.IndexOf(".") > -1) ? (table.IndexOf(".") == 0) ? table.Substring(1) : table : Database + "."       + table; }
   if (Dbms.EndsWith("mss")) { return (table.IndexOf(".") > -1) ? (table.IndexOf(".") == 0) ? table.Substring(1) : table : "dbo."               + table; }
   if (Dbms.EndsWith("ora")) { return (table.IndexOf(".") > -1) ? (table.IndexOf(".") == 0) ? table.Substring(1) : table : User.ToUpper() + "." + table; }
   if (Dbms.EndsWith("db2")) { return (table.IndexOf(".") > -1) ? (table.IndexOf(".") == 0) ? table.Substring(1) : table : Database + "."       + table; }
   if (Dbms.EndsWith("dby")) { return (table.IndexOf(".") > -1) ? (table.IndexOf(".") == 0) ? table.Substring(1) : table : Database + "."       + table; }
   if (Dbms.EndsWith("hsq")) { return (table.IndexOf(".") > -1) ? (table.IndexOf(".") == 0) ? table.Substring(1) : table : Database + "."       + table; }
   if (Dbms.EndsWith("tda")) { return (table.IndexOf(".") > -1) ? (table.IndexOf(".") == 0) ? table.Substring(1) : table : "PUBLIC."            + table; }
   return table;
  }

  private Pile<DbMsConnection> freeConnections = new Pile<DbMsConnection>();
  private Pile<DbMsConnection> usedConnections = new Pile<DbMsConnection>();

  public void setConnectionState(bool used, DbMsConnection cnn)
  {
   if (used) for(int i = 1; i <= freeConnections.Len; i++) if (cnn == freeConnections[i]) { freeConnections.Del(i); usedConnections.Push(cnn); return;}
   for (int i = 1; i <= usedConnections.Len; i++) if (cnn == usedConnections[i]) { usedConnections.Del(i); freeConnections.Push(cnn); return; }
  }
  
  public DbMsConnection newDbMsConnection()  // A new DatabaseDriverConnection (odbc or jdbc or dotnet resp.)
  {
   DbMsConnection ret = null;
   if (freeConnections.Len > 0) { ret = freeConnections[1]; setConnectionState(true, ret); return ret; }
   string DSN                 = Host.StartsWith("[")  ? Host.Substring(1, Host.Length - 2) : "";
   string Driver              = DSN.Trim().Length > 0 ? "" : url == null ? "" : url.Driver;
   string Server              = DSN.Trim().Length > 0 ? "" : Host.Trim().Length    == 0 ? "" : Host.Trim();
   ret = Db._newDbMsConnection(this, Dbtec, Dbms, DSN, Driver, Server, Instance.Trim(), Port, Database.Trim(), Schema.Trim(), User.Trim(), Password.Trim(), 0, 0, "3", "16834", false, 0, 200);  //odbc:MyS seems to want this Option "16834", no idea what it is good for
   ret.ConnectionTimeout = 90;
   ret.Open();
   usedConnections.Add(ret);
   return ret;
  }
  
  public string copyTo(Db target, string tableName)
  {
   string ret = "";
   string copied = "successfully copied (";
   string failed = "failed to copy: (";
   foreach (DbGrid tbl in Tables) try { if ((tableName.Trim().Equals("*")) || (tbl.Name.ToUpper().Trim().Equals(tableName.ToUpper().Trim()))) tbl.copyTo(target); copied += tbl.Name + ", "; } catch { failed += tbl.Name + ", "; }
   if (copied.Length > 21) ret += copied.Substring(0, copied.Length - 2) + ") \r\n";
   if (failed.Length > 17) ret += failed.Substring(0, failed.Length - 2) + ")";
   if (ret.EndsWith("\r\n")) return ret.Substring(0, ret.Length - 2); else return ret;
  }

  
 }
}














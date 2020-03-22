

using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Windows.Forms;

using Microsoft.Win32;

using ndBase;
using ndString;

namespace ndData
{
 public partial class frmDbConnect : Form
 {
  //Copyright© 1996-2012 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties, You may use, modify, distribute this code under GNU public licence for NON-Commercial use as long as this notice remains unchanged.
  private static bool selfTested  = false;  private static string ass(bool expr) { if (!expr) throw new Exception(ass(true) + " SelfTest Failure"); return "frmDbConnect"; } private static void selfTest() { selfTested = true; } private void init() { if (!selfTested) selfTest(); }

  private Db db = null;
  private void loadDbMru()
  {
   ctx cx = new ctx();
   KeyPile<string, string> mruList = new KeyPile<string, string>();
   for (long i = 1; i < 10; i++)
   {
    string val = "";
    try { val = (string)(Registry.CurrentUser.CreateSubKey("SOFTWARE\\" + cx.Name + "\\DataBases").GetValue("MRU" + i)); } catch { val = ""; }
    if (val != null) if (val.Length > 0) mruList.Add("MRU" + i, val);
   }
   long j = 0;
   foreach (string key in mruList.kAsc) { j++; if (j > 9) break; pupMru.Items.Add(mruList[key]); }
  }

  public frmDbConnect(Db db)
  {
   this.db = db;
   InitializeComponent();
   string activeDbms = " " + (string)(new ctx()).IniParam["ACTIVEDBMS"] + " ";
   pupDbms.Items.Clear();
   if (activeDbms.IndexOf(" h2s ")  > -1) pupDbms.Items.Add("h2s  (H2Sql)");
   if (activeDbms.IndexOf(" mys ")  > -1) pupDbms.Items.Add("mys  (Oracle MySql)");
   if (activeDbms.IndexOf(" 2mys ") > -1) pupDbms.Items.Add("2mys (Oracle MySql Emulation of H2Db)");
   if (activeDbms.IndexOf(" pgs ")  > -1) pupDbms.Items.Add("pgs  (PostGreSql)");
   if (activeDbms.IndexOf(" 2pgs ") > -1) pupDbms.Items.Add("2pgs (PostGreSql Emulation of H2Db)");
   if (activeDbms.IndexOf(" fbd ")  > -1) pupDbms.Items.Add("fbd  (FireBirdSql)");
   if (activeDbms.IndexOf(" 2fbd ") > -1) pupDbms.Items.Add("2fbd (FireBirdSql Emulation of H2Db)");
   if (activeDbms.IndexOf(" sql ")  > -1) pupDbms.Items.Add("sql  (SqLite)");
   if (activeDbms.IndexOf(" 2sql ") > -1) pupDbms.Items.Add("2sql (SqLite Emulation of H2Db)");
   if (activeDbms.IndexOf(" mss ")  > -1) pupDbms.Items.Add("mss  (MsSqlServer)");
   if (activeDbms.IndexOf(" 2mss ") > -1) pupDbms.Items.Add("2mss (MsSqlServer Emulation of H2Db)");
   if (activeDbms.IndexOf(" syb ")  > -1) pupDbms.Items.Add("syb  (SAP SyBase)");
   if (activeDbms.IndexOf(" 2syb ") > -1) pupDbms.Items.Add("2syb (SAP SyBase Emulation of H2Db)");
   if (activeDbms.IndexOf(" ora ")  > -1) pupDbms.Items.Add("ora  (Oracle)");
   if (activeDbms.IndexOf(" 2ora ") > -1) pupDbms.Items.Add("2ora (Oracle Emulation of H2Db)");
   if (activeDbms.IndexOf(" db2 ")  > -1) pupDbms.Items.Add("db2  (IBM DB2)");
   if (activeDbms.IndexOf(" 2db2 ") > -1) pupDbms.Items.Add("2db2 (IBM DB2 Emulation of H2Db)");
   if (activeDbms.IndexOf(" dby ")  > -1) pupDbms.Items.Add("dby  (Apache Derby)");
   if (activeDbms.IndexOf(" 2dby ") > -1) pupDbms.Items.Add("2dby (Apache Derby Emulation of H2Db)");
   if (activeDbms.IndexOf(" hsq ")  > -1) pupDbms.Items.Add("hsq  (HSqlDb)");
   if (activeDbms.IndexOf(" 2hsq ") > -1) pupDbms.Items.Add("2hsq (HSqlDb Emulation of H2Db)");
   if (activeDbms.IndexOf(" tda ")  > -1) pupDbms.Items.Add("tda  (Novell TeraData)");
   loadDbMru();
  }

  public DbUrl Url
  {
   get { return new DbUrl(pupDbTec.Text.Trim() + ":" + utl.cutl(pupDbms.Text.Trim(), " ") + "::" + txtInstance.Text.Trim().Replace("\\", "/") + "," + txtDatabase.Text.Trim() + "," + txtUser.Text.Trim() + "," + txtPassword.Text.Trim() + "<<" + txtTFilter.Text.Trim() + "<<" + txtVFilter.Text.Trim(), db.Drivers); }
   set 
   {
    for (int i = 1; i <= pupDbTec.Items.Count; i++) if (((string)(pupDbTec.Items[i - 1])).StartsWith(value.dbtec)) pupDbTec.SelectedIndex = i - 1;
    for (int i = 1; i <= pupDbms.Items.Count; i++) if (((string)(pupDbms.Items[i - 1])).StartsWith(value.dbms + " ")) pupDbms.SelectedIndex = i - 1;
    txtInstance.Text   = value.host.Trim() + utl.pV(":", value.port) + utl.pV("/", value.instance.Trim());
    txtDatabase.Text   = value.database.Trim() + utl.ppV("[", "]", value.schema.Trim());
    txtUser.Text       = value.user;
    txtPassword.Text   = value.password;
    txtTFilter.Text    = value.tfilter;
    txtVFilter.Text    = value.vfilter;
   }
  }

  private void pupMru_SelectedIndexChanged(object sender, EventArgs e) { Url = new DbUrl(pupMru.SelectedItem.ToString(), db.Drivers); }

 }
}






















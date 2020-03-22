using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Windows.Forms;

using ndBase;
using ndString;

namespace ndData
{
 public partial class frmSelectView : Form
 {
  //Copyright© 1996-2012 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties, You may use, modify, distribute this code under GNU public licence for NON-Commercial use as long as this notice remains unchanged.
  private static bool selfTested  = false;  private static string ass(bool expr) { if (!expr) throw new Exception(ass(true) + " SelfTest Failure"); return "frmSelectView"; } private static void selfTest() { selfTested = true; } private void init() { if (!selfTested) selfTest(); }

  private DbSlcBlock dbs = new DbSlcBlock();

  public frmSelectView(Db db, DbSlcBlock dbs)
  {
   InitializeComponent();
   this.dbs = dbs;
   foreach (DbSlc sel in dbs) lstSelectView.Items.Add(sel.sql(db).Replace("\r", " ").Replace("\n", " "));
  }
  
  public frmSelectView() { InitializeComponent(); }

  private void lstSelectView_SizeChanged(object sender, EventArgs e)
  {
   lstSelectView.Columns[0].Width = lstSelectView.Width;
  }

  private void frmSelectView_SizeChanged(object sender, EventArgs e)
  {
   lstSelectView.Width = Width;
   lstSelectView.Height = Height;
  }
 }
}




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

 
 public partial class frmDtvEdit : Form 
  
 {
  Trm trm = new Trm("");

  public frmDtvEdit() { InitializeComponent(); }

  private void lstDtv_DoubleClick(object sender, EventArgs e) { txtDtv.Text = lstDtv.SelectedItems[0].Text; }

  public string Directive()
  {
   return txtOptor.Text + " = " + txtOptr.Text  + "``" + txtDtv0.Text  + "``" + ((chkRight2Left.Checked) ? "<" : ">") + txtDtv1lS.Text  + "´" + txtDtv1lR.Text  + ((chkRevert.Checked) ? "²" : "¹") + txtDtv1mS.Text  + "´" + txtDtv1mR.Text  + ((chkRevert.Checked) ? "¹" : "²") +  txtDtv1rS.Text  + "´" + txtDtv1rR.Text  + "``" + txtDtv2.Text;
  }

  private int programGeneratedEvent = 0;

  private Trm trmA = new Trm("a");
  private Trm trmB = new Trm("b");
  private Trm trmC = new Trm("c");
  private Trm trmD = new Trm("d");

  private Trm trm4 = null;
  private Trm trm3 = null;
  private Trm trm2 = null;
  private Trm trm1 = null;


  private void txtDtv_TextChanged(object sender, EventArgs e) 
  {
   if (programGeneratedEvent > 0) return;
   programGeneratedEvent++;
   try
   {
    Reach dtv = txtDtv.Text;
    txtOptor.Text = dtv.before(1, " = ");
    writeToTable(txtOptor.Text, dtv.after(1, " = "));
    trm = new Trm(txtOptor.Text, txtOpnd1.Text, txtOpnd2.Text, txtOpnd3.Text, txtOpnd4.Text); //trm = new Trm(":dbg:" + txtOptor.Text, txtOpnd1.Text, txtOpnd2.Text, txtOpnd3.Text, txtOpnd4.Text);
    trm.Dtv.Set(txtOptor.Text, dtv.after(1, " = ").text);  //trm.Dtv.Set(":dbg:" + txtOptor.Text, dtv.after(1, " = ").text);
    try { txtVal4.Text = trm.val(); }
    catch { }
    txtOptr.Text = trm.optr;
    txtDtv0.Text = trm.dtv0;
    txtDtv1lR.Text = trm.dtv1lR;
    txtDtv1mR.Text = trm.dtv1mR;
    txtDtv1rR.Text = trm.dtv1rR;
    txtDtv1lS.Text = trm.dtv1lS;
    txtDtv1mS.Text = trm.dtv1mS;
    txtDtv1rS.Text = trm.dtv1rS;
    txtDtv2.Text = trm.dtv2;
    chkRight2Left.Checked = !trm.left2right;
    chkRevert.Checked = trm.revert;
    try
    {
     trm = new Trm(txtOptor.Text, txtOpnd1.Text, txtOpnd2.Text, txtOpnd3.Text);
     trm.Dtv.Set(txtOptor.Text, dtv.after(1, " = ").text);  //trm.Dtv.Set(":dbg:" + txtOptor.Text, dtv.after(1, " = ").text);
     txtVal3.Text = trm.val(); //txtVal3.Text = new Trm(":dbg:" + txtOptor.Text, txtOpnd1.Text, txtOpnd2.Text, txtOpnd3.Text).val();
     trm = new Trm(txtOptor.Text, new object[] { txtOpnd1.Text, txtOpnd2.Text });
     trm.Dtv.Set(txtOptor.Text, dtv.after(1, " = ").text);  //trm.Dtv.Set(":dbg:" + txtOptor.Text, dtv.after(1, " = ").text);
     txtVal2.Text = trm.val(); //txtVal2.Text = new Trm(":dbg:" + txtOptor.Text, new object[] { txtOpnd1.Text, txtOpnd2.Text }).val();
     trm = new Trm(txtOptor.Text, txtOpnd1.Text);
     trm.Dtv.Set(txtOptor.Text, dtv.after(1, " = ").text);  //trm.Dtv.Set(":dbg:" + txtOptor.Text, dtv.after(1, " = ").text);
     txtVal1.Text = trm.val(); //txtVal1.Text = new Trm(":dbg:" + txtOptor.Text, txtOpnd1.Text).val();
    }
    catch { }
   }
   finally { programGeneratedEvent--; }
  }

  private void txtDtv0_TextChanged               (object sender, EventArgs e) { if (programGeneratedEvent == 0) txtDtv.Text = Directive(); }
  private void txtDtv2_TextChanged               (object sender, EventArgs e) { if (programGeneratedEvent == 0) txtDtv.Text = Directive(); }
  private void txtDtv1lR_TextChanged             (object sender, EventArgs e) { if (programGeneratedEvent == 0) txtDtv.Text = Directive(); }
  private void txtDtv1mR_TextChanged             (object sender, EventArgs e) { if (programGeneratedEvent == 0) txtDtv.Text = Directive(); }
  private void txtDtv1rR_TextChanged             (object sender, EventArgs e) { if (programGeneratedEvent == 0) txtDtv.Text = Directive(); }
  private void txtDtv1lS_TextChanged             (object sender, EventArgs e) { if (programGeneratedEvent == 0) txtDtv.Text = Directive(); }
  private void txtDtv1mS_TextChanged             (object sender, EventArgs e) { if (programGeneratedEvent == 0) txtDtv.Text = Directive(); }
  private void txtDtv1rS_TextChanged             (object sender, EventArgs e) { if (programGeneratedEvent == 0) txtDtv.Text = Directive(); }
  private void txtOpnd1_TextChanged              (object sender, EventArgs e) { if (programGeneratedEvent == 0) txtDtv.Text = Directive(); }
  private void txtOpnd2_TextChanged              (object sender, EventArgs e) { if (programGeneratedEvent == 0) txtDtv.Text = Directive(); }
  private void txtOpnd3_TextChanged              (object sender, EventArgs e) { if (programGeneratedEvent == 0) txtDtv.Text = Directive(); }
  private void txtOpnd4_TextChanged              (object sender, EventArgs e) { if (programGeneratedEvent == 0) txtDtv.Text = Directive(); }
  private void txtOptor_TextChanged              (object sender, EventArgs e) { if (programGeneratedEvent == 0) txtDtv.Text = Directive(); }
  private void txtOptr_TextChanged               (object sender, EventArgs e) { if (programGeneratedEvent == 0) txtDtv.Text = Directive(); }
  private void chkRight2Left_CheckStateChanged   (object sender, EventArgs e) { if (programGeneratedEvent == 0) txtDtv.Text = Directive(); }
  private void chkRevert_CheckedChanged          (object sender, EventArgs e) { if (programGeneratedEvent == 0) txtDtv.Text = Directive(); /*txtDtv_TextChanged(sender, e);*/ }
 
  private void btnParseTrm4_Click   (object sender, EventArgs e)  { try {trm = new Trm(txtVal4.Text);   trm.Dtv.Set(txtOptor.Text, new Reach(txtDtv.Text).after(1, " = ").text); txtTrmVal.Text = trm.val()   ; } catch (Exception ex){txtTrmVal.Text = "Parese Error: " + ex.Message;} } 
  private void btnParseTrm3_Click   (object sender, EventArgs e)  { try {trm = new Trm(txtVal3.Text);   trm.Dtv.Set(txtOptor.Text, new Reach(txtDtv.Text).after(1, " = ").text); txtTrmVal.Text = trm.val()   ; } catch (Exception ex){txtTrmVal.Text = "Parese Error: " + ex.Message;} } 
  private void btnParseTrm2_Click   (object sender, EventArgs e)  { try {trm = new Trm(txtVal2.Text);   trm.Dtv.Set(txtOptor.Text, new Reach(txtDtv.Text).after(1, " = ").text); txtTrmVal.Text = trm.val()   ; } catch (Exception ex){txtTrmVal.Text = "Parese Error: " + ex.Message;} } 
  private void btnParseTrm1_Click   (object sender, EventArgs e)  { try {trm = new Trm(txtVal1.Text);   trm.Dtv.Set(txtOptor.Text, new Reach(txtDtv.Text).after(1, " = ").text); txtTrmVal.Text = trm.val()   ; } catch (Exception ex){txtTrmVal.Text = "Parese Error: " + ex.Message;} } 
  private void btnParseTrm_Click    (object sender, EventArgs e)  { try {trm = new Trm(txtNewTrm.Text); trm.Dtv.Set(txtOptor.Text, new Reach(txtDtv.Text).after(1, " = ").text); txtTrmVal.Text = trm.val()   ; } catch (Exception ex){txtTrmVal.Text = "Parese Error: " + ex.Message;} }

  private void chkDbgXpn_CheckedChanged(object sender, EventArgs e)
  {
   Trm.dbg = chkDbgXpn.Checked;
  }


  private void writeToTable(string key, string dtv)
  {
   if (dtv.Length == 0) return;
   DataGridViewRow r = null;
   foreach (DataGridViewRow rw in tblDtv.Rows) if (rw.Cells[0].Value != null) if (((string)rw.Cells[0].Value).Equals(key)) r = rw;
   if (r == null) r = tblDtv.Rows[tblDtv.Rows.Add()];
   r.Cells[0].Value = " " + key.Substring(1, key.Length -2) + " ";
   r.Cells[1].Value = utl.cutl(ref dtv, "``");
   r.Cells[2].Value = utl.cutl(ref dtv, "``");
   if (dtv.Length == 0) return;
   r.Cells[3].Value = "" + dtv[0];
   dtv = dtv.Substring(1);
   r.Cells[4].Value = utl.cutl(ref dtv, "``");
   r.Cells[8].Value = utl.cutl(ref dtv, "``");
   dtv = (string)r.Cells[4].Value;
   r.Cells[4].Value = ">";
   if ((dtv.IndexOf("¹") > -1) && (dtv.IndexOf("²") > -1)) if (dtv.IndexOf("¹") < dtv.IndexOf("²")) r.Cells[4].Value = ">"; else r.Cells[4].Value = "<";
   if (((string)r.Cells[4].Value).Equals(">"))
   {
    r.Cells[5].Value = new Reach(dtv).before(1, "¹").text;
    r.Cells[6].Value = new Reach(dtv).after(1, "¹").before(1, "²").text;
    r.Cells[7].Value = new Reach(dtv).after(1, "²").text;
   }
   else
   {
    r.Cells[5].Value = new Reach(dtv).before(1, "²").text;
    r.Cells[6].Value = new Reach(dtv).after(1, "²").before(1, "¹").text;
    r.Cells[7].Value = new Reach(dtv).after(1, "¹").text;
   }
  }

  private void reload()
  {

   tblDtv.Rows.Clear();
   trm4 = new Trm(" + ", trmA, trmB, trmC, trmD);
   trm3 = new Trm(" + ", trmA, trmB, trmC);
   trm2 = new Trm(" + ", trmA, trmB);
   trm1 = new Trm(" + ", trmA);

   new Trm().Dtv.Set("x,x", ",``¹``>¹²``, ° ²``");
   new Trm().Dtv.Set("x*x", "mal``¹``>¹²``, ° ²``");

   KeyPile<string, string> dtvs = new Trm().Dtv;

   foreach (string key in dtvs.Keys)
   {
    string dtv = dtvs[key];
    if (key.StartsWith("x"))
    {
     try
     {
      programGeneratedEvent++;
      writeToTable(key, dtv);
     }
     finally { programGeneratedEvent--; }
    }
   }

  }

  private void frmDtvEdit_Load(object sender, EventArgs e)
  {

   reload();

  }


  private void tblDtv_CellEnter(object sender, DataGridViewCellEventArgs e)
  {
   if (programGeneratedEvent > 0) return;
   if (tblDtv.CurrentRow == null) return;

   bool revert = false;
   string dtv = "";
   for (int i = 1; i <= 9; i++)
   {
    DataGridViewTextBoxCell c = (DataGridViewTextBoxCell)tblDtv.CurrentRow.Cells[i - 1];
    if (c.Value == null) return;
    switch (i)
    {
     case 1: dtv += (string)c.Value + " = "; break;
     case 2: dtv += (string)c.Value + "``"; break;
     case 3: dtv += (string)c.Value + "``"; break;
     case 4: dtv += (string)c.Value; break;
     case 5: dtv += ""; revert = ((string)c.Value).Equals("<"); break;
     case 6: dtv += (revert) ? (string)c.Value + "²" : (string)c.Value + "¹"; break;
     case 7: dtv += (revert) ? (string)c.Value + "¹" : (string)c.Value + "²"; break;
     case 8: dtv += (string)c.Value + "``"; break;
     case 9: dtv += (string)c.Value + "``"; break;
    }
   }
   //programGeneratedEvent++;
   txtDtv.Text = dtv;
   //programGeneratedEvent--;
   return;
  }

  private void tblDtv_RowEnter(object sender, DataGridViewCellEventArgs e)
  {
   if (programGeneratedEvent > 0) return;
   return;


  }

  private void btnReload_Click(object sender, EventArgs e)
  {
   reload();
  } 

 }
}






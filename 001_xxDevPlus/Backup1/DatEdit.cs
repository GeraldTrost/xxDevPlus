
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Windows.Forms;

using ndBase;
using ndString;
using ndData;


namespace ndData
{

 public class DatEdit
 {

  private Control           lstCtl   = null;
  KeyPile<string, Control>  dtlCtl   = new KeyPile<string, Control>();
  private DbObj             tplRec   = null; 
  public DbObj              curRec   { get { return (tplRec.Recs.Len > 0) && (((ListView)(lstCtl)).SelectedIndices.Count > 0) ? tplRec.Recs[((ListView)(lstCtl)).SelectedIndices[0] + 1] : tplRec; } }

  private void objToList(DbObj obj)
  {
   long linx = ((ListView)(lstCtl)).Items.Count;
   if (((ListView)(lstCtl)).SelectedIndices.Count > 0) linx = ((ListView)(lstCtl)).SelectedIndices[0]; else ((ListView)(lstCtl)).Items.Add("");
   ListViewItem item =  ((ListView)(lstCtl)).Items[(int)linx];
   item.SubItems[0].Text = "" + obj["nm"];
   if (item.SubItems.Count > 1) item.SubItems[1].Text = "" + obj["seq"]; else  item.SubItems.Add("" + obj["seq"]);
   if (item.SubItems.Count > 2) item.SubItems[2].Text = "" + obj["rpt"]; else item.SubItems.Add("" + obj["rpt"]);
  }
  
  private void reloadList()
  {
   ((ListView)(lstCtl)).Items.Clear();
   foreach (DbObj obj in tplRec.Recs) objToList(obj);
  }
  
  public DatEdit(Control lstCtl, KeyPile<string, Control> dtlCtl, DbObj tplRec)
  {
   this.tplRec = tplRec;
   this.lstCtl = lstCtl;
   this.dtlCtl = dtlCtl;
   foreach (Control ctl in dtlCtl) ((TextBox)(ctl)).TextChanged += dtlCtl_TextChanged;
   ((ListView)(lstCtl)).SelectedIndexChanged += new System.EventHandler(lstCtl_SelectedIndexChanged);
   reloadList();
  }

  private long passiveClick = 0;
  
  private void lstCtl_SelectedIndexChanged(object sender, EventArgs e) 
  { 
   passiveClick++;
   try { foreach (string key in dtlCtl.Keys) ((TextBox)(dtlCtl[key])).Text = "" + curRec[key]; } catch {}
   passiveClick--;
  }

  private void dtlCtl_TextChanged(object sender, EventArgs e) 
  {
   if (passiveClick > 0) return;
   foreach (string key in dtlCtl.Keys) 
   if (dtlCtl[key] == sender) 
   {
    Type t = curRec[key].GetType();
    try
    {
     if (t == typeof(Reach)) curRec[key] = ((TextBox)(dtlCtl[key])).Text.Trim();
     if (t == typeof(string)) curRec[key] = ((TextBox)(dtlCtl[key])).Text.Trim();
     if (t == typeof(double)) curRec[key] = double.Parse("0" + ((TextBox)(dtlCtl[key])).Text.Trim());
     if (t == typeof(long)) curRec[key] = long.Parse(((TextBox)(dtlCtl[key])).Text.Trim());
    }
    catch {}
    if (curRec.commit())  //has been saved in a new Table Row
    {
     objToList(tplRec.Recs[-1]);
     ((ListView)(lstCtl)).Items[(int)tplRec.Recs.Len - 1].Selected = true;
    }
    else
    {
     objToList(curRec);
    }
   }
  }

 }

}








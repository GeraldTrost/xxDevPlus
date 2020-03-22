using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Drawing;
using System.Data;
using System.Linq;
using System.Text;
using System.Windows.Forms;


using org_xxdevplus_utl;
using org_xxdevplus_struct;
using org_xxdevplus_chain;


namespace org_xxdevplus_gui
{
 public partial class ctlText : UserControl
 {
  //Copyright© 1996-2012 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties, You may use, modify, distribute this code under GNU public licence for NON-Commercial use as long as this notice remains unchanged.
  private static bool selfTested  = false;  private static string ass(bool expr) { if (!expr) throw new Exception(ass(true) + " SelfTest Failure"); return "ctlText"; } private static void selfTest() { selfTested = true; } private void init() { if (!selfTested) selfTest(); }

  public delegate void               SimpleEvent(object sender);
  public event     SimpleEvent       TextChanged     = null;


  public Pile<string> lItems = new Pile<string>();
  public Pile<string> rItems = new Pile<string>();
  

  private bool   extended = true;
  private bool   editMode = false;
  private long    readOnly = 0;

  public  long    ReadOnly {get{return readOnly;      } set{readOnly = value; txtLeft.ReadOnly = (readOnly / 2 > 0); txtRight.ReadOnly = (readOnly % 2 > 0);  } }
  public  bool   Extended {get{return extended;      } set{extended      = value; } }
  public string  lText    {get{return txtLeft.Text;  } set{txtLeft.Text  = value; } }
  public string  rText    {get{return txtRight.Text; } set{txtRight.Text = value; } }
  //public Font    Font     {get{return lblLeft.Font;  } set{lblLeft.Font  = value; lblRight.Font = value; txtLeft.Font = value; txtRight.Font = value; Height = lblLeft.Height + 2; } }

  private void adjustLabels() 
  { 
   if (extended) 
   {
    lblLeft.Location    = new Point(Width / 2 - (lblLeft.Width + lblRight.Width) / 2, lblLeft.Location.Y);
    lblRight.Location   = new Point(lblLeft.Location.X + lblLeft.Width, lblRight.Location.Y); 
   } 
   else lblLeft.Location    = new Point(Width / 2 - lblLeft.Width / 2, lblLeft.Location.Y); 
  }

  public bool EditMode 
  {
   get 
   {
    return editMode; 
   } 
   set
   {
    value = true;
    editMode = value; 
    if (editMode) 
    {
     txtLeft.Visible = true; 
     txtRight.Visible = extended; 
     lblLeft.Visible = false; 
     lblRight.Visible = false;
     AutoCompleteStringCollection lItemss = new AutoCompleteStringCollection();
     foreach (string txt in this.lItems) lItemss.Add(txt);
     txtLeft.AutoCompleteCustomSource = lItemss;
     txtLeft.AutoCompleteMode = AutoCompleteMode.SuggestAppend;
     AutoCompleteStringCollection rItemss = new AutoCompleteStringCollection();
     foreach (string txt in this.rItems) rItemss.Add(txt);
     txtRight.AutoCompleteCustomSource = rItemss;
    } 
    else 
    {
     txtLeft.Visible = false; 
     txtRight.Visible = false; 
     lblLeft.Visible = true; 
     lblRight.Visible = extended; 
    }
   } 
  }

  private void mnuArrowClick(object sender, EventArgs e)
  {
   //txtLeft.BorderStyle = BorderStyle.FixedSingle;
   txtLeft.BackColor = Color.LightBlue;
  }

  public ctlText()
  {
   InitializeComponent();

   ContextMenu mnuContextMenu = new ContextMenu();
   txtLeft.ContextMenu = mnuContextMenu;
   mnuContextMenu.MenuItems.Add(new MenuItem("Primary Key"));
   mnuContextMenu.MenuItems[0].Checked = false;
   mnuContextMenu.MenuItems[0].Click += mnuArrowClick;
   txtLeft.AutoCompleteCustomSource = new AutoCompleteStringCollection();
   txtRight.AutoCompleteCustomSource = new AutoCompleteStringCollection();
   EditMode = false;
  }

  private void ctlText_SizeChanged(object sender, EventArgs e)
  {
   EditMode = false;
   txtLeft.Height = Height - 2;
   txtRight.Height = Height - 2;
   lblLeft.Height = Height - 2;
   lblRight.Height = Height - 2;
   if (extended) { txtLeft.Width = ((int)(Width / 2.5)); txtRight.Width = Width - txtLeft.Width - 3; } else { txtLeft.Width = Width; txtRight.Width = Width; }
   txtLeft.Location = new Point(0, 0); txtRight.Location = new Point(txtLeft.Width + 3, 0);
   adjustLabels();
  }


  private void txtLeft_TextChanged(object sender, EventArgs e)  {lblLeft.Text = txtLeft.Text.Trim();    adjustLabels(); if (TextChanged != null) TextChanged(this);}
  private void txtRight_TextChanged(object sender, EventArgs e) {lblRight.Text = txtRight.Text.Trim();  adjustLabels(); if (TextChanged != null) TextChanged(this);}

  private void lblLeft_MouseEnter(object sender, EventArgs e)   {EditMode = true; txtLeft.Select(1, txtLeft.Text.Length);   }
  private void lblRight_MouseEnter(object sender, EventArgs e)  {EditMode = true; txtRight.Select(1, txtRight.Text.Length); }

  private void txtLeft_Leave(object sender, EventArgs e)        {return; if (this.ActiveControl != txtRight) EditMode = false; }
  private void txtRight_Leave(object sender, EventArgs e)       {return; if (this.ActiveControl != txtLeft) EditMode = false; }
  private void ctlText_Leave(object sender, EventArgs e)        {EditMode = false; }

 }
 
 
}












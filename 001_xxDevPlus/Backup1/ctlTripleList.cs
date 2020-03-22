

using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Drawing;
using System.Data;
using System.Linq;
using System.Text;
using System.Windows.Forms;

using ndBase;
using ndString;

namespace ndData
{

 public partial class ctlTripleList : ctlCanvas
 {
  //Copyright© 1996-2012 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties, You may use, modify, distribute this code under GNU public licence for NON-Commercial use as long as this notice remains unchanged.
  private static bool selfTested  = false; private static string ass(bool expr) { if (!expr) throw new Exception(ass(true) + " SelfTest Failure"); return "ctlTripleList"; } private static void selfTest() { selfTested = true; } private void init() { if (!selfTested) selfTest(); }

  protected double                        leftSpace          = 0.3;  //percentage
  protected long                          midWidth           = 30;   //pixel

  protected bool                          lEdit              = false;
  protected bool                          mEdit              = false;
  protected bool                          rEdit              = false;
  protected Control                       mouseDownControl   = null;
  public    Pile<ctlString>               lText              = new Pile<ctlString>();
  public    Pile<ctlString>               mText              = new Pile<ctlString>();
  public    Pile<ctlString>               rText              = new Pile<ctlString>();
  public    Pile<Pile<Control>>           oTrans             = new Pile<Pile<Control>>();    //should be a Pile<Pile<ctlArrowTerminal>>
  public    Pile<Control>                 iTrans             = new Pile<Control>();          //Pile<ctlArrowTerminal>
  protected ContextMenu                   lTextMenu          = new ContextMenu();
  protected ContextMenu                   mTextMenu          = new ContextMenu();
  protected ContextMenu                   rTextMenu          = new ContextMenu();
  protected Pile<string>                  condition          = new Pile<string>();
  protected ctlTripleList                 tableDef           = null;
  public    string                        Title              { get { return ddHandle.Text; } }

  protected virtual void reload() { BackColor = Color.FromArgb(190, 190, 190); }

  protected virtual void properView()
  {
   for (int i = 1; i <= lText.Len; i++) if ((lText[i].Text.Trim().Length == 0) ^ (rText[i].Text.Trim().Length == 0)) { /*mText[i].Text = "==";  */}
   condition = new Pile<string>();
   for (int i = 1; i <= lText.Len; i++) if (mText[i].Text.Trim().Length != 0) condition.Push(lText[i].Text + " = " + rText[i].Text);
   for (int i = 1; i <= lText.Len; i++) if (i <= condition.Len) { string cond = condition[i]; lText[i].Text = utl.cutl(ref cond, " = "); rText[i].Text = utl.cutl(ref cond, " = "); mText[i].Text = " = "; }  else { lText[i].Text = ""; rText[i].Text = ""; mText[i].Text = ""; }
  }
  
  public ctlTripleList TableDef { get { return tableDef; } set { tableDef = value; } }

  public ctlTripleList() : base() { Size = new Size(200, 60); InitializeComponent(); lTmpl.Visible = false; mTmpl.Visible = false; rTmpl.Visible = false; }

  public ctlTripleList(ctx cx, string editMode) : base(cx)
  {
   lEdit = editMode.Substring(0).StartsWith("1");
   mEdit = editMode.Substring(1).StartsWith("1");
   rEdit = editMode.Substring(2).StartsWith("1");
   InitializeComponent(); lTmpl.Visible = false; mTmpl.Visible = false; rTmpl.Visible = false;
   utl.setSize(this, 200, 60);
   Visible = false;
   ((Control)cx.Param["Canvas"]).Controls.Add(this);
   Initialized = true;
   if (cx.Param.hasKey("LeftMenu"))  utl.cloneMenu(cx.Name, ((ContextMenu)cx.Param["LeftMenu"]),  lTextMenu, lTextMenu_Click);
   if (cx.Param.hasKey("MidMenu"))   utl.cloneMenu(cx.Name, ((ContextMenu)cx.Param["MidMenu"]),   mTextMenu, mTextMenu_Click);
   if (cx.Param.hasKey("RightMenu")) utl.cloneMenu(cx.Name, ((ContextMenu)cx.Param["RightMenu"]), rTextMenu, rTextMenu_Click);
   utl.setControlFont(lTmpl, cx.DbFont);
   utl.setControlFont(mTmpl, cx.Font);
   utl.setControlFont(rTmpl, cx.TsFont);
   btnClose.BringToFront();
   reload();
  }

  public override void chgLeft(long delta)
  {
   base.chgLeft(delta);
   for (int j = 1; j <= iTrans.Len; j++) if (iTrans[j] != null) utl.chgLeft(iTrans[j], (int)delta);
   for (int i = 1; i <= oTrans.Len; i++) for (int j = 1; j <= oTrans[i].Len; j++) if (oTrans[i][j] != null) utl.chgLeft(oTrans[i][j], (int)delta);
  }

  public override void chgTop(long delta)
  {
   base.chgTop(delta);
   for (int j = 1; j <= iTrans.Len; j++) if (iTrans[j] != null) utl.chgTop(iTrans[j], (int)delta);
   for (int i = 1; i <= oTrans.Len; i++) for (int j = 1; j <= oTrans[i].Len; j++) if (oTrans[i][j] != null) utl.chgTop(oTrans[i][j], (int)delta);
  }

  public override void chgWidth(long delta)
  {
   base.chgWidth(delta);
   for (int i = 1; i <= oTrans.Len; i++) for (int j = 1; j <= oTrans[i].Len; j++) if (oTrans[i][j] != null) utl.chgLeft(oTrans[i][j], (int)delta);
  }

  public override void chgHeight(long delta)
  {
   base.chgHeight(delta);
  }

  public override void chgLocation(long dx, long dy)
  {
   base.chgLocation(dx, dy);
   for (int j = 1; j <= iTrans.Len; j++) if (iTrans[j] != null) utl.chgLocation(iTrans[j], (int)dx, (int)dy);
   for (int i = 1; i <= oTrans.Len; i++) for (int j = 1; j <= oTrans[i].Len; j++) if (oTrans[i][j] != null) utl.chgLocation(oTrans[i][j], (int)dx, (int)dy);
  }

  public override void chgSize(long dw, long dh)
  {
   base.chgSize(dw, dh);
   for (int i = 1; i <= oTrans.Len; i++) for (int j = 1; j <= oTrans[i].Len; j++) if (oTrans[i][j] != null) utl.chgSize(oTrans[i][j], (int)dw, (int)dh);
  }

  protected virtual void delLine(int inx)
  {
   for (int i = inx; i < lText.Len; i++) { lText[i].Text = lText[i + 1].Text; mText[i].Text = mText[i + 1].Text; rText[i].Text = rText[i + 1].Text; oTrans[i] = oTrans[i + 1]; for (int m = 1; m <= oTrans[i].Len; m++) if (oTrans[i][m] != null) oTrans[i][m].Top -= lText[1].Height; }
   lText[-1].Text = "";
   mText[-1].Text = "";
   rText[-1].Text = "";
   oTrans[-1] = new Pile<Control>();
  }

  protected virtual void pushLine()
  {
   lText.Push             ( new ctlString(lEdit));                   mText.Push             ( new ctlString(mEdit));                   rText.Push               ( new ctlString(rEdit));

   //lText[-1].ReadOnly     = !lEdit;                                  mText[-1].ReadOnly     = !mEdit;                                  rText[-1].ReadOnly     = !rEdit;                                   
   lText[-1].ContextMenu  = lTextMenu;                               mText[-1].ContextMenu  = mTextMenu;                               rText[-1].ContextMenu    = rTextMenu; 
   utl.cpyHeight(lText[-1], lTmpl);                                  utl.cpyHeight(mText[-1], mTmpl);                                  utl.cpyHeight(rText[-1], rTmpl);
   lText[-1].Font         = lTmpl.Font;                              mText[-1].Font         = mTmpl.Font;                              rText[-1].Font           = rTmpl.Font;
   //lText[-1].TextAlign    = HorizontalAlignment.Center;              mText[-1].TextAlign    = HorizontalAlignment.Center;              rText[-1].TextAlign      = HorizontalAlignment.Center;              
   lText[-1].ForeColor    = lTmpl.ForeColor;                         mText[-1].ForeColor    = mTmpl.ForeColor;                         rText[-1].ForeColor      = rTmpl.ForeColor;
   lText[-1].BackColor    = lTmpl.BackColor;                         mText[-1].BackColor    = mTmpl.BackColor;                         rText[-1].BackColor      = rTmpl.BackColor;
   lText[-1].BorderStyle  = lTmpl.BorderStyle;                       mText[-1].BorderStyle  = mTmpl.BorderStyle;                       rText[-1].BorderStyle    = rTmpl.BorderStyle;
   lText[-1].AllowDrop    = lTmpl.AllowDrop;                         mText[-1].AllowDrop    = mTmpl.AllowDrop;                         rText[-1].AllowDrop      = rTmpl.AllowDrop;

   lText[-1].DragDrop    += lText_DragDrop;                          mText[-1].DragDrop    += mText_DragDrop;                          rText[-1].DragDrop      += rText_DragDrop; 
   lText[-1].DragOver    += lText_DragOver;                          mText[-1].DragOver    += mText_DragOver;                          rText[-1].DragOver      += rText_DragOver;  
   lText[-1].MouseMove   += lText_MouseMove;                         mText[-1].MouseMove   += mText_MouseMove;                         rText[-1].MouseMove     += rText_MouseMove;
   lText[-1].MouseDown   += lText_MouseDown;                         mText[-1].MouseDown   += mText_MouseDown;                         rText[-1].MouseDown     += rText_MouseDown;
   lText[-1].DoubleClick += lText_DoubleClick;                       mText[-1].DoubleClick += mText_DoubleClick;     
   lText[-1].TextChanged += lText_TextChanged;                       mText[-1].TextChanged += mText_TextChanged;                       rText[-1].TextChanged   += rText_TextChanged;  

   fme.Controls.Add       ( lText[-1]);                              fme.Controls.Add       ( mText[-1]);                              fme.Controls.Add         ( rText[-1]);
   lText[-1].Visible      = false;                                   mText[-1].Visible      = false;                                   rText[-1].Visible      = false;                                        
   lText[-1].Visible      = true;                                    mText[-1].Visible      = true;                                    rText[-1].Visible      = true;                                        
   lText[-1].BringToFront ();                                        mText[-1].SendToBack();                                           rText[-1].BringToFront ();
   oTrans.Push(new Pile<Control>());
   if (lText[-1].Text.Length > 0)  mText[-1].Text = "=";
  }

  protected virtual void popLine() { fme.Controls.Remove(lText[-1]); fme.Controls.Remove(mText[-1]); fme.Controls.Remove(rText[-1]); lText.Pop(); mText.Pop(); rText.Pop(); oTrans.Pop(); }


  protected override void OnResize()
  {
   base.OnResize();
   //if (!Initialized) return;
   utl.setLocation(fme, 1, 1); utl.setSize(fme, Width - 2, Height - 2);
   long lWidth = (long)((fme.Width - midWidth) * leftSpace);
   long rWidth = fme.Width - midWidth - lWidth;
   ddHandle.Parent = fme;
   ddHandle.SendToBack();
   ddHandle.Anchor = AnchorStyles.None;
   ddHandle.Location = new Point(-1, -1);
   btnClose.Location = new Point(fme.Width - btnClose.Width, -1);
   utl.setHeight(ddHandle, 22);
   utl.setWidth(ddHandle, btnClose.Left + 2);
   //ddHandle.BringToFront();   // in erlier Version ddHandle was contained in fme! ddHandle.SendToBack();
   long count = (long)((fme.Height - 3 - ddHandle.Height) / (lTmpl.Height)) - lText.Len + 1;
   if (count < 0) for (long i = 1; i <= -count; i++) popLine(); else for (long i = 1; i <= count; i++) pushLine();
   for (int i = 1; i <= lText.Len; i++)
   {
    utl.cpyHeight(lText[i], lTmpl); utl.cpyHeight(mText[i], mTmpl); utl.cpyHeight(rText[i], rTmpl);
    utl.setWidth(lText[i], (int)lWidth - 2); utl.setWidth(mText[i], 2 * fme.Width); utl.setWidth(rText[i], (int)rWidth - 2);
    lText[i].Location = new Point(2, ddHandle.Height + (lTmpl.Height) * (int)(i - 1));
    //mText[i].Location = new Point((int)midWidth / 2 + (int)lWidth - fme.Width, ddHandle.Height + (lTmpl.Height) * (int)(i - 1));
    rText[i].Location = new Point((int)lWidth + (int)midWidth, ddHandle.Height + (lTmpl.Height) * (int)(i - 1));
    utl.setLocation(mText[i], lText[i].Width + 3, ddHandle.Height + (lTmpl.Height) * (int)(i - 1));
    utl.setSize(mText[i], (int)midWidth - 2, lText[i].Height);
    lText[i].BringToFront();
    rText[i].BringToFront();
   }
  }

  
  protected virtual void control_Load          (object sender,      EventArgs e) { Width = Width + 1; }
  protected virtual void btnClose_Click        (object sender,      EventArgs e) { Visible = false;   } //parentForm.RemoveTable(this); }

  protected virtual void lTextMenu_Click       (object sender,      EventArgs e) { ((ctlString)mouseDownControl).Text = ((MenuItem)sender).Text; } // private void lTextMenu_Click(object sender, EventArgs e) { ((frmSimpleGui)cx.Param["ParentForm"]).setDraggedNode(lblName.Text, ((MenuItem)sender).Text); lText_OnDragDrop(mouseDownControl); }
  protected virtual void mTextMenu_Click       (object sender,      EventArgs e) { ((ctlString)mouseDownControl).Text = ((MenuItem)sender).Text; }
  protected virtual void rTextMenu_Click       (object sender,      EventArgs e) { ((ctlString)mouseDownControl).Text = ((MenuItem)sender).Text; } // private void rTextMenu_Click(object sender, EventArgs e) { ((frmSimpleGui)cx.Param["ParentForm"]).setDraggedNode(lblName.Text, ((MenuItem)sender).Text); rText_OnDragDrop(mouseDownControl); }

  protected virtual void lText_OnDragDrop      (Control                  sender) { }
  protected virtual void mText_OnDragDrop      (Control                  sender) { }
  protected virtual void rText_OnDragDrop      (Control                  sender) { }

  protected virtual void lText_DragDrop        (object sender,  DragEventArgs e) { lText_OnDragDrop((Control)sender); }
  protected virtual void mText_DragDrop        (object sender,  DragEventArgs e) { mText_OnDragDrop((Control)sender); }
  protected virtual void rText_DragDrop        (object sender,  DragEventArgs e) { rText_OnDragDrop((Control)sender); }

  protected virtual void lText_DragOver        (object sender,  DragEventArgs e) 
  {
   string x = "";
  }
  
  protected virtual void mText_DragOver        (object sender,  DragEventArgs e) 
  {
   string x = "";
  }
  
  protected virtual void rText_DragOver        (object sender,  DragEventArgs e) 
  {
   string x = "";
  }

  protected virtual void lText_MouseMove       (object sender, MouseEventArgs e) { }
  protected virtual void mText_MouseMove       (object sender, MouseEventArgs e) { }
  protected virtual void rText_MouseMove       (object sender, MouseEventArgs e) { }

  protected virtual void lText_MouseDown       (object sender, MouseEventArgs e) 
  { 
   mouseDownControl = (((Control)sender).Parent.GetType() == typeof(ctlString)) ? ((Control)sender).Parent : (Control)sender; 
  }
  protected virtual void mText_MouseDown       (object sender, MouseEventArgs e) { mouseDownControl = (((Control)sender).Parent.GetType() == typeof(ctlString)) ? ((Control)sender).Parent : (Control)sender; }
  protected virtual void rText_MouseDown       (object sender, MouseEventArgs e) { mouseDownControl = (((Control)sender).Parent.GetType() == typeof(ctlString)) ? ((Control)sender).Parent : (Control)sender; }

  protected virtual void lText_DoubleClick     (object sender,      EventArgs e) { }
  protected virtual void mText_DoubleClick     (object sender,      EventArgs e) { }
  protected virtual void rText_DoubleClick     (object sender,      EventArgs e) { }

  protected virtual void lText_TextChanged     (object sender,      EventArgs e) { }
  protected virtual void mText_TextChanged     (object sender,      EventArgs e) { }
  protected virtual void rText_TextChanged     (object sender,      EventArgs e) { }

  protected virtual void xTextMenu_Click       (object sender,      EventArgs e)
  {
   /*
   if (((MenuItem)sender).Text.Equals("view/edit definition>"))
   {
    editField((ctlString)mouseDownControl);
    return;
   }
   */
   //((frmPublisher)cx.Param["ParentForm"]).setDraggedNode("", ddHandle.Text, ((MenuItem)sender).Text);
   if (mouseDownControl.Left == lText[1].Left) lTextMenu_Click(sender, e);
   if (mouseDownControl.Left == mText[1].Left) mTextMenu_Click(sender, e);
   if (mouseDownControl.Left == rText[1].Left) rTextMenu_Click(sender, e);
  }


 }
}







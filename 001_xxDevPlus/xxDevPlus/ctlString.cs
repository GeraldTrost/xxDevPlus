using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Drawing;
using System.Data;
using System.Linq;
using System.Text;
using System.Windows.Forms;


using org_xxdevplus_sys;
using org_xxdevplus_utl;
using org_xxdevplus_struct;
using org_xxdevplus_chain;

namespace org_xxdevplus_gui
{
 public partial class ctlString : UserControl
 {

  public event DragEventHandler    DragOver;
  public event DragEventHandler    DragDrop;
  public event MouseEventHandler   MouseMove;
  public event MouseEventHandler   MouseDown;
  public event EventHandler        DoubleClick;
  public event EventHandler        TextChanged;

  private bool closeBox = false;

  public ctlString(bool edit, string text, bool closeBox) { InitializeComponent(); this.closeBox = closeBox; Text = text; utl.setSize(this, lblString.Width, lblString.Height); if (edit) { lblString.Visible = false; txtString.Visible = true; } else { lblString.Visible = true; txtString.Visible = false; } }
  public ctlString(bool edit, string text) : this(edit, text, false) { }
  public ctlString(bool edit) : this(edit, "", false) { }
  public ctlString() : this(true, "", false) { }

  private void control_Resize(object sender, EventArgs e) { utl.setLocation(lblString, 0, 0); utl.setSize(lblString, Width, Height); utl.setLocation(txtString, 0, 0); utl.setSize(txtString, Width, Height); }

  public          ContentAlignment TextAlign   { get { return (ContentAlignment)txtString.TextAlign                                           ; } set { txtString.TextAlign = (HorizontalAlignment)value; lblString.TextAlign = value    ; } }
  public override string           Text        { get { if (txtString.Visible) return txtString.Text; else return lblString.Text               ; } set { txtString.Text = value; lblString.Text = value                                   ; } }
  public override Font             Font        { get { if (txtString.Visible) return txtString.Font; else return lblString.Font               ; } set { txtString.Font = value; lblString.Font = value                                   ; } }
  public override Color            ForeColor   { get { if (txtString.Visible) return txtString.ForeColor; else return lblString.ForeColor     ; } set { txtString.ForeColor = value; lblString.ForeColor = value                         ; } }
  public override Color            BackColor   { get { if (txtString.Visible) return txtString.BackColor; else return lblString.BackColor     ; } set { txtString.BackColor = value; lblString.BackColor = value                         ; } }
  public          BorderStyle      BorderStyle { get { if (txtString.Visible) return txtString.BorderStyle; else return lblString.BorderStyle ; } set { txtString.BorderStyle = value; lblString.BorderStyle = value                     ; } }


  /*
  private void txtString_DragOver(object sender, DragEventArgs e) { e.Effect = DragDropEffects.Copy; RaiseDragEvent(this, e); }
  private void lblString_DragOver(object sender, DragEventArgs e) { e.Effect = DragDropEffects.Copy; RaiseDragEvent(this, e); }
  private void lblString_DragDrop(object sender, DragEventArgs e) { RaiseDragEvent(this, e); }
  private void txtString_DragDrop(object sender, DragEventArgs e) { RaiseDragEvent(this, e); }
  */

  private void control_VisibleChanged(object sender, EventArgs e)
  {
   /*
   lblString.DragOver    += new DragEventHandler   (this.control_DragOver);
   lblString.DragDrop    += new DragEventHandler   (this.control_DragDrop);
   lblString.MouseMove   += new MouseEventHandler  (this.control_MouseMove);
   lblString.MouseDown   += new MouseEventHandler  (this.control_MouseDown);
   lblString.DoubleClick += new EventHandler       (this.control_DoubleClick);
   txtString.DragOver    += new DragEventHandler   (this.control_DragOver);
   txtString.DragDrop    += new DragEventHandler   (this.control_DragDrop);
   txtString.MouseMove   += new MouseEventHandler  (this.control_MouseMove);
   txtString.MouseDown   += new MouseEventHandler  (this.control_MouseDown);
   txtString.DoubleClick += new EventHandler       (this.control_DoubleClick);
   */

   base.DragOver           += DragOver;
   base.DragDrop           += DragDrop;
   base.MouseMove          += MouseMove;
   base.MouseDown          += MouseDown;
   base.DoubleClick        += DoubleClick;
   base.TextChanged        += TextChanged;

   lblString.DragOver      += DragOver;
   lblString.DragDrop      += DragDrop;
   lblString.MouseMove     += MouseMove;
   lblString.MouseDown     += MouseDown;
   lblString.DoubleClick   += DoubleClick;
   lblString.TextChanged   += TextChanged;

   txtString.DragOver      += DragOver;
   txtString.DragDrop      += DragDrop;
   txtString.MouseMove     += MouseMove;
   txtString.MouseDown     += MouseDown;
   txtString.DoubleClick   += DoubleClick;
   txtString.TextChanged   += TextChanged;

   /*
   private void control_DragOver          (object sender, DragEventArgs e)  { }
   private void control_DragDrop          (object sender, DragEventArgs e)  { }
   private void control_MouseMove         (object sender, MouseEventArgs e) { }
   private void control_MouseDown         (object sender, MouseEventArgs e) { }
   private void control_DoubleClick       (object sender, EventArgs e)      { }
   */
  }

  private void ctlString_MouseEnter(object sender, EventArgs e)
  {

   base.DragOver           += DragOver;
   base.DragDrop           += DragDrop;
   base.MouseMove          += MouseMove;
   base.MouseDown          += MouseDown;
   base.DoubleClick        += DoubleClick;
   base.TextChanged        += TextChanged;

   lblString.DragOver      += DragOver;
   lblString.DragDrop      += DragDrop;
   lblString.MouseMove     += MouseMove;
   lblString.MouseDown     += MouseDown;
   lblString.DoubleClick   += DoubleClick;
   lblString.TextChanged   += TextChanged;

   txtString.DragOver      += DragOver;
   txtString.DragDrop      += DragDrop;
   txtString.MouseMove     += MouseMove;
   txtString.MouseDown     += MouseDown;
   txtString.DoubleClick   += DoubleClick;
   txtString.TextChanged   += TextChanged;

  }

  //public bool             AllowDrop       { get { if (txtString.Visible) return txtString.AllowDrop; else return lblString.AllowDrop     ; } set { txtString.AllowDrop = value; lblString.AllowDrop = value                         ; } }

 }
}

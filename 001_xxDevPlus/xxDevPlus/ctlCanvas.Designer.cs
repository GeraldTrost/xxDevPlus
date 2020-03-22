
//** @author  GeTr Copyright© 1996-2020 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties
//** @license You may use, modify, distribute this code under GNU public dual licence free for NON-Commercial use as long as this notice remains unchanged.
//** @comment Draggable Canvas Control


namespace org_xxdevplus_gui
{
 public partial class ctlCanvas
 {
  /// <summary> 
  /// Required designer variable.
  /// </summary>
  private System.ComponentModel.IContainer components = null;

  /// <summary> 
  /// Clean up any resources being used.
  /// </summary>
  /// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
  protected override void Dispose(bool disposing)
  {
   if (disposing && (components != null))
   {
    components.Dispose();
   }
   base.Dispose(disposing);
  }

  #region Component Designer generated code

  /// <summary> 
  /// Required method for Designer support - do not modify 
  /// the contents of this method with the code editor.
  /// </summary>

  private void InitializeComponent()
  {
   this.ddHandle = new System.Windows.Forms.Label();
   this.SuspendLayout();
   // 
   // ddHandle
   // 
   this.ddHandle.Anchor = System.Windows.Forms.AnchorStyles.None;
   this.ddHandle.BackColor = System.Drawing.SystemColors.ButtonShadow;
   this.ddHandle.BorderStyle = System.Windows.Forms.BorderStyle.Fixed3D;
   this.ddHandle.Font = new System.Drawing.Font("Tahoma", 9.75F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
   this.ddHandle.ForeColor = System.Drawing.SystemColors.HighlightText;
   this.ddHandle.Location = new System.Drawing.Point(14, 0);
   this.ddHandle.Name = "ddHandle";
   this.ddHandle.Size = new System.Drawing.Size(256, 22);
   this.ddHandle.TabIndex = 61;
   this.ddHandle.Text = "ddHandle";
   this.ddHandle.TextAlign = System.Drawing.ContentAlignment.MiddleCenter;
   this.ddHandle.MouseLeave += new System.EventHandler(this.ddHandle_MouseLeave);
   this.ddHandle.DoubleClick += new System.EventHandler(this.ddHandle_DoubleClick);
   this.ddHandle.MouseMove += new System.Windows.Forms.MouseEventHandler(this.ddHandle_MouseMove);
   this.ddHandle.Click += new System.EventHandler(this.ddHandle_Click);
   this.ddHandle.MouseDown += new System.Windows.Forms.MouseEventHandler(this.ddHandle_MouseDown);
   // 
   // ctlCanvas
   // 
   this.BackgroundImageLayout = System.Windows.Forms.ImageLayout.None;
   this.Controls.Add(this.ddHandle);
   this.Name = "ctlCanvas";
   this.Size = new System.Drawing.Size(320, 150);
   this.LocationChanged += new System.EventHandler(this.control__LocationChanged);
   this.DoubleClick += new System.EventHandler(this.control_DoubleClick);
   this.MouseLeave += new System.EventHandler(this.control_MouseLeave);
   this.MouseMove += new System.Windows.Forms.MouseEventHandler(this.control_MouseMove);
   this.MouseDown += new System.Windows.Forms.MouseEventHandler(this.control_MouseDown);
   this.Resize += new System.EventHandler(this.control_Resize);
   this.MouseUp += new System.Windows.Forms.MouseEventHandler(this.control_MouseUp);
   this.ResumeLayout(false);

  }


  #endregion

  protected System.Windows.Forms.Label ddHandle;

 }
}



//** @author  GeTr Copyright© 1996-2020 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties
//** @license You may use, modify, distribute this code under GNU public dual licence free for NON-Commercial use as long as this notice remains unchanged.
//** @comment String Editor Control

using org_xxdevplus_sys;
using org_xxdevplus_utl;
using org_xxdevplus_struct;
using org_xxdevplus_chain;


namespace org_xxdevplus_gui
{
 partial class ctlString
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
   this.lblString = new System.Windows.Forms.Label();
   this.txtString = new System.Windows.Forms.TextBox();
   this.SuspendLayout();
   // 
   // lblString
   // 
   this.lblString.AllowDrop = true;
   this.lblString.BackColor = System.Drawing.Color.WhiteSmoke;
   this.lblString.Location = new System.Drawing.Point(3, 3);
   this.lblString.Name = "lblString";
   this.lblString.Size = new System.Drawing.Size(13, 13);
   this.lblString.TabIndex = 0;
   this.lblString.TextAlign = System.Drawing.ContentAlignment.MiddleCenter;
   // 
   // txtString
   // 
   this.txtString.AllowDrop = true;
   this.txtString.AutoCompleteCustomSource.AddRange(new string[] {
            " "});
   this.txtString.AutoCompleteMode = System.Windows.Forms.AutoCompleteMode.SuggestAppend;
   this.txtString.AutoCompleteSource = System.Windows.Forms.AutoCompleteSource.CustomSource;
   this.txtString.BackColor = System.Drawing.Color.WhiteSmoke;
   this.txtString.BorderStyle = System.Windows.Forms.BorderStyle.None;
   this.txtString.Font = new System.Drawing.Font("Arial Narrow", 8.25F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
   this.txtString.Location = new System.Drawing.Point(42, 2);
   this.txtString.Name = "txtString";
   this.txtString.Size = new System.Drawing.Size(62, 13);
   this.txtString.TabIndex = 52;
   this.txtString.TextAlign = System.Windows.Forms.HorizontalAlignment.Center;
   // 
   // ctlString
   // 
   this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
   this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
   this.Controls.Add(this.txtString);
   this.Controls.Add(this.lblString);
   this.Name = "ctlString";
   this.Size = new System.Drawing.Size(128, 23);
   this.VisibleChanged += new System.EventHandler(this.control_VisibleChanged);
   this.Resize += new System.EventHandler(this.control_Resize);
   this.MouseEnter += new System.EventHandler(this.ctlString_MouseEnter);
   this.ResumeLayout(false);
   this.PerformLayout();

  }

  #endregion

  private System.Windows.Forms.Label lblString;
  private System.Windows.Forms.TextBox txtString;
 }
}

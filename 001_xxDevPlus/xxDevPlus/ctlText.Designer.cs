
//** @author  GeTr Copyright© 1996-2020 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties
//** @license You may use, modify, distribute this code under GNU public dual licence free for NON-Commercial use as long as this notice remains unchanged.
//** @comment Text Editor Control



namespace org_xxdevplus_gui
{
 partial class ctlText
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
   this.lblLeft = new System.Windows.Forms.Label();
   this.lblRight = new System.Windows.Forms.Label();
   this.txtLeft = new System.Windows.Forms.TextBox();
   this.txtRight = new System.Windows.Forms.TextBox();
   this.SuspendLayout();
   // 
   // lblLeft
   // 
   this.lblLeft.BackColor = System.Drawing.Color.WhiteSmoke;
   this.lblLeft.Font = new System.Drawing.Font("Arial Narrow", 8.25F);
   this.lblLeft.Location = new System.Drawing.Point(86, 0);
   this.lblLeft.MinimumSize = new System.Drawing.Size(40, 0);
   this.lblLeft.Name = "lblLeft";
   this.lblLeft.Size = new System.Drawing.Size(40, 16);
   this.lblLeft.TabIndex = 48;
   this.lblLeft.TextAlign = System.Drawing.ContentAlignment.MiddleCenter;
   this.lblLeft.MouseEnter += new System.EventHandler(this.lblLeft_MouseEnter);
   // 
   // lblRight
   // 
   this.lblRight.BackColor = System.Drawing.Color.WhiteSmoke;
   this.lblRight.Font = new System.Drawing.Font("Arial Narrow", 8.25F);
   this.lblRight.ForeColor = System.Drawing.Color.DarkRed;
   this.lblRight.Location = new System.Drawing.Point(132, 0);
   this.lblRight.MinimumSize = new System.Drawing.Size(40, 0);
   this.lblRight.Name = "lblRight";
   this.lblRight.Size = new System.Drawing.Size(40, 16);
   this.lblRight.TabIndex = 49;
   this.lblRight.TextAlign = System.Drawing.ContentAlignment.MiddleCenter;
   this.lblRight.MouseEnter += new System.EventHandler(this.lblRight_MouseEnter);
   // 
   // txtLeft
   // 
   this.txtLeft.AutoCompleteCustomSource.AddRange(new string[] {
            " "});
   this.txtLeft.AutoCompleteMode = System.Windows.Forms.AutoCompleteMode.SuggestAppend;
   this.txtLeft.AutoCompleteSource = System.Windows.Forms.AutoCompleteSource.CustomSource;
   this.txtLeft.BackColor = System.Drawing.Color.WhiteSmoke;
   this.txtLeft.BorderStyle = System.Windows.Forms.BorderStyle.None;
   this.txtLeft.Font = new System.Drawing.Font("Arial Narrow", 8.25F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
   this.txtLeft.Location = new System.Drawing.Point(19, 30);
   this.txtLeft.Name = "txtLeft";
   this.txtLeft.ReadOnly = true;
   this.txtLeft.Size = new System.Drawing.Size(107, 13);
   this.txtLeft.TabIndex = 51;
   this.txtLeft.TextChanged += new System.EventHandler(this.txtLeft_TextChanged);
   this.txtLeft.Leave += new System.EventHandler(this.txtLeft_Leave);
   // 
   // txtRight
   // 
   this.txtRight.AutoCompleteCustomSource.AddRange(new string[] {
            " "});
   this.txtRight.AutoCompleteMode = System.Windows.Forms.AutoCompleteMode.SuggestAppend;
   this.txtRight.AutoCompleteSource = System.Windows.Forms.AutoCompleteSource.CustomSource;
   this.txtRight.BackColor = System.Drawing.Color.WhiteSmoke;
   this.txtRight.BorderStyle = System.Windows.Forms.BorderStyle.None;
   this.txtRight.Font = new System.Drawing.Font("Arial Narrow", 8.25F);
   this.txtRight.ForeColor = System.Drawing.Color.DarkRed;
   this.txtRight.Location = new System.Drawing.Point(132, 30);
   this.txtRight.Name = "txtRight";
   this.txtRight.ReadOnly = true;
   this.txtRight.Size = new System.Drawing.Size(107, 13);
   this.txtRight.TabIndex = 52;
   this.txtRight.TextChanged += new System.EventHandler(this.txtRight_TextChanged);
   this.txtRight.Leave += new System.EventHandler(this.txtRight_Leave);
   // 
   // ctlText
   // 
   this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
   this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
   this.Controls.Add(this.txtRight);
   this.Controls.Add(this.txtLeft);
   this.Controls.Add(this.lblRight);
   this.Controls.Add(this.lblLeft);
   this.Name = "ctlText";
   this.Size = new System.Drawing.Size(242, 63);
   this.Leave += new System.EventHandler(this.ctlText_Leave);
   this.SizeChanged += new System.EventHandler(this.ctlText_SizeChanged);
   this.ResumeLayout(false);
   this.PerformLayout();

  }

  #endregion

  private System.Windows.Forms.Label lblLeft;
  private System.Windows.Forms.Label lblRight;
  private System.Windows.Forms.TextBox txtLeft;
  private System.Windows.Forms.TextBox txtRight;
 }
}

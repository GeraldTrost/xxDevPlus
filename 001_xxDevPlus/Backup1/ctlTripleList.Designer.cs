namespace ndData
{
 partial class ctlTripleList
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
   System.ComponentModel.ComponentResourceManager resources = new System.ComponentModel.ComponentResourceManager(typeof(ctlTripleList));
   this.fme = new System.Windows.Forms.Panel();
   this.btnClose = new System.Windows.Forms.Button();
   this.rTmpl = new System.Windows.Forms.TextBox();
   this.mTmpl = new System.Windows.Forms.Label();
   this.lTmpl = new System.Windows.Forms.Label();
   this.fme.SuspendLayout();
   this.SuspendLayout();
   // 
   // ddHandle
   // 
   this.ddHandle.Location = new System.Drawing.Point(-1, -1);
   this.ddHandle.Size = new System.Drawing.Size(475, 22);
   // 
   // fme
   // 
   this.fme.Anchor = System.Windows.Forms.AnchorStyles.None;
   this.fme.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(255)))), ((int)(((byte)(255)))), ((int)(((byte)(192)))));
   this.fme.BackgroundImageLayout = System.Windows.Forms.ImageLayout.None;
   this.fme.Controls.Add(this.btnClose);
   this.fme.Controls.Add(this.rTmpl);
   this.fme.Controls.Add(this.mTmpl);
   this.fme.Controls.Add(this.lTmpl);
   this.fme.Location = new System.Drawing.Point(85, 67);
   this.fme.Name = "fme";
   this.fme.Size = new System.Drawing.Size(308, 76);
   this.fme.TabIndex = 55;
   // 
   // btnClose
   // 
   this.btnClose.BackgroundImage = ((System.Drawing.Image)(resources.GetObject("btnClose.BackgroundImage")));
   this.btnClose.FlatStyle = System.Windows.Forms.FlatStyle.Flat;
   this.btnClose.Location = new System.Drawing.Point(283, 0);
   this.btnClose.Name = "btnClose";
   this.btnClose.Size = new System.Drawing.Size(22, 22);
   this.btnClose.TabIndex = 62;
   this.btnClose.TabStop = false;
   this.btnClose.UseVisualStyleBackColor = true;
   this.btnClose.Click += new System.EventHandler(this.btnClose_Click);
   // 
   // rTmpl
   // 
   this.rTmpl.AutoCompleteCustomSource.AddRange(new string[] {
            " "});
   this.rTmpl.AutoCompleteMode = System.Windows.Forms.AutoCompleteMode.SuggestAppend;
   this.rTmpl.AutoCompleteSource = System.Windows.Forms.AutoCompleteSource.CustomSource;
   this.rTmpl.BackColor = System.Drawing.SystemColors.Window;
   this.rTmpl.BorderStyle = System.Windows.Forms.BorderStyle.None;
   this.rTmpl.Font = new System.Drawing.Font("Arial Narrow", 8.25F);
   this.rTmpl.ForeColor = System.Drawing.Color.Blue;
   this.rTmpl.Location = new System.Drawing.Point(129, 45);
   this.rTmpl.Name = "rTmpl";
   this.rTmpl.ReadOnly = true;
   this.rTmpl.Size = new System.Drawing.Size(38, 13);
   this.rTmpl.TabIndex = 59;
   this.rTmpl.TextAlign = System.Windows.Forms.HorizontalAlignment.Center;
   this.rTmpl.Visible = false;
   // 
   // mTmpl
   // 
   this.mTmpl.BackColor = System.Drawing.SystemColors.Window;
   this.mTmpl.Font = new System.Drawing.Font("Arial Narrow", 8.25F);
   this.mTmpl.ForeColor = System.Drawing.Color.FromArgb(((int)(((byte)(0)))), ((int)(((byte)(0)))), ((int)(((byte)(192)))));
   this.mTmpl.Location = new System.Drawing.Point(53, 40);
   this.mTmpl.MinimumSize = new System.Drawing.Size(40, 0);
   this.mTmpl.Name = "mTmpl";
   this.mTmpl.Size = new System.Drawing.Size(40, 18);
   this.mTmpl.TabIndex = 58;
   this.mTmpl.Text = "*";
   this.mTmpl.TextAlign = System.Drawing.ContentAlignment.MiddleCenter;
   this.mTmpl.Visible = false;
   // 
   // lTmpl
   // 
   this.lTmpl.AllowDrop = true;
   this.lTmpl.BackColor = System.Drawing.SystemColors.Window;
   this.lTmpl.Font = new System.Drawing.Font("Arial Narrow", 8.25F);
   this.lTmpl.ForeColor = System.Drawing.Color.FromArgb(((int)(((byte)(0)))), ((int)(((byte)(0)))), ((int)(((byte)(192)))));
   this.lTmpl.Location = new System.Drawing.Point(4, 40);
   this.lTmpl.MinimumSize = new System.Drawing.Size(40, 0);
   this.lTmpl.Name = "lTmpl";
   this.lTmpl.Size = new System.Drawing.Size(43, 18);
   this.lTmpl.TabIndex = 57;
   this.lTmpl.TextAlign = System.Drawing.ContentAlignment.MiddleCenter;
   this.lTmpl.Visible = false;
   // 
   // ctlTripleList
   // 
   this.AllowDrop = true;
   this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
   this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
   this.BackColor = System.Drawing.Color.LightGray;
   this.BackgroundImageLayout = System.Windows.Forms.ImageLayout.Zoom;
   this.Controls.Add(this.fme);
   this.Name = "ctlTripleList";
   this.Size = new System.Drawing.Size(473, 210);
   this.Load += new System.EventHandler(this.control_Load);
   this.fme.ResumeLayout(false);
   this.fme.PerformLayout();
   this.ResumeLayout(false);

  }

  #endregion

  protected System.Windows.Forms.Panel fme;
  protected System.Windows.Forms.TextBox rTmpl;
  protected System.Windows.Forms.Label mTmpl;
  protected System.Windows.Forms.Label lTmpl;
  protected System.Windows.Forms.Button btnClose;
 }
}

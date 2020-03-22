namespace ndData
{
 partial class frmSelectView
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

  #region Windows Form Designer generated code

  /// <summary>
  /// Required method for Designer support - do not modify
  /// the contents of this method with the code editor.
  /// </summary>
  private void InitializeComponent()
  {
   this.lstSelectView = new System.Windows.Forms.ListView();
   this.columnHeader2 = new System.Windows.Forms.ColumnHeader();
   this.SuspendLayout();
   // 
   // lstSelectView
   // 
   this.lstSelectView.AutoArrange = false;
   this.lstSelectView.BorderStyle = System.Windows.Forms.BorderStyle.FixedSingle;
   this.lstSelectView.Columns.AddRange(new System.Windows.Forms.ColumnHeader[] {
            this.columnHeader2});
   this.lstSelectView.Font = new System.Drawing.Font("Arial Unicode MS", 7F);
   this.lstSelectView.ForeColor = System.Drawing.Color.MediumBlue;
   this.lstSelectView.GridLines = true;
   this.lstSelectView.HeaderStyle = System.Windows.Forms.ColumnHeaderStyle.None;
   this.lstSelectView.HideSelection = false;
   this.lstSelectView.LabelEdit = true;
   this.lstSelectView.Location = new System.Drawing.Point(-1, -2);
   this.lstSelectView.MultiSelect = false;
   this.lstSelectView.Name = "lstSelectView";
   this.lstSelectView.ShowGroups = false;
   this.lstSelectView.Size = new System.Drawing.Size(601, 340);
   this.lstSelectView.TabIndex = 32;
   this.lstSelectView.UseCompatibleStateImageBehavior = false;
   this.lstSelectView.View = System.Windows.Forms.View.Details;
   this.lstSelectView.SizeChanged += new System.EventHandler(this.lstSelectView_SizeChanged);
   // 
   // columnHeader2
   // 
   this.columnHeader2.Width = 600;
   // 
   // frmSelectView
   // 
   this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
   this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
   this.ClientSize = new System.Drawing.Size(598, 337);
   this.Controls.Add(this.lstSelectView);
   this.Name = "frmSelectView";
   this.Text = "frmSelectView";
   this.SizeChanged += new System.EventHandler(this.frmSelectView_SizeChanged);
   this.ResumeLayout(false);

  }

  #endregion

  private System.Windows.Forms.ListView lstSelectView;
  private System.Windows.Forms.ColumnHeader columnHeader2;



 }
}
namespace ndData
{
 partial class frmDbConnect
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
   this.pupDbms = new System.Windows.Forms.ComboBox();
   this.label1 = new System.Windows.Forms.Label();
   this.label2 = new System.Windows.Forms.Label();
   this.label3 = new System.Windows.Forms.Label();
   this.label4 = new System.Windows.Forms.Label();
   this.txtInstance = new System.Windows.Forms.TextBox();
   this.txtDatabase = new System.Windows.Forms.TextBox();
   this.txtUser = new System.Windows.Forms.TextBox();
   this.btnTest = new System.Windows.Forms.Button();
   this.btnCancel = new System.Windows.Forms.Button();
   this.btnOk = new System.Windows.Forms.Button();
   this.txtPassword = new System.Windows.Forms.TextBox();
   this.label5 = new System.Windows.Forms.Label();
   this.label6 = new System.Windows.Forms.Label();
   this.pupMru = new System.Windows.Forms.ComboBox();
   this.label7 = new System.Windows.Forms.Label();
   this.pupDbTec = new System.Windows.Forms.ComboBox();
   this.colorDialog1 = new System.Windows.Forms.ColorDialog();
   this.colorDialog2 = new System.Windows.Forms.ColorDialog();
   this.colorDialog3 = new System.Windows.Forms.ColorDialog();
   this.colorDialog4 = new System.Windows.Forms.ColorDialog();
   this.txtTFilter = new System.Windows.Forms.TextBox();
   this.txtVFilter = new System.Windows.Forms.TextBox();
   this.label8 = new System.Windows.Forms.Label();
   this.SuspendLayout();
   // 
   // pupDbms
   // 
   this.pupDbms.AccessibleRole = System.Windows.Forms.AccessibleRole.None;
   this.pupDbms.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Left) 
            | System.Windows.Forms.AnchorStyles.Right)));
   this.pupDbms.BackColor = System.Drawing.Color.Snow;
   this.pupDbms.DropDownStyle = System.Windows.Forms.ComboBoxStyle.DropDownList;
   this.pupDbms.FlatStyle = System.Windows.Forms.FlatStyle.Flat;
   this.pupDbms.Font = new System.Drawing.Font("Arial Unicode MS", 8.25F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
   this.pupDbms.FormattingEnabled = true;
   this.pupDbms.Items.AddRange(new object[] {
            "{SQL Server}",
            "{Mysql ODBC 5.1 Driver}",
            "{Oracle in XE}"});
   this.pupDbms.Location = new System.Drawing.Point(125, 64);
   this.pupDbms.Name = "pupDbms";
   this.pupDbms.Size = new System.Drawing.Size(295, 23);
   this.pupDbms.TabIndex = 2;
   // 
   // label1
   // 
   this.label1.Location = new System.Drawing.Point(4, 13);
   this.label1.Name = "label1";
   this.label1.Size = new System.Drawing.Size(117, 18);
   this.label1.TabIndex = 26;
   this.label1.Text = "Recent Connections";
   this.label1.TextAlign = System.Drawing.ContentAlignment.MiddleRight;
   // 
   // label2
   // 
   this.label2.Location = new System.Drawing.Point(4, 95);
   this.label2.Name = "label2";
   this.label2.Size = new System.Drawing.Size(117, 18);
   this.label2.TabIndex = 27;
   this.label2.Text = "Host:Port/Instance";
   this.label2.TextAlign = System.Drawing.ContentAlignment.MiddleRight;
   // 
   // label3
   // 
   this.label3.Location = new System.Drawing.Point(4, 123);
   this.label3.Name = "label3";
   this.label3.Size = new System.Drawing.Size(117, 18);
   this.label3.TabIndex = 28;
   this.label3.Text = "Database[Schema]";
   this.label3.TextAlign = System.Drawing.ContentAlignment.MiddleRight;
   // 
   // label4
   // 
   this.label4.Location = new System.Drawing.Point(4, 150);
   this.label4.Name = "label4";
   this.label4.Size = new System.Drawing.Size(117, 18);
   this.label4.TabIndex = 29;
   this.label4.Text = "DbUser";
   this.label4.TextAlign = System.Drawing.ContentAlignment.MiddleRight;
   // 
   // txtInstance
   // 
   this.txtInstance.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Left) 
            | System.Windows.Forms.AnchorStyles.Right)));
   this.txtInstance.BackColor = System.Drawing.Color.Snow;
   this.txtInstance.BorderStyle = System.Windows.Forms.BorderStyle.None;
   this.txtInstance.Font = new System.Drawing.Font("Arial Unicode MS", 8.25F);
   this.txtInstance.Location = new System.Drawing.Point(125, 95);
   this.txtInstance.Name = "txtInstance";
   this.txtInstance.Size = new System.Drawing.Size(296, 15);
   this.txtInstance.TabIndex = 3;
   // 
   // txtDatabase
   // 
   this.txtDatabase.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Left) 
            | System.Windows.Forms.AnchorStyles.Right)));
   this.txtDatabase.BackColor = System.Drawing.Color.Snow;
   this.txtDatabase.BorderStyle = System.Windows.Forms.BorderStyle.None;
   this.txtDatabase.Font = new System.Drawing.Font("Arial Unicode MS", 8.25F);
   this.txtDatabase.Location = new System.Drawing.Point(125, 122);
   this.txtDatabase.Name = "txtDatabase";
   this.txtDatabase.Size = new System.Drawing.Size(296, 15);
   this.txtDatabase.TabIndex = 4;
   // 
   // txtUser
   // 
   this.txtUser.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Left) 
            | System.Windows.Forms.AnchorStyles.Right)));
   this.txtUser.BackColor = System.Drawing.Color.Snow;
   this.txtUser.BorderStyle = System.Windows.Forms.BorderStyle.None;
   this.txtUser.Font = new System.Drawing.Font("Arial Unicode MS", 8.25F);
   this.txtUser.Location = new System.Drawing.Point(125, 150);
   this.txtUser.Name = "txtUser";
   this.txtUser.Size = new System.Drawing.Size(296, 15);
   this.txtUser.TabIndex = 5;
   // 
   // btnTest
   // 
   this.btnTest.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Bottom | System.Windows.Forms.AnchorStyles.Left) 
            | System.Windows.Forms.AnchorStyles.Right)));
   this.btnTest.FlatStyle = System.Windows.Forms.FlatStyle.Flat;
   this.btnTest.Location = new System.Drawing.Point(181, 231);
   this.btnTest.Name = "btnTest";
   this.btnTest.Size = new System.Drawing.Size(77, 23);
   this.btnTest.TabIndex = 9;
   this.btnTest.Text = "Test";
   this.btnTest.UseVisualStyleBackColor = true;
   // 
   // btnCancel
   // 
   this.btnCancel.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Bottom | System.Windows.Forms.AnchorStyles.Left) 
            | System.Windows.Forms.AnchorStyles.Right)));
   this.btnCancel.DialogResult = System.Windows.Forms.DialogResult.Cancel;
   this.btnCancel.FlatStyle = System.Windows.Forms.FlatStyle.Flat;
   this.btnCancel.Location = new System.Drawing.Point(262, 231);
   this.btnCancel.Name = "btnCancel";
   this.btnCancel.Size = new System.Drawing.Size(77, 23);
   this.btnCancel.TabIndex = 10;
   this.btnCancel.Text = "Cancel";
   this.btnCancel.UseVisualStyleBackColor = true;
   // 
   // btnOk
   // 
   this.btnOk.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Bottom | System.Windows.Forms.AnchorStyles.Left) 
            | System.Windows.Forms.AnchorStyles.Right)));
   this.btnOk.DialogResult = System.Windows.Forms.DialogResult.OK;
   this.btnOk.FlatStyle = System.Windows.Forms.FlatStyle.Flat;
   this.btnOk.Location = new System.Drawing.Point(343, 231);
   this.btnOk.Name = "btnOk";
   this.btnOk.Size = new System.Drawing.Size(77, 23);
   this.btnOk.TabIndex = 11;
   this.btnOk.Text = "OK";
   this.btnOk.UseVisualStyleBackColor = true;
   // 
   // txtPassword
   // 
   this.txtPassword.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Left) 
            | System.Windows.Forms.AnchorStyles.Right)));
   this.txtPassword.BackColor = System.Drawing.Color.Snow;
   this.txtPassword.BorderStyle = System.Windows.Forms.BorderStyle.None;
   this.txtPassword.Font = new System.Drawing.Font("Arial Unicode MS", 8.25F);
   this.txtPassword.Location = new System.Drawing.Point(125, 178);
   this.txtPassword.Name = "txtPassword";
   this.txtPassword.PasswordChar = '*';
   this.txtPassword.Size = new System.Drawing.Size(296, 15);
   this.txtPassword.TabIndex = 6;
   // 
   // label5
   // 
   this.label5.Location = new System.Drawing.Point(4, 178);
   this.label5.Name = "label5";
   this.label5.Size = new System.Drawing.Size(117, 18);
   this.label5.TabIndex = 36;
   this.label5.Text = "Password";
   this.label5.TextAlign = System.Drawing.ContentAlignment.MiddleRight;
   // 
   // label6
   // 
   this.label6.Location = new System.Drawing.Point(5, 66);
   this.label6.Name = "label6";
   this.label6.Size = new System.Drawing.Size(117, 18);
   this.label6.TabIndex = 38;
   this.label6.Text = "DBMS";
   this.label6.TextAlign = System.Drawing.ContentAlignment.MiddleRight;
   // 
   // pupMru
   // 
   this.pupMru.AccessibleRole = System.Windows.Forms.AccessibleRole.None;
   this.pupMru.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Left) 
            | System.Windows.Forms.AnchorStyles.Right)));
   this.pupMru.BackColor = System.Drawing.Color.Snow;
   this.pupMru.DropDownStyle = System.Windows.Forms.ComboBoxStyle.DropDownList;
   this.pupMru.FlatStyle = System.Windows.Forms.FlatStyle.Flat;
   this.pupMru.Font = new System.Drawing.Font("Arial Unicode MS", 8.25F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
   this.pupMru.FormattingEnabled = true;
   this.pupMru.Location = new System.Drawing.Point(125, 12);
   this.pupMru.Name = "pupMru";
   this.pupMru.Size = new System.Drawing.Size(295, 23);
   this.pupMru.TabIndex = 0;
   this.pupMru.SelectedIndexChanged += new System.EventHandler(this.pupMru_SelectedIndexChanged);
   // 
   // label7
   // 
   this.label7.Location = new System.Drawing.Point(5, 40);
   this.label7.Name = "label7";
   this.label7.Size = new System.Drawing.Size(117, 18);
   this.label7.TabIndex = 40;
   this.label7.Text = "DB - Technology";
   this.label7.TextAlign = System.Drawing.ContentAlignment.MiddleRight;
   // 
   // pupDbTec
   // 
   this.pupDbTec.AccessibleRole = System.Windows.Forms.AccessibleRole.None;
   this.pupDbTec.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Left) 
            | System.Windows.Forms.AnchorStyles.Right)));
   this.pupDbTec.BackColor = System.Drawing.Color.Snow;
   this.pupDbTec.DropDownStyle = System.Windows.Forms.ComboBoxStyle.DropDownList;
   this.pupDbTec.FlatStyle = System.Windows.Forms.FlatStyle.Flat;
   this.pupDbTec.Font = new System.Drawing.Font("Arial Unicode MS", 8.25F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
   this.pupDbTec.FormattingEnabled = true;
   this.pupDbTec.Items.AddRange(new object[] {
            "jdbc",
            "odbc",
            "dotnet"});
   this.pupDbTec.Location = new System.Drawing.Point(125, 38);
   this.pupDbTec.Name = "pupDbTec";
   this.pupDbTec.Size = new System.Drawing.Size(295, 23);
   this.pupDbTec.TabIndex = 1;
   // 
   // txtTFilter
   // 
   this.txtTFilter.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Left) 
            | System.Windows.Forms.AnchorStyles.Right)));
   this.txtTFilter.BackColor = System.Drawing.Color.Snow;
   this.txtTFilter.BorderStyle = System.Windows.Forms.BorderStyle.None;
   this.txtTFilter.Font = new System.Drawing.Font("Arial Unicode MS", 8.25F);
   this.txtTFilter.Location = new System.Drawing.Point(125, 203);
   this.txtTFilter.Name = "txtTFilter";
   this.txtTFilter.Size = new System.Drawing.Size(146, 15);
   this.txtTFilter.TabIndex = 7;
   this.txtTFilter.Text = "*";
   // 
   // txtVFilter
   // 
   this.txtVFilter.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Left) 
            | System.Windows.Forms.AnchorStyles.Right)));
   this.txtVFilter.BackColor = System.Drawing.Color.Snow;
   this.txtVFilter.BorderStyle = System.Windows.Forms.BorderStyle.None;
   this.txtVFilter.Font = new System.Drawing.Font("Arial Unicode MS", 8.25F);
   this.txtVFilter.Location = new System.Drawing.Point(274, 203);
   this.txtVFilter.Name = "txtVFilter";
   this.txtVFilter.Size = new System.Drawing.Size(146, 15);
   this.txtVFilter.TabIndex = 8;
   this.txtVFilter.Text = "*";
   // 
   // label8
   // 
   this.label8.Location = new System.Drawing.Point(-66, 203);
   this.label8.Name = "label8";
   this.label8.Size = new System.Drawing.Size(188, 18);
   this.label8.TabIndex = 43;
   this.label8.Text = "connected tables/views";
   this.label8.TextAlign = System.Drawing.ContentAlignment.MiddleRight;
   // 
   // frmDbConnect
   // 
   this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
   this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
   this.ClientSize = new System.Drawing.Size(432, 260);
   this.ControlBox = false;
   this.Controls.Add(this.label8);
   this.Controls.Add(this.txtVFilter);
   this.Controls.Add(this.txtTFilter);
   this.Controls.Add(this.label7);
   this.Controls.Add(this.pupDbTec);
   this.Controls.Add(this.label6);
   this.Controls.Add(this.pupMru);
   this.Controls.Add(this.txtPassword);
   this.Controls.Add(this.label5);
   this.Controls.Add(this.btnOk);
   this.Controls.Add(this.btnCancel);
   this.Controls.Add(this.btnTest);
   this.Controls.Add(this.txtUser);
   this.Controls.Add(this.txtDatabase);
   this.Controls.Add(this.txtInstance);
   this.Controls.Add(this.label4);
   this.Controls.Add(this.label3);
   this.Controls.Add(this.label2);
   this.Controls.Add(this.label1);
   this.Controls.Add(this.pupDbms);
   this.Name = "frmDbConnect";
   this.Text = "Connect to Database";
   this.ResumeLayout(false);
   this.PerformLayout();

  }

  #endregion

  private System.Windows.Forms.Label label1;
  private System.Windows.Forms.Label label2;
  private System.Windows.Forms.Label label3;
  private System.Windows.Forms.Label label4;
  private System.Windows.Forms.Label label5;
  internal System.Windows.Forms.ComboBox pupDbms;
  internal System.Windows.Forms.TextBox txtInstance;
  internal System.Windows.Forms.TextBox txtDatabase;
  internal System.Windows.Forms.TextBox txtUser;
  internal System.Windows.Forms.Button btnTest;
  internal System.Windows.Forms.Button btnCancel;
  internal System.Windows.Forms.Button btnOk;
  internal System.Windows.Forms.TextBox txtPassword;
  private System.Windows.Forms.Label label6;
  internal System.Windows.Forms.ComboBox pupMru;
  private System.Windows.Forms.Label label7;
  internal System.Windows.Forms.ComboBox pupDbTec;
  private System.Windows.Forms.ColorDialog colorDialog1;
  private System.Windows.Forms.ColorDialog colorDialog2;
  private System.Windows.Forms.ColorDialog colorDialog3;
  private System.Windows.Forms.ColorDialog colorDialog4;
  internal System.Windows.Forms.TextBox txtTFilter;
  internal System.Windows.Forms.TextBox txtVFilter;
  private System.Windows.Forms.Label label8;
 }
}
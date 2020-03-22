
package org.xxdevplus.data;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JDialog;
import org.xxdevplus.struct.KeyPile;
import org.xxdevplus.utl.Registry;
import org.xxdevplus.utl.ctx;
import org.xxdevplus.utl.utl;

public class frmDbConnect extends JDialog
{
 //Copyright© 1996-2020 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties, You may use, modify, distribute this code under GNU public licence for NON-Commercial use as long as this notice remains unchanged.
 private static boolean selfTested  = false;  private static String ass(boolean expr) throws Exception { if (!expr) throw new Exception(ass(true) + " SelfTest Failure"); return "frmDbConnect"; } private static void selfTest() { selfTested = true; } private void init() { if (!selfTested) selfTest(); }

 private Db db = null;
 private void loadDbMru() throws Exception
 {
  ctx cx = new ctx();
  KeyPile<String, String> mruList = new KeyPile<String, String>();
  for (long i = 1; i < 10; i++)
  {
   String val = "";
   try { Registry.createKey(Registry.HKEY_CURRENT_USER, "SOFTWARE\\" + cx.Name() + "\\DataBases"); val = Registry.readString(Registry.HKEY_CURRENT_USER, "SOFTWARE\\" + cx.Name() + "\\DataBases", "MRU" + i); } catch (Exception ex) { val = ""; }
   if (val != null) if (val.length() > 0) mruList.Add("MRU" + i, val);
  }
  String[] itm = new String[9];
  int j = 0;
  for (String key : mruList.kAsc()) { j++; if (j > 9) break; itm[j - 1] = mruList.g(key); }
  int border = 10;
  for (int k = 1; k <=9; k++) if ((itm[k - 1] == null) || (itm[k - 1].trim().length() == 0)) { border = k; break; }
  String[] items = new String[border - 1];
  for (int k = 1; k < border; k++) items[k - 1] = itm[k - 1];
  pupMru.setModel(new DefaultComboBoxModel(items));
 }

 public frmDbConnect(Db db) throws Exception
 {
  this.db = db;
  initComponents();
  getRootPane().setDefaultButton(btnOk);
  String activeDbms = " " + (String)(new ctx()).IniParam().g("ACTIVEDBMS") + " ";
  ArrayList<String> items= new ArrayList<String>();
  if (activeDbms.indexOf(" h2s ")  > -1) items.add("h2s  (H2Sql)");
  if (activeDbms.indexOf(" mys ")  > -1) items.add("mys  (Oracle MySql)");
  if (activeDbms.indexOf(" 2mys ") > -1) items.add("2mys (Oracle MySql Emulation of H2Db)");
  if (activeDbms.indexOf(" pgs ")  > -1) items.add("pgs  (PostGreSql)");
  if (activeDbms.indexOf(" 2pgs ") > -1) items.add("2pgs (PostGreSql Emulation of H2Db)");
  if (activeDbms.indexOf(" fbd ")  > -1) items.add("fbd  (FireBirdSql)");
  if (activeDbms.indexOf(" 2fbd ") > -1) items.add("2fbd (FireBirdSql Emulation of H2Db)");
  if (activeDbms.indexOf(" sql ")  > -1) items.add("sql  (SqLite)");
  if (activeDbms.indexOf(" 2sql ") > -1) items.add("2sql (SqLite Emulation of H2Db)");
  if (activeDbms.indexOf(" mss ")  > -1) items.add("mss  (MsSqlServer)");
  if (activeDbms.indexOf(" 2mss ") > -1) items.add("2mss (MsSqlServer Emulation of H2Db)");
  if (activeDbms.indexOf(" syb ")  > -1) items.add("syb  (SAP SyBase)");
  if (activeDbms.indexOf(" 2syb ") > -1) items.add("2syb (SAP SyBase Emulation of H2Db)");
  if (activeDbms.indexOf(" ora ")  > -1) items.add("ora  (Oracle)");
  if (activeDbms.indexOf(" 2ora ") > -1) items.add("2ora (Oracle Emulation of H2Db)");
  if (activeDbms.indexOf(" db2 ")  > -1) items.add("db2  (IBM DB2)");
  if (activeDbms.indexOf(" 2db2 ") > -1) items.add("2db2 (IBM DB2 Emulation of H2Db)");
  if (activeDbms.indexOf(" dby ")  > -1) items.add("dby  (Apache Derby)");
  if (activeDbms.indexOf(" 2dby ") > -1) items.add("2dby (Apache Derby Emulation of H2Db)");
  if (activeDbms.indexOf(" hsq ")  > -1) items.add("hsq  (HSqlDb)");
  if (activeDbms.indexOf(" 2hsq ") > -1) items.add("2hsq (HSqlDb Emulation of H2Db)");
  if (activeDbms.indexOf(" tda ")  > -1) items.add("tda  (Novell TeraData)");
  String[] itm = new String[items.size()];
  for (int i = 1; i <= items.size(); i++) itm[i - 1] = items.get(i - 1);
  pupDbms.setModel(new DefaultComboBoxModel(itm));
  loadDbMru();
 }

 public DbUrl Url()
 {
  return new DbUrl(pupDbTec.getSelectedItem().toString().trim() + ":" + utl.cutl(pupDbms.getSelectedItem().toString().trim(), " ") + "::" + txtInstance.getText().trim().replace("\\", "/") + "," + txtDatabase.getText().trim() + "," + txtUser.getText().trim() + "," + txtPassword.getText().trim() + "<<" + txtTFilter.getText().trim() + "<<" + txtVFilter.getText().trim(), db.Drivers());
 }

 public void Url(DbUrl value)
 {
  for (int i = 1; i <= pupDbTec.getModel().getSize(); i++) if (((String)(pupDbTec.getModel().getElementAt(i - 1))).startsWith(value.dbtec))      pupDbTec.getModel().setSelectedItem(pupDbTec.getModel().getElementAt(i - 1));;
  for (int i = 1; i <= pupDbms.getModel().getSize();  i++) if (((String)(pupDbms.getModel().getElementAt(i - 1))).startsWith(value.dbms + " "))  pupDbms.getModel().setSelectedItem(pupDbms.getModel().getElementAt(i - 1));;
  txtInstance.setText   ( value.host.trim() + utl.pV(":", value.port) + utl.pV("/", value.instance.trim()) );
  txtDatabase.setText   ( value.database.trim() + utl.ppV("[", "]", value.schema.trim()));
  txtUser.setText       ( value.user                       );
  txtPassword.setText   ( value.password                   );
  txtTFilter.setText    ( value.tfilter                    );
  txtVFilter.setText    ( value.vfilter                    );
 }

 frmDbConnect(KeyPile<String, String> dbDrivers) { throw new UnsupportedOperationException("Not yet implemented"); }

 public static void main(String args[])
 {
  java.awt.EventQueue.invokeLater
  (
   new Runnable()
   {
    public void run() {try {new frmDbConnect(new Db(new ctx().DbDrivers())).setVisible(true);} catch (Exception ex) {Logger.getLogger(frmDbConnect.class.getName()).log(Level.SEVERE, null, ex);}}
   }
  );
 }

 private void pupMru_SelectedIndexChanged(Object sender, ActionEvent e) { Url(new DbUrl(pupMru.getSelectedItem().toString(), db.Drivers())); }


 @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        pupMru = new javax.swing.JComboBox();
        pupDbTec = new javax.swing.JComboBox();
        pupDbms = new javax.swing.JComboBox();
        txtInstance = new javax.swing.JTextField();
        txtDatabase = new javax.swing.JTextField();
        txtUser = new javax.swing.JTextField();
        btnTest = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        btnOk = new javax.swing.JButton();
        txtPassword = new javax.swing.JPasswordField();
        jLabel8 = new javax.swing.JLabel();
        txtTFilter = new javax.swing.JTextField();
        txtVFilter = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);

        jLabel1.setText("Recent Connections");

        jLabel2.setText("DB - Technology");

        jLabel3.setText("DBMS");

        jLabel4.setText("Host:Port/Instance");

        jLabel5.setText("Database[Schema]");

        jLabel6.setText("DbUser");

        jLabel7.setText("Password");

        pupMru.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pupMruActionPerformed(evt);
            }
        });

        pupDbTec.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "jdbc", "odbc", "dotnet" }));

        btnTest.setText("Test");
        btnTest.setName("btnTest"); // NOI18N

        btnCancel.setText("Cancel");
        btnCancel.setName("btnCancel"); // NOI18N

        btnOk.setText("OK");
        btnOk.setName("btnOk"); // NOI18N

        txtPassword.setText("jPasswordField1");

        jLabel8.setText("connected tables/views");

        txtTFilter.setText("*");

        txtVFilter.setText("*");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel2)
                            .addComponent(jLabel1)
                            .addComponent(jLabel7)
                            .addComponent(jLabel4)
                            .addComponent(jLabel6)
                            .addComponent(jLabel5)
                            .addComponent(jLabel8))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(pupDbTec, 0, 346, Short.MAX_VALUE)
                            .addComponent(pupDbms, 0, 346, Short.MAX_VALUE)
                            .addComponent(pupMru, 0, 346, Short.MAX_VALUE)
                            .addComponent(txtInstance, javax.swing.GroupLayout.DEFAULT_SIZE, 346, Short.MAX_VALUE)
                            .addComponent(txtDatabase, javax.swing.GroupLayout.DEFAULT_SIZE, 346, Short.MAX_VALUE)
                            .addComponent(txtPassword, javax.swing.GroupLayout.DEFAULT_SIZE, 346, Short.MAX_VALUE)
                            .addComponent(txtUser, javax.swing.GroupLayout.DEFAULT_SIZE, 346, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(txtTFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtVFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(127, 127, 127)
                        .addComponent(btnTest)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 89, Short.MAX_VALUE)
                        .addComponent(btnCancel)
                        .addGap(86, 86, 86)
                        .addComponent(btnOk)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(pupMru, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addGap(7, 7, 7)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(pupDbTec, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(pupDbms, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtInstance, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtDatabase, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txtUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(txtTFilter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtVFilter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnOk)
                    .addComponent(btnTest)
                    .addComponent(btnCancel))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

 private void pupMruActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pupMruActionPerformed
  pupMru_SelectedIndexChanged(evt.getSource(), evt);
 }//GEN-LAST:event_pupMruActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnCancel;
    public javax.swing.JButton btnOk;
    public javax.swing.JButton btnTest;
    public javax.swing.JLabel jLabel1;
    public javax.swing.JLabel jLabel2;
    public javax.swing.JLabel jLabel3;
    public javax.swing.JLabel jLabel4;
    public javax.swing.JLabel jLabel5;
    public javax.swing.JLabel jLabel6;
    public javax.swing.JLabel jLabel7;
    public javax.swing.JLabel jLabel8;
    public javax.swing.JComboBox pupDbTec;
    public javax.swing.JComboBox pupDbms;
    public javax.swing.JComboBox pupMru;
    public javax.swing.JTextField txtDatabase;
    public javax.swing.JTextField txtInstance;
    public javax.swing.JPasswordField txtPassword;
    public javax.swing.JTextField txtTFilter;
    public javax.swing.JTextField txtUser;
    public javax.swing.JTextField txtVFilter;
    // End of variables declaration//GEN-END:variables


 /*
 JButton btnCancel()      { return  btnCancel;   }
 JButton btnOk()          { return  btnOk;       }
 JButton btnTest()        { return  btnTest;     }
 JLabel jLabel1()         { return  jLabel1;     }
 JLabel jLabel2()         { return  jLabel2;     }
 JLabel jLabel3()         { return  jLabel3;     }
 JLabel jLabel4()         { return  jLabel4;     }
 JLabel jLabel5()         { return  jLabel5;     }
 JLabel jLabel6()         { return  jLabel6;     }
 JLabel jLabel7()         { return  jLabel7;     }
 JComboBox pupDbTec()     { return  pupDbTec;   }
 JComboBox pupDbms()      { return  pupDbms;   }
 JComboBox pupMru()       { return  pupMru;      }
 JTextField txtDatabase() { return  txtDatabase; }
 JTextField txtInstance() { return  txtInstance; }
 JTextField txtPassword() { return  txtPassword; }
 JTextField txtUser()     { return  txtUser;     }
 */


}





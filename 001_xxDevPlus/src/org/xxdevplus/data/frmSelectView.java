
package org.xxdevplus.data;

import java.awt.event.ComponentEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JDialog;
import org.xxdevplus.utl.utl;

public class frmSelectView extends JDialog
{
 //Copyright© 1996-2020 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties, You may use, modify, distribute this code under GNU public licence for NON-Commercial use as long as this notice remains unchanged.
 private static boolean selfTested  = false;  private static String ass(boolean expr) throws Exception { if (!expr) throw new Exception(ass(true) + " SelfTest Failure"); return "frmSelectView"; } private static void selfTest() { selfTested = true; } private void init() { if (!selfTested) selfTest(); }

 private DbSlcBlock dbs = new DbSlcBlock();

 public frmSelectView(Db db, DbSlcBlock dbs) throws Exception
 {
  initComponents();
  this.dbs = dbs;
  lstSelectView.setModel( new DefaultListModel());
  for (DbSlc sel : dbs) 
   ((DefaultListModel)lstSelectView.getModel()).add
   (
   ((DefaultListModel)lstSelectView.getModel()).getSize(),
   sel.sql(db).replace("\r", " ").replace("\n", " ")
   );
 }

 public frmSelectView() throws Exception { initComponents(); }

 private void lstSelectView_SizeChanged(Object sender, ComponentEvent e)
 {
  //lstSelectView.Columns[0].Width = lstSelectView.Width;
 }

 private void frmSelectView_SizeChanged(Object sender, ComponentEvent e)
 {
  utl.setWidth(lstSelectView, getSize().width);
  utl.setHeight(lstSelectView, getSize().height);
 }

 public static void main(String args[])  { java.awt.EventQueue.invokeLater(new Runnable()  { public void run() {try { new frmSelectView().setVisible(true); } catch (Exception ex) { Logger.getLogger(frmSelectView.class.getName()).log(Level.SEVERE, null, ex); } } }); }

 @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        lstSelectView = new javax.swing.JList();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        lstSelectView.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        lstSelectView.setPreferredSize(new java.awt.Dimension(640, 480));
        jScrollPane1.setViewportView(lstSelectView);

        getContentPane().add(jScrollPane1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents



    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JList lstSelectView;
    // End of variables declaration//GEN-END:variables

}

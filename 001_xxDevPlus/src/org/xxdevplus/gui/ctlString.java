

//** @author  GeTr Copyright© 1996-2020 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties
//** @license You may use, modify, distribute this code under GNU public dual licence free for NON-Commercial use as long as this notice remains unchanged.
//** @comment String Editor Control


package org.xxdevplus.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.border.Border;
import org.xxdevplus.utl.utl;

public class ctlString extends JPanel implements DragGestureListener, DragSourceListener
{

 private boolean closeBox = false;

 public ctlString(boolean edit, String text, boolean closeBox)
 {
  initComponents();
  this.closeBox = closeBox ;
  setText(text);
  utl.setSize(this, lblString.WIDTH, lblString.HEIGHT);
  txtString.setDragEnabled(false);
  DragSource.getDefaultDragSource().createDefaultDragGestureRecognizer(lblString, DnDConstants.ACTION_COPY_OR_MOVE, this);
  DragSource.getDefaultDragSource().createDefaultDragGestureRecognizer(txtString, DnDConstants.ACTION_COPY_OR_MOVE, this);
  if (edit) 
  {
   lblString.setVisible(false);
   txtString.setVisible(true);
   //lblString.setLocation(10000, 10000);
  }
  else
  {
   lblString.setVisible(true);
   txtString.setVisible(false);
   //txtString.setLocation(10000, 10000);
  }
  btnClose.setVisible(false);
 }
 public ctlString (boolean edit, String text) { this(edit, text , false); }
 public ctlString (boolean              edit) { this(edit, "aaa"   , false); }
 public ctlString (                         ) { this(true, ""   , false); }

 @Override public void setComponentPopupMenu(JPopupMenu mnu)
 {
  lblString.setComponentPopupMenu(mnu);
  txtString.setComponentPopupMenu(mnu);
 }

 private void properView(boolean showCloseButton)
 {
  //btnClose.setVisible(showCloseButton);
  utl.BringToFront(btnClose);
  utl.setLocation(btnClose, getWidth() - btnClose.getWidth() + 3, -1);
  utl.setLocation(lblString, 0, 0); utl.setSize(lblString, getWidth(), getHeight());
  utl.setLocation(txtString, 0, 0); utl.setSize(txtString, getWidth(), getHeight());
  if (btnClose.isVisible())
  {
   //utl.setLocation(lblString, 0, 0); utl.setSize(lblString, getWidth() - btnClose.getWidth() - 1, getHeight());
   //utl.setLocation(txtString, 0, 0); utl.setSize(txtString, getWidth() - btnClose.getWidth() - 1, getHeight());
  }
  else
  {
   //utl.setLocation(lblString, 0, 0); utl.setSize(lblString, getWidth(), getHeight());
   //utl.setLocation(txtString, 0, 0); utl.setSize(txtString, getWidth(), getHeight());
  }
 }

 private void ctlString_Resize(Object sender, ComponentEvent e) { properView(btnClose.isVisible()); }

 public int              TextAlign       (                 ) { return lblString.getHorizontalTextPosition()                                                         ; }
 public void             TextAlign       (int         value) { txtString.setHorizontalAlignment(value); lblString.setHorizontalTextPosition(value)                  ; }
 
 public String           getText         (                 ) { if (txtString.isVisible()) return txtString.getText(); else return lblString.getText()               ; }
 public void             setText         (String      value) { txtString.setText(value); lblString.setText(value)                                                   ; }
 
 public Font             Font            (                 ) { if ((txtString == null) || (lblString == null)) return null; if (txtString.isVisible()) return txtString.getFont(); else return lblString.getFont()               ; }
 public Font             getFont         (                 ) { if ((txtString == null) || (lblString == null)) return null; if (txtString.isVisible()) return txtString.getFont(); else return lblString.getFont()               ; }
 public void             Font            (Font        value) { if ((txtString == null) || (lblString == null)) return;      txtString.setFont(value); lblString.setFont(value)                                                   ; }
 public void             setFont         (Font        value) { if ((txtString == null) || (lblString == null)) return;      txtString.setFont(value); lblString.setFont(value)                                                   ; }
 
 public Color            ForeColor       (                 ) { if ((txtString == null) || (lblString == null)) return null; if (txtString.isVisible()) return txtString.getForeground() ; else return lblString.getForeground()  ; }
 public Color            getForeground   (                 ) { if ((txtString == null) || (lblString == null)) return null; if (txtString.isVisible()) return txtString.getForeground() ; else return lblString.getForeground()  ; }
 public void             ForeColor       (Color       value) { if ((txtString == null) || (lblString == null)) return;      txtString.setForeground(value); lblString.setForeground(value)                                       ; }
 public void             setForeground   (Color       value) { if ((txtString == null) || (lblString == null)) return;      txtString.setForeground(value); lblString.setForeground(value)                                       ; }
 
 public Color            BackColor       (                 ) { if (txtString.isVisible()) return txtString.getBackground(); else return lblString.getBackground()   ; }
 public void             BackColor       (Color       value) { txtString.setBackground(value); lblString.setBackground(value)                                       ; }
 
 public Border           BorderStyle     (                 ) { if (txtString.isVisible()) return txtString.getBorder(); else return lblString.getBorder()           ; }
 public void             BorderStyle     (Border      value) { txtString.setBorder(value); lblString.setBorder(value)                                               ; }
 
 //public boolean          AllowDrop       (                 ) { if (txtString.isVisible()) return txtString.AllowDrop; else return lblString.AllowDrop     ; }
 //public void             AllowDrop       (boolean     value) { txtString.AllowDrop = value; lblString.AllowDrop = value                         ; }

  private void lblString_DoubleClick(Object sender, MouseEvent e)
  {
   //txtString.setVisible(true); lblString.setVisible(false);
  }


 @SuppressWarnings("unchecked")
 // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
 private void initComponents() {

  lblString = new javax.swing.JLabel();
  txtString = new javax.swing.JTextField();
  btnClose = new javax.swing.JButton();

  setBorder(new javax.swing.border.MatteBorder(null));
  addComponentListener(new java.awt.event.ComponentAdapter() {
   public void componentResized(java.awt.event.ComponentEvent evt) {
    formComponentResized(evt);
   }
  });
  setLayout(null);

  lblString.setBackground(new java.awt.Color(245, 245, 245));
  lblString.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
  lblString.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
  lblString.setOpaque(true);
  lblString.addMouseListener(new java.awt.event.MouseAdapter() {
   public void mouseClicked(java.awt.event.MouseEvent evt) {
    lblStringMouseClicked(evt);
   }
   public void mouseExited(java.awt.event.MouseEvent evt) {
    lblStringMouseExited(evt);
   }
  });
  lblString.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
   public void mouseDragged(java.awt.event.MouseEvent evt) {
    lblStringMouseDragged(evt);
   }
   public void mouseMoved(java.awt.event.MouseEvent evt) {
    lblStringMouseMoved(evt);
   }
  });
  lblString.addFocusListener(new java.awt.event.FocusAdapter() {
   public void focusLost(java.awt.event.FocusEvent evt) {
    lblStringFocusLost(evt);
   }
  });
  add(lblString);
  lblString.setBounds(10, 0, 20, 13);

  txtString.setBackground(new java.awt.Color(245, 245, 245));
  txtString.setHorizontalAlignment(javax.swing.JTextField.CENTER);
  txtString.setBorder(null);
  txtString.setDropMode(javax.swing.DropMode.INSERT);
  txtString.addMouseListener(new java.awt.event.MouseAdapter() {
   public void mouseExited(java.awt.event.MouseEvent evt) {
    txtStringMouseExited(evt);
   }
  });
  txtString.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
   public void mouseDragged(java.awt.event.MouseEvent evt) {
    txtStringMouseDragged(evt);
   }
   public void mouseMoved(java.awt.event.MouseEvent evt) {
    txtStringMouseMoved(evt);
   }
  });
  txtString.addFocusListener(new java.awt.event.FocusAdapter() {
   public void focusLost(java.awt.event.FocusEvent evt) {
    txtStringFocusLost(evt);
   }
  });
  add(txtString);
  txtString.setBounds(50, 0, 30, 13);

  btnClose.setBackground(new java.awt.Color(153, 153, 255));
  btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/xxdevplus/gui/closebuttonBig.PNG"))); // NOI18N
  btnClose.setIconTextGap(-11);
  btnClose.addMouseListener(new java.awt.event.MouseAdapter() {
   public void mouseExited(java.awt.event.MouseEvent evt) {
    btnCloseMouseExited(evt);
   }
  });
  btnClose.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
   public void mouseMoved(java.awt.event.MouseEvent evt) {
    btnCloseMouseMoved(evt);
   }
  });
  btnClose.addActionListener(new java.awt.event.ActionListener() {
   public void actionPerformed(java.awt.event.ActionEvent evt) {
    btnCloseActionPerformed(evt);
   }
  });
  add(btnClose);
  btnClose.setBounds(100, 0, 22, 22);
 }// </editor-fold>//GEN-END:initComponents

 private void formComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentResized
  ctlString_Resize(evt.getSource(), evt);
 }//GEN-LAST:event_formComponentResized

 private void lblStringMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblStringMouseMoved
  //properView(true); for (MouseMotionListener l : getMouseMotionListeners()) l.mouseMoved(evt);
 }//GEN-LAST:event_lblStringMouseMoved

 private void lblStringMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblStringMouseDragged
  //properView(true); for (MouseMotionListener l : getMouseMotionListeners()) l.mouseMoved(evt);
 }//GEN-LAST:event_lblStringMouseDragged

 private void lblStringMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblStringMouseClicked
  if (evt.getClickCount() == 2) lblString_DoubleClick(evt.getSource(), evt);
 }//GEN-LAST:event_lblStringMouseClicked

 private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
  //try { btnClose_Click(evt.getSource(), evt); } catch (Exception ex) { Logger.getLogger(ctlTripleList.class.getName()).log(Level.SEVERE, null, ex); }
}//GEN-LAST:event_btnCloseActionPerformed

 private void txtStringMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtStringMouseDragged
  //properView(true);
 }//GEN-LAST:event_txtStringMouseDragged

 private void txtStringMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtStringMouseMoved
  //properView(true);
 }//GEN-LAST:event_txtStringMouseMoved

 private void txtStringMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtStringMouseExited
  //properView(false);
 }//GEN-LAST:event_txtStringMouseExited

 private void txtStringFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtStringFocusLost
  //properView(false);
 }//GEN-LAST:event_txtStringFocusLost

 private void lblStringFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_lblStringFocusLost
  //properView(false);
 }//GEN-LAST:event_lblStringFocusLost

 private void lblStringMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblStringMouseExited
  //properView(false);
 }//GEN-LAST:event_lblStringMouseExited

 private void btnCloseMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCloseMouseMoved
  //properView(true);
 }//GEN-LAST:event_btnCloseMouseMoved

 private void btnCloseMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCloseMouseExited
  //properView(true);
 }//GEN-LAST:event_btnCloseMouseExited


 // Variables declaration - do not modify//GEN-BEGIN:variables
 private javax.swing.JButton btnClose;
 private javax.swing.JLabel lblString;
 private javax.swing.JTextField txtString;
 // End of variables declaration//GEN-END:variables

 public void dragGestureRecognized    (DragGestureEvent      dge)
 {
  dge.startDrag(null, new StringSelection(txtString.getText()), this);
 }

 public void dragEnter                (DragSourceDragEvent  dsde) { }
 public void dragOver                 (DragSourceDragEvent  dsde) { }
 public void dropActionChanged        (DragSourceDragEvent  dsde) { }
 public void dragExit                 (DragSourceEvent       dse) { }
 public void dragDropEnd              (DragSourceDropEvent  dsde) { }

 @Override public void addMouseListener(MouseListener l)
 {
  txtString.addMouseListener(l);
  lblString.addMouseListener(l);
 }


}

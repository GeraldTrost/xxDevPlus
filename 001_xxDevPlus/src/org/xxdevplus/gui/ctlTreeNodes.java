

package org.xxdevplus.gui;


import java.awt.Color;
import java.awt.Point;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import org.xxdevplus.sys.CallBuilder;
import org.xxdevplus.struct.Pile;
import org.xxdevplus.utl.ctx;
import org.xxdevplus.utl.utl;


public class ctlTreeNodes extends ctlCanvas
{
 //Copyright© 1996-2020 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties, You may use, modify, distribute this code under GNU public licence for NON-Commercial use as long as this notice remains unchanged.
 private static boolean selfTested  = false; private static String ass(boolean expr) throws Exception { if (!expr) throw new Exception(ass(true) + " SelfTest Failure"); return "ctlTripleList"; } private static void selfTest() { selfTested = true; } private void init() { if (!selfTested) selfTest(); }
 protected double                        leftSpace          = 0.3;  //percentage
 protected long                          midWidth           = 30;   //pixel

 protected boolean                       lEdit              = false;
 protected boolean                       mEdit              = false;
 protected boolean                       rEdit              = false;
 protected JComponent                    mouseDownControl   = null;
 public    Pile<ctlString>               lText              = new Pile<ctlString>();
 public    Pile<ctlString>               mText              = new Pile<ctlString>();
 public    Pile<ctlString>               rText              = new Pile<ctlString>();
 public    Pile<Pile<JComponent>>        oTrans             = new Pile<Pile<JComponent>>(); //should be a Pile<Pile<ctlArrowTerminal>>
 public    Pile<JComponent>              iTrans             = new Pile<JComponent>();       //Pile<ctlArrowTerminal>
  
 /*
 protected JPopupMenu                    lTextMenu          = new JPopupMenu();
 protected JPopupMenu                    mTextMenu          = new JPopupMenu();
 protected JPopupMenu                    rTextMenu          = new JPopupMenu();
 */

 protected Pile<String>                  condition          = new Pile<String>();
 protected ctlTreeNodes                 tableDef           = null;
 public    String                        Title           () { return ddHandle.getText()   ; }

 protected void reload() throws Exception { setBackground(new Color(190, 190, 190)); }

 protected void properView() throws Exception
 {
  for (int i = 1; i <= lText.Len(); i++) if ((lText.g(i).getText().trim().length() == 0) ^ (rText.g(i).getText().trim().length() == 0)) { /*mText[i].getText = "==";  */}
  condition = new Pile<String>();
  for (int i = 1; i <= lText.Len(); i++) if (mText.g(i).getText().trim().length() != 0) condition.Push(lText.g(i).getText() + " = " + rText.g(i).getText());
  for (int i = 1; i <= lText.Len(); i++) if (i <= condition.Len()) { String[] cond = new String[] {condition.g(i)}; lText.g(i).setText(utl.cutl(cond, " = "));  rText.g(i).setText(utl.cutl(cond, " = ")); mText.g(i).setText(" = ");  }   else { lText.g(i).setText(""); rText.g(i).setText(""); mText.g(i).setText(""); }
 }

 public ctlTreeNodes TableDef() { return tableDef; } public void  TableDef (ctlTreeNodes value)  { tableDef = value; }

 public ctlTreeNodes() 
 { 
  try 
  {
   setSize(200, 60); 
   initComponents(); 
   lTmpl.setVisible(false); 
   mTmpl.setVisible(false); 
   rTmpl.setVisible(false); 
   ddHandle.setVisible(true); 
   ddHandle.setText("ctlTripleList"); 
   OnResize();
  } 
  catch (Exception ex) 
  {
     ex = ex;
  }
 }

 public ctlTreeNodes(ctx cx, String editMode) throws Exception
 {
  super(cx);
  lEdit = editMode.substring(0).startsWith("1");
  mEdit = editMode.substring(1).startsWith("1");
  rEdit = editMode.substring(2).startsWith("1");
  initComponents(); lTmpl.setVisible(false); mTmpl.setVisible(false); rTmpl.setVisible(false); OnResize();
  utl.setSize(this, 200, 60);
  setVisible(false);
  //((JComponent)cx.Param().g("Canvas")).add(this);
  Initialized = true;
  if (cx.Param().hasKey("LeftMenu"))  utl.cloneMenu(((JPopupMenu)cx.Param().g("LeftMenu")),  cx.Name(), lTextMenu, new CallBuilder(new Class[] { MouseEvent.class }, Void.TYPE).build(this,"lTextMenuMouseClicked"));
  if (cx.Param().hasKey("MidMenu"))   utl.cloneMenu(((JPopupMenu)cx.Param().g("MidMenu")),   cx.Name(), mTextMenu, new CallBuilder(new Class[] { MouseEvent.class }, Void.TYPE).build(this,"mTextMenuMouseClicked"));
  if (cx.Param().hasKey("RightMenu")) utl.cloneMenu(((JPopupMenu)cx.Param().g("RightMenu")), cx.Name(), rTextMenu, new CallBuilder(new Class[] { MouseEvent.class }, Void.TYPE).build(this,"rTextMenuMouseClicked"));
  utl.setControlFont(lTmpl, cx.DbFont());
  utl.setControlFont(mTmpl, cx.Font());
  utl.setControlFont(rTmpl, cx.TsFont());
  reload();
 }

 @Override public void chgLeft(long delta)
 {
  super.chgLeft(delta);
  for (int j = 1; j <= iTrans.Len(); j++) if (iTrans.g(j) != null) utl.chgLeft(iTrans.g(j), (int)delta);
  for (int i = 1; i <= oTrans.Len(); i++) for (int j = 1; j <= oTrans.g(i).Len(); j++) if (oTrans.g(i).g(j) != null) utl.chgLeft(oTrans.g(i).g(j), (int)delta);
 }

 @Override public void chgTop(long delta)
 {
  super.chgTop(delta);
  for (int j = 1; j <= iTrans.Len(); j++) if (iTrans.g(j) != null) utl.chgTop(iTrans.g(j), (int)delta);
  for (int i = 1; i <= oTrans.Len(); i++) for (int j = 1; j <= oTrans.g(i).Len(); j++) if (oTrans.g(i).g(j) != null) utl.chgTop(oTrans.g(i).g(j), (int)delta);
 }

 @Override public void chgWidth(long delta)
 {
  super.chgWidth(delta);
  for (int i = 1; i <= oTrans.Len(); i++) for (int j = 1; j <= oTrans.g(i).Len(); j++) if (oTrans.g(i).g(j) != null) utl.chgLeft(oTrans.g(i).g(j), (int)delta);
 }

 @Override public void chgHeight(long delta)
 {
  super.chgHeight(delta);
 }

 @Override public void chgLocation(long dx, long dy)
 {
  super.chgLocation(dx, dy);
  for (int j = 1; j <= iTrans.Len(); j++) if (iTrans.g(j) != null) utl.chgLocation(iTrans.g(j), (int)dx, (int)dy);
  for (int i = 1; i <= oTrans.Len(); i++) for (int j = 1; j <= oTrans.g(i).Len(); j++) if (oTrans.g(i).g(j) != null) utl.chgLocation(oTrans.g(i).g(j), (int)dx, (int)dy);
 }

 @Override public void chgSize(long dw, long dh)
 {
  super.chgSize(dw, dh);
  for (int i = 1; i <= oTrans.Len(); i++) for (int j = 1; j <= oTrans.g(i).Len(); j++) if (oTrans.g(i).g(j) != null) utl.chgSize(oTrans.g(i).g(j), (int)dw, (int)dh);
 }

 protected void delLine(int inx) throws Exception
 {
  for (int i = inx; i < lText.Len(); i++) { lText.g(i).setText( lText.g(i + 1).getText()); mText.g(i).setText( mText.g(i + 1).getText()); rText.g(i).setText( rText.g(i + 1).getText()); oTrans.s(oTrans.g(i + 1), i); for (int m = 1; m <= oTrans.g(i).Len(); m++) if (oTrans.g(i).g(m) != null) utl.chgTop(oTrans.g(i).g(m),  - lText.g(1).getSize().height); }
  lText.g(-1).setText("");
  mText.g(-1).setText("");
  rText.g(-1).setText("");
  oTrans.s(new Pile<JComponent>(), -1);
 }

 protected void pushLine() throws Exception
 {
  lText.Push                         (new ctlString(lEdit)           ); mText.Push                         (new ctlString(mEdit)           ); rText.Push                         (new ctlString(rEdit)           );
  fme.add                            (lText.g(-1)                    ); fme.add                            (mText.g(-1)                    ); fme.add                            (rText.g(-1)                    );

  utl.BringToFront(lText.g(-1));
  utl.BringToFront(rText.g(-1));

  lText.g(-1).add                    (lTextMenu                      ); mText.g(-1).add                    (mTextMenu                      ); rText.g(-1).add                    (rTextMenu                      );
  lText.g(-1).setComponentPopupMenu  (lTextMenu                      ); mText.g(-1).setComponentPopupMenu  (mTextMenu                      ); rText.g(-1).setComponentPopupMenu  (rTextMenu                      );
  utl.cpyHeight                      (lText.g(-1),              lTmpl); utl.cpyHeight                      (mText.g(-1), mTmpl             ); utl.cpyHeight                      (rText.g(-1), rTmpl             );
  lText.g(-1).setAlignmentX          (lTmpl.getAlignmentX          ()); mText.g(-1).setAlignmentX          (mTmpl.getAlignmentX          ()); rText.g(-1).setAlignmentX          (rTmpl.getAlignmentX          ());
  lText.g(-1).setAlignmentY          (lTmpl.getAlignmentY          ()); mText.g(-1).setAlignmentY          (mTmpl.getAlignmentY          ()); rText.g(-1).setAlignmentY          (rTmpl.getAlignmentY          ());
  lText.g(-1).setFont                (lTmpl.getFont                ()); mText.g(-1).setFont                (mTmpl.getFont                ()); 
  
  rText.g(-1).setFont                (rTmpl.getFont                ());


  lText.g(-1).setForeground          (lTmpl.getForeground          ()); mText.g(-1).setForeground          (mTmpl.getForeground          ()); 
  
  rText.g(-1).setForeground          (rTmpl.getForeground          ());


  //lText.g(-1).setHorizontalAlignment (lTmpl.getHorizontalAlignment ()); mText.g(-1).setHorizontalAlignment (mTmpl.getHorizontalAlignment ()); //rText.g(-1).setHorizontalAlignment (rTmpl.getHorizontalAlignment ());
  //lText.g(-1).setVerticalAlignment   (lTmpl.getVerticalAlignment   ()); mText.g(-1).setVerticalAlignment   (mTmpl.getVerticalAlignment   ());
  lText.g(-1).setForeground          (lTmpl.getForeground          ()); mText.g(-1).setForeground          (mTmpl.getForeground          ()); rText.g(-1).setForeground          (rTmpl.getForeground          ());
  lText.g(-1).setBackground          (lTmpl.getBackground          ()); mText.g(-1).setBackground          (mTmpl.getBackground          ()); rText.g(-1).setBackground          (rTmpl.getBackground          ());
  lText.g(-1).setBorder              (lTmpl.getBorder              ()); mText.g(-1).setBorder              (mTmpl.getBorder              ()); rText.g(-1).setBorder              (rTmpl.getBorder              ());
  lText.g(-1).setDropTarget          (lTmpl.getDropTarget          ()); mText.g(-1).setDropTarget          (mTmpl.getDropTarget          ()); rText.g(-1).setDropTarget          (rTmpl.getDropTarget          ());
  lText.g(-1).addMouseListener       (this                           ); mText.g(-1).addMouseListener       (this                           ); rText.g(-1).addMouseListener       (this                           );
  lText.g(-1).addMouseMotionListener (this                           ); mText.g(-1).addMouseMotionListener (this                           ); rText.g(-1).addMouseMotionListener (this                           );
  /*
  lText.g(-1).DragDrop    += lText_DragDrop;                            mText.g(-1).DragDrop    += mText_DragDrop;                            rText.g(-1).DragDrop      += rText_DragDrop;
  lText.g(-1).DragOver    += lText_DragOver;                            mText.g(-1).DragOver    += mText_DragOver;                            rText.g(-1).DragOver      += rText_DragOver;
  lText.g(-1).MouseMove   += lText_MouseMove;                           mText.g(-1).MouseMove   += mText_MouseMove;                           rText.g(-1).MouseMove     += rText_MouseMove;
  lText.g(-1).MouseDown   += lText_MouseDown;                           mText.g(-1).MouseDown   += mText_MouseDown;                           rText.g(-1).MouseDown     += rText_MouseDown;
  lText.g(-1).DoubleClick += lText_DoubleClick;                         mText.g(-1).DoubleClick += mText_DoubleClick;
  lText.g(-1).TextChanged += lText_TextChanged;                         mText.g(-1).TextChanged += mText_TextChanged;                         rText.g(-1).TextChanged   += rText_TextChanged;
   */
  oTrans.Push(new Pile<JComponent>());
  if (lText.g(-1).getText().length() > 0)  mText.g(-1).setText("=");
 }

 protected void popLine() throws Exception { fme.remove(lText.g(-1)); fme.remove(mText.g(-1)); fme.remove(rText.g(-1)); lText.Pop(); mText.Pop(); rText.Pop(); oTrans.Pop(); }

 @Override public void OnResize() throws Exception
 {
  super.OnResize();
  //if (!Initialized) return;
  utl.setLocation(fme, 2, 2); utl.setSize(fme, getSize().width - 4, getSize().height - 4);
  long lWidth = (long)((fme.getSize().width - midWidth) * leftSpace);
  long rWidth = fme.getSize().width - midWidth - lWidth;
  ddHandle.getParent().remove(ddHandle); fme.add(ddHandle);
  //dHandle.SendToBack();
  //ddHandle.Anchor = AnchorStyles.None;
  ddHandle.setLocation(new Point(-1, -1));
  btnClose.setLocation(new Point(fme.getSize().width - btnClose.getSize().width, -1));
  utl.setHeight(ddHandle, 22);
  utl.setWidth(ddHandle, btnClose.getLocation().x + 2);
  //ddHandle().BringToFront();   // in erlier Version ddHandle was contained in fme! ddHandle.SendToBack();
  long count = (long)((fme.getSize().height - 3 - ddHandle.getSize().height) / (lTmpl.getSize().height)) - lText.Len() + 1;
  if (count < 0) for (long i = 1; i <= -count; i++) popLine(); else for (long i = 1; i <= count; i++) pushLine();
  for (int i = 1; i <= lText.Len(); i++)
  {
   utl.cpyHeight(lText.g(i), lTmpl);           utl.cpyHeight(mText.g(i), mTmpl);                   utl.cpyHeight(rText.g(i), rTmpl);
   utl.setWidth(lText.g(i), (int)lWidth - 2);  utl.setWidth(mText.g(i), 2 * fme.getSize().width);  utl.setWidth(rText.g(i), (int)rWidth - 2);
   lText.g(i).setLocation(new Point(2, ddHandle.getSize().height + (lTmpl.getSize().height) * (int)(i - 1)));
   //mText.g(i).setLocation(new Point((int)midWidth / 2 + (int)lWidth - fme.getSize().width, ddHandle.getSize().height + (lTmpl.getSize().height) * (int)(i - 1)));
   rText.g(i).setLocation(new Point((int)lWidth + (int)midWidth, ddHandle.getSize().height + (lTmpl.getSize().height) * (int)(i - 1)));
   utl.setLocation(mText.g(i), lText.g(i).getSize().width + 3, ddHandle.getSize().height + (lTmpl.getSize().height) * (int)(i - 1));
   utl.setSize(mText.g(i), (int)midWidth - 2, lText.g(i).getSize().height);
   utl.BringToFront(lText.g(i));
   utl.BringToFront(rText.g(i));
  }
  //dbg this.setBackground(Color.blue);
 }

 protected void control_Load          (Object sender,      ComponentEvent e) throws Exception { setSize(getSize().width + 1, getSize().height); }
 protected void btnClose_Click        (Object sender,         ActionEvent e) throws Exception { setVisible(false); } //parentForm.RemoveTable(this); }

 protected void lTextMenu_Click       (Object sender                       ) throws Exception { ((ctlString)mouseDownControl).setText(((JMenuItem)sender).getText()); } // private void lTextMenu_Click(object sender, EventArgs e) { ((frmSimpleGui)cx.Param["ParentForm"]).setDraggedNode(lblName.getText, ((MenuItem)sender).getText); lText_OnDragDrop(mouseDownControl); }
 protected void mTextMenu_Click       (Object sender                       ) throws Exception { ((ctlString)mouseDownControl).setText(((JMenuItem)sender).getText()); }
 protected void rTextMenu_Click       (Object sender                       ) throws Exception { ((ctlString)mouseDownControl).setText(((JMenuItem)sender).getText()); } // private void rTextMenu_Click(object sender, EventArgs e) { ((frmSimpleGui)cx.Param["ParentForm"]).setDraggedNode(lblName.getText, ((MenuItem)sender).getText); rText_OnDragDrop(mouseDownControl); }

 protected void lText_OnDragDrop      (JComponent                    sender) throws Exception { }
 protected void mText_OnDragDrop      (JComponent                    sender) throws Exception { }
 protected void rText_OnDragDrop      (JComponent                    sender) throws Exception { }

 public    void lText_DragDrop        (Object sender, DropTargetDropEvent e) throws Exception { lText_OnDragDrop((JComponent)sender); }
 public    void mText_DragDrop        (Object sender, DropTargetDropEvent e) throws Exception { mText_OnDragDrop((JComponent)sender); }
 public    void rText_DragDrop        (Object sender, DropTargetDropEvent e) throws Exception { rText_OnDragDrop((JComponent)sender); }

 public    void lText_DragOver        (Object sender, DropTargetDragEvent e) throws Exception
 {
  e.acceptDrag(0);
 }
  
 public    void mText_DragOver        (Object sender, DropTargetDragEvent e) throws Exception
 {
  e.acceptDrag(0);
 }
  
 public    void rText_DragOver        (Object sender, DropTargetDragEvent e) throws Exception
 {
  e.acceptDrag(0);
 }

 protected void lText_MouseMove       (Object sender,          MouseEvent e) throws Exception { }
 protected void mText_MouseMove       (Object sender,          MouseEvent e) throws Exception { }
 protected void rText_MouseMove       (Object sender,          MouseEvent e) throws Exception { }

 protected void lText_MouseDown       (Object sender,          MouseEvent e) throws Exception { mouseDownControl = ((JComponent)(((JComponent)sender).getParent()) instanceof ctlString) ? (JComponent)(((JComponent)sender).getParent()) : (JComponent)sender; }
 protected void mText_MouseDown       (Object sender,          MouseEvent e) throws Exception { mouseDownControl = ((JComponent)(((JComponent)sender).getParent()) instanceof ctlString) ? (JComponent)(((JComponent)sender).getParent()) : (JComponent)sender; }
 protected void rText_MouseDown       (Object sender,          MouseEvent e) throws Exception { mouseDownControl = ((JComponent)(((JComponent)sender).getParent()) instanceof ctlString) ? (JComponent)(((JComponent)sender).getParent()) : (JComponent)sender; }

 protected void lText_MouseUp         (Object sender,          MouseEvent e) throws Exception {                                        }
 protected void mText_MouseUp         (Object sender,          MouseEvent e) throws Exception {                                        }
 protected void rText_MouseUp         (Object sender,          MouseEvent e) throws Exception {                                        }

 protected void lText_Click           (Object sender,          MouseEvent e) throws Exception {                                        }
 protected void mText_Click           (Object sender,          MouseEvent e) throws Exception {                                        }
 protected void rText_Click           (Object sender,          MouseEvent e) throws Exception {                                        }

 protected void lText_DoubleClick     (Object sender,      ComponentEvent e) throws Exception { }
 protected void mText_DoubleClick     (Object sender,      ComponentEvent e) throws Exception { }
 protected void rText_DoubleClick     (Object sender,      ComponentEvent e) throws Exception { }

 protected void lText_TextChanged     (Object sender,      ComponentEvent e) throws Exception { }
 protected void mText_TextChanged     (Object sender,      ComponentEvent e) throws Exception { }
 protected void rText_TextChanged     (Object sender,      ComponentEvent e) throws Exception { }


@SuppressWarnings("unchecked")
 // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
 private void initComponents() {

  lTextMenu = new javax.swing.JPopupMenu();
  mTextMenu = new javax.swing.JPopupMenu();
  rTextMenu = new javax.swing.JPopupMenu();
  lTmpl = new javax.swing.JLabel();
  mTmpl = new javax.swing.JLabel();
  rTmpl = new javax.swing.JTextField();
  fme = new javax.swing.JPanel();
  btnClose = new javax.swing.JButton();

  lTextMenu.addMouseListener(new java.awt.event.MouseAdapter() {
   public void mouseClicked(java.awt.event.MouseEvent evt) {
    lTextMenuMouseClicked(evt);
   }
  });

  lTmpl.setBackground(new java.awt.Color(255, 255, 255));
  lTmpl.setFont(new java.awt.Font("Arial Narrow", 0, 9)); // NOI18N
  lTmpl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
  lTmpl.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
  lTmpl.setOpaque(true);
  lTmpl.setPreferredSize(new java.awt.Dimension(40, 18));
  add(lTmpl);
  lTmpl.setBounds(40, 50, 40, 18);

  mTmpl.setBackground(new java.awt.Color(255, 255, 255));
  mTmpl.setFont(new java.awt.Font("Arial Narrow", 0, 9)); // NOI18N
  mTmpl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
  mTmpl.setText("*");
  mTmpl.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
  mTmpl.setOpaque(true);
  mTmpl.setPreferredSize(new java.awt.Dimension(40, 18));
  add(mTmpl);
  mTmpl.setBounds(90, 50, 40, 18);

  rTmpl.setFont(new java.awt.Font("Arial Narrow", 0, 9)); // NOI18N
  rTmpl.setHorizontalAlignment(javax.swing.JTextField.CENTER);
  rTmpl.setBorder(null);
  rTmpl.setPreferredSize(new java.awt.Dimension(38, 13));
  add(rTmpl);
  rTmpl.setBounds(290, 50, 38, 13);

  fme.setBackground(new java.awt.Color(255, 255, 255));
  fme.setLayout(null);

  btnClose.setBackground(new java.awt.Color(153, 153, 255));
  btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/xxdevplus/gui/closebuttonBig.PNG"))); // NOI18N
  btnClose.setIconTextGap(-11);
  btnClose.addActionListener(new java.awt.event.ActionListener() {
   public void actionPerformed(java.awt.event.ActionEvent evt) {
    btnCloseActionPerformed(evt);
   }
  });
  fme.add(btnClose);
  btnClose.setBounds(280, 10, 22, 22);

  add(fme);
  fme.setBounds(50, 90, 410, 200);
 }// </editor-fold>//GEN-END:initComponents


private void mTextMenuMouseClicked(java.awt.event.MouseEvent evt) {
 try { mTextMenu_Click(evt.getSource()); } catch (Exception ex) { Logger.getLogger(ctlTreeNodes.class.getName()).log(Level.SEVERE, null, ex); }
}

private void rTextMenuMouseClicked(java.awt.event.MouseEvent evt) {
 try { rTextMenu_Click(evt.getSource()); } catch (Exception ex) { Logger.getLogger(ctlTreeNodes.class.getName()).log(Level.SEVERE, null, ex); }
}



private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
 try { btnClose_Click(evt.getSource(), evt); } catch (Exception ex) { Logger.getLogger(ctlTreeNodes.class.getName()).log(Level.SEVERE, null, ex); }
}//GEN-LAST:event_btnCloseActionPerformed

private void lTextMenuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lTextMenuMouseClicked
 try { lTextMenu_Click(evt.getSource()); } catch (Exception ex) { Logger.getLogger(ctlTreeNodes.class.getName()).log(Level.SEVERE, null, ex); }
}//GEN-LAST:event_lTextMenuMouseClicked


 // Variables declaration - do not modify//GEN-BEGIN:variables
 protected javax.swing.JButton btnClose;
 protected javax.swing.JPanel fme;
 protected javax.swing.JPopupMenu lTextMenu;
 protected javax.swing.JLabel lTmpl;
 protected javax.swing.JPopupMenu mTextMenu;
 protected javax.swing.JLabel mTmpl;
 protected javax.swing.JPopupMenu rTextMenu;
 protected javax.swing.JTextField rTmpl;
 // End of variables declaration//GEN-END:variables


@Override public void mouseDragged       (MouseEvent       e)
{
 super.mouseDragged(e);
 try
 {
  if (e.getSource() instanceof ctlString)  if (lText.Contains(false, (ctlString)(((JComponent)e.getSource()).getParent()))) lText_MouseMove(e.getSource(), e);
  if (e.getSource() instanceof ctlString)  if (mText.Contains(false, (ctlString)(((JComponent)e.getSource()).getParent()))) mText_MouseMove(e.getSource(), e);
  if (e.getSource() instanceof ctlString)  if (rText.Contains(false, (ctlString)(((JComponent)e.getSource()).getParent()))) rText_MouseMove(e.getSource(), e);
 }
 catch (Exception ex)
 {
  Logger.getLogger(ctlTreeNodes.class.getName()).log(Level.SEVERE, null, ex);
 }
}
 
@Override public void mouseMoved         (MouseEvent       e)
{
 super.mouseMoved(e);
 try
 {
  if (((JComponent)e.getSource()).getParent() instanceof ctlString)  if (lText.Contains(false, (ctlString)(((JComponent)e.getSource()).getParent()))) lText_MouseMove(e.getSource(), e);
  if (((JComponent)e.getSource()).getParent() instanceof ctlString)  if (mText.Contains(false, (ctlString)(((JComponent)e.getSource()).getParent()))) mText_MouseMove(e.getSource(), e);
  if (((JComponent)e.getSource()).getParent() instanceof ctlString)  if (rText.Contains(false, (ctlString)(((JComponent)e.getSource()).getParent()))) rText_MouseMove(e.getSource(), e);
 }
 catch (Exception ex)
 {
  Logger.getLogger(ctlTreeNodes.class.getName()).log(Level.SEVERE, null, ex);
 }
}
 
@Override public void mouseClicked       (MouseEvent       e)
{
 super.mouseClicked(e);
 try
 {
  if (((JComponent)e.getSource()).getParent() instanceof ctlString)  if (lText.Contains(false, (ctlString)(((JComponent)e.getSource()).getParent()))) if (e.getClickCount() > 1) lText_DoubleClick(e.getSource(), e);
  if (((JComponent)e.getSource()).getParent() instanceof ctlString)  if (mText.Contains(false, (ctlString)(((JComponent)e.getSource()).getParent()))) if (e.getClickCount() > 1) mText_DoubleClick(e.getSource(), e);
  if (((JComponent)e.getSource()).getParent() instanceof ctlString)  if (rText.Contains(false, (ctlString)(((JComponent)e.getSource()).getParent()))) if (e.getClickCount() > 1) rText_DoubleClick(e.getSource(), e);
 }
 catch (Exception ex)
 {
  Logger.getLogger(ctlTreeNodes.class.getName()).log(Level.SEVERE, null, ex);
 }
}

@Override public void mousePressed       (MouseEvent       e)
{
 super.mousePressed(e);
 try
 {
  if (((JComponent)e.getSource()) instanceof ctlString)
  {
   if (lText.Contains(false, (ctlString) (((JComponent) e.getSource())))) { lText_MouseDown(e.getSource(), e); }
   if (mText.Contains(false, (ctlString) (((JComponent) e.getSource())))) { mText_MouseDown(e.getSource(), e); }
   if (rText.Contains(false, (ctlString) (((JComponent) e.getSource())))) { rText_MouseDown(e.getSource(), e); }
  }
  if (((JComponent)e.getSource()).getParent() instanceof ctlString)
  {
   if (lText.Contains(false, (ctlString) (((JComponent) e.getSource()).getParent()))) { lText_MouseDown(((JComponent)e.getSource()).getParent(), e); }
   if (mText.Contains(false, (ctlString) (((JComponent) e.getSource()).getParent()))) { mText_MouseDown(((JComponent)e.getSource()).getParent(), e); }
   if (rText.Contains(false, (ctlString) (((JComponent) e.getSource()).getParent()))) { rText_MouseDown(((JComponent)e.getSource()).getParent(), e); }
  }
  if (((JComponent)e.getSource()) instanceof ctlString)
  {
  }
 }
 catch (Exception ex)
 {
  Logger.getLogger(ctlTreeNodes.class.getName()).log(Level.SEVERE, null, ex);
 }
}

@Override public void mouseReleased      (MouseEvent       e)
{
 super.mouseReleased(e);
}
 
@Override public void mouseEntered       (MouseEvent       e)
{
 super.mouseEntered(e);
}
 
@Override public void mouseExited        (MouseEvent       e)
{
 super.mouseExited(e);
}

 public void lTextMousePressed       (MouseEvent evt) { try  { lText_MouseDown (((JComponent)(evt.getSource())).getParent(), evt); } catch (Exception e) {} }
 public void mTextMousePressed       (MouseEvent evt) { try  { mText_MouseDown (((JComponent)(evt.getSource())).getParent(), evt); } catch (Exception e) {} }
 public void rTextMousePressed       (MouseEvent evt) { try  { rText_MouseDown (((JComponent)(evt.getSource())).getParent(), evt); } catch (Exception e) {} }

 public void lTextMouseReleased      (MouseEvent evt) { try  { lText_MouseUp   (((JComponent)(evt.getSource())).getParent(), evt); } catch (Exception e) {} }
 public void mTextMouseReleased      (MouseEvent evt) { try  { mText_MouseUp   (((JComponent)(evt.getSource())).getParent(), evt); } catch (Exception e) {} }
 public void rTextMouseReleased      (MouseEvent evt) { try  { rText_MouseUp   (((JComponent)(evt.getSource())).getParent(), evt); } catch (Exception e) {} }

 public void lTextMouseClicked       (MouseEvent evt) { try  { lText_Click     (((JComponent)(evt.getSource())).getParent(), evt); } catch (Exception e) {} }
 public void mTextMouseClicked       (MouseEvent evt) { try  { mText_Click     (((JComponent)(evt.getSource())).getParent(), evt); } catch (Exception e) {} }
 public void rTextMouseClicked       (MouseEvent evt) { try  { rText_Click     (((JComponent)(evt.getSource())).getParent(), evt); } catch (Exception e) {} }

 protected void xTextMenu_Click(Object sender, ActionEvent e) throws Exception
 {
  //((frmPublisher)cx.Param().g("ParentForm")).setDraggedNode("", ddHandle.getText(), ((JMenuItem)sender).getText());
  if (mouseDownControl.getLocation().x == lText.g(1).getLocation().x) lTextMenu_Click(e.getSource());
  if (mouseDownControl.getLocation().x == mText.g(1).getLocation().x) mTextMenu_Click(e.getSource());
  if (mouseDownControl.getLocation().x == rText.g(1).getLocation().x) rTextMenu_Click(e.getSource());
 }

 @Override public void actionPerformed(ActionEvent e)
 {
  try
  {
   if (e.getSource() instanceof JMenuItem) xTextMenu_Click(e.getSource(), e);
  }
  catch (Exception ex)
  {
   Logger.getLogger(ctlTreeNodes.class.getName()).log(Level.SEVERE, null, ex);
  }
 }


}

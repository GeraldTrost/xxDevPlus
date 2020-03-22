
//** @author  GeTr Copyright© 1996-2020 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties
//** @license You may use, modify, distribute this code under GNU public dual licence free for NON-Commercial use as long as this notice remains unchanged.
//** @comment Draggable Canvas Control


package org.xxdevplus.gui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.JPanel;
import org.xxdevplus.utl.ctx;
import org.xxdevplus.utl.utl;

/**
 *
 * @author GeTr
 */
public class ctlCanvas extends JPanel implements ActionListener, DragSourceListener, DropTargetListener, MouseMotionListener, MouseListener
{
 //Copyright© 1996-2020 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties, You may use, modify, distribute this code under GNU public licence for NON-Commercial use as long as this notice remains unchanged.
 private static boolean selfTested  = false;  private static String ass(boolean expr) throws Exception { if (!expr) throw new Exception(ass(true) + " SelfTest Failure"); return "ctlCanvas"; } private static void selfTest() { selfTested = true; } private void init() { if (!selfTested) selfTest(); }

 protected Point       TraceStart                                        = new Point(0, 0);
 protected Point       TraceEnd                                          = new Point(0, 0);
 public    boolean     Paintable                                         = false;
 public    boolean     Sizeable                                          = true;
 protected boolean     Initialized                                       = false;
 private   boolean     dragging                                          = false;
 private   Point       dragCursor                                        = new Point();
 public    ctx         cx                                                = null;

 public    void        chgLeft       (long       delta)                  { utl.chgLeft     (this, delta)    ;}
 public    void        chgTop        (long       delta)                  { utl.chgTop      (this, delta)    ;}
 public    void        chgWidth      (long       delta)                  { utl.chgWidth    (this, delta)    ;}
 public    void        chgHeight     (long       delta)                  { utl.chgHeight   (this, delta)    ;}
 public    void        chgLocation   (long dx, long dy)                  { utl.chgLocation (this, dx, dy)   ;}
 public    void        chgSize       (long dw, long dh)                  { utl.chgSize     (this, dw, dh)   ;}

 protected void        chgCursor     (Cursor    cursor)                  { setCursor       ( cursor                                                                                          ); }
 protected void        chgCursor     (int       cursor)                  { setCursor       ( new Cursor ( cursor )                                                                           ); }
 public    void        stopDragSize  (                )                  { dragCursor      = new Point(); /* Capture = false; */ getParent().repaint(); setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR) ); }
 public    int         resizing                                          = 0;
 public    int         moving                                            = 0;

 public                ctlCanvas (                )                  { super(); try {this.cx = new ctx(); } catch (Exception ex) {} addMouseMotionListener(this); addMouseListener(this); /*GlobalScreen.getInstance().addNativeKeyListener(this);*/ initComponents(); }
 public                ctlCanvas (ctx           cx) throws Exception { super(); this.cx = cx; ((JComponent)cx.Param().g("Canvas")).add(this); addMouseMotionListener(this); addMouseListener(this);  /*GlobalScreen.getInstance().addNativeKeyListener(this);*/ initComponents(); }

 protected void OnResize              () throws Exception   { ddHandle.setLocation(-1, -1); ddHandle.setSize( getSize().width + 2, 22); }
 protected void OnMove                () throws Exception   {  }

 protected void OnMouseDown(Object sender, MouseEvent e) throws Exception
 {
  if (sender == ddHandle) { TraceStart = new Point((int)(e.getX() + ddHandle.getLocation().getX()), (int)(e.getY() + ddHandle.getLocation().getY())); } else if (sender == this) { TraceStart = new Point(e.getX(), e.getY()); }
  if ((getCursor().getType() == Cursor.N_RESIZE_CURSOR) || (getCursor().getType() == Cursor.S_RESIZE_CURSOR) || (getCursor().getType() == Cursor.E_RESIZE_CURSOR) || (getCursor().getType() == Cursor.W_RESIZE_CURSOR) || (getCursor().getType() == Cursor.NW_RESIZE_CURSOR) || (getCursor().getType() == Cursor.NE_RESIZE_CURSOR) || (getCursor().getType() == Cursor.SW_RESIZE_CURSOR) || (getCursor().getType() == Cursor.SE_RESIZE_CURSOR) || (getCursor().getType() == Cursor.HAND_CURSOR))
  {
   if (new ctx().keysMask(e, 0, 0, 0, 0, 1, 0, -1, -1)) //test if left Mouse Button is pressed And Shift is released and Ctrl is released
   {
    utl.BringToFront(this);
    //((Control)sender).Capture  = true;
    dragCursor = e.getPoint();
    return;
   }
  }
  stopDragSize();
 }

 @Override public void paint(Graphics g) { super.paint(g); }

 protected void OnMouseMove(Object sender, MouseEvent e) throws Exception
 {
  if ((Paintable) && ((TraceStart.getX() != 0) || (TraceStart.getY() != 0)))
  {
   Graphics g2d = (Graphics2D)e.getComponent().getGraphics();
   if ((TraceEnd.getX() != 0) || (TraceEnd.getY() != 0))
   {
    g2d.setColor(Color.GREEN);
    g2d.setXORMode(getBackground());
    g2d.drawLine((int) TraceStart.getX(), (int) TraceStart.getY(), (int) TraceEnd.getX(), (int) TraceEnd.getY());
    g2d.dispose();
   }
   TraceEnd = new Point(e.getX(), e.getY());
   g2d = (Graphics2D)e.getComponent().getGraphics();
   g2d.setColor(Color.GREEN);
   g2d.setXORMode(getBackground());
   //g2d.setPaintMode();
   g2d.drawLine((int)TraceStart.getX(), (int)TraceStart.getY(), (int)TraceEnd.getX(), (int)TraceEnd.getY());
   g2d.dispose();
  }
  if ((getCursor().getType() == Cursor.N_RESIZE_CURSOR) || (getCursor().getType() == Cursor.S_RESIZE_CURSOR) || (getCursor().getType() == Cursor.E_RESIZE_CURSOR) || (getCursor().getType() == Cursor.W_RESIZE_CURSOR) || (getCursor().getType() == Cursor.NW_RESIZE_CURSOR) || (getCursor().getType() == Cursor.NE_RESIZE_CURSOR) || (getCursor().getType() == Cursor.SW_RESIZE_CURSOR) || (getCursor().getType() == Cursor.SE_RESIZE_CURSOR) || (getCursor().getType() == Cursor.HAND_CURSOR))
  {
   if (new ctx().keysMask(e, 0, 0, 0, 0, 1, 0, -1, -1)) //test if left Mouse Button is pressed And Shift is released and Ctrl is released
   {
    long deltaX         =  (long) (e.getX() - dragCursor.getX());
    long deltaY         =  (long) (e.getY() - dragCursor.getY());
    if (getCursor().getType() == Cursor.HAND_CURSOR) { try { chgLocation (deltaX, deltaY); } catch (Exception ex) {}; return; }  // { chgLeft(deltaX); chgTop(deltaY); return; }
    if ((getCursor().getType() == Cursor.N_RESIZE_CURSOR)  || (getCursor().getType() == Cursor.S_RESIZE_CURSOR))    { if (dragCursor.getY() <= 5) { chgHeight (-deltaY); chgTop   (deltaY); } else { chgHeight (deltaY); dragCursor =  e.getPoint(); } return; }
    if ((getCursor().getType() == Cursor.E_RESIZE_CURSOR)  || (getCursor().getType() == Cursor.E_RESIZE_CURSOR))    { if (dragCursor.getX() <= 5) { chgWidth  (-deltaX); chgLeft  (deltaX); } else { chgWidth  (deltaX); dragCursor =  e.getPoint(); } return; }
    if ((getCursor().getType() == Cursor.NW_RESIZE_CURSOR) || (getCursor().getType() == Cursor.SE_RESIZE_CURSOR))   { if (dragCursor.getY() <= 5) { chgLeft   (deltaX);  chgTop   (deltaY); chgWidth  (-deltaX); chgHeight (-deltaY); } else { chgWidth (deltaX); chgHeight (deltaY); dragCursor = e.getPoint(); } return; }
    if ((getCursor().getType() == Cursor.NE_RESIZE_CURSOR) || (getCursor().getType() == Cursor.SW_RESIZE_CURSOR))   { if (dragCursor.getY() <= 5) { chgTop    (deltaY);  chgWidth (deltaX); chgHeight (-deltaY); dragCursor   = new Point(e.getX(), (int) dragCursor.getY()); } else { chgLeft (deltaX); chgWidth (-deltaX); chgHeight (deltaY); dragCursor = new Point((int) dragCursor.getX(),e.getY()); } return; }
   }
  }
  //test if no Mouse Button Shift or Ctrl are pressed:
  if (new ctx().keysMask(e, -1, 0, 0, 0, -1, 0, -1, -1))
  {
   //if (sender != this) {chgCursor(Cursor.HAND_CURSOR); return; }
   if (!Sizeable) { chgCursor(Cursor.HAND_CURSOR); return; }
   if (Math.sqrt((0          - e.getX()) * (0          - e.getX()) + (0           - e.getY()) * (0           - e.getY())) < 6) { chgCursor(Cursor.NW_RESIZE_CURSOR); return; }
   if (Math.sqrt((getWidth() - e.getX()) * (getWidth() - e.getX()) + (getHeight() - e.getY()) * (getHeight() - e.getY())) < 6) { chgCursor(Cursor.NW_RESIZE_CURSOR); return; }
   if (Math.sqrt((getWidth() - e.getX()) * (getWidth() - e.getX()) + (0           - e.getY()) * (0           - e.getY())) < 6) { chgCursor(Cursor.NE_RESIZE_CURSOR); return; }
   if (Math.sqrt((0          - e.getX()) * (0          - e.getX()) + (getHeight() - e.getY()) * (getHeight() - e.getY())) < 6) { chgCursor(Cursor.NE_RESIZE_CURSOR); return; }
   if (Math.abs(e.getY())                                                                                                 < 4) { chgCursor(Cursor.S_RESIZE_CURSOR);  return; }
   if (Math.abs(getHeight() - e.getY())                                                                                   < 4) { chgCursor(Cursor.S_RESIZE_CURSOR);  return; }
   if (Math.abs(getWidth()  - e.getX())                                                                                   < 4) { chgCursor(Cursor.E_RESIZE_CURSOR);  return; }
   if (Math.abs(e.getX())                                                                                                 < 4) { chgCursor(Cursor.E_RESIZE_CURSOR);  return; }
   if (Math.abs(e.getY())                                                                                                 < 6) { chgCursor(Cursor.HAND_CURSOR);      return; }
   if (Math.abs(getHeight() - e.getY())                                                                                   < 6) { chgCursor(Cursor.HAND_CURSOR);      return; }
   if (Math.abs(getWidth()  - e.getX())                                                                                   < 6) { chgCursor(Cursor.HAND_CURSOR);      return; }
   if (Math.abs(e.getX())                                                                                                 < 6) { chgCursor(Cursor.HAND_CURSOR);      return; }
   chgCursor(Cursor.HAND_CURSOR);
   return;
   //stopDragSize();
  }
 }

 protected void control_DoubleClick           (Object sender,     MouseEvent e) throws Exception {                            }
 protected void control_MouseDown             (Object sender,     MouseEvent e) throws Exception { OnMouseDown(sender, e)   ; }
 protected void control_MouseMove             (Object sender,     MouseEvent e) throws Exception { OnMouseMove(sender, e)   ; }
 protected void control_MouseUp               (Object sender,     MouseEvent e) throws Exception { stopDragSize()           ; }
 protected void control_MouseLeave            (Object sender,     MouseEvent e) throws Exception { /*stopDragSize()*/           ; }
 protected void ddHandle_MouseDown            (Object sender,     MouseEvent e) throws Exception { OnMouseDown(ddHandle, e) ; }
 protected void ddHandle_MouseMove            (Object sender,     MouseEvent e) throws Exception { OnMouseMove(ddHandle, e) ; }
 protected void ddHandle_MouseUp              (Object sender,     MouseEvent e) throws Exception { stopDragSize()           ; }
 protected void ddHandle_MouseLeave           (Object sender,     MouseEvent e) throws Exception { /*stopDragSize()*/           ; }
 protected void ddHandle_Click                (Object sender, ComponentEvent e) throws Exception {                            }
 protected void ddHandle_DoubleClick          (Object sender, ComponentEvent e) throws Exception {                            }
 protected void control_Resize                (Object sender, ComponentEvent e) throws Exception { if (resizing > 0) return; try { resizing++; OnResize(); } catch (Exception ex) { } finally { resizing--; } }
 protected void control__LocationChanged      (Object sender, ComponentEvent e) throws Exception { if (moving   > 0) return; try { moving++;   OnMove();   } catch (Exception ex) { } finally { moving--;   } }

 private void handleMouseEvent(Object sender, MouseEvent e) throws Exception
 {
  String s = "" + e.getID();
  switch (e.getID())
  {
   case MouseEvent.MOUSE_PRESSED:           control_MouseDown       ( e.getSource(), e); break;
   case MouseEvent.MOUSE_MOVED:
    control_MouseMove       ( e.getSource(), e);
    break;

   case MouseEvent.MOUSE_DRAGGED:           control_MouseMove       ( e.getSource(), e); break;
   case MouseEvent.MOUSE_RELEASED:          control_MouseUp         ( e.getSource(), e); break;
   case MouseEvent.MOUSE_EXITED:            control_MouseLeave      ( e.getSource(), e); break;
   case MouseEvent.ALT_DOWN_MASK:           s = "ALT_DOWN_MASK";           break;
   case MouseEvent.ALT_GRAPH_DOWN_MASK:     s = "ALT_GRAPH_DOWN_MASK";     break;
   case MouseEvent.ALT_GRAPH_MASK:          s = "ALT_GRAPH_MASK";          break;
   case MouseEvent.ALT_MASK:                s = "ALT_MASK";                break;
   case MouseEvent.BUTTON1:                 s = "BUTTON1";                 break;
   case MouseEvent.BUTTON1_DOWN_MASK:       s = "BUTTON1_DOWN_MASK";       break;
   case MouseEvent.BUTTON1_MASK:            s = "BUTTON1_MASK";            break;
   case MouseEvent.BUTTON2:                 s = "BUTTON2";                 break;
   case MouseEvent.BUTTON2_DOWN_MASK:       s = "BUTTON2_DOWN_MASK";       break;
   case MouseEvent.BUTTON3:                 s = "BUTTON3";                 break;
   case MouseEvent.BUTTON3_DOWN_MASK:       s = "BUTTON3_DOWN_MASK";       break;
   case MouseEvent.BUTTON3_MASK:            s = "BUTTON3_MASK";            break;
   case MouseEvent.COMPONENT_FIRST:         s = "COMPONENT_FIRST";         break;
   case MouseEvent.COMPONENT_HIDDEN:        s = "COMPONENT_HIDDEN";        break;
   case MouseEvent.COMPONENT_RESIZED:       s = "COMPONENT_RESIZED";       break;
   case MouseEvent.COMPONENT_SHOWN:         s = "COMPONENT_SHOWN";         break;
   case MouseEvent.CTRL_DOWN_MASK:          s = "CTRL_DOWN_MASK";          break;
   case MouseEvent.MOUSE_CLICKED:           s = "MOUSE_CLICKED";           break;
   case MouseEvent.MOUSE_ENTERED:           s = "MOUSE_ENTERED";           break;
   case MouseEvent.MOUSE_LAST:              s = "MOUSE_LAST";              break;
   case MouseEvent.NOBUTTON:                s = "NOBUTTON";                break;
   case MouseEvent.RESERVED_ID_MAX:         s = "RESERVED_ID_MAX";         break;
   case MouseEvent.SHIFT_DOWN_MASK:         s = "SHIFT_DOWN_MASK";         break;
 //case MouseEvent.BUTTON2_MASK:            s = "BUTTON2_MASK";            break;
 //case MouseEvent.COMPONENT_LAST:          s = "COMPONENT_LAST";          break;
 //case MouseEvent.COMPONENT_MOVED:         s = "COMPONENT_MOVED";         break;
 //case MouseEvent.CTRL_MASK:               s = "CTRL_MASK";               break;
 //case MouseEvent.COMPONENT_EVENT_MASK:    s = "COMPONENT_EVENT_MASK";    break;
 //case MouseEvent.CONTAINER_EVENT_MASK:    s = "CONTAINER_EVENT_MASK";    break;
 //case MouseEvent.META_DOWN_MASK:          s = "META_DOWN_MASK";          break;
 //case MouseEvent.META_MASK:               s = "META_MASK";               break;
 //case MouseEvent.MOUSE_FIRST:             s = "MOUSE_FIRST";             break;
 //case MouseEvent.MOUSE_WHEEL:             s = "MOUSE_WHEEL";             break;
 //case MouseEvent.MOUSE_EVENT_MASK:        s = "MOUSE_EVENT_MASK";        break;
 //case MouseEvent.MOUSE_MOTION_EVENT_MASK: s = "MOUSE_MOTION_EVENT_MASK"; break;
 //case MouseEvent.MOUSE_WHEEL_EVENT_MASK:  s = "MOUSE_WHEEL_EVENT_MASK";  break;
 //case MouseEvent.SHIFT_MASK:              s = "SHIFT_MASK";              break;
  }
  //System.out.println(s);
 }

 /*
 @Override public    void mouseDragged            (MouseEvent       e) { }
 @Override public    void mouseMoved              (MouseEvent       e) { }
 @Override public    void mouseClicked            (MouseEvent       e) { }
 @Override public    void mousePressed            (MouseEvent       e) { }
 @Override public    void mouseReleased           (MouseEvent       e) { }
 @Override public    void mouseEntered            (MouseEvent       e) { }
 @Override public    void mouseExited             (MouseEvent       e) { }
 @Override public    void nativeKeyPressed        (NativeKeyEvent nke) { }
 @Override public    void nativeKeyReleased       (NativeKeyEvent nke) { }
 @Override public    void nativeKeyTyped          (NativeKeyEvent nke) { }
 */

 @Override protected void processMouseMotionEvent (MouseEvent       e) { try { handleMouseEvent(e.getSource(), e); } catch (Exception ex) { Logger.getLogger(ctlCanvas.class.getName()).log(Level.SEVERE, null, ex); } super.processMouseMotionEvent(e);   }
 @Override protected void processMouseEvent       (MouseEvent       e) { try { handleMouseEvent(e.getSource(), e); } catch (Exception ex) { Logger.getLogger(ctlCanvas.class.getName()).log(Level.SEVERE, null, ex); } super.processMouseEvent(e);         }
 @Override protected void processComponentEvent   (ComponentEvent   e) { super.processComponentEvent(e); }

 public void drop(DropTargetDropEvent dtde) {
  throw new UnsupportedOperationException("Not supported yet.");
 }

 public void dragEnter(DragSourceDragEvent dsde) {
  throw new UnsupportedOperationException("Not supported yet.");
 }

 public void dragOver(DragSourceDragEvent dsde) {
  throw new UnsupportedOperationException("Not supported yet.");
 }

 public void dropActionChanged(DragSourceDragEvent dsde) {
  throw new UnsupportedOperationException("Not supported yet.");
 }

 public void dragExit(DragSourceEvent dse) {
  throw new UnsupportedOperationException("Not supported yet.");
 }

 public void dragEnter(DropTargetDragEvent dtde) {
  throw new UnsupportedOperationException("Not supported yet.");
 }

 public void dragOver(DropTargetDragEvent dtde) {
  throw new UnsupportedOperationException("Not supported yet.");
 }

 public void dropActionChanged(DropTargetDragEvent dtde) {
  throw new UnsupportedOperationException("Not supported yet.");
 }

 public void dragExit(DropTargetEvent dte) {
  throw new UnsupportedOperationException("Not supported yet.");
 }

 public void dragDropEnd(DragSourceDropEvent dsde) {
  throw new UnsupportedOperationException("Not supported yet.");
 }




 @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        ddHandle = new javax.swing.JLabel();

        setBackground(new java.awt.Color(65, 105, 255));
        setBorder(new javax.swing.border.MatteBorder(null));
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
        });
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentMoved(java.awt.event.ComponentEvent evt) {
                formComponentMoved(evt);
            }
            public void componentResized(java.awt.event.ComponentEvent evt) {
                formComponentResized(evt);
            }
        });
        setLayout(null);

        ddHandle.setBackground(new java.awt.Color(153, 153, 153));
        ddHandle.setForeground(new java.awt.Color(255, 255, 255));
        ddHandle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ddHandle.setText("ddHandle");
        ddHandle.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        ddHandle.setOpaque(true);
        ddHandle.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ddHandleMouseClicked(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                ddHandleMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                ddHandleMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                ddHandleMouseReleased(evt);
            }
        });
        ddHandle.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                ddHandleMouseDragged(evt);
            }
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                ddHandleMouseMoved(evt);
            }
        });
        add(ddHandle);
        ddHandle.setBounds(20, 0, 235, 22);
    }// </editor-fold>//GEN-END:initComponents

    private void ddHandleMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ddHandleMouseClicked
    try { if (evt.getClickCount() == 1) ddHandle_Click(ddHandle, evt); else if (evt.getClickCount() == 2) ddHandle_DoubleClick(ddHandle, evt); } catch (Exception ex) { Logger.getLogger(ctlCanvas.class.getName()).log(Level.SEVERE, null, ex); }
    }//GEN-LAST:event_ddHandleMouseClicked

    private void ddHandleMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ddHandleMouseDragged
     try { ddHandle_MouseMove(ddHandle, evt); } catch (Exception ex) { Logger.getLogger(ctlCanvas.class.getName()).log(Level.SEVERE, null, ex); }
    }//GEN-LAST:event_ddHandleMouseDragged

    private void ddHandleMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ddHandleMouseMoved
     try { ddHandle_MouseMove(ddHandle, evt); } catch (Exception ex) { Logger.getLogger(ctlCanvas.class.getName()).log(Level.SEVERE, null, ex); }
    }//GEN-LAST:event_ddHandleMouseMoved

    private void ddHandleMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ddHandleMousePressed
     try { ddHandle_MouseDown(ddHandle, evt); } catch (Exception ex) { Logger.getLogger(ctlCanvas.class.getName()).log(Level.SEVERE, null, ex); }
    }//GEN-LAST:event_ddHandleMousePressed

    private void ddHandleMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ddHandleMouseExited
     //try { ddHandle_MouseLeave(ddHandle, evt); } catch (Exception ex) { Logger.getLogger(ctlCanvas.class.getName()).log(Level.SEVERE, null, ex); }
    }//GEN-LAST:event_ddHandleMouseExited

    private void ddHandleMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ddHandleMouseReleased
     try { ddHandle_MouseUp(ddHandle, evt); } catch (Exception ex) { Logger.getLogger(ctlCanvas.class.getName()).log(Level.SEVERE, null, ex); }
    }//GEN-LAST:event_ddHandleMouseReleased

    private void formComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentResized
     try { control_Resize(evt.getSource(), evt); } catch (Exception ex) { Logger.getLogger(ctlCanvas.class.getName()).log(Level.SEVERE, null, ex); }
    }//GEN-LAST:event_formComponentResized

    private void formComponentMoved(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentMoved
     try { control__LocationChanged(evt.getSource(), evt); } catch (Exception ex) { Logger.getLogger(ctlCanvas.class.getName()).log(Level.SEVERE, null, ex); }
    }//GEN-LAST:event_formComponentMoved

    private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked
     try
     {
      if (evt.getClickCount() == 2) control_DoubleClick(evt.getSource(), evt);
     }
     catch (Exception ex) { Logger.getLogger(ctlCanvas.class.getName()).log(Level.SEVERE, null, ex); }
    }//GEN-LAST:event_formMouseClicked

    // Variables declaration - do not modify//GEN-BEGIN:variables
    protected javax.swing.JLabel ddHandle;
    // End of variables declaration//GEN-END:variables


 public void mouseDragged       (MouseEvent       e) {  }
 public void mouseMoved         (MouseEvent       e) {  }
 public void mouseClicked       (MouseEvent       e) {  }
 public void mousePressed       (MouseEvent       e) {  }
 public void mouseReleased      (MouseEvent       e) {  }
 public void mouseEntered       (MouseEvent       e) {  }
 public void mouseExited        (MouseEvent       e) {  }

 public void actionPerformed    (ActionEvent      e) 
 {
  String x = "";
 }

}

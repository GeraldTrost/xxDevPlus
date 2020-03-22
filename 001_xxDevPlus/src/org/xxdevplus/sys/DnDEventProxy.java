
package org.xxdevplus.sys;

import java.awt.Color;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;

public class DnDEventProxy extends DropTargetAdapter
{
 //private Delegate dragEnterHandler           = null;
 private   Delegate dragOverHandler            = null;
 //private Delegate dropActionChangedHandler   = null;
 //private Delegate dragExitHandler            = null;
 private   Delegate dropHandler                = null;
 private   int      ops                        = 0;

 private DropTarget dropTarget; private JComponent control;

 public DnDEventProxy(JComponent control, int ops, Delegate dragOverHandler, Delegate dropHandler)
 {
  this.dragOverHandler           = dragOverHandler;
  this.dropHandler               = dropHandler;
  this.ops                       = ops;
  this.control                   = control;
  dropTarget = new DropTarget(control, ops, this, true, null);
 }

 //@Override public void dragEnter          (DropTargetDragEvent   dtde) { try { dragEnterHandler.call          (new Object[] {panel, dtde}); } catch (Exception ex) { Logger.getLogger(DnDEventProxy.class.getName()).log(Level.SEVERE, null, ex); } }
   @Override public void dragOver           (DropTargetDragEvent   dtde) { try { dragOverHandler.call           (new Object[] {control, dtde}); } catch (Exception ex) { Logger.getLogger(DnDEventProxy.class.getName()).log(Level.SEVERE, null, ex); } }
 //@Override public void dropActionChanged  (DropTargetDragEvent   dtde) { try { dropActionChangedHandler.call  (new Object[] {panel, dtde}); } catch (Exception ex) { Logger.getLogger(DnDEventProxy.class.getName()).log(Level.SEVERE, null, ex); } }
 //@Override public void dragExit           (DropTargetEvent       dtde) { try { dragExitHandler.call           (new Object[] {panel, dtde}); } catch (Exception ex) { Logger.getLogger(DnDEventProxy.class.getName()).log(Level.SEVERE, null, ex); } }

 @Override public void drop               (DropTargetDropEvent  event)
 {
  control.setBackground(Color.WHITE);
  try
  {
   dropHandler.call(new Object[] {control, event});
   //Graphics2D g2d = ((Graphics2D) (control.getGraphics()));
   //g2d.setColor(Color.BLACK);
   //g2d.drawString((String) event.getTransferable().getTransferData(DataFlavor.stringFlavor), 5, 5);
   //g2d.dispose();
   event.acceptDrop(ops);
  }
  catch (Exception e) { e.printStackTrace(); event.rejectDrop(); }
 }
}




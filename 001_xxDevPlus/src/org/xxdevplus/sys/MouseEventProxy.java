
package org.xxdevplus.sys;

import java.awt.dnd.DropTarget;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JComponent;

public class MouseEventProxy extends MouseAdapter
{
 private   Delegate pressedHandler        = null;
 private   Delegate releasedHandler       = null;
 private   Delegate clickedHandler        = null;

 private DropTarget dropTarget; private JComponent control;

 public MouseEventProxy(JComponent control, Delegate pressedHandler, Delegate releasedHandler, Delegate clickedHandler)
 {
  this.pressedHandler    = pressedHandler;
  this.releasedHandler   = releasedHandler;
  this.clickedHandler    = clickedHandler;
  this.control           = control;
  control.addMouseListener(this);
 }

 @Override public void mousePressed   (MouseEvent evt) { try
 {
  pressedHandler.call  (new Object[] {evt});
 }
 catch (Exception e) { e.printStackTrace(); }
 }
 @Override public void mouseReleased  (MouseEvent evt) { try { releasedHandler.call (new Object[] {evt}); } catch (Exception e) { e.printStackTrace(); } }
 @Override public void mouseClicked   (MouseEvent evt) { try { clickedHandler.call  (new Object[] {evt}); } catch (Exception e) { e.printStackTrace(); } }

}




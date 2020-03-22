

package org.xxdevplus.gui;

import java.awt.AWTEvent;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;



public class ctlOval extends JPanel
{

 private void defaultEvents()  //enableEvents(0xFFFFFFFF - ((1 << 4) | (1 << 5) | (1 << 17))); //0xFFFFFFFF - 0x20030 = 0xFFFDFFCF = ALL - (DEC) 16 - (DEC) 32 - (DEC) 131072     //enableEvents(0xFFFFFFFF);
 {
  disableEvents(0xFFFFFFFF);                                              //Events received by:       Conatiner YES        Control NO
  //enableEvents(0xFFFFFFFF - ((1 << 4) | (1 << 5) | (1 << 17)));         //Events received by:       Conatiner YES        Control NO
  //enableEvents(0xFFFFFFFF - ((1 << 5) | (1 << 17)));                    //Events received by:       Conatiner NO         Control YES
  //enableEvents(0xFFFFFFFF - ((1 << 4) | (1 << 17)));                    //Events received by:       Conatiner NO         Control YES
  //enableEvents(0xFFFFFFFF - ((1 << 4) | (1 << 5) ));                    //Events received by:       Conatiner NO         Control NO
  enableEvents(0xFFFFFFFF - ((1 << 17)));                               //Events received by:       Conatiner NO         Control YES
  //enableEvents(0xFFFFFFFF - ((1 << 5) ));                               //Events received by:       Conatiner NO         Control YES
  //enableEvents(0xFFFFFFFF - ((1 << 4) ));                               //Events received by:       Conatiner NO         Control YES
 }


 @Override public boolean contains(int x, int y)
 {
  return (x <= getWidth() / 2) && (y <= getHeight() / 2);
 }
 
 @Override protected void processEvent(AWTEvent e)
 {
  System.out.println(" --- delegated to form");
  super.processEvent(e);
  if (true) return;

  System.out.println(" --- delegated to form");
  disableEvents(1 << 5);
  disableEvents(0xFFFFFFFF);
  Point oldPoint = getLocation();
  setLocation(new Point(0, 0));
  super.processEvent(e);
  setLocation(oldPoint);
  //dispatchEvent(e);
  defaultEvents();
 }

 public ctlOval()
 {
  initComponents();
  //enableEvents(0xFFFDFFCF);

  //enableEvents(1 << 4);   //     16
  //enableEvents(1 << 5);   //     32
  //enableEvents(1 << 17);  // 131072

  defaultEvents();

  /*
  enableEvents(0x0000000F);

  //enableEvents(0x00000010);
  //enableEvents(0x00000020);
  //enableEvents(0x00000030);
  enableEvents(0x00000040);
  //enableEvents(0x00000050);
  //enableEvents(0x00000060);
  //enableEvents(0x00000070);
  enableEvents(0x00000080);
  //enableEvents(0x00000090);
  //enableEvents(0x000000A0);
  //enableEvents(0x000000B0);
  enableEvents(0x000000C0);
  //enableEvents(0x000000D0);
  //enableEvents(0x000000E0);
  //enableEvents(0x000000F0);

  enableEvents(0x00000F00);
  enableEvents(0x0000F000);

  enableEvents(0x00010000);
  //enableEvents(0x00020000);
  //enableEvents(0x00030000);
  enableEvents(0x00040000);
  enableEvents(0x00050000);
  //enableEvents(0x00060000);
  //enableEvents(0x00070000);
  enableEvents(0x00080000);
  enableEvents(0x00090000);
  //enableEvents(0x000A0000);
  //enableEvents(0x000B0000);
  enableEvents(0x000C0000);
  enableEvents(0x000D0000);
  //enableEvents(0x000E0000);
  //enableEvents(0x000F0000);

  enableEvents(0x00F00000);
  enableEvents(0x0F000000);
  enableEvents(0xF0000000);
  */

 }


 @Override public    void paint(Graphics g) { super.paint(g); }

 @Override protected void paintComponent(Graphics g)
 {
  g.setColor( getForeground() );
  g.fillOval( 0, 0, getWidth(), getHeight() );
  g.fillRect( 0, 0, 60, 60 );
 }


 /*
 @Override protected void processEvent            (AWTEvent         e)
 {
  super.processMouseMotionEvent(e) ;
  if ((e.getX() < 60) && (e.getY() < 60)) return;
  e.consume();
 }
 */


 /*
 @Override protected void processMouseMotionEvent (MouseEvent       e) { super.processMouseMotionEvent(e)   ;}
 @Override protected void processMouseEvent       (MouseEvent       e) { super.processMouseEvent(e)         ;}
 @Override protected void processComponentEvent   (ComponentEvent   e) { super.processComponentEvent(e)     ;}
 */

 /*
 @Override public void paint (Graphics g)
 {
  g.setColor( getForeground() );
  g.fillOval( 0, 0, getWidth(), getHeight() );
 }
 */
 
 @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
    }// </editor-fold>//GEN-END:initComponents



    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

}




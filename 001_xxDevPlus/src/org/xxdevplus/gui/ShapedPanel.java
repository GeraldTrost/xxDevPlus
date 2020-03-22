
package org.xxdevplus.gui;

import java.awt.Shape;
import javax.swing.JPanel;




public class ShapedPanel extends JPanel 
{

/*
 implements BackgroundPainter
{

 private Direction direction = Direction.RIGHT;
 private boolean horizontalFlip;
 private boolean verticalFlip;
 private boolean clipChildren;
 private ComponentPainter painter;
 private ShapedBorder shapedBorder;
 private Insets shapedInsets;

 public                     ShapedPanel         (                                       ) { this(new BorderLayout());}
 public                     ShapedPanel         (LayoutManager                         l) { super(l); setOpaque(false); }
 public                     ShapedPanel         (ComponentPainter                painter) { this(); this.painter = painter; }
 public                     ShapedPanel         (ComponentPainter painter, Border border) { this(painter); setBorder(border); }
 public                     ShapedPanel         (Component                     component) { this(); add(component); }
 public    Shape            getShape            (                                       ) { ShapedBorder b = getShapedBorder(); return b == null ? null : b.getShape(this, shapedInsets.left, shapedInsets.top, getWidth() - shapedInsets.left - shapedInsets.right, getHeight() - shapedInsets.top - shapedInsets.bottom); }
 public    ComponentPainter getComponentPainter (                                       ) { return painter; }
 public    void             setComponentPainter (ComponentPainter                painter) { this.painter = painter; repaint(); }
 public    Direction        getDirection        (                                       ) { return direction; }
 public    boolean          isHorizontalFlip    (                                       ) { return horizontalFlip; }
 public    void             setHorizontalFlip   (boolean                  horizontalFlip) { this.horizontalFlip = horizontalFlip; revalidate(); }
 public    boolean          isVerticalFlip      (                                       ) { return verticalFlip; }
 public    void             setVerticalFlip´    (boolean                    verticalFlip) { this.verticalFlip = verticalFlip; revalidate(); }
 public    void             setDirection        (Direction                     direction) { this.direction = direction; revalidate(); repaint(); }
 public    boolean          isClipChildren      (                                       ) { return clipChildren; }
 public    void             setClipChildren     (boolean                    clipChildren) { this.clipChildren = clipChildren; }
 public    ShapedBorder     getShapedBorder     (                                       ) { return shapedBorder; }
 public    void             setBorder           (Border                           border) { super.setBorder(border); shapedBorder = null; findShapedBorder(getBorder(), new Insets(0, 0, 0, 0)); }
 protected void             paintChildren       (Graphics                              g)
 {
  if (clipChildren)
  {
   Shape shape = getShape();
   if (shape != null)
   {
    Graphics2D g2 = (Graphics2D) g;
    Shape clip = g2.getClip();
    g2.clip(shape);
    super.paintChildren(g);
    g2.setClip(clip);
    //ShapedBorder sb = getShapedBorder(); if (sb != null) sb.paintBorder(this, g, shapedInsets.left, shapedInsets.top, getWidth() - shapedInsets.left -shapedInsets.right, getHeight() - shapedInsets.top - shapedInsets.bottom);
    return;
   }
  }
  super.paintChildren(g);
 }

 protected void paintComponent(Graphics g)
 {
  super.paintComponent(g);
  if (painter != null)
  {
   Shape shape = getShape();
   if (shape != null)
   {
    Shape clip = g.getClip();
    g.clipRect(shapedInsets.left, shapedInsets.top, getWidth() - shapedInsets.left - shapedInsets.right, getHeight() - shapedInsets.top - shapedInsets.bottom);
    ((Graphics2D) g).clip(shape);
    painter.paint(this, g, 0, 0, getWidth(), getHeight(), direction, horizontalFlip, verticalFlip);
    g.setClip(clip);
   }
   else painter.paint(this, g, 0, 0, getWidth(), getHeight(), direction, horizontalFlip, verticalFlip);
  }
 }

 public boolean contains(int x, int y) { if (x < 0 || y < 0 || x >= getWidth() || y >= getHeight()) return false; Shape shape = getShape(); return shape == null ? super.contains(x, y) : shape.contains(x, y); }

 public boolean inside(int x, int y) { if (x < 0 || y < 0 || x >= getWidth() || y >= getHeight()) return false; Shape shape = getShape(); return shape == null ? super.inside(x, y) : shape.contains(x, y); }

 private boolean findShapedBorder(Border border, Insets i)  { if (border == null) return false; else if (border instanceof ShapedBorder) { shapedBorder = (ShapedBorder) border; shapedInsets = i; return true; } else if (border instanceof CompoundBorder) { CompoundBorder c = (CompoundBorder) border; if (findShapedBorder(c.getOutsideBorder(), i)) return true; return findShapedBorder(c.getInsideBorder(), InsetsUtil.add(c.getOutsideBorder().getBorderInsets(this), i)); } else return false; }

 public static void main(String[] args)
 {
  tabContentAreaColorPorvider tclrPvd = new tabContentAreaColorPorvider(Color.red);
  RectangleComponentPainter rctCmpPnt = new RectangleComponentPainter (tclrPvd, 1);
  int xPoints[]= {0,200,200,85,50,0,0};
  int yPoints[] ={0,0,80,80,100,100,0};
  PanelContentAreaBorder pnlBrd = new PanelContentAreaBorder(tclrPvd,xPoints, yPoints);
  JFrame frm = new JFrame("test Frame");
  ShapedPanel sp = new ShapedPanel(rctCmpPnt,pnlBrd);
  frm.getContentPane().setLayout(new FlowLayout());
  JButton btn = new JButton("Test Button");
  sp.add(btn);
  //frm.getContentPane().add(sp);
  frm.setVisible(true);
  frm.setState(Frame.MAXIMIZED_BOTH);
 }







*/

 @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

}




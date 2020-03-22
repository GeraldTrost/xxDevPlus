


using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Drawing;
using System.Data;
using System.Linq;
using System.Text;
using System.Windows.Forms;


using org_xxdevplus_utl;
using org_xxdevplus_struct;
using org_xxdevplus_chain;


namespace org_xxdevplus_gui
{
 public partial class ctlCanvas : UserControl
 {
  //Copyright© 1996-2012 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties, You may use, modify, distribute this code under GNU public licence for NON-Commercial use as long as this notice remains unchanged.
  private static bool selfTested  = false;  private static string ass(bool expr) { if (!expr) throw new Exception(ass(true) + " SelfTest Failure"); return "ctlMoveSizeCanvas"; } private static void selfTest() { selfTested = true; } private void init() { if (!selfTested) selfTest(); }

  protected         ctx         cx                                     = new ctx();
  public virtual    ctx         Cx                                     { get{ return cx;} set{cx = value;} }

  protected         Point       TraceStart                             = new Point(0, 0);
  protected         Point       TraceEnd                               = new Point(0, 0);
  public            bool        Paintable                              = false;
  public            bool        Sizeable                               = true;
  protected         bool        Initialized                            = false;
  private           bool        dragging                               = false;
  private           Point       dragCursor                             = Point.Empty;

  public    virtual void        chgLeft       (long    delta)          { utl.chgLeft   (this, delta)      ;}
  public    virtual void        chgTop        (long    delta)          { utl.chgTop    (this, delta)      ;}
  public    virtual void        chgWidth      (long    delta)          { utl.chgWidth  (this, delta)      ;}
  public    virtual void        chgHeight     (long    delta)          { utl.chgHeight (this, delta)      ;}
  public    virtual void        chgLocation   (long dx, long dy)       { utl.chgLocation (this, dx, dy)   ;}
  public    virtual void        chgSize       (long dw, long dh)       { utl.chgSize     (this, dw, dh)   ;}

  protected virtual void        chgCursor     (Cursor cursor)          { Cursor  = cursor; }
  public    virtual void        stopDragSize  (             )          { dragCursor = Point.Empty; Capture = false; Cursor = Cursors.Arrow;}
  public             int        resizing                               = 0;
  public             int        moving                                 = 0;

  public                        ctlCanvas (             ) : base() {                                             InitializeComponent();}
  public                        ctlCanvas (ctx        cx) : base() { this.cx =  cx;                              InitializeComponent();}

  protected virtual void OnMouseDown(object sender, MouseEventArgs e)
  {
   if (sender == ddHandle) { TraceStart = new Point(e.X + ddHandle.Location.X, e.Y + ddHandle.Location.Y); } else if (sender == this) { TraceStart = new Point(e.X, e.Y); }
   if ((Cursor == Cursors.SizeNWSE) || (Cursor  == Cursors.SizeNESW) || (Cursor == Cursors.SizeWE) || (Cursor == Cursors.SizeNS) || (Cursor == Cursors.Hand)) if (new ctx().keysMask(e, 0, 0, 0, 0, 1, 0, -1, -1)) /*left Mouse Button pressed, Shift & Ctrl released*/ { BringToFront(); ((Control)sender).Capture  = true; dragCursor = e.Location; return; }
   stopDragSize();
  }

  protected virtual void OnResize() { ddHandle.Location = new Point(-1, -1); ddHandle.Size = new Size(Size.Width + 2, 22); }
  protected virtual void OnMove()   {  }

  protected virtual void OnMouseMove(object sender, MouseEventArgs e)
  {
   if ((Paintable) && ((TraceStart.X != 0) || (TraceStart.Y != 0)))
   {
    using (Graphics dc = this.CreateGraphics())
    {
     if ((TraceEnd.X != 0) || (TraceEnd.Y != 0))
     {
      //g2d.setColor(Color.GREEN);
      //g2d.setXORMode(getBackground());
      utl.DrawLine(dc, utl.PenStyles.PS_SOLID, 1, Color.Green, (int)TraceStart.X, (int)TraceStart.Y, (int)TraceEnd.X, (int)TraceEnd.Y);
     }
     TraceEnd = new Point(e.X, e.Y);
     //g2d.setColor(Color.GREEN);
     //g2d.setXORMode(getBackground());
     //g2d.setPaintMode();
     utl.DrawLine(dc, utl.PenStyles.PS_SOLID, 1, Color.Green, (int)TraceStart.X, (int)TraceStart.Y, (int)TraceEnd.X, (int)TraceEnd.Y);
    }
   }
   if ((Cursor == Cursors.SizeNWSE) || (Cursor == Cursors.SizeNESW) || (Cursor == Cursors.SizeWE) || (Cursor == Cursors.SizeNS) || (Cursor == Cursors.Hand))
   {
    if (new ctx().keysMask(e, 0, 0, 0, 0, 1, 0, -1, -1)) /*left Mouse Button pressed, Shift & Ctrl released*/
    {
     long deltaX         =  e.X - dragCursor.X;
     long deltaY         =  e.Y - dragCursor.Y;
     if (Cursor == Cursors.Hand)     { try { chgLocation (deltaX, deltaY); } catch (Exception ex) {}; return; }  // { chgLeft(deltaX); chgTop(deltaY); return; }
     if (Cursor == Cursors.SizeNS)   { if (dragCursor.Y <= 5) { chgHeight (-deltaY); chgTop   (deltaY); } else { chgHeight (deltaY); dragCursor =  e.Location; } return; }
     if (Cursor == Cursors.SizeWE)   { if (dragCursor.X <= 5) { chgWidth  (-deltaX); chgLeft  (deltaX); } else { chgWidth  (deltaX); dragCursor =  e.Location; } return; }
     if (Cursor == Cursors.SizeNWSE) { if (dragCursor.Y <= 5) { chgLeft   (deltaX);  chgTop   (deltaY); chgWidth  (-deltaX); chgHeight (-deltaY); } else { chgWidth (deltaX); chgHeight (deltaY); dragCursor = e.Location; } return; }
     if (Cursor == Cursors.SizeNESW) { if (dragCursor.Y <= 5) { chgTop    (deltaY);  chgWidth (deltaX); chgHeight (-deltaY); dragCursor   = new Point(e.Location.X, dragCursor.Y); } else { chgLeft (deltaX); chgWidth (-deltaX); chgHeight (deltaY); dragCursor = new Point(dragCursor.X, e.Location.Y); } return; }
    }
   }
   //test if no Mouse Button Shift or Ctrl are pressed:
   if (new ctx().keysMask(e, -1, 0, 0, 0, -1, 0, -1, -1))
   {
    if (sender != this) {chgCursor(Cursors.Hand); return; }
    if (!Sizeable) { chgCursor(Cursors.Hand); return; }
    if (Math.Sqrt((0     - e.X) * (0     - e.X) + (0      - e.Y) * (0      - e.Y)) < 6) { chgCursor(Cursors.SizeNWSE); return; }
    if (Math.Sqrt((Width - e.X) * (Width - e.X) + (Height - e.Y) * (Height - e.Y)) < 6) { chgCursor(Cursors.SizeNWSE); return; }
    if (Math.Sqrt((Width - e.X) * (Width - e.X) + (0      - e.Y) * (0      - e.Y)) < 6) { chgCursor(Cursors.SizeNESW); return; }
    if (Math.Sqrt((0     - e.X) * (0     - e.X) + (Height - e.Y) * (Height - e.Y)) < 6) { chgCursor(Cursors.SizeNESW); return; }
    if (Math.Abs(e.Y)                                                              < 4) { chgCursor(Cursors.SizeNS);   return; }
    if (Math.Abs(Height  - e.Y)                                                    < 4) { chgCursor(Cursors.SizeNS);   return; }
    if (Math.Abs(Width   - e.X)                                                    < 4) { chgCursor(Cursors.SizeWE);   return; }
    if (Math.Abs(e.X)                                                              < 4) { chgCursor(Cursors.SizeWE);   return; }
    if (Math.Abs(e.Y)                                                              < 6) { chgCursor(Cursors.Hand);     return; }
    if (Math.Abs(Height  - e.Y)                                                    < 6) { chgCursor(Cursors.Hand);     return; }
    if (Math.Abs(Width   - e.X)                                                    < 6) { chgCursor(Cursors.Hand);     return; }
    if (Math.Abs(e.X)                                                              < 6) { chgCursor(Cursors.Hand);     return; }
    chgCursor(Cursors.Hand);
    return;
    //stopDragSize();
   }
  }

  protected virtual void        control_DoubleClick     (object sender,      EventArgs e) {                             }
  protected virtual void        control_MouseDown       (object sender, MouseEventArgs e) { OnMouseDown(sender, e)    ; }
  protected virtual void        control_MouseMove       (object sender, MouseEventArgs e) { OnMouseMove(sender, e)    ; }
  protected virtual void        control_MouseUp         (object sender, MouseEventArgs e) { stopDragSize()            ; }
  protected virtual void        control_MouseLeave      (object sender, EventArgs      e) { stopDragSize()            ; }
  protected virtual void        ddHandle_MouseDown      (object sender, MouseEventArgs e) { OnMouseDown(ddHandle, e)  ; }
  protected virtual void        ddHandle_MouseMove      (object sender, MouseEventArgs e) { OnMouseMove(ddHandle, e)  ; }
  protected virtual void        ddHandle_MouseUp        (object sender, MouseEventArgs e) { stopDragSize()            ; }
  protected virtual void        ddHandle_MouseLeave     (object sender,      EventArgs e) { stopDragSize()            ; }
  protected virtual void        ddHandle_Click          (object sender,      EventArgs e) {                             }
  protected virtual void        ddHandle_DoubleClick    (object sender,      EventArgs e) {                             }
  protected virtual void        control_Resize          (object sender,      EventArgs e) { if (resizing > 0) return; try { resizing++; OnResize(); } catch (Exception ex) { } finally { resizing--; } }
  protected virtual void        control__LocationChanged(object sender, EventArgs e) { if (moving   > 0) return; try { moving++; OnMove(); } catch (Exception ex) { } finally { moving--; } }

 }
}









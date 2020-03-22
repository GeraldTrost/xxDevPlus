

//** @author  GeTr Copyright© 1996-2020 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties
//** @license You may use, modify, distribute this code under GNU public dual licence free for NON-Commercial use as long as this notice remains unchanged.
//** @comment 3-dim Vector

package org.xxdevplus.math;

 public class g3PilVec extends Val<Double>
 {
  private static boolean selfTested  = false;  private static String ass(boolean expr) throws Exception { if (!expr) throw new Exception(ass(true) + " SelfTest Failure"); return "g3PilVec"; } private static void selfTest() { selfTested = true; } private void init() { if (!selfTested) selfTest(); }
 

  public double X() { return this.g(1, 1, 1); } public void X(double value)  { this.s(value, 1, 1, 1); } 
  public double Y() { return this.g(1, 1, 2); } public void Y(double value)  { this.s(value, 1, 1, 2); } 
  public double Z() { return this.g(1, 1, 3); } public void Z(double value)  { this.s(value, 1, 1, 3); } 
 
  public g3PilVec(double x, double y, double z) throws Exception { super(1, 1, 3); this.X(x); this.Y(y); this.Z(z);  }
  public g3PilVec(g3PilVec cloneFrom) throws Exception { super(3, 1, 1); this.X(cloneFrom.X()); this.Y(cloneFrom.Y()); this.Z(cloneFrom.Z()); } 

 }




//** @author  GeTr Copyright© 1996-2020 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties
//** @license You may use, modify, distribute this code under GNU public dual licence free for NON-Commercial use as long as this notice remains unchanged.
//** @comment 3-dim Vector

package org.xxdevplus.math;

 public class g3ColVec extends Val<Double>
 {
  private static boolean selfTested  = false;  private static String ass(boolean expr) throws Exception { if (!expr) throw new Exception(ass(true) + " SelfTest Failure"); return "g3ColVec"; } private static void selfTest() { selfTested = true; } private void init() { if (!selfTested) selfTest(); }

  public double X() { return g(1, 1, 1); } public void X(double value)  { s(value, 1, 1, 1); } 
  public double Y() { return g(1, 2, 1); } public void Y(double value)  { s(value, 1, 2, 1); } 
  public double Z() { return g(1, 3, 1); } public void Z(double value)  { s(value, 1, 3, 1); } 
 
  public g3ColVec(double x, double y, double z) throws Exception { super(1, 3, 1); X(x); Y(y); Z(z); }
  public g3ColVec(g3ColVec cloneFrom) throws Exception {super(3, 1, 1); X(cloneFrom.X()); Y(cloneFrom.Y()); Z(cloneFrom.Z()); }

 }
 



//** @author  GeTr Copyright© 1996-2020 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties
//** @license You may use, modify, distribute this code under GNU public dual licence free for NON-Commercial use as long as this notice remains unchanged.
//** @comment 3-dim Vector

package org.xxdevplus.math;

 public class g3RowColVec extends Val<Double>
 {
  private static boolean selfTested  = false;  private static String ass(boolean expr) throws Exception { if (!expr) throw new Exception(ass(true) + " SelfTest Failure"); return "g3RowColVec"; } private static void selfTest() { selfTested = true; } private void init() { if (!selfTested) selfTest(); }
 
  public g3RowColVec(int rows, int cols, double... vals) throws Exception
  {
   dim = new int[3];
   dim[0] = rows; 
   dim[1] = cols;
   dim[2] = 1;
   for(int i = 1; i <= IterationCoords.Len() ; i++) this.s(vals[i - 1], IterationCoords.g(i));
  }

 }

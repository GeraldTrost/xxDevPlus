

//** @author  GeTr Copyright© 1996-2020 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties
//** @license You may use, modify, distribute this code under GNU public dual licence free for NON-Commercial use as long as this notice remains unchanged.
//** @comment Storing Matrix in Cantor Diagonal Form

package org.xxdevplus.math;

public class CMatrix
{
 private static boolean selfTested  = false;  private static String ass(boolean expr) throws Exception { if (!expr) throw new Exception(ass(true) + " SelfTest Failure"); return "CMatrix"; } private static void selfTest() { selfTested = true; } private void init() { if (!selfTested) selfTest(); }

 private long dim;

 private long cantor(long x, long y) { return (long) (0.5 * (x + y) * (x + y + 1) + y); }

 private long cantor(long[] x)
 {
  if (x.length == 1) return x[0];
  if (x.length == 2) return cantor(x[0], x[1]);
  long[] y = new long[x.length - 1];
  for (long i = 0; i < y.length; i++) y[(int)i] = x[(int)i];
  return cantor(cantor(y), x[x.length - 1]);
 }

 public CMatrix(long dim)
 {
  this.dim = dim;
 }

 private long[] next(long[] current)
 {
  long[] ret = new long[current.length];
  return ret;
 }

}

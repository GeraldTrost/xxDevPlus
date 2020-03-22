

//** @author  GeTr Copyright© 1996-2020 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties
//** @license You may use, modify, distribute this code under GNU public dual licence free for NON-Commercial use as long as this notice remains unchanged.
//** @comment Numbering System for Cantor Diagonals


//** @author  GeTr Copyright© 1996-2020 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties
//** @license You may use, modify, distribute this code under GNU public dual licence free for NON-Commercial use as long as this notice remains unchanged.
//** @comment Storing Matrix in Cantor Diagonal Form


using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace org_xxdevplus_math
{
 public class CMatrix
 {
  //Copyright© 1996-2012 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties, You may use, modify, distribute this code under GNU public licence for NON-Commercial use as long as this notice remains unchanged.
  private static bool selfTested  = false;  private static string ass(bool expr) { if (!expr) throw new Exception(ass(true) + " SelfTest Failure"); return "CMatrix"; } private static void selfTest() { selfTested = true; } private void init() { if (!selfTested) selfTest(); }
 
  private long dim;

  private long cantor(long x, long y) { return (long) (0.5 * (x + y) * (x + y + 1) + y); }

  private long cantor(long[] x)
  {
   if (x.Length == 1) return x[0];
   if (x.Length == 2) return cantor(x[0], x[1]);
   long[] y = new long[x.Length - 1];
   for (long i = 0; i < y.Length; i++) y[i] = x[i];
   return cantor(cantor(y), x[x.Length - 1]);
  }

  public CMatrix(long dim)
  {
   this.dim = dim;
  }

  private long[] next(long[] current)
  {
   long[] ret = new long[current.Length];
   return ret;
  }
  
 }
}

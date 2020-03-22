


//** @author  GeTr Copyright© 1996-2020 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties
//** @license You may use, modify, distribute this code under GNU public dual licence free for NON-Commercial use as long as this notice remains unchanged.
//** @comment 3-dim Vector


using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace org_xxdevplus_math
{
 public class g3RowColVec : Val<double>
 {
  private static bool selfTested  = false;  private static string ass(bool expr) { if (!expr) throw new Exception(ass(true) + " SelfTest Failure"); return "g3RowColVec"; } private static void selfTest() { selfTested = true; } private void init() { if (!selfTested) selfTest(); }
 
  public g3RowColVec(int rows, int cols, params double[] vals)
  {
   dim = new int[3];
   dim[0] = rows;
   dim[1] = cols;
   dim[2] = 1;
   for(int i = 1; i <= IterationCoords.Len ; i++) this[IterationCoords[i]] = vals[i - 1];
  }

 }
}

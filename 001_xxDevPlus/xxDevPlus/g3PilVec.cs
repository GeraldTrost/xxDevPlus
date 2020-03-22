

//** @author  GeTr Copyright© 1996-2020 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties
//** @license You may use, modify, distribute this code under GNU public dual licence free for NON-Commercial use as long as this notice remains unchanged.
//** @comment 3-dim Vector

using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace org_xxdevplus_math
{
 public class g3PilVec : Val<double>
 {
  //Copyright© 1996-2012 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties, You may use, modify, distribute this code under GNU public licence for NON-Commercial use as long as this notice remains unchanged.
  private static bool selfTested  = false;  private static string ass(bool expr) { if (!expr) throw new Exception(ass(true) + " SelfTest Failure"); return "g3PilVec"; } private static void selfTest() { selfTested = true; } private void init() { if (!selfTested) selfTest(); }
 

  public double x { get { return this[1, 1, 1]; } set { this[1, 1, 1] = value; } }
  public double y { get { return this[1, 1, 2]; } set { this[1, 1, 2] = value; } }
  public double z { get { return this[1, 1, 3]; } set { this[1, 1, 3] = value; } }
 
  public g3PilVec(double x, double y, double z) : base(1, 1, 3) {this.x = x; this.y = y; this.z = z; }
  public g3PilVec(g3PilVec cloneFrom)           : base(3, 1, 1) {this.x = cloneFrom.x; this.y = cloneFrom.y; this.z = cloneFrom.z; }

 }
 
 
}

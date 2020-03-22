using System;

namespace ndString
{
 public class RchOp
 {
  //Copyright© 1996-2012 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties, You may use, modify, distribute this code under GNU public licence for NON-Commercial use as long as this notice remains unchanged.
  private static bool selfTested  = false; private static string ass(bool expr) { if (!expr) throw new Exception(ass(true) + " SelfTest Failure"); return "RchOp"; } private static void selfTest() { selfTested = true; } private void init() { if (!selfTested) selfTest(); }

  internal bool     bufop   = false;
  internal long     mode    = 0;       //will replace the bool bufop, 0 = affect only the new resulting Reach, 1 = change the Base Reach, 2 = alter the Maste Reach (StringBuffer of the Base Reach)
  internal Reach    Base    = null;
  internal Reach    Part    = null;
  internal bool     strong  = false;
  internal string   sTxt    = "";
  internal Reach    rTxt;
  internal int      cnt     = 0;

 }
}

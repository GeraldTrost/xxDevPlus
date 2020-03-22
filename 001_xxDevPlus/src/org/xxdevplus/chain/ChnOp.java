


//** @author  GeTr Copyright© 1996-2020 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties
//** @license You may use, modify, distribute this code under GNU public dual licence free for NON-Commercial use as long as this notice remains unchanged.
//** @comment Operator for special manipulations (directly with a Chain's Char-Buffer or not)


package org.xxdevplus.chain;

import org.xxdevplus.chain.Chain;

 public class ChnOp
 {
  //Copyright© 1996-2020 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties, You may use, modify, distribute this code under GNU public licence for NON-Commercial use as long as this notice remains unchanged.
  private static boolean selfTested  = false; private static String ass(boolean expr) throws Exception { if (!expr) throw new Exception(ass(true) + " SelfTest Failure"); return "RchOp"; } private static void selfTest() { selfTested = true; } private void init() { if (!selfTested) selfTest(); }

  boolean  bufop   = false;
  long     mode    = 0;       //will replace the bool bufop, 0 = affect only the new resulting Chain, 1 = change the Base Chain, 2 = alter the Maste Chain (StringBuffer of the Base Chain)
  Chain    Base    = null;
  Chain    Part    = null;
  boolean  strong  = false;
  String   sTxt    = "";
  Chain    rTxt;
  int      cnt     = 0;

 }

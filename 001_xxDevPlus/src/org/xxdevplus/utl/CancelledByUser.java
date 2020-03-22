
//** @author  GeTr Copyright© 1996-2020 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties
//** @license You may use, modify, distribute this code under GNU public dual licence free for NON-Commercial use as long as this notice remains unchanged.
//** @comment Exception indicating User Break


package org.xxdevplus.utl;

public class CancelledByUser extends Exception
{
 private static boolean selfTested  = false;  private static String ass(boolean expr) throws Exception { if (!expr) throw new Exception(ass(true) + " SelfTest Failure"); return "CancelledByUser"; } 
 private static void selfTest() { selfTested = true; } 
 private void init() { if (!selfTested) selfTest(); }

  public CancelledByUser() {}

  public String getMessage()
  { 
   return "cancelled by user";
  }

}


//** @author  GeTr Copyright© 1996-2020 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties
//** @license You may use, modify, distribute this code under GNU public dual licence free for NON-Commercial use as long as this notice remains unchanged.
//** @comment Exception indicating User Break


using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace org.xxdevplus.utl
{
 public class CancelledByUser : Exception
 {
  private static bool selfTested  = false;  private static string ass(bool expr) { if (!expr) throw new Exception(ass(true) + " SelfTest Failure"); return "CancelledByUser"; } private static void selfTest() { selfTested = true; } private void init() { if (!selfTested) selfTest(); }

   public CancelledByUser() {}

   public override string Message { get{return "cancelled by user";}}

 }
}

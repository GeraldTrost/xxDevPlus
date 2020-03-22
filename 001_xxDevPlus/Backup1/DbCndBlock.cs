using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using ndBase;
using ndString;

namespace ndData
{
 public class DbCndBlock : Xpn<DbCnd>
 {
  //Copyright© 1996-2012 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties, You may use, modify, distribute this code under GNU public licence for NON-Commercial use as long as this notice remains unchanged.
  private static bool selfTested  = false; private static string ass(bool expr) { if (!expr) throw new Exception(ass(true) + " SelfTest Failure"); return "DbCndBlock"; } 

  private void init() 
  {
   if (!selfTested) selfTest();
  }

  private static void selfTest() 
  {
   selfTested = true;
  }

  public DbCndBlock(params DbCnd[] opnd) : base(opnd) { init(); }

  public override string val(int inx, EvalExpert evx) 
  {
   throw new Exception("Not supported yet.");
  }


 }
}

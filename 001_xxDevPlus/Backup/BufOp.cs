using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace ndString
{
 public class BufOp
 {
  //Copyright© 1996-2012 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties, You may use, modify, distribute this code under GNU public licence for NON-Commercial use as long as this notice remains unchanged.
  private static bool selfTested  = false;  private static string ass(bool expr) { if (!expr) throw new Exception(ass(true) + " SelfTest Failure"); return "BufOp"; } private static void selfTest() { selfTested = true; } private void init() { if (!selfTested) selfTest(); }

  private Reach Base;
  internal BufOp(Reach Base) { this.Base = Base; }

  public Delete        _del(           )  {return new Delete          (true, Base);      }
  public Delete        _del(int     cnt)  {return new Delete          (true, Base, cnt); }
  public ReplaceWith   _rpw(string  txt)  {return new ReplaceWith     (true, Base, txt); }
  public ReplaceWith   _rpw(Reach   txt)  {return new ReplaceWith     (true, Base, txt); }
  public Insert        _ins(string  txt)  {return new Insert          (true, Base, txt); }
  public Insert        _ins(Reach   txt)  {return new Insert          (true, Base, txt); }

 }
}

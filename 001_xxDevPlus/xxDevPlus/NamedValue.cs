
//** @author  GeTr Copyright© 1996-2020 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties
//** @license You may use, modify, distribute this code under GNU public dual licence free for NON-Commercial use as long as this notice remains unchanged.
//** @comment Key-Value Pair

using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace org_xxdevplus_struct
{
 public class NamedValue<kTyp, vTyp>
 {
  private static bool selfTested  = false; private static string ass(bool expr) { if (!expr) throw new Exception(ass(true) + " SelfTest Failure"); return "NamedValue<,>"; } private static void selfTest() { selfTested = true; } private void init() { if (!selfTested) selfTest(); }
 
  private kTyp name;
  private vTyp value;
  public  kTyp Name                         { get { return name;   }  set { name = value;       } }
  public  vTyp Value                        { get { return value;  }  set { this.value = value; } }
  public  NamedValue(kTyp Name, vTyp Value) { this.name = Name; this.value = Value;               }
 }

}

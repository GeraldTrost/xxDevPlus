using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using ndBase;
using ndString;

namespace ndData
{
 internal class Conditions
 {
  //Copyright© 1996-2012 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties, You may use, modify, distribute this code under GNU public licence for NON-Commercial use as long as this notice remains unchanged.
  private static bool selfTested  = false;  private static string ass(bool expr) { if (!expr) throw new Exception(ass(true) + " SelfTest Failure"); return "Conditions"; } private static void selfTest() { selfTested = true; } private void init() { if (!selfTested) selfTest(); }

  private string includeCond = "";
  public  string baseName = "";
  public  string name = "";
  public  long matchCount = 0;

  private Pile<Condition> conds = new Pile<Condition>();

  public void reset() { matchCount = 0; }

  public bool matches(Tag t)
  {
   foreach (Condition cnd in conds) if (!cnd.matches(t)) return false;
   matchCount++;
   if (includeCond.Equals("*")) return true;
   if (includeCond.Equals("" + matchCount)) return true;
   return false;
   //throw new Exception("complex sequence not yet supported, currently allowed: * or integer number");
  }

  public Conditions(string baseName, string name, string includeCond, params Pile<string>[] cond) { this.includeCond = includeCond; this.baseName = baseName; this.name = name; for (long i = 1; i <= cond.Length; i++) conds.Add(new Condition(cond[i - 1])); }
  public Conditions(string baseName, string name, string includeCond, Pile<Pile<string>> cond) { this.includeCond = includeCond; this.baseName = baseName; this.name = name; for (int i = 1; i <= cond.Len; i++) conds.Add(new Condition(cond[i])); }
 }
}

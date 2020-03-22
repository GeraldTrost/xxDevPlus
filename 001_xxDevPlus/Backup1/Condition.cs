using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Text.RegularExpressions;

using ndBase;
using ndString;

namespace ndData
{

 internal class Condition
 {
  //Copyright© 1996-2012 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties, You may use, modify, distribute this code under GNU public licence for NON-Commercial use as long as this notice remains unchanged.
  private static bool selfTested  = false;  private static string ass(bool expr) { if (!expr) throw new Exception(ass(true) + " SelfTest Failure"); return "Condition"; } private static void selfTest() { selfTested = true; } private void init() { if (!selfTested) selfTest(); }

  private Pile<string> symbs = new Pile<string>();
  private Pile<string> comps = new Pile<string>();
  private Pile<string> patts = new Pile<string>();

  public bool matches(Tag t)
  {
   for (int i = 1; i <= symbs.Len; i++)
   {
    Reach value = t.val(symbs[i]);
    string comp = comps[i];
    string ptn = patts[i];
    bool neg = comp.StartsWith("!");
    if (neg) comp = comp.Substring(1);
    if (comp.Equals("==")) { if (neg ^ value.equals(ptn)) return true; } else if (comp.Equals("°=")) { if (neg ^ value.Equals(ptn)) return true; } else if (comp.Equals("~~")) { if (neg ^ new Regex(ptn).IsMatch(value.text)) return true; } else if (comp.Equals("°~")) { if (neg ^ new Regex(ptn.ToUpper()).IsMatch(value.uText)) return true; } else throw new Exception("Invalid Compare Operator for Tag Filter! Use one of these: == °= ~~ °~ !== !°= !~~ !°~");
   }
   return false;
  }

  public Condition(Pile<string> cond)
  {
   string sym = cond.Name;
   for (int i = 1; i <= cond.Len; i++)
   {
    string symbol = sym;
    string cnd = cond[i];
    string comp = "";
    string ptn = "";
    bool neg = false;
    if ((!cnd.StartsWith("!")) && (!cnd.StartsWith("°")) && (!cnd.StartsWith("~")) && (!cnd.StartsWith("=")))
    {
     long pos = -1;
     if (cnd.IndexOf("=") > -1) { pos = cnd.IndexOf("="); if (cnd.IndexOf("~") > -1) pos = Math.Min(pos, cnd.IndexOf("~")); } else if (cnd.IndexOf("~") > -1) pos = cnd.IndexOf("~");
     if (cnd.Substring(0, (int)pos).IndexOf("°") > -1) pos = cnd.IndexOf("°");
     string key = cnd.Substring(0, (int)pos);
     cnd = cnd.Substring((int)pos);
     neg = key.EndsWith("!");
     if (neg) { comp = cnd.Substring(0, 2); ptn = cnd.Substring(2); key = key.Substring(0, key.Length - 1); } else { comp = cnd.Substring(0, 2); ptn = cnd.Substring(2); }
     symbol = key;
    } else
    {
     neg = (cnd.StartsWith("!"));
     if (neg) { comp = cnd.Substring(1, 3); ptn = cnd.Substring(3); } else { comp = cnd.Substring(0, 2); ptn = cnd.Substring(2); }
    }
    patts.Add(ptn);
    comps.Add(comp);
    symbs.Add(symbol);
   }
  }



 }
}

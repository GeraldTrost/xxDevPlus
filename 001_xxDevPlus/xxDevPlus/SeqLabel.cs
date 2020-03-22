using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace FlexBase
{
 public class SeqLabel
 {
  //Copyright© 1996-2012 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties, You may use, modify, distribute this code as long as this notice remains unchanged.
  private static bool selfTested  = false;  private static string ass(bool expr) { if (!expr) throw new Exception(ass(true) + " SelfTest Failure"); return "SeqLabel"; } private static void selfTest() { selfTested = true; } private void init() { if (!selfTested) selfTest(); }
 
  private long lbl;
  public SeqLabel(long lbl) {this.lbl = lbl; }
  public long val {get {return lbl;}  set {lbl = value;} }
 }
}

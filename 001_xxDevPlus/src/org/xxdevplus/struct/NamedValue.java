


//** @author  GeTr Copyright© 1996-2020 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties
//** @license You may use, modify, distribute this code under GNU public dual licence free for NON-Commercial use as long as this notice remains unchanged.
//** @comment Key-Value Pair


package org.xxdevplus.struct;

 public class NamedValue<kTyp, vTyp>
 {
  private static boolean selfTested  = false;  private static String ass(boolean expr) throws Exception { if (!expr) throw new Exception(ass(true) + " SelfTest Failure"); return "NamedValue<,>"; } private static void selfTest() { selfTested = true; } private void init() { if (!selfTested) selfTest(); }
 
  private kTyp name;
  private vTyp value;
  public  kTyp Name ()  { return name;   }  public void Name  (kTyp value) { name = value;       } 
  public  vTyp Value()  { return value;  }  public void Value (vTyp value) { this.value = value; } 
  public  NamedValue(kTyp Name, vTyp Value) { this.name = Name; this.value = Value;               }
 }

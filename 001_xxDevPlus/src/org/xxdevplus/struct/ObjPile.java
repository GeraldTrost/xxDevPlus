
//** @author  GeTr Copyright© 1996-2020 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties
//** @license You may use, modify, distribute this code under GNU public dual licence free for NON-Commercial use as long as this notice remains unchanged.
//** @comment Pile<Object> for convenience

package org.xxdevplus.struct;

import org.xxdevplus.struct.Pile;

 
 public class ObjPile extends Pile<Object>
 {
  private static boolean selfTested  = false;  private static String ass(boolean expr) throws Exception { if (!expr) throw new Exception(ass(true) + " SelfTest Failure"); return "DatPile"; } 
  private static void selfTest() { selfTested = true; }
 
  public  ObjPile(Object... obj) { super("", 0, obj); }

 }




//** @author  GeTr Copyright© 1996-2020 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties
//** @license You may use, modify, distribute this code under GNU public dual licence free for NON-Commercial use as long as this notice remains unchanged.
//** @comment Application Context

package org.xxdevplus.frmlng;

import org.xxdevplus.struct.KeyPile;

 public interface EvalExpert
 {
  KeyPile<String, String> Dtv (                             );                  void                    Dtv (KeyPile<String, String> value);     // Directives
  String                  val (Object                    obj) throws Exception;
 }

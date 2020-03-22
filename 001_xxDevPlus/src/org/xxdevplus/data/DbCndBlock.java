/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.xxdevplus.data;

import org.xxdevplus.frmlng.EvalExpert;
import org.xxdevplus.struct.Pile;
import org.xxdevplus.frmlng.Xpn;

/**
 *
 * @author root
 */
public class DbCndBlock extends Xpn<DbCnd>
{
 //Copyright© 1996-2020 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties, You may use, modify, distribute this code under GNU public licence for NON-Commercial use as long as this notice remains unchanged.
 private static boolean selfTested  = false;  private static String ass(boolean expr) throws Exception { if (!expr) throw new Exception(ass(true) + " SelfTest Failure"); return "DbCndBlock"; }


 public void init1() throws Exception
 {
  if (!selfTested) selfTest();
 }

 private static void selfTest() throws Exception
 {
  selfTested = true;
 }

 public  DbCndBlock (DbCnd...                                 opnd) throws Exception { super(opnd                      ); init1(); }

 @Override
 public String val(int inx, EvalExpert evx) throws Exception
 {
  throw new UnsupportedOperationException("Not supported yet.");
 }


}

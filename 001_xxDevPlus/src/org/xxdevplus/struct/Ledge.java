/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.xxdevplus.struct;

import java.util.HashMap;
import org.apache.commons.collections.TreeBag;

/**
 *
 * @author GeTr
 */
public class Ledge
{
 //Copyright© 1996-2020 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties, You may use, modify, distribute this code under GNU public licence for NON-Commercial use as long as this notice remains unchanged.
 private static boolean selfTested  = false;  private static String ass(boolean expr) throws Exception { if (!expr) throw new Exception(ass(true) + " SelfTest Failure"); return "Bag"; } private static void selfTest() { selfTested = true; } private void init() { if (!selfTested) selfTest(); }


 TreeBag  bag = new TreeBag();

 public Ledge() { }

 public void      Put(Object o) { bag.add(o); }
 public boolean   Has(Object o) { return bag.contains(o); }

}


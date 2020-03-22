/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.xxdevplus.data;

import java.sql.ResultSet;


/**
 *
 * @author GeTr
 */

public class DbMsDataAdapter
{
 //Copyright© 1996-2020 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties, You may use, modify, distribute this code under GNU public licence for NON-Commercial use as long as this notice remains unchanged.
 private static boolean selfTested  = false; private static String ass(boolean expr) throws Exception { if (!expr) throw new Exception(ass(true) + " SelfTest Failure"); return "DbMsDataAdapter"; } private static void selfTest() { selfTested = true; }
 
 DbDataAdapter adapter = null;
  
 public void Fill(DataSet ds) { adapter.Fill(ds); }

 public      DbMsDataAdapter(DbMsCommand command) 
 {
  adapter = new DbDataAdapter (command.command);
 }

}





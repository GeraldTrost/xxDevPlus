/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.xxdevplus.frmlng;

import org.xxdevplus.udf.Tag;
import org.xxdevplus.struct.Pile;

/**
 *
 * @author GeTr
 */

 public class Conditions
 {

  private String includeCond = "";
  public  String baseName = "";
  public  String name = "";
  private int matchCount = 0;

  private Pile<Condition> conds = new Pile<Condition>(0);

  public void reset() { matchCount = 0; }
 
  public boolean  matches(Tag t) throws Exception
  {
   for (Condition cnd : conds) if (!cnd.matches(t)) return false;
   matchCount++;
   if (includeCond.equals("*")) return true;
   if (includeCond.equals("" + matchCount)) return true;
   return false;
   //throw new Exception("complex sequence not yet supported, currently allowed: * or integer number");
  }
  
  public Conditions(String baseName, String name, String includeCond, Pile<String>... cond) throws Exception      { this.includeCond = includeCond; this.baseName = baseName; this.name = name; for (int i = 1; i <= cond.length; i++) conds.Add(new Condition(cond[i - 1])); }
  public Conditions(String baseName, String name, String includeCond, Pile<Pile<String>> cond) throws Exception   { this.includeCond = includeCond; this.baseName = baseName; this.name = name; for (int i = 1; i <= cond.Len(); i++) conds.Add(new Condition(cond.g(i))); }
 }

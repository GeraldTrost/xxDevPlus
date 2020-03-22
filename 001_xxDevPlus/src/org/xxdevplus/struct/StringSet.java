/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.xxdevplus.struct;

import org.xxdevplus.struct.KeyPile;
import org.xxdevplus.chain.Chain;

/**
 *
 * @author root
 */
public class StringSet 
{
 
 private KeyPile<String, String> vals = new KeyPile<String, String>();
 
 public StringSet(String set) throws Exception
 {
  Chain c = new Chain(set + ",");
  Chain token = c.at(1, ",");
  while (token.len() > 0)
  {
   String val = c.before(token).text().trim();
   c = c.after(token);
   token = c.at(1, ",");
   if (val.length() > 0) vals.Add(val, val);
  }
 }

 public void put(String val) throws Exception
 {
  val = val.trim();
  if (vals.hasKey(val)) return;
  vals.Add(val, val);
 }
 
 public String StringValue() throws Exception
 {
  String ret = "";
  for (String key :vals.kAsc()) ret += ", " + vals.g(key);
  return (ret.length() == 0) ? ret : ret.substring(2);
 }
}

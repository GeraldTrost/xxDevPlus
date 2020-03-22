/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.xxdevplus.struct;

import java.io.BufferedOutputStream;
import org.xxdevplus.chain.Chain;

/**
 *
 * @author User
 */
public class RuleTriple 
{

  public char type  = 'd';  // basic, also allowed 'a' (attribute) and 'r' predicate

  protected Chain lTerm = null;
  protected Chain mTerm = null;
  protected Chain rTerm = null;

  public Chain lTerm(            ) throws Exception  { return lTerm; };
  public Chain mTerm(            ) throws Exception  { return mTerm; };
  public Chain rTerm(            ) throws Exception  { return rTerm; };
  public void   rTerm(String value) throws Exception  { rTerm = new Chain(value); };

  public RuleTriple(char type, String lTerm , String mTerm , String rTerm) throws Exception
  {
   this.type = type;
   this.lTerm = new Chain(lTerm); 
   this.mTerm = new Chain(mTerm); 
   this.rTerm = new Chain(rTerm); 
  }
 
  public RuleTriple(Chain line) throws Exception
  {
   Chain delim = line.at(1, " atr:");
   if (delim.len() == 0) delim = line.at(1, " rel:");
   if (delim.len() == 0) delim = line.at(1, " def:");
   type = delim.at(2).text().charAt(0);
   lTerm = line.before(delim).Trim();
   mTerm = line.after(delim).before(1, ":").Trim();
   rTerm = line.after(mTerm).after(2).Trim();
  }



}


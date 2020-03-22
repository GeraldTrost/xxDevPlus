/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.xxdevplus.struct;


import org.xxdevplus.utl.utl;

import java.io.BufferedOutputStream;
import static java.lang.System.out;
import java.sql.ResultSet;
import org.xxdevplus.utl.ctx;
import org.xxdevplus.chain.Chain;
import org.xxdevplus.data.TripleStore;

/**
 *
 * @author User
 */
public class Triple 
{
  public char type  = 'd' /* definition e.g Man IsA Mammal*/;  // also allowed 'a' (attribute) and 'r' (predicate)

  protected Chain lTerm = null;
  protected Chain mTerm = null;
  protected Chain rTerm = null;

  private boolean unbound(Chain c) throws Exception { return ((c.len() == 7) && (c.equals(">>any<<"))); }
  
  public long[] status(TripleStore ts, long domId) throws Exception
  {
   long[] ret = new long[4]; ret[0] = type; for (int i = 1; i <= 3; i++) ret[i] = 0;      // type, sbj[-10000|id], def[-10000|id], typ[-10000|id], atr[-10000|id], val[-10000|0], prd[-10000|id], obj[-10000|id]
   if (unbound(sbj())) ret[1] = -10000; else try {ret[1] = ts.KnownConcepts   (domId).g(sbj().text()) ;} catch (Exception ex) {ret[1] = ts.putDbConcept   (sbj().text(), domId); ts.KnownConcepts   (domId).p   (ret[1], sbj().text());}
   switch (type)
   {
    case 'd':
     //if (unbound(def())) ret[2] = -10000; else try {ret[2] = ts.KnownTypes    (domId).g(typ().text()) ;} catch (Exception ex) {ret[2] = ts.putDbClass     (typ().text(), domId); ts.KnownTypes    (domId).p (ret[3], typ().text());}
     if (unbound(typ())) ret[3] = -10000; else try {ret[3] = ts.KnownTypes    (domId).g(typ().text()) ;} catch (Exception ex) {ret[3] = ts.putDbClass     (typ().text(), domId); ts.KnownTypes    (domId).p (ret[3], typ().text());}
     break;

    case 'a':
     if (unbound(atr())) ret[2] = -10000; else try {ret[2] = ts.KnownAttributes (domId).g(atr().text()) ;} catch (Exception ex) {ret[2] = ts.putDbAttribute (atr().text(), domId); ts.KnownAttributes (domId).p   (ret[2], atr().text());}
     if (unbound(val())) ret[3] = -10000;
     break;

    case 'r':
     if (unbound(prd())) ret[2] = -10000; else try {ret[2] = ts.KnownPredicates (domId).g(prd().text()) ;} catch (Exception ex) {ret[2] = ts.putDbPredicate (prd().text(), domId); ts.KnownPredicates (domId).p   (ret[2], prd().text());}
     if (unbound(obj())) ret[3] = -10000; else try {ret[3] = ts.KnownConcepts   (domId).g(obj().text()) ;} catch (Exception ex) {ret[3] = ts.putDbConcept   (obj().text(), domId); ts.KnownConcepts   (domId).p   (ret[3], obj().text());}
     break;
   }
   return ret;
  }

  public int binding(TripleStore ts, long domId) throws Exception
  {
   long[] stat = status(ts, domId);
   int ret = 0;
   ret = (stat[3] == - 10000) ? 1 + 2 * ret : 0 + 2 * ret ;
   ret = (stat[2] == - 10000) ? 1 + 2 * ret : 0 + 2 * ret ;
   ret = (stat[1] == - 10000) ? 1 + 2 * ret : 0 + 2 * ret ;
   return 1 + ret; 
  }
  
  public Chain  sbj(            ) throws Exception  {return lTerm;             };
  public void   sbj(String value) throws Exception  {lTerm = new Chain(value); };
  public void   sbj(Chain  value) throws Exception  {lTerm = value;            };

  //Subject(sbj) is also called Item (itm) in case of attribute triples
  public Chain  itm(            ) throws Exception  {if (type != 'a') throw new Exception("The Item       field is only valid for Triples of type a (atr)"); return lTerm;              };
  public void   itm(String value) throws Exception  {if (type != 'a') throw new Exception("The Item       field is only valid for Triples of type a (atr)"); lTerm = new Chain(value);  };
  public void   itm(Chain value ) throws Exception  {if (type != 'a') throw new Exception("The Item       field is only valid for Triples of type a (atr)"); lTerm = value;             };

  //Subject(sbj) is also called Concept (cpt) in case of d-triples
  public Chain  cpt(            ) throws Exception  {if (type != 'd') throw new Exception("The Concept    field is only valid for Triples of type d (def)"); return lTerm;              };
  public void   cpt(String value) throws Exception  {if (type != 'd') throw new Exception("The Concept    field is only valid for Triples of type d (def)"); lTerm = new Chain(value);  };
  public void   cpt(Chain value) throws Exception   {if (type != 'd') throw new Exception("The Concept    field is only valid for Triples of type d (def)"); lTerm = value;             };

  public Chain  prd(            ) throws Exception  {if (type != 'r') throw new Exception("The Predicate  field is only valid for Triples of type r (rel)"); return mTerm;              };
  public void   prd(String value) throws Exception  {if (type != 'r') throw new Exception("The Predicate  field is only valid for Triples of type r (rel)"); mTerm = new Chain(value);  };
  public void   prd(Chain  value) throws Exception  {if (type != 'r') throw new Exception("The Predicate  field is only valid for Triples of type r (rel)"); mTerm = value;             };

  public Chain  obj(            ) throws Exception  {if (type != 'r') throw new Exception("The Object     field is only valid for Triples of type r (rel)"); return rTerm;              };
  public void   obj(String value) throws Exception  {if (type != 'r') throw new Exception("The Object     field is only valid for Triples of type r (rel)"); rTerm = new Chain(value);  };
  public void   obj(Chain  value) throws Exception  {if (type != 'r') throw new Exception("The Object     field is only valid for Triples of type r (rel)"); rTerm = value;             };
  
  public Chain  atr(            ) throws Exception  {if (type == 'a') return mTerm;               throw new Exception("The Attribute  field is only valid for Triples of type a (atr)");};
  public void   atr(String value) throws Exception  {if (type != 'a') throw new Exception("The Attribute  field is only valid for Triples of type a (atr)"); mTerm = new Chain(value);  };
  public void   atr(Chain value) throws Exception   {if (type != 'a') throw new Exception("The Attribute  field is only valid for Triples of type a (atr)"); mTerm = value;              };

  public Chain  val(            ) throws Exception  {if (type == 'a') return rTerm;               throw new Exception("The Value      field is only valid for Triples of type a (atr)");};
  public void   val(String value) throws Exception  {if (type != 'a') throw new Exception("The Value      field is only valid for Triples of type a (atr)"); rTerm = new Chain(value);  };
  public void   val(Chain value) throws Exception   {if (type != 'a') throw new Exception("The Value      field is only valid for Triples of type a (atr)"); rTerm = value;              };

  public Chain  def(            ) throws Exception  {if (type == 'd') return mTerm;               throw new Exception("The Definition field is only valid for Triples of type d (def)"); };
  public void   def(String value) throws Exception  {if (type != 'd') throw new Exception("The Definition field is only valid for Triples of type d (def)"); mTerm = new Chain(value);  };
  public void   def(Chain value) throws Exception   {if (type != 'd') throw new Exception("The Definition field is only valid for Triples of type d (def)"); mTerm = value;              };

  public Chain  typ(            ) throws Exception  {if (type == 'd') return rTerm;               throw new Exception("The Type       field is only valid for Triples of type d (def)"); };
  public void   typ(String value) throws Exception  {if (type != 'd') throw new Exception("The Type       field is only valid for Triples of type d (def)"); rTerm = new Chain(value);  };
  public void   typ(Chain  value) throws Exception  {if (type != 'd') throw new Exception("The Type       field is only valid for Triples of type d (def)"); rTerm = value;              };
  
  public Triple(char type, Chain lTerm , Chain mTerm , Chain rTerm) throws Exception
  {
   this.type = type;
   this.lTerm = lTerm; 
   this.mTerm = mTerm; 
   this.rTerm = rTerm; 
  }

  public Triple(char type, String lTerm , String mTerm , String rTerm) throws Exception
  {
   this(type, new Chain(lTerm), new Chain(mTerm), new Chain(rTerm));
  }
 
  public Triple(Chain line) throws Exception
  {   
   Chain delim = line.at(1, " atr:");
   if (delim.len() == 0) delim = line.at(1, " rel:");
   if (delim.len() == 0) delim = line.at(1, " def:");
   type = delim.at(2).text().charAt(0);
   lTerm = line.before(delim).Trim();
   mTerm = line.after(delim).before(1, ":").Trim();
   rTerm = line.after(mTerm).after(2).Trim();
  }

  public String toString(String delim) throws Exception
  {
   String dlm = "";
   switch (type)
   {
    case 'd': dlm = " def:"; break;
    case 'a': dlm = " atr:"; break;
    case 'r': dlm = " rel:"; break;
    default:  throw new Exception("INVALID TRIPLE TYPE " + type);                                                                   
   }
   return lTerm.text() + delim + dlm + mTerm.text() + ": " + delim + rTerm.text();
  }

  public void write(BufferedOutputStream stream, boolean xml) throws Exception
  {
   Chain right = rTerm;
   if (type == 'a') right = right.before(1, "[").Trim();
   if (right.len() == 0) return;
   String delim = "";
   switch (type)
   {
    case 'd': delim = " def:"; break;
    case 'a': delim = " atr:"; break;
    case 'r': delim = " rel:"; break;
    default:  throw new Exception("INVALID TRIPLE TYPE " + type);                                                                   
   }
   stream.write((lTerm.text() + delim + mTerm.text() + ": " + right .text() + utl.crlf()).getBytes());
  }

  
}

















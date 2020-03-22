
package org.xxdevplus.struct;


import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import org.xxdevplus.chain.Chain;
import org.xxdevplus.utl.ctx;

public class Triples extends Pile<Triple> 
{

 private   KeyPile<String, Triple> def       = new KeyPile<String, Triple>();;
 private   KeyPile<String, Triple> atr       = new KeyPile<String, Triple>();;
 private   KeyPile<String, Triple> rln       = new KeyPile<String, Triple>();;

 public Triples() throws Exception
 {   
 }
 
 public Triples(Chain line) throws Exception
 {   
  Push(new Triple(line));
 }

 public Triples(char type, Chain lTerm , Chain mTerm , Chain rTerm) throws Exception          
 {
  Push(new Triple(type, lTerm, mTerm, rTerm));
 }

 public Triples(char type, String lTerm , String mTerm , String rTerm) throws Exception
 {
  Push(new Triple(type, new Chain(lTerm), new Chain(mTerm), new Chain(rTerm)));
 }

 public Triples(Pile<Triple> facts) throws Exception
 {
  for (Triple fact : facts) Push(fact);
 }

 public   Triples        Add        (Triples                                 more) throws Exception { return (Triples) super.Add(more); }
 public   Triples        Add        (Triple...                               more) throws Exception { return (Triples) super.Add(more); }
 public   Triples        Add        (List<Triple>                            more) throws Exception { return (Triples) super.Add(more); }
 public   Triples        Add        (Pile<Triple>                            more) throws Exception { return (Triples) super.Add(more); }

 public   Triple         Push       (Triple                                  next) throws Exception { Add(next); return next; }
 public   Triples        Push       (Triples                                 more) throws Exception { return (Triples) super.Add(more); }

 public   Triple         Pop        (                                            ) throws Exception { if (Len() == 0) throw new Exception("0-Len Stack cannot Pop"); Triple ret = g(-1); Del(-1); return ret;                                                   }
 public   Triples        Pop        (int count                                   ) throws Exception { return (Triples) super.Pop(count);}

 public   Triple         Poke       (Triple                                  next) throws Exception { InsBefore(1, next); return (Triple) next; }
 public   Triples        Poke       (Triples                                 more) throws Exception { InsBefore(1, more); return more;  }

 public   Triple         Pull       (                                            ) throws Exception { if (Len() == 0) throw new Exception("0-Len Stack cannot Pull"); Triple ret = g(1);  Del(1); return ret; }

 public   Triples        Pull       (int count                                   ) throws Exception { return (Triples) super.Pull(count);}

 public   Triples        RollUp     (int count                                   ) throws Exception { return Push(Pull(count)) ;}        
 public   Triples        RollDn     (int count                                   ) throws Exception { return Poke(Pop (count)) ;}        


 private void upd() throws Exception
 {
  if (!dirty) return;
  dirty = false;
  def = new KeyPile<String, Triple>();
  atr = new KeyPile<String, Triple>();
  rln = new KeyPile<String, Triple>();
  for( Triple fact : this) 
  {
   switch (fact.type)
   {
    case 'd': def.p(fact, fact.sbj().plus(fact.def()).plus(fact.typ()).text()); break;
    case 'a': atr.p(fact, fact.sbj().plus(fact.atr()).plus(fact.val()).text()); break;
    case 'r': rln.p(fact, fact.sbj().plus(fact.prd()).plus(fact.obj()).text()); break;
   }
  }
 }

 public Triples def() throws Exception
 {
  upd();
  return (Triples) def.Vals();
 }

 public Triples atr() throws Exception
 {
  upd();
  return (Triples) atr.Vals();
 }

 public Triples rln() throws Exception
 {
  upd();
  return (Triples) rln.Vals();
 }
 
 public Triples unique() throws Exception
 {
  upd();
  return new Triples(def.Vals()).Add(atr.Vals()).Add(rln.Vals());
 }

 public KeyPile<String, Triples> bySbj() throws Exception
 {
  KeyPile<String, Triples> ret = new KeyPile<String, Triples>();
  upd();
  for (Triple fact : this)
  {
   String sbj = fact.sbj().text();
   if (!ret.hasKey(sbj)) ret.Add(sbj, new Triples());
   ret.g(sbj).Add(fact);
  }
  return ret;
 }

 /**
  * cumulates Triples (sums or averages Attributes starting with + and ~), do not use uniqTpls before this action - apply it afterwards!
  * @param facts Triples to be stored
  */
 public Triples cumulate() throws Exception
 {
  Triples ret = new Triples();
  KeyPile<String, KeyPile<String, Triple>> special = new KeyPile<String, KeyPile<String, Triple>>();
  KeyPile<String, Triple> entries = null;
  Chain value = Chain.Empty;
  for( Triple fact : this) 
  {
   if ((fact.type == 'a') && (fact.atr().startsWith("+") || fact.atr().startsWith("~")))
   {
    if (!special.hasKey(fact.atr().text())) special.Add(fact.atr().text(), new KeyPile<String, Triple>());
    entries = special.g(fact.atr().text());
    double ctr = 0;
    double dnm = 1;
    if (entries.hasKey(fact.sbj().text()))
    {
     value = entries.g(fact.sbj().text()).val();
     ctr = Double.parseDouble(value.before ("/").text());
     dnm = Math.max(1, Double.parseDouble(value.after  ("/").text() + "0"));
    }
    entries.p(new Triple('a', fact.sbj().text(), fact.atr().text(), "" + (ctr + Double.parseDouble(fact.val().text())) + "/"+ ++dnm ), fact.sbj().text());
   }
   else ret.Push(fact);
  }
  for (KeyPile<String, Triple> entry : special) for (Triple fact : entry) 
   if (fact.atr().startsWith("~"))
    ret.Push(new Triple('a', fact.sbj().text(), fact.atr().from(2).text(), fact.val().text()));
   else
    ret.Push(new Triple('a', fact.sbj().text(), fact.atr().from(2).text(), fact.val().before("/").text()));
  return ret;
 }

 public void write(File file, boolean xml, boolean append) throws Exception
 {
  BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file.getAbsolutePath(), append));
  for (Triple fact: this) fact.write(out, xml);
  out.close();
 }

 
 public String toString(String delim) throws Exception
 {
  String ret = "";
  for (Triple fact : this) ret += "\n" + fact.toString(delim);
  return (ret.length() > 0) ? ret.substring(1) : ret;
 }
 
         
 
 
}

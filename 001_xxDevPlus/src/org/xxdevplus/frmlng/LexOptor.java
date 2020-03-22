

package org.xxdevplus.frmlng;

import org.xxdevplus.chain.Chain;
import org.xxdevplus.struct.Pile;
import org.xxdevplus.utl.utl;


public class LexOptor 
{
  //** @author GeTr Copyright© 1996-2020 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties, You may use, modify, distribute this code under GNU public dual licence free for NON-Commercial use as long as this notice remains unchanged.
  //** @comment LexNode parses a textual Formula into a Tree
  static int _ic;String ass(boolean xp,String...msg)throws Exception{if(xp)return"LexOptor"; throw new Exception(ass(true)+" slfTst:"+utl.str(msg));}private void slfTst()throws Exception{tstSimple();}protected void init()throws Exception{if(_ic++==0)slfTst();}

  
  void tstSimple() throws Exception
  {

   LexOptor dot;
   dot = new LexOptor      // illusionary magical BINARY Surround DOT-Operator with 2 ARGS (NUM|SYM, SYM|NUM) on 2 Operands (SYM|LIT, SYM|LIT)
   (
    new Chain("2yPexTor(2)"), 
    new Pile<Chain>         // TOK
    ( "", 0, 
     new Chain("."),
     new Chain(".")
    ),
    new Pile<Chain>(0, new Chain("ATR")), 
    new Pile<Pile<Chain>>   // ARG
    ( "", 0, 
     new Pile<Chain>("", 0, new Chain("NUM"), new Chain("SYM")), 
     new Pile<Chain>("", 0, new Chain("SYM"), new Chain("NUM"))
    ),
    new Pile<Pile<Chain>>   // OPD
    ( "", 0, 
     new Pile<Chain>("", 0, new Chain("SYM"), new Chain("LIT")), 
     new Pile<Chain>("", 0, new Chain("SYM"), new Chain("LIT"))
    )
   );
  }

 public static Pile<Chain> noOpndTypes  = new Pile<Chain>();
               int                   typ;                // -1 = prefix, 0 = infix, 1 = postfix, 2 = surround
               Pile<Chain>           tok;                // e.g [((, ))]  or [+]
               Pile<Chain>           rst;                // Result Types
               Pile<Pile<Chain>>     arg;
               Pile<Pile<Chain>>     opd;
 public        Chain                 sig = Chain.Empty;
        
 public boolean opdWelcomeAt(int pos, String op) throws Exception
 {
  Pile<Chain> opddef = null;
  if (pos >= opd.Len() && valency() == -1) pos = opd.Len() - 1;
  for (Chain def : opd.g(pos)) if (def.equals(op)) return true;
  return false;
 }
 
 public Pile<Chain> opndTypes(int pos) throws Exception
 {
  if (pos == 0) return LexOptor.noOpndTypes;
  if (opd.g(-1).Len() == 0) return (pos > opd.Len() - 1) ? opd.g(-2) : opd.g(pos);
  return opd.g(pos);
 }

 public int valency() throws Exception
 {
  if (opd.Len() == 0) return 0;
  if (opd.g(-1).Len() == 0) return -1;
  return opd.Len();
 }
   
 public LexOptor (Chain typ, Pile<Chain> tok, Pile<Chain> res, Pile<Pile<Chain>> arg, Pile<Pile<Chain>> opd) throws Exception
 {
  init();
  typ = new Chain(typ.after(1, "y").before(1, "(").before(1, "Tor").Trim().text().toUpperCase());
  if (typ.startsWith("PE")) this.typ = -1; else if (typ.startsWith("IN")) this.typ = 0; else if (typ.startsWith("PO")) this.typ = 1; else this.typ = 2;
  this.tok = tok;
  this.rst = res;
  this.arg = arg;
  this.opd = opd;
  switch (this.typ)
  {
   case -1: sig = tok.g(1).plus(" ")                   ; break;
   case  0: sig = Chain.Blank.plus(tok.g(1)).plus(" ") ; break;
   case  1: sig = Chain.Blank.plus(tok.g(1))           ; break;
   case  2: sig = tok.g(1).plus(" ").plus(tok.g(2))    ; break;
  }
 }

 @Override
 public String toString()
 {
  try
  {
   int vcyopd = opd.Len() == 0 ? 0 : opd.g(-1).Len() == 0 ? -1 : opd.Len();
   int vcyarg = arg.Len() == 0 ? 0 : arg.g(-1).Len() == 0 ? -1 : arg.Len();

   String ret = " ";
   for (Chain rt : rst) ret += rt.text() + ",";
   if (rst.Len() > 0) ret = ret.substring(0, ret.length() - 1);
   
   ret = (ret + "                          ").substring(0, 20) + ("'" + sig + "'                          ").substring(0, 20);
   if (vcyopd == -1) ret += "n,"     ; else ret += vcyopd + ","     ;
   if (vcyarg == -1) ret += "n ary " ; else ret += vcyarg + " ary " ;
   

   ret = (ret + "                                                                                ").substring(0, 65);

   switch (this.typ)
   {
    case -1: ret += " PexOtr w/Arg("; break;
    case  0: ret += " InxOtr w/Arg("; break;
    case  1: ret += " PoxOtr w/Arg("; break;
    case  2: ret += " SurOtr w/Arg("; break;
   }

   for (Pile<Chain> ar : arg) 
   {
    for (Chain a : ar) ret += a.text() + "|";
    if (ar.Len() > 0) ret = ret.substring(0, ret.length() - 1);
    ret += ", ";
   }
   if (arg.Len() > 0) ret = ret.substring(0, ret.length() - 2);
   if (ret.endsWith(", ")) ret = ret.substring(0, ret.length() - 2) + " ...";
   
   ret += ") OnOpd ";
   
   for (Pile<Chain> op : opd) 
   {
    for (Chain o : op) ret += o.text() + "|";
    if (op.Len() > 0) ret = ret.substring(0, ret.length() - 1);
    ret += ", ";
   }
   if (opd.Len() > 0) ret = ret.substring(0, ret.length() - 2);
   if (ret.endsWith(", ")) ret = ret.substring(0, ret.length() - 2) + " ...";
   

   return ret;
  }
  catch (Exception ex) {return "<ERR>";}
 }
}











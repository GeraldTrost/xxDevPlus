 


/*

// 0y = "Nullary" 1y = "Unary" 2y = "Binary" 3y = "Ternary" ny = "n-ary" PexTor = "Prefix Operator" PoxTor = "Postfix Operator" SurTor = "Surrounding Operator" InxTor = "Infix Operator"

SYM        href text id a3 i8 ExtractYear len
NUM        3 8

FNC        <<ExtractYear>>                           1ySurTor "<<", ">>"()    ON SYMBOL                                             --> 1yPoxTor "<<ExtractYear>>"()  ON CXI      --> STR  2012

PRM        :id                                       1yPexTor ":"()           ON SYM                                                --> 1yPoxTor ":id"()              ON CXI      --> PRM  #1723
ATR        .src                                      1yPoxTor "."()           ON SYM                                                --> 1yPoxTor ".src"()             ON CXI      --> ATR  https://wikipedia.org
PTY        .text()                                   1ySurTor ".", "()"()     ON SYM                                                --> 1yPoxTor ".text()"()          ON CXI      --> STR  ich bin ein InnerText

LIT        '3'   '2.77'   'wpID:'                    1ySurTor "'", "'"()      ON SYM                                                                                              --> STR  hello
CCT        'aaa'.src                                 nyInxTor ""()            ON FNC|PRM|ATR|PTY|LIT|FNC                                                                          --> STR  atrId:manfre mann-http://mann.org

LST        0, -1, 2                                  nyInxTor ","()           ON SYM^n                                                                                            --> LST  0, -1, 2
NAV        <0,-1>                                    1ySurTor "<", ">"()      ON LST                                                --> 1yPoxTor "<0, 1>"()           ON CXI      --> CXI

CMP        'aaa' == .href                            2yInxTor "=="()          ON FNC|PRM|ATR|PTY|LIT|FNC, FNC|PRM|ATR|PTY|LIT|FNC                                                 --> BOO

OTR        2.77 + 3                                  2yInxTor "=="()          ON CONCAT                                                       --> NUMBER

RESTRICTOR 3..8                                      1ary IFX OPTR ".."       ON SYMBOL                                                       --> REGION

RESTRICTOR [3..8, 'aaa' != .href]                    1-poPTR "[", "]" (LIST(CND ,CND)) ON CXI, LIT, ATR, FNC,                                                        --> Bool

UNIFIER    {'aaa'el.href}                            1ary PPX OPTR "{", "}"   ON  CONCAT                                                       --> String

EVALUATOR  (el.'11'(el.'aaa'el.src)[3..8])[2..20]    1ary PPX OPTR "(", ")"   ON CONCAT                                                       --> String

*/


package org.xxdevplus.frmlng;

import org.xxdevplus.chain.Chain;
import org.xxdevplus.struct.KeyPile;
import org.xxdevplus.struct.Pile;
import org.xxdevplus.utl.utl;


public class LexLang 
{
 //** @author GeTr Copyright© 1996-2020 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties, You may use, modify, distribute this code under GNU public dual licence free for NON-Commercial use as long as this notice remains unchanged.
 //** @comment LexSpace contains all Operator Defs for parsing a textual Formula into a Tree
 static int _ic;String ass(boolean xp,String...msg)throws Exception{if(xp)return"LexLang"; throw new Exception(ass(true)+" slfTst:"+utl.str(msg));}private void slfTst()throws Exception{tstSimple();}

 private String name = "";
 
 private Pile<LexOptor> otrs   = new Pile<LexOptor>();   // definitions must be initialized this way: Keys with descending Length !!!
 //public  Pile<LexOptor> optors() { return optors; }

 private static KeyPile<String, LexLang> lng = new KeyPile<String, LexLang>();
 KeyPile<String, LexOptor> bTokOtrs = new KeyPile<String, LexOptor>();
 KeyPile<String, LexOptor> eTokOtrs = new KeyPile<String, LexOptor>();
 KeyPile<String, LexOptor> typOtrs  = new KeyPile<String, LexOptor>();

 private String[] oSmbs = {};

 public String[] otrSmbs() { return oSmbs; } 
 
 public static  LexLang Lng(String  name) throws Exception { return lng.g(name)                                                    ; }
 public static  LexLang Lng(LexLang  lng) throws Exception { LexLang.lng.p(lng, lng.name); return lng                              ; }
 
 private KeyPile<String, Chain> alphabet 
         = new KeyPile<String, Chain>();
 
 public  Chain Alphabet(String               name) throws Exception { return alphabet.g(name)                                      ; }
 
 public  Chain Alphabet(String alpha, String name) throws Exception 
 { 
  alphabet.p(new Chain(alpha), name); 
  return alphabet.g(name)  ; 
 }
 
 void tstSimple() throws Exception
 {
  
 }

 protected void init() throws Exception
 {
  if(_ic++==0)slfTst();  
 } 
 
 public LexLang(String name) throws Exception
 {
  Alphabet("abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ"   , "SYM");
  Alphabet("abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ"   , "FNC");
  Alphabet("0123456789.eE+-"                                                  , "NUM");
  Alphabet("0123456789+-"                                                     , "INT");
  this.name = name;
  init();
 }

 private void upd() throws Exception
 {
  bTokOtrs = new KeyPile<String, LexOptor>();
  eTokOtrs = new KeyPile<String, LexOptor>();
  typOtrs  = new KeyPile<String, LexOptor>();
  for (LexOptor otr : otrs)
  {
   typOtrs.Add(otr.sig.text(), otr);
   for (int i = 1; i<= otr.tok.Len(); i++) 
    if (i == 1) 
     bTokOtrs.Add(otr.tok.g(i).text(), otr); 
    else 
     try
     {
      eTokOtrs.Add(otr.tok.g(i).text(), otr);
     }
     catch (Exception ex) {}
  }
  Pile<String> otrSmbs = new Pile<String>(); // = lng.optors.Keys().before(-1);   
  for (String tok : bTokOtrs.Keys().before(-1)) otrSmbs.Push(tok); 
  for (String tok : eTokOtrs.Keys())            otrSmbs.Push(tok); 
  oSmbs = otrSmbs.strArray();   
 }
 
 /** add a new Opeartor to the Language */
 public void addOtr(String[] rst, String[][] qal, String[][] opd, String[] tok) throws Exception
 {
  int vcyopd = opd.length == 0 ? 0 : opd[opd.length -  1].length == 0 ? -1 : opd.length;
  int vcyarg = qal.length == 0 ? 0 : qal[qal.length -  1].length == 0 ? -1 : qal.length;
  Chain typ = vcyarg == -1 ? vcyopd == -1 ? new Chain("nyTor(").plus("n)") : new Chain("" + vcyopd + "yTor(").plus("n)") : vcyopd == -1 ? new Chain("nyTor(").plus("" + vcyarg + ")") : new Chain("" + vcyopd + "yTor(").plus("" + vcyarg + ")");
  Pile<Chain>        toks = new Pile<Chain>();
  Chain tkn = new Chain(tok[0]);
  if (tkn.startsWith(" ") && tkn.endsWith(" ")) {typ = typ.upto(2).plus("Inx").plus(typ.from(3)); toks.Push(tkn.after(1).before(-1)); }
  else
   if (tkn.startsWith(" ")) {typ = typ.upto(2).plus("Pox").plus(typ.from(3)); toks.Push(tkn.after(1)); }
   else
    if (tkn.endsWith(" ")) { typ = typ.upto(2).plus("Pex").plus(typ.from(3)); toks.Push(tkn.before(-1)); }
    else { typ = typ.upto(2).plus("Sur").plus(typ.from(3)); toks.Push(tkn.before(1, " ")); toks.Push(tkn.after(-1, " ")); }
  Pile<Chain>        rsts = new Pile<Chain>()                                                                                         ; for (String s : rst) rsts.Push(new Chain(s));
  Pile<Pile<Chain>>  qals = new Pile<Pile<Chain>>(); for (int i = 1; i <= qal.length; i++) {qals.Push((Pile<Chain>)new Pile<Chain>()) ; for (String s : qal[i-1]) qals.g(-1).Push(new Chain(s));}
  Pile<Pile<Chain>>  opds = new Pile<Pile<Chain>>(); for (int i = 1; i <= opd.length; i++) {opds.Push((Pile<Chain>)new Pile<Chain>()) ; for (String s : opd[i-1]) opds.g(-1).Push(new Chain(s));}
  //optors.Add(toks.g(1).text(), new LexOptor(typ, toks, rsts, qals, opds));
  otrs.Push(new LexOptor(typ, toks, rsts, qals, opds));
  upd();
 }
        
 
}

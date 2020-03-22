



package org.xxdevplus.frmlng;

import org.xxdevplus.chain.Chain;
import static org.xxdevplus.frmlng.LexLang.Lng;
import org.xxdevplus.struct.KeyPile;
import org.xxdevplus.struct.Pile;
import org.xxdevplus.utl.utl;


public class LexFmla 
{
  //** @author GeTr Copyright© 1996-2020 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties, You may use, modify, distribute this code under GNU public dual licence free for NON-Commercial use as long as this notice remains unchanged.
  //** @comment based on a given Lex Language LexFormula parses a textual Formula into a Tree
  static int _ic;String ass(boolean xp,String...msg)throws Exception{if(xp)return"LexFormula"; throw new Exception(ass(true)+" slfTst:"+utl.str(msg));}private void slfTst()throws Exception{tstSimple();}protected void init()throws Exception{if(_ic++==0)slfTst();}
  
  
  void tstSimple() throws Exception
  {
   
   LexLang lng = new LexLang("SelfTestLanguage 001");
   Lng(lng);                                                                                                                                              // register the lng with its name for use in formulas (read this statement as a setter or read it as a property being set or simple read it as a definition "lng is by this defined to be an existing Language"

   /* DO NOT DELETE THIS COMMENTS !!!!
   lng.optors.Add(".ATR", new LexOptor                                                                                                                    // UNARY Postfix ATR . Operator with NO ARGS () on 1 Operand (CXI)
   (
    new Chain("1yPoxTor(0)"),   new Pile<Chain>(0, new Chain("CXI")),                        new Pile<Chain>(0, new Chain("ATR")),                        // TYP, TOK, RES
    new Pile<Pile<Chain>>(),    new Pile<Pile<Chain>>(0,                                     new Pile<Chain>(0, new Chain("CXI")))                        // ARG, OPD
   ));

   lng.optors.Add("<<", new LexOptor                                                                                                                      // NARY Surround STR <<>> Operator with NO ARGS () on n Operand (SYM)
   (
    new Chain("nySurTor(0)"),  new Pile<Chain>(0, new Chain("<<"), new Chain(">>")),         new Pile<Chain>(0, new Chain("STR"), new Chain("STR")),      // TYP, TOK, RES
    new Pile<Pile<Chain>>(),   new Pile<Pile<Chain>>(0, new Pile<Chain>(0, new Chain("CXI")))                                                             // ARG, OPD
   ));

   lng.optors.Add("SIN", new LexOptor                                                                                                                      // NARY Surround STR <<>> Operator with NO ARGS () on n Operand (SYM)
   (
    new Chain("1ySurTor(0)"),  new Pile<Chain>(0, new Chain("SIN("), new Chain(")")),        new Pile<Chain>(0, new Chain("NUM")),                        // TYP, TOK, RES
    new Pile<Pile<Chain>>(),   new Pile<Pile<Chain>>(0, new Pile<Chain>(0, new Chain("NUM")))                                                             // ARG, OPD
   ));
   lng.optors.Add("(", new LexOptor                                                                                                                       // NARY Surround << Operator with NO ARGS () on n Operand (SYM)
   (
    new Chain("nySurTor(0)"),  new Pile<Chain>(0, new Chain("("), new Chain(")")),           new Pile<Chain>(0, new Chain("STR")),                        // TYP, TOK, RES
    new Pile<Pile<Chain>>(),   new Pile<Pile<Chain>>(0, new Pile<Chain>(0, new Chain("CXI")))                                                             // ARG, OPD
   ));
   lng.optors.Add(":PRM", new LexOptor                                                                                                                    // UNARY Postfix PRM-Operator with NO ARGS () on 1 Operand (CXI)
   (
    new Chain("1yPoxTor(0)"), new Pile<Chain>(0, new Chain("PRM")),                          new Pile<Chain>(0, new Chain("STR")),                        // TYP, TOK, RES
    new Pile<Pile<Chain>>(),  new Pile<Pile<Chain>>(0, new Pile<Chain>(0, new Chain("CXI")))                                                              // ARG, OPD
   ));
   lng.optors.Add(".", new LexOptor                                                                                                                       // UNARY Prefix DOT-Operator with NO ARGS () on 1 Operand (SYM)
   (
    new Chain("1yPexTor(0)"), new Pile<Chain>(0, new Chain(".")),                            new Pile<Chain>(0, new Chain("ATR")),                        // TYP, TOK, RES
    new Pile<Pile<Chain>>(),  new Pile<Pile<Chain>>(0, new Pile<Chain>(0, new Chain("SYM")))                                                              // ARG, OPD
   ));
   lng.optors.Add("::", new LexOptor                                                                                                                      // NARY Prefix DBLCOLON-Operator with NO ARGS () on n Operand (SYM)
   (
    new Chain("1yPexTor(0)"), new Pile<Chain>(0, new Chain(":")),                            new Pile<Chain>(0, new Chain("PRM")),                        // TYP, TOK, RES
    new Pile<Pile<Chain>>(),  new Pile<Pile<Chain>>(0, new Pile<Chain>(0, new Chain("SYM")), new Pile<Chain>())                                           // ARG, OPD              // ATTGeTr: n-ary Operators MUST end with an empty OPD Definition !
   ));
   lng.optors.Add(":", new LexOptor                                                                                                                       // UNARY Prefix COLON-Operator with NO ARGS () on 1 Operand (SYM)
   (
    new Chain("1yPexTor(0)"), new Pile<Chain>(0, new Chain(":")),                            new Pile<Chain>(0, new Chain("PRM")),                        // TYP, TOK, RES
    new Pile<Pile<Chain>>(),                                                                                                                              // ARG
    new Pile<Pile<Chain>>(0, new Pile<Chain>(0, new Chain("SYM")))                                                                                        // OPD
   ));
   lng.optors.Add("++", new LexOptor                                                                                                                      // NARY PostFix DBLCOLON-Operator with NO ARGS () on n Operand (SYM)
   (
    new Chain("1yPoxTor(0)"), new Pile<Chain>(0, new Chain(":")),                            new Pile<Chain>(0, new Chain("PRM")),                        // TYP, TOK, RES
    new Pile<Pile<Chain>>(),  new Pile<Pile<Chain>>(0, new Pile<Chain>(0, new Chain("SYM")), new Pile<Chain>())                                           // ARG, OPD              // ATTGeTr: n-ary Operators MUST end with an empty OPD Definition !
   ));
   lng.optors.Add("+", new LexOptor                                                                                                                       // NNARY Infix PLUS-Operator with NO ARGS () on n Operand (NUM)
   (
    new Chain("nyInxTor(0)"), new Pile<Chain>(0, new Chain("+")),                            new Pile<Chain>(0, new Chain("STR")),                        // TYP, TOK, RES
    new Pile<Pile<Chain>>(),  new Pile<Pile<Chain>>(0, new Pile<Chain>(0, new Chain("NUM")), new Pile<Chain>())                                           // ARG, OPD              // ATTGeTr: n-ary Operators MUST end with an empty OPD Definition !
   ));
   // ATTGETR: EMPTY OPERATOR MUST BE THE LAST OPERATOR
   lng.optors.Add("", new LexOptor                                                                                                                        // NNARY Infix CCT-Operator with NO ARGS () on n Operand (ATR|PRM|NUM, )
   (
    new Chain("nyInxTor(0)"), new Pile<Chain>(0, new Chain("")),                             new Pile<Chain>(0, new Chain("STR")),                        // TYP, TOK, RES
    new Pile<Pile<Chain>>(),  new Pile<Pile<Chain>>(0, new Pile<Chain>(0, new Chain("STR"), new Chain("NUM")), new Pile<Chain>())                         // ARG, OPD              // ATTGeTr: n-ary Operators MUST end with an empty OPD Definition !
   ));
   */
   
   // Syntax is             addXyz ( RST, QAL, OPD, TOK )    // ATTGETR: an empty Operator MUST BE DEFINED AT THE END !! ATTGETR: n-ary Operators MUST define an additional empty last OPD !!

   lng.addOtr(utl.sa("STR", "NUM" ), utl.saa(utl.sa()), utl.saa(                                                                 ), utl.sa("<. .>"     )); // NARY Surround STR "<<.>>"   Operator with NO ARGS() on n OPD (CXI|STR)
   lng.addOtr(utl.sa("STR", "STR" ), utl.saa(                       ), utl.saa(utl.sa("NUM", "SYM"                   ), utl.sa()                ), utl.sa("<< >>"     )); // NARY Surround STR "<<.>>"   Operator with NO ARGS() on n OPD (CXI|STR)
   lng.addOtr(utl.sa("NUM"        ), utl.saa(                       ), utl.saa(utl.sa("NUM"                          )                          ), utl.sa("SIN( )"    )); // 1ARY Surround STR "SIN(.)"  Operator with NO ARGS() on 1 OPD (NUM)
   lng.addOtr(utl.sa("LST"        ), utl.saa(                       ), utl.saa(utl.sa("NUM", "SYM"                   ), utl.sa()                ), utl.sa("( )"       )); // NARY Surround LST "( )"     Operator with NO ARGS() on n Operand (NUM|SYM)
   lng.addOtr(utl.sa("ATR"        ), utl.saa(                       ), utl.saa(utl.sa("SYM"                          )                          ), utl.sa(". "        )); // 1ARY Prefix   ATR ". "      Operator with NO ARGS() on 1 Operand (SYM)
   lng.addOtr(utl.sa("PRM|CXI"    ), utl.saa(                       ), utl.saa(utl.sa("SYM"                          )                          ), utl.sa(": "        )); // 1ARY Prefix   PRM ": "      Operator with NO ARGS() on 1 Operand (SYM)
   lng.addOtr(utl.sa("LST"        ), utl.saa(                       ), utl.saa(utl.sa("SYM"                          ), utl.sa()                ), utl.sa(":: "       )); // NARY Prefix   LST ":: "     Operator with NO ARGS() on n Operand (SYM)
   lng.addOtr(utl.sa("PRM"        ), utl.saa(                       ), utl.saa(utl.sa("SYM"                          ), utl.sa()                ), utl.sa(" ++"       )); // NARY PostFix  PRM " ++"     Operator with NO ARGS() on n Operand (SYM)
   lng.addOtr(utl.sa("STR"        ), utl.saa(                       ), utl.saa(utl.sa("NUM"                          ), utl.sa()                ), utl.sa(" + "       )); // NARY Infix    STR " + "     Operator with NO ARGS() on n Operand (NUM)
   lng.addOtr(utl.sa("LST"        ), utl.saa(                       ), utl.saa(utl.sa("ATR", "PRM", "NUM","SYM","STR"), utl.sa()                ), utl.sa("  "        )); // NARY Infix    LST "  "      Operator with NO ARGS() on n Operand (ATR|PRM|NUM)

   System.out.println("\n--------------------------------\ndefined Operators in Language \"SelfTestLanguage 001\":");
   for (LexOptor otr : lng.typOtrs.Vals()) System.out.println(otr.toString());
   System.out.println("--------------------------------\n");
   

   System.out.println("\n             THESE FORMULAS WORK FINE:\n");

   new LexFmla(new Chain("3 + 2 + 1 + 7")                       , "SelfTestLanguage 001");

   new LexFmla(new Chain("3 hello 14++")                        , "SelfTestLanguage 001");
   new LexFmla(new Chain("<<bb>>")                              , "SelfTestLanguage 001");   
   new LexFmla(new Chain("<< aaaa bbbb cccc>>")                 , "SelfTestLanguage 001");
   new LexFmla(new Chain("a b c")                               , "SelfTestLanguage 001");
   new LexFmla(new Chain(":: href id test")                     , "SelfTestLanguage 001");
   new LexFmla(new Chain(":: href .id test")                     , "SelfTestLanguage 001");
   new LexFmla(new Chain(".href :id")                           , "SelfTestLanguage 001");   

   System.out.println("\n             PROBLEMATIC FORMULAS \n");
   
   //*
   new LexFmla(new Chain("((3 + 2) + (3+7))")                   , "SelfTestLanguage 001");   
   new LexFmla(new Chain("(SIN(bb))")                           , "SelfTestLanguage 001");   
   new LexFmla(new Chain("(SIN(33 bb) + SIN(44 aa))")           , "SelfTestLanguage 001");   
   new LexFmla(new Chain("3 + (SIN(33) + SIN(44)) hello")       , "SelfTestLanguage 001");   
   new LexFmla(new Chain("3 + (SIN(33) + SIN(44)) hello")       , "SelfTestLanguage 001");   
   new LexFmla(new Chain("SIN(33) + SIN(44)")                   , "SelfTestLanguage 001");   
   new LexFmla(new Chain("3 + (SIN(33) + SIN(44)) hello")       , "SelfTestLanguage 001");   
   new LexFmla(new Chain("((aa bb cc++) + (:: 3 2 1))")         , "SelfTestLanguage 001");   
   /**/
   
  }
 
 
 private Chain    text;
 private LexNode  tree;
 private LexLang  lng;

 public LexFmla(Chain fmla, String lngName) throws Exception 
 {
  init();
  this.lng   = LexLang.Lng(lngName);
  this.text  = fmla;
  this.tree  = LexNode.newNode(lng);
  System.out.println("LexFmla \"" + fmla.text() + "\":");  
  tree.parse(fmla);
  System.out.print(tree.toString(false));
  System.out.println("\n·····················································");
 }

 
}






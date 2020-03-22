



package org.xxdevplus.frmlng;

import org.xxdevplus.chain.Chain;
import org.xxdevplus.struct.Pile;
import org.xxdevplus.utl.utl;



public class Pick 
{
 //** @author GeTr Copyright© 1996-2020 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties, You may use, modify, distribute this code under GNU public dual licence free for NON-Commercial use as long as this notice remains unchanged.
 //** @comment Stretch defines a Region of a String with BeginSequence, EndSequence, RepeatCount and several Stretches for the Definition of "skip" and "embed" Areas
 static int _ic;static String ass(boolean xp,String...msg)throws Exception{if(xp)return"Stretch"; throw new Exception(ass(true)+" slfTst:"+utl.str(msg));}private void slfTst()throws Exception{tstSimple();}

 private String            bSeq =                "\"";
 private String            eSeq =                "\"";
 private String             esc =                  "";
 private int                rpt =                   0; // repeat forever
 private int            nestLvl =                   0; // suppose curlyBracks host a String containing curlyBracks itself ... if nestLvl > 0 these inner curlyBracks will be added to the result // NOTYETIMPLEMENTED!!!
 private boolean           list =               false; // 
 private Pile<Pick>        avoid =    new Pile<Pick>();
 private Pile<Pick>        host =    new Pile<Pick>();

 public static Pick sglSQots; 
 public static Pick dblSQots; 
 public static Pick sglFQots; 
 public static Pick dblFQots; 
 public static Pick sglBQots; 
 public static Pick dblBQots; 
 public static Pick sglDQots; 
 public static Pick dblDQots; 
 public static Pick sglFSlhs; 
 public static Pick dblFSlhs; 
 public static Pick sglBSlhs; 
 public static Pick dblBSlhs; 
 public static Pick sglBrcs; 
 public static Pick dblBrcs; 
 public static Pick sglBrks; 
 public static Pick dblBrks; 
 public static Pick sglCrlys; 
 public static Pick dblCrlys; 
 public static Pick sglAngls; 
 public static Pick dblAngls;
 public static Pick sglComma; 
 public static Pick dblComma;
 
 public Pick       avoid   ( Pick       avoid ) throws Exception    { return new Pick(bSeq, eSeq, rpt, esc, nestLvl, list, this.avoid.Clone().Add(avoid),     host.Clone())                ;}
 public Pile<Pick> avoid   (                  ) throws Exception    { return avoid                                                                                                         ;}

 public Pick       host    ( Pick        host ) throws Exception    { return new Pick(bSeq, eSeq, rpt, esc, nestLvl, list, avoid.Clone(),                     this.host.Clone().Add(host)) ;}
 public Pile<Pick> host    (                  ) throws Exception    { return host                                                                                                          ;}
 
 public Pick       list    (                  ) throws Exception    { return new Pick(bSeq, eSeq, rpt, esc, nestLvl, true, avoid.Clone(),                     host.Clone())                ;}

 public Pick       esc     ( String esc       ) throws Exception    { return new Pick(bSeq, eSeq,   1, esc, nestLvl, list, avoid.Clone(),                     host.Clone())                ;}
 public String     esc     (                  ) throws Exception    { return esc                                                                                                           ;}

 public Pick       nestLvl ( int nestLvl      ) throws Exception    { return new Pick(bSeq, eSeq,   1, esc, nestLvl, list, avoid.Clone(),                     host.Clone())                ;}
 public int        nestLvl (                  ) throws Exception    { return nestLvl;}

 public Pick       grabOne (                  ) throws Exception    { return new Pick(bSeq, eSeq,   1, esc, nestLvl, list, avoid.Clone(),                     host.Clone())                ;}
 public Pick       grabAll (                  ) throws Exception    { return new Pick(bSeq, eSeq,   0, esc, nestLvl, list, avoid.Clone(),                     host.Clone())                ;}
 
 public Pick ( String bSeq, String eSeq, int rpt, String esc, int nestLvl, boolean list, Pile<Pick> except, Pile<Pick> host) throws Exception 
 {
  init(); this.list = list; this.bSeq    =     bSeq; this.eSeq    =     eSeq; this.esc     =      esc; this.rpt     =      rpt; this.nestLvl  =   nestLvl;
  if (except == null) this.avoid =  new Pile<Pick>(); else this.avoid =  except;
  if (host == null) this.host =  new Pile<Pick>(); else this.host = host;
 }
 
 public Pick ( String bSeq, String eSeq, int rpt, String esc, int nestLvl, boolean list) throws Exception 
 {
  this(bSeq, eSeq, rpt, esc, nestLvl, list, null, null);
 }

 public Pick ( String bSeq,              int rpt, String esc, int nestLvl ) throws Exception { this ( bSeq , utl.oppst(bSeq) , rpt, esc , nestLvl, false );}
 public Pick (                           int rpt, String esc, int nestLvl ) throws Exception { this ( "\"" ,            "\"" , rpt, esc , nestLvl, false );}
 public Pick ( String bSeq, String eSeq,          String esc, int nestLvl ) throws Exception { this ( bSeq ,            eSeq ,   0, esc , nestLvl, false );}
 public Pick ( String bSeq,              int rpt,             int nestLvl ) throws Exception { this ( bSeq , utl.oppst(bSeq) , rpt,  "" , nestLvl, false );}
 public Pick (                           int rpt,             int nestLvl ) throws Exception { this ( "\"" ,            "\"" , rpt,  "" , nestLvl, false );}
 public Pick ( String bSeq, String eSeq, int rpt,             int nestLvl ) throws Exception { this ( bSeq ,            eSeq , rpt,  "" , nestLvl, false );}
 public Pick ( String bSeq,              int rpt, String esc              ) throws Exception { this ( bSeq , utl.oppst(bSeq) , rpt, esc ,       0, false );}
 public Pick (                           int rpt, String esc              ) throws Exception { this ( "\"" ,            "\"" , rpt, esc ,       0, false );}
 public Pick ( String bSeq, String eSeq, int rpt, String esc              ) throws Exception { this ( bSeq ,            eSeq , rpt, esc ,       0, false );}
 public Pick ( String bSeq, String eSeq,          String esc              ) throws Exception { this ( bSeq ,            eSeq ,   0, esc ,       0, false );}
 public Pick ( String bSeq,                       String esc              ) throws Exception { this ( bSeq , utl.oppst(bSeq) ,   0, esc ,       0, false );}
 public Pick ( String bSeq,              int rpt                          ) throws Exception { this ( bSeq , utl.oppst(bSeq) , rpt,  "" ,       0, false );}
 public Pick ( String bSeq, String eSeq, int rpt                          ) throws Exception { this ( bSeq ,            eSeq , rpt,  "" ,       0, false );}
 public Pick ( String bSeq                                                ) throws Exception { this ( bSeq , utl.oppst(bSeq) ,   0,  "" ,       0, false );}
 public Pick (                                                            ) throws Exception { this ( "\"" ,            "\"" ,   0,  "" ,       0, false );}
 

 protected void init()throws Exception
 {
  if(_ic++==0)
  {
   sglSQots  = new Pick("'",    "\\" );
   dblSQots  = new Pick("''",   "\\" );
   sglFQots  = new Pick("´",    "\\" );
   dblFQots  = new Pick("´´",   "\\" );
   sglBQots  = new Pick("`",    "\\" );
   dblBQots  = new Pick("``",   "\\" );
   sglDQots  = new Pick("\"",   "\\" );
   dblDQots  = new Pick("\"\"", "\\" );
   sglFSlhs  = new Pick("/"          );
   dblFSlhs  = new Pick("//"         );
   sglBSlhs  = new Pick("\\"         );
   dblBSlhs  = new Pick("\\\\"       );
   sglBrcs   = new Pick("("          );
   dblBrcs   = new Pick("(("         );
   sglBrks   = new Pick("["          );
   dblBrks   = new Pick("[["         );
   sglCrlys  = new Pick("{"          );
   dblCrlys  = new Pick("{{"         );
   sglAngls  = new Pick("<"          );
   dblAngls  = new Pick("<<"         );
   sglComma  = new Pick(","          );
   dblComma  = new Pick(",,"         );
   slfTst();
  }
 }

 private static void tstSimple() throws Exception
 {
  
  Pick Brc        = sglBrcs.grabOne();
  Pick qBrc       = sglBrcs.grabOne().avoid(sglSQots).avoid(sglDQots); 
  Pick dqBrc      = sglBrcs.grabOne().avoid(sglDQots); 
  Pick sqBrc      = sglBrcs.grabOne().avoid(sglSQots); 

  Chain   op         = new Chain("(.href[at('//')..-2])[3..-1]");
  // buggy implementation in Pick !!! ass(sglBrcs.upon(op).equals("(.href[at('//')"));

  ass(sglSQots.grabOne().upon(new Chain(" 'efg")).equals("'efg"));

  ass(sglComma.grabOne().host(sglSQots).list().upon(new Chain("   a,   b")).equals("   a,"));
  ass(sglComma.grabOne().host(sglSQots).list().upon(new Chain("   '1,2'a,   b")).equals("   '1,2'a,"));
  ass(sglComma.grabOne().host(sglSQots).list().upon(new Chain("ab")).equals("ab"));
  ass(sglComma.grabOne().host(sglSQots).list().upon(new Chain("a'1,2'b ")).equals("a'1,2'b "));

  ass(sglBrcs.host(sglBrcs).upon(op).equals("(.href[at('//')..-2])"));

  
  
  Chain   trick      = new Chain("3+[[2*5]/2] - (5-2]");
  Chain   notf       = new Chain("3+[[2*5]/2] - [5-2]");  
  Chain   fmla       = new Chain("3+((2*5)/2) - (5-2)");  
  Chain   nsns       = new Chain("3(+((2*5)/))2) - (5-2)");
    
  ass(  Brc.upon(trick).equals("(5-2]"));                         ass(   Brc.upon(notf).equals(""));                ass(   Brc.upon(fmla).equals("((2*5)/2)"));
  ass(   Brc.upon(nsns).equals("(+((2*5)/))"));                   ass(  qBrc.upon(notf).equals(""));                ass(  qBrc.upon(fmla).equals("((2*5)/2)")); 
  ass(  qBrc.upon(nsns).equals("(+((2*5)/))"));                   ass( dqBrc.upon(notf).equals(""));                ass( dqBrc.upon(fmla).equals("((2*5)/2)")); 
  ass( dqBrc.upon(nsns).equals("(+((2*5)/))"));                   ass( sqBrc.upon(notf).equals(""));                ass( sqBrc.upon(fmla).equals("((2*5)/2)"));
  ass( sqBrc.upon(nsns).equals("(+((2*5)/))"));

  Chain   strick     = new Chain("3+[[2*5]\"/(\"2] - (5-2]");
  Chain   snotf      = new Chain("3+[[2*(5<<\")\"]/2] - [5-2]");
  Chain   sfmla      = new Chain("3+ \"))a((\" + ((2*5)/2) - \"))a((\" + (5-2) - cc");
  Chain   snsns      = new Chain("3+ \"))\\\"a((\" + ((2*5)/\\\"2) - \"))a(\"\\\"(\" + (5-2) - cc");
 
  ass(   Brc.upon(trick).equals("(5-2]"));                         ass( dqBrc.upon(trick).equals("(5-2]"));         ass( sqBrc.upon(trick).equals("(5-2]"));                                 ass(  qBrc.upon(trick).equals("(5-2]"));
  //ass(   Brc.upon(strick).equals(""));                             ass( dqBrc.upon(strick).equals(""));             ass( sqBrc.upon(strick).equals(""));                                     ass(  qBrc.upon(strick).equals(""));

  ass(   Brc.upon(snotf).equals("(5<<\")"));                       
  ass( dqBrc.upon(snotf).equals("(5<<\")"));
  //ass(dqBrc.host(sglDQots).upon(snotf).equals(""));
  ass( sqBrc.upon(snotf).equals("(5<<\")"));                               
  ass(  qBrc.upon(snotf).equals("(5<<\")"));
  //ass(qBrc.host(sglDQots).upon(snotf).equals(""));
  
  ass(   Brc.upon(sfmla).equals("((\" + ((2*5)/2) - \"))"));       
  ass( dqBrc.upon(sfmla).equals("((2*5)/2)"));     
  ass( sqBrc.upon(sfmla).equals("((\" + ((2*5)/2) - \"))"));               
  ass(  qBrc.upon(sfmla).equals("((2*5)/2)"));
  
  ass(   Brc.upon(snsns).equals("((\" + ((2*5)/\\\"2) - \"))"));   
  ass( dqBrc.upon(snsns).equals("((2*5)/\\\"2)")); 
  ass( sqBrc.upon(snsns).equals("((\" + ((2*5)/\\\"2) - \"))"));           
  ass(  qBrc.upon(snsns).equals("((2*5)/\\\"2)"));
   
  Pick Dqot       = sglDQots.grabOne();
  Pick sqDqot     = sglDQots.grabOne().avoid(sglSQots);

  ass(   Dqot.upon(trick).equals(""));   
  ass( sqDqot.upon(trick).equals("")); 
  ass(   Dqot.upon(strick).equals("\"/(\""));   
  ass( sqDqot.upon(strick).equals("\"/(\"")); 

  ass(   Dqot.upon(snotf).equals("\")\""));   
  ass( sqDqot.upon(snotf).equals("\")\"")); 

  ass(   Dqot.upon(sfmla).equals("\"))a((\""));   
  ass( sqDqot.upon(sfmla).equals("\"))a((\"")); 
 
  ass(   Dqot.upon(snsns).equals("\"))\\\"a((\""));   
  ass( sqDqot.upon(snsns).equals("\"))\\\"a((\"")); 
  
  Chain xstr = new Chain("he had a \"'bad \"bad\"'\"  '(very \"bad\")' day");
  Chain ystr = new Chain("he had a \"'bad \"bad\"'\"  '(very \"b'ad\")' day");
  Pick sqDqots       = sglDQots.avoid(sglSQots);
  Pick dqSqots       = sglSQots.avoid(sglDQots);
//Pick sqDqotsN      = sqDqots.nestLvl(true);   // a single quoted region within double quoted region can find its own smaller double quoted region at least when repeat count > 1
//Pick dqSqotsN      = dqSqots.nestLvl(true);   // a double quoted region within single quoted region can find its own smaller single quoted region at least when repeat count > 1

  ass( sqDqots.grabOne().upon(xstr).equals("\"'bad \""));   
  ass( dqSqots.grabOne().upon(xstr).equals("'(very \"bad\")'"));   
  ass( sqDqots.grabOne().upon(ystr).equals("\"'bad \""));   
  ass( dqSqots.grabOne().upon(ystr).equals("'(very \"b'"));   

  ass(sqDqots.host(sglSQots).grabOne().upon(ystr).equals("\"'bad \"bad\"'\""));   
  ass(dqSqots.host(sglDQots).grabOne().upon(ystr).equals("'(very \"b'ad\")'"));     
 }

 private Chain pickout(boolean insist, Chain txt, boolean hot, int rpt, int nestLvl, Pile<Pick> skip, Pile<Pick> host, boolean cover) throws Exception
 {
  if (txt.len() == 0) return txt.after(-1);
  Chain oTxt = txt;
  for (Pick p: skip) txt = txt.less(p.upon(txt)); 
  Chain b = txt.at(1, bSeq); 
  if (list) b = txt.before(1); else if (b.len() == 0) b = null;
  while (hot && (esc.length() > 0) && (b != null) && (endsWithOddOccurs(txt.before(b), esc))) b = txt.after(b).at(1, bSeq); 
  if (b == null) return txt.after(-1);
  hot = true;
  Chain rest = oTxt.after(b);
  for (Pick p: host) rest = rest.less(p.upon(rest)); 
  txt = txt.upto(b).plus(rest);



  Chain n = txt.after(b).at(1, true, eSeq, bSeq); // next (n) may be ( or may be ), in case of ( it ma be an inner () or may be an escaped ( 


  // maybe the next bSeq or next eSeq (n) is prefixed with an odd number of esc characters ... then continue to the next n ... provieded we are in an hot region
  while (hot && (esc.length() > 0) && (n.len() > 0) && (endsWithOddOccurs(txt.before(n), esc))) n = txt.after(n).at(1, true, eSeq, bSeq); 



  if (n.len() == 0) if (insist) throw new Exception("missing " + eSeq); // else return n;
  if ((n.equals(eSeq)) || ((n.len() == 0))) 
   if (--rpt == 0) 
    return cover ? txt.from(b).upto(n).upon(oTxt) : txt.after(b).before(n).upon(oTxt); 
   else 
    return cover? txt.from(b).upto(n).upon(oTxt).plus(pickout(insist, txt.after(n), false, rpt, nestLvl, skip, host, cover)) : txt.after(b).before(n).upon(oTxt).plus(pickout(insist, txt.after(n), false, rpt, nestLvl, skip, host, cover)); 
  Chain inner = pickout(insist, txt.from(n), false, rpt, nestLvl, skip, host, true);
  if (inner.len() == 0) if (insist) throw new Exception("missing " + eSeq); else return inner;
  return pickout(insist, txt.less(inner).from(b), false, rpt, nestLvl, skip, host, cover).upon(oTxt);  
 }

 public Chain upon (Chain txt                 ) throws Exception { return pickout(false,  txt, false, rpt, nestLvl, avoid, host, true)  ; }
 public Chain on   (Chain txt                 ) throws Exception { return pickout(false,  txt, false, rpt, nestLvl, avoid, host, false) ; }
 public Chain upon (boolean insist, Chain txt ) throws Exception { return pickout(insist, txt, false, rpt, nestLvl, avoid, host, true)  ; }
 public Chain on   (boolean insist, Chain txt ) throws Exception { return pickout(insist, txt, false, rpt, nestLvl, avoid, host, false) ; }

 private boolean endsWithOddOccurs(Chain txt, String esc) throws Exception
 {
  int ret = 0;
  while (txt.endsWith(esc))
  {
   ret++;
   txt = txt.before(-1, esc);
  }
  return (ret % 2 == 1);
 }
   
}

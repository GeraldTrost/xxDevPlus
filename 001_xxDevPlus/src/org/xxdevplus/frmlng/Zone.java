


//** @author  GeTr Copyright© 1996-2020 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties
//** @license You may use, modify, distribute this code under GNU public dual licence free for NON-Commercial use as long as this notice remains unchanged.
//** @comment Old any buggy implementation of Pick

package org.xxdevplus.frmlng;

import org.xxdevplus.chain.Chain;
import org.xxdevplus.struct.Pile;
import java.util.List;
import java.util.ArrayList;
import org.xxdevplus.utl.utl;

/**
 *
 * @author GeTr
 */
public class Zone
{
 //Copyright© 1996-2020 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties, You may use, modify, distribute this code under GNU public licence for NON-Commercial use as long as this notice remains unchanged.
 private static boolean selfTested  = false;  private static String ass(boolean expr) throws Exception { if (!expr) throw new Exception(ass(true) + " SelfTest Failure"); return "Zone"; }

 public static Zone quotations  = new Zone(new Pile<String>(), new Pile<String>("", 0, "\"", "\"", "||:0"));
 public static Zone charrays    = new Zone(new Pile<String>(), new Pile<String>("", 0, "'", "'", "||:0"));
 public static Zone bracelets   = new Zone(new Pile<String>(), new Pile<String>("", 0, "(", ")", "||:0"));
 public static Zone brackets    = new Zone(new Pile<String>(), new Pile<String>("", 0, "[", "]", "||:0"));
 public static Zone curlybracks = new Zone(new Pile<String>(), new Pile<String>("", 0, "{", "}", "||:0"));
 public static Zone pinbracks   = new Zone(new Pile<String>(), new Pile<String>("", 0, "<", ">", "||:0"));

 public static Zone quotation   = new Zone(new Pile<String>(), new Pile<String>("", 0, "\"", "\"", "||:1"));
 public static Zone charray     = new Zone(new Pile<String>(), new Pile<String>("", 0, "'", "'", "||:1"));
 public static Zone bracelet    = new Zone(new Pile<String>(), new Pile<String>("", 0, "(", ")", "||:1"));
 public static Zone bracket     = new Zone(new Pile<String>(), new Pile<String>("", 0, "[", "]", "||:1"));
 public static Zone curlybrack  = new Zone(new Pile<String>(), new Pile<String>("", 0, "{", "}", "||:1"));
 public static Zone pinbrack    = new Zone(new Pile<String>(), new Pile<String>("", 0, "<", ">", "||:1"));

 private static void selfTest() throws Exception
 {
  selfTested = true;
  Chain r0 = new Chain("' ', sbj.id, 'ape:', 'birth', ' ', LEFT(CONVERT(VARCHAR(23), sbj.birth, 121), 4) + '-' + LEFT(RIGHT(CONVERT(VARCHAR(23), sbj.birth, 121), 18), 2) + '-' + LEFT(RIGHT(CONVERT(VARCHAR(23), sbj.birth, 121), 15), 2) + ' ' + LEFT(RIGHT(CONVERT(VARCHAR(23), sbj.birth, 121), 12), 2) + ':' + LEFT(RIGHT(CONVERT(VARCHAR(23), sbj.birth, 121), 9), 2) + ':' + LEFT(RIGHT(CONVERT(VARCHAR(23), sbj.birth, 121), 6), 2) + '.' + RIGHT(CONVERT(VARCHAR(23), sbj.birth, 121), 3)");
  Chain r1 = new Chain("a('b')c");
  Chain r2 = new Chain("a('b')c('d')e");
  Chain r3 = new Chain("('field') + ('2')");
  ass(  charrays.upon (r1).equals("'b'"));
  ass( bracelets.upon (r1).equals("('b')"));
  ass(  charrays.upon (r2).equals("'b''d'"));
  ass( bracelets.upon (r2).equals("('b')('d')"));
  ass(  charrays.upon (r0).equals("' ''ape:''birth'' ''-''-'' '':'':''.'"));
  ass( bracelets.upon (r0).equals("(CONVERT(VARCHAR(23), sbj.birth, 121), 4)(RIGHT(CONVERT(VARCHAR(23), sbj.birth, 121), 18), 2)(RIGHT(CONVERT(VARCHAR(23), sbj.birth, 121), 15), 2)(RIGHT(CONVERT(VARCHAR(23), sbj.birth, 121), 12), 2)(RIGHT(CONVERT(VARCHAR(23), sbj.birth, 121), 9), 2)(RIGHT(CONVERT(VARCHAR(23), sbj.birth, 121), 6), 2)(CONVERT(VARCHAR(23), sbj.birth, 121), 3)"));
  ass(   charray.upon (r3).equals("'field'"));
  ass(   charray.on   (r3).equals("field"));
  ass(  bracelet.upon (r3).equals("('field')"));
  ass(  bracelet.on   (r3).equals("'field'"));
 }

 private void init() throws Exception { if (!selfTested) selfTest(); }

 private Pile<String>   tok;  // StartSequence - Tokens
 private Pile<String>[] def;  // StartSequence - Tokens and several endSequence Tokens for each StartSequence - Token
 private int[]          rpt;  // Repeat len for each def, 0 = endless, 5 = upto 5 times
 public  boolean []     nested;

 private void init(Pile<String> trm, Pile<String> strings) throws Exception
 {
  init();
  List<Pile<String>> results = new ArrayList<Pile<String>>(); List<Integer> repeats = new ArrayList<Integer>(); int start = 1; int i = 1; int repeat = 1;
  strings = strings.Clone(); //strings[i] will possibly be altered to "" in this method but we are not allowed to change the definition pile.
  do
  {
   if ((i > strings.Len()) || ((strings.g(i).equals("")) && (start != i)))
   {
    if ((i <= strings.Len()) || (!strings.g(strings.Len()).equals("")))
    {
     Pile<String> res = new Pile<String>(i - start - 1);
     for (int j = start + 1; j < i; j++) res.s(strings.g(j), j - start);
     res.Name(strings.g(start)); res.Add(trm);
     results.add(res); repeats.add(repeat); //for (int j = 1; j <= repeat; j++) results.add(res);
     start = i + 1; repeat = 1;
    }
   }
   i++;
   if (i <= strings.Len()) if (strings.g(i).startsWith("||:")) { repeat = Integer.parseInt(strings.g(i).substring(3)); strings.s("", i); }
  }
  while (i <= strings.Len() + 1);
  def = new Pile<String>(0).newPileOf_TypArray(results.size()); for (int j = 0; j < results.size(); j++) def[j] = results.get(j);
  rpt = new int[results.size()]; for (int j = 0; j < results.size(); j++) rpt[j] = repeats.get(j);
  tok = new Pile<String>(def.length); for (int j = 1; j <= def.length; j++) tok.s((def[j - 1]).Name(), j);
  nested = new boolean[results.size()];
  for (i = 1; i <= def.length; i++) { nested[i - 1] = ((def[i - 1]).g(0).length() > 0); 
  for (int j = 1; j <= (def[i - 1]).Len(); j++) if ((def[i - 1]).g(j).equals(tok.g(i))) nested[i - 1] = false; }
 }

 public Zone(String...                         def) { try { init(new Pile<String>(0), new Pile<String>("", 0, def)); } catch (Exception ex) {} }
 public Zone(Pile<String>                      def) { try { init(new Pile<String>(0), def);                          } catch (Exception ex) {} }
 public Zone(Pile<String> trm,       String... str) { try { init(trm, new Pile<String>("", 0, str));                 } catch (Exception ex) {} }
 public Zone(Pile<String> trm,    Pile<String> def) { try { init(trm, def);                                          } catch (Exception ex) {} }

 private Chain extract(Chain source, boolean includeTokens) throws Exception
 {
  Chain ret = source.upto(0).plus(source.from(source.len() + 1));  // FromLeftToRight strategy Chain ret = source.from(source.len + 1)
  for (int i = 1; i <= def.length; i++)
  {
   int k = 1;
   while ((k <= rpt[i - 1]) || ((rpt[i - 1] == 0) && (source.len() > 0)))
   {

    Chain leadIn = source.at(1, (def[i - 1]).Name());
    if ((leadIn.len() == 0) && ((def[i - 1]).Name().length() > 0)) return ret;
    if (includeTokens) if (ret.len() == 0) ret = leadIn; else ret = ret.plus(leadIn);
    Chain res = source.after(leadIn);
    Chain leadOut = null;
    int occur = 0; // first we try the first closing bracelet, only in case that this will be a non-matching bracelet then wee will try the second, third, fourth etc. closing closing bracelet

    do 
    { 
     occur++; 
     for (int j = (def[i - 1]).Len(); j > 0; j--) 
     { 
      Chain token = res.at(occur, (def[i - 1]).g(j)); 
      if (token.len() > 0) leadOut = token; 
     } 
    } 
    while (nested[i - 1] && (res.upto(leadOut).at(-occur, leadIn.text()).len() > 0));
    
    if (leadOut != null) res = res.before(leadOut);
    if (leadOut == null) return (ret.len() == 0) ? res : ret.plus(res);
    if (ret.len() == 0) ret = includeTokens ? source.after(leadIn).upto(leadOut) : source.after(leadIn).before(leadOut); else ret = includeTokens ? ret.plus(source.after(leadIn).upto(leadOut)) : ret.plus(source.after(leadIn).before(leadOut));
    source = source.after(leadOut);
    k++;
   }
  }
  return ret;
 }

 public Chain upon (Chain source) throws Exception { return extract(source, true); }
 public Chain on   (Chain source) throws Exception { return extract(source, false); }
}


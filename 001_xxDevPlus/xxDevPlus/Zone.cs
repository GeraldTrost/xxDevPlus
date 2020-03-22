


//** @author  GeTr Copyright© 1996-2020 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties
//** @license You may use, modify, distribute this code under GNU public dual licence free for NON-Commercial use as long as this notice remains unchanged.
//** @comment Old any buggy implementation of Pick


using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;



namespace org_xxdevplus_frmlng
{
 public class Zone
 {
  //Copyright© 1996-2012 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties, You may use, modify, distribute this code under GNU public licence for NON-Commercial use as long as this notice remains unchanged.
  private static bool selfTested  = false; private static string ass(bool expr) { if (!expr) throw new Exception(ass(true) + " SelfTest Failure"); return "Zone"; }

  public static Zone quotations  = new Zone(new Pile<String>(), new Pile<String>("", true, "\"", "\"", "||:0"));
  public static Zone charrays    = new Zone(new Pile<String>(), new Pile<String>("", true, "'", "'", "||:0"));
  public static Zone bracelets   = new Zone(new Pile<String>(), new Pile<String>("1", true, "(", ")", "||:0"));
  public static Zone brackets    = new Zone(new Pile<String>(), new Pile<String>("1", true, "[", "]", "||:0"));
  public static Zone curlybracks = new Zone(new Pile<String>(), new Pile<String>("1", true, "{", "}", "||:0"));

  public static Zone quotation   = new Zone(new Pile<String>(), new Pile<String>("", true, "\"", "\"", "||:1"));
  public static Zone charray     = new Zone(new Pile<String>(), new Pile<String>("", true, "'", "'", "||:1"));
  public static Zone bracelet    = new Zone(new Pile<String>(), new Pile<String>("1", true, "(", ")", "||:1"));
  public static Zone bracket     = new Zone(new Pile<String>(), new Pile<String>("1", true, "[", "]", "||:1"));
  public static Zone curlybrack  = new Zone(new Pile<String>(), new Pile<String>("1", true, "{", "}", "||:1"));
  
  private static void selfTest() 
  {
   if ((quotations == null) || (charrays == null) || (bracelets == null) || (brackets == null) || (curlybracks == null) || (quotation == null) || (charray == null) || (bracelet == null) || (bracket == null) || (curlybrack == null)) return;
   selfTested = true;

   Reach r0 = new Reach("' ', sbj.id, 'ape:', 'birth', ' ', LEFT(CONVERT(VARCHAR(23), sbj.birth, 121), 4) + '-' + LEFT(RIGHT(CONVERT(VARCHAR(23), sbj.birth, 121), 18), 2) + '-' + LEFT(RIGHT(CONVERT(VARCHAR(23), sbj.birth, 121), 15), 2) + ' ' + LEFT(RIGHT(CONVERT(VARCHAR(23), sbj.birth, 121), 12), 2) + ':' + LEFT(RIGHT(CONVERT(VARCHAR(23), sbj.birth, 121), 9), 2) + ':' + LEFT(RIGHT(CONVERT(VARCHAR(23), sbj.birth, 121), 6), 2) + '.' + RIGHT(CONVERT(VARCHAR(23), sbj.birth, 121), 3)");
   Reach r1 = new Reach("a('b')c");
   Reach r2 = new Reach("a('b')c('d')e");
   Reach r3 = new Reach("('field') + ('2')");

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

  private void init() { if (!selfTested) selfTest(); }

  private Pile<string>   tok;  // StartSequence - Tokens
  private Pile<string>[] def;  // StartSequence - Tokens and several endSequence Tokens for each StartSequence - Token
  private long[]         rpt;  // Repeat count for each def, 0 = endless, 5 = upto 5 times
  public  bool[]         nested;

  private void init(Pile<string> trm, Pile<string> strings)
  {
   init();
   Pile<Pile<string>> results = new Pile<Pile<string>>(); Pile<long> repeats = new Pile<long>(); int start = 1; int i = 1; long repeat = 1;
   strings = strings.Clone(); //strings[i] will possibly be altered to "" in this method but we are not allowed to change the definition pile.
   do
   {
    if ((i > strings.Len) || ((strings[i].Equals("")) && (start != i)))
    {
     if ((i <= strings.Len) || (!strings[strings.Len].Equals("")))
     {
      Pile<string> res = new Pile<string>(i - start - 1);
      for (int j = start + 1; j < i; j++) res[j - start] = strings[j];
      res.Name = strings[start]; res.Add(trm);
      results.Add(res); repeats.Add(repeat); //for (long j = 1; j <= repeat; j++) results.Add(res);
      start = i + 1; repeat = 1;
     }
    }
    i++;
    if (i <= strings.Len) if (strings[i].StartsWith("||:")) { repeat = long.Parse(strings[i].Substring(3)); strings[i] = ""; }
   }
   while (i <= strings.Len + 1);
   def = new Pile<string>[results.Len]; for (int j = 1; j <= results.Len; j++) def[j - 1] = results[j];
   rpt = new long[results.Len]; for (int j = 1; j <= results.Len; j++) rpt[j - 1] = repeats[j];
   tok = new Pile<string>(def.Length); for (int j = 1; j <= def.Length; j++) tok[j] = def[j - 1].Name;
   nested = new bool[results.Len];
   for (i = 1; i <= def.Length; i++) { nested[i - 1] = (def[i - 1][0].Length > 0); for (int j = 1; j <= def[i - 1].Len; j++) if (def[i - 1][j].Equals(tok[i])) nested[i - 1] = false; }
  }

  public Zone(params string[] def)                    { init(new Pile<string>(), new Pile<string>("", true, def)); }
  public Zone(Pile<string> def)                       { init(new Pile<string>(), def); }
  public Zone(Pile<string> trm, params string[] str)  { init(trm, new Pile<string>("", true, str)); }
  public Zone(Pile<string> trm, Pile<string> def)     { init(trm, def); }


  private Reach extract(Reach source, bool includeTokens) 
  {
   Reach ret = source.upto(0) + source.from(source.len + 1);  // FromLeftToRight strategy Reach ret = source.from(source.len + 1)
   for (long i = 1; i <= def.Length; i++)
   {
    long k = 1;
    while ((k <= rpt[i - 1]) || ((rpt[i - 1] == 0) && (source.len > 0)))
    {
     Reach leadIn = source.at(1, utl.dmyBool("al(occur, token) is planned"), def[i - 1].Name);
     if ((leadIn.len == 0) && (def[i - 1].Name.Length > 0)) return ret;
     if (includeTokens) if (ret.len == 0) ret = leadIn; else ret = ret + leadIn;
     Reach res = source.after(leadIn);
     Reach leadOut = null;
     int occur = 0; // first we try the first closing bracelet, only in case that this will be a non-matching bracelet then wee will try the second, third, fourth etc. closing closing bracelet
     do { occur++; for (int j = (def[i - 1]).Len; j > 0; j--) { Reach token = res.at(occur, utl.dmyBool("al(occur, token) is planned"), (def[i - 1])[j]); if (token.len > 0) leadOut = token; } } while (nested[i - 1] && (res.upto(leadOut).at(-occur, utl.dmyBool("al(occur, token) is planned"), leadIn.text).len > 0));
     if (leadOut != null) res = res.before(leadOut);
     if (leadOut == null) return (ret.len == 0) ? res : ret + res;
     if (ret.len == 0) ret = includeTokens ? source.after(leadIn).upto(leadOut) : source.after(leadIn).before(leadOut); else ret = includeTokens ? ret + source.after(leadIn).upto(leadOut) : ret + source.after(leadIn).before(leadOut);
     source = source.after(leadOut);
     k++;
    }
   }
   return ret;
  }

  public Reach upon(Reach source) { return extract(source, true); }

  public Reach on(Reach source){ return extract(source, false); }

 }

}


















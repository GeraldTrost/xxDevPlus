


//** @author  GeTr Copyright© 1996-2020 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties
//** @license You may use, modify, distribute this code under GNU public dual licence free for NON-Commercial use as long as this notice remains unchanged.
//** @comment String Alternative with commonly used Char-Buffers

package org.xxdevplus.chain;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URI;
import org.jsoup.nodes.Element;
import org.xxdevplus.data.FtxProvider;
import org.xxdevplus.struct.KeyPile;
import org.xxdevplus.struct.MappedBuffer;
import org.xxdevplus.struct.Pile;

public class Chain extends Chn
{
 //Copyright© 1996-2020 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties, You may use, modify, distribute this code under GNU public licence for NON-Commercial use as long as this notice remains unchanged.

 private static boolean selfTested  = false; private static String ass(boolean expr) throws Exception { if (!expr) throw new Exception(ass(true) + " SelfTest Failure"); return "Chain"; }

  public static Chain Empty;
  public static Chain Blank;
  
  private static void selfTest() throws Exception
  {
   selfTested    = true;
   Empty         = new Chain("");  // need to inizialize the static constant !
   Blank         = new Chain(" "); // need to inizialize the static constant !
   ftWordDelims  = " ^~°\t\n\r\"'´`.,;$§%\\/:*+-_?!|&<=>()[]{}";
   String[] test = {"a", "b", "A", "B" };
   
   Chain t1 = new Chain("bac");
   Chain t2 = new Chain("BAC");


   ass(t2.From(1, "B").equals("BAC"));
   ass(t2.At(1, false, test).equals("B"));
   ass(t2.From(1, true, test).equals("BAC"));

   
   //ass(t1.at(1, true, test).equals("a"));
   ass(t1.at(1, false, test).equals("b"));
   //ass(t2.at(1, true, test).equals("A"));
   ass(t2.at(1, false, test).equals("B"));

   //ass(t1.At(1, true, test).equals("a"));
   ass(t1.At(1, false, test).equals("b"));
   //ass(t2.At(1, true, test).equals("A"));
   ass(t2.At(1, false, test).equals("B"));
   
   
   KeyPile<String, Pile<Integer>> ftWinx = new KeyPile<String, Pile<Integer>> ();
   Chain t001 = Chain.Load("New York company is a new york based company.".toLowerCase(), "w§§c:/test.html", null, 0L);
   ass(t001.upto(-3, "new york").equals(""));
   ass(t001.after(t001.upto(-3, "new york")).equals("new york company is a new york based company."));
   ass(t001.upto(-2, "new york").equals("new york"));
   ass(t001.upto(-1, "new york").equals("new york company is a new york"));
   ass(t001.from(2, "new york").equals("new york based company."));
   ass(t001.upto(1, "new york").equals("new york"));
   ass(t001.from(1, "new york").equals("new york company is a new york based company."));
   ass(t001.from(1, "new york").equals("new york company is a new york based company."));
   ass(t001.at  (1, "new york").equals("new york"));
   ass(t001.after(1, "new york").equals(" company is a new york based company."));
   ass(t001.after(1, "new york").ftWinx().Len() == 6);
   
   Chain c01 = new Chain("ab123ab");
   ass(c01.upto(-2, "ab").equals("ab"));
   ass(c01.upto(-2, false, "ab", "bc").equals("ab"));
   ass(c01.upto(-2, true, "ab", "bc").equals("ab"));
   ass(c01.before(-2, "ab").equals(""));
   ass(c01.after(-2, "ab").equals("123ab"));
   Chain cr = new Chain("abcdef");
   ass(cr.pad(10  , "::=").equals("::=:abcdef"));
   ass(cr.pad(-10 , "::=").equals("abcdef=::="));
   ass(Chain.Empty.pad(3 , " ").equals("   "));
   Chain cd = cr.at(1, "c").plus(cr.at(1, "e"));
   ass(cr.less(cd).equals("abdf"));
   ass(cr.at(cd).equals("ce"));
   ass(cr.less(cr.before(cd).plus(cr.after(cd))).equals("cde"));
   ass(cd.upon(cr).equals("cde"));
   ass(cd.on  (cr).equals("d"));
   cd = cr.at(1, "c").plus(cr.at(1, "h"));
   ass(cr.at(cd).equals("c"));
   ass(cr.less(cr.before(cd).plus(cr.after(cd))).equals("cdef"));
   ass(cd.upon(cr).equals("cdef"));
   ass(cd.on  (cr).equals("def"));
   cd = cr.at(-1, "h").plus(cr.at(-1, "e"));
   ass(cr.at(cd).equals("e"));
   ass(cr.less(cr.before(cd).plus(cr.after(cd))).equals("abcde"));
   ass(cd.upon(cr).equals("abcde"));
   ass(cd.on  (cr).equals("abcd"));

   Chain ch1 = new Chain("abababa\"  ) ;xyxy\" )   ;wowowo\"   );zzss");
   ass(ch1.at(" ",  1, "abc", "\");").equals("\"  ) ;"));
   ass(ch1.at(" ",  2, "abc", "\");").equals("\" )   ;"));
   ass(ch1.at(" ",  3, "abc", "\");").equals("\"   );"));
   ass(ch1.at(" ",  4, "abc", "\");").equals(""));
   ass(ch1.at(" ", -3, "abc", "\");").equals("\"  ) ;"));
   ass(ch1.at(" ", -2, "abc", "\");").equals("\" )   ;"));
   ass(ch1.at(" ", -1, "abc", "\");").equals("\"   );"));
   ass(ch1.at(" ", -4, "abc", "\");").equals(""));
   
   Chain c1 = new Chain("  aaa  bbb  cccc");
   ass(c1.count    ("b"   ) == 3);
   ass(c1.count    ("  "  ) == 3);
   ass(c1.collapse (" "   ).equals(" aaa bbb cccc"));
   ass(c1.collapse ("c"   ).equals("  aaa  bbb  c"));
   ass(c1.collapse ("bb"  ).equals("  aaa  bbb  cccc"));
   ass(c1.collapse ("cc"  ).equals("  aaa  bbb  cc"));
   
   Chain r1 = new Chain("ab");
   Chain r2 = new Chain("..X--XX___XX,,XX");
   Chain r3 = new Chain("aa bb  cc ");

   ass(r3.firstWord(" ").equals(""));
   ass(r3.firstWord(" a").equals("aa "));
   ass(r3.lastWord (" ").equals(" "));

   ass(r2.upto( 1, false, "X,", "X_", "X-").equals("..X-"));
   ass(r2.upto( 2, false, "X,", "X_", "X-").equals("..X--XX_"));
   ass(r2.upto( 3, false, "X,", "X_", "X-").equals("..X--XX___XX,"));
   ass(r2.upto(-1, false, "X,", "X_", "X-").equals("..X--XX___XX,"));
   ass(r2.upto(-2, false, "X,", "X_", "X-").equals("..X--XX_"));
   ass(r2.upto(-3, false, "X,", "X_", "X-").equals("..X-"));

   ass(r2.upto( 1, false, ",XX", "_XX", "X-", "..").equals(".."));
   ass(r2.upto( 2, false, ",XX", "_XX", "X-", "..").equals("..X-"));
   ass(r2.upto( 3, false, ",XX", "_XX", "X-", "..").equals("..X--XX___XX"));
   ass(r2.upto(-1, false, ",XX", "_XX", "X-", "..").equals("..X--XX___XX,,XX"));
   ass(r2.upto(-2, false, ",XX", "_XX", "X-", "..").equals("..X--XX___XX"));
   ass(r2.upto(-3, false, ",XX", "_XX", "X-", "..").equals("..X-"));

   ass(r2.from( 1, false, "X,", "X_", "X-").equals("X--XX___XX,,XX"));
   ass(r2.from( 2, false, "X,", "X_", "X-").equals("X___XX,,XX"));
   ass(r2.from( 3, false, "X,", "X_", "X-").equals("X,,XX"));
   ass(r2.from(-1, false, "X,", "X_", "X-").equals("X,,XX"));
   ass(r2.from(-2, false, "X,", "X_", "X-").equals("X___XX,,XX"));
   ass(r2.from(-3, false, "X,", "X_", "X-").equals("X--XX___XX,,XX"));

   ass(r2.from( 1, false, ",XX", "_XX", "X-", "..").equals("..X--XX___XX,,XX"));
   ass(r2.from( 2, false, ",XX", "_XX", "X-", "..").equals("X--XX___XX,,XX"));
   ass(r2.from( 3, false, ",XX", "_XX", "X-", "..").equals("_XX,,XX"));
   ass(r2.from(-1, false, ",XX", "_XX", "X-", "..").equals(",XX"));
   ass(r2.from(-2, false, ",XX", "_XX", "X-", "..").equals("_XX,,XX"));
   ass(r2.from(-3, false, ",XX", "_XX", "X-", "..").equals("X--XX___XX,,XX"));

   ass(r1.upto(1).equals("a")); ass(r1.before(1).equals("")); ass(r1.from(1).equals("ab")); ass(r1.after(1).equals("b")); ass(r1.upto(-1).equals("ab")); ass(r1.before(-1).equals("a")); ass(r1.from(-1).equals("b")); ass(r1.after(-1).equals(""));
   
   ass(r2.upto(2, true, "XX").equals("..X--XX___XX")); ass(r2.upto(2, true, "X").equals("..X--X")); ass(r2.upto(2, true, "XX", "X").equals("..X--X")); ass(r2.upto(2, true, "X", "XX").equals("..X--X")); ass(r2.upto(-2, true, "XX").equals("..X--XX___XX")); ass(r2.upto(-2, true, "X").equals("..X--XX___XX,,X")); ass(r2.upto(-2, true, "XX", "X").equals("..X--XX___XX,,X")); ass(r2.upto(-2, true, "X", "XX").equals("..X--XX___XX,,X"));
   ass(r2.from(2, true, "XX").equals("XX,,XX")); ass(r2.from(2, true, "X").equals("XX___XX,,XX")); ass(r2.from(2, true, "XX", "X").equals("XX___XX,,XX")); ass(r2.from(2, true, "X", "XX").equals("XX___XX,,XX")); ass(r2.from(-2, true, "XX").equals("XX,,XX")); ass(r2.from(-2, true, "X").equals("XX")); ass(r2.from(-2, true, "XX", "X").equals("XX")); ass(r2.from(-2, true, "X", "XX").equals("XX"));
   ass(r2.before(2, true, "XX").equals("..X--XX___")); ass(r2.before(2, true, "X").equals("..X--")); ass(r2.before(2, true, "XX", "X").equals("..X--")); ass(r2.before(2, true, "X", "XX").equals("..X--")); ass(r2.before(-2, true, "XX").equals("..X--XX___")); ass(r2.before(-2, true, "X").equals("..X--XX___XX,,")); ass(r2.before(-2, true, "XX", "X").equals("..X--XX___XX,,")); ass(r2.before(-2, true, "X", "XX").equals("..X--XX___XX,,"));
   ass(r2.after(2, true, "XX").equals(",,XX")); ass(r2.after(2, true, "X").equals("X___XX,,XX")); ass(r2.after(2, true, "XX", "X").equals("X___XX,,XX")); ass(r2.after(2, true, "X", "XX").equals("X___XX,,XX")); ass(r2.after(-2, true, "XX").equals(",,XX")); ass(r2.after(-2, true, "X").equals("X")); ass(r2.after(-2, true, "XX", "X").equals("X")); ass(r2.after(-2, true, "X", "XX").equals("X"));
   ass(r2.at(2, true, "XX").equals("XX")); ass(r2.at(2, true, "X").equals("X")); ass(r2.at(2, true, "XX", "X").equals("X")); ass(r2.at(2, true, "X", "XX").equals("X")); ass(r2.at(-2, true, "XX").equals("XX")); ass(r2.at(-2, true, "X").equals("X")); ass(r2.at(-2, true, "XX", "X").equals("X")); ass(r2.at(-2, true, "X", "XX").equals("X"));

  }

 private void init() throws Exception { if (!selfTested) selfTest(); }

 //public static BufOp      operator!(Chain r) { return new BufOp(r); }     //this offers an alternative syntax to "._buf()" in c#
 //public static Extractor operator~(Chain r) { return new Extractor(r); }  //this offers an alternative syntax to ".XTR()" in c#

 public static boolean useReachNotify = false;

 public    BufOp        _buf()                           { return new BufOp(this); }
 public    Extract      XTR()                            { return new Extract(this); }
 public    Delete       DEL()                            { return new Delete(false, this); }
 public    Delete       DEL(int cnt)                     { return new Delete(false, this, cnt); }
 public    ReplaceWith  RPW(String txt)                  { return new ReplaceWith(false, this, txt); }
 public    ReplaceWith  RPW(Chain txt) throws Exception  { return new ReplaceWith(false, this, txt); }
 public    Insert       INS(String txt)                  { return new Insert(false, this, txt); }
 public    Insert       INS(Chain txt) throws Exception  { return new Insert(false, this, txt); }


 //public    Chain   (String text, KeyPile<String, Pile<Integer>> ftWinx) throws Exception { super(text, ftWinx);             init();}

 public                 Chain         (String                                        text) throws Exception { super(text);                     init();}
 public                 Chain         (Chain first,                        Chain   second) throws Exception { super(first, second);            init();}
 protected              Chain         (Chain source,                       Restrict    rt) throws Exception { super(source, rt);               init();}
 protected              Chain         (MappedBuffer mBuf, String sLbl,     String    eLbl) throws Exception { super(mBuf, sLbl, eLbl);         init();}

 //public static explicit operator String(Chain r) { return r.text; }
 //public static explicit operator Chain(String s) { return new Chain(s); }

  public static Chain LoadUri(String uri) throws Exception
  {
   InputStream in = null;
   int contentLength = -1;
   try
   {
    HttpURLConnection connection = (HttpURLConnection) new URI(uri).toURL().openConnection();
    connection.connect();
    if (connection.getResponseCode() / 100 != 2) { }
    contentLength = connection.getContentLength();
    if (contentLength < 1) {  }
    in = connection.getInputStream();
   }
   catch (Exception e)
   {
    if ((e.getMessage().startsWith("Illegal character")) || (e.getMessage().startsWith("unknown protocol")))
    {
     if (in != null) try { in.close(); } catch (Exception ee) {};
     uri = uri.replace("\\", "/");
     URI location = new URI(uri);
     in = new DataInputStream(new BufferedInputStream(new FileInputStream(uri)));
    }
    else throw new Exception(e.getMessage());
   }
   StringBuilder sb = new StringBuilder();
   char[] buffer = new char[40000];
   Reader inReader = new InputStreamReader(in, "UTF-8");
   int read = inReader.read(buffer, 0, buffer.length);
   while (read > -1)
   {
    sb.append(buffer, 0, read);
    read = inReader.read(buffer, 0, buffer.length);
   }
   if (in != null) try { in.close(); } catch (Exception ee) {};
   return new Chain(new String(sb.toString()));
  }

  /*
  public    static Chain Load       (Element el,  KeyPile<String, Pile<Integer>> ftWinx) throws Exception { return Chn.Load(el.text(), false,  ftWinx );}
  public    static Chain Load       (String text, KeyPile<String, Pile<Integer>> ftWinx) throws Exception { return Chn.Load(text     , false,  ftWinx );}
  */

  public    static Chain Load       (Element el,  Chain  ftSig, FtxProvider ftPvr, long... inBehalfOfs) throws Exception { return Chn.Load(el.text(), false,            ftSig, ftPvr, inBehalfOfs);}
  public    static Chain Load       (String text, Chain  ftSig, FtxProvider ftPvr, long... inBehalfOfs) throws Exception { return Chn.Load(text     , false,            ftSig, ftPvr, inBehalfOfs);}
  public    static Chain Load       (Element el,  String ftSig, FtxProvider ftPvr, long... inBehalfOfs) throws Exception { return Chn.Load(el.text(), false, new Chain(ftSig), ftPvr, inBehalfOfs);}
  public    static Chain Load       (String text, String ftSig, FtxProvider ftPvr, long... inBehalfOfs) throws Exception { return Chn.Load(text     , false, new Chain(ftSig), ftPvr, inBehalfOfs);}

  public Pile<Chain> split(String dlm, String cmt, boolean trim)        throws Exception
  {
   Pile<Chain> ret = new Pile<Chain>();
   Chain data = this;
   Chain def = data.before(1, dlm);
   while (data.len() > 0) 
   { 
    if (def.len() > 0) 
        ret.Push(cmt.length() > 0 
                ? 
                trim 
                        ? 
                        def.before(cmt).Trim() 
                        : 
                        def.before(cmt) 
                : 
                trim 
                        ? 
                        def.Trim() 
                        : 
                        def); 
    
    data = data.after(def.len() + dlm.length()); 
    def = data.before(1, dlm); 
   }
   return ret;
  }

  public Pile<Chain> split (String dlm,                String cmt)      throws Exception      { return split( dlm, cmt, false); }
  public Pile<Chain> split (String dlm,              boolean trim)      throws Exception      { return split( dlm,  "",  trim); }
  public Pile<Chain> split (String dlm                           )      throws Exception      { return split( dlm,  "", false); }

// ******************* (int occur, String ... sequences)
  
  public Chain at          (String skipChars, int occur, String ... seqPatterns)      throws Exception      { upd(); return  (Chain) super.at(skipChars, occur, seqPatterns); }

  public Chain upon        (Chain other                                        )      throws Exception      { upd(); return  (Chain) super.upon(other); }
  public Chain on          (Chain other                                        )      throws Exception      { upd(); return  (Chain) super.on  (other); }

  
// ******************* (Chn other)
  public Chain before      (Chain other)                                throws Exception      { upd(); other.upd(); return (Chain) super.before      (other); }
  public Chain upto        (Chain other)                                throws Exception      { upd(); other.upd(); return (Chain) super.upto        (other); }
  public Chain at          (Chain other)                                throws Exception      { upd(); other.upd(); return (Chain) super.at          (other); }
  public Chain from        (Chain other)                                throws Exception      { upd(); other.upd(); return (Chain) super.from        (other); }
  public Chain after       (Chain other)                                throws Exception      { upd(); other.upd(); return (Chain) super.after       (other); }
// ******************* (int pos) //supported: backward index (eInx < 0) where -1 == last Element
  public Chain before      (int pos)                                    throws Exception      { upd(); return (Chain) super.before      (pos); }
  public Chain upto        (int pos)                                    throws Exception      { upd(); return (Chain) super.upto        (pos); }
  public Chain at          (int pos)                                    throws Exception      { upd(); return (Chain) super.at          (pos); }
  public Chain from        (int pos)                                    throws Exception      { upd(); return (Chain) super.from        (pos); }
  public Chain after       (int pos)                                    throws Exception      { upd(); return (Chain) super.after       (pos); }
// ******************* (bool match, int occur, String chrs)
  public Chain before      (boolean match, int occur, String chrs)      throws Exception      { upd(); return (Chain) super.before      (match, occur, chrs); }
  public Chain upto        (boolean match, int occur, String chrs)      throws Exception      { upd(); return (Chain) super.upto        (match, occur, chrs); }
  public Chain at          (boolean match, int occur, String chrs)      throws Exception      { upd(); return (Chain) super.at          (match, occur, chrs); }
  public Chain from        (boolean match, int occur, String chrs)      throws Exception      { upd(); return (Chain) super.from        (match, occur, chrs); }
  public Chain after       (boolean match, int occur, String chrs)      throws Exception      { upd(); return (Chain) super.after       (match, occur, chrs); }
/*                                                                                                                                                                                                                                                                                                                                                                                                                            //
  public Chain before      (           boolean match, String chrs)      throws Exception      { upd(); return (Chain) super.before      (match, 1, chrs); }
  public Chain upto        (           boolean match, String chrs)      throws Exception      { upd(); return (Chain) super.upto        (match, 1, chrs); }
  public Chain at          (           boolean match, String chrs)      throws Exception      { upd(); return (Chain) super.at          (match, 1, chrs); }
  public Chain from        (           boolean match, String chrs)      throws Exception      { upd(); return (Chain) super.from        (match, 1, chrs); }
  public Chain after       (           boolean match, String chrs)      throws Exception      { upd(); return (Chain) super.after       (match, 1, chrs); }
*/
// ******************* (bool match, int occur, String chrs)
  public Chain Before      (boolean match, int occur, String chrs)      throws Exception      { upd(); return (Chain) super.Before      (match, occur, chrs); }
  public Chain Upto        (boolean match, int occur, String chrs)      throws Exception      { upd(); return (Chain) super.Upto        (match, occur, chrs); }
  public Chain At          (boolean match, int occur, String chrs)      throws Exception      { upd(); return (Chain) super.At          (match, occur, chrs); }
  public Chain From        (boolean match, int occur, String chrs)      throws Exception      { upd(); return (Chain) super.From        (match, occur, chrs); }
  public Chain After       (boolean match, int occur, String chrs)      throws Exception      { upd(); return (Chain) super.After       (match, occur, chrs); }
/*                                                                                                                                                                                                                                                                                                                                                                                                                            //
  public Chain Before      (           boolean match, String chrs)      throws Exception      { upd(); return (Chain) super.Before      (match, 1, chrs); }
  public Chain Upto        (           boolean match, String chrs)      throws Exception      { upd(); return (Chain) super.Upto        (match, 1, chrs); }
  public Chain At          (           boolean match, String chrs)      throws Exception      { upd(); return (Chain) super.At          (match, 1, chrs); }
  public Chain From        (           boolean match, String chrs)      throws Exception      { upd(); return (Chain) super.From        (match, 1, chrs); }
  public Chain After       (           boolean match, String chrs)      throws Exception      { upd(); return (Chain) super.After       (match, 1, chrs); }
*/
// ******************* (int occur, String token)
  public Chain before      (int occur,                String token)      throws Exception     { upd(); return (Chain) super.before      (occur, token); }
  public Chain upto        (int occur,                String token)      throws Exception     { upd(); return (Chain) super.upto        (occur, token); }
  public Chain at          (int occur,                String token)      throws Exception     { upd(); return (Chain) super.at          (occur, token); }
  public Chain from        (int occur,                String token)      throws Exception     { upd(); return (Chain) super.from        (occur, token); }
  public Chain after       (int occur,                String token)      throws Exception     { upd(); return (Chain) super.after       (occur, token); }
//                                                                                                                                                                                                                                                                                                                                                                                                                            //
  public Chain before      (                          String token)      throws Exception     { upd(); return (Chain) super.before      (1, token); }
  public Chain upto        (                          String token)      throws Exception     { upd(); return (Chain) super.upto        (1, token); }
  public Chain at          (                          String token)      throws Exception     { upd(); return (Chain) super.at          (1, token); }
  public Chain from        (                          String token)      throws Exception     { upd(); return (Chain) super.from        (1, token); }
  public Chain after       (                          String token)      throws Exception     { upd(); return (Chain) super.after       (1, token); }
// ******************* (int occur, String token)
  public Chain Before      (int occur,                String token)      throws Exception     { upd(); return (Chain) super.Before      (occur, token); }
  public Chain Upto        (int occur,                String token)      throws Exception     { upd(); return (Chain) super.Upto        (occur, token); }
  public Chain At          (int occur,                String token)      throws Exception     { upd(); return (Chain) super.At          (occur, token); }
  public Chain From        (int occur,                String token)      throws Exception     { upd(); return (Chain) super.From        (occur, token); }
  public Chain After       (int occur,                String token)      throws Exception     { upd(); return (Chain) super.After       (occur, token); }
//                                                                                                                                                                                                                                                                                                                                                                                                                            //
  public Chain Before      (                          String token)      throws Exception     { upd(); return (Chain) super.Before      (1, token); }
  public Chain Upto        (                          String token)      throws Exception     { upd(); return (Chain) super.Upto        (1, token); }
  public Chain At          (                          String token)      throws Exception     { upd(); return (Chain) super.At          (1, token); }
  public Chain From        (                          String token)      throws Exception     { upd(); return (Chain) super.From        (1, token); }
  public Chain After       (                          String token)      throws Exception     { upd(); return (Chain) super.After       (1, token); }
// ******************* (int occur, bool prio, String... tokens)
  public Chain before      (int occur,           boolean prio, String... tokens)      throws Exception      { upd(); return (Chain) super.before      (occur, prio, tokens); }
  public Chain upto        (int occur,           boolean prio, String... tokens)      throws Exception      { upd(); return (Chain) super.upto        (occur, prio, tokens); }
  public Chain at          (int occur,           boolean prio, String... tokens)      throws Exception      { upd(); return (Chain) super.at          (occur, prio, tokens); }
  public Chain from        (int occur,           boolean prio, String... tokens)      throws Exception      { upd(); return (Chain) super.from        (occur, prio, tokens); }
  public Chain after       (int occur,           boolean prio, String... tokens)      throws Exception      { upd(); return (Chain) super.after       (occur, prio, tokens); }
//                                                                                                                                                                                                                                                                                                                                                                                                                            //
  public Chain before      (                     boolean prio, String... tokens)      throws Exception      { upd(); return (Chain) super.before      (1, prio, tokens); }
  public Chain upto        (                     boolean prio, String... tokens)      throws Exception      { upd(); return (Chain) super.upto        (1, prio, tokens); }
  public Chain at          (                     boolean prio, String... tokens)      throws Exception      { upd(); return (Chain) super.at          (1, prio, tokens); }
  public Chain from        (                     boolean prio, String... tokens)      throws Exception      { upd(); return (Chain) super.from        (1, prio, tokens); }
  public Chain after       (                     boolean prio, String... tokens)      throws Exception      { upd(); return (Chain) super.after       (1, prio, tokens); }
// ******************* (int occur, bool prio, String... tokens)
  public Chain Before      (int occur,           boolean prio, String... tokens)      throws Exception      { upd(); return (Chain) super.Before      (occur, prio, tokens); }
  public Chain Upto        (int occur,           boolean prio, String... tokens)      throws Exception      { upd(); return (Chain) super.Upto        (occur, prio, tokens); }
  public Chain At          (int occur,           boolean prio, String... tokens)      throws Exception      { upd(); return (Chain) super.At          (occur, prio, tokens); }
  public Chain From        (int occur,           boolean prio, String... tokens)      throws Exception      { upd(); return (Chain) super.From        (occur, prio, tokens); }
  public Chain After       (int occur,           boolean prio, String... tokens)      throws Exception      { upd(); return (Chain) super.After       (occur, prio, tokens); }
//                                                                                                                                                                                                                                                                                                                                                                                                                            //
  public Chain Before      (                     boolean prio, String... tokens)      throws Exception      { upd(); return (Chain) super.Before      (1, prio, tokens); }
  public Chain Upto        (                     boolean prio, String... tokens)      throws Exception      { upd(); return (Chain) super.Upto        (1, prio, tokens); }
  public Chain At          (                     boolean prio, String... tokens)      throws Exception      { upd(); return (Chain) super.At          (1, prio, tokens); }
  public Chain From        (                     boolean prio, String... tokens)      throws Exception      { upd(); return (Chain) super.From        (1, prio, tokens); }
  public Chain After       (                     boolean prio, String... tokens)      throws Exception      { upd(); return (Chain) super.After       (1, prio, tokens); }
//                                                                                                                                                                                                                                                                                                                                                                                                                            //
  
  public Chain firstWord   (String wordCharacters    ) throws Exception { upd(); return (Chain) super.firstWord (wordCharacters); }
  public Chain FirstWord   (String wordCharacters    ) throws Exception { upd(); return (Chain) super.FirstWord (wordCharacters); }
  public Chain lastWord    (String wordCharacters    ) throws Exception { upd(); return (Chain) super.lastWord  (wordCharacters); }
  public Chain LastWord    (String wordCharacters    ) throws Exception { upd(); return (Chain) super.LastWord  (wordCharacters); }

  public int   count       (String ptn               ) throws Exception { upd(); return         super.count    (ptn       ); }
  public Chain collapse    (String ptn               ) throws Exception { upd(); return (Chain) super.collapse (ptn       ); }
  public Chain skip        (String ptn               ) throws Exception { upd(); return (Chain) super.skip     (ptn       ); }
  public Chain pad         (int len,      String fill) throws Exception { upd(); return (Chain) super.pad      (len, fill ); }

  public Chain lTrim       (                         ) throws Exception { upd(); return (Chain) super.after(firstWord(" ")); }
  public Chain rTrim       (                         ) throws Exception { upd(); return (Chain) super.before(lastWord(" ")); }
  public Chain Trim        (                         ) throws Exception { upd(); return (Chain) super.after(firstWord(" ")).before(lastWord(" ")); }
  public Chain trimC        ( String trimChars       ) throws Exception { upd(); return (Chain) super.after(firstWord(trimChars)).before(lastWord(trimChars)); }

  public Chain trimW        ( String word            ) throws Exception 
  { 
   upd(); 
   Chain ret = upto(-1);
   while (ret.startsWith(word)) ret = ret.after  ( word.length());
   while (ret.endsWith  (word)) ret = ret.before (-word.length());
   return ret;
  }
  
  public Chain TrimW        ( String word            ) throws Exception 
  { 
   upd(); 
   Chain ret = upto(-1);
   while (ret.StartsWith(word)) ret = ret.after  ( word.length());
   while (ret.EndsWith  (word)) ret = ret.before (-word.length());
   return ret;
  }

  public String  dbgText()                             throws Exception { return super.dbgText(); }

  public Chain mostSimilar(Pile<String> vals) throws Exception                                                  
  {
   int res = 10000;
   Chain ret = Chain.Empty;
   String txt = text();
   for (String val : vals)
    if (res > Math.abs(org.apache.commons.lang3.StringUtils.getLevenshteinDistance(txt, val)))
    {
     res = Math.abs(org.apache.commons.lang3.StringUtils.getLevenshteinDistance(txt, val));
     ret = new Chain(val);
    }
   return ret;
  }
  
 @Override
  public String  toString()                                             { try {upd(); return super.text();} catch(Exception ex) {return "<ERR>";} }
  public String  text()                                throws Exception { upd(); return super.text(); }
  public String  uText()                               throws Exception { upd(); return super.uText(); }
  public String  lText()                               throws Exception { upd(); return super.lText(); }
  public int     len()                                                  { return super.len(); }
  public char    charAt(int pos, boolean csns)         throws Exception { upd(); return super.charAt(pos, csns);  }

  public boolean startsWith(String token)              throws Exception { upd(); return super.startsWith(token); }
  public boolean StartsWith(String token)              throws Exception { upd(); return super.StartsWith(token); }
  public boolean endsWith(String token)                throws Exception { upd(); return super.endsWith(token); }
  public boolean EndsWith(String token)                throws Exception { upd(); return super.EndsWith(token); }

  public int     incident(String ... test)             throws Exception { upd(); return super.incident(test); }

  public boolean startsWith  (boolean match, String chars ) throws Exception { upd(); return super.startsWith(match, chars);                }
  public boolean StartsWith  (boolean match, String chars ) throws Exception { upd(); return super.StartsWith(match, chars);                }
  public boolean endsWith    (boolean match, String chars ) throws Exception { upd(); return super.endsWith(match, chars);                  }
  public boolean EndsWith    (boolean match, String chars ) throws Exception { upd(); return super.EndsWith(match, chars);                  }

  public boolean equals(Chain other)                   throws Exception { upd(); other.upd(); return super.equals(other); }
  public boolean Equals(Chain other)                   throws Exception { upd(); other.upd(); return super.Equals(other); }

  public boolean equals(String other)                  throws Exception { upd(); return super.equals(other); }
  public boolean Equals(String other)                  throws Exception { upd(); return super.Equals(other); }

  /*
  public Pile<Chain> split(String delim) throws Exception 
  { 
   Chain res = this; 
   Pile<Chain> ret = new Pile<Chain>(0); 
   while (res.len() > 0) 
   {
    Chain part = res.before(1, delim); 
    res = res.after(1, delim); 
    ret.Add(part); 
   } 
   return ret; 
  }
 */
  

  public Chain plus(Chain  second)                     throws Exception { upd(); second.upd(); return (Chain) ((Chn) this).plus((Chn) second); }
  //public static Chain plus(String first, Chain second) throws Exception { second.upd(); return (Chain) Chain.plus(first, (Chn)second); }
  public Chain plus(String second)                     throws Exception 
  { 
   return plus(new Chain(second)); 
  }
  public Chain less(Chain  second)                     throws Exception { upd(); second.upd(); return (Chain) ((Chn) this).less((Chn)second); }
  public Chain less(String second)                     throws Exception { return less(new Chain(second)); }
 }

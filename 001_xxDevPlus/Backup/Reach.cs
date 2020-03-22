using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Net;
using System.IO;

using ndBase;

namespace ndString
{
 public partial class Reach: Rch
 {
  //Copyright© 1996-2012 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties, You may use, modify, distribute this code under GNU public licence for NON-Commercial use as long as this notice remains unchanged.
  private static bool selfTested  = false; private static string ass(bool expr) { if (!expr) throw new Exception(ass(true) + " SelfTest Failure"); return "Reach"; }


  private static void selfTest()
  {
   selfTested = true;
   Reach r1 = new Reach("ab");
   Reach r2 = new Reach("..X--XX___XX,,XX");
   Reach r3 = new Reach("aa bb  cc ");

   ass(r3.firstWord(" ").equals(""));
   ass(r3.firstWord(" a").equals("aa "));
   ass(r3.lastWord(" ").equals(" "));

   ass(r2.upto(1, false, "X,", "X_", "X-").equals("..X-"));
   ass(r2.upto(2, false, "X,", "X_", "X-").equals("..X--XX_"));
   ass(r2.upto(3, false, "X,", "X_", "X-").equals("..X--XX___XX,"));
   ass(r2.upto(-1, false, "X,", "X_", "X-").equals("..X--XX___XX,"));
   ass(r2.upto(-2, false, "X,", "X_", "X-").equals("..X--XX_"));
   ass(r2.upto(-3, false, "X,", "X_", "X-").equals("..X-"));

   ass(r2.upto(1, false, ",XX", "_XX", "X-", "..").equals(".."));
   ass(r2.upto(2, false, ",XX", "_XX", "X-", "..").equals("..X-"));
   ass(r2.upto(3, false, ",XX", "_XX", "X-", "..").equals("..X--XX___XX"));
   ass(r2.upto(-1, false, ",XX", "_XX", "X-", "..").equals("..X--XX___XX,,XX"));
   ass(r2.upto(-2, false, ",XX", "_XX", "X-", "..").equals("..X--XX___XX"));
   ass(r2.upto(-3, false, ",XX", "_XX", "X-", "..").equals("..X-"));

   ass(r2.from(1, false, "X,", "X_", "X-").equals("X--XX___XX,,XX"));
   ass(r2.from(2, false, "X,", "X_", "X-").equals("X___XX,,XX"));
   ass(r2.from(3, false, "X,", "X_", "X-").equals("X,,XX"));
   ass(r2.from(-1, false, "X,", "X_", "X-").equals("X,,XX"));
   ass(r2.from(-2, false, "X,", "X_", "X-").equals("X___XX,,XX"));
   ass(r2.from(-3, false, "X,", "X_", "X-").equals("X--XX___XX,,XX"));

   ass(r2.from(1, false, ",XX", "_XX", "X-", "..").equals("..X--XX___XX,,XX"));
   ass(r2.from(2, false, ",XX", "_XX", "X-", "..").equals("X--XX___XX,,XX"));
   ass(r2.from(3, false, ",XX", "_XX", "X-", "..").equals("_XX,,XX"));
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

  private void init() { if (!selfTested) selfTest(); }
 
  public static bool            useReachNotify = false;
  static CookieCollection       Cookies   = new CookieCollection();
  
  public Operator take() {return new Operator(this);}
 
  public static BufOp   operator!(Reach r) { return new BufOp(r);   }  //this offers an alternative syntax to "._bop()"
  public static Extract operator~(Reach r) { return new Extract(r); }  //this offers an alternative syntax to "._xtr()"

  public   BufOp        _buf()           { return new BufOp(this);                   }
  public   Extract      XTR()            { return new Extract(this);                 }
  public   Delete       DEL()            { return new Delete(false, this);           }
  public   Delete       DEL(int cnt)     { return new Delete(false, this, cnt);      }
  public   ReplaceWith  RPW(string txt)  { return new ReplaceWith(false, this, txt); }
  public   ReplaceWith  RPW(Reach txt)   { return new ReplaceWith(false, this, txt); }
  public   Insert       INS(string txt)  { return new Insert(false, this, txt);      }
  public   Insert       INS(Reach txt)   { return new Insert(false, this, txt);      }

/*

  public   Delete       _del()           { return new Delete(true, Base);            }
  public   Delete       _del(long cnt)    { return new Delete(true, Base, cnt);       }
  public   ReplaceWith  _rpw(string txt) { return new ReplaceWith(true, Base, txt);  }
  public   ReplaceWith  _rpw(Reach txt)  { return new ReplaceWith(true, Base, txt);  }
  public   Insert       _ins(string txt) { return new Insert(true, Base, txt);       }
  public   Insert       _ins(Reach txt)  { return new Insert(true, Base, txt);       }

 */

  public   Reach   (string                                   text) : base(text)             { init();}
  public   Reach   (Reach first,                     Reach second) : base(first, second)    { init();}
  internal Reach   (Reach source,                     Restrict rt) : base(source, rt)       { init();}
  internal Reach   (MappedBuffer mBuf, string sLbl,   string eLbl) : base(mBuf, sLbl, eLbl) { init();}

  //public static explicit operator string(Reach r) { return r.text; }
  //public static explicit operator Reach(string s) { return new Reach(s); }
  public static implicit operator string(Reach r) { return r.text; }
  //public static implicit operator bool(Reach r) { return (r == null) ? false : (r.len > 0); }
  public static implicit operator Reach(string s) { return new Reach(s); }

  private Uri location;

  public void Save(string uri) { location = new Uri(uri); if (location.IsFile) utl.s2f(this, location.ToString(), false); else throw new Exception("unable to upload " + location.ToString()); }
  public void Save() { Save(location.ToString()); }
  public Reach Load() { return Reach.Load(location.ToString()); }
  
  
  public static Reach Load(string uri)
  {
   Reach Uri = new Reach(uri);
   uri = Uri.before(1, "///?");
   byte[] post = new ASCIIEncoding().GetBytes(Uri.after(1, "///?")); 
   Uri location = new Uri(uri);
   if (location.IsFile)
   {
    Reach ret = new Reach(utl.f2s(location.ToString())); 
    ret.location = location;
    return ret; 
   }
   else
   {
    HttpWebRequest req;
    HttpWebResponse resp;
    Encoding enc;
    req = (HttpWebRequest) WebRequest.Create(location.ToString());
    req.CookieContainer = new CookieContainer();
    req.CookieContainer.Add(Cookies);
    req.Timeout = 30000;  // 30 secs
    req.UserAgent = "Internet Explorer";
    if (post.Length > 0)
    {
     req.Method = "POST";
     req.ContentType="application/x-www-form-urlencoded";
     req.ContentLength = post.Length;
     req.GetRequestStream().Write(post, 0, post.Length);
    }
    try
    {
     resp = (HttpWebResponse) req.GetResponse();
     Cookies.Add(resp.Cookies);
     
     Reach ret = null;
     try
     {
      Stream responseStream = resp.GetResponseStream();
      Encoding e = Encoding.GetEncoding(resp.CharacterSet);
      StreamReader reader = new StreamReader(responseStream, e);  
      string s = reader.ReadToEnd();
      ret = new Reach(s);
     }
     catch
     {
      //enc = Encoding.GetEncoding(1252);  // Windows-1252 or iso
      //enc = Encoding.GetEncoding("unicodeFFFE");  // 1201 = "UTF-16BE" or "unicodeFFFE"
      enc = Encoding.GetEncoding("unicode");  // 1200 = "UTF-16LE", "utf-16", "ucs-2", "unicode", or "ISO-10646-UCS-2"
      try { if (resp.ContentEncoding.Length > 0) { enc = Encoding.GetEncoding(resp.ContentEncoding); } } catch (Exception e) { e.ToString(); }
      ret = new Reach((new StreamReader(resp.GetResponseStream())).ReadToEnd());
     }
     ret.location = location;
     return ret;
    }
    catch (Exception e)
    {
     utl.msg(" ******* CANNOT LOAD DOCUMENT FROM WEB ********" + e.ToString());
     return null;
     //SleepEx(20000, 1); //debug
    }
   }

  }

// ******************* (Rch other)
  public Reach before      (Reach other)                                      { upd(); other.upd(); return (Reach) base.before      (other); }
  public Reach upto        (Reach other)                                      { upd(); other.upd(); return (Reach) base.upto        (other); }
  public Reach at          (Reach other)                                      { upd(); other.upd(); return (Reach) base.at          (other); }
  public Reach from        (Reach other)                                      { upd(); other.upd(); return (Reach) base.from        (other); }
  public Reach after       (Reach other)                                      { upd(); other.upd(); return (Reach) base.after       (other); }
// ******************* (int pos) //supported: backward index (eInx < 0) where -1 == last Element
  public Reach before      (int  pos)                                         { upd(); return (Reach) base.before      (pos); }
  public Reach upto        (int  pos)                                         { upd(); return (Reach) base.upto        (pos); }
  public Reach at          (int  pos)                                         { upd(); return (Reach) base.at          (pos); }
  public Reach from        (int  pos)                                         { upd(); return (Reach) base.from        (pos); }
  public Reach after       (int  pos)                                         { upd(); return (Reach) base.after       (pos); }
// ******************* (bool match, int occur, String chrs)
  public Reach before      (bool match, long occur, string chrs)              { upd(); return (Reach) base.before      (match, occur, chrs); }
  public Reach upto        (bool match, long occur, string chrs)              { upd(); return (Reach) base.upto        (match, occur, chrs); }
  public Reach at          (bool match, long occur, string chrs)              { upd(); return (Reach) base.at          (match, occur, chrs); }
  public Reach from        (bool match, long occur, string chrs)              { upd(); return (Reach) base.from        (match, occur, chrs); }
  public Reach after       (bool match, long occur, string chrs)              { upd(); return (Reach) base.after       (match, occur, chrs); }
/*  
  public Reach before      (            bool match, string chrs)              { upd(); return (Reach) base.before      (match, 1, chrs); }
  public Reach upto        (            bool match, string chrs)              { upd(); return (Reach) base.upto        (match, 1, chrs); }
  public Reach at          (            bool match, string chrs)              { upd(); return (Reach) base.at          (match, 1, chrs); }
  public Reach from        (            bool match, string chrs)              { upd(); return (Reach) base.from        (match, 1, chrs); }
  public Reach after       (            bool match, string chrs)              { upd(); return (Reach) base.after       (match, 1, chrs); }
*/
// ******************* (bool match, int occur, String chrs)
  public Reach Before      (bool match, long occur, string chrs)              { upd(); return (Reach) base.Before      (match, occur, chrs); }
  public Reach Upto        (bool match, long occur, string chrs)              { upd(); return (Reach) base.Upto        (match, occur, chrs); }
  public Reach At          (bool match, long occur, string chrs)              { upd(); return (Reach) base.At          (match, occur, chrs); }
  public Reach From        (bool match, long occur, string chrs)              { upd(); return (Reach) base.From        (match, occur, chrs); }
  public Reach After       (bool match, long occur, string chrs)              { upd(); return (Reach) base.After       (match, occur, chrs); }
/*
  public Reach Before      (            bool match, string chrs)              { upd(); return (Reach) base.Before      (match, 1, chrs); }
  public Reach Upto        (            bool match, string chrs)              { upd(); return (Reach) base.Upto        (match, 1, chrs); }
  public Reach At          (            bool match, string chrs)              { upd(); return (Reach) base.At          (match, 1, chrs); }
  public Reach From        (            bool match, string chrs)              { upd(); return (Reach) base.From        (match, 1, chrs); }
  public Reach After       (            bool match, string chrs)              { upd(); return (Reach) base.After       (match, 1, chrs); }
*/
// ******************* (int occur, String token)
  public Reach before      (long occur,             string token)             { upd(); return (Reach) base.before      (occur, token); }
  public Reach upto        (long occur,             string token)             { upd(); return (Reach) base.upto        (occur, token); }
  public Reach at          (long occur,             string token)             { upd(); return (Reach) base.at          (occur, token); }
  public Reach from        (long occur,             string token)             { upd(); return (Reach) base.from        (occur, token); }
  public Reach after       (long occur,             string token)             { upd(); return (Reach) base.after       (occur, token); }
//
  public Reach before      (                        string token)             { upd(); return (Reach) base.before      (1, token); }
  public Reach upto        (                        string token)             { upd(); return (Reach) base.upto        (1, token); }
  public Reach at          (                        string token)             { upd(); return (Reach) base.at          (1, token); }
  public Reach from        (                        string token)             { upd(); return (Reach) base.from        (1, token); }
  public Reach after       (                        string token)             { upd(); return (Reach) base.after       (1, token); }
// ******************* (int occur, String token) 
  public Reach Before      (long occur,             string token)             { upd(); return (Reach) base.Before      (occur, token); }
  public Reach Upto        (long occur,             string token)             { upd(); return (Reach) base.Upto        (occur, token); }
  public Reach At          (long occur,             string token)             { upd(); return (Reach) base.At          (occur, token); }
  public Reach From        (long occur,             string token)             { upd(); return (Reach) base.From        (occur, token); }
  public Reach After       (long occur,             string token)             { upd(); return (Reach) base.After       (occur, token); }
//
  public Reach Before      (                        string token)             { upd(); return (Reach) base.Before      (1, token); }
  public Reach Upto        (                        string token)             { upd(); return (Reach) base.Upto        (1, token); }
  public Reach At          (                        string token)             { upd(); return (Reach) base.At          (1, token); }
  public Reach From        (                        string token)             { upd(); return (Reach) base.From        (1, token); }
  public Reach After       (                        string token)             { upd(); return (Reach) base.After       (1, token); }
// ******************* (int occur, bool prio, String... tokens)
  public Reach before      (long occur,  bool prio, params string[] tokens)   { upd(); return (Reach) base.before      (occur, prio, tokens); }
  public Reach upto        (long occur,  bool prio, params string[] tokens)   { upd(); return (Reach) base.upto        (occur, prio, tokens); }
  public Reach at          (long occur,  bool prio, params string[] tokens)   { upd(); return (Reach) base.at          (occur, prio, tokens); }
  public Reach from        (long occur,  bool prio, params string[] tokens)   { upd(); return (Reach) base.from        (occur, prio, tokens); }
  public Reach after       (long occur,  bool prio, params string[] tokens)   { upd(); return (Reach) base.after       (occur, prio, tokens); }
//
  public Reach before      (             bool prio, params string[] tokens)   { upd(); return (Reach) base.before      (1, prio, tokens); }
  public Reach upto        (             bool prio, params string[] tokens)   { upd(); return (Reach) base.upto        (1, prio, tokens); }
  public Reach at          (             bool prio, params string[] tokens)   { upd(); return (Reach) base.at          (1, prio, tokens); }
  public Reach from        (             bool prio, params string[] tokens)   { upd(); return (Reach) base.from        (1, prio, tokens); }
  public Reach after       (             bool prio, params string[] tokens)   { upd(); return (Reach) base.after       (1, prio, tokens); }
// ******************* (int occur, bool prio, String... tokens) 
  public Reach Before      (long occur,  bool prio, params string[] tokens)   { upd(); return (Reach) base.Before      (occur, prio, tokens); }
  public Reach Upto        (long occur,  bool prio, params string[] tokens)   { upd(); return (Reach) base.Upto        (occur, prio, tokens); }
  public Reach At          (long occur,  bool prio, params string[] tokens)   { upd(); return (Reach) base.At          (occur, prio, tokens); }
  public Reach From        (long occur,  bool prio, params string[] tokens)   { upd(); return (Reach) base.From        (occur, prio, tokens); }
  public Reach After       (long occur,  bool prio, params string[] tokens)   { upd(); return (Reach) base.After       (occur, prio, tokens); }
//
  public Reach Before      (             bool prio, params string[] tokens)   { upd(); return (Reach) base.Before      (1, prio, tokens); }
  public Reach Upto        (             bool prio, params string[] tokens)   { upd(); return (Reach) base.Upto        (1, prio, tokens); }
  public Reach At          (             bool prio, params string[] tokens)   { upd(); return (Reach) base.At          (1, prio, tokens); }
  public Reach From        (             bool prio, params string[] tokens)   { upd(); return (Reach) base.From        (1, prio, tokens); }
  public Reach After       (             bool prio, params string[] tokens)   { upd(); return (Reach) base.After       (1, prio, tokens); }
//



  public Reach firstWord  (string wordCharacters) { upd(); return (Reach) base.firstWord(wordCharacters); }
  public Reach FirstWord  (string wordCharacters) { upd(); return (Reach) base.FirstWord(wordCharacters); }
  public Reach lastWord   (string wordCharacters) { upd(); return (Reach) base.lastWord(wordCharacters); }
  public Reach LastWord   (string wordCharacters) { upd(); return (Reach) base.LastWord(wordCharacters); }

  public Reach lTrim() { upd(); return (Reach) base.after(firstWord(" ")); }
  public Reach rTrim() { upd(); return (Reach) base.before(lastWord(" ")); }
  public Reach Trim() { upd(); return (Reach)base.after(firstWord(" ")).before(lastWord(" ")); }

  public string  dbgTxt                                  { get { return base.dbgTxt;             } }
  public string  text                                    { get { upd(); return base.text; } }
  public string  uText                                   { get { upd(); return base.uText; } }
  public string  lText                                   { get { upd(); return base.lText; } }
  public int     len                                     { get { return base.len; } }
  public char    this        [int  pos, bool upper     ] { get { upd(); return base[pos, upper];  } }

  public bool    startsWith  (string token             ) { upd(); return base.startsWith(token);                }
  public bool    StartsWith  (string token             ) { upd(); return base.StartsWith(token);                }
  public bool    endsWith    (string token             ) { upd(); return base.endsWith(token);                  }
  public bool    EndsWith    (string token             ) { upd(); return base.EndsWith(token);                  }

  public bool    startsWith  (bool match, string chars ) { upd(); return base.startsWith(match, chars);                }
  public bool    StartsWith  (bool match, string chars ) { upd(); return base.StartsWith(match, chars);                }
  public bool    endsWith    (bool match, string chars ) { upd(); return base.endsWith(match, chars);                  }
  public bool    EndsWith    (bool match, string chars ) { upd(); return base.EndsWith(match, chars);                  }

  public bool    equals      (Reach other  ) { upd(); other.upd(); return base.equals((Rch)other);  }
  public bool    Equals      (Reach other  ) { upd(); other.upd(); return base.Equals((Rch) other); }

  public Pile<Reach> split(string delim) { Reach res = this; Pile<Reach> ret = new Pile<Reach>(); while (res.len > 0) { Reach part = res.before(1, utl.dmyBool("al(occur, token) is planned"), delim); res = res.after(1, utl.dmyBool("al(occur, token) is planned"), delim); ret.Add(part); } return ret; }

  public static Reach operator+  (Reach first,  Reach second  ) { first.upd();  second.upd();  return (Reach) ((Rch) first + (Rch) second); }
  public static Reach operator+  (string first, Reach second  ) { second.upd(); return (Reach) (first + (Rch) second);                      }
  public static Reach operator+  (Reach first,  string second ) { first.upd();  return (Reach) ((Rch) first + second);                      }
  public static Reach operator-  (Reach first,  Reach second  ) { first.upd();  second.upd();  return (Reach) ((Rch) first - (Rch) second); }

 }
}

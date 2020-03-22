

//** @author  GeTr Copyright© 1996-2020 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties
//** @license You may use, modify, distribute this code under GNU public dual licence free for NON-Commercial use as long as this notice remains unchanged.
//** @comment 1 based Enhanced HashMap also supporting negative backward counting 


using System;
using System.Threading;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Collections;

namespace org_xxdevplus_struct
{


 public class KeyPile<kTyp, iTyp> : IEnumerable<iTyp> 
 {
  //Copyright© 1996-2012 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties, You may use, modify, distribute this code under GNU public licence for NON-Commercial use as long as this notice remains unchanged.
  private static bool selfTested  = false;  private static string ass(bool expr) { if (!expr) throw new Exception(ass(true) + " SelfTest Failure"); return "KeyPile<,>";  } private static void selfTest() { selfTested = true; } private void init() { if (!selfTested) selfTest(); }

  public  Object                _name = "";

  private Pile<iTyp>            val;
  private KeyInxMap<kTyp>       kim;
  private bool                  uniqueKeys = true;

  public void                   clear      (                               ) { val.Clear(); kim.clear(); }
  public int                    Len                                          { get { return val.Len; } }
  public bool                   hasKey     (kTyp                        key) { return kim.hasKey(key); }
  public string                 Name                                         { get { return (string)_name; } set { _name = value; } }
  public iTyp                   this       [int                         inx] { get { return val[inx]; } set { if (inx < 0) inx = (int)(val.Len + inx + 1); if (inx == 0) _name = value; else val[inx] = value; } }
  public iTyp                   this       [kTyp                        key] { get { int inx = kim[key]; if (inx < 0) inx = val.Len + inx + 1; return val[inx]; } set { int inx = kim[key]; if (inx < 0) inx = val.Len + inx + 1; if (inx == 0) _name = value; else val[inx] = value; } }

  public void                   Push       (KeyPile<kTyp,        iTyp> more) { foreach (kTyp key in more.Keys) Add(key, more[key]);              }
  public KeyPile<kTyp, iTyp>    Add        (KeyPile<kTyp,        iTyp> more) { foreach (kTyp key in more.Keys) Add(key, more[key]); return this; }
  public KeyPile<kTyp, iTyp>    Add        (kTyp key,             iTyp more) { kim.Add(key); val.Add(  more); return this; }
  public KeyPile<kTyp, iTyp>    Set        (kTyp key,             iTyp more) { if (kim.hasKey(key)) val[kim[key]] = more; else {kim.Add(key); val.Add(more);} return this; }
  public KeyPile<kTyp, iTyp>    Put        (kTyp key,             iTyp more) { if (kim.hasKey(key)) {Del(key); Add(key, more); } else {kim.Add(key); val.Add(more);} return this; }
  public KeyPile<kTyp, iTyp>    Del        (kTyp                        key) { try { val.Del(kim[key]); kim.Del(key); } catch (Exception exp) {}  return this; }
  
  public Pile<kTyp>            Keys                                          { get {return kim.Keys;} }
  public Pile<kTyp>            kAsc                                          { get {return kim.kAsc; } }
  public Pile<kTyp>            kDsc                                          { get {return kim.kDsc; } }

  public KeyPile<kTyp, iTyp> before(int inx)
  {
   KeyPile<kTyp, iTyp> ret = new KeyPile<kTyp, iTyp>();
   ret.val = val.before(inx);
   ret.kim = kim.before(inx);
   return ret;
  }

  public KeyPile<kTyp, iTyp> upto(int inx)
  {
   KeyPile<kTyp, iTyp> ret = new KeyPile<kTyp, iTyp>();
   ret.val = val.upto(inx);
   ret.kim = kim.upto(inx);
   return ret;
  }

  public KeyPile<kTyp, iTyp> at(int inx)
  {
   KeyPile<kTyp, iTyp> ret = new KeyPile<kTyp, iTyp>();
   ret.val = val.at(inx);
   ret.kim = kim.at(inx);
   return ret;
  }

  public KeyPile<kTyp, iTyp> from(int inx)
  {
   KeyPile<kTyp, iTyp> ret = new KeyPile<kTyp, iTyp>();
   ret.val = val.from(inx);
   ret.kim = kim.from(inx);
   return ret;
  }

  public KeyPile<kTyp, iTyp> after(int inx)
  {
   KeyPile<kTyp, iTyp> ret = new KeyPile<kTyp, iTyp>();
   ret.val = val.after(inx);
   ret.kim = kim.after(inx);
   return ret;
  }

  public KeyPile <kTyp, iTyp> before      (kTyp key) { return before(kim[key]);  }
  public KeyPile <kTyp, iTyp> upto        (kTyp key) { return upto(kim[key]);    }
  public KeyPile <kTyp, iTyp> at          (kTyp key) { return at(kim[key]);      }
  public KeyPile <kTyp, iTyp> from        (kTyp key) { return from(kim[key]);    }
  public KeyPile <kTyp, iTyp> after       (kTyp key) { return after(kim[key]);   }
  
  public IEnumerator<iTyp> GetEnumerator() { return val.GetEnumerator(); }
  IEnumerator IEnumerable.GetEnumerator() { return GetEnumerator(); }

  public KeyPile (                               ) { this.uniqueKeys = true; this.val = new Pile<iTyp>()  ; this.kim = new KeyInxMap<kTyp>(uniqueKeys); }
  public KeyPile (KeyPile<kTyp, iTyp>   cloneFrom) { _name = cloneFrom._name; val = cloneFrom.val.Clone() ; kim = new KeyInxMap<kTyp>(cloneFrom.kim); }

  public KeyPile<kTyp, iTyp> Clone() 
  {
   KeyPile<kTyp, iTyp> ret = new KeyPile<kTyp, iTyp>();
   ret._name = _name; 
   ret.kim = kim.Clone();
   ret.val = val.Clone();
   return ret;
  }


 }








}



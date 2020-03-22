

//** @author  GeTr Copyright© 1996-2020 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties
//** @license You may use, modify, distribute this code under GNU public dual licence free for NON-Commercial use as long as this notice remains unchanged.
//** @comment Utility Class for profiling Methods




using System;
using System.Collections.Generic;
using System.Collections;
using System.Linq;
using System.Text;

namespace org_xxdevplus_struct
{

 public class KeyInxMap<kTyp> //fast Collections are available in C# but it you use those then you will have difficulties to port your source to Java. If you use KeyInxMap (which is available in both, C# and Java) then you will pot your source easily :-)
 {
  //Copyright© 1996-2012 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties, You may use, modify, distribute this code under GNU public licence for NON-Commercial use as long as this notice remains unchanged.
  private static bool selfTested  = false;  private static string ass(bool expr) { if (!expr) throw new Exception(ass(true) + " SelfTest Failure"); return "KeyInxMap<>";  } private void init() { if (!selfTested) selfTest(); }

  private bool                       strong       = true;

  private Dictionary<kTyp, int>      keyinxS      = null;
  private Dictionary<int, kTyp>      inxkeyS      = null;
  private Hashtable                  keyinxW      = null;
  private Hashtable                  inxkeyW      = null;

  private Pile<kTyp>                 _Keys        = null;
  private Pile<kTyp>                 _kAsc        = null;
  private Pile<kTyp>                 _kDsc        = null;


  private static void selfTest() 
  {
   selfTested = true;
   KeyInxMap<string> kim = new KeyInxMap<string>(true);
   kim.Add("c"); kim.Add("b"); kim.Add("a");
   ass(kim[1].Equals("c")); ass(kim[2].Equals("b")); ass(kim[3].Equals("a"));
   ass(kim["a"] == 3); ass(kim["b"] == 2); ass(kim["c"] == 1);
   ass(kim[kim.Keys[1]] == 1); ass(kim[kim.Keys[2]] == 2); ass(kim[kim.Keys[3]] == 3);
   ass(kim[kim.kAsc[1]] == 3); ass(kim[kim.kAsc[2]] == 2); ass(kim[kim.kAsc[3]] == 1);
  }

  internal void clear() { if (strong) { keyinxS = new Dictionary<kTyp, int>(); inxkeyS = new Dictionary<int, kTyp>(); } else { keyinxW = new Hashtable(); inxkeyW = new Hashtable(); } _Keys = null; _kAsc = null; _kDsc = null; }

  public KeyInxMap(bool uniqueKeys) { init(); strong = uniqueKeys; clear(); }

  public KeyInxMap(KeyInxMap<kTyp> cloneFrom) 
  {
   init();
   strong = cloneFrom.strong;
   clear();
   if (strong) { foreach (kTyp key in cloneFrom.keyinxS.Keys) keyinxS.Add(key, cloneFrom.keyinxS[key]); foreach (int key in cloneFrom.inxkeyS.Keys) inxkeyS.Add(key, cloneFrom.inxkeyS[key]); } else { foreach (kTyp key in cloneFrom.keyinxW.Keys) keyinxW.Add(key, cloneFrom.keyinxW[key]); foreach (long key in cloneFrom.inxkeyW.Keys) inxkeyW.Add(key, cloneFrom.inxkeyW[key]); }
   if (cloneFrom._Keys  != null) _Keys = cloneFrom._Keys.Clone();  
   if (cloneFrom._kAsc  != null) _kAsc = cloneFrom._kAsc.Clone();  
   if (cloneFrom._kDsc  != null) _kDsc = cloneFrom._kDsc.Clone();  
  }

  public bool            hasKey    (kTyp   key) { return strong ? keyinxS.ContainsKey(key) : keyinxW.ContainsKey(key); }
  public int             Len                    { get { return strong ? keyinxS.Count : keyinxW.Count; }}
  public void            Add       (kTyp   key) { _Keys = null; _kAsc = null; _kDsc = null; if (strong) { inxkeyS.Add(keyinxS.Count + 1, key); try {keyinxS.Add(key, keyinxS.Count + 1);} catch (Exception ex) {inxkeyS.Remove(keyinxS.Count + 1); throw ex;}} else {inxkeyW.Add(keyinxW.Count + 1, key); try{keyinxW.Add(key, keyinxW.Count + 1);} catch (Exception ex) {inxkeyW.Remove(keyinxW.Count + 1); throw ex;} }}
  public void            Del       (int    inx) { _Keys = null; _kAsc = null; _kDsc = null; if (strong) { keyinxS.Remove(inxkeyS[inx]); inxkeyS.Remove(inx); for (int i = inx + 1; i <= inxkeyS.Count + 1; i++) { kTyp key = (kTyp)inxkeyS[i]; inxkeyS.Remove(i); inxkeyS.Add(i - 1, key); keyinxS[key] = i - 1; } } else {keyinxW.Remove(inxkeyW[inx]); inxkeyW.Remove(inx); for (int i = inx + 1; i <= inxkeyW.Count + 1; i++) {kTyp key = (kTyp)inxkeyW[i]; inxkeyW.Remove(i); inxkeyW.Add(i - 1, key); keyinxW[key] = i - 1; }  } }
  public void            Del       (kTyp   key) { if (strong) Del(keyinxS[key]); else Del((int)keyinxW[key]); }
  public kTyp            this      [int    inx] { get {if (strong) {if (inx < 0) inx = keyinxS.Count + inx + 1; return inxkeyS[inx];} else {if (inx < 0) inx = keyinxW.Count + inx + 1; return (kTyp)inxkeyW[inx];} } }
  public int             this      [kTyp   key] { get {return strong ? keyinxS[key] : ((int)keyinxW[key]); } }

  public Pile<kTyp>      Keys                   { get {if (_Keys != null) return _Keys;  if (strong) {_Keys = new Pile<kTyp>(keyinxS.Count); for (int i = 1; i <= keyinxS.Count; i++) _Keys[i] = inxkeyS[i]; return _Keys; } else {_Keys = new Pile<kTyp>(keyinxW.Count); for (int i = 1; i <= keyinxW.Count; i++) _Keys[i] = (kTyp)inxkeyW[i]; return _Keys; } } }

  public Pile<kTyp>      kAsc                   { get { if (_kAsc != null) return _kAsc; if (strong) { _kAsc = new Pile<kTyp>(keyinxS.Count); for (int i = 1; i <= keyinxS.Count; i++) _kAsc[ i] = inxkeyS[i]; } else { _kAsc = new Pile<kTyp>(keyinxW.Count); for (int i = 1; i <= keyinxW.Count; i++) _kAsc[ i] = (kTyp)inxkeyW[i]; } _kAsc.Sort(); return _kAsc; } }
  public Pile<kTyp>      kDsc                   { get { if (_kDsc != null) return _kDsc; if (strong) { _kDsc = new Pile<kTyp>(keyinxS.Count); for (int i = 1; i <= keyinxS.Count; i++) _kDsc[-i] = inxkeyS[i]; } else { _kDsc = new Pile<kTyp>(keyinxW.Count); for (int i = 1; i <= keyinxW.Count; i++) _kDsc[-i] = (kTyp)inxkeyW[i]; } _kDsc.Sort(); return _kDsc; } }

  public KeyInxMap<kTyp> before    (int    inx) { if (inx < 0) inx = Len + inx + 1; KeyInxMap<kTyp> ret = new KeyInxMap<kTyp>(strong); for (int i = 1; i < inx; i++) ret.Add(this[i]); return ret; }
  public KeyInxMap<kTyp> upto      (int    inx) { if (inx < 0) inx = Len + inx + 1; KeyInxMap<kTyp> ret = new KeyInxMap<kTyp>(strong); for (int i = 1; i <= inx; i++) ret.Add(this[i]); return ret; }
  public KeyInxMap<kTyp> at        (int    inx) { if (inx < 0) inx = Len + inx + 1; KeyInxMap<kTyp> ret = new KeyInxMap<kTyp>(strong); ret.Add(this[inx]); return ret; }
  public KeyInxMap<kTyp> from      (int    inx) { if (inx < 0) inx = Len + inx + 1; KeyInxMap<kTyp> ret = new KeyInxMap<kTyp>(strong); for (int i = inx; i <= Len; i++) ret.Add(this[i]); return ret; }
  public KeyInxMap<kTyp> after     (int    inx) { if (inx < 0) inx = Len + inx + 1; KeyInxMap<kTyp> ret = new KeyInxMap<kTyp>(strong); for (int i = inx + 1; i <= Len; i++) ret.Add(this[i]); return ret; }

  public KeyInxMap<kTyp> before    (kTyp   key) { return before (this[key]); }
  public KeyInxMap<kTyp> upto      (kTyp   key) { return upto   (this[key]); }
  public KeyInxMap<kTyp> at        (kTyp   key) { return at     (this[key]); }
  public KeyInxMap<kTyp> from      (kTyp   key) { return from   (this[key]); }
  public KeyInxMap<kTyp> after     (kTyp   key) { return after  (this[key]); }

  internal KeyInxMap<kTyp> Clone()
  {
   KeyInxMap<kTyp> ret = new KeyInxMap<kTyp>(strong);
   /*
   if (_Keys != null) ret._Keys = _Keys.Clone();
   if (_kAsc != null) ret._kAsc = _kAsc.Clone();
   if (_kDsc != null) ret._kDsc = _kDsc.Clone();
   */
   if (keyinxS != null) { ret.keyinxS = new Dictionary<kTyp, int>(); foreach (kTyp key in keyinxS.Keys) ret.keyinxS.Add(key, keyinxS[key]); }
   if (inxkeyS != null) { ret.inxkeyS = new Dictionary<int, kTyp>(); foreach (int  inx in inxkeyS.Keys) ret.inxkeyS.Add(inx, inxkeyS[inx]); }
   if (keyinxW != null) ret.keyinxW   = (Hashtable)keyinxW.Clone();
   if (inxkeyW != null) ret.inxkeyW   = (Hashtable)inxkeyW.Clone();
   return ret;
  }

 }
}















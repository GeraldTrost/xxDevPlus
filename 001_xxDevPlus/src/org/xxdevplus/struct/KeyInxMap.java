


//** @author  GeTr Copyright© 1996-2020 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties
//** @license You may use, modify, distribute this code under GNU public dual licence free for NON-Commercial use as long as this notice remains unchanged.
//** @comment Utility Class for profiling Methods

package org.xxdevplus.struct;
import java.util.Hashtable;


 public class KeyInxMap<kTyp> //fast Collections are available in C# but it you use those then you will have difficulties to port your source to Java. If you use KeyInxMap (which is available in both, C# and Java) then you will pot your source easily :-)
 {
  private static boolean selfTested  = false;  private static String ass(boolean expr) throws Exception { if (!expr) throw new Exception(ass(true) + " SelfTest Failure"); return "KeyInxMap<>";  } private void init() throws Exception { if (!selfTested) selfTest(); }

  private boolean                      strong       = true;
  private Hashtable<kTyp    , Integer> keyinxS      = new Hashtable<kTyp   , Integer>();
  private Hashtable<Integer , kTyp>    inxkeyS      = new Hashtable<Integer , kTyp>();
  private Hashtable<kTyp    , Integer> keyinxW      = new Hashtable<kTyp   , Integer>();
  private Hashtable<Integer , kTyp>    inxkeyW      = new Hashtable<Integer , kTyp>();

  private Pile<kTyp>                  _Keys        = null;
  private Pile<kTyp>                  _kAsc        = null;
  private Pile<kTyp>                  _kDsc        = null;
  
  private static void selfTest() throws Exception 
  {
   selfTested = true;
   KeyInxMap<String> kim = new KeyInxMap<String>(true);
   kim.Add("c"); kim.Add("b"); kim.Add("a");
   ass(kim.g(1).equals("c")); ass(kim.g(2).equals("b")); ass(kim.g(3).equals("a"));
   ass(kim.g("a") == 3); ass(kim.g("b") == 2); ass(kim.g("c") == 1);
   ass(kim.g(kim.Keys().g(1)) == 1); ass(kim.g(kim.Keys().g(2)) == 2); ass(kim.g(kim.Keys().g(3)) == 3);
   ass(kim.g(kim.kAsc().g(1)) == 3); ass(kim.g(kim.kAsc().g(2)) == 2); ass(kim.g(kim.kAsc().g(3)) == 1);
  } 

  void clear() { if (strong) {keyinxS = new Hashtable<kTyp, Integer>(); inxkeyS = new Hashtable<Integer, kTyp>(); } else {keyinxW = new Hashtable<kTyp, Integer>(); inxkeyW = new Hashtable<Integer, kTyp>(); } _Keys = null; _kAsc = null; _kDsc = null; }

  public KeyInxMap(boolean uniqueKeys) {strong = uniqueKeys; clear(); }

  public KeyInxMap(KeyInxMap<kTyp> cloneFrom) throws Exception 
  {
   strong = cloneFrom.strong;
   clear(); 
   if (strong) { keyinxS = (Hashtable<kTyp, Integer>) cloneFrom.keyinxS.clone();inxkeyS = (Hashtable<Integer, kTyp>) cloneFrom.inxkeyS.clone(); } else {keyinxW  = (Hashtable<kTyp, Integer>) cloneFrom.keyinxW.clone(); inxkeyW  = (Hashtable<Integer, kTyp>) cloneFrom.inxkeyW.clone(); }
   if (cloneFrom._Keys != null)   _Keys = cloneFrom._Keys.Clone(); 
   if (cloneFrom._kAsc != null)   _kAsc = cloneFrom._kAsc.Clone(); 
   if (cloneFrom._kDsc != null)   _kDsc = cloneFrom._kDsc.Clone(); 
  }

  public boolean     hasKey        (kTyp key)                  {return strong ? keyinxS.containsKey(key) : keyinxW.containsKey(key); }
  public int         Len()                                     { return strong ? keyinxS.size() : keyinxW.size(); }


  public void        Add           (kTyp key) throws Exception 
  {
   _Keys = null; 
   _kAsc = null; 
   _kDsc = null; 
   if (strong) 
   { 
    long oldSize = inxkeyS.size();
    inxkeyS.put(keyinxS.size() + 1, key); 
    if (oldSize == inxkeyS.size()) throw new Exception("Duplocate Key in KeyPile: " + key);
    try 
    {
     oldSize = keyinxS.size();
     keyinxS.put(key, keyinxS.size() + 1);
     if (oldSize == keyinxS.size()) 
         throw new Exception("Duplocate Key in KeyPile: " + key);
    } 
    catch (Exception ex) 
    {
     inxkeyS.remove(keyinxS.size() + 1L); 
     throw ex;
    }
   } 
   else 
   {
    inxkeyW.put(keyinxW.size() + 1, key); 
    try
    {
     keyinxW.put(key, keyinxW.size() + 1);
    } 
    catch (Exception ex) 
    {
     inxkeyW.remove(keyinxW.size() + 1); 
     throw ex;} 
   }
  }
  
  public void        Del           (int  inx) throws Exception  {try{_Keys = null; _kAsc = null; _kDsc = null; if (strong) {keyinxS.remove(inxkeyS.get(inx)); inxkeyS.remove(inx); for (int i = inx + 1; i <= inxkeyS.size() + 1; i++) { kTyp key = (kTyp)inxkeyS.get(i); inxkeyS.remove(i); inxkeyS.put(i - 1, key); keyinxS.put(key, i - 1); } } else {keyinxW.remove(inxkeyW.get(inx)); inxkeyW.remove(inx); for (int i = inx + 1; i <= inxkeyW.size() + 1; i++) {kTyp key = (kTyp)inxkeyW.get(i); inxkeyW.remove(i); inxkeyW.put(i - 1, key); keyinxW.put(key, i - 1); } } }catch(Exception ex){throw new Exception("inx not found " + inx);}}
  public void        Del           (kTyp key) throws Exception  {try{if (strong) Del(keyinxS.get(key)); else Del(keyinxW.get(key));}catch(Exception ex){throw new Exception("key not found " + key.toString());} }
  public kTyp        g             (int  inx) throws Exception  {try{if (strong) {if (inx < 0) inx = keyinxS.size() + inx + 1; return inxkeyS.get(inx);} else {if (inx < 0) inx = keyinxW.size() + inx + 1; return (kTyp)inxkeyW.get(inx);} }catch(Exception ex){throw new Exception("inx not found " + inx);}}
  public int         g             (kTyp key) throws Exception  {try{return strong ? keyinxS.get(key) : keyinxW.get(key);}catch(Exception ex){throw new Exception("key not found " + key.toString());}} 

  public Pile<kTyp> Keys          (         ) throws Exception  
  {
   if (_Keys != null) return _Keys; 
   if (strong) 
   {
    _Keys = new Pile<kTyp>(keyinxS.size()); 
    for (int i = 1; i <= keyinxS.size(); i++) _Keys.s(inxkeyS.get(i), i); 
    return _Keys; 
   } 
   else 
   {
    _Keys = new Pile<kTyp>(keyinxW.size()); 
    for (int i = 1; i <= keyinxW.size(); i++) _Keys.s((kTyp)inxkeyW.get(i), i); 
    return _Keys; 
   } 
  } 
  
  public Pile<kTyp> kAsc          (         ) throws Exception  
  {
   if (_kAsc != null) return _kAsc; 
   if (strong) 
   {
    _kAsc = new Pile<kTyp>(keyinxS.size()); 
    for (int i = 1; i <= keyinxS.size(); i++) _kAsc.s(inxkeyS.get(i),  i);
   } 
   else 
   {
    _kAsc = new Pile<kTyp>(keyinxW.size()); 
    for (int i = 1; i <= keyinxW.size(); i++) _kAsc.s((kTyp)inxkeyW.get(i),  i); 
   } 
   _kAsc.sort(); 
   return _kAsc; 
  } 
  public Pile<kTyp> kDsc          (         ) throws Exception  {if (_kDsc != null) return _kDsc; if (strong) {_kDsc = new Pile<kTyp>(keyinxS.size()); for (int i = 1; i <= keyinxS.size(); i++) _kDsc.s(inxkeyS.get(i), -i);} else {_kDsc = new Pile<kTyp>(keyinxW.size()); for (int i = 1; i <= keyinxW.size(); i++) _kDsc.s((kTyp)inxkeyW.get(i), -i); } _kDsc.sort(); return _kDsc; } 

  public KeyInxMap<kTyp> before   (int inx) throws Exception    {if (inx < 0) inx = Len() + inx + 1; KeyInxMap<kTyp> ret = new KeyInxMap<kTyp>(strong); for (int i = 1; i < inx; i++) ret.Add(g(i)); return ret; }
  public KeyInxMap<kTyp> upto     (int inx) throws Exception    {if (inx < 0) inx = Len() + inx + 1; KeyInxMap<kTyp> ret = new KeyInxMap<kTyp>(strong); for (int i = 1; i <= inx; i++) ret.Add(g(i)); return ret; }
  public KeyInxMap<kTyp> at       (int inx) throws Exception    {if (inx < 0) inx = Len() + inx + 1; KeyInxMap<kTyp> ret = new KeyInxMap<kTyp>(strong); ret.Add(g(inx)); return ret; }
  public KeyInxMap<kTyp> from     (int inx) throws Exception    {if (inx < 0) inx = Len() + inx + 1; KeyInxMap<kTyp> ret = new KeyInxMap<kTyp>(strong); for (int i = inx; i <= Len(); i++) ret.Add(g(i)); return ret; }
  public KeyInxMap<kTyp> after    (int inx) throws Exception    {if (inx < 0) inx = Len() + inx + 1; KeyInxMap<kTyp> ret = new KeyInxMap<kTyp>(strong); for (int i = inx + 1; i <= Len(); i++) ret.Add(g(i)); return ret; }

  public KeyInxMap<kTyp> before   (kTyp key) throws Exception   {return before(g(key)); }
  public KeyInxMap<kTyp> upto     (kTyp key) throws Exception   {return upto(g(key)); }
  public KeyInxMap<kTyp> at       (kTyp key) throws Exception   {return at(g(key)); }
  public KeyInxMap<kTyp> from     (kTyp key) throws Exception   {return from(g(key)); }
  public KeyInxMap<kTyp> after    (kTyp key) throws Exception   {return after(g(key)); }

         KeyInxMap<kTyp> Clone() throws Exception
  {
   KeyInxMap<kTyp> ret = new KeyInxMap<kTyp>(strong);
   /*
   if (_Keys != null) ret._Keys = _Keys.Clone();
   if (_kAsc != null) ret._kAsc = _kAsc.Clone();
   if (_kDsc != null) ret._kDsc = _kDsc.Clone();
   */
   if (keyinxS      != null) ret.keyinxS      = (Hashtable<kTyp    , Integer>)keyinxS.clone();
   if (inxkeyS      != null) ret.inxkeyS      = (Hashtable<Integer , kTyp>   )inxkeyS.clone();
   if (keyinxW      != null) ret.keyinxW      = (Hashtable<kTyp    , Integer>)keyinxW.clone();
   if (inxkeyW      != null) ret.inxkeyW      = (Hashtable<Integer , kTyp>   )inxkeyW.clone();
   return ret;
  }

  
  
 /* Old source, thid worked fine when all long variables were still int variables ...
 public boolean      hasKey      (kType key)  {return keyinx.containsKey(key); }
 public long         len         ()           {return inxkey.size(); }
 public void         add         (kType key)  throws Exception {_keys = null; _sortedKeys  = null; inxkey.put(keyinx.size() + 1, key); try {keyinx.put(key, keyinx.size() + 1);} catch (Exception ex) {inxkey.remove(keyinx.size() + 1); throw ex;} }
 public void         del         (long inx)   {_keys = null; _sortedKeys  = null; keyinx.remove(inxkey.get(inx)); inxkey.remove(inx); for (int i = inx + 1; i <= inxkey.size() + 1; i++) {kType key = inxkey.get(i); inxkey.remove(i); inxkey.put(i - 1, key); keyinx.put(key, i - 1); } }
 public void         del         (kType key)  {del(keyinx.get(key)); }
 public kType        get         (long inx)    {if (inx < 0) inx = keyinx.size() + inx + 1; return inxkey.get(inx); }
 public int          get         (kType key)  {return keyinx.get(key); }
 public Pack<kType>  keys        ()           {if (_keys != null) return _keys; _keys = new Pack<kType>(keyinx.size()); for(int i = 1; i <= keyinx.size(); i++) _keys.set(i, inxkey.get(i)); return _keys; }
 public Pack<kType>  sortedKeys  ()           {if (_sortedKeys != null) return _sortedKeys; _sortedKeys = new Pack<kType>(keyinx.size()); for(int i = 1; i <= keyinx.size(); i++) _sortedKeys.set(i, inxkey.get(i)); _sortedKeys.sort(); return _sortedKeys; }

 public KeyInxMap<kType> before  (int inx)    throws Exception {if (inx < 0) inx = keyinx.size() + inx + 1; KeyInxMap<kType> ret = new KeyInxMap<kType>(); for (int i = 1; i < inx; i++) ret.add(get(i)); return ret; }
 public KeyInxMap<kType> upto    (int inx)    throws Exception {if (inx < 0) inx = keyinx.size() + inx + 1; KeyInxMap<kType> ret = new KeyInxMap<kType>(); for (int i = 1; i <= inx; i++) ret.add(get(i)); return ret; }
 public KeyInxMap<kType> at      (int inx)    throws Exception {if (inx < 0) inx = keyinx.size() + inx + 1; KeyInxMap<kType> ret = new KeyInxMap<kType>(); ret.add(get(inx)); return ret; }
 public KeyInxMap<kType> from    (int inx)    throws Exception {if (inx < 0) inx = keyinx.size() + inx + 1; KeyInxMap<kType> ret = new KeyInxMap<kType>(); for (int i = inx; i <= inxkey.size(); i++) ret.add(get(i)); return ret; }
 public KeyInxMap<kType> after   (int inx)    throws Exception {if (inx < 0) inx = keyinx.size() + inx + 1; KeyInxMap<kType> ret = new KeyInxMap<kType>(); for (int i = inx + 1; i <= inxkey.size(); i++) ret.add(get(i)); return ret; }

 public KeyInxMap<kType> before  (kType key)  throws Exception {return before(get(key)); }
 public KeyInxMap<kType> upto    (kType key)  throws Exception {return upto(get(key));   }
 public KeyInxMap<kType> at      (kType key)  throws Exception {return at(get(key));     }
 public KeyInxMap<kType> from    (kType key)  throws Exception {return from(get(key));   }
 public KeyInxMap<kType> after   (kType key)  throws Exception {return after(get(key));  }
 */
  

}


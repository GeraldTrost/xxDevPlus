

//** @author  GeTr Copyright© 1996-2020 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties
//** @license You may use, modify, distribute this code under GNU public dual licence free for NON-Commercial use as long as this notice remains unchanged.
//** @comment 1 based Enhanced HashMap also supporting negative backward counting 


package org.xxdevplus.struct;



import org.xxdevplus.struct.KeyInxMap;
import java.util.ArrayList;
import java.util.Iterator;
import org.xxdevplus.chain.Chain;



public class KeyPile<kTyp, iTyp> implements Iterable<iTyp> //similar to array<iType> but Index starts at 1, not 0!  constructor takes a list of arguments.
{

 //Copyright© 1996-2020 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties, You may use, modify, distribute this code under GNU public licence for NON-Commercial use as long as this notice remains unchanged.
 private static boolean selfTested  = false;  private static String ass(boolean expr) throws Exception { if (!expr) throw new Exception(ass(true) + " SelfTest Failure"); return "KeyPile<,>";  } private void init() throws Exception { if (!selfTested) selfTest(); }

 public  Object                     _name = "";

 private Pile<iTyp>                 val;
 private KeyInxMap<kTyp>            kim;
 private boolean                    uniqueKeys = true;

 public void                      clear         (                               ) throws Exception {val.Clear(); kim.clear(); }
 public int                       Len           (                               ) throws Exception { return val.Len(); }
 public boolean                   hasKey        (kTyp                        key) throws Exception { return kim.hasKey(key); }
 public String                    Name          (                               ) throws Exception { return (String)_name; }                                                               public void Name  (String           value)  { _name = value; } 
 public iTyp                      g             (int                         inx) throws Exception { return val.g(inx);  }                                                                 public void s     (iTyp value, int   inx)  { if (inx < 0) inx = val.Len() + inx + 1; if (inx == 0) _name = value; else val.s(value, inx); }

 public iTyp                      g             (kTyp                        key) throws Exception                  
 { 
  int inx = kim.g(key);
  if (inx < 0) inx = val.Len() + inx + 1; 
  return val.g(inx); 
 }  

 public void p     (iTyp value, kTyp key) throws Exception {if (kim.hasKey(key)) s(value, key); else Add(key, value);}

 public void s     (iTyp value, kTyp key) throws Exception
 {
  int inx = kim.g(key);
  if (inx < 0) inx = val.Len() + inx + 1;
  if (inx == 0) _name = value; else val.s(value, inx);
 }

 public void                      Push          (KeyPile<kTyp, iTyp> more       ) throws Exception { for (kTyp key : more.Keys()) Add(key, more.g(key)); }
 public KeyPile<kTyp, iTyp>       Add           (KeyPile<kTyp, iTyp> more       ) throws Exception { for (kTyp key : more.Keys()) Add(key, more.g(key)); return this; }


 public KeyPile<kTyp, iTyp>       Add           (kTyp key, iTyp more            ) throws Exception 
 { 
  if (kim.hasKey(key)) throw new Exception("Duplicate Keys in Keypile: " + key);
  kim.Add(key);
  val.Add(more); 
  return this; 
 }


 public KeyPile<kTyp, iTyp>       Set           (kTyp key, iTyp more            ) throws Exception { if (kim.hasKey(key)) val.s(more, kim.g(key)); else {kim.Add(key); val.Add(more);} return this; }
 public KeyPile<kTyp, iTyp>       Put           (kTyp key, iTyp more            ) throws Exception { if (kim.hasKey(key)) {Del(key); Add(key, more); } else {kim.Add(key); val.Add(more);} return this; }

 public KeyPile<kTyp, iTyp>       Del           (kTyp key                       ) throws Exception 
 { 
  try 
  { 
   val.Del(kim.g(key)); 
   kim.Del(key); 
  } 
  catch (Exception ex) {} 
  return this; 
 }

 public Pile<kTyp>                Keys          (                               ) throws Exception { return kim.Keys();       }
 public Pile<kTyp>                kAsc          (                               ) throws Exception { return kim.kAsc(); }
 public Pile<kTyp>                kDsc          (                               ) throws Exception { return kim.kDsc(); }

 public boolean             hasNulls ()      throws Exception   { return val.hasNulls();}

 public KeyPile<kTyp, iTyp> before (int inx) throws Exception   { KeyPile<kTyp, iTyp> ret = new KeyPile<kTyp, iTyp>(); ret.val = val.before(inx); ret.kim = kim.before(ret.val.Len()                 ); return ret; }
 public KeyPile<kTyp, iTyp> upto   (int inx) throws Exception   { KeyPile<kTyp, iTyp> ret = new KeyPile<kTyp, iTyp>(); ret.val = val.upto(inx);   ret.kim = kim.upto  (ret.val.Len()                 ); return ret; }
 public KeyPile<kTyp, iTyp> at     (int inx) throws Exception   { KeyPile<kTyp, iTyp> ret = new KeyPile<kTyp, iTyp>(); ret.val = val.at(inx);     ret.kim = kim.at    (inx                           ); return ret; }
 public KeyPile<kTyp, iTyp> from   (int inx) throws Exception   { KeyPile<kTyp, iTyp> ret = new KeyPile<kTyp, iTyp>(); ret.val = val.from(inx);   ret.kim = kim.from  (1 + val.Len() - ret.val.Len() ); return ret; }
 public KeyPile<kTyp, iTyp> after  (int inx) throws Exception   { KeyPile<kTyp, iTyp> ret = new KeyPile<kTyp, iTyp>(); ret.val = val.after(inx);  ret.kim = kim.after (1 + val.Len() - ret.val.Len() ); return ret; }

 public KeyPile<kTyp, iTyp> before(kTyp key) throws Exception   { return before (kim.g(key));  }
 public KeyPile<kTyp, iTyp> upto(kTyp key)   throws Exception   { return upto   (kim.g(key));  }
 public KeyPile<kTyp, iTyp> at(kTyp key)     throws Exception   { return at     (kim.g(key));  }
 public KeyPile<kTyp, iTyp> from(kTyp key)   throws Exception   { return from   (kim.g(key));  }
 public KeyPile<kTyp, iTyp> after(kTyp key)  throws Exception   { return after  (kim.g(key));  }

 public Pile<iTyp> select(kTyp ...          key)  throws Exception       { Pile<iTyp> ret = new Pile<iTyp>(); for (kTyp k : key) ret.Add(g(k)); return ret; }
 public Pile<iTyp> select(Pile<kTyp>        key)  throws Exception       { Pile<iTyp> ret = new Pile<iTyp>(); for (kTyp k : key) ret.Add(g(k)); return ret; }
 public Pile<iTyp> select(ArrayList<kTyp>   key)  throws Exception       { Pile<iTyp> ret = new Pile<iTyp>(); for (kTyp k : key) ret.Add(g(k)); return ret; }

 public Pile<iTyp>     Vals    ()  { return val;             }
 public Iterator<iTyp> iterator()  { return  val.iterator(); }

  
 /* this is the old implementation before all int turned into long. it worked fine ....
   public KeyPack<kType, iType>[] toHashPileArray(Object... items)
 {
  //List<T> rowData = getRowsAsList(rows);
  Class<?> cls = this.getClass();Moreover
  KeyPack<kType, iType>[] ret = (KeyPack<kType, iType>[])Array.newInstance(cls, items.length);
  for (int i = 0; i < items.length; i++) ret[i] = (KeyPack<kType, iType>) items[i];
  return ret;
 }

 public  Object                    _name = "";

 private Pack<iType>                 val;
 private KeyInxMap<kType>           _keys;

 public String getName()  { return (String) _name; }
 public void setName(Object value) { _name = value; }

 public int len() { return val.len();}

 public boolean hasKey(kType key) { return _keys.hasKey(key); }

 public iType get(int inx) { return val.get(inx); }

 public iType get(kType key)
 {
  int inx = _keys.get(key); if (inx < 0) inx = val.len() + inx + 1;
  return val.get(inx);
 }

 public void set(int inx, iType value)
 {
  if (inx < 0) inx = val.len() + inx + 1;
  if (inx == 0) _name = value; else val.set(inx, value);
 }

 public void set(kType key, iType value)
 {
  int inx = _keys.get(key); if (inx < 0) inx = val.len() + inx + 1;
  if (inx == 0) _name = value; else val.set(inx, value);
 }

 public KeyPack(int count)
 {
  this.val = new Pack<iType>(count);     //
  this._keys = new KeyInxMap<kType>();
 }

 /*
 public KeyPack<iType> clone()
 {
  KeyPack<iType> ret = new KeyPack<iType>(len()); ret._name = _name;
  for (int i = 1; i <= len(); i++) ret.set(i, this.get(i));
  ret._keys = new KeyInxMap<String>(); for(String k : _keys.keySet()) { ret._keys.put(k, _keys.get(k)); }
  return ret; //return (Pack<iType>) mw._obj(ret);
 }
 */

 /*
 public KeyPack(String name, iType... val)
 {
  this._name = name;
  this.val = val;
 }

 public KeyPack(String name, List<iType> val)
 {
  this._name = name;
  this.val = (iType[]) new Object[val.size()];
  for (int i = 1; i <= val.size(); i++)  this.val[i - 1] = val.get(i - 1);
 }

 public KeyPack<iType> slice(int from, int upto)
 {
  KeyPack<iType> ret = new KeyPack<iType>(1 + upto - from);
  ret._name = _name;
  for (int i = 1; i <= ret.len(); i++) { ret.set(i , this.get(i + from - 1)); }
  ret._keys ...
  return ret;
 }

 public void add(Pack<iType> more)
 {
  iType[] res = (iType[]) new Object[len() + more.len()];
  for (int i = 1; i <= val.length; i++) res[i - 1] = val[i - 1];
  for (int i = 1; i <= more.len(); i++) res[i + val.length - 1] = more.get(i);
  val = res;
 }

 public void add(List<iType> more)
 {
  iType[] res = (iType[]) new Object[len() + more.size()];
  for (int i = 1; i <= val.length; i++)
   res[i - 1] = val[i - 1];
  for (int i = 1; i <= more.size(); i++)
   res[i + val.length - 1] = more.get(i); val = res;
 }
 */

 /*
 public void add(kType key, iType more)
 {
  _keys.add(key);
  val.add(  more);
 }

 public void del(kType key)
 {
  val.del(_keys.get(key));
  _keys.del(key);
 }

 public Pack<kType> keys()
 {
  return _keys.keys();
 }

 public Pack<kType> sortedKeys()
 {
  return _keys.sortedKeys();
 }



 public KeyPack<kType, iType> before(int inx)
 {
  KeyPack<kType, iType> ret = new KeyPack<kType, iType>(0);
  ret.val = val.before(inx);
  ret._keys = _keys.before(inx);
  return ret;
 }

 public KeyPack<kType, iType> upto(int inx)
 {
  KeyPack<kType, iType> ret = new KeyPack<kType, iType>(0);
  ret.val = val.upto(inx);
  ret._keys = _keys.upto(inx);
  return ret;
 }

 public KeyPack<kType, iType> at(int inx)
 {
  KeyPack<kType, iType> ret = new KeyPack<kType, iType>(0);
  ret.val = val.at(inx);
  ret._keys = _keys.at(inx);
  return ret;
 }

 public KeyPack<kType, iType> from(int inx)
 {
  KeyPack<kType, iType> ret = new KeyPack<kType, iType>(0);
  ret.val = val.from(inx);
  ret._keys = _keys.from(inx);
  return ret;
 }

 public KeyPack<kType, iType> after(int inx)
 {
  KeyPack<kType, iType> ret = new KeyPack<kType, iType>(0);
  ret.val = val.after(inx);
  ret._keys = _keys.after(inx);
  return ret;
 }

 public KeyPack<kType, iType> before(kType key)   { return before(_keys.get(key));  }
 public KeyPack<kType, iType> upto(kType key)     { return upto(_keys.get(key));  }
 public KeyPack<kType, iType> at(kType key)       { return at(_keys.get(key));  }
 public KeyPack<kType, iType> from(kType key)     { return from(_keys.get(key));  }
 public KeyPack<kType, iType> after(kType key)    { return after(_keys.get(key));  }


 public Iterator<iType> iterator()
 {
  return  val.iterator();
 }

 //public IEnumerator<iType> GetEnumerator() { return new PileEnumerator(this); }
 //IEnumerator IEnumerable.GetEnumerator() { return GetEnumerator(); }
 //public class PileEnumerator: IEnumerator<iType>
 //{
 // private Pack<iType> coll;
 // private iType current;
 // private int index;
 // public PileEnumerator(Pack<iType> coll) { Monitor.Enter(coll.val.SyncRoot); this.index = -1; this.coll = coll; }
 // public iType Current { get { return current; } }
 // object IEnumerator.Current { get { return Current; } }
 // public bool MoveNext() { if (++index >= coll.val.Length) { return false; } else { current = coll.val[index]; return true; } }
 // public void Reset() { current = default(iType); index = 0; }
 // public void Dispose() { try { current = default(iType); index = coll.val.Length; } finally { Monitor.Exit(coll.val.SyncRoot); } }
 //}

 */


 private static void selfTest() throws Exception 
 { 
  selfTested = true; 
  KeyPile<String, Long> kp = new KeyPile<String, Long>();
  kp.Add("a", 1L);
  kp.Add("b", 2L);
  ass(kp.from   (3).Len() == 0);
  ass(kp.after  (2).Len() == 0);
  ass(kp.upto   (5).Len() == 2);
  ass(kp.before (5).Len() == 2);
  
  ass(kp.Vals().csv(", ", "", "").equals("1, 2"));
  ass(kp.Keys().csv(", ", "", "").equals("a, b"));
  try {kp.Add("b", 122L);} catch (Exception ex) {}
  ass(kp.Vals().csv(", ", "", "").equals("1, 2"));
  ass(kp.Keys().csv(", ", "", "").equals("a, b"));
  kp.s(44L, "b");
  ass(kp.Vals().csv(", ", "", "").equals("1, 44"));
  ass(kp.Keys().csv(", ", "", "").equals("a, b"));
  kp.p(22L, "c");
  ass(kp.Vals().csv(", ", "", "").equals("1, 44, 22"));
  ass(kp.Keys().csv(", ", "", "").equals("a, b, c"));
  kp.p(22L, "b");
  ass(kp.Vals().csv(", ", "", "").equals("1, 22, 22"));
  ass(kp.Keys().csv(", ", "", "").equals("a, b, c"));
  KeyPile<String, Long> kp1 = new KeyPile<String, Long>();
  KeyPile<Long, Integer> kp2 = new KeyPile<Long, Integer>();
  kp2.Add(10737418241L, 2);
  kp2.Add(2147483649L, 2);
  kp2.Add(2147483647L, -13);
  kp2.Add(2147483650L, 5);
  kp2.Add(2147483646L, 5);
  kp2.Add(-27917287426L, -7);
  kp2.Add(-53687091201L, -13);
  kp2.Add(23622320130L, 5);
  kp2.Del(-27917287426L);
  ass(kp2.g(kp2.Keys().g(4)) == 5L);

  ass(kp2.Keys().csv(", ", "", "()").equals("(10737418241, 2147483649, 2147483647, 2147483650, 2147483646, -53687091201, 23622320130)"));
  ass((new Pile<Integer>(0, kp2.val.array())).csv(", ", "", "()").equals("(2, 2, -13, 5, 5, -13, 5)"));
  kp2.s(-4, 2147483646L);
  ass((new Pile<Integer>(0, kp2.val.array())).csv(", ", "", "()").equals("(2, 2, -13, 5, -4, -13, 5)"));
  kp2.Del(-53687091201L);
  ass((new Pile<Integer>(0, kp2.val.array())).csv(", ", "", "()").equals("(2, 2, -13, 5, -4, 5)"));
  ass(kp2.Keys().csv(", ", "", "()").equals("(10737418241, 2147483649, 2147483647, 2147483650, 2147483646, 23622320130)"));
  ass(kp2.g(kp2.Keys().g(4)) == 5L);



  kp1.Add("a", 1L);
  kp1.Add("b", 2L);
  kp1.Add("c", 3L);
  kp1.Add("d", 4L);
  ass(kp1.select("b", "c").csv(", ", "", "()").equals("(2, 3)"));
  kp1.Del("b");
  ass(kp1.select("a", "c").csv(", ", "", "()").equals("(1, 3)"));
  kp1.Del("a");
  ass(kp1.select("c", "d").csv(", ", "", "()").equals("(3, 4)"));
 } 

 public KeyPile                                 (                               )                  { try { this.uniqueKeys = true; this.val = new Pile<iTyp>(); this.kim = new KeyInxMap<kTyp>(uniqueKeys); init(); } catch (Exception ex) { ex = ex; } }
 public KeyPile                                 (KeyPile<kTyp, iTyp>   cloneFrom) throws Exception { try { _name = cloneFrom._name; val = cloneFrom.val.Clone(); kim = new KeyInxMap<kTyp>(cloneFrom.kim); } catch (Exception ex) {}}

 
 public KeyPile                                 (String[][] keyData) throws Exception 
 { 
  this();
  for (int i = 1; i <= keyData.length; i++)
  {
   Pile<iTyp> data = new Pile<iTyp>(0, (iTyp[]) keyData[ i - 1 ]);
   Add((kTyp)keyData[ i - 1 ], (iTyp) data);
  }
 }

 public KeyPile                                 (String[] keyData) throws Exception 
 { 
  this();
  for (int i = 1; i <= keyData.length; i++)
  {
   Pile<String> data = new Pile<String>();
   Chain line = new Chain(keyData[ i - 1 ].trim());
   
   kTyp key = (kTyp) line.before(1, "|").text().trim();
   line = line.after(1, "|").Trim(); 
   
   while (line.len() > 0)
   {
    data.Push(line.before(1, "|").text().trim());
    line = line.after(1, "|").Trim(); 
   }
   if (data.Len() == 1) data.Push("");
   Add(key, (iTyp) data);
  }
 }

 public KeyPile<kTyp, iTyp> Clone() throws Exception
 {
  KeyPile<kTyp, iTyp> ret = new KeyPile<kTyp, iTyp>();
  ret._name = _name;
  ret.kim = kim.Clone();
  ret.val = val.Clone();
  return ret;
 }

  
}













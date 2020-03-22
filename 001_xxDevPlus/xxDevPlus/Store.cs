

//** @author  GeTr Copyright© 1996-2020 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties
//** @license You may use, modify, distribute this code under GNU public dual licence free for NON-Commercial use as long as this notice remains unchanged.
//** @comment General Store






using System;
using System.Threading;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Collections;


/** IMPORTANT NOTES: seek and peek only work if you have sorted data in the Store !!!
 if (seek(obj) == peek(obj)) then obj is uniquely contained and the result is its pos (1 .. len()). if result <= 0 then obj is not contained and result is backward pos of where it should stand.
 
  if (seek(obj) != peek(obj)) but both are btw (1 .. len()) then the obj has been found several times, peek return the lowest pos, seek returns the highest pos.
 
  if not found peek returns Previous position and seek returns Subsequent position (seek works from left to right, peek workd from right to left)
*/

namespace org_xxdevplus_struct
{

 public class Store<typ> : IEnumerable<typ> //similar to array<typ> but Index starts at 1, not 0! Reverse index starts at -1. Moreover constructor takes a list of arguments.
 {
  //Copyright© 1996-2012 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties, You may use, modify, distribute this code under GNU public licence for NON-Commercial use as long as this notice remains unchanged.
  // ATTENTION
  // Currently this class is un-finished - so it supports only SORTED DataSets.
  // When finished - it will also support unsorted DataSets and it will switch to internal hashmap (oim) on demand.
  // Q: why implement yet anotehr indexed collecion class ? A: in Java or in C# resp. there is no TreeMap or SortedList that accepts duplicate keys ...

  private static bool selfTested  = false; private static string ass(bool expr) { if (!expr) throw new Exception(ass(true) + " SelfTest Failure"); return "Store"; }

  protected      Sorter                               srt       = null;
  protected      typ[]                                buf       = null;
  protected      int                                  sInx      = 0;      // Index of first Array-Item
  protected      int                                  eInx      = -1;     // Index of last  Array-Item
  protected      int                                  sRsv      = 0;      // desired reserve before start when buf must be re-created
  protected      int                                  eRsv      = 0;      // desired reserve after end when buf must be re-created
  protected      bool                                 sorted    = true;
  private        Dictionary<typ, List<int>>           oim       = null;   // The Object - Index Map

  protected      string dbgString()
 {
    if (buf.Length == 0) return "[0]";
    string ret = "[" + len() + "] ";
    if (srt.num == 0) for (int i = 0; i < buf.Length; i++) ret += buf[i] + " (" + i + "), "; else for (int i = 0; i < buf.Length; i++) if (buf[i] == null) ret += buf[i] + " (" + i + "), "; else ret += "@" + ((TaggedObject<Object>)(object)buf[i]).Tag(srt.num) + " (" + i + "), ";
    return ret.Substring(0, ret.Length - 2);
  }

  private        static void        selfTest() 
  {
   selfTested = true;
   Store<string> a;
   a = new Store<string>(0, 0, 0, new Sorter(0)); ass(a.peek("mary") == -1); ass(a.seek("mary") == 0);
   ass(a.dbgString().Equals("[0] null (0), null (1), null (2), null (3)"));
   a.copyItems(0, 0, 1);
   ass(a.dbgString().Equals("[0] null (0), null (1), null (2), null (3)"));
   a = new Store<string>(2, 0, 2, new Sorter(0)); ass(a.peek("mary") == -1); ass(a.seek("mary") == 0);
   a.push("mary"); ass(a.peek("mary") == 1); ass(a.seek("mary") == 1); ass(a.peek("gary") == -2); ass(a.seek("gary") == -1); ass(a.peek("tary") == -1); ass(a.seek("tary") == 0);
   a = new Store<string>(2, 0, 2, new Sorter(0)); ass(a.peek("mary") == -1); ass(a.seek("mary") == 0);
   a.poke("mary"); ass(a.peek("mary") == 1); ass(a.seek("mary") == 1); ass(a.peek("gary") == -2); ass(a.seek("gary") == -1); ass(a.peek("tary") == -1); ass(a.seek("tary") == 0);
   a = new Store<string>(new string[] { "mary" }, 2, 2, new Sorter(0)); ass(a.peek("mary") == 1); ass(a.seek("mary") == 1); ass(a.peek("gary") == -2); ass(a.seek("gary") == -1); ass(a.peek("tary") == -1); ass(a.seek("tary") == 0);
   a = new Store<string>(new string[] { "mary", "mary", "mary" }, 2, 2, new Sorter(0)); ass(a.peek("mary") == 3); ass(a.seek("mary") == 1); ass(a.peek("gary") == -4); ass(a.seek("gary") == -3); ass(a.peek("tary") == -1); ass(a.seek("tary") == 0);
   a = new Store<string>(new string[] { "mary", "mary", "mary", "mary" }, 2, 2, new Sorter(0));
   ass(a.peek("mary") == 4); ass(a.seek("mary") == 1); ass(a.peek("gary") == -5); ass(a.seek("gary") == -4); ass(a.peek("tary") == -1); ass(a.seek("tary") == 0);
   a = new Store<string>(new string[] { "mary", "mary", "mary", "mary", "mary" }, 2, 2, new Sorter(0));
   ass(a.peek("mary") == 5); ass(a.seek("mary") == 1); ass(a.peek("gary") == -6); ass(a.seek("gary") == -5); ass(a.peek("tary") == -1); ass(a.seek("tary") == 0);
   a = new Store<string>(new string[] { "a", "c", "c", "c", "f", "f", "f", "g" }, 2, 2, new Sorter(0));
   ass(a.peek("c1") == -5);
   a.copyItems(-2, -1, 2); ass(a.sInx == 2); ass(a.eInx == 11);
   a.copyItems(1, 2, -2); ass(a.sInx == 0); ass(a.eInx == 11);
   a = new Store<string>(new string[] { "a", "b", "c", "d", "e", "f", "g" }, 3, 4, new Sorter(0)); ass(a.buf.Length == 7); ass(a.peek("a1") == -7); ass(a.seek("a1") == -6);
   a.copyItems(1, 1, -1);                                                                            ass(a.buf.Length == 15); ass(a.dbgString().Equals("[8] null (0), null (1), null (2), a (3), a (4), b (5), c (6), d (7), e (8), f (9), g (10), null (11), null (12), null (13), null (14)"));
   a = new Store<string>(new string[] { "a", "b", "c", "d", "e", "f", "g" }, 3, 4, new Sorter(0));
   a.copyItems(1, 7, -7);                                                                            ass(a.dbgString().Equals("[14] null (0), null (1), null (2), a (3), b (4), c (5), d (6), e (7), f (8), g (9), a (10), b (11), c (12), d (13), e (14), f (15), g (16), null (17), null (18), null (19), null (20)"));
   a = new Store<string>(new string[] { "a", "b", "c", "d", "e", "f", "g" }, 3, 4, new Sorter(0));
   a.poke(2, "a1"); ass(a.buf.Length == 15); ass(a.dbgString().Equals("[8] null (0), null (1), null (2), a (3), a1 (4), b (5), c (6), d (7), e (8), f (9), g (10), null (11), null (12), null (13), null (14)")); ass(a.sInx == 3); ass(a.eInx == 10);
   a.poke(2, "a1"); ass(a.buf.Length == 15); ass(a.dbgString().Equals("[9] null (0), null (1), null (2), a (3), a1 (4), a1 (5), b (6), c (7), d (8), e (9), f (10), g (11), null (12), null (13), null (14)")); ass(a.sInx == 3); ass(a.eInx == 11);
   a = new Store<string>(new string[] { "a", "b", "c", "d", "e", "f", "g" }, 3, 4, new Sorter(0));
   ass(a.peek("!") == -8); ass(a.peek("f1") == -2); ass(a.peek("h") == -1); ass(a.seek("!") == -7); ass(a.seek("h") == 0);
   a.poke("a1"); ass(a.buf.Length == 15); ass(a.dbgString().Equals("[8] null (0), null (1), null (2), a (3), a1 (4), b (5), c (6), d (7), e (8), f (9), g (10), null (11), null (12), null (13), null (14)")); ass(a.sInx == 3); ass(a.eInx == 10);
   a.poke("a1"); ass(a.buf.Length == 15); ass(a.dbgString().Equals("[9] null (0), null (1), null (2), a (3), a1 (4), a1 (5), b (6), c (7), d (8), e (9), f (10), g (11), null (12), null (13), null (14)")); ass(a.sInx == 3); ass(a.eInx == 11);
   a = new Store<string>(new string[] { "a", "b", "c", "d", "e", "f", "g" }, 3, 4, new Sorter(0));
   a.copyItems(1, 1, -1); ass(a.buf.Length == 15); ass(a.dbgString().Equals("[8] null (0), null (1), null (2), a (3), a (4), b (5), c (6), d (7), e (8), f (9), g (10), null (11), null (12), null (13), null (14)"));
   a = new Store<string>(new string[] { "a", "b", "c", "d", "e", "f", "g" }, 3, 4, new Sorter(0));
   a.push(1, "a1"); ass(a.buf.Length == 15); ass(a.dbgString().Equals("[8] null (0), null (1), null (2), a (3), a1 (4), b (5), c (6), d (7), e (8), f (9), g (10), null (11), null (12), null (13), null (14)")); ass(a.sInx == 3); ass(a.eInx == 10);
   a.push(2, "a1"); ass(a.buf.Length == 15); ass(a.dbgString().Equals("[9] null (0), null (1), a (2), a1 (3), a1 (4), b (5), c (6), d (7), e (8), f (9), g (10), null (11), null (12), null (13), null (14)")); ass(a.sInx == 2); ass(a.eInx == 10);
   a = new Store<string>(new string[] { "a", "b", "c", "d", "e", "f", "g" }, 3, 4, new Sorter(0));
   a.push("a1"); ass(a.buf.Length == 15); ass(a.dbgString().Equals("[8] null (0), null (1), null (2), a (3), a1 (4), b (5), c (6), d (7), e (8), f (9), g (10), null (11), null (12), null (13), null (14)")); ass(a.sInx == 3); ass(a.eInx == 10);
   a.push("a1"); ass(a.buf.Length == 15); ass(a.dbgString().Equals("[9] null (0), null (1), a (2), a1 (3), a1 (4), b (5), c (6), d (7), e (8), f (9), g (10), null (11), null (12), null (13), null (14)")); ass(a.sInx == 2); ass(a.eInx == 10);
   a = new Store<string>(new string[] { "a", "b", "c", "d", "e", "f", "g" }, 3, 4, new Sorter(0)); a.push(-8, "!"); a.poke(9, "z");
   ass(a.sorted); ass(a.dbgString().Equals("[9] null (0), null (1), ! (2), a (3), b (4), c (5), d (6), e (7), f (8), g (9), z (10), null (11), null (12), null (13), null (14)"));
   a = new Store<string>(new string[] { "a", "b", "c", "d", "e", "f", "g" }, 3, 4, new Sorter(0)); a.push(0, "!"); a.poke(0, "z");
   ass(a.sorted); ass(a.dbgString().Equals("[9] null (0), null (1), ! (2), a (3), b (4), c (5), d (6), e (7), f (8), g (9), z (10), null (11), null (12), null (13), null (14)"));
   a = new Store<string>(3, 0, 4, new Sorter(0));
   a.push("2000"); ass(a.dbgString().Equals("[1] null (0), null (1), 2000 (2), null (3), null (4), null (5), null (6)"));
   a.push("1970"); ass(a.dbgString().Equals("[2] null (0), null (1), 1970 (2), 2000 (3), null (4), null (5), null (6)"));
   a.push("1980"); ass(a.dbgString().Equals("[3] null (0), 1970 (1), 1980 (2), 2000 (3), null (4), null (5), null (6)"));
   //System.out.println(a.dbgString());
  }
 
  private        int         normed    (int pos,                    bool trailingZero)                  { return (pos > len()) ? len() + 1: (pos < -len()) ? 0 : (trailingZero) ? (pos > 0) ? pos : len() + pos + 1 : (pos >= 0) ? pos : len() + pos + 1; }
  protected      void        copyItems (int bsPos, int bePos,               int delta)    // Block-sPos and Block-ePos are based on 1, may also be negative numbers if one needs to count from right to left
  {
   if (len() == 0) return;
   if (bsPos <= 0) bsPos = len() + bsPos + 1; if (bePos <= 0) bePos = len() + bePos + 1;
   int bsInx = inx4Pos(bsPos)               ; int beInx = inx4Pos(bePos);
   int sOccupy = sInx - (bsInx + delta)     ; int eOccupy = beInx + delta - eInx;  //how many reserved Items will be occupied by the shift at the start and/or at the end ?
   if (sOccupy < 0) sOccupy = 0             ; if (eOccupy < 0) eOccupy = 0;
   if ((sOccupy > sInx) || (eOccupy > buf.Length - eInx - 1))                      // there is not enough reserve space left for the shift operation, buf must be expanded
   {
    typ[] buf1 = new typ[len() + sOccupy + eOccupy + sRsv + eRsv];
    Array.Copy(buf, sInx, buf1, sRsv + sOccupy, len());
    int inxShift = sRsv + sOccupy - sInx;
    if (oim != null) for (int i = sInx; i <= eInx ; i++) { List<int> l = oim[buf[i]]; for (int j = 0; j <= l.Count; j++) l[i] = l[j] + inxShift; }
    sInx += inxShift; eInx += inxShift; bsInx += inxShift; beInx += inxShift;
    buf = buf1;
   }
   int bsInxNew = inx4Pos(bsPos) + delta;
   int beInxNew = inx4Pos(bePos) + delta;
   Array.Copy(buf, inx4Pos(bsPos), buf, bsInxNew, beInxNew - bsInxNew + 1);
   if (oim != null)
   {
    for (int i = bsInxNew; i <= beInxNew ; i++) { List<int> l = oim[(typ)buf[i]]; for (int j = 0; j <= l.Count; j++) l[i] = l[j] + delta; }
    for (int i = sInx - sOccupy; i < sInx ; i++) oim[(typ)buf[i]].Add(i);
    for (int i = eInx + 1; i <= eInx +  eOccupy ; i++) oim[(typ)buf[i]].Add(i);
   }
   sInx -= sOccupy; eInx += eOccupy;
  }

  private        int         peek      (int                                       inx)  { if (!sorted) throw new Exception("seek on unsorted Arry"); while ((inx < eInx) && (srt.compare((typ)(buf[inx + 1]), (typ)(buf[inx])) == 0)) inx++; return inx; }
  private        int         peek      (typ obj,                         int s, int e)  { int m = (int)(0.5 + (s + e) / 2.0); int sortorder = srt.compare(obj, (typ)(buf[m])); if (sortorder == 0) return pos4Inx(peek(m), false); if (s == e) return (sortorder < 0) ? pos4Inx(seek(m) - 1, true) : pos4Inx(peek(m), true); if (m == e) return peek(obj, s, s); return (sortorder > 0)? peek(obj, m, e) : peek(obj, s, m); }
  public         int         peek      (typ                                       obj)  { if (!sorted) throw new Exception("peek on unsorted Arry"); if (len() == 0) return -1; int s = inx4Pos(1); int e = inx4Pos(len()); if (e < 0) return 0; if (srt.compare(obj, (typ)(buf[e])) > 0) return pos4Inx(e, true); return peek(obj, s, e); }
  private        int         seek      (int                                       inx)  { if (!sorted) throw new Exception("peek on unsorted Arry"); while ((inx > sInx) && (srt.compare((typ)(buf[inx - 1]), (typ)(buf[inx])) == 0)) inx--; return inx; }
  private        int         seek      (typ obj,                         int s, int e)  { int m = (int)((s + e) / 2.0); int sortorder = srt.compare(obj, (typ)(buf[m])); if (sortorder == 0) return pos4Inx(seek(m), false);  if (s == e) return (sortorder < 0) ? pos4Inx(seek(m), true) : pos4Inx(peek(m) + 1, true); if (s == m) return seek(obj, e, e); return (sortorder > 0)? seek(obj, m, e) : seek(obj, s, m); }
  public         int         seek      (typ                                       obj)  { if (!sorted) throw new Exception("seek on unsorted Arry"); if (len() == 0) return 0; int s = inx4Pos(1); int e = inx4Pos(len()); if (e < 0) return len() + 1; if (srt.compare(obj, (typ)(buf[s])) < 0) return pos4Inx(s, true); return seek(obj, s, e); }
  
  private        void        chkSorted (typ v1,                                typ v2) 
  {
   if ((v1 == null) || (v2 == null)) return;
   if (oim == null)
   if (srt.compare(v1, v2) > 0)
    oim = new Dictionary<typ, List<int>>();
  } //return (!sorted) ? false : (v1 == null) ? false : (v2 == null) ? false : (srt.compare(v1, v2) > 0) ? false : true;
  private        void        chkSorted (typ v1,                        typ v2, typ v3) 
  {
   if ((v1 == null) || (v2 == null) || (v3 == null)) return;
   if (oim == null)
    if ((srt.compare(v1, v2) > 0) || (srt.compare(v2, v3) > 0))
     oim = new Dictionary<typ, List<int>>();
  } //return (!sorted) ? false : (v1 == null) ? false : (v2 == null) ? false : (v3 == null) ? false : (srt.compare(v1, v2) > 0) ? false : (srt.compare(v2, v3) > 0) ? false : true;
 
  private        void        init      (                                   Sorter srt)  { if (!selfTested)selfTest(); this.srt = srt; }
  private        void        init      (int sRsv, int size,  int eRsv,     Sorter srt)  { if (!selfTested) selfTest(); this.srt = srt; this.sRsv = Math.Max(2, sRsv); this.eRsv = Math.Max(2, eRsv); buf = new typ[this.sRsv + size + this.eRsv]; sInx = this.sRsv; eInx = buf.Length - this.eRsv - 1; }
  public                     Store (int sRsv, int size,  int eRsv,     Sorter srt)  { init(sRsv, size, eRsv, srt); }
  public                     Store (int size,                          Sorter srt)  { init(4, size, 4, srt);                                                                                                                    }
  public                     Store (typ[] buf, int sRsv, int eRsv,     Sorter srt)  { init(srt); this.buf = buf; eInx = buf.Length - 1; this.sRsv = sRsv; this.eRsv = eRsv;                                                     }
  public                     Store (typ[] buf,                         Sorter srt)  { init(srt); this.buf = buf; eInx = buf.Length - 1; this.sRsv = 4; this.eRsv = 4;                                                           }
  protected      int         inx4Pos   (int                                       pos)                  { if (pos <= 0) pos = len() + pos + 1; return sInx + pos - 1; }
  protected      int         pos4Inx   (int inx,                        bool backward)                  { return (backward) ? inx - sInx - len(): inx + 1 - sInx; }
  public         int         len       (                                             )                  { return (eInx - sInx + 1); }
 

  public         typ[]       array     (                                             )                  { if ((sInx == 0) && (eInx == buf.Length - 1)) return (typ[]) buf; typ[] ret = new typ[len()]; Array.Copy(buf, sInx, ret, 0, len()); return  ret ; }
  public         int         compareTo (Store<typ>                          other)  { return srt.compare(g(1), other.g(1)); }
  public         typ         g         (int                                       pos)                  { return (pos <= 0) ? (typ)(buf[len() + pos + sInx]) : (typ)(buf[pos - 1 + sInx]); }

  public         bool        s         (int pos,                              typ obj) 
  {
   if (pos <= 0) pos = len() + pos + 1;
   if (len() > 1) if (oim == null) if (pos == 1) chkSorted(obj, g(2)); else if (pos == len()) chkSorted(g(-2), obj); else chkSorted(g(pos - 1), obj, g(pos + 1));
   if (oim != null) //AttGeTr: this second condition is necessary because chkSorted() may instanciate the oim!
   { 
    if (oim.Count > 0) { oim[buf[pos - 1 + sInx]].Remove(pos); if (oim[buf[pos - 1 + sInx]].Count == 0) oim.Remove(buf[pos - 1 + sInx]); }
    //!!if (!oim.Contains(obj)) 
     oim.Add(obj, new List<int>());
    oim[obj].Add(pos);
   }
   buf[pos - 1 + sInx] = obj;
   return (oim == null);
  }

  public         typ         punch     (int pos                                      )
  {
   typ ret = g(pos);
   if (pos == 1) sInx++;
   else
    if (pos == len()) eInx--;
    else
     if ((len() - eInx - 1) > sInx)
      copyItems(1, pos - 1, - 1);
     else
      copyItems(pos + 1, len(), - 1);
   return ret;
  }

  public         bool        poke      (int pos,                              typ obj) 
  {
   if (len() == 0)
   {
    sInx = sRsv - 1;
    buf[sInx] = obj;
    return true;
   }
   pos = normed(pos, true);
   if (pos > len()) return push(-1, obj);
   copyItems(pos, len(), 1);
   return s(pos, obj);
  }

  public         bool        push      (int pos,                              typ obj) 
  {
   if (len() == 0)
   {
    sInx = sRsv - 1;
    buf[sInx] = obj;
    return true;
   }
   pos = normed(pos, false);
   if (pos == 0) return poke(1, obj);
   copyItems(1, pos, -1);
   return s(pos + 1, obj);
  }

  public         void        poke      (typ                                       obj)  
  {
   if (len() == 0)
   {
    sInx = sRsv - 1;
    buf[sInx] = obj;
    return;
   }
   poke(seek(obj), obj);
   /*
   int pos = seek(obj);
   copyItems(pos, len(), 1);
   if (pos <= 0)
    s(len() + pos, obj);
   else
    s(pos, obj);
   */
  }

  public         void        push      (typ                                       obj) 
  {
   if (len() == 0)
   {
    sInx = sRsv - 1;
    buf[sInx] = obj; return;
   }
   push(peek(obj), obj);
   /*
   int pos = peek(obj);
   copyItems(1, pos, -1);
   if (pos <= 0)
    s(pos, obj);
   else
    s(pos, obj);
    */
  }

  public IEnumerator<typ> GetEnumerator() { return new StoreEnumerator(this); }
  IEnumerator IEnumerable.GetEnumerator() { return GetEnumerator(); }
  public class StoreEnumerator : IEnumerator<typ>
  {
   private Store<typ>      coll;
   private typ                 current;
   private int                 index;
   public StoreEnumerator(Store<typ> coll) { Monitor.Enter(coll.buf.SyncRoot); this.index = -1; this.coll = coll; }
   public typ Current { get { return current; } }
   object IEnumerator.Current { get { return Current; } }
   public bool MoveNext() { if (++index >= coll.buf.Length) { return false; } else { current = coll.buf[index]; return true; } }
   public void Reset() { current = default(typ); index = 0; }
   public void Dispose() { try { current = default(typ); index = coll.buf.Length; } finally { Monitor.Exit(coll.buf.SyncRoot); } }
  }

 }



}






/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.xxdevplus.struct;

import org.xxdevplus.struct.Store;
import org.xxdevplus.struct.Sorter;


/**
 *
 * @author GeTr
 */
public class SmartStore<typ> extends Store<Store<typ>>
{
 //Copyright© 1996-2020 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties, You may use, modify, distribute this code under GNU public licence for NON-Commercial use as long as this notice remains unchanged.
 private static boolean selfTested  = false;  private static String ass(boolean expr) throws Exception { if (!expr) throw new Exception(ass(true) + " SelfTest Failure"); return "Vat"; }

 public                     SmartStore   (int sRsv, int size,  int eRsv,           Sorter srt) throws Exception { super(sRsv, size, eRsv, srt);      init(srt);}
 public                     SmartStore   (int size,                                Sorter srt) throws Exception { super(size, srt);                  init(srt);}
 public                     SmartStore   (Store<typ>[] buf, int sRsv, int eRsv,     Sorter srt) throws Exception { super(buf, sRsv, eRsv, srt);       init(srt);}
 public                     SmartStore   (Store<typ>[] buf,                         Sorter srt) throws Exception { super(buf, srt);                   init(srt);}

 private static void selfTest() throws Exception
 {
  selfTested = true;
  SmartStore<String> c;
  c = new SmartStore<String>(2, 3, 2, new Sorter(0));
  c.s(1, new Store<String>(new String[] {"a", "b", "c"}, 2, 2, new Sorter(0)));
  c.s(2, new Store<String>(new String[] {"d", "e", "f"}, 2, 2, new Sorter(0)));
  c.s(3, new Store<String>(new String[] {"g", "h", "i"}, 2, 2, new Sorter(0)));
  c.sorted = true;
  ass(c.Len() == 9); ass(c.G(1).equals("a")); ass(c.G(2).equals("b")); ass(c.G(3).equals("c")); ass(c.G(4).equals("d")); ass(c.G(-1).equals("i")); ass(c.G(-2).equals("h"));
  ass(c.Peek("!") == -10); ass(c.Peek("a") == 1); ass(c.Peek("a1") == -9); ass(c.Peek("b") == 2); ass(c.Peek("b1") == -8); ass(c.Peek("c") == 3); ass(c.Peek("c1") == -7); ass(c.Peek("d") == 4); ass(c.Peek("d1") == -6); ass(c.Peek("e") == 5); ass(c.Peek("e1") == -5); ass(c.Peek("f") == 6); ass(c.Peek("f1") == -4); ass(c.Peek("g") == 7); ass(c.Peek("g1") == -3); ass(c.Peek("h") == 8); ass(c.Peek("h1") == -2); ass(c.Peek("i") == 9); ass(c.Peek("i1") == -1); ass(c.Peek("j") == -1);
  ass(c.Seek("!") == -9); ass(c.Seek("a") == 1); ass(c.Seek("a1") == -8); ass(c.Seek("b") == 2); ass(c.Seek("b1") == -7); ass(c.Seek("c") == 3); ass(c.Seek("c1") == -6); ass(c.Seek("d") == 4); ass(c.Seek("d1") == -5); ass(c.Seek("e") == 5); ass(c.Seek("e1") == -4); ass(c.Seek("f") == 6); ass(c.Seek("f1") == -3); ass(c.Seek("g") == 7); ass(c.Seek("g1") == -2); ass(c.Seek("h") == 8); ass(c.Seek("h1") == -1); ass(c.Seek("i") == 9); ass(c.Seek("i1") == 0); ass(c.Seek("j") == 0);
  c = new SmartStore<String>(2, 8, 2, new Sorter(0));
  c.s(1, new Store<String>(new String[] {"a", "b", "c"}, 2, 2, new Sorter(0)));
  c.s(2, new Store<String>(new String[] {"c", "c", "c"}, 2, 2, new Sorter(0)));
  c.s(3, new Store<String>(new String[] {"c", "c", "c"}, 2, 2, new Sorter(0)));
  c.s(4, new Store<String>(new String[] {"c", "d", "f"}, 2, 2, new Sorter(0)));
  c.s(5, new Store<String>(new String[] {"f", "f", "f"}, 2, 2, new Sorter(0)));
  c.s(6, new Store<String>(new String[] {"f", "f", "f"}, 2, 2, new Sorter(0)));
  c.s(7, new Store<String>(new String[] {"f", "f", "g"}, 2, 2, new Sorter(0)));
  c.s(8, new Store<String>(new String[] {"g", "h", "i"}, 2, 2, new Sorter(0)));
  c.sorted = true;
  ass(c.Len() == 24); ass(c.G(1).equals("a")); ass(c.G(2).equals("b")); ass(c.G(3).equals("c")); ass(c.G(4).equals("c")); ass(c.G(-4).equals("g")); ass(c.G(-2).equals("h"));
  ass(c.Peek("!") == -25); ass(c.Peek("a") == 1); ass(c.Peek("a1") == -24); ass(c.Peek("b") == 2); ass(c.Peek("b1") == -23); ass(c.Peek("c") == 10); ass(c.Peek("c1") == -15); ass(c.Peek("d") == 11); ass(c.Peek("d1") == -14); ass(c.Peek("e") == -14); ass(c.Peek("e1") == -14); ass(c.Peek("f") == 20); ass(c.Peek("f1") == -5); ass(c.Peek("g") == 22); ass(c.Peek("g1") == -3); ass(c.Peek("h") == 23); ass(c.Peek("h1") == -2); ass(c.Peek("i") == 24); ass(c.Peek("i1") == -1); ass(c.Peek("j") == -1);
  ass(c.Seek("!") == -24); ass(c.Seek("a") == 1); ass(c.Seek("a1") == -23); ass(c.Seek("b") == 2); ass(c.Seek("b1") == -22); ass(c.Seek("c") == 3); ass(c.Seek("c1") == -14); ass(c.Seek("d") == 11); ass(c.Seek("d1") == -13); ass(c.Seek("e") == -13); ass(c.Seek("e1") == -13); ass(c.Seek("f") == 12); ass(c.Seek("f1") == -4); ass(c.Seek("g") == 21); ass(c.Seek("g1") == -2); ass(c.Seek("h") == 23); ass(c.Seek("h1") == -1); ass(c.Seek("i") == 24); ass(c.Seek("i1") == 0); ass(c.Seek("j") == 0);
  c.Poke(1, "!"); c.Push(1, "!1"); c.Push(-1, "z1"); c.Poke(-1, "z");
  ass(c.G(1).equals("!")); ass(c.G(2).equals("!1")); ass(c.G(-2).equals("z")); ass(c.G(-1).equals("z1"));
  c = new SmartStore<String>(2, 0, 2, new Sorter(0));
  c.Push("2000"); ass(c.DbgString().equals("[1] null (0), 2000 (1), null (2), null (3)"));
  c.Push("1970"); ass(c.DbgString().equals("[2] null (0), 1970 (1), 2000 (2), null (3)"));
  c.Push("1980");
  c.Punch(2);
  c.Punch(1);
  c.Punch(-1);
  ass(c.Len() == 0);
  c.Push("2000");
  c.Push("1970");
  c.Push("1980");
  c.Punch(3);
  c.Punch(2);
  c.Punch(1);
  ass(c.Len() == 0);
 }

 private void init(Sorter srt) throws Exception { if (!selfTested) selfTest(); this.srt = srt;}

 public         String      DbgString (                                             )                  { String ret = ""; for (int pos = 1; pos <= len(); pos ++) ret += "\r\n" + g(pos).dbgString(); return ret.substring(2); }

 private        int         len       (int                                       pos)                  { int ret = 0; int p = 0; while (p < pos - 1) ret += (g(++p)).len(); return ret; }
 public         int         Len       (                                             )                  { int ret = 0; int p = 0; while (p < len()) ret += ((Store<typ>)g(++p)).len(); return ret; }
 public         typ         G         (int                                       Pos)                  { if (Pos <= 0) Pos = Len() + Pos + 1; int lenSum = 0; int p = 0; while ((lenSum < Pos) && (p < len())) lenSum += (g(++p)).len(); return g(p).g(g(p).len() - lenSum + Pos); }
 public         void        S         (int Pos,                              typ obj) throws Exception { if (Pos <= 0) Pos = Len() + Pos + 1; int lenSum = 0; int p = 0; while ((lenSum < Pos) && (p < len())) lenSum += (g(++p)).len(); g(p).s(g(p).len() - lenSum + Pos, obj); sorted = sorted && (!g(p).sorted); }

 //public         typ         Punch       (int pos                                      )                  { typ ret = g(pos); if (pos == 1) sInx++; else if (pos == len())  eInx--;  else  if ((len() - eInx - 1) > sInx)  copyItems(1, pos - 1, - 1); else copyItems(pos + 1, len(), - 1); return ret; }

 private        int         Normed    (int Pos,                 boolean trailingZero)                  { return (Pos > Len()) ? Len() + 1: (Pos < -Len()) ? 0 : (trailingZero) ? (Pos > 0) ? Pos : Len() + Pos + 1 : (Pos >= 0) ? Pos : Len() + Pos + 1; }

 public         typ         Punch       (int                                       Pos) throws Exception
 {
  typ ret = null;
  Pos = Normed(Pos, false);
  if (Pos <= 0) throw new Exception("Value not found");
  int lenSum = 0; int p = 0; while ((lenSum < Pos) && (p < len())) lenSum += (g(++p)).len();
  int pos = Pos - lenSum + g(p).len();
  ret = g(p).punch(pos);
  if (g(p).len() == 0) punch(p);
  return ret;
 }

 public         typ         Punch       (typ                                       obj) throws Exception
 {
  int Pos = Peek(obj);
  if (Pos <= 0) throw new Exception("Value not found");
  return Punch(Pos);
 }

 public         int         Peek      (typ                                       obj) throws Exception
 {
  if (!sorted) throw new Exception("Peek on unsorted Vat");
  if ((Len() > 0) && (srt.compare(obj, G(Len())) > 0)) return -1;
  int pos = peek(new Store<typ> ((typ[])(new Object[] {obj}), srt));
  if (pos > 0) return len(pos) + g(pos).peek(obj);
  else
  {
   pos = len() + pos + 1;
   if (pos < 1) return -Len() - 1;
   int pk = g(pos).peek(obj);
   return (pk > 0) ? len(pos) + pk : len(pos) + g(pos).len() + pk - Len();
  }
 }

 public         int         Seek      (typ                                       obj) throws Exception
 {
  if (!sorted) throw new Exception("Seek on unsorted Vat");
  if ((Len() > 0) && (srt.compare(obj, G(1)) < 0)) return -Len();
  int pos = seek(new Store<typ> ((typ[])(new Object[] {obj}), srt));
  if (pos > 0) { while ((pos > 1) && (srt.compare(g(pos - 1).g(-1), obj) == 0)) pos--; return len(pos) + g(pos).seek(obj); }
  else
  {
   pos = len() + pos;
   int sk = g(pos).seek(obj);
   return (sk > 0) ? len(pos) + sk : len(pos) + g(pos).len() + sk - Len();
  }
 }

 public         void        Poke      (int Pos,                              typ obj) throws Exception
 {
  Pos = Normed(Pos, true);
  if (Pos > Len()) {Push (-1, obj); return;}
  int lenSum = 0; int p = 0; while ((lenSum < Pos) && (p < len())) lenSum += (g(++p)).len();
  int moved = 0;
  if (g(p).len() >= 2 * len())
  {
   moved = Math.max(1, (int)(0.33 * g(p).len()));
   if (p == len())
   if ((p == len()) || (g(p + 1).len() >= 2 * len())) { push(p, new Store<typ>(g(p).sRsv, 0, g(p).eRsv, g(p).srt)); p--;}
   for (int pp = 1; pp <= moved; pp++)
   {
    g(p + 1).poke(1, g(p).g(-pp));
    g(p).eInx--;
   }
   p++;
  }
  if (moved == 0) sorted = sorted && g(p).poke(g(p).len() - lenSum + Pos, obj); else Poke(Pos, obj);
 }

 public         void        Push      (int Pos,                              typ obj) throws Exception
 {
  Pos = Normed(Pos, false);
  if (Pos == 0) {Poke (1, obj); return;}
  int lenSum = 0; int p = 0; while ((lenSum < Pos) && (p < len())) lenSum += (g(++p)).len();
  int moved = 0;
  if (g(p).len() >= 2 * len())
  {
   moved = Math.max(1, (int)(0.33 * g(p).len()));
   if ((p == 1) || (g(p - 1).len() >= 2 * len())) { poke(p, new Store<typ>(g(p).sRsv, 0, g(p).eRsv, g(p).srt)); p++; }
   for (int pp = 1; pp <= moved; pp++)
   {
    g(p - 1).push(-1, g(p).g(pp));
    g(p).sInx++;
   }
   p++;
  }
  if (moved == 0) sorted = sorted && g(p).push(g(p).len() - lenSum + Pos, obj); else Push(Pos, obj);
 }

 public         void        Poke      (typ                                       obj) throws Exception
 {
  if (Len() == 0) { Store<typ> a = new Store<typ>(0, 0, 0, srt); a.poke(obj); poke(a); return; }
  Poke(Seek(obj), obj); // old implementation is wrong because no balances buffer splitting appears here !!!
  //int Pos = Seek(obj);
  //if (Pos <= 0) Pos = Len() + Pos + 1;
  //Poke(Pos, obj);
  //int lenSum = 0; int p = 0; while ((lenSum < Pos) && (p < len())) lenSum += (g(++p)).len();
  //g(p).poke(obj);
 }

 public         void        Push      (typ                                       obj) throws Exception
 {
  if (Len() == 0) { Store<typ> a = new Store<typ>(0, 0, 0, srt); a.push(obj); push(a); return; }
  Push(Peek(obj), obj);
  //int Pos = Peek(obj); // old implementation is wrong because no balances buffer splitting appears here !!!
  //if (Pos <= 0) Pos = Len() + Pos + 1;
  //int lenSum = 0; int p = 0; while ((lenSum < Pos) && (p < len())) lenSum += (g(++p)).len();
  //g(p).push(obj);
 }

 /*
 public         typ[]       Array     (                                             )                  { if ((sInx == 0) && (eInx == buf.length - 1)) return (typ[]) buf; Object[] ret = new Object[len()]; System.arraycopy(buf, sInx, ret, 0, len()); return (typ[]) ret ; }
  */







































}



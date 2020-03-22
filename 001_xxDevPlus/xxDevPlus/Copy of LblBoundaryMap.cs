using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using FlexBase;

namespace FlexBase
{
 
 public class LblBoundaryMap : InxObserver
 {
  //Copyright© 1996-2012 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties, You may use, modify, distribute this code as long as this notice remains unchanged.
  private static bool selfTested  = false;  private static string ass(bool expr) { if (!expr) throw new Exception(ass(true) + " SelfTest Failure"); return "LblBoundaryMap"; } private void init() { if (!selfTested) selfTest(); }
 
  public  bool  useReachNotify   = false;
  private long  sLabelMax        = 0;
  private long  eLabelMax        = 0;
  private KeyPile<long, long> store = new KeyPile<long, long>();

  // Definition: Labes and Indexes are positive int - Values - NOT positive long Values!!! (only 1 to 2147483647 are valid values for Labels and Indexes, 0 is neither a valid index nor is it a valid label!)
  //
  // For convenience all END-Labels and all END-Indexes are stores as negative int. 
  // All START-Labels and all START-Indexes are stored as positive int.
  // The InxLblMap may contain the following types of entries:
  // 1.) StartIndex and StartLabel e.g key = (17, 2), value = 17
  // 2.) EndIndex and EndLabel e.g key = (-33, -5), value = -33
  // 3.) Zero and StartLabel e.g key = (0, 4) value = 18
  // 4.) Zero and EndLabel e.g key = (0, -4) value = -40
  //
  // Thus every Index/label pair is unique.
  // The List may contain (33, 5)/33 and (33, 7)/33 and (33, 9)/33 so there may be several Labels fot the Index 33.
  // But there may be only 1 Index for a specific Label!
  // There may be several Label entries for the same Index but there may be only ONE Index for a specific Label!!!

  public void AddStart(long inx, long lbl) 
  {
   if (useReachNotify) return;
   inx = inx + 1;
   store.Add(((0L + (int)inx) << 32) | ((-2147483648L + (int)lbl) & 0xFFFFFFFF), inx);
   store.Add(((0L) << 32) | ((-2147483648L + (int)lbl) & 0xFFFFFFFF), inx); 
  }
  
  public void AddEnd(long inx, long lbl) 
  {
   if (useReachNotify) return;
   inx = inx + 1;
   store.Add(((0L + -(int)inx) << 32) | ((-2147483648L + -(int)lbl) & 0xFFFFFFFF), -inx);
   store.Add(((0L) << 32) | ((-2147483648L + -(int)lbl) & 0xFFFFFFFF), -inx); 
  }

  //public int sIndex  (int sLabel) { return useReachNotify ? Math.Abs(sLabel) :  store[((0L) << 32) | ((-2147483648L + sLabel)  & 0xFFFFFFFF)] - 1; }
  //public int eIndex(int eLabel)   { return useReachNotify ? Math.Abs(eLabel) : -store[((0L) << 32) | ((-2147483648L + -eLabel) & 0xFFFFFFFF)] - 1; }

  public long obdry(string sLabel) { int sLbl = Int32.Parse(sLabel); return useReachNotify ? Math.Abs((int)sLbl) :  store[((0L) << 32) | ((-2147483648L + (int)sLbl)  & 0xFFFFFFFF)] - 1; }
  public long cbdry(string eLabel) { int eLbl = Int32.Parse(eLabel); return useReachNotify ? Math.Abs((int)eLbl) : -store[((0L) << 32) | ((-2147483648L + -(int)eLbl) & 0xFFFFFFFF)] - 0; }


  public Pile<long> sLabels(long oBdry)
  {
   oBdry = oBdry + 1;
   Pile<long> ret = new Pile<long>();
   if (useReachNotify) { ret.Add(oBdry); return ret; }
   if (store.Len == 0) return ret;
   long s = 0;
   long e = store.Len - 1;
   long m = (s + e) / 2;
   long lookup = ((0L + (int)oBdry) << 32) | ((-2147483648L + 0) & 0xFFFFFFFF);
   while (e - s > 1) { if (store.SortedKeys.ElementAt((int)m) > lookup) e = m; else s = m; m = (s + e) / 2; }
   if ((m > 0) && ((int)(store.SortedKeys.ElementAt((int)m) >> 32) > oBdry)) m -= 1;
   if ((m < store.Len - 1) && ((int)(store.SortedKeys.ElementAt((int)m) >> 32) < oBdry)) m += 1;
   if ((int)(store.SortedKeys.ElementAt((int)m) >> 32) != oBdry) return ret;
   while ((m > 0) && ((int)(store.SortedKeys.ElementAt((int)m) >> 32) == oBdry)) m -= 1;
   if ((int)(store.SortedKeys.ElementAt((int)m) >> 32) < oBdry) m += 1;
   while ((m <= store.Len - 1) && ((int)(store.SortedKeys.ElementAt((int)m) >> 32) == oBdry)) { if ((store.SortedKeys.ElementAt((int)m) >> 32) != 0) ret.Add((int)((store.SortedKeys.ElementAt((int)m) & 0xFFFFFFFF) + 2147483648L)); m++; }
   return ret;
  }

  private List<NamedItem<long, long>> sInxLblFrom(long sIndex)
  {
   List<NamedItem<long, long>> ret = new List<NamedItem<long, long>>();
   long s = 0;
   long e = store.Len - 1;
   long m = (s + e) / 2;
   long lookup = ((0L + (int)sIndex) << 32) | ((-2147483648L + 0) & 0xFFFFFFFF);
   while (e - s > 1) { if (store.SortedKeys.ElementAt((int)m) > lookup) e = m; else s = m; m = (s + e) / 2; }
   if ((m > 0) && ((int)(store.SortedKeys.ElementAt((int)m) >> 32) > sIndex)) m -= 1;
   if ((m < store.Len - 1) && ((int)(store.SortedKeys.ElementAt((int)m) >> 32) < sIndex)) m += 1;
   while ((m > 0) && ((int)(store.SortedKeys.ElementAt((int)m) >> 32) == sIndex)) m -= 1;
   if ((int)(store.SortedKeys.ElementAt((int)m) >> 32) < sIndex) m += 1;
   while ((m <= store.Len - 1)) { if ((store.SortedKeys.ElementAt((int)m) >> 32) != 0) ret.Add(new NamedItem<long, long>(store.SortedKeys.ElementAt((int)m), store[store.SortedKeys[(int)m]])); m++; }
   return ret;
  }

  public void sIndexShift(long from, long amount)
  {
   if (useReachNotify) return;
   from = from + 1;
   List<NamedItem<long, long>> items = sInxLblFrom(from);
   foreach (NamedItem<long, long> item in items)
   {
    store.Del(item.Name);
    long inx;
    if (amount < 0) inx = Math.Max((int) (item.Name >> 32) + amount, from); else inx = (int) (item.Name >> 32) + amount;
    long lbl = (int) ((item.Name & 0xFFFFFFFF) + 2147483648L);
    store.Add(((0L + inx) << 32) | ((-2147483648L + (int)lbl) & 0xFFFFFFFF), inx);
    store[((0L) << 32) | ((-2147483648L + (int)lbl) & 0xFFFFFFFF)] = inx;
   }
  }

  public Pile<long> eLabels(long cBdry)
  {
   cBdry = cBdry + 1;
   Pile<long> ret = new Pile<long>();
   if (useReachNotify) { ret.Add(cBdry); return ret; }
   if (store.Len == 0) return ret;
   long s = 0;
   long e = store.Len - 1;
   long m = (s + e) / 2;
   long lookup = ((0L + -cBdry) << 32) | ((-2147483648L + 0) & 0xFFFFFFFF);
   while (e - s > 1) { if (store.SortedKeys.ElementAt((int)m) > lookup) e = m; else s = m; m = (s + e) / 2; }
   if ((m > 0) && ((int)(store.SortedKeys.ElementAt((int)m) >> 32) > -cBdry)) m -= 1;
   if ((m < store.Len - 1) && ((int)(store.SortedKeys.ElementAt((int)m) >> 32) < -cBdry)) m += 1;
   if ((int)(store.SortedKeys.ElementAt((int)m) >> 32) != -cBdry) return ret;
   while ((m < store.Len - 1) && ((int)(store.SortedKeys.ElementAt((int)m) >> 32) == -cBdry)) m += 1;
   if ((int)(store.SortedKeys.ElementAt((int)m) >> 32) > -cBdry) m -= 1;
   while ((m >= 0) && ((int)(store.SortedKeys.ElementAt((int)m) >> 32) == -cBdry)) { if ((store.SortedKeys.ElementAt((int)m) >> 32) != 0) ret.Add(-(int)((store.SortedKeys.ElementAt((int)m) & 0xFFFFFFFF) + 2147483648L)); m--; }
   return ret;
  }

  private List<NamedItem<long, long>> eInxLblFrom(long eIndex)
  {
   List<NamedItem<long, long>> ret = new List<NamedItem<long, long>>();
   long s = 0;
   long e = store.Len - 1;
   long m = (s + e) / 2;
   long lookup = ((0L + -(int)eIndex) << 32) | ((-2147483648L + 0) & 0xFFFFFFFF);
   while (e - s > 1) { if (store.SortedKeys.ElementAt((int)m) > lookup) e = m; else s = m; m = (s + e) / 2; }
   if ((m > 0) && ((int)(store.SortedKeys.ElementAt((int)m) >> 32) > -eIndex)) m -= 1;
   if ((m < store.Len - 1) && ((int)(store.SortedKeys.ElementAt((int)m) >> 32) < -eIndex)) m += 1;
   while ((m < store.Len - 1) && ((int)(store.SortedKeys.ElementAt((int)m) >> 32) == -eIndex)) m += 1;
   if ((int)(store.SortedKeys.ElementAt((int)m) >> 32) > -eIndex) m -= 1;
   while ((m >= 0)) { if ((store.SortedKeys.ElementAt((int)m) >> 32) != 0) ret.Add(new NamedItem<long, long>(store.ElementAt((int)m), store[store[(int)m]])); m--; }
   return ret;
  }

  public void eIndexShift(long from, long amount)
  {
   if (useReachNotify) return;
   from = from + 1;
   List<NamedItem<long, long>> items = eInxLblFrom(from);
   foreach (NamedItem<long, long> item in items)
   {
    store.Del(item.Name);
    long inx;
    if (amount < 0) inx = Math.Max(-(int) (item.Name >> 32) + amount, from - 1); else inx = -(int) (item.Name >> 32) + amount;
    long lbl = -(int) ((item.Name & 0xFFFFFFFF) + 2147483648L);
    store.Add(((0L + -inx) << 32) | ((-2147483648L + -(int)lbl) & 0xFFFFFFFF), -inx);
    store[((0L) << 32) | ((-2147483648L + -(int)lbl) & 0xFFFFFFFF)] = -inx;
   }
  }


  /*
  internal string dbgStore()
  {
   string ret = "";
   foreach (NamedItem<long, long> item in store)
   {
    long inx = (int) (item.Key >> 32);
    if (inx < 0) inx += 1; else if (inx > 0) inx -= 1;
    long ix = item.Value;
    if (ix < 0) ix += 1; else if (ix > 0) ix -= 1;
    long lbl = (int) ((item.Key & 0xFFFFFFFF) + 2147483648L);
    ret+= "\r\n[" + inx + "," + lbl + "]    " + ix;
   }
   return ret.Substring(2);
  }
  */

  private void selfTest()
  {
   selfTested = true;
   if (useReachNotify) return;
   string test = "hello world"; // will be extended to "hello new world"
   AddStart(1, ++sLabelMax);
   AddEnd(11, ++eLabelMax);
   ass(obdry("1") == 1);
   ass(cbdry("1") == 12);
   ass(sLabels(1).Len == 1);
   ass(eLabels(11).Len == 1);
   ass(sLabels(1)[1] == 1);
   ass(eLabels(11)[1] == 1);

   test = "hello new world";
   sIndexShift(7, 4);
   eIndexShift(7, 4);
   ass(obdry("1") == 1);
   ass(cbdry("1") == 16);
   ass(sLabels(1).Len == 1);
   ass(eLabels(15).Len == 1);
   ass(sLabels(1)[1] == 1);
   ass(eLabels(15)[1] == 1);

   AddStart(7, ++sLabelMax);
   AddEnd(9, ++eLabelMax);
   ass(obdry("1") == 1);
   ass(obdry("2") == 7);
   ass(cbdry("1") == 16);
   ass(cbdry("2") == 10);
   ass(sLabels(1).Len == 1);
   ass(sLabels(7).Len == 1);
   ass(eLabels(9).Len == 1);
   ass(eLabels(15).Len == 1);
   ass(sLabels(1)[1] == 1);
   ass(sLabels(7)[1] == 2);
   ass(eLabels(9)[1] == 2);
   ass(eLabels(15)[1] == 1);

   test = "he new world";
   sIndexShift(3, -3);
   eIndexShift(3, -3);
   ass(obdry("1") == 1);
   ass(obdry("2") == 4);
   ass(cbdry("1") == 13);
   ass(cbdry("2") == 7);
   ass(sLabels(1).Len == 1);
   ass(sLabels(4).Len == 1);
   ass(eLabels(6).Len == 1);
   ass(eLabels(12).Len == 1);
   ass(sLabels(1)[1] == 1);
   ass(sLabels(4)[1] == 2);
   ass(eLabels(6)[1] == 2);
   ass(eLabels(12)[1] == 1);

   test = "he world";
   sIndexShift(4, -4);
   eIndexShift(4, -4);
   ass(obdry("1") == 1);
   ass(obdry("2") == 4);
   ass(cbdry("1") == 9);
   ass(cbdry("2") == 4);
   ass(sLabels(1).Len == 1);
   ass(sLabels(4).Len == 1);
   ass(eLabels(3).Len == 1);
   ass(eLabels(8).Len == 1);
   ass(sLabels(1)[1] == 1);
   ass(sLabels(4)[1] == 2);
   ass(eLabels(3)[1] == 2);
   ass(eLabels(8)[1] == 1);

   store = new KeyPile<long, long>();
   sLabelMax = 0;
   eLabelMax = 0;
  }

  private void init(bool useReachNotify)
  {
   this.useReachNotify = useReachNotify;
   if (!selfTested) selfTest();
  }

  public LblBoundaryMap(bool useReachNotify)
  {
   init(useReachNotify);
  }

 }

}










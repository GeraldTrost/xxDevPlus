


//** @author  GeTr Copyright© 1996-2020 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties
//** @license You may use, modify, distribute this code under GNU public dual licence free for NON-Commercial use as long as this notice remains unchanged.
//** @comment Label Map used in Chain


using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using org_xxdevplus_utl;
using org_xxdevplus_struct;
using org_xxdevplus_chain;


namespace org_xxdevplus_chain
{

 public class LblBoundaryMap
 {
  private static bool selfTested  = false; private static string ass(bool expr) { if (!expr) throw new Exception(ass(true) + " SelfTest Failure"); return "LblBoundaryMap"; }
  private void init() { if (!selfTested) selfTest(); }
 
  public  bool                useReachNotify  = false;
  private long                sLabelMax       = 0;
  private long                eLabelMax       = 0;
  private KeyPile<long, int>  store           = new KeyPile<long, int>();

  // Definition: Labes and Boundaries are positive int - Values - NOT positive long Values!!! (only 1 to 2147483647 are valid values for Labels and Indexes, 0 is neither a valid index nor is it a valid label!)
  //   schema for strings:    bdy0  CHAR1  bry1   CHAR2  bry2   CHAR3  bdy3
  // For convenience all END-Labels and all END-Indexes are stores as negative int. 
  // All START-Labels and all START-Indexes are stored as positive int.
  // The InxLblMap may contain the following types of entries:
  // 1.) StartBdy and StartLabel e.g key = (  17,  2 )  , value =  17
  // 2.) EndBdy   and EndLabel   e.g key = ( -33, -5 )  , value = -33
  // 3.) Zero     and StartLabel e.g key = (   0,  4 )  , value =  18
  // 4.) Zero     and EndLabel   e.g key = (   0, -4 )  , value = -40
  //
  // Thus every Bdy/Label pair is unique.
  // The List may contain (33, 5)/33 and (33, 7)/33 and (33, 9)/33 so there may be several Labels fot the Index 33.
  // But there may be only 1 Index for a specific Label!
  // There may be several Label entries for the same Index but there may be only ONE Index for a specific Label!!!

  private long pack      ( int     inx, long lbl ) { return ((0L + inx) << 32) | ((-2147483648L + (int)lbl) & 0xFFFFFFFF); }

  private int  binSearch ( int    bdry, long lbl, int dtn) 
  {
   int s = 0; int e = store.Len - 1; int m = (s + e) / 2; long lookup = pack(bdry, lbl); while (e - s > 1) { if (store.kAsc[m + 1] > lookup) e = m; else s = m; m = (s + e) / 2; } m++;
   if ((m > 1) && ((store.kAsc[m] >> 32) > bdry)) m -= 1;
   if ((m < store.Len) && ((store.kAsc[m] >> 32) < bdry)) m += 1;
   //if ((store.SortedKeys[m] >> 32) != bdry) return ret;
   while ((m >             1) && ((store.kAsc[m] >> 32) == bdry)) m -= dtn;
   if ((store.kAsc[m] >> 32) * dtn < bdry * dtn) m += dtn;
   return m;
  }

  public  void AddStart  ( int    inx, long lbl ) { if (useReachNotify) return; inx = inx + 1; store.Add(pack( inx,  lbl),  inx); store.Add(pack(0,  lbl),  inx); }
  public  void AddEnd    ( int    inx, long lbl ) { if (useReachNotify) return; inx = inx + 1; store.Add(pack(-inx, -lbl), -inx); store.Add(pack(0, -lbl), -inx); }
  public  int  sbdry     ( string sLabel        ) { long sLbl = long.Parse(sLabel); return useReachNotify ? (int)Math.Abs(sLbl) :  store[pack(0,  sLbl)] - 1; }
  public  int  ebdry     ( string eLabel        ) { long eLbl = long.Parse(eLabel); return useReachNotify ? (int)Math.Abs(eLbl) : -store[pack(0, -eLbl)] - 0; }

  public Pile<long> Labels(int bdry, int dtn) //dtn = -1 means Ending Labels, dtn = 1 menas Starting Labels
  {
   bdry = (bdry + 1); Pile<long> ret = new Pile<long>(); if (useReachNotify) { ret.Add(bdry); return ret; } if (store.Len == 0) return ret;
   int m = binSearch(dtn * bdry, 0, dtn);
   while ((m >= 1) && (m <= store.Len) && ((int)(store.kAsc[m] >> 32) ==  dtn * bdry)) { if ((store.kAsc[m] >> 32) != 0) ret.Add(dtn * (int)((store.kAsc[m] & 0xFFFFFFFF) + 2147483648L)); m += dtn; }
   return ret;
  }

  private Pile<long> InxLblFrom(int bdry, int dtn)
  {
   Pile<long> ret = new Pile<long>();
   int m = binSearch(dtn * bdry, 0, dtn);
   while ((m >= 1) && (m <= store.Len)) { if ((store.kAsc[m] >> 32) != 0) ret.Add(store.kAsc[m]); m += dtn; }
   return ret;
  }

  public void IndexShift(int from, int amount, int dtn) //Direction dtn = 1 if we search Indexes of Starting Labels but dtn = -1 when we search for Indexes of Ending Labels
  {
   if (useReachNotify) return;
   from = from + 1;
   foreach (long key in InxLblFrom(from, dtn))
   {
    store.Del(key);
    int inx = 0;
    if (amount < 0) inx = Math.Max(dtn * (int)(key >> 32) + amount, from + (int)((dtn - 1) / 2)); else inx = dtn * (int)(key >> 32) + amount;
    long lbl = dtn * (int)((key & 0xFFFFFFFF) + 2147483648L);
    store.Add(pack(dtn * inx, dtn * lbl), dtn * inx);
    store[pack(0, dtn * lbl)] = dtn * inx;
   }
  }
  
  /*
  internal string dbgStore()
  {
   string ret = "";
   foreach (KeyValuePair<long, long> item in store)
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

   store = new KeyPile<long, int>();
   sLabelMax = 0;
   eLabelMax = 0;

   ass(store.Len == 0);
   AddStart(1, ++sLabelMax);
   ass(store.Len == 2);
   AddEnd(11, ++eLabelMax);
   ass(store.Len == 4);

   //ass(Labels(    0,  1).Len == 0);
   //ass(Labels(   -1,  1).Len == 0);
   //ass(Labels( 2177,  1).Len == 0);
   //ass(Labels(    0, -1).Len == 0);
   //ass(Labels(   -1, -1).Len == 0);
   //ass(Labels( 2177, -1).Len == 0);
   
   
   
   ass(sbdry("1") == 1);
   ass(ebdry("1") == 12);
   ass(Labels(1, 1).Len == 1);
   ass(Labels(11, -1).Len == 1);
   ass(Labels(1, 1)[1] == 1);
   ass(Labels(11, -1)[1] == 1);

   test = "hello new world";
   IndexShift(7, 4, 1);
   IndexShift(7, 4, -1);
   ass(sbdry("1") == 1);
   ass(ebdry("1") == 16);
   ass(Labels(1, 1).Len == 1);
   ass(Labels(15, -1).Len == 1);
   ass(Labels(1, 1)[1] == 1);
   ass(Labels(15, -1)[1] == 1);

   AddStart(7, ++sLabelMax);
   AddEnd(9, ++eLabelMax);
   ass(sbdry("1") == 1);
   ass(sbdry("2") == 7);
   ass(ebdry("1") == 16);
   ass(ebdry("2") == 10);
   ass(Labels(1, 1).Len == 1);
   ass(Labels(7, 1).Len == 1);
   ass(Labels(9, -1).Len == 1);
   ass(Labels(15, -1).Len == 1);
   ass(Labels(1, 1)[1] == 1);
   ass(Labels(7, 1)[1] == 2);
   ass(Labels(9, -1)[1] == 2);
   ass(Labels(15, -1)[1] == 1);

   test = "he new world";
   IndexShift(3, -3, 1);
   IndexShift(3, -3, -1);
   ass(sbdry("1") == 1);
   ass(sbdry("2") == 4);
   ass(ebdry("1") == 13);
   ass(ebdry("2") == 7);
   ass(Labels(1, 1).Len == 1);
   ass(Labels(4, 1).Len == 1);
   ass(Labels(6, -1).Len == 1);
   ass(Labels(12, -1).Len == 1);
   ass(Labels(1, 1)[1] == 1);
   ass(Labels(4, 1)[1] == 2);
   ass(Labels(6, -1)[1] == 2);
   ass(Labels(12, -1)[1] == 1);

   test = "he world";
   IndexShift(4, -4, 1);
   IndexShift(4, -4, -1);
   ass(sbdry("1") == 1);
   ass(sbdry("2") == 4);
   ass(ebdry("1") == 9);
   ass(ebdry("2") == 4);
   ass(Labels(1, 1).Len == 1);
   ass(Labels(4, 1).Len == 1);
   ass(Labels(3, -1).Len == 1);
   ass(Labels(8, -1).Len == 1);
   ass(Labels(1, 1)[1] == 1);
   ass(Labels(4, 1)[1] == 2);
   ass(Labels(3, -1)[1] == 2);
   ass(Labels(8, -1)[1] == 1);

   store = new KeyPile<long, int>();
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










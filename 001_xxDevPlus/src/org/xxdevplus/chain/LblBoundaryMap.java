

//** @author  GeTr Copyright© 1996-2020 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties
//** @license You may use, modify, distribute this code under GNU public dual licence free for NON-Commercial use as long as this notice remains unchanged.
//** @comment Label Map used in Chain



package org.xxdevplus.chain;


import org.xxdevplus.struct.KeyPile;
import org.xxdevplus.struct.Pile;

 public class LblBoundaryMap
 {
  private static boolean selfTested  = false; private static String ass(boolean expr) throws Exception { if (!expr) throw new Exception(ass(true) + " SelfTest Failure"); return "LblBoundaryMap"; } 
  private void init() throws Exception { if (!selfTested) selfTest(); }
 
  public  boolean                 useReachNotify  = false;
  private long                    sLabelMax       = 0;
  private long                    eLabelMax       = 0;
  private KeyPile<Long, Integer>  store           = new KeyPile<Long, Integer>();

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

  private long pack      ( int     inx, long lbl ) { return ((0L + inx) << 32) | ((-2147483648L + lbl) - 0xFFFFFFFF00000000L); }

  private int  binSearch ( int    bdry, long lbl, int dtn) throws Exception 
  {
   int s = 0; int e = store.Len() - 1; int m = (s + e) / 2; long lookup = pack(bdry, lbl); while (e - s > 1) { if (store.kAsc().g(m + 1) > lookup) e = m; else s = m; m = (s + e) / 2; } m++;
   if ((m > 1) && ((store.kAsc().g(m) >> 32) > bdry)) m -= 1;
   if ((m < store.Len()) && ((store.kAsc().g(m) >> 32) < bdry)) m += 1;
   //if ((store.SortedKeys[m] >> 32) != bdry) return ret;
   while ((m >             1) && ((store.kAsc().g(m) >> 32) == bdry)) m -= dtn;
   if ((store.kAsc().g(m) >> 32) * dtn < bdry * dtn) m += dtn;
   return m;
  }

  public  void AddStart  ( int    inx, long lbl ) throws Exception 
  { 
   if (useReachNotify) return; 
   inx = inx + 1; 
   store.Add(pack( inx,  lbl),  inx); 
   store.Add(pack(0,  lbl),  inx); 
  }
  
  public  void AddEnd    ( int    inx, long lbl ) throws Exception { if (useReachNotify) return; inx = inx + 1; store.Add(pack(-inx, -lbl), -inx); store.Add(pack(0, -lbl), -inx); }

  
  public  int  sbdry     ( String sLabel        ) throws Exception                  { long sLbl = Long.parseLong(sLabel); return useReachNotify ? (int)Math.abs(sLbl) : store.g(pack(0,  sLbl)) - 1; }
  public  int  ebdry     ( String eLabel        ) throws Exception                  { long eLbl = Long.parseLong(eLabel); return useReachNotify ? (int)Math.abs(eLbl) : -store.g(pack(0, -eLbl)) - 0; }

  public Pile<Long> Labels(int bdry, int dtn) throws Exception //dtn = -1 means Ending Labels, dtn = 1 menas Starting Labels
  {
   bdry = (bdry + 1); Pile<Long> ret = new Pile<Long>(); if (useReachNotify) { ret.Add(bdry + 0L); return ret; } if (store.Len() == 0) return ret;
   int m = binSearch(dtn * bdry, 0, dtn);
   while ((m >= 1) && (m <= store.Len()) && ((int)(store.kAsc().g(m) >> 32) ==  dtn * bdry)) { if ((store.kAsc().g(m) >> 32) != 0) ret.Add(0L + dtn * (int)((store.kAsc().g(m) & 0xFFFFFFFF) + 2147483648L)); m += dtn; }
   return ret;
  }

  private Pile<Long> InxLblFrom(int bdry, int dtn) throws Exception
  {
   Pile<Long> ret = new Pile<Long>();
   int m = binSearch(dtn * bdry, 0, dtn);
   while ((m >= 1) && (m <= store.Len())) { if ((store.kAsc().g(m) >> 32) != 0) ret.Add(0L + store.kAsc().g(m)); m += dtn; }
   return ret;
  }

  public void IndexShift(int from, int amount, int dtn) throws Exception //Direction dtn = 1 if we search Indexes of Starting Labels but dtn = -1 when we search for Indexes of Ending Labels
  {
   if (useReachNotify) return;
   from = from + 1;
   for (long key : InxLblFrom(from, dtn))
   {
    store.Del(key);
    int inx = 0;
    if (amount < 0) inx = Math.max(dtn * (int)(key >> 32) + amount, from + (int)((dtn - 1) / 2)); else inx = dtn * (int)(key >> 32) + amount;
    long lbl = dtn * (int)((key & 0xFFFFFFFF) + 2147483648L);
    store.Add(pack(dtn * inx, dtn * lbl), dtn * inx);
    store.s(dtn * inx, pack(0, dtn * lbl));
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
  
  private void selfTest() throws Exception
  {
   selfTested = true;
   if (useReachNotify) return;
   String test = "hello world"; // will be extended to "hello new world"

   store = new KeyPile<Long, Integer>();
   sLabelMax = 0;
   eLabelMax = 0;
   ass(store.Len() == 0);
   AddStart(1, ++sLabelMax);
   ass(store.Len() == 2);
   AddEnd(11, ++eLabelMax);
   ass(store.Len() == 4);

   //ass(Labels(    0,  1).Len() == 0);
   //ass(Labels(   -1,  1).Len() == 0);
   //ass(Labels( 2177,  1).Len() == 0);
   //ass(Labels(    0, -1).Len() == 0);
   //ass(Labels(   -1, -1).Len() == 0);
   //ass(Labels( 2177, -1).Len() == 0);
   
   ass(sbdry("1") == 1);
   ass(ebdry("1") == 12);
   ass(Labels(1, 1).Len() == 1);
   ass(Labels(11, -1).Len() == 1);
   ass(Labels(1, 1).g(1) == 1);
   ass(Labels(11, -1).g(1) == 1);

   test = "hello new world";
   IndexShift(7, 4, 1);
   IndexShift(7, 4, -1);
   ass(sbdry("1") == 1);
   ass(ebdry("1") == 16);
   ass(Labels(1, 1).Len() == 1);
   ass(Labels(15, -1).Len() == 1);
   ass(Labels(1, 1).g(1) == 1);
   ass(Labels(15, -1).g(1) == 1);

   AddStart(7, ++sLabelMax);
   AddEnd(9, ++eLabelMax);
   ass(sbdry("1") == 1);
   ass(sbdry("2") == 7);
   ass(ebdry("1") == 16);
   ass(ebdry("2") == 10);
   ass(Labels(1, 1).Len() == 1);
   ass(Labels(7, 1).Len() == 1);
   ass(Labels(9, -1).Len() == 1);
   ass(Labels(15, -1).Len() == 1);
   ass(Labels(1, 1).g(1) == 1);
   ass(Labels(7, 1).g(1) == 2);
   ass(Labels(9, -1).g(1) == 2);
   ass(Labels(15, -1).g(1) == 1);

   test = "he new world";
   IndexShift(3, -3, 1);
   IndexShift(3, -3, -1);
   ass(sbdry("1") == 1);
   ass(sbdry("2") == 4);
   ass(ebdry("1") == 13);
   ass(ebdry("2") == 7);
   ass(Labels(1, 1).Len() == 1);
   ass(Labels(4, 1).Len() == 1);
   ass(Labels(6, -1).Len() == 1);
   ass(Labels(12, -1).Len() == 1);
   ass(Labels(1, 1).g(1) == 1);
   ass(Labels(4, 1).g(1) == 2);
   ass(Labels(6, -1).g(1) == 2);
   ass(Labels(12, -1).g(1) == 1);

   test = "he world";
   IndexShift(4, -4, 1);
   IndexShift(4, -4, -1);
   ass(sbdry("1") == 1);
   ass(sbdry("2") == 4);
   ass(ebdry("1") == 9);
   ass(ebdry("2") == 4);
   ass(Labels(1, 1).Len() == 1);
   ass(Labels(4, 1).Len() == 1);
   ass(Labels(3, -1).Len() == 1);
   ass(Labels(8, -1).Len() == 1);
   ass(Labels(1, 1).g(1) == 1);
   ass(Labels(4, 1).g(1) == 2);
   ass(Labels(3, -1).g(1) == 2);
   ass(Labels(8, -1).g(1) == 1);

   store = new KeyPile<Long, Integer>();
   sLabelMax = 0;
   eLabelMax = 0;
  }

  private void init(boolean useReachNotify) throws Exception
  {
   this.useReachNotify = useReachNotify;
   if (!selfTested) selfTest();
  }

  public LblBoundaryMap(boolean useReachNotify) throws Exception
  {
   init(useReachNotify);
  }

 }


/*
 public class LblBoundaryMap
 {
  public boolean useReachNotify = false;
  static boolean passed = false;
  private int sLabelMax = 0;
  private int eLabelMax = 0;
  private Map<Long, Integer> store = new TreeMap<Long, Integer>();
  Object[] keys = new Object[0];
  //private Map<Long, Integer> store = new HashMap<Long, Integer>();
  //private TreeSet<Long, Integer> store = new TreeSet<Long, Integer>();

  // Definition: Labes and Indexes are positive int - Values - NOT positive long Values!!! (only 1 to 2147483647 are valid values for Labels and Indexes, 0 is neither a valid index nor is it a valid label!)
  //
  // For convenience all END-Labels and all END-Indexes are stores as negative int.
  // All START-Labels and all START-Indexes are stored as positive int.
  // The LblBoundaryMap may contain the following types of entries:
  // 1.) StartIndex and StartLabel e.g key = (17, 2), value = 17
  // 2.) EndIndex and EndLabel e.g key = (-33, -5), value = -33
  // 3.) Zero and StartLabel e.g key = (0, 4) value = 18
  // 4.) Zero and EndLabel e.g key = (0, -4) value = -40
  //
  // Thus every Index/label pair is unique.
  // The List may contain (33, 5)/33 and (33, 7)/33 and (33, 9)/33 so there may be several Labels fot the Index 33.
  // But there may be only 1 Index for a specific Label!
  // There may be several Label entries for the same Index but there may be only ONE Index for a specific Label!!!


  private int  inx  (long inxlbl)         { return (int)(inxlbl >> 32);}
  private int  lbl  (long inxlbl)         { return (int)(((inxlbl + 2147483648L) << 32) >> 32); }

  private long pack (int inx, int lbl) throws Exception
  {
   long ret = ((0L + inx) << 32) | ((-2147483648L + lbl) - 0xFFFFFFFF00000000L);
   if (inx(ret) != inx) throw new Exception("int pack error");
   if (lbl(ret) != lbl) throw new Exception("int pack error");
   return ret;
  }

  public void    AddStart   (int inx, int lbl) throws Exception
  {
   if (useReachNotify) return;
   inx = inx + 1;
   store.put( pack(inx, lbl), inx);
   store.put( pack(0  , lbl), inx);
   keys = store.keySet().toArray();
  }

  public void    AddEnd     (int inx, int lbl) throws Exception
  {
   if (useReachNotify) return;
   inx = inx + 1;
   store.put( pack(-inx, -lbl) , -inx);
   store.put( pack(0   , -lbl) , -inx);
   keys = store.keySet().toArray();
}

  //public int   sIndex     (int sLabel) throws Exception           { return useReachNotify ? Math.abs(sLabel) :  store.get(pack(0, sLabel))  - 1; }
  //public int   eIndex     (int eLabel) throws Exception           { return useReachNotify ? Math.abs(eLabel) : -store.get(pack(0, -eLabel)) - 1; }
  public int     obdry      (String sLabel) throws Exception        { int sLbl = Integer.parseInt(sLabel); return useReachNotify ? Math.abs(sLbl) :  store.get(pack(0, sLbl))  - 1; }
  public int     cbdry      (String eLabel) throws Exception        { int eLbl = Integer.parseInt(eLabel); return useReachNotify ? Math.abs(eLbl) : -store.get(pack(0, -eLbl)) - 0; }

  public Pack<Integer> sLabels(int oBdry) throws Exception
  {
   oBdry = oBdry + 1;
   Pack<Integer> ret = new Pack<Integer>();
   if (useReachNotify) { ret.Add(oBdry); return ret; }
   if (store.size() == 0) return ret;
   int s = 0;
   int e = store.size() - 1;
   int m = (s + e) / 2;
   long lookup = pack(oBdry, 0);
   //Object[] stor = store.entrySet().toArray();
   //Object[] keys = store.keySet().toArray();
   while (e - s > 1) { if ((Long)keys[m] > lookup) e = m; else s = m; m = (s + e) / 2; }
   if ((m > 0) && (inx((Long) keys[m]) > oBdry)) m -= 1;
   if ((m < keys.length - 1) && (inx(((Long) keys[m])) < oBdry)) m += 1;
   if (inx((Long) keys[m]) != oBdry) return ret;
   while ((m > 0) && (inx((Long) keys[m]) == oBdry)) m -= 1;
   if (inx((Long) keys[m]) < oBdry) m += 1;
   while ((m <= keys.length - 1) && (inx((Long) keys[m]) == oBdry)) { if ((Long) keys[m] != 0) ret.Add(lbl((Long) keys[m])); m++; }
   return ret;
  }

  private List<Map.Entry<Long, Integer>> sInxLblFrom(int sIndex) throws Exception
  {
   List<Map.Entry<Long, Integer>> ret = new ArrayList<Map.Entry<Long, Integer>>();
   int s = 0;
   int e = store.size() - 1;
   int m = (s + e) / 2;
   long lookup = pack(sIndex, 0);
   Object[] stor = store.entrySet().toArray();
   while (e - s > 1) { if (((Map.Entry<Long, Integer>) stor[m]).getKey() > lookup) e = m; else s = m; m = (s + e) / 2; }
   if ((m > 0) && (inx(((Map.Entry<Long, Integer>) stor[m]).getKey()) > sIndex)) m -= 1;
   if ((m < stor.length - 1) && (inx(((Map.Entry<Long, Integer>) stor[m]).getKey()) < sIndex)) m += 1;
   while ((m > 0) && (inx(((Map.Entry<Long, Integer>) stor[m]).getKey()) == sIndex)) m -= 1;
   if (inx(((Map.Entry<Long, Integer>) stor[m]).getKey()) < sIndex) m += 1;
   while ((m <= stor.length - 1)) { if (((Map.Entry<Long, Integer>) stor[m]).getKey() != 0) ret.add(((Map.Entry<Long, Integer>) stor[m])); m++; }
   return ret;
  }

  public void sIndexShift(int from, int amount) throws Exception
  {
   if (useReachNotify) return;
   from = from + 1;
   List<Map.Entry<Long, Integer>> items = sInxLblFrom(from);
   for (Map.Entry<Long, Integer> item : items)
   {
    long itemKey = item.getKey();
    store.remove(itemKey);
    int inx;
    if (amount < 0) inx = Math.max(inx(itemKey) + amount, from); else inx = inx(itemKey) + amount;
    int lbl = lbl(itemKey);
    store.put(pack(inx, lbl), inx);
    store.remove(pack(0, lbl));
    store.put(pack(0, lbl), inx);
    keys = store.keySet().toArray();
   }
  }

  public Pack<Integer> eLabels(int cBdry) throws Exception
  {
   cBdry = cBdry + 1;
   Pack<Integer> ret = new Pack<Integer>();
   if (useReachNotify) { ret.Add(cBdry); return ret; }
   if (store.size() == 0) return ret;
   int s = 0;
   int e = store.size() - 1;
   int m = (s + e) / 2;
   long lookup = pack(-cBdry, 0);
   //Object[] keys = store.keySet().toArray();
   while (e - s > 1) { if (((Long) keys[m]) > lookup) e = m; else s = m; m = (s + e) / 2; }
   if ((m > 0) && (inx((Long) keys[m]) > -cBdry)) m -= 1;
   if ((m < keys.length - 1) && (inx((Long) keys[m]) < -cBdry)) m += 1;
   if (inx((Long) keys[m]) != -cBdry) return ret;
   while ((m < keys.length - 1) && (inx((Long) keys[m]) == -cBdry)) m += 1;
   if (inx((Long) keys[m]) > -cBdry) m -= 1;
   while ((m >= 0) && (inx((Long) keys[m]) == -cBdry)) { if ((Long) keys[m] != 0) ret.Add ( -lbl((Long) keys[m])); m--; }
   return ret;
  }

  private List<Map.Entry<Long, Integer>> eInxLblFrom(int eIndex) throws Exception
  {
   List<Map.Entry<Long, Integer>> ret = new ArrayList<Map.Entry<Long, Integer>> ();
   int s = 0;
   int e = store.size() - 1;
   int m = (s + e) / 2;
   long lookup = pack(-eIndex, 0);
   Object[] stor = store.entrySet().toArray();
   while (e - s > 1) { if (((Map.Entry<Long, Integer>) stor[m]).getKey() > lookup) e = m; else s = m; m = (s + e) / 2; }
   if ((m > 0) && (inx(((Map.Entry<Long, Integer>) stor[m]).getKey()) > -eIndex)) m -= 1;
   if ((m < stor.length - 1) && (inx(((Map.Entry<Long, Integer>) stor[m]).getKey()) < -eIndex)) m += 1;
   while ((m < stor.length - 1) && (inx(((Map.Entry<Long, Integer>) stor[m]).getKey()) == -eIndex)) m += 1;
   if (inx(((Map.Entry<Long, Integer>) stor[m]).getKey()) > -eIndex) m -= 1;
   while ((m >= 0)) { if (((Map.Entry<Long, Integer>) stor[m]).getKey() != 0) ret.add(((Map.Entry<Long, Integer>) stor[m])); m--; }
   return ret;
  }

  public void eIndexShift(int from, int amount) throws Exception
  {
   if (useReachNotify) return;
   from = from + 1;
   List<Map.Entry<Long, Integer>> items = eInxLblFrom(from);
   for (Map.Entry<Long, Integer> item : items)
   {
    long itemKey = item.getKey();
    store.remove(itemKey);
    int inx;
    if (amount < 0) inx = Math.max(-inx(itemKey) + amount, from - 1); else inx = -inx(itemKey) + amount;
    int lbl = -lbl(itemKey);
    store.put(pack(-inx, -lbl), -inx);
    store.remove(pack(0, -lbl));
    store.put(pack(0, -lbl), -inx);
    keys = store.keySet().toArray();
   }
  }


  protected String dbgStore()
  {
   String ret = "";
   for (Map.Entry<Long, Integer> item : store.entrySet())
   {
    int inx = inx(item.getKey());
    if (inx < 0) inx += 1; else if (inx > 0) inx -= 1;
    int ix = item.getValue();
    if (ix < 0) ix += 1; else if (ix > 0) ix -= 1;
    int lbl = lbl(item.getKey());
    ret+= "\r\n[" + inx + "," + lbl + "]    " + ix;
   }
   return ret.substring(2);
  }



  private void ass(boolean expr) throws Exception { if (!expr) throw new Exception("InxLblMap SelfText Failure"); }

  private void selftest() throws Exception
  {
   if (useReachNotify) return;

   String test = "hello world"; // will be extended to "hello new world"
   AddStart(1, ++sLabelMax);
   AddEnd(11, ++eLabelMax);
   ass(obdry("1") == 1);
   ass(cbdry("1") == 12);
   ass(sLabels(1).len() == 1);
   ass(eLabels(11).len() == 1);
   ass(sLabels(1).get(1) == 1);
   ass(eLabels(11).get(1) == 1);

   test = "hello new world";
   sIndexShift(7, 4);
   eIndexShift(7, 4);
   ass(obdry("1") == 1);
   ass(cbdry("1") == 16);
   ass(sLabels(1).len() == 1);
   ass(eLabels(15).len() == 1);
   ass(sLabels(1).get(1) == 1);
   ass(eLabels(15).get(1) == 1);

   AddStart(7, ++sLabelMax);
   AddEnd(9, ++eLabelMax);
   ass(obdry("1") == 1);
   ass(obdry("2") == 7);
   ass(cbdry("1") == 16);
   ass(cbdry("2") == 10);
   ass(sLabels(1).len() == 1);
   ass(sLabels(7).len() == 1);
   ass(eLabels(9).len() == 1);
   ass(eLabels(15).len() == 1);
   ass(sLabels(1).get(1) == 1);
   ass(sLabels(7).get(1) == 2);
   ass(eLabels(9).get(1) == 2);
   ass(eLabels(15).get(1) == 1);

   test = "he new world";
   sIndexShift(3, -3);
   eIndexShift(3, -3);
   ass(obdry("1") == 1);
   ass(obdry("2") == 4);
   ass(cbdry("1") == 13);
   ass(cbdry("2") == 7);
   ass(sLabels(1).len() == 1);
   ass(sLabels(4).len() == 1);
   ass(eLabels(6).len() == 1);
   ass(eLabels(12).len() == 1);
   ass(sLabels(1).get(1) == 1);
   ass(sLabels(4).get(1) == 2);
   ass(eLabels(6).get(1) == 2);
   ass(eLabels(12).get(1) == 1);

   test = "he world";
   sIndexShift(4, -4);
   eIndexShift(4, -4);
   ass(obdry("1") == 1);
   ass(obdry("2") == 4);
   ass(cbdry("1") == 9);
   ass(cbdry("2") == 4);
   ass(sLabels(1).len() == 1);
   ass(sLabels(4).len() == 1);
   ass(eLabels(3).len() == 1);
   ass(eLabels(8).len() == 1);
   ass(sLabels(1).get(1) == 1);
   ass(sLabels(4).get(1) == 2);
   ass(eLabels(3).get(1) == 2);
   ass(eLabels(8).get(1) == 1);

   passed = true;
   store = new TreeMap<Long, Integer>();
   sLabelMax = 0;
   eLabelMax = 0;

  }

  private void init(boolean useReachNotify) throws Exception
  {
   this.useReachNotify = useReachNotify;
   if (!passed) selftest();
  }


  public LblBoundaryMap(boolean useReachNotify) throws Exception
  {
   init(useReachNotify);
  }

 }
 */








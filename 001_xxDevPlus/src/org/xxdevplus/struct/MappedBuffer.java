

//** @author  GeTr Copyright© 1996-2020 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties
//** @license You may use, modify, distribute this code under GNU public dual licence free for NON-Commercial use as long as this notice remains unchanged.
//** @comment Mapped Buffer used in Chain

package org.xxdevplus.struct;

import org.xxdevplus.chain.LblBoundaryMap;
import org.xxdevplus.chain.InxObserver;

 public class MappedBuffer
 {
  private static boolean selfTested  = false;  private static String ass(boolean expr) throws Exception { if (!expr) throw new Exception(ass(true) + " SelfTest Failure"); return "MappedBuffer"; } private static void selfTest() { selfTested = true; } private void init() { if (!selfTested) selfTest(); }

  private                Pile<InxObserver> listeners      = new Pile<InxObserver>();
  private                     CharSequence sbuf           = null;
  private                     CharSequence ibuf           = null; // only use this via buf() !!! might be null !!! internal lowecase Buffer for case-insensitive parsing
  public    KeyPile<String, Pile<Integer>> ftWinx         = null;

  public         CharSequence        buf(boolean cSns)   throws Exception   // insensitive Buffer, named after iLike in Postgres
  {
   if (cSns) return sbuf;
   if (ibuf != null) return ibuf; 
   ibuf = ((String)sbuf).toLowerCase();
   if (sbuf.length() != ibuf.length()) 
   {
    StringBuilder ibuf = new StringBuilder(sbuf.length());
    for (int i = 1; i <= sbuf.length(); i++) if (Character.isUpperCase(sbuf.charAt(i - 1))) ibuf.append(Character.toLowerCase(sbuf.charAt(i - 1))); else ibuf.append(sbuf.charAt(i - 1));
    this.ibuf = sbuf.toString();
   }
   if (ibuf.length() != sbuf.length()) throw new Exception("internal Error in MappedBuffer.iBuf() : lowercase String differs in Length!");
   return ibuf;
  }
  
  public          char                charAt    (int pos, boolean cSns) throws Exception  {return buf(cSns).charAt(pos - 1); }
  public          int                 Len()                                               {return sbuf.length(); }
  private         LblBoundaryMap      lbMap;
  private         long                sLblMax;
  private         long                eLblMax;
  public          long                sLabel    (int               bdry) throws Exception {if (lbMap.useReachNotify) return bdry; Pile<Long> l = lbMap.Labels(bdry,  1); if (l.Len() > 0) return l.g(1); lbMap.AddStart  (bdry, ++sLblMax); return sLblMax; }
  public          long                eLabel    (int               bdry) throws Exception {if (lbMap.useReachNotify) return bdry; Pile<Long> l = lbMap.Labels(bdry, -1); if (l.Len() > 0) return l.g(1); lbMap.AddEnd    (bdry, ++eLblMax); return eLblMax; }

  
  public int sbdry(String lbl) throws Exception { return lbMap.useReachNotify ? Integer.parseInt(lbl) : lbMap.sbdry(lbl); }
  public int ebdry(String lbl) throws Exception { return lbMap.useReachNotify ? Integer.parseInt(lbl) : lbMap.ebdry(lbl); }

  public MappedBuffer(String text, boolean useReachNotify) throws Exception {lbMap = new LblBoundaryMap(useReachNotify); sbuf = text; }

  public String partText(int sPos, int ePos) 
  { 
   return ((String)sbuf).substring(sPos - 1, ePos);
  }

  public void insBefore(int inx, String text) throws Exception
  {
   sbuf = ((String)sbuf).substring(1 - 1, inx - 1) + text + ((String)sbuf).substring(inx - 1, sbuf.length());
   ibuf = null;
   lbMap.IndexShift(inx, text.length(), 1);
   lbMap.IndexShift(inx - 1, text.length(), -1);
   if (lbMap.useReachNotify) for (InxObserver r : listeners) r.sIndexShift(inx, text.length());
  }

  public void insAfter(int inx, String text) throws Exception
  {
   sbuf = ((String)sbuf).substring(1 - 1, inx) + text + ((String)sbuf).substring(inx, sbuf.length());
   ibuf = null;
   lbMap.IndexShift(inx + 1, text.length(), 1);
   lbMap.IndexShift(inx, text.length(), -1);
   if (lbMap.useReachNotify) for (InxObserver r : listeners) r.sIndexShift(inx + 1, text.length());
  }

  public void delAfter(int inx, int count) throws Exception
  {
   sbuf = ((String)sbuf).substring(1 - 1, inx) + ((String)sbuf).substring(inx + count, sbuf.length());
   ibuf = null;
   lbMap.IndexShift(inx + 1, -count, 1);
   lbMap.IndexShift(inx + 1, -count, -1);
   if (lbMap.useReachNotify) for (InxObserver r : listeners) r.sIndexShift(inx + 1, -count);
  }

  public void subscribe(InxObserver listener) throws Exception { if (lbMap.useReachNotify) listeners.Add(listener); }


 }

/*
 class MappedBuffer
 {
  private         Pack<Rch>       listeners       = new Pack<Rch>(0);     //private         ArrayList<Rch>  listeners       = new ArrayList<Rch>();
  private         char[]          buf;
  private         char[]          _Buf;
  private         char[]          Buf()           throws Exception {if (_Buf != null) return _Buf; _Buf = new String(buf).toUpperCase().toCharArray() ; if (buf.length != _Buf.length) throw new Exception("Internal error in Reach: uppercase text differs in length"); return _Buf;}
  private         char            charAt(int inx, boolean upper) throws Exception {return (upper) ? Buf()[inx] : buf[inx]; }
  public          char            get(int inx)                   throws Exception {return charAt(inx, false); }
  public          char            Get(int inx)                   throws Exception {return charAt(inx, true); }
  public          int             len()           {return buf.length;}
  private         LblBoundaryMap       inxlbl;
  private         int             sLblMax;
  private         int             eLblMax;
  public          int             sLabel(int inx) throws Exception {if (inxlbl.useReachNotify) return inx; Pack<Integer> l = inxlbl.sLabels(inx); if (l.len() > 0) return l.get(1); inxlbl.AddStart(inx, ++sLblMax); return sLblMax; }
  public          int             eLabel(int inx) throws Exception {if (inxlbl.useReachNotify) return inx; Pack<Integer> l = inxlbl.eLabels(inx); if (l.len() > 0) return l.get(1); inxlbl.AddEnd(inx, ++eLblMax); return eLblMax; }

  //public        int             sIndex(int lbl) throws Exception { return inxlbl.useReachNotify ? lbl : inxlbl.sIndex(lbl); }
  //public        int             eIndex(int lbl) throws Exception { return inxlbl.useReachNotify ? lbl : inxlbl.eIndex(lbl); }
  public          int             obdry (String lbl) throws Exception { return inxlbl.useReachNotify ? Integer.parseInt(lbl) : inxlbl.obdry(lbl);  }
  public          int             cbdry (String lbl) throws Exception { return inxlbl.useReachNotify ? Integer.parseInt(lbl) : inxlbl.cbdry(lbl);  }

  public MappedBuffer(String text, boolean useReachNotify) throws Exception  {inxlbl = new LblBoundaryMap(useReachNotify); buf = text.toCharArray(); }

  public String partText(int sPos, int ePos) { return new String(java.util.Arrays.copyOfRange(buf, sPos - 1, ePos));}

  public void insBefore(int inx, String text) throws Exception
  {
   String b = new String(buf);
   String txt = b.substring(1 - 1, inx - 1) + text + b.substring(inx - 1, buf.length);
   buf = txt.toCharArray();
   _Buf = null;
   inxlbl.sIndexShift(inx, text.length());
   inxlbl.eIndexShift(inx - 1, text.length());
   if (inxlbl.useReachNotify) for (Rch r : listeners) r.indexShift(inx, text.length());
  }

  public void insAfter(int inx, String text) throws Exception
  {
   String b = new String(buf);
   String txt = b.substring(1 - 1, inx) + text + b.substring(inx, buf.length);
   buf = txt.toCharArray();
   _Buf = null;
   inxlbl.sIndexShift(inx + 1, text.length());
   inxlbl.eIndexShift(inx, text.length());
   if (inxlbl.useReachNotify) for (Rch r : listeners) r.indexShift(inx + 1, text.length());
  }

  public void delAfter(int inx, int count) throws Exception
  {
   String b = new String(buf);
   String txt = b.substring(1 - 1, inx) + b.substring(inx + count, buf.length);
   buf = txt.toCharArray();
   _Buf = null;
   inxlbl.sIndexShift(inx + 1, -count);
   inxlbl.eIndexShift(inx + 1, -count);
   if (inxlbl.useReachNotify) for (Rch r : listeners) r.indexShift(inx + 1, -count);
  }

  public void subscribe(Rch listener)
  {
   if (inxlbl.useReachNotify) listeners.add(listener);
  }
 */
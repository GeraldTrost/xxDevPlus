

//** @author  GeTr Copyright© 1996-2020 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties
//** @license You may use, modify, distribute this code under GNU public dual licence free for NON-Commercial use as long as this notice remains unchanged.
//** @comment Mapped Buffer used in Chain




using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;


using org_xxdevplus_utl;
using org_xxdevplus_struct;
using org_xxdevplus_chain;


namespace org_xxdevplus_struct
{

 public class MappedBuffer
 {
  private static bool selfTested  = false;  private static string ass(bool expr) { if (!expr) throw new Exception(ass(true) + " SelfTest Failure"); return "MappedBuffer"; } private static void selfTest() { selfTested = true; } private void init() { if (!selfTested) selfTest(); }

  private         Pile<InxObserver>   listeners                         = new Pile<InxObserver>();
  private         char[]              buf;
  private         char[]              _Buf;
  private         char[]              Buf                                {get {if (_Buf != null) return _Buf; _Buf = ASCIIEncoding.Unicode.GetChars(ASCIIEncoding.Unicode.GetBytes(partText(1, buf.Length).ToUpper())); if (buf.Length != _Buf.Length) throw new Exception("Internal error in Reach: uppercase text differs in length"); return _Buf; } }
  private         char                charAt     (int inx,  bool upper)  {return (upper) ? Buf[inx] : buf[inx]; }
  public          char                get        (int              inx)  {return charAt(inx, false); }
  public          char                Get        (int              inx)  {return charAt(inx, true); }
  public          int                 Len()                              {return buf.Length; }
  private         LblBoundaryMap      lbMap;
  private         long                sLblMax;
  private         long                eLblMax;
  public          long                sLabel     (int              bdy)  {if (lbMap.useReachNotify) return bdy; Pile<long> l = lbMap.Labels(bdy,  1); if (l.Len > 0) return l[1]; lbMap.AddStart(bdy, ++sLblMax); return sLblMax; }
  public          long                eLabel     (int              bdy)  {if (lbMap.useReachNotify) return bdy; Pile<long> l = lbMap.Labels(bdy, -1); if (l.Len > 0) return l[1]; lbMap.AddEnd(bdy, ++eLblMax);   return eLblMax; }

  public int sbdry(string lbl) { return lbMap.useReachNotify ? Int32.Parse(lbl) : lbMap.sbdry(lbl); }
  public int ebdry(string lbl) { return lbMap.useReachNotify ? Int32.Parse(lbl) : lbMap.ebdry(lbl); }

  public MappedBuffer(string text, bool useReachNotify) {lbMap = new LblBoundaryMap(useReachNotify); buf = ASCIIEncoding.Unicode.GetChars(ASCIIEncoding.Unicode.GetBytes(text)); }

  public string partText(int sPos, int ePos) { return ASCIIEncoding.Unicode.GetString(ASCIIEncoding.Unicode.GetBytes(buf, (int)sPos - 1, (int)ePos - (int)sPos + 1)); }

  public void insBefore(int inx, string text)
  {
   string txt = ASCIIEncoding.Unicode.GetString(ASCIIEncoding.Unicode.GetBytes(buf, 1 - 1, (int)inx - 1)) + text + ASCIIEncoding.Unicode.GetString(ASCIIEncoding.Unicode.GetBytes(buf, (int)inx - 1, buf.Length - (int)inx + 1));
   buf = ASCIIEncoding.Unicode.GetChars(ASCIIEncoding.Unicode.GetBytes(txt));
   _Buf = null;
   if (lbMap.useReachNotify) foreach (InxObserver r in listeners) { r.sIndexShift(inx, text.Length); r.eIndexShift(inx - 1, text.Length); } else { lbMap.IndexShift(inx, text.Length, 1); lbMap.IndexShift(inx - 1, text.Length, -1); }
  }

  public void insAfter(int inx, string text)
  {
   string txt = ASCIIEncoding.Unicode.GetString(ASCIIEncoding.Unicode.GetBytes(buf, 1 - 1, (int)inx)) + text + ASCIIEncoding.Unicode.GetString(ASCIIEncoding.Unicode.GetBytes(buf, (int)inx, buf.Length - (int)inx));
   buf = ASCIIEncoding.Unicode.GetChars(ASCIIEncoding.Unicode.GetBytes(txt));
   _Buf = null;
   lbMap.IndexShift(inx + 1, text.Length, 1);
   lbMap.IndexShift(inx, text.Length, -1);
   if (lbMap.useReachNotify) foreach (InxObserver r in listeners) { r.sIndexShift(inx + 1, text.Length); r.eIndexShift(inx, text.Length); } else { lbMap.IndexShift(inx + 1, text.Length, 1); lbMap.IndexShift(inx, text.Length, -1); }
  }

  public void delAfter(int inx, int count)
  {
   string txt = ASCIIEncoding.Unicode.GetString(ASCIIEncoding.Unicode.GetBytes(buf, 1 - 1, (int)inx)) + ASCIIEncoding.Unicode.GetString(ASCIIEncoding.Unicode.GetBytes(buf, (int)inx + count, buf.Length - (int)inx - count));
   buf = ASCIIEncoding.Unicode.GetChars(ASCIIEncoding.Unicode.GetBytes(txt));
   _Buf = null;
   if (lbMap.useReachNotify) foreach (InxObserver r in listeners) { r.sIndexShift(inx + 1, -count); r.eIndexShift(inx, -count); } else { lbMap.IndexShift(inx + 1, -count, 1); lbMap.IndexShift(inx, -count, -1); }
  }

  public void subscribe(InxObserver listener)
  {
   if (lbMap.useReachNotify) listeners.Add(listener);
  }


 }

}



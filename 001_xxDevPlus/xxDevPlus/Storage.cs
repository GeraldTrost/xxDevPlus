using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace FlexBase
{
 public class Storage
 {
  //Copyright© 1996-2012 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties, You may use, modify, distribute this code as long as this notice remains unchanged.
  private static bool selfTested  = false;  private static string ass(bool expr) { if (!expr) throw new Exception(ass(true) + " SelfTest Failure"); return "Storage"; } private void init() { if (!selfTested) selfTest(); }
 

  private Sequence       elStor;
  private SeqLabel       oBdry;
  private SeqLabel       cBdry;
  private Pile<Block>    bl;
  private bool           fitted    = false;
  
  internal Storage(Sequence elStor, SeqLabel oBdry, SeqLabel cBdry) {bl = new Pile<Block>("", new Block(this)); this.elStor = elStor; this.oBdry = oBdry; this.cBdry = cBdry; elStor.subscribe(this); upd(); }

  private long absPos(long len, long pos) { if (pos >= 0) return pos; return len + pos + 1; }

  internal Storage(long sPosVal, long LenVal, long ePosVal, long RestrictPattern)
  {
   long        fullLen  = 0;
   bool       sWide    = (RestrictPattern >= 10000);  //skip empty blocks before sPos if applicable
   long        sPos     = 0;
   long        len      = 0;
   long        ePos     = 0;
   bool       eWide    = (RestrictPattern % 2 > 0);   //skip empty blocks after ePos if applicable

   if (sWide) RestrictPattern -= 10000;
   RestrictPattern = RestrictPattern / 10;
   switch (RestrictPattern)
   {
    case 000: sPos = 1;                         ePos = fullLen;                                                                                                                  break;   //no Pos is given
    case 100: sPos = absPos(fullLen, sPosVal);  ePos = fullLen;                                                                                                                  break;   //only StartPos is given
    case 001: sPos = 1;                         ePos = absPos(fullLen, ePosVal);                                                                                                 break;   //only EndPos is given
    case 101: sPos = absPos(fullLen, sPosVal);  ePos = absPos(fullLen, ePosVal);                                                                                                 break;   //StartPos and EndPos are given
    case 010: if (LenVal < 0) { ePos = fullLen; sPos = ePos + LenVal + 1; } else { sPos = 1; ePos = sPos + LenVal - 1; }                                                         break;   //only Len is given
    case 110: if (LenVal < 0) { ePos = absPos(fullLen, sPosVal); sPos = ePos + LenVal + 1; } else { sPos = absPos(fullLen, sPosVal); ePos = sPos + LenVal - 1; }                 break;   //StartPos and Len are given
    case 011: if (LenVal < 0) { sPos = absPos(fullLen, ePosVal); ePos = sPos - LenVal - 1; } else { ePos = absPos(fullLen, ePosVal); sPos = ePos - LenVal + 1; }                 break;   //Len and EndPos are given
    case 111: if (LenVal < 0) { sPos = absPos(fullLen, ePosVal); ePos = absPos(fullLen, sPosVal); } else { sPos = absPos(fullLen, sPosVal); ePos = absPos(fullLen, ePosVal); }   break;   //StartPos and Len and EndPos are given
   }
   if (sPos < 1) { sPos = 1; fitted = true; } if (ePos > fullLen) { ePos = fullLen; fitted = true; } if (sPos > fullLen + 1) { sPos = fullLen + 1; fitted = true; } if (ePos < 0) { ePos = 0; fitted = true; } len = ePos - sPos + 1;
   
   bl = new Pile<Block>("", new Block(this));
   long sbl = 0; long ebl = 0; long sblPos = 0; long eblPos = 0; block4Pos(sPos, ePos, ref sbl, ref ebl, ref sblPos, ref eblPos);
   if (ePos < sPos) { ebl = sbl; eblPos = sblPos - 1; }
   if ((sWide) && (sblPos <= 1)) for (long i = sbl - 1; i > 0; i--) { if (bl[i].Len > 0) break; sbl = i; sblPos = 1; }
   if ((eWide) && (eblPos >= bl[ebl].Len)) for (long i = ebl + 1; i <= bl.Len; i++) { if (bl[i].Len > 0) break; ebl = i; eblPos = 0; }
   if (sbl == ebl)
   {
    elStor = bl[sbl].item.elStor;
    //long sInx = elStor.sIndex(source.bl[sbl].oBdry); // UNBELIEVABLE!!!! This variation slows doen by 10% !!!!!  long sInx = source.bl_sInx(sbl);
    long sInx = bl[sbl].sInx;
//    oBdry = "" + elStor.oBdry(sInx - 1 + sblPos);
//    cBdry = "" + elStor.cBdry(sInx - 1 + eblPos);
    elStor.subscribe(this);
   } else
   {
    bl = bl.slice(sbl, ebl);
//    bl[1] = bl[sbl].from(sblPos);
//    bl[-1] = bl[ebl].upto(eblPos);
   }
   upd(); //inf = new long[bl.Count + 1, 3];
   //mw._void();
  }

  private long[] block4Pos(long pos)
  {
   //MethWatch mw = new MethWatch("RchFdn.block4Pos");
   if (pos == 0) return new long[2] { 1, bl[1].sInx - 1 };  //if (pos == 0) return mw._intA(new long[2] { 1, bl_sInx(1) - 1 });
   if ((pos < 1) || (pos > Len())) throw new Exception("invalid Position within Reach");
   for (long i = 1; i <= bl.Len; i++) if ((bl[i].sPos <= pos) && (pos <= bl[i].ePos)) return new long[2] { i, 1 + pos - bl[i].sPos };  //for (long i = 1; i <= bl.Count; i++) if ((bl_sPos(i) <= pos) && (pos <= bl_ePos(i))) return mw._intA(new long[2] { i, 1 + pos - bl_sPos(i) });
   return new long[2] { -1, -1 };  //return mw._intA(new long[2] { -1, -1 });
  }

  private void block4Pos(long sPos, long ePos, ref long sbl, ref long ebl, ref long sblPos, ref long eblPos)
  {
   //MethWatch mw = new MethWatch("RchFdn.block4Pos");
   if (sPos == 0) { sbl = 1; sblPos = bl[1].sInx - 1; }
   if (ePos == 0) { ebl = 1; eblPos = bl[1].sInx - 1; }
//   if (sPos == len + 1) { sbl = bl.Len(); sblPos = bl[-1].Len + 1; }
//   if (ePos == len + 1) { ebl = bl.Len(); eblPos = bl[-1].Len + 1; }
   //if ((sPos < 1) || (sPos > len) || (ePos < 1) || (ePos > len)) throw new Exception("invalid Position within Reach");
   for (long i = 1; i <= bl.Len; i++) if ((bl[i].sPos <= sPos) && (sPos <= bl[i].ePos)) { sbl  = i; sblPos = 1 + sPos - bl[i].sPos; }
   if (ePos == sPos) { ebl = sbl; eblPos = sblPos; return; }  //if (ePos == sPos) { ebl = sbl; eblPos = sblPos; mw._void(); return; }
   for (long i = sbl; i <= bl.Len; i++) if ((bl[i].sPos <= ePos) && (ePos <= bl[i].ePos)) { ebl  = i; eblPos = 1 + ePos - bl[i].sPos; return; }  //for (long i = sbl; i <= bl.Count; i++) if ((bl_sPos(i) <= ePos) && (ePos <= bl_ePos(i))) { ebl  = i; eblPos = 1 + ePos - bl_sPos(i); mw._void(); return; }
   //mw._void();
  }


  protected Storage(Storage first, Storage second) //this special constructor is used to create a non-standard Storage with 1 compact Storage item <> this in Block List, used intemediately during operator+
  {
   //MethWatch mw = new MethWatch("RchFdn.Rch_first_second");
   //   bl = new Pile<Rch>();
   bl.Add(first.bl);
   bl.Add(second.bl);
   //bl = pieces();
   upd();
   fitted = (first.fitted || second.fitted);
   //mw._void();
  }

  public Storage(Sequence sq)
  {
   if (!selfTested) selfTest();
   this.elStor = sq;
   this.oBdry = new SeqLabel(1);
   this.cBdry = new SeqLabel(sq.Len() + 1);
   bl = null;
  }


  internal void upd() { }

  private static void selfTest()
  {
   selfTested = true;
   string[] elements = new string[3];
   elements[0] = "abc";
   elements[1] = "def";
   elements[2] = "ghi";
   Storage stor = new Storage(new Sequence(elements));
   ass(stor.Len() == 3);
  }

  public long    Len   ()        { return elStor.Len();                                 }
  public object this  [long inx] { get { return elStor[inx]; } set { elStor[inx] = value; } }
 }


}

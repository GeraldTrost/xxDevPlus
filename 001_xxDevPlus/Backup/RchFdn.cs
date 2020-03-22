using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using ndBase;
using ndString;

 // AttGetr: Remaks and changes in Definitions:
 // 
 // A Reach object represents a text or a substring of a text.
 // A reach object may internally consist of a a LabelIndexMapBuffer.
 // 

 public partial class Rch  //: Store Deprecated, should be replaced with ndBase.store
 {
  //Copyright© 1996-2012 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties, You may use, modify, distribute this code under GNU public licence for NON-Commercial use as long as this notice remains unchanged.
  private static bool selfTested  = false;  private static string ass(bool expr) { if (!expr) throw new Exception(ass(true) + " SelfTest Failure"); return "Rch"; } private static void selfTest() { selfTested = true; } private void init() { if (!selfTested) selfTest(); }

  private      Pile<Rch>     bl;
  private      int[,]        inf;
  private      string        sLbl;
  private      string        eLbl;
  private      MappedBuffer  mBuf;
  protected    bool          fitted;

  /*
   * 
   * ATTGETR

   * Die Definition für schnelle Rech Klasse hat so gelautet:
   * Reach ist entweder compact oder compound. 
   *   compact Reach hat keine BlockList (bl == null) aber einen Mapped Buffer (mBuf != null) und sLbl, eLbl
   *   compund Reach BlockList (bl == Liste von capact Reaches) aber keinen Mapped Buffer (mBuf == null) und keinen sLbl, keinen eLbl

   * Die Definition für langsames Rech Klasse lautet nun so:
   * Reach ist entweder compact oder compound. 
   *   compact Reach einen Mapped Buffer (mBuf != null) und sLbl, eLbl und in seiner Block List bl ist this als einziges kompaktes Reach eingetregen
   *   compund Reach BlockList (bl == Liste von capact Reaches, alle != this!) aber keinen Mapped Buffer (mBuf == null) und keinen sLbl, keinen eLbl

   * Der Unterschied in der Philosophie besteht darin, daß man sich in der slow Variante immer darauf verlassen kann, daß in der Block List ein Item
   * vorhanden ist, die interne Implementierung wird übersichtlicher und konsostenter, viele if-Verzweigungen entfallen dadurc, source wird lesbar.
   * Der Nachteil von slow Reach ist aber, daß es um 50% langsamer ist, sowohl im Exe als auch im Debugger (12 sec vs. 18 sec)
   * 
   * Das Memory Manegement von DOTNET ist wohl ein Hindernis, wenn man für jedes noch so winzige Reach immer einen Pile<Reach> mit einem einzigen Element
   * anlegen muß. Vielleicht kann man den Zeitverlust aber auch dadurch beheben, daß man die Klasse Pile optimiert und den Inhalt nicht in Array sondern 
   * in List einfügt ?
   * 
   * Vielleicht aber fallen auch unnötige Prozedurteile weg, wenn man am slow Reach weiterimplementiert sodaß letztlich beide gleich schnell sind,
   * das ist eine unbestätigte Hoffnung, ein Versuch. Die Messungen wurden in einer VM gemacht, die selbst wenig Memory hatte, vielleicht ist der Zeitunterschied
   * auf schnellen Rechnern belanglos. Das ist ebenfalls eine unbestätigte Hoffnung.
   * 
   * Die check Routine für das fast Reach ist hier auskommentiert:
   * 
  private void chk() //check if Reach is valid within the given Rules for constructing Reaches
  {
   if ((mBuf == null) && (bl == null))                      throw new Exception("Invalid Reach: has neither Mapped Buffer nor has it a Block List");
   if ((mBuf != null) && (bl != null))                      throw new Exception("Invalid Reach: has both Mapped Buffer and Block List");
   if (bl != null) foreach (Reach b in bl) if (!b.compact)  throw new Exception("Invalid Reach: At least one Reach in Block List has its own Block List (is not compact)");
  }
  */

  // Check Routine für slow Reach:
  private void chk() //check if Reach is valid within the given Rules for constructing Reaches
  {
   if ((compact) && (bl == null))                           throw new Exception("Invalid Reach: compact Reach must contain itselb in Block List");
   if ((compact) && bl.Len != 1)                            throw new Exception("Invalid Reach: compact Reach must contain itselb in Block List");
   if ((compact) && (bl[1] != this))                        throw new Exception("Invalid Reach: compact Reach must contain itselb in Block List");
   if (bl != null) foreach (Rch b in bl) if (!b.compact)    throw new Exception("Invalid Reach: At least one Reach in Block List has its own Block List (is not compact)");
  }

  internal Rch(MappedBuffer mBuf, string sLbl, string eLbl)
  {
   bl = new Pile<Rch>("", true, this);
   this.mBuf = mBuf;
   this.sLbl = sLbl;
   this.eLbl = eLbl;
   mBuf.subscribe(this);
   upd();
  }

  internal Rch(Rch source, Restrict rt)
  {
   //MethWatch mw = new MethWatch("RchFdn.Rch_Rch_Restrict");
   fitted = rt.fitted | source.fitted;
   bl = new Pile<Rch>("", true, this);
   int sbl = 0; int ebl = 0; int sblPos = 0; int eblPos = 0; source.block4Pos(rt.sPos, rt.ePos, ref sbl, ref ebl, ref sblPos, ref eblPos);
   if (rt.ePos < rt.sPos) { ebl = sbl; eblPos = sblPos - 1; }
   if ((rt.sWide) && (sblPos <= 1)) for (int i = sbl - 1; i > 0; i--) { if (source.bl_Len(i) > 0) break; sbl = i; sblPos = 1; }
   if ((rt.eWide) && (eblPos >= source.bl_Len(ebl))) for (int i = ebl + 1; i <= source.bl.Len; i++) { if (source.bl_Len(i) > 0) break; ebl = i; eblPos = 0; }
   if (sbl == ebl)
   {
    mBuf = source.bl[sbl].mBuf;
    //long sInx = mBuf.sIndex(source.bl[sbl].sLbl); // UNBELIEVABLE!!!! This variation slows doen by 10% !!!!!  long sInx = source.bl_sInx(sbl);
    long sInx = source.bl_sInx(sbl);
    sLbl = "" + mBuf.sLabel((int)(sInx - 1 + sblPos));
    eLbl = "" + mBuf.eLabel((int)(sInx - 1 + eblPos));
    mBuf.subscribe(this);
   } else
   {
    bl = source.bl.slice(sbl, ebl);
    bl[1] = source.bl[sbl].from(sblPos);
    bl[-1] = source.bl[ebl].upto(eblPos);
   }
   upd(); //inf = new long[bl.Count + 1, 3];
   //mw._void();
  }

  protected Rch(Rch first, Rch second) //this special constructor is used to create a non-standard Reach with 1 compact Reach item <> this in Block List, used intemediately during operator+
  {
   //MethWatch mw = new MethWatch("RchFdn.Rch_first_second");
   bl = new Pile<Rch>();
   bl.Add(first.bl);
   bl.Add(second.bl);
   //bl = pieces();
   upd();
   fitted = (first.fitted || second.fitted);
   //mw._void();
  }

  private static Rch fit(Rch first, Rch second) //AttGeTr: tricky: this function always returns the first reach but when one of both is fitted it returns a fitted copy of the first reach
  {
   if ((first.fitted) || (!second.fitted)) return first;
   Rch ret = first.upto(first.len);
   ret.fitted = true;
   return ret;
  }

  private Rch    fit          (Rch r)        {r.fitted = true; return r; }

  private Rch badImpl(Rch r)
  {
   // AttGeTr: sometimes resulting Reaches are "fitted" - I must research why it is.
   // the concept of "fitted Reaches" and "not fitted Reaches" SURELY MAKES A LOT OF SENSE
   // I remember that a resulting reach is "fitted" in case you calculate Reach("abc").from(17) - this gives a fitted empty Reach
   // "fitted" has the meaning similar to: "a recent calculation violated bounds and the Result had to be fitted to its bounds" ...
   // IMPORTANT: the badImpl() function is NOT menat for debuggig/investiation of the "fitted" feature !!!
   // IMPORTANT: some calculations in the Rch Class call badImpl() because of invalid implemention.
   //            the implemention WAS VALID BEFORE the string parameter token has been altered to string... tokens
   //            a valid implementation MUST be found and so you should debug with a breakpoint at the next line:
   return (r); //place your debuggin breakpoint here!
  }

  private int    bl_Len       (int blInx)   {return (blInx < 0) ? inf[bl.Len + 1 + blInx, 0] : inf[blInx, 0]; }
  private int    bl_sPos      (int blInx)   {return bl_ePos(blInx) - bl_Len(blInx) + 1; }
  private int    bl_ePos      (int blInx)   {return (blInx < 0) ? inf[bl.Len + 1 + blInx, 1] : inf[blInx, 1]; }
  private int    bl_sInx      (int blInx)   {return (blInx < 0) ? inf[bl.Len + 1 + blInx, 2] : inf[blInx, 2]; }
  private int    bl_eInx      (int blInx)   {return bl_sInx(blInx) + bl_Len(blInx) - 1; }
  
  //private long sIndex       (long lbl)     {return mBuf.sIndex(lbl); } //dbgchk!! not necessary in release build //private long sIndex(long lbl) { assCompact(); return mBuf.sIndex(lbl); }
  //private long eIndex       (long lbl)     {return mBuf.eIndex(lbl); } //dbgchk!! not necessary in release build //private long eIndex(long lbl) { assCompact(); return mBuf.eIndex(lbl); }
  private int    sbdry        (string lbl)   {return mBuf.sbdry(lbl);  } //dbgchk!! not necessary in release build //private long sIndex(long lbl) { assCompact(); return mBuf.sIndex(lbl); }
  private int    ebdry        (string lbl)   {return mBuf.ebdry(lbl);  } //dbgchk!! not necessary in release build //private long eIndex(long lbl) { assCompact(); return mBuf.eIndex(lbl); }
  
  private long   sLabel       (int inx)      {return mBuf.sLabel(inx); } //dbgchk!! not necessary in release build //private long sLabel(long inx) { assCompact(); return mBuf.sLabel(inx); }
  private long   cLabel       (int inx)      {return mBuf.eLabel(inx); } //dbgchk!! not necessary in release build //private long eLabel(long inx) { assCompact(); return mBuf.eLabel(inx); }
  private bool   compact                     {get { return (mBuf != null); } }
  private void   assCompact   ()             {if (!compact) throw new Exception("Illegal Operation on a compound Reach"); }

  private Pile<Rch> pieces()
  {
   //MethWatch mw = new MethWatch("RchFdn.pieces");
   Pile<Rch> ret = new Pile<Rch>();
   Pile<Rch> res = new Pile<Rch>();
   foreach (Rch r in bl) if (r.compact) res.Add(r); else res.Add(r.pieces());
   int i = 1;
   while (i <= res.Len)
   {
    MappedBuffer _mBuf = res[i].mBuf;
    long _eInx = res[i].bl_eInx(1);
    int next_i = i + 1;
    for (int j = i + 1; j <= res.Len; j++)
    {
     if (res[j].mBuf != _mBuf) break;
     if (res[j].bl_sInx(1) != _eInx + 1) break;
     _eInx = res[j].bl_eInx(1);
     next_i = j + 1;
    }
    if (next_i == i + 1) ret.Add(res[i]); else ret.Add((Rch)new Reach(res[i].mBuf, res[i].sLbl, res[next_i - 1].eLbl));
    i = next_i;
   }
   return ret; //return mw._Pile_Rch(ret); 
  }

  private int[] block4Pos(int pos)
  {
   //MethWatch mw = new MethWatch("RchFdn.block4Pos");
   if (pos == 0) return new int[2] { 1, bl_sInx(1) - 1 };  //if (pos == 0) return mw._intA(new long[2] { 1, bl_sInx(1) - 1 });
   if ((pos < 1) || (pos > len)) throw new Exception("invalid Position within Reach");
   for (int i = 1; i <= bl.Len; i++) if ((bl_sPos(i) <= pos) && (pos <= bl_ePos(i))) return new int[2] { i, 1 + pos - bl_sPos(i) };  //for (long i = 1; i <= bl.Count; i++) if ((bl_sPos(i) <= pos) && (pos <= bl_ePos(i))) return mw._intA(new long[2] { i, 1 + pos - bl_sPos(i) });
   return new int[2] { -1, -1 };  //return mw._intA(new long[2] { -1, -1 });
  }

  private void block4Pos(int sPos, int ePos, ref int sbl, ref int ebl, ref int sblPos, ref int eblPos)
  {
   //MethWatch mw = new MethWatch("RchFdn.block4Pos");
   if (sPos == 0) { sbl = 1; sblPos = bl_sInx(1) - 1; }
   if (ePos == 0) { ebl = 1; eblPos = bl_sInx(1) - 1; }
   if (sPos == len + 1) { sbl = (int)bl.Len; sblPos = bl_Len(-1) + 1; }
   if (ePos == len + 1) { ebl = (int)bl.Len; eblPos = bl_Len(-1) + 1; }
   //if ((sPos < 1) || (sPos > len) || (ePos < 1) || (ePos > len)) throw new Exception("invalid Position within Reach");
   for (int i = 1; i <= bl.Len; i++) if ((bl_sPos(i) <= sPos) && (sPos <= bl_ePos(i))) { sbl  = i; sblPos = 1 + sPos - bl_sPos(i); }
   if (ePos == sPos) { ebl = sbl; eblPos = sblPos; return; }  //if (ePos == sPos) { ebl = sbl; eblPos = sblPos; mw._void(); return; }
   for (int i = sbl; i <= bl.Len; i++) if ((bl_sPos(i) <= ePos) && (ePos <= bl_ePos(i))) { ebl  = i; eblPos = 1 + ePos - bl_sPos(i); return; }  //for (long i = sbl; i <= bl.Count; i++) if ((bl_sPos(i) <= ePos) && (ePos <= bl_ePos(i))) { ebl  = i; eblPos = 1 + ePos - bl_sPos(i); mw._void(); return; }
   //mw._void();
  }

  private long incr(long i) { return (i < 0) ? i - 1 : i + 1; }
  private long decr(long i) { return (i < 0) ? i + 1 : i - 1; }

  private  Rch del(int cnt) { return (cnt < 0) ? upto(cnt - 1) : from(cnt + 1); }
  internal Rch del_() { return del_(len); }
  internal Rch del_(int cnt)
  {
   if (cnt == 0) return this;
   int sbl = 0; int ebl = 0; int sblPos = 0; int eblPos = 0;
   if (cnt > 0)
   {
    if (cnt > len) { cnt = len; fitted = true; } 
    block4Pos(1, cnt, ref sbl, ref ebl, ref sblPos, ref eblPos);
   } else
   {
    if (cnt < -len) { cnt = -len; fitted = true; } 
    block4Pos(len + 1 + cnt, len, ref sbl, ref ebl, ref sblPos, ref eblPos);
   }
   if (sbl == ebl)
    bl[sbl].mBuf.delAfter((int)(bl[sbl].sbdry(bl[sbl].sLbl) + sblPos - 2), (int)(Math.Abs(cnt)));
   else
   {
    ((Reach)bl[sbl]).DEL(bl[sbl].len - sblPos).after(sblPos - 1);
    for (int i = sbl + 1; i < ebl; i++) ((Reach)bl[i]).DEL(bl[i].len).after(0);
    ((Reach)bl[ebl]).DEL(eblPos).after(0);
   }
   return this;
  }

  internal void upd()
  {
   //if (inf != null) return; // this is a future option once MappedBuffer increments version after modification of buf we could selectively perform the update resulting in 10 percent performance gain.
   //chk(); //dbgchk!! not necessary in release build, saves 20 percent
   fitted = false;
   if (inf == null) inf = new int[bl.Len + 1, 3]; // inf contains [ bl.len, bl.ePos, bl.inx ] for i = 1 ... bl.Count. inf[0, x] reserved
   
   int lenSum = 0;
   for (int i = 1; i <= bl.Len; i++)
   {
    if (this == bl[i]) 
    {
     inf[i, 2] = sbdry(sLbl);
     inf[i, 0] = ebdry(eLbl) - inf[i, 2];
     inf[i, 1] = inf[i, 0];
     return;
    }
    inf[i, 0] = bl[i].len;
    lenSum += inf[i, 0];
    inf[i, 1] = lenSum;
    inf[i, 2] = bl[i].sbdry(bl[i].sLbl);
   }
  }

  internal void cloneFrom(Rch other) //only allowed if the other dies right after this call, example a.cloneFrom(b - c)
  {
   bl = other.bl;
   if (bl.Len == 1) bl[1] = this;
   mBuf = other.mBuf;
   sLbl = other.sLbl;
   eLbl = other.eLbl;
   inf = other.inf;
   fitted = other.fitted;
  }

  internal Rch cloneDiff(Rch other)            {cloneFrom(this - other); return other; }
  internal Rch cloneDiff(Rch other, Rch skip)  {cloneFrom(this - other - skip); return other; }
    
  protected string dbgTxt  { get { string ret = ""; foreach (Rch r in bl) ret += "«" + r.text + "»"; return ret; } }
  protected int    stepPos (long occur, int stepLen) { if ((stepLen == 0) && (occur < 0)) return len + 1; return Math.Sign((int)occur) * stepLen; }

  protected Rch(string text)
  {
   //MethWatch mw = new MethWatch("RchFdn.Rch_string");
   //text = utl.f2s("e:/sfconf/Triples.0003.txt");
   bl = new Pile<Rch>("", true, this);
   mBuf = new MappedBuffer(text, Reach.useReachNotify);
   sLbl = "" + mBuf.sLabel(1);
   eLbl = "" + mBuf.eLabel((int)mBuf.Len());
   mBuf.subscribe(this);
   upd(); //inf = new long[bl.Count + 1, 3];
   //mw._void();
  }

  public static Rch operator+(Rch first, string second) { return first + (Rch)(new Reach(second)); }
  public static Rch operator+(Rch first, Rch second) { return new Reach((Reach)first, (Reach)second); }
  public static Rch operator+(string first, Rch second)  {return (Rch) (new Reach(first)) + second;}

  public static Rch operator-(Rch first, Rch second)
  {
   if (first.len * second.len == 0) return fit(first, second);
   if ((!first.compact) || (!second.compact)) 
   {
    if (first.compact) 
    { 
     Rch ret = first; 
     foreach (Rch s in second.bl) ret = ret - s;
     return ret;
    } 
    else 
    {
     Rch ret = fit(first.upto(0) + first.from(first.len + 1), second); // delFromLeftToRight strategy: Rch ret = fit(first.from(first.len + 1), second);
     foreach (Rch f in first.bl) 
     {
      Rch diff = f - second;
      if (diff.len > 0) if (ret.len == 0) ret = diff; else ret = ret + diff;
     }
     return ret;
    }

   }
   if (first.mBuf != second.mBuf) return fit(first, second);
   int sInx1 = first.sbdry(first.sLbl);
   int eInx1 = first.ebdry(first.eLbl) - 1;
   int sInx2 = second.sbdry(second.sLbl);
   int eInx2 = second.ebdry(second.eLbl) - 1;
   if ((eInx2 < sInx1) || (sInx2 > eInx1)) return fit(first, second);
   if ((sInx2 <= sInx1) && (eInx2 >= eInx1)) return fit(first.upto(0) + first.from(first.len + 1), second); // delFromLeftToRight strategy: fit(first.from(first.len + 1), second);
   if ((sInx2 > sInx1) && (eInx2 < eInx1)) return fit(first.upto(sInx2 - sInx1) + first.from(first.len + 1 - (eInx1 - eInx2)), second);
   if (sInx2 <= sInx1) return fit(first.from(first.len + 1 - (eInx1 - eInx2)), second);
   return fit(first.upto(sInx2 - sInx1), second);
  }


 }






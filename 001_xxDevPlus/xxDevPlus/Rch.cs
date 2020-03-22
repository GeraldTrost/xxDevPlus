

//** @author  GeTr Copyright© 1996-2020 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties
//** @license You may use, modify, distribute this code under GNU public dual licence free for NON-Commercial use as long as this notice remains unchanged.
//** @comment Base Class for Chain 


using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace org_xxdevplus_chain
{

 public partial class Rch : InxObserver
 {
  //private static bool selfTested  = false;  private static string ass(bool expr) { if (!expr) throw new Exception(ass(true) + " SelfTest Failure"); return "Rch"; } private static void selfTest() { selfTested = true; } private void init() { if (!selfTested) selfTest(); }

  protected char this[int pos, bool upper]
  {
   get
   {
    if (pos < 0) pos = 1 + len + pos;
    int sbl = 0; int ebl = 0; int sblPos = 0; int eblPos = 0; block4Pos(pos, pos, ref sbl, ref ebl, ref sblPos, ref eblPos);
    if (sbl < 1) return '\0';
    return (upper) ? bl[sbl].mBuf.Get(bl_sInx(sbl) - 1 + sblPos - 1) : bl[sbl].mBuf.get(bl_sInx(sbl) - 1 + sblPos - 1);
   }
  }

  protected int len { get { return bl_ePos(-1); } }    //public  long len { get { if (!compact) { long ret = 0; foreach (Rch r in bl) ret += r.len; return ret; } return eIndex(eLbl) - sIndex(sLbl) + 1; } }
  protected string text { get { if (!compact) { string ret = ""; foreach (Rch r in bl) ret += r.text; return ret; } return mBuf.partText(sbdry(sLbl), ebdry(eLbl) - 1); } }
  protected string uText { get { return text.ToUpper(); } }
  protected string lText { get { return text.ToLower(); } }

  public void sIndexShift(int from, int amount)
  {
   from = from + 1;
   if (sbdry(sLbl) - 1 < from) return;
   if (amount < 0) sLbl = "" + Math.Max(sbdry(sLbl) - 1 + amount, from); else sLbl = sLbl + amount;
   //upd();
  }

  public void eIndexShift(int from, int amount)
  {
   from = from + 1;
   if (ebdry(eLbl) - 1 < from) return;
   if (amount < 0) eLbl = "" + Math.Max(ebdry(eLbl) + amount, from - 2); else eLbl = eLbl + amount;
   //upd();
  }

  // ******************* (Rch other)

  internal Rch before(Rch other)
  {
   Rch ret = this;
   MappedBuffer limitBuf = other.bl[1].mBuf;
   int limitInx = other.bl[1].sbdry(other.bl[1].sLbl);
   for (int i = 1; i <= bl.Len; i++) if (bl[i].mBuf == limitBuf) if ((bl[i].sbdry(bl[i].sLbl) <= limitInx + 1) && (limitInx - 1 <= bl[i].ebdry(bl[i].eLbl) - 1)) return upto(bl_sPos(i) + limitInx - bl[i].sbdry(bl[i].sLbl) - 1);
   return ret;
  }
  internal Rch before_(Rch other) { return cloneDiff(before(other)); }
  internal Rch before__(Rch other) { return cloneDiff(before(other), at(other)); }
  internal Rch insbefore(Rch other, RchOp op) { return before(other) + op.sTxt + from(other); }
  internal Rch insbefore_(Rch other, RchOp op) { return before(other).insafter_(-1, op); } //return from(other).insbefore_(1, op); }
  internal Rch insbefore(Rch other, Rch txt) { return before(other) + txt + from(other); }
  internal Rch delbefore(Rch other) { return from(other); }
  internal Rch delbefore_(Rch other) { before(other).del_(); return this; }
  internal Rch delbefore(Rch other, RchOp op) { return before(other).del(op.cnt) + from(other); }
  internal Rch delbefore_(Rch other, RchOp op) { before(other).del_(op.cnt); return this; }
  internal Rch rplbefore(Rch other, RchOp op) { return op.sTxt + from(other); }
  internal Rch rplbefore_(Rch other, RchOp op) { return op.sTxt + from(other); }
  internal Rch rplbefore(Rch other, Rch txt) { return txt + from(other); }

  internal Rch upto(Rch other) { return before(other) + at(other); }
  internal Rch upto_(Rch other) { return cloneDiff(upto(other)); }
  internal Rch upto__(Rch other) { return cloneDiff(upto(other), at(other)); }
  internal Rch delupto(Rch other) { return after(other); }
  internal Rch delupto_(Rch other) { upto(other).del_(); return this; }
  internal Rch delupto(Rch other, RchOp op) { return upto(other).del(op.cnt) + after(other); }
  internal Rch delupto_(Rch other, RchOp op) { upto(other).del_(op.cnt); return this; }
  internal Rch rplupto(Rch other, RchOp op) { return op.sTxt + after(other); }
  internal Rch rplupto_(Rch other, RchOp op) { return op.sTxt + after(other); }
  internal Rch rplupto(Rch other, Rch txt) { return txt + after(other); }

  internal Rch at(Rch other) { return this - (this - other); }
  internal Rch at_(Rch other) { return cloneDiff(at(other)); }
  internal Rch at__(Rch other) { return cloneDiff(at(other), at(other)); }
  internal Rch delat(Rch other) { return before(other) + after(other); }
  internal Rch delat_(Rch other) { at(other).del_(); return this; }
  internal Rch delat(Rch other, RchOp op) { return before(other) + at(other).del(op.cnt) + from(other); }
  internal Rch delat_(Rch other, RchOp op) { at(other).del_(op.cnt); return this; }
  internal Rch rplat(Rch other, RchOp op) { return before(other) + op.sTxt + after(other); }
  internal Rch rplat_(Rch other, RchOp op) { return before(other) + op.sTxt + after(other); }
  internal Rch rplat(Rch other, Rch txt) { return before(other) + txt + after(other); }

  internal Rch from(Rch other) { return at(other) + after(other); }
  internal Rch from_(Rch other) { return cloneDiff(from(other)); }
  internal Rch from__(Rch other) { return cloneDiff(from(other), at(other)); }
  internal Rch delfrom(Rch other) { return before(other); }
  internal Rch delfrom_(Rch other) { from(other).del_(); return this; }
  internal Rch delfrom(Rch other, RchOp op) { return before(other) + from(other).del(op.cnt); }
  internal Rch delfrom_(Rch other, RchOp op) { from(other).del_(op.cnt); return this; }
  internal Rch rplfrom(Rch other, RchOp op) { return before(other) + op.sTxt; }
  internal Rch rplfrom_(Rch other, RchOp op) { return before(other) + op.sTxt; }
  internal Rch rplfrom(Rch other, Rch txt) { return before(other) + txt; }

  internal Rch after(Rch other)
  {
   Rch ret = from(len + 1);
   MappedBuffer limitBuf = other.bl[-1].mBuf;
   int limitInx = other.bl[-1].ebdry(other.bl[-1].eLbl) - 1;
   for (int i = 1; i <= bl.Len; i++) if (bl[i].mBuf == limitBuf) if ((bl[i].sbdry(bl[i].sLbl) <= limitInx + 1) && (limitInx - 1 <= bl[i].ebdry(bl[i].eLbl) - 1)) return from(bl_sPos(i) + limitInx - bl[i].sbdry(bl[i].sLbl) + 1);
   return ret;
  }
  internal Rch after_(Rch other) { return cloneDiff(after(other)); }
  internal Rch after__(Rch other) { return cloneDiff(after(other), at(other)); }
  internal Rch insafter(Rch other, RchOp op) { return upto(other) + op.sTxt + after(other); }
  internal Rch insafter_(Rch other, RchOp op) { return upto(other).insafter_(-1, op); }
  internal Rch insafter(Rch other, Rch txt) { return upto(other) + txt + after(other); }
  internal Rch delafter(Rch other) { return upto(other); }
  internal Rch delafter_(Rch other) { after(other).del_(); return this; }
  internal Rch delafter(Rch other, RchOp op) { return upto(other) + after(other).del(op.cnt); }
  internal Rch delafter_(Rch other, RchOp op) { after(other).del_(op.cnt); return this; }
  internal Rch rplafter(Rch other, RchOp op) { return upto(other) + op.sTxt; }
  internal Rch rplafter_(Rch other, RchOp op) { return upto(other) + op.sTxt; }
  internal Rch rplafter(Rch other, Rch txt) { return upto(other) + txt; }

  // ******************* (int  pos) //supported: backward index (eInx < 0) where -1 == last Element

  internal Rch before(int pos) { return new Reach((Reach)this, new Restrict(true, (Reach)this, 10011, 0, 0, pos - 1)); }
  internal Rch before_(int pos) { return cloneDiff(before(pos)); }
  internal Rch before__(int pos) { return cloneDiff(before(pos), at(pos)); }
  internal Rch insbefore(int pos, RchOp op) { return before(pos) + op.sTxt + from(pos); }
  internal Rch insbefore_(int pos, RchOp op)
  {
   if (pos < 0) pos = len + 1 + pos;
   if (op.sTxt.Length == 0) return this;
   int[] Bl = block4Pos(pos);
   Rch block = bl[Bl[0]];
   block.mBuf.insBefore((int)(block.sbdry(block.sLbl) + Bl[1] - 1), op.sTxt);
   return this;
  }
  internal Rch insbefore(int pos, Rch txt) { return before(pos) + txt + from(pos); }
  internal Rch delbefore(int pos) { return from(pos); }
  internal Rch delbefore_(int pos) { before(pos).del_(); return this; }
  internal Rch delbefore(int pos, RchOp op) { return before(pos).del(op.cnt) + from(pos); }
  internal Rch delbefore_(int pos, RchOp op) { before(pos).del_(op.cnt); return this; }
  internal Rch rplbefore(int pos, RchOp op) { return op.sTxt + from(pos); }
  internal Rch rplbefore_(int pos, RchOp op) { return op.sTxt + from(pos); }
  internal Rch rplbefore(int pos, Rch txt) { return txt + from(pos); }

  internal Rch upto(int pos) { return new Reach((Reach)this, new Restrict(true, (Reach)this, 10010, 0, 0, pos)); }
  internal Rch upto_(int pos) { return cloneDiff(upto(pos)); }
  internal Rch upto__(int pos) { return cloneDiff(upto(pos), at(pos)); }
  internal Rch delupto(int pos) { return after(pos); }
  internal Rch delupto_(int pos) { upto(pos).del_(); return this; }
  internal Rch delupto(int pos, RchOp op) { return upto(pos).del(op.cnt) + after(pos); }
  internal Rch delupto_(int pos, RchOp op) { upto(pos).del_(op.cnt); return this; }
  internal Rch rplupto(int pos, RchOp op) { return op.sTxt + after(pos); }
  internal Rch rplupto_(int pos, RchOp op) { return op.sTxt + after(pos); }
  internal Rch rplupto(int pos, Rch txt) { return txt + after(pos); }

  internal Rch at(int pos) { return new Reach((Reach)this, new Restrict(true, (Reach)this, 01100, pos, 1, 0)); }
  internal Rch at_(int pos) { return cloneDiff(at(pos)); }
  internal Rch at__(int pos) { return cloneDiff(at(pos), at(pos)); }
  internal Rch delat(int pos) { return before(pos) + after(pos); }
  internal Rch delat_(int pos) { at(pos).del_(); return this; }
  internal Rch delat(int pos, RchOp op) { return before(pos) + at(pos).del(op.cnt) + after(pos); }
  internal Rch delat_(int pos, RchOp op) { at(pos).del_(op.cnt); return this; }
  internal Rch rplat(int pos, RchOp op) { return before(pos) + op.sTxt + after(pos); }
  internal Rch rplat_(int pos, RchOp op) { return before(pos) + op.sTxt + after(pos); }
  internal Rch rplat(int pos, Rch txt) { return before(pos) + txt + after(pos); }

  internal Rch from(int pos) { return new Reach((Reach)this, new Restrict(false, (Reach)this, 01001, pos, 0, 0)); }
  internal Rch from_(int pos) { return cloneDiff(from(pos)); }
  internal Rch from__(int pos) { return cloneDiff(from(pos), at(pos)); }
  internal Rch delfrom(int pos) { return before(pos); }
  internal Rch delfrom_(int pos) { from(pos).del_(); return this; }
  internal Rch delfrom(int pos, RchOp op) { return before(pos) + from(pos).del(op.cnt); }
  internal Rch delfrom_(int pos, RchOp op) { from(pos).del_(op.cnt); return this; }
  internal Rch rplfrom(int pos, RchOp op) { return before(pos) + op.sTxt; }
  internal Rch rplfrom_(int pos, RchOp op) { return before(pos) + op.sTxt; }
  internal Rch rplfrom(int pos, Rch txt) { return before(pos) + txt; }

  internal Rch after(int pos) { return new Reach((Reach)this, new Restrict(false, (Reach)this, 11001, pos + 1, 0, 0)); }
  internal Rch after_(int pos) { return cloneDiff(after(pos)); }
  internal Rch after__(int pos) { return cloneDiff(after(pos), at(pos)); }
  internal Rch insafter(int pos, RchOp op) { return upto(pos) + op.sTxt + after(pos); }
  internal Rch insafter_(int pos, RchOp op)
  {
   if (pos < 0) pos = len + 1 + pos;
   if (op.sTxt.Length == 0) return this;
   int sbl = 0; int ebl = 0; int sblPos = 0; int eblPos = 0; block4Pos(pos, pos, ref sbl, ref ebl, ref sblPos, ref eblPos);
   Rch block = bl[sbl];
   block.mBuf.insAfter((int)(block.sbdry(block.sLbl) + sblPos - 1), op.sTxt);
   return this;
  }
  internal Rch insafter(int pos, Rch txt) { return upto(pos) + txt + after(pos); }
  internal Rch delafter(int pos) { return upto(pos); }
  internal Rch delafter_(int pos) { after(pos).del_(); return this; }
  internal Rch delafter(int pos, RchOp op) { return upto(pos) + after(pos).del(op.cnt); }
  internal Rch delafter_(int pos, RchOp op) { after(pos).del_(op.cnt); return this; }
  internal Rch rplafter(int pos, RchOp op) { return upto(pos) + op.sTxt; }
  internal Rch rplafter_(int pos, RchOp op) { return upto(pos) + op.sTxt; }
  internal Rch rplafter(int pos, Rch txt) { return upto(pos) + txt; }

  // ******************* (bool match, long occur, string chrs)

  internal Rch before(bool match, long occur, string chrs) { Rch ret = upto(match, occur, chrs); return ret.fitted ? ret : ret.upto(-2); }
  internal Rch before_(bool match, long occur, string chrs) { return cloneDiff(before(match, occur, chrs)); }
  internal Rch before__(bool match, long occur, string chrs) { return cloneDiff(before(match, occur, chrs), at(match, occur, chrs)); }
  internal Rch insbefore(bool match, long occur, string chrs, RchOp op) { return before(match, occur, chrs) + op.sTxt + from(match, occur, chrs); }
  internal Rch insbefore_(bool match, long occur, string chrs, RchOp op) { return from(match, occur, chrs).insbefore_(1, op); }
  internal Rch insbefore(bool match, long occur, string chrs, Rch txt) { return before(match, occur, chrs) + txt + from(match, occur, chrs); }
  internal Rch delbefore(bool match, long occur, string chrs) { return from(match, occur, chrs); }
  internal Rch delbefore_(bool match, long occur, string chrs) { before(match, occur, chrs).del_(); return this; }
  internal Rch delbefore(bool match, long occur, string chrs, RchOp op) { return before(match, occur, chrs).del(op.cnt) + from(match, occur, chrs); }
  internal Rch delbefore_(bool match, long occur, string chrs, RchOp op) { before(match, occur, chrs).del_(op.cnt); return this; }
  internal Rch rplbefore(bool match, long occur, string chrs, RchOp op) { return op.sTxt + from(match, occur, chrs); }
  internal Rch rplbefore_(bool match, long occur, string chrs, RchOp op) { return op.sTxt + from(match, occur, chrs); }
  internal Rch rplbefore(bool match, long occur, string chrs, Rch txt) { return txt + from(match, occur, chrs); }

  internal Rch upto(bool match, long occur, string chrs)                                                                                  //symmetric implementation fromChr and uptpChr use each other in case the search - heading in inappropriate for one ob both
  {
   if (occur > 0) { Rch diff = from(match, occur, chrs); return (diff.fitted) ? this - diff : this - diff.from(2); }
   for (int i = len; i >= 1; i--) if ((!match) ^ (chrs.IndexOf(this[i, false]) > -1)) return (occur == -1) ? upto(i) : upto(i - 1).upto(match, occur + 1, chrs);
   return fit(upto(0));
  }
  internal Rch upto_(bool match, long occur, string chrs) { return cloneDiff(upto(match, occur, chrs)); }
  internal Rch upto__(bool match, long occur, string chrs) { return cloneDiff(upto(match, occur, chrs), at(match, occur, chrs)); }
  internal Rch delupto(bool match, long occur, string chrs) { return after(match, occur, chrs); }
  internal Rch delupto_(bool match, long occur, string chrs) { upto(match, occur, chrs).del_(); return this; }
  internal Rch delupto(bool match, long occur, string chrs, RchOp op) { return upto(match, occur, chrs).del(op.cnt) + after(match, occur, chrs); }
  internal Rch delupto_(bool match, long occur, string chrs, RchOp op) { upto(match, occur, chrs).del_(op.cnt); return this; }
  internal Rch rplupto(bool match, long occur, string chrs, RchOp op) { return op.sTxt + after(match, occur, chrs); }
  internal Rch rplupto_(bool match, long occur, string chrs, RchOp op) { return op.sTxt + after(match, occur, chrs); }
  internal Rch rplupto(bool match, long occur, string chrs, Rch txt) { return txt + after(match, occur, chrs); }

  internal Rch at(bool match, long occur, string chrs) { return (occur > 0) ? from(match, occur, chrs).upto(1) : upto(match, occur, chrs).from(-1); }
  internal Rch at_(bool match, long occur, string chrs) { return cloneDiff(at(match, occur, chrs)); }
  internal Rch at__(bool match, long occur, string chrs) { return cloneDiff(at(match, occur, chrs), at(match, occur, chrs)); }
  internal Rch delat(bool match, long occur, string chrs) { return before(match, occur, chrs) + after(match, occur, chrs); }
  internal Rch delat_(bool match, long occur, string chrs) { at(match, occur, chrs).del_(); return this; }
  internal Rch delat(bool match, long occur, string chrs, RchOp op) { return before(match, occur, chrs) + at(match, occur, chrs).del(op.cnt) + after(match, occur, chrs); }
  internal Rch delat_(bool match, long occur, string chrs, RchOp op) { at(match, occur, chrs).del_(op.cnt); return this; }
  internal Rch rplat(bool match, long occur, string chrs, RchOp op) { return before(match, occur, chrs) + op.sTxt + after(match, occur, chrs); }
  internal Rch rplat_(bool match, long occur, string chrs, RchOp op) { return before(match, occur, chrs) + op.sTxt + after(match, occur, chrs); }
  internal Rch rplat(bool match, long occur, string chrs, Rch txt) { return before(match, occur, chrs) + txt + after(match, occur, chrs); }

  internal Rch from(bool match, long occur, string chrs)                                                                                  //symmetric implementation fromChr and uptpChr use each other in case the search - heading in inappropriate for one ob both
  {
   if (occur < 0) { Rch diff = upto(match, occur, chrs); return (diff.fitted) ? this - diff : this - diff.upto(-2); }
   for (int i = 1; i <= len; i++) if ((!match) ^ (chrs.IndexOf(this[i, false]) > -1)) return (occur == 1) ? from(i) : from(i + 1).from(match, occur - 1, chrs);
   return fit(from(len + 1));
  }
  internal Rch from_(bool match, long occur, string chrs) { return cloneDiff(from(match, occur, chrs)); }
  internal Rch from__(bool match, long occur, string chrs) { return cloneDiff(from(match, occur, chrs), at(match, occur, chrs)); }
  internal Rch delfrom(bool match, long occur, string chrs) { return before(match, occur, chrs); }
  internal Rch delfrom_(bool match, long occur, string chrs) { from(match, occur, chrs).del_(); return this; }
  internal Rch delfrom(bool match, long occur, string chrs, RchOp op) { return before(match, occur, chrs) + from(match, occur, chrs).del(op.cnt); }
  internal Rch delfrom_(bool match, long occur, string chrs, RchOp op) { from(match, occur, chrs).del_(op.cnt); return this; }
  internal Rch rplfrom(bool match, long occur, string chrs, RchOp op) { return before(match, occur, chrs) + op.sTxt; }
  internal Rch rplfrom_(bool match, long occur, string chrs, RchOp op) { return before(match, occur, chrs) + op.sTxt; }
  internal Rch rplfrom(bool match, long occur, string chrs, Rch txt) { return before(match, occur, chrs) + txt; }

  internal Rch after(bool match, long occur, string chrs) { Rch ret = from(match, occur, chrs); return ret.fitted ? ret : ret.from(2); }
  internal Rch after_(bool match, long occur, string chrs) { return cloneDiff(after(match, occur, chrs)); }
  internal Rch after__(bool match, long occur, string chrs) { return cloneDiff(after(match, occur, chrs), at(match, occur, chrs)); }
  internal Rch insafter(bool match, long occur, string chrs, RchOp op) { return upto(match, occur, chrs) + op.sTxt + after(match, occur, chrs); }
  internal Rch insafter_(bool match, long occur, string chrs, RchOp op) { return upto(match, occur, chrs).insafter_(-1, op); }
  internal Rch insafter(bool match, long occur, string chrs, Rch txt) { return upto(match, occur, chrs) + txt + after(match, occur, chrs); }
  internal Rch delafter(bool match, long occur, string chrs) { return upto(match, occur, chrs); }
  internal Rch delafter_(bool match, long occur, string chrs) { after(match, occur, chrs).del_(); return this; }
  internal Rch delafter(bool match, long occur, string chrs, RchOp op) { return upto(match, occur, chrs) + after(match, occur, chrs).del(op.cnt); }
  internal Rch delafter_(bool match, long occur, string chrs, RchOp op) { after(match, occur, chrs).del_(op.cnt); return this; }
  internal Rch rplafter(bool match, long occur, string chrs, RchOp op) { return upto(match, occur, chrs) + op.sTxt; }
  internal Rch rplafter_(bool match, long occur, string chrs, RchOp op) { return upto(match, occur, chrs) + op.sTxt; }
  internal Rch rplafter(bool match, long occur, string chrs, Rch txt) { return upto(match, occur, chrs) + txt; }

  // ******************* (bool match, long occur, string chrs)

  internal Rch Before(bool match, long occur, string chrs) { Rch ret = Upto(match, occur, chrs); return ret.fitted ? ret : ret.upto(-2); }
  internal Rch Before_(bool match, long occur, string chrs) { return cloneDiff(Before(match, occur, chrs)); }
  internal Rch Before__(bool match, long occur, string chrs) { return cloneDiff(Before(match, occur, chrs), At(match, occur, chrs)); }
  internal Rch insBefore(bool match, long occur, string chrs, RchOp op) { return Before(match, occur, chrs) + op.sTxt + From(match, occur, chrs); }
  internal Rch insBefore_(bool match, long occur, string chrs, RchOp op) { return From(match, occur, chrs).insbefore_(1, op); }
  internal Rch insBefore(bool match, long occur, string chrs, Rch txt) { return Before(match, occur, chrs) + txt + From(match, occur, chrs); }
  internal Rch delBefore(bool match, long occur, string chrs) { return From(match, occur, chrs); }
  internal Rch delBefore_(bool match, long occur, string chrs) { Before(match, occur, chrs).del_(); return this; }
  internal Rch delBefore(bool match, long occur, string chrs, RchOp op) { return Before(match, occur, chrs).del(op.cnt) + From(match, occur, chrs); }
  internal Rch delBefore_(bool match, long occur, string chrs, RchOp op) { Before(match, occur, chrs).del_(op.cnt); return this; }
  internal Rch rplBefore(bool match, long occur, string chrs, RchOp op) { return op.sTxt + From(match, occur, chrs); }
  internal Rch rplBefore_(bool match, long occur, string chrs, RchOp op) { return op.sTxt + From(match, occur, chrs); }
  internal Rch rplBefore(bool match, long occur, string chrs, Rch txt) { return txt + From(match, occur, chrs); }

  internal Rch Upto(bool match, long occur, string chrs)                                                                                  //symmetric implementation fromChr and uptpChr use each other in case the search - heading in inappropriate for one ob both
  {
   if (occur > 0) { Rch diff = From(match, occur, chrs); return (diff.fitted) ? this - diff : this - diff.from(2); }
   chrs = chrs.ToUpper();
   for (int i = len; i >= 1; i--) if ((!match) ^ (chrs.IndexOf(this[i, true]) > -1)) return (occur == -1) ? upto(i) : upto(i - 1).upto(match, occur + 1, chrs);
   return fit(upto(0));
  }
  internal Rch Upto_(bool match, long occur, string chrs) { return cloneDiff(Upto(match, occur, chrs)); }
  internal Rch Upto__(bool match, long occur, string chrs) { return cloneDiff(Upto(match, occur, chrs), At(match, occur, chrs)); }
  internal Rch delUpto(bool match, long occur, string chrs) { return After(match, occur, chrs); }
  internal Rch delUpto_(bool match, long occur, string chrs) { Upto(match, occur, chrs).del_(); return this; }
  internal Rch delUpto(bool match, long occur, string chrs, RchOp op) { return Upto(match, occur, chrs).del(op.cnt) + After(match, occur, chrs); }
  internal Rch delUpto_(bool match, long occur, string chrs, RchOp op) { Upto(match, occur, chrs).del_(op.cnt); return this; }
  internal Rch rplUpto(bool match, long occur, string chrs, RchOp op) { return op.sTxt + After(match, occur, chrs); }
  internal Rch rplUpto_(bool match, long occur, string chrs, RchOp op) { return op.sTxt + After(match, occur, chrs); }
  internal Rch rplUpto(bool match, long occur, string chrs, Rch txt) { return txt + After(match, occur, chrs); }

  internal Rch At(bool match, long occur, string chrs) { return (occur > 0) ? From(match, occur, chrs).upto(1) : Upto(match, occur, chrs).from(-1); }
  internal Rch At_(bool match, long occur, string chrs) { return cloneDiff(At(match, occur, chrs)); }
  internal Rch At__(bool match, long occur, string chrs) { return cloneDiff(At(match, occur, chrs), At(match, occur, chrs)); }
  internal Rch delAt(bool match, long occur, string chrs) { return Before(match, occur, chrs) + After(match, occur, chrs); }
  internal Rch delAt_(bool match, long occur, string chrs) { At(match, occur, chrs).del_(); return this; }
  internal Rch delAt(bool match, long occur, string chrs, RchOp op) { return Before(match, occur, chrs) + At(match, occur, chrs).del(op.cnt) + After(match, occur, chrs); }
  internal Rch delAt_(bool match, long occur, string chrs, RchOp op) { At(match, occur, chrs).del_(op.cnt); return this; }
  internal Rch rplAt(bool match, long occur, string chrs, RchOp op) { return Before(match, occur, chrs) + op.sTxt + After(match, occur, chrs); }
  internal Rch rplAt_(bool match, long occur, string chrs, RchOp op) { return Before(match, occur, chrs) + op.sTxt + After(match, occur, chrs); }
  internal Rch rplAt(bool match, long occur, string chrs, Rch txt) { return Before(match, occur, chrs) + txt + After(match, occur, chrs); }

  internal Rch From(bool match, long occur, string chrs)                                                                                  //symmetric implementation fromChr and uptpChr use each other in case the search - heading in inappropriate for one ob both
  {
   if (occur < 0) { Rch diff = Upto(match, occur, chrs); return (diff.fitted) ? this - diff : this - diff.upto(-2); }
   chrs = chrs.ToUpper();
   for (int i = 1; i <= len; i++) if ((!match) ^ (chrs.IndexOf(this[i, true]) > -1)) return (occur == 1) ? from(i) : from(i + 1).from(match, occur - 1, chrs);
   return fit(from(len + 1));
  }
  internal Rch From_(bool match, long occur, string chrs) { return cloneDiff(From(match, occur, chrs)); }
  internal Rch From__(bool match, long occur, string chrs) { return cloneDiff(From(match, occur, chrs), At(match, occur, chrs)); }
  internal Rch delFrom(bool match, long occur, string chrs) { return Before(match, occur, chrs); }
  internal Rch delFrom_(bool match, long occur, string chrs) { From(match, occur, chrs).del_(); return this; }
  internal Rch delFrom(bool match, long occur, string chrs, RchOp op) { return Before(match, occur, chrs) + From(match, occur, chrs).del(op.cnt); }
  internal Rch delFrom_(bool match, long occur, string chrs, RchOp op) { From(match, occur, chrs).del_(op.cnt); return this; }
  internal Rch rplFrom(bool match, long occur, string chrs, RchOp op) { return Before(match, occur, chrs) + op.sTxt; }
  internal Rch rplFrom_(bool match, long occur, string chrs, RchOp op) { return Before(match, occur, chrs) + op.sTxt; }
  internal Rch rplFrom(bool match, long occur, string chrs, Rch txt) { return Before(match, occur, chrs) + txt; }

  internal Rch After(bool match, long occur, string chrs) { Rch ret = From(match, occur, chrs); return ret.fitted ? ret : ret.from(2); }
  internal Rch After_(bool match, long occur, string chrs) { return cloneDiff(After(match, occur, chrs)); }
  internal Rch After__(bool match, long occur, string chrs) { return cloneDiff(After(match, occur, chrs), At(match, occur, chrs)); }
  internal Rch insAfter(bool match, long occur, string chrs, RchOp op) { return Upto(match, occur, chrs) + op.sTxt + After(match, occur, chrs); }
  internal Rch insAfter_(bool match, long occur, string chrs, RchOp op) { return Upto(match, occur, chrs).insafter_(-1, op); }
  internal Rch insAfter(bool match, long occur, string chrs, Rch txt) { return Upto(match, occur, chrs) + txt + After(match, occur, chrs); }
  internal Rch delAfter(bool match, long occur, string chrs) { return Upto(match, occur, chrs); }
  internal Rch delAfter_(bool match, long occur, string chrs) { After(match, occur, chrs).del_(); return this; }
  internal Rch delAfter(bool match, long occur, string chrs, RchOp op) { return Upto(match, occur, chrs) + After(match, occur, chrs).del(op.cnt); }
  internal Rch delAfter_(bool match, long occur, string chrs, RchOp op) { After(match, occur, chrs).del_(op.cnt); return this; }
  internal Rch rplAfter(bool match, long occur, string chrs, RchOp op) { return Upto(match, occur, chrs) + op.sTxt; }
  internal Rch rplAfter_(bool match, long occur, string chrs, RchOp op) { return Upto(match, occur, chrs) + op.sTxt; }
  internal Rch rplAfter(bool match, long occur, string chrs, Rch txt) { return Upto(match, occur, chrs) + txt; }

  // ******************* (          long occur, string token)

  internal Rch before(long occur, string token) { return upto(occur, token) - from(occur, token); }
  internal Rch before_(long occur, string token) { return cloneDiff(before(occur, token)); }
  internal Rch before__(long occur, string token) { return cloneDiff(before(occur, token), at(occur, token)); }
  internal Rch insbefore(RchOp op, long occur, string token) { return before(occur, token) + op.sTxt + from(occur, token); }
  internal Rch insbefore_(RchOp op, long occur, string token) { return from(occur, token).insbefore_(1, op); }
  internal Rch insbefore(Rch txt, long occur, string token) { return before(occur, token) + txt + from(occur, token); }
  internal Rch delbefore(long occur, string token) { return from(occur, token); }
  internal Rch delbefore_(long occur, string token) { before(occur, token).del_(); return this; }
  internal Rch delbefore(RchOp op, long occur, string token) { return before(occur, token).del(op.cnt) + from(occur, token); }
  internal Rch delbefore_(RchOp op, long occur, string token) { before(occur, token).del_(op.cnt); return this; }
  internal Rch rplbefore(RchOp op, long occur, string token) { return op.sTxt + from(occur, token); }
  internal Rch rplbefore_(RchOp op, long occur, string token) { return op.sTxt + from(occur, token); }
  internal Rch rplbefore(Rch txt, long occur, string token) { return txt + from(occur, token); }

  protected static bool xorTokens = false;

  internal Rch upto(long occur, string token)
  {
   if (token.Length == 0) return (occur > 0) ? upto((int)occur - 1) : upto(len + (int)occur + 1);
   if (occur > 0) { Rch diff = from(occur, token); return (diff.fitted) ? this - diff : this - diff.from(token.Length + 1); }
   for (int i = len; i >= token.Length; i--) if (token[0] == this[i, false]) if (upto(i + token.Length - 1).endsWith(token)) return (occur == -1) ? upto(i + token.Length - 1) : upto(i - 1).upto(occur + 1, token);
   return fit(upto(0));
  }

  internal Rch upto_(long occur, string token) { return cloneDiff(upto(occur, token)); }
  internal Rch upto__(long occur, string token) { return cloneDiff(upto(occur, token), at(occur, token)); }
  internal Rch delupto(long occur, string token) { return after(occur, token); }
  internal Rch delupto_(long occur, string token) { upto(occur, token).del_(); return this; }
  internal Rch delupto(RchOp op, long occur, string token) { return upto(occur, token).del(op.cnt) + after(occur, token); }
  internal Rch delupto_(RchOp op, long occur, string token) { upto(occur, token).del_(op.cnt); return this; }
  internal Rch rplupto(RchOp op, long occur, string token) { return op.sTxt + after(occur, token); }
  internal Rch rplupto_(RchOp op, long occur, string token) { return op.sTxt + after(occur, token); }
  internal Rch rplupto(Rch txt, long occur, string token) { return txt + after(occur, token); }

  internal Rch at(long occur, string token) { return (occur > 0) ? from(occur, token).upto(stepPos((int)occur, token.Length)) : upto(occur, token).from(stepPos((int)occur, token.Length)); }
  internal Rch at_(long occur, string token) { return cloneDiff(at(occur, token)); }
  internal Rch at__(long occur, string token) { return cloneDiff(at(occur, token), at(occur, token)); }
  internal Rch delat(long occur, string token) { return before(occur, token) + after(occur, token); }
  internal Rch delat_(long occur, string token) { at(occur, token).del_(); return this; }
  internal Rch delat(RchOp op, long occur, string token) { return before(occur, token) + at(occur, token).del(op.cnt) + after(occur, token); }
  internal Rch delat_(RchOp op, long occur, string token) { at(occur, token).del_(op.cnt); return this; }
  internal Rch rplat(RchOp op, long occur, string token) { return before(occur, token) + op.sTxt + after(occur, token); }
  internal Rch rplat_(RchOp op, long occur, string token) { return before(occur, token) + op.sTxt + after(occur, token); }
  internal Rch rplat(Rch txt, long occur, string token) { return before(occur, token) + txt + after(occur, token); }

  internal Rch from(long occur, string token)
  {
   if (token.Length == 0) return (occur > 0) ? from((int)occur) : from(len + (int)occur + 2);
   if (occur < 0) { Rch diff = upto(occur, token); return (diff.fitted) ? this - diff : this - diff.upto(-token.Length - 1); }
   for (int i = 1; i <= len - token.Length + 1; i++) if (token[0] == this[i, false]) if (from(i).startsWith(token)) return (occur == 1) ? from(i) : from(i + 1).from(occur - 1, token);
   return fit(from(len + 1));
  }

  internal Rch from_(long occur, string token) { return cloneDiff(from(occur, token)); }
  internal Rch from__(long occur, string token) { return cloneDiff(from(occur, token), at(occur, token)); }
  internal Rch delfrom(long occur, string token) { return before(occur, token); }
  internal Rch delfrom_(long occur, string token) { from(occur, token).del_(); return this; }
  internal Rch delfrom(RchOp op, long occur, string token) { return before(occur, token) + from(occur, token).del(op.cnt); }
  internal Rch delfrom_(RchOp op, long occur, string token) { from(occur, token).del_(op.cnt); return this; }
  internal Rch rplfrom(RchOp op, long occur, string token) { return before(occur, token) + op.sTxt; }
  internal Rch rplfrom_(RchOp op, long occur, string token) { return before(occur, token) + op.sTxt; }
  internal Rch rplfrom(Rch txt, long occur, string token) { return before(occur, token) + txt; }

  internal Rch after(long occur, string token) { return from(occur, token) - upto(occur, token); }
  internal Rch after_(long occur, string token) { return cloneDiff(after(occur, token)); }
  internal Rch after__(long occur, string token) { return cloneDiff(after(occur, token), at(occur, token)); }
  internal Rch insafter(RchOp op, long occur, string token) { return upto(occur, token) + op.sTxt + after(occur, token); }
  internal Rch insafter_(RchOp op, long occur, string token) { return upto(occur, token).insafter_(-1, op); }
  internal Rch insafter(Rch txt, long occur, string token) { return upto(occur, token) + txt + after(occur, token); }
  internal Rch delafter(long occur, string token) { return upto(occur, token); }
  internal Rch delafter_(long occur, string token) { after(occur, token).del_(); return this; }
  internal Rch delafter(RchOp op, long occur, string token) { return upto(occur, token) + after(occur, token).del(op.cnt); }
  internal Rch delafter_(RchOp op, long occur, string token) { after(occur, token).del_(op.cnt); return this; }
  internal Rch rplafter(RchOp op, long occur, string token) { return upto(occur, token) + op.sTxt; }
  internal Rch rplafter_(RchOp op, long occur, string token) { return upto(occur, token) + op.sTxt; }
  internal Rch rplafter(Rch txt, long occur, string token) { return upto(occur, token) + txt; }

  // ******************* (          long occur, string token)

  internal Rch Before(long occur, string token) { return Upto(occur, token) - From(occur, token); }
  internal Rch Before_(long occur, string token) { return cloneDiff(Before(occur, token)); }
  internal Rch Before__(long occur, string token) { return cloneDiff(Before(occur, token), At(occur, token)); }
  internal Rch insBefore(RchOp op, long occur, string token) { return Before(occur, token) + op.sTxt + From(occur, token); }
  internal Rch insBefore_(RchOp op, long occur, string token) { return From(occur, token).insbefore_(1, op); }
  internal Rch insBefore(Rch txt, long occur, string token) { return Before(occur, token) + txt + From(occur, token); }
  internal Rch delBefore(long occur, string token) { return From(occur, token); }
  internal Rch delBefore_(long occur, string token) { Before(occur, token).del_(); return this; }
  internal Rch delBefore(RchOp op, long occur, string token) { return Before(occur, token).del(op.cnt) + From(occur, token); }
  internal Rch delBefore_(RchOp op, long occur, string token) { Before(occur, token).del_(op.cnt); return this; }
  internal Rch rplBefore(RchOp op, long occur, string token) { return op.sTxt + From(occur, token); }
  internal Rch rplBefore_(RchOp op, long occur, string token) { return op.sTxt + From(occur, token); }
  internal Rch rplBefore(Rch txt, long occur, string token) { return txt + From(occur, token); }

  internal Rch Upto(long occur, string token)
  {
   if (token.Length == 0) return (occur > 0) ? upto((int)occur - 1) : upto(len + (int)occur + 1);
   if (occur > 0) { Rch diff = From(occur, token); return (diff.fitted) ? this - diff : this - diff.from(token.Length + 1); }
   token = token.ToUpper();
   for (int i = len; i >= token.Length; i--) if (token[0] == this[i, true]) if (upto(i + token.Length - 1).EndsWith(token)) return (occur == -1) ? upto(i + token.Length - 1) : upto(i - 1).upto(occur + 1, token);
   return fit(upto(0));
  }

  internal Rch Upto_(long occur, string token) { return cloneDiff(Upto(occur, token)); }
  internal Rch Upto__(long occur, string token) { return cloneDiff(Upto(occur, token), At(occur, token)); }
  internal Rch delUpto(long occur, string token) { return After(occur, token); }
  internal Rch delUpto_(long occur, string token) { Upto(occur, token).del_(); return this; }
  internal Rch delUpto(RchOp op, long occur, string token) { return Upto(occur, token).del(op.cnt) + After(occur, token); }
  internal Rch delUpto_(RchOp op, long occur, string token) { Upto(occur, token).del_(op.cnt); return this; }
  internal Rch rplUpto(RchOp op, long occur, string token) { return op.sTxt + After(occur, token); }
  internal Rch rplUpto_(RchOp op, long occur, string token) { return op.sTxt + After(occur, token); }
  internal Rch rplUpto(Rch txt, long occur, string token) { return txt + After(occur, token); }

  internal Rch At(long occur, string token) { return (occur > 0) ? From(occur, token).upto(stepPos(occur, token.Length)) : Upto(occur, token).from(stepPos(occur, token.Length)); }
  internal Rch At_(long occur, string token) { return cloneDiff(At(occur, token)); }
  internal Rch At__(long occur, string token) { return cloneDiff(At(occur, token), At(occur, token)); }
  internal Rch delAt(long occur, string token) { return Before(occur, token) + After(occur, token); }
  internal Rch delAt_(long occur, string token) { At(occur, token).del_(); return this; }
  internal Rch delAt(RchOp op, long occur, string token) { return Before(occur, token) + At(occur, token).del(op.cnt) + After(occur, token); }
  internal Rch delAt_(RchOp op, long occur, string token) { At(occur, token).del_(op.cnt); return this; }
  internal Rch rplAt(RchOp op, long occur, string token) { return Before(occur, token) + op.sTxt + After(occur, token); }
  internal Rch rplAt_(RchOp op, long occur, string token) { return Before(occur, token) + op.sTxt + After(occur, token); }
  internal Rch rplAt(Rch txt, long occur, string token) { return Before(occur, token) + txt + After(occur, token); }

  internal Rch From(long occur, string token)
  {
   if (token.Length == 0) return (occur > 0) ? from((int)occur) : from(len + (int)occur + 2);
   if (occur < 0) { Rch diff = Upto(occur, token); return (diff.fitted) ? this - diff : this - diff.upto(-token.Length - 1); }
   token = token.ToUpper();
   for (int i = 1; i <= len - token.Length + 1; i++) if (token[0] == this[i, true]) if (from(i).StartsWith(token)) return (occur == 1) ? from(i) : from(i + 1).from(occur - 1, token);
   return fit(from(len + 1));
  }

  internal Rch From_(long occur, string token) { return cloneDiff(From(occur, token)); }
  internal Rch From__(long occur, string token) { return cloneDiff(From(occur, token), At(occur, token)); }
  internal Rch delFrom(long occur, string token) { return Before(occur, token); }
  internal Rch delFrom_(long occur, string token) { From(occur, token).del_(); return this; }
  internal Rch delFrom(RchOp op, long occur, string token) { return Before(occur, token) + From(occur, token).del(op.cnt); }
  internal Rch delFrom_(RchOp op, long occur, string token) { From(occur, token).del_(op.cnt); return this; }
  internal Rch rplFrom(RchOp op, long occur, string token) { return Before(occur, token) + op.sTxt; }
  internal Rch rplFrom_(RchOp op, long occur, string token) { return Before(occur, token) + op.sTxt; }
  internal Rch rplFrom(Rch txt, long occur, string token) { return Before(occur, token) + txt; }

  internal Rch After(long occur, string token) { return From(occur, token) - Upto(occur, token); }
  internal Rch After_(long occur, string token) { return cloneDiff(After(occur, token)); }
  internal Rch After__(long occur, string token) { return cloneDiff(After(occur, token), at(occur, token)); }
  internal Rch insAfter(RchOp op, long occur, string token) { return Upto(occur, token) + op.sTxt + After(occur, token); }
  internal Rch insAfter_(RchOp op, long occur, string token) { return Upto(occur, token).insafter_(-1, op); }
  internal Rch insAfter(Rch txt, long occur, string token) { return Upto(occur, token) + txt + After(occur, token); }
  internal Rch delAfter(long occur, string token) { return Upto(occur, token); }
  internal Rch delAfter_(long occur, string token) { After(occur, token).del_(); return this; }
  internal Rch delAfter(RchOp op, long occur, string token) { return Upto(occur, token) + After(occur, token).del(op.cnt); }
  internal Rch delAfter_(RchOp op, long occur, string token) { After(occur, token).del_(op.cnt); return this; }
  internal Rch rplAfter(RchOp op, long occur, string token) { return Upto(occur, token) + op.sTxt; }
  internal Rch rplAfter_(RchOp op, long occur, string token) { return Upto(occur, token) + op.sTxt; }
  internal Rch rplAfter(Rch txt, long occur, string token) { return Upto(occur, token) + txt; }




  //protected static bool xorTokens = false;  // now: prio = false

  /*  the not-xor Implementation is imcomplete!
  *   In case of negative occurence iot does not work!
  *   The new idea is to use some expression  like this - after(...) in case negative occurence !!
  *   in general and most important the equations upto(...) = this.after(...) and from(..) = this - before(....) should hold and should be used for a the final dualic implementation in all slicing-Methods !!!
  */


  // ******************* (          long occur, bool prio, params string[] tokens)

  internal Rch before(long occur, bool prio, params string[] tokens) { return upto(occur, prio, tokens) - from(occur, prio, tokens); }
  internal Rch before_(long occur, bool prio, params string[] tokens) { return cloneDiff(before(occur, prio, tokens)); }
  internal Rch before__(long occur, bool prio, params string[] tokens) { return cloneDiff(before(occur, prio, tokens), at(occur, prio, tokens)); }
  internal Rch insbefore(RchOp op, long occur, bool prio, params string[] tokens) { return before(occur, prio, tokens) + op.sTxt + from(occur, prio, tokens); }
  internal Rch insbefore_(RchOp op, long occur, bool prio, params string[] tokens) { return from(occur, prio, tokens).insbefore_(1, op); }
  internal Rch insbefore(Rch txt, long occur, bool prio, params string[] tokens) { return before(occur, prio, tokens) + txt + from(occur, prio, tokens); }
  internal Rch delbefore(long occur, bool prio, params string[] tokens) { return from(occur, prio, tokens); }
  internal Rch delbefore_(long occur, bool prio, params string[] tokens) { before(occur, prio, tokens).del_(); return this; }
  internal Rch delbefore(RchOp op, long occur, bool prio, params string[] tokens) { return before(occur, prio, tokens).del(op.cnt) + from(occur, prio, tokens); }
  internal Rch delbefore_(RchOp op, long occur, bool prio, params string[] tokens) { before(occur, prio, tokens).del_(op.cnt); return this; }
  internal Rch rplbefore(RchOp op, long occur, bool prio, params string[] tokens) { return op.sTxt + from(occur, prio, tokens); }
  internal Rch rplbefore_(RchOp op, long occur, bool prio, params string[] tokens) { return op.sTxt + from(occur, prio, tokens); }
  internal Rch rplbefore(Rch txt, long occur, bool prio, params string[] tokens) { return txt + from(occur, prio, tokens); }

  internal Rch upto(long occur, bool prio, params string[] tokens)
  {
   if (tokens.Length == 1) return upto(occur, tokens[0]);
   Rch Ret = (occur > 0) ? upto(-1) : before(1);
   if (prio) { string tok = ""; foreach (string token in tokens) { Rch r = upto(occur, prio, token); if (!r.fitted) if (r.len - token.Length == Ret.len - tok.Length) { if (token.Length > tok.Length) { tok = token; Ret = r; } } else if (((occur > 0) && (r.len < Ret.len)) || ((occur < 0) && (r.len > Ret.len))) { tok = token; Ret = r; } } }
   else
    switch (occur)
    {
     case 1: { for (int i = 1; i <= tokens.Length; i++) if (at(1, prio, tokens[i - 1]).len > 0) { Rch r = upto(1, prio, tokens[i - 1]); if (r.len < Ret.len) Ret = r; } return Ret; }
     case -1: { for (int i = 1; i <= tokens.Length; i++) if (at(-1, prio, tokens[i - 1]).len > 0) { Rch r = upto(-1, prio, tokens[i - 1]); if (r.len > Ret.len) Ret = r; } return Ret; }
     default: { Ret = (occur > 0) ? before(1) : upto(-1) + " "; if (occur > 0) for (int i = 1; i <= occur; i++) Ret = Ret + after(Ret).upto(1, prio, tokens); else for (int i = -1; i >= occur; i--) Ret = Ret.upto(-2).upto(-1, prio, tokens); } break;
    }
   return Ret;
  }

  internal Rch upto_(long occur, bool prio, params string[] tokens) { return cloneDiff(upto(occur, prio, tokens)); }
  internal Rch upto__(long occur, bool prio, params string[] tokens) { return cloneDiff(upto(occur, prio, tokens), at(occur, prio, tokens)); }
  internal Rch delupto(long occur, bool prio, params string[] tokens) { return after(occur, prio, tokens); }
  internal Rch delupto_(long occur, bool prio, params string[] tokens) { upto(occur, prio, tokens).del_(); return this; }
  internal Rch delupto(RchOp op, long occur, bool prio, params string[] tokens) { return upto(occur, prio, tokens).del(op.cnt) + after(occur, prio, tokens); }
  internal Rch delupto_(RchOp op, long occur, bool prio, params string[] tokens) { upto(occur, prio, tokens).del_(op.cnt); return this; }
  internal Rch rplupto(RchOp op, long occur, bool prio, params string[] tokens) { return op.sTxt + after(occur, prio, tokens); }
  internal Rch rplupto_(RchOp op, long occur, bool prio, params string[] tokens) { return op.sTxt + after(occur, prio, tokens); }
  internal Rch rplupto(Rch txt, long occur, bool prio, params string[] tokens) { return txt + after(occur, prio, tokens); }

  internal Rch at(long occur, bool prio, params string[] tokens) { if (tokens.Length == 1) return at(occur, tokens[0]); return this - before(occur, prio, tokens) - after(occur, prio, tokens); }
  internal Rch at_(long occur, bool prio, params string[] tokens) { return cloneDiff(at(occur, prio, tokens)); }
  internal Rch at__(long occur, bool prio, params string[] tokens) { return cloneDiff(at(occur, prio, tokens), at(occur, prio, tokens)); }
  internal Rch delat(long occur, bool prio, params string[] tokens) { return before(occur, prio, tokens) + after(occur, prio, tokens); }
  internal Rch delat_(long occur, bool prio, params string[] tokens) { at(occur, prio, tokens).del_(); return this; }
  internal Rch delat(RchOp op, long occur, bool prio, params string[] tokens) { return before(occur, prio, tokens) + at(occur, prio, tokens).del(op.cnt) + after(occur, prio, tokens); }
  internal Rch delat_(RchOp op, long occur, bool prio, params string[] tokens) { at(occur, prio, tokens).del_(op.cnt); return this; }
  internal Rch rplat(RchOp op, long occur, bool prio, params string[] tokens) { return before(occur, prio, tokens) + op.sTxt + after(occur, prio, tokens); }
  internal Rch rplat_(RchOp op, long occur, bool prio, params string[] tokens) { return before(occur, prio, tokens) + op.sTxt + after(occur, prio, tokens); }
  internal Rch rplat(Rch txt, long occur, bool prio, params string[] tokens) { return before(occur, prio, tokens) + txt + after(occur, prio, tokens); }

  internal Rch from(long occur, bool prio, params string[] tokens)
  {
   if (tokens.Length == 1) return from(occur, tokens[0]);
   Rch Ret = (occur > 0) ? after(-1) : from(1);
   if (prio) { string tok = ""; foreach (string token in tokens) { Rch r = from(occur, prio, token); if (!r.fitted) if (r.len - token.Length == Ret.len - tok.Length) { if (token.Length > tok.Length) { tok = token; Ret = r; } } else if (((occur > 0) && (r.len > Ret.len)) || ((occur < 0) && (r.len < Ret.len))) { tok = token; Ret = r; } } }
   else
    switch (occur)
    {
     case 1: { for (int i = 1; i <= tokens.Length; i++) if (at(1, prio, tokens[i - 1]).len > 0) { Rch r = from(1, prio, tokens[i - 1]); if (r.len > Ret.len) Ret = r; } return Ret; }
     case -1: { for (int i = 1; i <= tokens.Length; i++) if (at(-1, prio, tokens[i - 1]).len > 0) { Rch r = from(-1, prio, tokens[i - 1]); if (r.len < Ret.len) Ret = r; } return Ret; }
     default: { Ret = (occur > 0) ? " " + from(1) : after(-1); if (occur > 0) for (int i = 1; i <= occur; i++) Ret = Ret.from(2).from(1, prio, tokens); else for (int i = -1; i >= occur; i--) Ret = (this - Ret).from(-1, prio, tokens) + Ret; } break;
    }
   return Ret;
  }

  internal Rch from_(long occur, bool prio, params string[] tokens) { return cloneDiff(from(occur, prio, tokens)); }
  internal Rch from__(long occur, bool prio, params string[] tokens) { return cloneDiff(from(occur, prio, tokens), at(occur, prio, tokens)); }
  internal Rch delfrom(long occur, bool prio, params string[] tokens) { return before(occur, prio, tokens); }
  internal Rch delfrom_(long occur, bool prio, params string[] tokens) { from(occur, prio, tokens).del_(); return this; }
  internal Rch delfrom(RchOp op, long occur, bool prio, params string[] tokens) { return before(occur, prio, tokens) + from(occur, prio, tokens).del(op.cnt); }
  internal Rch delfrom_(RchOp op, long occur, bool prio, params string[] tokens) { from(occur, prio, tokens).del_(op.cnt); return this; }
  internal Rch rplfrom(RchOp op, long occur, bool prio, params string[] tokens) { return before(occur, prio, tokens) + op.sTxt; }
  internal Rch rplfrom_(RchOp op, long occur, bool prio, params string[] tokens) { return before(occur, prio, tokens) + op.sTxt; }
  internal Rch rplfrom(Rch txt, long occur, bool prio, params string[] tokens) { return before(occur, prio, tokens) + txt; }

  internal Rch after(long occur, bool prio, params string[] tokens) { return from(occur, prio, tokens) - upto(occur, prio, tokens); }
  internal Rch after_(long occur, bool prio, params string[] tokens) { return cloneDiff(after(occur, prio, tokens)); }
  internal Rch after__(long occur, bool prio, params string[] tokens) { return cloneDiff(after(occur, prio, tokens), at(occur, prio, tokens)); }
  internal Rch insafter(RchOp op, long occur, bool prio, params string[] tokens) { return upto(occur, prio, tokens) + op.sTxt + after(occur, prio, tokens); }
  internal Rch insafter_(RchOp op, long occur, bool prio, params string[] tokens) { return upto(occur, prio, tokens).insafter_(-1, op); }
  internal Rch insafter(Rch txt, long occur, bool prio, params string[] tokens) { return upto(occur, prio, tokens) + txt + after(occur, prio, tokens); }
  internal Rch delafter(long occur, bool prio, params string[] tokens) { return upto(occur, prio, tokens); }
  internal Rch delafter_(long occur, bool prio, params string[] tokens) { after(occur, prio, tokens).del_(); return this; }
  internal Rch delafter(RchOp op, long occur, bool prio, params string[] tokens) { return upto(occur, prio, tokens) + after(occur, prio, tokens).del(op.cnt); }
  internal Rch delafter_(RchOp op, long occur, bool prio, params string[] tokens) { after(occur, prio, tokens).del_(op.cnt); return this; }
  internal Rch rplafter(RchOp op, long occur, bool prio, params string[] tokens) { return upto(occur, prio, tokens) + op.sTxt; }
  internal Rch rplafter_(RchOp op, long occur, bool prio, params string[] tokens) { return upto(occur, prio, tokens) + op.sTxt; }
  internal Rch rplafter(Rch txt, long occur, bool prio, params string[] tokens) { return upto(occur, prio, tokens) + txt; }

  // ******************* (          long occur, bool prio, params string[] tokens)

  internal Rch Before(long occur, bool prio, params string[] tokens) { return Upto(occur, prio, tokens) - From(occur, prio, tokens); }
  internal Rch Before_(long occur, bool prio, params string[] tokens) { return cloneDiff(Before(occur, prio, tokens)); }
  internal Rch Before__(long occur, bool prio, params string[] tokens) { return cloneDiff(Before(occur, prio, tokens), At(occur, prio, tokens)); }
  internal Rch insBefore(RchOp op, long occur, bool prio, params string[] tokens) { return Before(occur, prio, tokens) + op.sTxt + From(occur, prio, tokens); }
  internal Rch insBefore_(RchOp op, long occur, bool prio, params string[] tokens) { return From(occur, prio, tokens).insbefore_(1, op); }
  internal Rch insBefore(Rch txt, long occur, bool prio, params string[] tokens) { return Before(occur, prio, tokens) + txt + From(occur, prio, tokens); }
  internal Rch delBefore(long occur, bool prio, params string[] tokens) { return From(occur, prio, tokens); }
  internal Rch delBefore_(long occur, bool prio, params string[] tokens) { Before(occur, prio, tokens).del_(); return this; }
  internal Rch delBefore(RchOp op, long occur, bool prio, params string[] tokens) { return Before(occur, prio, tokens).del(op.cnt) + From(occur, prio, tokens); }
  internal Rch delBefore_(RchOp op, long occur, bool prio, params string[] tokens) { Before(occur, prio, tokens).del_(op.cnt); return this; }
  internal Rch rplBefore(RchOp op, long occur, bool prio, params string[] tokens) { return op.sTxt + From(occur, prio, tokens); }
  internal Rch rplBefore_(RchOp op, long occur, bool prio, params string[] tokens) { return op.sTxt + From(occur, prio, tokens); }
  internal Rch rplBefore(Rch txt, long occur, bool prio, params string[] tokens) { return txt + From(occur, prio, tokens); }

  internal Rch Upto(long occur, bool prio, params string[] tokens)
  {
   if (tokens.Length == 1) return Upto(occur, tokens[0]);
   Rch Ret = (occur > 0) ? upto(-1) : before(1);
   if (prio) { string tok = ""; foreach (string token in tokens) { Rch r = Upto(occur, prio, token); if (!r.fitted) if (r.len - token.Length == Ret.len - tok.Length) { if (token.Length > tok.Length) { tok = token; Ret = r; } } else if (((occur > 0) && (r.len < Ret.len)) || ((occur < 0) && (r.len > Ret.len))) { tok = token; Ret = r; } } }
   else
    switch (occur)
    {
     case 1: { for (int i = 1; i <= tokens.Length; i++) if (At(1, prio, tokens[i - 1]).len > 0) { Rch r = Upto(1, prio, tokens[i - 1]); if (r.len < Ret.len) Ret = r; } return Ret; }
     case -1: { for (int i = 1; i <= tokens.Length; i++) if (At(-1, prio, tokens[i - 1]).len > 0) { Rch r = Upto(-1, prio, tokens[i - 1]); if (r.len > Ret.len) Ret = r; } return Ret; }
     default: { Ret = (occur > 0) ? before(1) : upto(-1) + " "; if (occur > 0) for (int i = 1; i <= occur; i++) Ret = Ret + after(Ret).Upto(1, prio, tokens); else for (int i = -1; i >= occur; i--) Ret = Ret.upto(-2).Upto(-1, prio, tokens); } break;
    }
   return Ret;
  }
  internal Rch Upto_(long occur, bool prio, params string[] tokens) { return cloneDiff(Upto(occur, prio, tokens)); }
  internal Rch Upto__(long occur, bool prio, params string[] tokens) { return cloneDiff(Upto(occur, prio, tokens), At(occur, prio, tokens)); }
  internal Rch delUpto(long occur, bool prio, params string[] tokens) { return After(occur, prio, tokens); }
  internal Rch delUpto_(long occur, bool prio, params string[] tokens) { Upto(occur, prio, tokens).del_(); return this; }
  internal Rch delUpto(RchOp op, long occur, bool prio, params string[] tokens) { return Upto(occur, prio, tokens).del(op.cnt) + After(occur, prio, tokens); }
  internal Rch delUpto_(RchOp op, long occur, bool prio, params string[] tokens) { Upto(occur, prio, tokens).del_(op.cnt); return this; }
  internal Rch rplUpto(RchOp op, long occur, bool prio, params string[] tokens) { return op.sTxt + After(occur, prio, tokens); }
  internal Rch rplUpto_(RchOp op, long occur, bool prio, params string[] tokens) { return op.sTxt + After(occur, prio, tokens); }
  internal Rch rplUpto(Rch txt, long occur, bool prio, params string[] tokens) { return txt + After(occur, prio, tokens); }

  internal Rch At(long occur, bool prio, params string[] tokens) { if (tokens.Length == 1) return At(occur, tokens[0]); return this - Before(occur, prio, tokens) - After(occur, prio, tokens); }
  internal Rch At_(long occur, bool prio, params string[] tokens) { return cloneDiff(At(occur, prio, tokens)); }
  internal Rch At__(long occur, bool prio, params string[] tokens) { return cloneDiff(At(occur, prio, tokens), At(occur, prio, tokens)); }
  internal Rch delAt(long occur, bool prio, params string[] tokens) { return Before(occur, prio, tokens) + After(occur, prio, tokens); }
  internal Rch delAt_(long occur, bool prio, params string[] tokens) { At(occur, prio, tokens).del_(); return this; }
  internal Rch delAt(RchOp op, long occur, bool prio, params string[] tokens) { return Before(occur, prio, tokens) + At(occur, prio, tokens).del(op.cnt) + After(occur, prio, tokens); }
  internal Rch delAt_(RchOp op, long occur, bool prio, params string[] tokens) { At(occur, prio, tokens).del_(op.cnt); return this; }
  internal Rch rplAt(RchOp op, long occur, bool prio, params string[] tokens) { return Before(occur, prio, tokens) + op.sTxt + After(occur, prio, tokens); }
  internal Rch rplAt_(RchOp op, long occur, bool prio, params string[] tokens) { return Before(occur, prio, tokens) + op.sTxt + After(occur, prio, tokens); }
  internal Rch rplAt(Rch txt, long occur, bool prio, params string[] tokens) { return Before(occur, prio, tokens) + txt + After(occur, prio, tokens); }

  internal Rch From(long occur, bool prio, params string[] tokens)
  {
   if (tokens.Length == 1) return From(occur, tokens[0]);
   Rch Ret = (occur > 0) ? after(-1) : from(1);
   if (prio) { string tok = ""; foreach (string token in tokens) { Rch r = From(occur, prio, token); if (!r.fitted) if (r.len - token.Length == Ret.len - tok.Length) { if (token.Length > tok.Length) { tok = token; Ret = r; } } else if (((occur > 0) && (r.len > Ret.len)) || ((occur < 0) && (r.len < Ret.len))) { tok = token; Ret = r; } } }
   else
    switch (occur)
    {
     case 1: { for (int i = 1; i <= tokens.Length; i++) if (At(1, prio, tokens[i - 1]).len > 0) { Rch r = From(1, prio, tokens[i - 1]); if (r.len > Ret.len) Ret = r; } return Ret; }
     case -1: { for (int i = 1; i <= tokens.Length; i++) if (At(-1, prio, tokens[i - 1]).len > 0) { Rch r = From(-1, prio, tokens[i - 1]); if (r.len < Ret.len) Ret = r; } return Ret; }
     default: { Ret = (occur > 0) ? " " + from(1) : after(-1); if (occur > 0) for (int i = 1; i <= occur; i++) Ret = Ret.from(2).From(1, prio, tokens); else for (int i = -1; i >= occur; i--) Ret = (this - Ret).From(-1, prio, tokens) + Ret; } break;
    }
   return Ret;
  }

  internal Rch From_(long occur, bool prio, params string[] tokens) { return cloneDiff(From(occur, prio, tokens)); }
  internal Rch From__(long occur, bool prio, params string[] tokens) { return cloneDiff(From(occur, prio, tokens), At(occur, prio, tokens)); }
  internal Rch delFrom(long occur, bool prio, params string[] tokens) { return Before(occur, prio, tokens); }
  internal Rch delFrom_(long occur, bool prio, params string[] tokens) { From(occur, prio, tokens).del_(); return this; }
  internal Rch delFrom(RchOp op, long occur, bool prio, params string[] tokens) { return Before(occur, prio, tokens) + From(occur, prio, tokens).del(op.cnt); }
  internal Rch delFrom_(RchOp op, long occur, bool prio, params string[] tokens) { From(occur, prio, tokens).del_(op.cnt); return this; }
  internal Rch rplFrom(RchOp op, long occur, bool prio, params string[] tokens) { return Before(occur, prio, tokens) + op.sTxt; }
  internal Rch rplFrom_(RchOp op, long occur, bool prio, params string[] tokens) { return Before(occur, prio, tokens) + op.sTxt; }
  internal Rch rplFrom(Rch txt, long occur, bool prio, params string[] tokens) { return Before(occur, prio, tokens) + txt; }

  internal Rch After(long occur, bool prio, params string[] tokens) { return From(occur, prio, tokens) - Upto(occur, prio, tokens); }
  internal Rch After_(long occur, bool prio, params string[] tokens) { return cloneDiff(After(occur, prio, tokens)); }
  internal Rch After__(long occur, bool prio, params string[] tokens) { return cloneDiff(After(occur, prio, tokens), at(occur, prio, tokens)); }
  internal Rch insAfter(RchOp op, long occur, bool prio, params string[] tokens) { return Upto(occur, prio, tokens) + op.sTxt + After(occur, prio, tokens); }
  internal Rch insAfter_(RchOp op, long occur, bool prio, params string[] tokens) { return Upto(occur, prio, tokens).insafter_(-1, op); }
  internal Rch insAfter(Rch txt, long occur, bool prio, params string[] tokens) { return Upto(occur, prio, tokens) + txt + After(occur, prio, tokens); }
  internal Rch delAfter(long occur, bool prio, params string[] tokens) { return Upto(occur, prio, tokens); }
  internal Rch delAfter_(long occur, bool prio, params string[] tokens) { After(occur, prio, tokens).del_(); return this; }
  internal Rch delAfter(RchOp op, long occur, bool prio, params string[] tokens) { return Upto(occur, prio, tokens) + After(occur, prio, tokens).del(op.cnt); }
  internal Rch delAfter_(RchOp op, long occur, bool prio, params string[] tokens) { After(occur, prio, tokens).del_(op.cnt); return this; }
  internal Rch rplAfter(RchOp op, long occur, bool prio, params string[] tokens) { return Upto(occur, prio, tokens) + op.sTxt; }
  internal Rch rplAfter_(RchOp op, long occur, bool prio, params string[] tokens) { return Upto(occur, prio, tokens) + op.sTxt; }
  internal Rch rplAfter(Rch txt, long occur, bool prio, params string[] tokens) { return Upto(occur, prio, tokens) + txt; }



  public Rch lTrim() { return after(firstWord(" ")); }
  public Rch rTrim() { return before(lastWord(" ")); }

  public bool equals(Rch r)
  {
   if (r == null) return false;
   if (r.len != len) return false;
   return r.equals(text);
  }

  public bool Equals(Rch r)
  {
   if (r == null) return false;
   if (r.len != len) return false;
   return r.Equals(text);
  }

  public bool equals(string s)
  {
   if (s == null) return (len == 0);
   if (s.Length == 0) return (len == 0);
   if (len != s.Length) return false;
   if (this[1, false] != s[0]) return false;
   if (this[-1, false] != s[s.Length - 1]) return false;
   //if (s.Length <= 3) return true;  //AttGeTr: weiss nicht was ich mir dabei gedacht habe, jedenfalls ergab der vergleich (" = " eq " ~ ") true !!!
   return text.Equals(s);
  }

  public bool Equals(string s)
  {
   if (s == null) return (len == 0);
   if (s.Length == 0) return (len == 0);
   if (len != s.Length) return false;
   s = s.ToUpper();
   //if (this[1]) != s[0]) return false;
   //if (this[-1] != s[s.Length - 1]) return false;
   //if (s.Length <= 3) return true;
   return uText.Equals(s);
  }


  /*
  public bool matches(string p)
  {
   if (p == null) return (len == 0);
   if (p.Length == 0) return (len == 0);
   
   
   if (this[1, false] != p[0]) return false;
   if (this[-1, false] != p[p.Length - 1]) return false;
   if (p.Length <= 3) return true;
   return text.Equals(p);
  }

  public bool Matches(string p)
  {
   if (p == null) return (len == 0);
   if (p.Length == 0) return (len == 0);
   
   p = p.ToUpper();
   //if (this[1]) != s[0]) return false;
   //if (this[-1] != s[s.Length - 1]) return false;
   //if (s.Length <= 3) return true;
   return Text.Equals(p);
  }
  */

  private Rch firstWord(string wordCharacters, bool igCase) { return before(false, 1, wordCharacters); }
  protected Rch firstWord(string wordCharacters) { return firstWord(wordCharacters, false); }
  protected Rch FirstWord(string wordCharacters) { return firstWord(wordCharacters.ToUpper(), true); }
  private Rch lastWord(string wordCharacters, bool igCase) { return after(false, -1, wordCharacters); }
  protected Rch lastWord(string wordCharacters) { return lastWord(wordCharacters, false); }
  protected Rch LastWord(string wordCharacters) { return lastWord(wordCharacters.ToUpper(), true); }

  protected bool startsWith(string token) { return upto(token.Length).text.Equals(token); }
  protected bool StartsWith(string token) { return upto(token.Length).uText.Equals(token.ToUpper()); }
  protected bool endsWith(string token) { return from(-token.Length).text.Equals(token); }
  protected bool EndsWith(string token) { return from(-token.Length).uText.Equals(token.ToUpper()); }

  protected bool startsWith(bool match, string chars) { return upto(1).upto(-1, match, chars).len > 0; }
  protected bool StartsWith(bool match, string chars) { return upto(1).Upto(-1, match, chars).len > 0; }
  protected bool endsWith(bool match, string chars) { return from(-1).from(1, match, chars).len > 0; }
  protected bool EndsWith(bool match, string chars) { return from(-1).From(1, match, chars).len > 0; }

  public override string ToString() { return text; }

 }




}









//** @author  GeTr Copyright© 1996-2020 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties
//** @license You may use, modify, distribute this code under GNU public dual licence free for NON-Commercial use as long as this notice remains unchanged.
//** @comment 1 based Enhanced ArrayList also supporting negative backward counting 


using System;
using System.Threading;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Collections;
using System.Windows.Forms;

using org_xxdevplus_sys;
using org_xxdevplus_utl;
using org_xxdevplus_struct;
using org_xxdevplus_chain;


namespace org_xxdevplus_struct
{


 public class Pile<typ> : IEnumerable<typ> //similar to array<typ> but Index starts at 1, not 0! Reverse index starts at -1. Moreover constructor takes a list of arguments.
 {
  public static long iic = 0;

  public virtual void visEdit(Object parentForm, params Object[] more) { }

  private static string ass(bool expr) { if (!expr) throw new Exception(ass(true) + " SelfTest Failure"); return "Pile<>"; } 
  private static void   ass(bool expr, string msg) { if (!expr) throw new Exception("Error in Pile.selfTest(): " + msg); }
  private static void   ass(string e1, string e2, string msg) { ass(e1.Equals(e2), msg); }
  private static void   ass(double e1, double e2, string msg) { ass(e1 == e2, msg); }
  private static void   tstSimple()
  {
   long i = 0;
   Pile<long> test = new Pile<long>();
   test.Add(1);
   test.Add(2);
   test.Add(3);
   test.Del(2);
   ass(test.Len, 2, "tstSimple.0");
   ass(test[1], 1, "tstSimple.1");
   ass(test[2], 3, "tstSimple.2");
   test.Del(1);
   ass(test[1], 3, "tstSimple.3");
   ass(test.Len, 1, "tstSimple.4");
  }

  private static void selfTest() { tstSimple(); }
  private void init() { if (iic++ == 0) selfTest(); }

  public   object           _name   = "";
  private  typ[]            val;

  public   string    Count     (        ) { return ""; } // block some methods of superclass IEnumerator
  //public   string    Contains  (object o) { return ""; } // block some methods of superclass IEnumerator
  //public   bool      hasItem  (typ                                  item)  { if (kim == null) { kim = new KeyInxMap<typ>(true); foreach (typ itm in val) kim.Add(item); } return kim.hasKey(item); } 

  public   Pile               (                                         )  { MethWatch mw = new MethWatch("!Pile"); init(); int count = 0; this.val = new typ[count]; mw._void(); }
  public   Pile               (int                                 count)  { init(); this.val = new typ[count]; }
  public   Pile               (bool dummy , params typ[]           array)  { init(); _name = ""; Add(array); }
  public   Pile               (bool dummy , List<typ>                val)  { init(); this._name = ""; this.val = new typ[val.Count]; for (int i = 1; i <= val.Count; i++) this.val[i - 1] = val[(int)i - 1]; }
  public   Pile               (string name, bool dummy, params typ[] val)  { init(); this._name = name; this.val = val; }
  public   Pile               (string name, bool dummy, List<typ>    val)  { init(); this._name = name; this.val = new typ[val.Count]; for (int i = 1; i <= val.Count; i++) this.val[i - 1] = val[(int)i - 1]; }

  public   string    Name                                                  { get { return (string) _name; } set { _name = value; }  }
  public   int       Len                                                   { get { if (val == null) return 0; return val.Length; } }
  public   typ       this     [int                                   pos]  { get { if (pos < 0) pos = val.Count() + pos + 1; return (pos == 0) ? (typ)_name : (typ)(val[pos - 1]); }  set { if (pos < 0) pos = val.Count() + pos + 1; if (pos == 0) _name = value; else val[pos - 1] = value; }  }

  public   Pile<typ> before   (int                                   inx)  { if (inx < 0) inx = val.Count() + inx + 1; Pile<typ> ret = new Pile<typ>(inx); ret._name = _name; for (int i = 1; i < ret.Len; i++) ret[i] = this[i]; return ret; }
  public   Pile<typ> upto     (int                                   inx)  { if (inx < 0) inx = val.Count()  + inx + 1; Pile<typ> ret = new Pile<typ>(inx); ret._name = _name; for (int i = 1; i <= ret.Len; i++) ret[i] = this[i]; return ret; }
  public   Pile<typ> at       (int                                   inx)  { if (inx < 0) inx = val.Count() + inx + 1; Pile<typ> ret = new Pile<typ>(1); ret._name = _name; ret[inx] = this[inx]; return ret; }
  public   Pile<typ> slice    (int from,                        int upto)  { if (from < 0) from = val.Count() + from + 1; if (upto < 0) upto = val.Count() + upto + 1; Pile<typ> ret = new Pile<typ>(1 + upto - from); ret._name = _name; for (int i = 1; i <= ret.Len; i++) ret[i] = this[i + from - 1]; return ret; }
  public   Pile<typ> from     (int                                   inx)  { if (inx < 0) inx = val.Count() + inx + 1; if (1 + val.Count() - inx < 0) return new Pile<typ>(); Pile<typ> ret = new Pile<typ>(1 + val.Count() - inx); ret._name = _name; for (int i = 1; i <= ret.Len; i++) ret[i] = this[i + inx - 1]; return ret; }
  public   Pile<typ> after    (int                                   inx)  { if (inx < 0) inx = val.Count()  + inx + 1; Pile<typ> ret = new Pile<typ>(1 + val.Count() - inx); ret._name = _name; for (int i = 2; i <= ret.Len; i++) ret[i] = this[i + inx - 1]; return ret; }
  public   Pile<typ> Add      (Pile<typ>                            more)  { typ[] res = new typ[Len + more.Len]; for (int i = 1; i <= val.Length; i++) res[i - 1] = val[i - 1]; for (int i = 1; i <= more.Len; i++) res[i + val.Length - 1] = more[i]; val = res; return this; }
  public   Pile<typ> Add      (params typ[]                         more)  { typ[] res = new typ[Len + more.Length]; for (int i = 1; i <= Len; i++) res[i - 1] = val[i - 1]; for (int i = 1; i <= more.Length; i++) res[i + Len - 1] = more[i - 1]; val = res; return this; }
  public   Pile<typ> Add      (List<typ>                            more)  { typ[] res = new typ[Len + more.Count]; for (int i = 1; i <= val.Length; i++) res[i - 1] = val[i - 1]; for (int i = 1; i <= more.Count; i++) res[i + val.Length - 1] = more[(int)i]; val = res; return this; }
  public   typ       Push     (typ                                  next)  { Add(next); return next; }
  public   typ       Pop      (                                         )  { typ ret = this[-1]; Del(-1); return ret; }
  public   void      Clear    (                                         )  { val = new typ[0]; }
  public   Pile<typ> Del      (int                                   inx)  { if (inx < 0) inx = val.Count() + inx + 1; typ[] _val = new typ[val.Length - 1]; for (int i = 0; i < inx - 1; i++) _val[i] = val[i]; for (int i = inx - 1; i < _val.Length; i++) _val[i] = val[i + 1]; val = _val; return this; }
  internal void      Sort     (                                         )  { Array.Sort(val); }
  public   Pile<typ> Clone    (                                         )  { Pile<typ> ret = new Pile<typ>(Len); ret._name = _name; for (int i = 1; i <= Len; i++) ret[i] = this[i]; return ret; }
  public   typ[]     array    (                                         )  { return val; }

  public   override string    ToString (                                         )  
  {
   string ret = "";
   foreach (typ item in this) ret += item.ToString() + "\r\n";
   return (ret.Length > 0) ? ret.Substring(0, ret.Length - 2) : "";
  }


  public   IEnumerator<typ> GetEnumerator() { return new PileEnumerator(this); }
  IEnumerator IEnumerable.GetEnumerator() { return GetEnumerator(); }
  public class PileEnumerator: IEnumerator<typ>
  {
   private Pile<typ>       coll;
   private typ             current;
   private int             index;
   public                  PileEnumerator      (Pile<typ> coll) { Monitor.Enter(coll.val.SyncRoot); this.index = -1; this.coll = coll; }
   public  typ             Current                              { get { return current; } }
           object          IEnumerator.Current                  { get { return Current; } }
   public bool             MoveNext            (              ) { if (++index >= coll.val.Length) { return false; } else { current = coll.val[index]; return true; } }
   public void             Reset               (              ) { current = default(typ); index = 0; }
   public void             Dispose             (              ) { try { current = default(typ); index = coll.val.Length; } finally { Monitor.Exit(coll.val.SyncRoot); } }
  }

 }
}

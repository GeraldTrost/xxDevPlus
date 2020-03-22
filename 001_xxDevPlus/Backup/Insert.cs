﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace ndString
{
 public class Insert : RchOp
 {
  //Copyright© 1996-2012 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties, You may use, modify, distribute this code under GNU public licence for NON-Commercial use as long as this notice remains unchanged.
  private static bool selfTested  = false;  private static string ass(bool expr) { if (!expr) throw new Exception(ass(true) + " SelfTest Failure"); return "RchOp"; } private static void selfTest() { selfTested = true; } private void init() { if (!selfTested) selfTest(); }
 
  private bool pure;

  internal Insert(bool bufop, Reach Base, string txt) { this.bufop = bufop; this.Base = Base; sTxt = txt; pure = true; }
  internal Insert(bool bufop, Reach Base, Reach txt) { this.bufop = bufop; this.Base = Base; rTxt = txt; pure = false; rTxt.upd(); }
// ******************* (Rch other)
  public Reach before      (Reach other)                                      { Base.upd(); other.upd(); return (pure) ? (bufop) ? (Reach) Base.insbefore_     (other, this)              : (Reach) Base.insbefore      (other, this)              : (bufop) ? (Reach) Base.insbefore     (other, (Rch) rTxt)              : (Reach) Base.insbefore      (other, (Rch) rTxt); }
  //public Reach upto      (Reach other)                                      { Base.upd(); other.upd(); return (pure) ? (bufop) ? (Reach) Base.insupto_       (other, this)              : (Reach) Base.insupto        (other, this)              : (bufop) ? (Reach) Base.insupto       (other, (Rch) rTxt)              : (Reach) Base.insupto        (other, (Rch) rTxt); }
  //public Reach at        (Reach other)                                      { Base.upd(); other.upd(); return (pure) ? (bufop) ? (Reach) Base.insat_         (other, this)              : (Reach) Base.insat          (other, this)              : (bufop) ? (Reach) Base.insat         (other, (Rch) rTxt)              : (Reach) Base.insat          (other, (Rch) rTxt); }
  //public Reach from      (Reach other)                                      { Base.upd(); other.upd(); return (pure) ? (bufop) ? (Reach) Base.insfrom_       (other, this)              : (Reach) Base.insfrom        (other, this)              : (bufop) ? (Reach) Base.insfrom       (other, (Rch) rTxt)              : (Reach) Base.insfrom        (other, (Rch) rTxt); }
  public Reach after       (Reach other)                                      { Base.upd(); other.upd(); return (pure) ? (bufop) ? (Reach) Base.insafter_      (other, this)              : (Reach) Base.insafter       (other, this)              : (bufop) ? (Reach) Base.insafter      (other, (Rch) rTxt)              : (Reach) Base.insafter       (other, (Rch) rTxt); }
// ******************* (int pos) //supported: backward index (eInx < 0) where -1 == last Element
  public Reach before      (int  pos)                                          { Base.upd();              return (pure) ? (bufop) ? (Reach) Base.insbefore_     (pos, this)                : (Reach) Base.insbefore      (pos, this)                : (bufop) ? (Reach) Base.insbefore     (pos, (Rch) rTxt)                : (Reach) Base.insbefore      (pos, (Rch) rTxt); }
  //public Reach upto      (int  pos)                                          { Base.upd();              return (pure) ? (bufop) ? (Reach) Base.insupto_       (pos, this)                : (Reach) Base.insupto        (pos, this)                : (bufop) ? (Reach) Base.insupto       (pos, (Rch) rTxt)                : (Reach) Base.insupto        (pos, (Rch) rTxt); }
  //public Reach at        (int  pos)                                          { Base.upd();              return (pure) ? (bufop) ? (Reach) Base.insat_         (pos, this)                : (Reach) Base.insat          (pos, this)                : (bufop) ? (Reach) Base.insat         (pos, (Rch) rTxt)                : (Reach) Base.insat          (pos, (Rch) rTxt); }
  //public Reach from      (int  pos)                                          { Base.upd();              return (pure) ? (bufop) ? (Reach) Base.insfrom_       (pos, this)                : (Reach) Base.insfrom        (pos, this)                : (bufop) ? (Reach) Base.insfrom       (pos, (Rch) rTxt)                : (Reach) Base.insfrom        (pos, (Rch) rTxt); }
  public Reach after       (int  pos)                                          { Base.upd();              return (pure) ? (bufop) ? (Reach) Base.insafter_      (pos, this)                : (Reach) Base.insafter       (pos, this)                : (bufop) ? (Reach) Base.insafter      (pos, (Rch) rTxt)                : (Reach) Base.insafter       (pos, (Rch) rTxt); }
// ******************* (bool match, int occur, String chrs)
  public Reach before      (bool match, long occur, string chrs)               { Base.upd();              return (pure) ? (bufop) ? (Reach) Base.insbefore_     (match, occur, chrs, this) : (Reach) Base.insbefore      (match, occur, chrs, this) : (bufop) ? (Reach) Base.insbefore     (match, occur, chrs, (Rch) rTxt) : (Reach) Base.insbefore      (match, occur, chrs, (Rch) rTxt); }
  //public Reach upto      (bool match, long occur, string chrs)               { Base.upd();              return (pure) ? (bufop) ? (Reach) Base.insupto_       (match, occur, chrs, this) : (Reach) Base.insupto        (match, occur, chrs, this) : (bufop) ? (Reach) Base.insupto       (match, occur, chrs, (Rch) rTxt) : (Reach) Base.insupto        (match, occur, chrs, (Rch) rTxt); }
  //public Reach at        (bool match, long occur, string chrs)               { Base.upd();              return (pure) ? (bufop) ? (Reach) Base.insat_         (match, occur, chrs, this) : (Reach) Base.insat          (match, occur, chrs, this) : (bufop) ? (Reach) Base.insat         (match, occur, chrs, (Rch) rTxt) : (Reach) Base.insat          (match, occur, chrs, (Rch) rTxt); }
  //public Reach from      (bool match, long occur, string chrs)               { Base.upd();              return (pure) ? (bufop) ? (Reach) Base.insfrom_       (match, occur, chrs, this) : (Reach) Base.insfrom        (match, occur, chrs, this) : (bufop) ? (Reach) Base.insfrom       (match, occur, chrs, (Rch) rTxt) : (Reach) Base.insfrom        (match, occur, chrs, (Rch) rTxt); }
  public Reach after       (bool match, long occur, string chrs)               { Base.upd();              return (pure) ? (bufop) ? (Reach) Base.insafter_      (match, occur, chrs, this) : (Reach) Base.insafter       (match, occur, chrs, this) : (bufop) ? (Reach) Base.insafter      (match, occur, chrs, (Rch) rTxt) : (Reach) Base.insafter       (match, occur, chrs, (Rch) rTxt); }
/*                                                                                                                                                                                                                                                                                                                                                                                                                  //
  public Reach before      (            bool match, string chrs)               { Base.upd();              return (pure) ? (bufop) ? (Reach) Base.insbefore_     (match, 1, chrs, this)     : (Reach) Base.insbefore      (match, 1, chrs, this)     : (bufop) ? (Reach) Base.insbefore     (match, 1, chrs, (Rch) rTxt)     : (Reach) Base.insbefore      (match, 1, chrs, (Rch) rTxt); }
  //public Reach upto      (            bool match, string chrs)               { Base.upd();              return (pure) ? (bufop) ? (Reach) Base.insupto_       (match, 1, chrs, this)     : (Reach) Base.insupto        (match, 1, chrs, this)     : (bufop) ? (Reach) Base.insupto       (match, 1, chrs, (Rch) rTxt)     : (Reach) Base.insupto        (match, 1, chrs, (Rch) rTxt); }
  //public Reach at        (            bool match, string chrs)               { Base.upd();              return (pure) ? (bufop) ? (Reach) Base.insat_         (match, 1, chrs, this)     : (Reach) Base.insat          (match, 1, chrs, this)     : (bufop) ? (Reach) Base.insat         (match, 1, chrs, (Rch) rTxt)     : (Reach) Base.insat          (match, 1, chrs, (Rch) rTxt); }
  //public Reach from      (            bool match, string chrs)               { Base.upd();              return (pure) ? (bufop) ? (Reach) Base.insfrom_       (match, 1, chrs, this)     : (Reach) Base.insfrom        (match, 1, chrs, this)     : (bufop) ? (Reach) Base.insfrom       (match, 1, chrs, (Rch) rTxt)     : (Reach) Base.insfrom        (match, 1, chrs, (Rch) rTxt); }
  public Reach after       (            bool match, string chrs)               { Base.upd();              return (pure) ? (bufop) ? (Reach) Base.insafter_      (match, 1, chrs, this)     : (Reach) Base.insafter       (match, 1, chrs, this)     : (bufop) ? (Reach) Base.insafter      (match, 1, chrs, (Rch) rTxt)     : (Reach) Base.insafter       (match, 1, chrs, (Rch) rTxt); }
*/
// ******************* (bool match, int occur, String chrs)
  public Reach Before      (bool match, long occur, string chrs)               { Base.upd();              return (pure) ? (bufop) ? (Reach) Base.insBefore_     (match, occur, chrs, this) : (Reach) Base.insBefore      (match, occur, chrs, this) : (bufop) ? (Reach) Base.insBefore     (match, occur, chrs, (Rch) rTxt) : (Reach) Base.insBefore      (match, occur, chrs, (Rch) rTxt); }
  //public Reach Upto      (bool match, long occur, string chrs)               { Base.upd();              return (pure) ? (bufop) ? (Reach) Base.insUpto_       (match, occur, chrs, this) : (Reach) Base.insUpto        (match, occur, chrs, this) : (bufop) ? (Reach) Base.insUpto       (match, occur, chrs, (Rch) rTxt) : (Reach) Base.insUpto        (match, occur, chrs, (Rch) rTxt); }
  //public Reach At        (bool match, long occur, string chrs)               { Base.upd();              return (pure) ? (bufop) ? (Reach) Base.insAt_         (match, occur, chrs, this) : (Reach) Base.insAt          (match, occur, chrs, this) : (bufop) ? (Reach) Base.insAt         (match, occur, chrs, (Rch) rTxt) : (Reach) Base.insAt          (match, occur, chrs, (Rch) rTxt); }
  //public Reach From      (bool match, long occur, string chrs)               { Base.upd();              return (pure) ? (bufop) ? (Reach) Base.insFrom_       (match, occur, chrs, this) : (Reach) Base.insFrom        (match, occur, chrs, this) : (bufop) ? (Reach) Base.insFrom       (match, occur, chrs, (Rch) rTxt) : (Reach) Base.insFrom        (match, occur, chrs, (Rch) rTxt); }
  public Reach After       (bool match, long occur, string chrs)               { Base.upd();              return (pure) ? (bufop) ? (Reach) Base.insAfter_      (match, occur, chrs, this) : (Reach) Base.insAfter       (match, occur, chrs, this) : (bufop) ? (Reach) Base.insAfter      (match, occur, chrs, (Rch) rTxt) : (Reach) Base.insAfter       (match, occur, chrs, (Rch) rTxt); }
/*                                                                                                                                                                                                                                                                                                                                                                                                                  //
  public Reach Before      (            bool match, string chrs)               { Base.upd();              return (pure) ? (bufop) ? (Reach) Base.insBefore_     (match, 1, chrs, this)     : (Reach) Base.insBefore      (match, 1, chrs, this)     : (bufop) ? (Reach) Base.insBefore     (match, 1, chrs, (Rch) rTxt)     : (Reach) Base.insBefore      (match, 1, chrs, (Rch) rTxt); }
  //public Reach Upto      (            bool match, string chrs)               { Base.upd();              return (pure) ? (bufop) ? (Reach) Base.insUpto_       (match, 1, chrs, this)     : (Reach) Base.insUpto        (match, 1, chrs, this)     : (bufop) ? (Reach) Base.insUpto       (match, 1, chrs, (Rch) rTxt)     : (Reach) Base.insUpto        (match, 1, chrs, (Rch) rTxt); }
  //public Reach At        (            bool match, string chrs)               { Base.upd();              return (pure) ? (bufop) ? (Reach) Base.insAt_         (match, 1, chrs, this)     : (Reach) Base.insAt          (match, 1, chrs, this)     : (bufop) ? (Reach) Base.insAt         (match, 1, chrs, (Rch) rTxt)     : (Reach) Base.insAt          (match, 1, chrs, (Rch) rTxt); }
  //public Reach From      (            bool match, string chrs)               { Base.upd();              return (pure) ? (bufop) ? (Reach) Base.insFrom_       (match, 1, chrs, this)     : (Reach) Base.insFrom        (match, 1, chrs, this)     : (bufop) ? (Reach) Base.insFrom       (match, 1, chrs, (Rch) rTxt)     : (Reach) Base.insFrom        (match, 1, chrs, (Rch) rTxt); }
  public Reach After       (            bool match, string chrs)               { Base.upd();              return (pure) ? (bufop) ? (Reach) Base.insAfter_      (match, 1, chrs, this)     : (Reach) Base.insAfter       (match, 1, chrs, this)     : (bufop) ? (Reach) Base.insAfter      (match, 1, chrs, (Rch) rTxt)     : (Reach) Base.insAfter       (match, 1, chrs, (Rch) rTxt); }
*/                                                                                                                                                                                                                                                                                                                                                                                                                  //
// ******************* (int occur, String token)
  public Reach before      (long occur,             string token)              { Base.upd();              return (pure) ? (bufop) ? (Reach) Base.insbefore_     (this, occur, token)       : (Reach) Base.insbefore      (this, occur, token)       : (bufop) ? (Reach) Base.insbefore     ((Rch) rTxt, occur, token)       : (Reach) Base.insbefore      ((Rch) rTxt, occur, token); }
  //public Reach upto      (long occur,             string token)              { Base.upd();              return (pure) ? (bufop) ? (Reach) Base.insupto_       (this, occur, token)       : (Reach) Base.insupto        (this, occur, token)       : (bufop) ? (Reach) Base.insupto       ((Rch) rTxt, occur, token)       : (Reach) Base.insupto        ((Rch) rTxt, occur, token); }
  //public Reach at        (long occur,             string token)              { Base.upd();              return (pure) ? (bufop) ? (Reach) Base.insat_         (this, occur, token)       : (Reach) Base.insat          (this, occur, token)       : (bufop) ? (Reach) Base.insat         ((Rch) rTxt, occur, token)       : (Reach) Base.insat          ((Rch) rTxt, occur, token); }
  //public Reach from      (long occur,             string token)              { Base.upd();              return (pure) ? (bufop) ? (Reach) Base.insfrom_       (this, occur, token)       : (Reach) Base.insfrom        (this, occur, token)       : (bufop) ? (Reach) Base.insfrom       ((Rch) rTxt, occur, token)       : (Reach) Base.insfrom        ((Rch) rTxt, occur, token); }
  public Reach after       (long occur,             string token)              { Base.upd();              return (pure) ? (bufop) ? (Reach) Base.insafter_      (this, occur, token)       : (Reach) Base.insafter       (this, occur, token)       : (bufop) ? (Reach) Base.insafter      ((Rch) rTxt, occur, token)       : (Reach) Base.insafter       ((Rch) rTxt, occur, token); }
//                                                                                                                                                                                                                                                                                                                                                                                                                 //
  public Reach before      (                        string token)              { Base.upd();              return (pure) ? (bufop) ? (Reach) Base.insbefore_     (this, 1, token)           : (Reach) Base.insbefore      (this, 1, token)           : (bufop) ? (Reach) Base.insbefore     ((Rch) rTxt, 1, token)           : (Reach) Base.insbefore      ((Rch) rTxt, 1, token); }
  //public Reach upto      (                        string token)              { Base.upd();              return (pure) ? (bufop) ? (Reach) Base.insupto_       (this, 1, token)           : (Reach) Base.insupto        (this, 1, token)           : (bufop) ? (Reach) Base.insupto       ((Rch) rTxt, 1, token)           : (Reach) Base.insupto        ((Rch) rTxt, 1, token); }
  //public Reach at        (                        string token)              { Base.upd();              return (pure) ? (bufop) ? (Reach) Base.insat_         (this, 1, token)           : (Reach) Base.insat          (this, 1, token)           : (bufop) ? (Reach) Base.insat         ((Rch) rTxt, 1, token)           : (Reach) Base.insat          ((Rch) rTxt, 1, token); }
  //public Reach from      (                        string token)              { Base.upd();              return (pure) ? (bufop) ? (Reach) Base.insfrom_       (this, 1, token)           : (Reach) Base.insfrom        (this, 1, token)           : (bufop) ? (Reach) Base.insfrom       ((Rch) rTxt, 1, token)           : (Reach) Base.insfrom        ((Rch) rTxt, 1, token); }
  public Reach after       (                        string token)              { Base.upd();              return (pure) ? (bufop) ? (Reach) Base.insafter_      (this, 1, token)           : (Reach) Base.insafter       (this, 1, token)           : (bufop) ? (Reach) Base.insafter      ((Rch) rTxt, 1, token)           : (Reach) Base.insafter       ((Rch) rTxt, 1, token); }
// ******************* (int occur, String token)
  public Reach Before      (long occur,             string token)              { Base.upd();              return (pure) ? (bufop) ? (Reach) Base.insBefore_     (this, occur, token)       : (Reach) Base.insBefore      (this, occur, token)       : (bufop) ? (Reach) Base.insBefore     ((Rch) rTxt, occur, token)       : (Reach) Base.insBefore      ((Rch) rTxt, occur, token); }
  //public Reach Upto      (long occur,             string token)              { Base.upd();              return (pure) ? (bufop) ? (Reach) Base.insUpto_       (this, occur, token)       : (Reach) Base.insUpto        (this, occur, token)       : (bufop) ? (Reach) Base.insUpto       ((Rch) rTxt, occur, token)       : (Reach) Base.insUpto        ((Rch) rTxt, occur, token); }
  //public Reach At        (long occur,             string token)              { Base.upd();              return (pure) ? (bufop) ? (Reach) Base.insAt_         (this, occur, token)       : (Reach) Base.insAt          (this, occur, token)       : (bufop) ? (Reach) Base.insAt         ((Rch) rTxt, occur, token)       : (Reach) Base.insAt          ((Rch) rTxt, occur, token); }
  //public Reach From      (long occur,             string token)              { Base.upd();              return (pure) ? (bufop) ? (Reach) Base.insFrom_       (this, occur, token)       : (Reach) Base.insFrom        (this, occur, token)       : (bufop) ? (Reach) Base.insFrom       ((Rch) rTxt, occur, token)       : (Reach) Base.insFrom        ((Rch) rTxt, occur, token); }
  public Reach After       (long occur,             string token)              { Base.upd();              return (pure) ? (bufop) ? (Reach) Base.insAfter_      (this, occur, token)       : (Reach) Base.insAfter       (this, occur, token)       : (bufop) ? (Reach) Base.insAfter      ((Rch) rTxt, occur, token)       : (Reach) Base.insAfter       ((Rch) rTxt, occur, token); }
//                                                                                                                                                                                                                                                                                                                                                                                                                  //
  public Reach Before      (                        string token)              { Base.upd();              return (pure) ? (bufop) ? (Reach) Base.insBefore_     (this, 1, token)           : (Reach) Base.insBefore      (this, 1, token)           : (bufop) ? (Reach) Base.insBefore     ((Rch) rTxt, 1, token)           : (Reach) Base.insBefore      ((Rch) rTxt, 1, token); }
  //public Reach Upto      (                        string token)              { Base.upd();              return (pure) ? (bufop) ? (Reach) Base.insUpto_       (this, 1, token)           : (Reach) Base.insUpto        (this, 1, token)           : (bufop) ? (Reach) Base.insUpto       ((Rch) rTxt, 1, token)           : (Reach) Base.insUpto        ((Rch) rTxt, 1, token); }
  //public Reach At        (                        string token)              { Base.upd();              return (pure) ? (bufop) ? (Reach) Base.insAt_         (this, 1, token)           : (Reach) Base.insAt          (this, 1, token)           : (bufop) ? (Reach) Base.insAt         ((Rch) rTxt, 1, token)           : (Reach) Base.insAt          ((Rch) rTxt, 1, token); }
  //public Reach From      (                        string token)              { Base.upd();              return (pure) ? (bufop) ? (Reach) Base.insFrom_       (this, 1, token)           : (Reach) Base.insFrom        (this, 1, token)           : (bufop) ? (Reach) Base.insFrom       ((Rch) rTxt, 1, token)           : (Reach) Base.insFrom        ((Rch) rTxt, 1, token); }
  public Reach After       (                        string token)              { Base.upd();              return (pure) ? (bufop) ? (Reach) Base.insAfter_      (this, 1, token)           : (Reach) Base.insAfter       (this, 1, token)           : (bufop) ? (Reach) Base.insAfter      ((Rch) rTxt, 1, token)           : (Reach) Base.insAfter       ((Rch) rTxt, 1, token); }
// ******************* (int occur, bool prio, String... tokens )
  public Reach before      (long occur,  bool prio, params string[] tokens)    { Base.upd();              return (pure) ? (bufop) ? (Reach) Base.insbefore_     (this, occur, prio, tokens): (Reach) Base.insbefore      (this, occur, prio, tokens): (bufop) ? (Reach) Base.insbefore     ((Rch) rTxt, occur, prio, tokens): (Reach) Base.insbefore      ((Rch) rTxt, occur, prio, tokens); }
  //public Reach upto      (long occur,  bool prio, params string[] tokens)    { Base.upd();              return (pure) ? (bufop) ? (Reach) Base.insupto_       (this, occur, prio, tokens): (Reach) Base.insupto        (this, occur, prio, tokens): (bufop) ? (Reach) Base.insupto       ((Rch) rTxt, occur, prio, tokens): (Reach) Base.insupto        ((Rch) rTxt, occur, prio, tokens); }
  //public Reach at        (long occur,  bool prio, params string[] tokens)    { Base.upd();              return (pure) ? (bufop) ? (Reach) Base.insat_         (this, occur, prio, tokens): (Reach) Base.insat          (this, occur, prio, tokens): (bufop) ? (Reach) Base.insat         ((Rch) rTxt, occur, prio, tokens): (Reach) Base.insat          ((Rch) rTxt, occur, prio, tokens); }
  //public Reach from      (long occur,  bool prio, params string[] tokens)    { Base.upd();              return (pure) ? (bufop) ? (Reach) Base.insfrom_       (this, occur, prio, tokens): (Reach) Base.insfrom        (this, occur, prio, tokens): (bufop) ? (Reach) Base.insfrom       ((Rch) rTxt, occur, prio, tokens): (Reach) Base.insfrom        ((Rch) rTxt, occur, prio, tokens); }
  public Reach after       (long occur,  bool prio, params string[] tokens)    { Base.upd();              return (pure) ? (bufop) ? (Reach) Base.insafter_      (this, occur, prio, tokens): (Reach) Base.insafter       (this, occur, prio, tokens): (bufop) ? (Reach) Base.insafter      ((Rch) rTxt, occur, prio, tokens): (Reach) Base.insafter       ((Rch) rTxt, occur, prio, tokens); }
//                                                                                                                                                                                                                                                                                                                                                                                                                  //
  public Reach before      (             bool prio, params string[] tokens)    { Base.upd();              return (pure) ? (bufop) ? (Reach) Base.insbefore_     (this, 1, prio, tokens)    : (Reach) Base.insbefore      (this, 1, prio, tokens)    : (bufop) ? (Reach) Base.insbefore     ((Rch) rTxt, 1, prio, tokens)    : (Reach) Base.insbefore      ((Rch) rTxt, 1, prio, tokens); }
  //public Reach upto      (             bool prio, params string[] tokens)    { Base.upd();              return (pure) ? (bufop) ? (Reach) Base.insupto_       (this, 1, prio, tokens)    : (Reach) Base.insupto        (this, 1, prio, tokens)    : (bufop) ? (Reach) Base.insupto       ((Rch) rTxt, 1, prio, tokens)    : (Reach) Base.insupto        ((Rch) rTxt, 1, prio, tokens); }
  //public Reach at        (             bool prio, params string[] tokens)    { Base.upd();              return (pure) ? (bufop) ? (Reach) Base.insat_         (this, 1, prio, tokens)    : (Reach) Base.insat          (this, 1, prio, tokens)    : (bufop) ? (Reach) Base.insat         ((Rch) rTxt, 1, prio, tokens)    : (Reach) Base.insat          ((Rch) rTxt, 1, prio, tokens); }
  //public Reach from      (             bool prio, params string[] tokens)    { Base.upd();              return (pure) ? (bufop) ? (Reach) Base.insfrom_       (this, 1, prio, tokens)    : (Reach) Base.insfrom        (this, 1, prio, tokens)    : (bufop) ? (Reach) Base.insfrom       ((Rch) rTxt, 1, prio, tokens)    : (Reach) Base.insfrom        ((Rch) rTxt, 1, prio, tokens); }
  public Reach after       (             bool prio, params string[] tokens)    { Base.upd();              return (pure) ? (bufop) ? (Reach) Base.insafter_      (this, 1, prio, tokens)    : (Reach) Base.insafter       (this, 1, prio, tokens)    : (bufop) ? (Reach) Base.insafter      ((Rch) rTxt, 1, prio, tokens)    : (Reach) Base.insafter       ((Rch) rTxt, 1, prio, tokens); }
// ******************* (int occur, bool prio, String... tokens)
  public Reach Before      (long occur,  bool prio, params string[] tokens)    { Base.upd();              return (pure) ? (bufop) ? (Reach) Base.insBefore_     (this, occur, prio, tokens): (Reach) Base.insBefore      (this, occur, prio, tokens): (bufop) ? (Reach) Base.insBefore     ((Rch) rTxt, occur, prio, tokens): (Reach) Base.insBefore      ((Rch) rTxt, occur, prio, tokens); }
  //public Reach Upto      (long occur,  bool prio, params string[] tokens)    { Base.upd();              return (pure) ? (bufop) ? (Reach) Base.insUpto_       (this, occur, prio, tokens): (Reach) Base.insUpto        (this, occur, prio, tokens): (bufop) ? (Reach) Base.insUpto       ((Rch) rTxt, occur, prio, tokens): (Reach) Base.insUpto        ((Rch) rTxt, occur, prio, tokens); }
  //public Reach At        (long occur,  bool prio, params string[] tokens)    { Base.upd();              return (pure) ? (bufop) ? (Reach) Base.insAt_         (this, occur, prio, tokens): (Reach) Base.insAt          (this, occur, prio, tokens): (bufop) ? (Reach) Base.insAt         ((Rch) rTxt, occur, prio, tokens): (Reach) Base.insAt          ((Rch) rTxt, occur, prio, tokens); }
  //public Reach From      (long occur,  bool prio, params string[] tokens)    { Base.upd();              return (pure) ? (bufop) ? (Reach) Base.insFrom_       (this, occur, prio, tokens): (Reach) Base.insFrom        (this, occur, prio, tokens): (bufop) ? (Reach) Base.insFrom       ((Rch) rTxt, occur, prio, tokens): (Reach) Base.insFrom        ((Rch) rTxt, occur, prio, tokens); }
  public Reach After       (long occur,  bool prio, params string[] tokens)    { Base.upd();              return (pure) ? (bufop) ? (Reach) Base.insAfter_      (this, occur, prio, tokens): (Reach) Base.insAfter       (this, occur, prio, tokens): (bufop) ? (Reach) Base.insAfter      ((Rch) rTxt, occur, prio, tokens): (Reach) Base.insAfter       ((Rch) rTxt, occur, prio, tokens); }
//                                                                                                                                                                                                                                                                                                                                                                                                                  //
  public Reach Before      (             bool prio, params string[] tokens)    { Base.upd();              return (pure) ? (bufop) ? (Reach) Base.insBefore_     (this, 1, prio, tokens)    : (Reach) Base.insBefore      (this, 1, prio, tokens)    : (bufop) ? (Reach) Base.insBefore     ((Rch) rTxt, 1, prio, tokens)    : (Reach) Base.insBefore      ((Rch) rTxt, 1, prio, tokens); }
  //public Reach Upto      (             bool prio, params string[] tokens)    { Base.upd();              return (pure) ? (bufop) ? (Reach) Base.insUpto_       (this, 1, prio, tokens)    : (Reach) Base.insUpto        (this, 1, prio, tokens)    : (bufop) ? (Reach) Base.insUpto       ((Rch) rTxt, 1, prio, tokens)    : (Reach) Base.insUpto        ((Rch) rTxt, 1, prio, tokens); }
  //public Reach At        (             bool prio, params string[] tokens)    { Base.upd();              return (pure) ? (bufop) ? (Reach) Base.insAt_         (this, 1, prio, tokens)    : (Reach) Base.insAt          (this, 1, prio, tokens)    : (bufop) ? (Reach) Base.insAt         ((Rch) rTxt, 1, prio, tokens)    : (Reach) Base.insAt          ((Rch) rTxt, 1, prio, tokens); }
  //public Reach From      (             bool prio, params string[] tokens)    { Base.upd();              return (pure) ? (bufop) ? (Reach) Base.insFrom_       (this, 1, prio, tokens)    : (Reach) Base.insFrom        (this, 1, prio, tokens)    : (bufop) ? (Reach) Base.insFrom       ((Rch) rTxt, 1, prio, tokens)    : (Reach) Base.insFrom        ((Rch) rTxt, 1, prio, tokens); }
  public Reach After       (             bool prio, params string[] tokens)    { Base.upd();              return (pure) ? (bufop) ? (Reach) Base.insAfter_      (this, 1, prio, tokens)    : (Reach) Base.insAfter       (this, 1, prio, tokens)    : (bufop) ? (Reach) Base.insAfter      ((Rch) rTxt, 1, prio, tokens)    : (Reach) Base.insAfter       ((Rch) rTxt, 1, prio, tokens); }
//                                                                                                                                                                                                                                                                                                                                                                                                                  //
 }
}











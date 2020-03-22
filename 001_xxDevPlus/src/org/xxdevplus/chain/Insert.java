


//** @author  GeTr Copyright© 1996-2020 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties
//** @license You may use, modify, distribute this code under GNU public dual licence free for NON-Commercial use as long as this notice remains unchanged.
//** @comment Allows Inserts directly with a Chain's Char-Buffer abd thereby alter firmer Chain Objects as side-effexct

package org.xxdevplus.chain;

import org.xxdevplus.chain.Chn;

public class Insert extends ChnOp
{

  private boolean  pure;

  private boolean  bufop;
  protected Insert(boolean  bufop, Chain Base, String txt) { this.bufop = bufop; this.Base = Base; sTxt = txt; pure = true; }
  protected Insert(boolean  bufop, Chain Base, Chain txt) throws Exception { this.bufop = bufop; this.Base = Base; rTxt = txt; pure = false; rTxt.upd(); }
//                                                                                                                                                                                                                                                                                                                                                                                                                                  //
  public Chain before      (Chain other)                                      throws Exception { Base.upd(); other.upd(); return (pure) ? (bufop) ? (Chain) Base.insbefore_   (this, other)              : (Chain) Base.insbefore      (this, other)              : (bufop) ? (Chain) Base.insbefore      ((Chn) rTxt, other)              : (Chain) Base.insbefore      ((Chn) rTxt, other); }
  //public Chain upto      (Chain other)                                      throws Exception { Base.upd(); other.upd(); return (pure) ? (bufop) ? (Chain) Base.insupto_     (this, other)              : (Chain) Base.insupto        (this, other)              : (bufop)   ? (Chain) Base.insupto_     ((Chn) rTxt, other)              : (Chain) Base.insupto        ((Chn) rTxt, other); }
  //public Chain at        (Chain other)                                      throws Exception { Base.upd(); other.upd(); return (pure) ? (bufop) ? (Chain) Base.insat_       (this, other)              : (Chain) Base.insat          (this, other)              : (bufop)   ? (Chain) Base.insat_       ((Chn) rTxt, other)              : (Chain) Base.insat          ((Chn) rTxt, other); }
  //public Chain from      (Chain other)                                      throws Exception { Base.upd(); other.upd(); return (pure) ? (bufop) ? (Chain) Base.insfrom_     (this, other)              : (Chain) Base.insfrom        (this, other)              : (bufop)   ? (Chain) Base.insfrom_     ((Chn) rTxt, other)              : (Chain) Base.insfrom        ((Chn) rTxt, other); }
  public Chain after       (Chain other)                                      throws Exception { Base.upd(); other.upd(); return (pure) ? (bufop) ? (Chain) Base.insafter_    (this, other)              : (Chain) Base.insafter       (this, other)              : (bufop) ? (Chain) Base.insafter       ((Chn) rTxt, other)              : (Chain) Base.insafter       ((Chn) rTxt, other); }
//                                                                                                                                                                                                                                                                                                                                                                                                                                  //
  public Chain before      (int pos)                                          throws Exception { Base.upd();              return (pure) ? (bufop) ? (Chain) Base.insbefore_   (this, pos)                : (Chain) Base.insbefore      (this, pos)                : (bufop) ? (Chain) Base.insbefore      ((Chn) rTxt, pos)                : (Chain) Base.insbefore      ((Chn) rTxt, pos); }
  //public Chain upto      (int pos)                                          throws Exception { Base.upd();              return (pure) ? (bufop) ? (Chain) Base.insupto_     (this, pos)                : (Chain) Base.insupto        (this, pos)                : (bufop) ? (Chain) Base.insupto_       ((Chn) rTxt, pos)                : (Chain) Base.insupto        ((Chn) rTxt, pos); }
  //public Chain at        (int pos)                                          throws Exception { Base.upd();              return (pure) ? (bufop) ? (Chain) Base.insat_       (this, pos)                : (Chain) Base.insat          (this, pos)                : (bufop) ? (Chain) Base.insat_         ((Chn) rTxt, pos)                : (Chain) Base.insat          ((Chn) rTxt, pos); }
  //public Chain from      (int pos)                                          throws Exception { Base.upd();              return (pure) ? (bufop) ? (Chain) Base.insfrom_     (this, pos)                : (Chain) Base.insfrom        (this, pos)                : (bufop) ? (Chain) Base.insfrom_       ((Chn) rTxt, pos)                : (Chain) Base.insfrom        ((Chn) rTxt, pos); }
  public Chain after       (int pos)                                          throws Exception { Base.upd();              return (pure) ? (bufop) ? (Chain) Base.insafter_    (this, pos)                : (Chain) Base.insafter       (this, pos)                : (bufop) ? (Chain) Base.insafter       ((Chn) rTxt, pos)                : (Chain) Base.insafter       ((Chn) rTxt, pos); }
//                                                                                                                                                                                                                                                                                                                                                                                                                                  //
  public Chain before      (boolean match, int occur, String chrs)            throws Exception { Base.upd();              return (pure) ? (bufop) ? (Chain) Base.insbefore_   (this, match, occur, chrs) : (Chain) Base.insbefore      (this, match, occur, chrs) : (bufop) ? (Chain) Base.insbefore      ((Chn) rTxt, match, occur, chrs) : (Chain) Base.insbefore      ((Chn) rTxt, match, occur, chrs); }
  //public Chain upto      (boolean match, int occur, String chrs)            throws Exception { Base.upd();              return (pure) ? (bufop) ? (Chain) Base.insupto_     (this, match, occur, chrs) : (Chain) Base.insupto        (this, match, occur, chrs) : (bufop) ? (Chain) Base.insupto_       ((Chn) rTxt, match, occur, chrs) : (Chain) Base.insupto        ((Chn) rTxt, match, occur, chrs); }
  //public Chain at        (boolean match, int occur, String chrs)            throws Exception { Base.upd();              return (pure) ? (bufop) ? (Chain) Base.insat_       (this, match, occur, chrs) : (Chain) Base.insat          (this, match, occur, chrs) : (bufop) ? (Chain) Base.insat_         ((Chn) rTxt, match, occur, chrs) : (Chain) Base.insat          ((Chn) rTxt, match, occur, chrs); }
  //public Chain from      (boolean match, int occur, String chrs)            throws Exception { Base.upd();              return (pure) ? (bufop) ? (Chain) Base.insfrom_     (this, match, occur, chrs) : (Chain) Base.insfrom        (this, match, occur, chrs) : (bufop) ? (Chain) Base.insfrom_       ((Chn) rTxt, match, occur, chrs) : (Chain) Base.insfrom        ((Chn) rTxt, match, occur, chrs); }
  public Chain after       (boolean match, int occur, String chrs)            throws Exception { Base.upd();              return (pure) ? (bufop) ? (Chain) Base.insafter_    (this, match, occur, chrs) : (Chain) Base.insafter       (this, match, occur, chrs) : (bufop) ? (Chain) Base.insafter       ((Chn) rTxt, match, occur, chrs) : (Chain) Base.insafter       ((Chn) rTxt, match, occur, chrs); }
/*                                                                                                                                                                                                                                                                                                                                                                                                                                  //
  public Chain before      (           boolean match, String chrs)            throws Exception { Base.upd();              return (pure) ? (bufop) ? (Chain) Base.insbefore_   (this, match, 1, chrs)     : (Chain) Base.insbefore      (this, match, 1, chrs)     : (bufop) ? (Chain) Base.insbefore      ((Chn) rTxt, match, 1, chrs)     : (Chain) Base.insbefore      ((Chn) rTxt, match, 1, chrs); }
  //public Chain upto      (           boolean match, String chrs)            throws Exception { Base.upd();              return (pure) ? (bufop) ? (Chain) Base.insupto_     (this, match, 1, chrs)     : (Chain) Base.insupto        (this, match, 1, chrs)     : (bufop) ? (Chain) Base.insupto_       ((Chn) rTxt, match, 1, chrs)     : (Chain) Base.insupto        ((Chn) rTxt, match, 1, chrs); }
  //public Chain at        (           boolean match, String chrs)            throws Exception { Base.upd();              return (pure) ? (bufop) ? (Chain) Base.insat_       (this, match, 1, chrs)     : (Chain) Base.insat          (this, match, 1, chrs)     : (bufop) ? (Chain) Base.insat_         ((Chn) rTxt, match, 1, chrs)     : (Chain) Base.insat          ((Chn) rTxt, match, 1, chrs); }
  //public Chain from      (           boolean match, String chrs)            throws Exception { Base.upd();              return (pure) ? (bufop) ? (Chain) Base.insfrom_     (this, match, 1, chrs)     : (Chain) Base.insfrom        (this, match, 1, chrs)     : (bufop) ? (Chain) Base.insfrom_       ((Chn) rTxt, match, 1, chrs)     : (Chain) Base.insfrom        ((Chn) rTxt, match, 1, chrs); }
  public Chain after       (           boolean match, String chrs)            throws Exception { Base.upd();              return (pure) ? (bufop) ? (Chain) Base.insafter_    (this, match, 1, chrs)     : (Chain) Base.insafter       (this, match, 1, chrs)     : (bufop) ? (Chain) Base.insafter       ((Chn) rTxt, match, 1, chrs)     : (Chain) Base.insafter       ((Chn) rTxt, match, 1, chrs); }
*/                                                                                                                                                                                                                                                                                                                                                                                                                                  //
  public Chain Before      (boolean match, int occur, String chrs)            throws Exception { Base.upd();              return (pure) ? (bufop) ? (Chain) Base.insBefore_   (this, match, occur, chrs) : (Chain) Base.insBefore      (this, match, occur, chrs) : (bufop) ? (Chain) Base.insBefore      ((Chn) rTxt, match, occur, chrs) : (Chain) Base.insBefore      ((Chn) rTxt, match, occur, chrs); }
  //public Chain Upto      (boolean match, int occur, String chrs)            throws Exception { Base.upd();              return (pure) ? (bufop) ? (Chain) Base.insUpto_     (this, match, occur, chrs) : (Chain) Base.insUpto        (this, match, occur, chrs) : (bufop) ? (Chain) Base.insUpto_       ((Chn) rTxt, match, occur, chrs) : (Chain) Base.insUpto        ((Chn) rTxt, match, occur, chrs); }
  //public Chain At        (boolean match, int occur, String chrs)            throws Exception { Base.upd();              return (pure) ? (bufop) ? (Chain) Base.insAt_       (this, match, occur, chrs) : (Chain) Base.insAt          (this, match, occur, chrs) : (bufop) ? (Chain) Base.insAt_         ((Chn) rTxt, match, occur, chrs) : (Chain) Base.insAt          ((Chn) rTxt, match, occur, chrs); }
  //public Chain From      (boolean match, int occur, String chrs)            throws Exception { Base.upd();              return (pure) ? (bufop) ? (Chain) Base.insFrom_     (this, match, occur, chrs) : (Chain) Base.insFrom        (this, match, occur, chrs) : (bufop) ? (Chain) Base.insFrom_       ((Chn) rTxt, match, occur, chrs) : (Chain) Base.insFrom        ((Chn) rTxt, match, occur, chrs); }
  public Chain After       (boolean match, int occur, String chrs)            throws Exception { Base.upd();              return (pure) ? (bufop) ? (Chain) Base.insAfter_    (this, match, occur, chrs) : (Chain) Base.insAfter       (this, match, occur, chrs) : (bufop) ? (Chain) Base.insAfter       ((Chn) rTxt, match, occur, chrs) : (Chain) Base.insAfter       ((Chn) rTxt, match, occur, chrs); }
/*                                                                                                                                                                                                                                                                                                                                                                                                                                  //
  public Chain Before      (           boolean match, String chrs)            throws Exception { Base.upd();              return (pure) ? (bufop) ? (Chain) Base.insBefore_   (this, match, 1, chrs)     : (Chain) Base.insBefore      (this, match, 1, chrs)     : (bufop) ? (Chain) Base.insBefore      ((Chn) rTxt, match, 1, chrs)     : (Chain) Base.insBefore      ((Chn) rTxt, match, 1, chrs); }
  //public Chain Upto      (           boolean match, String chrs)            throws Exception { Base.upd();              return (pure) ? (bufop) ? (Chain) Base.insUpto_     (this, match, 1, chrs)     : (Chain) Base.insUpto        (this, match, 1, chrs)     : (bufop) ? (Chain) Base.insUpto_       ((Chn) rTxt, match, 1, chrs)     : (Chain) Base.insUpto        ((Chn) rTxt, match, 1, chrs); }
  //public Chain At        (           boolean match, String chrs)            throws Exception { Base.upd();              return (pure) ? (bufop) ? (Chain) Base.insAt_       (this, match, 1, chrs)     : (Chain) Base.insAt          (this, match, 1, chrs)     : (bufop) ? (Chain) Base.insAt_         ((Chn) rTxt, match, 1, chrs)     : (Chain) Base.insAt          ((Chn) rTxt, match, 1, chrs); }
  //public Chain From      (           boolean match, String chrs)            throws Exception { Base.upd();              return (pure) ? (bufop) ? (Chain) Base.insFrom_     (this, match, 1, chrs)     : (Chain) Base.insFrom        (this, match, 1, chrs)     : (bufop) ? (Chain) Base.insFrom_       ((Chn) rTxt, match, 1, chrs)     : (Chain) Base.insFrom        ((Chn) rTxt, match, 1, chrs); }
  public Chain After       (           boolean match, String chrs)            throws Exception { Base.upd();              return (pure) ? (bufop) ? (Chain) Base.insAfter_    (this, match, 1, chrs)     : (Chain) Base.insAfter       (this, match, 1, chrs)     : (bufop) ? (Chain) Base.insAfter       ((Chn) rTxt, match, 1, chrs)     : (Chain) Base.insAfter       ((Chn) rTxt, match, 1, chrs); }
*/                                                                                                                                                                                                                                                                                                                                                                                                                                  //
/*                                                                                                                                                                                                                                                                                                                                                                                                                                  //
  public Chain before      (int occur,           String... tokens)            throws Exception { Base.upd();              return (pure) ? (bufop) ? (Chain) Base.insbefore_   (this, occur, tokens)      : (Chain) Base.insbefore      (this, occur, tokens)      : (bufop) ? (Chain) Base.insbefore      ((Chn) rTxt, occur, tokens)      : (Chain) Base.insbefore      ((Chn) rTxt, occur, tokens); }
  //public Chain upto      (int occur,           String... tokens)            throws Exception { Base.upd();              return (pure) ? (bufop) ? (Chain) Base.insupto_     (this, occur, tokens)      : (Chain) Base.insupto        (this, occur, tokens)      : (bufop) ? (Chain) Base.insupto_       ((Chn) rTxt, occur, tokens)      : (Chain) Base.insupto        ((Chn) rTxt, occur, tokens); }
  //public Chain at        (int occur,           String... tokens)            throws Exception { Base.upd();              return (pure) ? (bufop) ? (Chain) Base.insat_       (this, occur, tokens)      : (Chain) Base.insat          (this, occur, tokens)      : (bufop) ? (Chain) Base.insat_         ((Chn) rTxt, occur, tokens)      : (Chain) Base.insat          ((Chn) rTxt, occur, tokens); }
  //public Chain from      (int occur,           String... tokens)            throws Exception { Base.upd();              return (pure) ? (bufop) ? (Chain) Base.insfrom_     (this, occur, tokens)      : (Chain) Base.insfrom        (this, occur, tokens)      : (bufop) ? (Chain) Base.insfrom_       ((Chn) rTxt, occur, tokens)      : (Chain) Base.insfrom        ((Chn) rTxt, occur, tokens); }
  public Chain after       (int occur,           String... tokens)            throws Exception { Base.upd();              return (pure) ? (bufop) ? (Chain) Base.insafter_    (this, occur, tokens)      : (Chain) Base.insafter       (this, occur, tokens)      : (bufop) ? (Chain) Base.insafter       ((Chn) rTxt, occur, tokens)      : (Chain) Base.insafter       ((Chn) rTxt, occur, tokens); }
//                                                                                                                                                                                                                                                                                                                                                                                                                                  //
  public Chain before      (                     String... tokens)            throws Exception { Base.upd();              return (pure) ? (bufop) ? (Chain) Base.insbefore_   (this, 1, tokens)          : (Chain) Base.insbefore      (this, 1, tokens)          : (bufop) ? (Chain) Base.insbefore      ((Chn) rTxt, 1, tokens)          : (Chain) Base.insbefore      ((Chn) rTxt, 1, tokens); }
  //public Chain upto      (                     String... tokens)            throws Exception { Base.upd();              return (pure) ? (bufop) ? (Chain) Base.insupto_     (this, 1, tokens)          : (Chain) Base.insupto        (this, 1, tokens)          : (bufop) ? (Chain) Base.insupto_       ((Chn) rTxt, 1, tokens)          : (Chain) Base.insupto        ((Chn) rTxt, 1, tokens); }
  //public Chain at        (                     String... tokens)            throws Exception { Base.upd();              return (pure) ? (bufop) ? (Chain) Base.insat_       (this, 1, tokens)          : (Chain) Base.insat          (this, 1, tokens)          : (bufop) ? (Chain) Base.insat_         ((Chn) rTxt, 1, tokens)          : (Chain) Base.insat          ((Chn) rTxt, 1, tokens); }
  //public Chain from      (                     String... tokens)            throws Exception { Base.upd();              return (pure) ? (bufop) ? (Chain) Base.insfrom_     (this, 1, tokens)          : (Chain) Base.insfrom        (this, 1, tokens)          : (bufop) ? (Chain) Base.insfrom_       ((Chn) rTxt, 1, tokens)          : (Chain) Base.insfrom        ((Chn) rTxt, 1, tokens); }
  public Chain after       (                     String... tokens)            throws Exception { Base.upd();              return (pure) ? (bufop) ? (Chain) Base.insafter_    (this, 1, tokens)          : (Chain) Base.insafter       (this, 1, tokens)          : (bufop) ? (Chain) Base.insafter       ((Chn) rTxt, 1, tokens)          : (Chain) Base.insafter       ((Chn) rTxt, 1, tokens); }
//                                                                                                                                                                                                                                                                                                                                                                                                                                  //
  public Chain Before      (int occur,           String... tokens)            throws Exception { Base.upd();              return (pure) ? (bufop) ? (Chain) Base.insBefore_   (this, occur, tokens)      : (Chain) Base.insBefore      (this, occur, tokens)      : (bufop) ? (Chain) Base.insBefore      ((Chn) rTxt, occur, tokens)      : (Chain) Base.insBefore      ((Chn) rTxt, occur, tokens); }
  //public Chain Upto      (int occur,           String... tokens)            throws Exception { Base.upd();              return (pure) ? (bufop) ? (Chain) Base.insUpto_     (this, occur, tokens)      : (Chain) Base.insUpto        (this, occur, tokens)      : (bufop) ? (Chain) Base.insUpto_       ((Chn) rTxt, occur, tokens)      : (Chain) Base.insUpto        ((Chn) rTxt, occur, tokens); }
  //public Chain At        (int occur,           String... tokens)            throws Exception { Base.upd();              return (pure) ? (bufop) ? (Chain) Base.insAt_       (this, occur, tokens)      : (Chain) Base.insAt          (this, occur, tokens)      : (bufop) ? (Chain) Base.insAt_         ((Chn) rTxt, occur, tokens)      : (Chain) Base.insAt          ((Chn) rTxt, occur, tokens); }
  //public Chain From      (int occur,           String... tokens)            throws Exception { Base.upd();              return (pure) ? (bufop) ? (Chain) Base.insFrom_     (this, occur, tokens)      : (Chain) Base.insFrom        (this, occur, tokens)      : (bufop) ? (Chain) Base.insFrom_       ((Chn) rTxt, occur, tokens)      : (Chain) Base.insFrom        ((Chn) rTxt, occur, tokens); }
  public Chain After       (int occur,           String... tokens)            throws Exception { Base.upd();              return (pure) ? (bufop) ? (Chain) Base.insAfter_    (this, occur, tokens)      : (Chain) Base.insAfter       (this, occur, tokens)      : (bufop) ? (Chain) Base.insAfter       ((Chn) rTxt, occur, tokens)      : (Chain) Base.insAfter       ((Chn) rTxt, occur, tokens); }
//                                                                                                                                                                                                                                                                                                                                                                                                                                  //
  public Chain Before      (                     String... tokens)            throws Exception { Base.upd();              return (pure) ? (bufop) ? (Chain) Base.insBefore_   (this, 1, tokens)          : (Chain) Base.insBefore      (this, 1, tokens)          : (bufop) ? (Chain) Base.insBefore      ((Chn) rTxt, 1, tokens)          : (Chain) Base.insBefore      ((Chn) rTxt, 1, tokens); }
  //public Chain Upto      (                     String... tokens)            throws Exception { Base.upd();              return (pure) ? (bufop) ? (Chain) Base.insUpto_     (this, 1, tokens)          : (Chain) Base.insUpto        (this, 1, tokens)          : (bufop) ? (Chain) Base.insUpto_       ((Chn) rTxt, 1, tokens)          : (Chain) Base.insUpto        ((Chn) rTxt, 1, tokens); }
  //public Chain At        (                     String... tokens)            throws Exception { Base.upd();              return (pure) ? (bufop) ? (Chain) Base.insAt_       (this, 1, tokens)          : (Chain) Base.insAt          (this, 1, tokens)          : (bufop) ? (Chain) Base.insAt_         ((Chn) rTxt, 1, tokens)          : (Chain) Base.insAt          ((Chn) rTxt, 1, tokens); }
  //public Chain From      (                     String... tokens)            throws Exception { Base.upd();              return (pure) ? (bufop) ? (Chain) Base.insFrom_     (this, 1, tokens)          : (Chain) Base.insFrom        (this, 1, tokens)          : (bufop) ? (Chain) Base.insFrom_       ((Chn) rTxt, 1, tokens)          : (Chain) Base.insFrom        ((Chn) rTxt, 1, tokens); }
  public Chain After       (                     String... tokens)            throws Exception { Base.upd();              return (pure) ? (bufop) ? (Chain) Base.insAfter_    (this, 1, tokens)          : (Chain) Base.insAfter       (this, 1, tokens)          : (bufop) ? (Chain) Base.insAfter       ((Chn) rTxt, 1, tokens)          : (Chain) Base.insAfter       ((Chn) rTxt, 1, tokens); }
*/                                                                                                                                                                                                                                                                                                                                                                                                                                  //
//                                                                                                                                                                                                                                                                                                                                                                                                                                  //
  public Chain before      (int occur,           boolean prio, String... tokens)            throws Exception { Base.upd();              return (pure) ? (bufop) ? (Chain) Base.insbefore_   (this, occur, prio, tokens)      : (Chain) Base.insbefore      (this, occur, prio, tokens)      : (bufop) ? (Chain) Base.insbefore      ((Chn) rTxt, occur, prio, tokens)      : (Chain) Base.insbefore      ((Chn) rTxt, occur, prio, tokens); }
  //public Chain upto      (int occur,           boolean prio, String... tokens)            throws Exception { Base.upd();              return (pure) ? (bufop) ? (Chain) Base.insupto_     (this, occur, prio, tokens)      : (Chain) Base.insupto        (this, occur, prio, tokens)      : (bufop) ? (Chain) Base.insupto_       ((Chn) rTxt, occur, prio, tokens)      : (Chain) Base.insupto        ((Chn) rTxt, occur, prio, tokens); }
  //public Chain at        (int occur,           boolean prio, String... tokens)            throws Exception { Base.upd();              return (pure) ? (bufop) ? (Chain) Base.insat_       (this, occur, prio, tokens)      : (Chain) Base.insat          (this, occur, prio, tokens)      : (bufop) ? (Chain) Base.insat_         ((Chn) rTxt, occur, prio, tokens)      : (Chain) Base.insat          ((Chn) rTxt, occur, prio, tokens); }
  //public Chain from      (int occur,           boolean prio, String... tokens)            throws Exception { Base.upd();              return (pure) ? (bufop) ? (Chain) Base.insfrom_     (this, occur, prio, tokens)      : (Chain) Base.insfrom        (this, occur, prio, tokens)      : (bufop) ? (Chain) Base.insfrom_       ((Chn) rTxt, occur, prio, tokens)      : (Chain) Base.insfrom        ((Chn) rTxt, occur, prio, tokens); }
  public Chain after       (int occur,           boolean prio, String... tokens)            throws Exception { Base.upd();              return (pure) ? (bufop) ? (Chain) Base.insafter_    (this, occur, prio, tokens)      : (Chain) Base.insafter       (this, occur, prio, tokens)      : (bufop) ? (Chain) Base.insafter       ((Chn) rTxt, occur, prio, tokens)      : (Chain) Base.insafter       ((Chn) rTxt, occur, prio, tokens); }
//                                                                                                                                                                                                                                                                                                                                                                                                                                  //
  public Chain before      (                     boolean prio, String... tokens)            throws Exception { Base.upd();              return (pure) ? (bufop) ? (Chain) Base.insbefore_   (this, 1, prio, tokens)          : (Chain) Base.insbefore      (this, 1, prio, tokens)          : (bufop) ? (Chain) Base.insbefore      ((Chn) rTxt, 1, prio, tokens)          : (Chain) Base.insbefore      ((Chn) rTxt, 1, prio, tokens); }
  //public Chain upto      (                     boolean prio, String... tokens)            throws Exception { Base.upd();              return (pure) ? (bufop) ? (Chain) Base.insupto_     (this, 1, prio, tokens)          : (Chain) Base.insupto        (this, 1, prio, tokens)          : (bufop) ? (Chain) Base.insupto_       ((Chn) rTxt, 1, prio, tokens)          : (Chain) Base.insupto        ((Chn) rTxt, 1, prio, tokens); }
  //public Chain at        (                     boolean prio, String... tokens)            throws Exception { Base.upd();              return (pure) ? (bufop) ? (Chain) Base.insat_       (this, 1, prio, tokens)          : (Chain) Base.insat          (this, 1, prio, tokens)          : (bufop) ? (Chain) Base.insat_         ((Chn) rTxt, 1, prio, tokens)          : (Chain) Base.insat          ((Chn) rTxt, 1, prio, tokens); }
  //public Chain from      (                     boolean prio, String... tokens)            throws Exception { Base.upd();              return (pure) ? (bufop) ? (Chain) Base.insfrom_     (this, 1, prio, tokens)          : (Chain) Base.insfrom        (this, 1, prio, tokens)          : (bufop) ? (Chain) Base.insfrom_       ((Chn) rTxt, 1, prio, tokens)          : (Chain) Base.insfrom        ((Chn) rTxt, 1, prio, tokens); }
  public Chain after       (                     boolean prio, String... tokens)            throws Exception { Base.upd();              return (pure) ? (bufop) ? (Chain) Base.insafter_    (this, 1, prio, tokens)          : (Chain) Base.insafter       (this, 1, prio, tokens)          : (bufop) ? (Chain) Base.insafter       ((Chn) rTxt, 1, prio, tokens)          : (Chain) Base.insafter       ((Chn) rTxt, 1, prio, tokens); }
//                                                                                                                                                                                                                                                                                                                                                                                                                                  //
  public Chain Before      (int occur,           boolean prio, String... tokens)            throws Exception { Base.upd();              return (pure) ? (bufop) ? (Chain) Base.insBefore_   (this, occur, prio, tokens)      : (Chain) Base.insBefore      (this, occur, prio, tokens)      : (bufop) ? (Chain) Base.insBefore      ((Chn) rTxt, occur, prio, tokens)      : (Chain) Base.insBefore      ((Chn) rTxt, occur, prio, tokens); }
  //public Chain Upto      (int occur,           boolean prio, String... tokens)            throws Exception { Base.upd();              return (pure) ? (bufop) ? (Chain) Base.insUpto_     (this, occur, prio, tokens)      : (Chain) Base.insUpto        (this, occur, prio, tokens)      : (bufop) ? (Chain) Base.insUpto_       ((Chn) rTxt, occur, prio, tokens)      : (Chain) Base.insUpto        ((Chn) rTxt, occur, prio, tokens); }
  //public Chain At        (int occur,           boolean prio, String... tokens)            throws Exception { Base.upd();              return (pure) ? (bufop) ? (Chain) Base.insAt_       (this, occur, prio, tokens)      : (Chain) Base.insAt          (this, occur, prio, tokens)      : (bufop) ? (Chain) Base.insAt_         ((Chn) rTxt, occur, prio, tokens)      : (Chain) Base.insAt          ((Chn) rTxt, occur, prio, tokens); }
  //public Chain From      (int occur,           boolean prio, String... tokens)            throws Exception { Base.upd();              return (pure) ? (bufop) ? (Chain) Base.insFrom_     (this, occur, prio, tokens)      : (Chain) Base.insFrom        (this, occur, prio, tokens)      : (bufop) ? (Chain) Base.insFrom_       ((Chn) rTxt, occur, prio, tokens)      : (Chain) Base.insFrom        ((Chn) rTxt, occur, prio, tokens); }
  public Chain After       (int occur,           boolean prio, String... tokens)            throws Exception { Base.upd();              return (pure) ? (bufop) ? (Chain) Base.insAfter_    (this, occur, prio, tokens)      : (Chain) Base.insAfter       (this, occur, prio, tokens)      : (bufop) ? (Chain) Base.insAfter       ((Chn) rTxt, occur, prio, tokens)      : (Chain) Base.insAfter       ((Chn) rTxt, occur, prio, tokens); }
//                                                                                                                                                                                                                                                                                                                                                                                                                                  //
  public Chain Before      (                     boolean prio, String... tokens)            throws Exception { Base.upd();              return (pure) ? (bufop) ? (Chain) Base.insBefore_   (this, 1, prio, tokens)          : (Chain) Base.insBefore      (this, 1, prio, tokens)          : (bufop) ? (Chain) Base.insBefore      ((Chn) rTxt, 1, prio, tokens)          : (Chain) Base.insBefore      ((Chn) rTxt, 1, prio, tokens); }
  //public Chain Upto      (                     boolean prio, String... tokens)            throws Exception { Base.upd();              return (pure) ? (bufop) ? (Chain) Base.insUpto_     (this, 1, prio, tokens)          : (Chain) Base.insUpto        (this, 1, prio, tokens)          : (bufop) ? (Chain) Base.insUpto_       ((Chn) rTxt, 1, prio, tokens)          : (Chain) Base.insUpto        ((Chn) rTxt, 1, prio, tokens); }
  //public Chain At        (                     boolean prio, String... tokens)            throws Exception { Base.upd();              return (pure) ? (bufop) ? (Chain) Base.insAt_       (this, 1, prio, tokens)          : (Chain) Base.insAt          (this, 1, prio, tokens)          : (bufop) ? (Chain) Base.insAt_         ((Chn) rTxt, 1, prio, tokens)          : (Chain) Base.insAt          ((Chn) rTxt, 1, prio, tokens); }
  //public Chain From      (                     boolean prio, String... tokens)            throws Exception { Base.upd();              return (pure) ? (bufop) ? (Chain) Base.insFrom_     (this, 1, prio, tokens)          : (Chain) Base.insFrom        (this, 1, prio, tokens)          : (bufop) ? (Chain) Base.insFrom_       ((Chn) rTxt, 1, prio, tokens)          : (Chain) Base.insFrom        ((Chn) rTxt, 1, prio, tokens); }
  public Chain After       (                     boolean prio, String... tokens)            throws Exception { Base.upd();              return (pure) ? (bufop) ? (Chain) Base.insAfter_    (this, 1, prio, tokens)          : (Chain) Base.insAfter       (this, 1, prio, tokens)          : (bufop) ? (Chain) Base.insAfter       ((Chn) rTxt, 1, prio, tokens)          : (Chain) Base.insAfter       ((Chn) rTxt, 1, prio, tokens); }
//                                                                                                                                                                                                                                                                                                                                                                                                                                  //
}









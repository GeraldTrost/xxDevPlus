


//** @author  GeTr Copyright© 1996-2020 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties
//** @license You may use, modify, distribute this code under GNU public dual licence free for NON-Commercial use as long as this notice remains unchanged.
//** @comment Allows deleting Extracts directly with a Chain's Char-Buffer abd thereby alter firmer Chain Objects as side-effexct

package org.xxdevplus.chain;

import org.xxdevplus.chain.Chain;

public class Extract extends ChnOp
{

  //public static Extractor operator~(Extractor x) { return new Extractor(x); } // this offers an alternative syntax to "._xtr()" in c#
  public Extract _xtr() { return new Extract(this); }

  protected Extract(Chain Base) { this.Base = Base; }
  protected Extract(Extract XBase) { this.Base = XBase.Base; strong = true;  }
// ******************* (Chn other)
  public Chain before      (Chain other)                             throws Exception { Base.upd(); other.upd(); return (strong) ? (Chain) Base.before__    (other)              : (Chain) Base.before_     (other); }
  public Chain upto        (Chain other)                             throws Exception { Base.upd(); other.upd(); return (strong) ? (Chain) Base.upto__      (other)              : (Chain) Base.upto_       (other); }
  public Chain at          (Chain other)                             throws Exception { Base.upd(); other.upd(); return (strong) ? (Chain) Base.at__        (other)              : (Chain) Base.at_         (other); }
  public Chain from        (Chain other)                             throws Exception { Base.upd(); other.upd(); return (strong) ? (Chain) Base.from__      (other)              : (Chain) Base.from_       (other); }
  public Chain after       (Chain other)                             throws Exception { Base.upd(); other.upd(); return (strong) ? (Chain) Base.after__     (other)              : (Chain) Base.after_      (other); }
// ******************* (int pos) //supported: backward index (eInx < 0) where -1 == last Element
  public Chain before      (int pos)                                 throws Exception { Base.upd();              return (strong) ? (Chain) Base.before__    (pos)                : (Chain) Base.before_     (pos); }
  public Chain upto        (int pos)                                 throws Exception { Base.upd();              return (strong) ? (Chain) Base.upto__      (pos)                : (Chain) Base.upto_       (pos); }
  public Chain at          (int pos)                                 throws Exception { Base.upd();              return (strong) ? (Chain) Base.at__        (pos)                : (Chain) Base.at_         (pos); }
  public Chain from        (int pos)                                 throws Exception { Base.upd();              return (strong) ? (Chain) Base.from__      (pos)                : (Chain) Base.from_       (pos); }
  public Chain after       (int pos)                                 throws Exception { Base.upd();              return (strong) ? (Chain) Base.after__     (pos)                : (Chain) Base.after_      (pos); }
// ******************* (bool match, int occur, String chrs)
  public Chain before      (boolean match, int occur, String chrs)   throws Exception { Base.upd();              return (strong) ? (Chain) Base.before__    (match, occur, chrs) : (Chain) Base.before_     (match, occur, chrs); }
  public Chain upto        (boolean match, int occur, String chrs)   throws Exception { Base.upd();              return (strong) ? (Chain) Base.upto__      (match, occur, chrs) : (Chain) Base.upto_       (match, occur, chrs); }
  public Chain at          (boolean match, int occur, String chrs)   throws Exception { Base.upd();              return (strong) ? (Chain) Base.at__        (match, occur, chrs) : (Chain) Base.at_         (match, occur, chrs); }
  public Chain from        (boolean match, int occur, String chrs)   throws Exception { Base.upd();              return (strong) ? (Chain) Base.from__      (match, occur, chrs) : (Chain) Base.from_       (match, occur, chrs); }
  public Chain after       (boolean match, int occur, String chrs)   throws Exception { Base.upd();              return (strong) ? (Chain) Base.after__     (match, occur, chrs) : (Chain) Base.after_      (match, occur, chrs); }
/*                                                                                                                                                                                                                                                                                                                                                                                                                                  //
  public Chain before      (           boolean match, String chrs)   throws Exception { Base.upd();              return (strong) ? (Chain) Base.before__    (match, 1, chrs)     : (Chain) Base.before_     (match, 1, chrs); }
  public Chain upto        (           boolean match, String chrs)   throws Exception { Base.upd();              return (strong) ? (Chain) Base.upto__      (match, 1, chrs)     : (Chain) Base.upto_       (match, 1, chrs); }
  public Chain at          (           boolean match, String chrs)   throws Exception { Base.upd();              return (strong) ? (Chain) Base.at__        (match, 1, chrs)     : (Chain) Base.at_         (match, 1, chrs); }
  public Chain from        (           boolean match, String chrs)   throws Exception { Base.upd();              return (strong) ? (Chain) Base.from__      (match, 1, chrs)     : (Chain) Base.from_       (match, 1, chrs); }
  public Chain after       (           boolean match, String chrs)   throws Exception { Base.upd();              return (strong) ? (Chain) Base.after__     (match, 1, chrs)     : (Chain) Base.after_      (match, 1, chrs); }
*/                                                                                                                                                                                                                                                                                                                                                                                                                                  
// ******************* (bool match, int occur, String chrs)
  public Chain Before      (boolean match, int occur, String chrs)   throws Exception { Base.upd();              return (strong) ? (Chain) Base.Before__    (match, occur, chrs) : (Chain) Base.Before_     (match, occur, chrs); }
  public Chain Upto        (boolean match, int occur, String chrs)   throws Exception { Base.upd();              return (strong) ? (Chain) Base.Upto__      (match, occur, chrs) : (Chain) Base.Upto_       (match, occur, chrs); }
  public Chain At          (boolean match, int occur, String chrs)   throws Exception { Base.upd();              return (strong) ? (Chain) Base.At__        (match, occur, chrs) : (Chain) Base.At_         (match, occur, chrs); }
  public Chain From        (boolean match, int occur, String chrs)   throws Exception { Base.upd();              return (strong) ? (Chain) Base.From__      (match, occur, chrs) : (Chain) Base.From_       (match, occur, chrs); }
  public Chain After       (boolean match, int occur, String chrs)   throws Exception { Base.upd();              return (strong) ? (Chain) Base.After__     (match, occur, chrs) : (Chain) Base.After_      (match, occur, chrs); }
/*                                                                                                                                                                                                                                                                                                                                                                                                                                  //
  public Chain Before      (           boolean match, String chrs)   throws Exception { Base.upd();              return (strong) ? (Chain) Base.Before__    (match, 1, chrs)     : (Chain) Base.Before_     (match, 1, chrs); }
  public Chain Upto        (           boolean match, String chrs)   throws Exception { Base.upd();              return (strong) ? (Chain) Base.Upto__      (match, 1, chrs)     : (Chain) Base.Upto_       (match, 1, chrs); }
  public Chain At          (           boolean match, String chrs)   throws Exception { Base.upd();              return (strong) ? (Chain) Base.At__        (match, 1, chrs)     : (Chain) Base.At_         (match, 1, chrs); }
  public Chain From        (           boolean match, String chrs)   throws Exception { Base.upd();              return (strong) ? (Chain) Base.From__      (match, 1, chrs)     : (Chain) Base.From_       (match, 1, chrs); }
  public Chain After       (           boolean match, String chrs)   throws Exception { Base.upd();              return (strong) ? (Chain) Base.After__     (match, 1, chrs)     : (Chain) Base.After_      (match, 1, chrs); }
*/
// ******************* (int occur, String token)
  public Chain before      (int occur,                String token)  throws Exception { Base.upd();              return (strong) ? (Chain) Base.before__    (occur, token)       : (Chain) Base.before_     (occur, token); }
  public Chain upto        (int occur,                String token)  throws Exception { Base.upd();              return (strong) ? (Chain) Base.upto__      (occur, token)       : (Chain) Base.upto_       (occur, token); }
  public Chain at          (int occur,                String token)  throws Exception { Base.upd();              return (strong) ? (Chain) Base.at__        (occur, token)       : (Chain) Base.at_         (occur, token); }
  public Chain from        (int occur,                String token)  throws Exception { Base.upd();              return (strong) ? (Chain) Base.from__      (occur, token)       : (Chain) Base.from_       (occur, token); }
  public Chain after       (int occur,                String token)  throws Exception { Base.upd();              return (strong) ? (Chain) Base.after__     (occur, token)       : (Chain) Base.after_      (occur, token); }
//                                                                                                                                                                                                                                                                                                                                                                                                                                  //
  public Chain before      (                          String token)  throws Exception { Base.upd();              return (strong) ? (Chain) Base.before__    (1, token)           : (Chain) Base.before_     (1, token); }
  public Chain upto        (                          String token)  throws Exception { Base.upd();              return (strong) ? (Chain) Base.upto__      (1, token)           : (Chain) Base.upto_       (1, token); }
  public Chain at          (                          String token)  throws Exception { Base.upd();              return (strong) ? (Chain) Base.at__        (1, token)           : (Chain) Base.at_         (1, token); }
  public Chain from        (                          String token)  throws Exception { Base.upd();              return (strong) ? (Chain) Base.from__      (1, token)           : (Chain) Base.from_       (1, token); }
  public Chain after       (                          String token)  throws Exception { Base.upd();              return (strong) ? (Chain) Base.after__     (1, token)           : (Chain) Base.after_      (1, token); }
// ******************* (int occur, String token)
  public Chain Before      (int occur,                String token)  throws Exception { Base.upd();              return (strong) ? (Chain) Base.Before__    (occur, token)       : (Chain) Base.Before_     (occur, token); }
  public Chain Upto        (int occur,                String token)  throws Exception { Base.upd();              return (strong) ? (Chain) Base.Upto__      (occur, token)       : (Chain) Base.Upto_       (occur, token); }
  public Chain At          (int occur,                String token)  throws Exception { Base.upd();              return (strong) ? (Chain) Base.At__        (occur, token)       : (Chain) Base.At_         (occur, token); }
  public Chain From        (int occur,                String token)  throws Exception { Base.upd();              return (strong) ? (Chain) Base.From__      (occur, token)       : (Chain) Base.From_       (occur, token); }
  public Chain After       (int occur,                String token)  throws Exception { Base.upd();              return (strong) ? (Chain) Base.After__     (occur, token)       : (Chain) Base.After_      (occur, token); }
//                                                                                                                                                                                                                                                                                                                                                                                                                                  //
  public Chain Before      (                          String token)  throws Exception { Base.upd();              return (strong) ? (Chain) Base.Before__    (1, token)           : (Chain) Base.Before_     (1, token); }
  public Chain Upto        (                          String token)  throws Exception { Base.upd();              return (strong) ? (Chain) Base.Upto__      (1, token)           : (Chain) Base.Upto_       (1, token); }
  public Chain At          (                          String token)  throws Exception { Base.upd();              return (strong) ? (Chain) Base.At__        (1, token)           : (Chain) Base.At_         (1, token); }
  public Chain From        (                          String token)  throws Exception { Base.upd();              return (strong) ? (Chain) Base.From__      (1, token)           : (Chain) Base.From_       (1, token); }
  public Chain After       (                          String token)  throws Exception { Base.upd();              return (strong) ? (Chain) Base.After__     (1, token)           : (Chain) Base.After_      (1, token); }
// ******************* (int occur, bool prio, String... tokens)
  public Chain before      (int occur,                boolean prio, String... tokens)  throws Exception { Base.upd();              return (strong) ? (Chain) Base.before__    (occur, prio, tokens)       : (Chain) Base.before_     (occur, prio, tokens); }
  public Chain upto        (int occur,                boolean prio, String... tokens)  throws Exception { Base.upd();              return (strong) ? (Chain) Base.upto__      (occur, prio, tokens)       : (Chain) Base.upto_       (occur, prio, tokens); }
  public Chain at          (int occur,                boolean prio, String... tokens)  throws Exception { Base.upd();              return (strong) ? (Chain) Base.at__        (occur, prio, tokens)       : (Chain) Base.at_         (occur, prio, tokens); }
  public Chain from        (int occur,                boolean prio, String... tokens)  throws Exception { Base.upd();              return (strong) ? (Chain) Base.from__      (occur, prio, tokens)       : (Chain) Base.from_       (occur, prio, tokens); }
  public Chain after       (int occur,                boolean prio, String... tokens)  throws Exception { Base.upd();              return (strong) ? (Chain) Base.after__     (occur, prio, tokens)       : (Chain) Base.after_      (occur, prio, tokens); }
//                                                                                                                                                                                                                                                                                                                                                                                                                                  //
  public Chain before      (                          boolean prio, String... tokens)  throws Exception { Base.upd();              return (strong) ? (Chain) Base.before__    (1, prio, tokens)           : (Chain) Base.before_     (1, prio, tokens); }
  public Chain upto        (                          boolean prio, String... tokens)  throws Exception { Base.upd();              return (strong) ? (Chain) Base.upto__      (1, prio, tokens)           : (Chain) Base.upto_       (1, prio, tokens); }
  public Chain at          (                          boolean prio, String... tokens)  throws Exception { Base.upd();              return (strong) ? (Chain) Base.at__        (1, prio, tokens)           : (Chain) Base.at_         (1, prio, tokens); }
  public Chain from        (                          boolean prio, String... tokens)  throws Exception { Base.upd();              return (strong) ? (Chain) Base.from__      (1, prio, tokens)           : (Chain) Base.from_       (1, prio, tokens); }
  public Chain after       (                          boolean prio, String... tokens)  throws Exception { Base.upd();              return (strong) ? (Chain) Base.after__     (1, prio, tokens)           : (Chain) Base.after_      (1, prio, tokens); }
// ******************* (int occur, bool prio, String... tokens)
  public Chain Before      (int occur,                boolean prio, String... tokens)  throws Exception { Base.upd();              return (strong) ? (Chain) Base.Before__    (occur, prio, tokens)       : (Chain) Base.Before_     (occur, prio, tokens); }
  public Chain Upto        (int occur,                boolean prio, String... tokens)  throws Exception { Base.upd();              return (strong) ? (Chain) Base.Upto__      (occur, prio, tokens)       : (Chain) Base.Upto_       (occur, prio, tokens); }
  public Chain At          (int occur,                boolean prio, String... tokens)  throws Exception { Base.upd();              return (strong) ? (Chain) Base.At__        (occur, prio, tokens)       : (Chain) Base.At_         (occur, prio, tokens); }
  public Chain From        (int occur,                boolean prio, String... tokens)  throws Exception { Base.upd();              return (strong) ? (Chain) Base.From__      (occur, prio, tokens)       : (Chain) Base.From_       (occur, prio, tokens); }
  public Chain After       (int occur,                boolean prio, String... tokens)  throws Exception { Base.upd();              return (strong) ? (Chain) Base.After__     (occur, prio, tokens)       : (Chain) Base.After_      (occur, prio, tokens); }
//                                                                                                                                                                                                                                                                                                                                                                                                                                  //
  public Chain Before      (                          boolean prio, String... tokens)  throws Exception { Base.upd();              return (strong) ? (Chain) Base.Before__    (1, prio, tokens)           : (Chain) Base.Before_     (1, prio, tokens); }
  public Chain Upto        (                          boolean prio, String... tokens)  throws Exception { Base.upd();              return (strong) ? (Chain) Base.Upto__      (1, prio, tokens)           : (Chain) Base.Upto_       (1, prio, tokens); }
  public Chain At          (                          boolean prio, String... tokens)  throws Exception { Base.upd();              return (strong) ? (Chain) Base.At__        (1, prio, tokens)           : (Chain) Base.At_         (1, prio, tokens); }
  public Chain From        (                          boolean prio, String... tokens)  throws Exception { Base.upd();              return (strong) ? (Chain) Base.From__      (1, prio, tokens)           : (Chain) Base.From_       (1, prio, tokens); }
  public Chain After       (                          boolean prio, String... tokens)  throws Exception { Base.upd();              return (strong) ? (Chain) Base.After__     (1, prio, tokens)           : (Chain) Base.After_      (1, prio, tokens); }
//                                                                                                                                                                                                                                                                                                                                                                                                                                  //
}







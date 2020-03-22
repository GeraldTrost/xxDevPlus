
//** @author  GeTr Copyright© 1996-2020 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties
//** @license You may use, modify, distribute this code under GNU public dual licence free for NON-Commercial use as long as this notice remains unchanged.
//** @comment Allows deleting Extracts directly with a Chain's Char-Buffer abd thereby alter firmer Chain Objects as side-effexct

using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace org_xxdevplus_chain
{
 public class Extract : RchOp
 {
  //Copyright© 1996-2012 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties, You may use, modify, distribute this code under GNU public licence for NON-Commercial use as long as this notice remains unchanged.
  private static bool selfTested  = false;  private static string ass(bool expr) { if (!expr) throw new Exception(ass(true) + " SelfTest Failure"); return "RchOp"; } private static void selfTest() { selfTested = true; } private void init() { if (!selfTested) selfTest(); }
 
  public static Extract operator~(Extract x) { return new Extract(x); } // this offers an alternative syntax to "._xtr()" in c#
  public Extract _xtr() { return new Extract(this); }

  internal Extract(Reach Base) { this.Base = Base; }
  internal Extract(Extract XBase) { this.Base = XBase.Base; strong = true;  }

// ******************* (Rch other)
  public Reach before      (Reach other)                                       { Base.upd(); other.upd(); return (strong) ? (Reach) Base.before__    (other)              : (Reach) Base.before_     (other); }
  public Reach upto        (Reach other)                                       { Base.upd(); other.upd(); return (strong) ? (Reach) Base.upto__      (other)              : (Reach) Base.upto_       (other); }
  public Reach at          (Reach other)                                       { Base.upd(); other.upd(); return (strong) ? (Reach) Base.at__        (other)              : (Reach) Base.at_         (other); }
  public Reach from        (Reach other)                                       { Base.upd(); other.upd(); return (strong) ? (Reach) Base.from__      (other)              : (Reach) Base.from_       (other); }
  public Reach after       (Reach other)                                       { Base.upd(); other.upd(); return (strong) ? (Reach) Base.after__     (other)              : (Reach) Base.after_      (other); }
// ******************* (int pos) //supported: backward index (eInx < 0) where -1 == last Element
  public Reach before      (int  pos)                                          { Base.upd();              return (strong) ? (Reach) Base.before__    (pos)                : (Reach) Base.before_     (pos); }
  public Reach upto        (int  pos)                                          { Base.upd();              return (strong) ? (Reach) Base.upto__      (pos)                : (Reach) Base.upto_       (pos); }
  public Reach at          (int  pos)                                          { Base.upd();              return (strong) ? (Reach) Base.at__        (pos)                : (Reach) Base.at_         (pos); }
  public Reach from        (int  pos)                                          { Base.upd();              return (strong) ? (Reach) Base.from__      (pos)                : (Reach) Base.from_       (pos); }
  public Reach after       (int  pos)                                          { Base.upd();              return (strong) ? (Reach) Base.after__     (pos)                : (Reach) Base.after_      (pos); }
// ******************* (bool match, int occur, String chrs)
  public Reach before      (bool match, long occur, string chrs)               { Base.upd();              return (strong) ? (Reach) Base.before__    (match, occur, chrs) : (Reach) Base.before_     (match, occur, chrs); }
  public Reach upto        (bool match, long occur, string chrs)               { Base.upd();              return (strong) ? (Reach) Base.upto__      (match, occur, chrs) : (Reach) Base.upto_       (match, occur, chrs); }
  public Reach at          (bool match, long occur, string chrs)               { Base.upd();              return (strong) ? (Reach) Base.at__        (match, occur, chrs) : (Reach) Base.at_         (match, occur, chrs); }
  public Reach from        (bool match, long occur, string chrs)               { Base.upd();              return (strong) ? (Reach) Base.from__      (match, occur, chrs) : (Reach) Base.from_       (match, occur, chrs); }
  public Reach after       (bool match, long occur, string chrs)               { Base.upd();              return (strong) ? (Reach) Base.after__     (match, occur, chrs) : (Reach) Base.after_      (match, occur, chrs); }
/*                                                                                                                                                                                                                                                                                                                                                                                                                  //
  public Reach before      (            bool match, string chrs)               { Base.upd();              return (strong) ? (Reach) Base.before__    (match, 1, chrs)     : (Reach) Base.before_     (match, 1, chrs); }
  public Reach upto        (            bool match, string chrs)               { Base.upd();              return (strong) ? (Reach) Base.upto__      (match, 1, chrs)     : (Reach) Base.upto_       (match, 1, chrs); }
  public Reach at          (            bool match, string chrs)               { Base.upd();              return (strong) ? (Reach) Base.at__        (match, 1, chrs)     : (Reach) Base.at_         (match, 1, chrs); }
  public Reach from        (            bool match, string chrs)               { Base.upd();              return (strong) ? (Reach) Base.from__      (match, 1, chrs)     : (Reach) Base.from_       (match, 1, chrs); }
  public Reach after       (            bool match, string chrs)               { Base.upd();              return (strong) ? (Reach) Base.after__     (match, 1, chrs)     : (Reach) Base.after_      (match, 1, chrs); }
*/
// ******************* (bool match, int occur, String chrs)
  public Reach Before      (bool match, long occur, string chrs)               { Base.upd();              return (strong) ? (Reach) Base.Before__    (match, occur, chrs) : (Reach) Base.Before_     (match, occur, chrs); }
  public Reach Upto        (bool match, long occur, string chrs)               { Base.upd();              return (strong) ? (Reach) Base.Upto__      (match, occur, chrs) : (Reach) Base.Upto_       (match, occur, chrs); }
  public Reach At          (bool match, long occur, string chrs)               { Base.upd();              return (strong) ? (Reach) Base.At__        (match, occur, chrs) : (Reach) Base.At_         (match, occur, chrs); }
  public Reach From        (bool match, long occur, string chrs)               { Base.upd();              return (strong) ? (Reach) Base.From__      (match, occur, chrs) : (Reach) Base.From_       (match, occur, chrs); }
  public Reach After       (bool match, long occur, string chrs)               { Base.upd();              return (strong) ? (Reach) Base.After__     (match, occur, chrs) : (Reach) Base.After_      (match, occur, chrs); }
/*
  public Reach Before      (            bool match, string chrs)               { Base.upd();              return (strong) ? (Reach) Base.Before__    (match, 1, chrs)     : (Reach) Base.Before_     (match, 1, chrs); }
  public Reach Upto        (            bool match, string chrs)               { Base.upd();              return (strong) ? (Reach) Base.Upto__      (match, 1, chrs)     : (Reach) Base.Upto_       (match, 1, chrs); }
  public Reach At          (            bool match, string chrs)               { Base.upd();              return (strong) ? (Reach) Base.At__        (match, 1, chrs)     : (Reach) Base.At_         (match, 1, chrs); }
  public Reach From        (            bool match, string chrs)               { Base.upd();              return (strong) ? (Reach) Base.From__      (match, 1, chrs)     : (Reach) Base.From_       (match, 1, chrs); }
  public Reach After       (            bool match, string chrs)               { Base.upd();              return (strong) ? (Reach) Base.After__     (match, 1, chrs)     : (Reach) Base.After_      (match, 1, chrs); }
*/
// ******************* (int occur, String token)
  public Reach before      (long occur,             string token)              { Base.upd();              return (strong) ? (Reach) Base.before__    (occur, token)       : (Reach) Base.before_     (occur, token); }
  public Reach upto        (long occur,             string token)              { Base.upd();              return (strong) ? (Reach) Base.upto__      (occur, token)       : (Reach) Base.upto_       (occur, token); }
  public Reach at          (long occur,             string token)              { Base.upd();              return (strong) ? (Reach) Base.at__        (occur, token)       : (Reach) Base.at_         (occur, token); }
  public Reach from        (long occur,             string token)              { Base.upd();              return (strong) ? (Reach) Base.from__      (occur, token)       : (Reach) Base.from_       (occur, token); }
  public Reach after       (long occur,             string token)              { Base.upd();              return (strong) ? (Reach) Base.after__     (occur, token)       : (Reach) Base.after_      (occur, token); }
//                                                                                                                                                                                                                                                                                                                                                                                                                  //
  public Reach before      (                        string token)              { Base.upd();              return (strong) ? (Reach) Base.before__    (1, token)           : (Reach) Base.before_     (1, token); }
  public Reach upto        (                        string token)              { Base.upd();              return (strong) ? (Reach) Base.upto__      (1, token)           : (Reach) Base.upto_       (1, token); }
  public Reach at          (                        string token)              { Base.upd();              return (strong) ? (Reach) Base.at__        (1, token)           : (Reach) Base.at_         (1, token); }
  public Reach from        (                        string token)              { Base.upd();              return (strong) ? (Reach) Base.from__      (1, token)           : (Reach) Base.from_       (1, token); }
  public Reach after       (                        string token)              { Base.upd();              return (strong) ? (Reach) Base.after__     (1, token)           : (Reach) Base.after_      (1, token); }
// ******************* (int occur, String token)
  public Reach Before      (long occur,             string token)              { Base.upd();              return (strong) ? (Reach) Base.Before__    (occur, token)       : (Reach) Base.Before_     (occur, token); }
  public Reach Upto        (long occur,             string token)              { Base.upd();              return (strong) ? (Reach) Base.Upto__      (occur, token)       : (Reach) Base.Upto_       (occur, token); }
  public Reach At          (long occur,             string token)              { Base.upd();              return (strong) ? (Reach) Base.At__        (occur, token)       : (Reach) Base.At_         (occur, token); }
  public Reach From        (long occur,             string token)              { Base.upd();              return (strong) ? (Reach) Base.From__      (occur, token)       : (Reach) Base.From_       (occur, token); }
  public Reach After       (long occur,             string token)              { Base.upd();              return (strong) ? (Reach) Base.After__     (occur, token)       : (Reach) Base.After_      (occur, token); }
//                                                                                                                                                                                                                                                                                                                                                                                                                  //
  public Reach Before      (                        string token)              { Base.upd();              return (strong) ? (Reach) Base.Before__    (1, token)           : (Reach) Base.Before_     (1, token); }
  public Reach Upto        (                        string token)              { Base.upd();              return (strong) ? (Reach) Base.Upto__      (1, token)           : (Reach) Base.Upto_       (1, token); }
  public Reach At          (                        string token)              { Base.upd();              return (strong) ? (Reach) Base.At__        (1, token)           : (Reach) Base.At_         (1, token); }
  public Reach From        (                        string token)              { Base.upd();              return (strong) ? (Reach) Base.From__      (1, token)           : (Reach) Base.From_       (1, token); }
  public Reach After       (                        string token)              { Base.upd();              return (strong) ? (Reach) Base.After__     (1, token)           : (Reach) Base.After_      (1, token); }
// ******************* (int occur, bool prio, String... tokens)
  public Reach before      (long occur,             bool prio, params string[] tokens)    { Base.upd();              return (strong) ? (Reach) Base.before__    (occur, prio, tokens)       : (Reach) Base.before_     (occur, prio, tokens); }
  public Reach upto        (long occur,             bool prio, params string[] tokens)    { Base.upd();              return (strong) ? (Reach) Base.upto__      (occur, prio, tokens)       : (Reach) Base.upto_       (occur, prio, tokens); }
  public Reach at          (long occur,             bool prio, params string[] tokens)    { Base.upd();              return (strong) ? (Reach) Base.at__        (occur, prio, tokens)       : (Reach) Base.at_         (occur, prio, tokens); }
  public Reach from        (long occur,             bool prio, params string[] tokens)    { Base.upd();              return (strong) ? (Reach) Base.from__      (occur, prio, tokens)       : (Reach) Base.from_       (occur, prio, tokens); }
  public Reach after       (long occur,             bool prio, params string[] tokens)    { Base.upd();              return (strong) ? (Reach) Base.after__     (occur, prio, tokens)       : (Reach) Base.after_      (occur, prio, tokens); }
//                                                                                                                                                                                                                                                                                                                                                                                                                  //
  public Reach before      (                        bool prio, params string[] tokens)    { Base.upd();              return (strong) ? (Reach) Base.before__    (1, prio, tokens)           : (Reach) Base.before_     (1, prio, tokens); }
  public Reach upto        (                        bool prio, params string[] tokens)    { Base.upd();              return (strong) ? (Reach) Base.upto__      (1, prio, tokens)           : (Reach) Base.upto_       (1, prio, tokens); }
  public Reach at          (                        bool prio, params string[] tokens)    { Base.upd();              return (strong) ? (Reach) Base.at__        (1, prio, tokens)           : (Reach) Base.at_         (1, prio, tokens); }
  public Reach from        (                        bool prio, params string[] tokens)    { Base.upd();              return (strong) ? (Reach) Base.from__      (1, prio, tokens)           : (Reach) Base.from_       (1, prio, tokens); }
  public Reach after       (                        bool prio, params string[] tokens)    { Base.upd();              return (strong) ? (Reach) Base.after__     (1, prio, tokens)           : (Reach) Base.after_      (1, prio, tokens); }
// ******************* (int occur, bool prio, String... tokens)
  public Reach Before      (long occur,             bool prio, params string[] tokens)    { Base.upd();              return (strong) ? (Reach) Base.Before__    (occur, prio, tokens)       : (Reach) Base.Before_     (occur, prio, tokens); }
  public Reach Upto        (long occur,             bool prio, params string[] tokens)    { Base.upd();              return (strong) ? (Reach) Base.Upto__      (occur, prio, tokens)       : (Reach) Base.Upto_       (occur, prio, tokens); }
  public Reach At          (long occur,             bool prio, params string[] tokens)    { Base.upd();              return (strong) ? (Reach) Base.At__        (occur, prio, tokens)       : (Reach) Base.At_         (occur, prio, tokens); }
  public Reach From        (long occur,             bool prio, params string[] tokens)    { Base.upd();              return (strong) ? (Reach) Base.From__      (occur, prio, tokens)       : (Reach) Base.From_       (occur, prio, tokens); }
  public Reach After       (long occur,             bool prio, params string[] tokens)    { Base.upd();              return (strong) ? (Reach) Base.After__     (occur, prio, tokens)       : (Reach) Base.After_      (occur, prio, tokens); }
//                                                                                                                                                                                                                                                                                                                                                                                                                  //
  public Reach Before      (                        bool prio, params string[] tokens)    { Base.upd();              return (strong) ? (Reach) Base.Before__    (1, prio, tokens)           : (Reach) Base.Before_     (1, prio, tokens); }
  public Reach Upto        (                        bool prio, params string[] tokens)    { Base.upd();              return (strong) ? (Reach) Base.Upto__      (1, prio, tokens)           : (Reach) Base.Upto_       (1, prio, tokens); }
  public Reach At          (                        bool prio, params string[] tokens)    { Base.upd();              return (strong) ? (Reach) Base.At__        (1, prio, tokens)           : (Reach) Base.At_         (1, prio, tokens); }
  public Reach From        (                        bool prio, params string[] tokens)    { Base.upd();              return (strong) ? (Reach) Base.From__      (1, prio, tokens)           : (Reach) Base.From_       (1, prio, tokens); }
  public Reach After       (                        bool prio, params string[] tokens)    { Base.upd();              return (strong) ? (Reach) Base.After__     (1, prio, tokens)           : (Reach) Base.After_      (1, prio, tokens); }
//                                                                                                                                                                                                                                                                                                                                                                                                                  //

 }



}










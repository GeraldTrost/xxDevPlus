

//** @author  GeTr Copyright© 1996-2020 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties
//** @license You may use, modify, distribute this code under GNU public dual licence free for NON-Commercial use as long as this notice remains unchanged.
//** @comment Index Observer used in special modes in Chain


using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace org_xxdevplus_chain
{
 public interface InxObserver
 {
  void sIndexShift(int from, int amount);
  void eIndexShift(int from, int amount);
 }
}



//** @author  GeTr Copyright© 1996-2020 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties
//** @license You may use, modify, distribute this code under GNU public dual licence free for NON-Commercial use as long as this notice remains unchanged.
//** @comment Template Class for more general Chain-like sequences


using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace org_xxdevplus_struct
{
 //classes Block, Store, InxLbl, Sequence will once be the foundation for more general Reaches that operate on any arrays - not only on char-arrays
 public class Sequence
 {
  //Copyright© 1996-2012 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties, You may use, modify, distribute this code under GNU public licence for NON-Commercial use as long as this notice remains unchanged.
  private static bool selfTested  = false;  private static string ass(bool expr) { if (!expr) throw new Exception(ass(true) + " SelfTest Failure"); return "Sequence"; } private static void selfTest() { selfTested = true; } private void init() { if (!selfTested) selfTest(); }

  private int       count;
  private object[]  Elements;
  private bool      useReachNotify    = false;

  public            Sequence  (object[] Elements)            {this.Elements = Elements; this.count = Elements.Count(); }
  public  int       Len       ()                             {return count;}

  public  object    this[int inx] { get{if (inx < 0) inx += 1 + Len(); return Elements[inx -1]; } set{if (inx < 0) inx += 1 + Len(); Elements[inx -1] = value;} }

  public  void      subscribe(object o) {}

  private           long            sLblMax;
  private           long            eLblMax;
  
  //public          long            sLabel(long bdy) { if (useReachNotify) return bdy; Pile<long> l = bdylbl.sLabels(bdy); if (l.Len() > 0) return l[1]; bdylbl.AddStart(bdy, ++sLblMax); return sLblMax; }
  //public          long            eLabel(long bdy) { if (useReachNotify) return bdy; Pile<long> l = bdylbl.eLabels(bdy); if (l.Len() > 0) return l[1]; bdylbl.AddEnd(bdy, ++eLblMax); return eLblMax; }


 }



}

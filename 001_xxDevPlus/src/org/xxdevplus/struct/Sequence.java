


//** @author  GeTr Copyright© 1996-2020 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties
//** @license You may use, modify, distribute this code under GNU public dual licence free for NON-Commercial use as long as this notice remains unchanged.
//** @comment Template Class for more general Chain-like sequences

package org.xxdevplus.struct;

 public class Sequence
 {
  //classes Block, Store, InxLbl, Sequence will once be the foundation for more general Reaches that operate on any arrays - not only on char-arrays
  private static boolean selfTested  = false;  private static String ass(boolean expr) throws Exception { if (!expr) throw new Exception(ass(true) + " SelfTest Failure"); return "Sequence"; } private static void selfTest() { selfTested = true; } private void init() { if (!selfTested) selfTest(); }

  private int       count;
  private Object[]  Elements;
  private boolean   useReachNotify    = false;

  public            Sequence  (Object[] Elements)            {this.Elements = Elements; this.count = Elements.length; }
  public  int       Len       ()                             {return count;}

  public  Object    g(int inx) { if (inx < 0) inx += 1 + Len(); return Elements[inx -1]; } public void s(Object value, int inx) {if (inx < 0) inx += 1 + Len(); Elements[inx -1] = value;} 

  public  void      subscribe(Object o) {}

  private           long            sLblMax;
  private           long            eLblMax;
  
  //public          long            sLabel(long bdy) { if (useReachNotify) return bdy; Pile<long> l = bdylbl.sLabels(bdy); if (l.Len() > 0) return l[1]; bdylbl.AddStart(bdy, ++sLblMax); return sLblMax; }
  //public          long            eLabel(long bdy) { if (useReachNotify) return bdy; Pile<long> l = bdylbl.eLabels(bdy); if (l.Len() > 0) return l[1]; bdylbl.AddEnd(bdy, ++eLblMax); return eLblMax; }


 }


/*
//classes Block, Store, InxLbl, Sequence will once be the foundation for more general Reaches that operate on any arrays - not only on char-arrays
 public class Sequence
 {
  //Copyright© 1996-2020 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties, You may use, modify, distribute this code as long as this notice remains unchanged.
  private static boolean selfTested  = false;  private static String ass(boolean expr) throws Exception { if (!expr) throw new Exception(ass(true) + " SelfTest Failure"); return "Sequence"; } private static void selfTest() { selfTested = true; } private void init() { if (!selfTested) selfTest(); }

  private int      count;
  private Object[] Elements;
  private boolean  useReachNotify    = false;

  public           Sequence  (Object[] Elements)            {this.Elements = Elements; this.count = Elements.length; }
  public  int      Len       ()                             {return count;}

  public  Object   g(int inx)               { if (inx < 0) inx += 1 + Len(); return Elements[inx -1];  } 
  public void      s(Object value, int inx) { if (inx < 0) inx += 1 + Len(); Elements[inx -1] = value; }
  

  public  void     subscribe(Object o) {}

  private         int            sLblMax;
  private         int            eLblMax;
  //public          int            sLabel(int bdy) { if (useReachNotify) return bdy; Pile<int> l = bdylbl.sLabels(bdy); if (l.Len() > 0) return l[1]; bdylbl.AddStart(bdy, ++sLblMax); return sLblMax; }
  //public          int            eLabel(int bdy) { if (useReachNotify) return bdy; Pile<int> l = bdylbl.eLabels(bdy); if (l.Len() > 0) return l[1]; bdylbl.AddEnd(bdy, ++eLblMax); return eLblMax; }


 }
 */





//** @author  GeTr Copyright© 1996-2020 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties
//** @license You may use, modify, distribute this code under GNU public dual licence free for NON-Commercial use as long as this notice remains unchanged.
//** @comment 3-dim line


package org.xxdevplus.math;

import org.xxdevplus.math.g3ColVec;
import org.xxdevplus.math.g3RowVec;

 public class g3Line 
 {
  private static boolean selfTested  = false;  private static String ass(boolean expr) throws Exception { if (!expr) throw new Exception(ass(true) + " g3Line SelfTest Failure"); return "g3Line"; } private void init() throws Exception { if (!selfTested) selfTest(); }

  private g3RowVec      sPoint      = null;
  private g3RowVec      ePoint      = null;
  private double        len         = 0;
  private g3ColVec      pnt         = null;
  private g3ColVec      dir         = null;

  private void init(g3RowVec start, g3RowVec end) throws Exception
  {
   if (!selfTested) selfTest();
   pnt         = new g3ColVec(0, 0, 0);
   dir         = new g3ColVec(0, 0, 0);
   this.sPoint = start;
   this.ePoint   = end;
   this.pnt.X(start.X()); this.pnt.Y(start.Y()); this.pnt.Z(start.Z());
   this.dir.X(end.g(1, 1, 1) - start.g(1, 1, 1)); this.dir.Y(end.g(1, 2, 1) - start.g(1, 2, 1)); this.dir.Z(end.g(1, 3, 1) - start.g(1, 3, 1));
   len = Math.sqrt(this.dir.X() * this.dir.X() + this.dir.Y() * this.dir.Y() + this.dir.Z() * this.dir.Z());
   this.dir.X(this.dir.X() / len); this.dir.Y(this.dir.Y() / len); this.dir.Z(this.dir.Z() / len);
  }

  public g3Line(g3RowVec start, g3RowVec end) throws Exception { init(start, end); }
  public g3Line(g3Line cloneFrom) throws Exception { init(new g3RowVec(cloneFrom.sPoint), new g3RowVec(cloneFrom.ePoint)); }

  public g3RowVec SPoint () {return sPoint; } public void SPoint (g3RowVec value) throws Exception {init(value, ePoint);                                                                                                                 } 
  public g3RowVec EPoint () {return ePoint; } public void EPoint (g3RowVec value) throws Exception {init(sPoint, value);                                                                                                                 } 
  public g3ColVec Dir    () {return dir;    } public void Dir    (g3RowVec value) throws Exception {ePoint.X(sPoint.X() + value.X());       ePoint.Y(sPoint.Y() + value.Y());       ePoint.Z(sPoint.Z() + value.Z());       init(sPoint, ePoint); } 
  public double   Len    () {return len;    } public void Len    (double   value) throws Exception {ePoint.X(sPoint.X() + value * dir.X()); ePoint.Y(sPoint.Y() + value * dir.Y()); ePoint.Z(sPoint.Z() + value * dir.Z()); init(sPoint, ePoint); } 
    
  public g3ColVec At(double len) throws Exception
  {
   g3ColVec ret = new g3ColVec(0, 0, 0);
   ret.s(pnt.g(1, 1, 1) + dir.g(1, 1, 1) * len, 1, 1, 1);
   ret.s(pnt.g(1, 2, 1) + dir.g(1, 2, 1) * len, 1, 2, 1);
   ret.s(pnt.g(1, 3, 1) + dir.g(1, 3, 1) * len, 1, 3, 1);
   return ret;
  }

  public g3Line parallel(double dist) throws Exception                                                              // must be moved to g2Line, only for testing, all z-Values must be 0 for testing
  {
   g3Line ret = new g3Line(this);
   ret.sPoint.Y(sPoint.Y() + Math.sqrt(dir.X() * dir.X() + dir.Y() * dir.Y()) * dist / dir.X());
   ret.ePoint.X(ret.sPoint.X() + dir.X());
   ret.ePoint.Y(ret.sPoint.Y() + dir.Y());
   ret.ePoint.Z(ret.sPoint.Z() + dir.Z());
   ret.pnt.X(ret.sPoint.X());
   ret.pnt.Y(ret.sPoint.Y());
   ret.pnt.Z(ret.sPoint.Z());
   return ret;
  }

  public g3Line vertical(g3RowVec point) throws Exception                                                           // must be moved to g2Line, only for testing, all z-Values must be 0 for testing
  {
   g3Line ret = new g3Line(this);
   ret.ePoint.X(point.X() + dir.Y());
   ret.ePoint.Y(point.Y() - dir.X());
   ret.init(point, ret.ePoint);
   return ret;
  }

  public g3ColVec InterSect(g3Line second) throws Exception {return second.At((dir.g(1, 2, 1) * pnt.g(1, 1, 1) - dir.g(1, 1, 1) * pnt.g(1, 2, 1) + dir.g(1, 1, 1) * second.pnt.g(1, 2, 1) - dir.g(1, 2, 1) * second.pnt.g(1, 1, 1)) / (dir.g(1, 2, 1) * second.dir.g(1, 1, 1) - dir.g(1, 1, 1) * second.dir.g(1, 2, 1))); }
 
  public double yAt(double x) { return (x * dir.Y() - pnt.X() * dir.Y() + pnt.Y() * dir.X()) / dir.X(); }      // must be moved to g2Line, only for testing, all z-Values must be 0 for testing
  public double xAt(double y) { return (y * dir.X() + pnt.X() * dir.Y() - pnt.Y() * dir.X()) / dir.Y(); }      // must be moved to g2Line, only for testing, all z-Values must be 0 for testing

  private static void selfTest() throws Exception
  {
   selfTested = true;
   g3Line g = new g3Line(new g3RowVec(0, 0, 0), new g3RowVec(1, 1, 0));
   ass(g.At((Math.sqrt(2))).X() == 1);
   ass(g.At((Math.sqrt(2))).Y() == 1);
   
   g3Line h = new g3Line(new g3RowVec(0, 2, 0), new g3RowVec(2, 0, 0));
   g3ColVec s = h.InterSect(g);
   ass(s.g(1, 1, 1) == 1);
   ass(s.g(1, 2, 1) == 1);
   ass(s.g(1, 3, 1) == 0);
   ass(h.yAt(2) == 0);
   ass(h.xAt(1) == 1);
   ass(h.parallel(Math.sqrt(2)/2).yAt(3) == 0);
  }
    
 }
















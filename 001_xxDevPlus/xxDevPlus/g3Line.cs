

//** @author  GeTr Copyright© 1996-2020 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties
//** @license You may use, modify, distribute this code under GNU public dual licence free for NON-Commercial use as long as this notice remains unchanged.
//** @comment 3-dim line


using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace org_xxdevplus_math
{
 public class g3Line
 {
  //Copyright© 1996-2012 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties, You may use, modify, distribute this code under GNU public licence for NON-Commercial use as long as this notice remains unchanged.
  private static bool selfTested  = false;  private static string ass(bool expr) { if (!expr) throw new Exception(ass(true) + " SelfTest Failure"); return "g3Line"; } private void init() { if (!selfTested) selfTest(); }

  private g3RowVec      sPoint      = null;
  private g3RowVec      ePoint      = null;
  private double        len         = 0;
  private g3ColVec      pnt         = new g3ColVec(0, 0, 0);
  private g3ColVec      dir         = new g3ColVec(0, 0, 0);

  private void init(g3RowVec start, g3RowVec end)
  {
   if (!selfTested) selfTest();
   this.sPoint = start;
   this.ePoint   = end;
   this.pnt.x = start.x; this.pnt.y = start.y; this.pnt.z = start.z;
   this.dir.x = end[1, 1, 1] - start[1, 1, 1]; this.dir.y = end[1, 2, 1] - start[1, 2, 1]; this.dir.z = end[1, 3, 1] - start[1, 3, 1];
   len = Math.Sqrt(this.dir.x * this.dir.x + this.dir.y * this.dir.y + this.dir.z * this.dir.z);
   this.dir.x = this.dir.x / len; this.dir.y = this.dir.y / len; this.dir.z = this.dir.z / len;
  }

  public g3Line(g3RowVec start, g3RowVec end) { init(start, end); }
  public g3Line(g3Line cloneFrom) { init(new g3RowVec(cloneFrom.sPoint), new g3RowVec(cloneFrom.ePoint)); }

  public g3RowVec SPoint {get {return sPoint; } set {init(value, ePoint);                                                                                                                 } }
  public g3RowVec EPoint {get {return ePoint; } set {init(sPoint, value);                                                                                                                 } }
  public g3ColVec Dir    {get {return dir;    } set {ePoint.x = sPoint.x + value.x; ePoint.y = sPoint.y + value.y; ePoint.z = sPoint.z + value.z; init(sPoint, ePoint);                   } }
  public double   Len    {get {return len;    } set {ePoint.x = sPoint.x + value * dir.x; ePoint.y = sPoint.y + value * dir.y; ePoint.z = sPoint.z + value * dir.z; init(sPoint, ePoint); } }
    
  public g3ColVec At(double len)
  {
   g3ColVec ret = new g3ColVec(0, 0, 0);
   ret[1, 1, 1] = pnt[1, 1, 1] + dir[1, 1, 1] * len;
   ret[1, 2, 1] = pnt[1, 2, 1] + dir[1, 2, 1] * len;
   ret[1, 3, 1] = pnt[1, 3, 1] + dir[1, 3, 1] * len;
   return ret;
  }

  public g3Line parallel(double dist)                                                              // must be moved to g2Line, only for testing, all z-Values must be 0 for testing
  {
   g3Line ret = new g3Line(this);
   ret.sPoint.y = sPoint.y + Math.Sqrt(dir.x * dir.x + dir.y * dir.y) * dist / dir.x;
   ret.ePoint.x = ret.sPoint.x + dir.x;
   ret.ePoint.y = ret.sPoint.y + dir.y;
   ret.ePoint.z = ret.sPoint.z + dir.z;
   ret.pnt.x = ret.sPoint.x;
   ret.pnt.y = ret.sPoint.y;
   ret.pnt.z = ret.sPoint.z;
   return ret;
  }

  public g3Line vertical(g3RowVec point)                                                           // must be moved to g2Line, only for testing, all z-Values must be 0 for testing
  {
   g3Line ret = new g3Line(this);
   ret.ePoint.x = point.x + dir.y;
   ret.ePoint.y = point.y - dir.x;
   ret.init(point, ret.ePoint);
   return ret;
  }

  public g3ColVec InterSect(g3Line second) {return second.At((dir[1, 2, 1] * pnt[1, 1, 1] - dir[1, 1, 1] * pnt[1, 2, 1] + dir[1, 1, 1] * second.pnt[1, 2, 1] - dir[1, 2, 1] * second.pnt[1, 1, 1]) / (dir[1, 2, 1] * second.dir[1, 1, 1] - dir[1, 1, 1] * second.dir[1, 2, 1])); }
 
  public double yAt(double x) { return (x * dir.y - pnt.x * dir.y + pnt.y * dir.x) / dir.x; }      // must be moved to g2Line, only for testing, all z-Values must be 0 for testing
  public double xAt(double y) { return (y * dir.x + pnt.x * dir.y - pnt.y * dir.x) / dir.y; }      // must be moved to g2Line, only for testing, all z-Values must be 0 for testing

  private static void selfTest()
  {
   selfTested = true;
   g3Line g = new g3Line(new g3RowVec(0, 0, 0), new g3RowVec(1, 1, 0));
   ass(g.At((Math.Sqrt(2))).x == 1);
   ass(g.At((Math.Sqrt(2))).y == 1);
   
   g3Line h = new g3Line(new g3RowVec(0, 2, 0), new g3RowVec(2, 0, 0));
   g3ColVec s = h.InterSect(g);
   ass(s[1, 1, 1] == 1);
   ass(s[1, 2, 1] == 1);
   ass(s[1, 3, 1] == 0);
   ass(h.yAt(2) == 0);
   ass(h.xAt(1) == 1);
   ass(h.parallel(Math.Sqrt(2)/2).yAt(3) == 0);
  }
    
 }
}








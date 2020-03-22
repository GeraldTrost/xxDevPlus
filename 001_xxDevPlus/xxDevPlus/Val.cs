

//** @author  GeTr Copyright© 1996-2020 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties
//** @license You may use, modify, distribute this code under GNU public dual licence free for NON-Commercial use as long as this notice remains unchanged.
//** @comment General Value



using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using org_xxdevplus_sys;
using org_xxdevplus_utl;
using org_xxdevplus_struct;
using org_xxdevplus_chain;



namespace org_xxdevplus_math
{

 public class Val<typ>
 {
  private static bool selfTested  = false;  private static string ass(bool expr) { if (!expr) throw new Exception(ass(true) + " SelfTest Failure"); return "Val<>";  }
  private void init() { if (!selfTested) selfTest(); }

  public typ[] newTypArray(params object[] items) { typ[] ret = new typ[items.Length]; for (long i = 0; i < items.Length; i++) try { ret[i] = (typ)items[i]; } catch { } return ret; }
  public typ[] newTypArray(int size)              { return newTypArray(new object[size]); }

  internal int[]   dim = new int[0];
  internal typ[]   val = new typ[0];

  public bool Equals(Val<typ> other)
  {
   if (dim.Count() != other.dim.Count()) return false;
   for (int i = 0; i < dim.Count(); i++) if (dim[i] != other.dim[i]) return false;
   for (int i = 1; i <= IterationCoords.Len; i++) if (!this[IterationCoords[i]].Equals(other[IterationCoords[i]])) return false;
   return true;
  }

  private bool countUp(int[] coords, int[] dim)
  {
   for (int i = dim.Length - 1; i >= 0; i--) if (coords[i] < dim[i]) {coords[i]++; return true; } else coords[i] = 1;
   return false;
  }

  private void extend(int dim, int count)
  {
   int[] d = new int[this.dim.Length]; for (int i = 0; i < this.dim.Length; i++) d[i] = this.dim[i];
   d[dim] += count;
   Val<typ> tmp = new Val<typ>(d);
   int[] coords = new int[this.dim.Length]; for (int i = 0; i < this.dim.Length; i++) coords[i] = 1;
   do tmp[coords] = this[coords]; while (countUp(coords, this.dim));
   val       = tmp.val;
   this.dim  = tmp.dim;
  }

  public Pile<int[]> IterationCoords = new Pile<int[]>();

  private void init(params int[] dim)
  {
   if (!selfTested) selfTest();
   this.dim = new int[dim.Length];
   for(int i = 0; i < dim.Length; i++) this.dim[i] = dim[i];
   int vallen = 1; for(int i = 0; i < dim.Length; i++) if (dim[i] > 0 ) vallen *= dim[i];
   this.val = newTypArray(vallen);
   int[] coords = new int[this.dim.Length]; for (int i = 0; i < this.dim.Length; i++) coords[i] = 1;
   bool carry = false;
   do
   {
    int[] c = new int[this.dim.Length]; for (int i = 0; i < this.dim.Length; i++) c[i] = coords[i];
    IterationCoords.Push(c);
    for (int i = dim.Length - 1; i >= 0; i--) if (coords[i] < dim[i]) { coords[i]++; break; } else { coords[i] = 1; carry = (i == 0); }
   }
   while (!carry);
  }
  
  public Val(typ v)               {init(1);             val[0]  = v;                                                      }
  public Val(Val<typ> cloneFrom)  {init(cloneFrom.dim); for (int i = 0; i < val.Length; i++) val[i] = cloneFrom.val[i];   }
  public Val(params int[] dim)    {init(dim);                                                                             }

  //xxx public Val<typ> pls(Val<typ> second) {Val<typ> ret = new Val<typ>(this); foreach (long[] coords in ret.Iterate) ret[coords] = ret[coords] + second[coords]; return ret; }
  //xxx public Val<typ> lss(Val<typ> second) {Val<typ> ret = new Val<typ>(this); foreach (long[] coords in ret.Iterate) ret[coords] = ret[coords] - second[coords]; return ret; }

  public typ gat(int inx) { return val[inx - 1]; } public void sat(typ value, int inx) { val[inx - 1] = value; }

  public typ this[params int[] coords]
  {
   get { return gat(inx(coords)); }
   set { sat(value, inx(coords)); }
  }

  private int inx(params int[] coords)
  {
   int ret = coords[0] - 1;
   for (int i = 1; i < coords.Length; i++) ret = ret * dim[i] + (coords[i] - 1);
   return ret + 1;
  }

  public typ Value { get { return val[0]; } set { val[0] = value; } }

  private string printable()
  {

   string ret = "";
   int[] coords = new int[this.dim.Length]; for (int i = 0; i < this.dim.Length; i++) coords[i] = 1;
   bool carry = false;
   do
   {
    //if (!ret.EndsWith(",")) ret += "(";
    int cnt = 0;
    for (cnt = 0; cnt < coords.Length; cnt++) if (coords[coords.Length - 1 - cnt] != 1) break;
    for (int i = 1; i <= cnt ; i++) ret += "(";
    ret += ("" + this[coords]).Replace(",", ".") + ",";
    for (int i = dim.Length - 1; i >= 0; i--)
    {
     if (coords[i] < dim[i])
     {
      coords[i]++;
      break;
     } else
     {
      coords[i] = 1;
      ret = ret.Substring(0, ret.Length - 1) + "),";
      carry = (i == 0);
     }
    }
   }
   while (!carry);
   return ret.Substring(0, ret.Length - 1);
  }


  private static void selfTest()
  {
   selfTested = true;

   Val<double> v, a, b;
   v = new Val<double>(3, 4, 2); ass(v.inx(3, 2, 2) == 20);
   v = new Val<double>(5.2); ass(v.Value == 5.2); ass(v[1] == 5.2); ass(v.printable().Equals("(5.2)"));                                                     // use Val to store a real
   v = new Val<double>(2, 3); ass(v.inx(2, 1) == 4); ass(v.inx(2, 3) == 6); ass(v.printable().Equals("((0,0,0),(0,0,0))"));                                 // 2 Spalten in x-Richtung, 3 Zeilen in y-Richtung, 0 Lagen in z-Richtung
   v = new Val<double>(2, 3, 5); ass(v.printable().Equals("(((0,0,0,0,0),(0,0,0,0,0),(0,0,0,0,0)),((0,0,0,0,0),(0,0,0,0,0),(0,0,0,0,0)))"));                // 2 Spalten in x-Richtung, 3 Zeilen in y-Richtung, 5 Lagen in z-Richtung
   v = new Val<double>(2, 3, 1); ass(v.printable().Equals("(((0),(0),(0)),((0),(0),(0)))")); ass(v.inx(2, 1) == 4); ass(v.inx(2, 3) == 6);                  // 2 Spalten in x-Richtung, 3 Zeilen in y-Richtung, 0 Lagen in z-Richtung
   ass(v[1, 1] == 0); ass(v.val.Length == 6); v[2, 1] = 22.22; ass(v[2, 1] == v[2, 1, 1]);                                                                  // given Koordinate of NullSized Dimension does not matter: Matrix with 2 x-Dims and 3 y-Dims and 0 z-Dims has identical values at [1,1,0] and at [1,1,55]
   v[1, 1] = 33.33; ass(v[1, 1] == v[1, 1, 1]); ass(v[1, 1] == v[1, 1, 1]);                                                                                 // given Koordinate of NullSized Dimension does not matter: Matrix with 2 x-Dims and 3 y-Dims and 0 z-Dims has identical values at [1,1,0] and at [1,1,55]
   v = new Val<double>(2, 1, 5); ass(v.val.Length == 10); v[1, 1, 1] = 32; ass(v.inx(1, 1, 1) == 1); ass(v.inx(1, 1, 5) == 5); ass(v.inx(2, 1, 1) == 6);
   ass(v.val[0] == 32); v[2, 1, 5] = 17; ass(v[2, 1, 5] == 17); ass(v.val[v.val.Length - 1] == 17);

   a = new Val<double>(2); a[1] = 2.1; a[2] = 2.4; b = new Val<double>(2); b[1] = 1.1; b[2] = 1.4; //xxxx ass(a.pls(b).printable().Equals("(3.2,3.8)"));                       // Addition zweier Punkte oder Komplexer Zahlen (Zeilenvektoren)
   a = new Val<double>(1, 2); a[1, 1] = 2.1; a[1, 2] = 2.4; b = new Val<double>(1, 2); b[1, 1] = 1.1; b[1, 2] = 1.4; //xxxx ass(a.pls(b).printable().Equals("((3.2,3.8))"));   // Addition zweier Ortsvektoren oder Vektoren (Spaltenvektoren)

  }





 
 }
}


















//** @author  GeTr Copyright© 1996-2020 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties
//** @license You may use, modify, distribute this code under GNU public dual licence free for NON-Commercial use as long as this notice remains unchanged.
//** @comment General Value

package org.xxdevplus.math;

import java.lang.Double;
import java.lang.Object;
import java.lang.reflect.Array;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import org.xxdevplus.struct.Pile;
//import org.springframework.core.GenericTypeResolver;

/**
 *
 * @author GeTr
 */

 public class Val<typ>
 {
  private static boolean selfTested  = false;  private static String ass(boolean expr) throws Exception { if (!expr) throw new Exception(ass(true) + " SelfTest Failure"); return "Val<>";  } 
  private void init() throws Exception { if (!selfTested) selfTest(); }

  public     typ[] newTypArray(Object... items)  { typ[] ret = (typ[])Array.newInstance(this.getClass(), items.length); for (int i = 0; i < items.length; i++) ret[i] = (typ)items[i]; return ret; }
  public     typ[] newTypArray(int size)         { return newTypArray(new Object[size]); }

  int[]      dim = new int[0];
  Object[]   val = new Object[0];


  /*
  private Class<typ>  testObject;
  private Val<Double> zeroDouble   = new Val<Double>(0.0);
  private Val<Long>   zeroLong     = new Val<Long>(0L);
  */

  public boolean Equals(Val<typ> other)
  {
   if (dim.length != other.dim.length) return false;
   for (int i = 0; i < dim.length; i++) if (dim[i] != other.dim[i]) return false;
   for (int i = 1; i <= IterationCoords.Len(); i++) if (!g(IterationCoords.g(i)).equals(other.g(IterationCoords.g(i)))) return false;
   return true;
  }

  private boolean countUp(int[] coords, int[] dim)
  {
   for (int i = dim.length - 1; i >= 0; i--) if (coords[i] < dim[i]) {coords[i]++; return true; } else coords[i] = 1;
   return false;
  }

  private void extend(int dim, int count) throws Exception
  {
   int[] d = new int[this.dim.length]; for (int i = 0; i < this.dim.length; i++) d[i] = this.dim[i];
   d[dim] += count;
   Val<typ> tmp = new Val<typ>(d);
   int[] coords = new int[this.dim.length]; for (int i = 0; i < this.dim.length; i++) coords[i] = 1;
   do tmp.s(g(coords), coords); while (countUp(coords, this.dim));
   val       = tmp.val;
   this.dim  = tmp.dim;
  }

  public Pile<int[]> IterationCoords = new Pile<int[]>();


  private void init(int... dim) throws Exception
  {
   if (!selfTested) selfTest();

   //Class<typ> t = (Class<typ>) GenericTypeResolver.resolveTypeArgument(getClass(), Val.class);

   /*

   Object o  = ((ParameterizedType)(getClass().getGenericSuperclass())).getActualTypeArguments();

   //Class cls  = (Class) (getClass().getGenericSuperclass());

   //Object o  = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];

   //Object o  = ((Class) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]);

   Class<typ> t = (Class<typ>) TypeResolver.resolveArgument( getClass(), Val.class );
   if (t == Long.class || t == Long.TYPE)
   {
    String x = "";
   }
   else
    if (t == Double.class || t == Double.TYPE)
    {
     String x = "";
    }
    else throw new Exception("unsupported typ in Val, only Val<Long> and Val<Double> are valid generic Classes!");

   */

   this.dim = new int[dim.length];
   for(int i = 0; i < dim.length; i++) this.dim[i] = dim[i];
   int vallen = 1; for(int i = 0; i < dim.length; i++) if (dim[i] > 0 ) vallen *= dim[i];
   this.val = newTypArray(vallen);
   Object[] _val = new Object[vallen]; for (int i = 1; i <= vallen; i++) _val[i - 1] = 0.0;
   this.val = (typ[]) _val;
   int[] coords = new int[this.dim.length]; for (int i = 0; i < this.dim.length; i++) coords[i] = 1;
   boolean carry = false;
   do
   {
    int[] c = new int[this.dim.length]; for (int i = 0; i < this.dim.length; i++) c[i] = coords[i];
    IterationCoords.Push(c);
    for (int i = dim.length - 1; i >= 0; i--) if (coords[i] < dim[i]) { coords[i]++; break; } else { coords[i] = 1; carry = (i == 0); }
   }
   while (!carry);
  }
  
  public Val(typ v) throws Exception { init(1); val = (typ[])(new Object[] {v}); }

  public Val(Val<typ> cloneFrom) throws Exception  {init(cloneFrom.dim); for (int i = 0; i < val.length; i++) val[i] = cloneFrom.val[i];   }
  public Val(int... dim) throws Exception          {init(dim);                                                                             }

  //xxx public Val<typ> pls(Val<typ> second) {Val<typ> ret = new Val<typ>(this); foreach (long[] coords in ret.Iterate) ret[coords] = ret[coords] + second[coords]; return ret; }
  //xxx public Val<typ> lss(Val<typ> second) {Val<typ> ret = new Val<typ>(this); foreach (long[] coords in ret.Iterate) ret[coords] = ret[coords] - second[coords]; return ret; }

  public typ   g (           int... coords) { return (typ)(val[inx(coords) - 1]);  }
  public void  s (typ value, int... coords) { val[inx(coords) - 1] = value; }

  private int inx(int... coords)
  {
   int ret = coords[0] - 1;
   for (int i = 1; i < coords.length; i++) ret = ret * dim[i] + (coords[i] - 1);
   return ret + 1;
  }

  public typ Value() { return (typ)(val[0]); } public void Value (typ value) { val[0] = (Object)value; }

  private String printable()
  {

   String ret = "";
   int[] coords = new int[this.dim.length]; for (int i = 0; i < this.dim.length; i++) coords[i] = 1;
   boolean carry = false;
   do
   {
    //if (!ret.EndsWith(",")) ret += "(";
    int cnt = 0;
    for (cnt = 0; cnt < coords.length; cnt++) if (coords[coords.length - 1 - cnt] != 1) break;
    for (int i = 1; i <= cnt ; i++) ret += "(";
    ret += ((typ)(g(coords))).toString().replace(",", ".") + ",";  //ret += ("" + g(coords)).replace(",", ".") + ",";
    for (int i = dim.length - 1; i >= 0; i--)
    {
     if (coords[i] < dim[i])
     {
      coords[i]++;
      break;
     } else
     {
      coords[i] = 1;
      ret = ret.substring(0, ret.length() - 1) + "),";
      carry = (i == 0);
     }
    }
   }
   while (!carry);
   return ret.substring(0, ret.length() - 1);
  }


  private static void selfTest() throws Exception
  {
   selfTested = true;
   Val<Double>  v, a, b;
   Val<Long>    w, c, d;
   v = new Val<Double>(3, 4, 2); ass(v.inx(3, 2, 2) == 20);
   v = new Val<Double>(5.2); ass(v.Value() == 5.2); ass(v.g(1) == 5.2); ass(v.printable().equals("(5.2)"));                                                 // use Val to store a real

   /*
   w = new Val<Long>(7L);
   ass(w.Value() == 7);
   ass(w.g(1) == 7);
   ass(w.printable().equals("(7)"));                                                           // use Val to store a long
    */

   v = new Val<Double>(2, 3); 
   ass(v.inx(2, 1) == 4);
   ass(v.inx(2, 3) == 6);
   ass(v.printable().equals("((0.0,0.0,0.0),(0.0,0.0,0.0))"));
   // 2 Spalten in x-Richtung, 3 Zeilen in y-Richtung, 0 Lagen in z-Richtung

   /*
   w = new Val<Long>(2, 3);
   ass(w.inx(2, 1) == 4);
   ass(w.inx(2, 3) == 6);
   ass(w.g(1, 1) == 0);
   w.s(27L, 1, 1);
   ass(w.g(1, 1) == 27);
   ass(w.printable().equals("((0,0,0),(0,0,0))"));
   */

   // 2 Spalten in x-Richtung, 3 Zeilen in y-Richtung, 0 Lagen in z-Richtung

   v = new Val<Double>(2, 3, 5); ass(v.printable().equals("(((0.0,0.0,0.0,0.0,0.0),(0.0,0.0,0.0,0.0,0.0),(0.0,0.0,0.0,0.0,0.0)),((0.0,0.0,0.0,0.0,0.0),(0.0,0.0,0.0,0.0,0.0),(0.0,0.0,0.0,0.0,0.0)))"));                // 2 Spalten in x-Richtung, 3 Zeilen in y-Richtung, 5 Lagen in z-Richtung
   v = new Val<Double>(2, 3, 1); ass(v.printable().equals("(((0.0),(0.0),(0.0)),((0.0),(0.0),(0.0)))")); ass(v.inx(2, 1) == 4); ass(v.inx(2, 3) == 6);                  // 2 Spalten in x-Richtung, 3 Zeilen in y-Richtung, 0 Lagen in z-Richtung
   ass(v.g(1, 1) == 0); v.s(22.22, 2, 1); ass(v.g(2, 1) == v.g(2, 1, 1));                                                                                               // given Koordinate of NullSized Dimension does not matter: Matrix with 2 x-Dims and 3 y-Dims and 0 z-Dims has identical values at [1,1,0] and at [1,1,55]
   v.s(33.33, 1, 1); ass(v.g(1, 1) == v.g(1, 1, 1)); ass(v.g(1, 1) == v.g(1, 1, 1));                                                                                    // given Koordinate of NullSized Dimension does not matter: Matrix with 2 x-Dims and 3 y-Dims and 0 z-Dims has identical values at [1,1,0] and at [1,1,55]
   v = new Val<Double>(2, 1, 5); ass(v.val.length == 10); v.s(32.0, 1, 1, 1); ass(v.inx(1, 1, 1) == 1); ass(v.inx(1, 1, 5) == 5); ass(v.inx(2, 1, 1) == 6);  ass((Double)v.val[0] == 32.0);
   v.s(17.0, 2, 1, 5); ass(v.g(2, 1, 5) == 17.0); ass((Double)(v.val[v.val.length - 1]) == 17.0);
   a = new Val<Double>(2); a.s(2.1, 1); a.s(2.4, 2); b = new Val<Double>(2); b.s(1.1, 1); b.s(1.4, 2); //xxxx ass(a.pls(b).printable().Equals("(3.2,3.8)"));                       // Addition zweier Punkte oder Komplexer Zahlen (Zeilenvektoren)
   a = new Val<Double>(1, 2); a.s(2.1, 1, 1); a.s(2.4, 1, 2); b = new Val<Double>(1, 2); b.s(1.1, 1, 1); b.s(1.4, 1, 2); //xxxx ass(a.pls(b).printable().Equals("((3.2,3.8))"));   // Addition zweier Ortsvektoren oder Vektoren (Spaltenvektoren)
  }
 
 }





/*
public class Val<type>
 {
  //Copyright© 1996-2020 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties, You may use, modify, distribute this code as long as this notice remains unchanged.
  private static boolean selfTested  = false;  private static String ass(boolean expr) throws Exception { if (!expr) throw new Exception(ass(true) + " Val SelfTest Failure"); return "Val<>";  } private void init() throws Exception { if (!selfTested) selfTest(); }

  public type[] newArray(Object... items)  { type[] ret = (type[])Array.newInstance(this.getClass(), items.length); for (int i = 0; i < items.length; i++) ret[i] = (type)items[i]; return ret; }
  public type[] newArray(int size)         { return newArray(new Object[size]); }
  
  int[]      dim = new int[0];
  type[]     val = newArray(0);

  public boolean Equals(Val<type> other)
  {
   if (dim.length != other.dim.length) return false;
   for (int i = 0; i < dim.length; i++) if (dim[i] != other.dim[i]) return false;
   for (int i = 1; i <= IterationCoords.Len(); i++) if (!get(IterationCoords.get(i)).equals(other.get(IterationCoords.get(i)))) return false;
   return true;
  }

  private boolean countUp(int[] coords, int[] dim)
  {
   for (int i = dim.length - 1; i >= 0; i--) if (coords[i] < dim[i]) {coords[i]++; return true; } else coords[i] = 1;
   return false;
  }

  private void extend(int dim, int count) throws Exception
  {
   int[] d = new int[this.dim.length]; for (int i = 0; i < this.dim.length; i++) d[i] = this.dim[i];
   d[dim] += count;
   Val<type> tmp = new Val<type>(d);
   int[] coords = new int[this.dim.length]; for (int i = 0; i < this.dim.length; i++) coords[i] = 1;
   do tmp.set(get(coords), coords); while (countUp(coords, this.dim));
   val       = tmp.val;
   this.dim  = tmp.dim;
  }

  public Pack<int[]> IterationCoords = new Pack<int[]>();

  private void init(int... dim) throws Exception
  {
   if (!selfTested) selfTest();
   this.dim = new int[dim.length];
   for(int i = 0; i < dim.length; i++) this.dim[i] = dim[i];
   int vallen = 1; for(int i = 0; i < dim.length; i++) if (dim[i] > 0 ) vallen *= dim[i];
   this.val = newArray(vallen);
   int[] coords = new int[this.dim.length]; for (int i = 0; i < this.dim.length; i++) coords[i] = 1;
   boolean carry = false;
   do
   {
    int[] c = new int[this.dim.length]; for (int i = 0; i < this.dim.length; i++) c[i] = coords[i];
    IterationCoords.push(c);
    for (int i = dim.length - 1; i >= 0; i--) if (coords[i] < dim[i]) { coords[i]++; break; } else { coords[i] = 1; carry = (i == 0); }
   }
   while (!carry);
  }
  
  public Val(type v) throws Exception               {init(1);             val[0]  = v;                                                      }
  public Val(Val<type> cloneFrom) throws Exception  {init(cloneFrom.dim); for (int i = 0; i < val.length; i++) val[i] = cloneFrom.val[i];   }
  public Val(int... dim) throws Exception           {init(dim);                                                                             }

  //xxx public Val<type> pls(Val<type> second) {Val<type> ret = new Val<type>(this); foreach (int[] coords in ret.Iterate) ret[coords] = ret[coords] + second[coords]; return ret; }
  //xxx public Val<type> lss(Val<type> second) {Val<type> ret = new Val<type>(this); foreach (int[] coords in ret.Iterate) ret[coords] = ret[coords] - second[coords]; return ret; }

  public type get(int... coords)              {return (type)(val[inx(coords) - 1]); }
  public void set(type value, int... coords)  {val[inx(coords) - 1] = value;        }
  private int inx(int... coords) {int ret = coords[0] - 1; for (int i = 1; i < coords.length; i++) ret = ret * dim[i] + (coords[i] - 1); return ret + 1; }

  public type getValue (          ) { return val[0];   } 
  public void setValue (type value) { val[0] = value;  }

  private String printable()
  {

   String ret = "";
   int[] coords = new int[this.dim.length]; for (int i = 0; i < this.dim.length; i++) coords[i] = 1;
   boolean carry = false;
   do
   {
    //if (!ret.EndsWith(",")) ret += "(";
    int cnt = 0;
    for (cnt = 0; cnt < coords.length; cnt++) if (coords[coords.length - 1 - cnt] != 1) break;
    for (int i = 1; i <= cnt ; i++) ret += "(";
    ret += ("" + get(coords)).replace(",", ".") + ",";
    for (int i = dim.length - 1; i >= 0; i--)
    {
     if (coords[i] < dim[i])
     {
      coords[i]++;
      break;
     } else
     {
      coords[i] = 1;
      ret = ret.substring(0, ret.length() - 1) + "),";
      carry = (i == 0);
     }
    }
   }
   while (!carry);
   return ret.substring(0, ret.length() - 1);
  }


  private static void selfTest() throws Exception
  {
   selfTested = true;
   Val<Double> v;
   Val<Double> a, b;
   v = new Val<Double>(3, 4, 2); ass(v.inx(3, 2, 2) == 20);
   v = new Val<Double>(5.2); ass(v.getValue() == 5.2); ass(v.get(1) == 5.2); ass(v.printable().equals("(5.2)"));                                                     // use Val to store a real
   v = new Val<Double>(2, 3); ass(v.inx(2, 1) == 4); ass(v.inx(2, 3) == 6); ass(v.printable().equals("((0,0,0),(0,0,0))"));                                 // 2 Spalten in x-Richtung, 3 Zeilen in y-Richtung, 0 Lagen in z-Richtung
   v = new Val<Double>(2, 3, 5); ass(v.printable().equals("(((0,0,0,0,0),(0,0,0,0,0),(0,0,0,0,0)),((0,0,0,0,0),(0,0,0,0,0),(0,0,0,0,0)))"));                // 2 Spalten in x-Richtung, 3 Zeilen in y-Richtung, 5 Lagen in z-Richtung
   v = new Val<Double>(2, 3, 1); ass(v.printable().equals("(((0),(0),(0)),((0),(0),(0)))")); ass(v.inx(2, 1) == 4); ass(v.inx(2, 3) == 6);                  // 2 Spalten in x-Richtung, 3 Zeilen in y-Richtung, 0 Lagen in z-Richtung
   ass(v.get(1, 1) == 0); ass(v.val.length == 6); v.set(22.22, 2, 1); ass(v.get(2, 1) == v.get(2, 1, 1));                                                                  // given Koordinate of NullSized Dimension does not matter: Matrix with 2 x-Dims and 3 y-Dims and 0 z-Dims has identical values at [1,1,0] and at [1,1,55]
   v.set(33.33, 1, 1); ass(v.get(1, 1) == v.get(1, 1, 1)); ass(v.get(1, 1) == v.get(1, 1, 1));                                                                                 // given Koordinate of NullSized Dimension does not matter: Matrix with 2 x-Dims and 3 y-Dims and 0 z-Dims has identical values at [1,1,0] and at [1,1,55]
   v = new Val<Double>(2, 1, 5); ass(v.val.length == 10); v.set(32.0, 1, 1, 1); ass(v.inx(1, 1, 1) == 1); ass(v.inx(1, 1, 5) == 5); ass(v.inx(2, 1, 1) == 6);
   ass(v.val[0] == 32); v.set(17.0, 2, 1, 5); ass(v.get(2, 1, 5) == 17); ass(v.val[v.val.length - 1] == 17);
   a = new Val<Double>(2); a.set(2.1, 1); a.set(2.4, 2); b = new Val<Double>(2); b.set(1.1, 1); b.set(1.4, 2); //xxxx ass(a.pls(b).printable().Equals("(3.2,3.8)"));                       // Addition zweier Punkte oder Komplexer Zahlen (Zeilenvektoren)
   a = new Val<Double>(1, 2); a.set(2.1, 1, 1); a.set(2.4, 1, 2); b = new Val<Double>(1, 2); b.set(1.1, 1, 1); b.set(1.4, 1, 2); //xxxx ass(a.pls(b).printable().Equals("((3.2,3.8))"));   // Addition zweier Ortsvektoren oder Vektoren (Spaltenvektoren)
  }
 }
 */



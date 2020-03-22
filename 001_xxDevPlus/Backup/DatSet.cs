using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using ndBase;

namespace ndString
{
 public class DatSet
 {
  //Copyright© 1996-2012 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties, You may use, modify, distribute this code under GNU public licence for NON-Commercial use as long as this notice remains unchanged.
  private static bool selfTested  = false;  private static string ass(bool expr) { if (!expr) throw new Exception(ass(true) + " SelfTest Failure"); return "DatSet"; } private static void selfTest() { selfTested = true; } private void init() { if (!selfTested) selfTest(); }
 
  public Pile<Reach>        nam  = new Pile<Reach>();
  public Pile<Reach>        typ  = new Pile<Reach>();

  //public Pile<Pile<Reach>>  rows = new Pile<Pile<object>>();

  public Pile<ObjPile>      Raws = new Pile<ObjPile>();

  private Pile<Pile<Reach>>  rows = new Pile<Pile<Reach>>();

  public Pile<Pile<Reach>>  Rows
  {
   get 
   {
    if (rows.Len != Raws.Len)
    {
     rows = new Pile<Pile<Reach>>();
     foreach (ObjPile raw in Raws)
     {
      Pile<Reach> row = new Pile<Reach>();
      foreach (object obj in raw)
      {
       Type t = obj.GetType();
       if (t == typeof(Reach))  row.Push((Reach)obj);
       if (t == typeof(string)) row.Push((string)obj);
       if (t == typeof(double)) row.Push(("" + (double)obj).Replace(", ", "."));
       if (t == typeof(long))   row.Push("" + (long)obj);
      }
      rows.Push(row);
     }
    }
    return rows;
   }
  }
  
  public Pile<Pile<Reach>>  Cols
  {
   get 
   {
    if (Rows.Len == 0) return new Pile<Pile<Reach>>();
    Pile<Pile<Reach>> cols = new Pile<Pile<Reach>>(Rows[1].Len);
    for (int i = 1; i <= Rows[1].Len; i++) cols[i] = new Pile<Reach>();
    foreach (Pile<Reach> row in Rows) for (int i = 1; i <= row.Len; i++) cols[i].Push(row[i]);
    return cols;
   }
  }
 }
}

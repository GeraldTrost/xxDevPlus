



//** @author  GeTr Copyright© 1996-2020 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties
//** @license You may use, modify, distribute this code under GNU public dual licence free for NON-Commercial use as long as this notice remains unchanged.
//** @comment Set of Data


package org.xxdevplus.data;

import org.xxdevplus.chain.Chain;
import org.xxdevplus.struct.ObjPile;
import org.xxdevplus.struct.Pile;


 public class DatSet
 {
  //Copyright© 1996-2020 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties, You may use, modify, distribute this code under GNU public licence for NON-Commercial use as long as this notice remains unchanged.
  private static boolean selfTested  = false;  private static String ass(boolean expr) throws Exception { if (!expr) throw new Exception(ass(true) + " SelfTest Failure"); return "DatSet"; } private static void selfTest() { selfTested = true; } private void init() { if (!selfTested) selfTest(); }
 
  public Pile<Chain>        nam  = new Pile<Chain>();
  public Pile<Chain>        typ  = new Pile<Chain>();

  //public Pack<Pile<Reach>>  rows = new Pack<Pile<object>>();

  public Pile<ObjPile>      Raws = new Pile<ObjPile>();

  private Pile<Pile<Chain>>  rows = new Pile<Pile<Chain>>();

  public Pile<Pile<Chain>>  Rows() throws Exception
  {
   if (rows.Len() != Raws.Len())
   {
    rows = new Pile<Pile<Chain>>();
    for (ObjPile raw : Raws)
    {
     Pile<Chain> row = new Pile<Chain>();
     for (Object obj : raw)
     {
      if (obj instanceof Chain)  row.Push((Chain)obj);
      if (obj instanceof String) row.Push(new Chain((String)obj));
      if (obj instanceof Double) row.Push(new Chain(("" + (Double)obj).replace(", ", ".")));
      if (obj instanceof Long)   row.Push(new Chain("" + (Long)obj));
     }
     rows.Push(row);
    }
   }
   return rows;
  }
  
  public Pile<Pile<Chain>>  Cols() throws Exception
  {
   if (Rows().Len() == 0) return new Pile<Pile<Chain>>();
   Pile<Pile<Chain>> cols = new Pile<Pile<Chain>>(Rows().g(1).Len());
   for (int i = 1; i <= Rows().g(1).Len(); i++) cols.s(new Pile<Chain>(), i);
   for (Pile<Chain> row : Rows()) for (int i = 1; i <= row.Len(); i++) cols.g(i).Push(row.g(i));
   return cols;
   }
 }



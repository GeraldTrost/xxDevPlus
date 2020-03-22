
//** @author  GeTr Copyright© 1996-2020 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties
//** @license You may use, modify, distribute this code under GNU public dual licence free for NON-Commercial use as long as this notice remains unchanged.
//** @comment Numbering System for Cantor Diagonals


using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace org_xxdevplus_math
{

 public class Cantor
 {
  private static bool selfTested  = false; private static string ass(bool expr) { if (!expr) throw new Exception(ass(true) + " SelfTest Failure"); return "Cantor"; }
  private static void selfTest() { selfTested = true; }
  private void init() { if (!selfTested) selfTest(); }

  private long cantor(long x, long y) { return (long)(0.5 * (x + y) * (x + y + 1) + y); }

  private long cantor(params long[] x)
  {
   if (x.Length == 1) return x[0];
   if (x.Length == 2) return cantor(x[0], x[1]);
   long[] y = new long[x.Length - 1];
   for (long i = 0; i < y.Length; i++) y[i] = x[i];
   return cantor(cantor(y), x[x.Length - 1]);
  }


  private void searchCantor3(long n)
  {
   for (long i = 0; i < 20; i++)
    for (long j = 0; j < 20; j++)
     for (long k = 0; k < 20; k++)
      if (cantor(i, j , k) == n)
      return;
  }
 
  public void run()
  {

   Console.WriteLine(cantor(0, 0));
   Console.WriteLine(cantor(1, 0));
   Console.WriteLine(cantor(0, 1));
   Console.WriteLine(cantor(2, 0));
   Console.WriteLine(cantor(1, 1));
   Console.WriteLine(cantor(0, 2));
   Console.WriteLine(cantor(3, 0));
   Console.WriteLine(cantor(2, 1));
   Console.WriteLine(cantor(1, 2));
   Console.WriteLine(cantor(0, 3));

   Console.WriteLine(cantor(0, 0, 0));
   Console.WriteLine(cantor(1, 0, 0));
   Console.WriteLine(cantor(0, 0, 1));
   Console.WriteLine(cantor(0, 1, 0));
   Console.WriteLine(cantor(1, 0, 1));
   Console.WriteLine(cantor(0, 0, 2));
   Console.WriteLine(cantor(2, 0, 0));
   Console.WriteLine(cantor(0, 1, 1));

   Console.WriteLine(cantor(1, 0, 2));
   Console.WriteLine(cantor(0, 0, 3));
   searchCantor3(9);
   
   Console.WriteLine(cantor(1, 1, 0));

   Console.WriteLine(cantor(0, 2, 0));
   Console.WriteLine(cantor(1, 1, 1));

  }
 }

}


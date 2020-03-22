
//** @author  GeTr Copyright© 1996-2020 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties
//** @license You may use, modify, distribute this code under GNU public dual licence free for NON-Commercial use as long as this notice remains unchanged.
//** @comment Numbering System for Cantor Diagonals

package org.xxdevplus.math;

 public class Cantor
 {
  private static boolean selfTested  = false; private static String ass(boolean expr) throws Exception { if (!expr) throw new Exception(ass(true) + " SelfTest Failure"); return "Cantor"; } 
  private static void selfTest() { selfTested = true; } 
  private void init() { if (!selfTested) selfTest(); }

  private long cantor(long x, long y) { return (long)(0.5 * (x + y) * (x + y + 1) + y); }

  private long cantor(long... x)
  {
   if (x.length == 1) return x[0];
   if (x.length == 2) return cantor(x[0], x[1]);
   long[] y = new long[x.length - 1];
   for (long i = 0; i < y.length; i++) y[(int)i] = x[(int)i];
   return cantor(cantor(y), x[x.length - 1]);
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

   System.out.println(cantor(0, 0));
   System.out.println(cantor(1, 0));
   System.out.println(cantor(0, 1));
   System.out.println(cantor(2, 0));
   System.out.println(cantor(1, 1));
   System.out.println(cantor(0, 2));
   System.out.println(cantor(3, 0));
   System.out.println(cantor(2, 1));
   System.out.println(cantor(1, 2));
   System.out.println(cantor(0, 3));

   System.out.println(cantor(0, 0, 0));
   System.out.println(cantor(1, 0, 0));
   System.out.println(cantor(0, 0, 1));
   System.out.println(cantor(0, 1, 0));
   System.out.println(cantor(1, 0, 1));
   System.out.println(cantor(0, 0, 2));
   System.out.println(cantor(2, 0, 0));
   System.out.println(cantor(0, 1, 1));

   System.out.println(cantor(1, 0, 2));
   System.out.println(cantor(0, 0, 3));
   searchCantor3(9);
   
   System.out.println(cantor(1, 1, 0));

   System.out.println(cantor(0, 2, 0));
   System.out.println(cantor(1, 1, 1));

  }
 }

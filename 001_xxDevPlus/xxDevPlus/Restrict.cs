

//** @author  GeTr Copyright© 1996-2020 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties
//** @license You may use, modify, distribute this code under GNU public dual licence free for NON-Commercial use as long as this notice remains unchanged.
//** @comment Marks the used region of a Char-Buffer


using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;


namespace org_xxdevplus_chain
{

 internal class Restrict //Deprecated, should be replaced with ndBase.store
 {
  //Copyright© 1996-2012 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties, You may use, modify, distribute this code under GNU public licence for NON-Commercial use as long as this notice remains unchanged.
  private static bool selfTested = false; private static string ass(bool expr) { if (!expr) throw new Exception(ass(true) + " SelfTest Failure"); return "Restrict"; }
  private static void selfTest() { selfTested = true; }
  private void init() { if (!selfTested) selfTest(); }

  public bool fitted = false;
  public int fullLen = 0;
  public int sPos = 0;
  public int len = 0;
  public int ePos = 0;
  public bool sWide = false;  //skip empty blocks before sPos if applicable
  public bool eWide = false;  //skip empty blocks after ePos if applicable


  //private Reach      _source  = null;
  //private BlockList  _scope   = null; //AttGeTr: this must be built on Demand! Diomensions are used very often so we MUST aviod to implicitly generate thousands of Labels for EACH'n EVERY Character in root.buf!
  //public BlockList scope { get { if (_scope == null) buildScope(_source); return _scope; } }


  private bool upward = false;

  // A string has the following indexes, as shown in this example:
  // String:                      a  b  c  d  e  f  g
  // index:                    0  1  2  3  4  5  6  7   8
  // index:                   -8 -7 -6 -5 -4 -3 -2 -1   0
  // In this Example the indics 8, -8 and 0 are outside the string bounds
  // !!! BUT 0 IS NOT A UNIQUE INDEX !!! 0 may be "before the forst character" or 0 may be "after the last character"
  // So we need the directive flag "upward" to decide wheather 0 is in front position or 0 is in rear position

  private int absPos(int len, int pos) { if (upward) { if (pos >= 0) return pos; return len + pos + 1; } else { if (pos > 0) return pos; return len + pos + 1; } }

  public Restrict(bool upward, Reach source, long pattern, int sPosVal, int LenVal, int ePosVal)
  {
   this.upward = upward;
   fullLen = source.len;
   sWide = (pattern >= 10000);
   eWide = (pattern % 2 > 0);
   if (sWide) pattern -= 10000;
   pattern = pattern / 10;
   switch (pattern)
   {
    case 000:             //no Pos is given
     sPos = 1;
     ePos = fullLen;
     break;
    case 100:             //only StartPos is given
     sPos = absPos(fullLen, sPosVal);
     ePos = fullLen;
     break;
    case 001:             //only EndPos is given
     sPos = 1;
     ePos = absPos(fullLen, ePosVal);
     break;
    case 101:             //StartPos and EndPos are given
     sPos = absPos(fullLen, sPosVal);
     ePos = absPos(fullLen, ePosVal);
     break;
    case 010:             //only Len is given
     if (LenVal < 0) { ePos = fullLen; sPos = ePos + LenVal + 1; } else { sPos = 1; ePos = sPos + LenVal - 1; }
     break;
    case 110:             //StartPos and Len are given
     if (LenVal < 0) { ePos = absPos(fullLen, sPosVal); sPos = ePos + LenVal + 1; } else { sPos = absPos(fullLen, sPosVal); ePos = sPos + LenVal - 1; }
     break;
    case 011:             //Len and EndPos are given
     if (LenVal < 0) { sPos = absPos(fullLen, ePosVal); ePos = sPos - LenVal - 1; } else { ePos = absPos(fullLen, ePosVal); sPos = ePos - LenVal + 1; }
     break;
    case 111:             //StartPos and Len and EndPos are given
     if (LenVal < 0) { sPos = absPos(fullLen, ePosVal); ePos = absPos(fullLen, sPosVal); } else { sPos = absPos(fullLen, sPosVal); ePos = absPos(fullLen, ePosVal); }
     break;
   }

   if (sPos < 1) { sPos = 1; fitted = true; }
   if (ePos > fullLen) { ePos = fullLen; fitted = true; }

   if (sPos > fullLen + 1) { sPos = fullLen + 1; fitted = true; }
   if (ePos < 0) { ePos = 0; fitted = true; }

   len = ePos - sPos + 1;
  }

 }



}



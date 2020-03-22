package org.xxdevplus.chain;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author GeTr
 */


 public class TextBuffer
 {
  //CopyrightÂ© 1996-2012 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties, You may use, modify, distribute this code under GNU public licence for NON-Commercial use as long as this notice remains unchanged.
  private static boolean selfTested  = false;  private static String ass(boolean expr) throws Exception { if (!expr) throw new Exception(ass(true) + " SelfTest Failure"); return "MappedBuffer"; } private static void selfTest() { selfTested = true; } private void init() { if (!selfTested) selfTest(); }

  private         char[]              buf;
  private         char[]              _Buf;
  private         char[]              Buf()                              throws Exception {if (_Buf != null) return _Buf; _Buf = new String(buf).toUpperCase().toCharArray() ; if (buf.length != _Buf.length) throw new Exception("Internal error in Reach: uppercase text differs in length"); return _Buf;}
  private         char                charAt    (int inx, boolean upper) throws Exception {return (upper) ? Buf()[inx] : buf[inx]; }
  public          char                get       (int                inx) throws Exception {return charAt(inx, false); }
  public          char                Get       (int                inx) throws Exception {return charAt(inx, true); }
  public          int                 Len()                                               {return buf.length; }
  private         long                sLblMax;
  private         long                eLblMax;

  public TextBuffer(String text) throws Exception {buf = text.toCharArray(); }
  public String partText(int sPos, int ePos) { return new String(java.util.Arrays.copyOfRange(buf, sPos - 1, ePos));}


 }


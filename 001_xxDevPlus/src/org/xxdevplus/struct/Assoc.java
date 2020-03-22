/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.xxdevplus.struct;

import org.xxdevplus.struct.NamedValue;
import org.xxdevplus.struct.KeyPile;
import org.xxdevplus.chain.Chain;
import org.xxdevplus.udf.Tag;
import org.xxdevplus.frmlng.Zone;


/**
 *
 * @author GeTr
 */



 public class Assoc extends KeyPile<String, NamedValue<Chain, Chain>>
 {
  //Copyright© 1996-2020 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties, You may use, modify, distribute this code under GNU public licence for NON-Commercial use as long as this notice remains unchanged.
  private static boolean selfTested  = false;  private static String ass(boolean expr) throws Exception { if (!expr) throw new Exception(ass(true) + " SelfTest Failure"); return "Assoc"; } private static void selfTest() { selfTested = true; } private void init() { if (!selfTested) selfTest(); }

  private String kvDelim;
  private String recDelim;

 @Override
  public NamedValue<Chain, Chain> g(int inx) throws Exception { KeyPile<String, NamedValue<Chain, Chain>> me = (KeyPile<String, NamedValue<Chain, Chain>>)this; return me.g(me.Keys().g(inx)); } 

  public String text() throws Exception { String ret = ""; for (String key : this.Keys()) ret += recDelim + key + kvDelim + g(key).Value().text(); if (ret.length() == 0) return ret; else return ret.substring(recDelim.length()); } 

  private void init(Tag tag, String KvDelim, String RecDelim) throws Exception
  {
   kvDelim = KvDelim;
   recDelim = RecDelim;
   if (tag.type.equals("table"))
   {
    for (Tag row : tag.Tags())
    {
     if (null != row.type) if (row.type.Equals("tr"))
     {
      int i = 0;
      Chain key = null;
      Chain val = null;
      for (Tag cell : row.Tags())
      {
       if (null != cell.type) if (cell.type.Equals("td"))
       {
        for (Tag cellTag : cell.Tags()) if (null != cellTag.type) if (cellTag.type.Equals("a")) if (cellTag.attr().hasKey("class")) if (cellTag.attr().g("class").Value().Equals("image")) { key = cellTag.attr().g("class").Value(); val = cellTag.attr().g("href").Value(); this.Add(key.text(), new NamedValue<Chain, Chain>(key, val)); }
        i++;
        switch (i)
        {
         case 1: key = cell.txt(); break;
         case 2: val = cell.txt(); this.Add(key.text(), new NamedValue<Chain, Chain>(key, val)); break;
        }
       }
      }
     }
    }
   }
  }

  private void init(Chain sourceText, String kvDelim, String recDelim, String stripQuotes, Zone blindZone) throws Exception
  {
   this.kvDelim  = kvDelim;
   this.recDelim = recDelim;
   Chain key = sourceText.upto(0);
   Chain val;
   while (sourceText.len() > 0)
   {
    //Console.WriteLine(" - " + sourceText.len);
    if (kvDelim.length() != 0)
    {
     key = sourceText.before(1, kvDelim).lTrim().rTrim();
     if (key.from(1, recDelim).len() > 0) key = key.before(1, recDelim).lTrim().rTrim(); else sourceText = sourceText.after(1, kvDelim).lTrim();
    }
    if (blindZone == null) val = sourceText.before(1, recDelim); else { Chain recDlm = (sourceText.less(blindZone.upon(sourceText)).at(1, recDelim)); val = sourceText.before(recDlm); } //Reach val = sourceText.before(1, RecDelim, blindSpots);
    sourceText = sourceText.after(val).from(1 + recDelim.length()).lTrim();
    val = val.lTrim().rTrim();
    if ((stripQuotes.length() > 0) && (val.startsWith(stripQuotes))) val = val.from(2).upto(-2);
    if (kvDelim.length() == 0) key = val;
    if (!this.hasKey(key.text())) this.Add(key.text(), new NamedValue<Chain, Chain>(key, val));
   }
  }

  public Assoc(Tag tag, String KvDelim, String RecDelim) throws Exception { init(tag, KvDelim, RecDelim); }

  public Assoc(String KvDelim, String RecDelim) { kvDelim = KvDelim; recDelim = RecDelim; }

  public Assoc(Chain sourceText, String KvDelim, String RecDelim, String stripQuotes) throws Exception { init(sourceText, KvDelim, RecDelim, stripQuotes, null); }

  //public Assoc(Chain sourceText, string KvDelim, string RecDelim, string stripQuotes, Dictionary<string, string[]> blindSpots) { init(sourceText, KvDelim, RecDelim, stripQuotes, blindSpots); }

  public Assoc(Chain sourceText, String KvDelim, String RecDelim, String stripQuotes, Zone blindZone) throws Exception { init(sourceText, KvDelim, RecDelim, stripQuotes, blindZone); }

 }





using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using ndBase;
using ndString;


namespace ndData

{
 public class Assoc: KeyPile<string, NamedValue<Reach, Reach>>
 {
  //Copyright© 1996-2012 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties, You may use, modify, distribute this code under GNU public licence for NON-Commercial use as long as this notice remains unchanged.
  private static bool selfTested  = false;  private static string ass(bool expr) { if (!expr) throw new Exception(ass(true) + " SelfTest Failure"); return "Assoc"; } private static void selfTest() { selfTested = true; } private void init() { if (!selfTested) selfTest(); }

  private string kvDelim;
  private string recDelim;

  public NamedValue<Reach, Reach> this[int inx] { get { KeyPile<string, NamedValue<Reach, Reach>> me = (KeyPile<string, NamedValue<Reach, Reach>>)this; return me[me.Keys[inx]]; } }

  public string text { get { string ret = ""; foreach (string key in this.Keys) ret += recDelim + key + kvDelim + this[key].Value.text; if (ret.Length == 0) return ret; else return ret.Substring(recDelim.Length); } }

  private void init(Tag tag, string KvDelim, string RecDelim)
  {
   kvDelim = KvDelim;
   recDelim = RecDelim;
   if (tag.type.Equals("table"))
   {
    foreach (Tag row in tag.Tags)
    {
     if (null != row.type) if (row.type.Equals("tr"))
     {
      long i = 0;
      Reach key = null;
      Reach val = null;
      foreach (Tag cell in row.Tags)
      {
       if (null != cell.type) if (cell.type.Equals("td"))
       {
        foreach (Tag cellTag in cell.Tags) if (null != cellTag.type) if (cellTag.type.Equals("a")) if (cellTag.attr.hasKey("class")) if (cellTag.attr["class"].Value.Equals("image")) { key = cellTag.attr["class"].Value; val = cellTag.attr["href"].Value; this.Add(key.text, new NamedValue<Reach, Reach>(key, val)); } i++;
        switch (i)
        {
         case 1: key = cell.txt; break;
         case 2: val = cell.txt; this.Add(key.text, new NamedValue<Reach, Reach>(key, val)); break;
        }
       }
      }
     }
    }
   }
  }

  private void init(Reach sourceText, string kvDelim, string recDelim, string stripQuotes, Zone blindZone)
  {
   this.kvDelim = kvDelim;
   this.recDelim = recDelim;
   Reach key = sourceText.upto(0);
   Reach val;
   while (sourceText.len > 0)
   {
    //Console.WriteLine(" - " + sourceText.len);
    if (kvDelim.Length != 0)
    {
     key = sourceText.before(1, kvDelim).lTrim().rTrim();
     if (key.from(1, recDelim).len > 0) key = key.before(1, recDelim).lTrim().rTrim(); else sourceText = sourceText.after(1, kvDelim).lTrim();
    }
    if (blindZone == null) val = sourceText.before(1, recDelim); else { Reach recDlm = (sourceText - blindZone.upon(sourceText)).at(1, recDelim); val = sourceText.before(recDlm); } //Reach val = sourceText.before(1, RecDelim, blindSpots);
    sourceText = sourceText.after(val).from(1 + recDelim.Length).lTrim();
    val = val.lTrim().rTrim();
    if ((stripQuotes.Length > 0) && (val.startsWith(stripQuotes))) val = val.from(2).upto(-2);
    if (kvDelim.Length == 0) key = val;
    if (!this.hasKey(key.text)) this.Add(key.text, new NamedValue<Reach, Reach>(key, val));
   }
  }

  public Assoc(Tag tag, string KvDelim, string RecDelim) { init(tag, KvDelim, RecDelim); }

  public Assoc(string KvDelim, string RecDelim) { kvDelim = KvDelim; recDelim = RecDelim; }

  public Assoc(Reach sourceText, string KvDelim, string RecDelim, string stripQuotes) { init(sourceText, KvDelim, RecDelim, stripQuotes, null); }

  //public Assoc(Reach sourceText, string KvDelim, string RecDelim, string stripQuotes, Dictionary<string, string[]> blindSpots) { init(sourceText, KvDelim, RecDelim, stripQuotes, blindSpots); }

  public Assoc(Reach sourceText, string KvDelim, string RecDelim, string stripQuotes, Zone blindZone) { init(sourceText, KvDelim, RecDelim, stripQuotes, blindZone); }

 }

}



using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using ndBase;
using ndString;

namespace ndData
{

 public class Fmla
 {
  //Copyright© 1996-2012 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties, You may use, modify, distribute this code under GNU public licence for NON-Commercial use as long as this notice remains unchanged.
  private static bool selfTested  = false; private static string ass(bool expr) { if (!expr) throw new Exception(ass(true) + " SelfTest Failure"); return "Fmla"; } private void init() { if (!selfTested) selfTest(); }

  private static void selfTest()
  {
   selfTested = true;
  }

  private Reach text = "";

  private static Pile<string> ops = new Pile<string>("ops", true, "\"", "'", "(", "[", "{", "+", "||", "ltrim(", "rtrim(");

  private Pile<Reach> parts = new Pile<Reach>();

  private void splitparts()
  {
   Pile<Reach> res = new Pile<Reach>();

   Reach s = parts[1].Trim();
   while (s.len > 0)
   {
    Reach first = s.At(1, false, ops.array());
    if (first.len == 0)
    {
     res.Push(s); s = "";
    }
    else
    {
     if (s.startsWith(first))
     {
      if (first.Equals("\"")) { res.Push("`dsF`"); res.Push(Zone.quotation.on(s)); res.Push(s.after(res[-1]).Trim()); s = ""; }
      if (first.Equals("'")) { res.Push("`dsF`"); res.Push(Zone.charray.on(s)); res.Push(s.after(res[-1]).Trim()); s = ""; }
      if (first.Equals("(")) { res.Push(Zone.bracelet.on(s)); res.Push(s.after(res[-1]).Trim()); s = ""; }
      if (first.Equals("[")) { res.Push(Zone.bracket.on(s)); res.Push(s.after(res[-1]).Trim()); s = ""; }
      if (first.Equals("{")) { res.Push(Zone.curlybrack.on(s)); res.Push(s.after(res[-1]).Trim()); s = ""; }

      if (first.Equals("+")) { res.Push("`cct`"); res.Push(Zone.curlybrack.on(s)); res.Push(s.after(res[-1]).Trim()); s = ""; }
      if (first.Equals("||")) { res.Push("`cct`"); res.Push(Zone.curlybrack.on(s)); res.Push(s.after(res[-1]).Trim()); s = ""; }

      if (first.Equals("ltrim(")) { res.Push("`ltr`"); res.Push(Zone.bracelet.on(s)); res.Push(s.after(res[-1]).Trim()); s = ""; }
      if (first.Equals("rtrim(")) { res.Push("`rtr`"); res.Push(Zone.bracelet.on(s)); res.Push(s.after(res[-1]).Trim()); s = ""; }

     }
     else
     {
      res.Push("`dF`");
      res.Push(s.before(first).Trim());
      res.Push(s.from(first).Trim());
      s = "";
     }
    }
   }
   parts = res;
  }

  public Fmla(string text)
  {
   while (text.IndexOf(" (") > 0) text = text.Replace(" (", "(");
   this.text = text.Trim();
   parts.Push(text);
   splitparts();
  }

  public void show()
  {
   //utl.say(parts.visEdit);
   utl.say(Zone.charray.on(text));
   utl.say(Zone.bracelet.on(text));
  }


 }



}














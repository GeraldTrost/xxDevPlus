



//** @author  GeTr Copyright© 1996-2020 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties
//** @license You may use, modify, distribute this code under GNU public dual licence free for NON-Commercial use as long as this notice remains unchanged.
//** @comment Providing Tags for any Object


using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace org_xxdevplus_struct
{
 public class TaggedObject<typ>
 {
  private static bool selfTested  = false; private static string ass(bool expr) { if (!expr) throw new Exception(ass(true) + " SelfTest Failure"); return "TaggedObject<>"; } private static void selfTest() { selfTested = true; } private void init() { if (!selfTested) selfTest(); }

             string[]               tags;
  private    typ                    obj;

  public                            TaggedObject       (typ obj,      params string[] tags)                { this.tags = tags; this.obj = obj; }
  public     string                 Tag                (int                            pos)                { return tags[pos - 1];   }  public void Tag (int pos, string value) { tags[pos - 1] = value; }
  public     typ                    Obj                (                                  )                { return obj;  }
  public     void                   Obj                (typ                            obj)                { this.obj = obj; }
  public     int                    Dim                (                                  )                { return tags.Length;  }
  public     TaggedObject<int>      select             (int                            num)                { return new TaggedObject<int>(num, tags); }

 }
}

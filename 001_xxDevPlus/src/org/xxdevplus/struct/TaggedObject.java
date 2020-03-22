


//** @author  GeTr Copyright© 1996-2020 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties
//** @license You may use, modify, distribute this code under GNU public dual licence free for NON-Commercial use as long as this notice remains unchanged.
//** @comment Providing Tags for any Object


package org.xxdevplus.struct;

 public class TaggedObject<typ>
 {
  private static boolean selfTested  = false;  private static String ass(boolean expr) throws Exception { if (!expr) throw new Exception(ass(true) + " SelfTest Failure"); return "TaggedObject<>"; } private static void selfTest() { selfTested = true; } private void init() { if (!selfTested) selfTest(); }

             String[]               tags;
  private    typ                    obj;

  public                            TaggedObject       (typ obj,            String... tags)                { this.tags = tags; this.obj = obj; }
  public     String                 Tag                (int                            pos)                { return tags[pos - 1];   }  public void Tag (int pos, String value) { tags[pos - 1] = value; }
  public     typ                    Obj                (                                  )                { return obj;  }
  public     void                   Obj                (typ                            obj)                { this.obj = obj; }
  public     int                    Dim                (                                  )                { return tags.length;  }
  public     TaggedObject<Integer>  select             (int                            num)                { return new TaggedObject<Integer>(num, tags); }

 }

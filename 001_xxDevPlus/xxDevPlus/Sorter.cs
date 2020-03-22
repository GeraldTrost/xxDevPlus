
//** @author  GeTr Copyright© 1996-2020 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties
//** @license You may use, modify, distribute this code under GNU public dual licence free for NON-Commercial use as long as this notice remains unchanged.
//** @comment Sorter for general Structures





using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace org_xxdevplus_struct
{
 public class Sorter
 {

  internal int num;
 
  public Sorter(int num) { this.num = num; }

  public int compare(Object o1, Object o2) 
  {

   if (o1 == o2) return 0;
   if (o1 == null) return 1;
   if (o2 == null) return -1;

   Type typ        = o1.GetType();

   if (typ == typeof(TaggedObject<Object>))
   {
    return ((string)((TaggedObject<Object>)o1).Tag(num)).CompareTo(((string)((TaggedObject<Object>)o1).Tag(num)));

    /*
    if (((TaggedObject)o1).Obj() instanceof Integer) { int num = (Integer)(((TaggedObject)o1).Obj()); return ((string) ((TaggedObject) o1).Tag(num)).compareTo(((string) ((TaggedObject) o2).Tag(num))); }
    else if (((TaggedObject)o2).Obj() instanceof Integer) { int num = (Integer)(((TaggedObject)o2).Obj()); return ((string) ((TaggedObject) o1).Tag(num)).compareTo(((string) ((TaggedObject) o2).Tag(num))); }
         else { int ret = 0; for (int num = 1; num <= ((TaggedObject)o1).Dim(); num++) { ret += ((string) ((TaggedObject) o1).Tag(num)).compareTo(((string) ((TaggedObject) o2).Tag(num))); if (ret != 0) return ret; } return ret; }
     */
   }

   if (typ == typeof(NamedValue<string, Object>)) return ((string)((NamedValue<string, Object>)o1).Name).CompareTo(((string)((NamedValue<string, Object>)o2).Name));

   if (typ == typeof(string))          return ((string)  o1).CompareTo(((string)   o2));
   if (typ == typeof(int))             return ((int)     o1).CompareTo(((int)      o2));
   if (typ == typeof(long))            return ((long)    o1).CompareTo(((long)     o2));
   if (typ == typeof(float))           return ((float)   o1).CompareTo(((float)    o2));
   if (typ == typeof(Double))          return ((Double)  o1).CompareTo(((Double)   o2));

   if (typ == typeof(Store<Object>)) return ((Store<Object>)o1).compareTo(((Store<Object>)o2));

   if (typ == typeof(Address))         return ((Address)o1).CompareTo(((Address)o2));
    
   throw new Exception("unable to compare Objects of this class!");

  }


 }
}

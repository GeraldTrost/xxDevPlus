

//** @author  GeTr Copyright© 1996-2020 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties
//** @license You may use, modify, distribute this code under GNU public dual licence free for NON-Commercial use as long as this notice remains unchanged.
//** @comment Sorter for general Structures


package org.xxdevplus.struct;

import org.xxdevplus.struct.TaggedObject;
import org.xxdevplus.struct.Address;

public class Sorter
{

 int num;

 public Sorter(int num) { this.num = num; }

 public int compare(Object o1, Object o2) throws Exception
 {

  if (o1 == o2) return 0;
  if (o1 == null) return 1;
  if (o2 == null) return -1;

  if (o1 instanceof TaggedObject)
  {
   return ((String) ((TaggedObject) o1).Tag(num)).compareTo(((String) ((TaggedObject) o2).Tag(num)));
   /*
   if (((TaggedObject)o1).Obj() instanceof Integer) { int num = (Integer)(((TaggedObject)o1).Obj()); return ((String) ((TaggedObject) o1).Tag(num)).compareTo(((String) ((TaggedObject) o2).Tag(num))); }
   else if (((TaggedObject)o2).Obj() instanceof Integer) { int num = (Integer)(((TaggedObject)o2).Obj()); return ((String) ((TaggedObject) o1).Tag(num)).compareTo(((String) ((TaggedObject) o2).Tag(num))); }
        else { int ret = 0; for (int num = 1; num <= ((TaggedObject)o1).Dim(); num++) { ret += ((String) ((TaggedObject) o1).Tag(num)).compareTo(((String) ((TaggedObject) o2).Tag(num))); if (ret != 0) return ret; } return ret; }
    */
  }

  if (o1 instanceof NamedValue)      return ((String)((NamedValue) o1).Name()).compareTo(((String)((NamedValue)  o2).Name()));

  if (o1 instanceof String)          return ((String)  o1).compareTo(((String)   o2));
  if (o1 instanceof Integer)         return ((Integer) o1).compareTo(((Integer)  o2));
  if (o1 instanceof Long)            return ((Long)    o1).compareTo(((Long)     o2));
  if (o1 instanceof Float)           return ((Float)   o1).compareTo(((Float)    o2));
  if (o1 instanceof Double)          return ((Double)  o1).compareTo(((Double)   o2));

  if (o1 instanceof Store)            return ((Store)    o1).compareTo(((Store)     o2));

  if (o1 instanceof Address)         return ((Address) o1).compareTo(((Address)  o2));

  throw new Exception("unable to compare Objects of this class!");

 }

}
























/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.xxdevplus.struct;

import org.xxdevplus.struct.TaggedObject;
import org.xxdevplus.struct.Address;
import java.lang.reflect.Array;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 *
 * @author GeTr
 */
public class KeyStore<typ> extends SmartStore<TaggedObject<typ>>
{
 //Copyright© 1996-2020 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties, You may use, modify, distribute this code under GNU public licence for NON-Commercial use as long as this notice remains unchanged.
 private static boolean selfTested  = false;  private static String ass(boolean expr) throws Exception { if (!expr) throw new Exception(ass(true) + " SelfTest Failure"); return "Jar"; }

 private             Sorter            srt                   = null;
 private             String[]          kAsc                  = null;
 private             String[]          kDsc                  = null;

 private static void selfTest() throws Exception
 {

  selfTested = true;
  Address a1 = new Address("postal", "St. Veiter Strasse", "im Hof", "50", "Klagenfurt", "Austria", "9020");
  Address a2 = new Address("postal", "Dr. Robert Koch Strasse", "", "10", "Klagenfurt", "Austria", "9020");
  Address a3 = new Address("postal", "Kanaltaler Strasse", "", "50", "Klagenfurt", "Austria", "9020");

  KeyStore<Address> j = new KeyStore<Address>(2, 0, 2, new Sorter(1));
  j.push(a2, "Ricki"); j.poke(a1, "Gerald"); j.push(a3, "Maria");
  ass(j.peek("Gerald") == 1); ass(j.peek("Maria") == 2); ass(j.peek("Ricki") == 3);
  ass(j.seek("Gerald") == 1); ass(j.seek("Maria") == 2); ass(j.seek("Ricki") == 3);
  ass(j.pop("Maria") == a3);
  ass(j.seek("Gerald") == 1); ass(j.seek("Ricki") == 2);
 }

 public              typ[]             newTypArray           (int                                     size) throws Exception          { return newTypArray(new Object[size]); }
 public              typ[]             newTypArray           (Object...                              items) throws Exception          { typ[] holder = (typ[]) (Array.newInstance(items.getClass().getComponentType(), items.length)); System.arraycopy(items, 0, holder, 0, items.length); return holder; }
 public              KeyStore<typ>[]   newKeyListOf_TypArray (Object...                              items)                           { Class<?> cls = this.getClass(); KeyStore<typ>[] ret = (KeyStore<typ>[])Array.newInstance(cls, items.length); for (int i = 0; i < items.length; i++) ret[i] = (KeyStore<typ>) items[i]; return ret;  }
 public              KeyStore<typ>[]   newKeyListOf_TypArray (int                                     size)                           { return newKeyListOf_TypArray(new Object[size]); }
 private             Class<?>          extractClassFromType  (Type                               superType) throws ClassCastException { if (superType instanceof Class<?>) return (Class<?>)superType; return (Class<?>)((ParameterizedType)superType).getRawType();  }

 private             void              init                  (Sorter                                          srt) throws Exception { if (!selfTested) selfTest(); this.srt = srt;}
 public                                KeyStore               (int sRsv, int size,  int eRsv,           Sorter srt) throws Exception { super(sRsv, size, eRsv, srt);      init(srt);}
 public                                KeyStore               (int size,                                Sorter srt) throws Exception { super(size, srt);                  init(srt);}
 public              typ               pop                   (String                                          key) throws Exception { kAsc = null; kDsc = null; TaggedObject<typ> ret = Punch(new TaggedObject(null, key)); return ret.Obj(); }
 private             void              push                  (typ obj,                                 String key) throws Exception { kAsc = null; kDsc = null; Push(new TaggedObject(obj, key)); }
 private             void              poke                  (typ obj,                                 String key) throws Exception { kAsc = null; kDsc = null; Poke(new TaggedObject(obj, key)); }
 private             int               peek                  (String                                          key) throws Exception { return Peek(new TaggedObject(null, key)); }
 private             int               seek                  (String                                          key) throws Exception { return Seek(new TaggedObject(null, key)); }
 public              boolean           hasKey                (String                                          Key) throws Exception { return (peek(Key) > 0); }

 public              String[]          kAsc                  (                                                   ) throws Exception
 {
  if (kAsc == null)
  {
   kAsc = new String[Len()];
   for (int Pos = 1; Pos <= kAsc.length; Pos++) kAsc[Pos - 1] = G(Pos).tags[srt.num - 1];
  }
  return kAsc;
 }

 public              String[]          kDsc                  (                                                   ) throws Exception
 {
  if (kDsc == null)
  {
   kDsc = new String[Len()];
   for (int Pos = 1; Pos <= kDsc.length; Pos++) kDsc[Pos - 1] = G(-Pos).tags[srt.num - 1];
  }
  return kDsc;
 }



}




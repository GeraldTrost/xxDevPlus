/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.xxdevplus.struct;

import org.xxdevplus.struct.KeyStore;
import org.xxdevplus.struct.TaggedObject;
import org.xxdevplus.struct.Address;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;

/**
 *
 * @author GeTr
 */



public class KeysStore<typ> implements Iterable<typ> //similar to array<typ> but Index starts at 1, not 0! Moreover constructor takes a list of arguments.
{
 //Copyright© 1996-2020 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties, You may use, modify, distribute this code under GNU public licence for NON-Commercial use as long as this notice remains unchanged.
 public  static long iic = 0;
 private static String ass       (boolean                        expr) throws Exception { if (!expr) throw new Exception(ass(true) + " SelfTest Failure");  return "Bale<>"; }
 private static void   ass       (boolean expr,            String msg) throws Exception { if (!expr) throw new Exception("Error in Pile.selfTest(): " + msg); }
 private static void   ass       (String e1   , String e2, String msg) throws Exception { ass(e1.equals(e2), msg); }
 private static void   ass       (double e1   , double e2, String msg) throws Exception { ass(e1 == e2, msg); }

 private        Sorter              srt    = null;
 private        int                 kdim   = 0; //Key Dimensions ...
 public         Object              _name  = "";
 private        KeyStore<typ>[]      vals   = null;

 private static void   tstSimple (                                   ) throws Exception 
 {

  Address a1 = new Address("postal", "St. Veiter Strasse", "im Hof", "50", "Klagenfurt", "Austria", "9020");
  Address a2 = new Address("postal", "Dr. Robert Koch Strasse", "", "10", "Klagenfurt", "Austria", "9020");
  Address a3 = new Address("postal", "Kanaltaler Strasse", "", "50", "Klagenfurt", "Austria", "9020");
  KeysStore<Address> tl = new KeysStore<Address>(3, new Sorter(1));
  tl.push(a1, "Gerald", "2000", "200.00"); ass(tl.g(1, 1) == a1); ass(tl.g(2, 1) == a1); ass(tl.g(3, 1) == a1);
  tl.push(a2, "Ricky",  "1970", "058.00"); tl.push(a3, "Mary",   "1980", "076.00");

  ass(tl.g(1, 1) == a1); ass(tl.g(1, 2) == a3); ass(tl.g(1, 3) == a2);
  ass(tl.g(2, 1) == a2); ass(tl.g(2, 2) == a3); ass(tl.g(2, 3) == a1);
  ass(tl.g(3, 1) == a2); ass(tl.g(3, 2) == a3); ass(tl.g(3, 3) == a1);

  tl.pop(null, "1980", null);
  tl.pop(null, "2000", null);
  tl.pop(null, "1970", null);

  ass(tl.vals[0].Len() == 0);
  ass(tl.vals[1].Len() == 0);
  ass(tl.vals[2].Len() == 0);

  /*
  b.push("Gary");
  b.poke("Mary");
  b.push("Tary");
  ass(b.g(1, "Gary").equals("Gary")); ass(b.g(1, "Mary").equals("Mary")); ass(b.g(1, "Tary").equals("Tary"));

  b.poke("Gary"); b.push("Mary"); b.poke("Tary");
  ass(b.peek("Gary") == 2); ass(b.peek("Mary") == 4); ass(b.peek("Tary") == 6); ass(b.seek("Gary") == 1); ass(b.seek("Mary") == 3); ass(b.seek("Tary") == 5); ass(b.peek("Bary") == -7); ass(b.peek("Wary") == -1); ass(b.seek("Bary") == -6); ass(b.seek("Wary") == 0);

  b.vals[0].Push(-1,"yyy"); b.vals[0].Poke(1,"xxx");
  ass(b.g(1).equals("xxx")); ass(b.g(-1).equals("yyy"));

  Address a1 = new Address("postal", "St. Veiter Strasse", "im Hof", "50", "Klagenfurt", "Austria", "9020");
  Address a2 = new Address("postal", "Dr. Robert Koch Strasse", "", "10", "Klagenfurt", "Austria", "9020");
  Address a3 = new Address("postal", "Kanaltaler Strasse", "", "50", "Klagenfurt", "Austria", "9020");

  KeysStore<Address> a;

  a = new KeysStore<Address>(1);
  a.push(a1); a.poke(a2); a.push(a3);

  a = new KeysStore<Address>();
  a.push(a1, "Gerald");
  a.poke(a2, "Maria");
  a.push(a3, "Ricki");
  */

 }

 public         void           pop        (String ...                      tags) throws Exception
 {
  if (vals[0].Len() == 0) return;
  for (int num = 1; num <= kdim; num++)
  {
   if (tags[num - 1] != null)
   {
    int Pos = vals[num - 1].Seek(new TaggedObject<typ>(null, tags));
    if (Pos > 0)
    {
     TaggedObject<typ> objToRemove = vals[num - 1].G(Pos);
     int[] PosToRemove = new int[kdim];
     PosToRemove[num - 1] = Pos;
     for (int i = 1; i <= kdim; i++)
     {
      if (i != num)
      {
       int P = vals[i - 1].Seek(objToRemove);
       if (P <= 0) return;
       while (P < Len() && (vals[i - 1].G(P + 1).Tag(i).equals(objToRemove.Tag(i))) && (vals[i - 1].G(P).Obj() != objToRemove.Obj())) P++;
       if (vals[i - 1].G(P).Obj() != objToRemove.Obj()) return;
       PosToRemove[i - 1] = P;
      }
     }
     for (int i = 1; i <= kdim; i++)
      vals[i - 1].Punch(PosToRemove[i - 1]);
     pop (objToRemove.tags);
    }
   }
  }
 }

 private String dbgString()
 {
  String ret = "";
  for (int num = 1; num <= kdim; num++) ret += "\r\n\r\n" + vals[num - 1].DbgString();
  return ret.substring(4);
 }





















































































































































































 private static void   selfTest  (                                   ) throws Exception { tstSimple(); }
 private        void   init      (int kdim,                Sorter srt) throws Exception
 {
  this.srt = srt;
  if (iic++ == 0) selfTest();
  this.kdim = kdim;
  vals  = new KeyStore<typ>(0, srt).newKeyListOf_TypArray(kdim);
  for (int num = 1; num <= kdim; num++) vals[num - 1] = new KeyStore<typ>(0, new Sorter(num));
 }

 public KeysStore(int kdim, Sorter srt) throws Exception { init(kdim, srt); };

 public         String         Name       (                                            )                  { return (String) _name; } public void Name(String value)  { _name = value; }
 public         int            Len        (                                            )                  { if (vals == null) return 0; return vals[0].Len(); }

 public         void           push       (typ Obj,                     String ... tags) throws Exception
 {
  TaggedObject<typ> to = new TaggedObject<typ>(Obj, tags);
  for (int num = 1; num <= kdim; num++)
   vals[num - 1].Push(to);
 }
 
 public         void           poke       (typ Obj,                     String ... tags) throws Exception
 {
  TaggedObject<typ> to = new TaggedObject<typ>(Obj, tags);
  for (int num = 1; num <= kdim; num++)
   vals[num - 1].Poke(to);
 }

 public         typ            g          (int num,                             int pos) throws Exception { return vals[num - 1].G(pos).Obj(); }

 public         typ            g          (int num,                          String key) throws Exception
 {
  int pos = vals[num - 1].Seek(new TaggedObject<typ> (null, key));
  if (pos < 0) throw new Exception("Key not found");
  return g(num, pos);
 }


 /*
 public         void           s          (typ obj,                             int pos) throws Exception { if (pos < 0) pos = vals.Len()  + pos + 1; if (pos == 0) _name = obj; else vals.S(pos, obj); }
 */


 /*
 public         boolean        Contains   (typ obj) throws Exception //fromerly: hasItem
 {
  return (peek(obj) > 0);
 }

 public         int            peek       (typ                                      obj) throws Exception { return vals.Peek(obj); }
 public         int            seek       (typ                                      obj) throws Exception { return vals.Seek(obj); }


 public         void           poke       (typ                                      obj) throws Exception { vals.Poke(obj); }

 public         void           poke       (typ obj,                       String... key) throws Exception
 {
  vals.Poke(obj);
 }


 public         void           push       (typ                                      obj) throws Exception { vals.Push(obj); }

 public         void           push       (typ obj,                       String... key) throws Exception
 {
  vals.Push(obj);
 }
  */

 public   Iterator<typ>  iterator() { return null; }

}




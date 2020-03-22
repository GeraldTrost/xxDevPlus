

//** @author  GeTr Copyright© 1996-2020 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties
//** @license You may use, modify, distribute this code under GNU public dual licence free for NON-Commercial use as long as this notice remains unchanged.
//** @comment 1 based Enhanced ArrayList also supporting negative backward counting 


package org.xxdevplus.struct;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.xxdevplus.utl.utl;
import org.xxdevplus.chain.Chain;
import org.xxdevplus.data.Db;
import org.xxdevplus.frmlng.Pick;

 public class Pile<typ> implements Iterable<typ>
 {
  static int _ic;String ass(boolean xp,String...msg)throws Exception{if(xp)return"Pile<typ>"; throw new Exception(ass(true)+" slfTst:"+utl.str(msg));}private void slfTst()throws Exception{tstSimple();}protected void init()throws Exception{if(_ic++==0)slfTst();}
  private void ass(String  e1   , String e2, String...msg) throws Exception { ass(e1.equals(e2), msg); }
  private void ass(double  e1   , double e2, String...msg) throws Exception { ass(e1 == e2     , msg); }
  
  void tstSimple() throws Exception
  {
   long i = 0;
   Pile<Long> test = new Pile<Long>();
   test.Add(1L);
   test.Add(2L);
   test.Add(3L);
   test.Del(2);
   ass(test.Len() , 2, "tstSimple.0");
   ass(test.g(1)  , 1, "tstSimple.1");
   ass(test.g(2)  , 3, "tstSimple.2");
   test.Del(1);
   ass(test.g(1)  , 3, "tstSimple.3");
   ass(test.Len() , 1, "tstSimple.4");
   
   Pile<String> ps = new Pile<String>(0, "a", "b", "c");
   Pile<Pile<String>> cmb = ps.combinations(2, false);
   ass(cmb.g(1).csv(", ","", ""), "a, b");
   ass(cmb.g(2).csv(", ","", ""), "a, c");
   ass(cmb.g(3).csv(", ","''", "[]"), "['b', 'c']");
   
   Pile<String> stackA, stackB;
   stackA = new Pile<String>("", 0, new String[]{"1", "2", "3"}); stackA.Push ("4");       ass(stackA.csv(",", "", "").equals("1,2,3,4"   ));
                                                                  stackA.Pop  (   );       ass(stackA.csv(",", "", "").equals("1,2,3"     ));
   stackA = new Pile<String>("", 0, new String[]{"1", "2", "3"}); stackA.Poke ("0");       ass(stackA.csv(",", "", "").equals("0,1,2,3"   ));
                                                                  stackA.Pull (   );       ass(stackA.csv(",", "", "").equals("1,2,3"     ));
   stackA = new Pile<String>("", 0, new String[]{"1", "2", "3"}); stackB = stackA.Clone(); stackA.Push(stackB);
   ass(stackA.csv(",", "", "").equals("1,2,3,1,2,3"));
   stackB = stackA.Pop(2);   ass(stackB.csv(",", "", "").equals("2,3"));
   stackB = stackA.Pop(-2);  ass(stackB.csv(",", "", "").equals("1,3"));
   
   stackA = new Pile<String>("", 0, new String[]{"1", "2", "3"}); stackB = stackA.Clone(); stackA.Poke(stackB);
   ass(stackA.csv(",", "", "").equals("1,2,3,1,2,3"));
   stackB = stackA.Pull(2);  ass(stackB.csv(",", "", "").equals("1,2"));
   stackB = stackA.Pull(-2); ass(stackB.csv(",", "", "").equals("1,3"));
   
   stackA = new Pile<String>("", 0, new String[]{"1", "2", "3"}); stackB = stackA.Clone(); for (int j = 1; j <= 2; j++) stackB.RollUp(1);
   ass(stackA.RollUp(2).csv(",", "", "").equals("1,2")); 
   ass(stackA.csv(",", "", "").equals(stackB.csv(",", "", "")));
   stackA = new Pile<String>("", 0, new String[]{"1", "2", "3"}); stackB = stackA.Clone(); for (int j = 1; j <= 2; j++) stackB.RollDn(1);
   ass(stackA.RollDn(2).csv(",", "", "").equals("2,3")); 
   ass(stackA.csv(",", "", "").equals(stackB.csv(",", "", "")));
   stackA = new Pile<String>("", 0, new String[]{"1", "2", "3"}); stackB = stackA.Clone();
   ass(stackA.RollUp(-2).csv(",", "", "").equals("2,1")); 
   ass(stackA.csv(",", "", "").equals("3,2,1"));
   stackA = new Pile<String>("", 0, new String[]{"1", "2", "3"}); stackB = stackA.Clone();
   ass(stackA.RollDn(-2).csv(",", "", "").equals("3,2")); 
   ass(stackA.csv(",", "", "").equals("3,2,1"));
  }

  public void visEdit(Object parentForm, Object... more) throws Exception {/*TODO*/}

  private typ testVal;
    
  public   typ[]          newTypArray          (int size                                    ) throws Exception          { return newTypArray(new Object[size]); }       
  public   typ[]          newTypArray          (Object... items                             ) throws Exception          { typ[] holder = (typ[]) (Array.newInstance(items.getClass().getComponentType(), items.length));  System.arraycopy(items, 0, holder, 0, items.length); return holder; }
  public   Pile<typ>[]    newPileOf_TypArray   (Object...                              items)                           { Class<?> cls = this.getClass(); Pile<typ>[] ret = (Pile<typ>[])Array.newInstance(cls, items.length); for (int i = 0; i < items.length; i++) ret[i] = (Pile<typ>) items[i]; return ret; }
  public   Pile<typ>[]    newPileOf_TypArray   (int                                     size)                           { return newPileOf_TypArray(new Object[size]); }
  
//private  Class<?>       extractClassFromType (Type                               superType) throws ClassCastException { if (superType instanceof Class<?>) return (Class<?>)superType; return (Class<?>)((ParameterizedType)superType).getRawType();  }

  //public Class<typ> getInnerClass() throws ClassCastException 
  //{ 
  // Type superType;
  // Class<?> superClass = this.getClass(); // initial value Type superType; 
  // do 
  // { 
  //  superType = superClass.getGenericSuperclass(); 
  //  superClass = extractClassFromType(superType); 
  // } while (! (superClass.equals(Pile.class))); 
  // Type actualArg = ((ParameterizedType)superType).getActualTypeArguments()[0]; 
  // return (Class<typ>)extractClassFromType(actualArg); 
  //} 

  //public Class<?> innerClass()
  //{
  // Class<?> clsPileOfTyp = this.getClass();
  // Type typPileOfTyp = clsPileOfTyp.getGenericSuperclass();
  // Type typRawPileOfTyp = ((ParameterizedType)typPileOfTyp).getRawType();
  //
  // Class<?> result = null;
  // do
  // {
  //  if (typRawPileOfTyp instanceof ParameterizedType)
  //  {
  //   ParameterizedType pt =(ParameterizedType) typRawPileOfTyp;
  //   Type[] fieldArgTypes = pt.getActualTypeArguments();
  //   return (Class<?>) fieldArgTypes[0];
  //  }
  //  typRawPileOfTyp = extractClassFromType(typRawPileOfTyp).getGenericSuperclass();
  // } while (typRawPileOfTyp != null);
  // return result;
  //}

  public   Object         _name  = "";
  private  ArrayList<typ> val = new ArrayList<typ>(4);
  
  public boolean          hasNulls   (              )      throws Exception   { for (typ el : val) if (el == null) return true; return false;}

  public   Pile                      (                                      ) { try { /*MethWatch mw = new MethWatch("!Pile");*/ init(); _name =   ""; this.val = new ArrayList<typ>(Arrays.asList(newTypArray(0      )));           /*mw._void();*/} catch (Exception exp) {}}
  public   Pile                      (int                              count) { try { /*MethWatch mw = new MethWatch("!Pile");*/ init(); _name =   ""; this.val = new ArrayList<typ>(Arrays.asList(newTypArray(count  )));           /*mw._void();*/} catch (Exception exp) {}}
  public   Pile                      (int iniCnt,  typ...                val) { try { /*MethWatch mw = new MethWatch("!Pile");*/ init(); _name =   ""; this.val = new ArrayList<typ>(Arrays.asList(newTypArray(iniCnt ))); Add(val); /*mw._void();*/} catch (Exception exp) {}}
  public   Pile                      (int iniCnt,  List<typ>             val) { try { /*MethWatch mw = new MethWatch("!Pile");*/ init(); _name =   ""; this.val = new ArrayList<typ>(Arrays.asList(newTypArray(iniCnt ))); Add(val); /*mw._void();*/} catch (Exception exp) {}}
  public   Pile                      (String name, int iniCnt, typ...    val) { try { /*MethWatch mw = new MethWatch("!Pile");*/ init(); _name = name; this.val = new ArrayList<typ>(Arrays.asList(newTypArray(iniCnt ))); Add(val); /*mw._void();*/} catch (Exception exp) {}}
  public   Pile                      (String name, int iniCnt, List<typ> val) { try { /*MethWatch mw = new MethWatch("!Pile");*/ init(); _name = name; this.val = new ArrayList<typ>(Arrays.asList(newTypArray(iniCnt ))); Add(val); /*mw._void();*/} catch (Exception exp) {}}

  public   Pile<Pile<typ>> combinations(int k, boolean repeat) throws Exception
  {
   Pile<Pile<typ>> ret = new Pile<Pile<typ>>();
   Pile<Long> selector = new Pile<Long>();
   for (long i = 1; i <= k; i++) selector.Push(i);
   while (selector.Len() > 0)
   {
    Pile<typ> sample = new Pile<typ>();
    for (long i : selector) sample.Push(g((int) i));
    ret.Push(sample);
    int i = -1; 
    while ((selector.Len() > 0) && (selector.g(-1) >= Len() + i + 1)) {selector.Pop(); i--;}
    if (selector.Len() > 0)
    {
     selector.Push(selector.Pop() + 1); 
     while (selector.Len() < k) selector.Push(selector.g(-1) + 1); 
    }
   } 
   return ret;
  }
  
  public   Pile                      (Chain txt,    boolean csvQuoteOnDemand)
  {
   try 
   { 
    if (!csvQuoteOnDemand) throw new Exception("NIY Pile<Chain> constructor");
    /*MethWatch mw = new MethWatch("!Pile");*/ 
    init(); 
    _name =   ""; 
    this.val = new ArrayList<typ>(Arrays.asList(newTypArray(0)));  
    Pick zone = Pick.sglDQots.esc("\\").grabOne();
    boolean stringFormat = txt.startsWith("\"");
    Chain found = Chain.Empty;
    while (txt.len() > 0)
    {
     if (stringFormat) found = zone.upon(txt); else found = txt.before(1, ",");
     txt = txt.after(found).after(1, ",").Trim();
     if (found.startsWith("\"")) found = new Chain(found.from(2).upto(-2).text().replace("\\\"", "\""));

     try 
     {
      Push((typ) found);
     }
     catch (Exception ex)
     {
      try 
      {
       Push((typ) found.text());
      }
      catch (Exception ex1)
      {
       try 
       {
        Push((typ) (Long) Long.parseLong(found.text()));
       }
       catch (Exception ex2)
       {
        try 
        {
         Push((typ) (Integer) Integer.parseInt(found.text()));
        }
        catch (Exception ex3)
        {
          throw new Exception("incompatible Pile-Type");
        }
       }
      }
     }

     /*     
     if      (testVal.getClass().getName().equals(Chain.class.getName()))    
      Push((typ)           found);
     else if (testVal.getClass().getName().equals(String.class.getName()))   
      Push((typ)           found.text());
     else if (testVal.getClass().getName().equals(Long.class.getName()))     
      Push((typ) (Long)    Long.parseLong(found.text()));
     else if (testVal.getClass().getName().equals(Integer.class.getName()))  
      Push((typ) (Integer) Integer.parseInt(found.text()));
     else 
      Push((typ) found);
     */

    }
    /*mw._void();*/
   } 
   catch (Exception exp) 
   {
    exp = exp;
   }
  }

  
  public   String         Name       (                                      ) { return (String) _name; } public void Name(String value)  { _name = value;                                                                     }
  public   int            Len        (                                      ) { if (val == null) return 0; return val.size();                                                                                                 }
  
  public   typ            g          (int                                pos) 
  { 
   if (pos < 0) pos = val.size() + pos + 1; 
   return (pos == 0) ? null : (typ)(val.get(pos - 1));                                                
  }

  public   void           s          (typ value,                     int pos) { if (pos < 0) pos = val.size()  + pos + 1; if (pos == 0) _name = value; else val.set(pos - 1, value);                                             }

  public   boolean        Contains   (boolean all,              typ... value) //fromerly: hasItem
  {
   if (all)
   {
    for (typ v  : value) if (!val.contains(v)) return false;
    return true;
   }
   else
   {
    for (typ v : value) if (val.contains(v)) return true;
    return false;
   }
  } 

  public   boolean        Contains   (boolean all,       Pile<typ>... values) 
  {
   if (all)
   {
    for (Pile<typ> vp : values)
    {
     boolean matches = true; for (typ v: vp) if (!val.contains(v)) {matches = false; break;}
     if (!matches) return false;
    }    
    return true;
   }
   else
   {
    for (Pile<typ> vp : values)
    {
     boolean matches = true; for (typ v: vp) if (!val.contains(v)) {matches = false; break;}
     if (matches) return true;
    }    
    return false;
   }
  } 

  public   boolean        Contains   (boolean all,    Pile<Pile<typ>> values) throws Exception 
  { 
   if (all)
   {
    for (Pile<typ> vp : values)
    {
     boolean matches = true; for (typ v: vp) if (!val.contains(v)) {matches = false; break;}
     if (!matches) return false;
    }    
    return true;
   }
   else
   {
    for (Pile<typ> vp : values)
    {
     boolean matches = true; for (typ v: vp) if (!val.contains(v)) {matches = false; break;}
     if (matches) return true;
    }    
    return false;
   }
  }

  public   typ            first      (                                      ) throws Exception  { return g( 1); }
  public   typ            last       (                                      ) throws Exception  { return g(-1); }
  
  public   Pile<typ>      before     (int                                inx) throws Exception 
  { 
   if (inx < 0) inx = val.size() + inx + 1; 
   return upto(inx - 1);
  }
  
  public   Pile<typ>      upto       (int                                inx) throws Exception 
  { 
   if (inx < 0) inx = val.size() + inx + 1; 
   if (inx < 1) return new Pile<typ>(); 
   inx = Math.max(1, Math.min(inx, val.size())); 
   Pile<typ> ret = new Pile<typ>(); ret._name = _name; 
   for (int i = 1; i <= inx; i++) ret.Add(this.g(i)); 
   return ret;                                                                                                                 
  }
  
  public   Pile<typ>      at         (int                                inx) throws Exception 
  { 
   if (inx < 0) inx = val.size() + inx + 1; 
   if ((inx > val.size()) || (inx == 0)) return new Pile<typ>(); 
   inx = Math.max(1, Math.min(inx, val.size())); 
   Pile<typ> ret = new Pile<typ>(); ret._name = _name; 
   ret.Add(this.g(inx)); 
   return ret;                                                                                                                                                    
  }
  
  public   Pile<typ>      slice      (int from,                     int upto) throws Exception 
  { 
   if (from < 0) from = val.size() + from + 1; from = Math.min(from, val.size()); 
   if (upto < 0) upto = val.size() + upto + 1; upto = Math.min(upto, val.size()); 
   Pile<typ> ret = new Pile<typ>(); ret._name = _name; 
   for (int i = 1; i <= 1 + upto - from; i++) ret.Add(this.g(i + from - 1)); 
   return ret;        
  }
  
  public   Pile<typ>      from       (int                                inx) throws Exception 
  { 
   if (inx < 0) inx = val.size() + inx + 1; 
   if (inx > val.size()) return new Pile<typ>(); 
   inx = Math.max(1, Math.min(inx, val.size())); 
   Pile<typ> ret = new Pile<typ>(); ret._name = _name; 
   for (int i = 1; i <= 1 + val.size() - inx; i++) ret.Add(this.g(i + inx - 1)); 
   return ret;                                
  }
  
  public   Pile<typ>      after      (int                                inx) throws Exception 
  { 
   if (inx < 0) inx = val.size() + inx + 1; 
   return from(inx + 1);
  }

  protected boolean dirty = false;

  public   Pile<typ>      Del        (int                                      inx) throws Exception { dirty = true; if (inx < 0) inx = val.size() + inx + 1; val.remove(inx - 1);  return this                  ;}

  public   typ            Remove     (int                                      inx) throws Exception { dirty = true; if (inx < 0) inx = val.size() + inx + 1;                       return val.remove(inx - 1)   ;}

  public   Pile<typ>      InsBefore  (int inx,                      Pile<typ> more) throws Exception { dirty = true; if (inx < 0) inx = val.size() + inx + 1; for (int i = more.Len();  i >= 1; i--) val.add(inx - 1, more.g(i))     ; return this  ;}                     
  public   Pile<typ>      InsBefore  (int inx,                         typ... more) throws Exception { dirty = true; if (inx < 0) inx = val.size() + inx + 1; for (int i = more.length; i >= 1; i--) val.add(inx - 1, more[i-1])     ; return this  ;}
  public   Pile<typ>      InsBefore  (int inx,                      List<typ> more) throws Exception { dirty = true; if (inx < 0) inx = val.size() + inx + 1; for (int i = more.size(); i >= 1; i--) val.add(inx - 1, more.get(i-1)) ; return this  ;}

  public   Pile<typ>      InsAfter   (int inx,                      Pile<typ> more) throws Exception { dirty = true; if (inx < 0) inx = val.size() + inx + 1; for (int i = more.Len();  i >= 1; i--) val.add(inx    , more.g(i))     ; return this  ;}              
  public   Pile<typ>      InsAfter   (int inx,                         typ... more) throws Exception { dirty = true; if (inx < 0) inx = val.size() + inx + 1; for (int i = more.length; i >= 1; i--) val.add(inx    , more[i-1])     ; return this  ;}
  public   Pile<typ>      InsAfter   (int inx,                      List<typ> more) throws Exception { dirty = true; if (inx < 0) inx = val.size() + inx + 1; for (int i = more.size(); i >= 1; i--) val.add(inx    , more.get(i-1)) ; return this  ;}

  public   Pile<typ>      Add        (Pile<typ>                               more) throws Exception { dirty = true; for (int i = 1; i <= more.Len();  i++) val.add(more.g(i));     return this;             }
  public   Pile<typ>      Add        (typ...                                  more) throws Exception { dirty = true; for (int i = 1; i <= more.length; i++) val.add(more[i-1]);     return this;             }
  public   Pile<typ>      Add        (List<typ>                               more) throws Exception { dirty = true; for (int i = 1; i <= more.size(); i++) val.add(more.get(i-1)); return this;             }

  // Pile also resembles a Stack !
  
  public   typ            Push       (typ                                     next) throws Exception { Add(next); return next;                                                                 }
  public   Pile<typ>      Push       (Pile<typ>                               more) throws Exception { Add(more); return more;                                                                 }
 
  public   typ            Pop        (                                            ) throws Exception { if (Len() == 0) throw new Exception("0-Len Stack cannot Pop"); typ ret = g(-1); Del(-1); return ret;                                                   }
  public   Pile<typ>      Pop        (int count                                   ) throws Exception 
  {
   if (Len() < Math.abs(count)) throw new Exception("Stack cannot Pop more than Len");
   Pile<typ> ret = new Pile<typ>();
   for (int i = 1; i <= Math.abs(count); i++) if (count >= 0) ret.Poke(Pop()); else ret.Push(Pop());
   return ret;                                                   
  }

  public   typ            Poke       (typ                                     next) throws Exception { InsBefore(1, next); return next;                                                        }
  public   Pile<typ>      Poke       (Pile<typ>                               more) throws Exception { InsBefore(1, more); return more;                                                        }

  public   typ            Pull       (                                            ) throws Exception { if (Len() == 0) throw new Exception("0-Len Stack cannot Pull"); typ ret = g(1);  Del(1); return ret;                                                    }

  public   Pile<typ>      Pull       (int count                                   ) throws Exception 
  { 
   if (Len() < Math.abs(count)) throw new Exception("Stack cannot Pull more than Len");
   Pile<typ> ret = new Pile<typ>();
   for (int i = 1; i <= Math.abs(count); i++) if (count >= 0) ret.Push(Pull()); else ret.Poke(Pull());
   return ret;                                                   
  }
  
  public   Pile<typ>      RollUp     (int count                                   ) throws Exception { return Push(Pull(count)) ;}        
  public   Pile<typ>      RollDn     (int count                                   ) throws Exception { return Poke(Pop (count)) ;}        
  
  public   void           Clear      (                                            ) throws Exception { dirty = true; val = new ArrayList<typ>(0);                                                                                                                                                                                                                                                                                                    }
  

  public void Sort() throws Exception {throw new Exception("NIY:case insentive sorting of Pile");}
  public void sort() throws Exception {Collections.sort((List)val); }
  
  public   Pile<typ>      Clone      (                                            ) throws Exception 
  { 
   Pile<typ> ret = new Pile<typ>(); 
   ret._name = _name; 
   for (int i = 1; i <= Len(); i++) ret.Add(g(i)); 
   return ret;                                                                                                                                                                                                    
  }

  public  typ[]          array      (                                            ) throws Exception                  
  { 
   return newTypArray(val.toArray()); // return (typ[]) val.toArray(); 
  }

  public  long[]          longArray      (                                            ) throws Exception                  
  { 
   long[] ret = new long[val.size()];
   for (int i = 1; i <= val.size(); i++) ret[i - 1] = ((Long)val.get(i - 1));
   return ret;
  }

  public  String[]       strArray   (                                            ) throws Exception                  
  { 
   String[] ret = new String[Len()];
   for(int i = 1; i <= Len(); i++) 
   {
    Object item = g(i);
    ret[i - 1] = item instanceof Chain ? ((Chain)g(i)).text() : item instanceof String ? (String)g(i) : item.toString();
   }
   return ret;   
  }
  
  public  String         csv        (String delim, String quotes, String brackets, Db db, int len) throws Exception                  
  { 
   String StartQuote   = quotes.substring(0, quotes.length() / 2);
   String EndQuote     = quotes.substring(quotes.length() / 2);
   String StartBracket = brackets.substring(0, brackets.length() / 2);
   String EndBracket   = brackets.substring(brackets.length() / 2);
   String ret = StartBracket;
   for (typ s: this)
   {
    String item = (s instanceof Integer) ? "" + (Integer)s : (s instanceof Long) ? "" + (Long)s : (s instanceof Chain) ? ((Chain)s).text() : (String)s; 
    if (db == null)
     ret += StartQuote + item + EndQuote + delim;
    else
     ret += StartQuote + db.toDB(item, len) + EndQuote + delim;
   }
   return (ret.length() >  StartBracket.length()) ? ret.substring(0, ret.length() - delim.length()) + EndBracket : ret + EndBracket;
   //String ret = Arrays.toString(array()).replace(", ",  + delim + );
   //return brackets.substring(0, brackets.length() / 2) + quotes.substring(0, quotes.length() / 2) + ret.substring(1, ret.length()-1) + quotes.substring(quotes.length() / 2) + brackets.substring(brackets.length() / 2); 
  }

  public  String         csv        (String delim, String quotes, String brackets) throws Exception                  
  {
   return csv (delim, quotes, brackets, null, 0);
  }
  
  public String toString()
  {
   try 
   {
    return csv(", ", "\"\"", "[]");
   } 
   catch (Exception ex) {ex = ex; };
   return "";
  }
  
  public  Iterator<typ>  iterator   (                                            )                  { return val.iterator(); }
  
         
           
  //public IEnumerator<type> GetEnumerator() { return new PileEnumerator(this); }
  //IEnumerator IEnumerable.GetEnumerator() { return GetEnumerator(); }
  //public class PileEnumerator: IEnumerator<type>
  //{
  // private v<type> coll;
  // private type current;
  // private longlong index;
  // public vEnumerator(Pile<type> coll) { Monitor.Enter(coll.val.SyncRoot); this.index = -1; this.coll = coll; }
  // public type Current { get { return current; } }
  // object IEnumerator.Current { get { return Current; } }
  // public bool MoveNext() { if (++index >= coll.val.Length) { return false; } else { current = coll.val[index]; return true; } }
  // public void Reset() { current = default(type); index = 0; }
  // public void Dispose() { try { current = default(type); index = coll.val.Length; } finally { Monitor.Exit(coll.val.SyncRoot); } }
  //}


 }







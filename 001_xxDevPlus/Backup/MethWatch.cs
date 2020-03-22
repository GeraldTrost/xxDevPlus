using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using ndBase;


namespace ndString
{
 public class MethWatch
 {
  //Copyright© 1996-2012 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties, You may use, modify, distribute this code under GNU public licence for NON-Commercial use as long as this notice remains unchanged.
  private static bool selfTested  = false;  private static string ass(bool expr) { if (!expr) throw new Exception(ass(true) + " SelfTest Failure"); return "MethWatch"; } private static void selfTest() { selfTested = true; } private void init() { if (!selfTested) selfTest(); }

  string methName;
  DateTime sTime;

  public MethWatch(string methName)
  {
   ctx cx = new ctx();
   this.methName = methName;
   sTime = utl.Now();
   if (!cx.Stat.hasKey(methName)) cx.Stat.Add(methName, new NamedValue<long, TimeSpan>(0, new TimeSpan(0)));
   //Console.WriteLine("* " + methName);
  }

  private void stop()
  {
   ctx cx = new ctx();
   cx.Stat[methName] = new NamedValue<long, TimeSpan>(cx.Stat[methName].Name + 1, cx.Stat[methName].Value + (utl.Now() - sTime));
   //Console.WriteLine("~ " + methName + " " + (utl.Now() - sTime));
  }

  public void         _void()                { stop();           }
  public object       _obj(object v)         { stop(); return v; }
  public int          _int(int v)            { stop(); return v; }
  public long         _long(long v)          { stop(); return v; }
  public int[]        _intA(int[] v)         { stop(); return v; }
  public long[]       _longA(long[] v)       { stop(); return v; }
  public char         _char(char  v)         { stop(); return v; }
  public string       _string(string v)      { stop(); return v; }
  public Reach        _Reach(Reach v)        { stop(); return v; }
  public Rch          _Rch(Rch v)            { stop(); return v; }
  public Pile<Rch>    _Pile_Rch(Pile<Rch> v) { stop(); return v; }
  

  /*
  ~MethWatch()
  {
   Console.WriteLine("~ " + methName);
  }
  */

 }
}

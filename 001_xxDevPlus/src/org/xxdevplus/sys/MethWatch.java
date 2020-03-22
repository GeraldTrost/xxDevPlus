


//** @author  GeTr Copyright© 1996-2020 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties
//** @license You may use, modify, distribute this code under GNU public dual licence free for NON-Commercial use as long as this notice remains unchanged.
//** @comment Utility Class for profiling Methods


package org.xxdevplus.sys;

import org.xxdevplus.utl.ctx;
import java.sql.Timestamp;
import org.xxdevplus.struct.NamedValue;
import org.xxdevplus.struct.Pile;
import org.xxdevplus.utl.utl;


 public class MethWatch
 {
  private static boolean selfTested  = false;  private static String ass(boolean expr) throws Exception { if (!expr) throw new Exception(ass(true) + " SelfTest Failure"); return "MethWatch"; } private static void selfTest() { selfTested = true; } private void init() { if (!selfTested) selfTest(); }

  String methName;
  Long   sTime;

  public MethWatch(String methName) throws Exception
  {
   ctx cx = new ctx();
   if (cx.Stat() == null) return;
    this.methName = methName;
   sTime =  utl.Now().getTime();
   if (!cx.Stat().hasKey(methName)) cx.Stat().Add(methName, new NamedValue<Long, Long>(0L, 0L));
   //Console.WriteLine("* " + methName);
  }

  private void stop() throws Exception  { ctx cx = new ctx(); if (cx.Stat() == null) return; cx.Stat().s( new NamedValue<Long, Long> (cx.Stat().g(methName).Name() + 1, cx.Stat().g(methName).Value() + (utl.Now().getTime() - sTime)) , methName); }

  public void          _void          (               ) throws Exception  { stop();           }
  public Object        _obj           (Object        v) throws Exception  { stop(); return v; }
  public int           _int           (int           v) throws Exception  { stop(); return v; }
  public long          _long          (long          v) throws Exception  { stop(); return v; }
  public int[]         _intA          (int[]         v) throws Exception  { stop(); return v; }
  public long[]        _longA         (long[]        v) throws Exception  { stop(); return v; }
  public char          _char          (char          v) throws Exception  { stop(); return v; }
  public String        _string        (String        v) throws Exception  { stop(); return v; }
  public String[]      _stringA       (String[]      v) throws Exception  { stop(); return v; }
  public Pile<String>  _Pile_string   (Pile<String>  v) throws Exception  { stop(); return v; }
  public Object        _object        (Object        v) throws Exception  { stop(); return v; }
  public Object[]      _objectA       (Object[]      v) throws Exception  { stop(); return v; }
  public Pile<Object>  _Pile_object   (Pile<Object>  v) throws Exception  { stop(); return v; }

  //public Reach        _Reach(Reach v)        { stop(); return v; }
  //public Rch          _Rch(Rch v)            { stop(); return v; }
  //public Pack<Rch>    _Pile_Rch(Pack<Rch> v) { stop(); return v; }
  

  /*
  ~MethWatch()
  {
   Console.WriteLine("~ " + methName);
  }
  */

 }

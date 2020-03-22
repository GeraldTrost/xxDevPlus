


//** @author  GeTr Copyright© 1996-2020 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties
//** @license You may use, modify, distribute this code under GNU public dual licence free for NON-Commercial use as long as this notice remains unchanged.
//** @comment Application Context

using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.IO;
using Microsoft.Win32;
using System.Reflection;
using System.Windows.Forms;
using System.Runtime.InteropServices;

using org_xxdevplus_utl;
using org_xxdevplus_struct;
using org_xxdevplus_chain;


namespace org_xxdevplus_utl
{

 public class ctx //Options ... Here may your Application store some global Options ...
 {
  private static bool selfTested  = false; private static string ass(bool expr) { if (!expr) throw new Exception(ass(true) + " SelfTest Failure"); return "ctx"; } private static void selfTest() { selfTested = true; } private static void init() { if (!selfTested) selfTest(); }

  [DllImport("user32.dll")]
  private static extern short GetAsyncKeyState(long vKey);

  public  static   bool       useGuiDialogs                                                             = false;

  private static   FileInfo   logPath                                                                   = null;
  public  static   string     gLogPath (object                                                     obj) { return ""; } //return (logPath == null) ? "" : logPath.FullName + ; }  }
  public  static   void       sLogPath (object obj,                                       string value) { logPath = (value.Length == 0) ? null : new FileInfo(Directory.CreateDirectory(value).FullName); }
  private static   string     formatId (long                                                        id) { string ret = "                    " + id; return ret.Substring(ret.Length  - 20); }
  public  static   void       logErr   (object caller, long prio, long id, string location, string msg) { using (StreamWriter sw = logPath.AppendText()) sw.WriteLine("<!> " + utl.stdDateTimeStamp(utl.Now(), false) + " " + prio + " ERR " + formatId(id) + " \t" + location + "\r\n" + msg); }
  public  static   void       logWrn   (object caller, long prio, long id, string location, string msg) { using (StreamWriter sw = logPath.AppendText()) sw.WriteLine("<!> " + utl.stdDateTimeStamp(utl.Now(), false) + " " + prio + " WRN " + formatId(id) + " \t" + location + "\r\n" + msg); }
  public  static   void       logScc   (object caller, long prio, long id, string location, string msg) { using (StreamWriter sw = logPath.AppendText()) sw.WriteLine("<!> " + utl.stdDateTimeStamp(utl.Now(), false) + " " + prio + " SCC " + formatId(id) + " \t" + location + "\r\n" + msg); }
  public  static   void       logInf   (object caller, long prio, long id, string location, string msg) { using (StreamWriter sw = logPath.AppendText()) sw.WriteLine("<!> " + utl.stdDateTimeStamp(utl.Now(), false) + " " + prio + " INF " + formatId(id) + " \t" + location + "\r\n" + msg); }
  public  static   void       logHit   (object caller, long prio, long id, string location, string msg) { using (StreamWriter sw = logPath.AppendText()) sw.WriteLine("<!> " + utl.stdDateTimeStamp(utl.Now(), false) + " " + prio + " HIT " + formatId(id) + " \t" + location + "\r\n" + msg); }

  protected static KeyPile<string, object>                       _param                 = new KeyPile<string, object>();
  public           KeyPile<string, object>                       param                  = _param;
  public           ctx()   {  }

  private static KeyPile<string, NamedValue<long, TimeSpan>>   stat                   = new KeyPile<string, NamedValue<long, TimeSpan>>();
  private static object                                        draggedObject          = null;
  private static KeyPile<string, string[]>                     dbDrivers              = new KeyPile<string, string[]>();
  private static bool                                          useHtmlAgilityPack     = false; // use the HtmlAgilityPack
  private static string                                        name                   = "Application";
  private static Pile<string>                                  args                   = new Pile<string>();
  private static KeyPile<string, string>                       iniParam               = new KeyPile<string, string>();
  private static string                                        font                   = "Arial Narrow; 8,25; 12; 0; 0; 0";
  private static string                                        dbFont                 = "Arial Narrow; 8,25; 12; 0; 0; 192";
  private static string                                        tsFont                 = "Arial Narrow; 8,25; 12; #8b0000";

  public         KeyPile<string, NamedValue<long, TimeSpan>>   Stat                   { get { return stat                   ;} set { stat                   = value; } }
  public         object                                        DraggedObject          { get { return draggedObject          ;} set { draggedObject          = value; } }
  public         KeyPile<string, string[]>                     DbDrivers              { get { return dbDrivers              ;} set { dbDrivers              = value; } }
  public         bool                                          UseHtmlAgilityPack     { get { return useHtmlAgilityPack     ;} set { useHtmlAgilityPack     = value; } }
  
  public         string                                        Name                   { get { if (param.hasKey("Name")) return (string) param["Name"]; return name; } set { param.Set("Name", value);} }
  
  public         Pile<string>                                  Args                   { get { return args                   ;} set { args                   = value; } }
  public         KeyPile<string, string>                       IniParam               { get { return iniParam               ;} set { iniParam               = value; } }
  public         KeyPile<string, object>                       Param                  { get { return param                  ;} set { param                  = value; } }
  public         string                                        Font                   { get { return font                   ;} set { font                   = value; } }
  public         string                                        DbFont                 { get { return dbFont                 ;} set { dbFont                 = value; } }
  public         string                                        TsFont                 { get { return tsFont                 ;} set { tsFont                 = value; } }

  public static  DirectoryInfo                                 confFolder             = Directory.CreateDirectory(".");
  public static  DirectoryInfo                                 logFolder              = Directory.CreateDirectory(".");
  public static  DirectoryInfo                                 cacheFolder            = Directory.CreateDirectory(".");

  public         DirectoryInfo                                 ConfFolder             { get {return confFolder                  ;} set { confFolder                 = value; } }
  public         DirectoryInfo                                 LogFolder              { get {return logFolder                   ;} set { logFolder                  = value; } }
  public         DirectoryInfo                                 CacheFolder            { get {return cacheFolder                 ;} set { cacheFolder                = value; } }

  public void connect(string confPath, string logPath, string cachePath) { confFolder  = Directory.CreateDirectory(confPath); logFolder   = Directory.CreateDirectory(logPath); cacheFolder = Directory.CreateDirectory(cachePath); }

  private string[] dbdriverEntry(string val)
  {
   string[] ret = new string[3]; // odbc, jdbc, dotnet
   ret[0] = utl.cutl(val, ";"); val = val.Substring(ret[0].Length + 1, val.Length - ret[0].Length - 1); ret[0] = ret[0].Trim();
   ret[1] = utl.cutl(val, ";"); val = val.Substring(ret[1].Length + 1, val.Length - ret[1].Length - 1); ret[1] = ret[1].Trim();
   ret[2] = utl.cutl(val, ";");                                                                         ret[2] = ret[2].Trim();
   return ret;
  }

  public void init(string App_Name, string[] args) //reads Options from Environment, Arguments and Ini-Files INTO the static class "ao" (AppOptions)
  {
   /*
   dbDrivers.Set("h2s",  "h2s  00  (H2Sql)");
   dbDrivers.Set("mys",  "mys  01  (Oracle MySql)");
   dbDrivers.Set("2mys", "2mys 01h (MySql Emulation of H2Db)");
   dbDrivers.Set("pgs",  "pgs  02  (PostGreSql)");
   dbDrivers.Set("2pgs", "2pgs 02h (PostGreSql Emulation of H2Db)");
   dbDrivers.Set("fbd",  "fbd  03  (FireBirdSql)");
   dbDrivers.Set("2fbd", "2fbd 03h (FireBirdSql Emulation of H2Db)");
   dbDrivers.Set("sql",  "sql  04  (SqLite)");
   dbDrivers.Set("2sql", "2sql 04h (SqLite Emulation of H2Db)");
   dbDrivers.Set("mss",  "mss  05  (MsSqlServer)");
   dbDrivers.Set("2mss", "2mss 05h (MsSqlServer Emulation of H2Db)");
   dbDrivers.Set("syb",  "syb  06  (SAP SyBase)");
   dbDrivers.Set("2syb", "2syb 06h (SAP SyBase Emulation of H2Db)");
   dbDrivers.Set("ora",  "ora  07  (Oracle)");
   dbDrivers.Set("2ora", "2ora 07h (Oracle Emulation of H2Db)");
   dbDrivers.Set("db2",  "db2  08  (IBM DB2)");
   dbDrivers.Set("2db2", "2db2 08h (IBM DB2 Emulation of H2Db)");
   dbDrivers.Set("dby",  "dby  09  (Apache Derby)");
   dbDrivers.Set("2dby", "2dby 09h (Apache Derby Emulation of H2Db)");
   dbDrivers.Set("hsq",  "hsq  10  (HSqlDb)");
   dbDrivers.Set("2hsq", "2hsq 10h (HSqlDb Emulation of H2Db)");
   */

   useHtmlAgilityPack = true; // ndStruct should replace the HtmlAgilityPack but ndStruct is not tested enough and ndStructs is not yet performant enough
   name = App_Name;
   ctx.args.Add(args);

   //if (args.Count() != 1) { Console.WriteLine("Usage: " + appName + " \"Directory\""); Console.WriteLine("press any key to abort ..."); Console.ReadKey(); return; }
   Registry.CurrentUser.CreateSubKey("SOFTWARE\\" + name);
   string iniContent = "";
   try
   {
    string iniFile = Path.GetDirectoryName((new Uri(Assembly.GetEntryAssembly().CodeBase)).AbsolutePath) + "/" + name + ".ini".Replace("\\", "/").Replace("//", "\\\\");
    iniContent = utl.f2s(iniFile);
   }
   catch (Exception e) 
   {
    try
    {
     string iniFile = Path.GetDirectoryName((new Uri(Assembly.GetEntryAssembly().CodeBase)).AbsolutePath) + "\\..\\..\\..\\" + name + ".ini".Replace("\\", "/").Replace("//", "\\\\");
     iniContent = utl.f2s(iniFile);
    }
    catch (Exception ex) { } // cannot throw Exception because there is no INI-File when ctx has been initialized from a Web App! 
   } 
   
   while (iniContent.Length > 0)
   {
    string line = utl.cutl(ref iniContent, "\n").Replace("\r", "").Trim();
    if ((line.IndexOf("=") > -1) & (!line.StartsWith("//")))
    {
     string entryName = utl.cutl(ref line, "=").Trim();
     string entryValue = utl.cutl(ref line, "//").Trim();
     if (entryName.ToUpper().Equals("FONT")) Font = entryValue;
     else if (entryName.ToUpper().Equals("DBFONT")) DbFont = entryValue;
     else if (entryName.ToUpper().Equals("TSFONT")) TsFont = entryValue;

     else if (entryName.ToUpper().Equals("DBD-H2S" ))  dbDrivers.Set("h2s",  dbdriverEntry(entryValue)); //00  (H2Sql)");
     else if (entryName.ToUpper().Equals("DBD-MYS"))   dbDrivers.Set("mys",  dbdriverEntry(entryValue)); //01  (Oracle MySql)");
     else if (entryName.ToUpper().Equals("DBD-2MYS"))  dbDrivers.Set("2mys", dbdriverEntry(entryValue)); //01h (MySql Emulation of H2Db)");
     else if (entryName.ToUpper().Equals("DBD-PGS"))   dbDrivers.Set("pgs",  dbdriverEntry(entryValue)); //02  (PostGreSql)");
     else if (entryName.ToUpper().Equals("DBD-2PGS"))  dbDrivers.Set("2pgs", dbdriverEntry(entryValue)); //02h (PostGreSql Emulation of H2Db)");
     else if (entryName.ToUpper().Equals("DBD-FBD"))   dbDrivers.Set("fbd",  dbdriverEntry(entryValue)); //03  (FireBirdSql)");
     else if (entryName.ToUpper().Equals("DBD-2FBD"))  dbDrivers.Set("2fbd", dbdriverEntry(entryValue)); //03h (FireBirdSql Emulation of H2Db)");
     else if (entryName.ToUpper().Equals("DBD-SQL"))   dbDrivers.Set("sql",  dbdriverEntry(entryValue)); //04  (SqLite)");
     else if (entryName.ToUpper().Equals("DBD-2SQL"))  dbDrivers.Set("2sql", dbdriverEntry(entryValue)); //04h (SqLite Emulation of H2Db)");
     else if (entryName.ToUpper().Equals("DBD-MSS" ))  dbDrivers.Set("mss",  dbdriverEntry(entryValue)); //05  (MsSqlServer)");
     else if (entryName.ToUpper().Equals("DBD-2MSS"))  dbDrivers.Set("2mss", dbdriverEntry(entryValue)); //05h (MsSqlServer Emulation of H2Db)");
     else if (entryName.ToUpper().Equals("DBD-SYB" ))  dbDrivers.Set("syb",  dbdriverEntry(entryValue)); //06  (SAP SyBase)");
     else if (entryName.ToUpper().Equals("DBD-2SYB"))  dbDrivers.Set("2syb", dbdriverEntry(entryValue)); //06h (SAP SyBase Emulation of H2Db)");
     else if (entryName.ToUpper().Equals("DBD-ORA" ))  dbDrivers.Set("ora",  dbdriverEntry(entryValue)); //07  (Oracle)");
     else if (entryName.ToUpper().Equals("DBD-2ORA"))  dbDrivers.Set("2ora", dbdriverEntry(entryValue)); //07h (Oracle Emulation of H2Db)");
     else if (entryName.ToUpper().Equals("DBD-DB2" ))  dbDrivers.Set("db2",  dbdriverEntry(entryValue)); //08  (IBM DB2)");
     else if (entryName.ToUpper().Equals("DBD-2DB2"))  dbDrivers.Set("2db2", dbdriverEntry(entryValue)); //08h (IBM DB2 Emulation of H2Db)");
     else if (entryName.ToUpper().Equals("DBD-DBY" ))  dbDrivers.Set("dby",  dbdriverEntry(entryValue)); //09  (Apache Derby)");
     else if (entryName.ToUpper().Equals("DBD-2DBY"))  dbDrivers.Set("2dby", dbdriverEntry(entryValue)); //09h (Apache Derby Emulation of H2Db)");
     else if (entryName.ToUpper().Equals("DBD-HSQ" ))  dbDrivers.Set("hsq",  dbdriverEntry(entryValue)); //10  (HSqlDb)");
     else if (entryName.ToUpper().Equals("DBD-2HSQ"))  dbDrivers.Set("2hsq", dbdriverEntry(entryValue)); //10h (HSqlDb Emulation of H2Db)");
     else if (entryName.ToUpper().Equals("DBD-TDA"))   dbDrivers.Set("tda",  dbdriverEntry(entryValue)); //11  (Novell TeraData)");

     iniParam.Add(entryName.ToUpper(), entryValue);
    }
   }
  }

  public bool keysActive(bool exact, MouseEventArgs e, long rMouse, long numLock, long scrollLock, long capsLock, long lMouse, long alt, long control, long shift)
  {
   long lMouseButton = (e.Button == MouseButtons.Left)  ? 1 : 0;
   long rMouseButton = (e.Button == MouseButtons.Right) ? 1 : 0;
   return (exact) ? (0 == (((rMouse << 7) + (numLock << 6) + (scrollLock << 5) + (capsLock << 4) + (lMouse << 3) + (alt << 2) + (control << 2) + shift) ^ ((rMouseButton << 7) + (GetAsyncKeyState((long)Keys.NumLock) << 6) + (GetAsyncKeyState((long)Keys.Scroll) << 5) + (GetAsyncKeyState((long)Keys.CapsLock) << 4) + (lMouseButton << 3) + (GetAsyncKeyState((long)Keys.Alt) << 2) + (GetAsyncKeyState((long)Keys.ControlKey) << 1) + GetAsyncKeyState((long)Keys.ShiftKey)))) : (0 != (((rMouse << 7) + (numLock << 6) + (scrollLock << 5) + (capsLock << 4) + (lMouse << 3) + (alt << 2) + (control << 2) + shift) & ((rMouseButton << 7) + (GetAsyncKeyState((long)Keys.NumLock) << 6) + (GetAsyncKeyState((long)Keys.Scroll) << 5) + (GetAsyncKeyState((long)Keys.CapsLock) << 4) + (lMouseButton << 3) + (GetAsyncKeyState((long)Keys.Alt) << 2) + (GetAsyncKeyState((long)Keys.ControlKey) << 1) + GetAsyncKeyState((long)Keys.ShiftKey))));
  }

  public bool keysMask(MouseEventArgs e, long rMouse, long numLock, long scrollLock, long capsLock, long lMouse, long alt, long control, long shift)
  {
   long lMouseButton = (e == null)? 0: (e.Button == MouseButtons.Left)  ? 1 : 0;
   long rMouseButton = (e == null)? 0: (e.Button == MouseButtons.Right) ? 1: 0;
   ulong NotMustBePressed  = 0xFFFFFFFFFFFFFF00 | (ulong)((Math.Abs(Math.Sign(rMouse - 1)) << 7) | (Math.Abs(Math.Sign(numLock - 1)) << 6) | (Math.Abs(Math.Sign(scrollLock - 1)) << 5) | (Math.Abs(Math.Sign(capsLock - 1)) << 4) | (Math.Abs(Math.Sign(lMouse - 1)) << 3) | (Math.Abs(Math.Sign(alt - 1)) << 2) | (Math.Abs(Math.Sign(control - 1)) << 1) | Math.Abs(Math.Sign(shift - 1)));
   ulong NotMustBeReleased = 0xFFFFFFFFFFFFFF00 | (ulong)((Math.Abs(Math.Sign(rMouse + 1)) << 7) | (Math.Abs(Math.Sign(numLock + 1)) << 6) | (Math.Abs(Math.Sign(scrollLock + 1)) << 5) | (Math.Abs(Math.Sign(capsLock + 1)) << 4) | (Math.Abs(Math.Sign(lMouse + 1)) << 3) | (Math.Abs(Math.Sign(alt + 1)) << 2) | (Math.Abs(Math.Sign(control + 1)) << 1) | Math.Abs(Math.Sign(shift + 1)));
   //ulong NotCurrent = ~((ulong)((rMouseButton << 7) | (Math.Sign(GetAsyncKeyState((long)Keys.NumLock)) << 6) | (Math.Sign(GetAsyncKeyState((long)Keys.Scroll)) << 5) | (Math.Sign(GetAsyncKeyState((long)Keys.CapsLock)) << 4) | (lMouseButton << 3) | (Math.Sign(GetAsyncKeyState((long)Keys.Alt)) << 2) | (Math.Sign(GetAsyncKeyState((long)Keys.ControlKey)) << 1) | Math.Sign(GetAsyncKeyState((long)Keys.ShiftKey))));
   ulong NotCurrent = ~((ulong)((rMouseButton << 7) | (Math.Sign(GetAsyncKeyState((long)Keys.NumLock)) << 6) | (Math.Sign(GetAsyncKeyState((long)Keys.Scroll)) << 5) | (Math.Sign(GetAsyncKeyState((long)Keys.CapsLock)) << 4) | (lMouseButton << 3) | (Math.Sign(GetAsyncKeyState((long)Keys.Alt)) << 2) | (Math.Sign(GetAsyncKeyState((long)Keys.ControlKey)) << 1) | Math.Sign(GetAsyncKeyState((long)Keys.ShiftKey))));
   return (0 == ~(NotMustBeReleased | NotCurrent)) && ((NotMustBePressed | NotCurrent) == NotMustBePressed);
  }

  public string curDir { get { return System.IO.Directory.GetCurrentDirectory()        ; } set { System.IO.Directory.SetCurrentDirectory(value); } }
  public string exeDir { get { return System.Windows.Forms.Application.ExecutablePath  ; }                                                         }   //System.Windows.Forms.Application.StartupPath
  


 }

}













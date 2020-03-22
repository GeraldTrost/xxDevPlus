

//** @author  GeTr Copyright© 1996-2020 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties
//** @license You may use, modify, distribute this code under GNU public dual licence free for NON-Commercial use as long as this notice remains unchanged.
//** @comment Application Context

package org.xxdevplus.utl;


import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import org.xxdevplus.frmlng.LexLang;
import org.xxdevplus.frmlng.Pick;
import org.xxdevplus.gui.XhtDbgDlg;
import org.xxdevplus.gui.XhtParserView;
import org.xxdevplus.struct.KeyPile;
import org.xxdevplus.struct.NamedValue;
import org.xxdevplus.struct.Pile;
import org.xxdevplus.utl.utl;

/**
 *
 * @author GeTr
 */
 
 public class ctx  //Options ... Here may your Application store some global Options ...
 {
  private static boolean selfTested  = false; private static String ass(boolean expr) throws Exception { if (!expr) throw new Exception(ass(true) + " SelfTest Failure"); return "ctx"; } private static void selfTest() { selfTested = true; } private static void init() { if (!selfTested) selfTest(); }

  public  static   boolean useGuiDialogs          = false;

  private static   int[]   keyStates              = new int[65537];
  private static   int[]   mouseStates            = new int[4];

  public  static   long    Keys_NumLock           =  0;
  public  static   long    Keys_Scroll            =  0;
  public  static   long    Keys_CapsLock          =  0;
  public  static   long    Keys_Alt               = 18;
  public  static   long    Keys_ControlKey        = 17;
  public  static   long    Keys_ShiftKey          = 16;


  protected static KeyPile<String, Object>               _param                     = new KeyPile<String, Object>();
  public           KeyPile<String, Object>                 param                      = _param;
  public           ctx() throws Exception  
  {
   new LexLang("init");         // must initialize static Members of class LexLang before using it;
   Pick p = new Pick("", "");   // need to make sure thet Pick gets initialized at least once
  }

  private static KeyPile<String, NamedValue<Long, Long>> stat                       = new KeyPile<String, NamedValue<Long, Long>>();
  private static Object                                  draggedObject              = null;
  private static boolean                                 useHtmlAgilityPack         = false; // use the HtmlAgilityPack
  private static String                                  name                       = "Application";
  private static Pile<String>                            args                       = new Pile<String>();
  private static KeyPile<String, String>                 iniParam                   = new KeyPile<String, String>();
  private static KeyPile<String, String[]>               dbDrivers                  = new KeyPile<String, String[]>();
  private static String                                  font                       = "Arial Narrow; 8,25; 12; 0; 0; 0";
  private static String                                  dbFont                     = "Arial Narrow; 8,25; 12; 0; 0; 192";
  private static String                                  tsFont                     = "Arial Narrow; 8,25; 12; #8b0000";

  public KeyPile<String, NamedValue<Long, Long>> Stat               (             ) { return stat               ; } public void Stat               (KeyPile<String, NamedValue<Long, Long>> value)                  { stat               = value; }
  public Object                                  DraggedObject      (             ) { return draggedObject      ; } public void DraggedObject      (Object                                  value)                  { draggedObject      = value; }
  public KeyPile<String, String[]>               DbDrivers          (             ) { return dbDrivers          ; } public void DbDrivers          (KeyPile<String, String[]>               value)                  { dbDrivers          = value; }
  public boolean                                 UseHtmlAgilityPack (             ) { return useHtmlAgilityPack ; } public void UseHtmlAgilityPack (boolean                                 value)                  { useHtmlAgilityPack = value; }

  public String                                  Name               (             ) throws Exception { if (param.hasKey("Name")) return (String) param.g("Name"); return name; }
  public void                                    Name               (String value ) throws Exception { param.Set("Name", value); }

  // public Pile<String>                         Args               (             ) { return args               ; } public void Args               (Pile<String>                            value)                  { args               = value; }
  public KeyPile<String, String>                 Args            (             ) { return cmdArgs            ; } //public void cmdArgs             (Pile<String>                            value)                  { args               = value; }
  
  public KeyPile<String, String>                 IniParam           (             ) { return iniParam           ; } public void IniParam           (KeyPile<String, String>                 value)                  { iniParam           = value; }
  public KeyPile<String, Object>                 Param              (             ) { return param              ; } public void Param              (KeyPile<String, Object>                 value)                  { param              = value; }
  public String                                  Font               (             ) { return font               ; } public void Font               (String                                  value)                  { font               = value; }
  public String                                  DbFont             (             ) { return dbFont             ; } public void DbFont             (String                                  value)                  { dbFont             = value; }
  public String                                  TsFont             (             ) { return tsFont             ; } public void TsFont             (String                                  value)                  { tsFont             = value; }
  
  public static  File                            conf                               = new File("./conf");
  public static  File                            log                                = new File("./log");
  public static  File                            cache                              = new File("./cache");

  public         File                            Conf               (             ) { return conf               ; } public void Conf        (File         val)  { conf                       = val; }
  public         File                            Log                (             ) { return log                ; } public void Log         (File         val)  { log                        = val; }
  public         File                            Cache              (             ) { return cache              ; } public void Cache       (File         val)  { cache                      = val; }

  public static  Class<?>                        startClass         = null;
  public static  Object                          application        = null; 
 
  public static  Pile<String>                    opts               = new Pile<String>(0, "help", "console");
  public         Pile<String>                    Opts               (             ) { return opts               ; } public void Opts        (Pile<String> val)  { opts                       = val; }

  public static  KeyPile<String, String>         cmdArgs            = new KeyPile<String, String>();

  public void connect(String confPath, String logPath, String cachePath) { conf = new File(confPath); conf.mkdir(); log = new File(logPath); log.mkdir(); cache = new File(cachePath); cache.mkdir(); }
  
  public int           xhtdbg        (                   ) throws Exception { if (Param().hasKey("xhtdbg")) return Integer.parseInt((String) Param().g("xhtdbg")); return 0; }
  public void          xhtdbg        (int        dbgLevel) throws Exception { Param().p("" + dbgLevel, "xhtdbg"); }
  public XhtDbgDlg     xhtDbgDlg     (                   ) throws Exception { if (Param().hasKey("xhtDbgDlg")) return (XhtDbgDlg)Param().g("xhtDbgDlg"); return null; }
  public void          xhtDbgDlg     (XhtDbgDlg xhtDbgDlg) throws Exception { Param().p(xhtDbgDlg, "xhtDbgDlg"); }
  public XhtParserView xhtParserView (                   ) throws Exception { if (Param().hasKey("xhtParserView")) return (XhtParserView)Param().g("xhtParserView"); return null; }
  public void          xhtParserView (XhtParserView xhtParserView) throws Exception { Param().p(xhtParserView, "xhtParserView"); }
  
  private String[] dbdriverEntry(String val)
  {
   String[] ret = new String[3]; // odbc, jdbc, dotnet
   ret[0] = utl.cutl(val, ";"); val = val.substring(ret[0].length() + 1, val.length()); ret[0] = ret[0].trim();
   ret[1] = utl.cutl(val, ";"); val = val.substring(ret[1].length() + 1, val.length()); ret[1] = ret[1].trim();
   ret[2] = utl.cutl(val, ";");                                                         ret[2] = ret[2].trim();
   return ret;
  }

  public ctx init( String App_Name, Pile<String> opts, String[] args, Class<?> startClass, Object application) throws IOException, Exception //reads Options from Environment, Arguments and Ini-Files INTO the static class "ao" (AppOptions)
  {

   ctx.opts.Add(opts);
   ctx.startClass   = startClass;
   ctx.application  = application;

   int cmdCtr = -1;
   int optCtr = 0;
   for (int i = 1; i <= args.length; i++)
   {
    String arg  = args[i-1].trim();
    utl.say(arg);
    String larg = arg.toLowerCase();
    if (larg.startsWith("/"))
    {
     if ((larg.indexOf(':') == -1) || (larg.substring(1, larg.indexOf(':')).equals("cmd")))
     {
      if (++cmdCtr == 0) cmdArgs.Add("cmd", arg.substring(Math.max(1, larg.indexOf(':') + 1))); else cmdArgs.Add("cmd" + cmdCtr, arg.substring(Math.max(1, larg.indexOf(':') + 1))); 
     }
     else 
     {
      cmdArgs.Add(larg.substring(1, larg.indexOf(':')), arg.substring(arg.indexOf(':') + 1));
     }
    }
    else
    {
     cmdArgs.Add("opt" + ++optCtr, arg);
    }
   }

   if (!cmdArgs.hasKey("cmd")) cmdArgs.Add("cmd", "console"); // if no cmd is given in the commandLine it will be console per default
   //int inx = 0; for (String key : cmdArgs.Keys()) { utl.say("" + ++inx + "\t" + key + "\t" + cmdArgs.g(key)); }
   
   /*
   dbDrivers.Set("h2s" , "h2s  00  (H2Sql)");
   dbDrivers.Set("mys",  "mys  01  (Oracle MySql)");
   dbDrivers.Set("2mys", "2mys 01h (MySql Emulation of H2Db)");
   dbDrivers.Set("pgs" , "pgs  02  (PostGreSql)");
   dbDrivers.Set("2pgs", "2pgs 02h (PostGreSql Emulation of H2Db)");
   dbDrivers.Set("fbd" , "fbd  03  (FireBirdSql)");
   dbDrivers.Set("2fbd", "2fbd 03h (FireBirdSql Emulation of H2Db)");
   dbDrivers.Set("sql" , "sql  04  (SqLite)");
   dbDrivers.Set("2sql", "2sql 04h (SqLite Emulation of H2Db)");
   dbDrivers.Set("mss" , "mss  05  (MsSqlServer)");
   dbDrivers.Set("2mss", "2mss 05h (MsSqlServer Emulation of H2Db)");
   dbDrivers.Set("syb" , "syb  06  (SAP SyBase)");
   dbDrivers.Set("2syb", "2syb 06h (SAP SyBase Emulation of H2Db)");
   dbDrivers.Set("ora" , "ora  07  (Oracle)");
   dbDrivers.Set("2ora", "2ora 07h (Oracle Emulation of H2Db)");
   dbDrivers.Set("db2" , "db2  08  (IBM DB2)");
   dbDrivers.Set("2db2", "2db2 08h (IBM DB2 Emulation of H2Db)");
   dbDrivers.Set("dby" , "dby  09  (Apache Derby)");
   dbDrivers.Set("2dby", "2dby 09h (Apache Derby Emulation of H2Db)");
   dbDrivers.Set("hsq" , "hsq  10  (HSqlDb)");
   dbDrivers.Set("2hsq", "2hsq 10h (HSqlDb Emulation of H2Db)");
   */

   useHtmlAgilityPack = true; // ndStruct should replace the HtmlAgilityPack but ndStruct is not tested enough and ndStructs is not yet performant enough
   name = App_Name;
   ctx.args.Add(args);

   //if (args.Count() != 1) { Console.WriteLine("Usage: " + name + " \"Directory\""); Console.WriteLine("press any key to abort ..."); Console.ReadKey(); return; }
   //Registry.CurrentUser.CreateSubKey("SOFTWARE\\" + Name);
   //GlobalScreen globalScreen = GlobalScreen.getInstance();


   //globalScreen.addNativeKeyListener(this);

   //GlobalScreen.getInstance().addNativeMouseListener(this);
   //GlobalScreen.getInstance().addNativeMouseMotionListener(this);
   //GlobalScreen.getInstance().addNativeMouseWheelListener(this);
   //try { GlobalScreen.registerNativeHook(); } catch (Exception ex) { System.err.println("cannot register native hooks in this Operating System \r\n" + ex.getMessage()); }

   String[] iniContent = new String[1];
   String iniFile = new File(".").getCanonicalPath() + "/" + name + ".properties".replace("\\", "/").replace("//", "\\\\");
   
   try { iniContent[0] = utl.f2s(iniFile); } catch (Exception e) { throw new Exception("" + name + ".properties not found in Folder " + new File(".").getAbsolutePath()); }

   String iniSection = "";
   
   while (iniContent[0].length() > 0)
   {
    String[] line = new String[1];
    line[0] = utl.cutl(iniContent, "\n").replace("\r", "").trim();
    if ((line[0].length() > 0) && (!line[0].startsWith("//")))
    {
     if (line[0].startsWith("[")) 
      iniSection = line[0].replace("[", "").replace("]", "").toUpperCase().trim(); 
     else
      {
       String entryName = utl.cutl(line, "=").trim();
       String entryValue = utl.cutl(line, "//").trim();
       if (entryName.toUpperCase().equals("FONT"))   Font   (entryValue);
       else if (entryName.toUpperCase().equals("DBFONT")) DbFont(entryValue);
       else if (entryName.toUpperCase().equals("TSFONT")) TsFont(entryValue);
       else if (entryName.toUpperCase().equals("DBD-H2S" ))  dbDrivers.Set("h2s",  dbdriverEntry(entryValue)); //00  (H2Sql)");
       else if (entryName.toUpperCase().equals("DBD-MYS" ))  dbDrivers.Set("mys",  dbdriverEntry(entryValue)); //01  (Oracle MySql)");
       else if (entryName.toUpperCase().equals("DBD-2MYS"))  dbDrivers.Set("2mys", dbdriverEntry(entryValue)); //01h (MySql Emulation of H2Db)");
       else if (entryName.toUpperCase().equals("DBD-PGS" ))  dbDrivers.Set("pgs",  dbdriverEntry(entryValue)); //02  (PostGreSql)");
       else if (entryName.toUpperCase().equals("DBD-2PGS"))  dbDrivers.Set("2pgs", dbdriverEntry(entryValue)); //02h (PostGreSql Emulation of H2Db)");
       else if (entryName.toUpperCase().equals("DBD-FBD" ))  dbDrivers.Set("fbd",  dbdriverEntry(entryValue)); //03  (FireBirdSql)");
       else if (entryName.toUpperCase().equals("DBD-2FBD"))  dbDrivers.Set("2fbd", dbdriverEntry(entryValue)); //03h (FireBirdSql Emulation of H2Db)");
       else if (entryName.toUpperCase().equals("DBD-SQL" ))  dbDrivers.Set("sql",  dbdriverEntry(entryValue)); //04  (SqLite)");
       else if (entryName.toUpperCase().equals("DBD-2SQL"))  dbDrivers.Set("2sql", dbdriverEntry(entryValue)); //04h (SqLite Emulation of H2Db)");
       else if (entryName.toUpperCase().equals("DBD-MSS" ))  dbDrivers.Set("mss",  dbdriverEntry(entryValue)); //05  (MsSqlServer)");
       else if (entryName.toUpperCase().equals("DBD-2MSS"))  dbDrivers.Set("2mss", dbdriverEntry(entryValue)); //05h (MsSqlServer Emulation of H2Db)");
       else if (entryName.toUpperCase().equals("DBD-SYB" ))  dbDrivers.Set("syb",  dbdriverEntry(entryValue)); //06  (SAP SyBase)");
       else if (entryName.toUpperCase().equals("DBD-2SYB"))  dbDrivers.Set("2syb", dbdriverEntry(entryValue)); //06h (SAP SyBase Emulation of H2Db)");
       else if (entryName.toUpperCase().equals("DBD-ORA" ))  dbDrivers.Set("ora",  dbdriverEntry(entryValue)); //07  (Oracle)");
       else if (entryName.toUpperCase().equals("DBD-2ORA"))  dbDrivers.Set("2ora", dbdriverEntry(entryValue)); //07h (Oracle Emulation of H2Db)");
       else if (entryName.toUpperCase().equals("DBD-DB2" ))  dbDrivers.Set("db2",  dbdriverEntry(entryValue)); //08  (IBM DB2)");
       else if (entryName.toUpperCase().equals("DBD-2DB2"))  dbDrivers.Set("2db2", dbdriverEntry(entryValue)); //08h (IBM DB2 Emulation of H2Db)");
       else if (entryName.toUpperCase().equals("DBD-DBY" ))  dbDrivers.Set("dby",  dbdriverEntry(entryValue)); //09  (Apache Derby)");
       else if (entryName.toUpperCase().equals("DBD-2DBY"))  dbDrivers.Set("2dby", dbdriverEntry(entryValue)); //09h (Apache Derby Emulation of H2Db)");
       else if (entryName.toUpperCase().equals("DBD-HSQ" ))  dbDrivers.Set("hsq",  dbdriverEntry(entryValue)); //10  (HSqlDb)");
       else if (entryName.toUpperCase().equals("DBD-2HSQ"))  dbDrivers.Set("2hsq", dbdriverEntry(entryValue)); //10h (HSqlDb Emulation of H2Db)");
       else if (entryName.toUpperCase().equals("DBD-TDA"))   dbDrivers.Set("tda",  dbdriverEntry(entryValue)); //11  (Novell TeraData)");
       iniParam.Add((iniSection + ".").substring((iniSection.length() == 0) ? 1  : 0) + entryName.toUpperCase(), entryValue);
      }
     }
    }
   
   if (!cmdArgs.hasKey("prod")) cmdArgs.Add("prod", iniParam.g("COMMANDARGS./PROD").replace("\"", ""));
   if (!cmdArgs.hasKey("db")) cmdArgs.Add("db", iniParam.g("COMMANDARGS./DB").replace("\"", ""));
   
   String paths = (cmdArgs.hasKey("prod")) ? cmdArgs.g("prod") : iniParam.hasKey("COMMANDARGS./PROD") ? iniParam.g("COMMANDARGS./PROD").replace("\"", "") : "./conf;./log;./cache";
   conf         = new File(paths.substring(0, paths.indexOf(';')));             conf.mkdir();
   paths        = paths.substring(paths.indexOf(';') + 1);
   log          = new File(paths.substring(0, paths.indexOf(';')));             log.mkdir();
   cache        = new File(paths.substring(paths.indexOf(';') + 1));            cache.mkdir();
   return this;
  }

 public  int   GetAsyncKeyState      (long         keyCode)       { return ((keyCode < 0) && (keyCode > 65536)) ? 0 : (keyStates   [(int)keyCode] == 0) ? 0 : 1; }
 public  int   GetAsyncMouseState    (long         keyCode)       { return ((keyCode < 0) && (keyCode > 65536)) ? 0 : (mouseStates [(int)keyCode] == 0) ? 0 : 1; }
 //public  void  nativeKeyTyped        (NativeKeyEvent   nke)       {                                     }
 //public  void  nativeKeyPressed      (NativeKeyEvent   nke)       { int inx = nke.getKeyCode(); if ((inx >= 0) && (inx <= 65536)) keyStates[inx] = 1; }
 //public  void  nativeKeyReleased     (NativeKeyEvent   nke)       { int inx = nke.getKeyCode(); if ((inx >= 0) && (inx <= 65536)) keyStates[inx] = 0; }
 //public  void  nativeMouseClicked    (NativeMouseEvent nme)       {                                     }
 //public  void  nativeMousePressed    (NativeMouseEvent nme)       { int inx = nme.getButton(); if ((inx >= 0) && (inx <= 3)) mouseStates[inx] = 1; }
 //public  void  nativeMouseReleased   (NativeMouseEvent nme)       { int inx = nme.getButton(); if ((inx >= 0) && (inx <= 3)) mouseStates[inx] = 0; }
 //public  void  nativeMouseMoved      (NativeMouseEvent nme)       {                                     }
 //public  void  nativeMouseDragged    (NativeMouseEvent nme)       {                                     }
 //public  void  nativeMouseWheelMoved (NativeMouseWheelEvent nmwe) {                                     }

 public boolean keysMask(MouseEvent e, long rMouse, long numLock, long scrollLock, long capsLock, long lMouse, long alt, long control, long shift)
 {
  long lMouseButton = (e == null)? 0: GetAsyncMouseState(1);
  long rMouseButton = (e == null)? 0: GetAsyncMouseState(3);
  long NotMustBePressed  =  0xFFFFFFFFFFFFFF00L | ((Math.abs((long)Math.signum(rMouse - 1)) << 7) | (Math.abs((long)Math.signum(numLock - 1)) << 6) | (Math.abs((long)Math.signum(scrollLock - 1)) << 5) | (Math.abs((long)Math.signum(capsLock - 1)) << 4) | (Math.abs((long)Math.signum(lMouse - 1)) << 3) | (Math.abs((long)Math.signum(alt - 1)) << 2) | (Math.abs((long)Math.signum(control - 1)) << 1) | Math.abs((long)Math.signum(shift - 1)));
  long NotMustBeReleased =  0xFFFFFFFFFFFFFF00L | ((Math.abs((long)Math.signum(rMouse + 1)) << 7) | (Math.abs((long)Math.signum(numLock + 1)) << 6) | (Math.abs((long)Math.signum(scrollLock + 1)) << 5) | (Math.abs((long)Math.signum(capsLock + 1)) << 4) | (Math.abs((long)Math.signum(lMouse + 1)) << 3) | (Math.abs((long)Math.signum(alt + 1)) << 2) | (Math.abs((long)Math.signum(control + 1)) << 1) | Math.abs((long)Math.signum(shift + 1)));
  long NotCurrent = ~((long)((rMouseButton << 7) | ((long)Math.signum(GetAsyncKeyState((long)Keys_NumLock)) << 6) | ((long)(Math.signum(GetAsyncKeyState((long)Keys_Scroll))) << 5) | ((long)(Math.signum(GetAsyncKeyState((long)Keys_CapsLock))) << 4) | (lMouseButton << 3) | ((long)(Math.signum(GetAsyncKeyState((long)Keys_Alt))) << 2) | ((long)(Math.signum(GetAsyncKeyState((long)Keys_ControlKey))) << 1) | ((long)Math.signum(GetAsyncKeyState((long)Keys_ShiftKey)))));
  //System.out.println(lMouse + " (lmouse) keysMask: " + ((0 == ~(NotMustBeReleased | NotCurrent)) && ((NotMustBePressed | NotCurrent) == NotMustBePressed)));
  return (0 == ~(NotMustBeReleased | NotCurrent)) && ((NotMustBePressed | NotCurrent) == NotMustBePressed);
 }

 public boolean keysActive(boolean exact, MouseEvent e, long rMouse, long numLock, long scrollLock, long capsLock, long lMouse, long alt, long control, long shift)
 {
  long lMouseButton = GetAsyncMouseState(1);
  long rMouseButton = GetAsyncMouseState(3);
  return (exact) ?
   (0 == (((rMouse << 7) + (numLock << 6) + (scrollLock << 5) + (capsLock << 4) + (lMouse << 3) + (alt << 2) + (control << 2) + shift) ^ ((rMouseButton << 7) + (GetAsyncKeyState((long)Keys_NumLock) << 6) + (GetAsyncKeyState((long)Keys_Scroll) << 5) + (GetAsyncKeyState((long)Keys_CapsLock) << 4) + (lMouseButton << 3) + (GetAsyncKeyState((long)Keys_Alt) << 2) + (GetAsyncKeyState((long)Keys_ControlKey) << 1) + GetAsyncKeyState((long)Keys_ShiftKey))))
   :
   (0 != (((rMouse << 7) + (numLock << 6) + (scrollLock << 5) + (capsLock << 4) + (lMouse << 3) + (alt << 2) + (control << 2) + shift) & ((rMouseButton << 7) + (GetAsyncKeyState((long)Keys_NumLock) << 6) + (GetAsyncKeyState((long)Keys_Scroll) << 5) + (GetAsyncKeyState((long)Keys_CapsLock) << 4) + (lMouseButton << 3) + (GetAsyncKeyState((long)Keys_Alt) << 2) + (GetAsyncKeyState((long)Keys_ControlKey) << 1) + GetAsyncKeyState((long)Keys_ShiftKey))));
 }

 public Thread[] allThreads()
 {
  ThreadGroup group = Thread.currentThread().getThreadGroup();
  while (group.getParent() != null) group = group.getParent();
  Thread[] threads = new Thread[group.activeCount() + 200];  //200 is safety factor just in case that suddenly many sub-Treads got created ...
  group.enumerate(threads);
  return threads;
 }

 public String curDir() { return System.getProperty("user.dir")                   ; } public void curDir(String value) { System.setProperty("user.dir", value); }

 public String exeDir() throws UnsupportedEncodingException
 {
  return URLDecoder.decode(startClass.getProtectionDomain().getCodeSource().getLocation().getPath(), "UTF-8");
 }

 public void showExeDir() throws Exception 
 {
  utl.say(exeDir());
 }

 

}





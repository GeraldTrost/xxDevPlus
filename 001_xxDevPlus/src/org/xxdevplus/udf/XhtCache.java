package org.xxdevplus.udf;  // Universal Data Finder

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import org.xxdevplus.ytapi.YtSearch;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.xxdevplus.chain.Chain;
import org.xxdevplus.struct.Pile;
import org.xxdevplus.utl.ctx;
import org.xxdevplus.utl.utl;
import javafx.concurrent.Worker;
import javafx.scene.Scene;
import javafx.scene.web.WebView;
import javafx.stage.Stage;


public class XhtCache 
{ 
 
 private ParserControl      ctl                         = null;
 private static   long      recenDownloadTimeMillis          = System.currentTimeMillis();
 
 private         File       baseFolder                  = null;
 public         String      fileNamesPattern            = "";
 private      YtSearch      yts                         = new YtSearch();

 
 public  File BaseFolder(               ) {return baseFolder               ; }
 public  void BaseFolder(File baseFolder) {this.baseFolder = baseFolder    ; }
 
 public XhtCache(File baseFolder, String fileNamesPattern, ParserControl ctl) throws Exception
 {
  this.ctl                           = ctl;  
  this.fileNamesPattern              = fileNamesPattern;
  this.baseFolder                    = baseFolder;
  this.baseFolder.mkdir();
 }

 public ParserControl ctl() { return ctl; }       //void ctl(ParserControl newVal) { ctl = newVal; }

 public XhtCache(String fileNamesPattern, ParserControl ctl) throws Exception  {this((File)(new ctx().Cache()), fileNamesPattern, ctl);}

 public File cachedFile(XhtUrl url) throws Exception
 {
  File ret = new File(baseFolder.getAbsolutePath() + "/" + url.signature(fileNamesPattern));
  ret.getParentFile().mkdirs();
  return ret;
 }
 
 public void write(File cachedFile, String data) throws Exception
 {
  cachedFile.getParentFile().mkdirs();
  BufferedWriter bw = new BufferedWriter(new FileWriter(cachedFile));
  bw.write(data);
  bw.close();
 }
 
 public File cachedFile = null;
 private String lastUrl = ""; 
 
 
 private Document dldYtSearch(String url) throws Exception
 {
  Chain u = new Chain(url);
  long lim = Long.parseLong(u.after(1, ";;").text().trim());
  Pile<Pile<String>> res = yts.ytSearch(lim, u.before(1, ";;").after(-1, "/").text());
  String xml = "<body>\n";
  for (Pile<String> row : res) 
  {
   xml += " <ytresult\n";         
   xml += "  videoid = \""       + utl.toXml(row.g(1), true) + "\" \n";
   xml += "  title = \""         + utl.toXml(row.g(2), true) + "\" \n";
   xml += "  publishedat = \""   + utl.toXml(row.g(3), true) + "\" \n";
   xml += "  description = \""   + utl.toXml(row.g(4), true) + "\" \n";
   xml += "  channeltitle = \""  + utl.toXml(row.g(5), true) + "\" \n";
   xml += " />\n";
  }
  xml += "</body>\n";
  return (Document) Jsoup.parse(xml, "", Parser.xmlParser());
 }
 
 private Document dldWithJsoup(XhtUrl docUrl) throws Exception
 {
  return (Document) 
  Jsoup
  .connect(docUrl.toString()) 
  .ignoreHttpErrors(false)
  .timeout(10000)
  .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_6) AppleWebKit/601.7.7 (KHTML, like Gecko) Version/9.1.2 Safari/601.7.7")
  .parser(Parser.xmlParser())
  .get();
 }
 
 private boolean isPlausibleCachedFile(Pile<Chain> words ) throws Exception
 {
  if ((!cachedFile.exists()) || (cachedFile.length() < 20000)) return false;
  Chain content = new Chain(utl.f2s(cachedFile.getAbsolutePath()));
  if (content.At(1, "page not found").len() > 0) return false;
  for (Chain word : words) if (content.at(1, word.text()).len() == 0) return false;
  return true;        
 }
 
 private Document dldWithWebView(XhtUrl docUrl) throws Exception
 {
  JFXPanel jfxPanel = new JFXPanel();
  jfxPanel.setVisible(true);

  Platform.runLater
  (
   new Runnable() 
   {
    @Override
    public void run() 
    {
     WebView webView = new WebView();
     jfxPanel.setScene(new Scene(webView));
     //webView.getEngine().setUserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.143 Safari/537.36");
     //webView.getEngine().setUserAgent("Google Chrome Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36.");
     //webView.getEngine().setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36 OPR/66.0.3515.36");
     //webView.getEngine().setUserAgent("OPR/66.0.3515.36");
     webView.getEngine().setUserAgent("Mozilla/5.0 (compatible, MSIE 11, Windows NT 6.3; Trident/7.0; rv:11.0) like Gecko");
     webView.getEngine().setJavaScriptEnabled(true);
     //webView.getEngine().load("https://sensortower.com/ios/rankings/top/iphone/us/all-categories/");
     //webView.getEngine().load("https://sensortower.com/ios/rankings/top/iphone/us/all-categories?date=2020-01-23");
     //webView.getEngine().load("https://www.artbasel.com/catalog/gallery/1055/Miguel-Abreu-Gallery");
     //webView.getEngine().load("https://everest.vallant.org/");
     //webView.getEngine().load("https://sensortower.com/ios/rankings/top/iphone/us/");
     webView.getEngine().load("https://www.artmiami.com/galleries");
     //webView.getEngine().load("https://tvthek.orf.at");
     Stage stage = new Stage();
     stage.setScene(jfxPanel.getScene());
     Worker<Void> worker = webView.getEngine().getLoadWorker();
    
     /*
     worker.stateProperty().addListener
     (
      new ChangeListener<State>()
      {
       @Override
       public void changed(ObservableValue<? extends State> observable, State oldValue, State newValue)
       {
        if (newValue == Worker.State.SUCCEEDED)
        {
         stage.setTitle(webView.getEngine().getLocation());
        }
       }
      }
     stage.show();
     stage.setFullScreen(true);
    }
          */
    }
   }
  ); 
  return (Document) Jsoup.parse(cachedFile, "UTF-8");  
 }
 
 private Document dldWithAhkOpera(XhtUrl docUrl) throws Exception
 {

  if (utl.runtimeProcesses().at(1, "[opera.exe]").len() == 0) 
  {
   Runtime.getRuntime().exec("cmd /C start opera");
   while (utl.runtimeProcesses().at(1, "[opera.exe]").len() == 0) Thread.sleep(800);
   Thread.sleep(800);
  }
  Pile<Chain> words = new Chain(docUrl.signature("{Fn}")).split("_");
  String ahkScript = 
   "#Singleinstance,Force\n" +
  " \n" +
  " u = " + docUrl.toString() + "\n" +
  " f = " + cachedFile.getAbsolutePath() + "\n" +
  " d = c:\\tmp\\dld ; tmp download folder\n" +
  " FileRemoveDir, %d%, 1\n" +
  " FileCreateDir, %d%\n" +
  " WinActivate  ahk_class Chrome_WidgetWin_1 ahk_exe opera.exe \n" +
  " WinMaximize  ahk_class Chrome_WidgetWin_1 ahk_exe opera.exe \n" +
  " WinWait      ahk_class Chrome_WidgetWin_1 ahk_exe opera.exe \n" +
  " WinActivate  ahk_class Chrome_WidgetWin_1 ahk_exe opera.exe \n" +
  " MsgBox  , , %u%, %f%, 3\n" +
  " WinActivate  ahk_class Chrome_WidgetWin_1 ahk_exe opera.exe \n" +
  " OutputDebug, info: sending url\n" +
  " Send ^{e}{BackSpace}\n" +
  " Sleep 800\n" +
  " WinActivate  ahk_class Chrome_WidgetWin_1 ahk_exe opera.exe \n" +
  " Send ^{e}{BackSpace}%u%`r\n" +
  " Sleep 2000\n" +
  " SendEvent +^{r}\n" +
  " Sleep 3000\n" +
  " Loop, 5\n" +
  " {\n" +
  "  SendEvent ^{-}\n" +
  "  Sleep 300\n" +
  " }\n" +
  " Loop, 12\n" +
  " {\n" +
  "  SendEvent {PGDN}\n" +
  "  Sleep 1200\n" +
  " }\n" +
  " SendEvent ^s\n" +
  " Sleep 6000\n" +
  " Send !{n}!{n}{Text}%d%\\job.html\n" +
  " Send !{t}!{t}{DOWN 3}{Enter}\n" +
  " Send !{s}!{s}\n" +
  " Sleep 18000\n" +
  " Send !{Left}\n" +
  " FileRead b, %d%\\job.html\n" +
  " FileDelete, %f%\n" +
  " FileAppend %b%, %f%\n" +
  " WinActivate  ahk_class Chrome_WidgetWin_1 ahk_exe opera.exe \n" +
  " WinMinimize  ahk_class Chrome_WidgetWin_1 ahk_exe opera.exe \n" +
  " ExitApp\n";
  
  utl.s2f(ahkScript, "c:/tmp/job.ahk", false);
  int retry = 4;
  while ((!isPlausibleCachedFile(words)) && (retry-- > 0))
  {
   Runtime.getRuntime().exec("cmd /C taskkill /f /T /IM AutoHotkey.exe");
   Runtime.getRuntime().exec("cmd /C taskkill /f /T /IM AutoHotkey.exe");
   Runtime.getRuntime().exec("cmd /C taskkill /f /T /IM AutoHotkey.exe");
   Thread.sleep(500);
   Runtime.getRuntime().exec("cmd /C c:/tmp/job.ahk");
   Thread.sleep(500);
   while(utl.runtimeProcesses().at(1, "[AutoHotkey.exe]").len() > 0) 
    Thread.sleep(200);
  }
  if (!isPlausibleCachedFile(words))
  {
   cachedFile.delete();
   throw new Exception("AHK/Opera download failes for " + cachedFile.getAbsolutePath());
  }
  return (Document) Jsoup.parse(cachedFile, "UTF-8");
 }
         
 
 public Document getDocument(String url, int[] dldOption) throws Exception
 {
  int dldMethod = Math.max(1, dldOption[0]);
  Document ret = null;
  XhtUrl docUrl = new XhtUrl(url);  
  if ((!url.equals(lastUrl)) || (cachedFile == null)) cachedFile = cachedFile(docUrl);
  lastUrl = url; 
  
  if (cachedFile.exists())
  {
   dldOption[0] = 0;          //indicate that no download was necessary
   ret = (Document) Jsoup.parse(cachedFile, "UTF-8");
   System.out.println("found cache file " + cachedFile(docUrl));
  }
  else
  {
   long sleepTime = recenDownloadTimeMillis + ctl.download_Wait() - System.currentTimeMillis();
   if (sleepTime > 0) Thread.sleep(Math.max(0, recenDownloadTimeMillis + ctl.download_Wait() - System.currentTimeMillis()));
   recenDownloadTimeMillis = System.currentTimeMillis();

   for (int i = 1; i <= ctl.webExcp_tryUrl() ; i++)
   {

    try
    {
     if (url.startsWith("ytsearch://")) ret = dldYtSearch(url);
     else
     switch(dldOption[0])
     {
 
      case 0: ret = dldWithJsoup    (docUrl); break;
      case 1: ret = dldWithJsoup    (docUrl); break;
      case 2: ret = dldWithWebView  (docUrl); break;
      case 3: ret = dldWithAhkOpera (docUrl); break;
     }
     write(cachedFile, ret.toString());   
     ret = (Document) Jsoup.parse(cachedFile, "UTF-8");
     System.out.println("downloaded " + cachedFile(docUrl));
     break;
    }
    catch (Exception ex)
    {
     System.out.println("download #" + i + " failed for " + cachedFile(docUrl) + "(" + ex.getMessage() + ")");
     if (++ctl.excpCount > ctl.webExcp_tryMax()) throw new Exception("Break downloads because more than " + ctl.webExcp_tryMax() + " downloads failed.");
     Thread.sleep(ctl.webExcp_Pause());
     if (ctl.webExcp_cacheTrace()) write(cachedFile, "<html><!--  The html Content of this Document could not be downloaded - possibly due to a timeOut Error  --><body></body></html>");   
     ret = (Document) Jsoup.parse("<html><body></body></html>");
    }
    
    
   }
  }  
  return ret;
 }

 public File getFile(String url) throws Exception
 {
  if (!cachedFile(new XhtUrl(url)).exists()) throw new Exception("XhtCache File does not exist in Filesystem");
  return cachedFile(new XhtUrl(url));
 }
 
 public String cleanedUrl(String url) throws Exception
 {
  return new XhtUrl(url).toString();
 }
 
}





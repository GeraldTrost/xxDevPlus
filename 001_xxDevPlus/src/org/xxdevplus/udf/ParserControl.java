

package org.xxdevplus.udf;

import org.xxdevplus.data.FtxProvider;
import org.xxdevplus.struct.Pile;
import org.xxdevplus.sys.StringVal;

public class ParserControl 
{
 
 public  int         excpCount           =       0;
 private boolean     processCacheHit     =    true;
 private boolean     processCacheMiss    =    true;
 private StringVal   inspector           =    null;

 // Defaults
 private long        download_Wait       =       0;  // wait between downloads
 private long        webExcp_Pause       =   60000;  // Wait after exception
 private int         webExcp_tryUrl      =       3;  // Three retries for one url
 private int         webExcp_tryMax      =      20;  // 20 retries for all urls   ///  ACTUNG NUR WENN DER VISITOR DIE AÜSSERE SCHLEIFE MACHT, sonst selbst implementiere
 private boolean     webExcp_cacheTrace  =   false;  // leave no dummy file
 public  long        inBehalfOf          =       0;
 public  FtxProvider ftxProvider         =    null;
         
 Pile<XhtReasoner> listeners = new Pile<XhtReasoner>();
 
 public ParserControl(long inBehalfOf, FtxProvider ftxProvider, XhtReasoner ... listeners) throws Exception  
 {
  this.inBehalfOf   =                          inBehalfOf;
  this.ftxProvider  =                         ftxProvider; 
  this.listeners    = new Pile<XhtReasoner>(0, listeners);
 }
 
 public boolean   processCacheHit  () { return processCacheHit   ; }      public void processCacheHit  (boolean newVal)                  { processCacheHit  = newVal; }
 public boolean   processCacheMiss () { return processCacheMiss  ; }      public void processCacheMiss (boolean newVal)                  { processCacheMiss = newVal; }
 public StringVal inspector        () { return inspector         ; }      public void inspector        (String  newVal) throws Exception { inspector = new StringVal(newVal, XhtParser.class); }

 
 
 public void netBehaviour(long wait, long pause, int tryUrl, int tryMax, boolean cacheTrace)
 {
  download_Wait       =           wait;
  webExcp_Pause       =          pause;
  webExcp_tryUrl      =         tryUrl;
  webExcp_tryMax      =         tryMax;
  webExcp_cacheTrace  =     cacheTrace;
 }

 public long     download_Wait()       { return download_Wait;      }
 public long     webExcp_Pause()       { return webExcp_Pause;      }
 public int      webExcp_tryUrl()      { return webExcp_tryUrl;     }
 public int      webExcp_tryMax()      { return webExcp_tryMax;     }
 public boolean  webExcp_cacheTrace()  { return webExcp_cacheTrace; }

 
 boolean dispatch(int event, XhtParser parser) throws Exception
 {
  for (XhtReasoner listener : listeners)
  {
   switch (event)
   {
    case 1:
     if (!listener.beforeDownload   (parser)) return false;   break;
    case -1: 
     if (!listener.afterDownload    (parser)) return false;   break;
    case 2: 
     if (!listener.beforeSelections (parser)) return false;   break;
    case -2: 
     if (!listener.afterSelections  (parser)) return false;   break;
    case 3: 
     if (!listener.beforeSelection  (parser)) return false;   break;
    case -3: 
     if (!listener.afterSelection   (parser)) return false;   break;
    case 4: 
     if (!listener.beforeTriples    (parser)) return false;   break;
    case -4: 
     if (!listener.afterTriples     (parser)) return false;   break;
   }
  }
  return true;  
 }
 
}








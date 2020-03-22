

package org.xxdevplus.udf;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.net.URLStreamHandler;
import java.net.URLStreamHandlerFactory;
import org.xxdevplus.chain.Chain;
import org.xxdevplus.frmlng.Pick;
import org.xxdevplus.frmlng.Zone;
import org.xxdevplus.struct.Pile;
import org.xxdevplus.utl.utl;




public class XhtUrl
{

 //** @author GeTr Copyright© 1996-2020 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties, You may use, modify, distribute this code under GNU public dual licence free for NON-Commercial use as long as this notice remains unchanged.
 //** @comment Pile is like List<typ> but counts from 1 and also has negative counting from the end backwards
 static int _ic;String ass(boolean xp,String...msg)throws Exception{if(!xp)throw new Exception(ass(true)+" slfTst:"+msg);return "XhtUrl";}private void slfTst()throws Exception{tstSimple();}protected void init()throws Exception{if(_ic++==0)slfTst();}
 
 private void tstSimple() 
 {
  
  try 
  {
   URL.setURLStreamHandlerFactory
   (
    new URLStreamHandlerFactory() 
    {
     public URLStreamHandler createURLStreamHandler(String protocol) 
     {
      return "ytsearch".equals(protocol) ? new URLStreamHandler() 
      {
       protected URLConnection openConnection(URL url) throws IOException 
       {
        return new URLConnection(url) 
        {
         public void connect() throws IOException 
         {
          System.out.println("ytsearch Connected!");
         }
        };
       }
      } 
     : 
      null;
     }
    }
   );
  }  
  catch (Exception ex) 
  {
   ex = ex;
  }

 }
 
 private URL origUrl      = null;
 private URL url4fs     = null;

 public static String fileSystemENC           = "fs";
 public static String urlENC                  = "url";
 public static String xmlENC                  = "xml";
  
 public static String defaultSIG              = "{Fi}{.{Fe}}{?{Qu}}{#{An}}";
 public static String completeSIG             = "{{Pr}://}{{Un}{:{Up}}@}{{Hs}.}{{Hd}.}{He}{:{Po}}{/{Pa}}{/{Fi}}{.{Fe}}{?{Qu}}{#{An}}";
 public static String fileExtSIG              = "{Fi}{.{Fe}}";
 public static String fullPathSIG             = "{Pa}/{Fi}.{Fe}";
 public static String reversePathSIG          = "{P2}/{P1}";
 
 public String protocol  ()                   { return utl.notNull(url4fs.getProtocol());                                                            }
 public String host      () throws Exception  { return new Chain(utl.notNull(url4fs.getHost())).text();                                              }                                                   
 public String hsrv      () throws Exception  { return new Chain(utl.notNull(url4fs.getHost())).before(-2, ".").text();                              }                                                   
 public String hdom      () throws Exception  { return new Chain(utl.notNull(url4fs.getHost())).after(-2, ".").before(-1, ".").text();               }
 public String hext      () throws Exception  { return new Chain(utl.notNull(url4fs.getHost())).after(-1, ".").text();                               }
 public String port      ()                   { if (url4fs.getPort() == -1) return "";   return "" + url4fs.getPort();                                  }
 public String username  () throws Exception  { return new Chain(utl.notNull(url4fs.getUserInfo())).before(1, ":").text();                           }
 public String userpwd   () throws Exception  { return new Chain(utl.notNull(url4fs.getUserInfo())).after(1,":").text();                             }
 public String path      () throws Exception  { return new Chain(utl.notNull(url4fs.getPath())).before(-1, "/").from(2).text();                      }

 public String fname      () throws Exception  
 { 
  Chain ret = new Chain(utl.notNull(url4fs.getPath())).after(-1, "/");
  if (url4fs.toString().endsWith("?")) ret = ret.plus("%3F");
  if (ret.at(1, ".").len() == 0) return ret.text(); return ret.before(-1, ".").text();
 }
 
 public String fext () throws Exception  
 { 
  Chain ret = new Chain(utl.notNull(url4fs.getPath())).after(2 + path().length());
  if (ret.at(1, ".").len() == 0) return ""; return ret.after(-1, ".").text();
 }

 public String file      () throws Exception  
 { 
  if (fext().length() == 0) return fname();
  return fname() + "." + fext();
 }

 public String query     ()                   { return utl.notNull(url4fs.getQuery());                                                               }
 public String anchor    ()                   { return utl.notNull(url4fs.getRef());                                                                 }
 

 private Chain Part(Chain Key, Chain selector) throws Exception
 {
  return Part(Key).upto(Integer.parseInt(selector.after(1, "..").text())).from(Integer.parseInt(selector.before(1, "..").text()));          
 }

 private Chain Part(Chain Key) throws Exception
 {
  if (Key.len() < 2) return Key;
  if (Key.from(1, "[").at(1, "..").len() > 0) return Part(Key.before(1, "["), Key.after(1, "[").before(1, "]"));
  String key = Key.text();  
  if ((key.charAt(0) == 'P') && (key.charAt(1) != 'r') && (key.charAt(1) != 'o') && (key.charAt(1) != 'a')) return new Chain("/" + path()).after(Integer.parseInt(key.substring(1)), "/").before(1, "/");
  if ((key.charAt(0) == 'q') && (key.charAt(1) != 'u')) return new Chain("&" + query()).after(Integer.parseInt(key.substring(1)), "&").before(1, "&");
  int hash     = 256 * key.charAt(0) + key.charAt(1);
  switch (hash)
  {
   case 20594    : return new Chain(protocol()   );     
   case 18543    : return new Chain(host()       );     
   case 18547    : return new Chain(hsrv()       );     
   case 18532    : return new Chain(hdom()       );     
   case 18533    : return new Chain(hext()       );     
   case 20591    : return new Chain(port()       );     
   case 21870    : return new Chain(username()   );     
   case 21872    : return new Chain(userpwd()    );     
   case 20577    : return new Chain(path()       );     
   case 18025    : return new Chain(file()       );     
   case 18030    : return new Chain(fname()       );     
   case 18021    : return new Chain(fext()  );     
   case 20853    : return new Chain(query()      );     
   case 16750    : return new Chain(anchor()     );     
   default       : return Key;
  }
 }

 private Chain Part(Chain Key, String encoding) throws Exception
 {
  Chain ret = Part(Key);
  if (encoding.equals(""))     return ret;
  if (encoding.equals("fs"))   return new Chain(utl.fsEncode(ret.text()));      //return new Chain(utl.validFileNameFromUrl(ret.text()));
  if (encoding.equals("url"))  return new Chain(URLEncoder.encode(ret.text(), "UTF-8"));
  if (encoding.equals("xml"))  ret = new Chain(utl.toXml(ret.text(), true));
  return ret;
 }

 private Chain signature (Chain sig, boolean bracketed, String encoding) throws Exception  
 {
  if (sig.at("{").len() == 0) if (bracketed) return Part(sig, encoding); else return sig;
  Pile<Chain> optr = new Pile<Chain>();
  Pile<Chain> opnd = new Pile<Chain>();
  Pile<Chain> res  = new Pile<Chain>();
  while (sig.len() > 0)
  {   
   opnd.Push(Pick.sglCrlys.host(Pick.sglCrlys).grabOne().upon(sig));
   
   if ((opnd.Len() == 0) || (opnd.g(-1).len() == 0)) 
   {
    optr.Push(sig); 
    sig = Chain.Empty;
   } 
   else 
   {
    optr.Push(sig.before(opnd.g(-1))); 
    sig = sig.after(opnd.g(-1));
   }
  }
  for (Chain opd : opnd) res.Push(signature(Pick.sglCrlys.host(Pick.sglCrlys).grabOne().upon(opd).from(2).upto(-2), true, encoding));
  Chain ret = Chain.Empty;
  if (!bracketed || (res.csv("", "", "").length() > 0)) for (int i = 1; i <= optr.Len(); i++) ret = ret.plus(optr.g(i)).plus(res.g(i));
  return ret;
 } 
 
 public String signature (String PrUnUpHoHsHdHePoPaFiExQuAn_SIGNATURE) throws Exception  //Protocol, Username, UserPwd, Host, HostServer, HostDomain, Hostextension, Port, Path, File, extension, query, anchor
 {
  String encoding = "";
  Chain sig = new Chain(PrUnUpHoHsHdHePoPaFiExQuAn_SIGNATURE);
  if (sig.startsWith("((")) 
  {
   encoding = sig.before(1, "))").after(2).Trim().text();
   sig = sig.after(1, "))").Trim();
  }
  return signature (sig.Trim(), false, encoding).text();  
 }

 public String signature () throws Exception { return signature (XhtUrl.defaultSIG); }
   
 public XhtUrl(String url) throws Exception
 {
  init();
  url = url.trim(); if (url.endsWith("/")) url = url.substring(0, url.length() - 1);
  this.origUrl  = new URL(url);
  if (url.contains("index.php?title=Category:"))
   this.url4fs = this.origUrl; 
  else 
   this.url4fs = new URL(URLDecoder.decode(url.replace("%", ">>percent<<").replace("+", ">>plus<<"), "UTF-8").replace(">>percent<<", "%").replace(">>plus<<", "+"));
 }
 
 @Override
 public String toString() { return origUrl.toString(); }
 
}

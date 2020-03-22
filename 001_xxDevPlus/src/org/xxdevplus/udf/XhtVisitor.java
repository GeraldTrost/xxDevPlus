



package org.xxdevplus.udf;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import org.xxdevplus.chain.Chain;
import org.xxdevplus.data.Db;
import org.xxdevplus.data.DbMsDataReader;
import org.xxdevplus.struct.KeyPile;
import org.xxdevplus.struct.Pile;
import org.xxdevplus.struct.Triple;
import org.xxdevplus.struct.Triples;
import org.xxdevplus.data.TripleStore;

public class XhtVisitor 
{

 public static Chain storeDb = Chain.Empty;
 
 private                    TripleStore  tplStore               =                                   null ;
 private                         String  defaultStringMethClass =                                     "" ;
 private                         String  cacheBaseFolder        =                                     "" ;
 private                         String  cacheFileNamePattern   =                                     "" ;
 private                           long  inBehalfOf             =                                      0 ; // inBehalfOf = some mandantId or clientId or AppId or (in like in everest) some domainId
 private   KeyPile<String, Pile<Chain>>  kVals                  =   new KeyPile<String, Pile<Chain>>  () ;
 private                  ParserControl  ctl                    =                                   null ;
         
 public  ParserControl ctl() { return ctl; }       // void ctl(ParserControl newVal) { ctl = newVal; }

 
 /**
  * @param tplStore                 (extended) Db Objects implementing TripleStore and FtxProvider
  * @param inBehalfOf               some mandantId or clientId or AppId or (in like in everest) some domainId
  * @param defaultStringMethClass   fully qualified ClassName of a Class containing String Methods(String)
  * @param cacheBaseFolder          Folder containing locally cached Web Pages
  * @param cacheFileNamePattern     pattern for building Filenames for cached Web Pages
  */
 
 
 private void init(TripleStore tplStore, long inBehalfOf, String defaultStringMethClass, String cacheBaseFolder, String cacheFileNamePattern, KeyPile<String, Pile<Chain>> kVals, ParserControl ctl) throws Exception
 {
  if (storeDb == Chain.Empty) storeDb = new Chain("[db] ");
  this.tplStore               =                            tplStore;
  this.defaultStringMethClass =              defaultStringMethClass;
  this.cacheBaseFolder        =                     cacheBaseFolder;
  this.cacheFileNamePattern   =                cacheFileNamePattern;
  this.inBehalfOf             =                          inBehalfOf;
  if (kVals == null)    kVals = new KeyPile<String, Pile<Chain>> ();
  this.kVals                  =                               kVals;
  if (ctl == null)        ctl =         new ParserControl(0L, null);
  this.ctl                    =                                 ctl;
 }
 
 public XhtVisitor(TripleStore tplStore, long inBehalfOf, String defaultStringMethClass, String cacheBaseFolder, String cacheFileNamePattern, KeyPile<String, Pile<Chain>> kVals, ParserControl ctl) throws Exception
 {
  init(tplStore, inBehalfOf, defaultStringMethClass, cacheBaseFolder, cacheFileNamePattern, kVals, ctl);
 }

 public XhtVisitor(TripleStore tplStore, long inBehalfOf, String defaultStringMethClass, String cacheBaseFolder, String cacheFileNamePattern, String[][] kValsArray, ParserControl ctl) throws Exception
 {
  init(tplStore, inBehalfOf, defaultStringMethClass, cacheBaseFolder, cacheFileNamePattern, null, ctl);
  for (int i = 1; i <= kValsArray.length; i++) 
  {
   Pile<Chain> vals = new Pile<Chain>();
   for (int j = 2; j <= kValsArray[i - 1].length ; j++) vals.Add(new Chain(kValsArray[i - 1][j - 1]));
   this.kVals.Add(kValsArray[i - 1][0], vals); 
  }
 }

 public XhtVisitor(TripleStore tplStore, long inBehalfOf, String defaultStringMethClass, String cacheBaseFolder, String cacheFileNamePattern, String[] kValsArray, ParserControl ctl) throws Exception
 {
  init(tplStore, inBehalfOf, defaultStringMethClass, cacheBaseFolder, cacheFileNamePattern, null, ctl);
  for (int i = 1; i <= kValsArray.length; i++) 
  {
   this.kVals.Add(kValsArray[i - 1].substring(0, kValsArray[i - 1].indexOf("|")), new Pile<Chain>()); 
   Chain text = new Chain(kValsArray[i - 1]).after(1, "|");
   while (text.len() > 0)
   {
    this.kVals.g(-1).Push(text.before(1, "|"));
    text = text.after(1, "|");
   }
  }
 }

 public XhtVisitor(TripleStore tplStore, long inBehalfOf, String defaultStringMethClass, String cacheBaseFolder, String cacheFileNamePattern, String kValsSql, ParserControl ctl) throws Exception
 {
  init(tplStore, inBehalfOf, defaultStringMethClass, cacheBaseFolder, cacheFileNamePattern, null, ctl);
  this.kVals = new KeyPile<String, Pile<Chain>>();
  DbMsDataReader rdr = ((Db)tplStore).exec(kValsSql);
  while (rdr.Read()) 
  {
   if (!this.kVals.hasKey(rdr.GetString(1)))
   {
    if (!this.kVals.hasKey(rdr.GetString(1))) this.kVals.p(new Pile<Chain>(), rdr.GetString(1));
    int i = 2; while (i != 0) try { this.kVals.g(rdr.GetString(1)).Push(new Chain(rdr.GetString(i++))); } catch (Exception ex) { i = 0;}
   }
  }
 }

 /**
  * @param store        XhtVisitor.storeDb.plus(outFileName) OR XhtVisitor.storeDb OR outFileName OR ""
  * @param sqlJobDesc
  * @return
  * @throws Exception
  */
 public Triples visitPages(Chain store, String sqlJobDesc) throws Exception 
 {
  Triples                   ret = new Triples();
  XhtParser              pgParser = null;
  DbMsDataReader             rdr = ((Db)tplStore).exec(sqlJobDesc);
  String                  rctUrl = "";
  Chain                 rctPgCtx = Chain.Empty;
  BufferedOutputStream       out = null;
  Chain                  outFile = store;
  if (outFile.at(1, storeDb.text()).len() > 0) outFile = outFile.after(1, storeDb.text()).Trim();
  if (outFile.len() > 0) out = new BufferedOutputStream(new FileOutputStream(outFile.text(), true));
  int urlCtr = 0;
  int storedTripleCtr = 0;
  while (rdr.Read())
  {
   int dldOption = 1; 
   urlCtr++ ;
   Pile<Chain> selectors = new Pile<Chain>();   
   String             url = rdr.GetString(1);
   if (url.startsWith("(")) { dldOption = Integer.parseInt("" + url.charAt(1)); url = url.substring(3); }
   Chain            pgCtx = new Chain(rdr.GetString(7) + " , " + rdr.GetString(2)).trimC(" ,"); // add urlCtx to tplCtx
   Chain          sltrDef = new Chain(rdr.GetString(3)).from(2).upto(-2).Trim();
   String            lDef = rdr.GetString(4);
   String            mDef = rdr.GetString(5);
   String            rDef = rdr.GetString(6);
   while (sltrDef.len() > 0) 
   { 
    selectors.Push(sltrDef.before(1, ",").trimC(" \"")); 
    sltrDef = sltrDef.after(1, ",").Trim(); 
   } 
   if ((selectors.g(1).len() == 0) && (selectors.Len() == 1)) selectors.Clear();   
   if ((!rctUrl.equals(url)) || (!rctPgCtx.equals(pgCtx.text()))) 
   { 
    try                  { pgParser = new XhtParser("(" + dldOption + ")" + url, pgCtx.text(), cacheBaseFolder, cacheFileNamePattern, defaultStringMethClass, kVals, ctl);  }
    catch (Exception ex) { ex.printStackTrace(); continue; }
    rctUrl = url; rctPgCtx = pgCtx; 
   }
   Triples res = pgParser.select(selectors).triples(lDef, mDef, rDef);
   ret.Push(res);
   if (store.equals(Chain.Empty)) 
    System.out.println("found " + res.Len() + " Triples like ( " + lDef + " - " + mDef + " - " + rDef + " )"); 
   else
    if (store.startsWith(storeDb.text())) 
    {
     Triples toStore = res.cumulate().unique();
     ((TripleStore)tplStore).Add(toStore, inBehalfOf); // store Triples in Db after cumulation of Sum/Avg-Attributes starting with + or ~ and thereafter purging all duplicates
     storedTripleCtr += toStore.Len();
     //System.out.println("stored " + res.Len() + " Triples"); 
     //System.out.println(res.toString(" | ")); 
    }
   if (out != null) for( Triple fact: res) fact.write(out, false);
  
   //if (storedTripleCtr > 0) System.out.println("storec " + storedTripleCtr + " Triples.");
   storedTripleCtr = 0;
  }
  rdr.Close();
  if (out != null) out.close();
  return ret;
 }

 public Triples visitPages(Chain store, String sqlTplDefs, String sqlSelectors, String sqlUrlsDesc) throws Exception 
 {
  String sqlJobDesc          = 
  "SELECT * FROM\n"          +
  "(\n"                      + 
  " \n" + sqlUrlsDesc        + "\n"+ 
  ") AS urldefs\n"           +
  ",\n"                      +
  "(\n" + sqlSelectors       + "\n"+ 
  ") AS selectors\n"         +
  ",\n"                      +
  "(\n" + sqlTplDefs         + "\n"+ 
  ") AS triples\n"           +
   "ORDER BY 1, 2, 4, 5, 6;";
  
  return visitPages(store, sqlJobDesc);
 }

 public Triples visitPages(Chain store, String[][] tplDefsArr, String sqlSelectors, String sqlUrlsDesc) throws Exception 
 {
  return visitPages(store, ((Db)tplStore).sql(tplDefsArr, "", 0).text(), sqlSelectors, sqlUrlsDesc);
 }

 public Triples visitPages(Chain store, String[] tplDefs, String sqlSelectors, String sqlUrlsDesc) throws Exception 
 {
  return visitPages(store, ((Db)tplStore).sql(tplDefs, "|", "", 0).text(), sqlSelectors, sqlUrlsDesc);
 }

 public Triples visitPages(Chain store, String sqlTplDefs, String[][] selectors, String sqlUrlsDesc) throws Exception 
 {
  return visitPages(store, sqlTplDefs, ((Db)tplStore).sql(selectors, "", 1).text(), sqlUrlsDesc);
 }

 public Triples visitPages(Chain store, String sqlTplDefs, String[] selectors, String sqlUrlsDesc) throws Exception 
 {
  return visitPages(store, sqlTplDefs, ((Db)tplStore).sql(selectors, "|", "", 1).text(), sqlUrlsDesc);
 }

 public Triples visitPages(Chain store, String sqlTplDefs, String sqlSelectors, String[][] urlsDescArr) throws Exception 
 {
  return visitPages(store, sqlTplDefs, sqlSelectors, ((Db)tplStore).sql(urlsDescArr, "", 0).text());
 }

 public Triples visitPages(Chain store, String sqlTplDefs, String sqlSelectors, KeyPile<String, String> urlsDesc) throws Exception 
 {
  String[][] urlsDescArr = new String[urlsDesc.Len()][2];
  for (int i = 1; i <= urlsDesc.Len(); i++) { urlsDescArr[i - 1][0] = urlsDesc.Keys().g(i); urlsDescArr[i - 1][1] = urlsDesc.g(urlsDesc.Keys().g(i)); }
  return visitPages(store, sqlTplDefs, sqlSelectors, urlsDescArr);
 }

 public Triples visitPages(Chain store, String sqlTplDefs, String sqlSelectors, String[] urlsDesc) throws Exception 
 {
  return visitPages(store, sqlTplDefs, sqlSelectors, ((Db)tplStore).sql(urlsDesc, "|", "", 0).text());
 }

 public Triples visitPages(Chain store, String[][] tplDefs, String[][] selectors, String sqlUrlsDesc) throws Exception 
 {
  return visitPages(store, ((Db)tplStore).sql(tplDefs, "", 0).text(), ((Db)tplStore).sql(selectors, "", 1).text(), sqlUrlsDesc);
 }

 public Triples visitPages(Chain store, String[][] tplDefs, String[] selectors, String sqlUrlsDesc) throws Exception 
 {
  return visitPages(store, ((Db)tplStore).sql(tplDefs, "", 0).text(), ((Db)tplStore).sql(selectors, "|", "", 1).text(), sqlUrlsDesc);
 }

 public Triples visitPages(Chain store, String[] tplDefs, String[][] selectors, String sqlUrlsDesc) throws Exception 
 {
  return visitPages(store, ((Db)tplStore).sql(tplDefs, "|", "", 0).text(), ((Db)tplStore).sql(selectors, "", 1).text(), sqlUrlsDesc);
 }
  
 public Triples visitPages(Chain store, String[] tplDefs, String[] selectors, String sqlUrlsDesc) throws Exception 
 {
  return visitPages(store, ((Db)tplStore).sql(tplDefs, "|", "", 0).text(), ((Db)tplStore).sql(selectors, "|", "", 1).text(), sqlUrlsDesc);
 }

 public Triples visitPages(Chain store, String[][] tplDefs, String sqlSelectors, String[][] urlsDescArr) throws Exception 
 {
  return visitPages(store, ((Db)tplStore).sql(tplDefs, "", 0).text(), sqlSelectors, ((Db)tplStore).sql(urlsDescArr, "", 0).text());
 }

 public Triples visitPages(Chain store, String[][] tplDefs, String sqlSelectors, KeyPile<String, String> urlsDesc) throws Exception 
 {
  String[][] urlsDescArr = new String[urlsDesc.Len()][2];
  for (int i = 1; i <= urlsDesc.Len(); i++) { urlsDescArr[i - 1][0] = urlsDesc.Keys().g(i); urlsDescArr[i - 1][1] = urlsDesc.g(urlsDesc.Keys().g(i)); }
  return visitPages(store, ((Db)tplStore).sql(tplDefs, "", 0).text(), sqlSelectors, urlsDescArr);
 }
 
 public Triples visitPages(Chain store, String[][] tplDefs, String sqlSelectors, String[] urlsDesc) throws Exception 
 {
  return visitPages(store, ((Db)tplStore).sql(tplDefs, "", 0).text(), sqlSelectors, ((Db)tplStore).sql(urlsDesc, "|", "", 0).text());
 }

 public Triples visitPages(Chain store, String[] tplDefs, String sqlSelectors, String[][] urlsDescArr) throws Exception 
 {
  return visitPages(store, ((Db)tplStore).sql(tplDefs, "|", "", 0).text(), sqlSelectors, ((Db)tplStore).sql(urlsDescArr, "", 0).text());
 }

 public Triples visitPages(Chain store, String[] tplDefs, String sqlSelectors, KeyPile<String, String> urlsDesc) throws Exception 
 {
  String[][] urlsDescArr = new String[urlsDesc.Len()][2];
  for (int i = 1; i <= urlsDesc.Len(); i++) { urlsDescArr[i - 1][0] = urlsDesc.Keys().g(i); urlsDescArr[i - 1][1] = urlsDesc.g(urlsDesc.Keys().g(i)); }
  return visitPages(store, ((Db)tplStore).sql(tplDefs, "|", "", 0).text(), sqlSelectors, urlsDescArr);
 }

 public Triples visitPages(Chain store, String[] tplDefs, String sqlSelectors, String[] urlsDesc) throws Exception 
 {
  return visitPages(store, ((Db)tplStore).sql(tplDefs, "|", "", 0).text(), sqlSelectors, ((Db)tplStore).sql(urlsDesc, "|", "", 0).text());
 }

 public Triples visitPages(Chain store, String sqlTplDefs, String[][] selectors, String[][] urlsDescArr) throws Exception 
 {
  return visitPages(store, sqlTplDefs, ((Db)tplStore).sql(selectors, "", 1).text(), ((Db)tplStore).sql(urlsDescArr, "", 0).text());
 }

 public Triples visitPages(Chain store, String sqlTplDefs, String[][] selectors, KeyPile<String, String> urlsDesc) throws Exception 
 {
  String[][] urlsDescArr = new String[urlsDesc.Len()][2];
  for (int i = 1; i <= urlsDesc.Len(); i++) { urlsDescArr[i - 1][0] = urlsDesc.Keys().g(i); urlsDescArr[i - 1][1] = urlsDesc.g(urlsDesc.Keys().g(i)); }
  return visitPages(store, sqlTplDefs, ((Db)tplStore).sql(selectors, "", 1).text(), urlsDescArr);
 }

 public Triples visitPages(Chain store, String sqlTplDefs, String[][] selectors, String[] urlsDesc) throws Exception 
 {
  return visitPages(store, sqlTplDefs, ((Db)tplStore).sql(selectors, "", 1).text(), ((Db)tplStore).sql(urlsDesc, "|", "", 0).text());
 }

 public Triples visitPages(Chain store, String sqlTplDefs, String[] selectors, String[][] urlsDescArr) throws Exception 
 {
  return visitPages(store, sqlTplDefs, ((Db)tplStore).sql(selectors, "|", "", 1).text(), ((Db)tplStore).sql(urlsDescArr, "", 0).text());
 }

 public Triples visitPages(Chain store, String sqlTplDefs, String[] selectors, KeyPile<String, String> urlsDesc) throws Exception 
 {
  String[][] urlsDescArr = new String[urlsDesc.Len()][2];
  for (int i = 1; i <= urlsDesc.Len(); i++) { urlsDescArr[i - 1][0] = urlsDesc.Keys().g(i); urlsDescArr[i - 1][1] = urlsDesc.g(urlsDesc.Keys().g(i)); }
  return visitPages(store, sqlTplDefs, ((Db)tplStore).sql(selectors, "|", "", 1).text(), urlsDescArr);
 }

 public Triples visitPages(Chain store, String sqlTplDefs, String[] selectors, String[] urlsDesc) throws Exception 
 {
  return visitPages(store, sqlTplDefs, ((Db)tplStore).sql(selectors, "|", "", 1).text(), ((Db)tplStore).sql(urlsDesc, "|", "", 0).text());
 }

 public Triples visitPages(Chain store, String[][] tplDefs, String[][] selectors, String[][] urlsDescArr) throws Exception 
 {
  return visitPages(store, ((Db)tplStore).sql(tplDefs, "", 0).text(), ((Db)tplStore).sql(selectors, "", 1).text(), ((Db)tplStore).sql(urlsDescArr, "", 0).text());
 }

 public Triples visitPages(Chain store, String[][] tplDefs, String[][] selectors, KeyPile<String, String> urlsDesc) throws Exception 
 {
  String[][] urlsDescArr = new String[urlsDesc.Len()][2];
  for (int i = 1; i <= urlsDesc.Len(); i++) { urlsDescArr[i - 1][0] = urlsDesc.Keys().g(i); urlsDescArr[i - 1][1] = urlsDesc.g(urlsDesc.Keys().g(i)); }
  return visitPages(store, ((Db)tplStore).sql(tplDefs, "", 0).text(), ((Db)tplStore).sql(selectors, "", 1).text(), urlsDescArr);
 }
 
 public Triples visitPages(Chain store, String[][] tplDefs, String[] selectors, String[] urlsDesc) throws Exception 
 {
  return visitPages(store, ((Db)tplStore).sql(tplDefs, "", 0).text(), ((Db)tplStore).sql(selectors, "|", "", 1).text(), ((Db)tplStore).sql(urlsDesc, "|", "", 0).text());
 }
 public Triples visitPages(Chain store, String[] tplDefs, String[][] selectors, String[] urlsDesc) throws Exception 
 {
  return visitPages(store, ((Db)tplStore).sql(tplDefs, "|", "", 0).text(), ((Db)tplStore).sql(selectors, "", 1).text(), ((Db)tplStore).sql(urlsDesc, "|", "", 0).text());
 }

 public Triples visitPages(Chain store, String[] tplDefs, String[] selectors, String[][] urlsDescArr) throws Exception 
 {
  return visitPages(store, ((Db)tplStore).sql(tplDefs, "|", "", 0).text(), ((Db)tplStore).sql(selectors, "|", "", 1).text(), ((Db)tplStore).sql(urlsDescArr, "", 0).text());
 }

 public Triples visitPages(Chain store, String[] tplDefs, String[] selectors, KeyPile<String, String> urlsDesc) throws Exception 
 {
  String[][] urlsDescArr = new String[urlsDesc.Len()][2];
  for (int i = 1; i <= urlsDesc.Len(); i++) { urlsDescArr[i - 1][0] = urlsDesc.Keys().g(i); urlsDescArr[i - 1][1] = urlsDesc.g(urlsDesc.Keys().g(i)); }
  return visitPages(store, ((Db)tplStore).sql(tplDefs, "|", "", 0).text(), ((Db)tplStore).sql(selectors, "|", "", 1).text(), urlsDescArr);
 }

 public Triples visitPages(Chain store, String[] tplDefs, String[][] selectors, String[][] urlsDescArr) throws Exception 
 {
  return visitPages(store, ((Db)tplStore).sql(tplDefs, "|", "", 0).text(), ((Db)tplStore).sql(selectors, "", 1).text(), ((Db)tplStore).sql(urlsDescArr, "", 0).text());
 }

 public Triples visitPages(Chain store, String[] tplDefs, String[][] selectors, KeyPile<String, String> urlsDesc) throws Exception 
 {
  String[][] urlsDescArr = new String[urlsDesc.Len()][2];
  for (int i = 1; i <= urlsDesc.Len(); i++) { urlsDescArr[i - 1][0] = urlsDesc.Keys().g(i); urlsDescArr[i - 1][1] = urlsDesc.g(urlsDesc.Keys().g(i)); }
  return visitPages(store, ((Db)tplStore).sql(tplDefs, "|", "", 0).text(), ((Db)tplStore).sql(selectors, "", 1).text(), urlsDescArr);
 }

 public Triples visitPages(Chain store, String[][] tplDefs, String[] selectors, String[][] urlsDescArr) throws Exception 
 {
  return visitPages(store, ((Db)tplStore).sql(tplDefs, "", 0).text(), ((Db)tplStore).sql(selectors, "|", "", 1).text(), ((Db)tplStore).sql(urlsDescArr, "", 0).text());
 }

 public Triples visitPages(Chain store, String[][] tplDefs, String[] selectors, KeyPile<String, String> urlsDesc) throws Exception 
 {
  String[][] urlsDescArr = new String[urlsDesc.Len()][2];
  for (int i = 1; i <= urlsDesc.Len(); i++) { urlsDescArr[i - 1][0] = urlsDesc.Keys().g(i); urlsDescArr[i - 1][1] = urlsDesc.g(urlsDesc.Keys().g(i)); }
  return visitPages(store, ((Db)tplStore).sql(tplDefs, "", 0).text(), ((Db)tplStore).sql(selectors, "|", "", 1).text(), urlsDescArr);
 }

 public Triples visitPages(Chain store, String[][] tplDefs, String[][] selectors, String[] urlsDesc) throws Exception 
 {
  return visitPages(store, ((Db)tplStore).sql(tplDefs, "", 0).text(), ((Db)tplStore).sql(selectors, "", 1).text(), ((Db)tplStore).sql(urlsDesc, "|", "", 0).text());
 }
 
 public Triples visitPages(Chain store, String[] tplDefs, String[] selectors, String[] urlsDesc) throws Exception 
 {
  return visitPages(store, ((Db)tplStore).sql(tplDefs, "|", "", 0).text(), ((Db)tplStore).sql(selectors, "|", "", 1).text(), ((Db)tplStore).sql(urlsDesc, "|", "", 0).text());
 }

 public Triples visitPages(String store, String[][] tplDefsArr, String sqlSelectors, String sqlUrlsDesc) throws Exception 
 {
  return visitPages(new Chain(store), ((Db)tplStore).sql(tplDefsArr, "", 0).text(), sqlSelectors, sqlUrlsDesc);
 }

 public Triples visitPages(String store, String[] tplDefs, String sqlSelectors, String sqlUrlsDesc) throws Exception 
 {
  return visitPages(new Chain(store), ((Db)tplStore).sql(tplDefs, "|", "", 0).text(), sqlSelectors, sqlUrlsDesc);
 }

 public Triples visitPages(String store, String sqlTplDefs, String[][] selectors, String sqlUrlsDesc) throws Exception 
 {
  return visitPages(new Chain(store), sqlTplDefs, ((Db)tplStore).sql(selectors, "", 1).text(), sqlUrlsDesc);
 }

 public Triples visitPages(String store, String sqlTplDefs, String[] selectors, String sqlUrlsDesc) throws Exception 
 {
  return visitPages(new Chain(store), sqlTplDefs, ((Db)tplStore).sql(selectors, "|", "", 1).text(), sqlUrlsDesc);
 }

 public Triples visitPages(String store, String sqlTplDefs, String sqlSelectors, String[][] urlsDescArr) throws Exception 
 {
  return visitPages(new Chain(store), sqlTplDefs, sqlSelectors, ((Db)tplStore).sql(urlsDescArr, "", 0).text());
 }

 public Triples visitPages(String store, String sqlTplDefs, String sqlSelectors, KeyPile<String, String> urlsDesc) throws Exception 
 {
  String[][] urlsDescArr = new String[urlsDesc.Len()][2];
  for (int i = 1; i <= urlsDesc.Len(); i++) { urlsDescArr[i - 1][0] = urlsDesc.Keys().g(i); urlsDescArr[i - 1][1] = urlsDesc.g(urlsDesc.Keys().g(i)); }
  return visitPages(new Chain(store), sqlTplDefs, sqlSelectors, urlsDescArr);
 }

 public Triples visitPages(String store, String sqlTplDefs, String sqlSelectors, String[] urlsDesc) throws Exception 
 {
  return visitPages(new Chain(store), sqlTplDefs, sqlSelectors, ((Db)tplStore).sql(urlsDesc, "|", "", 0).text());
 }

 public Triples visitPages(String store, String[][] tplDefs, String[][] selectors, String sqlUrlsDesc) throws Exception 
 {
  return visitPages(new Chain(store), ((Db)tplStore).sql(tplDefs, "", 0).text(), ((Db)tplStore).sql(selectors, "", 1).text(), sqlUrlsDesc);
 }

 public Triples visitPages(String store, String[][] tplDefs, String[] selectors, String sqlUrlsDesc) throws Exception 
 {
  return visitPages(new Chain(store), ((Db)tplStore).sql(tplDefs, "", 0).text(), ((Db)tplStore).sql(selectors, "|", "", 1).text(), sqlUrlsDesc);
 }

 public Triples visitPages(String store, String[] tplDefs, String[][] selectors, String sqlUrlsDesc) throws Exception 
 {
  return visitPages(new Chain(store), ((Db)tplStore).sql(tplDefs, "|", "", 0).text(), ((Db)tplStore).sql(selectors, "", 1).text(), sqlUrlsDesc);
 }
  
 public Triples visitPages(String store, String[] tplDefs, String[] selectors, String sqlUrlsDesc) throws Exception 
 {
  return visitPages(new Chain(store), ((Db)tplStore).sql(tplDefs, "|", "", 0).text(), ((Db)tplStore).sql(selectors, "|", "", 1).text(), sqlUrlsDesc);
 }

 public Triples visitPages(String store, String[][] tplDefs, String sqlSelectors, String[][] urlsDescArr) throws Exception 
 {
  return visitPages(new Chain(store), ((Db)tplStore).sql(tplDefs, "", 0).text(), sqlSelectors, ((Db)tplStore).sql(urlsDescArr, "", 0).text());
 }

 public Triples visitPages(String store, String[][] tplDefs, String sqlSelectors, KeyPile<String, String> urlsDesc) throws Exception 
 {
  String[][] urlsDescArr = new String[urlsDesc.Len()][2];
  for (int i = 1; i <= urlsDesc.Len(); i++) { urlsDescArr[i - 1][0] = urlsDesc.Keys().g(i); urlsDescArr[i - 1][1] = urlsDesc.g(urlsDesc.Keys().g(i)); }
  return visitPages(new Chain(store), ((Db)tplStore).sql(tplDefs, "", 0).text(), sqlSelectors, urlsDescArr);
 }
 
 public Triples visitPages(String store, String[][] tplDefs, String sqlSelectors, String[] urlsDesc) throws Exception 
 {
  return visitPages(new Chain(store), ((Db)tplStore).sql(tplDefs, "", 0).text(), sqlSelectors, ((Db)tplStore).sql(urlsDesc, "|", "", 0).text());
 }

 public Triples visitPages(String store, String[] tplDefs, String sqlSelectors, String[][] urlsDescArr) throws Exception 
 {
  return visitPages(new Chain(store), ((Db)tplStore).sql(tplDefs, "|", "", 0).text(), sqlSelectors, ((Db)tplStore).sql(urlsDescArr, "", 0).text());
 }

 public Triples visitPages(String store, String[] tplDefs, String sqlSelectors, KeyPile<String, String> urlsDesc) throws Exception 
 {
  String[][] urlsDescArr = new String[urlsDesc.Len()][2];
  for (int i = 1; i <= urlsDesc.Len(); i++) { urlsDescArr[i - 1][0] = urlsDesc.Keys().g(i); urlsDescArr[i - 1][1] = urlsDesc.g(urlsDesc.Keys().g(i)); }
  return visitPages(new Chain(store), ((Db)tplStore).sql(tplDefs, "|", "", 0).text(), sqlSelectors, urlsDescArr);
 }

 public Triples visitPages(String store, String[] tplDefs, String sqlSelectors, String[] urlsDesc) throws Exception 
 {
  return visitPages(new Chain(store), ((Db)tplStore).sql(tplDefs, "|", "", 0).text(), sqlSelectors, ((Db)tplStore).sql(urlsDesc, "|", "", 0).text());
 }

 public Triples visitPages(String store, String sqlTplDefs, String[][] selectors, String[][] urlsDescArr) throws Exception 
 {
  return visitPages(new Chain(store), sqlTplDefs, ((Db)tplStore).sql(selectors, "", 1).text(), ((Db)tplStore).sql(urlsDescArr, "", 0).text());
 }

 public Triples visitPages(String store, String sqlTplDefs, String[][] selectors, KeyPile<String, String> urlsDesc) throws Exception 
 {
  String[][] urlsDescArr = new String[urlsDesc.Len()][2];
  for (int i = 1; i <= urlsDesc.Len(); i++) { urlsDescArr[i - 1][0] = urlsDesc.Keys().g(i); urlsDescArr[i - 1][1] = urlsDesc.g(urlsDesc.Keys().g(i)); }
  return visitPages(new Chain(store), sqlTplDefs, ((Db)tplStore).sql(selectors, "", 1).text(), urlsDescArr);
 }

 public Triples visitPages(String store, String sqlTplDefs, String[][] selectors, String[] urlsDesc) throws Exception 
 {
  return visitPages(new Chain(store), sqlTplDefs, ((Db)tplStore).sql(selectors, "", 1).text(), ((Db)tplStore).sql(urlsDesc, "|", "", 0).text());
 }

 public Triples visitPages(String store, String sqlTplDefs, String[] selectors, String[][] urlsDescArr) throws Exception 
 {
  return visitPages(new Chain(store), sqlTplDefs, ((Db)tplStore).sql(selectors, "|", "", 1).text(), ((Db)tplStore).sql(urlsDescArr, "", 0).text());
 }

 public Triples visitPages(String store, String sqlTplDefs, String[] selectors, KeyPile<String, String> urlsDesc) throws Exception 
 {
  String[][] urlsDescArr = new String[urlsDesc.Len()][2];
  for (int i = 1; i <= urlsDesc.Len(); i++) { urlsDescArr[i - 1][0] = urlsDesc.Keys().g(i); urlsDescArr[i - 1][1] = urlsDesc.g(urlsDesc.Keys().g(i)); }
  return visitPages(new Chain(store), sqlTplDefs, ((Db)tplStore).sql(selectors, "|", "", 1).text(), urlsDescArr);
 }

 public Triples visitPages(String store, String sqlTplDefs, String[] selectors, String[] urlsDesc) throws Exception 
 {
  return visitPages(new Chain(store), sqlTplDefs, ((Db)tplStore).sql(selectors, "|", "", 1).text(), ((Db)tplStore).sql(urlsDesc, "|", "", 0).text());
 }

 public Triples visitPages(String store, String[][] tplDefs, String[][] selectors, String[][] urlsDescArr) throws Exception 
 {
  return visitPages(new Chain(store), ((Db)tplStore).sql(tplDefs, "", 0).text(), ((Db)tplStore).sql(selectors, "", 1).text(), ((Db)tplStore).sql(urlsDescArr, "", 0).text());
 }

 public Triples visitPages(String store, String[][] tplDefs, String[][] selectors, KeyPile<String, String> urlsDesc) throws Exception 
 {
  String[][] urlsDescArr = new String[urlsDesc.Len()][2];
  for (int i = 1; i <= urlsDesc.Len(); i++) { urlsDescArr[i - 1][0] = urlsDesc.Keys().g(i); urlsDescArr[i - 1][1] = urlsDesc.g(urlsDesc.Keys().g(i)); }
  return visitPages(new Chain(store), ((Db)tplStore).sql(tplDefs, "", 0).text(), ((Db)tplStore).sql(selectors, "", 1).text(), urlsDescArr);
 }
 
 public Triples visitPages(String store, String[][] tplDefs, String[] selectors, String[] urlsDesc) throws Exception 
 {
  return visitPages(new Chain(store), ((Db)tplStore).sql(tplDefs, "", 0).text(), ((Db)tplStore).sql(selectors, "|", "", 1).text(), ((Db)tplStore).sql(urlsDesc, "|", "", 0).text());
 }
 public Triples visitPages(String store, String[] tplDefs, String[][] selectors, String[] urlsDesc) throws Exception 
 {
  return visitPages(new Chain(store), ((Db)tplStore).sql(tplDefs, "|", "", 0).text(), ((Db)tplStore).sql(selectors, "", 1).text(), ((Db)tplStore).sql(urlsDesc, "|", "", 0).text());
 }

 public Triples visitPages(String store, String[] tplDefs, String[] selectors, String[][] urlsDescArr) throws Exception 
 {
  return visitPages(new Chain(store), ((Db)tplStore).sql(tplDefs, "|", "", 0).text(), ((Db)tplStore).sql(selectors, "|", "", 1).text(), ((Db)tplStore).sql(urlsDescArr, "", 0).text());
 }

 public Triples visitPages(String store, String[] tplDefs, String[] selectors, KeyPile<String, String> urlsDesc) throws Exception 
 {
  String[][] urlsDescArr = new String[urlsDesc.Len()][2];
  for (int i = 1; i <= urlsDesc.Len(); i++) { urlsDescArr[i - 1][0] = urlsDesc.Keys().g(i); urlsDescArr[i - 1][1] = urlsDesc.g(urlsDesc.Keys().g(i)); }
  return visitPages(new Chain(store), ((Db)tplStore).sql(tplDefs, "|", "", 0).text(), ((Db)tplStore).sql(selectors, "|", "", 1).text(), urlsDescArr);
 }

 public Triples visitPages(String store, String[] tplDefs, String[][] selectors, String[][] urlsDescArr) throws Exception 
 {
  return visitPages(new Chain(store), ((Db)tplStore).sql(tplDefs, "|", "", 0).text(), ((Db)tplStore).sql(selectors, "", 1).text(), ((Db)tplStore).sql(urlsDescArr, "", 0).text());
 }

 public Triples visitPages(String store, String[] tplDefs, String[][] selectors, KeyPile<String, String> urlsDesc) throws Exception 
 {
  String[][] urlsDescArr = new String[urlsDesc.Len()][2];
  for (int i = 1; i <= urlsDesc.Len(); i++) { urlsDescArr[i - 1][0] = urlsDesc.Keys().g(i); urlsDescArr[i - 1][1] = urlsDesc.g(urlsDesc.Keys().g(i)); }
  return visitPages(new Chain(store), ((Db)tplStore).sql(tplDefs, "|", "", 0).text(), ((Db)tplStore).sql(selectors, "", 1).text(), urlsDescArr);
 }

 public Triples visitPages(String store, String[][] tplDefs, String[] selectors, String[][] urlsDescArr) throws Exception 
 {
  return visitPages(new Chain(store), ((Db)tplStore).sql(tplDefs, "", 0).text(), ((Db)tplStore).sql(selectors, "|", "", 1).text(), ((Db)tplStore).sql(urlsDescArr, "", 0).text());
 }

 public Triples visitPages(String store, String[][] tplDefs, String[] selectors, KeyPile<String, String> urlsDesc) throws Exception 
 {
  String[][] urlsDescArr = new String[urlsDesc.Len()][2];
  for (int i = 1; i <= urlsDesc.Len(); i++) { urlsDescArr[i - 1][0] = urlsDesc.Keys().g(i); urlsDescArr[i - 1][1] = urlsDesc.g(urlsDesc.Keys().g(i)); }
  return visitPages(new Chain(store), ((Db)tplStore).sql(tplDefs, "", 0).text(), ((Db)tplStore).sql(selectors, "|", "", 1).text(), urlsDescArr);
 }

 public Triples visitPages(String store, String[][] tplDefs, String[][] selectors, String[] urlsDesc) throws Exception 
 {
  return visitPages(new Chain(store), ((Db)tplStore).sql(tplDefs, "", 0).text(), ((Db)tplStore).sql(selectors, "", 1).text(), ((Db)tplStore).sql(urlsDesc, "|", "", 0).text());
 }
 
 public Triples visitPages(String store, String[] tplDefs, String[] selectors, String[] urlsDesc) throws Exception 
 {
  return visitPages(new Chain(store), ((Db)tplStore).sql(tplDefs, "|", "", 0).text(), ((Db)tplStore).sql(selectors, "|", "", 1).text(), ((Db)tplStore).sql(urlsDesc, "|", "", 0).text());
 }
  
}







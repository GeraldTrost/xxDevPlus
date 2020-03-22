
package org.xxdevplus.udf;

import java.io.File;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.xxdevplus.chain.Chain;
import org.xxdevplus.data.FtxProvider;
import org.xxdevplus.frmlng.Pick;
import org.xxdevplus.frmlng.Zone;
import org.xxdevplus.gui.XhtDbgDlg;
import org.xxdevplus.gui.XhtParserView;
import org.xxdevplus.struct.CplrOpnd;
import org.xxdevplus.struct.EvtrOpnd;
import org.xxdevplus.struct.KeyPile;
import org.xxdevplus.struct.Pile;
import org.xxdevplus.struct.Triple;
import org.xxdevplus.struct.Triples;
import org.xxdevplus.sys.StringVal;
import org.xxdevplus.utl.ctx;


/** Xml or Html Tags Collection based on Jsoup: provides most versatile cascading select Methods with jquery-similar select syntax and holds the result Elements*/
public class XhtParser extends Pile<Element> 
{  

 private class searchDesc
 {
  private Chain        token    = Chain.Empty;
  private Chain        functor  = Chain.Empty;
  private Pile<Chain>  args     = new Pile<Chain>();
        
  public searchDesc(Chain functor, Pile<Chain> args, Chain token)
  {
   this.functor = functor;
   this.args    = args;
   this.token   = token;
  }
 }

 private ctx                                           cx                 =                                             new ctx();
 public  String                                        defaultMethodClass =                                                    "";
 private XhtCache                                      cache              =                                                  null;
 public  Element                                       root               =                                                  null;
 public  KeyPile<String, Pile<Chain>>                  extParams          =                    new KeyPile<String, Pile<Chain>>();
 public  KeyPile<String, KeyPile<String, Pile<Chain>>> kVals              =  new KeyPile<String, KeyPile<String, Pile<Chain>>> ();
 private KeyPile<Element, Chain>                       txtCache           =                         new KeyPile<Element, Chain>();
 private KeyPile<String, Pile<Chain>>                  keys               =                    new KeyPile<String, Pile<Chain>>();
 private ParserControl                                 ctl                =                                                  null;
 private boolean                                       foundInCache       =                                                  true; 
 public  XhtDbgDlg                                     xhtDbgDlg          =                                                  null; 
 private XhtParserView                                 xhtParserView      =                                                  null; 
 public  Pile<Chain>                                   selectors          =                                     new Pile<Chain>();
 public  String                                        lDef               =                                                    ""; 
 public  String                                        mDef               =                                                    ""; 
 public  String                                        rDef               =                                                    "";
 private int                                           dldOption          =                                                     1;
 private boolean                                       cancel             =                                                 false;
 
 public ParserControl ctl() { return ctl; }       // void ctl(ParserControl newVal) { ctl = newVal; }
 
 
 private void kVals(KeyPile<String, Pile<Chain>> kVals) throws Exception
 {
  if (kVals != null) 
  {
   this.kVals.p(kVals, "");
   this.keys.p(new Pile<Chain>(), "");
   for (String s : this.kVals.g("").Keys()) keys.g("").Push(new Chain(s));
  }
 }
 
 public void extParams(Object extParams) throws Exception
 {
  if (extParams == null) extParams = "";
  if (extParams instanceof String)
  {
   this.extParams = new KeyPile<String, Pile<Chain>>();
   Chain extPrm = new Chain(((String)extParams).trim());
   while (extPrm.len() > 0)
   {
    Chain val = Zone.charray.upon(extPrm);
    if (val.len() == 0) 
     extPrm = Chain.Empty; 
    else
     {
      this.extParams.Add(extPrm.before(1, "=").Trim().text(), new Pile<Chain>(0, new Chain(val.from(2).upto(-2).text().replace("&sq;", "'").replace("&comma;", ","))));
      extPrm = extPrm.after(val).after(1, ",").Trim(); 
     }
   }
  }
  else this.extParams = (KeyPile<String, Pile<Chain>> )extParams;
 }
 
 
 /** @param defaultMethodClass "" OR fully qualified name of a public class for calling Test-Methods during future select calls*/
 private XhtParser(String defaultMethodClass, Object extParams, KeyPile<String, Pile<Chain>> kVals, KeyPile<Element, Chain> txtCache, ParserControl ctl) throws Exception 
 {
  this.defaultMethodClass      = defaultMethodClass ;
  this.txtCache = txtCache;
  extParams(extParams);  
  kVals(kVals);
  if (ctl != null) this.ctl    = ctl;
  xhtDbgDlg = cx.xhtDbgDlg(); 
  if ((cx.xhtdbg() > 0) && (xhtDbgDlg == null))
  {
   xhtDbgDlg = new XhtDbgDlg(null, false);
   cx.xhtDbgDlg(xhtDbgDlg); 
   xhtDbgDlg.setAlwaysOnTop (true);
   //xhtDbgDlg.setVisible(true);
  }
  xhtParserView = cx.xhtParserView();
  if ((cx.xhtdbg() > 0) && (xhtDbgDlg.ChkShowXhtParserForEveryUrl()) && (xhtParserView == null))
  {
   xhtParserView =  new XhtParserView();
   cx.xhtParserView(xhtParserView);
  }
 }

 /** @param root Jsoup Element @param defaultMethodClass "" OR fully qualified name of a public class for calling Test-Methods during future select calls*/
 public  XhtParser(String url, String extParams, String cacheFolder, String cacheFileNamePattern, String defaultMethodClass, KeyPile<String, Pile<Chain>> kVals, ParserControl ctl) throws Exception 
 {
  this(defaultMethodClass, extParams, kVals, new KeyPile<Element, Chain>(), ctl);
  this.cache                   = new XhtCache(new File(cacheFolder), cacheFileNamePattern, this.ctl);
  dldOption = 1;
  if (url.startsWith("("))     { dldOption = Integer.parseInt("" + url.charAt(1)); url = url.substring(3); }
  if (!url.contains("://"))    { /* if (!url.startsWith("www.")) url = "www." + url; */ url = "https://" + url; }
  int[]                dld     = new int[] {dldOption};
  _name                        = url;
  //cache.ctl(ctl);
  
  if (!(ctl == null)) cancel   = !ctl.dispatch(1, this);
  if (!cancel)        root     = cache.getDocument(url, dld);
  if (!(ctl == null)) cancel   = !ctl.dispatch(-1, this);
  
  foundInCache = (dld[0] == 0);
 }
   
 private XhtParser(Pile<Chain> selectors, Element begin, Element root, String url, XhtCache cache, String defaultMethodClass, KeyPile<String, Pile<Chain>> extParams, KeyPile<String, Pile<Chain>> kVals, KeyPile<Element, Chain> txtCache, ParserControl ctl) throws Exception 
 {
  this(defaultMethodClass, extParams, kVals, txtCache, ctl);
  this.selectors               = selectors       ;
  this.cache                   = cache           ;
  _name                        = url             ;
  this.root                    = root            ;
  if (begin != null)
  {
   Clear();
   Push(begin);
  }
 }

 private Pile<Chain> terms(Chain argm, String delim) throws Exception  //builds a list of termss
 {

  Pile<Chain> ret = new Pile<Chain>();
  
  Chain firstTerm = null;
  switch (argm.charAt(1, false))
  {
   case '\'':
    break;
   case '(':
    break;
   case '{':
    break;
   case '[':
    break;
   case '"':
    break;
    
  }
  
  return ret;
 }
 
 
 private Pile<searchDesc> findFirst(int lstPos, Chain opnd, Chain def, int lmt, Element el, KeyPile<String, StringVal> strValMethods) throws Exception
 {
  Pile<searchDesc> ret = new Pile<searchDesc>();
  if (lmt == 0)
  {
   Chain             delim = Chain.Empty;
   Chain              argm = Pick.sglBrcs.host(Pick.sglSQots).grabOne().upon(def);
   Chain           functor = def.before(argm).Trim();
   Pile<Chain>        args = terms(argm.from(2).upto(-2).Trim(), ",");
           
   String arguments = argm.from(2).upto(-2).Trim().text();
   if (arguments.contains(","))
           arguments = arguments;
   
   Pile<CplrOpnd>   cpArgs       = compileTerm(lstPos, argm.from(2).upto(-2).Trim(), strValMethods);
   Pile<Chain>      multiOperand = evalTerm(lstPos, false, el, cpArgs, strValMethods); 
           
   boolean done = false;  // some functors might use the multiOperand as argument and others might use each single Operand in a forr loop
   for (Chain arg : multiOperand)
   {
    if (done) break;
    switch (functor.incident("at", "At", "ctm", "wtm", "Wtm", "rpl"))
    {
     case 1:  
              if (cpArgs.g(1).ops().g(1).startsWith("'"))
              {
               while (arg.len() > 0) {args.Push(arg.before(1, "|")); arg = arg.after(1, "|"); }
               delim = opnd.at(1, true, args.strArray());
              }
              else
              {
               delim = opnd.at(1, true, multiOperand.strArray());
               done = true;
              }
              break;
               
     case 2:  
              if (cpArgs.g(1).ops().g(1).startsWith("'"))
              {
               while (arg.len() > 0) {args.Push(arg.before(1, "|")); arg = arg.after(1, "|"); }
               delim = opnd.At(1, true, args.strArray());
              }
              else
              {
               delim = opnd.At(1, true, multiOperand.strArray());
               done = true;
              }
              break;
              
     case 3:  
              args.Push(arg);
              delim = opnd.trimC(args.g(1).text()); break;
     case 4:  
              args.Push(arg);
              delim = opnd.trimW(args.g(1).text()); break;
     case 5:  
              args.Push(arg);
              delim = opnd.TrimW(args.g(1).text()); break;
     case 6:  
              while (arg.len() > 0) {args.Push(arg.before(1, "|")); arg = arg.after(1, "|"); }
              delim = new Chain(opnd.text().replace(args.g(1).text(), args.g(2).text())); break;
     default: throw new Exception("unknown functor " + functor);
    }
    if (delim.len() > 0) ret.Push(new searchDesc(functor, args, delim));
   }
   
           
  }
  else ret.Push(new searchDesc(Chain.Empty, new Pile<Chain>(), opnd.at(lmt)));
  return ret;
 }




 
 private searchDesc findNext(Chain opnd, searchDesc recent) throws Exception
 {
  if (recent.functor .len() == 0) throw new Exception ("unable to find further results in String");
  Chain             delim = Chain.Empty;
  switch (recent.functor.incident("at", "At", "ctm", "wtm", "Wtm", "rpl"))
  {
   case 1:  delim = opnd.at   (1, true, recent.args.strArray()  ); break;
   case 2:  delim = opnd.At   (1, true, recent.args.strArray()  ); break;
   case 3:  delim = opnd.trimC (recent.args.g(1).text()          ); break;
   case 4:  delim = opnd.trimW (recent.args.g(1).text()          ); break;
   case 5:  delim = opnd.TrimW (recent.args.g(1).text()          ); break;
   case 6:  
    delim = opnd.trimC (recent.args.g(1).text()          ); 
    break;
   default: throw new Exception("unknown functor " + recent.functor);
  }
  return (delim.len() > 0) ? new searchDesc(recent.functor, recent.args, delim) : new searchDesc(recent.functor, recent.args, delim);
 }

 private Pile<Chain> calcRestrVnts(int lstPos, Chain operand, String  opndSig, Chain restrictor, Element el, KeyPile<String, StringVal> strValMethods) throws Exception
 {
  Pile<Chain> ret = new Pile<Chain>();
  restrictor = restrictor.from(2).upto(-2); int min = 0; int max = 0; Chain left  = restrictor; Chain right = Chain.Empty;
  Chain delim = restrictor.less(Pick.sglBrcs.host(Pick.sglSQots).upon(restrictor)).at(1, "..").upon(restrictor);
  if (delim.len() > 0) { left  = restrictor.before (delim).Trim(); right = restrictor.after(delim).Trim(); }
  try { min = Integer.parseInt(  left.text());} catch(Exception ex) {}
  try { max = Integer.parseInt( right.text());} catch(Exception ex) {}
  if (min != 0) left  = Chain.Empty; if (max != 0) right = Chain.Empty;
  Pile<searchDesc> rSearch  = (right.len() == 0 && max == 0) ? findFirst(lstPos, operand, Chain.Empty, -1, el, strValMethods) : findFirst(lstPos, operand, right, max, el, strValMethods);
  for (searchDesc rSrch : rSearch)
  {
   Chain contrib = operand.upto(rSrch.token);
   Chain rTok = Chain.Empty;
   searchDesc rSubSrch = rSrch;
   do
   {
    Pile<searchDesc> lSearch = findFirst(lstPos, contrib, left, min, el, strValMethods);
    for (searchDesc lSrch : lSearch)
    {
     Chain ctb = contrib.upto(-1); // cloning
     Chain lTok = Chain.Empty;
     searchDesc lSubSrch = lSrch;
     do
     {
      if (right.len() > 0 || max != 0) ret.Push(ctb.from(lSubSrch.token)); else ret.Push(ctb.at(lSubSrch.token)); 
      ctb = contrib.after(lSubSrch.token);
      if (min != 0) lTok = Chain.Empty;
      else
      {
       lSubSrch = findNext(ctb, lSubSrch);
       lTok = lSubSrch.token;
      }
     }
     while (lTok.len() > 0);
    }
    if ((max != 0) || (rSubSrch.functor.len() == 0)) rTok = Chain.Empty;
    else
    {
     contrib = operand.after(contrib);
     rSubSrch = findNext(contrib, rSubSrch);
     rTok = rSubSrch.token;
     contrib = contrib.upto(rTok);
    }
   }
   while (rTok.len() > 0);
  }
  return ret;  
 }
 
 private Pile<Chain> calcRestrVnts(int lstPos, Chain operand, String  opndSig, Pile<Chain> restrictors, Element el, KeyPile<String, StringVal> strValMethods) throws Exception
 {
  Pile<Chain> concatBufs = new Pile<Chain>(); // if a restrictor pushes smore than 1 result on concatBufs then all subsequent restrictors MUST add to all of these - but calculates on base of operand, not on base of any concatBuf Entry
  for (Chain restrictor : restrictors)
  {
   Pile<Chain> found = calcRestrVnts(lstPos, operand, opndSig, restrictor, el, strValMethods);
   if (concatBufs.Len() == 0)
    concatBufs.Push(found);
   else
   {
    Pile<Chain> results = new Pile<Chain>();
    for (Chain c : concatBufs) for (Chain f : found) results.Push(c.plus(f));
    concatBufs = results;
   }
  }
  return concatBufs;
 }


 private Pile<Chain> evalFunctor(int lstPos, Chain functor, CplrOpnd op, Element el, KeyPile<String, StringVal> strValMethods) throws Exception
 {
  Pile<Chain> multiRes = new Pile<Chain>();

  Pile<CplrOpnd> compiled = compileTerm(lstPos, op.ops().g(lstPos).after(functor).from(2).upto(-2), strValMethods);
  for (Chain arg : evalTerm(lstPos, false, el, compiled, strValMethods))
  {
   arg = arg.Trim();
   switch (functor.incident("text", "own", "html", "class", "tag", "<n/d>", "sin", "len", "all", "keys", "val1", "val2", "val3"))
   {
    
    
    
    case   1: 
           {
            if (!txtCache.hasKey(el))
            {
             if ((arg.len() == 0) || (ctl.ftxProvider == null) || (ctl.inBehalfOf == 0L))
              txtCache.Add(el, new Chain(el.text()));
             else
             {
              String ft_file   = cache.cachedFile.getName();
              txtCache.Add(el, Chain.Load(el.text(), "w§" + arg.text() + "§" + ft_file, ctl.ftxProvider, ctl.inBehalfOf));
             }
            }
            multiRes.Push(txtCache.g(el));
            break;      
           }

           
    case   2: multiRes.Push(new Chain(el.ownText()))   ; break;      
    case   3: multiRes.Push(new Chain(el.html()))      ; break;      
    case   4: multiRes.Push(new Chain(el.className())) ; break;      
    case   5: multiRes.Push(new Chain(el.tagName()))   ; break;      

    case   7: 
              if (arg.endsWith("°")) 
               multiRes.Push(new Chain("" + Math.sin(Double.parseDouble(arg.before(1, "°").Trim().text()) * Math.PI / 180))); 
              else
              {
               multiRes.Push(new Chain("len: " + arg.len()));
               //multiRes.Push(new EvtrOpnd("" + Math.sin(Double.parseDouble(arg))));
              }
              break;
    case   8: 
              multiRes.Push(new Chain("" + arg.len()));
              break;
    case   9: 
              Chain a = arg;
              multiRes.Push(new Chain(a.before(1, "|").text()));
              a = a.after(1, "|");
              while(a.len() > 0)
              {
               multiRes.Push(a.before(1, "|"));
               a = a.after(1, "|");
              }
              break;
    case  10: 
     
              if (!keys.hasKey(arg.text().trim()))
              {
               kVals.Add(arg.text().trim(), new KeyPile<String, Pile<Chain>> ());
               Pile<Chain> kvs = extParams.g(arg.text().trim());
               Chain kv = kvs.g(1);
               while (kv.len() > 0)
               {
                Chain s = kv.before(1, ",").Trim();
                if (!kVals.g(arg.text().trim()).hasKey(s.after(1, " ").Trim().text())) kVals.g(arg.text().trim()).Add(s.after(1, " ").Trim().text(), new Pile<Chain>());
                kVals.g(arg.text().trim()).g(s.after(1, " ").Trim().text()).Push(s.before(1, " "));
                kv = kv.after(1, ",").Trim();
               }
               
               this.keys.p(new Pile<Chain>(), arg.text().trim());
               for (String s : this.kVals.g(arg.text().trim()).Keys()) 
                keys.g(arg.text().trim()).Push(new Chain(s));
              }
              multiRes = keys.g(arg.text().trim());
              break;
    case  11: 
              if (kVals.g("").hasKey(arg.text())) multiRes.Push(kVals.g("").g(arg.text()).g(1));
              break;
    case  12: 
              if (kVals.g("").hasKey(arg.text())) multiRes.Push(kVals.g("").g(arg.text()).g(2));
              break;
    case  13: 
              if (kVals.g("").hasKey(arg.text())) multiRes.Push(kVals.g("").g(arg.text()).g(3));
              break;

    default : throw new Exception("unknown Jsoup function() " + functor.text());
   }      
  }
  return multiRes;
 }
 
 private Pile<Element> navigate(int lstPos, CplrOpnd op, Pile<Element> currentNav) throws Exception
 {
  {
   int n = 1;
   while (n <= currentNav.Len())
   {
    Chain navigation = Pick.sglAngls.grabOne().host(Pick.sglSQots).upon(true, op.ops().g(lstPos)).from(2).upto(-2);
    Chain move = navigation.before(1, ";");
    navigation = navigation.from(1, ";").Trim();
    int hmove = Integer.parseInt(move.before(1, ",").text());
    int vmove = Integer.parseInt(move.after(1, ",").text());
    if (hmove < 0) for (int i = hmove; i < 0; i++) currentNav.s(currentNav.g(n).previousElementSibling(), n); else for (int i = 1    ; i <= hmove; i++) currentNav.s(currentNav.g(n).nextElementSibling(), n); 
    if (vmove < 0) for (int i = vmove; i < 0; i++) currentNav.s(currentNav.g(n).parent()                , n); else for (int i = 1    ; i <= vmove; i++) currentNav.s(currentNav.g(n).child(0)            , n); 
    if (navigation.startsWith(";"))
    {
     navigation = navigation.from(2).Trim();
     Pile<Chain> selectors = new Pile<Chain>();
     while (navigation.len() > 0)
     {
      Chain nav = navigation.less(Pick.sglBrks.host(Pick.sglSQots).upon(navigation));
      nav = nav.less(Pick.sglBrcs.host(Pick.sglSQots).upon(nav));
      Chain delim = nav.at(1, ",");
      selectors.Push(navigation.upto(nav.before(delim)).Trim());
      navigation = navigation.after(delim).Trim();
     }
     n -= currentNav.Len() - currentNav.InsBefore(n, subSelect(lstPos, currentNav.g(n), selectors)).Len();
     currentNav.Del(n);
    }
    else n++;
   }
  }
  return currentNav;
 } 
 
 // constDependsOnVars: if all variable Terms are emty then the constant Terms will be omitted
 private Pile<Chain> evalTerm(int lstPos, boolean constDependOnVars, Element el, Pile<CplrOpnd> operands, KeyPile<String, StringVal> strValMethods) throws Exception
 {
  Pile<EvtrOpnd> res = new Pile<EvtrOpnd>();
  Pile<Element> plannedNav = new Pile<Element>();   // an "<>" Operand bay have defined to navigate to a different Jsoup Element that is called "planned Navigation" in this context
  for (CplrOpnd op: operands)
  {
   op = op.clone();
   Pile<Element> currentNav = plannedNav;
   if (currentNav.Len() == 0) currentNav.Push(el); // no navigation done by previsou opnd
   plannedNav = new Pile<Element>(0, el);
   if (op.sig().equals("navigate")) plannedNav = navigate(lstPos, op, currentNav);
   else
   {
    for (int n = 1; n <= currentNav.Len(); n++)
    {     
     /*
     Pile<Chain> restrictors = new Pile<Chain>();
     if (op.op().startsWith("(.href[at('//')..-2])"))
      op = op;
     
     while (op.op().less(Pick.sglSQots.upon(op.op())).less(Pick.sglCrlys.upon(op.op())).less(Pick.sglBrcs.upon(op.op())).at(1, "[").len() > 0)   // while (op.less(Zone.charrays.upon(op)).less(Zone.curlybracks.upon(op)).less(Zone.bracelets.upon(op)).at(1, "[").len() > 0)
     {
      Chain restrictor = Zone.bracket.upon(op.op().less(Zone.charrays.upon(op.op())).less(Zone.curlybracks.upon(op.op())).less(Zone.bracelets.upon(op.op())));
      Chain rest = op.op().before(restrictor).plus(op.op().after(restrictor));
      restrictors.Push(op.op().less(rest));
      op.op(op.op().less(restrictors.g(-1)), false);   
     }
     */
     if (op.sig().equals("functor") || op.ops().g(lstPos).startsWith("{") || op.ops().g(lstPos).startsWith("(")) 
     {
      if (op.sig().equals("functor")) 
      {   
       Pile<Chain> multiRes = evalFunctor(lstPos, op.ops().g(lstPos).before(1, "("), op, currentNav.g(n), strValMethods);
       if (multiRes.Len() > 0) res.Push(new EvtrOpnd(op.sig(), multiRes));
      }
      else
      {
       Pile<CplrOpnd> compiled = compileTerm(lstPos, op.ops().g(lstPos).from(2).upto(-2), strValMethods);
       Chain endMarker = op.ops().g(lstPos).startsWith("{") ? op.ops().g(lstPos).less(Zone.charrays.upon(op.ops().g(lstPos))).at(1, "}") : op.ops().g(lstPos).less(Zone.charrays.upon(op.ops().g(lstPos))).at(1, ")");
       if (endMarker.len() == 0) throw new Exception("malformed Term " + op.ops().g(lstPos).text());
       for (Chain term : evalTerm(lstPos, op.ops().g(lstPos).startsWith("{"), currentNav.g(n), compiled, strValMethods)) res.Push(new EvtrOpnd(term));
      }
     }
     else
     {
      if (op.ops().g(lstPos).startsWith("'")) 
       res.Push(new EvtrOpnd("const", op.ops().g(lstPos).from(2).before(-1)));
      else
       if (op.ops().g(lstPos).startsWith(".")) 
       {
        if (op.ops().g(lstPos).at("()").len() == 0) res.Push(new EvtrOpnd(currentNav.g(n).attr(op.ops().g(lstPos).from(2).text().trim())));
        else
        {
         if (op.ops().g(lstPos).Equals(".own()"))            res.Push(new EvtrOpnd(currentNav.g(n).ownText()));
         else if (op.ops().g(lstPos).Equals(".text()"))      res.Push(new EvtrOpnd(currentNav.g(n).text()));
          else if (op.ops().g(lstPos).Equals(".html()"))     res.Push(new EvtrOpnd(currentNav.g(n).html()));
           else if (op.ops().g(lstPos).Equals(".class()"))   res.Push(new EvtrOpnd(currentNav.g(n).className()));
            else throw new Exception("unknown Jsoup function() " + op.ops().g(lstPos).text());
        }
       }
       else 
        if (op.ops().g(lstPos).startsWith(":")) 
        {
         if (op.ops().g(lstPos).at("()").len() == 0) 
         {
          if (op.ops().g(lstPos).at(1, "=").len() == 0)
           res.Push(new EvtrOpnd("ctxVar", extParams.g(op.ops().g(lstPos).from(2).text())));
          else
          {
           String varName = op.ops().g(lstPos).from(2).before(1, "=").Trim().text();
           extParams.p(evalTerm(lstPos, constDependOnVars, el, compileTerm(lstPos, op.ops().g(lstPos).from(2).after(1, "=").Trim(), strValMethods), strValMethods), varName);
           res.Push(new EvtrOpnd("ctxVar", extParams.g(varName)));
          }
         }
         else
         {
          if (op.ops().g(lstPos).Equals(":product()"))   res.Push(new EvtrOpnd("xxDevPlus"));
          else throw new Exception("unknown external function() " + op.ops().g(lstPos).text());
         }
        }
        else
         if (op.ops().g(lstPos).startsWith("<<")) res.Push(new EvtrOpnd(((String)(strValMethods.g(op.ops().g(lstPos).text()).method.invoke(strValMethods.g(op.ops().g(lstPos).text()).creator, currentNav.g(n)))).trim()));
         else throw new Exception("malformed Term " + op.ops().g(lstPos).text());
     }

     //  here comes the most difficult part with restrictors on variants of operands
     //  think of the following two restrictors [at('o')][2..3] on the operand 'hello'
     //  the first restricor would result in 'o' (this is the last charactor in 'hello') and the second restrictor would result in 'el' (these are the second and the third characters in 'hello')
     //  the result will be 'oel'.
     //  the scenario would surely get more difficult if the restrictors have to operate on two variants of an operand.
     //  e.g. the last operand of the current result could consist of 2 variants 'hello' and/or 'world' just because the recent eval did result in 2 strings
     //  now the 2 restrictors must end up in the 2 variants 'ooe' and 'oor'.
     //  this is what the outer loop "while (vnt <= res.g(-1).opVnts().Len())" does.
     //  things will be even more difficult if one or both restrictors also yield multiple results.
     //  let us think of the example [at('o')][2..3] on the operand variants 'helloo' 'woorld'
     //  in this case the at('o') restrictor would find 2 results in each variant ... but the next restrictor would still operate on 'helloo' and 'woorld', even if must be calculated twice as often.
     //  resumee: I was unable to write this in cascaded loops - i splitted this into seperate mathod calls.

     Pile<Chain> restrictors = op.restrict();
     if ((res.Len() > 0) && (restrictors.Len() > 0))
     {
      int vnt = 1; 
      while (vnt <= res.g(-1).opVnts().Len())
      {
       Pile<Chain> restrictedVnts = calcRestrVnts(lstPos, res.g(-1).opVnts().g(vnt), res.g(-1).sig(), restrictors, el, strValMethods);
       res.g(-1).opVnts().InsAfter(vnt, restrictedVnts);
       res.g(-1).opVnts().Del(vnt);
       vnt += restrictedVnts.Len();
      }
     }
    }
   }
  }

  // now all calculations are done ... we only need to concat all the result variants
  
  Pile<Chain> ret = new Pile<Chain>(0, Chain.Empty);
  Pile<Chain> tet = new Pile<Chain>(0, Chain.Empty);  // test ret for case constDependOnVars ( in this special case const delims are ignored when variables result to "" )
  for (EvtrOpnd op : res) 
  {
   int retBlock = ret.Len();
   for (int i = 1; i <= op.opVnts().Len(); i++) ret.Push(ret.upto(retBlock));
   for (int i = 1; i <= op.opVnts().Len(); i++) tet.Push(tet.upto(retBlock));

   for (int j = 1; j <= op.opVnts().Len(); j++) for (int i = 1; i <= retBlock; i++) ret.s(ret.g(i).plus(op.opVnts().g(j)), j * retBlock + i); 

   for (int j = 1; j <= op.opVnts().Len(); j++) for (int i = 1; i <= retBlock; i++) 
    if (op.sig().equals("const") || op.sig().equals("ctxVar")) 
     tet.s(tet.g(i).plus(""), j * retBlock + i); 
    else 
     tet.s(tet.g(i).plus(op.opVnts().g(j)), j * retBlock + i);

   ret = ret.after(retBlock);
   tet = tet.after(retBlock);
  }
  if (constDependOnVars) for (int i = 1 ; i <= tet.Len(); i++) if (tet.g(i).Trim().len() == 0) ret.s(Chain.Empty, i);
  
  return ret;
 }

 
 private boolean findMatch(int lstPos, Element el, Pile<CplrOpnd> cmpldOpnds, int i, KeyPile<String, StringVal> strValMethods) throws Exception
 {
  Pile<Chain> ls = evalTerm(lstPos, false, el, cmpldOpnds.before (i), strValMethods);
  Pile<Chain> rs = evalTerm(lstPos, false, el, cmpldOpnds.after  (i), strValMethods);
  for (int ii = 1; ii <= ls.Len(); ii++) for (int jj = 1; jj <= rs.Len(); jj++) 
   if (ls.g(ii).equals(rs.g(jj))) 
    return true;
  return false;
 }
 
 private boolean evalCond(int lstPos, Element el, Pile<Pile<CplrOpnd>> operandsSet, KeyPile<String, StringVal> strValMethods) throws Exception
 {
  for (Pile<CplrOpnd> cmpldOpnds : operandsSet)
   for (int i = 1; i <= cmpldOpnds.Len(); i++)
   {
    if ((cmpldOpnds.g(i).ops().g(lstPos).text().equals("==")) || (cmpldOpnds.g(i).ops().g(lstPos).text().equals("!=")))
    {
     if ((cmpldOpnds.g(i).ops().g(lstPos).text().equals("==")) ^ (findMatch(lstPos, el, cmpldOpnds, i, strValMethods))) return false;
     break;
    }
   }
  return true;
 }

 private Pile<CplrOpnd> compileTerm(int lstPos, Chain def, KeyPile<String, StringVal> strValMethods) throws Exception
 {
  Pile<CplrOpnd> ret = new Pile<CplrOpnd>();
  def = def.Trim();
  while (def.len() > 0)
  {
   Chain prefix = null;
   if ((def.startsWith("<")) && !(def.startsWith("<<")))
   {
    prefix = Pick.sglAngls.grabOne().host(Pick.sglSQots).upon(true, def);   
    def = def.after(prefix);
   }   
   if ((def.startsWith(","))) // this operand is a list
   {
    Chain dlm = def.less(Pick.sglSQots.upon(def)).less(Pick.sglBrcs.upon(def)).less(Pick.sglCrlys.upon(def)).less(Pick.sglBrks.upon(def)).at(1, ",");
    if (dlm.len() == 0) ret.g(-1).ops().Push(def.Trim()); ret.g(-1).ops().Push(def.before(dlm).Trim());
    def = def.after(dlm).Trim();
   }
   else
   {
    if ((def.startsWith("'")))                            
     ret.Push(new CplrOpnd(def.upto(2, "'")));
    else
     if ((def.startsWith("==")) || (def.startsWith("!="))) 
      ret.Push(new CplrOpnd(def.upto(2)));
     else
      if ((def.startsWith(".")))                            
       ret.Push(new CplrOpnd(def.before(1, " ").upto(1, "()").before(1, "<").before(1, "{").before(1, "[").before(1, "(").before(1, "'").before(1, ":").before(2, ".")));
      else
       if ((def.startsWith(":")))
       {
        //if ((def.startsWith("::")))
        // ret.Push(new CplrOpnd("tplVar", def.before(1, " ").upto(1, "()").before(1, "<").before(1, "{").before(1, "[").before(1, "(").before(1, "'").before(1, ".").before(3, ":")));
        //else
        ret.Push(new CplrOpnd("ctxVar", def.before(1, " ").upto(1, "()").before(1, "<").before(1, "{").before(1, "[").before(1, "(").before(1, "'").before(1, ".").before(2, ":")));
        if (def.after(ret.g(-1).ops().g(lstPos)).Trim().StartsWith("=")) 
         if (def.after(ret.g(-1).ops().g(lstPos)).after(1, "=").Trim().StartsWith("{"))
          ret.g(-1).ops().s( def.upto(Pick.sglCrlys.grabOne().host(Pick.sglCrlys).host(Pick.sglBrks).host(Pick.sglBrcs).host(Pick.sglSQots).upon(def)).Trim(), lstPos);
         else
          ret.g(-1).ops().s( def.upto(Pick.sglBrcs.grabOne().host(Pick.sglCrlys).host(Pick.sglBrks).host(Pick.sglBrcs).host(Pick.sglSQots).upon(def)).Trim(), lstPos);
       }
       else
        if (def.startsWith("{") || def.startsWith("("))
        {
         Pick p = def.startsWith("{") ? Pick.sglCrlys : Pick.sglBrcs;
         ret.Push(new CplrOpnd(p.grabOne().upon(true, def)));
        }
        else
         if ((def.startsWith("<<")))
         {
          ret.Push(new CplrOpnd(Pick.dblAngls.grabOne().host(Pick.sglSQots).upon(def)));
          if (ret.g(-1).ops().g(lstPos).at(".").len() == 0) 
           strValMethods.Add(ret.g(-1).ops().g(lstPos).text(), new StringVal(defaultMethodClass + "." + ret.g(-1).ops().g(lstPos).after(2).before(-2).text().trim(), Object.class)); 
          else 
           strValMethods.Add(ret.g(-1).ops().g(lstPos).text(), new StringVal(ret.g(-1).ops().g(lstPos).after(2).before(-2).text().trim(), Object.class));
         }
         else 
         {
          Pick p = Pick.sglBrcs.host(Pick.sglSQots).avoid(Pick.sglSQots).grabOne();
          Chain arg = p.upon(def); 
          if (arg.len() == 0) throw new Exception("malformed XhtTag term " + def.text());
          ret.Push(new CplrOpnd("functor", def.upto(arg)));
         }
    if (ret.g(-1).ops().g(lstPos).len() == 0) throw new Exception("malformed XhtTag selector");
    def = def.after(ret.g(-1).ops().g(lstPos)).Trim();
    //debug def = new Chain("[1..2, 'ab]cd'][3..5]div");
    if (prefix != null) ret.InsBefore(-1, new CplrOpnd("navigate", prefix)); // ret.s(new CplrOpnd(ret.g(-1).sig(), prefix.plus(ret.g(-1).op())), -1);
    while (def.startsWith("[") && def.len() > 0)
    {
     if (def.startsWith("[1.."))
      def = def;
     Chain restrictor = Pick.sglBrks.grabOne().host(Pick.sglSQots).upon(true, def);
     ret.g(-1).restrict().Push(restrictor); // ret.s(new CplrOpnd(ret.g(-1).sig(), ret.g(-1).op().plus(restrictor)), -1);
     def = def.after(restrictor).Trim();
    }
   }
  }
  return ret;
 }

 
 /** qjuery similar select 
     @param selector samples:                                                                                     <br>
     <b>"div#mydiv"</b>                                                                                           <br>
      see jquery documentation                                                                                    <br>
     <b>"[2..3]div#mydiv"</b>                                                                                     <br>
      only return second and third found Element                                                                  <br>
     <b>"[2..3], -1]div#mydiv"</b>                                                                                <br>
      return predecessor siblings of second and third found Element                                               <br>
     <b>"[2..3, org.tst.TstMeth.tst1, org.tst.TstMeth.tst1]div#mydiv"</b>                                         <br>
      return second and third found Element but only those who pass BOTH test-Methods returning non-empty Strings <br>
     <b>"[2..3, tst1, tst1]div#mydiv"</b>                                                                         <br>
      same if defaultMethodClass "org.tst.TstMeth" was specified in constructor                                   <br>
     <b>"div#mydiv", "[1..2]a" (cascaded select)</b>                                                              <br>
      first select all mathing div Elements and from each retürn the first 2 anchor Elements                      <br>
 */

 public  Pile<Chain>    recentSelectors = new Pile<Chain>();
 public  XhtParser      recentResult;        

 private void inspect(XhtParser result) throws Exception
 {
  if (ctl.inspector() != null) ctl.inspector().method.invoke(ctl.inspector().creator, result);
 } 
 
 public XhtParser select(Pile<Chain> selectors) throws Exception
 {
  if (cancel) return this; if (!(ctl == null)) cancel   = !ctl.dispatch(2, this); if (cancel) return this;
  this.selectors = selectors;
  boolean differs = false;
  if ((recentSelectors.Len() != selectors.Len()) || (selectors.Len() == 0)) 
   differs = true; 
  else 
   for (int i = 1; i <= selectors.Len(); i++) if (!selectors.g(i).equals(recentSelectors.g(i))) differs = true;
  if (differs) 
  {
   recentSelectors = selectors;
   while (this.Len() > 0) Pop();
   //if ((this.url.length() > 0) && !(root.baseUri().contains(this.url))) return this;
   if (root instanceof Document) Push(root.select("body").first()); else Push(root);
   int lstPos = 1;
   recentResult = subSelect(lstPos, null, selectors);
  }
  inspect(recentResult);
  if (!(ctl == null)) cancel   = !ctl.dispatch(-2, this);
  return recentResult;
 }

 public XhtParser select(Chain... selectors) throws Exception
 {
  return select(new Pile<Chain>(0, selectors));
 }

 public XhtParser select(String... selectors) throws Exception
 {
  Pile<Chain> slc = new Pile<Chain>();
  for (String s : selectors) slc.Push(new Chain(s));
  return select(slc);
 }

 public XhtParser select(boolean startWithDocument, Pile<Chain> selector) throws Exception
 {
  int lstPos = 1;
  while (this.Len() > 0) Pop();
  //if ((this.url.length() > 0) && !(root.baseUri().contains(this.url))) return this;
  if (startWithDocument) Push(root.ownerDocument()); else if (root instanceof Document) Push(root.select("body").first()); else Push(root);
  return subSelect(lstPos, null, selector);
 }
 
 public XhtParser subSelect(int lstPos, Element begin, Pile<Chain> selectors) throws Exception
 {
  if (selectors.Len() == 0) return this;
  if (cancel) return this;
  Pile<Pile<CplrOpnd>>          operandSet  = new Pile<Pile<CplrOpnd>>();
  KeyPile<String, StringVal> strValMethods  = new KeyPile<String, StringVal>();  
  Pile<Chain> subSelector = selectors.from(2);
  Chain query = selectors.g(1).Trim();
  int dist = 0; int min  = 0; int max  = 0; 
  if (query.StartsWith("["))
  {
   Chain defs = query.less(query.after(Zone.bracket.upon(query.less(Zone.charrays.upon(query))))).from(2).upto(-2);
   query = query.after(defs).from(2);
   while (defs.len() > 0)
   {
    Chain def = defs.before(1, ",").Trim();
    while (((def.count("'") % 2) == 1) && (defs.after(def).count("'") > 0)) def = def.plus(",").plus(defs.after(def).from(2).before(1, ",").Trim());
    defs = defs.after(def).from(2).Trim();
    if (def.before(1, "[").before(1, "'").at(1, "..").len() == 0) try { dist = Integer.parseInt(def.text().trim());} catch (Exception ex) {operandSet.Push(compileTerm(lstPos, def, strValMethods)); }
    else { min = Integer.parseInt(def.before( 1, "..").text().trim()); max = Integer.parseInt(def.after(  1, "..").text().trim()); }
   }
  }
  XhtParser ret = new XhtParser(this.selectors, begin, root, Name(), cache, defaultMethodClass, extParams, this.kVals.g(""), txtCache, ctl); 
  Elements found = null;
  for (Element top: this) 
  {
   try { found = top.select(query.text()); } catch (Exception ex) { ex = ex;}
   if (found.size() > 0) 
   {
    if (found.get(0) == top) found.remove(0);
    if (max  > 0) while (found.size() > max) found.remove(max);
    if (min  > 0) for (int i = 1; i < min; i++) found.remove(0);                         
    if (dist < 0) for (int i = 1; i <= found.size(); i++) for (int j = 1; j <= -dist; j++) found.set(i - 1, found.get(i - 1).previousElementSibling() );   // search not the found Element but rather search the next     siblings
    if (dist > 0) for (int i = 1; i <= found.size(); i++) for (int j = 1; j <=  dist; j++) found.set(i - 1, found.get(i - 1).nextElementSibling()     );   // search not the found Element but rather search the previous siblings  
    for (Element el : found) if (evalCond(lstPos, el, operandSet, strValMethods)) ret.Push(el);
   }
  }
  if (!(ctl == null)) cancel   = !ctl.dispatch(3, ret); if (cancel) return this;
  ret = ret.subSelect(lstPos, null, subSelector);  
  if (!(ctl == null)) cancel   = !ctl.dispatch(-3, ret); if (cancel) return this;
  return ret;
  
 } 

 public Triples triples = null;
 
 public Triples triples(String lDef, String mDef, String rDef) throws Exception
 {
  if (cancel) return triples;
  this.lDef = lDef;
  this.mDef = mDef;
  this.rDef = rDef;
  if ((foundInCache && !ctl.processCacheHit()) || (!foundInCache && !ctl.processCacheMiss())) return new Triples();
  int lstPos = 1; // per default only the first element of a list will be evaluated, so replace('aaaa', 'a', 'b') is not evalible because the list operand 'aaaa', 'a', 'b' has pos = 1 and pos = 2 and pos = 3
  triples = new Triples();
  KeyPile<String, StringVal> strValMethods  = new KeyPile<String, StringVal>();  
  Pile<CplrOpnd> lOps = compileTerm(lstPos, new Chain(lDef), strValMethods);
  Pile<CplrOpnd> mOps = compileTerm(lstPos, new Chain(mDef), strValMethods);
  Pile<CplrOpnd> rOps = compileTerm(lstPos, new Chain(rDef), strValMethods);
  if (!(ctl == null)) cancel   = !ctl.dispatch(4, this); if (cancel) return triples;
  for (Element el : this)
  {
   Pile<Chain> ls = evalTerm(lstPos, false, el, lOps, strValMethods);
   Pile<Chain> ms = evalTerm(lstPos, false, el, mOps, strValMethods);
   Pile<Chain> rs = evalTerm(lstPos, false, el, rOps, strValMethods);
   for (Chain l : ls) for (Chain m : ms) for (Chain r : rs)
   {
    l = l.Trim(); m = m.Trim(); r = r.Trim();
    if ((l.len() > 0) && (m.len() > 4) && (r.len() > 0)) triples.Push(new Triple(m.text().charAt(0), l, m.from(5), r));   
   }
  }
  if (xhtParserView != null) xhtParserView.xhtParser(this);
  if (!(ctl == null)) cancel   = !ctl.dispatch(-4, this); if (cancel) return new Triples();
  return triples;
 }

 public Triples triples(String[][] defs) throws Exception
 {
  Triples ret = new Triples();
  for (String[] def : defs) ret.Push(triples(def[0], def[1], def[2]));
  return ret;
 }
  
 public XhtCache cache()
 {
  return cache;
 }        

 
}




/*

:id
'atr:Boxoffice'
{text()[at('Box office')..at('ion')]}[12..-1]

:id
'atr:Budget'
{text()[at('Budget')..at('ion')]}[8..-1]



*/
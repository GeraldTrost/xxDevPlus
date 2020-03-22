

// UniversalDataFinder - hi sophisticated parsing of (html)files in different style as jsoup with implicit looping and multilevel parsing


/** @author GeTr */

package org.xxdevplus.udf;

import java.io.File;

import org.xxdevplus.utl.utl;
import org.xxdevplus.struct.KeyPile;
import org.xxdevplus.struct.NamedValue;
import org.xxdevplus.struct.Pile;
import org.xxdevplus.chain.Chain;
import org.xxdevplus.frmlng.Conditions;
import org.xxdevplus.frmlng.Conditions;


/**
 *
 * @author GeTr
 */

public class Udf
{

 public static String Exec(String xmlFile, String name, String param)  throws Exception { return new Udf(xmlFile).exec(name, param); }

 private String xmlDefFile;
 private String xmlDefPath;
 private Chain xmlDef;

 private String Proper(String x) { return x.substring(0, 1).toUpperCase() + x.substring(1); }

 public String sourceCode(String name, int type) throws Exception
 {
  Chain cSharp  = new Chain("");
  cSharp = cSharp.plus("\n");
  cSharp = cSharp.plus("\n");
  cSharp = cSharp.plus("/**\n");
  cSharp = cSharp.plus(" *\n");
  cSharp = cSharp.plus(" * generated source code, do not change\n");
  cSharp = cSharp.plus(" */\n");
  cSharp = cSharp.plus("\n");
  cSharp = cSharp.plus("using System;\n");
  cSharp = cSharp.plus("using System.Collections.Generic;\n");
  cSharp = cSharp.plus("using System.Linq;\n");
  cSharp = cSharp.plus("using System.Text;\n");
  cSharp = cSharp.plus("\n");
  cSharp = cSharp.plus("using ndBase;\n");
  cSharp = cSharp.plus("using ndString;\n");
  cSharp = cSharp.plus("using ndData;\n");
  cSharp = cSharp.plus("using ndUdf;\n");
  cSharp = cSharp.plus("\n");
  cSharp = cSharp.plus("public class " + name + "Call\n");
  cSharp = cSharp.plus("{\n");
  cSharp = cSharp.plus(" private static string  parserName = \"" + name + "\";\n");
  cSharp = cSharp.plus(" private static bool    tested     = false;\n");
  cSharp = cSharp.plus(" private UdfDisp        Udf        = null;\n");
  cSharp = cSharp.plus("\n");

  Chain Java    = new Chain("");
  Java = Java.plus("\n");
  Java = Java.plus("\n");
  Java = Java.plus("/**\n");
  Java = Java.plus(" *\n");
  Java = Java.plus(" * generated source code, do not change\n");
  Java = Java.plus(" */\n");
  Java = Java.plus("import org.getr.base.Pile;\n");
  Java = Java.plus("import org.getr.string.Reach;\n");
  Java = Java.plus("import org.getr.data.Tag;\n");
  Java = Java.plus("\n");
  Java = Java.plus("public class " + name + "Call\n");
  Java = Java.plus("{\n");
  Java = Java.plus("\n");
  Java = Java.plus(" private static String  parserName = \"" + name + "\";\n");
  Java = Java.plus(" private static boolean tested     = false;\n");
  Java = Java.plus(" private UdfDisp        Udf        = null;\n");
  Java = Java.plus("\n");

  Tag def = new Tag(getParser(name, 0), true);
  Tag requestDef   = def.tags(2, new Pile<String>("", 0, "[type]==request"  )).g(1);
  Tag parserDef    = def.tags(2, new Pile<String>("", 0, "[type]==parser"   )).g(1);
  Tag responseDef  = def.tags(2, new Pile<String>("", 0, "[type]==response" )).g(1);

  KeyPile<String, Chain> rqParams = new KeyPile<String, Chain>();
  KeyPile<String, Chain> rpParams = new KeyPile<String, Chain>();

  int ctr = 0;
  while (true)
  {
   Pile<Tag> found = requestDef.tags(1, new Pile<String>("", 0, "[type]==param"), new Pile<String>("", 0, "key==" + (++ctr) + ""));
   if (found.Len() == 0) break;
   Tag paramTag = found.g(1);
   String key = paramTag.txt().after(1, "[").before(1, "]").text();
   String test = "";
   if (paramTag.attr().hasKey("test")) test = paramTag.attr().g("test").Value().text();
   rqParams.Add(key, new Chain(test));
  }

  for (Tag t : responseDef.tags(1, new Pile<String>("", 0, "[type]==param")))
  {
   String key = t.attr().g("name").Value().text();
   String test = "";
   if (t.attr().hasKey("test")) test = t.attr().g("test").Value().text();
   rpParams.Add(key, new Chain(test));
  }

  for (String paramName : rpParams.Keys())
  {
   cSharp = cSharp.plus(" private Reach m_" + Proper(paramName) + ";\n");
   Java = Java.plus(" private Reach m_" + Proper(paramName) + ";\n");
  }
  cSharp = cSharp.plus("\n");
  Java = Java.plus("\n");

  for (String paramName : rpParams.Keys())
  {
   cSharp = cSharp.plus(" public Reach " + Proper(paramName) + "                   { get { return m_" + Proper(paramName) + ";                } }\n");
   Java = Java.plus(new Chain(" public Reach get" + Proper(paramName) + "                () { return m_" + Proper(paramName) + ";                }\n"));
  }

  cSharp = cSharp.plus(new Chain("\n"));
  Java = Java.plus(new Chain("\n"));
  cSharp = cSharp.plus(" public " + name + "Call(UdfDisp Udf)\n");
  Java = Java.plus(" public " + name + "Call(UdfDisp Udf) throws Exception\n");
  cSharp = cSharp.plus(" {\n");
  Java = Java.plus(" {\n");
  cSharp = cSharp.plus("  this.Udf = Udf;\n");
  Java = Java.plus("  this.Udf = Udf;\n");
  cSharp = cSharp.plus("  if (!tested)\n");
  Java = Java.plus("  if (!tested)\n");
  cSharp = cSharp.plus("  {\n");
  Java = Java.plus("  {\n");
  cSharp = cSharp.plus("   tested = true;\n");
  Java = Java.plus("   tested = true;\n");

  String testParams = "";
  for (String paramName : rpParams.Keys()) {testParams += rpParams.g(paramName).text();}
  if (testParams.length() == 0)
  {
   cSharp = cSharp.plus("  }\n");
   Java = Java.plus("  }\n");
  }
  else
  {

   for (String paramName : rpParams.Keys())
   {
    cSharp = cSharp.plus("   bool p_" + Proper(paramName) + " = false;\n");
    Java = Java.plus("   boolean p_" + Proper(paramName) + " = false;\n");
   }

   Chain execParams = new Chain("");
   for (Chain test : rqParams) { execParams = execParams.plus("\"").plus(test).plus("\", "); }
   execParams = execParams.upto(-3);

   cSharp = cSharp.plus("   foreach (" + name + "Call res in exec( " + execParams.text() + "))\n");
   Java = Java.plus("   for (" + name + "Call res : exec( " + execParams.text() + "))\n");
   cSharp = cSharp.plus("   {\n");
   Java = Java.plus("   {\n");

   String passedCondition = "";
   for (String paramName : rpParams.Keys())
   {
    String test = rpParams.g(paramName).text();
    passedCondition += " && p_" + Proper(paramName);
    cSharp = cSharp.plus("    if (res." + Proper(paramName) + ".Equals(\"" + test + "\"))       p_" + Proper(paramName) + " = true;\n");
    Java = Java.plus("    if (res.get" + Proper(paramName) + "().Equals(\"" + test + "\"))  p_" + Proper(paramName) + " = true;\n");
   }
   passedCondition = "(!(" + passedCondition.substring(4) + "))";
   cSharp = cSharp.plus("   }\n");
   Java = Java.plus("   }\n");
   cSharp = cSharp.plus("   if " + passedCondition + " throw new Exception(\"" + name + "Call SelfTest FAILED! Check test values in " + name + ".xml against the WebPage you get from the url! (has the html structure changed?)\");\n");
   Java = Java.plus("   if " + passedCondition + " throw new Exception(\"" + name + "Call SelfTest FAILED! Check test values in " + name + ".xml against the WebPage you get from the url! (has the html structure changed?)\");\n");
   cSharp = cSharp.plus("  }\n");
   Java = Java.plus("  }\n");
  }
  cSharp = cSharp.plus(" }\n");
  Java = Java.plus(" }\n");
  cSharp = cSharp.plus("\n");
  Java = Java.plus("\n");

  Chain cSharpParams = new Chain("");
  Chain javaParams = new Chain("");
  for (String key : rqParams.Keys()) { cSharpParams = cSharpParams.plus("string ").plus(key).plus(", "); javaParams = javaParams.plus("String ").plus(key).plus(", "); }
  cSharpParams = cSharpParams.upto(-3);
  javaParams = javaParams.upto(-3);

  cSharp = cSharp.plus(" public Pile<" + name + "Call> exec(" + cSharpParams.text() + ")\n");
  Java = Java.plus(" public Pile<" + name + "Call> exec(" + javaParams.text() + ") throws Exception\n");
  cSharp = cSharp.plus(" {\n");
  Java = Java.plus(" {\n");
  cSharp = cSharp.plus("  string callParams = \"\";\n");
  Java = Java.plus("  String callParams = \"\";\n");

  for (String key : rqParams.Keys())
  {
   cSharp = cSharp.plus("  callParams += \"<param name=\\\"" + key + "\\\" value=\\\"\" + " + key + " + \"\\\"/>\";\n");
   Java = Java.plus("  callParams += \"<param name=\\\"" + key + "\\\" value=\\\"\" + " + key + " + \"\\\"/>\";\n");
  }
  cSharp = cSharp.plus("  Tag result = new Tag(Udf.Exec(parserName, callParams), true);\n");
  Java = Java.plus("  Tag result = new Tag(Udf.exec(parserName, callParams), true);\n");
  cSharp = cSharp.plus("  Pile<" + name + "Call> ret = new Pile<" + name + "Call>(0);\n");
  Java = Java.plus("  Pile<" + name + "Call> ret = new Pile<" + name + "Call>(0);\n");
  cSharp = cSharp.plus("  ret.Name = result.struc(-1);\n");
  Java = Java.plus("  ret.setName(result.struc(-1));\n");
  cSharp = cSharp.plus("  foreach (Tag tr in result.tags(1, new Pile<string>(\"\", \"[type]==tr\")))\n");
  Java = Java.plus("  for (Tag tr : result.tags(1, new Pile<String>(\"\", \"[type]==tr\")))\n");
  cSharp = cSharp.plus("  {\n");
  Java = Java.plus("  {\n");
  cSharp = cSharp.plus("   " + name + "Call res = new " + name + "Call(Udf);\n");
  Java = Java.plus("   " + name + "Call res = new " + name + "Call(Udf);\n");
  int rpCtr = 0;
  for (String key : rpParams.Keys())
  {
   rpCtr++;
   cSharp = cSharp.plus("   res.m_" + Proper(key) + " = tr.Tags[" + rpCtr + "].txt;\n");
   Java = Java.plus("   res.m_" + Proper(key) + " = tr.Tags().get(" + rpCtr + ").txt();\n");
  }
  cSharp = cSharp.plus("   ret.Add(res);\n");
  Java = Java.plus("   ret.add(res);\n");
  cSharp = cSharp.plus("  }\n");
  Java = Java.plus("  }\n");
  cSharp = cSharp.plus("  return ret;\n");
  Java = Java.plus("  return ret;\n");
  cSharp = cSharp.plus(" }\n");
  Java = Java.plus(" }\n");
  cSharp = cSharp.plus("}\n");
  Java = Java.plus("}\n");
  cSharp = cSharp.plus("\n");
  Java = Java.plus("\n");
  cSharp = cSharp.plus("\n");
  Java = Java.plus("\n");
  cSharp = cSharp.plus("\n");
  Java = Java.plus("\n");

  switch (type)
  {
   case 1: return cSharp.text();
   case 2: return Java.text();
  }
  return "";
 }

 private String prepare(String name, String param, KeyPile<String, Tag> paramSet, Pile<Tag> rsDefSet, KeyPile<String, Conditions> rsCondSet, KeyPile<String, Tag> responseSet, KeyPile<String, Tag> results, String debug) throws Exception
 {

  Tag def = new Tag(getParser(name, 0), true);
  Tag requestDef   = def.tags(2, new Pile<String>("", 0, "[type]==request"  )).g(1);
  Tag parserDef    = def.tags(2, new Pile<String>("", 0, "[type]==parser"   )).g(1);
  Tag responseDef  = def.tags(2, new Pile<String>("", 0, "[type]==response" )).g(1);

  KeyPile<String, Tag> resultSetDefinitions = new KeyPile<String, Tag>();
  String url = parserDef.tags(2, Tag.where("[type]==url")).g(1).txt().text();
  for (Tag t : parserDef.tags(2, Tag.where("[type]==resultset"))) resultSetDefinitions.Add(t.attr().g("name").Value().text(), t);

  for (String key : resultSetDefinitions.Keys()) rsDefSet.Add(resultSetDefinitions.g(key));

  for (Tag t : responseDef.tags(2, Tag.where("[type]==param"))) responseSet.Add(t.attr().g("name").Value().text(), t);
  KeyPile<Integer, Tag> paramList = new KeyPile<Integer, Tag>();
  for (Tag t : requestDef.tags(2, Tag.where("[type]==param"))) paramList.Add(Integer.parseInt(t.attr().g("key").Value().text()), t);
  for (int key : paramList.kAsc()) paramSet.Add(paramList.g(key).txt().after(1, "[").before(1, "]").text(), paramList.g(key));


  Pile<Tag> qParams = new Tag(param, true).tags(2, Tag.where("[type]==param"));
   String[] qp = new String[qParams.Len()];
  for (int i = 1; i <= qParams.Len(); i++)
  {
   Tag t = qParams.g(i);
   qp[i - 1] = t.attr().g("name").Value().text() + "=" + t.attr().g("value").Value().text();
  }

  for(String worddef : qp)
  {
   Chain wDef = new Chain(worddef);
   String word = wDef.after(1, "=").text();
   String key = wDef.before(1, "=").text();
   int pos = 0;
   for (int i = 1; i <= paramSet.Len(); i++) if (paramSet.Keys().g(i).equals(key)) { pos = i; break; }
   url = url.replace("[" + pos + "]", paramSet.g(key).txt().text().replace("[" + key + "]", word));
  }
  url = url.replace("&amp;", "&");
  for(int i = 1; i <= paramSet.Len(); i++) url = url.replace("[" + i + "]", "");

  for (int rset = 1; rset <= rsDefSet.Len(); rset++)
  {
   if (debug.equals("base")) break;
   Tag rsDef = rsDefSet.g(rset);
   Pile<Pile<String>> rsFilters = new Pile<Pile<String>>(0);

   for (Tag filterDef : rsDef.tags(2, Tag.where("[type]==filter")))
   {
    Pile<String> rsPatterns = new Pile<String>(0);
    for (Tag patternDef : filterDef.tags(2, Tag.where("[type]==pattern")))
    {
     boolean neg     = patternDef.attr().g("neg").Value().Equals("1");
     boolean csens   = patternDef.attr().g("csens").Value().Equals("1");
     boolean rexp    = patternDef.attr().g("rexp").Value().Equals("1");
     String key      = patternDef.attr().g("key").Value().text().replace("&gt;", ">").replace("&lt;", "<");
     String val      = patternDef.txt().text();
     String cmp      = "";
     if (neg) cmp    = "!";
     if (!csens) cmp += "°";
     if (rexp) cmp   += "~"; else cmp += "=";
     if (csens) cmp  += cmp.substring(cmp.length() - 1);
     rsPatterns.Add(key + cmp + val);
    }
    rsFilters.Add(rsPatterns);
   }
   rsCondSet.Add(rsDef.attr().g("base").Value().text() + "-" + rsDef.attr().g("name").Value().text(), new Conditions(rsDef.attr().g("base").Value().text(), rsDef.attr().g("name").Value().text(), rsDef.tags(2, Tag.where("[type]==sequence")).g(1).attr().g("include").Value().text().trim(), rsFilters));
   if (debug.equals(rsDef.attr().g("name").Value().text())) break;
  }
  Tag doc = new Tag(Chain.LoadUri(url), false);
  //for (int i = 1; i <= doc.Tags().len(); i++) doc.Tags().get(i).setName("base:" + i);
  results.Add("base", doc);
  return url;
 }

 private int execCounter = 0;

 public String exec(String name, String param) throws Exception
 {

  String ret = "";
  String debug = ""; Tag debugDef = null; try { debugDef = new Tag(param, true).tags(2, Tag.where("[type]==debug")).g(1); } catch (Exception ex) { } if (debugDef != null) debug = debugDef.attr().g("name").Value().text();

  KeyPile<String, Tag>                            paramSet    = new KeyPile<String, Tag>();     //Dictionary<string, Tag> paramSet = new Dictionary<string, Tag>();
  Pile<Tag>                                       rsDefSet    = new Pile<Tag>();
  KeyPile<String, Conditions>                     rsCondSet   = new KeyPile<String, Conditions>();
  KeyPile<String, Tag>                            responseSet = new KeyPile<String, Tag>();  //Dictionary<string, Tag> responseSet = new Dictionary<string, Tag>();
  KeyPile<String, Tag>                            results     = new KeyPile<String,Tag>();
  KeyPile<String, Pile<NamedValue<String, Tag>>>  resSet      = new KeyPile<String, Pile<NamedValue<String, Tag>>>();
  String url = prepare(name, param, paramSet, rsDefSet, rsCondSet, responseSet, results, debug);

  Pile<NamedValue<String, Tag>> baseSet = new Pile<NamedValue<String, Tag>>(0);
  for (Tag t : results.g(1).Tags()) baseSet.Add(new NamedValue<String, Tag>("base:" + (baseSet.Len() + 1), t));
  resSet.Add("base", baseSet);
  results.g(1).tags(-1, 1, rsCondSet, resSet);

  ret += "<tr>\n<td>" + (++execCounter) + "</td></tr>\n";
  for (NamedValue<String, Tag> nt : resSet.g(-1))
  {
   ret += "<tr>\n";
   if (debug.length() == 0)
   {
    for (Tag rDef : responseSet)
    {
     Chain val;
     Chain valDef = new Chain(rDef.attr().g("value").Value().text().replace("&lt;", "<").replace("&gt;", ">").toLowerCase());
     Chain delim = valDef.at(1, ".");
     if ((delim.len() > 0) && (!valDef.before(delim).Equals(resSet.Keys().g(-1))))
     {
      NamedValue<String, Tag> baseTag = nt;
      while (true) { Chain baseName = new Chain(baseTag.Name()); int num = Integer.parseInt(baseName.after(1, ":").text()); baseTag = resSet.g(baseName.before(1, ":").text()).g(num); if (baseName.before(1, ":").Equals(valDef.before(delim))) { val = baseTag.Value().val(valDef.after(delim).text()); break; } }
     } 
     else if (delim.len() > 0) val = nt.Value().val(valDef.after(delim).text()); else val = nt.Value().val(valDef.text());
     ret += " <td>" + val.text().replace("\n", "").replace("\r", "") + " </td>\n";
    }
    ret += "</tr>\n";
   } 
   else ret += nt.Value().struc(-1) + "</tr>\n";
  }
  return "<url>" + url + "</url>" + ret;

 }

 public Udf(String xmlFile) throws Exception
 {
  xmlDefFile = xmlFile;
  xmlDefPath = new File(xmlFile).getParent();
  xmlDef = Chain.LoadUri(xmlFile);
 }

 public void setParser(String def) throws Exception
 {
  Tag parserDef = new Tag(def, true);
  String name = parserDef.tags(1, new Pile<String>("", 0, "[type]==parser")).g(1).attr().g("name").Value().text();
  utl.string2File(parserDef.struc(-1), xmlDefPath + "/parsers/" + name + ".xml");
 }

 public String getParser(String name, int type) throws Exception
 {
  switch (type) // 0:xml 1:c# 2:java
  {
   case 0: return Chain.LoadUri(xmlDefPath + "/parsers/" + name + ".xml").text();
   case 1: return sourceCode(name, type);
   case 2: return sourceCode(name, type);
  }
  return "";
 }

 public String listMeth() throws Exception
 {
  String ret = "";
  File[] files = new File(xmlDefPath + "/parsers").listFiles();
  for(File f : files) if (f.getName().toLowerCase().endsWith(".xml")) ret += "<tr>" + f.getName().substring(0, f.getName().length() - 4) + "</tr>";
  return ret;
 }

}















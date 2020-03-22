using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Xml;
using System.IO;
using System.Net;

using ndBase;
using ndString;

namespace ndData
{
  
 public class Udf //Universal Data Filter
 {
  //Copyright© 1996-2012 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties, You may use, modify, distribute this code under GNU public licence for NON-Commercial use as long as this notice remains unchanged.
  private static bool selfTested  = false;  private static string ass(bool expr) { if (!expr) throw new Exception(ass(true) + " SelfTest Failure"); return "Udf"; } private static void selfTest() { selfTested = true; } private void init() { if (!selfTested) selfTest(); }

  public static string Exec(string xmlFile, string name, string param) { return new Udf(xmlFile).Exec(name, param); }

  private Reach xmlDef;
  private string xmlDefPath;
  private string xmlDefFile;

  private string Proper(string x) { return x.Substring(0, 1).ToUpper() + x.Substring(1); }

  public string SourceCode(string name, long type)
  {
   Reach cSharp  = new Reach("");
   cSharp = cSharp + new Reach("\n");
   cSharp = cSharp + new Reach("\n");
   cSharp = cSharp + new Reach("/**\n");
   cSharp = cSharp + new Reach(" *\n");
   cSharp = cSharp + new Reach(" * generated source code, do not change\n");
   cSharp = cSharp + new Reach(" */\n");
   cSharp = cSharp + new Reach("\n");
   cSharp = cSharp + new Reach("using System;\n");
   cSharp = cSharp + new Reach("using System.Collections.Generic;\n");
   cSharp = cSharp + new Reach("using System.Linq;\n");
   cSharp = cSharp + new Reach("using System.Text;\n");
   cSharp = cSharp + new Reach("\n");
   cSharp = cSharp + new Reach("using ndBase;\n");
   cSharp = cSharp + new Reach("using ndString;\n");
   cSharp = cSharp + new Reach("using ndData;\n");
   cSharp = cSharp + new Reach("using ndUdf;\n");
   cSharp = cSharp + new Reach("\n");
   cSharp = cSharp + new Reach("public class " + name + "Call\n");
   cSharp = cSharp + new Reach("{\n");
   cSharp = cSharp + new Reach(" private static string  parserName = \"" + name + "\";\n");
   cSharp = cSharp + new Reach(" private static bool    tested     = false;\n");
   cSharp = cSharp + new Reach(" private UdfDisp        Udf        = null;\n");
   cSharp = cSharp + new Reach("\n");

   Reach Java    = new Reach("");
   Java = Java + new Reach("\n");
   Java = Java + new Reach("\n");
   Java = Java + new Reach("/**\n");
   Java = Java + new Reach(" *\n");
   Java = Java + new Reach(" * generated source code, do not change\n");
   Java = Java + new Reach(" */\n");
   Java = Java + new Reach("import org.getr.ndBase.Pile;\n");
   Java = Java + new Reach("import org.getr.ndString.Reach;\n");
   Java = Java + new Reach("import org.getr.ndData.Tag;\n");
   Java = Java + new Reach("\n");
   Java = Java + new Reach("public class " + name + "Call\n");
   Java = Java + new Reach("{\n");
   Java = Java + new Reach("\n");
   Java = Java + new Reach(" private static String  parserName = \"" + name + "\";\n");
   Java = Java + new Reach(" private static boolean tested     = false;\n");
   Java = Java + new Reach(" private UdfDisp        Udf        = null;\n");
   Java = Java + new Reach("\n");

   Tag def = new Tag(GetParser(name, 0), true);
   Tag requestDef = def.tags(2, new Pile<string>("", true, "[type]==request"))[1];
   Tag parserDef = def.tags(2, new Pile<string>("", true, "[type]==parser"))[1];
   Tag responseDef = def.tags(2, new Pile<string>("", true, "[type]==response"))[1];

   KeyPile<string, Reach> rqParams = new KeyPile<string, Reach>();
   KeyPile<string, Reach> rpParams = new KeyPile<string, Reach>();

   long ctr = 0;
   while (true)
   {
    Pile<Tag> found = requestDef.tags(1, new Pile<string>("", true, "[type]==param"), new Pile<string>("", true, "key==" + (++ctr) + ""));
    if (found.Len == 0) break;
    Tag paramTag = found[1];
    string key = paramTag.txt.after(1, "[").before(1, "]").text;
    string test = "";
    if (paramTag.attr.hasKey("test")) test = paramTag.attr["test"].Value;
    rqParams.Add(key, test);
   }

   foreach (Tag t in responseDef.tags(1, new Pile<string>("", true, "[type]==param")))
   {
    string key = t.attr["name"].Value;
    string test = "";
    if (t.attr.hasKey("test")) test = t.attr["test"].Value;
    rpParams.Add(key, test);
   }
   
   foreach (string paramName in rpParams.Keys)
   {
    cSharp = cSharp + new Reach(" private Reach m_" + Proper(paramName) + ";\n");
    Java = Java + new Reach(" private Reach m_" + Proper(paramName) + ";\n");
   }
   cSharp = cSharp + new Reach("\n");
   Java = Java + new Reach("\n");

   foreach (string paramName in rpParams.Keys)
   {
    cSharp = cSharp  + new Reach(" public Reach " + Proper(paramName) + "                   { get { return m_" + Proper(paramName) + ";                } }\n");
    Java = Java      + new Reach(" public Reach get" + Proper(paramName) + "                () { return m_" + Proper(paramName) + ";                }\n");
   }
   
   cSharp = cSharp + new Reach("\n");
   Java = Java + new Reach("\n");
   cSharp = cSharp + new Reach(" public " + name + "Call(UdfDisp Udf)\n");
   Java = Java + new Reach(" public " + name + "Call(UdfDisp Udf) throws Exception\n");
   cSharp = cSharp + new Reach(" {\n");
   Java = Java + new Reach(" {\n");
   cSharp = cSharp + new Reach("  this.Udf = Udf;\n");
   Java = Java + new Reach("  this.Udf = Udf;\n");
   cSharp = cSharp + new Reach("  if (!tested)\n");
   Java = Java + new Reach("  if (!tested)\n");
   cSharp = cSharp + new Reach("  {\n");
   Java = Java + new Reach("  {\n");
   cSharp = cSharp + new Reach("   tested = true;\n");
   Java = Java + new Reach("   tested = true;\n");

   string testParams = "";
   foreach (string paramName in rpParams.Keys) {testParams += rpParams[paramName].text;}
   if (testParams.Length == 0)
   {
    cSharp = cSharp + new Reach("  }\n");
    Java = Java + new Reach("  }\n");
   }
   else
   {
    foreach (string paramName in rpParams.Keys)
    {
     cSharp = cSharp + new Reach("   bool p_" + Proper(paramName) + " = false;\n");
     Java = Java + new Reach("   boolean p_" + Proper(paramName) + " = false;\n");
    }
    Reach execParams = new Reach("");
    foreach (Reach test in rqParams) { execParams += new Reach("\"") + test + new Reach("\", "); }
    execParams = execParams.upto(-3);

    cSharp = cSharp + new Reach("   foreach (" + name + "Call res in exec( " + execParams.text + "))\n");
    Java = Java + new Reach("   for (" + name + "Call res : exec( " + execParams.text + "))\n");
    cSharp = cSharp + new Reach("   {\n");
    Java = Java + new Reach("   {\n");

    string passedCondition = "";
    foreach (string paramName in rpParams.Keys)
    {
     string test = rpParams[paramName].text;
     passedCondition += " && p_" + Proper(paramName);
     cSharp = cSharp + new Reach("    if (res." + Proper(paramName) + ".Equals(\"" + test + "\"))       p_" + Proper(paramName) + " = true;\n");
     Java = Java     + new Reach("    if (res.get" + Proper(paramName) + "().Equals(\"" + test + "\"))  p_" + Proper(paramName) + " = true;\n");
    }
    passedCondition = "(!(" + passedCondition.Substring(4) + "))";
    cSharp = cSharp + new Reach("   }\n");
    Java = Java + new Reach("   }\n");
    cSharp = cSharp + new Reach("   if " + passedCondition + " throw new Exception(\"" + name + "Call selfTest FAILED! Check test values in " + name + ".xml against the WebPage you get from the url! (has the html structure changed?)\");\n");
    Java = Java + new Reach("   if " + passedCondition + " throw new Exception(\"" + name + "Call selfTest FAILED! Check test values in " + name + ".xml against the WebPage you get from the url! (has the html structure changed?)\");\n");
    cSharp = cSharp + new Reach("  }\n");
    Java = Java + new Reach("  }\n");
   }

   cSharp = cSharp + new Reach(" }\n");
   Java = Java + new Reach(" }\n");
   cSharp = cSharp + new Reach("\n");
   Java = Java + new Reach("\n");

   Reach cSharpParams = new Reach("");
   Reach javaParams = new Reach("");
   foreach (string key in rqParams.Keys) { cSharpParams += new Reach("string ") + key + new Reach(", "); javaParams += new Reach("String ") + key + new Reach(", "); }
   cSharpParams = cSharpParams.upto(-3);
   javaParams = javaParams.upto(-3);

   cSharp = cSharp + new Reach(" public Pile<" + name + "Call> exec(" + cSharpParams + ")\n");
   Java = Java + new Reach(" public Pile<" + name + "Call> exec(" + javaParams + ") throws Exception\n");
   cSharp = cSharp + new Reach(" {\n");
   Java = Java + new Reach(" {\n");
   cSharp = cSharp + new Reach("  string callParams = \"\";\n");
   Java = Java + new Reach("  String callParams = \"\";\n");

   foreach (string key in rqParams.Keys)
   {
    cSharp = cSharp + new Reach("  callParams += \"<param name=\\\"" + key + "\\\" value=\\\"\" + " + key + " + \"\\\"/>\";\n");
    Java = Java + new Reach("  callParams += \"<param name=\\\"" + key + "\\\" value=\\\"\" + " + key + " + \"\\\"/>\";\n");
   }
   cSharp = cSharp + new Reach("  Tag result = new Tag(Udf.Exec(parserName, callParams), true);\n");
   Java = Java + new Reach("  Tag result = new Tag(Udf.exec(parserName, callParams), true);\n");
   cSharp = cSharp + new Reach("  Pile<" + name + "Call> ret = new Pile<" + name + "Call>(0);\n");
   Java = Java + new Reach("  Pile<" + name + "Call> ret = new Pile<" + name + "Call>(0);\n");
   cSharp = cSharp + new Reach("  ret.Name = result.struc(-1);\n");
   Java = Java + new Reach("  ret.setName(result.struc(-1));\n");
   cSharp = cSharp + new Reach("  foreach (Tag tr in result.tags(1, new Pile<string>(\"\", \"[type]==tr\")))\n");
   Java = Java + new Reach("  for (Tag tr : result.tags(1, new Pile<String>(\"\", \"[type]==tr\")))\n");
   cSharp = cSharp + new Reach("  {\n");
   Java = Java + new Reach("  {\n");
   cSharp = cSharp + new Reach("   " + name + "Call res = new " + name + "Call(Udf);\n");
   Java = Java + new Reach("   " + name + "Call res = new " + name + "Call(Udf);\n");
   long rpCtr = 0;
   foreach (string key in rpParams.Keys)
   {
    rpCtr++;
    cSharp = cSharp + new Reach("   res.m_" + Proper(key) + " = tr.Tags[" + rpCtr + "].txt;\n");
    Java = Java + new Reach("   res.m_" + Proper(key) + " = tr.Tags().get(" + rpCtr + ").txt();\n");
   }
   cSharp = cSharp + new Reach("   ret.Add(res);\n");
   Java = Java + new Reach("   ret.add(res);\n");
   cSharp = cSharp + new Reach("  }\n");
   Java = Java + new Reach("  }\n");
   cSharp = cSharp + new Reach("  return ret;\n");
   Java = Java + new Reach("  return ret;\n");
   cSharp = cSharp + new Reach(" }\n");
   Java = Java + new Reach(" }\n");
   cSharp = cSharp + new Reach("}\n");
   Java = Java + new Reach("}\n");
   cSharp = cSharp + new Reach("\n");
   Java = Java + new Reach("\n");
   cSharp = cSharp + new Reach("\n");
   Java = Java + new Reach("\n");
   cSharp = cSharp + new Reach("\n");
   Java = Java + new Reach("\n");

   switch (type)
   {
    case 1: return cSharp.text;
    case 2: return Java.text;
   }
   return "";
  }

  private string prepare(string name, string param, KeyPile<string, Tag> paramSet, Pile<Tag> rsDefSet, KeyPile<string, Conditions> rsCondSet, KeyPile<string, Tag> responseSet, KeyPile<string, Tag> results, string debug)
  {

   Tag def = new Tag(GetParser(name, 0), true);
   Tag requestDef = def.tags(2, new Pile<string>("", true, "[type]==request"))[1];
   Tag parserDef = def.tags(2, new Pile<string>("", true, "[type]==parser"))[1];
   Tag responseDef = def.tags(2, new Pile<string>("", true, "[type]==response"))[1];

   KeyPile<string, Tag> resultSetDefinitions = new KeyPile<string, Tag>();
   string url = parserDef.tags(2, Tag.where("[type]==url"))[1].txt;
   foreach (Tag t in parserDef.tags(2, Tag.where("[type]==resultset"))) resultSetDefinitions.Add(t.attr["name"].Value.text, t);

   foreach (string key in resultSetDefinitions.Keys) rsDefSet.Add(resultSetDefinitions[key]);

   foreach (Tag t in responseDef.tags(2, Tag.where("[type]==param"))) responseSet.Add(t.attr["name"].Value.text, t);
   KeyPile<long, Tag> paramList = new KeyPile<long, Tag>();
   foreach (Tag t in requestDef.tags(2, Tag.where("[type]==param"))) paramList.Add(Int32.Parse(t.attr["key"].Value.text), t);
   foreach (long key in paramList.kAsc) paramSet.Add(paramList[key].txt.after(1, "[").before(1, "]").text, paramList[key]);

   Pile<Tag> qParams = new Tag(param, true).tags(2, Tag.where("[type]==param"));
   string[] qp = new string[qParams.Len];
   for (int i = 1; i <= qParams.Len; i++)
   {
    Tag t = qParams[i];
    qp[i - 1] = t.attr["name"].Value.text + "=" + t.attr["value"].Value.text;
   }

   foreach (string worddef in qp)
   {
    string word = worddef;
    string key = utl.cutl(ref word, "=");
    long pos = 0;
    for (int i = 1; i <= paramSet.Len; i++) if (paramSet.Keys[i].Equals(key)) { pos = i; break; }
    url = url.Replace("[" + pos + "]", paramSet[key].txt.text.Replace("[" + key + "]", word));
   }
   url = url.Replace("&amp;", "&");
   for (long i = 1; i <= paramSet.Len; i++) url = url.Replace("[" + i + "]", "");
   for (int rset = 1; rset <= rsDefSet.Len; rset++)
   {
    if (debug.Equals("base")) break;
    Tag rsDef = rsDefSet[rset];
    Pile<Pile<string>> rsFilters = new Pile<Pile<string>>();
    foreach (Tag filterDef in rsDef.tags(2, Tag.where("[type]==filter")))
    {
     Pile<string> rsPatterns = new Pile<string>();
     foreach (Tag patternDef in filterDef.tags(2, Tag.where("[type]==pattern")))
     {
      bool neg = patternDef.attr["neg"].Value.Equals("1");
      bool csens = patternDef.attr["csens"].Value.Equals("1");
      bool rexp = patternDef.attr["rexp"].Value.Equals("1");
      string key = patternDef.attr["key"].Value.text.Replace("&gt;", ">").Replace("&lt;", "<");
      string val = patternDef.txt;
      string cmp = "";
      if (neg) cmp = "!";
      if (!csens) cmp += "°";
      if (rexp) cmp += "~"; else cmp += "=";
      if (csens) cmp += cmp.Substring(cmp.Length - 1);
      rsPatterns.Add(key + cmp + val);
     }
     rsFilters.Add(rsPatterns);
    }
    rsCondSet.Add(rsDef.attr["base"].Value + "-" + rsDef.attr["name"].Value, new Conditions(rsDef.attr["base"].Value, rsDef.attr["name"].Value, rsDef.tags(2, Tag.where("[type]==sequence"))[1].attr["include"].Value.text.Trim(), rsFilters));
    if (debug.Equals(rsDef.attr["name"].Value)) break;
    //rsCondSet.Add(rsDef.attr["base"].Item, new Conditions(rsDef.attr["base"].Item, rsDef.attr["name"].Item, rsDef.tags(2, Tag.where("[type]==sequence"))[1].attr["include"].Item.text.Trim(), rsFilters));
   }
   //long Pos1 = ((string)Reach.Load("http://www.livejasmin.com/listpage.php?page=22&livejasmin_session=4678d6bb9242e4bef687d240c293b873&tags=girl&type=40&orderby=0")).IndexOf("allonline_perfnametext");
   Tag doc = new Tag(Reach.Load(url), false);
   //long Pos2 = doc.struc(-1).IndexOf("allonline_perfnametext");
   //for (long i = 1; i <= doc.Tags.Len(); i++) doc.Tags[i].Name = "base:" + i;
   results.Add("base", doc);
   return url;
  }

  private long execCounter = 0;

  public string Exec(string name, string param)
  {

   string ret = "";
   string debug = ""; Tag debugDef = null; try { debugDef = new Tag(param, true).tags(2, Tag.where("[type]==debug"))[1]; } catch (Exception ex) { } if (debugDef != null) debug = debugDef.attr["name"].Value;

   KeyPile<string, Tag>                           paramSet    = new KeyPile<string, Tag>();      //Dictionary<string, Tag> paramSet = new Dictionary<string, Tag>();
   Pile<Tag>                                      rsDefSet    = new Pile<Tag>();
   KeyPile<string, Conditions>                    rsCondSet   = new KeyPile<string, Conditions>();
   KeyPile<string, Tag>                           responseSet = new KeyPile<string, Tag>();      //Dictionary<string, Tag> responseSet = new Dictionary<string, Tag>();
   KeyPile<string, Tag>                           results     = new KeyPile<string, Tag>();
   KeyPile<string, Pile<NamedValue<string, Tag>>>  resSet      = new KeyPile<string, Pile<NamedValue<string, Tag>>>();

   string url = prepare(name, param, paramSet, rsDefSet, rsCondSet, responseSet, results, debug);

   Pile<NamedValue<string, Tag>> baseSet = new Pile<NamedValue<string, Tag>>();
   foreach (Tag t in results[1].Tags) baseSet.Add(new NamedValue<string, Tag>("base:" + (baseSet.Len + 1), t));
   resSet.Add("base", baseSet);
   results[1].tags(-1, 1, rsCondSet, resSet);
   ret += "<tr>\n<td>" + (++execCounter) + "</td></tr>\n";
   foreach (NamedValue<string, Tag> nt in resSet[-1])
   {
    ret += "<tr>\n";
    if (debug.Length == 0)
    {
     foreach (Tag rDef in responseSet)
     {
      Reach val;
      Reach valDef = rDef.attr["value"].Value.text.Replace("&lt;", "<").Replace("&gt;", ">").ToLower();
      Reach delim = valDef.at(1, ".");
      if ((delim.len > 0) && (!valDef.before(delim).Equals(resSet.Keys[-1])))
      {
       NamedValue<string, Tag> baseTag = nt;
       while (true) { Reach baseName = (Reach)baseTag.Name; int num = Int32.Parse(baseName.after(1, ":")); baseTag = resSet[baseName.before(1, ":")][num]; if (baseName.before(1, ":").Equals(valDef.before(delim))) { val = baseTag.Value.val(valDef.after(delim)); break; } }
      } 
      else if (delim.len > 0) val = nt.Value.val(valDef.after(delim)); else val = nt.Value.val(valDef);
      ret += " <td>" + val.text.Replace("\n", "").Replace("\r", "") + " </td>\n";
     }
     ret += "</tr>\n";
    }
    else ret += nt.Value.struc(-1) + "</tr>\n";
   }
   return "<url>" + url + "</url>" + ret;
  }
  
  
  
  public Udf(string xmlFile)
  {
   xmlDefFile = xmlFile;
   xmlDefPath = Path.GetDirectoryName(xmlDefFile);
   xmlDef = Reach.Load(xmlFile);
  }

  public void SetParser(string def)
  {
   Tag parserDef = new Tag(def, true);
   string name = parserDef.tags(1, new Pile<string>("", true, "[type]==parser"))[1].attr["name"].Value.text;
    utl.s2f(parserDef.struc(-1), xmlDefPath + "/parsers/" + name + ".xml", false);
  }

  public string GetParser(string name, long type)
  {
   switch (type) // 0:xml 1:c# 2:java
   {
    case 0: return Reach.Load(xmlDefPath + "/parsers/" + name + ".xml").text;
    case 1: return SourceCode(name, type);
    case 2: return SourceCode(name, type);
   }
   return "";
  }
  
  public string ListMeth()
  {
   string ret = "";
   string[] files = Directory.GetFiles(xmlDefPath + "/parsers", "*.xml");
   foreach (string fPath in files) { string fName = Path.GetFileName(fPath); ret += "<tr>" + fName.Substring(0, fName.Length - 4) + "</tr>"; }
   return ret;
  }
  
 }
 
 
}











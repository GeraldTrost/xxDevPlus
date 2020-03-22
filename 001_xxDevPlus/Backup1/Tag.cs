using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Text.RegularExpressions;

using ndBase;
using ndString;

namespace ndData
{

 public class Tag
 {
  //Copyright© 1996-2012 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties, You may use, modify, distribute this code under GNU public licence for NON-Commercial use as long as this notice remains unchanged.
  private static bool selfTested  = false;  private static string ass(bool expr) { if (!expr) throw new Exception(ass(true) + " SelfTest Failure"); return "Tag"; } private void init() {}

  private static SortedList<string, string> tagDefs = new SortedList<string,string>();

  private static   long              ic = 0; //instance counter, used for selfTest()

  private          bool              isXmlTag;
  private          Reach             def;
  private          Reach             ctn;
  private          Reach             _htm;

  private          Tag               closingTag = null;
  public           Tag               parent;
  public           Tag               root;
  private          Pile<Tag>         all;

  public           Reach             type;
  public           bool              isRelevant; //if yes, the Tag will be child of another Tag. If no then the Tag will be in root.all but it will not appear as child of any other
  private          Reach             attrib;
  private          Pile<Tag>         _tags;
  private          Assoc             _attr;

  private static Zone blindArea = new Zone(new Pile<string>("", true, "\r", "\n"), "\"", "\"", "||:0");

  public Pile<Tag> Tags { get { return this._tags; } }

  public static Pile<string> or(params string[] cond)
  {
   return new Pile<string>("", true, cond);
  }

  public static Pile<string>[] where(params object[] cond)
  {
   Pile<string>[] res = new Pile<string>[cond.Length];
   for (long i = 0; i < cond.Length; i++) if (typeof(string) == cond[i].GetType()) { Pile<string> cnd = new Pile<string>(); cnd.Add((string)cond[i]); res[i] = cnd; } else res[i] = (Pile<string>)cond[i];
   return res;
  }

  internal Pile<Tag> tags(long maxDepth, long level, KeyPile<string, Conditions> condsSet, KeyPile<string, Pile<NamedValue<string, Tag>>> resSet)
  {
   long foundInThisLevel = 0;
   if (resSet.Len> 0) if ((resSet.Len != condsSet.Len + 1)) foreach (Conditions cnd in condsSet) resSet.Add(cnd.name, new Pile<NamedValue<string, Tag>>());
   Pile<Tag> ret = new Pile<Tag>();
   if (condsSet.Len < level) return ret;
   Conditions conds = condsSet[(int)level];
   //conds.reset();
   if (maxDepth == 0) return ret;
   foreach (Tag t in _tags)
   {
    if (conds.matches(t))
    {
     ret.Add(t);
     string tagBase = "";
     if (resSet.Len > 0) 
     {
      if (conds.baseName.Length > 0) if (level == 1) tagBase = "base:" + ++foundInThisLevel; else tagBase = condsSet[(int)level - 1].name + ":" + resSet[condsSet[(int)level - 1].name].Len;
      resSet[(int)level + 1].Add(new NamedValue<string, Tag>(tagBase, t)); 
     }
     if (level < condsSet.Len)
     {
      Tag baseTag = t;
      NamedValue<string, Tag> namedBaseTag = new NamedValue<string, Tag>(tagBase, baseTag);
      if (!condsSet[(int)level + 1].baseName.Equals(condsSet[(int)level].name))
      {
       Reach baseName;
       int num;
       while (!condsSet[(int)level + 1].baseName.Equals(((Reach)namedBaseTag.Name).before(1, ":"))) { baseName = (Reach)namedBaseTag.Name; num = Int32.Parse(baseName.after(1, ":")); namedBaseTag = resSet[baseName.before(1, ":")][num]; }
       baseName = (Reach)namedBaseTag.Name; num = Int32.Parse(baseName.after(1, ":")); namedBaseTag = resSet[baseName.before(1, ":")][num];
      }
      baseTag = namedBaseTag.Value;
      Pile<Tag> subLevelResults = null;
      condsSet[(int)level + 1].reset();
      subLevelResults = baseTag.tags(-1, level + 1, condsSet, resSet);
     }
    } 
    else
    {
     Pile<Tag> subResults = null;
     subResults = t.tags(maxDepth - 1, level, condsSet, resSet);
     if (subResults.Len > 0) ret.Add(subResults);
    }
   }
   return ret;
  }

  public Pile<Tag> tags(long maxDepth, params Pile<string>[] cond)
  {
   KeyPile<string, Pile<NamedValue<string, Tag>>> resSet = new KeyPile<string, Pile<NamedValue<string, Tag>>>();
   KeyPile<string, Conditions> condsSet = new KeyPile<string, Conditions>();
   condsSet.Add("", new Conditions("", "", "*", cond));
   //if (resSet.Count == 0) resSet.Add("base", new Pile<Tag>());
   return tags(maxDepth, 1, condsSet, resSet);
  }

  public Assoc attr
  {
   get
   {
    if (attrib == null) attrib = def.after(type).upto(-2).lTrim().rTrim();
    if (_attr == null) _attr = new Assoc(attrib, "=", " ", "\"", blindArea);  //this is most expensive - 1 minute when called 4000 times
    return _attr;
   }
  }

  public Reach txt { get { Reach ret = ctn.before(1, "<"); foreach (Tag tag in _tags) ret += tag.txt; return ret; } }
  public Reach code { get { if (type.len > 0) { Reach ret = _htm;                  foreach (Tag tag in _tags) ret += tag.code; return ret; } else { return ctn; } } }

  public string struc(long indent) 
  {
   string ret = new string(' ', Math.Max(0, (int)indent));
   if (this != root) ret += def + ctn.before(1, "<");
   string lastType = "";
   foreach (Tag t in _tags)
   {
    if ((t.type.len == 0) && ((t.def + t.ctn.before(1, "<")).len > 0)) ret+= t.def + t.ctn.before(1, "<"); else if (t.type.text.ToLower().Equals("/" + lastType)) ret +=  t.def; else ret += "\n".Substring(0, Math.Min(1, ret.Length)) + t.struc(indent + 1) ; 
    lastType = t.type.text.ToLower();
   }
   if (closingTag != null) if (closingTag != this) ret += closingTag.def; else if (!type.Equals("") && !type.Equals("!") && !type.startsWith("/")) if (!def.at(-2).equals("/")) ret += "</" + type + ">";
   return ret;
  }

  /*
  private void getTags(Pile<Tag> target, string kind)
  {
   for (long i = 1; i <= _tags.Count; i++)
    if ((_tags[i]).type) //((tags[i]).type != null)
    {
     if ((_tags[i]).type.Equals(kind)) target.Add((Tag)_tags[i]);
     _tags[i].getTags(target, kind);
    }
  }

  private void getTags(Pile<Tag> target, string kind, string key, string value)
  {
   for (long i = 1; i <= _tags.Count; i++)
    if ((_tags[i]).type) //((tags[i]).type != null)
    {
     if ((kind.Length == 0) || ((_tags[i]).type.Equals(kind))) if (_tags[i].attr.ContainsKey(key)) if (_tags[i].attr[key].Value.Equals(value)) target.Add((Tag)_tags[i]);
     _tags[i].getTags(target, kind, key, value);
    }
  }
  */ 

  public Reach val(string symbol) 
  {
   if (symbol.Length == 0) return this.type.upto(0);
   if (symbol.Equals("[type]")) return this.type;
   if (symbol.Equals("[text]")) return this.txt;
   if (symbol.Equals("[html]")) return this.code;
   if (this.attr.hasKey(symbol.ToLower())) return this.attr[symbol.ToLower()].Value; 
   return this.type.upto(0);
  }
 
  private bool isWhiteSpace() 
  {
   if (type.len > 0) return false;
   string t = txt.text;
   for(long i = 0; i < t.Length; i++)
   {
    switch (t[(int)i])
    {
    case ' ':  break;
    case '\t': break;
    case '\r': break;
    case '\n': break;
    default:   return false;
    }
   }
   return true;
  }

  private void init(Reach src, Tag parent, bool xml)
  {
   isRelevant = true;
   type = src.upto(0);   //AttGeTr: type was null in new Tag("", false)
   ctn = src.upto(0);    //AttGeTr: ctn was null in some TestCase ...
   isXmlTag = xml;
   _tags = new Pile<Tag>();
   this.parent = parent;
   def = src;
   if (parent == null)
   {
    root = this;
    all = new Pile<Tag>();
    int done = 0;
    while (done < def.len)
    {
     all.Add(new Tag(def.after(done), this, xml));
     if (all[-1].isWhiteSpace()) all[-1].isRelevant = false;
     if (all.Len == 1)
      all[-1].parent = this;
     else
     {
      if (all[-1].type.startsWith("/"))
      {
       for (int i = all.Len - 1; i > 0; i--) if ((all[-1].type.Equals("/" + all[i].type.text)) && (all[i].closingTag == null)) { all[i].closingTag = all[-1]; all[-1].parent = all[i].parent; break; }
       if (all[-1].parent == null) all[-1].parent = all[-2].parent; // orphaned closing tag
      }
      else
      {
       if ((all[-1].type.len == 0) && (!all[-2].type.startsWith("/"))) // new definition: a "plain text tag" may be contained in any tag but may not always be relevant ...
       {
        if (!all[-2].mayContain(all[-1].type.text)) all[-1].isRelevant = false;         
        if (all[-2].closingTag == null) all[-1].parent = all[-2]; else all[-1].parent = all[-2].parent;
       }
       else
       {
        if ((all[-2].mayContain(all[-1].type.text))) all[-1].parent = all[-2]; else { all[-1].parent = all[-2].parent; if (all[-2].closingTag == null) all[-2].closingTag = all[-2]; }
       }
      }
     }
     done = done + all[-1]._htm.len;
    }
    for (int i = 1; i <= all.Len; i++)
    {
     if ((all[i].isRelevant) && (!all[i].type.startsWith("/"))) all[i].parent._tags.Add(all[i]);
    }

   }
   else
   {
    root = parent.root;
    if (def.startsWith("<"))
    {
     def = def.upto(1, ">");
     type = def.after(1).before(-1).before(1, " ");
     if (type.startsWith("!")) if (type.startsWith("!--")) type = type.upto(3); else type = type.upto(1);
     if (type.endsWith("/")) { type = type.before(-1); }
     if (type.Equals("!--"))
     {
      closingTag = this;
      type = type.upto(1);
      def = root.def.from(def).upto(1, "-->");
      ctn = def.after(-1).upto(0);
      _htm = def;
      attrib = def.after(type).upto(-2).lTrim().rTrim(); if (attrib.endsWith("/")) { closingTag = this; attrib = attrib.before(-1); }
     }
     else
     {
      if (type.startsWith("!")) { closingTag = this; type = type.upto(1); }
      if (type.Equals("script"))
      {
       ctn = root.def.from(def).Before(1, "</script");
       _htm = ctn.upto(-1); //htm = def + ctn + root.def.after(ctn).upto(1, ">");
       attrib = def.after(type).upto(-2).lTrim().rTrim(); if (attrib.endsWith("/")) { closingTag = this; attrib = attrib.before(-1); }
      }
      else
      {
       ctn = root.def.after(def).upto(0);
       _htm = def.upto(-1); //_htm = def.plus(ctn); 
       attrib = def.after(type).upto(-2).lTrim().rTrim(); if (attrib.endsWith("/")) { closingTag = this; attrib = attrib.before(-1); }
      }
     }
    }
    else
    {
     closingTag = this;
     ctn = def.before(1, "<");
     type = def.upto(0);
     def = def.upto(0);
     attrib = def.upto(0);
     _htm = ctn.upto(-1);
    }
   }
  }
  
  private Tag(Reach sourceString, Tag parent, bool xml) { init(sourceString, parent, xml); }
  public Tag(Reach source, bool xml) { if (ic++ == 0) selfTest(); init(source, null, xml); }
  
  
  public bool mayContain(string type) 
  {
   try
   {
    //string test = root.def.from(this.type).upto(40).text;
    //if (test.Length > 30) if (test.Substring(0, 30).IndexOf("begin prepage section") > -1) type = type;
    if (closingTag != null) return false;
    if ((this.type.startsWith("/")) || (this.type.startsWith("!"))) return false;
    if (this.type.len == 0) return (this == root);
    if (isXmlTag) return true;
    string key = this.type.text.ToLower();
    if (key.Equals("body")) return true;    // override the default, be more tolerant to incorrect html code
    if (key.Equals("meta")) return false;   // return (!type.ToLower().Equals("meta"));
    return (tagDefs.ContainsKey(key)) ? (tagDefs[key].IndexOf("," + type.ToLower() + ",") > -1) : false;
   }
   catch (Exception ex) { throw ex; }
  }

   private static void ass(bool expr, string msg) { if (!expr) throw new Exception("Error in Tag.selfTest(): " + msg); }
   private static void ass(string e1, string e2, string msg) { ass(e1.Equals(e2), msg); }
   private static void ass(double e1, double e2, string msg) { ass(e1 == e2, msg); }

  private static void tstSimple()
  {
   Tag doc = new Tag("", false);
   ass(doc.all.Len, 0, "tstSimple.a");
   //Tag tag = new Tag(" \n ", this, false);
   //ass(tag.type, "", "tstSimple.b");
   //ass(tag.def, "", "tstSimple.c");
   //ass(tag.ctn, " \n ", "tstSimple.d");
   doc = new Tag("<html><body>abc</body></html>", false);
   ass(doc.all.Len, 5, "tstSimple.0");
   ass(doc.txt, "abc", "tstSimple.0");
   ass(doc.all[1].type, "html", "tstSimple.1");
   ass(doc.all[2].type, "body", "tstSimple.2");
   ass(doc.all[3].type, "", "tstSimple.3");
   ass(doc.all[4].type, "/body", "tstSimple.4");
   ass(doc.all[5].type, "/html", "tstSimple.5");
   ass(doc._tags[1].type, "html", "tstSimple.6");
   ass(doc._tags[1]._tags[1].type, "body", "tstSimple.7");
   ass(doc._tags[1]._tags[1]._tags[1].ctn, "abc", "tstSimple.8");
   ass(doc.struc(-1), "<html>\n <body>abc</body></html>", "tstSimple.9");
   doc = new Tag("<tr>\n <td name=\"1\"/>\n <td name=\"2\"/>\n</tr>\n<tr>\n <td name=\"1\"/>\n <td name=\"2\"/>\n</tr>\n<tr>\n <td name=\"1\"/>\n <td name=\"2\"/>\n</tr>", true);
   ass(doc.struc(-1), "<tr>\n <td name=\"1\"/>\n <td name=\"2\"/></tr>\n<tr>\n <td name=\"1\"/>\n <td name=\"2\"/></tr>\n<tr>\n <td name=\"1\"/>\n <td name=\"2\"/></tr>", "tstSimple.10");
  }
  
  private static void selfTest()
  {
   //Class Constructor:
   tagDefs.Add("", "");
   tagDefs.Add("!", "");
   tagDefs.Add("a", ",,!,tt,i,b,big,small,center,font,nobr,i,u,em,strong,dfn,code,samp,kbd,var,cite,abbr,acronym,img,object,br,script,map,q,sub,sup,span,bdo,input,select,textarea,label,button,");
   tagDefs.Add("abbr", ",,!,tt,i,b,big,small,center,font,nobr,i,u,em,strong,dfn,code,samp,kbd,var,cite,abbr,acronym,a,img,object,br,script,map,q,sub,sup,span,bdo,input,select,textarea,label,button,");
   tagDefs.Add("acronym", ",,!,tt,i,b,big,small,center,font,nobr,i,u,em,strong,dfn,code,samp,kbd,var,cite,abbr,acronym,a,img,object,br,script,map,q,sub,sup,span,bdo,input,select,textarea,label,button,");
   tagDefs.Add("address", ",,!,tt,i,b,big,small,center,font,nobr,i,u,em,strong,dfn,code,samp,kbd,var,cite,abbr,acronym,a,img,object,br,script,map,q,sub,sup,span,bdo,input,select,textarea,label,button,");
   tagDefs.Add("area", "");
   tagDefs.Add("b", ",,!,tt,i,b,big,small,center,font,nobr,i,u,em,strong,dfn,code,samp,kbd,var,cite,abbr,acronym,a,img,object,br,script,map,q,sub,sup,span,bdo,input,select,textarea,label,button,");
   tagDefs.Add("base", "");
   tagDefs.Add("bdo", ",,!,tt,i,b,big,small,center,font,nobr,i,u,em,strong,dfn,code,samp,kbd,var,cite,abbr,acronym,a,img,object,br,script,map,q,sub,sup,span,bdo,input,select,textarea,label,button,");
   tagDefs.Add("big", ",,!,tt,i,b,big,small,center,font,nobr,i,u,em,strong,dfn,code,samp,kbd,var,cite,abbr,acronym,a,img,object,br,script,map,q,sub,sup,span,bdo,input,select,textarea,label,button,");
   tagDefs.Add("blockquote", ",p,h1,h2,h3,h4,h5,h6,ul,ol,pre,dl,div,noscript,blockquote,form,hr,table,fieldset,address,script,");
   tagDefs.Add("body", ",p,h1,h2,h3,h4,h5,h6,ul,ol,pre,dl,div,noscript,blockquote,form,hr,table,fieldset,address,script,ins,del,");
   tagDefs.Add("br", "");
   tagDefs.Add("button", ",,!,p,h1,h2,h3,h4,h5,h6,ul,ol,pre,dl,div,noscript,blockquote,hr,table,address,tt,i,b,big,small,center,font,nobr,i,u,em,strong,dfn,code,samp,kbd,var,cite,abbr,acronym,img,object,br,script,map,q,sub,sup,span,bdo,");
   tagDefs.Add("caption", ",,!,tt,i,b,big,small,center,font,nobr,i,u,em,strong,dfn,code,samp,kbd,var,cite,abbr,acronym,a,img,object,br,script,map,q,sub,sup,span,bdo,input,select,textarea,label,button,");
   tagDefs.Add("cite", ",,!,tt,i,b,big,small,center,font,nobr,i,u,em,strong,dfn,code,samp,kbd,var,cite,abbr,acronym,a,img,object,br,script,map,q,sub,sup,span,bdo,input,select,textarea,label,button,");
   tagDefs.Add("code", ",,!,tt,i,b,big,small,center,font,nobr,i,u,em,strong,dfn,code,samp,kbd,var,cite,abbr,acronym,a,img,object,br,script,map,q,sub,sup,span,bdo,input,select,textarea,label,button,");
   tagDefs.Add("col", "");
   tagDefs.Add("colgroup", ",col,");
   tagDefs.Add("dd", ",,!,p,h1,h2,h3,h4,h5,h6,ul,ol,pre,dl,div,noscript,blockquote,form,hr,table,fieldset,address,tt,i,b,big,small,center,font,nobr,i,u,em,strong,dfn,code,samp,kbd,var,cite,abbr,acronym,a,img,object,br,script,map,q,sub,sup,span,bdo,input,select,textarea,label,button,");
   tagDefs.Add("del", ",,!,p,h1,h2,h3,h4,h5,h6,ul,ol,pre,dl,div,noscript,blockquote,form,hr,table,fieldset,address,tt,i,b,big,small,center,font,nobr,i,u,em,strong,dfn,code,samp,kbd,var,cite,abbr,acronym,a,img,object,br,script,map,q,sub,sup,span,bdo,input,select,textarea,label,button,");
   tagDefs.Add("dfn", ",,!,tt,i,b,big,small,center,font,nobr,i,u,em,strong,dfn,code,samp,kbd,var,cite,abbr,acronym,a,img,object,br,script,map,q,sub,sup,span,bdo,input,select,textarea,label,button,");
   tagDefs.Add("div", ",,!,p,h1,h2,h3,h4,h5,h6,ul,ol,pre,dl,div,noscript,blockquote,form,hr,table,fieldset,address,tt,i,b,big,small,center,font,nobr,i,u,em,strong,dfn,code,samp,kbd,var,cite,abbr,acronym,a,img,object,br,script,map,q,sub,sup,span,bdo,input,select,textarea,label,button,");
   tagDefs.Add("dl", ",dt,dd,");
   tagDefs.Add("dt", ",,!,tt,i,b,big,small,center,font,nobr,i,u,em,strong,dfn,code,samp,kbd,var,cite,abbr,acronym,a,img,object,br,script,map,q,sub,sup,span,bdo,input,select,textarea,label,button,");
   tagDefs.Add("em", ",,!,tt,i,b,big,small,center,font,nobr,i,u,em,strong,dfn,code,samp,kbd,var,cite,abbr,acronym,a,img,object,br,script,map,q,sub,sup,span,bdo,input,select,textarea,label,button,");
   tagDefs.Add("fieldset", ",,!,legend,p,h1,h2,h3,h4,h5,h6,ul,ol,pre,dl,div,noscript,blockquote,form,hr,table,fieldset,address,tt,i,b,big,small,center,font,nobr,i,u,em,strong,dfn,code,samp,kbd,var,cite,abbr,acronym,a,img,object,br,script,map,q,sub,sup,span,bdo,input,select,textarea,label,button,");
   tagDefs.Add("form", ",p,h1,h2,h3,h4,h5,h6,ul,ol,pre,dl,div,noscript,blockquote,hr,table,fieldset,address,script,");
   tagDefs.Add("h1", ",,!,tt,i,b,big,small,center,font,nobr,i,u,em,strong,dfn,code,samp,kbd,var,cite,abbr,acronym,a,img,object,br,script,map,q,sub,sup,span,bdo,input,select,textarea,label,button,");
   tagDefs.Add("h2", ",,!,tt,i,b,big,small,center,font,nobr,i,u,em,strong,dfn,code,samp,kbd,var,cite,abbr,acronym,a,img,object,br,script,map,q,sub,sup,span,bdo,input,select,textarea,label,button,");
   tagDefs.Add("h3", ",,!,tt,i,b,big,small,center,font,nobr,i,u,em,strong,dfn,code,samp,kbd,var,cite,abbr,acronym,a,img,object,br,script,map,q,sub,sup,span,bdo,input,select,textarea,label,button,");
   tagDefs.Add("h4", ",,!,tt,i,b,big,small,center,font,nobr,i,u,em,strong,dfn,code,samp,kbd,var,cite,abbr,acronym,a,img,object,br,script,map,q,sub,sup,span,bdo,input,select,textarea,label,button,");
   tagDefs.Add("h5", ",,!,tt,i,b,big,small,center,font,nobr,i,u,em,strong,dfn,code,samp,kbd,var,cite,abbr,acronym,a,img,object,br,script,map,q,sub,sup,span,bdo,input,select,textarea,label,button,");
   tagDefs.Add("h6", ",,!,tt,i,b,big,small,center,font,nobr,i,u,em,strong,dfn,code,samp,kbd,var,cite,abbr,acronym,a,img,object,br,script,map,q,sub,sup,span,bdo,input,select,textarea,label,button,");
   tagDefs.Add("head", ",title,base,?,script,style,meta,link,object,");
   tagDefs.Add("hr", "");
   tagDefs.Add("html", ",head,body,");
   tagDefs.Add("i", ",,!,tt,i,b,big,small,center,font,nobr,i,u,em,strong,dfn,code,samp,kbd,var,cite,abbr,acronym,a,img,object,br,script,map,q,sub,sup,span,bdo,input,select,textarea,label,button,");
   tagDefs.Add("img", "");
   tagDefs.Add("input", "");
   tagDefs.Add("ins", ",,!,p,h1,h2,h3,h4,h5,h6,ul,ol,pre,dl,div,noscript,blockquote,form,hr,table,fieldset,address,tt,i,b,big,small,center,font,nobr,i,u,em,strong,dfn,code,samp,kbd,var,cite,abbr,acronym,a,img,object,br,script,map,q,sub,sup,span,bdo,input,select,textarea,label,button,");
   tagDefs.Add("kbd", ",,!,tt,i,b,big,small,center,font,nobr,i,u,em,strong,dfn,code,samp,kbd,var,cite,abbr,acronym,a,img,object,br,script,map,q,sub,sup,span,bdo,input,select,textarea,label,button,");
   tagDefs.Add("label", ",,!,tt,i,b,big,small,center,font,nobr,i,u,em,strong,dfn,code,samp,kbd,var,cite,abbr,acronym,a,img,object,br,script,map,q,sub,sup,span,bdo,input,select,textarea,button,");
   tagDefs.Add("legend", ",,!,tt,i,b,big,small,center,font,nobr,i,u,em,strong,dfn,code,samp,kbd,var,cite,abbr,acronym,a,img,object,br,script,map,q,sub,sup,span,bdo,input,select,textarea,label,button,");
   tagDefs.Add("li", ",,!,p,h1,h2,h3,h4,h5,h6,ul,ol,pre,dl,div,noscript,blockquote,form,hr,table,fieldset,address,tt,i,b,big,small,center,font,nobr,i,u,em,strong,dfn,code,samp,kbd,var,cite,abbr,acronym,a,img,object,br,script,map,q,sub,sup,span,bdo,input,select,textarea,label,button,");
   tagDefs.Add("link", "");
   tagDefs.Add("map", ",p,h1,h2,h3,h4,h5,h6,ul,ol,pre,dl,div,noscript,blockquote,form,hr,table,fieldset,address,area,");
   tagDefs.Add("meta", "");
   tagDefs.Add("noscript", ",p,h1,h2,h3,h4,h5,h6,ul,ol,pre,dl,div,noscript,blockquote,form,hr,table,fieldset,address,");
   tagDefs.Add("object", ",,!,param,p,h1,h2,h3,h4,h5,h6,ul,ol,pre,dl,div,noscript,blockquote,form,hr,table,fieldset,address,tt,i,b,big,small,center,font,nobr,i,u,em,strong,dfn,code,samp,kbd,var,cite,abbr,acronym,a,img,object,br,script,map,q,sub,sup,span,bdo,input,select,textarea,label,button,");
   tagDefs.Add("ol", ",li,");
   tagDefs.Add("optgroup", ",option,");
   tagDefs.Add("option", ",,!,");
   tagDefs.Add("p", ",,!,tt,i,b,big,small,center,font,nobr,i,u,em,strong,dfn,code,samp,kbd,var,cite,abbr,acronym,a,img,object,br,script,map,q,sub,sup,span,bdo,input,select,textarea,label,button,");
   tagDefs.Add("param", "");
   tagDefs.Add("pre", ",,!,tt,i,b,em,strong,dfn,code,samp,kbd,var,cite,abbr,acronym,a,br,script,map,q,span,bdo,input,select,textarea,label,button,");
   tagDefs.Add("q", ",,!,tt,i,b,big,small,center,font,nobr,i,u,em,strong,dfn,code,samp,kbd,var,cite,abbr,acronym,a,img,object,br,script,map,q,sub,sup,span,bdo,input,select,textarea,label,button,");
   tagDefs.Add("samp", ",,!,tt,i,b,big,small,center,font,nobr,i,u,em,strong,dfn,code,samp,kbd,var,cite,abbr,acronym,a,img,object,br,script,map,q,sub,sup,span,bdo,input,select,textarea,label,button,");
   tagDefs.Add("script", ",cdata,");
   tagDefs.Add("select", ",optgroup,option,");
   tagDefs.Add("small", ",,!,tt,i,b,big,small,center,font,nobr,i,u,em,strong,dfn,code,samp,kbd,var,cite,abbr,acronym,a,img,object,br,script,map,q,sub,sup,span,bdo,input,select,textarea,label,button,");
   tagDefs.Add("center", ",,!,tt,i,b,big,small,center,font,nobr,i,u,em,strong,dfn,code,samp,kbd,var,cite,abbr,acronym,a,img,object,br,script,map,q,sub,sup,span,bdo,input,select,textarea,label,button,");
   tagDefs.Add("font", ",,!,tt,i,b,big,small,center,font,nobr,i,u,em,strong,dfn,code,samp,kbd,var,cite,abbr,acronym,a,img,object,br,script,map,q,sub,sup,span,bdo,input,select,textarea,label,button,");
   tagDefs.Add("nobr", ",,!,tt,i,b,big,small,center,font,nobr,i,u,em,strong,dfn,code,samp,kbd,var,cite,abbr,acronym,a,img,object,br,script,map,q,sub,sup,span,bdo,input,select,textarea,label,button,");
   tagDefs.Add("u", ",,!,tt,i,b,big,small,center,font,nobr,i,u,em,strong,dfn,code,samp,kbd,var,cite,abbr,acronym,a,img,object,br,script,map,q,sub,sup,span,bdo,input,select,textarea,label,button,");
   tagDefs.Add("span", ",,!,tt,i,b,big,small,center,font,nobr,i,u,em,strong,dfn,code,samp,kbd,var,cite,abbr,acronym,a,img,object,br,script,map,q,sub,sup,span,bdo,input,select,textarea,label,button,");
   tagDefs.Add("strong", ",,!,tt,i,b,big,small,center,font,nobr,i,u,em,strong,dfn,code,samp,kbd,var,cite,abbr,acronym,a,img,object,br,script,map,q,sub,sup,span,bdo,input,select,textarea,label,button,");
   tagDefs.Add("style", ",cdata,");
   tagDefs.Add("sub", ",,!,tt,i,b,big,small,center,font,nobr,i,u,em,strong,dfn,code,samp,kbd,var,cite,abbr,acronym,a,img,object,br,script,map,q,sub,sup,span,bdo,input,select,textarea,label,button,");
   tagDefs.Add("sup", ",,!,tt,i,b,big,small,center,font,nobr,i,u,em,strong,dfn,code,samp,kbd,var,cite,abbr,acronym,a,img,object,br,script,map,q,sub,sup,span,bdo,input,select,textarea,label,button,");
   tagDefs.Add("table", ",caption,tr,col,colgroup,thead,td,tfoot,th,tbody,");
   tagDefs.Add("tbody", ",tr,");
   tagDefs.Add("td", ",,!,p,h1,h2,h3,h4,h5,h6,ul,ol,pre,dl,div,noscript,blockquote,form,hr,table,fieldset,address,tt,i,b,big,small,center,font,nobr,i,u,em,strong,dfn,code,samp,kbd,var,cite,abbr,acronym,a,img,object,br,script,map,q,sub,sup,span,bdo,input,select,textarea,label,button,");
   tagDefs.Add("textarea", ",,!,");
   tagDefs.Add("tfoot", ",tr,");
   tagDefs.Add("th", ",,!,p,h1,h2,h3,h4,h5,h6,ul,ol,pre,dl,div,noscript,blockquote,form,hr,table,fieldset,address,tt,i,b,big,small,center,font,nobr,i,u,em,strong,dfn,code,samp,kbd,var,cite,abbr,acronym,a,img,object,br,script,map,q,sub,sup,span,bdo,input,select,textarea,label,button,");
   tagDefs.Add("thead", ",tr,");
   tagDefs.Add("title", ",,!,");
   tagDefs.Add("tr", ",th,td,");
   tagDefs.Add("tt", ",,!,tt,i,b,big,small,center,font,nobr,i,u,em,strong,dfn,code,samp,kbd,var,cite,abbr,acronym,a,img,object,br,script,map,q,sub,sup,span,bdo,input,select,textarea,label,button,");
   tagDefs.Add("ul", ",li,");
   tagDefs.Add("var", ",,!,tt,i,b,big,small,center,font,nobr,i,u,em,strong,dfn,code,samp,kbd,var,cite,abbr,acronym,a,img,object,br,script,map,q,sub,sup,span,bdo,input,select,textarea,label,button,");
   tstSimple();
  }

 }
}









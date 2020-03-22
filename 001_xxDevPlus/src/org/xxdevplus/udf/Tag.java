/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.xxdevplus.udf;

import org.xxdevplus.struct.Assoc;
import java.util.Arrays;
import java.util.regex.Pattern;
import org.xxdevplus.struct.Pile;
import org.xxdevplus.chain.Chain;
import org.xxdevplus.frmlng.Zone;
import java.util.HashMap;
import org.xxdevplus.frmlng.Conditions;
import org.xxdevplus.frmlng.Zone;
import org.xxdevplus.struct.KeyPile;
import org.xxdevplus.struct.NamedValue;


/**
 *
 * @author GeTr
 */
public class Tag
{

  private static HashMap<String, String> tagDefs = new HashMap<String,String>();

  private static   long              ic = 0; //instance counter, used for selfTest()

  private          boolean           isXmlTag;
  private          Chain             def;
  private          Chain             ctn;
  private          Chain             _htm;

  private          Tag               closingTag = null;
  public           Tag               parent;
  public           Tag               root;
  private          Pile<Tag>         all;

  public           Chain             type;
  public           boolean           isRelevant; //if yes, the Tag will be child of another Tag. If no then the Tag will be in root.all but it will not appear as child of any other
  private          Chain             attrib;
  private          Pile<Tag>         _tags;
  private          Assoc             _attr;

  private static Zone blindArea = new Zone(new Pile<String>("", 0, "\r", "\n"), "\"", "\"", "||:0");
  public Pile<Tag> Tags() { return this._tags; }

  public static Pile<String> or(String... cond)
  {
   return new Pile<String>("", 0, cond);
  }

  public static Pile<String>[] where(Object... cond) throws Exception
  {
   Object[] res = new Object[cond.length];
   for(int i = 0; i < cond.length; i++) if (cond[i].getClass().getName().equals("java.lang.String")) { Pile<String> cnd = new Pile<String>(0); cnd.Add((String)cond[i]); res[i] = cnd; } else res[i] = (Pile<String>) cond[i];
   return new Pile<String>(0).newPileOf_TypArray(res);
  }

  public Pile<Tag> tags(int maxDepth, int level, KeyPile<String, Conditions> condsSet, KeyPile<String, Pile<NamedValue<String, Tag>>> resSet) throws Exception
  {
   int foundInThisLevel = 0;
   if (resSet.Len() > 0) if ((resSet.Len() != condsSet.Len() + 1)) for (Conditions cnd : condsSet) resSet.Add(cnd.name, new Pile<NamedValue<String, Tag>>(0));
   Pile<Tag> ret = new Pile<Tag>(0);
   if (condsSet.Len() < level) return ret;
   Conditions conds = condsSet.g(level);
   //conds.reset();
   if (maxDepth == 0) return ret;
   for (Tag t : _tags)
   {
    if (conds.matches(t))
    {
     ret.Add(t);
     String tagBase = "";
     if (resSet.Len() > 0) 
     {
      if (conds.baseName.length() > 0) if (level == 1) tagBase = "base:" + ++foundInThisLevel; else tagBase = condsSet.g(level - 1).name + ":" + resSet.g(condsSet.g(level - 1).name).Len();
      resSet.g(level + 1).Add(new NamedValue<String, Tag>(tagBase, t));
     }
     if (level < condsSet.Len())
     {
      Tag baseTag = t;
      NamedValue<String, Tag> namedBaseTag = new NamedValue<String, Tag>(tagBase, baseTag);
      if (!condsSet.g(level + 1).baseName.equals(condsSet.g(level).name))
      {
       Chain baseName;
       int num;
       while (!condsSet.g(level + 1).baseName.equals(new Chain(namedBaseTag.Name()).before(1, ":").text())) {baseName = new Chain(namedBaseTag.Name()); num = Integer.parseInt(baseName.after(1, ":").text()); namedBaseTag = resSet.g(baseName.before(1, ":").text()).g(num); }
       baseName = new Chain(namedBaseTag.Name()); num = Integer.parseInt(baseName.after(1, ":").text()); namedBaseTag = resSet.g(baseName.before(1, ":").text()).g(num);
      }
      baseTag = namedBaseTag.Value();
      Pile<Tag> subLevelResults = null;
      condsSet.g(level + 1).reset();
      subLevelResults = baseTag.tags(-1, level + 1, condsSet, resSet);
     }
    }
    else
    {
     Pile<Tag> subResults = null;
     subResults = t.tags(maxDepth - 1, level, condsSet, resSet);
     if (subResults.Len() > 0) ret.Add(subResults);
    }
   }
   return ret;
  }

  public Pile<Tag> tags(int maxDepth, Pile<String>... cond) throws Exception
  {
   KeyPile<String, Pile<NamedValue<String, Tag>>> resSet = new KeyPile<String, Pile<NamedValue<String, Tag>>>();
   KeyPile<String, Conditions> condsSet = new KeyPile<String, Conditions>();
   condsSet.Add("", new Conditions("", "", "*", cond));
   //if (resSet.Count == 0) resSet.Add("base", new Pack<Tag>(0));
   return tags(maxDepth, 1, condsSet, resSet);
  }

  public Assoc attr() throws Exception
  {
    if (attrib == null) attrib = def.after(type).upto(-2).lTrim().rTrim();
    if (_attr == null) _attr = new Assoc(attrib, "=", " ", "\"", blindArea);  //this is most expensive - 1 minute when called 4000 times
    return _attr;
  }

  public Chain txt()      throws Exception { Chain ret = ctn.before(1, "<"); for (Tag tag : _tags) ret = ret.plus(tag.txt()); return ret; }

  public Chain code()  throws Exception { if (type.len() > 0) { Chain ret = _htm;                for (Tag tag : _tags) ret = ret.plus(tag.code());  return ret; } else return ctn; }

  public String struc(int indent) throws Exception
  {
   char[] blanks = new char[Math.max(0, indent)];
   Arrays.fill(blanks, ' ');
   String ret = new String(blanks);
   if (this != root) ret += def.text() + ctn.before(1, "<").text();
   String lastType = "";
   for (Tag t : _tags)
   {
    if ((t.type.len() == 0) && ((t.def.text() + t.ctn.before(1, "<").text()).length() > 0)) ret+= t.def.text() + t.ctn.before(1, "<").text(); else if (t.type.text().toLowerCase().equals("/" + lastType)) ret +=  t.def.text(); else ret += "\n".substring(0, Math.min(1, ret.length())) + t.struc(indent + 1) ;
    lastType = t.type.text().toLowerCase();
   }
   if (closingTag != null) if (closingTag != this) ret += closingTag.def.text(); else if (!type.Equals("") && !type.Equals("!") && !type.startsWith("/")) if (!def.at(-2).equals("/")) ret += "</" + type.text() + ">";
   return ret;
  }

  /*
  private void getTags(Pack<Tag> target, String kind) throws Exception
  {
   for (int i = 1; i <= _tags.len(); i++)
    if ((_tags.get(i)).type.len() > 0) //((tags[i]).type != null)
    {
     if ((_tags.get(i)).type.Equals(kind)) target.add((Tag)_tags.get(i));
     _tags.get(i).getTags(target, kind);
    }
  }

  private void getTags(Pack<Tag> target, String kind, String key, String value) throws Exception
  {
   for (int i = 1; i <= _tags.len(); i++)
    if ((_tags.get(i)).type.len() > 0) //((tags[i]).type != null)
    {
     if ((kind.length() == 0) || ((_tags.get(i)).type.Equals(kind))) if (_tags.get(i).attr().containsKey(key)) if (_tags.get(i).attr().get(key).getItem().Equals(value)) target.add((Tag)_tags.get(i));
     _tags.get(i).getTags(target, kind, key, value);
    }
  }
  */

  public Chain val(String symbol) throws Exception
  {
   if (symbol.length() == 0) return this.type.upto(0);
   if (symbol.equals("[type]")) return this.type;
   if (symbol.equals("[text]")) return this.txt();
   if (symbol.equals("[html]")) return this.code();
   if (this.attr().hasKey(symbol.toLowerCase())) return this.attr().g(symbol.toLowerCase()).Value();
   return this.type.upto(0);
  }

  private boolean isWhiteSpace() throws Exception
  {
   if (type.len() > 0) return false;
   String t = txt().text();
   for(int i = 0; i < t.length(); i++)
   {
    switch (t.charAt(i))
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

  private void init(Chain src, Tag parent, boolean xml) throws Exception
  {
   isRelevant = true;
   type = src.upto(0);   //AttGeTr: type was null in new Tag("", false)
   ctn = src.upto(0);    //AttGeTr: ctn was null in some TestCase ...
   isXmlTag = xml;
   _tags = new Pile<Tag>(0);
   this.parent = parent;
   def = src;
   if (parent == null)
   {
    root = this;
    all = new Pile<Tag>(0);
    int done = 0;
    while (done < def.len())
    {
     all.Add(new Tag(def.after(done), this, xml));
     if (all.g(-1).isWhiteSpace()) all.g(-1).isRelevant = false;
     if (all.Len() == 1)
      all.g(-1).parent = this;
     else
     {
      if (all.g(-1).type.startsWith("/"))
      {
       for (int i = all.Len() - 1; i > 0; i--) if ((all.g(-1).type.Equals("/" + all.g(i).type.text())) && (all.g(i).closingTag == null)) { all.g(i).closingTag = all.g(-1); all.g(-1).parent = all.g(i).parent; break; }
       if (all.g(-1).parent == null) all.g(-1).parent = all.g(-2).parent; // orphaned closing tag
      }
      else
      {
       if ((all.g(-1).type.len() == 0) && (!all.g(-2).type.startsWith("/"))) // new definition: a "plain text tag" may be contained in any tag but may not always be relevant ...
       {
        if (!all.g(-2).mayContain(all.g(-1).type.text())) all.g(-1).isRelevant = false;         
        if (all.g(-2).closingTag == null) all.g(-1).parent = all.g(-2); else all.g(-1).parent = all.g(-2).parent;
       }
       else
       {
        if ((all.g(-2).mayContain(all.g(-1).type.text()))) all.g(-1).parent = all.g(-2); else { all.g(-1).parent = all.g(-2).parent; if (all.g(-2).closingTag == null) all.g(-2).closingTag = all.g(-2); }
       }
      }
     }
     done = done + all.g(-1)._htm.len();
    }
    for (int i = 1; i <= all.Len(); i++)
    {
     if ((all.g(i).isRelevant) && (!all.g(i).type.startsWith("/"))) all.g(i).parent._tags.Add(all.g(i));
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

  private Tag(Chain  sourceString, Tag parent, boolean xml) throws Exception { init(sourceString, parent, xml); }
  private Tag(String sourceString, Tag parent, boolean xml) throws Exception { init(new Chain(sourceString), parent, xml); }
  public  Tag(Chain  source                  , boolean xml) throws Exception { if (ic++ == 0) selfTest(); init(source, null, xml); }
  public  Tag(String source                  , boolean xml) throws Exception { if (ic++ == 0) selfTest(); init(new Chain(source), null, xml); }

  public boolean mayContain(String type) throws Exception
  {
   if (closingTag != null) return false;
   if ((this.type.startsWith("/")) || (this.type.startsWith("!"))) return false;
   if (this.type.len() == 0) return (this == root);
   if (isXmlTag) return true;
   String key = this.type.text().toLowerCase();
   if (key.equals("body")) return true;    // override the default, be more tolerant to incorrect html code
   if (key.equals("meta")) return false;   // return (!type.ToLower().Equals("meta"));
   return (tagDefs.keySet().contains(key)) ? (tagDefs.get(key).indexOf("," + type.toLowerCase() + ",") > -1) : false;
  }

  private static void ass(boolean expr, String msg) throws Exception { if (!expr) throw new Exception("Error in Tag.selfTest(): " + msg); }
  private static void ass(String e1, String e2, String msg) throws Exception { ass(e1.equals(e2), msg); }
  private static void ass(double e1, double e2, String msg) throws Exception { ass(e1 == e2, msg); }

  private void tstSimple() throws Exception
  {
   Tag doc = new Tag("", false);
   ass(doc.all.Len(), 0, "tstSimple.a");
   Tag tag = new Tag(" \n ", this, false);
   ass(tag.type.text(), "", "tstSimple.b");
   ass(tag.def.text(), "", "tstSimple.c");
   ass(tag.ctn.text(), " \n ", "tstSimple.d");
   doc = new Tag("<html><body>abc</body></html>", false);
   ass(doc.all.Len(), 5, "tstSimple.0");
   ass(doc.txt().text(), "abc", "tstSimple.0");
   ass(doc.all.g(1).type.text(), "html", "tstSimple.1");
   ass(doc.all.g(2).type.text(), "body", "tstSimple.2");
   ass(doc.all.g(3).type.text(), "", "tstSimple.3");
   ass(doc.all.g(4).type.text(), "/body", "tstSimple.4");
   ass(doc.all.g(5).type.text(), "/html", "tstSimple.5");
   ass(doc._tags.g(1).type.text(), "html", "tstSimple.6");
   ass(doc._tags.g(1)._tags.g(1).type.text(), "body", "tstSimple.7");
   ass(doc._tags.g(1)._tags.g(1)._tags.g(1).ctn.text(), "abc", "tstSimple.8");
   ass(doc.struc(-1), "<html>\n <body>abc</body></html>", "tstSimple.9");
   doc = new Tag("<tr>\n <td name=\"1\"/>\n <td name=\"2\"/>\n</tr>\n<tr>\n <td name=\"1\"/>\n <td name=\"2\"/>\n</tr>\n<tr>\n <td name=\"1\"/>\n <td name=\"2\"/>\n</tr>", true);
   ass(doc.struc(-1), "<tr>\n <td name=\"1\"/>\n <td name=\"2\"/></tr>\n<tr>\n <td name=\"1\"/>\n <td name=\"2\"/></tr>\n<tr>\n <td name=\"1\"/>\n <td name=\"2\"/></tr>", "tstSimple.10");
  }

  private void selfTest() throws Exception
  {
   //Class Constructor:
   tagDefs.put("", "");
   tagDefs.put("!", "");
   tagDefs.put("a", ",,!,tt,i,b,big,small,center,font,nobr,i,u,em,strong,dfn,code,samp,kbd,var,cite,abbr,acronym,img,object,br,script,map,q,sub,sup,span,bdo,input,select,textarea,label,button,");
   tagDefs.put("abbr", ",,!,tt,i,b,big,small,center,font,nobr,i,u,em,strong,dfn,code,samp,kbd,var,cite,abbr,acronym,a,img,object,br,script,map,q,sub,sup,span,bdo,input,select,textarea,label,button,");
   tagDefs.put("acronym", ",,!,tt,i,b,big,small,center,font,nobr,i,u,em,strong,dfn,code,samp,kbd,var,cite,abbr,acronym,a,img,object,br,script,map,q,sub,sup,span,bdo,input,select,textarea,label,button,");
   tagDefs.put("address", ",,!,tt,i,b,big,small,center,font,nobr,i,u,em,strong,dfn,code,samp,kbd,var,cite,abbr,acronym,a,img,object,br,script,map,q,sub,sup,span,bdo,input,select,textarea,label,button,");
   tagDefs.put("area", "");
   tagDefs.put("b", ",,!,tt,i,b,big,small,center,font,nobr,i,u,em,strong,dfn,code,samp,kbd,var,cite,abbr,acronym,a,img,object,br,script,map,q,sub,sup,span,bdo,input,select,textarea,label,button,");
   tagDefs.put("base", "");
   tagDefs.put("bdo", ",,!,tt,i,b,big,small,center,font,nobr,i,u,em,strong,dfn,code,samp,kbd,var,cite,abbr,acronym,a,img,object,br,script,map,q,sub,sup,span,bdo,input,select,textarea,label,button,");
   tagDefs.put("big", ",,!,tt,i,b,big,small,center,font,nobr,i,u,em,strong,dfn,code,samp,kbd,var,cite,abbr,acronym,a,img,object,br,script,map,q,sub,sup,span,bdo,input,select,textarea,label,button,");
   tagDefs.put("blockquote", ",p,h1,h2,h3,h4,h5,h6,ul,ol,pre,dl,div,noscript,blockquote,form,hr,table,fieldset,address,script,");
   tagDefs.put("body", ",p,h1,h2,h3,h4,h5,h6,ul,ol,pre,dl,div,noscript,blockquote,form,hr,table,fieldset,address,script,ins,del,");
   tagDefs.put("br", "");
   tagDefs.put("button", ",,!,p,h1,h2,h3,h4,h5,h6,ul,ol,pre,dl,div,noscript,blockquote,hr,table,address,tt,i,b,big,small,center,font,nobr,i,u,em,strong,dfn,code,samp,kbd,var,cite,abbr,acronym,img,object,br,script,map,q,sub,sup,span,bdo,");
   tagDefs.put("caption", ",,!,tt,i,b,big,small,center,font,nobr,i,u,em,strong,dfn,code,samp,kbd,var,cite,abbr,acronym,a,img,object,br,script,map,q,sub,sup,span,bdo,input,select,textarea,label,button,");
   tagDefs.put("cite", ",,!,tt,i,b,big,small,center,font,nobr,i,u,em,strong,dfn,code,samp,kbd,var,cite,abbr,acronym,a,img,object,br,script,map,q,sub,sup,span,bdo,input,select,textarea,label,button,");
   tagDefs.put("code", ",,!,tt,i,b,big,small,center,font,nobr,i,u,em,strong,dfn,code,samp,kbd,var,cite,abbr,acronym,a,img,object,br,script,map,q,sub,sup,span,bdo,input,select,textarea,label,button,");
   tagDefs.put("col", "");
   tagDefs.put("colgroup", ",col,");
   tagDefs.put("dd", ",,!,p,h1,h2,h3,h4,h5,h6,ul,ol,pre,dl,div,noscript,blockquote,form,hr,table,fieldset,address,tt,i,b,big,small,center,font,nobr,i,u,em,strong,dfn,code,samp,kbd,var,cite,abbr,acronym,a,img,object,br,script,map,q,sub,sup,span,bdo,input,select,textarea,label,button,");
   tagDefs.put("del", ",,!,p,h1,h2,h3,h4,h5,h6,ul,ol,pre,dl,div,noscript,blockquote,form,hr,table,fieldset,address,tt,i,b,big,small,center,font,nobr,i,u,em,strong,dfn,code,samp,kbd,var,cite,abbr,acronym,a,img,object,br,script,map,q,sub,sup,span,bdo,input,select,textarea,label,button,");
   tagDefs.put("dfn", ",,!,tt,i,b,big,small,center,font,nobr,i,u,em,strong,dfn,code,samp,kbd,var,cite,abbr,acronym,a,img,object,br,script,map,q,sub,sup,span,bdo,input,select,textarea,label,button,");
   tagDefs.put("div", ",,!,p,h1,h2,h3,h4,h5,h6,ul,ol,pre,dl,div,noscript,blockquote,form,hr,table,fieldset,address,tt,i,b,big,small,center,font,nobr,i,u,em,strong,dfn,code,samp,kbd,var,cite,abbr,acronym,a,img,object,br,script,map,q,sub,sup,span,bdo,input,select,textarea,label,button,");
   tagDefs.put("dl", ",dt,dd,");
   tagDefs.put("dt", ",,!,tt,i,b,big,small,center,font,nobr,i,u,em,strong,dfn,code,samp,kbd,var,cite,abbr,acronym,a,img,object,br,script,map,q,sub,sup,span,bdo,input,select,textarea,label,button,");
   tagDefs.put("em", ",,!,tt,i,b,big,small,center,font,nobr,i,u,em,strong,dfn,code,samp,kbd,var,cite,abbr,acronym,a,img,object,br,script,map,q,sub,sup,span,bdo,input,select,textarea,label,button,");
   tagDefs.put("fieldset", ",,!,legend,p,h1,h2,h3,h4,h5,h6,ul,ol,pre,dl,div,noscript,blockquote,form,hr,table,fieldset,address,tt,i,b,big,small,center,font,nobr,i,u,em,strong,dfn,code,samp,kbd,var,cite,abbr,acronym,a,img,object,br,script,map,q,sub,sup,span,bdo,input,select,textarea,label,button,");
   tagDefs.put("form", ",p,h1,h2,h3,h4,h5,h6,ul,ol,pre,dl,div,noscript,blockquote,hr,table,fieldset,address,script,");
   tagDefs.put("h1", ",,!,tt,i,b,big,small,center,font,nobr,i,u,em,strong,dfn,code,samp,kbd,var,cite,abbr,acronym,a,img,object,br,script,map,q,sub,sup,span,bdo,input,select,textarea,label,button,");
   tagDefs.put("h2", ",,!,tt,i,b,big,small,center,font,nobr,i,u,em,strong,dfn,code,samp,kbd,var,cite,abbr,acronym,a,img,object,br,script,map,q,sub,sup,span,bdo,input,select,textarea,label,button,");
   tagDefs.put("h3", ",,!,tt,i,b,big,small,center,font,nobr,i,u,em,strong,dfn,code,samp,kbd,var,cite,abbr,acronym,a,img,object,br,script,map,q,sub,sup,span,bdo,input,select,textarea,label,button,");
   tagDefs.put("h4", ",,!,tt,i,b,big,small,center,font,nobr,i,u,em,strong,dfn,code,samp,kbd,var,cite,abbr,acronym,a,img,object,br,script,map,q,sub,sup,span,bdo,input,select,textarea,label,button,");
   tagDefs.put("h5", ",,!,tt,i,b,big,small,center,font,nobr,i,u,em,strong,dfn,code,samp,kbd,var,cite,abbr,acronym,a,img,object,br,script,map,q,sub,sup,span,bdo,input,select,textarea,label,button,");
   tagDefs.put("h6", ",,!,tt,i,b,big,small,center,font,nobr,i,u,em,strong,dfn,code,samp,kbd,var,cite,abbr,acronym,a,img,object,br,script,map,q,sub,sup,span,bdo,input,select,textarea,label,button,");
   tagDefs.put("head", ",title,base,?,script,style,meta,link,object,");
   tagDefs.put("hr", "");
   tagDefs.put("html", ",head,body,");
   tagDefs.put("i", ",,!,tt,i,b,big,small,center,font,nobr,i,u,em,strong,dfn,code,samp,kbd,var,cite,abbr,acronym,a,img,object,br,script,map,q,sub,sup,span,bdo,input,select,textarea,label,button,");
   tagDefs.put("img", "");
   tagDefs.put("input", "");
   tagDefs.put("ins", ",,!,p,h1,h2,h3,h4,h5,h6,ul,ol,pre,dl,div,noscript,blockquote,form,hr,table,fieldset,address,tt,i,b,big,small,center,font,nobr,i,u,em,strong,dfn,code,samp,kbd,var,cite,abbr,acronym,a,img,object,br,script,map,q,sub,sup,span,bdo,input,select,textarea,label,button,");
   tagDefs.put("kbd", ",,!,tt,i,b,big,small,center,font,nobr,i,u,em,strong,dfn,code,samp,kbd,var,cite,abbr,acronym,a,img,object,br,script,map,q,sub,sup,span,bdo,input,select,textarea,label,button,");
   tagDefs.put("label", ",,!,tt,i,b,big,small,center,font,nobr,i,u,em,strong,dfn,code,samp,kbd,var,cite,abbr,acronym,a,img,object,br,script,map,q,sub,sup,span,bdo,input,select,textarea,button,");
   tagDefs.put("legend", ",,!,tt,i,b,big,small,center,font,nobr,i,u,em,strong,dfn,code,samp,kbd,var,cite,abbr,acronym,a,img,object,br,script,map,q,sub,sup,span,bdo,input,select,textarea,label,button,");
   tagDefs.put("li", ",,!,p,h1,h2,h3,h4,h5,h6,ul,ol,pre,dl,div,noscript,blockquote,form,hr,table,fieldset,address,tt,i,b,big,small,center,font,nobr,i,u,em,strong,dfn,code,samp,kbd,var,cite,abbr,acronym,a,img,object,br,script,map,q,sub,sup,span,bdo,input,select,textarea,label,button,");
   tagDefs.put("link", "");
   tagDefs.put("map", ",p,h1,h2,h3,h4,h5,h6,ul,ol,pre,dl,div,noscript,blockquote,form,hr,table,fieldset,address,area,");
   tagDefs.put("meta", "");
   tagDefs.put("noscript", ",p,h1,h2,h3,h4,h5,h6,ul,ol,pre,dl,div,noscript,blockquote,form,hr,table,fieldset,address,");
   tagDefs.put("object", ",,!,param,p,h1,h2,h3,h4,h5,h6,ul,ol,pre,dl,div,noscript,blockquote,form,hr,table,fieldset,address,tt,i,b,big,small,center,font,nobr,i,u,em,strong,dfn,code,samp,kbd,var,cite,abbr,acronym,a,img,object,br,script,map,q,sub,sup,span,bdo,input,select,textarea,label,button,");
   tagDefs.put("ol", ",li,");
   tagDefs.put("optgroup", ",option,");
   tagDefs.put("option", ",,!,");
   tagDefs.put("p", ",,!,tt,i,b,big,small,center,font,nobr,i,u,em,strong,dfn,code,samp,kbd,var,cite,abbr,acronym,a,img,object,br,script,map,q,sub,sup,span,bdo,input,select,textarea,label,button,");
   tagDefs.put("param", "");
   tagDefs.put("pre", ",,!,tt,i,b,em,strong,dfn,code,samp,kbd,var,cite,abbr,acronym,a,br,script,map,q,span,bdo,input,select,textarea,label,button,");
   tagDefs.put("q", ",,!,tt,i,b,big,small,center,font,nobr,i,u,em,strong,dfn,code,samp,kbd,var,cite,abbr,acronym,a,img,object,br,script,map,q,sub,sup,span,bdo,input,select,textarea,label,button,");
   tagDefs.put("samp", ",,!,tt,i,b,big,small,center,font,nobr,i,u,em,strong,dfn,code,samp,kbd,var,cite,abbr,acronym,a,img,object,br,script,map,q,sub,sup,span,bdo,input,select,textarea,label,button,");
   tagDefs.put("script", ",cdata,");
   tagDefs.put("select", ",optgroup,option,");
   tagDefs.put("small", ",,!,tt,i,b,big,small,center,font,nobr,i,u,em,strong,dfn,code,samp,kbd,var,cite,abbr,acronym,a,img,object,br,script,map,q,sub,sup,span,bdo,input,select,textarea,label,button,");
   tagDefs.put("center", ",,!,tt,i,b,big,small,center,font,nobr,i,u,em,strong,dfn,code,samp,kbd,var,cite,abbr,acronym,a,img,object,br,script,map,q,sub,sup,span,bdo,input,select,textarea,label,button,");
   tagDefs.put("font", ",,!,tt,i,b,big,small,center,font,nobr,i,u,em,strong,dfn,code,samp,kbd,var,cite,abbr,acronym,a,img,object,br,script,map,q,sub,sup,span,bdo,input,select,textarea,label,button,");
   tagDefs.put("nobr", ",,!,tt,i,b,big,small,center,font,nobr,i,u,em,strong,dfn,code,samp,kbd,var,cite,abbr,acronym,a,img,object,br,script,map,q,sub,sup,span,bdo,input,select,textarea,label,button,");
   tagDefs.put("u", ",,!,tt,i,b,big,small,center,font,nobr,i,u,em,strong,dfn,code,samp,kbd,var,cite,abbr,acronym,a,img,object,br,script,map,q,sub,sup,span,bdo,input,select,textarea,label,button,");
   tagDefs.put("span", ",,!,tt,i,b,big,small,center,font,nobr,i,u,em,strong,dfn,code,samp,kbd,var,cite,abbr,acronym,a,img,object,br,script,map,q,sub,sup,span,bdo,input,select,textarea,label,button,");
   tagDefs.put("strong", ",,!,tt,i,b,big,small,center,font,nobr,i,u,em,strong,dfn,code,samp,kbd,var,cite,abbr,acronym,a,img,object,br,script,map,q,sub,sup,span,bdo,input,select,textarea,label,button,");
   tagDefs.put("style", ",cdata,");
   tagDefs.put("sub", ",,!,tt,i,b,big,small,center,font,nobr,i,u,em,strong,dfn,code,samp,kbd,var,cite,abbr,acronym,a,img,object,br,script,map,q,sub,sup,span,bdo,input,select,textarea,label,button,");
   tagDefs.put("sup", ",,!,tt,i,b,big,small,center,font,nobr,i,u,em,strong,dfn,code,samp,kbd,var,cite,abbr,acronym,a,img,object,br,script,map,q,sub,sup,span,bdo,input,select,textarea,label,button,");
   tagDefs.put("table", ",caption,tr,col,colgroup,thead,td,tfoot,th,tbody,");
   tagDefs.put("tbody", ",tr,");
   tagDefs.put("td", ",,!,p,h1,h2,h3,h4,h5,h6,ul,ol,pre,dl,div,noscript,blockquote,form,hr,table,fieldset,address,tt,i,b,big,small,center,font,nobr,i,u,em,strong,dfn,code,samp,kbd,var,cite,abbr,acronym,a,img,object,br,script,map,q,sub,sup,span,bdo,input,select,textarea,label,button,");
   tagDefs.put("textarea", ",,!,");
   tagDefs.put("tfoot", ",tr,");
   tagDefs.put("th", ",,!,p,h1,h2,h3,h4,h5,h6,ul,ol,pre,dl,div,noscript,blockquote,form,hr,table,fieldset,address,tt,i,b,big,small,center,font,nobr,i,u,em,strong,dfn,code,samp,kbd,var,cite,abbr,acronym,a,img,object,br,script,map,q,sub,sup,span,bdo,input,select,textarea,label,button,");
   tagDefs.put("thead", ",tr,");
   tagDefs.put("title", ",,!,");
   tagDefs.put("tr", ",th,td,");
   tagDefs.put("tt", ",,!,tt,i,b,big,small,center,font,nobr,i,u,em,strong,dfn,code,samp,kbd,var,cite,abbr,acronym,a,img,object,br,script,map,q,sub,sup,span,bdo,input,select,textarea,label,button,");
   tagDefs.put("ul", ",li,");
   tagDefs.put("var", ",,!,tt,i,b,big,small,center,font,nobr,i,u,em,strong,dfn,code,samp,kbd,var,cite,abbr,acronym,a,img,object,br,script,map,q,sub,sup,span,bdo,input,select,textarea,label,button,");
   tstSimple();
  }



}


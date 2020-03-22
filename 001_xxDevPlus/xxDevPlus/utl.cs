


//** @author  GeTr Copyright© 1996-2020 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties
//** @license You may use, modify, distribute this code under GNU public dual licence free for NON-Commercial use as long as this notice remains unchanged.
//** @comment General Utilitiy Functions


using System;
using System.Collections.Generic;
using System.Linq;
using System.IO;
using System.Diagnostics;
using System.Drawing;
using System.Windows.Forms;
using System.Runtime.InteropServices;
using Microsoft.VisualBasic;


using org_xxdevplus_utl;
using org_xxdevplus_struct;
using org_xxdevplus_chain;


namespace org_xxdevplus_utl
{

 public static class utl //DocGetr: u is short for utilities, convention: class names in lower case indicate static classes
 {
  //Copyright© 1996-2012 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties, You may use, modify, distribute this code under GNU public licence for NON-Commercial use as long as this notice remains unchanged.
  private static bool selfTested = false; private static string ass(bool expr) { if (!expr) throw new Exception(ass(true) + " SelfTest Failure"); return "utl"; }
  private static void selfTest() { selfTested = true; }
  private static void init() { if (!selfTested) selfTest(); }

  public static DateTime Now() { return DateTime.Now; }

  //Copmparators:
  public static string CPR(string key)
  {
   if (key.Equals("IS")) return " == ";
   if (key.Equals("NOT_IS")) return " !== ";
   if (key.Equals("EQ")) return " = ";
   if (key.Equals("NOT_EQ")) return " != ";
   if (key.Equals("GT")) return " > ";
   if (key.Equals("LT")) return " < ";
   if (key.Equals("GE")) return " >= ";
   if (key.Equals("LE")) return " <= ";
   if (key.Equals("LIKE")) return " ~ ";
   if (key.Equals("NOT_LIKE")) return " !~ ";
   if (key.Equals("IN")) return " ° ";
   if (key.Equals("NOT_IN")) return " !° ";
   if (key.Equals("MV")) return " ≡ ";
   if (key.Equals("NOT_MV")) return " !≡ ";
   if (key.Equals("MU")) return " ≡> ";
   if (key.Equals("NOT_MU")) return " !≡> ";
   if (key.Equals("ML")) return " ≡< ";
   if (key.Equals("NOT_ML")) return " !≡< ";
   if (key.Equals("XS")) return " °° ";
   if (key.Equals("NOT_XS")) return " !°° ";
   return key.ToUpper();
  }

  //Operators:
  public static string OPR(string key)
  {
   if (key.Equals("APD")) return " _ ";
   if (key.Equals("CPF")) return "   ";         //composite filed
   if (key.Equals("CCT")) return " & ";
   if (key.Equals("TM")) return " |.| ";
   if (key.Equals("LTM")) return " |. ";
   if (key.Equals("RTM")) return " .| ";
   if (key.Equals("CVS")) return " >$ ";
   if (key.Equals("OR")) return " || ";
   if (key.Equals("AND")) return " && ";
   if (key.Equals("MNS")) return " \\\\ ";
   if (key.Equals("XOR")) return " ^^ ";
   return key.ToUpper();
  }

  public static void cloneMenu(ContextMenu source, string subMenu, ContextMenu target, EventHandler click)
  {
   target.MenuItems.Clear();
   foreach (MenuItem mi in source.MenuItems)
   {
    if ((subMenu.Trim().Length == 0) || (mi.Text.Equals(subMenu)))
    {
     if (subMenu.Trim().Length == 0) target.MenuItems.Add(new MenuItem(mi.Text));
     foreach (MenuItem m in mi.MenuItems)
     {
      if (subMenu.Trim().Length > 0) target.MenuItems.Add(new MenuItem(m.Text)); else target.MenuItems[target.MenuItems.Count - 1].MenuItems.Add(new MenuItem(m.Text));
      if (subMenu.Trim().Length > 0) target.MenuItems[target.MenuItems.Count - 1].Click += click; else target.MenuItems[target.MenuItems.Count - 1].MenuItems[target.MenuItems[target.MenuItems.Count - 1].MenuItems.Count - 1].Click += click;
     }
    }
   }
  }

  public static void cloneMenu(string topMenuName, ContextMenu source, ContextMenu target, EventHandler menuAction) { target.MenuItems.Clear(); foreach (MenuItem mi in source.MenuItems) if (mi.Text.Equals(topMenuName)) foreach (MenuItem m in mi.MenuItems) { target.MenuItems.Add(new MenuItem(m.Text)); target.MenuItems[target.MenuItems.Count - 1].Click += menuAction; } }

  public static object[] prm(params string[] s) { if (s == null) return new string[0]; else return s; } // Helpers function to use multiple params... parameters in 1 Method header

  public static string twoDigits(long value) { string result = "00" + value; return result.Substring(result.Length - 2); }
  public static string threeDigits(long value) { string result = "000" + value; return result.Substring(result.Length - 3); }
  public static string fourDigits(long value) { string result = "0000" + value; return result.Substring(result.Length - 4); }
  public static string fiveDigits(long value) { string result = "00000" + value; return result.Substring(result.Length - 5); }

  public static string stdDateTimeStamp(DateTime dt, bool compressed) { if (compressed) return "" + dt.Year + twoDigits(dt.Month) + twoDigits(dt.Day) + twoDigits(dt.Hour) + twoDigits(dt.Minute) + twoDigits(dt.Second) + threeDigits(dt.Millisecond); else return "" + dt.Year + "-" + twoDigits(dt.Month) + "-" + twoDigits(dt.Day) + " " + twoDigits(dt.Hour) + ":" + twoDigits(dt.Minute) + ":" + twoDigits(dt.Second) + "." + threeDigits(dt.Millisecond); }
  public static string stdTimeStamp(DateTime dt, bool compressed) { if (compressed) return "" + twoDigits(dt.Hour) + twoDigits(dt.Minute) + twoDigits(dt.Second) + threeDigits(dt.Millisecond); else return "" + twoDigits(dt.Hour) + ":" + twoDigits(dt.Minute) + ":" + twoDigits(dt.Second) + "." + threeDigits(dt.Millisecond); }
  public static string stdDateStamp(DateTime dt, bool compressed) { if (compressed) return "" + dt.Year + twoDigits(dt.Month) + twoDigits(dt.Day); else return "" + dt.Year + "-" + twoDigits(dt.Month) + "-" + twoDigits(dt.Day); }

  private static string toDb(string x) { return x.Replace("'", "``"); }

  public static string ds(bool avoidEmptyStrings, string x) { return ((avoidEmptyStrings) && (x.Length == 0)) ? "'" + toDb(x) + " '" : "'" + toDb(x) + "'"; }
  public static string dS(bool avoidEmptyStrings, string x) { return ((avoidEmptyStrings) && (x.Length == 0)) ? "'" + toDb(x.ToUpper()) + " '" : "'" + toDb(x.ToUpper()) + "'"; }
  public static string Ds(bool avoidEmptyStrings, string x) { return ((avoidEmptyStrings) && (x.Length == 0)) ? "'" + toDb(x.ToLower()) + " '" : "'" + toDb(x.ToLower()) + "'"; }


  public static string fromDb(string x, bool cutCategory) { string ret = x.Replace("``", "'"); if (cutCategory) for (long i = ret.Length - 1; i >= 0; i--) if (ret[(int)i] == '(') return ret.Substring(0, Math.Max(0, (int)i - 1)).Trim(); return ret; }

  public static void testKeysPatterns()
  {
   //AttGetr: Alt did not work on my computer!
   //AttGetr: shift, control, numlock must be presses BEFORE MouseDown, this is most likely due to the apple-like mouse driver!!!
   //if (keysPattern(e, 0, 0, 0, 0, 1, 0, 0, 0)) utl.say("PASSED"); else utl.say("FAILED");   //left mouse must be pressed and - other keys dont matter ...
   //if (keysPattern(e, 0, 0, 0, 0, 1, 0, 1, 0))   utl.say("PASSED"); else utl.say("FAILED"); //left mouse,ctrl must be pressed and  - other keys dont matter ...
   //if (keysPattern(e, 0, -1, 0, 0, 1, 0, 1, 0)) utl.say("PASSED"); else utl.say("FAILED");  //left mouse,ctrl must be pressed and NUMLOCK MUST BE RELEASED and - other keys dont matter ...
   //if (keysPattern(e, 0, 0, 0, 0, 1, 0, 0, 1)) utl.say("PASSED"); else utl.say("FAILED");   //left mouse and shift must be pressed and other keys dont matter ...
   //if (keysPattern(e, 0, 0, 0, 0, 1, 0, 0, -1)) utl.say("PASSED"); else utl.say("FAILED");   //left mouse must be pressed and SHIFT MUST BE RELEASED - other keys dont matter ...
  }

  /*
  public static long[] rnd;
  public static long[] random(long count, long min, long max)
  {
   rnd = new long[count];
   for (long i = 1; i <= count; i++) rnd[(long)i - 1] = new Random().Next((int)min, (int)max);
   return rnd;
  }

  public static string reverse(this string me)
  {
   char[] chars = me.ToCharArray();
   Array.Reverse(chars);
   return new string(chars);
  }
  */

  //public Reach r(string s) { return new Reach(s); }

  public static readonly string WordCharacters = "abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";

  //public static void msg(string x) { Console.WriteLine(x); }

  public static void msg(string x) { say(x); }

  public static void say(string msg) { if (ctx.useGuiDialogs) MessageBox.Show("<html><body>" + msg.Replace("\r\n", "<br>") + "</body></html>", "", MessageBoxButtons.OK); else Console.WriteLine(msg); }

  public static string inputBox(string prompt, string title, string deflt) { return Interaction.InputBox(prompt, title, deflt, 30, 30); }
  public static DialogResult ask(string msg) { return MessageBox.Show(msg, "SemanticFinder", MessageBoxButtons.YesNo, MessageBoxIcon.Question, MessageBoxDefaultButton.Button3); }

  public static string list(string delim, params object[] obj)
  {
   string ret = "";
   if (obj.Count() == 0) return ret;
   foreach (object o in obj) { if (o.GetType() == typeof(string)) ret += delim + ((string)o); else if (o.GetType() == typeof(long)) ret += delim + ((long)o); else if (o.GetType() == typeof(long)) ret += delim + ((long)o); else ret += delim + o.ToString(); }
   return ret.Substring(delim.Length);
  }


  //public static string[] str(params string[] str) { return str; }       //short: strings         exact: "array of strings"
  //public static string[] keys(kStr[] kStrs) { string[] ret = new string[kStrs.Length]; for (long i = 0; i < kStrs.Length; i++) ret[i] = kStrs[i].key; return ret; }

  //public static kStr[]   kStrs(params kStr[] strstr) { return strstr; }   //short: keyed strings   exact: "array of keyed string arrays"

  public static void exec(string cmd, string args, bool byStart, bool wait)
  {
   using (Process proc = new Process())
   {
    proc.StartInfo.UseShellExecute = byStart;
    proc.StartInfo.FileName = cmd;
    proc.StartInfo.Arguments = args;
    proc.Start();
    if (wait) proc.WaitForExit();
   }
  }


  public static string formatAttribute(string attr, string value)
  {
   string ret = utl.fromXml(value).Replace("br ", " ");
   if (attr.Equals("Birthdate"))
   {
    if ((ret.ToUpper().StartsWith("{{BIRTH DATE")) || (ret.ToUpper().StartsWith("{{BDA")))
     ret = ret.ToUpper().Replace("{{", "").Replace("}}", "").Replace("DF=YES", "").Replace("DF=NO", "").Replace("DF=Y", "").Replace("DF=N", "").Replace("BDA", "").Replace("BIRTH DATE AND AGE", "").Replace("BIRTH DATE", "");
   }
   else if (attr.Equals("YearsActive"))
   {
    ret = ret.ToUpper().Replace("{{", "").Replace("}}", "").Replace("FY|", "");
   }
   else if (attr.Equals("Budget"))
   {
    ret = ret.ToUpper().Replace("[[US$]]", "$");
   }
   ret = ret.Trim();
   if (ret.StartsWith("|")) ret = ret.Substring(1).Trim();
   if (ret.EndsWith("|")) ret = ret.Substring(0, ret.Length - 1).Trim();
   return ret;
  }

  public static string edit(string str)
  {
   string fileName = Path.GetTempPath() + "/" + getTempFileName(false) + ".txt";
   using (StreamWriter sw = new StreamWriter(fileName, false)) { sw.Write(str); }
   exec("Notepad", fileName, true, true);
   using (StreamReader sr = new StreamReader(fileName)) { return sr.ReadToEnd(); }
  }

  public static void view(string str)
  {
   string fileName = Path.GetTempPath() + "/" + getTempFileName(false) + ".txt";
   using (StreamWriter sw = new StreamWriter(fileName, false)) { sw.Write(str); }
   //exec("Notepad", fileName, true, false);
   exec("Write", fileName, true, false);
  }

  public static void cOut(String x) { Console.Write(x); }
  public static void cOutLn(String x) { Console.WriteLine(x); }

  public static void s2f(string x, string fileName, bool append)
  {
   if (fileName.Trim().ToUpper().StartsWith("FILE:///")) fileName = fileName.Substring(8);
   StreamWriter sw = new StreamWriter(fileName, append);
   sw.Write(x);
   sw.Flush();
   sw.Close();
  }

  public static string f2s(string fileName)  //UniCodeOKAY
  {
   string ret = null;
   if (fileName.ToLower().StartsWith("file://")) fileName = fileName.Substring(8);
   StreamReader sr = new StreamReader(fileName); ret = sr.ReadToEnd(); sr.Close();
   return ret;
  }

  public static void setControlFont(Control ctl, string defaultFont)
  {
   ctl.Font = new Font(new FontFamily(utl.cutl(ref defaultFont, "; ")), float.Parse(utl.cutl(ref defaultFont, "; ")));
   ctl.Height = (int)long.Parse(utl.cutl(ref defaultFont, "; "));
   if (defaultFont.Trim().StartsWith("#")) ctl.ForeColor = ColorTranslator.FromHtml(utl.cutl(ref defaultFont, "; ")); else ctl.ForeColor = Color.FromArgb( /* R */ (int)long.Parse(utl.cutl(ref defaultFont, "; ")), /* G */ (int)long.Parse(utl.cutl(ref defaultFont, "; ")), /* B */ (int)long.Parse(utl.cutl(ref defaultFont, "; ")));
  }

  public static string cutl(ref string anystring, string delimiter)
  {
   string result;
   long pos = anystring.IndexOf(delimiter);
   if (pos == -1) { result = anystring; anystring = ""; } else { result = anystring.Substring(0, (int)pos); anystring = anystring.Substring((int)pos + delimiter.Length); }
   return result;
  }

  public static string cutl(string anystring, string delimiter)
  {
   string result;
   long pos = anystring.IndexOf(delimiter);
   if (pos == -1) { result = anystring; anystring = ""; } else { result = anystring.Substring(0, (int)pos); anystring = anystring.Substring((int)pos + delimiter.Length); }
   return result;
  }

  public static string cutr(ref string anystring, string delimiter)
  {
   string result = "";
   long pos = anystring.Length - delimiter.Length;
   while (pos > -1) if (anystring.Substring((int)pos, delimiter.Length).Equals(delimiter)) { result = anystring.Substring((int)pos + delimiter.Length); anystring = anystring.Substring(0, (int)pos); return result; } else pos--;
   result = anystring;
   anystring = "";
   return result;
  }

  public static string cutr(string anystring, string delimiter)
  {
   string result = "";
   long pos = anystring.Length - delimiter.Length;
   while (pos > -1) if (anystring.Substring((int)pos, delimiter.Length).Equals(delimiter)) { result = anystring.Substring((int)pos + delimiter.Length); anystring = anystring.Substring(0, (int)pos); return result; } else pos--;
   result = anystring;
   anystring = "";
   return result;
  }

  public static string cutl(ref string anystring, int count)
  {
   string result = "";
   if (anystring.Length < count) { result = anystring; anystring = ""; } else { result = anystring.Substring(0, count); anystring = anystring.Substring(count); }
   return result;
  }

  public static string cutl(string anystring, int count)
  {
   string result = "";
   if (anystring.Length < count) { result = anystring; anystring = ""; } else { result = anystring.Substring(0, count); anystring = anystring.Substring(count); }
   return result;
  }

  public static string cutr(ref string anystring, int count)
  {
   string result = "";
   if (anystring.Length < count) { result = anystring; anystring = ""; } else { result = anystring.Substring(anystring.Length - count); anystring = anystring.Substring(0, anystring.Length - count); }
   return result;
  }

  public static string cutr(string anystring, int count)
  {
   string result = "";
   if (anystring.Length < count) { result = anystring; anystring = ""; } else { result = anystring.Substring(anystring.Length - count); anystring = anystring.Substring(0, anystring.Length - count); }
   return result;
  }

  public static string ppV(bool cnd, string prefix, string postfix, string val, string dflt) { if (!cnd) if (dflt.Trim().Length == 0) return ""; else return prefix + dflt.Trim() + postfix; return prefix + val.Trim() + postfix; }  //PrefixPostFixValue
  public static string ppV(bool cnd, string prefix, string postfix, long val, long dflt) { if (!cnd) if (dflt == 0) return ""; else return prefix + dflt + postfix; return prefix + val + postfix; }  //PrefixPostFixValue
  public static string ppV(bool cnd, string prefix, string postfix, bool val, bool dflt) { if (!cnd) if (dflt == false) return ""; else return prefix + dflt + postfix; return prefix + val + postfix; }  //PrefixPostFixValue

  public static string ppV(bool cnd, string prefix, string postfix, string val) { return ppV(cnd, prefix, postfix, val, ""); }  //PrefixPostFixValue
  public static string ppV(bool cnd, string prefix, string postfix, long val) { return ppV(cnd, prefix, postfix, val, 0); }  //PrefixPostFixValue
  public static string ppV(bool cnd, string prefix, string postfix, bool val) { return ppV(cnd, prefix, postfix, val, false); }  //PrefixPostFixValue

  public static string ppV(string prefix, string postfix, string val, string dflt) { return ppV(val.Trim().Length > 0, prefix, postfix, val, dflt); }  //PrefixPostFixValue
  public static string ppV(string prefix, string postfix, long val, long dflt) { return ppV(val != 0, prefix, postfix, val, dflt); }  //PrefixPostFixValue
  public static string ppV(string prefix, string postfix, bool val, bool dflt) { return ppV(val != false, prefix, postfix, val, dflt); }  //PrefixPostFixValue

  public static string ppV(string prefix, string postfix, string val) { return ppV(val.Trim().Length > 0, prefix, postfix, val, ""); }  //PrefixPostFixValue
  public static string ppV(string prefix, string postfix, long val) { return ppV(val != 0, prefix, postfix, val, 0); }  //PrefixPostFixValue
  public static string ppV(string prefix, string postfix, bool val) { return ppV(val != false, prefix, postfix, val, false); }  //PrefixPostFixValue

  public static string pV(bool cnd, string prefix, string val) { return ppV(cnd, prefix, "", val, ""); }  //PrefixValue
  public static string pV(bool cnd, string prefix, long val) { return ppV(cnd, prefix, "", val, 0); }  //PrefixValue
  public static string pV(bool cnd, string prefix, bool val) { return ppV(cnd, prefix, "", val, false); }  //PrefixValue

  public static string pV(string prefix, string val) { return ppV(val.Trim().Length > 0, prefix, "", val, ""); }  //PrefixValue
  public static string pV(string prefix, long val) { return ppV(val != 0, prefix, "", val, 0); }  //PrefixValue
  public static string pV(string prefix, bool val) { return ppV(val != false, prefix, "", val, false); }  //PrefixValue

  public static List<string> s2l(string s, bool trimlines, bool skipempty)
  {
   List<string> ret = new List<string>();
   s = s.Replace("\r\n", "\n");
   //if (collateWs) { s = s.Replace("\t", " ").Replace("�", " "); while (s.IndexOf("  ") > -1) s = s.Replace("  ", " "); }
   while (s.Length > 0) { string x = cutl(ref s, "\n"); if (trimlines) x = x.Trim(); if (!skipempty || x.Length > 0) ret.Add(x); }
   return ret;
  }

  public static string l2s(List<string> l, string delim)
  {
   string ret = "";
   foreach (string x in l) if (ret.Length == 0) ret = x; else ret += delim + x;
   return ret;
  }

  public static string stringArr2s(string[] a, string delim)
  {
   string ret = "";
   foreach (string x in a) if (ret.Length == 0) ret = x; else ret += delim + x;
   return ret;
  }

  public static string intArr2s(long[] a, string delim)
  {
   string ret = "";
   foreach (long x in a) if (ret.Length == 0) ret = "" + x; else ret += delim + x;
   return ret;
  }

  public static bool canRead(string fileName)  //UniCodeOKAY
  {
   if (fileName.ToLower().StartsWith("file://")) fileName = fileName.Substring(8);
   try { StreamReader sr = new StreamReader(fileName); sr.Read(); sr.Close(); }
   catch { return false; }
   return true;
  }


  public static string getTempFileName(bool full)
  {
   string filename = Path.GetTempFileName();
   string path = Path.GetDirectoryName(filename);
   File.Delete(filename);
   return full ? path + "\\" + stdDateTimeStamp(utl.Now(), true) + Path.GetFileNameWithoutExtension(filename) : stdDateTimeStamp(utl.Now(), true) + Path.GetFileNameWithoutExtension(filename);
  }

  public static string toXml(string s)
  {
   if (s == null) return "";
   string ret = s;
   ret = ret.Replace("&", "&amp;");
   ret = ret.Replace("&lt;", "&lt;");
   ret = ret.Replace("&gt;", "&gt;");
   ret = ret.Replace("\"", "&quot;");
   ret = ret.Replace("'", "&apos;");
   return ret;
  }

  public static string fromXml(string s)
  {
   if (s == null) return "";
   string ret = s;
   ret = ret.Replace("&apos;", "'");
   ret = ret.Replace("&quot;", "\"");
   ret = ret.Replace("&gt;", "&gt;");
   ret = ret.Replace("&lt;", "&lt;");
   ret = ret.Replace("&amp;", "&");
   return ret;
  }

  /*
  public static Dictionary<string, string[]> strKeyStrArrays(string literal)
  {
   Dictionary<string, string[]> ret = new Dictionary<string, string[]>();
   while (literal.Length > 0)
   {
    string key = null;
    string[] val = new string[0];
    string line = cutl(ref literal, "´°°`");
    while (line.Length > 0)
    {
     string term = cutl(ref line, "´°`");
     if (key == null)
      key = term;
     else
     {
      string[] val1 = new string[val.Length + 1];
      for (long i = 0; i < val.Length; i++) val1[i] = val[i];
      val1[val.Length] = term;
      val = val1;
     }
    }
    ret.Add(key, val);
   }
   return ret;
  }
  */

  public static void setLeft(Control control, long value) { try { control.Left = (int)value; } catch { } }
  public static void setTop(Control control, long value) { try { control.Top = (int)value; } catch { } }
  public static void setWidth(Control control, long value) { try { control.Width = (int)value; } catch { } }
  public static void setHeight(Control control, long value) { try { control.Height = (int)value; } catch { } }
  public static void setLocation(Control control, long dx, long dy) { try { control.Location = new Point((int)dx, (int)dy); } catch { } }
  public static void setSize(Control control, long dw, long dh) { try { control.Size = new Size((int)dw, (int)dh); } catch { } }

  public static void chgLeft(Control control, long delta) { try { control.Left += (int)delta; } catch { } }
  public static void chgTop(Control control, long delta) { try { control.Top += (int)delta; } catch { } }
  public static void chgWidth(Control control, long delta) { try { control.Width += (int)delta; } catch { } }
  public static void chgHeight(Control control, long delta) { try { control.Height += (int)delta; } catch { } }
  public static void chgLocation(Control control, long dx, long dy) { try { control.Location = new Point(control.Left + (int)dx, control.Top + (int)dy); } catch { } }
  public static void chgSize(Control control, long dw, long dh) { try { control.Size = new Size(control.Width + (int)dw, control.Height + (int)dh); } catch { } }

  public static void cpyLeft(Control control, Control source) { try { control.Left = source.Left; } catch { } }
  public static void cpyTop(Control control, Control source) { try { control.Top = source.Top; } catch { } }
  public static void cpyWidth(Control control, Control source) { try { control.Width = source.Width; } catch { } }
  public static void cpyHeight(Control control, Control source) { try { control.Height = source.Height; } catch { } }
  public static void cpyLocation(Control control, Control source) { try { control.Location = new Point(source.Left, source.Top); } catch { } }
  public static void cpySize(Control control, Control source) { try { control.Size = new Size(source.Width, source.Height); } catch { } }

  public enum PenStyles { PS_SOLID = 0, PS_DASH = 1, PS_DOT = 2, PS_DASHDOT = 3, PS_DASHDOTDOT = 4, PS_NULL = 5, PS_INSIDEFRAME = 6, PS_USERSTYLE = 7, PS_ALTERNATE = 8, PS_STYLE_MASK = 0x0000000F, PS_ENDCAP_ROUND = 0x00000000, PS_ENDCAP_SQUARE = 0x00000100, PS_ENDCAP_FLAT = 0x00000200, PS_ENDCAP_MASK = 0x00000F00, PS_JOIN_ROUND = 0x00000000, PS_JOIN_BEVEL = 0x00001000, PS_JOIN_MITER = 0x00002000, PS_JOIN_MASK = 0x0000F000, PS_COSMETIC = 0x00000000, PS_GEOMETRIC = 0x00010000, PS_TYPE_MASK = 0x000F0000 }
  public enum drawingMode { R2_BLACK = 1 /*0*/, R2_NOTMERGEPEN = 2/*DPon*/, R2_MASKNOTPEN = 3/*DPna*/, R2_NOTCOPYPEN = 4/*PN*/, R2_MASKPENNOT = 5/*PDna*/, R2_NOT = 6/*Dn*/, R2_XORPEN = 7/*DPx*/, R2_NOTMASKPEN = 8/*DPan*/, R2_MASKPEN = 9/*DPa*/, R2_NOTXORPEN = 10/*DPxn*/, R2_NOP = 11/*D*/, R2_MERGENOTPEN = 12/*DPno*/, R2_COPYPEN = 13/*P*/, R2_MERGEPENNOT = 14/*PDno*/, R2_MERGEPEN = 15 /*DPo*/, R2_WHITE = 16/*1*/, R2_LAST = 16 }

  private static int NULL_BRUSH = 5;
  private static int TRANSPARENT = 1;

  public static int RGB(int R, int G, int B) { return (R | (G << 8) | (B << 16)); }

  public static void DrawLine(Graphics dc, PenStyles penStyle, int penWidth, Color col, int X1, int Y1, int X2, int Y2)
  {
   IntPtr hdc = dc.GetHdc();
   IntPtr gdiPen = GDI.CreatePen(penStyle, penWidth, RGB(col.R, col.G, col.B));
   GDI.SetROP2(hdc, drawingMode.R2_XORPEN);
   GDI.SetBkMode(hdc, TRANSPARENT);
   GDI.SetROP2(hdc, drawingMode.R2_XORPEN);
   IntPtr oldPen = GDI.SelectObject(hdc, gdiPen);
   GDI.MoveToEx(hdc, X1, Y1, 0);
   GDI.LineTo(hdc, X2, Y2);
   GDI.SelectObject(hdc, oldPen);
   GDI.DeleteObject(gdiPen);
   dc.ReleaseHdc(hdc);
  }

  public static void DrawRect(Graphics dc, PenStyles penStyle, int penWidth, Color col, int X1, int Y1, int X2, int Y2)
  {
   IntPtr hdc = dc.GetHdc();
   IntPtr gdiPen = GDI.CreatePen(penStyle, penWidth, RGB(col.R, col.G, col.B));
   GDI.SetROP2(hdc, drawingMode.R2_XORPEN);
   GDI.SetBkMode(hdc, TRANSPARENT);
   GDI.SetROP2(hdc, drawingMode.R2_XORPEN);
   IntPtr oldPen = GDI.SelectObject(hdc, gdiPen); IntPtr oldBrush = GDI.SelectObject(hdc, GDI.GetStockObject(NULL_BRUSH));
   GDI.Rectangle(hdc, X1, Y1, X2, Y2);
   GDI.SelectObject(hdc, oldBrush); GDI.SelectObject(hdc, oldPen);
   GDI.DeleteObject(gdiPen); // no need to delete a stock object but we do need to delete the pen
   dc.ReleaseHdc(hdc);
  }

  private class GDI
  {
   private IntPtr hdc;
   private System.Drawing.Graphics grp;

   public void BeginGDI(System.Drawing.Graphics g) { grp = g; hdc = grp.GetHdc(); }
   public void EndGDI() { grp.ReleaseHdc(hdc); }
   public IntPtr CreatePEN(PenStyles fnPenStyle, int nWidth, int crColor) { return CreatePen(fnPenStyle, nWidth, crColor); }
   public bool DeleteOBJECT(IntPtr hObject) { return DeleteObject(hObject); }
   public IntPtr SelectObject(IntPtr hgdiobj) { return SelectObject(hdc, hgdiobj); }
   public void MoveTo(int X, int Y) { MoveToEx(hdc, X, Y, 0); }
   public void LineTo(int X, int Y) { LineTo(hdc, X, Y); }
   public int SetROP2(drawingMode fnDrawMode) { return SetROP2(hdc, fnDrawMode); }
   public void SetPixel(int x, int y, int color) { SetPixelV(hdc, x, y, color & 0x00ffffff); }

   [DllImportAttribute("gdi32.dll")]
   public static extern void SetPixelV(IntPtr hdc, int x, int y, int color);
   [DllImportAttribute("gdi32.dll")]
   public static extern int SetROP2(IntPtr hdc, drawingMode fnDrawMode);
   [DllImportAttribute("gdi32.dll")]
   public static extern bool MoveToEx(IntPtr hdc, int X, int Y, int oldp);
   [DllImportAttribute("gdi32.dll")]
   public static extern bool LineTo(IntPtr hdc, int nXEnd, int nYEnd);
   [DllImportAttribute("gdi32.dll")]
   public static extern IntPtr CreatePen(PenStyles fnPenStyle, int nWidth, int crColor);
   [DllImportAttribute("gdi32.dll")]
   public static extern bool DeleteObject(IntPtr hObject);
   [DllImportAttribute("gdi32.dll")]
   public static extern IntPtr SelectObject(IntPtr hdc, IntPtr hgdiobj);
   [DllImportAttribute("gdi32.dll")]
   public static extern void Rectangle(IntPtr hdc, int X1, int Y1, int X2, int Y2);
   [DllImportAttribute("gdi32.dll")]
   public static extern IntPtr GetStockObject(int brStyle);
   [DllImportAttribute("gdi32.dll")]
   public static extern int SetBkMode(IntPtr hdc, int iBkMode);

  }

  public static void fail(string msg) { throw new Exception(msg); }

  public static bool dmyBool(string notes) { return false; } //for setting a marker in unfinished code
  public static string dmyStr(string notes) { return ""; } //for setting a marker in unfinished code
  public static int dmyInt(string notes) { return 0; } //for setting a marker in unfinished code
  public static long dmyLong(string notes) { return 0; } //for setting a marker in unfinished code

  public static string notNull(object o) { if (o == null) return ""; return (string)o; }

 }

}



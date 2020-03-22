/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.xxdevplus.frmlng;

import org.xxdevplus.udf.Tag;
import java.util.regex.Pattern;
import org.xxdevplus.struct.Pile;
import org.xxdevplus.chain.Chain;

/**
 *
 * @author GeTr
 */

 public class Condition
 {

  private Pile<String> symbs = new Pile<String>(0);
  private Pile<String> comps = new Pile<String>(0);
  private Pile<String> patts = new Pile<String>(0);

  public boolean matches(Tag t) throws Exception
  {
   for (int i = 1; i <= symbs.Len(); i++)
   {
    Chain value = t.val(symbs.g(i));
    String comp = comps.g(i);
    String ptn = patts.g(i);
    boolean neg = comp.startsWith("!");
    if (neg) comp = comp.substring(1);
    if (comp.equals("==")) { if (neg ^ value.equals(ptn)) return true; }
    else if (comp.equals("°=")) { if (neg ^ value.Equals(ptn)) return true; }
     else if (comp.equals("~~")) { if (neg ^ Pattern.compile(ptn).matcher(value.text()).find()) return true; }
      else if (comp.equals("°~"))  { if (neg ^ Pattern.compile(ptn.toUpperCase()).matcher(value.uText()).find()) return true; }
       else throw new Exception("Invalid Compare Operator for Tag Filter! Use one of these: == °= ~~ °~ !== !°= !~~ !°~");
   }
   return false;
  }

  public Condition(Pile<String> cond) throws Exception
  {
   String sym = cond.Name();
   for (int i = 1; i <= cond.Len(); i++)
   {
    String symbol = sym;
    String cnd = cond.g(i);
    String comp = "";
    String ptn = "";
    boolean neg = false;
    if ((!cnd.startsWith("!")) && (!cnd.startsWith("°")) && (!cnd.startsWith("~")) && (!cnd.startsWith("=")))
    {
     int pos = -1;
     if (cnd.indexOf("=") > -1) { pos = cnd.indexOf("="); if (cnd.indexOf("~") > -1) pos = Math.min(pos, cnd.indexOf("~")); } else if (cnd.indexOf("~") > -1) pos = cnd.indexOf("~");
     if (cnd.substring(0, pos).indexOf("°") > -1) pos = cnd.indexOf("°");
     String key = cnd.substring(0, pos);
     cnd = cnd.substring(pos);
     neg = key.endsWith("!");
     if (neg) { comp = cnd.substring(0, 2); ptn = cnd.substring(2); key = key.substring(0, key.length() - 1); } else { comp = cnd.substring(0, 2); ptn = cnd.substring(2); }
     symbol = key;
    } else
    {
     neg = (cnd.startsWith("!"));
     if (neg) { comp = cnd.substring(1, 3); ptn = cnd.substring(3); } else { comp = cnd.substring(0, 2); ptn = cnd.substring(2); }
    }
    patts.Add(ptn);
    comps.Add(comp);
    symbs.Add(symbol);
   }
  }



 }


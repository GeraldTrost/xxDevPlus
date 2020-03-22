/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.xxdevplus.udf;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.xxdevplus.utl.utl;


/**
 *
 * @author Angerer Bernhard
 */
public class ElementFilter 
{

 Element el = null;
 public ElementFilter(Element el)
 {
  this.el = el;
 }
 
 public Elements withOut(String ... selectors) throws Exception
 {
  Elements ret = new Elements();
  for (Element ce : el.children())
  {
   int found = 0;
   for (String slc : selectors) 
    if (ce.select(slc).size() > 0) 
     if (ce.select(slc).get(0) == ce)
      found = 1; // the ce itself  is the unwanted element
     else
      found = 2; // one of the ce'S children is the unwanted element
   if (found == 2)
    for (Element f : new ElementFilter(ce).withOut(selectors)) ret.add(f);
   else
    if (found == 0)
    {
     // utl.say("               --------------------------------------        ");
     // utl.say(ce.text());
     ret.add(ce);
    }
  }
  return ret;
 }
 
}





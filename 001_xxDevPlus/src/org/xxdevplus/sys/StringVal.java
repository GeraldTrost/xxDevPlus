



package org.xxdevplus.sys;

import java.lang.reflect.Method;
import org.xxdevplus.chain.Chain;

public class StringVal 
{
 public Object creator  = null;
 public Method method   = null;
 public Class  cls      = null;
 public Class  paramCls = null;
 
 public StringVal (String method, Class paramCls) throws Exception
 {
  Chain c = new Chain(method); //"org.everest.base.StringMethods.testElement"
  Chain m = c.after(-1, ".");
  c = c.before(m).upto(-2);
  cls = Class.forName(c.text());
  creator = cls.newInstance();
  try { this.method  = cls.getMethod(m.text(), paramCls);} catch (Exception ex) { System.out.println("EXCEPTION " + ex.getMessage()); }
 }
 
 public StringVal (Object creator , String method, Class cls, Class paramCls)
 {
  this.creator = creator ;
  try { this.method  = cls.getMethod(method, paramCls);} catch (Exception ex) { System.out.println("EXCEPTION " + ex.getMessage()); }
 }
         
 
}


package org.xxdevplus.sys;  // original @Copyright com.lordjoe.csharp.CallBuilder @author Steve Lewis smlewis@lordjoe.com

import java.lang.reflect.*;
import java.util.List;
import java.util.ArrayList;

public class CallBuilder
{

 private        final            Class  iface;
 private        final            Class  rturn;
 private        final           Class[] agmts;
 public                          Class  Return        (                                   )                                         { return rturn; }
 public                        Class[]  Arguments     (                                   )                                         { return agmts; }
 public                          Class  Interface     (                                   )                                         { return iface; }

 public  static final         Method[]  noMethod                                                                                    = {};
 public  static final         Object[]  noObject                                                                                    = {};
 public  static final    CallBuilder[]  noCallBuilder                                                                               = {};
 public  static final      CallBuilder  callBuilder                                                                                 = new CallBuilder(Runnable.class);
 public  static               Runnable  buildRunnable (Object item,      String methodName)                                         { return((Runnable)callBuilder.build(item, methodName)); }
 public  static               Runnable  buildRunnable (Class item,       String methodName)                                         { return((Runnable)callBuilder.build(item, methodName)); }
 public                    CallBuilder                (Class[] params,      Class retClass)                                         { iface = null; rturn = retClass; agmts = params; }
 public                    CallBuilder                (Class                  TheInterface)                                         { iface =  TheInterface; Method met = findMethod(TheInterface); rturn = met.getReturnType(); agmts = met.getParameterTypes(); }
 public                       Delegate  build         (Class  target,    String MethodName)                                         { Class myInterface =  Interface(); CallHandler theDelegate = new CallHandler(null,target,MethodName,this); if(myInterface != null) { Class[] interfaces = { myInterface,Delegate.class }; Delegate ret = (Delegate)java.lang.reflect.Proxy.newProxyInstance( target.getClassLoader(), interfaces,theDelegate); return(ret); } return((Delegate)theDelegate); }
 public                       Delegate  build         (Object target,    String MethodName)                                         { Class myInterface =  Interface(); CallHandler theDelegate = new CallHandler(target,target.getClass(),MethodName,this); if(myInterface != null) { Class[] interfaces = { myInterface,Delegate.class }; Delegate ret = (Delegate)java.lang.reflect.Proxy.newProxyInstance( target.getClass().getClassLoader(), interfaces,theDelegate); return(ret); } if(theDelegate instanceof Delegate) return((Delegate)theDelegate); throw new ClassCastException();  }

 protected class  CallHandler implements Delegate,InvocationHandler
 {
  private   final  Method       method;
  private   final  Object       target;
  private   final  CallBuilder  m_Template;
  protected                              CallHandler  (Object tgt, Class tgtClass, String meth, CallBuilder templ)                  { m_Template = templ; this.target = tgt; method = findSuitableMethod(tgtClass, meth, templ); }
  public                         Object  invoke       (                                                          ) throws Exception { return(call(noObject)); }
  public                         Object  invoke       (Object                                                 arg) throws Exception { Object[] args = { arg }; return(call(args)); }
  public                         Object  invoke       (Object arg1,                                   Object arg2) throws Exception { Object[] args = { arg1, arg2 }; return(call(args)); }
  public                         Object  invoke       (Object proxy, Method method,                 Object[] args) throws Exception { return(call(args)); }
  public                         Object  call         (Object[]                                              args) throws Exception { try { Object ret = getMethod().invoke(getTarget(),args); return(ret); } catch(Exception ex1) {throw new Exception("Delegate Error " + ex1.getMessage()); } }
  public                         Method  getMethod    (                                                          )                  { return method; }
  public                         Object  getTarget    (                                                          )                  { return target; }
  protected                        void  validateArgs (Object[]                                              args) throws Exception
  {
   Class[] MyArgs = Arguments();
   if(args.length !=  MyArgs.length) throw new Exception("Delegate required " +  MyArgs.length + "arguments");
   for(int i = 0; i < args.length; i++) { if(!MyArgs[i].isInstance(args[i]) ) throw new Exception("Argument " + i + " must be of class " +  MyArgs[i].getName()); }
  }
 }

 protected static boolean isSuitableMethod(Method testMethod,Class[] args,Class retClass) { Class[] methodArgs = testMethod.getParameterTypes(); for(int i = 0; i < methodArgs.length; i++) { Class arg = methodArgs[i]; if(!arg.isAssignableFrom(args[i])) return(false); } isValidReturn(testMethod,retClass); return(true); }
 protected static Method[] getCandidateMethods(Class targetClass,String MethodName,int nargs) { Method[] possibilities =  targetClass.getMethods(); List holder = new ArrayList(); for(int i = 0; i < possibilities.length; i++) { Method possibility = possibilities[i]; if( possibility.getName().equals(MethodName) && possibility.getParameterTypes().length == nargs && Modifier.isPublic(possibility.getModifiers())) holder.add(possibility); } return((Method[])holder.toArray(noMethod)); }
 protected static boolean isValidReturn(Method test, Class retClass) { if(retClass == null) return(true); if(test.getReturnType() ==  retClass) return(true); if(retClass.isAssignableFrom(test.getReturnType())) return(true); return(false); }

 protected static Method findSuitableMethod(Class targetClass,String MethodName,CallBuilder templ)
 {
  Class[] args = templ.Arguments();
  Class retClass = templ.Return();
  try
  {
   Method ret =  targetClass.getMethod(MethodName,args);
   if(!isValidReturn(ret,retClass)) throw new IllegalArgumentException("Requested method returns wrong type");
   if(!Modifier.isPublic(ret.getModifiers())) throw new IllegalArgumentException("Requested method is not public");
   return(ret);
  }
  catch(Exception ex) {} // on to try2
  Method[] possibilities = getCandidateMethods(targetClass,MethodName,args.length);
  for(int i = 0; i < possibilities.length; i++)
  {
   Method possibility = possibilities[i];
   if(isSuitableMethod(possibility,args,retClass)) return(possibility);
  }
  throw new IllegalArgumentException("No suitable method found");
 }

 protected static Method findMethod(Class TheInterface)
 {
  if(!TheInterface.isInterface()) throw new IllegalArgumentException("DelegateTemplate must be constructed with an interface");
  Method[] methods = TheInterface.getMethods();
  Method ret = null;
  for(int i = 0; i < methods.length; i++) { Method test = methods[i]; if(Modifier.isAbstract(test.getModifiers())) { if(ret != null) throw new IllegalArgumentException("DelegateTemplate must be constructed " + " with an interface implementing only one method!"); ret = test; } }
  if(ret == null) throw new IllegalArgumentException("DelegateTemplate must be constructed " + " with an interface implementing exactly method!");
  return(ret);
 }

}


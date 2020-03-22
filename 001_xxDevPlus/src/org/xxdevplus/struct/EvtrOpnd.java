



package org.xxdevplus.struct;

import org.xxdevplus.chain.Chain;







public class EvtrOpnd 
{

 private String        sig  =    "";
 private Pile<Chain>    op  =  null;
 
 public EvtrOpnd (String sig, Chain ... op)
 {
  this.sig   = sig;
  this.op    = new Pile<Chain>(0, op);
 }

 public EvtrOpnd (String sig, String ... op) throws Exception
 {
  this.sig   = sig;
  this.op    = new Pile<Chain>();
  for (String o : op) this.op.Push(new Chain(o));
 }

 public EvtrOpnd (String sig, Pile<Chain> op)
 {
  this.sig   = sig;
  this.op    = op;
 }

 public EvtrOpnd (Chain op) throws Exception
 {
  this("", op);
 }

 public EvtrOpnd (String op) throws Exception
 {
  this("", new Chain(op));
 }
 
 public String sig()
 {
  return this.sig;
 }

 public Chain op()
 {
  return this.op.g(1);
 }

 public Pile<Chain> opVnts()
 {
  return this.op;
 }

 public void opVnts(Pile<Chain> val) throws Exception
 {
  this.op = val;
 }
         
}



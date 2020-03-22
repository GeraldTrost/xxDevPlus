



package org.xxdevplus.struct;

import org.xxdevplus.chain.Chain;
import org.xxdevplus.frmlng.Pick;

public class CplrOpnd 
{

 private String           sig =  ""                ;
 private Pile<Chain>      ops =  new Pile<Chain>() ;  // this may represent an Operand or a List of Operands, sample max('a') has an Operand as Argument and max('a', 'b', 'c') has a List Of Operands as Argument
 private Pile<Chain> restrict =  null              ;
 private            CplrOpnd  ( String sig, Pile<Chain> ops, Pile<Chain> restrict) throws Exception { this.restrict = restrict         ; this.sig = sig; ops(ops)                          ; }
 public             CplrOpnd  ( String sig,                              Chain op) throws Exception { this.restrict = new Pile<Chain>(); this.sig = sig; op(op)                            ; }
 public             CplrOpnd  ( Chain                                          op) throws Exception { this("", op)                                                                         ; }
 public CplrOpnd       clone  (                                                  )                  { try { return new CplrOpnd(sig, ops, restrict); } catch (Exception ex) {} return null ; }
 public String           sig  (                                                  )                  { return this.sig                                                                      ; }
 public Pile<Chain> restrict  (                                                  )                  { return this.restrict                                                                 ; }
 public Pile<Chain>      ops  (                                                  )                  { return this.ops                                                                      ; }
 public void              op  ( Chain                                          op) throws Exception { this.ops = new Pile<Chain>(0, op);                                                   ; }
 public void             ops  ( Pile<Chain>                                   ops) throws Exception { this.ops = ops                                                                       ; }
         
}



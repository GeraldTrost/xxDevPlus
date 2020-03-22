




//** @author  GeTr Copyright© 1996-2020 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties
//** @license You may use, modify, distribute this code under GNU public dual licence free for NON-Commercial use as long as this notice remains unchanged.
//** @comment Allows manipulations directly with a Chain's Char-Buffer abd thereby alter firmer Chain Objects as side-effexct



package org.xxdevplus.chain;


 public class BufOp
 {
  //** @author GeTr Copyright© 1996-2020 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties, You may use, modify, distribute this code under GNU public dual licence free for NON-Commercial use as long as this notice remains unchanged.
  //** @comment Pile is like List<typ> but counts from 1 and also has negative counting from the end backwards
  static int _ic;String ass(boolean xp,String...msg)throws Exception{if(!xp)throw new Exception(ass(true)+" slfTst:"+msg);return "BufOp";}private void slfTst()throws Exception{tstSimple();}protected void init()throws Exception{if(_ic++==0)slfTst();}
  private void tstSimple() {}
  
  private Chain Base;
  BufOp(Chain Base) { this.Base = Base; }

  public Delete        _del(            )                  {return new Delete          (true, Base);      }
  public Delete        _del(int      cnt)                  {return new Delete          (true, Base, cnt); }
  public ReplaceWith   _rpw(String   txt)                  {return new ReplaceWith     (true, Base, txt); }
  public ReplaceWith   _rpw(Chain    txt) throws Exception {return new ReplaceWith     (true, Base, txt); }
  public Insert        _ins(String   txt)                  {return new Insert          (true, Base, txt); }
  public Insert        _ins(Chain    txt) throws Exception {return new Insert          (true, Base, txt); }

 }

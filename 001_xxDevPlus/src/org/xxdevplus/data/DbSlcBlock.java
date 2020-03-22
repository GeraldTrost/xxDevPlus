/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.xxdevplus.data;

import org.xxdevplus.frmlng.EvalExpert;
import org.xxdevplus.frmlng.Xpn;
import org.xxdevplus.utl.ctx;
import org.xxdevplus.chain.Chain;

/**
 *
 * @author GeTr
 */

 public class DbSlcBlock extends Xpn<DbSlc>
 {
  //Copyright© 1996-2020 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties, You may use, modify, distribute this code under GNU public licence for NON-Commercial use as long as this notice remains unchanged.
  private static boolean selfTested  = false; private static String ass(boolean expr) throws Exception { if (!expr) throw new Exception(ass(true) + " SelfTest Failure"); return "DbSlcBlock"; } 
  
  public  void init1() throws Exception 
  {
   //Dtv.Set(" _ "  , "AP``¹``>¹²``, ²``");
   //Dtv.Set(" & "  , "CC``¹``>´°(¹´, ²´)````");
   //Dtv.Set(" >$ " , "CS``¹``>(´¹ ²)´`` ° ²``");
   //Dtv.Set(" && " , "AN``¹``>(´¹ ²)´`` ° ²````");
   //Dtv.Set(" || " , "OR``¹``>(´¹ ²)´`` ° ²``");
   //Dtv.Set(" ^^ " , "XR``¹``>(´¹ ²)´`` ° ²``");
   //Dtv.Set(" °° " , "XS``¹``>(´¹ ²)´`` ° ²``");
   //Dtv.Set(" !°° ", "NX``¹``>(´¹ ²)´`` ° ²``");
   //Dtv.Set(" == " , "IS``¹``>(´¹ ²)´`` ° ²``");
   //Dtv.Set(" !== ", "NS``¹``>(´¹ ²)´`` ° ²``");
   //Dtv.Set(" = "  , "EQ``¹``>(´¹ ²)´`` ° ²``");
   //Dtv.Set(" != " , "NE``¹``>(´¹ ²)´`` ° ²``");
   //Dtv.Set(" > "  , "GT``¹``>(´¹ ²)´`` ° ²``");
   //Dtv.Set(" < "  , "LT``¹``>(´¹ ²)´`` ° ²``");
   //Dtv.Set(" >= " , "GE``¹``>(´¹ ²)´`` ° ²``");
   //Dtv.Set(" <= " , "LE``¹``>(´¹ ²)´`` ° ²``");
   //Dtv.Set(" ~ "  , "LK``¹``>(´¹ ²)´`` ° ²``");
   //Dtv.Set(" !~ " , "NL``¹``>(´¹ ²)´`` ° ²``");
   //Dtv.Set(" ° "  , "IN``¹``>(´¹ ²)´`` ° ²``");
   //Dtv.Set(" !° " , "NI``¹``>(´¹ ²)´`` ° ²``");
   //Dtv.Set(" ≡ "  , "MV``¹``>(´¹ ²)´`` ° ²``");
   //Dtv.Set(" !≡ " , "NV``¹``>(´¹ ²)´`` ° ²``");
   //Dtv.Set(" ≡> " , "MU``¹``>(´¹ ²)´`` ° ²``");
   //Dtv.Set(" !≡> ", "NU``¹``>(´¹ ²)´`` ° ²``");
   //Dtv.Set(" ≡< " , "ML``¹``>(´¹ ²)´`` ° ²``");
   //Dtv.Set(" !≡< ", "NL``¹``>(´¹ ²)´`` ° ²``");
   Dtv().Set(" <|> ", "UNION``¹``>(´¹´²)´``) ° (²``");
   Dtv().Set(" <&> ", "INTERSECT``¹``>(´¹)´ ° ²``(²)``");
   Dtv().Set(" <-> ", "EXCEPT``¹``>(´¹)´ ° ²``(²)``");
   if (!selfTested) selfTest(); 
  }

  private static void selfTest() throws Exception
  {
   selfTested = true;
   Db db = new Db(new ctx().DbDrivers());
   ass( new DbSlcBlock(" <|> ", db.Join("tbla a").sC("id").SLC(), db.Join("tblb b").sC("id").SLC(), db.Join("tblc c").sC("id").SLC()).val().replace("\r\n", "").equals("( SELECT id  FROM tbla a) UNION ( SELECT id  FROM tblb b) UNION ( SELECT id  FROM tblc c)"));
  }

  @Override public void visEdit(Object parentForm, Object... more) throws Exception
  {
   super.visEdit(parentForm);
   frmSelectView frm = new frmSelectView((Db)(more[0]), this);
   frm.setModal(true);
   frm.setVisible(true);
  }

  @Override public String val(int i, EvalExpert evx) throws Exception
  {
   return (evx instanceof Db) ? ((DbSlc)g(i)).sql((Db)evx) : ((DbSlc)g(i)).sql() ;
   //throw new Exception("Err: NYI: DbSlcBlock.val()");
  }

  public  DbSlcBlock (DbSlc   lOpnd                                ) throws Exception { super("",                  lOpnd); init1(); }
  public  DbSlcBlock (DbSlc   lOpnd, Chain cptor,       DbSlc rOpnd) throws Exception { super(cptor.text(), lOpnd, rOpnd); init1(); }
  public  DbSlcBlock (                                             ) throws Exception { super(                          ); init1(); }
  public  DbSlcBlock (int                                     count) throws Exception { super(count                     ); init1(); }
  public  DbSlcBlock (DbSlc...                                 opnd) throws Exception { super(opnd                      ); init1(); }
  //public  DbSlcBlock (Pile<DbSlc> opnd                             ) : base(opnd                    ) { init(); }
  public  DbSlcBlock (String optor,                   DbSlc... opnd) throws Exception { super(optor,                opnd); init1(); }
  //public  DbSlcBlock (string optor, Pile<DbSlc> opnd               ) : base(optor,      ^       opnd) { init(); }

 }












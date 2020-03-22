

using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using Microsoft.Win32;

using ndBase;
using ndString;

namespace ndData
{

 public class DbSlcBlock : Xpn<DbSlc>
 {
  //Copyright© 1996-2012 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties, You may use, modify, distribute this code under GNU public licence for NON-Commercial use as long as this notice remains unchanged.
  private static bool selfTested  = false; private static string ass(bool expr) { if (!expr) throw new Exception(ass(true) + " SelfTest Failure"); return "DbSlcBlock"; } 
  
  private void init() 
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
   Dtv.Set(" <|> ", "UNION``¹``>(´¹´²)´``) ° (²``");
   Dtv.Set(" <&> ", "INTERSECT``¹``>(´¹)´ ° ²``(²)``");
   Dtv.Set(" <-> ", "EXCEPT``¹``>(´¹)´ ° ²``(²)``");
   if (!selfTested) selfTest(); 
  }

  private static void selfTest()
  {
   selfTested = true;
   Db db = new Db(new ctx().DbDrivers);
   ass(new DbSlcBlock(" <|> ", db.Grid("tbla a").sC("id").SLC, db.Grid("tblb b").sC("id").SLC, db.Grid("tblc c").sC("id").SLC).val().Replace("\r\n", "").Equals("( SELECT id  FROM tbla a) UNION ( SELECT id  FROM tblb b) UNION ( SELECT id  FROM tblc c)"));
  }

  public override void visEdit(Object parentForm, params Object[] more) 
  {
   base.visEdit(parentForm);
   frmSelectView frm = new frmSelectView((Db)(more[0]), this);
   frm.Show((IWin32Window)parentForm);
  }

  public override string val(int i, EvalExpert evx)
  {
   return (evx.GetType() == typeof(Db)) ? ((DbSlc)this[i]).sql((Db)evx) : ((DbSlc)this[i]).sql() ;
   //throw new Exception("Err: NYI: DbSlcBlock.val()");
  }

  public  DbSlcBlock (DbSlc   lOpnd                                ) : base("",                lOpnd) { init(); }
  public  DbSlcBlock (DbSlc   lOpnd, Reach cptor, DbSlc rOpnd      ) : base(cptor.text, lOpnd, rOpnd) { init(); }
  public  DbSlcBlock (                                             ) : base(                        ) { init(); }
  public  DbSlcBlock (int count                                    ) : base(count                   ) { init(); }
  public  DbSlcBlock (params DbSlc[] opnd                          ) : base(opnd                    ) { init(); }
  //public  DbSlcBlock (Pile<DbSlc> opnd                             ) : base(opnd                    ) { init(); }
  public  DbSlcBlock (string optor, params DbSlc[] opnd            ) : base(optor,              opnd) { init(); }
  //public  DbSlcBlock (string optor, Pile<DbSlc> opnd               ) : base(optor,      ^       opnd) { init(); }

 }

}











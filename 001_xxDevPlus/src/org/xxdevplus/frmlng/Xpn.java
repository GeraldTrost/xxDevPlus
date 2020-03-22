


//** @author  GeTr Copyright© 1996-2020 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties
//** @license You may use, modify, distribute this code under GNU public dual licence free for NON-Commercial use as long as this notice remains unchanged.
//** @comment General Expression


package org.xxdevplus.frmlng;

import org.xxdevplus.struct.KeyPile;
import org.xxdevplus.struct.Pile;
import org.xxdevplus.utl.utl;

 public abstract class Xpn<typ> extends Pile<typ> implements EvalExpert
 {
  private static boolean selfTested  = false; private static String ass(boolean expr) throws java.lang.Exception { if (!expr) throw new Exception(ass(true) + " SelfTest Failure"); return "Xpn<>";  }

  public static KeyPile<String, String> _dtv = new KeyPile<String, String>();

  public KeyPile<String, String> Dtv() { return _dtv; } public void Dtv(KeyPile<String, String> value) { _dtv = value; } 
  public String val(Object obj)   { return "Err: Xpn is not a fully implemented EvalExpert."; } 
  public String val() throws Exception { return val(this); }

  public String optor() { return Name(); } public void optor ( String value) { Name(value);  } 
  public typ    lOpnd() { return g(1);   } public void lOpnd ( typ    value) { s(value, 1);   } 
  public typ    rOpnd() { return g(2);   } public void rOpnd ( typ    value) { s(value, 2);   } 

  private static void selfTest() throws java.lang.Exception
  {
   selfTested = true; // we cannot instanciate the abstract class Xpn sol tests will be done in derieved classes

   // Note: these Operator Evaluation Directives are some fancy presets for studying new fancy formula parsers. the "real thing" directives are to be defined in derieved classes or in EvalExpert implementations!.

   _dtv.Set(" _ "    , "AP``¹``>¹²``, ²``");            // append
   _dtv.Set(" & "    , "CC``¹``>´°(¹´, ²´)````");       // convert charray
   _dtv.Set(" >$ "   , "CS``¹``>(´¹ ²)´`` ° ²``");      // convert string

   _dtv.Set(" |. "   , "LTM``¹``>(´¹ ²)´`` ° ²``");     // lTrim
   _dtv.Set(" .| "   , "RTM``¹``>(´¹ ²)´`` ° ²``");     // rTrim
   _dtv.Set(" |.| "  , "TM``¹``>(´¹ ²)´`` ° ²``");      // trim

   _dtv.Set(" && "   , "AN``¹``>(´¹ ²)´`` ° ²````");
   _dtv.Set(" || "   , "OR``¹``>(´¹ ²)´`` ° ²``");
   _dtv.Set(" ^^ "   , "XR``¹``>(´¹ ²)´`` ° ²``");
   _dtv.Set(" °° "   , "XS``¹``>(´¹ ²)´`` ° ²``");
   _dtv.Set(" !°° "  , "NX``¹``>(´¹ ²)´`` ° ²``");
   _dtv.Set(" == "   , "IS``¹``>(´¹ ²)´`` ° ²``");
   _dtv.Set(" !== "  , "NS``¹``>(´¹ ²)´`` ° ²``");
   _dtv.Set(" = "    , "EQ``¹``>(´¹ ²)´`` ° ²``");
   _dtv.Set(" != "   , "NE``¹``>(´¹ ²)´`` ° ²``");
   _dtv.Set(" > "    , "GT``¹``>(´¹ ²)´`` ° ²``");
   _dtv.Set(" < "    , "LT``¹``>(´¹ ²)´`` ° ²``");
   _dtv.Set(" >= "   , "GE``¹``>(´¹ ²)´`` ° ²``");
   _dtv.Set(" <= "   , "LE``¹``>(´¹ ²)´`` ° ²``");
   _dtv.Set(" ~ "    , "LK``¹``>(´¹ ²)´`` ° ²``");
   _dtv.Set(" !~ "   , "NL``¹``>(´¹ ²)´`` ° ²``");
   _dtv.Set(" ° "    , "IN``¹``>(´¹ ²)´`` ° ²``");
   _dtv.Set(" !° "   , "NI``¹``>(´¹ ²)´`` ° ²``");
   _dtv.Set(" ≡ "    , "MV``¹``>(´¹ ²)´`` ° ²``");
   _dtv.Set(" !≡ "   , "NV``¹``>(´¹ ²)´`` ° ²``");
   _dtv.Set(" ≡> "   , "MU``¹``>(´¹ ²)´`` ° ²``");
   _dtv.Set(" !≡> "  , "NU``¹``>(´¹ ²)´`` ° ²``");
   _dtv.Set(" ≡< "   , "ML``¹``>(´¹ ²)´`` ° ²``");
   _dtv.Set(" !≡< "  , "NL``¹``>(´¹ ²)´`` ° ²``");
   _dtv.Set(" <|> ", "UNION``¹``>(´¹´²)´``) ° (²``");
   _dtv.Set(" <&> ", "INTERSECT``¹``>(´¹)´ ° ²``(²)``");
   _dtv.Set(" <-> ", "EXCEPT``¹``>(´¹)´ ° ²``(²)``");

   _dtv.Set(" sin "  , "SIN``¹``>(´¹ ²)´`` ° ²``");
   _dtv.Set(" cos "  , "COS``¹``>(´¹ ²)´`` ° ²``");
   _dtv.Set(" tan "  , "TAN``¹``>(´¹ ²)´`` ° ²``");
  }

  public abstract String val(int inx, EvalExpert evx) throws Exception; //for debugging only (selfTest)        {return (this[inx]).ToString(); }

  public static boolean dbg  = false;

  public    String   optr       = "";
  public    String   optrSymbol = "";

  protected String[] dtv0       = new String[] {""};
  protected String[] dtv1lS     = new String[] {""}; protected String[] dtv1mS = new String[] {""}; protected String[] dtv1rS = new String[] {""};
  protected String[] dtv1lR     = new String[] {""}; protected String[] dtv1mR = new String[] {""}; protected String[] dtv1rR = new String[] {""};
  protected String[] dtv2       = new String[] {""};
  public    boolean  revert     = false;
  public    boolean  left2right = true;




  private void splitDirective(String[] dtv)
  {
   dtv2 = new String[] {""}; revert = false;
   optr       = utl.cutl(dtv, "``");
   dtv0[0]    = utl.cutl(dtv, "``");
   dtv1rR[0]  = utl.cutl(dtv, "``");
   left2right = dtv1rR[0].startsWith(">");
   dtv1rR[0]  = dtv1rR[0].substring(1);
   dtv1lR[0]  = utl.cutl(dtv1rR , "¹");
   dtv1mR[0]  = ""; if (dtv1rR[0].indexOf("²") > -1) dtv1mR[0] = utl.cutl(dtv1rR, "²"); else { revert = true; dtv1mR[0]  = dtv1lR[0]; dtv1lR[0] = utl.cutl(dtv1mR, "²"); }
   dtv1lS[0]  = utl.cutl(dtv1lR, "´");
   dtv1mS[0]  = utl.cutl(dtv1mR, "´");
   dtv1rS[0]  = utl.cutl(dtv1rR, "´");
   dtv2[0]    = utl.cutl(dtv, "``");
   optrSymbol = optr.replace("(", "");
  }

  private String debug(String res) { return dbg? "[" + res + "]" : res; }
  
  public String val(EvalExpert evx) throws Exception //directives dtv are explicitly given
  {
   String ret = "";
   if (Len() == 0) 
   {
    int i = Len();
    throw new Exception("Err: Xpn: no operand given.");
   }
   if (Name().length() == 0) {for(int i = 1; i <= Len(); i++) ret += val(i, evx); return ret; }
   try { splitDirective(new String[] {evx.Dtv().g(Name())});  } catch (Exception ex) {throw new Exception("Err: Xpn: undef operator " + Name());}
   if ((Len() == 1) && (dtv0[0].length() > 0)) return dtv0[0].replace("¹", val(1, evx)).replace("°", optrSymbol);
   if (dtv2[0].length() == 0)
   {
    if (left2right)
    {
     ret = val(1, evx) + dtv1mS[0];
     if (revert) for (int i = Len(); i > 1; i--) ret = dtv1lR[0] + val(i, evx) + dtv1mR[0] + ret + dtv1rR[0]; else for (int i = 2; i <= Len(); i++) ret = dtv1lR[0] + ret + dtv1mR[0] + val(i, evx) + dtv1rR[0];
     return (revert) ? (dtv1lS[0] + ret + dtv1rS[0]).replace("°", optrSymbol) : (dtv1lS[0] + ret + dtv1rS[0]).replace("°", optrSymbol);
    } else
    {
     ret = dtv1mS[0] + val(Len(), evx);
     if (revert) for (int i = 1; i < Len(); i++) ret = dtv1lR[0] + ret + dtv1mR[0] + val(i, evx) + dtv1rR[0]; else for (int i = Len() - 1; i > 0; i--) ret = dtv1lR[0] + val(i, evx) + dtv1mR[0] + ret + dtv1rR[0];
     return (revert) ? (dtv1lS[0] + ret + dtv1rS[0]).replace("°", optrSymbol) : (dtv1lS[0] + ret + dtv1rS[0]).replace("°", optrSymbol);
    }
   } else
   {
    if (revert) for (int i = 2; i <= Len(); i++) ret = dtv2[0].replace("²", val(i, evx)) + ret; else for (int i = Len(); i > 1; i--) ret = dtv2[0].replace("²", val(i, evx)) + ret;
    return (revert) ? (dtv1lS[0] + dtv1lR[0] + ret + dtv1mR[0] + val(1, evx) + dtv1rR[0] + dtv1rS[0]).replace("°", optrSymbol) : (dtv1lS[0] + dtv1lR[0] + val(1, evx) + dtv1mR[0] + ret + dtv1rR[0] + dtv1rS[0]).replace("°", optrSymbol);
   }
  }

  //public static Xpn<object> xp(string optr, params object[] opnd) { return new Xpn<object>(optr, opnd); }
  //public string val() { return val(_dtv); }

  public Pile<Xpn<typ>> fromTxtDef(String def, boolean isSmb)
  {
   //AttGeTr TBD TO BE DONE !!!
   return new Pile<Xpn<typ>>();
  }

  public Xpn(                                   ) throws Exception { super()                          ; init(); }
  public Xpn(int                           count) throws Exception { super(count)                     ; init(); }
  public Xpn(typ...                         opnd) throws Exception { super(0, opnd)                   ; init(); }
  //public Xpn(Pack<typ>                    opnd) throws Exception { super(opnd.array())              ; init(); }
  public Xpn(String optor,           typ... opnd) throws Exception { super(optor, 0, opnd)            ; init(); }
  //public Xpn(String optor,        Pack<typ> opnd) throws Exception { super(optor, true, opnd.array()) ; init(); }
    
 }





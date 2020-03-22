


//** @author  GeTr Copyright© 1996-2020 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties
//** @license You may use, modify, distribute this code under GNU public dual licence free for NON-Commercial use as long as this notice remains unchanged.
//** @comment General Expression



using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;


using org_xxdevplus_sys;
using org_xxdevplus_utl;
using org_xxdevplus_struct;
using org_xxdevplus_chain;

namespace org_xxdevplus_frmlng
{

 public abstract class Xpn<typ> : Pile<typ>, EvalExpert
 {
  //Copyright© 1996-2012 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties, You may use, modify, distribute this code under GNU public licence for NON-Commercial use as long as this notice remains unchanged.
  private static bool selfTested  = false;  private static string ass(bool expr) { if (!expr) throw new Exception(ass(true) + " SelfTest Failure"); return "Xpn<>";  }

  protected static KeyPile<string, string> _dtv = new KeyPile<string, string>();

  public KeyPile<string, string> Dtv { get { return _dtv; } set { _dtv = value; } }
  public string val(object obj) { throw new Exception("Err: Xpn is not a fully implemented EvalExpert."); }
  public string val() { return val(this); }

  public string optor { get { return Name;    } set { Name = value;    } }
  public typ    lOpnd { get { return this[1]; } set { this[1] = value; } }
  public typ    rOpnd { get { return this[2]; } set { this[2] = value; } }

  private void init()  { if (!selfTested) selfTest(); }

  private static void selfTest()
  {
   selfTested = true; // we cannot instanciate the abstract class Xpn sol tests will be done in derieved classes

   // Note: these Operator Evaluation Directives are some fancy presets for studying new fancy formula parsers. the "real thing" directives are to be defined in derieved classes or in EvalExpert implementations!.

   _dtv.Set(" _ "    , "AP``¹``>¹²``, ²``");              // append
   _dtv.Set(" & "    , "CC``¹``>´°(¹´, ²´)````");         // convert charray 
   _dtv.Set(" >$ "   , "CS``¹``>(´¹ ²)´`` ° ²``");        // convert string

   _dtv.Set(" |. "   , "LTM``¹``>(´¹ ²)´`` ° ²``");       // lTrim
   _dtv.Set(" .| "   , "RTM``¹``>(´¹ ²)´`` ° ²``");       // rTrim
   _dtv.Set(" |.| "  , "TM``¹``>(´¹ ²)´`` ° ²``");        // trim
   
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
   _dtv.Set(" <|> "  , "UNION``¹``>(´¹´²)´``) ° (²``");
   _dtv.Set(" <&> "  , "INTERSECT``¹``>(´¹)´ ° ²``(²)``");
   _dtv.Set(" <-> "  , "EXCEPT``¹``>(´¹)´ ° ²``(²)``");

   _dtv.Set(" sin "  , "SIN``¹``>(´¹ ²)´`` ° ²``");
   _dtv.Set(" cos "  , "COS``¹``>(´¹ ²)´`` ° ²``");
   _dtv.Set(" tan "  , "TAN``¹``>(´¹ ²)´`` ° ²``");
  }

  public abstract string val(int inx, EvalExpert evx); //for debugging only (selfTest)        {return (this[inx]).ToString(); }

  public static bool dbg  = false;

  public string optr       = "";
  public string optrSymbol = "";
  public string dtv0       = "";
  public string dtv1lS     = ""; public string dtv1mS = ""; public string dtv1rS = "";
  public string dtv1lR     = ""; public string dtv1mR = ""; public string dtv1rR = "";
  public string dtv2       = "";
  public bool   revert     = false;
  public bool   left2right = true;

  private void splitDirective(string dtv)
  {
   dtv2 = ""; revert = false;
   optr = utl.cutl(ref dtv, "``");
   dtv0 = utl.cutl(ref dtv, "``");
   dtv1rR = utl.cutl(ref dtv, "``");
   left2right = dtv1rR.StartsWith(">");
   dtv1rR = dtv1rR.Substring(1);
   dtv1lR = utl.cutl(ref dtv1rR, "¹");
   dtv1mR = ""; if (dtv1rR.IndexOf("²") > -1) dtv1mR = utl.cutl(ref dtv1rR, "²"); else { revert = true; dtv1mR  = dtv1lR; dtv1lR = utl.cutl(ref dtv1mR, "²"); }
   dtv1lS = utl.cutl(ref dtv1lR, "´");
   dtv1mS = utl.cutl(ref dtv1mR, "´");
   dtv1rS = utl.cutl(ref dtv1rR, "´");
   dtv2 = utl.cutl(ref dtv, "``");
   optrSymbol = optr.Replace("(", "");
  }

  private string debug(string res) { return dbg? "[" + res + "]" : res; }

  public string val(EvalExpert evx) //directives dtv are explicitly given
  {
   string ret = "";
   if (Len == 0) throw new Exception("Err: Xpn: no operand given.");
   if (Name.Length == 0) { for (int i = 1; i <= Len; i++) ret += val(i, evx); return debug(ret); }
   try { splitDirective(evx.Dtv[Name]); } catch (Exception ex) { throw new Exception("Err: Xpn: undef operator " + Name); }
   if ((Len == 1) && (dtv0.Length > 0)) return debug(dtv0.Replace("¹", val(1, evx)).Replace("°", optrSymbol)); 
   if (dtv2.Length == 0)
   {
    if (left2right)
    {
     ret = val(1, evx) + dtv1mS;
     if (revert) for (int i = Len; i > 1; i--) ret = dtv1lR + val(i, evx) + dtv1mR + ret + dtv1rR; else for (int i = 2; i <= Len; i++) ret = dtv1lR + ret + dtv1mR + val(i, evx) + dtv1rR;
     return (revert) ? debug((dtv1lS + ret + dtv1rS).Replace("°", optrSymbol)) : debug((dtv1lS + ret + dtv1rS).Replace("°", optrSymbol));
    } else
    {
     ret = dtv1mS + val(Len, evx);
     if (revert) for (int i = 1; i < Len; i++) ret = dtv1lR + ret + dtv1mR + val(i, evx) + dtv1rR; else for (int i = Len - 1; i > 0; i--) ret = dtv1lR + val(i, evx) + dtv1mR + ret + dtv1rR;
     return (revert) ? debug((dtv1lS + ret + dtv1rS).Replace("°", optrSymbol)) : debug((dtv1lS + ret + dtv1rS).Replace("°", optrSymbol));
    }
   } else
   {
    if (revert) for (int i = 2; i <= Len; i++) ret = dtv2.Replace("²", val(i, evx)) + ret; else for (int i = Len; i > 1; i--) ret = dtv2.Replace("²", val(i, evx)) + ret;
    return (revert) ? debug((dtv1lS + dtv1lR + ret + dtv1mR + val(1, evx) + dtv1rR + dtv1rS).Replace("°", optrSymbol)) : debug((dtv1lS + dtv1lR + val(1, evx) + dtv1mR + ret + dtv1rR + dtv1rS).Replace("°", optrSymbol));
   }
  }

  //public static Xpn<object> xp(string optr, params object[] opnd) { return new Xpn<object>(optr, opnd); }
  //public string val() { return val(_dtv); }

  public Pile<Xpn<typ>> fromTxtDef(string def, bool isSmb)
  {
   //AttGeTr TBD TO BE DONE !!!
   return new Pile<Xpn<typ>>();
  }

  public Xpn(                                   ) : base()                            { init(); }
  public Xpn(int count                          ) : base(count)                       { init(); }
  public Xpn(params typ[] opnd                  ) : base(true, opnd)                  { init(); }
  //public Xpn(Pile<typ> opnd                     ) : base(true, opnd.array())        { init(); }
  public Xpn(string optor, params typ[] opnd    ) : base(optor, true, opnd)           { init(); }
  //public Xpn(string optor, Pile<typ> opnd       ) : base(optor, true, opnd.array()) { init(); }
    
 }
}











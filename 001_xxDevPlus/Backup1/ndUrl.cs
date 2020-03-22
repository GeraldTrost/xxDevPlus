

using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using ndBase;
using ndString;


// new Url("ftp://user:password@www.host.com:8080/test/test/test.php?test.html?a=2&b=3") //test


namespace ndData
{
 public class ndUrl
 {
  //Copyright© 1996-2012 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties, You may use, modify, distribute this code under GNU public licence for NON-Commercial use as long as this notice remains unchanged.
  private static bool selfTested  = false;  private static string ass(bool expr) { if (!expr) throw new Exception(ass(true) + " SelfTest Failure"); return "ndUrl"; } private static void selfTest() { selfTested = true; } private void init() { if (!selfTested) selfTest(); }

  private static string _allExtensions = " AF COM.AF AX AL COM.AL DZ COM.DZ AS AD AO CO.AO OG.AO AI COM.AI AQ AG COM.AG AE AR COM.AR AM AW COM.AW AC ASIA COM.AU AU AT CO.AT OR.AT AZ CO.AZ COM.AZ BS COM.BS BH COM.BH BD COM.BD BB COM.BB BY COM.BY MINSK.BY BE BZ COM.BZ BJ BM COM.BM BT COM.BT BO COM.BO BA CO.BA BW CO.BW BV BR COM.BR AM.BR FM.BR TV.BR NET.BR IO VG BN COM.BN BG BF BI CO.BI COM.BI OR.BI KH COM.KH CM COM.CM CO.CM NET.CM CA AB.CA BC.CA MB.CA NB.CA NL.CA NS.CA NT.CA NU.CA ON.CA PE.CA QC.CA SK.CA YK.CA NF.CA CV KY COM.KY CF TD CL CN COM.CN AH.CN BJ.CN CQ.CN FJ.CN GD.CN GS.CN GX.CN GZ.CN HA.CN HB.CN HE.CN HI.CN HK.CN HL.CN HN.CN JL.CN JS.CN JX.CN LN.CN MO.CN NM.CN NX.CN QH.CN SC.CN SD.CN SH.CN SN.CN SX.CN TJ.CN TW.CN XJ.CN CX CC CO COM.CO KM COM.KM NOTAIRES.KM PHARMACIENS.KM VETERINAIRE.KM CK CO.CK CR CO.CR CI CO.CI HR COM.HR CU COM.CU CY COM.CY CZ Congo CD DK DJ DM DO COM.DO TP EC COM.EC EG COM.EG SV COM.SV Guinea GQ ER EE CO.EE COM.EE ET COM.ET EU FK CO.FK FO FJ COM.FJ FI FR COM.FR GF PF TF GA GE COM.GE DE GH COM.GH GI COM.GI MOD.GI GB GR COM.GR GL CO.GL COM.GL GD GP COM.GP GU COM.GU GT COM.GT GG CO.GG GN COM.GN GW GY HT HN COM.HN HK COM.HK HU CO.HU IS IN CO.IN ID CO.ID IR CO.IR IQ IE IM CO.IM IL CO.IL IT JM COM.JM JP CO.JP NE.JP JE CO.JE JO COM.JO KZ COM.KZ KE CO.KE KI COM.KI KW COM.KW KG COM.KG LA LV COM.LV LB COM.LB LS CO.LS LR COM.LR LY COM.LY LI LT LU MO COM.MO MK COM.MK MG COM.MG MW CO.MW COM.MW MY COM.MY MV COM.MV ML MT COM.MT MP MH MQ MR MU COM.MU CO.MU OR.MU YT HM COM.MX MX FM MD MC MN MS MA CO.MA MZ CO.MZ MM COM.MM NA COM.NA CO.NA NR COM.NR NP COM.NP NL AN COM.AN NC COM.NC PG COM.PG NZ CO.NZ NI COM.NI NE NG COM.NG NU NF COM.NF KP NO OM CO.OM COM.OM PK COM.PK PW PS COM.PS PA COM.PA PY COM.PY PE COM.PE PH COM.PH PN CO.PN PL COM.PL PT COM.PT PR COM.PR ISLA.PR QA COM.QA Congo CG RE COM.RE RO CO.RO COM.RO RU COM.RU RW LC CO.LC COM.LC PM SM ST SA COM.SA SN CS CO.RS RS SC COM.SC SL SG COM.SG SK SI SB COM.SB SO ZA CO.ZA GS KR CO.KR NE.KR OR.KR SU ES COM.ES LK COM.LK Helena SH Vincent and The Grenadines VC Vincent and The Grenadines COM.VC Kitts and Nevis KN SD COM.SD TV.SD SR SJ SZ SE CH SY COM.SY TW COM.TW TJ COM.TJ CO.TJ TZ CO.TZ TH CO.TH OR.TH GM TL COM.TL TG TK TO TT COM.TT CO.TT TN COM.TN TR COM.TR TM TC TV UG CO.UG OR.UG UA COM.UA UK CO.UK US UM UY COM.UY UZ CO.UZ COM.UZ VU VA VE COM.VE VN COM.VN VI CO.VI COM.VI WF EH WS YE COM.YE CO.YU ZM CO.ZM COM.ZM ZW CO.ZW NET ORG COM BIZ INFO AERO CAT NAME COOP JOBS MOBI MUSEUM PRO LAW.PRO MED.PRO CPA.PRO AVOCAT.PRO BAR.PRO RECHT.PRO JUR.PRO AAA.PRO ACA.PRO ACCT.PRO ARC.PRO BUS.PRO CHI.PRO CHIRO.PRO DEN.PRO DENT.PRO PROF.PRO TEACH.PRO NUR.PRO NURSE.PRO PRX.PRO PHARMA.PRO REL.PRO MIN.PRO VET.PRO CFP.PRO DDS.PRO ED.PRO NTR.PRO OPT.PRO PA.PRO PHA.PRO POD.PRO PR.PRO PSY.PRO PT.PRO TRAVEL AU.COM BR.COM CN.COM EU.COM DE.COM GB.COM GB.NET HU.COM JPN.COM NV.COM NO.COM QC.COM RU.COM SA.COM ZA.COM KR.COM SE.COM SE.NET UK.COM UK.NET US.COM UY.COM ";

  private Reach _url;

  public Reach Protocol;
  public Reach Domain;
  public Reach User;
  public Reach Password;
  public Reach Server;
  public Reach Extension;
  public Reach Port;
  public Reach Path;
  public Reach File;
  public Assoc Query;

  public string text
  {
   get
   {
    string ret = Protocol.text + "://";
    if (User.len + Password.len > 0) ret += User.text + ":" + Password.text + "@";
    if (Port.len > 0) ret += Server.text + "." + Domain.text + "." + Extension.text + ":" + Port.text + "/" + Path.text + "/" + File.text; else ret+=Server.text + "." + Domain.text + "." + Extension.text + "/" + Path.text + "/" + File.text;
    if (Query.Len > 0) ret += "?" + Query.text;
    return ret;
   }
  }


  public ndUrl(string urlString)
  {
   urlString = urlString.Trim();
   if (urlString.IndexOf("://") < 0) urlString = "http://" + urlString;
   _url = new Reach(urlString);
   Protocol = _url.before(1, "://");
   _url = _url.after(Protocol).from(4);
   Domain = _url.before(1, "/");
   _url = _url.after(Domain).from(2);
   Password = Domain.before(-1, "@");
   if (Password.len > 0)
   {
    Domain = Domain.after(Password).from(2);
    User = Password.before(1, ":");
    if (User.len > 0) Password = Password.after(User).from(2);
   } else
   {
    User = Password.upto(0);
   }
   Port = Domain.after(1, ":");
   if (Port.len > 0) Domain = Domain.before(Port).upto(-2);
   Extension = Domain.after(-2, ".");
   if (_allExtensions.IndexOf(" " + Extension.uText + " ") < 0) Extension = Domain.after(-1, ".");
   if (_allExtensions.IndexOf(" " + Extension.uText + " ") < 0) Extension = Domain.from(Domain.len + 1);
   if (Extension.len > 0) Domain = Domain.before(Extension).upto(-2);
   Server = Domain.before(-1, ".");
   if (Server.len > 0) Domain = Domain.after(Server).from(2);
   Path = _url.before(1, "?");
   if (Path.len > 0) _url = _url.after(Path).from(2);
   Query = new Assoc(_url, "=", "&", "");
   File = Path.after(-1, "/");
   if (File.len > 0) Path = Path.before(File).upto(-2);
  }


 }
}

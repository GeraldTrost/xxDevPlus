
//** @author  GeTr Copyright© 1996-2020 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties
//** @license You may use, modify, distribute this code under GNU public dual licence free for NON-Commercial use as long as this notice remains unchanged.
//** @comment Application Context


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

 public interface EvalExpert
 {
  KeyPile<string, string> Dtv { get; set; }   // Directives
  string val (object obj);
  
 }
}

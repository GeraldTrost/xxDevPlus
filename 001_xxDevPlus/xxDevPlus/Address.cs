
//** @author  GeTr Copyright© 1996-2020 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties
//** @license You may use, modify, distribute this code under GNU public dual licence free for NON-Commercial use as long as this notice remains unchanged.
//** @comment this class is currently only used as sample class for selftest in other classes 


namespace org_xxdevplus_struct
{
 public class Address
 {

  private string                   type            = "";
  private string                   street          = "";
  private string                   streetinfo      = "";
  private string                   number          = "";
  private string                   city            = "";
  private string                   state           = "";
  private string                   zip_code        = "";

  public Address(string type, string street, string streetinfo, string number, string city, string state, string zip_code)
  {
   this.type            = type;
   this.street          = street;
   this.streetinfo      = streetinfo;
   this.number          = number;
   this.city            = city;
   this.state           = state;
   this.zip_code        = zip_code;
  }

  public int CompareTo(Address theOther) { return ((street + number).CompareTo(theOther.street + theOther.number)); }

 }
}

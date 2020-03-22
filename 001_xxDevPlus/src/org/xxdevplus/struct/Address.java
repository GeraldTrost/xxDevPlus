

//** @author  GeTr Copyright© 1996-2020 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties
//** @license You may use, modify, distribute this code under GNU public dual licence free for NON-Commercial use as long as this notice remains unchanged.
//** @comment this class is currently only used as sample class for selftest in other classes 

package org.xxdevplus.struct;

public class Address
{

 private String                   type            = "";
 private String                   street          = "";
 private String                   streetinfo      = "";
 private String                   number          = "";
 private String                   city            = "";
 private String                   state           = "";
 private String                   zip_code        = "";

 public Address (String type, String street, String streetinfo, String number, String city, String state, String zip_code)
 {
  this.type            = type            ;
  this.street          = street          ;
  this.streetinfo      = streetinfo      ;
  this.number          = number          ;
  this.city            = city            ;
  this.state           = state           ;
  this.zip_code        = zip_code        ;
 }

 public int compareTo(Address theOther) { return ((street + number).compareTo(theOther.street + theOther.number)); }

}

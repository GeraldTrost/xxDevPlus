/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.xxdevplus.data;

import org.xxdevplus.struct.KeyPile;
import org.xxdevplus.utl.utl;

/**
 *
 * @author root
 */

public class DbUrl
{
 //Copyright© 1996-2020 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties, You may use, modify, distribute this code under GNU public licence for NON-Commercial use as long as this notice remains unchanged.
 private static boolean selfTested  = false; private static String ass(boolean expr) throws Exception { if (!expr) throw new Exception(ass(true) + " SelfTest Failure"); return "DbUrl"; } private static void selfTest() { selfTested = true; }

 private KeyPile<String, String[]> drivers = new KeyPile<String, String[]>();
 public KeyPile<String, String[]> Drivers() { return drivers; } public void Drivers(KeyPile<String, String[]> value) { drivers = value; }

 public  String                Driver     () throws Exception 
 { 
  if (dbtec.equals("odbc")) 
   return drivers.g(dbms)[0]; 
  if (dbtec.equals("jdbc")) 
   return drivers.g(dbms)[1]; 
  return drivers.g(dbms)[2]; 
 }

 public  String                dbtec      = "odbc";
 public  String                dbms       = "";     //must be empty because an uninited Db must not be fooled!
 public  String                host       = "/";
 public  long                  port       =  0;
 public  String                instance   = "";
 public  String                database   = "";
 public  String                schema     = "";
 public  String                user       = "root";
 public  String                password   = "root";
 public  String                tfilter    = "*";
 public  String                vfilter    = "*";

 public DbUrl (KeyPile<String, String[]> drivers)
 {
  this.drivers = drivers;
 }

 public DbUrl(DbUrl cloneFrom)
 {
  this.drivers = cloneFrom.drivers;
  dbtec        = cloneFrom.dbtec;
  dbms         = cloneFrom.dbms;
  host         = cloneFrom.host;
  port         = cloneFrom.port;
  instance     = cloneFrom.instance;
  database     = cloneFrom.database;
  schema       = cloneFrom.schema;
  user         = cloneFrom.user;
  password     = cloneFrom.password;
  tfilter      = cloneFrom.tfilter;
  vfilter      = cloneFrom.vfilter;
 }


 public DbUrl (String connectString, KeyPile<String, String[]> drivers)
 {
  this.drivers = drivers;
  String[] connectionString = new String[] {connectString};
  String[] tfilter          = new String[] {""};
  String[] vfilter          = new String[] {""};
  if (connectString.indexOf("<<") > -1)
  {
   vfilter[0] = connectString;
   connectionString[0] = utl.cutl(vfilter, "<<");
   tfilter[0]          = utl.cutl(vfilter, "<<");
  }
  String[] dbms       = new String[] {utl.cutl(connectionString , "::")};
  String[] dbtec      = new String[] {utl.cutl(dbms             , ":"    )};
  String[] instance   = new String[] {utl.cutl(connectionString , ",").replace("\\", "/")};
  String[] port       = new String[] {utl.cutl(instance         , "/"    )};
  String[] host       = new String[] {utl.cutl(port             , ":"    )};
  String[] schema     = new String[] {utl.cutl(connectionString , ",")};
  String[] database   = new String[] {utl.cutl(schema           , "["    )};
  String[] user       = new String[] {utl.cutl(connectionString , ",")};
  String[] password   = new String[] {utl.cutl(connectionString , ",")};
  this.dbtec          = dbtec[0].trim();
  this.dbms           = dbms[0].trim();
  this.host           = host[0].trim();
  this.port           = Long.parseLong("0" + port[0]);
  this.instance       = instance[0].trim();
  this.database       = database[0].trim();
  this.schema         = utl.cutl(schema[0], "]").trim();
  this.user           = user[0].trim();
  this.password       = password[0].trim();
  this.tfilter        = tfilter[0].trim();
  this.vfilter        = vfilter[0].trim();
  if (this.tfilter.length() == 0) this.tfilter = "*";
  if (this.vfilter.length() == 0) this.vfilter = "*";
 }

 public String ConnectString(boolean hidePassword)
 {
  String pwd = hidePassword ? "" : password;
  return
   dbtec.trim() + ":"
   + dbms.trim() + "::"
   + host.trim() + utl.pV(":", port) + utl.pV("/", instance.trim()) + ","
   + database.trim() + utl.ppV("[", "]", schema.trim()) + ","
   + user.trim() + ","
   + pwd.trim() + "<<"
   + tfilter.trim() + "<<"
   + vfilter.trim();
 }

 public String Name()
 {
  return  dbms.trim() + ":" + database.trim() + utl.ppV("[", "]", schema.trim()) + " on " + host.trim() + utl.pV(":", port) + utl.pV("/", instance.trim());
 }

}

using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using ndBase;
using ndString;

namespace ndData
{
 
 public class DbUrl
 {
  //Copyright© 1996-2012 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties, You may use, modify, distribute this code under GNU public licence for NON-Commercial use as long as this notice remains unchanged.
  private static bool selfTested  = false; private static string ass(bool expr) { if (!expr) throw new Exception(ass(true) + " SelfTest Failure"); return "DbUrl"; } private static void selfTest() { selfTested = true; }

  private KeyPile<string, string[]> drivers = new KeyPile<string, string[]>();
  public KeyPile<string, string[]> Drivers { get { return drivers; } set { drivers = value; } }

  public string Driver { get { if (dbtec.Equals("odbc")) return drivers[dbms][0]; if (dbtec.Equals("jdbc")) return drivers[dbms][1]; return drivers[dbms][2]; } }

  public  string                dbtec      = "odbc";
  public  string                dbms       = "";     //must be empty because an uninited Db must not be fooled!
  public  string                host       = "/";
  public  long                  port       = 0;
  public  string                instance   = "";
  public  string                database   = "";
  public  string                schema     = "";
  public  string                user       = "root";
  public  string                password   = "root";
  public  string                tfilter    = "*";
  public  string                vfilter    = "*";


  public DbUrl(KeyPile<string, string[]> drivers) { this.drivers = drivers; }

  public DbUrl(DbUrl cloneFrom)
  {
   this.drivers = cloneFrom.drivers;
   dbtec      = cloneFrom.dbtec;
   dbms       = cloneFrom.dbms;
   host       = cloneFrom.host;
   port       = cloneFrom.port;
   instance   = cloneFrom.instance;
   database   = cloneFrom.database;
   schema     = cloneFrom.schema;
   user       = cloneFrom.user;
   password   = cloneFrom.password;
   tfilter    = cloneFrom.tfilter;
   vfilter    = cloneFrom.vfilter;
  }

  public DbUrl(string connectionString, KeyPile<string, string[]> drivers)
  {
   this.drivers = drivers;
   string tfilter      = "";
   string vfilter      = "";
   if (connectionString.IndexOf("<<") > -1)
   {
    vfilter = connectionString;
    connectionString = utl.cutl(ref vfilter, "<<");
    tfilter          = utl.cutl(ref vfilter, "<<");
   }
   string   dbms       = utl.cutl(ref connectionString , "::");
   string   dbtec      = utl.cutl(ref dbms             , ":"    );
   string   instance   = utl.cutl(ref connectionString , ",").Replace("\\", "/");
   string   port       = utl.cutl(ref instance         , "/"    );
   string   host       = utl.cutl(ref port             , ":"    );
   string   schema     = utl.cutl(ref connectionString , ",");
   string   database   = utl.cutl(ref schema           , "["    );
   string   user       = utl.cutl(ref connectionString , ",");
   string   password   = utl.cutl(ref connectionString , ",");
   this.dbtec          = dbtec.Trim();
   this.dbms           = dbms.Trim();
   this.host           = host.Trim();
   this.port           = long.Parse("0" + port);
   this.instance       = instance.Trim();
   this.database       = database.Trim();
   this.schema         = utl.cutl(ref schema, "]").Trim();
   this.user           = user.Trim();
   this.password       = password.Trim();
   this.tfilter        = tfilter.Trim();
   this.vfilter        = vfilter.Trim();
   if (this.tfilter.Length == 0) this.tfilter = "*";
   if (this.vfilter.Length == 0) this.vfilter = "*";
  }

  public string ConnectString(bool hidePassword)
  {
   string pwd = hidePassword ? "" : password;
   return
    dbtec.Trim() + ":"
    + dbms.Trim() + "::"
    + host.Trim() + utl.pV(":", port) + utl.pV("/", instance.Trim()) + ","
    + database.Trim() + utl.ppV("[", "]", schema.Trim()) + ","
    + user.Trim() + ","
    + pwd.Trim() + "<<"
    + tfilter.Trim() + "<<"
    + vfilter.Trim();
  }
 
  public string Name()
  {
   return dbms.Trim() + ":" + database.Trim() + utl.ppV("[", "]", schema.Trim()) + " on " + host.Trim() + utl.pV(":", port) + utl.pV("/", instance.Trim());
  }

 }


}

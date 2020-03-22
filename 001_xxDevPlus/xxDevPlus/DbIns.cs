


//** @author  GeTr Copyright© 1996-2020 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties
//** @license You may use, modify, distribute this code under GNU public dual licence free for NON-Commercial use as long as this notice remains unchanged.
//** @comment Database Insert Command




using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;


namespace org_xxdevplus_data
{
 public class DbIns
 {
  //Copyright© 1996-2012 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties, You may use, modify, distribute this code under GNU public licence for NON-Commercial use as long as this notice remains unchanged.
  private static bool selfTested  = false;  private static string ass(bool expr) { if (!expr) throw new Exception(ass(true) + " SelfTest Failure"); return "DbIns"; } private void init() { if (!selfTested) selfTest(); }

  internal string                      into         = "";
  private  Pile<DbField>               fields       = new Pile<DbField>();
  public   Pile<DbField>               Fields       { get { return fields; } }
  internal Pile<string>                values       = new Pile<string>();
  internal DbSlc                       slc          = null;

  public   DbGrid                      grid         = null;
  public   DatSet                      datSet       = null;

  private static void selfTest()
  {
   selfTested = true;
   ctx cx = new ctx();
   Db db = new Db(cx.DbDrivers);
   DatSet ds = new DatSet();
   ObjPile v = new ObjPile("1", 2);
   ds.Raws.Add(v); ds.Raws.Add(v);
   ass(db.Grid("tbl").sC(db.dF("a"), db.dsF("isa")).INS(db.ds("1"), 2).sql().Equals("INSERT INTO tbl (a, 'isa' ) \r\nVALUES ('1', 2 )"));
   ass(db.Grid("tbl").sC("id, nm").INS(db.ds("1"), 2).sql().Equals("INSERT INTO tbl (id, nm ) \r\nVALUES ('1', 2 )"));
   ass(db.Grid("tbl").sC("id, nm").INS(ds).sql().Equals("INSERT INTO tbl (id, nm ) \r\n(\r\n SELECT '1', 2)\r\nUNION\r\n( SELECT '1', 2\r\n)"));
   ass(db.Grid("tbl").sC("id, nm").INS(db.Grid("tbl").sC("id, nm").sR(db.cd("id").GT(0)).SLC).sql().Equals("INSERT INTO tbl (id, nm ) \r\n SELECT id, nm \r\n FROM tbl tbl \r\n WHERE ( (id > 0)  ) "));
  }

  public string sql() { return sql(new Db(new ctx().DbDrivers)); }

  public string sql(Db db)
  {
   if (grid == null)
   {
    if (into.Trim().Length == 0) return "";
    string ret = "INSERT INTO " + db.dbTable(into) + " (";
    foreach (DbField f in fields) ret += f.sql(db) + ", ";
    if (!ret.EndsWith(" (")) ret = ret.Substring(0, ret.Length - 2);
    if (slc == null) { ret += " ) \r\nVALUES ("; foreach (string v in values) ret += v + ", "; ret = ret.Substring(0, ret.Length - 2) + " )"; } else { if (!ret.EndsWith(" (")) ret += " ) \r\n"; ret += slc.sql(db); }
    return ret;
   } else
   {
    DbSlcBlock slc = new DbSlcBlock(" <|> ");
    foreach (ObjPile raw in datSet.Raws)
    {
     string fld = "";
     foreach (object obj in raw)
     {
      Type t = obj.GetType();
      if (t == typeof(Reach))                              fld += (Reach)obj + ", ";
      if (t == typeof(string))                             fld += db.ds((string)obj) + ", ";
      if ((t == typeof(double)) || (t == typeof(float)))   fld += ("" + obj).Replace(",", ".") + ", ";
      if ((t == typeof(long))   || (t == typeof(int)))     fld += "" + obj + ", ";
     }
     fld = fld.Substring(0, fld.Length - 2);
     if (db.avoidEmptyStrings) slc.Push(new DbGrid(".all_tables").sR(db.cd("table_name").EQ(new DbGrid(".all_tables").sC("Min(table_name)").SLC)).sC(fld).SLC); else slc.Push(new DbGrid("").sC(fld).SLC);
    }
    return grid.INS(db.SlcOR(slc.array())).sql(db);
    //return sql(i.grid.INS(slc.val(this)));  //slc.val(this) is a string - so the INS does not create "insert into .... select ..." but it creates insert into .... values .....
   }
  }

  /*
  public string _sql()
  {
   if (grid == null)
   {
    string ret = "INSERT INTO " + into + " (";
    foreach (string f in fieldNames) ret += f + ", ";
    ret = ret.Substring(0, ret.Length - 2);
    if (slc == null)
    { ret += " ) \r\nVALUES ("; foreach (string v in values) ret += v + ", "; ret = ret.Substring(0, ret.Length - 2) + " )"; } else ret += " ) \r\n" + slc.sql();
    return ret;
   } else
   {
    DbSlcBlock slc = new DbSlcBlock();
    foreach (ObjPile raw in datSet.Raws)
    {
     string fld = "";
     foreach (object obj in raw)
     {
      Type t = obj.GetType();
      if (t == typeof(Reach)) fld += (Reach)obj + ", ";
      if (t == typeof(string)) fld += Db._ds((string)obj) + ", ";
      if (t == typeof(double)) fld += ("" + (double)obj).Replace(",", ".") + ", ";
      if (t == typeof(long)) fld += "" + (long)obj + ", ";
     }
     fld = fld.Substring(0, fld.Length - 2);
     slc.Push(new DbGrid("").sC(fld).SLC);                                                                                            //for MsS and PgS
     //slc.Push(new DbGrid(".all_tables").sR(Db.cd("table_name").EQ(new DbGrid(".all_tables").sC("Min(table_name)").SLC)).sC(fld).SLC);     //for Ora
    }
    return grid.INS(Db.SlcOR(slc.array()))._sql();
   }
  }
  */
  
  public string smb()
  {
   string ret = "Grid(\"" + into + "\").sC(\"";
   foreach (DbField f in fields) ret += f.smb() + ", ";
   ret = ret.Substring(0, ret.Length - 2) + "\").INS(";
   if (slc == null) { foreach (string v in values) if (v.StartsWith("'"))  ret += "ds(\"" + v.Substring(1, v.Length - 2) + "\"), "; else ret += v + ", "; ret = ret.Substring(0, ret.Length - 2) + " )"; } else ret +=  slc.smb() + ")";
   return ret;
  }

  internal DbIns(string into, Pile<DbField> fields, DbSlc select)
  {
   init();
   this.slc = select;
   this.fields.Add(fields); //fields = fields.Trim(); while (fields.Length > 0) { this.fields.Add(utl.cutl(ref fields, ",").Trim()); fields = fields.Trim(); }
   this.into   = into.Trim();
   string tShort = utl.cutl(ref into, ",").Trim().ToLower();
   string tName  = utl.cutl(ref tShort, " ").Trim();
   tShort = tShort.Trim(); if (tShort.Length == 0) tShort = tName;
   into = tName.Trim() + " " + tShort.Trim();
  }

  internal DbIns(DbGrid into, DatSet values)
  {
   init();
   grid = into;
   datSet = values;
  }

  internal DbIns(string into, Pile<DbField> fields, object[] values)
  {
   init();
   //this.fieldNames.Add(fieldNames); //fields = fields.Trim(); while (fields.Length > 0) { this.fields.Add(utl.cutl(ref fields, ",").Trim()); fields = fields.Trim(); }
   //this.fieldTypes.Add(fieldTypes); //fields = fields.Trim(); while (fields.Length > 0) { this.fields.Add(utl.cutl(ref fields, ",").Trim()); fields = fields.Trim(); }
   this.fields = fields.Clone();
   foreach (object o in values) if (o.GetType() == typeof(string)) this.values.Add((string)o); else if (o.GetType() == typeof(double)) this.values.Add(("" + o).Replace(",", ".")); else this.values.Add("" + o);
   this.into   = into.Trim();
   string tShort = utl.cutl(ref into, ",").Trim().ToLower();
   string tName  = utl.cutl(ref tShort, " ").Trim();
   tShort = tShort.Trim(); if (tShort.Length == 0) tShort = tName;
   into = tName.Trim() + " " + tShort.Trim();
  }

  internal DbIns(Reach smb)
  {
   init();
   //TBD
   Db db = new Db(new ctx().DbDrivers);
   DbIns ret = null;
   Zone   bktFilter   = new Zone(new Pile<string>(), new Pile<string>("", true, "(", ")", "||:0"));
   Zone   strFilter   = new Zone(new Pile<string>(), new Pile<string>("", true, "\"", "\"", "||:1"));

   /*
   string insstr = "Grid(\"prdrln\").sC(\"sbj, obj, vb, rul, steps, excerpt, trust\").INS(XXXX)";
   insstr = insstr.Replace("XXXX", "Grid(\"prdrln p1, prdvrb pv1, prdrln p2, prdvrb pv2, kndrln sc, itmrln si, prdvrb pv, prdrul pr, itmrln oi, kndrln oc\", cd(\"p1.obj\").EQ(\"p2.sbj\")).sC(\"si.id, oi.id, pv.id, pr.id, 0, '', 0.5\").sR(XXXX).SLD");
   insstr = insstr.Replace("XXXX", "cd(\"0\").EQ(Grid(\"prdrln prdrln\").sC(\"count(*)\").sR(cd(\"sbj\").EQ(\"si.id\"), cd(\"obj\").EQ(\"oi.id\"), cd(\"vb\").EQ(\"pr.vb\")).SLC ), XXXX");
   insstr = bktFilter.on(insstr).text;
   insstr = insstr.Replace("XXXX", "cd(\"0\").EQ(Grid(\"prdrln prdrln\").sC(\"count(*)\").sR(cd(\"sbj\").EQ(\"si.id\"), cd(\"obj\").EQ(\"oi.id\"), cd(\"vb\").EQ(\"pr.vb\")).SLC ), XXXX");
   */

   Reach gridDef = smb - bktFilter.on(smb);
   Reach ins = gridDef.at(".INS(");
   if (ins.len == 0) throw new Exception("DbIns Constructor: invalid symbolic Definition");
   DbGrid res = db.Grid(smb.before(ins));
   smb = smb.after(ins).before(-1, ")");
   if (smb.startsWith("Grid(")) ret = res.INS(new DbSlc(smb));
   else
   {
    Pile<string> val = new Pile<string>();
    while (smb.len > 0)
    {
     if (smb.startsWith("ds(")) { val.Push(db.ds(smb.after(1, "\"").before(1, "\""))); smb = smb.after(1, "),").Trim(); } else { val.Push(smb.before(1, ",").Trim()); smb = smb.after(1, ",").Trim(); }
    }
    ret = res.INS(val.array());
   }
   into         = ret.into;
   fields       = ret.fields.Clone();   //new Pile<string>(ret.fieldNames);
   values       = ret.values;
   slc          = ret.slc;

  }
  
 }
}



//** @author  GeTr Copyright© 1996-2020 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties
//** @license You may use, modify, distribute this code under GNU public dual licence free for NON-Commercial use as long as this notice remains unchanged.
//** @comment Database Insert Command


package org.xxdevplus.data;

import org.xxdevplus.struct.ObjPile;
import org.xxdevplus.struct.Pile;
import org.xxdevplus.utl.ctx;
import org.xxdevplus.utl.utl;
import org.xxdevplus.chain.Chain;
import org.xxdevplus.frmlng.Zone;

/**
 *
 * @author GeTr
 */
 public class DbIns
 {
  //Copyright© 1996-2020 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties, You may use, modify, distribute this code under GNU public licence for NON-Commercial use as long as this notice remains unchanged.
  private static boolean selfTested  = false;  private static String ass(boolean expr) throws Exception { if (!expr) throw new Exception(ass(true) + " SelfTest Failure"); return "DbIns"; } private void init() throws Exception { if (!selfTested) selfTest(); }
  
  private static void selfTest() throws Exception
  {
   selfTested = true;
   Db db = new Db(new ctx().DbDrivers());
   DatSet ds = new DatSet();
   ObjPile v = new ObjPile("1", 2);
   ds.Raws.Add(v); ds.Raws.Add(v);
   ass(db.Join("tbl").sC(db.dF("a"), db.dsF("isa")).INS(db.ds("1"), new Integer(2)).sql().equals("INSERT INTO tbl (a, 'isa' ) \r\nVALUES ('1', 2 )"));
   ass(db.Join("tbl").sC("id, nm").INS(db.ds("1"), 2).sql().equals("INSERT INTO tbl (id, nm ) \r\nVALUES ('1', 2 )"));
   ass(db.Join("tbl").sC("id, nm").INS(ds).sql().equals("INSERT INTO tbl (id, nm ) \r\n(\r\n SELECT '1', 2)\r\nUNION\r\n( SELECT '1', 2\r\n)"));
   ass(db.Join("tbl").sC("id, nm").INS(db.Join("tbl").sC("id, nm").sR(db.cd("id").GT(0)).SLC()).sql().equals("INSERT INTO tbl (id, nm ) \r\n SELECT id, nm \r\n FROM tbl tbl \r\n WHERE ( (id > 0)  ) "));
  } 
  
           String                      into         = "";
  private  Pile<DbField>               fields       = new Pile<DbField>();
  public   Pile<DbField>               Fields (   ) { return fields; }
           Pile<String>                values       = new Pile<String>();
           DbSlc                       slc          = null;

  public   DbJoin                      grid         = null;
  public   DatSet                      datSet       = null;



  public String sql() throws Exception { return sql(new Db(new ctx().DbDrivers())); }

  public String sql(Db db) throws Exception
  {
   if (grid == null)
   {
    if (into.trim().length() == 0) return "";
    String ret = "INSERT INTO " + db.dbTable(into) + " (";
    for (DbField f : fields) ret += f.sql(db).text() + ", ";
    if (!ret.endsWith(" (")) ret = ret.substring(0, ret.length() - 2);
    if (slc == null) { ret += " ) \r\nVALUES ("; for (String v : values) ret += v + ", "; ret = ret.substring(0, ret.length() - 2) + " )"; } else { if (!ret.endsWith(" (")) ret += " ) \r\n"; ret += slc.sql(db); }
    return ret;
   } else
   {
    DbSlcBlock slc = new DbSlcBlock(" <|> ");
    for (ObjPile raw : datSet.Raws)
    {
     String fld = "";
     for (Object obj : raw)
     {
      if (obj instanceof Chain)  fld += (Chain)obj + ", ";
      if (obj instanceof String) fld += db.ds((String)obj) + ", ";
      if ((obj instanceof Double) || (obj instanceof Float))     fld += ("" + obj).replace(",", ".") + ", ";
      if ((obj instanceof Long)   || (obj instanceof Integer))   fld += "" + obj + ", ";
     }
     fld = fld.substring(0, fld.length() - 2);
     if (db.avoidEmptyStrings) slc.Push(new DbJoin(".all_tables").sR(db.cd("table_name").EQ(new DbJoin(".all_tables").sC("Min(table_name)").SLC())).sC(fld).SLC()); else slc.Push(new DbJoin("").sC(fld).SLC());
    }
    return grid.INS(db.SlcOR(Db.cloneSlcArray(slc))).sql(db);
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
    foreach (ObjPack raw in datSet.Raws)
    {
     string fld = "";
     foreach (object obj in raw)
     {
      Type t = obj.GetType();
      if (t == typeof(Chain)) fld += (Chain)obj + ", ";
      if (t == typeof(string)) fld += Db._ds((string)obj) + ", ";
      if (t == typeof(double)) fld += ("" + (double)obj).Replace(",", ".") + ", ";
      if (t == typeof(long)) fld += "" + (long)obj + ", ";
     }
     fld = fld.Substring(0, fld.Length - 2);
     slc.Push(new DbJoin("").sC(fld).SLC);                                                                                            //for MsS and PgS
     //slc.Push(new DbJoin(".all_tables").sR(Db.cd("table_name").EQ(new DbJoin(".all_tables").sC("Min(table_name)").SLC)).sC(fld).SLC);     //for Ora
    }
    return grid.INS(Db.SlcOR(slc.array()))._sql();
   }
  }
  */
  
  
  public String smb() throws Exception
  {
   String ret = "Grid(\"" + into + "\").sC(\"";
   for (DbField f : fields) ret += f.smb() + ", ";
   ret = ret.substring(0, ret.length() - 2) + "\").INS(";
   if (slc == null) { for (String v : values) if (v.startsWith("'"))  ret += "ds(\"" + v.substring(1, v.length() - 2) + "\"), "; else ret += v + ", "; ret = ret.substring(0, ret.length() - 2) + " )"; } else ret +=  slc.smb() + ")";
   return ret;
  }

  DbIns(String into, Pile<DbField> fields, DbSlc select) throws Exception
  {
   init();
   this.slc = select;
   this.fields.Add(fields); //fields = fields.Trim(); while (fields.Length > 0) { this.fields.Add(utl.cutl(ref fields, ",").Trim()); fields = fields.Trim(); }
   this.into   = into.trim();
   String[] nto     = new String[] {into};
   String[] tShort  = new String[] {utl.cutl(nto, ",").trim().toLowerCase()};
   String tName     = utl.cutl(tShort, " ").trim();
   tShort[0] = tShort[0].trim(); if (tShort[0].length() == 0) tShort[0] = tName;
   into = tName.trim() + " " + tShort[0].trim();
  }

   DbIns(DbJoin into, DatSet values) throws Exception
  {
   init();
   grid = into;
   datSet = values;
  }

  DbIns(String into, Pile<DbField> fields, Object[] values) throws Exception
  {
   init();
   //this.fieldNames.Add(fieldNames); //fields = fields.Trim(); while (fields.Length > 0) { this.fields.Add(utl.cutl(ref fields, ",").Trim()); fields = fields.Trim(); }
   //this.fieldTypes.Add(fieldTypes); //fields = fields.Trim(); while (fields.Length > 0) { this.fields.Add(utl.cutl(ref fields, ",").Trim()); fields = fields.Trim(); }
   for (DbField field : fields) this.fields.Add(field);
   for (Object o : values) if (o instanceof String) this.values.Add((String)o); else if (o instanceof Double) this.values.Add(("" + o).replace(",", ".")); else this.values.Add("" + o);
   this.into   = into.trim();
   String[] nto     = new String[] {into};
   String[] tShort  = new String[] {utl.cutl(nto, ",").trim().toLowerCase()};
   String tName  = utl.cutl(tShort, " ").trim();
   tShort[0] = tShort[0].trim(); if (tShort[0].length() == 0) tShort[0] = tName;
   into = tName.trim() + " " + tShort[0].trim();
  }

  DbIns(Chain smb) throws Exception 
  {
   //TBD
   init();
   Db db = new Db(new ctx().DbDrivers());
   DbIns ret = null;
   Zone   bktFilter   = new Zone(new Pile<String>(), new Pile<String>("", 0, "(", ")", "||:0"));
   Zone   strFilter   = new Zone(new Pile<String>(), new Pile<String>("", 0, "\"", "\"", "||:1"));

   /*
   string insstr = "Join(\"prdrln\").sC(\"sbj, obj, vb, rul, steps, excerpt, trust\").INS(XXXX)";
   insstr = insstr.Replace("XXXX", "Join(\"prdrln p1, prdvrb pv1, prdrln p2, prdvrb pv2, kndrln sc, itmrln si, prdvrb pv, prdrul pr, itmrln oi, kndrln oc\", cd(\"p1.obj\").EQ(\"p2.sbj\")).sC(\"si.id, oi.id, pv.id, pr.id, 0, '', 0.5\").sR(XXXX).SLD");
   insstr = insstr.Replace("XXXX", "cd(\"0\").EQ(Join(\"prdrln prdrln\").sC(\"count(*)\").sR(cd(\"sbj\").EQ(\"si.id\"), cd(\"obj\").EQ(\"oi.id\"), cd(\"vb\").EQ(\"pr.vb\")).SLC ), XXXX");
   insstr = bktFilter.on(insstr).text;
   insstr = insstr.Replace("XXXX", "cd(\"0\").EQ(Join(\"prdrln prdrln\").sC(\"count(*)\").sR(cd(\"sbj\").EQ(\"si.id\"), cd(\"obj\").EQ(\"oi.id\"), cd(\"vb\").EQ(\"pr.vb\")).SLC ), XXXX");
   */

   Chain gridDef = smb.less(bktFilter.on(smb));
   Chain ins = gridDef.at(".INS(");
   if (ins.len() == 0) throw new Exception("DbIns Constructor: invalid symbolic Definition");
   DbJoin res = db.Join(smb.before(ins));
   smb = smb.after(ins).before(-1, ")");
   if (smb.startsWith("Grid(")) ret = res.INS(new DbSlc(smb));
   else 
   {
    Pile<String> val = new Pile<String>();
    while (smb.len() > 0)
    {
     if (smb.startsWith("ds(")) { val.Push(db.ds(smb.after(1, "\"").before(1, "\""))); smb = smb.after(1, "),").Trim(); } else { val.Push(smb.before(1, ",").text().trim()); smb = smb.after(1, ",").Trim(); }
    }
    ret = res.INS(val.array());
   }
   into         = ret.into;
   fields       = ret.fields.Clone();
   values       = ret.values;
   slc          = ret.slc;
    
  }

 }











package org.xxdevplus.data;


import java.lang.reflect.Array;
import org.xxdevplus.struct.KeyPile;
import org.xxdevplus.struct.Pile;
import org.xxdevplus.utl.utl;
import org.xxdevplus.chain.Chain;
import org.xxdevplus.frmlng.Zone;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author GeTr
 */

public class DbJoin
{

 //Copyright© 1996-2020 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties, You may use, modify, distribute this code under GNU public licence for NON-Commercial use as long as this notice remains unchanged.
 private static boolean selfTested  = false;  private static String ass(boolean expr) throws Exception { if (!expr) throw new Exception(ass(true) + " SelfTest Failure"); return "DbJoin"; } 
  
 private static Zone strZone = new Zone(new Pile<String>(), new Pile<String>("", 0, "\"", "\"", "||:0"));
 private static Zone chrZone = new Zone(new Pile<String>(), new Pile<String>("", 0, "'", "'", "||:0"));
 private static Zone bltZone = new Zone(new Pile<String>(), new Pile<String>("", 0, "(", ")", "||:0"));
 private static Zone bktZone = new Zone(new Pile<String>(), new Pile<String>("", 0, "[", "]", "||:0"));
 private static Zone cbtZone = new Zone(new Pile<String>(), new Pile<String>("", 0, "{", "}", "||:0"));
  
 private void init()
 { 
  sName  ("");
  sShort ("");
  if (!selfTested) selfTest(); 
 }
  
 //dbdb private Db                       db       = null;
 private KeyPile<String, DbJoin>  tables         = new KeyPile<String, DbJoin>(); // not used if the DbJoin represents just 1 table ...

 private  Pile<DbField>           fields          = new Pile<DbField>();
 public   Pile<DbField>           Fields       () { return fields; }

 private  Pile<DbField>           allFields       = null;
 public   Pile<DbField>           AllFields    () { return allFields; }

 private Pile<DbCnd>             joinCnd         = new Pile<DbCnd>();
 private Pile<DbCnd>             selCnd          = new Pile<DbCnd>();

 private String                  name = "";
 private String                  shrt = "";
         String                  Name()          {return name; }   void sName (String value) {name  = value;}
         String                  Short()         {return shrt; }   void sShort(String value) {shrt  = value;}
 
         String                  type            = "";
         Pile<DbJoin>            parts           = new Pile<DbJoin>();

         Db                      db              = null;
 public  Db                      Db()            { return db; } public void Db(Db value) { db = value; }



 private static void selfTest()
 {
  selfTested = true;
  //DbGrid g1 = db.Join("tlb1 a, tbl2 b", Db.cd("a.id").IN(db.Join("tbla a").sC("Max(id)").SLD), Db.cd("a.id").EQ(Db.ds("uuu"))).sC("a.nm, b.nm").sR(Db.cd("a.id").GT(2));
  //DbGrid g2 = new DbJoin(new Chain("Join(\"tlb1 a, tbl2 b\", cd(\"a.id\").IN(Join(\"tbla a\").sC(\"Max(id)\").SLD), cd(\"a.id\").EQ(ds(\"uuu\"))).sC(\"a.nm, b.nm\").sR(cd(\"a.id\").GT(2))"));
  //ass(Db._sql(g1.SLC).Equals(Db._sql(g2.SLC)));
 }


  
 private DbJoin(String type, DbJoin s1, DbJoin s2) throws Exception
 {
  init();
  this.type = type.toUpperCase().substring(0, 1);
  parts.Push(s1);
  parts.Push(s2);
  if ((parts.g(1).db != null) && (parts.g(2).db != null)) if (parts.g(1).db != parts.g(2).db) throw new Exception("Unable to merge grids from different data sources in INTERSECT or EXCEPT or UNION");
  if ((parts.g(1).db == null) || (parts.g(2).db == null)) { if (parts.g(2).db == null) this.db = parts.g(1).db; else this.db = parts.g(2).db; parts.g(1).Db(this.db); parts.g(2).Db(this.db); }
 }

 // Ideen:
 // Eine Projektion aus joined Tables könnte etwa so definiert werden ......
 // Join jtb = new Join("mitarbeiter m, ProcessInstance pi", "m.Id = pi.mitarbeiter");
 // Projection pjtb = jtb.Project("m.Id, m.nm, m.vorname, pi.nm, pi.Id, pi.StarDate");
 // pjtb.Select(true, "m.Id > 200", "m.nm LIKE '*Stefan*'");

 DbJoin(DbJoin cloneFrom) throws Exception
 {
  init();
  db = cloneFrom.db;
  fields     = cloneFrom.fields.Clone(); if (cloneFrom.allFields == cloneFrom.fields) allFields = fields; else if (cloneFrom.allFields != null) allFields = cloneFrom.allFields.Clone();
  tables   = new KeyPile<String, DbJoin>(cloneFrom.tables);
  joinCnd  = cloneFrom.joinCnd.Clone();   // new Pack<DbCnd>(cloneFrom.joinCnd);
  selCnd   = cloneFrom.selCnd.Clone();    // new Pack<DbCnd>(cloneFrom.selCnd);
  sName    (cloneFrom.Name());
  sShort   (cloneFrom.Short());
 }

 // 0: this is empty, 1: this represents a single table or view, 2: this represents a join (of tables, views, grids)
 private long currentState() throws Exception { return (this.tables.Len() == 0) && ((Name() + Short()).length() > 0) ? 1: this.tables.Len() == 0 ? 0: 2; }

 public void addTables(String tbles) throws Exception
 {
  tbles = tbles.trim();
  String[] tables = new String[] {tbles};
  while (tables[0].length() > 0)
  {
   String[] tableShort = new String[] {utl.cutl(tables, ",").trim().toLowerCase()};
   tables[0] = tables[0].trim();
   String tableName = utl.cutl(tableShort, " ").trim().toLowerCase();
   if (tableShort[0].length() == 0) tableShort[0] = tableName;
   //if ((this.tables.Len() == 0) && (tables.Length == 0)) {this.Name = tableName; this.Short = tableShort[0];  return; }
   if (currentState() == 0) // state is "empty", this DbJoin does not even contain a single table ...
   {
    sName(tableName); 
    sShort(tableShort[0]);
    //dbdb if (db.Tables.hasKey(Name)) fields = new KeyPack<string, string>(db.Tables[Name].fields);
    for (int i = 1; i <= fields.Len(); i++) fields.s(new DbField(Short() + "." + fields.g(i).sql()), i);
   }
   else
   {
    DbJoin g = new DbJoin(this);
    if (currentState() == 1) this.tables.Add(Name() + " " + Short(), new DbJoin(this)); 
    this.tables.Add(tableName + " " + tableShort[0], new DbJoin(tableName + " " + tableShort[0]));
    sName  (""); 
    sShort ("");
    fields.Clear();
    for (DbJoin tab : this.tables) for (DbField x : tab.Fields()) fields.Add(new DbField(tab.Short() + "." + x.sql()));
   }
  }
 }
    
 public void addTables(String tables, DbCnd... cnd) throws Exception
 {
  addTables(tables);
  joinCnd.Add(cnd);
 }

 DbJoin(String tables, DbCnd... joinConditions) throws Exception
 {
  init();
  allFields = fields;
  addTables(tables);
  joinCnd.Add(joinConditions);
 }

 DbJoin(String tables) throws Exception
 {
  init();
  allFields = fields;
  addTables(tables);
 }

 DbJoin(Chain smb) throws Exception
 {
  init();
  Zone   bktFilter   = new Zone(new Pile<String>(), new Pile<String>("", 0, "(", ")", "||:1"));
  Zone   strFilter   = new Zone(new Pile<String>(), new Pile<String>("", 0, "\"", "\"", "||:1"));
  if (smb.at(".OR").len() > 0) smb = smb;
  //def = new Chain("Join(\"cpt cpt\").sC(\"id\").sR(cd(\"idnm\").EQ(ds(\"TS\")).OR(cd(\"idnm\").EQ(ds(\"TO\"))).OR(cd(\"idnm\").EQ(ds(\"TO\")))).SLC");
  smb = new Chain(smb.text());
  Chain  gridDef     = bktFilter.on(smb);
  Chain  tblDef      = strFilter.on(gridDef);
  addTables(tblDef.text());
  Chain jCnd = smb.after(tblDef).after(1, ", ");
  joinCnd.Add(DbCnd.fromSymbolicDef(jCnd));
  String testsql = this.SLC().sql();
  smb = smb.after(gridDef).from(3);
  while (smb.len() > 0)
  {
   if (smb.startsWith("sR("))
   {
    Chain selCnds = bktFilter.on(smb);
    joinCnd = sR(DbCnd.fromSymbolicDef(selCnds)).joinCnd;
    selCnd  = sR(DbCnd.fromSymbolicDef(selCnds)).selCnd;
    smb = smb.after(selCnds).from(3);
   }
   else
   if (smb.startsWith("sC("))
   {
    Chain colDef = strFilter.on(bktFilter.on(smb));
    fields = sC(colDef.text()).fields;
    smb = smb.after(colDef).from(4);
   }
   else
   if (smb.startsWith("SL"))
   {
    smb = smb.from(4);
   }
  }
  allFields = fields;
  //addTables(tables);
 }

 DbJoin(Db db, String tables, DbCnd... joinConditions) throws Exception
 {
  init();
  this.db = db;
  allFields = fields;
  addTables(tables);
  joinCnd.Add(joinConditions);
 }

 DbJoin(Db db, String tables) throws Exception
 {
  init();
  this.db = db;
  allFields = fields;
  addTables(tables);
 }

 public Pile<DbCnd> Join()     { return joinCnd; }

 public Pile<DbCnd> Where()    { return selCnd;  }

 public String From() throws Exception
 {
  if (currentState() == 0) return "";
  if (currentState() == 1) return Name() + " " + Short();
  String ret = "";
  for (DbJoin g : tables) ret += ", " + g.From();
  return ret.substring(2);
 }

 private static long in_Assertion = 10000;

 /// <summary>  SLC ("Select") builds a distinct DbSelect </summary>
 public DbSlc SLC() throws Exception
 {
  if (in_Assertion == 0)
  {
   in_Assertion++;
   DbSlc copyOfRet  = new DbSlc(this);
   DbSlc recreated   = new DbSlc(new Chain((copyOfRet.smb())));
   recreated   = db.Join(new Chain((copyOfRet.smb()))).SLC();
   ass(copyOfRet.sql().equals(recreated.sql()));
   in_Assertion--;
  }
  return new DbSlc(this);
 }

 public DbSlc SLC0() throws Exception // work around a severe bug in Zone.on(). because of this endless loop here is a SLC Method WITHOUT extra debug checking
 {
  return new DbSlc(this);
 }
  
 /// <summary>  SLD ("Select Distinct") builds a distinct DbSelect </summary>
 //public DbSlc   SLD                                   { get { return new DbSlc(this).DIST;   } }

 public DbSlc SLD() throws Exception 
 { 
  if (in_Assertion == 0) 
  {
   in_Assertion++;
   DbSlc copyOfRet  = new DbSlc(this).DST();
   DbSlc recreated   = new DbSlc(new Chain((copyOfRet.smb()))).DST();
   //recreated   = db.Join((Chain)(Db.smb(copyOfRet))).SLD;
   ass(copyOfRet.sql().equals(recreated.sql())); 
   in_Assertion--; 
  } 
  return new DbSlc(this).DST(); 
 }

 public DbSlc SLD0() throws Exception // work around a severe bug in Zone.on(). because of this endless loop here is a SLC Method WITHOUT extra debug checking
 {
  return new DbSlc(this).DST();
 }

 //public DbIns INS (params string[] values)          { return new DbInsert(Name, fieldNames, fieldTypes, utl.stringArr2s(values, ", "))   ; }
 //public DbIns INS (string values)                   { return new DbIns(Name, fieldNames, fieldTypes, values)                            ; }

 public long copyTo(Db tgtDb) throws Exception
 {
  //if (this.Name.Equals("webpge")) this.Name = this.Name;
  tgtDb.exec(tgtDb.Tables().g(this.Name()).DEL());
  DatSet dat = db.exec(this.db.Tables().g(this.Name()).SLD()).all();
  if (dat.Raws.Len() == 0) return 0;
  return tgtDb.exec(tgtDb.Tables().g(this.Name()).INS(dat));
 }

 public DbIns   INS (Object... values) throws Exception          
 {
  if (in_Assertion == 0)
  {
   in_Assertion++;
   DbIns copyOfRet  = new DbIns(Name(), fields, values);
   DbIns recreated   = new DbIns(new Chain(copyOfRet.smb()));
   ass(copyOfRet.sql().equals(recreated.sql()));
   in_Assertion--;
  }
  return new DbIns(Name(), fields, values);
 }

 public DbIns   INS (DatSet values)                   throws Exception { return new DbIns(this, values); }
 public DbIns   INS (DbSlc sel)                       throws Exception { return new DbIns(Name(), fields, sel); }
 //public DbIns INS (DbJoin g)                        throws Exception { return new DbInsert(Name, fields, new DbSelect(g)); }

 public DbUpd   UPD (Object... values)                throws Exception { return new DbUpd(Name(), fields, values, Where())           ; }
 //public DbUpd UPD (params string[] values)          throws Exception { return new DbUpdate(Name, utl.stringArr2s(fieldNames.ToArray(), ", "), utl.stringArr2s(values, ", "), Where)     ; }
 //public DbUpd UPD (string values)                   throws Exception { return new DbUpd(Name, utl.stringArr2s(fieldNames.ToArray(), ", "), values, Where)                           ; }

 //public DbDel DEL (params DbCnd[] deleteConditions)   throws Exception { return new DbDelete(Name, deleteConditions); }
  
 private DbCnd[] _selCnd() throws Exception
 {
  DbCnd[] ret = Db.cloneCndArray(selCnd);
  return ret;
 }
  
 public DbDel   DEL()                                 throws Exception 
 { 
  //return new DbDel(Name(), selCnd()); 
  return new DbDel(Name(), Db.cloneCndArray(selCnd)); 
 }

  /// <summary> sC ("select Columns") is the fields selector clause. (like in SQL Statements)</summary>
 public DbJoin sC(String allfields) throws Exception
 {
  DbJoin ret = new DbJoin(this);
  ret.allFields = ret.fields.Clone(); //new Pack<string> (ret.fieldNames);
  Chain fields = new Chain(allfields + ",");
  if (!fields.Trim().equals("*,"))
  {
   ret.fields.Clear();
   Chain tokenMap = fields.less(bltZone.upon(fields.less(chrZone.upon(fields)))); //AttGeTr: upon is too expensive!
   while (fields.Trim().len() > 0)
   {
    Chain token = tokenMap.at(1, ",");
    ret.fields.Add(new DbField(fields.before(token).Trim().text()));   //AttGeTr: no type information is added for each field !!! / ToBeSolved !!!
    fields = fields.after(token);
    tokenMap = tokenMap.after(token);
   }
  } 
  return ret;
 }

 public DbJoin sC(DbField... fields) throws Exception
 {
  DbJoin ret = new DbJoin(this);
  ret.fields = new Pile<DbField>(0, fields);
  ret.allFields = ret.fields;
  //if (!fields.Trim().Equals("*")) { ret.fieldNames.clear(); ret.fieldTypes.clear(); while (fields.Trim().Length > 0) { ret.fieldTypes.Add(""); ret.fieldNames.Add(utl.cutl(ref fields, ",").Trim()); } } //AttGeTr: no type information is added for each field !!! / ToBeSolved !!!
  return ret;
 }
  
 /// <summary> sR ("select Rows") extends the WHERE clause with addinitional AND condition(s).</summary>
 public DbJoin sR(DbCnd... restrictions) throws Exception
 {
  DbJoin ret = new DbJoin(this);
  ret.allFields = ret.fields.Clone();  //new Pack<string> (ret.fieldNames);
  ret.selCnd.Add(restrictions);
  return ret;
 }

 /// <summary> sR ("select Rows") extends the WHERE clause with addinitional AND condition(s).</summary>
 public DbJoin sR(Pile<DbCnd> restrictions) throws Exception { return sR(Db.cloneCndArray(restrictions)); }  //public DbJoin sR(Pack<DbCnd> restrictions) { return sR(restrictions.ToArray<DbCnd>()); }

 /*
 public DbJoin UNI(DbJoin other) { return new DbJoin("U", this, other); }
 public DbJoin ICT(DbJoin other) { return new DbJoin("I", this, other); }
 public DbJoin XPT(DbJoin other) { return new DbJoin("X", this, other); }
 */
 
}








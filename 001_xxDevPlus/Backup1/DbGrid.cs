using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using ndBase;
using ndString;

namespace ndData
{
 public class DbGrid
 {
  //Copyright© 1996-2012 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties, You may use, modify, distribute this code under GNU public licence for NON-Commercial use as long as this notice remains unchanged.
  private static bool selfTested  = false;  private static string ass(bool expr) { if (!expr) throw new Exception(ass(true) + " SelfTest Failure"); return "DbGrid"; }

  private static Zone strZone = new Zone(new Pile<String>(), new Pile<String>("", true, "\"", "\"", "||:0"));
  private static Zone chrZone = new Zone(new Pile<String>(), new Pile<String>("", true, "'", "'", "||:0"));
  private static Zone bltZone = new Zone(new Pile<String>(), new Pile<String>("", true, "(", ")", "||:0"));
  private static Zone bktZone = new Zone(new Pile<String>(), new Pile<String>("", true, "[", "]", "||:0"));
  private static Zone cbtZone = new Zone(new Pile<String>(), new Pile<String>("", true, "{", "}", "||:0"));
  
  private void init() 
  { 
   Name = "";
   Short = "";
   if (!selfTested) selfTest(); 
  }


  //dbdb private Db                       db        = null;
  private  KeyPile<string, DbGrid>  tables          = new KeyPile<string, DbGrid>(); // not used if the DbGrid represents just 1 table ...

  private  Pile<DbField>            fields          = new Pile<DbField>();
  public   Pile<DbField>            Fields          { get { return fields; } }

  private  Pile<DbField>            allFields       = null;
  public   Pile<DbField>            AllFields       { get { return allFields; } }

  private  Pile<DbCnd>              joinCnd         = new Pile<DbCnd>();
  private  Pile<DbCnd>              selCnd          = new Pile<DbCnd>();

  public   string                   Name            {get; set;}
  public   string                   Short           {get; set;}

  internal string                   type            = "";
  internal Pile<DbGrid>             parts           = new Pile<DbGrid>();

  internal Db                       db              = null;
  public   Db                       Db              { get { return db; } set { db = value; }}



  private static void selfTest()
  {
   selfTested = true;
   //DbGrid g1 = db.Grid("tlb1 a, tbl2 b", Db.cd("a.id").IN(db.Grid("tbla a").sC("Max(id)").SLD), Db.cd("a.id").EQ(Db.ds("uuu"))).sC("a.nm, b.nm").sR(Db.cd("a.id").GT(2));
   //DbGrid g2 = new DbGrid(new Reach("Grid(\"tlb1 a, tbl2 b\", cd(\"a.id\").IN(Grid(\"tbla a\").sC(\"Max(id)\").SLD), cd(\"a.id\").EQ(ds(\"uuu\"))).sC(\"a.nm, b.nm\").sR(cd(\"a.id\").GT(2))"));
   //ass(Db._sql(g1.SLC).Equals(Db._sql(g2.SLC)));
  }


  
  private DbGrid(string type, DbGrid s1, DbGrid s2)
  {
   init();
   this.type = type.ToUpper().Substring(0, 1);
   parts.Push(s1);
   parts.Push(s2);
   if ((parts[1].db != null) && (parts[2].db != null)) if (parts[1].db != parts[2].db) throw new Exception("Unable to merge grids from different data sources in INTERSECT or EXCEPT or UNION");
   if ((parts[1].db == null) || (parts[2].db == null)) { if (parts[2].db == null) this.db = parts[1].db; else this.db = parts[2].db; parts[1].db = this.db; parts[2].db = this.db; }
  }

  // Ideen:
  // Eine Projektion aus joined Tables könnte etwa so definiert werden ......
  // Join jtb = new Join("mitarbeiter m, ProcessInstance pi", "m.Id = pi.mitarbeiter");
  // Projection pjtb = jtb.Project("m.Id, m.nm, m.vorname, pi.nm, pi.Id, pi.StarDate");
  // pjtb.Select(true, "m.Id > 200", "m.nm LIKE '*Stefan*'");

  internal DbGrid(DbGrid cloneFrom)
  {
   init();
   db = cloneFrom.db;
   fields     = cloneFrom.fields.Clone(); if (cloneFrom.allFields == cloneFrom.fields) allFields = fields; else if (cloneFrom.allFields != null) allFields = cloneFrom.allFields.Clone();  
   tables   = new KeyPile<string, DbGrid>(cloneFrom.tables);
   joinCnd  = cloneFrom.joinCnd.Clone();   // new Pile<Cnd>(cloneFrom.joinCnd);
   selCnd   = cloneFrom.selCnd.Clone();    // new Pile<Cnd>(cloneFrom.selCnd);
   Name     = cloneFrom.Name;
   Short    = cloneFrom.Short;
  }

  // 0: this is empty, 1: this represents a single table or view, 2: this represents a join (of tables, views, grids)
  private long currentState() { return (this.tables.Len == 0) && ((Name + Short).Length > 0) ? 1: this.tables.Len == 0 ? 0: 2; }

  public void addTables(string tables)
  {
   tables = tables.Trim();
   while (tables.Length > 0)
   {
    string tableShort = utl.cutl(ref tables, ",").Trim().ToLower();
    tables = tables.Trim();
    string tableName = utl.cutl(ref tableShort, " ").Trim().ToLower();
    if (tableShort.Length == 0) tableShort = tableName;
    //if ((this.tables.Len() == 0) && (tables.Length == 0)) {this.Name = tableName; this.Short = tableShort;  return; }

    if (currentState() == 0) // state is "empty", this DbGrid does not even contain a single table ...
    {
     Name = tableName; 
     Short = tableShort;
     //dbdb if (db.Tables.hasKey(Name)) fields = new KeyPile<string, string>(db.Tables[Name].fields);
     for (int i = 1; i <= fields.Len; i++) fields[i] = new DbField(Short + "." + fields[i].sql());
    }
    else
    {
     DbGrid g = new DbGrid(this);
     if (currentState() == 1) this.tables.Add(Name + " " + Short, new DbGrid(this)); 
     this.tables.Add(tableName + " " + tableShort, new DbGrid(tableName + " " + tableShort));
     Name = ""; 
     Short = "";
     fields = new Pile<DbField>();
     foreach (DbGrid tab in this.tables) foreach (DbField x in tab.Fields) fields.Add(new DbField(tab.Short + "." + x.sql()));
    }
   }
  }
    
  public void addTables(string tables, params DbCnd[] cnd)
  {
   addTables(tables);
   joinCnd.Add(cnd);
  }

  internal DbGrid(string tables, params DbCnd[] joinConditions)
  {
   init();
   allFields = fields;
   addTables(tables);
   joinCnd.Add(joinConditions);
  }

  internal DbGrid(string tables)
  {
   init();
   allFields = fields;
   addTables(tables);
  }

  internal DbGrid(Reach smb)
  {
   init();
   Zone   bktFilter   = new Zone(new Pile<string>(), new Pile<string>("", true, "(", ")", "||:1"));
   Zone   strFilter   = new Zone(new Pile<string>(), new Pile<string>("", true, "\"", "\"", "||:1"));
   if (smb.at(".OR").len > 0) smb = smb;

   //def = new Reach("Grid(\"cpt cpt\").sC(\"id\").sR(cd(\"idnm\").EQ(ds(\"TS\")).OR(cd(\"idnm\").EQ(ds(\"TO\"))).OR(cd(\"idnm\").EQ(ds(\"TO\")))).SLC");
   smb = new Reach(smb.text);
   
   Reach  gridDef     = bktFilter.on(smb);
   Reach  tblDef      = strFilter.on(gridDef);
   addTables(tblDef);
   Reach jCnd = smb.after(tblDef).after(1, ", ");
   joinCnd.Add(DbCnd.fromSymbolicDef(jCnd));
   string testsql = this.SLC.sql();
   smb = smb.after(gridDef).from(3);
   while (smb.len > 0)
   {
    if (smb.startsWith("sR("))
    {
     Reach selCnds = bktFilter.on(smb);
     joinCnd = sR(DbCnd.fromSymbolicDef(selCnds)).joinCnd;
     selCnd  = sR(DbCnd.fromSymbolicDef(selCnds)).selCnd;
     smb = smb.after(selCnds).from(3);
    }
    else
    if (smb.startsWith("sC("))
    {
     Reach colDef = strFilter.on(bktFilter.on(smb));
     fields = sC(colDef).fields;
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

  internal DbGrid(Db db, string tables, params DbCnd[] joinConditions)
  {
   init();
   this.db = db;
   allFields = fields;
   addTables(tables);
   joinCnd.Add(joinConditions);
  }

  internal DbGrid(Db db, string tables)
  {
   init();
   this.db = db;
   allFields = fields;
   addTables(tables);
  }

  public Pile<DbCnd> Join
  {
   get
   {
    return joinCnd;
    //if (joinCnd.Len == 0) return "";
    //string ret = "";
    //foreach (Cnd c in joinCnd) ret += " AND " + c.text();
    //return ret.Substring(5);
   }
  }

  public Pile<DbCnd> Where
  {
   get
   {
    return selCnd;
    //if (selCnd.Len == 0) return "";
    //string ret = "";
    //foreach (Cnd c in selCnd) ret += " AND " + c.text();
    //return ret.Substring(5);
   }
  }

  public string From
  {
   get
   {
    if (currentState() == 0) return "";
    if (currentState() == 1) return Name + " " + Short;
    string ret = "";
    foreach (DbGrid g in tables) ret += ", " + g.From;
    return ret.Substring(2);
   }
  }

  private static long in_Assertion = 10000;

  /// <summary>  SLC ("Select") builds a distinct DbSelect </summary>
  public DbSlc SLC
  {
   get
   {
    if (in_Assertion == 0)
    {
     in_Assertion++;
     DbSlc copyOfRet  = new DbSlc(this);
     DbSlc recreated   = new DbSlc((Reach)(copyOfRet.smb()));
     recreated   = db.Grid((Reach)(copyOfRet.smb())).SLC;
     ass(copyOfRet.sql().Equals(recreated.sql()));
     in_Assertion--;
    }
    return new DbSlc(this);
   }
  }

  public DbSlc SLC0 // work around a severe bug in Zone.on(). because of this endless loop here is a SLC Method WITHOUT extra debug checking
  {
   get
   {
    //if (in_Assertion == 0)
    //{
    // in_Assertion++;
    // DbSlc copyOfRet  = new DbSlc(this);
    // DbSlc recreated   = db.Grid((Reach)(Db._symbolic(copyOfRet))).SLC;
    // ass(Db._sql(copyOfRet).Equals(Db._sql(recreated)));
    // in_Assertion--;
    //}
    return new DbSlc(this);
   }
  }
  
  /// <summary>  SLD ("Select Distinct") builds a distinct DbSelect </summary>
  //public DbSlc   SLD                                   { get { return new DbSlc(this).DIST;   } }

  public DbSlc SLD 
  { 
   get 
   { 
    if (in_Assertion == 0) 
    {
     in_Assertion++;
     DbSlc copyOfRet  = new DbSlc(this).DST;
     DbSlc recreated   = new DbSlc((Reach)(copyOfRet.smb())).DST;
     //recreated   = db.Grid((Reach)(Db._smb(copyOfRet))).SLD;
     ass(copyOfRet.sql().Equals(recreated.sql())); 
     in_Assertion--; 
    } 
    return new DbSlc(this).DST; 
   }
  }

  public DbSlc SLD0 // work around a severe bug in Zone.on(). because of this endless loop here is a SLC Method WITHOUT extra debug checking
  {
   get
   {
    //if (in_Assertion == 0)
    //{
    // in_Assertion++;
    // DbSlc copyOfRet  = new DbSlc(this).DST;
    // DbSlc recreated   = db.Grid((Reach)(Db._symbolic(copyOfRet))).SLD;
    // ass(Db._sql(copyOfRet).Equals(Db._sql(recreated)));
    // in_Assertion--;
    //}
    return new DbSlc(this).DST;
   }
  }

  //public DbIns INS (params string[] values)          { return new DbInsert(Name, fieldNames, fieldTypes, utl.stringArr2s(values, ", "))   ; }
  //public DbIns INS (string values)                   { return new DbIns(Name, fieldNames, fieldTypes, values)                            ; }

  public long copyTo(Db tgtDb)
  {
   //if (this.Name.Equals("webpge")) this.Name = this.Name;
   tgtDb.exec(tgtDb.Tables[this.Name].DEL);
   DatSet dat = this.db.exec(this.db.Tables[this.Name].SLD);
   if (dat.Raws.Len == 0) return 0;
   return tgtDb.exec(tgtDb.Tables[this.Name].INS(dat));
  }

  public DbIns   INS (params object[] values)          
  {
   if (in_Assertion == 0)
   {
    in_Assertion++;
    DbIns copyOfRet  = new DbIns(Name, fields, values);
    DbIns recreated   = new DbIns((Reach)(copyOfRet.smb()));
    ass(copyOfRet.sql().Equals(recreated.sql()));
    in_Assertion--;
   }
   return new DbIns(Name, fields, values); 
  }

  public DbIns   INS (DatSet values)                   { return new DbIns(this, values); }
  public DbIns   INS (DbSlc sel)                       { return new DbIns(Name, fields, sel); }
  //public DbIns INS (DbGrid g)                        { return new DbInsert(Name, fields, new DbSelect(g)); }

  public DbUpd   UPD (params object[] values)          { return new DbUpd(Name, fields, values, Where)           ; }
  //public DbUpd UPD (params string[] values)          { return new DbUpdate(Name, utl.stringArr2s(fieldNames.ToArray(), ", "), utl.stringArr2s(values, ", "), Where)     ; }
  //public DbUpd UPD (string values)                   { return new DbUpd(Name, utl.stringArr2s(fieldNames.ToArray(), ", "), values, Where)                           ; }

  //public DbDel DEL (params Cnd[] deleteConditions)   { return new DbDelete(Name, deleteConditions); }
  public DbDel   DEL                                   { get { return new DbDel(Name, selCnd.array()); } }

  /// <summary> sC ("select Columns") is the fields selector clause. (like in SQL Statements)</summary>
  public DbGrid sC(string allfields)
  {
   DbGrid ret = new DbGrid(this);
   ret.allFields = ret.fields.Clone(); //new Pile<string> (ret.fieldNames);
   Reach fields = new Reach(allfields + ",");
   if (!fields.Trim().equals("*,"))
   {
    ret.fields= new Pile<DbField>();
    Reach tokenMap = fields - bltZone.upon(fields - chrZone.upon(fields)); //AttGeTr: upon is too expensive!
    while (fields.Trim().len > 0)
    {
     Reach token = tokenMap.at(1, ",");
     ret.fields.Add(new DbField(fields.before(token).Trim().text));   //AttGeTr: no type information is added for each field !!! / ToBeSolved !!!
     fields = fields.after(token);
     tokenMap = tokenMap.after(token);
    }
   } 
   return ret;
  }

  public DbGrid sC(params DbField[] fields)
  {
   DbGrid ret = new DbGrid(this);
   ret.fields = new Pile<DbField>();
   foreach (DbField fld in fields) ret.fields.Add(fld);
   ret.allFields = ret.fields;
   //if (!fields.Trim().Equals("*")) { ret.fieldNames.clear(); ret.fieldTypes.clear(); while (fields.Trim().Length > 0) { ret.fieldTypes.Add(""); ret.fieldNames.Add(utl.cutl(ref fields, ",").Trim()); } } //AttGeTr: no type information is added for each field !!! / ToBeSolved !!!
   return ret;
  }
  
  /// <summary> sR ("select Rows") extends the WHERE clause with addinitional AND condition(s).</summary>
  public DbGrid sR(params DbCnd[] restrictions)
  {
   DbGrid ret = new DbGrid(this);
   ret.allFields = ret.fields.Clone();  //new Pile<string> (ret.fieldNames);
   ret.selCnd.Add(restrictions);
   return ret;
  }

  /// <summary> sR ("select Rows") extends the WHERE clause with addinitional AND condition(s).</summary>
  public DbGrid sR(Pile<DbCnd> restrictions) { return sR(restrictions.array()); }  //public DbGrid sR(Pile<Cnd> restrictions) { return sR(restrictions.ToArray<Cnd>()); }

  /*
  public DbGrid UNI(DbGrid other) { return new DbGrid("U", this, other); }
  public DbGrid ICT(DbGrid other) { return new DbGrid("I", this, other); }
  public DbGrid XPT(DbGrid other) { return new DbGrid("X", this, other); }
  */
  
 }

}




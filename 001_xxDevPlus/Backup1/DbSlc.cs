

using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;


using ndBase;
using ndString;

namespace ndData
{
 public class DbSlc
 {
  //Copyright© 1996-2012 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties, You may use, modify, distribute this code under GNU public licence for NON-Commercial use as long as this notice remains unchanged.
  private static bool selfTested  = false;  private static string ass(bool expr) { if (!expr) throw new Exception(ass(true) + " SelfTest Failure"); return "DbSlc"; }

  private static void selfTest()
  {
   selfTested = true;
   ctx cx = new ctx();
   Db db = new Db(cx.DbDrivers);
   ass(db.Grid("tbl t").sC(db.dF("id"), db.dF("nm")).SLC.sql().Equals(" SELECT id, nm \r\n FROM tbl t"));
   ass(db.Grid("tbl t").sC((DbField)(db.dF("id").cct("nm"))).SLC.sql().Equals(" SELECT CCT(id, nm) \r\n FROM tbl t"));
   ass(db.Grid("tbl t").sC(db.dF("ci.nm"), db.dF("'['").cct("cc.nm").cct("']+i'")).SLC.sql().Equals(" SELECT ci.nm, CCT(CCT('[', cc.nm), ']+i') \r\n FROM tbl t"));
   ass(db.Grid("tbl").sC("id, nm").sR(db.cd("id").GT(0)).SLC.sql().Equals(" SELECT id, nm \r\n FROM tbl tbl \r\n WHERE ( (id > 0)  ) "));
   ass(db.Grid("tbla a, tblb b").sC("id, nm").sR(db.cd("id").GT(0)).SLC.sql().Equals(" SELECT id, nm \r\n FROM tbla a, tblb b \r\n WHERE ( (id > 0)  ) "));

  } 

  private void init()  { if (!selfTested) selfTest(); }

  private  long                        count         = -1;
  internal bool                        distinct      = false;

  private  Pile<DbField>               fields        = new Pile<DbField>();
  public   Pile<DbField>               Fields        { get { return fields; } }
 
  private  KeyPile<string, string>     from          = new KeyPile<string, string>();
  private  Pile<DbCnd>                 join          = new Pile<DbCnd>();
  private  Pile<DbCnd>                 where         = new Pile<DbCnd>();
  private  string                      order         = "";

  public   long                        Count         { get{ return count;} set{ count = value;}}
  public   KeyPile<string, string>     From          { get { return from; } }
  public   Pile<DbCnd>                 Join          { get { return join; } }
  public   Pile<DbCnd>                 Where         { get { return where; } }
  public   string                      Order         { get{ return order;}}

  internal string                      type          = "";
  internal DbSlcBlock                  parts         = new DbSlcBlock();


  private DbSlc(string type, DbSlc s1, DbSlc s2)
  {
   init(); 
   this.type = type.ToUpper().Trim();
   //string s = Db._sql(s2);
   parts.Push(s1);
   parts.Push(s2);
  }

  private DbSlc(DbSlc cloneFrom) 
  {
   init(); 
   this.count        = cloneFrom.count;
   this.fields       = cloneFrom.fields.Clone();    //new Pile<string>(cloneFrom.fieldNames);
   this.from         = new KeyPile<string, string>(cloneFrom.from);
   this.join         = cloneFrom.join.Clone();          //new Pile<Cnd>(cloneFrom.join);
   this.where        = cloneFrom.where.Clone();         //new Pile<Cnd>(cloneFrom.where);
   this.order        = cloneFrom.order;
  }

  private void init(Pile<DbField> fields, string from, Pile<DbCnd> join, Pile<DbCnd> where)
  {
   this.count = -1;
   this.distinct = false;
   this.order = "";
   this.fields = fields.Clone();
   from   = from.Trim();
   while (from.Length > 0)
   {
    string tShort = utl.cutl(ref from, ",").Trim().ToLower();
    string tName  = utl.cutl(ref tShort, " ").Trim();
    tShort = tShort.Trim(); if (tShort.Length == 0) tShort = tName;
    this.from.Add(tShort, tName);
    from = from.Trim();
   }
   this.join  = join;
   this.where = where;
  }

  public DbSlc(DbGrid g)                                                
  {
   init(); 
   if (g.type.Length == 0)
   {
    init(g.Fields, g.From, g.Join, g.Where);
    //string sql = Db._sql(this);
   }
   else
   {
    type = g.type;
    parts.Push(new DbSlc(g.parts[1]));
    parts.Push(new DbSlc(g.parts[2]));    
   }
  }

  public DbSlc(Reach smb) 
  {
   Db db = new Db(new ctx().DbDrivers);
   init(); 
   DbSlc ret = new DbGrid(smb.before(-1, ".SL")).SLC;
   smb = smb.after(-1, ".SL");
   if (smb.startsWith("D")) ret = ret.DST;
   smb  = smb.from(3);
   while (smb.len > 0)
   {
    if (smb.startsWith("DST")) { ret = ret.DST; smb  = smb.from(5); continue; }
    if (smb.startsWith("TOP(")) { ret = ret.TOP(long.Parse(smb.from(5).before(1, ")").text)); smb  = smb.after(1, ")").from(2); continue; }
    if (smb.startsWith("ORD(")) { ret = ret.ORD(smb.from(5).before(1, ")").after(1, "\"").before(1, "\"").text); smb  = smb.after(1, ")").from(2); continue; }
   }
   this.count        = ret.count;
   this.fields       = ret.fields.Clone(); //new Pile<string>(ret.fieldNames);
   this.from         = new KeyPile<string, string>(ret.from);
   this.join         = ret.join.Clone();       //new Pile<Cnd>(ret.join);
   this.where        = ret.where.Clone();      //new Pile<Cnd>(ret.where);
   this.order        = ret.order;
  }

  private DbSlc() { init(); }
  
  public DbSlc Clone()
  {
   DbSlc ret = new DbSlc();
   ret.count        = count;
   ret.distinct     = distinct;
   ret.fields       = fields.Clone();
   ret.from         = new KeyPile<string, string>();
   foreach (string k in from.Keys) ret.from.Add(k, from[k]);
   ret.join         = join.Clone();    //new Pile<Cnd>(join);
   ret.where        = where.Clone();   //new Pile<Cnd>(where);
   ret.order        = order;
   return ret;
  }

  public DbSlc DST                    { get { DbSlc ret = Clone(); ret.distinct = true; return ret; } }
  public DbSlc TOP(long count)        { DbSlc ret = Clone(); ret.count = count; return ret; }
  public DbSlc ORD(string order)      { DbSlc ret = Clone(); ret.order = order; return ret; }

  public Reach fromClause() { return fromClause(new Db(new ctx().DbDrivers)); }
  public Reach fromClause(Db db)
  {
   Reach ret = "";
   foreach (string t in From.Keys) ret += db.dbTable(From[t]) + " " + t.Replace(".", "") + ", "; if (ret.EndsWith(" \r\n FROM ")) ret = ret.before(-9); else ret = ret.before(-2);
   return ret;
  }

  public Reach whereClause() { return whereClause(new Db(new ctx().DbDrivers)); }
  public Reach whereClause(Db db)
  {
   Reach ret = "";
   string where = "";
   if (Join.Len > 0) { where += "( "; foreach (DbCnd j in Join) if (j.sql(db).len > 0) where += j.sql(db) + " AND "; if (where.Length < 4) where = ""; else where = where.Substring(0, where.Length - 4) + " ) "; }
   string and = "";
   if (Where.Len > 0) { and += "( "; foreach (DbCnd w in Where) if (w.sql(db).len > 0) and += w.sql(db) + " AND "; if (and.Length < 4) and = ""; else and = and.Substring(0, and.Length - 4) + " ) "; }
   if ((where.Length > 0) && (and.Length > 0)) where = " \r\n WHERE " + where + " \r\n AND " + and; else if (where.Length > 0) where = " \r\n WHERE " + where; else if (and.Length > 0) where = " \r\n WHERE " + and; else where = "";
   if (db.Dbms.EndsWith("ora")) if (Count > 0) if (Join.Len + Where.Len > 0) where += " AND (ROWNUM <= " + Count + ") "; else where += " WHERE (ROWNUM <= " + Count + ") ";
   ret += where;
   return ret;
  }

  public string sql()                 { return sql(new Db(new ctx().DbDrivers)); }
  public string sql(Db db)
  {
   if (type.Length == 0)
   {

    Reach ret = " SELECT ";
    if (distinct) ret += " DISTINCT ";
    if (db.Dbms.EndsWith("mss")) if (Count > 0) ret += " TOP " + Count + " ";
    if (db.Dbms.EndsWith("syb")) if (Count > 0) ret += " TOP " + Count + " ";
    if (fields.Len == 0)
     ret = new Reach(" SELECT * \r\n FROM ");
    else
    {
     DbField f = new DbField("");
     foreach (DbField t in fields) f = f.apd(t); 
     ret = ret + f.sql(db).from(3) + " \r\n FROM ";
    }

    /*
    Zone   bktFilter   = new Zone(new Pile<string>(), new Pile<string>("", true, "(", ")", "||:1"));
    while (ret.at(1, "::CC::").len > 0)
    {
     Pile<Reach> parts = new Pile<Reach>();
     Reach toConvert = bktFilter.on(ret.after(1, "::CC::"));
     while (toConvert.len > 0) {parts.Push(toConvert.before(1, ", ")); toConvert = toConvert.after(1, ", "); }
     Reach res = " CONCAT(";
     foreach(Reach r in parts) res = res + ", " + r;
     res = res + ")";
     toConvert = bktFilter.on(ret.after(1, "::CC::"));
     if (driver.Equals(dbDrivers["MyS"])) ret = ret.Replace(" ::&:: ", " + ");
     if (driver.Equals(dbDrivers["Ora"])) ret = ret.Replace(" ::&:: ", " + ");
     if (driver.Equals(dbDrivers["MsS"])) ret = ret.Replace(" ::&:: ", " + ");
     if (driver.Equals(dbDrivers["PgS"])) ret.Replace(" ::&:: ", " || "); 
    }
    */

    ret += fromClause(db);
    if (ret.EndsWith(" \r\n FROM ")) ret = ret.before(-9);
    ret += whereClause(db);

    if (Order.Length > 0) ret += " ORDER BY " + Order;
    if (db.Dbms.EndsWith("mys")) if (Count > 0) ret += " LIMIT 0," + (Count - 1) + " ";
    if (db.Dbms.EndsWith("pgs")) if (Count > 0) ret += " LIMIT " + (Count - 1) + " ";
    return ret;
   }
   if (type.Equals("OR")) return "(\r\n" + parts[1].sql(db) + ")\r\nUNION\r\n("   + parts[2].sql(db) + "\r\n)";
   if (type.Equals("AND")) return "(\r\n" + parts[1].sql(db) + "\r\nINTERSECT\r\n" + parts[2].sql(db) + "\r\n)";
   if (type.Equals("MNS")) return "(\r\n" + parts[1].sql(db) + "\r\nEXCEPT\r\n"    + parts[2].sql(db) + "\r\n)";
   return "INVALID TYPE IN DBSELECT OR IN DBGRID";
  }

  /*
  public string _sql()
  {
   if (type.Length == 0)
   {
    Reach ret = " SELECT ";
    if (distinct) ret += " DISTINCT ";
    if (Count > 0) ret += " TOP " + Count + " ";
    if (FieldTerms.Len > 0)
    {
     Trm trm = FieldTerms[1]; foreach (Trm t in FieldTerms.from(2)) trm = trm.apd(t); ret += trm._sql() + " \r\n FROM "; ;
    } else
    {
     if (FieldNames.Len == 0) ret += "*, "; else foreach (string f in FieldNames) ret += f + ", "; ret = ret.before(-2) + " \r\n FROM ";
    }
    foreach (string t in From.Keys) ret += From[t] + " " + t + ", ";
    ret = ret.before(-2);
    string where = "";
    if (Join.Len > 0) { where += "( "; foreach (Cnd j in Join) if (j._sql().len > 0) where += j._sql() + " AND "; if (where.Length < 4) where = ""; else where = where.Substring(0, where.Length - 4) + " ) "; }
    string and = "";
    if (Where.Len > 0) { and += "( "; foreach (Cnd w in Where) if (w._sql().len > 0) and += w._sql() + " AND "; if (and.Length < 4) and = ""; else and = and.Substring(0, and.Length - 4) + " ) "; }
    if ((where.Length > 0) && (and.Length > 0)) where = " \r\n WHERE " + where + " \r\n AND " + and; else if (where.Length > 0) where = " \r\n WHERE " + where; else if (and.Length > 0) where = " \r\n WHERE " + and; else where = "";
    //if (driver.Equals(dbDrivers["Ora"])) if (s.Count > 0) where += " AND (ROWNUM <= " + s.Count + ") ";
    //if (driver.Equals(dbDrivers["PgS"])) if (s.Count > 0) where += " AND (ROWNUM <= " + s.Count + ") ";
    ret += where;
    if (Order.Length > 0) ret += " ORDER BY " + Order;
    //if (driver.Equals(dbDrivers["MyS"])) if (s.Count > 0) ret += " LIMIT 0," + (s.Count - 1) + " ";
    return ret;
   }
   if (type.Equals("OR")) return "(\r\n" + parts[1]._sql() + ")\r\nUNION\r\n("   + parts[2]._sql() + "\r\n)";
   if (type.Equals("AND")) return "(\r\n" + parts[1]._sql() + "\r\nINTERSECT\r\n" + parts[2]._sql() + "\r\n)";
   if (type.Equals("MNS")) return "(\r\n" + parts[1]._sql() + "\r\nEXCEPT\r\n"    + parts[2]._sql() + "\r\n)";
   return "INVALID TYPE IN DBSELECT OR IN DBGRID";
  }
  */
  
  public string smb()
  {
   if (type.Length == 0)
   {
    string ret = "Grid(\"";
    foreach (string t in From.Keys) ret += From[t] + " " + t + ", ";
    ret = ret.Substring(0, ret.Length - 2) + "\"";
    if (Join.Len > 0) foreach (DbCnd j in Join) if (j.sql().len > 0) ret +=  ", " + j.smb();
    ret += ").sC(\"";
    if (fields.Len == 0) ret += "*, "; else foreach (DbField f in fields) ret += f.smb() + ", ";
    ret = ret.Substring(0, ret.Length - 2) + "\").sR(";
    if (Where.Len == 0) ret = ret.Substring(0, ret.Length - 5) + ", "; else foreach (DbCnd w in Where) if (w.sql().len > 0) ret += w.smb() + ", ";
    ret = ret.Substring(0, ret.Length - 2) + ")";
    if (distinct) ret += ".SLD"; else ret += ".SLC";
    if (Count > 0) ret += ".TOP(" + Count + ")";
    if (Order.Length > 0) ret += ".ORD(" + Order + ")";
    return ret;
   }
   if (type.Equals("OR")) return "(OR(\r\n"  + parts[1].smb() + ", "    + parts[2].smb() + "))";
   if (type.Equals("AND")) return "(AND(\r\n" + parts[1].smb() + ", "   + parts[2].smb() + "))";
   if (type.Equals("MNS")) return "(MNS(\r\n" + parts[1].smb() + ", "   + parts[2].smb() + "))";
   return "INVALID TYPE IN DBSELECT OR IN DBGRID";
  }




  //public DbSlc Clone()             { return new DbSlc(this); }

  public DbSlc OR (params DbSlc[] other) { DbSlc ret = this; for (int i = 0; i < other.Length; i++) ret = new DbSlc("OR",  ret, other[i]); return ret; }
  public DbSlc AND(params DbSlc[] other) { DbSlc ret = this; for (int i = 0; i < other.Length; i++) ret = new DbSlc("AND", ret, other[i]); return ret; }
  public DbSlc MNS(params DbSlc[] other) { DbSlc ret = this; for (int i = 0; i < other.Length; i++) ret = new DbSlc("MNS", ret, other[i]); return ret; }
    
  
 }
}


package org.xxdevplus.data;

import org.xxdevplus.struct.KeyPile;
import org.xxdevplus.struct.Pile;
import org.xxdevplus.utl.ctx;
import org.xxdevplus.utl.utl;
import org.xxdevplus.chain.Chain;

 public class DbSlc
 {
  //Copyright© 1996-2020 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties, You may use, modify, distribute this code under GNU public licence for NON-Commercial use as long as this notice remains unchanged.
  private static boolean selfTested  = false;  private static String ass(boolean expr) throws Exception { if (!expr) throw new Exception(ass(true) + " SelfTest Failure"); return "DbSlc"; } 
  
  private static void selfTest() throws Exception 
  { 
   selfTested = true;
   Db db = new Db(new ctx().DbDrivers());
   ass(db.Join("tbl t").sC(db.dF("id"), db.dF("nm")).SLC().sql().equals(" SELECT id, nm \r\n FROM tbl t"));
   ass(db.Join("tbl t").sC(db.dF("id").cct("nm")).SLC().sql().equals(" SELECT CCT(id, nm) \r\n FROM tbl t"));
   ass(db.Join("tbl t").sC(db.dF("ci.nm"), db.dF("'['").cct("cc.nm").cct("']+i'")).SLC().sql().equals(" SELECT ci.nm, CCT(CCT('[', cc.nm), ']+i') \r\n FROM tbl t"));
   ass(db.Join("tbl").sC("id, nm").sR(db.cd("id").GT(0)).SLC().sql().equals(" SELECT id, nm \r\n FROM tbl tbl \r\n WHERE ( (id > 0)  ) "));
   ass(db.Join("tbla a, tblb b").sC("id, nm").sR(db.cd("id").GT(0)).SLC().sql().equals(" SELECT id, nm \r\n FROM tbla a, tblb b \r\n WHERE ( (id > 0)  ) "));
  } 
  
  private void init() throws Exception  { if (!selfTested) selfTest();  }

  private  long                        count          = -1;
           boolean                     distinct       = false;

  private  Pile<DbField>               fields         = new Pile<DbField>();
  public   Pile<DbField>               Fields (   )   { return fields; }

  private  KeyPile<String, String>     from           = new KeyPile<String, String>();
  private  Pile<DbCnd>                 join           = new Pile<DbCnd>();
  private  Pile<DbCnd>                 where          = new Pile<DbCnd>();
  private  String                      order          = "";

  public   long                         Count()       { return count;} public void Count(long value) { count = value;}
  public   KeyPile<String, String>      From()        { return from;  }
  public   Pile<DbCnd>                  Join()        { return join;  }
  public   Pile<DbCnd>                  Where()       { return where; }
  public   String                       Order()       { return order; }

           String                     type         = "";
           DbSlcBlock                 parts        = new DbSlcBlock();


  private DbSlc(String type, DbSlc s1, DbSlc s2) throws Exception
  {
   init(); 
   this.type = type.toUpperCase().trim();
   //string s = Db._sql(s2);
   parts.Push(s1);
   parts.Push(s2);
  }

  private DbSlc(DbSlc cloneFrom) throws Exception 
  {
   init(); 
   this.count        = cloneFrom.count;
   this.fields       = cloneFrom.fields.Clone();    //new Pack<string>(cloneFrom.fieldNames);
   this.from         = new KeyPile<String, String>(cloneFrom.from);
   this.join         = cloneFrom.join.Clone();          //new Pack<DbCnd>(cloneFrom.join);
   this.where        = cloneFrom.where.Clone();         //new Pack<DbCnd>(cloneFrom.where);
   this.order        = cloneFrom.order;
  }

  private void init(Pile<DbField> fields, String frm, Pile<DbCnd> join, Pile<DbCnd> where) throws Exception
  {
   String[] fr  = new String[] {frm};
   this.count = -1;
   this.distinct = false;
   this.order = "";
   this.fields = new Pile<DbField>(0, Db.cloneDbFieldArray(fields));
   fr[0]   = fr[0].trim();
   while (fr[0].length() > 0)
   {
    String[] tShort = new String[] {utl.cutl(fr, ",").trim().toLowerCase()};
    String tName  = utl.cutl(tShort, " ").trim();
    tShort[0] = tShort[0].trim(); if (tShort[0].length() == 0) tShort[0] = tName;
    this.from.Add(tShort[0], tName);
    fr[0] = fr[0].trim();
   }
   this.join  = join;
   this.where = where;
  }

  public DbSlc(DbJoin g) throws Exception
  {
   init(); 
   if (g.type.length() == 0)
   {
    init(g.Fields(), g.From(), g.Join(), g.Where());
    //string sql = Db._sql(this);
   }
   else
   {
    type = g.type;
    parts.Push(new DbSlc(g.parts.g(1)));
    parts.Push(new DbSlc(g.parts.g(2)));    
   }
  }

  public DbSlc(Chain smb) throws Exception
  {
   Db db = new Db(new ctx().DbDrivers());
   init(); 
   DbSlc ret = new DbJoin(smb.before(-1, ".SL")).SLC();
   smb = smb.after(-1, ".SL");
   if (smb.startsWith("D")) ret = ret.DST();
   smb  = smb.from(3);
   while (smb.len() > 0)
   {
    if (smb.startsWith("DST")) { ret = ret.DST(); smb  = smb.from(5); continue; }
    if (smb.startsWith("TOP(")) { ret = ret.TOP(Long.parseLong(smb.from(5).before(1, ")").text())); smb  = smb.after(1, ")").from(2); continue; }
    if (smb.startsWith("ORD(")) { ret = ret.ORD(smb.from(5).before(1, ")").after(1, "\"").before(1, "\"").text()); smb  = smb.after(1, ")").from(2); continue; }
   }
   this.count        = ret.count;
   this.fields       = ret.fields.Clone(); //new Pack<string>(ret.fieldNames);
   this.from         = new KeyPile<String, String>(ret.from);
   this.join         = ret.join.Clone();       //new Pack<DbCnd>(ret.join);
   this.where        = ret.where.Clone();      //new Pack<DbCnd>(ret.where);
   this.order        = ret.order;
  }

  private DbSlc() throws Exception { init(); }
  
  public DbSlc Clone() throws Exception
  {
   DbSlc ret = new DbSlc();
   ret.count        = count;
   ret.distinct     = distinct;
   ret.fields       = fields.Clone();
   ret.from         = new KeyPile<String, String>();
   for (String k : from.Keys()) ret.from.Add(k, from.g(k));
   ret.join         = join.Clone();    //new Pack<DbCnd>(join);
   ret.where        = where.Clone();   //new Pack<DbCnd>(where);
   ret.order        = order;
   return ret;
  }

  public DbSlc DST() throws Exception                   { DbSlc ret = Clone(); ret.distinct = true; return ret; }
  public DbSlc TOP(long count) throws Exception         { DbSlc ret = Clone(); ret.count = count; return ret; }
  public DbSlc ORD(String order) throws Exception       { DbSlc ret = Clone(); ret.order = order; return ret; }

  public Chain fromClause() throws Exception { return fromClause(new Db(new ctx().DbDrivers())); }

  public Chain fromClause(Db db) throws Exception
  {
   Chain ret = new Chain("");
   for (String t : From().Keys()) ret = ret.plus(db.dbTable(From().g(t))).plus(" ").plus(t.replace(".", "")).plus(", ");
   if (ret.EndsWith(" \r\n FROM ")) ret = ret.before(-9); else ret = ret.before(-2);
   return ret;
  }

  public Chain whereClause() throws Exception { return whereClause(new Db(new ctx().DbDrivers())); }
  public Chain whereClause(Db db) throws Exception
  {
   Chain ret = new Chain("");
   String where = "";
   if (Join().Len() > 0) { where += "( "; for (DbCnd j : Join()) if (j.sql(db).len() > 0) where += j.sql(db).text() + " AND "; if (where.length() < 4) where = ""; else where = where.substring(0, where.length() - 4) + " ) "; }
   String and = "";
   if (Where().Len() > 0) { and += "( "; for (DbCnd w : Where()) if (w.sql(db).len() > 0) and += w.sql(db).text() + " AND "; if (and.length() < 4) and = ""; else and = and.substring(0, and.length() - 4) + " ) "; }
   if ((where.length() > 0) && (and.length() > 0)) where = " \r\n WHERE " + where + " \r\n AND " + and; else if (where.length() > 0) where = " \r\n WHERE " + where; else if (and.length() > 0) where = " \r\n WHERE " + and; else where = "";
   if (db.Dbms().endsWith("ora")) if (Count() > 0) if (Join().Len() + Where().Len() > 0) where += " AND (ROWNUM <= " + Count() + ") "; else where += " WHERE (ROWNUM <= " + Count() + ") ";
   ret = ret.plus(where);
   return ret;
  }

  public String sql() throws Exception { return sql(new Db(new ctx().DbDrivers())); }
  public String sql(Db db) throws Exception
  {
   if (type.length() == 0)
   {

    Chain ret = new Chain(" SELECT ");
    if (distinct) ret = ret.plus(" DISTINCT ");
    if (db.Dbms().endsWith("mss")) if (Count() > 0) ret = ret.plus(" TOP " + Count() + " ");
    if (db.Dbms().endsWith("syb")) if (Count() > 0) ret = ret.plus(" TOP " + Count() + " ");
    if (fields.Len() == 0)
     ret = new Chain(" SELECT * \r\n FROM ");
    else
    {
     DbField f = new DbField("");
     for (DbField t : fields) f = f.apd(t);
     ret = ret.plus(f.sql(db).from(3)).plus(" \r\n FROM ");
    }

    /*
    Zone   bktFilter   = new Zone(new Pack<string>(), new Pack<string>("", true, "(", ")", "||:1"));
    while (ret.at(1, "::CC::").len > 0)
    {
     Pack<Reach> parts = new Pack<Reach>();
     Chain toConvert = bktFilter.on(ret.after(1, "::CC::"));
     while (toConvert.len > 0) {parts.Push(toConvert.before(1, ", ")); toConvert = toConvert.after(1, ", "); }
     Chain res = " CONCAT(";
     foreach(Chain r in parts) res = res + ", " + r;
     res = res + ")";
     toConvert = bktFilter.on(ret.after(1, "::CC::"));
     if (driver.Equals(dbDrivers["MyS"])) ret = ret.Replace(" ::&:: ", " + ");
     if (driver.Equals(dbDrivers["Ora"])) ret = ret.Replace(" ::&:: ", " + ");
     if (driver.Equals(dbDrivers["MsS"])) ret = ret.Replace(" ::&:: ", " + ");
     if (driver.Equals(dbDrivers["PgS"])) ret.Replace(" ::&:: ", " || "); 
    }
    */

    ret = ret.plus(fromClause(db));
    if (ret.EndsWith(" \r\n FROM ")) ret = ret.before(-9);
    ret = ret.plus(whereClause(db));

    if (Order().length() > 0) ret = ret.plus(" ORDER BY ").plus(Order());
    if (db.Dbms().endsWith("mys")) if (Count() > 0) ret = ret.plus(" LIMIT 0," + (Count() - 1) + " ");
    if (db.Dbms().endsWith("pgs")) if (Count() > 0) ret = ret.plus(" LIMIT " + (Count() - 1) + " ");
    return ret.text();
   }
   if (type.equals("OR"))  return "(\r\n" + parts.g(1).sql(db) + ")\r\nUNION\r\n("   + parts.g(2).sql(db) + "\r\n)";
   if (type.equals("AND")) return "(\r\n" + parts.g(1).sql(db) + "\r\nINTERSECT\r\n" + parts.g(2).sql(db) + "\r\n)";
   if (type.equals("MNS")) return "(\r\n" + parts.g(1).sql(db) + "\r\nEXCEPT\r\n"    + parts.g(2).sql(db) + "\r\n)";
   return "INVALID TYPE IN DBSELECT OR IN DBGRID";
  }

  /*
  public string _sql()
  {
   if (type.Length == 0)
   {
    Chain ret = " SELECT ";
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
    if (Join.Len > 0) { where += "( "; foreach (DbCnd j in Join) if (j._sql().len > 0) where += j._sql() + " AND "; if (where.Length < 4) where = ""; else where = where.Substring(0, where.Length - 4) + " ) "; }
    string and = "";
    if (Where.Len > 0) { and += "( "; foreach (DbCnd w in Where) if (w._sql().len > 0) and += w._sql() + " AND "; if (and.Length < 4) and = ""; else and = and.Substring(0, and.Length - 4) + " ) "; }
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
  
  public String smb() throws Exception
  {
   if (type.length() == 0)
   {
    String ret = "Grid(\"";
    for (String t : From().Keys()) ret += From().g(t) + " " + t + ", ";
    ret = ret.substring(0, ret.length() - 2) + "\"";
    if (Join().Len() > 0) for (DbCnd j : Join()) if (j.sql().len() > 0) ret +=  ", " + j.smb();
    ret += ").sC(\"";
    if (fields.Len() == 0) ret += "*, "; else for (DbField f : fields) ret += f.smb() + ", ";
    ret = ret.substring(0, ret.length() - 2) + "\").sR(";
    if (Where().Len() == 0) ret = ret.substring(0, ret.length() - 5) + ", "; else for (DbCnd w : Where()) if (w.sql().len() > 0) ret += w.smb().text() + ", ";
    ret = ret.substring(0, ret.length() - 2) + ")";
    if (distinct) ret += ".SLD"; else ret += ".SLC";
    if (Count() > 0) ret += ".TOP(" + Count() + ")";
    if (Order().length() > 0) ret += ".ORD(" + Order() + ")";
    return ret;
   }
   if (type.equals("OR"))  return "(OR(\r\n"  + parts.g(1).smb() + ", "   + parts.g(2).smb() + "))";
   if (type.equals("AND")) return "(AND(\r\n" + parts.g(1).smb() + ", "   + parts.g(2).smb() + "))";
   if (type.equals("MNS")) return "(MNS(\r\n" + parts.g(1).smb() + ", "   + parts.g(2).smb() + "))";
   return "INVALID TYPE IN DBSELECT OR IN DBGRID";
  }




  //public DbSlc Clone()             { return new DbSlc(this); }

  public DbSlc OR (DbSlc... other) throws Exception { DbSlc ret = this; for (int i = 0; i < other.length; i++) ret = new DbSlc("OR",  ret, other[i]); return ret; }
  public DbSlc AND(DbSlc... other) throws Exception { DbSlc ret = this; for (int i = 0; i < other.length; i++) ret = new DbSlc("AND", ret, other[i]); return ret; }
  public DbSlc MNS(DbSlc... other) throws Exception { DbSlc ret = this; for (int i = 0; i < other.length; i++) ret = new DbSlc("MNS", ret, other[i]); return ret; }
    
  
 }













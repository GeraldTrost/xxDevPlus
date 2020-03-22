using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using ndBase;
using ndString;

namespace ndData
{

 public abstract class DbObj
 {
  //Copyright© 1996-2012 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties, You may use, modify, distribute this code under GNU public licence for NON-Commercial use as long as this notice remains unchanged.
  private static bool selfTested  = false;  private static string ass(bool expr) { if (!expr) throw new Exception(ass(true) + " SelfTest Failure"); return "DbObj"; } private static void selfTest() { selfTested = true; }

  protected       DbGrid                 bscTbl    = null;
  protected       DbGrid                 vrbTbl    = null;
  public readonly Db                     db        = null;
  public          KeyPile<string, int>   map       = new KeyPile<string, int>();
  private         Pile<DbObj>            recs      = new Pile<DbObj>();
  public          Pile<DbObj>            Recs      { get {return recs;} }

  private         Pile<object>           val       = new Pile<object>();
  private         Pile<object>           ori       = new Pile<object>();

  protected       object                 getVal(int inx)                { return val[inx]; }

  private         bool                   checkDirty()
  {
   if (!isDirty) return false;
   for (int inx = 1; inx <= val.Len; inx++)
   {
    bool isDifferent = false;
    Type t = val[inx].GetType();
    if (t == typeof(Reach))   isDifferent = !((Reach)(ori[inx])).equals((Reach)(val[inx]));
    if (t == typeof(string))  isDifferent = !((string)(ori[inx])).Equals((string)(val[inx]));
    if (t == typeof(double))  isDifferent = ((double)(ori[inx])) !=     ((double)(val[inx]));
    if (t == typeof(long))    isDifferent = ((long)(ori[inx]))    !=     ((long)(val[inx]));
    if (isDifferent) return true;
   }
   ori = new Pile<object>();
   return false;
  }
  
  protected       void                  setVal(int inx, object value)  
  {
   if (initializing) { val[inx] = value; isDirty = false; return; }
   bool isDifferent = false;
   Type t = val[inx].GetType();
   if (t == typeof(Reach))  isDifferent = !((Reach)(value)).equals ((Reach)  (val[inx]));
   if (t == typeof(string)) isDifferent = !((string)(value)).Equals((string) (val[inx]));
   if (t == typeof(double)) isDifferent = ((double)(value)) !=     ((double) (val[inx]));
   if (t == typeof(long))   isDifferent = ((long)(value))    !=     ((long)    (val[inx]));
   if (isDifferent)
   {
    if (!isDirty) {if (ori.Len == 0) ori = val.Clone(); isDirty = true; }
    val[inx] = value;
   }
   isDirty = checkDirty();
  }

  public bool     isDirty       = false;
  public bool     isInDb        = false;
  public bool     initializing  = true;

  protected abstract DbObj  Clone();
  protected void            initVal(int count) { val = new Pile<object>(count); map = new KeyPile<string, int>(); }

  public object this[int    inx]  { get { return getVal(inx);    } set { setVal(inx, value);     } }
  public object this[string key]  { get { return this[map[key]]; } set { this[map[key]] = value; } }

  //private Pile<DbObject> rows = new Pile<DbObject>();

  private DbObj clone(Pile<Reach> row) 
  {
   DbObj ret = Clone();
   ret.isInDb = true;
   ret.initializing = true;
   for (int i = 1; i <= row.Len; i++) 
   {
    Type typ = ret[i].GetType();
    if (typ == typeof(Reach))  { ret[i] = row[i]               ; continue; }
    if (typ == typeof(string)) { ret[i] = row[i].text          ; continue; }
    if (typ == typeof(long))   { ret[i] = long.Parse(row[i])   ; continue; }
    if (typ == typeof(double)) { ret[i] = double.Parse(row[i]) ; continue; }
    throw new Exception("Unknown Data Type in DbObj.clone()");
   }
   ret.initializing = false;
   return ret; 
  }

  //public Pile<DbObject> dbrGetAll() {Pile<DbObject> ret = new Pile<DbObject>(); foreach(Pile<Reach> row in db.exec(bscTbl.SELECT).Rows) ret.Push(row2Instance(row)); return ret; }

  public DbObj(DbGrid bscTbl, DbGrid vrbTbl)
  {
   init();
   //rows.Push(this);
   this.bscTbl      = bscTbl     ;
   this.vrbTbl      = vrbTbl     ;
   this.db          = bscTbl.Db  ;
   if (this.db == null) this.db = vrbTbl.Db;
   bscTbl.Db = this.db;
   vrbTbl.Db = this.db;
   isDirty = false;
  }
  
  public Pile<object> values
  {
   get
   {
    Pile<object> ret = new Pile<object>();
    for (int i = 1; ; i++) { try { if (this[i].GetType() == typeof(string)) ret.Push(utl.ds(false, (string)(this[i]))); else  ret.Push(this[i]); } catch (Exception e) { break; } }
    return ret;
   }
  }
  
  public string text()
  {
   string ret = "";
   int i = 0;
   try { while (true) ret += ", \"" + this[++i] + "\""; } catch { }
   return ret.Substring(2);
  }

  public bool commit()
  {
   bool ret = false;
   if (!isDirty) return ret;
   string colNames = ""; foreach(DbField f in bscTbl.Fields.from(2)) colNames += ", " + f.sql(db); colNames = colNames.Substring(2);
   if (isInDb)
    db.exec(bscTbl.sR(db.cd(bscTbl.Fields[1].sql(db)).EQ((long)(this[1]))).sC(colNames).UPD(values.from(2).array()));
   else
    {
     db.push(bscTbl.INS(bscTbl.sC("Max(id) + 1, " + Db.ds(values.from(2).array())).SLD));
     db.push(bscTbl.sR(db.cd("id").EQ(bscTbl.sC("Max(id)").SLD0)).SLD0); 
     Recs.Push(clone(db.exec()[1].Rows[1]));
     ret = true;
    }
   ori = val.Clone();
   isDirty = false;
   return ret;
  }

  public abstract void init();
  public DbSlc loadRecsSlc = null;
  
  public void reload()
  {
   initializing = true;
   init();
   recs.Clear();
   foreach (Pile<Reach> row in db.exec(loadRecsSlc).Rows) recs.Push(clone(row));
   initializing = false;
   isDirty = false;
  }

  public DbObj delete(params DbCnd[] cnd)
  {
   initializing = true;
   DbObj ret = Clone();
   DbDel d = bscTbl.sR(cnd).DEL;
   db.exec(d);
   ret.reload();
   initializing = false;
   isDirty = false;
   return ret;
  }
  
  public DbObj readBsc(long top, bool distinct, string order, params DbCnd[] cnd)
  {
   initializing = true;
   DbObj ret = Clone();
   DbSlc s = bscTbl.sR(cnd).SLC;
   if (top > -1) s = s.TOP(top);
   if (distinct) s = s.DST;
   if (order.Trim().Length > 0) s = s.ORD(order);
   ret.loadRecsSlc = s;
   ret.reload();
   initializing = false;
   isDirty = false;
   return ret;
  }

  public DbObj readVrb(long top, bool distinct, string order, params DbCnd[] cnd)
  {
   initializing = true;
   DbObj ret = Clone();
   DbSlc s = vrbTbl.sR(cnd).SLC;
   if (top > -1) s = s.TOP(top);
   if (distinct) s = s.DST;
   if (order.Trim().Length > 0) s = s.ORD(order);
   ret.loadRecsSlc = s;
   ret.reload();
   initializing = false;
   isDirty = false;
   return ret;
  }
 
 }



}








/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.xxdevplus.data;

import org.xxdevplus.struct.KeyPile;
import org.xxdevplus.struct.Pile;
import org.xxdevplus.utl.utl;
import org.xxdevplus.chain.Chain;

public abstract class DbObj
{
 //Copyright© 1996-2020 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties, You may use, modify, distribute this code under GNU public licence for NON-Commercial use as long as this notice remains unchanged.
 private static boolean selfTested  = false;  private static String ass(boolean expr) throws Exception { if (!expr) throw new Exception(ass(true) + " SelfTest Failure"); return "DbObj"; } private static void selfTest() { selfTested = true; }

 protected       DbJoin                     bscTbl    = null;
 protected       DbJoin                     vrbTbl    = null;
 public          Db                         db        = null;
 public          KeyPile<String, Integer>   map       = new KeyPile<String, Integer>();
 private         Pile<DbObj>                recs      = new Pile<DbObj>();
 public          Pile<DbObj>                Recs()    { return recs;} 

 private         Pile<Object>               val       = new Pile<Object>();
 private         Pile<Object>               ori       = new Pile<Object>();

 protected       Object                     getVal(int inx)                { return val.g(inx); }

 private         boolean                    checkDirty() throws Exception
 {
  if (!isDirty) return false;
  for (int inx = 1; inx <= val.Len(); inx++)
  {
   boolean isDifferent = false;
   Object item = val.g(inx);
   if (item instanceof Chain)  isDifferent = !((Chain)(ori.g(inx))).equals  ((Chain)(item));
   if (item instanceof String) isDifferent = !((String)(ori.g(inx))).equals ((String)(item));
   if (item instanceof Double) isDifferent =  ((Double)(ori.g(inx))) !=     ((Double)(item));
   if (item instanceof Long)   isDifferent =  ((Long)(ori.g(inx)))   !=     ((Long)(item));
   if (isDifferent) return true;
  }
  ori = new Pile<Object>();
  return false;
 }
  
 protected       void                  setVal(int inx, Object value) throws Exception  
 {
  if (initializing) { val.s(value, inx); isDirty = false; return; }
  boolean isDifferent = false;
  Object item = val.g(inx);
  if (item instanceof Chain)  isDifferent = !((Chain)(value)).equals  ((Chain)  (item));
  if (item instanceof String) isDifferent = !((String)(value)).equals ((String) (item));
  if (item instanceof Double) isDifferent =  ((Double)(value)) !=     ((Double) (item));
  if (item instanceof Long)   isDifferent =  ((Long)(value))   !=     ((Long)   (item));
  if (isDifferent)
  {
   if (!isDirty) {if (ori.Len() == 0) ori = val.Clone(); isDirty = true; }
   val.s(value, inx);
  }
  isDirty = checkDirty();
 }

 public boolean     isDirty       = false;
 public boolean     isInDb        = false;
 public boolean     initializing  = true;

 protected abstract DbObj  Clone() throws Exception;
 protected void            initVal(int count) { val = new Pile<Object>(count); map = new KeyPile<String, Integer>(); }

 public Object g(int    inx) throws Exception { return getVal(inx)       ;}  public void s(Object value, int    inx) throws Exception { setVal(inx, value); } 
 public Object g(String key) throws Exception { return this.g(map.g(key));}  public void s(Object value, String key) throws Exception { s(value, map.g(key));  } 

 //private Pack<DbObject> rows = new Pack<DbObject>();

 private DbObj clone(Pile<Chain> row) throws Exception 
 {
  DbObj ret = Clone();
  ret.isInDb = true;
  ret.initializing = true;
  for (int i = 1; i <= row.Len(); i++) 
  {
   Object item = ret.g(i);
   if (item instanceof Chain)  { ret.s(row.g(i), i)                            ; continue; }
   if (item instanceof String) { ret.s(row.g(i).text(), i)                     ; continue; }
   if (item instanceof Long)   { ret.s(Long.parseLong(row.g(i).text()), i)     ; continue; }
   if (item instanceof Double) { ret.s(Double.parseDouble(row.g(i).text()), i) ; continue; }
   throw new Exception("Unknown Data Type in DbObj.clone()");
  }
  ret.initializing = false;
  return ret; 
 }

 //public Pack<DbObject> dbrGetAll() {Pack<DbObject> ret = new Pack<DbObject>(); foreach(Pack<Reach> row in db.exec(bscTbl.SELECT).Rows) ret.Push(row2Instance(row)); return ret; }

 public DbObj(DbJoin bscTbl, DbJoin vrbTbl) throws Exception
 {
  init();
  //rows.Push(this);
  this.bscTbl      = bscTbl      ;
  this.vrbTbl      = vrbTbl      ;
  this.db          = bscTbl.Db() ;
  if (this.db == null) this.db = vrbTbl.Db();
  bscTbl.Db(this.db);
  vrbTbl.Db(this.db);
  isDirty = false;
 }
  
 public Pile<Object> values()
 {
  Pile<Object> ret = new Pile<Object>();
  for (int i = 1; ; i++) { try { if (this.g(i) instanceof String) ret.Push(utl.ds(false, (String)(this.g(i)))); else  ret.Push(this.g(i)); } catch (Exception e) { break; } }
  return ret;
 }
  
 public String text()
 {
  String ret = "";
  int i = 0;
  try { while (true) ret += ", \"" + this.g(++i) + "\""; } catch (Exception ex ){ }
  return ret.substring(2);
 }

 public boolean commit() throws Exception
 {
  boolean ret = false;
  if (!isDirty) return ret;
  String colNames = ""; for (DbField f : bscTbl.Fields().from(2)) colNames += ", " + f.sql(db); colNames = colNames.substring(2);
  if (isInDb) 
   db.exec(bscTbl.sR(db.cd(bscTbl.Fields().g(1).sql(db).text()).EQ((Long)(this.g(1)))).sC(colNames).UPD(Db.cloneObjectArray(values().from(2))));
  else
   {
    db.push(bscTbl.INS(bscTbl.sC("Max(id) + 1, " + Db.ds(Db.cloneObjectArray(values().from(2)))).SLD()));
    db.push(bscTbl.sR(db.cd("id").EQ(bscTbl.sC("Max(id)").SLD0())).SLD0()); 
    Recs().Push(clone(db.exec().g(1).Rows().g(1)));
    ret = true;
   }
  ori = val.Clone();
  isDirty = false;
  return ret;
 }

 public abstract void init() throws Exception;
  
 public DbSlc loadRecsSlc = null;
  
 public void reload() throws Exception
 {
  initializing = true;
  init();
  recs.Clear();
  for (Pile<Chain> row : db.exec(loadRecsSlc).all().Rows()) recs.Push(clone(row));
  initializing = false;
  isDirty = false;
 }

 public DbObj delete(DbCnd... cnd) throws Exception
 {
  initializing = true;
  DbObj ret = Clone();
  DbDel d = bscTbl.sR(cnd).DEL();
  db.exec(d);
  ret.reload();
  initializing = false;
  isDirty = false;
  return ret;
 }
  
 public DbObj readBsc(long top, boolean distinct, String order, DbCnd... cnd) throws Exception
 {
  initializing = true;
  DbObj ret = Clone();
  DbSlc s = bscTbl.sR(cnd).SLC();
  if (top > -1) s = s.TOP(top);
  if (distinct) s = s.DST();
  if (order.trim().length() > 0) s = s.ORD(order);
  ret.loadRecsSlc = s;
  ret.reload();
  initializing = false;
  isDirty = false;
  return ret;
 }

 public DbObj readVrb(long top, boolean distinct, String order, DbCnd... cnd) throws Exception
 {
  initializing = true;
  DbObj ret = Clone();
  DbSlc s = vrbTbl.sR(cnd).SLC();
  if (top > -1) s = s.TOP(top);
  if (distinct) s = s.DST();
  if (order.trim().length() > 0) s = s.ORD(order);
  ret.loadRecsSlc = s;
  ret.reload();
  initializing = false;
  isDirty = false;
  return ret;
 }
 
}









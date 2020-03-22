/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.xxdevplus.data;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.xxdevplus.struct.KeyPile;
import org.xxdevplus.utl.utl;

/**
 *
 * @author GeTr
 */


public class DbConnection
{

 protected Connection connection = null;
 
 public DbConnection(Db db, String connectString) throws Exception
 {
  String[] rest = new String[] {connectString};
  connectString = utl.cutl(rest, ";user=");
  String user = utl.cutl(rest, ";");
  if (rest[0].trim().length() > 0) rest[0] = connectString + ";" + rest[0]; else rest[0] = connectString;
  connectString = utl.cutl(rest, ";password=");
  String password = utl.cutl(rest, ";");
  if (rest[0].trim().length() > 0) rest[0] = connectString + ";" + rest[0]; else rest[0] = connectString;
  connectString = rest[0];

  if ( db.Dbtec().equals("jdbc") ) Class.forName(db.Drivers().g(db.Dbms())[1]);
  connection = DriverManager.getConnection(connectString, user, password);
 }

 void sConnectionTimeout(int value)
 {
  //TBD
 }

 int ConnectionTimeout()
 {
  //TBD
  return 0;
 }

 DbTransaction BeginTransaction (int IsolationLevel) throws Exception { return new DbTransaction(connection, IsolationLevel                            ); }
 DbTransaction BeginTransaction (                  ) throws Exception { return new DbTransaction(connection, Connection.TRANSACTION_READ_UNCOMMITTED   ); }

 void Open()
 {
  //connection.open();
 }

 void ChangeDatabase(String dbName) throws Exception
 {
  connection.setCatalog(dbName);
  //throw new UnsupportedOperationException("Not yet implemented");
 }


 JdbcCommand CreateCommand()
 {
  //TBD
  throw new UnsupportedOperationException("Not yet implemented");
 }

 void Dispose () throws Exception    { connection.close(); connection = null; }
 void Close   () throws Exception    { connection.close(); connection = null; }

 
}

























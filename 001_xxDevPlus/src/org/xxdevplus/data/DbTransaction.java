/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.xxdevplus.data;

import java.sql.Connection;

/**
 *
 * @author GeTr
 */


public class DbTransaction
{

 Connection connection = null;

 public      DbTransaction(Connection connection, int isolationLevel) throws Exception { this.connection = connection; connection.setAutoCommit(false); connection.setTransactionIsolation(isolationLevel); }

 public void Rollback()  throws Exception { connection.rollback(); connection.setTransactionIsolation(0); connection.setAutoCommit(true);    }

 public void Commit()    throws Exception
 {
  boolean auto = connection.getAutoCommit();
  connection.commit();
  try { connection.setTransactionIsolation(0); } catch (Exception e) {}
  connection.setAutoCommit(true);
 }

 public void Dispose()   { connection = null; }

}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.xxdevplus.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import org.xxdevplus.struct.KeyPile;
import org.xxdevplus.utl.utl;

/**
 *
 * @author GeTr
 */


public class OdbcConnection extends DbConnection
{
 public OdbcConnection(Db db, String connectString) throws Exception { super(db, "jdbc:odbc:" + connectString); }
}







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
public class OdbcTransaction extends DbTransaction
{
 public OdbcTransaction(Connection connection, int isolationLevel) throws Exception { super (connection, isolationLevel) ; }
}

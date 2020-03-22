/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.xxdevplus.data;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author GeTr
 */

public class JdbcCommand extends DbCommand
{
 public JdbcCommand(String sql, DbConnection connection) throws Exception { super(sql, connection); }
}

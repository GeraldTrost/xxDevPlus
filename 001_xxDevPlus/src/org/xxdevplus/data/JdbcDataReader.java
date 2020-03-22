/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.xxdevplus.data;

import java.sql.ResultSet;

/**
 *
 * @author GeTr
 */
public class JdbcDataReader extends DbDataReader
{
 public   JdbcDataReader   (ResultSet resultset)                   { super(resultset); }
}

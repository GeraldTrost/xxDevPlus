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


public class DbDataReader
{
 protected ResultSet        resultset = null;

 public    DbDataReader    (ResultSet resultset) { this.resultset = resultset; }

 String   GetDataTypeName  (int             inx) throws Exception  { return resultset.getMetaData().getColumnTypeName(inx + 1)     ; }
 String   GetName          (int             inx) throws Exception  { return resultset.getMetaData().getColumnName(inx + 1)         ; }
 boolean  Read             (                   ) throws Exception  
 { 
     try 
     {
     return resultset.next()                                       ; 
     }
     catch (Exception ex)
     {
         ex = ex;
         return false;
     }
 }
 
 void     Dispose          (                   ) throws Exception  {                                                                 } //TBD //resultset.close();
 void     Close            (                   ) throws Exception  { resultset.close()                                             ; }
 String   GetString        (int             inx) throws Exception  { return resultset.getString   (inx + 1)                        ; }
 long     GetInt64         (int             inx) throws Exception  { return resultset.getLong     (inx + 1)                        ; }
 double   GetDouble        (int             inx) throws Exception  { return resultset.getDouble   (inx + 1)                        ; }
 long     GetInt32         (int             inx) throws Exception  { return resultset.getInt      (inx + 1)                        ; }
 long     GetInt16         (int             inx) throws Exception  { return resultset.getInt      (inx + 1)                        ; }
 float    GetFloat         (int             inx) throws Exception  { return resultset.getFloat    (inx + 1)                        ; }
 

}




package org.xxdevplus.data;

import java.io.File;
import java.sql.ResultSet;
import org.xxdevplus.struct.KeyPile;
import org.xxdevplus.struct.Pile;
import org.xxdevplus.utl.utl;



public interface FtxProvider 
{
 
 public  KeyPile<String, Long> KnownFtxFiles   (long domId, String segment, char typ) throws Exception;
 public  KeyPile<String, Long> KnownFtAtoms    (char   typ) throws Exception;

 public long getFtAtom(String name, char typ) throws Exception;
 public long putFtAtom(String name, char typ) throws Exception;
 public long getFtFile(String name, long domId) throws Exception;
 public long putFtFile(String name, long domId) throws Exception;
 
 public KeyPile<String, Pile<Integer>> loadFtInx        (char typ,     long inBehalfOf, String file, String segment                                                         ) throws Exception;
 public                           void storeFtFileInx   (char typ,     String file, String segment, KeyPile<String, Pile<Integer>> positions, int blocksize, long... inBehalfOf) throws Exception;  
 public boolean                        tmpDatFileExists (String table                                                                                                       ) throws Exception;  
 
 public void ImportFtxDb (File folder) throws Exception;
 public void ExportFtxDb (File folder) throws Exception;

 
 
}



package org.xxdevplus.data;

import java.io.File;
import java.sql.ResultSet;
import org.xxdevplus.struct.KeyPile;
import org.xxdevplus.struct.Pile;
import org.xxdevplus.struct.Triple;
import org.xxdevplus.struct.Triples;
import org.xxdevplus.utl.utl;



public interface TripleStore
{
 // inBehalfOf = some mandantId or clientId or AppId or (in like in everest) some domainId

 public  KeyPile<String, Long> KnownTypes     (long domId) throws Exception;
 public  KeyPile<String, Long> KnownConcepts    (long domId) throws Exception;
 public  KeyPile<String, Long> KnownAttributes  (long domId) throws Exception;
 public  KeyPile<String, Long> KnownPredicates  (long domId) throws Exception;

 public long putDbClass(String name, long domId) throws Exception;
 public long putDbConcept(String name, long domId) throws Exception;
 public long putDbPredicate(String name, long domId) throws Exception;
 public long putDbAttribute(String name, long domId) throws Exception;
 
 public Triples Get   (Triple fact, long domId) throws Exception;
 public int     Count (Triple fact, long domId) throws Exception;

 public void    Add   (Triple        fact, long inBehalfOf) throws Exception;
 public void    Add   (Triples      facts, long inBehalfOf) throws Exception;

 public void    Set   (Triple        fact, long inBehalfOf) throws Exception;
 public void    Set   (Triples      facts, long inBehalfOf) throws Exception;

 public void    Put   (Triple        fact, long inBehalfOf) throws Exception;
 public void    Put   (Triples      facts, long inBehalfOf) throws Exception;

 public void    Del   (Triple        fact, long inBehalfOf) throws Exception;
 public void    Del   (Triples      facts, long inBehalfOf) throws Exception;

 public void ImportTdb (File folder) throws Exception;
 public void ExportTdb (File folder) throws Exception;

}

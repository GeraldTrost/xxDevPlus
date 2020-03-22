

package org.xxdevplus.data;

import org.xxdevplus.struct.KeyPile;
import org.xxdevplus.struct.Pile;
import org.xxdevplus.struct.Triple;



public interface TplStore
{
 // inBehalfOf = some mandantId or clientId AppId or (in like in everest) some domainId
 public void storeTriple  (Triple        fact, long inBehalfOf) throws Exception;
 public void storeTriples (Pile<Triple> facts, long inBehalfOf) throws Exception;

}

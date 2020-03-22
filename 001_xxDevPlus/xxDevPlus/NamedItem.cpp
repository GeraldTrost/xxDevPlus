/* 
 * File:   NamedItem.cpp
 * Author: GeTr
 * 
 * Created on 23. April 2010, 02:55
 */

#include "NamedItem.h"




 public nType getName() { return (nType) holder[0]; }
 public void setName(nType value) { holder[0] = value; }
 public iType getItem() { return (iType) holder[1]; }
 public void setItem(iType value) { holder[1] = value; }
 public NamedItem(nType name, iType item) { holder[0] = name; holder[1] = item; }
}

NamedItem::NamedItem()
{
}



NamedItem::NamedItem(const NamedItem& orig)
{
}


NamedItem::~NamedItem()
{
}


/* 
 * File:   NamedItem.h
 * Author: GeTr
 *
 * Created on 23. April 2010, 02:55
 */

#ifndef _NAMEDITEM_H
#define	_NAMEDITEM_H

#include <strings.h>;

template <class iType>
class NamedItem
{
 private:
  String n = "";

  object[] holder = new Object[2];

 public:
  NamedItem();
  NamedItem(const NamedItem& orig);
  virtual ~NamedItem();

};

#endif	/* _NAMEDITEM_H */




package org.xxdevplus.frmlng;

import org.xxdevplus.chain.Chain;
import org.xxdevplus.struct.Pile;
import org.xxdevplus.utl.utl;
import sun.security.util.Length;

public class LexNode 
{
 //** @author GeTr Copyright© 1996-2020 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties, You may use, modify, distribute this code under GNU public dual licence free for NON-Commercial use as long as this notice remains unchanged.
 //** @comment LexNode parses a textual Formula into a Tree
 static int _ic;String ass(boolean xp,String...msg)throws Exception{if(xp)return"LexNode"; throw new Exception(ass(true)+" slfTst:"+utl.str(msg));}private void slfTst()throws Exception{tstSimple();}protected void init()throws Exception{if(_ic++==0)slfTst();}
  
 void tstSimple() throws Exception
 {
 }

private LexLang              lng = null;
private LexNode           parent = null;
         Chain              type = null;
         Chain              name = null;
         Chain             value = null;
         Pile<LexNode>  children = new Pile<LexNode>();
 private Chain            parsed = Chain.Empty;
 private Chain           toParse = Chain.Empty;

 public static LexNode newNode(LexLang lng                          ) throws Exception {return new LexNode(lng,     Chain.Empty, Chain.Empty, Chain.Empty, null); }
 public static LexNode newNode(LexLang lng, String              type) throws Exception {return new LexNode(lng, new Chain(type), Chain.Empty, Chain.Empty, null); }
 public static LexNode newNode(LexLang lng, Chain               type) throws Exception {return new LexNode(lng,            type, Chain.Empty, Chain.Empty, null); }

 public        LexNode newNode(LexLang lng, Chain  type, Chain token) throws Exception {return new LexNode(lng,            type, Chain.Empty,       token, this); }
 
 public LexNode(LexLang lng, Chain type, Chain name, Chain value, LexNode parent) throws Exception {init(); this.lng = lng; this.parent = parent; this.type   = type; this.name   = name; this.value  = value;}

 /*
 private int spread(int depth)
 {
  int ret = 0;
  if ((depth == 1) || (children.Len() == 0)) return 1;
  for (LexNode n : children) ret += n.spread(depth -1);
  return ret;
 }
 private Pile<Pile<Chain>> matrix() throws Exception
 {
  Pile<Pile<Chain>> mx = new Pile<Pile<Chain>>();
  int cols = 0; int rows = 0;
  for (int r = 1; r <= 1000; r++) 
  {
   rows ++;
   int colsInRow = 0;
   if ((colsInRow = spread(r)) == cols) break;
   cols = colsInRow;
  }
  Pile<Chain> rw = new Pile<Chain>();
  for (int c = 1; c <= cols; c++) rw.Add(Chain.Empty);
  for (int r = 1; r <= cols; r++) mx.Add(rw.Clone());

  Pile<Chain> ret = new Pile<Chain>();
  //if (children.Len() == 0) return new Pile<Chain>( 0, new Chain("[" + type.text() + "] " + value.text()) );
  //for (LexNode n : children) ret.Push(n.asString());
  return mx;
 }
 /**/
 

 private void   done(Chain done, Chain todo) throws Exception {if (!todo.upto(done.len()).equals(done.text())) throw new Exception("invalid parsing prograss: done = '" + done.text() + "' out of '" + todo.text() + "'"); this.parsed = done; this.toParse = todo; }
 private void   done(Chain             done) throws Exception {done(done, toParse);}
 private void   todo(Chain             todo) throws Exception {done(todo.before(1), todo);}
 private String done(                      ) throws Exception {return (toParse.len() == 0) && (toParse.len() == 0) ? "" : parsed.text();}
 private String todo(                      ) throws Exception {return (toParse.len() == 0) && (toParse.len() == 0) ? "" : toParse.text();}
 
 /** short Description shtDpt(verbous, language)*/
 private Chain shtDpt      (boolean vrb                               ) throws Exception 
 {
  Chain pfx = Chain.Empty; 
  if (lng != null) try {for (Chain resTyp : lng.typOtrs.g(type.text()).rst) pfx = pfx.plus(resTyp).plus(" ");} catch (Exception ex) {ex = ex;} 
  return vrb ? pfx.plus(new Chain("⌠")).plus(type).plus(new Chain("⌡")).plus(value).plus("`").plus(parsed).plus("´").plus(toParse.after(parsed.len())) : pfx.plus(new Chain("⌠")).plus(type).plus(new Chain("⌡")).plus(value).plus(" ");
 }

 private Chain portionInRow(boolean vrb, int depth, int ancestorWidth) throws Exception
 {
  Chain ret    = Chain.Empty;
  Chain shDskp = shtDpt(vrb);
  if (children.Len() == 0) 
  {
   ret = ((parent != null) && (parent.children.Len() == 1)) ? shDskp.pad((1 + ancestorWidth + shDskp.len()) / 2, " ").pad(-ancestorWidth, " ") : shDskp;
   if (depth > 1) 
    ret = Chain.Empty.pad(ret.len(), " ");
  }
  else
  {
   for (LexNode c : children) ret = ret.plus(" ").plus(c.portionInRow(vrb, depth - 1, Math.max(ancestorWidth, shDskp.len())));
   ret = ret.from(2);
   if (depth == 1) 
    ret = shDskp.pad((1 + ret.len() + shDskp.len())/ 2, " ").pad(-ret.len(), " ");
  }
  if (depth < 1) ret = Chain.Empty.pad(ret.len(), " ");
  return ret;
 }

 @Override
 public String  toString   (                         )                  { try { return toString(true); } catch (Exception ex) {return "<ERR>";} }
 public String  toString   (boolean vrb              ) throws Exception { String ret = ""; int rw = 1; String text = portionInRow(vrb, rw, 0).text(); while (text.trim().length() > 0) {ret += "\n" + text; text = portionInRow(vrb, ++rw, 0).text(); } return (ret.length() < 2) ? "" : ret.substring(1); }
 
 private LexOptor otr()
 {
  LexOptor otr    = null;
  try { otr    = lng.typOtrs.g(    type.text()); } catch (Exception ex) {ex = ex;}
  return otr;
 }

 private int scenario(LexOptor otr, LexOptor resOtr)
 {
  int scenario = 0;
  scenario  = otr    == null ? -20 : 10 * otr.typ;
  scenario += resOtr == null ?  -2 : resOtr.typ;
  return scenario;
 }

 
 public boolean haveSpaceFor (   int newChildren) throws Exception 
 {
  LexOptor otr    = otr();
  if (otr == null) return (type.len() == 0);
  return ((otr.valency() == -1) || (otr.valency() >= children.Len() + newChildren));
 }
 
 public boolean isMergeable  (        LexNode res) throws Exception 
 {
  LexOptor otr    = otr();
  LexOptor resOtr = res.otr();
  if (!haveSpaceFor(res.children.Len())) return false;
  if (otr == null) return ((type.len() == 0) && (res.type.len() == 0));
  switch (scenario(otr, resOtr))
  {
   case -11:     // PexOtr + PexOtr
    return (type.equals(res.type));
   case   0:     // InxOtr + InxOtr
    return (type.equals(res.type));
   case  11:     // PoxOtr + PoxOtr
    return (type.equals(res.type));
  }      
  return false;
 }

 public boolean isInsertable  (        LexNode res) throws Exception 
 {
  LexOptor otr    = otr();
  LexOptor resOtr = res.otr();
  if (!haveSpaceFor(1)) 
   return false;
  if (otr == null) 
   return (type.len() == 0);
  if (type.len() == 0) throw new Exception("Invalid Operand: Operand is not LST but type = \"\"");
  switch (scenario(otr, resOtr))
  {
   case -22:     // opd    + opd
    break;
   case -21:     // opd    + PexOtr
    break;
   case -20:     // opd    + InxOtr
    break;
   case -19:     // opd    + PoxOtr
    break;
   case -18:     // opd    + SurOtr
       break;

   case -12:     // PexOtr + opd
    break;
   case -11:     // PexOtr + PexOtr
    break;
   case -10:     // PexOtr + InxOtr
    break;
   case  -9:     // PexOtr + PoxOtr
    break;
   case  -8:     // PexOtr + SurOtr
    break;

   case  -2:     // InxOtr + opd
    return otr.opdWelcomeAt(1, res.type.toString());
   case  -1:     // InxOtr + PexOtr
    break;
   case   0:     // InxOtr + InxOtr
    break;
   case   1:     // InxOtr + PoxOtr
    break;
   case   2:     // InxOtr + SurOtr
    break;

   case   8:     // PoxOtr + opd
    break;
   case   9:     // PoxOtr + PexOtr
    break;
   case  10:     // PoxOtr + InxOtr
    break;
   case  11:     // PoxOtr + PoxOtr
    break;
   case  12:     // PoxOtr + SurOtr
    break;

   case  18:     // SurOtr + opd
    return otr.opdWelcomeAt(1, res.type.toString());
   case  19:     // SurOtr + PexOtr
    break;
   case  20:     // SurOtr + InxOtr
    break;
   case  21:     // SurOtr + PoxOtr
    break;
   case  22:     // SurOtr + SurOtr
    break;
  }      
  return false;
 }

 public boolean isAddable  (        LexNode res) throws Exception 
 {
  LexOptor otr    = otr();
  LexOptor resOtr = res.otr();
  if (!haveSpaceFor(1)) 
   return false;
  if (otr == null) 
   return (type.len() == 0);
  if (type.len() == 0) throw new Exception("Invalid Operand: Operand is not LST but type = \"\"");
  switch (scenario(otr, resOtr))
  {
   case -22:     // opd    + opd
    break;
   case -21:     // opd    + PexOtr
    break;
   case -20:     // opd    + InxOtr
    break;
   case -19:     // opd    + PoxOtr
    break;
   case -18:     // opd    + SurOtr
       break;

   case -12:     // PexOtr + opd
    break;
   case -11:     // PexOtr + PexOtr
    break;
   case -10:     // PexOtr + InxOtr
    break;
   case  -9:     // PexOtr + PoxOtr
    break;
   case  -8:     // PexOtr + SurOtr
    break;

   case  -2:     // InxOtr + opd
    return otr.opdWelcomeAt(children.Len() + 1, res.type.toString());
   case  -1:     // InxOtr + PexOtr
    break;
   case   0:     // InxOtr + InxOtr
    break;
   case   1:     // InxOtr + PoxOtr
    break;
   case   2:     // InxOtr + SurOtr
    break;

   case   8:     // PoxOtr + opd
    break;
   case   9:     // PoxOtr + PexOtr
    break;
   case  10:     // PoxOtr + InxOtr
    break;
   case  11:     // PoxOtr + PoxOtr
    break;
   case  12:     // PoxOtr + SurOtr
    break;

   case  18:     // SurOtr + opd
    return otr.opdWelcomeAt(children.Len() + 1, res.type.toString());
   case  19:     // SurOtr + PexOtr
    break;
   case  20:     // SurOtr + InxOtr
    break;
   case  21:     // SurOtr + PoxOtr
    break;
   case  22:     // SurOtr + SurOtr
    break;
  }      
  return false;
 }
 
 
 public LexNode adopt    (               LexNode res) throws Exception 
 {
  res.parent = this; 
  children.Add(res); 
  return children.g(-1); 
 }
 
 public LexNode swallow  (               LexNode res) throws Exception 
 {
  type  = res.type; 
  name  = res.name; 
  value = res.value; 
  for (LexNode c : res.children) adopt(c); 
  return this; 
 }

 public LexNode addSibling (Chain newType, Chain newName, Chain newValue, LexNode newNode) throws Exception 
 {
  LexNode move = new LexNode(lng, type, name, value, parent); 
  for (LexNode c : children) move.adopt(c); 
  type  = newType;  name  = newName;  value = newValue; 
  children.Clear(); adopt(move); adopt(newNode); 
  return newNode; 
 } 
 
 private void fitIn(LexNode res) throws Exception
 {
  LexOptor otr         = otr();
  LexOptor resOtr      = res.otr();

  if (type.len() == 0) 
  switch (children.Len())
  {
   case 0:  swallow (res); break;
   default: adopt   (res); break;
  }
  else
   if (isMergeable(res))
    for (LexNode c : res.children) adopt(c);
   else
    if (isAddable(res))
     adopt(res);
    else
     if (isInsertable(res))
      res = res;
     else
  {
   if (type.len() == 0) 
    switch (children.Len())
    {
     case 0:  type  = res.type; name  = res.name; value = res.value; for (LexNode c : res.children) adopt(c); break;
     default: adopt(res); break;
    }
   else
   {
    switch (scenario(otr, resOtr))
    {
     case -22:     // opd    + opd
      if (otr == null) addSibling(Chain.Empty, Chain.Empty, Chain.Empty, res);
      else
       if ((otr.valency() == -1) || (children.Len() < otr.valency())) adopt(res); else addSibling(Chain.Empty, Chain.Empty, Chain.Empty, res);
      break;
     case -21:     // opd    + PexOtr
      break;
     case -20:     // opd    + InxOtr
      break;
     case -19:     // opd    + PoxOtr
      if (type.len() != 0)          throw new Exception("Postfix Operator " + res.type + " cannot be fitted into occupied Node");
      if (res.children.Len() != 0)  throw new Exception("Postfix Operator " + res.type + " cannot have childNodes before fitted in");
      type  = res.type; name  = res.name; value = res.value; 
      break;
     case -18:     // opd    + SurOtr


         break;

     case -12:     // PexOtr + opd
      break;
     case -11:     // PexOtr + PexOtr
      break;
     case -10:     // PexOtr + InxOtr
      break;
     case  -9:     // PexOtr + PoxOtr
      break;
     case  -8:     // PexOtr + SurOtr
      break;

     case  -2:     // InxOtr + opd
      break;
     case  -1:     // InxOtr + PexOtr
      break;
     case   0:     // InxOtr + InxOtr
      break;
     case   1:     // InxOtr + PoxOtr
      break;
     case   2:     // InxOtr + SurOtr
      break;

     case   8:     // PoxOtr + opd
      break;
     case   9:     // PoxOtr + PexOtr
      break;
     case  10:     // PoxOtr + InxOtr
      break;
     case  11:     // PoxOtr + PoxOtr
      break;
     case  12:     // PoxOtr + SurOtr
      break;

     case  18:     // SurOtr + opd
      if (type.len() == 0)          throw new Exception("Surround Operator " + res.type + " cannot be empty");
      if ((otr.valency() == -1) || (children.Len() < otr.valency())) 
       adopt(res); 
      else 
       addSibling(Chain.Empty, Chain.Empty, Chain.Empty, res);
      break;
     case  19:     // SurOtr + PexOtr
      break;
     case  20:     // SurOtr + InxOtr
      break;
     case  21:     // SurOtr + PoxOtr
      break;
     case  22:     // SurOtr + SurOtr
      break;
    }      
   }
  }
 }

 private Chain parseSurOtr(Chain todo, LexOptor otr) throws Exception
 {
  Chain bracketed = new Pick(otr.tok.g(1).text(), otr.tok.g(2).text(), "").avoid(Pick.sglSQots).host(Pick.sglSQots).grabOne().upon(true, todo).after(otr.tok.g(1).len()).before(-otr.tok.g(2).len());
  done(todo.upto(todo.after(bracketed).upto(1, otr.tok.g(2).text())));
  LexNode res = LexNode.newNode(lng, otr.sig).parse(bracketed);
  fitIn(res);
  return parsed;
 }

 private Chain parsePexOtr(Chain todo, Chain token, LexOptor otr) throws Exception
 {
  LexNode res = LexNode.newNode(lng, otr.sig).parse(todo.after(token));
  done(todo.upto(token).plus(todo.after(token).upto(res.done().length())));
  fitIn(res);
  return parsed;
 }

 private Chain parseInxOtr(Chain todo, Chain token, LexOptor otr) throws Exception
 {
  LexNode res = LexNode.newNode(lng, otr.sig).parse(todo.after(token));
  done(todo.upto(token).plus(todo.after(token).upto(res.done().length())));
  fitIn(res);
  return parsed;
 }

 private Chain parsePoxOtr(Chain todo, Chain token, LexOptor otr) throws Exception
 {
  LexNode res = LexNode.newNode(lng, otr.sig).parse(todo.after(token));
  done(todo.upto(token).plus(todo.after(token).upto(res.done().length())));
  fitIn(res);
  return parsed;
 }
 
 public LexNode parse(Chain fmla) throws Exception
 {
  todo(fmla);
  while (toParse.len() > 0)
  {
   Chain token = toParse.before(1, true, lng.otrSmbs()).before(1, " ").Trim(); 
   LexNode res = null;
   if ((token.len() > 0) && (token.firstWord(lng.Alphabet("SYM").text()).len() == token.len()))
   {
    token = token.Trim();
    boolean isNumeric = true; try { Double.parseDouble(token.text()); } catch (Exception ex) { isNumeric = false; }
    res = isNumeric ? newNode(lng, new Chain("NUM"), token) : newNode(lng, new Chain("SYM"), token);
    done(toParse.before(-toParse.after(token).Trim().len()));
    fitIn(res);                                                                                                                        // No Operator known so far, (still undef), maybe the type of operator will be found during further parsing ....
   }
   else
   {
    token = toParse.at(1, true, lng.otrSmbs()).Trim();
    LexOptor otr = lng.bTokOtrs.g(token.text());
    done(toParse.before(-toParse.after(token).Trim().len()));
    switch (otr.typ)   
    {
     case -1: { parsePexOtr(toParse, token, otr); break; }
     case  0: { parseInxOtr(toParse, token, otr); break; }
     case  1: { parsePoxOtr(toParse, token, otr); break; }
     case  2: { parseSurOtr(toParse, otr); break; }
     default: throw new Exception("Undefined Operator Type " + otr.typ);     
    }
   }
   //System.out.println(toString());
   todo(toParse.after(done().length()).Trim());
  }
  return this;
 }
  
}





//** @author  GeTr Copyright© 1996-2020 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties
//** @license You may use, modify, distribute this code under GNU public dual licence free for NON-Commercial use as long as this notice remains unchanged.
//** @comment Base Class for Chain 

package org.xxdevplus.chain;

import org.xxdevplus.chain.Restrict;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.xxdevplus.data.FtxProvider;
import org.xxdevplus.struct.KeyPile;
import org.xxdevplus.struct.MappedBuffer;
import org.xxdevplus.struct.Pile;



public class Chn implements InxObserver
{

 //public static Dictionary<string, KeyValuePair<int, TimeSpan>> stat = new Dictionary<string, KeyValuePair<int, TimeSpan>>();

 /*
  * l
  
  * ATTGETR

  * Counter Variables i and inx are 0-based Indexes while p and pos are 1-based Indexes!
 
   * Die Definition für schnelle Chain Klasse hat so gelautet:
  * Chain ist entweder compact oder compound. 
  *   compact Chain hat keine BlockList (bl == null) aber einen Mapped Buffer (mBuf != null) und sLbl, eLbl
  *   compund Chain BlockList (bl == Liste von capact Reaches) aber keinen Mapped Buffer (mBuf == null) und keinen sLbl, keinen eLbl
  
  * Die Definition für langsames Chain Klasse gelautet nun so:
  * Chain ist entweder compact oder compound. 
  *   compact Chain einen Mapped Buffer (mBuf != null) und sLbl, eLbl und in seiner Block List bl ist this als einziges kompaktes Chain eingetregen
  *   compund Chain BlockList (bl == Liste von capact Reaches, alle != this!) aber keinen Mapped Buffer (mBuf == null) und keinen sLbl, keinen eLbl
  * Der Unterschied in der Philosophie besteht darin, daß man sich in der slow Variante immer darauf verlassen kann, daß in der Block List ein Item
  * vorhanden ist, die interne Implementierung wird übersichtlicher und konsostenter, viele if-Verzweigungen entfallen dadurc, source wird lesbar.
  * Der Nachteil von slow Chain ist aber, daß es um 50% langsamer ist, sowohl im Exe als auch im Debugger (12 sec vs. 18 sec)
  * 
  * Das Memory Manegement von DOTNET ist wohl ein Hindernis, wenn man für jedes noch so winzige Chain immer einen Pack<Reach> mit einem einzigen Element
  * anlegen muß. Vielleicht kann man den Zeitverlust aber auch dadurch beheben, daß man die Klasse Pack optimiert und den Inhalt nicht in Array sondern 
  * in List einfügt ?
  * 
  * Vielleicht aber fallen auch unnötige Prozedurteile weg, wenn man am slow Chain weiterimplementiert sodaß letztlich beide gleich schnell sind,
  * das ist eine unbestätigte Hoffnung, ein Versuch. Die Messungen wurden in einer VM gemacht, die selbst wenig Memory hatte, vielleicht ist der Zeitunterschied
  * auf schnellen Rechnern belanglos. Das ist ebenfalls eine unbestätigte Hoffnung.
  * 
  * Die check Routine für das fast Chain ist hier auskommentiert:
  * 
 private void chk() //check if Chain is valid within the given Rules for constructing Reaches
 {
  if ((mBuf == null) && (bl == null)) throw new Exception("Invalid Chain: has neither Mapped Buffer nor has it a Block List");
  if ((mBuf != null) && (bl != null)) throw new Exception("Invalid Chain: has both Mapped Buffer and Block List");
  if (bl != null) foreach (Chain b in bl) if (!b.compact) throw new Exception("Invalid Chain: At least one Chain in Block List has its own Block List (is not compact)");
 }
 */

 // Check Routine für slow Chain:
 private void chk() throws Exception  //check if Chain is valid within the given Rules for constructing Reaches
 {
  if ((compact()) && (bl == null)) throw new Exception("Invalid Reach: compact Reach must contain itselb in Block List");
  if ((compact()) && bl.Len() != 1) throw new Exception("Invalid Reach: compact Reach must contain itselb in Block List");
  if ((compact()) && (bl.g(1) != this)) throw new Exception("Invalid Reach: compact Reach must contain itselb in Block List");
  if (bl != null) for( int i = 1; i <= bl.Len(); i++) if (!bl.g(i).compact()) throw new Exception("Invalid Reach: At least one Reach in Block List has its own Block List (is not compact)");
 }

 public static  String                            ftWordDelims;
 public         KeyPile<String, Pile<Integer>>    ftWinx (            ) { return mBuf.ftWinx; }  

 protected                                           Chn ( String text) throws Exception { this(text, null); }
 protected                                           Chn ( String text, KeyPile<String, Pile<Integer>> ftWinx) throws Exception
 {
 //MethWatch mw = new MethWatch("RchFdn.Rch_string");
  bl = new Pile<Chn>("", 0, this);
  mBuf = new MappedBuffer(text, Chain.useReachNotify);
  mBuf.ftWinx = ftWinx; 
  sLbl = "" + mBuf.sLabel(1);
  eLbl = "" + mBuf.eLabel(mBuf.Len());
  mBuf.subscribe(this);
  upd(); //inf = new int[bl.Count + 1, 3];
  //mw._void();
 }
          
 protected Chn(Chn source, Restrict rt) throws Exception
 {
  //super();
  //MethWatch mw = new MethWatch("RchFdn.Rch_Rch_Restrict");
  fitted = rt.fitted | source.fitted;
  bl = new Pile<Chn>(0, this);
  int[] sbl = new int[1]; int[] ebl = new int[1]; int[] sblPos = new int[1]; int[] eblPos = new int[1]; source.block4Pos(rt.sPos, rt.ePos, sbl, ebl, sblPos, eblPos);
  if ( rt.ePos    < rt.sPos ) { ebl[0] = sbl[0]; eblPos[0] = sblPos[0] - 1; }
  if ((rt.sWide) && (sblPos[0] <= 1)) for (int i = sbl[0] - 1; i > 0; i--) { if (source.bl_Len(i) > 0) break; sbl[0] = i; sblPos[0] = 1; }
  if ((rt.eWide) && (eblPos[0] >= source.bl_Len(ebl[0]))) for (int i = ebl[0] + 1; i <= source.bl.Len(); i++) { if (source.bl_Len(i) > 0) break; ebl[0] = i; eblPos[0] = 0; }
  if ( sbl[0] == ebl[0])
  {
   mBuf = source.bl.g(sbl[0]).mBuf;
   //int sInx = mBuf.sIndex(source.bl[sbl].sLbl); // UNBELIEVABLE!!!! This variation slows doen by 10% !!!!!  int sInx = source.bl_sInx(sbl);
   int sInx = source.bl_sInx(sbl[0]);
   sLbl = "" + mBuf.sLabel(sInx  - 1 + sblPos[0]);
   eLbl = "" + mBuf.eLabel(sInx  - 1 + eblPos[0]);
   mBuf.subscribe(this);
  }
  else
  {
   bl = source.bl.slice(sbl[0], ebl[0]);
   bl.s(source.bl.g(sbl[0]).from(sblPos[0]),  1);
   bl.s(source.bl.g(ebl[0]).upto(eblPos[0]), -1);
  }
  upd(); //inf = new int[bl.Count + 1, 3];
  //mw._void();
 }

 protected Chn(Chn first, Chn second) throws Exception //this special constructor is used to create a non-standard Chain with 1 compact Chain item <> this in Block List, used intemediately during operator+
 {
  //MethWatch mw = new MethWatch("RchFdn.Rch_first_second");
  bl = new Pile<Chn>(0);
  bl.Add(first.bl);
  bl.Add(second.bl);
  //bl = pieces();
  upd();
  fitted = (first.fitted || second.fitted);
  //mw._void();
 }

 protected Chn(MappedBuffer mBuf, String sLbl, String eLbl) throws Exception
 {
  bl = new Pile<Chn>("", 0, this);
  this.mBuf = mBuf;
  this.sLbl = sLbl;
  this.eLbl = eLbl;
  mBuf.subscribe(this);
  upd();
 }

 public String ftInxUpd = "";

 protected static Chain Load(String text, boolean lower, Chain ftSig, FtxProvider ftPvr, long... inBehalfOfs) throws Exception 
 {
  Pile<Long> domIds = new Pile<Long>(); for (long l : inBehalfOfs) domIds.Push(l);
  
  char   typ       = ftSig.before (1, "§").text().charAt(0);
  String fileName  = ftSig.after  (2, "§").text().trim();
  String segment   = ftSig.before (2, "§").after(1, "§").text().trim();

  if (ftPvr != null)
   for (int i = domIds.Len(); i >=1 ; i--)
   {
    if (ftPvr.KnownFtxFiles(domIds.g(i), segment, typ).hasKey(fileName)) 
    {
     System.out.println("ftWInx already indexed segment '" + segment + "' of File " +  fileName); //this segment of this cachefile is already indexed in this domain ...
     domIds.Del(i);
    } 
   }
         
  KeyPile<String, Pile<Integer>> ftWinx;
  ftWinx = 
   (ftPvr == null) 
   ? 
    new KeyPile<String, Pile<Integer>> () 
   : 
    ftPvr.loadFtInx(ftSig.before(1, "§").text().charAt(0), inBehalfOfs[0], ftSig.after(2, "§").text().trim(), ftSig.before(2, "§").after(1, "§").text().trim());
  
  Chain ret = ftWinx != null && ftWinx.Len() > 0 ? lower ? new Chain(text.toLowerCase()) : new Chain(text) : lower ? new Chain(text.toLowerCase()): new Chain(text);
  if (ftWinx == null) return ret; else ret.mBuf.ftWinx = ftWinx;
  if ((ftWinx.Len() > 0) && (inBehalfOfs.length > 1)) throw new Exception("Unable to Load Chain from ftx for multiple Domains(Multitenants)");
  
  
  if (ftWinx.Len() == 0)
  {
   ret.ftInxUpd = "w";
   int wordPos = 1;
   int dlmPos = 0;
   while (dlmPos < ((String)(ret.mBuf.buf(true))).length())
   {
    try
    {
     while (ftWordDelims.indexOf(((String)(ret.mBuf.buf(true))).charAt(wordPos - 1)) > -1) wordPos++;
    }
    catch (Exception ex)
    {
     break; // the next wordpos is past the string because the String has only worddelims at its end !
    }
    dlmPos = wordPos + 1;
    while (dlmPos <= ((String)(ret.mBuf.buf(true))).length()) if (ftWordDelims.indexOf(((String)(ret.mBuf.buf(true))).charAt(dlmPos - 1)) > -1) break; else dlmPos++;
    String word = ((String)(ret.mBuf.buf(false))).substring(wordPos - 1, dlmPos - 1).toLowerCase();
    if (ret.mBuf.ftWinx.hasKey(word)) 
     ret.mBuf.ftWinx.g(word).Push(wordPos); 
    else 
     ret.mBuf.ftWinx.Add(word, new Pile<Integer>(0, wordPos));     
    wordPos = ++dlmPos;
   }
   if (ftPvr != null) 
    ftPvr.storeFtFileInx
     (
      typ, 
      fileName, 
      segment, 
      ftWinx, ftPvr.tmpDatFileExists("ft_occur") ? 0 : 600, 
      domIds.longArray()
      ); // @param blocksize 2..2000 = multiline insert, >2000 = most probably crash postgres stack, 1 = single line inserts, 0 = (NIY) write to file for later "copy file to table ft_occur"
  }
  return ret;
 } 
 
 private static Chn fit(Chn first, Chn second) throws Exception //AttGeTr: tricky: this function always returns the first reach but when one of both is fitted it returns a fitted copy of the first reach
 {
  if ((first.fitted) || (!second.fitted)) return first;
  Chn ret = first.upto(first.len());
  ret.fitted = true;
  return ret;
 }

 private Chn     asFitted        (Chn c)      throws Exception {c.fitted = true; return c; }

 private Chn badImpl(Chn c)
 {
  // AttGeTr: sometimes resulting Reaches are "fitted" - I must research why it is.
  // the concept of "fitted Reaches" and "not fitted Reaches" SURELY MAKES A LOT OF SENSE
  // I remember that a resulting reach is "fitted" in case you calculate Chain("abc").from(17) - this gives a fitted empty Chain
  // "fitted" has the meaning similar to: "a recent calculation violated bounds and the Result had to be fitted to its bounds" ...
  // IMPORTANT: the badImpl() function is NOT meant for debuggig/investiation of the "fitted" feature !!!
  // IMPORTANT: some calculations in the Chn Class call badImpl() because of invalid implemention.
  //            the implemention WAS VALID BEFORE the string parameter token has been altered to string... tokens
  //            a valid implementation MUST be found and so you should debug with a breakpoint at the next line:
  return(c); //place your debuggin breakpoint here!
 }


 protected void upd() throws Exception
 {
  //if (inf != null) return; // this is a future option once MappedBuffer increments version after modification of buf we could selectively perform the update resulting in 10 percent performance gain.
  //chk(); //dbgchk!! not necessary in release build, saves 20 percent
  fitted = false;
  if (inf == null) inf = new int[bl.Len() + 1][3]; // inf contains [ bl.len, bl.ePos, bl.inx ] for i = 1 ... bl.Count. inf[0, x] reserved
   
  int lenSum = 0;
  for (int i = 1; i <= bl.Len(); i++)
  {
   if (this == bl.g(i)) 
   {
    inf[i][2] = sbdry(sLbl);
    inf[i][0] = ebdry(eLbl) - inf[i][2];
    inf[i][1] = inf[i][0];
    return;
   }
   inf[i][0] = bl.g(i).len();
   lenSum += inf[i][0];
   inf[i][1] = lenSum;
   inf[i][2] = bl.g(i).sbdry(bl.g(i).sLbl);
  }
 }

 protected void cloneFrom(Chn other) //only allowed if the other dies right after this call, example a.cloneFrom(b - c)
 {
  bl      = other.bl;
  if (bl.Len() == 1) bl.s(this, 1);
  mBuf    = other.mBuf;
  sLbl    = other.sLbl;
  eLbl    = other.eLbl;
  inf     = other.inf;
  fitted  = other.fitted;
 }

 protected Chn cloneDiff(Chn other)             throws Exception {cloneFrom(this.less(other)); return other; }
 protected Chn cloneDiff(Chn other, Chn skip)   throws Exception {cloneFrom(this.less(other).less(skip)); return other; }

 protected        int       stepPos     (int occur,    int stepLen)                   { if ((stepLen == 0) && (occur < 0)) return len() + 1; return ((int)(Math.signum(occur))) * stepLen; }
 protected static Chn       plus        (String first,  Chn second) throws Exception  { return (Chn) (new Chain(first).plus(second)); }
 protected        Chn       plus        (Chn second               ) throws Exception  { return new Chain((Chain) this, (Chain) second); }
 protected        Chn       plus        (String second            ) throws Exception  { return ((Chain)this).plus(second); }

 protected        Chn less(Chn second) throws Exception
 {
  Chn first = this;
  if (first.len() * second.len() == 0) return fit(first, second);
  if ((!first.compact()) || (!second.compact())) 
  {
   if (first.compact())
   {
    Chn ret = first;
    for (Chn s : second.bl) ret = ret.less(s);
    return ret;
   }
   else
   {
    Chn ret = fit(first.upto(0).plus(first.from(first.len() + 1)), second); // delFromLeftToRight strategy: Chn ret = fit(first.from(first.len + 1), second);
    for (Chn f : first.bl)
    {
     Chn diff = f.less(second);
     if (diff.len() > 0) if (ret.len() == 0) ret = diff; else ret = ret.plus(diff);
    }
    return ret;
   }
  }
  if (first.mBuf != second.mBuf) return fit(first, second);
  int sInx1 = first.sbdry(first.sLbl);
  int eInx1 = first.ebdry(first.eLbl) - 1;
  int sInx2 = second.sbdry(second.sLbl);
  int eInx2 = second.ebdry(second.eLbl) - 1;
  if ((eInx2 < sInx1) || (sInx2 > eInx1)) return fit(first, second);
  if ((sInx2 <= sInx1) && (eInx2 >= eInx1)) return fit(first.upto(0).plus(first.from(first.len() + 1)), second); // delFromLeftToRight strategy: fit(first.from(first.len + 1), second);
  if ((sInx2 > sInx1) && (eInx2 < eInx1)) return fit(first.upto(sInx2 - sInx1).plus(first.from(first.len() + 1 - (eInx1 - eInx2))), second);
  if (sInx2 <= sInx1) return fit(first.from(first.len() + 1 - (eInx1 - eInx2)), second);
  return fit(first.upto(sInx2 - sInx1), second);
 }

  // END OF RCHFDN.CS ------ START OF RCH.CS


 
 protected Chn at(int occur, Chn sq, int searchChar, Chn foundSoFar) throws Exception
 {
  Chn region  = (occur > 0) ? after(foundSoFar) : before(foundSoFar);
  if (occur > 0)
   if (region.before(1, sq.at(searchChar).text()).Trim().len() > 0) return foundSoFar; else return foundSoFar.plus(region.upto(1, sq.at(searchChar).text()));
  else
   if (region.after(-1, sq.at(searchChar).text()).Trim().len() > 0) return foundSoFar; else return region.from(-1, sq.at(searchChar).text()).plus(foundSoFar); 
 }

 protected Chn at(int occur, Chain sq) throws Exception
 {
  Chn region     = this;
  Chn foundSoFar = (occur > 0) ? region.at(1, sq.at(1).text()) : region.at(-1, sq.at(-1).text()); 
  while ((region.len() > 0) && (occur != 0))
  {
   foundSoFar = (occur > 0) ? region.at(1, sq.at(1).text()) : region.at(-1, sq.at(-1).text()); 
   if (foundSoFar.len() == 0) return foundSoFar;
   Chn oldFoundSoFar  = (occur > 0) ? region.before(1) : region.after(-1);  
   int i = (occur > 0) ? 1 : sq.len();
   while (((i > 1) || (occur > 0)) && ((i < sq.len() || (occur < 0))) && (foundSoFar.len() > oldFoundSoFar.len()))
   {
    oldFoundSoFar  = foundSoFar;
    foundSoFar     = (occur > 0) ? region.at(occur, sq, ++i, oldFoundSoFar) : region.at(occur, sq, --i, oldFoundSoFar);
   }
   region = (occur > 0) ? region.after(foundSoFar) : region.before(foundSoFar);
   if (((i == 1) ||(i == sq.len())) && (foundSoFar.len() > oldFoundSoFar.len())) 
    if (Math.abs(occur) == 1) 
     return foundSoFar; 
    else 
     return (occur > 0) ? region.at(--occur, sq) : region.at(++occur, sq);
  }
  return foundSoFar;
 }




// *******************     (          Chn other)

 
 protected Chn before      (          Chn other)                                throws Exception { return _before(other);}
 protected Chn before_     (          Chn other)                                throws Exception { return cloneDiff(before(other)); }
 protected Chn before__    (          Chn other)                                throws Exception { return cloneDiff(before(other), at(other)); }
 protected Chn insbefore   (ChnOp op, Chn other)                                throws Exception { return before(other).plus(op.sTxt).plus(from(other)); }
 protected Chn insbefore_  (ChnOp op, Chn other)                                throws Exception { return from(other).insbefore_(op, 1); }
 protected Chn insbefore   (Chn txt,  Chn other)                                throws Exception { return before(other).plus(txt).plus(from(other)); }
 protected Chn delbefore   (          Chn other)                                throws Exception { return from(other); }
 protected Chn delbefore_  (          Chn other)                                throws Exception { before(other).del_(); return this; }
 protected Chn delbefore   (ChnOp op, Chn other)                                throws Exception { return before(other).del(op.cnt).plus(from(other)); }
 protected Chn delbefore_  (ChnOp op, Chn other)                                throws Exception { before(other).del_(op.cnt); return this; }
 protected Chn rplbefore   (ChnOp op, Chn other)                                throws Exception { return new Chain(op.sTxt).plus(from(other)); }
 protected Chn rplbefore_  (ChnOp op, Chn other)                                throws Exception { return new Chain(op.sTxt).plus(from(other)); }
 protected Chn rplbefore   (Chn txt,  Chn other)                                throws Exception { return txt.plus(from(other)); }

 protected Chn upto        (          Chn other)                                throws Exception { return before(other).plus(at(other)); }
 protected Chn upto_       (          Chn other)                                throws Exception { return cloneDiff(upto(other)); }
 protected Chn upto__      (          Chn other)                                throws Exception { return cloneDiff(upto(other), at(other)); }
 protected Chn delupto     (          Chn other)                                throws Exception { return after(other); }
 protected Chn delupto_    (          Chn other)                                throws Exception { upto(other).del_(); return this; }
 protected Chn delupto     (ChnOp op, Chn other)                                throws Exception { return upto(other).del(op.cnt).plus(after(other)); }
 protected Chn delupto_    (ChnOp op, Chn other)                                throws Exception { upto(other).del_(op.cnt); return this; }
 protected Chn rplupto     (ChnOp op, Chn other)                                throws Exception { return new Chain(op.sTxt).plus(after(other)); }
 protected Chn rplupto_    (ChnOp op, Chn other)                                throws Exception { return new Chain(op.sTxt).plus(after(other)); }
 protected Chn rplupto     (Chn txt,  Chn other)                                throws Exception { return txt.plus(after(other)); }

 protected Chn at          (          Chn other)                                throws Exception { return this.less(this.less(other)); }
 protected Chn at_         (          Chn other)                                throws Exception { return cloneDiff(at(other)); }
 protected Chn at__        (          Chn other)                                throws Exception { return cloneDiff(at(other), at(other)); }
 protected Chn delat       (          Chn other)                                throws Exception { return before(other).plus(after(other)); }
 protected Chn delat_      (          Chn other)                                throws Exception { at(other).del_(); return this; }
 protected Chn delat       (ChnOp op, Chn other)                                throws Exception { return before(other).plus(at(other).del(op.cnt)).plus(from(other)); }
 protected Chn delat_      (ChnOp op, Chn other)                                throws Exception { at(other).del_(op.cnt); return this; }
 protected Chn rplat       (ChnOp op, Chn other)                                throws Exception { return before(other).plus(op.sTxt).plus(after(other)); }
 protected Chn rplat_      (ChnOp op, Chn other)                                throws Exception { return before(other).plus(op.sTxt).plus(after(other)); }
 protected Chn rplat       (Chn txt,  Chn other)                                throws Exception { return before(other).plus(txt).plus(after(other)); }

 protected Chn from        (          Chn other)                                throws Exception { return at(other).plus(after(other)); }
 protected Chn from_       (          Chn other)                                throws Exception { return cloneDiff(from(other)); }
 protected Chn from__      (          Chn other)                                throws Exception { return cloneDiff(from(other), at(other)); }
 protected Chn delfrom     (          Chn other)                                throws Exception { return before(other); }
 protected Chn delfrom_    (          Chn other)                                throws Exception { from(other).del_(); return this; }
 protected Chn delfrom     (ChnOp op, Chn other)                                throws Exception { return before(other).plus(from(other).del(op.cnt)); }
 protected Chn delfrom_    (ChnOp op, Chn other)                                throws Exception { from(other).del_(op.cnt); return this; }
 protected Chn rplfrom     (ChnOp op, Chn other)                                throws Exception { return before(other).plus(op.sTxt); }
 protected Chn rplfrom_    (ChnOp op, Chn other)                                throws Exception { return before(other).plus(op.sTxt); }
 protected Chn rplfrom     (Chn txt,  Chn other)                                throws Exception { return before(other).plus(txt); }
 
 protected Chn after       (          Chn other)                                throws Exception { return _after(other);}
 protected Chn after_      (          Chn other)                                throws Exception { return cloneDiff(after(other)); }
 protected Chn after__     (          Chn other)                                throws Exception { return cloneDiff(after(other), at(other)); }
 protected Chn insafter    (ChnOp op, Chn other)                                throws Exception { return upto(other).plus(op.sTxt).plus(after(other)); }
 protected Chn insafter_   (ChnOp op, Chn other)                                throws Exception { return upto(other).insafter_(op, -1); }
 protected Chn insafter    (Chn txt,  Chn other)                                throws Exception { return upto(other).plus(txt).plus(after(other)); }
 protected Chn delafter    (          Chn other)                                throws Exception { return upto(other); }
 protected Chn delafter_   (          Chn other)                                throws Exception { after(other).del_(); return this; }
 protected Chn delafter    (ChnOp op, Chn other)                                throws Exception { return upto(other).plus(after(other).del(op.cnt)); }
 protected Chn delafter_   (ChnOp op, Chn other)                                throws Exception { after(other).del_(op.cnt); return this; }
 protected Chn rplafter    (ChnOp op, Chn other)                                throws Exception { return upto(other).plus(op.sTxt); }
 protected Chn rplafter_   (ChnOp op, Chn other)                                throws Exception { return upto(other).plus(op.sTxt); }
 protected Chn rplafter    (Chn txt,  Chn other)                                throws Exception { return upto(other).plus(txt); }


// *******************     (          int pos)                                  //supports backward index (pos < 0) where -1 == last Element
 
 
 protected Chn before      (          int pos)                                  throws Exception { return new Chain((Chain) this, new Restrict(true, (Chain) this, 10011, 0, 0, pos - 1)); }
 protected Chn before_     (          int pos)                                  throws Exception { return cloneDiff(before(pos)); }
 protected Chn before__    (          int pos)                                  throws Exception { return cloneDiff(before(pos), at(pos)); }
 protected Chn insbefore   (ChnOp op, int pos)                                  throws Exception { return before(pos).plus(op.sTxt).plus(from(pos)); }
 protected Chn insbefore_  (ChnOp op, int pos)                                  throws Exception { return _insbefore_  (op, pos); }
 protected Chn insbefore   (Chn txt,  int pos)                                  throws Exception { return before(pos).plus(txt).plus(from(pos)); }
 protected Chn delbefore   (int pos)                                            throws Exception { return from(pos); }
 protected Chn delbefore_  (int pos)                                            throws Exception { before(pos).del_(); return this; }
 protected Chn delbefore   (ChnOp op, int pos)                                  throws Exception { return before(pos).del(op.cnt).plus(from(pos)); }
 protected Chn delbefore_  (ChnOp op, int pos)                                  throws Exception { before(pos).del_(op.cnt); return this; }
 protected Chn rplbefore   (ChnOp op, int pos)                                  throws Exception { return new Chain(op.sTxt).plus(from(pos)); }
 protected Chn rplbefore_  (ChnOp op, int pos)                                  throws Exception { return new Chain(op.sTxt).plus(from(pos)); }
 protected Chn rplbefore   (Chn txt,  int pos)                                  throws Exception { return txt.plus(from(pos)); }

 protected Chn upto        (          int pos)                                  throws Exception { return new Chain((Chain) this, new Restrict(true, (Chain) this, 10010, 0, 0, pos)); }
 protected Chn upto_       (          int pos)                                  throws Exception { return cloneDiff(upto(pos)); }
 protected Chn upto__      (          int pos)                                  throws Exception { return cloneDiff(upto(pos), at(pos)); }
 protected Chn delupto     (          int pos)                                  throws Exception { return after(pos); }
 protected Chn delupto_    (          int pos)                                  throws Exception { upto(pos).del_(); return this; }
 protected Chn delupto     (ChnOp op, int pos)                                  throws Exception { return upto(pos).del(op.cnt).plus(after(pos)); }
 protected Chn delupto_    (ChnOp op, int pos)                                  throws Exception { upto(pos).del_(op.cnt); return this; }
 protected Chn rplupto     (ChnOp op, int pos)                                  throws Exception { return new Chain(op.sTxt).plus(after(pos)); }
 protected Chn rplupto_    (ChnOp op, int pos)                                  throws Exception { return new Chain(op.sTxt).plus(after(pos)); }
 protected Chn rplupto     (Chn txt,  int pos)                                  throws Exception { return txt.plus(after(pos)); }

 protected Chn at          (          int pos)                                  throws Exception { return new Chain((Chain) this, new Restrict(true, (Chain) this, 1100, pos, 1, 0)); }
 protected Chn at_         (          int pos)                                  throws Exception { return cloneDiff(at(pos)); }
 protected Chn at__        (          int pos)                                  throws Exception { return cloneDiff(at(pos), at(pos)); }
 protected Chn delat       (          int pos)                                  throws Exception { return before(pos).plus(after(pos)); }
 protected Chn delat_      (          int pos)                                  throws Exception { at(pos).del_(); return this; }
 protected Chn delat       (ChnOp op, int pos)                                  throws Exception { return before(pos).plus(at(pos).del(op.cnt)).plus(after(pos)); }
 protected Chn delat_      (ChnOp op, int pos)                                  throws Exception { at(pos).del_(op.cnt); return this; }
 protected Chn rplat       (ChnOp op, int pos)                                  throws Exception { return before(pos).plus(op.sTxt).plus(after(pos)); }
 protected Chn rplat_      (ChnOp op, int pos)                                  throws Exception { return before(pos).plus(op.sTxt).plus(after(pos)); }
 protected Chn rplat       (Chn txt,  int pos)                                  throws Exception { return before(pos).plus(txt).plus(after(pos)); }

 protected Chn from        (          int pos)                                  throws Exception { return new Chain((Chain) this, new Restrict(false, (Chain) this, 1001, pos, 0, 0)); }
 protected Chn from_       (          int pos)                                  throws Exception { return cloneDiff(from(pos)); }
 protected Chn from__      (          int pos)                                  throws Exception { return cloneDiff(from(pos), at(pos)); }
 protected Chn delfrom     (          int pos)                                  throws Exception { return before(pos); }
 protected Chn delfrom_    (          int pos)                                  throws Exception { from(pos).del_(); return this; }
 protected Chn delfrom     (ChnOp op, int pos)                                  throws Exception { return before(pos).plus(from(pos).del(op.cnt)); }
 protected Chn delfrom_    (ChnOp op, int pos)                                  throws Exception { from(pos).del_(op.cnt); return this; }
 protected Chn rplfrom     (ChnOp op, int pos)                                  throws Exception { return before(pos).plus(op.sTxt); }
 protected Chn rplfrom_    (ChnOp op, int pos)                                  throws Exception { return before(pos).plus(op.sTxt); }
 protected Chn rplfrom     (Chn txt,  int pos)                                  throws Exception { return before(pos).plus(txt); }

 protected Chn after       (          int pos)                                  throws Exception { return new Chain((Chain) this, new Restrict(false,(Chain) this, 11001, pos + 1, 0, 0)); }
 protected Chn after_      (          int pos)                                  throws Exception { return cloneDiff(after(pos)); }
 protected Chn after__     (          int pos)                                  throws Exception { return cloneDiff(after(pos), at(pos)); }
 protected Chn insafter    (ChnOp op, int pos)                                  throws Exception { return upto(pos).plus(op.sTxt).plus(after(pos)); }
 protected Chn insafter_   (ChnOp op, int pos)                                  throws Exception { return _insafter_(op, pos);}
 protected Chn insafter    (Chn txt,  int pos)                                  throws Exception { return upto(pos).plus(txt).plus(after(pos)); }
 protected Chn delafter    (          int pos)                                  throws Exception { return upto(pos); }
 protected Chn delafter_   (          int pos)                                  throws Exception { after(pos).del_(); return this; }
 protected Chn delafter    (ChnOp op, int pos)                                  throws Exception { return upto(pos).plus(after(pos).del(op.cnt)); }
 protected Chn delafter_   (ChnOp op, int pos)                                  throws Exception { after(pos).del_(op.cnt); return this; }
 protected Chn rplafter    (ChnOp op, int pos)                                  throws Exception { return upto(pos).plus(op.sTxt); }
 protected Chn rplafter_   (ChnOp op, int pos)                                  throws Exception { return upto(pos).plus(op.sTxt); }
 protected Chn rplafter    (Chn txt,  int pos)                                  throws Exception { return upto(pos).plus(txt); }

  
// *******************     (          bool    match, int occur, String chrs)
 
 
 protected Chn before      (          boolean match, int occur, String chrs)    throws Exception { Chn ret = upto(match, occur, chrs); return ret.fitted ? ret : ret.upto(-2); }
 protected Chn before_     (          boolean match, int occur, String chrs)    throws Exception { return cloneDiff(before(match, occur, chrs)); }
 protected Chn before__    (          boolean match, int occur, String chrs)    throws Exception { return cloneDiff(before(match, occur, chrs), at(match, occur, chrs)); }
 protected Chn insbefore   (ChnOp op, boolean match, int occur, String chrs)    throws Exception { return before(match, occur, chrs).plus(op.sTxt).plus(from(match, occur, chrs)); }
 protected Chn insbefore_  (ChnOp op, boolean match, int occur, String chrs)    throws Exception { return from(match, occur, chrs).insbefore_(op, 1); }
 protected Chn insbefore   (Chn txt,  boolean match, int occur, String chrs)    throws Exception { return before(match, occur, chrs).plus(txt).plus(from(match, occur, chrs)); }
 protected Chn delbefore   (          boolean match, int occur, String chrs)    throws Exception { return from(match, occur, chrs); }
 protected Chn delbefore_  (          boolean match, int occur, String chrs)    throws Exception { before(match, occur, chrs).del_() ; return this; }
 protected Chn delbefore   (ChnOp op, boolean match, int occur, String chrs)    throws Exception { return before(match, occur, chrs).del(op.cnt).plus(from(match, occur, chrs)); }
 protected Chn delbefore_  (ChnOp op, boolean match, int occur, String chrs)    throws Exception { before(match, occur, chrs).del_(op.cnt) ; return this; }
 protected Chn rplbefore   (ChnOp op, boolean match, int occur, String chrs)    throws Exception { return new Chain(op.sTxt).plus(from(match, occur, chrs)); }
 protected Chn rplbefore_  (ChnOp op, boolean match, int occur, String chrs)    throws Exception { return new Chain(op.sTxt).plus(from(match, occur, chrs)); }
 protected Chn rplbefore   (Chn txt,  boolean match, int occur, String chrs)    throws Exception { return txt.plus(from(match, occur, chrs)); }

 protected Chn upto        (          boolean match, int occur, String chrs)    throws Exception { return _upto (true, match, occur, chrs);}
 protected Chn upto_       (          boolean match, int occur, String chrs)    throws Exception { return cloneDiff(upto(match, occur, chrs)); }
 protected Chn upto__      (          boolean match, int occur, String chrs)    throws Exception { return cloneDiff(upto(match, occur, chrs), at(match, occur, chrs)); }
 protected Chn delupto     (          boolean match, int occur, String chrs)    throws Exception { return after(match, occur, chrs); }
 protected Chn delupto_    (          boolean match, int occur, String chrs)    throws Exception { upto(match, occur, chrs).del_() ; return this; }
 protected Chn delupto     (ChnOp op, boolean match, int occur, String chrs)    throws Exception { return upto(match, occur, chrs).del(op.cnt).plus(after(match, occur, chrs)); }
 protected Chn delupto_    (ChnOp op, boolean match, int occur, String chrs)    throws Exception { upto(match, occur, chrs).del_(op.cnt) ; return this; }
 protected Chn rplupto     (ChnOp op, boolean match, int occur, String chrs)    throws Exception { return new Chain(op.sTxt).plus(after(match, occur, chrs)); }
 protected Chn rplupto_    (ChnOp op, boolean match, int occur, String chrs)    throws Exception { return new Chain(op.sTxt).plus(after(match, occur, chrs)); }
 protected Chn rplupto     (Chn txt,  boolean match, int occur, String chrs)    throws Exception { return txt.plus(after(match, occur, chrs)); }

 protected Chn at          (          boolean match, int occur, String chrs)    throws Exception { return (occur > 0) ? from(match, occur, chrs).upto(1): upto(match, occur, chrs).from(-1); }
 protected Chn at_         (          boolean match, int occur, String chrs)    throws Exception { return cloneDiff(at(match, occur, chrs)); }
 protected Chn at__        (          boolean match, int occur, String chrs)    throws Exception { return cloneDiff(at(match, occur, chrs), at(match, occur, chrs)); }
 protected Chn delat       (          boolean match, int occur, String chrs)    throws Exception { return before(match, occur, chrs).plus(after(match, occur, chrs)); }
 protected Chn delat_      (          boolean match, int occur, String chrs)    throws Exception { at(match, occur, chrs).del_(); return this; }
 protected Chn delat       (ChnOp op, boolean match, int occur, String chrs)    throws Exception { return before(match, occur, chrs).plus(at(match, occur, chrs).del(op.cnt)).plus(after(match, occur, chrs)); }
 protected Chn delat_      (ChnOp op, boolean match, int occur, String chrs)    throws Exception { at(match, occur, chrs).del_(op.cnt); return this; }
 protected Chn rplat       (ChnOp op, boolean match, int occur, String chrs)    throws Exception { return before(match, occur, chrs).plus(op.sTxt).plus(after(match, occur, chrs)); }
 protected Chn rplat_      (ChnOp op, boolean match, int occur, String chrs)    throws Exception { return before(match, occur, chrs).plus(op.sTxt).plus(after(match, occur, chrs)); }
 protected Chn rplat       (Chn txt,  boolean match, int occur, String chrs)    throws Exception { return before(match, occur, chrs).plus(txt).plus(after(match, occur, chrs)); }

 protected Chn from        (          boolean match, int occur, String chrs)    throws Exception { return _from(true, match, occur, chrs);}
 protected Chn from_       (          boolean match, int occur, String chrs)    throws Exception { return cloneDiff(from(match, occur, chrs)); }
 protected Chn from__      (          boolean match, int occur, String chrs)    throws Exception { return cloneDiff(from(match, occur, chrs), at(match, occur, chrs)); }
 protected Chn delfrom     (          boolean match, int occur, String chrs)    throws Exception { return before(match, occur, chrs); }
 protected Chn delfrom_    (          boolean match, int occur, String chrs)    throws Exception { from(match, occur, chrs).del_(); return this; }
 protected Chn delfrom     (ChnOp op, boolean match, int occur, String chrs)    throws Exception { return before(match, occur, chrs).plus(from(match, occur, chrs).del(op.cnt)); }
 protected Chn delfrom_    (ChnOp op, boolean match, int occur, String chrs)    throws Exception { from(match, occur, chrs).del_(op.cnt); return this; }
 protected Chn rplfrom     (ChnOp op, boolean match, int occur, String chrs)    throws Exception { return before(match, occur, chrs).plus(op.sTxt); }
 protected Chn rplfrom_    (ChnOp op, boolean match, int occur, String chrs)    throws Exception { return before(match, occur, chrs).plus(op.sTxt); }
 protected Chn rplfrom     (Chn txt,  boolean match, int occur, String chrs)    throws Exception { return before(match, occur, chrs).plus(txt); }

 protected Chn after       (          boolean match, int occur, String chrs)    throws Exception { Chn ret = from(match, occur, chrs); return ret.fitted ? ret : ret.from(2); }
 protected Chn after_      (          boolean match, int occur, String chrs)    throws Exception { return cloneDiff(after(match, occur, chrs)); }
 protected Chn after__     (          boolean match, int occur, String chrs)    throws Exception { return cloneDiff(after(match, occur, chrs), at(match, occur, chrs)); }
 protected Chn insafter    (ChnOp op, boolean match, int occur, String chrs)    throws Exception { return upto(match, occur, chrs).plus(op.sTxt).plus(after(match, occur, chrs)); }
 protected Chn insafter_   (ChnOp op, boolean match, int occur, String chrs)    throws Exception { return upto(match, occur, chrs).insafter_(op, -1); }
 protected Chn insafter    (Chn txt,  boolean match, int occur, String chrs)    throws Exception { return upto(match, occur, chrs).plus(txt).plus(after(match, occur, chrs)); }
 protected Chn delafter    (          boolean match, int occur, String chrs)    throws Exception { return upto(match, occur, chrs); }
 protected Chn delafter_   (          boolean match, int occur, String chrs)    throws Exception { after(match, occur, chrs).del_(); return this; }
 protected Chn delafter    (ChnOp op, boolean match, int occur, String chrs)    throws Exception { return upto(match, occur, chrs).plus(after(match, occur, chrs).del(op.cnt)); }
 protected Chn delafter_   (ChnOp op, boolean match, int occur, String chrs)    throws Exception { after(match, occur, chrs).del_(op.cnt); return this; }
 protected Chn rplafter    (ChnOp op, boolean match, int occur, String chrs)    throws Exception { return upto(match, occur, chrs).plus(op.sTxt); }
 protected Chn rplafter_   (ChnOp op, boolean match, int occur, String chrs)    throws Exception { return upto(match, occur, chrs).plus(op.sTxt); }
 protected Chn rplafter    (Chn txt,  boolean match, int occur, String chrs)    throws Exception { return upto(match, occur, chrs).plus(txt); }

 
// *******************     (          bool    match, int occur, String chrs)
 
  
 protected Chn Before      (          boolean match, int occur, String chrs)    throws Exception { Chn ret = Upto(match, occur, chrs); return ret.fitted ? ret : ret.upto(-2); }
 protected Chn Before_     (          boolean match, int occur, String chrs)    throws Exception { return cloneDiff(Before(match, occur, chrs)); }
 protected Chn Before__    (          boolean match, int occur, String chrs)    throws Exception { return cloneDiff(Before(match, occur, chrs), At(match, occur, chrs)); }
 protected Chn insBefore   (ChnOp op, boolean match, int occur, String chrs)    throws Exception { return Before(match, occur, chrs).plus(op.sTxt).plus(From(match, occur, chrs)); }
 protected Chn insBefore_  (ChnOp op, boolean match, int occur, String chrs)    throws Exception { return From(match, occur, chrs).insbefore_(op, 1); }
 protected Chn insBefore   (Chn txt,  boolean match, int occur, String chrs)    throws Exception { return Before(match, occur, chrs).plus(txt).plus(From(match, occur, chrs)); }
 protected Chn delBefore   (          boolean match, int occur, String chrs)    throws Exception { return From(match, occur, chrs); }
 protected Chn delBefore_  (          boolean match, int occur, String chrs)    throws Exception { Before(match, occur, chrs).del_(); return this; }
 protected Chn delBefore   (ChnOp op, boolean match, int occur, String chrs)    throws Exception { return Before(match, occur, chrs).del(op.cnt).plus(From(match, occur, chrs)); }
 protected Chn delBefore_  (ChnOp op, boolean match, int occur, String chrs)    throws Exception { Before(match, occur, chrs).del_(op.cnt); return this; }
 protected Chn rplBefore   (ChnOp op, boolean match, int occur, String chrs)    throws Exception { return new Chain(op.sTxt).plus(From(match, occur, chrs)); }
 protected Chn rplBefore_  (ChnOp op, boolean match, int occur, String chrs)    throws Exception { return new Chain(op.sTxt).plus(From(match, occur, chrs)); }
 protected Chn rplBefore   (Chn txt,  boolean match, int occur, String chrs)    throws Exception { return txt.plus(From(match, occur, chrs)); }

 protected Chn Upto        (          boolean match, int occur, String chrs)    throws Exception { return _upto(false, match, occur,  chrs) ; }
 protected Chn Upto_       (          boolean match, int occur, String chrs)    throws Exception { return cloneDiff(Upto(match, occur, chrs)); }
 protected Chn Upto__      (          boolean match, int occur, String chrs)    throws Exception { return cloneDiff(Upto(match, occur, chrs), At(match, occur, chrs)); }
 protected Chn delUpto     (          boolean match, int occur, String chrs)    throws Exception { return After(match, occur, chrs); }
 protected Chn delUpto_    (          boolean match, int occur, String chrs)    throws Exception { Upto(match, occur, chrs).del_(); return this; }
 protected Chn delUpto     (ChnOp op, boolean match, int occur, String chrs)    throws Exception { return Upto(match, occur, chrs).del(op.cnt).plus(After(match, occur, chrs)); }
 protected Chn delUpto_    (ChnOp op, boolean match, int occur, String chrs)    throws Exception { Upto(match, occur, chrs).del_(op.cnt); return this; }
 protected Chn rplUpto     (ChnOp op, boolean match, int occur, String chrs)    throws Exception { return new Chain(op.sTxt).plus(After(match, occur, chrs)); }
 protected Chn rplUpto_    (ChnOp op, boolean match, int occur, String chrs)    throws Exception { return new Chain(op.sTxt).plus(After(match, occur, chrs)); }
 protected Chn rplUpto     (Chn txt,  boolean match, int occur, String chrs)    throws Exception { return txt.plus(After(match, occur, chrs)); }

 protected Chn At          (          boolean match, int occur, String chrs)    throws Exception { return (occur > 0) ? From(match, occur, chrs).upto(1): Upto(match, occur, chrs).from(-1); }
 protected Chn At_         (          boolean match, int occur, String chrs)    throws Exception { return cloneDiff(At(match, occur, chrs)); }
 protected Chn At__        (          boolean match, int occur, String chrs)    throws Exception { return cloneDiff(At(match, occur, chrs), At(match, occur, chrs)); }
 protected Chn delAt       (          boolean match, int occur, String chrs)    throws Exception { return Before(match, occur, chrs).plus(After(match, occur, chrs)); }
 protected Chn delAt_      (          boolean match, int occur, String chrs)    throws Exception { At(match, occur, chrs).del_(); return this; }
 protected Chn delAt       (ChnOp op, boolean match, int occur, String chrs)    throws Exception { return Before(match, occur, chrs).plus(At(match, occur, chrs).del(op.cnt)).plus(After(match, occur, chrs)); }
 protected Chn delAt_      (ChnOp op, boolean match, int occur, String chrs)    throws Exception { At(match, occur, chrs).del_(op.cnt); return this; }
 protected Chn rplAt       (ChnOp op, boolean match, int occur, String chrs)    throws Exception { return Before(match, occur, chrs).plus(op.sTxt).plus(After(match, occur, chrs)); }
 protected Chn rplAt_      (ChnOp op, boolean match, int occur, String chrs)    throws Exception { return Before(match, occur, chrs).plus(op.sTxt).plus(After(match, occur, chrs)); }
 protected Chn rplAt       (Chn txt,  boolean match, int occur, String chrs)    throws Exception { return Before(match, occur, chrs).plus(txt).plus(After(match, occur, chrs)); }

 protected Chn From        (          boolean match, int occur, String chrs)    throws Exception { return _from (false, match, occur, chrs); }
 protected Chn From_       (          boolean match, int occur, String chrs)    throws Exception { return cloneDiff(From(match, occur, chrs));  }
 protected Chn From__      (          boolean match, int occur, String chrs)    throws Exception { return cloneDiff(From(match, occur, chrs), At(match, occur, chrs));  }
 protected Chn delFrom     (          boolean match, int occur, String chrs)    throws Exception { return Before(match, occur, chrs);}
 protected Chn delFrom_    (          boolean match, int occur, String chrs)    throws Exception { From(match, occur, chrs).del_(); return this; }
 protected Chn delFrom     (ChnOp op, boolean match, int occur, String chrs)    throws Exception { return Before(match, occur, chrs).plus(From(match, occur, chrs).del(op.cnt)); }
 protected Chn delFrom_    (ChnOp op, boolean match, int occur, String chrs)    throws Exception { From(match, occur, chrs).del_(op.cnt); return this; }
 protected Chn rplFrom     (ChnOp op, boolean match, int occur, String chrs)    throws Exception { return Before(match, occur, chrs).plus(op.sTxt); }
 protected Chn rplFrom_    (ChnOp op, boolean match, int occur, String chrs)    throws Exception { return Before(match, occur, chrs).plus(op.sTxt); }
 protected Chn rplFrom     (Chn txt,  boolean match, int occur, String chrs)    throws Exception { return Before(match, occur, chrs).plus(txt); }

 protected Chn After       (          boolean match, int occur, String chrs)    throws Exception { Chn ret = From(match, occur, chrs); return ret.fitted ? ret : ret.from(2); }
 protected Chn After_      (          boolean match, int occur, String chrs)    throws Exception { return cloneDiff(After(match, occur, chrs)); }
 protected Chn After__     (          boolean match, int occur, String chrs)    throws Exception { return cloneDiff(After(match, occur, chrs), At(match, occur, chrs)); }
 protected Chn insAfter    (ChnOp op, boolean match, int occur, String chrs)    throws Exception { return Upto(match, occur, chrs).plus(op.sTxt).plus(After(match, occur, chrs)); }
 protected Chn insAfter_   (ChnOp op, boolean match, int occur, String chrs)    throws Exception { return Upto(match, occur, chrs).insafter_(op, -1); }
 protected Chn insAfter    (Chn txt,  boolean match, int occur, String chrs)    throws Exception { return Upto(match, occur, chrs).plus(txt).plus(After(match, occur, chrs)); }
 protected Chn delAfter    (          boolean match, int occur, String chrs)    throws Exception { return Upto(match, occur, chrs); }
 protected Chn delAfter_   (          boolean match, int occur, String chrs)    throws Exception { After(match, occur, chrs).del_(); return this; }
 protected Chn delAfter    (ChnOp op, boolean match, int occur, String chrs)    throws Exception { return Upto(match, occur, chrs).plus(After(match, occur, chrs).del(op.cnt)); }
 protected Chn delAfter_   (ChnOp op, boolean match, int occur, String chrs)    throws Exception { After(match, occur, chrs).del_(op.cnt); return this; }
 protected Chn rplAfter    (ChnOp op, boolean match, int occur, String chrs)    throws Exception { return Upto(match, occur, chrs).plus(op.sTxt); }
 protected Chn rplAfter_   (ChnOp op, boolean match, int occur, String chrs)    throws Exception { return Upto(match, occur, chrs).plus(op.sTxt); }
 protected Chn rplAfter    (Chn txt,  boolean match, int occur, String chrs)    throws Exception { return Upto(match, occur, chrs).plus(txt); }

 
 // *******************    (          int occur, String token)
 
 
 protected Chn before      (          int occur, String token)              throws Exception { return upto(occur, token).less(from(occur, token)); }
 protected Chn before_     (          int occur, String token)              throws Exception { return cloneDiff(before(occur, token)); }
 protected Chn before__    (          int occur, String token)              throws Exception { return cloneDiff(before(occur, token), at(occur, token)); }
 protected Chn insbefore   (ChnOp op, int occur, String token)              throws Exception { return before(occur, token).plus(op.sTxt).plus(from(occur, token)); }
 protected Chn insbefore_  (ChnOp op, int occur, String token)              throws Exception { return from(occur, token).insbefore_(op, 1); }
 protected Chn insbefore   (Chn txt,  int occur, String token)              throws Exception { return before(occur, token).plus(txt).plus(from(occur, token)); }
 protected Chn delbefore   (          int occur, String token)              throws Exception { return from(occur, token); }
 protected Chn delbefore_  (          int occur, String token)              throws Exception { before(occur, token).del_(); return this; }
 protected Chn delbefore   (ChnOp op, int occur, String token)              throws Exception { return before(occur, token).del(op.cnt).plus(from(occur, token)); }
 protected Chn delbefore_  (ChnOp op, int occur, String token)              throws Exception { before(occur, token).del_(op.cnt); return this; }
 protected Chn rplbefore   (ChnOp op, int occur, String token)              throws Exception { return new Chain(op.sTxt).plus(from(occur, token)); }
 protected Chn rplbefore_  (ChnOp op, int occur, String token)              throws Exception { return new Chain(op.sTxt).plus(from(occur, token)); }
 protected Chn rplbefore   (Chn txt,  int occur, String token)              throws Exception { return txt.plus(from(occur, token)); }

 protected Chn upto        (          int occur, String token)              throws Exception { return _upto (true, occur, token); }
 protected Chn upto_       (          int occur, String token)              throws Exception { return cloneDiff(upto(occur, token)); }
 protected Chn upto__      (          int occur, String token)              throws Exception { return cloneDiff(upto(occur, token), at(occur, token)); }
 protected Chn delupto     (          int occur, String token)              throws Exception { return after(occur, token); }
 protected Chn delupto_    (          int occur, String token)              throws Exception { upto(occur, token).del_(); return this; }
 protected Chn delupto     (ChnOp op, int occur, String token)              throws Exception { return upto(occur, token).del(op.cnt).plus(after(occur, token)); }
 protected Chn delupto_    (ChnOp op, int occur, String token)              throws Exception { upto(occur, token).del_(op.cnt); return this; }
 protected Chn rplupto     (ChnOp op, int occur, String token)              throws Exception { return new Chain(op.sTxt).plus(after(occur, token)); }
 protected Chn rplupto_    (ChnOp op, int occur, String token)              throws Exception { return new Chain(op.sTxt).plus(after(occur, token)); }
 protected Chn rplupto     (Chn txt,  int occur, String token)              throws Exception { return txt.plus(after(occur, token)); }

 protected Chn at          (          int occur, String token)              throws Exception { return (occur > 0) ? from(occur, token).upto(stepPos(occur, token.length())) : upto(occur, token).from(stepPos(occur, token.length())); }
 protected Chn at_         (          int occur, String token)              throws Exception { return cloneDiff(at(occur, token)); }
 protected Chn at__        (          int occur, String token)              throws Exception { return cloneDiff(at(occur, token), at(occur, token)); }
 protected Chn delat       (          int occur, String token)              throws Exception { return before(occur, token).plus(after(occur, token)); }
 protected Chn delat_      (          int occur, String token)              throws Exception { at(occur, token).del_(); return this; }
 protected Chn delat       (ChnOp op, int occur, String token)              throws Exception { return before(occur, token).plus(at(occur, token).del(op.cnt)).plus(after(occur, token)); }
 protected Chn delat_      (ChnOp op, int occur, String token)              throws Exception { at(occur, token).del_(op.cnt); return this; }
 protected Chn rplat       (ChnOp op, int occur, String token)              throws Exception { return before(occur, token).plus(op.sTxt).plus(after(occur, token)); }
 protected Chn rplat_      (ChnOp op, int occur, String token)              throws Exception { return before(occur, token).plus(op.sTxt).plus(after(occur, token)); }
 protected Chn rplat       (Chn txt,  int occur, String token)              throws Exception { return before(occur, token).plus(txt).plus(after(occur, token)); }

 protected Chn from        (          int occur, String token)              throws Exception { return _from(true, occur, token);}
 protected Chn from_       (          int occur, String token)              throws Exception { return cloneDiff(from(occur, token)); }
 protected Chn from__      (          int occur, String token)              throws Exception { return cloneDiff(from(occur, token), at(occur, token)); }
 protected Chn delfrom     (          int occur, String token)              throws Exception { return before(occur, token); }
 protected Chn delfrom_    (          int occur, String token)              throws Exception { from(occur, token).del_(); return this; }
 protected Chn delfrom     (ChnOp op, int occur, String token)              throws Exception { return before(occur, token).plus(from(occur, token).del(op.cnt)); }
 protected Chn delfrom_    (ChnOp op, int occur, String token)              throws Exception { from(occur, token).del_(op.cnt); return this; }
 protected Chn rplfrom     (ChnOp op, int occur, String token)              throws Exception { return before(occur, token).plus(op.sTxt); }
 protected Chn rplfrom_    (ChnOp op, int occur, String token)              throws Exception { return before(occur, token).plus(op.sTxt); }
 protected Chn rplfrom     (Chn txt,  int occur, String token)              throws Exception { return before(occur, token).plus(txt); }

 protected Chn after       (          int occur, String token)              throws Exception { return from(occur, token).less(upto(occur, token)); }
 protected Chn after_      (          int occur, String token)              throws Exception { return cloneDiff(after(occur, token)); }
 protected Chn after__     (          int occur, String token)              throws Exception { return cloneDiff(after(occur, token), at(occur, token)); }
 protected Chn insafter    (ChnOp op, int occur, String token)              throws Exception { return upto(occur, token).plus(op.sTxt).plus(after(occur, token)); }
 protected Chn insafter_   (ChnOp op, int occur, String token)              throws Exception { return upto(occur, token).insafter_(op, -1); }
 protected Chn insafter    (Chn txt,  int occur, String token)              throws Exception { return upto(occur, token).plus(txt).plus(after(occur, token)); }
 protected Chn delafter    (          int occur, String token)              throws Exception { return upto(occur, token); }
 protected Chn delafter_   (          int occur, String token)              throws Exception { after(occur, token).del_(); return this; }
 protected Chn delafter    (ChnOp op, int occur, String token)              throws Exception { return upto(occur, token).plus(after(occur, token).del(op.cnt)); }
 protected Chn delafter_   (ChnOp op, int occur, String token)              throws Exception { after(occur, token).del_(op.cnt); return this; }
 protected Chn rplafter    (ChnOp op, int occur, String token)              throws Exception { return upto(occur, token).plus(op.sTxt); }
 protected Chn rplafter_   (ChnOp op, int occur, String token)              throws Exception { return upto(occur, token).plus(op.sTxt); }
 protected Chn rplafter    (Chn txt,  int occur, String token)              throws Exception { return upto(occur, token).plus(txt); }

  
 // *******************    (          int occur, String token)
 
 
 protected Chn Before      (          int occur, String token)              throws Exception { return Upto(occur, token).less(From(occur, token)); }
 protected Chn Before_     (          int occur, String token)              throws Exception { return cloneDiff(Before(occur, token)); }
 protected Chn Before__    (          int occur, String token)              throws Exception { return cloneDiff(Before(occur, token), At(occur, token)); }
 protected Chn insBefore   (ChnOp op, int occur, String token)              throws Exception { return Before(occur, token).plus(op.sTxt).plus(From(occur, token)); }
 protected Chn insBefore_  (ChnOp op, int occur, String token)              throws Exception { return From(occur, token).insbefore_(op, 1); }
 protected Chn insBefore   (Chn txt,  int occur, String token)              throws Exception { return Before(occur, token).plus(txt).plus(From(occur, token)); }
 protected Chn delBefore   (          int occur, String token)              throws Exception { return From(occur, token); }
 protected Chn delBefore_  (          int occur, String token)              throws Exception { Before(occur, token).del_(); return this; }
 protected Chn delBefore   (ChnOp op, int occur, String token)              throws Exception { return Before(occur, token).del(op.cnt).plus(From(occur, token)); }
 protected Chn delBefore_  (ChnOp op, int occur, String token)              throws Exception { Before(occur, token).del_(op.cnt); return this; }
 protected Chn rplBefore   (ChnOp op, int occur, String token)              throws Exception { return new Chain(op.sTxt).plus(From(occur, token)); }
 protected Chn rplBefore_  (ChnOp op, int occur, String token)              throws Exception { return new Chain(op.sTxt).plus(From(occur, token)); }
 protected Chn rplBefore   (Chn txt,  int occur, String token)              throws Exception { return txt.plus(From(occur, token)); }

 protected Chn Upto        (          int occur, String token)              throws Exception { return _upto(false, occur, token); }
 protected Chn Upto_       (          int occur, String token)              throws Exception { return cloneDiff(Upto(occur, token)); }
 protected Chn Upto__      (          int occur, String token)              throws Exception { return cloneDiff(Upto(occur, token), At(occur, token)); }
 protected Chn delUpto     (          int occur, String token)              throws Exception { return After(occur, token); }
 protected Chn delUpto_    (          int occur, String token)              throws Exception { Upto(occur, token).del_(); return this; }
 protected Chn delUpto     (ChnOp op, int occur, String token)              throws Exception { return Upto(occur, token).del(op.cnt).plus(After(occur, token)); }
 protected Chn delUpto_    (ChnOp op, int occur, String token)              throws Exception { Upto(occur, token).del_(op.cnt); return this; }
 protected Chn rplUpto     (ChnOp op, int occur, String token)              throws Exception { return new Chain(op.sTxt).plus(After(occur, token)); }
 protected Chn rplUpto_    (ChnOp op, int occur, String token)              throws Exception { return new Chain(op.sTxt).plus(After(occur, token)); }
 protected Chn rplUpto     (Chn txt,  int occur, String token)              throws Exception { return txt.plus(After(occur, token)); }

 protected Chn At          (          int occur, String token)              throws Exception 
 { 
  return (occur > 0) 
  
          ? 
          
          From(occur, token).upto(stepPos(occur, token.length())) 
          
          : 
          
          Upto(occur, token).from(stepPos(occur, token.length())); 
  
 }
 protected Chn At_         (          int occur, String token)              throws Exception { return cloneDiff(At(occur, token)); }
 protected Chn At__        (          int occur, String token)              throws Exception { return cloneDiff(At(occur, token), At(occur, token)); }
 protected Chn delAt       (          int occur, String token)              throws Exception { return Before(occur, token).plus(After(occur, token)); }
 protected Chn delAt_      (          int occur, String token)              throws Exception { At(occur, token).del_(); return this; }
 protected Chn delAt       (ChnOp op, int occur, String token)              throws Exception { return Before(occur, token).plus(At(occur, token).del(op.cnt)).plus(After(occur, token)); }
 protected Chn delAt_      (ChnOp op, int occur, String token)              throws Exception { At(occur, token).del_(op.cnt); return this; }
 protected Chn rplAt       (ChnOp op, int occur, String token)              throws Exception { return Before(occur, token).plus(op.sTxt).plus(After(occur, token)); }
 protected Chn rplAt_      (ChnOp op, int occur, String token)              throws Exception { return Before(occur, token).plus(op.sTxt).plus(After(occur, token)); }
 protected Chn rplAt       (Chn txt,  int occur, String token)              throws Exception { return Before(occur, token).plus(txt).plus(After(occur, token)); }

 protected Chn From        (          int occur, String token)              throws Exception { return _from(false, occur, token); }
 protected Chn From_       (          int occur, String token)              throws Exception { return cloneDiff(From(occur, token)); }
 protected Chn From__      (          int occur, String token)              throws Exception { return cloneDiff(From(occur, token), At(occur, token)); }
 protected Chn delFrom     (          int occur, String token)              throws Exception { return Before(occur, token); }
 protected Chn delFrom_    (          int occur, String token)              throws Exception { From(occur, token).del_(); return this; }
 protected Chn delFrom     (ChnOp op, int occur, String token)              throws Exception { return Before(occur, token).plus(From(occur, token).del(op.cnt)); }
 protected Chn delFrom_    (ChnOp op, int occur, String token)              throws Exception { From(occur, token).del_(op.cnt); return this; }
 protected Chn rplFrom     (ChnOp op, int occur, String token)              throws Exception { return Before(occur, token).plus(op.sTxt); }
 protected Chn rplFrom_    (ChnOp op, int occur, String token)              throws Exception { return Before(occur, token).plus(op.sTxt); }
 protected Chn rplFrom     (Chn txt,  int occur, String token)              throws Exception { return Before(occur, token).plus(txt); }

 protected Chn After       (          int occur, String token)              throws Exception { return From(occur, token).less(Upto(occur, token)); }
 protected Chn After_      (          int occur, String token)              throws Exception { return cloneDiff(After(occur, token)); }
 protected Chn After__     (          int occur, String token)              throws Exception { return cloneDiff(After(occur, token), at(occur, token)); }
 protected Chn insAfter    (ChnOp op, int occur, String token)              throws Exception { return Upto(occur, token).plus(op.sTxt).plus(After(occur, token)); }
 protected Chn insAfter_   (ChnOp op, int occur, String token)              throws Exception { return Upto(occur, token).insafter_(op, -1); }
 protected Chn insAfter    (Chn txt, int occur, String token)              throws Exception { return Upto(occur, token).plus(txt).plus(After(occur, token)); }
 protected Chn delAfter    (          int occur, String token)              throws Exception { return Upto(occur, token); }
 protected Chn delAfter_   (          int occur, String token)              throws Exception { After(occur, token).del_(); return this; }
 protected Chn delAfter    (ChnOp op, int occur, String token)              throws Exception { return Upto(occur, token).plus(After(occur, token).del(op.cnt)); }
 protected Chn delAfter_   (ChnOp op, int occur, String token)              throws Exception { After(occur, token).del_(op.cnt); return this; }
 protected Chn rplAfter    (ChnOp op, int occur, String token)              throws Exception { return Upto(occur, token).plus(op.sTxt); }
 protected Chn rplAfter_   (ChnOp op, int occur, String token)              throws Exception { return Upto(occur, token).plus(op.sTxt); }
 protected Chn rplAfter    (Chn txt,  int occur, String token)              throws Exception { return Upto(occur, token).plus(txt); }
 

 // *******************    (          int occur, bool prio, String... tokens)
 
 
 protected Chn before      (          int occur, boolean prio, String... tokens)              throws Exception { return upto(occur, prio, tokens).less(from(occur, prio, tokens)); }
 protected Chn before_     (          int occur, boolean prio, String... tokens)              throws Exception { return cloneDiff(before(occur, prio, tokens)); }
 protected Chn before__    (          int occur, boolean prio, String... tokens)              throws Exception { return cloneDiff(before(occur, prio, tokens), at(occur, prio, tokens)); }
 protected Chn insbefore   (ChnOp op, int occur, boolean prio, String... tokens)              throws Exception { return before(occur, prio, tokens).plus(op.sTxt).plus(from(occur, prio, tokens)); }
 protected Chn insbefore_  (ChnOp op, int occur, boolean prio, String... tokens)              throws Exception { return from(occur, prio, tokens).insbefore_(op, 1); }
 protected Chn insbefore   (Chn txt,  int occur, boolean prio, String... tokens)              throws Exception { return before(occur, prio, tokens).plus(txt).plus(from(occur, prio, tokens)); }
 protected Chn delbefore   (          int occur, boolean prio, String... tokens)              throws Exception { return from(occur, prio, tokens); }
 protected Chn delbefore_  (          int occur, boolean prio, String... tokens)              throws Exception { before(occur, prio, tokens).del_(); return this; }
 protected Chn delbefore   (ChnOp op, int occur, boolean prio, String... tokens)              throws Exception { return before(occur, prio, tokens).del(op.cnt).plus(from(occur, prio, tokens)); }
 protected Chn delbefore_  (ChnOp op, int occur, boolean prio, String... tokens)              throws Exception { before(occur, prio, tokens).del_(op.cnt); return this; }
 protected Chn rplbefore   (ChnOp op, int occur, boolean prio, String... tokens)              throws Exception { return new Chain(op.sTxt).plus(from(occur, prio, tokens)); }
 protected Chn rplbefore_  (ChnOp op, int occur, boolean prio, String... tokens)              throws Exception { return new Chain(op.sTxt).plus(from(occur, prio, tokens)); }
 protected Chn rplbefore   (Chn txt,  int occur, boolean prio, String... tokens)              throws Exception { return txt.plus(from(occur, prio, tokens)); }

 protected Chn upto        (          int occur, boolean prio, String... tokens)              throws Exception { return _upto (true, occur, prio, tokens); }
 protected Chn upto_       (          int occur, boolean prio, String... tokens)              throws Exception { return cloneDiff(upto(occur, prio, tokens)); }
 protected Chn upto__      (          int occur, boolean prio, String... tokens)              throws Exception { return cloneDiff(upto(occur, prio, tokens), at(occur, prio, tokens)); }
 protected Chn delupto     (          int occur, boolean prio, String... tokens)              throws Exception { return after(occur, prio, tokens); }
 protected Chn delupto_    (          int occur, boolean prio, String... tokens)              throws Exception { upto(occur, prio, tokens).del_(); return this; }
 protected Chn delupto     (ChnOp op, int occur, boolean prio, String... tokens)              throws Exception { return upto(occur, prio, tokens).del(op.cnt).plus(after(occur, prio, tokens)); }
 protected Chn delupto_    (ChnOp op, int occur, boolean prio, String... tokens)              throws Exception { upto(occur, prio, tokens).del_(op.cnt); return this; }
 protected Chn rplupto     (ChnOp op, int occur, boolean prio, String... tokens)              throws Exception { return new Chain(op.sTxt).plus(after(occur, prio, tokens)); }
 protected Chn rplupto_    (ChnOp op, int occur, boolean prio, String... tokens)              throws Exception { return new Chain(op.sTxt).plus(after(occur, prio, tokens)); }
 protected Chn rplupto     (Chn txt,  int occur, boolean prio, String... tokens)              throws Exception { return txt.plus(after(occur, prio, tokens)); }

 protected Chn at          (          int occur, boolean prio, String... tokens)              throws Exception { return _at(true, occur, prio, tokens); }
 protected Chn at_         (          int occur, boolean prio, String... tokens)              throws Exception { return cloneDiff(at(occur, prio, tokens)); }
 protected Chn at__        (          int occur, boolean prio, String... tokens)              throws Exception { return cloneDiff(at(occur, prio, tokens), at(occur, prio, tokens)); }
 protected Chn delat       (          int occur, boolean prio, String... tokens)              throws Exception { return before(occur, prio, tokens).plus(after(occur, prio, tokens)); }
 protected Chn delat_      (          int occur, boolean prio, String... tokens)              throws Exception { at(occur, prio, tokens).del_(); return this; }
 protected Chn delat       (ChnOp op, int occur, boolean prio, String... tokens)              throws Exception { return before(occur, prio, tokens).plus(at(occur, prio, tokens).del(op.cnt)).plus(after(occur, prio, tokens)); }
 protected Chn delat_      (ChnOp op, int occur, boolean prio, String... tokens)              throws Exception { at(occur, prio, tokens).del_(op.cnt); return this; }
 protected Chn rplat       (ChnOp op, int occur, boolean prio, String... tokens)              throws Exception { return before(occur, prio, tokens).plus(op.sTxt).plus(after(occur, prio, tokens)); }
 protected Chn rplat_      (ChnOp op, int occur, boolean prio, String... tokens)              throws Exception { return before(occur, prio, tokens).plus(op.sTxt).plus(after(occur, prio, tokens)); }
 protected Chn rplat       (Chn txt,  int occur, boolean prio, String... tokens)              throws Exception { return before(occur, prio, tokens).plus(txt).plus(after(occur, prio, tokens)); }

 protected Chn from        (          int occur, boolean prio, String... tokens)              throws Exception { return _from (true, occur, prio, tokens); }
 protected Chn from_       (          int occur, boolean prio, String... tokens)              throws Exception { return cloneDiff(from(occur, prio, tokens)); }
 protected Chn from__      (          int occur, boolean prio, String... tokens)              throws Exception { return cloneDiff(from(occur, prio, tokens), at(occur, prio, tokens)); }
 protected Chn delfrom     (          int occur, boolean prio, String... tokens)              throws Exception { return before(occur, prio, tokens); }
 protected Chn delfrom_    (          int occur, boolean prio, String... tokens)              throws Exception { from(occur, prio, tokens).del_(); return this; }
 protected Chn delfrom     (ChnOp op, int occur, boolean prio, String... tokens)              throws Exception { return before(occur, prio, tokens).plus(from(occur, prio, tokens).del(op.cnt)); }
 protected Chn delfrom_    (ChnOp op, int occur, boolean prio, String... tokens)              throws Exception { from(occur, prio, tokens).del_(op.cnt); return this; }
 protected Chn rplfrom     (ChnOp op, int occur, boolean prio, String... tokens)              throws Exception { return before(occur, prio, tokens).plus(op.sTxt); }
 protected Chn rplfrom_    (ChnOp op, int occur, boolean prio, String... tokens)              throws Exception { return before(occur, prio, tokens).plus(op.sTxt); }
 protected Chn rplfrom     (Chn txt,  int occur, boolean prio, String... tokens)              throws Exception { return before(occur, prio, tokens).plus(txt); }

 protected Chn after       (          int occur, boolean prio, String... tokens)              throws Exception { return from(occur, prio, tokens).less(upto(occur, prio, tokens)); }
 protected Chn after_      (          int occur, boolean prio, String... tokens)              throws Exception { return cloneDiff(after(occur, prio, tokens)); }
 protected Chn after__     (          int occur, boolean prio, String... tokens)              throws Exception { return cloneDiff(after(occur, prio, tokens), at(occur, prio, tokens)); }
 protected Chn insafter    (ChnOp op, int occur, boolean prio, String... tokens)              throws Exception { return upto(occur, prio, tokens).plus(op.sTxt).plus(after(occur, prio, tokens)); }
 protected Chn insafter_   (ChnOp op, int occur, boolean prio, String... tokens)              throws Exception { return upto(occur, prio, tokens).insafter_(op, -1); }
 protected Chn insafter    (Chn txt,  int occur, boolean prio, String... tokens)              throws Exception { return upto(occur, prio, tokens).plus(txt).plus(after(occur, prio, tokens)); }
 protected Chn delafter    (          int occur, boolean prio, String... tokens)              throws Exception { return upto(occur, prio, tokens); }
 protected Chn delafter_   (          int occur, boolean prio, String... tokens)              throws Exception { after(occur, prio, tokens).del_(); return this; }
 protected Chn delafter    (ChnOp op, int occur, boolean prio, String... tokens)              throws Exception { return upto(occur, prio, tokens).plus(after(occur, prio, tokens).del(op.cnt)); }
 protected Chn delafter_   (ChnOp op, int occur, boolean prio, String... tokens)              throws Exception { after(occur, prio, tokens).del_(op.cnt); return this; }
 protected Chn rplafter    (ChnOp op, int occur, boolean prio, String... tokens)              throws Exception { return upto(occur, prio, tokens).plus(op.sTxt); }
 protected Chn rplafter_   (ChnOp op, int occur, boolean prio, String... tokens)              throws Exception { return upto(occur, prio, tokens).plus(op.sTxt); }
 protected Chn rplafter    (Chn txt,  int occur, boolean prio, String... tokens)              throws Exception { return upto(occur, prio, tokens).plus(txt); }

 
 // *******************    (          int occur, bool prio, String... tokens)

 
 protected Chn Before      (          int occur, boolean prio, String... tokens)              throws Exception { return Upto(occur, prio, tokens).less(From(occur, prio, tokens)); }
 protected Chn Before_     (          int occur, boolean prio, String... tokens)              throws Exception { return cloneDiff(Before(occur, prio, tokens)); }
 protected Chn Before__    (          int occur, boolean prio, String... tokens)              throws Exception { return cloneDiff(Before(occur, prio, tokens), At(occur, prio, tokens)); }
 protected Chn insBefore   (ChnOp op, int occur, boolean prio, String... tokens)              throws Exception { return Before(occur, prio, tokens).plus(op.sTxt).plus(From(occur, prio, tokens)); }
 protected Chn insBefore_  (ChnOp op, int occur, boolean prio, String... tokens)              throws Exception { return From(occur, prio, tokens).insbefore_(op, 1); }
 protected Chn insBefore   (Chn txt,  int occur, boolean prio, String... tokens)              throws Exception { return Before(occur, prio, tokens).plus(txt).plus(From(occur, prio, tokens)); }
 protected Chn delBefore   (          int occur, boolean prio, String... tokens)              throws Exception { return From(occur, prio, tokens); }
 protected Chn delBefore_  (          int occur, boolean prio, String... tokens)              throws Exception { Before(occur, prio, tokens).del_(); return this; }
 protected Chn delBefore   (ChnOp op, int occur, boolean prio, String... tokens)              throws Exception { return Before(occur, prio, tokens).del(op.cnt).plus(From(occur, prio, tokens)); }
 protected Chn delBefore_  (ChnOp op, int occur, boolean prio, String... tokens)              throws Exception { Before(occur, prio, tokens).del_(op.cnt); return this; }
 protected Chn rplBefore   (ChnOp op, int occur, boolean prio, String... tokens)              throws Exception { return new Chain(op.sTxt).plus(From(occur, prio, tokens)); }
 protected Chn rplBefore_  (ChnOp op, int occur, boolean prio, String... tokens)              throws Exception { return new Chain(op.sTxt).plus(From(occur, prio, tokens)); }
 protected Chn rplBefore   (Chn txt,  int occur, boolean prio, String... tokens)              throws Exception { return txt.plus(From(occur, prio, tokens)); }

 protected Chn Upto        (          int occur, boolean prio, String... tokens)              throws Exception { return _upto (false, occur, prio, tokens);}
 protected Chn Upto_       (          int occur, boolean prio, String... tokens)              throws Exception { return cloneDiff(Upto(occur, prio, tokens)); }
 protected Chn Upto__      (          int occur, boolean prio, String... tokens)              throws Exception { return cloneDiff(Upto(occur, prio, tokens), At(occur, prio, tokens)); }
 protected Chn delUpto     (          int occur, boolean prio, String... tokens)              throws Exception { return After(occur, prio, tokens); }
 protected Chn delUpto_    (          int occur, boolean prio, String... tokens)              throws Exception { Upto(occur, prio, tokens).del_(); return this; }
 protected Chn delUpto     (ChnOp op, int occur, boolean prio, String... tokens)              throws Exception { return Upto(occur, prio, tokens).del(op.cnt).plus(After(occur, prio, tokens)); }
 protected Chn delUpto_    (ChnOp op, int occur, boolean prio, String... tokens)              throws Exception { Upto(occur, prio, tokens).del_(op.cnt); return this; }
 protected Chn rplUpto     (ChnOp op, int occur, boolean prio, String... tokens)              throws Exception { return new Chain(op.sTxt).plus(After(occur, prio, tokens)); }
 protected Chn rplUpto_    (ChnOp op, int occur, boolean prio, String... tokens)              throws Exception { return new Chain(op.sTxt).plus(After(occur, prio, tokens)); }
 protected Chn rplUpto     (Chn txt,  int occur, boolean prio, String... tokens)              throws Exception { return txt.plus(After(occur, prio, tokens)); }

 protected Chn At          (          int occur, boolean prio, String... tokens)              throws Exception { return _at (false, occur, prio, tokens); }
 protected Chn At_         (          int occur, boolean prio, String... tokens)              throws Exception { return cloneDiff(At(occur, prio, tokens)); }
 protected Chn At__        (          int occur, boolean prio, String... tokens)              throws Exception { return cloneDiff(At(occur, prio, tokens), At(occur, prio, tokens)); }
 protected Chn delAt       (          int occur, boolean prio, String... tokens)              throws Exception { return Before(occur, prio, tokens).plus(After(occur, prio, tokens)); }
 protected Chn delAt_      (          int occur, boolean prio, String... tokens)              throws Exception { At(occur, prio, tokens).del_(); return this; }
 protected Chn delAt       (ChnOp op, int occur, boolean prio, String... tokens)              throws Exception { return Before(occur, prio, tokens).plus(At(occur, prio, tokens).del(op.cnt)).plus(After(occur, prio, tokens)); }
 protected Chn delAt_      (ChnOp op, int occur, boolean prio, String... tokens)              throws Exception { At(occur, prio, tokens).del_(op.cnt); return this; }
 protected Chn rplAt       (ChnOp op, int occur, boolean prio, String... tokens)              throws Exception { return Before(occur, prio, tokens).plus(op.sTxt).plus(After(occur, prio, tokens)); }
 protected Chn rplAt_      (ChnOp op, int occur, boolean prio, String... tokens)              throws Exception { return Before(occur, prio, tokens).plus(op.sTxt).plus(After(occur, prio, tokens)); }
 protected Chn rplAt       (Chn txt,  int occur, boolean prio, String... tokens)              throws Exception { return Before(occur, prio, tokens).plus(txt).plus(After(occur, prio, tokens)); }

 protected Chn From        (          int occur, boolean prio, String... tokens)              throws Exception { return _from(false, occur, prio, tokens); }
 protected Chn From_       (          int occur, boolean prio, String... tokens)              throws Exception { return cloneDiff(From(occur, prio, tokens)); }
 protected Chn From__      (          int occur, boolean prio, String... tokens)              throws Exception { return cloneDiff(From(occur, prio, tokens), At(occur, prio, tokens)); }
 protected Chn delFrom     (          int occur, boolean prio, String... tokens)              throws Exception { return Before(occur, prio, tokens); }
 protected Chn delFrom_    (          int occur, boolean prio, String... tokens)              throws Exception { From(occur, prio, tokens).del_(); return this; }
 protected Chn delFrom     (ChnOp op, int occur, boolean prio, String... tokens)              throws Exception { return Before(occur, prio, tokens).plus(From(occur, prio, tokens).del(op.cnt)); }
 protected Chn delFrom_    (ChnOp op, int occur, boolean prio, String... tokens)              throws Exception { From(occur, prio, tokens).del_(op.cnt); return this; }
 protected Chn rplFrom     (ChnOp op, int occur, boolean prio, String... tokens)              throws Exception { return Before(occur, prio, tokens).plus(op.sTxt); }
 protected Chn rplFrom_    (ChnOp op, int occur, boolean prio, String... tokens)              throws Exception { return Before(occur, prio, tokens).plus(op.sTxt); }
 protected Chn rplFrom     (Chn txt,  int occur, boolean prio, String... tokens)              throws Exception { return Before(occur, prio, tokens).plus(txt); }

 protected Chn After       (          int occur, boolean prio, String... tokens)              throws Exception { return From(occur, prio, tokens).less(Upto(occur, prio, tokens)); }
 protected Chn After_      (          int occur, boolean prio, String... tokens)              throws Exception { return cloneDiff(After(occur, prio, tokens)); }
 protected Chn After__     (          int occur, boolean prio, String... tokens)              throws Exception { return cloneDiff(After(occur, prio, tokens), at(occur, prio, tokens)); }
 protected Chn insAfter    (ChnOp op, int occur, boolean prio, String... tokens)              throws Exception { return Upto(occur, prio, tokens).plus(op.sTxt).plus(After(occur, prio, tokens)); }
 protected Chn insAfter_   (ChnOp op, int occur, boolean prio, String... tokens)              throws Exception { return Upto(occur, prio, tokens).insafter_(op, -1); }
 protected Chn insAfter    (Chn txt,  int occur, boolean prio, String... tokens)              throws Exception { return Upto(occur, prio, tokens).plus(txt).plus(After(occur, prio, tokens)); }
 protected Chn delAfter    (          int occur, boolean prio, String... tokens)              throws Exception { return Upto(occur, prio, tokens); }
 protected Chn delAfter_   (          int occur, boolean prio, String... tokens)              throws Exception { After(occur, prio, tokens).del_(); return this; }
 protected Chn delAfter    (ChnOp op, int occur, boolean prio, String... tokens)              throws Exception { return Upto(occur, prio, tokens).plus(After(occur, prio, tokens).del(op.cnt)); }
 protected Chn delAfter_   (ChnOp op, int occur, boolean prio, String... tokens)              throws Exception { After(occur, prio, tokens).del_(op.cnt); return this; }
 protected Chn rplAfter    (ChnOp op, int occur, boolean prio, String... tokens)              throws Exception { return Upto(occur, prio, tokens).plus(op.sTxt); }
 protected Chn rplAfter_   (ChnOp op, int occur, boolean prio, String... tokens)              throws Exception { return Upto(occur, prio, tokens).plus(op.sTxt); }
 protected Chn rplAfter    (Chn txt,  int occur, boolean prio, String... tokens)              throws Exception { return Upto(occur, prio, tokens).plus(txt); }


 protected        String    dbgText     (                          ) throws Exception  { String ret = ""; for (Chn r : bl) ret += "«" + r.text() + "»"; return ret; }
 protected        Chn       lTrim       (                          ) throws Exception  { return after(firstWord(" ")); }
 protected        Chn       rTrim       (                          ) throws Exception  { return before(lastWord(" ")); }
 protected        Chn       Trim        (                          ) throws Exception  { return after(firstWord(" ")).before(lastWord(" ")); }
 
 protected boolean equals(Chn r) throws Exception
 {
  if (r == null) return false;
  if (r.len() != len()) return false;
  return r.equals(text());
 }

 protected boolean Equals(Chn r) throws Exception
 {
  if (r == null) return false;
  if (r.len() != len()) return false;
  return r.Equals(text());
 }

 protected boolean equals(String s) throws Exception
 {
  if (s == null) return (len() == 0);
  if (s.length() == 0) return (len() == 0);
  if (len() != s.length()) return false;
  if (this.charAt( 1, true) != s.charAt(0)) return false;
  if (this.charAt(-1, true) != s.charAt(s.length() - 1)) return false;
  //if (s.length() <= 3) return true; //AttGeTr: weiss nicht was ich mir dabei gedacht habe, jedenfalls ergab der vergleich (" = " eq " ~ ") true !!!
  return text().equals(s);
 }

 protected boolean Equals(String s) throws Exception
 {
  if (s == null) return (len() == 0);
  if (s.length() == 0) return (len() == 0);
  if (len() != s.length()) return false;
  s = s.toLowerCase();
  //if (this[1]) != s[0]) return false;
  //if (this[-1] != s[s.Length - 1]) return false;
  //if (s.Length <= 3) return true;
  return lText().equals(s);
 }

 protected int    count       (String ptn                )              throws Exception 
 {
  int ret = 0;
  Chn pattern = at(1, ptn);
  while (pattern.len() > 0) { ret++; pattern = after(pattern).at(1, ptn); }
  return ret; 
 }
  
 protected Chn    collapse    (String ptn                )              throws Exception 
 { 
  Chain ret = (Chain) this;
  ptn += ptn;
  Chn pattern = at(1, ptn);
  while (pattern.len() > 0) 
  { 
   ret = (Chain)ret.upto(pattern).before(-ptn.length()/2).plus(ret.after(pattern)); 
   pattern = ret.at(1, ptn);
  }
  return ret; 
 }

 protected Chn    skip        (String ptn                )              throws Exception 
 { 
  Chain ret = (Chain) this;
  Chn pattern = at(1, ptn);
  while (pattern.len() > 0) 
  { 
   ret = (Chain)ret.before(pattern).plus(ret.after(pattern)); 
   pattern = ret.at(1, ptn);
  }
  return ret; 
 }
 
 protected Chn    pad         (int len,       String fill)              throws Exception  
 {
  if (Math.abs(len) <= len()) return new Chain(text());
  Chn ret  = Chain.Empty;
  for (int i = 1; i <= 1 + (Math.abs(len) - len() / fill.length()); i++) ret = ret.plus(fill);
  return len > 0 ? ret.upto(Math.abs(len) - len()).plus(this) : this.plus(ret.from(len() - Math.abs(len)));
 }
 
 private   Chn firstWord        (String wordCharacters, boolean igCase)  throws Exception  { return before    (false                        ,  1,            wordCharacters); }
 protected Chn firstWord        (String wordCharacters)                  throws Exception  { return firstWord (wordCharacters               ,                         false); }
 protected Chn FirstWord        (String wordCharacters)                  throws Exception  { return firstWord (wordCharacters.toUpperCase() ,                          true); }
 private   Chn lastWord         (String wordCharacters, boolean igCase)  throws Exception  { return after     (false                        , -1,            wordCharacters); }
 protected Chn lastWord         (String wordCharacters)                  throws Exception  { return lastWord  (wordCharacters               ,                         false); }
 protected Chn LastWord         (String wordCharacters)                  throws Exception  { return lastWord  (wordCharacters.toUpperCase() ,                          true); }
 
 protected boolean startsWith   (String token)                           throws Exception  { return _startsWith   (true , token); }
 protected boolean StartsWith   (String token)                           throws Exception  { return _startsWith   (false, token); }
 protected boolean endsWith     (String token)                           throws Exception  { return _endsWith     (true,  token); }
 protected boolean EndsWith     (String token)                           throws Exception  { return _endsWith     (false, token); }

 protected boolean startsWith   (boolean match, String chars)            throws Exception  { return upto(1).upto  (-1, match, chars).len() > 0; }
 protected boolean StartsWith   (boolean match, String chars)            throws Exception  { return upto(1).Upto  (-1, match, chars).len() > 0; }
 protected boolean endsWith     (boolean match, String chars)            throws Exception  { return from(-1).from ( 1, match, chars).len() > 0; }
 protected boolean EndsWith     (boolean match, String chars)            throws Exception  { return from(-1).From ( 1, match, chars).len() > 0; }

 @Override
 public String toString (                                         )
 {
  try { return text(); }
  catch (Exception ex) { Logger.getLogger(Chn.class.getName()).log(Level.SEVERE, null, ex); }
  return "<n/a>";
 }





/****************************************************************************************************************************************************************************************/
/*              THE ACTUAL WORKER FUNCTIONS -- these functions access mBuf in loops and calculate Positions - SHOULD THEY BE IMPLEMENTED IN MappedBuffer for clearness ???              */
/****************************************************************************************************************************************************************************************/

 
 
 protected   Pile<Chn>        bl;   // BlockList is a List of constituents
 protected   int[][]         inf;   // INFormation on Parts (Blocks) 
 protected   String         sLbl;   // Start Label
 protected   String         eLbl;   // End Label
 protected   MappedBuffer   mBuf;   // Map on Buffer
 protected   boolean      fitted;   // valid if not fitted

 private     int          bl_Len    (int  blInx)                   {return (blInx < 0) ? inf[bl.Len() + 1 + blInx][0] : inf[blInx][0]; }
 private     int         bl_sPos    (int  blInx)                   {return bl_ePos(blInx) - bl_Len(blInx) + 1; }
 private     int         bl_ePos    (int  blInx)                   {return (blInx < 0) ? inf[bl.Len() + 1 + blInx][1] : inf[blInx][1]; }
 private     int         bl_sInx    (int  blInx)                   {return (blInx < 0) ? inf[bl.Len() + 1 + blInx][2] : inf[blInx][2]; }
 private     int         bl_eInx    (int  blInx)                   {return bl_sInx(blInx) + bl_Len(blInx) - 1; }

 //protected int          sIndex    (int    lbl)  throws Exception {return mBuf.sIndex(lbl); } //dbgchk!! not necessary in release build //private int sIndex(int lbl) { assCompact(); return mBuf.sIndex(lbl); }
 //protected int          eIndex    (int    lbl)  throws Exception {return mBuf.eIndex(lbl); } //dbgchk!! not necessary in release build //private int eIndex(int lbl) { assCompact(); return mBuf.eIndex(lbl); }
 protected   int           sbdry    (String lbl)  throws Exception {return mBuf.sbdry(lbl);  } //dbgchk!! not necessary in release build //private int sIndex(int lbl) { assCompact(); return mBuf.sIndex(lbl); }
 protected   int           ebdry    (String lbl)  throws Exception {return mBuf.ebdry(lbl);  } //dbgchk!! not necessary in release build //private int eIndex(int lbl) { assCompact(); return mBuf.eIndex(lbl); }

 private     long         sLabel    (int    inx)  throws Exception {return mBuf.sLabel(inx); } //dbgchk!! not necessary in release build //private int sLabel(int inx) { assCompact(); return mBuf.sLabel(inx); }
 private     long         eLabel    (int    inx)  throws Exception {return mBuf.sLabel(inx); } //dbgchk!! not necessary in release build //private int eLabel(int inx) { assCompact(); return mBuf.eLabel(inx); }
 private     boolean     compact    (          )                   {return (mBuf != null); }
 private     void     assCompact    (          )  throws Exception {if (!compact()) throw new Exception("Illegal Operation on a compound Chain"); }

 public void sIndexShift(int from, int amount) throws Exception
 {
  from = from + 1;
  if (sbdry(sLbl) - 1 < from) return;
  if (amount < 0) sLbl = "" + Math.max(sbdry(sLbl) - 1 + amount, from); else sLbl = sLbl  + amount;
  //upd();
 }

 public void eIndexShift(int from, int amount) throws Exception
 {
  from = from + 1;
  if (ebdry(eLbl) - 1 < from) return;
  if (amount < 0) eLbl = "" + Math.max(ebdry(eLbl) + amount, from - 2); else eLbl = eLbl + amount;
  //upd();
 }
 
 private Pile<Chn> pieces() throws Exception
 {
  //MethWatch mw = new MethWatch("RchFdn.pieces");
  Pile<Chn> ret = new Pile<Chn>(0);
  Pile<Chn> res = new Pile<Chn>(0);
  for (Chn r: bl) if (r.compact()) res.Add(r); else res.Add(r.pieces());
  int i = 1;
  while (i <= res.Len())
  {
   MappedBuffer _mBuf = res.g(i).mBuf;
   int _eInx = res.g(i).bl_eInx(1);
   int next_i = i + 1;
   for (int j = i + 1; j <= res.Len(); j++)
   {
    if (res.g(j).mBuf != _mBuf) break;
    if (res.g(j).bl_sInx(1) != _eInx + 1) break;
    _eInx = res.g(j).bl_eInx(1);
    next_i = j + 1;
   }
   if (next_i == i + 1) ret.Add(res.g(i)); else ret.Add((Chn) new Chain(res.g(i).mBuf, res.g(i).sLbl, res.g(next_i - 1).eLbl));
   i = next_i;
  }
  return ret; //return mw._Pile_Rch(ret);
 }

 private int[] block4Pos(int pos) throws Exception
 {
  //MethWatch mw = new MethWatch("RchFdn.block4Pos");
  if (pos == 0) return new int[] { 1, bl_sInx(1) - 1 }; //if (pos == 0) return mw._intA(new int[2] { 1, bl_sInx(1) - 1 });
  if ((pos < 1) || (pos > len())) throw new Exception("invalid Position within Chain");
  for (int i = 1; i <= bl.Len(); i++)
   if ((bl_sPos(i) <= pos) && (pos <= bl_ePos(i)))
    return new int[] { i, 1 + pos - bl_sPos(i) };  //for (int i = 1; i <= bl.Count; i++) if ((bl_sPos(i) <= pos) && (pos <= bl_ePos(i))) return mw._intA(new int[2] { i, 1 + pos - bl_sPos(i) });
  return new int[] { -1, -1 };  //return mw._intA(new int[2] { -1, -1 });
 }

 private void block4Pos(int sPos, int ePos, int[] sbl, int[] ebl, int[] sblPos, int[] eblPos)
 {
  //MethWatch mw = new MethWatch("RchFdn.block4Pos");
  if (sPos == 0) { sbl[0] = 1; sblPos[0] = bl_sInx(1) - 1; }
  if (ePos == 0) { ebl[0] = 1; eblPos[0] = bl_sInx(1) - 1; }
  if (sPos == len() + 1) { sbl[0] = bl.Len(); sblPos[0] = bl_Len(-1) + 1; }
  if (ePos == len() + 1) { ebl[0] = bl.Len(); eblPos[0] = bl_Len(-1) + 1; }
  //if ((sPos < 1) || (sPos > len) || (ePos < 1) || (ePos > len)) throw new Exception("invalid Position within Chain");
  for (int i = 1; i <= bl.Len(); i++) if ((bl_sPos(i) <= sPos) && (sPos <= bl_ePos(i))) { sbl[0]  = i; sblPos[0] = 1 + sPos - bl_sPos(i); }
  if (ePos == sPos) { ebl[0] = sbl[0]; eblPos[0] = sblPos[0]; return; }  //if (ePos == sPos) { ebl = sbl; eblPos = sblPos; mw._void(); return; }
  for (int i = sbl[0]; i <= bl.Len(); i++) if ((bl_sPos(i) <= ePos) && (ePos <= bl_ePos(i))) { ebl[0]  = i; eblPos[0] = 1 + ePos - bl_sPos(i); return; }  //for (int i = sbl; i <= bl.Count; i++) if ((bl_sPos(i) <= ePos) && (ePos <= bl_ePos(i))) { ebl  = i; eblPos = 1 + ePos - bl_sPos(i); mw._void(); return; }
  //mw._void();
 }

 private   int incr ( int   i )                  { return (i < 0) ? i - 1 : i + 1; }
 private   int decr ( int   i )                  { return (i < 0) ? i + 1 : i - 1; }

 private   Chn del  ( int cnt ) throws Exception { return (cnt < 0) ? upto(cnt - 1) : from(cnt + 1); }
 protected Chn del_ (         ) throws Exception { return del_(len()); }
 protected Chn del_ ( int cnt ) throws Exception
 {
  if (cnt == 0) return this;
  int[] sbl = new int[1]; int[] ebl = new int[1]; int[] sblPos = new int[1]; int[] eblPos = new int[1];
  if (cnt > 0)
  {
   if (cnt > len()) { cnt = len(); fitted = true; }
   block4Pos(1, cnt, sbl, ebl, sblPos, eblPos);
  }
  else
  {
   if (cnt < -len()) { cnt = -len(); fitted = true; }
   block4Pos(len() + 1 + cnt, len(), sbl, ebl, sblPos, eblPos);
  }
  if (sbl[0] == ebl[0])
   bl.g(sbl[0]).mBuf.delAfter(sblPos[0] - 1, Math.abs(cnt));
  else
  {
   ((Chain)bl.g(sbl[0])).DEL(bl.g(sbl[0]).len() - sblPos[0]).after(sblPos[0] - 1);
   for (int i = sbl[0] + 1; i < ebl[0]; i++) ((Chain)bl.g(i)).DEL(bl.g(i).len()).after(0);
   ((Chain)bl.g(ebl[0])).DEL(eblPos[0]).after(0);
  }
  return this;
 }
 
 private static long cc_uptofrom_char      = 0;  // token had only 1 char
 private static long cc_uptofrom_str       = 0;
 private static long cc_matchuptofrom_char = 0;  // token had only 1 char
 private static long cc_matchuptofrom_str  = 0;
 private static long cc_get                = 0;
 private KeyPile<Integer, Pile<Integer>> charInx = null;
 
 protected char charAt(int pos, boolean cSns) throws Exception
 {
  cc_get++;
  if (pos < 0) pos = 1 + len() + pos;
  int[] sbl = new int[1]; int[] ebl = new int[1]; int[] sblPos = new int[1]; int[] eblPos = new int[1]; block4Pos(pos, pos, sbl, ebl, sblPos, eblPos);
  return sbl[0] < 1 ? '\0' : bl.g(sbl[0]).mBuf.charAt(bl_sInx(sbl[0]) + sblPos[0] - 1, cSns) ;
 }

 protected int             len (                           )                  { return bl_ePos(-1); }    //public  int len { get { if (!compact) { int ret = 0; foreach (Chn r in bl) ret += r.len; return ret; } return eIndex(eLbl) - sIndex(sLbl) + 1; } }
 protected String         text (                           ) throws Exception { if (!compact()) { String ret = ""; for (Chn r : bl) ret += r.text();  return ret; } return mBuf.partText(sbdry(sLbl), ebdry(eLbl) - 1); }
 protected String        uText (                           ) throws Exception { return text().toUpperCase(); }
 protected String        lText (                           ) throws Exception { return text().toLowerCase(); }

 protected boolean _startsWith ( boolean cSns, String token) throws Exception { return cSns ? upto ( token.length()).text().equals(token) : upto( token.length()).lText().equals(token.toLowerCase());}
 protected boolean   _endsWith ( boolean cSns, String token) throws Exception { return cSns ? from (-token.length()).text().equals(token) : from(-token.length()).lText().equals(token.toLowerCase());}

 protected int        incident ( String ...            test) throws Exception
 {
  String text = text();
  for (int i = 1; i <= test.length; i++) if (test[i - 1].equals(text)) return i;
  return 0;
 }
 protected Chn            upon ( Chain                other) throws Exception { return other.less(other.before(this).plus(other.after(this))); }
 protected Chn              on ( Chain                other) throws Exception { return upon(other).less(this); }
 
 
 // ******************* (int occur, String ... sequences)

 
 protected Chn at(String skipChars, int occur, String ... seqPatterns) throws Exception
 {
  Chn ret = null;
  for (String sq : seqPatterns)
  {
   Chn foundSoFar = at(occur, new Chain(sq));
   if (foundSoFar.skip(" ").equals(sq)) return foundSoFar;
  }
  return (occur > 0) ? after(-1) : before(1);
 }
 
 
 // ******************* (Chn other)

 
 protected Chn _before (Chn other) throws Exception
 {
  Chn ret = this;
  MappedBuffer limitBuf = other.bl.g(1).mBuf;
  int limitInx = other.bl.g(1).sbdry(other.bl.g(1).sLbl);
  for (int p = 1; p <= bl.Len(); p++) if (bl.g(p).mBuf == limitBuf) if ((bl.g(p).sbdry(bl.g(p).sLbl) <= limitInx + 1) && (limitInx - 1 <= bl.g(p).ebdry(bl.g(p).eLbl) - 1)) return upto(bl_sPos(p) + limitInx - bl.g(p).sbdry(bl.g(p).sLbl) - 1);
  return ret;
 }
 
 protected Chn _after       (Chn other) throws Exception
 {
  Chn ret = from(len() + 1);
  MappedBuffer limitBuf = other.bl.g(-1).mBuf;
  int limitInx = other.bl.g(-1).ebdry(other.bl.g(-1).eLbl) - 1;
  for (int p = 1; p <= bl.Len(); p++) if (bl.g(p).mBuf == limitBuf) if ((bl.g(p).sbdry(bl.g(p).sLbl) <= limitInx + 1) && (limitInx - 1 <= bl.g(p).ebdry(bl.g(p).eLbl) - 1)) return from(bl_sPos(p) + limitInx - bl.g(p).sbdry(bl.g(p).sLbl) + 1);
  return ret;
 }


 // ******************* (int pos) //supported: backward index (eInx < 0) where -1 == last Element

 
 protected Chn _insbefore_  (ChnOp op, int pos) throws Exception
 {
  if (pos < 0) pos = len() + 1 + pos;
  if (op.sTxt.length() == 0) return this;
  int[] Bl = block4Pos(pos);
  Chn block = bl.g(Bl[0]);
  block.mBuf.insBefore(block.sbdry(block.sLbl) + Bl[1] - 1, op.sTxt);
  return this;
 }
 
  protected Chn _insafter_   (ChnOp op, int pos) throws Exception
 {
  if (pos < 0) pos = len() + 1 + pos;
  if (op.sTxt.length() == 0) return this;
  int[] sbl = new int[1]; int[] ebl = new int[1]; int[] sblPos = new int[1]; int[] eblPos = new int[1]; block4Pos(pos, pos, sbl, ebl, sblPos, eblPos);
  Chn block = bl.g(sbl[0]);
  block.mBuf.insAfter(block.sbdry(block.sLbl) + sblPos[0] - 1, op.sTxt);
  return this;
 }
  
 
 // ******************* (bool match, int occur, String chrs)

 
 protected Chn _upto        (boolean cSns, boolean match, int occur, String chrs) throws Exception   //symmetric implementation fromChr and uptpChr use each other in case the search - heading in inappropriate for one ob both
 {
  if (chrs.length() == 1) cc_matchuptofrom_char++; else cc_matchuptofrom_str++;
  if (occur > 0) { Chn diff = _from(cSns, match, occur, chrs); return (diff.fitted) ? this.less(diff) : this.less(diff.from(2)); }
  if (!cSns) chrs = chrs.toLowerCase();
  for (int p = len(); p >= 1; p--) if ((!match) ^ (chrs.indexOf(charAt(p, cSns)) > -1)) 
   return (occur == -1) ? 
    upto(p) 
   : 
    upto(p - 1)._upto(cSns, match, occur + 1, chrs);
  return asFitted(upto(0));
 }
 
 protected Chn _from        (boolean cSns, boolean match, int occur, String chrs) throws Exception   //symmetric implementation fromChr and uptpChr use each other in case the search - heading in inappropriate for one ob both
 {
  if (chrs.length() == 1) cc_matchuptofrom_char++; else cc_matchuptofrom_str++;
  if (occur < 0) { Chn diff = _upto(cSns, match, occur, chrs); return (diff.fitted) ? this.less(diff) : this.less(diff.upto(-2)); }
  for (int p = 1; p <= len(); p++) if ((!match) ^ (chrs.indexOf(charAt(p, cSns)) > -1)) 
   return (occur == 1) ? 
    from(p)
   : 
    from(p + 1)._from(cSns, match, occur - 1, chrs);
  return asFitted(from(len() + 1));
 }

 
 // ******************* (int occur, String token)

 
 protected Chn _upto        (boolean cSns,           int occur, String token)              throws Exception
 {
  if (token.length() == 0) return (occur > 0) ? upto(occur - 1): upto(len() + occur + 1);
  if (token.length() == 1) cc_uptofrom_char++; else cc_uptofrom_str++;
  if (occur > 0) { Chn diff = _from(cSns, occur, token); return (diff.fitted) ? this.less(diff) : this.less(diff.from(token.length() + 1)); }
  if (!cSns) token = token.toLowerCase();

  if ((mBuf == null) || (mBuf.ftWinx == null) || (ftWordDelims.indexOf(token.charAt(token.length() - 1)) > -1)) // no FulText Index available or ftWordIndex not applicable
  {
   for (int p = len(); p >= token.length(); p--) 
    if (token.charAt(token.length() - 1) == this.charAt(p, cSns)) 
     if (upto(p)._endsWith(cSns, token)) 
      return (occur == -1) ? 
       upto(p)
      : 
       before(p + 1 - token.length())._upto(cSns, occur + 1, token);
  }
  else
  {
   int ofs = ebdry(eLbl);
   int dlmPos = 0;
   String tok = ' ' + token;
   for (int j = tok.length(); j >= 1; j --) if (ftWordDelims.indexOf(tok.charAt(j - 1)) > -1) { dlmPos = j; break;}
   String lastWord = token.substring(dlmPos - 1, token.length());

   if (mBuf.ftWinx.hasKey(lastWord))
   {
    for (int i = mBuf.ftWinx.g(lastWord).Len(); i >= 1; i--)
    {
     int epos = mBuf.ftWinx.g(lastWord).g(i) +  lastWord.length() - 1;
     if ((epos <= len()) && ( epos + 1 - token.length() >= 1))
     {
      if (token.equals(((String)(mBuf.buf(cSns))).substring(epos - token.length(), epos)))
      {
       return (occur == -1) ? 
        upto(epos)
       : 
        before(epos)._upto(cSns, occur + 1, token);
      }
     }
    }
   }
  }
  return asFitted(upto(0));
 }

 
 protected Chn _from        (boolean cSns,           int occur, String token)              throws Exception
 {

  if (token.length() == 0) return (occur > 0) ? from(occur): from(len() + occur + 2);
  if (token.length() == 1) cc_uptofrom_char++; else cc_uptofrom_str++;
  if (occur < 0) { Chn diff = _upto(cSns, occur, token); return (diff.fitted) ? this.less(diff) : this.less(diff.upto(- token.length() - 1)); }
  if (!cSns) token = token.toLowerCase();

  if ((mBuf == null) || (mBuf.ftWinx == null) || (ftWordDelims.indexOf(token.charAt(0)) > - 1)) // no FulText Index available or ftWordIndex not applicable
  {
   for (int p = 1; p <= len() - token.length() + 1; p++) 
    if (token.charAt(0) == this.charAt(p, cSns)) 
     if (from(p)._startsWith(cSns, token)) 
      return (occur == 1) ? 
       from(p)
      : 
       after(p)._from(cSns, occur - 1, token);
  }
  else
  {
   int ofs = sbdry(sLbl);
   int dlmPos = 0;
   String tok = token + ' ';
   for (int j = 1; j <= tok.length(); j ++) if (ftWordDelims.indexOf(tok.charAt(j - 1)) > -1) { dlmPos = j; break;}
   String firstWord = token.substring(0, dlmPos - 1);
   if (mBuf.ftWinx.hasKey(firstWord))
   {
    for (int i = 1; i <= mBuf.ftWinx.g(firstWord).Len(); i++)
    {
     int spos = mBuf.ftWinx.g(firstWord).g(i) + 1 - ofs;
     if ((spos >= 1) && (spos + token.length() - 1 <= len()))
     {
      if 
       (token.equals(((String)(mBuf.buf(cSns))).substring(mBuf.ftWinx.g(firstWord).g(i) - 1, mBuf.ftWinx.g(firstWord).g(i) - 1 + token.length())) 
       && // issue "the" was found via ftWinx and "the guardian" matches - but the text contains "the guardians" --- so we better could text if the word "guardian" is in ftWinx at the right position rather than checking if after "the guardian" comes a delimiter ....
       ((spos + token.length() - 1 == len()) || ftWordDelims.indexOf(((String)(mBuf.buf(cSns))).charAt(mBuf.ftWinx.g(firstWord).g(i) - 1 + token.length())) > -1))
      {        
       return (occur == 1) ? 
        from(spos)
       : 
        after(spos)._from(cSns, occur - 1, token);
      }
     }
    }
   }
  }
  return asFitted(from(len() + 1));
 }

 
 // ******************* (int occur, bool prio, String... tokens)

 
 protected Chn _upto   (boolean cSns, int occur, boolean prio, String... tokens)              throws Exception
 {
  if (tokens.length == 1) return _upto(cSns, occur, tokens[0]);
  Chn ret = (occur > 0) ? upto(-1) : before(1); ret.fitted = true;
  if (prio) 
  { 
   String tok = ""; 
   for (String token : tokens) 
   { 
    Chn r = _upto(cSns, occur, token); 
    if (!r.fitted) 
     if (ret.fitted) { ret = r; tok = token; }
     else
     {
      if      (r.len() - token.length() == ret.len() - tok.length()) { if (token.length() > tok.length()) { tok = token; ret = r; } } 
      else if (((occur > 0) && (r.len() < ret.len())) || ((occur < 0) && (r.len() > ret.len()))) { tok = token; ret = r; }        
     }     
   } 
  }
  else
   switch (occur)
   {
    case 1:  { for (int i = 1; i <= tokens.length; i++) if (_at(cSns,  1, prio, tokens[i - 1]).len() > 0) { Chn r = _upto(cSns,  1, prio, tokens[i - 1]); if (r.len() < ret.len()) ret = r; } return ret; }
    case -1: { for (int i = 1; i <= tokens.length; i++) if (_at(cSns, -1, prio, tokens[i - 1]).len() > 0) { Chn r = _upto(cSns, -1, prio, tokens[i - 1]); if (r.len() > ret.len()) ret = r; } return ret; }
    default: { ret = (occur > 0) ? before(1) : upto(-1).plus(" "); if (occur > 0) for (int i = 1; i <= occur; i++) ret = ret.plus(after(ret)._upto(cSns, 1, prio, tokens)); else for (int i = -1; i >= occur; i--) ret = ret.upto(-2)._upto(cSns, -1, prio, tokens); } break;
   }
  return ret;
 }
 
 protected Chn _at (boolean cSns, int occur, boolean prio, String... tokens)              throws Exception 
 { 
  if (tokens.length == 1) return cSns ? at(occur, tokens[0]) : At(occur, tokens[0]); 
  return cSns ? this.less(before(occur, prio, tokens)).less(after(occur, prio, tokens)) : this.less(Before(occur, prio, tokens)).less(After(occur, prio, tokens)); 
 }
 
 protected Chn _from   (boolean cSns, int occur, boolean prio, String... tokens)              throws Exception
 {
  if (tokens.length == 1) return _from(cSns, occur, tokens[0]);
  Chn ret = (occur > 0) ? after(-1) : from(1); ret.fitted = true;
  if (prio) 
  { 
   String tok = ""; 
   for (String token : tokens) 
   { 
    Chn r = _from(cSns, occur, token);  
    if (!r.fitted) 
     if (ret.fitted) { ret = r; tok = token; }
     else
     {
      if      (r.len() == ret.len()) { if (token.length() > tok.length()) { tok = token; ret = r; } } 
      else if (((occur > 0) && (r.len() > ret.len())) || ((occur < 0) && (r.len() < ret.len()))) { tok = token; ret = r; } 
     }
   } 
  }
  else
   switch (occur)
   {
    case 1:  
    { 
     for (int i = 1; i <= tokens.length; i++) 
      if (_at(cSns,  1, prio, tokens[i - 1]).len() > 0) 
      { 
       Chn r = _from(cSns,  1, prio, tokens[i - 1]); 
       if (r.len() > ret.len()) ret = r; 
      } 
     return ret; }
    case -1: { for (int i = 1; i <= tokens.length; i++) if (_at(cSns, -1, prio, tokens[i - 1]).len() > 0) { Chn r = _from(cSns, -1, prio, tokens[i - 1]); if (r.len() < ret.len()) ret = r; } return ret; }
    default: { ret = (occur > 0) ? new Chain(" ").plus(from(1)) : after(-1); if (occur > 0) for (int i = 1; i <= occur; i++) ret = ret.from(2)._from(cSns, 1, prio, tokens); else for (int i = -1; i >= occur; i--) ret = this.less(ret)._from(cSns, -1, prio, tokens).plus(ret); }
    break;
   }
  return ret;
 }
 

}




//** @author  GeTr Copyright© 1996-2020 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties
//** @license You may use, modify, distribute this code under GNU public dual licence free for NON-Commercial use as long as this notice remains unchanged.
//** @comment General Utilitiy Functions



package org.xxdevplus.utl;

import org.xxdevplus.struct.Pile;
import org.xxdevplus.sys.Delegate;
import org.xxdevplus.sys.SystemTimer;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.net.URLDecoder;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import org.xxdevplus.chain.Chain;

public class utl //DocGetr: u is short for utilities, convention: class names in lower case indicate static classes
{
  //Copyright© 1996-2020 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties, You may use, modify, distribute this code under GNU public licence for NON-Commercial use as long as this notice remains unchanged.
 private static boolean selfTested  = false; private static String ass(boolean expr) throws Exception { if (!expr) throw new Exception(ass(true) + " SelfTest Failure"); return "utl"; } private static void selfTest(){selfTested=true;} private static void init(){ if (!selfTested) selfTest(); }
 
 public static SystemTimer SysTimer = new SystemTimer();
 public static Timestamp Now() { return new Timestamp(new Date().getTime()); }
 public static String crlf()   { return "\r\n"; } // if unix ,,,, testen     return "\n";   else ...
 
 public static Pile<File> allFilesRecursive(String directoryName) throws Exception            // Check mit GERALD!!!!
 {
  Pile<File> ret = new Pile<File>();
  for (File file : new File(directoryName).listFiles()) 
  if (file.isFile()) ret.Add(file); else ret.Add(allFilesRecursive(file.getAbsolutePath())); 
  return ret;
 }

 public static String webDomain     (String webUrl) throws Exception
 {
  Chain url = new Chain(webUrl);
  if (url.at(1, "//").len() > 0) return url.after(1, "//").before(1, "/").text();
  return url.before(1, "/").text();
 }
 
 public static void showModalJFrame(final JFrame window, final Frame owner, final JDialog dialog) 
{
 if (window == null) throw new IllegalArgumentException(); 
 window.setModalExclusionType(Dialog.ModalExclusionType.APPLICATION_EXCLUDE);
 window.setVisible(true);
 window.setAlwaysOnTop(true);
 //final JDialog hiddenDialogForModality = new JDialog(owner, true);
 final class MyWindowCloseListener extends WindowAdapter 
 {
  @Override
  public void windowClosed(final WindowEvent e) { window.dispose(); dialog.dispose(); }
 }
 final MyWindowCloseListener myWindowCloseListener = new MyWindowCloseListener();
 window.addWindowListener(myWindowCloseListener);
 
 /*
 final Dimension smallSize = new Dimension(80, 80);
 dialog.setMinimumSize(smallSize);
 dialog.setSize(smallSize);
 dialog.setMaximumSize(smallSize);
 dialog.setLocation(-smallSize.width * 2, -smallSize.height * 2);
 */
 
 dialog.setModal(true);
 dialog.setAlwaysOnTop(true);
 dialog.setVisible(true);
 dialog.setAlwaysOnTop(true);
 window.removeWindowListener(myWindowCloseListener);
}
 public static void throwIfDupRow     (Exception ex) throws Exception 
 { 
  if ( ex.getMessage().startsWith("ERROR: duplicate key value violates")) throw ex; 
 }

 public static void throwIfNotDupRow  (Exception ex) throws Exception 
 { 
  if (!ex.getMessage().startsWith("ERROR: duplicate key value violates")) throw ex; 
 }


 public static String ass(boolean expr , String module, String...msg) throws Exception { if (expr) return module; else throw new Exception(str(msg));}
 public static void ass(String  e1   , String e2, String...msg) throws Exception { ass(e1.equals(e2), msg); }
 public static void ass(double  e1   , double e2, String...msg) throws Exception { ass(e1 == e2     , msg); }
 public static void ass(int     e1   , int    e2, String...msg) throws Exception { ass(e1 == e2     , msg); }
 public static void ass(long    e1   , long   e2, String...msg) throws Exception { ass(e1 == e2     , msg); }
 public static void ass(Object  e1   , Object e2, String...msg) throws Exception { ass(e1 == e2     , msg); }

 public static String str(String...msg) {String ret = ""; for(String s:msg) {ret += "\r\n" + s; } return ret; }

  //Copmparators:
  public static String CPR(String key)
  {
   if (key.equals("IS"))       return " == ";
   if (key.equals("NOT_IS"))   return " !== ";
   if (key.equals("EQ"))       return " = ";
   if (key.equals("NOT_EQ"))   return " != ";
   if (key.equals("GT"))       return " > ";
   if (key.equals("LT"))       return " < ";
   if (key.equals("GE"))       return " >= ";
   if (key.equals("LE"))       return " <= ";
   if (key.equals("LIKE"))     return " ~ ";
   if (key.equals("NOT_LIKE")) return " !~ ";
   if (key.equals("IN"))       return " ° ";
   if (key.equals("NOT_IN"))   return " !° ";
   if (key.equals("MV"))       return " ≡ ";
   if (key.equals("NOT_MV"))   return " !≡ ";
   if (key.equals("MU"))       return " ≡> ";
   if (key.equals("NOT_MU"))   return " !≡> ";
   if (key.equals("ML"))       return " ≡< ";
   if (key.equals("NOT_ML"))   return " !≡< ";
   if (key.equals("XS"))       return " °° ";
   if (key.equals("NOT_XS"))   return " !°° ";
   return key.toUpperCase();  //return "";
  }

  //Operators:
  public static String OPR(String key)
  {
   if (key.equals("APD"))  return " _ ";         // Append Operation
   if (key.equals("CPF"))  return "   ";         // composite field
   if (key.equals("CCT"))  return " & ";         // Concat Operation
   if (key.equals("TM"))   return " |.| ";       //
   if (key.equals("LTM"))  return " |. ";        //
   if (key.equals("RTM"))  return " .| ";        //
   if (key.equals("CVS"))  return " >$ ";        //
   if (key.equals("OR"))   return " || ";        // UNION/OR Operation
   if (key.equals("AND"))  return " && ";        // INTERSECT/AND Operation
   if (key.equals("MNS"))  return " \\\\ ";      // MINUS/EXCEPT Operation
   if (key.equals("XOR"))  return " ^^ ";
   return key.toUpperCase();  //return "";
  }

  //[DllImport("user32.dll")]
  //private static extern short GetAsyncKeyState(long vKey);

  public static Object[] prm(String... s) { if (s == null) return new String[0]; else return s; } // Helpers function to use multiple params... parameters in 1 Method header

  //private static string twoDigit     (string s    ) { if (s.Length == 1) return "0" + s; return s; }
  public  static String twoDigits    (long value  ) { String result = "00"    + value; return result.substring(result.length() - 2); }
  public  static String threeDigits  (long value  ) { String result = "000"   + value; return result.substring(result.length() - 3); }
  public  static String fourDigits   (long value  ) { String result = "0000"  + value; return result.substring(result.length() - 4); }
  public  static String fiveDigits   (long value  ) { String result = "00000" + value; return result.substring(result.length() - 5); }

  public static String stdDateTimeStamp (Timestamp dt, boolean compressed) { if (compressed) return "" + (1900 + dt.getYear()) + twoDigits(dt.getMonth()) + twoDigits(dt.getDay()) + twoDigits(dt.getHours()) + twoDigits(dt.getMinutes()) + twoDigits(dt.getSeconds()) + threeDigits(0); else return "" + (1900 + dt.getYear()) + "-" + twoDigits(dt.getMonth()) + "-" + twoDigits(dt.getDay()) + " " + twoDigits(dt.getHours()) + ":" + twoDigits(dt.getMinutes()) + ":" + twoDigits(dt.getSeconds()) + "." + threeDigits(0); }
  public static String stdTimeStamp     (Timestamp dt, boolean compressed) { if (compressed) return "" + twoDigits(dt.getHours()) + twoDigits(dt.getMinutes()) + twoDigits(dt.getSeconds()) + threeDigits(0); else return "" + twoDigits(dt.getHours()) + ":" + twoDigits(dt.getMinutes()) + ":" + twoDigits(dt.getSeconds()) + "." + threeDigits(0); }
  public static String stdDateStamp     (Timestamp dt, boolean compressed) { if (compressed) return "" + (1900 + dt.getYear()) + twoDigits(dt.getMonth()) + twoDigits(dt.getDay()); else return "" + (1900 + dt.getYear()) + "-" + twoDigits(dt.getMonth()) + "-" + twoDigits(dt.getDay()); }

  //public static string DateTimeStamp(DateTime dt)
  //{
  // if (dt == null) return "";
  // if (dt.Year == 1900) return "";
  // return dt.Year + "-" + twoDigit("" + dt.Month) + "-" + twoDigit("" + dt.Day) + " " + dt.ToLongTimeString();
  //}

  private static String toDb(String x) { return x.replace("'", "``"); }

  public static String ds(boolean avoidEmptyStrings, String x) { return ((avoidEmptyStrings) && (x.length() == 0)) ? "'" + toDb(x) + " '"            : "'" + toDb(x) + "'";           }
  public static String dS(boolean avoidEmptyStrings, String x) { return ((avoidEmptyStrings) && (x.length() == 0)) ? "'" + toDb(x.toUpperCase()) + " '"  : "'" + toDb(x.toUpperCase()) + "'"; }
  public static String Ds(boolean avoidEmptyStrings, String x) { return ((avoidEmptyStrings) && (x.length() == 0)) ? "'" + toDb(x.toLowerCase()) + " '"  : "'" + toDb(x.toLowerCase()) + "'"; }


  public static String fromDb(String x, boolean cutCategory)  { String ret = x.replace("``", "'"); if (cutCategory) for (long i = ret.length() - 1; i >= 0; i--) if (ret.charAt((int)i) == '(') return ret.substring(0, Math.max(0, (int)i - 1)).trim(); return ret; }

  /*
  public static boolean keysActive(boolean exact, MouseEventArgs e, long rMouse, long numLock, long scrollLock, long capsLock, long lMouse, long alt, long control, long shift)
  {
   long lMouseButton = (e.Button == MouseButtons.Left)  ? 1 : 0;
   long rMouseButton = (e.Button == MouseButtons.Right) ? 1 : 0;
   return (exact) ? (0 == (((rMouse << 7) + (numLock << 6) + (scrollLock << 5) + (capsLock << 4) + (lMouse << 3) + (alt << 2) + (control << 2) + shift) ^ ((rMouseButton << 7) + (GetAsyncKeyState((long)Keys.NumLock) << 6) + (GetAsyncKeyState((long)Keys.Scroll) << 5) + (GetAsyncKeyState((long)Keys.CapsLock) << 4) + (lMouseButton << 3) + (GetAsyncKeyState((long)Keys.Alt) << 2) + (GetAsyncKeyState((long)Keys.ControlKey) << 1) + GetAsyncKeyState((long)Keys.ShiftKey)))) : (0 != (((rMouse << 7) + (numLock << 6) + (scrollLock << 5) + (capsLock << 4) + (lMouse << 3) + (alt << 2) + (control << 2) + shift) & ((rMouseButton << 7) + (GetAsyncKeyState((long)Keys.NumLock) << 6) + (GetAsyncKeyState((long)Keys.Scroll) << 5) + (GetAsyncKeyState((long)Keys.CapsLock) << 4) + (lMouseButton << 3) + (GetAsyncKeyState((long)Keys.Alt) << 2) + (GetAsyncKeyState((long)Keys.ControlKey) << 1) + GetAsyncKeyState((long)Keys.ShiftKey))));
  }

  public static bool keysMask(MouseEventArgs e, long rMouse, long numLock, long scrollLock, long capsLock, long lMouse, long alt, long control, long shift)
  {
   long lMouseButton = (e.Button == MouseButtons.Left)  ? 1 : 0;
   long rMouseButton = (e.Button == MouseButtons.Right) ? 1: 0;
   ulong NotCurrent = ~((ulong)((rMouseButton << 7) + (Math.Sign(GetAsyncKeyState((long)Keys.NumLock)) << 6) + (Math.Sign(GetAsyncKeyState((long)Keys.Scroll)) << 5) + (Math.Sign(GetAsyncKeyState((long)Keys.CapsLock)) << 4) + (lMouseButton << 3) + (Math.Sign(GetAsyncKeyState((long)Keys.Alt)) << 2) + (Math.Sign(GetAsyncKeyState((long)Keys.ControlKey)) << 1) + Math.Sign(GetAsyncKeyState((long)Keys.ShiftKey))));
   ulong NotMustBePressed  = 0xFFFFFFFFFFFFFF00 + (ulong)((Math.Abs(Math.Sign(rMouse - 1)) << 7) + (Math.Abs(Math.Sign(numLock - 1)) << 6) + (Math.Abs(Math.Sign(scrollLock - 1)) << 5) + (Math.Abs(Math.Sign(capsLock - 1)) << 4) + (Math.Abs(Math.Sign(lMouse - 1)) << 3) + (Math.Abs(Math.Sign(alt - 1)) << 2) + (Math.Abs(Math.Sign(control - 1)) << 1) + Math.Abs(Math.Sign(shift - 1)));
   ulong NotMustBeReleased = 0xFFFFFFFFFFFFFF00 + (ulong)((Math.Abs(Math.Sign(rMouse + 1)) << 7) + (Math.Abs(Math.Sign(numLock + 1)) << 6) + (Math.Abs(Math.Sign(scrollLock + 1)) << 5) + (Math.Abs(Math.Sign(capsLock + 1)) << 4) + (Math.Abs(Math.Sign(lMouse + 1)) << 3) + (Math.Abs(Math.Sign(alt + 1)) << 2) + (Math.Abs(Math.Sign(control + 1)) << 1) + Math.Abs(Math.Sign(shift + 1)));
   return (0 == ~(NotMustBeReleased | NotCurrent)) && ((NotMustBePressed | NotCurrent) == NotMustBePressed);
  }
  */

  
 public static TreePath treePath(TreeNode treeNode) 
 {
  List<Object> nodes = new ArrayList<Object>();
  if (treeNode == null) return null;
  nodes.add(treeNode);
  treeNode = treeNode.getParent();
  while (treeNode != null) 
  {
   nodes.add(0, treeNode);
   treeNode = treeNode.getParent();
  }
  return new TreePath(nodes.toArray());
 }
 
 public static Chain runtimeProcesses() throws Exception
 {
  Chain ret = Chain.Empty;
  try 
  {
   BufferedReader stdInput = new BufferedReader(new InputStreamReader(Runtime.getRuntime().exec("tasklist -fo csv /nh").getInputStream()));
   String line = "";
   while ((line = stdInput.readLine()) != null) 
   {
    ret = ret.plus("\n");
    for (String column: line.split("\"")) if (!column.equals(",") && !column.equals("")) ret = ret.plus("[" + column + "]");
   }
  }
  catch (Exception e){e.printStackTrace();}
  return ret.from(2);  
 }

  public static void testKeysPatterns()
  {
   //AttGetr: Alt did not work on my computer!
   //AttGetr: shift, control, numlock must be presses BEFORE MouseDown, this is most likely due to the apple-like mouse driver!!!
   //if (keysMask(e, 0, 0, 0, 0, 1, 0, 0, 0)) utl.say("PASSED"); else utl.say("FAILED");   //left mouse must be pressed and - other keys dont matter ...
   //if (keysMask(e, 0, 0, 0, 0, 1, 0, 1, 0))   utl.say("PASSED"); else utl.say("FAILED"); //left mouse,ctrl must be pressed and  - other keys dont matter ...
   //if (keysMask(e, 0, -1, 0, 0, 1, 0, 1, 0)) utl.say("PASSED"); else utl.say("FAILED");  //left mouse,ctrl must be pressed and NUMLOCK MUST BE RELEASED and - other keys dont matter ...
   //if (keysMask(e, 0, 0, 0, 0, 1, 0, 0, 1)) utl.say("PASSED"); else utl.say("FAILED");   //left mouse and shift must be pressed and other keys dont matter ...
   //if (keysMask(e, 0, 0, 0, 0, 1, 0, 0, -1)) utl.say("PASSED"); else utl.say("FAILED");   //left mouse must be pressed and SHIFT MUST BE RELEASED - other keys dont matter ...
  }

  /*
  public static long[] rnd;
  public static long[] random(long count, long min, long max)
  {
   rnd = new long[count];
   for (long i = 1; i <= count; i++) rnd[(long)i - 1] = new Random().Next((int)min, (int)max);
   return rnd;
  }

  public static String reverse(this String me)
  {
   char[] chars = me.ToCharArray();
   Array.Reverse(chars);
   return new string(chars);
  }
  */

  //public Reach r(string s) { return new Reach(s); }

  public static String WordCharacters = "abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";

  //public static void msg(String x) { System.out.println(x); }


  
  /**
   
          Advances xxDevPlus String Philosophy - by Gerald Trost
          ======================================================
          
          MOTIVATION 
          
          Programmers might think of Strings differently and one of
          the biggest mistakes ever since Informatics exist is:
                     Progrmmers do not think of Strings at all.
                                                    
          Programmers do not think of Strings as mathematical Objects.
          Programmers do not think of Strings as philosophical Objects.
          
          Most programmers think of Strings in a very technical and pragmatic way 
          - it is nothing more than a sequence of Characters.
          - or maybe it is an array of Characters - what else
          - there is no mystery to Strings!
          
          This is most probably the way moftly all Programmers think of Strings.
          
          Programmers want to save time and they want to write reliable Algorithms
          and so they are not ready to do some String Philosopy or String Mathematics.
           
          They cannot see any benefit in these.
           
          AND HERE I AM WRITING THESE NOTES TO SHOW YOU ALL THAT THE OPPOSITE IS TRUE:
           
          If - and only if - you take your time to do some String Philosphy - only in this case 
          - will you be able to write algorithms faster, easier, more reliable and better readable.
           
          I will NOT prove what I have written so far - for those who go into my String Philosophy
          it will be clear and evident that the source codes become 
                         NOTHING THEY HAVE EVER SEEN BEFORE
          and after some while it will become so natural and easy to read like they 
                           never thought it was possible.
                           
          Let me first state that there is no readable String Code out in this word and
          mostly String code makes heavy use of try-catch in order to deal with
          all possible use cases. So it becomes difficult to read just because there
          are so many exceptional szenarios to be covered by the code.
          Simply said: it is ugly code.
          
          what should be the outcome in this sample: 

                System.out.println("hello world".before(" "));

          it Should print "hello", right ? clear ? obvious ?.
          
          or, a similar example:

                System.out.println("hello-world".before(" "));

          it Should print "hello-world", right ? clear ? obvious ?
          
          it should print the whole String because the whole String
          comes before the first blank (in case there is no blank).
          
          Is this clear - is this a natural way of thinking ?
          
          Or - with a bit of irony in my mind - would you rather catch the  
          situation with a BlankNotFound-Exception and then print 
          the whole String again ?
          
          My Sample above did not need any try-catch clause and yet the outcome was the 
          most natural outcome in case the deimiter "blank" is missing in the String.
          
          So there is at least 1 philosophical question to discuss with String:

          At what Position should the Character "blank" be "defined to be" found
          if the Character "blank" is not there at all - how should we look at it
          - how should String Functions cover this case ?

          Should it then be at Position -2 or -0.5 ?
          Should it then be at Position -1 ?
          Should it then be at Position +infinite or -infinite ?
          Should it then be at Position length() + 1 ?
          Should it then be at Position length() + 1/3 ?
          
          This is just ONE philosophical issue to be discussed and it is 
             NOT JUST ANOTHER PHILOSOPHICAL QUESTION with many possible answers 
          -  NO 
          -  it is an ESSENTIAL PHILOSOPHICAL CONSIDERATION that might
             make coding a HUGE DIFFERENCE !!!
          
          you will not understand why this is the right solution but later on in this course 
          it will become quite clear and obvious to you:
          
          if I wrote a Function "posOfChar(ch)" then the Function would return
          str.lebngth() + 1/3 for each and every non-existant Character ch.
          
          sample: ("hello world".posOfChar('x') == 11.3333333) would be true.
          
          By now you will not see why this is a useful definition but on 
          the long run will you find that ist becomes a very useful and 
          natural way of calculation - an it will simplify a lot of issues 
          with string processing.
          
          
          ======================
          CHAPTER I:    COUNTING
          ======================
          
          Characters may be counted with integers like wagons of a train.
          
          there is possible wagon 1 and wagon 2 and wagon 3 on a short train.
          
          in anology to this there is Character 1 and Character 2 and Character 3
          in the String "abc" - this short String behaves just like a short Train.
           
          Nothing special here so far.
          
          Note: there is NOT a wagon with the position 0!
          
          "abc" does NOT AT ALL CONSIST of a Character in Position 0 and
          a Character in Position 1 and a Character in Position 2 - NO - 
          THIS IS SIMPLY NOT THE CASE - THIS IS SIMPLY WRONG for those
          who still remember primary school.
          
          So all programmers MUST learn that str.charAt(0) is invalid and
          that str.charAt(0) throws an InvalidIndexException in any valid 
          String Library.
          
          Or let me put it in other words:
          String Functions in Java, JS, c# and c++ are INVALID - these
          contradict to what programmers have learned in primary school.
          In my street there is a House woth number 1 but there is NO HOUSE  
          WITH NUMBER 0! How is your street?
          
          Or even other words:
          NO VALID STRING LIBRARY MUST SUPPOT 0-based COUNTING !
          EACH AND EVERY STRING LIBRARY HAS TO SUPPOT 1-based 
          COUNTING OF CHARACTERS AND NOTHING ELSE!
          
          NOT humans have to learn the way computers like to count - 
          NO - computers have to learn the way humans count!
          
          This will greatly simplify thinking and implementing.
           
          BUT - the Chapter is not yet over.          
          
          We start counting at 1 because it is natural - but how could we
          count the Characters that are NOT THERE ?
           
          Yes, this is a serious question:
          how could we count the Characters that are NON EXISTANT ?
          
          It might read as a pure philosophical question at first glance -
          nonetheless it is in fact the quintessence for simplification.
          
          Everybody will surely agree that in the String "abc" there is a
          "b" - Character at Poritions 2, no one will doubt this.
          
          but who can make a suggestions on the Characters between "b" and "c".
          At what Positions are these Characters and how many are there
          between "b" and "c".
          
          You might suggest that there are 0 Characters between "b" and "c" 
          and you might also suggest that these 0 Characters are probalbly
          located at Position 2 - at the same position as the Character "b".
          These are surely reasonable suggestions that might well be considered!
          This sounds natural and sound.
          
          But could we also find a "better" definition for the Position of the
          missing Characters between "b" and "c" - could we define Positions
          that bring along more benefit for "real life" String Processing Issues ?
           
          It may sound cracy but what if there was an "empty" Character like 
          there is an empty String in all programming languages ...
          
          oops, dis I go too far ? there is no empty Character in ANY of them
          programming languages and script languages - there are an empty
          Array, an empty List, an empty String but there is NEVER 
          an empty Character!
           
          Ok - true so far - but still, can your mind imagine a virtual
          empty Character between "b" and "c" and how much space would this
          empty Character occupy in the String "abc" ?
          
          OK - your mind can imagine a virtual empty Character takeing up
          no space and sitting there happily between "b" and "c"
          and doing no harm to any other Character.
          This is what I like to imagine.
          
          So for easier reading let me define the symbol "eC" for the
          empty Character and let me define the symbol "eS" for the empty String
          just like Mathematicians have defined the symbol Pi for easier reading
          and easier denoting of 3.14159.
          
          Thus, using the eC that does not yet exist in any programming 
          language or in any script language we could now write the String
          "abc" this way:
          
           eC eC a eC eC b eC eC c eC eC
          
          This would be just another notaion of the same String and it would
          be no different to the String "abc" - still would the "a" be 
          at Position 1 and the "b" would still be at Position 2 and the
          "c" would still be at Position 3 - nothing goes wrong here so far.
          
          But in this imaginary view there would also be an empty Character at 
          Position 0.33333.
          And there would be an eC at Position 1.66666
          And there would be an eC at Position 3.33333
          and so on ....
          
          We now have got a new and different Point of View that thinks of 
          the String "abc" in a quite different way 
          - but we still have not got any clue what this new Point of view
          could possibly be benificial for.
          
          Of course these empty Characters do not exist in the String and
          they do not exist in the computer's memory and they do not exist
          in the files of our harddisks - they do not take up any space - these
          do only exist in our minds - we can imagine that these empty
          Characters do sit there happily between the real Characters of
          the String.
          
          And we can define that the "real" Characters all have Integer Positions
          from 1 up to the String length() and we can also define that those
          imaginary empty Characters have fractional Position Numbers
          like 1 1/3 or like 3 2/3 and so on ...
          
          But we still do not know the benefits of this way of thinking ....


          NOTES ON DENOTATION:
          
          Mathematicians are commonly very clear when using thir Symbols but 
          in some papers they are not so clear - they use 0 for the empty Set
          as well as for the Number zero.
          
          And I will also write down some Symbols in a rather relaxed notation,
          e.g. will I write "a" for the Character a and sometimes I will write
          "a" for the one-character String "a".
          Also "" may mean the eC while in other cases "" will denote the 
          empty String.
          
          From the context you should all see which one I am speaking of.
          
          ==========================
          CHAPTER 2: One Way Streets
          ==========================

          To me it is unbelievable that there is an indexOf Method with 
          Seach String and From Index but there is no indexOf Method with
          Seach String and From Index and Upto Index in most languages.
          
          But even worse: there is no indexOf Method that works in the
          opposite direction.
          
          In a huge text that came from any web page I might be interested
          in the very last occurence of the word " contacts " and I cannot
          simply code:
          
          int pos = text.find(-1, " contacts ");
          
          I cannot even retrieve the last or second last Character in a
          simple and readable manner like:
          
          Char c = text.charAt(-1);
          Char c = text.charAt(-2);
          
          But every one should have a clear view of where the second
          last Character is located in the String, right ?
          Everybody knows it.
          
          I think most of the children would easily count a String backwirds
          like this:
          
          In "abc" is "c" at the minus first Position and "b" is at the
          minus second position and "a" is at the minus third posistion.
         
          Would any child in primary school have difficulties with counting
          bachwards with negative position numbers ?
          realls - I do not think so.
          
          So why can I then denote str.charAt(2) and why cannot I write
          str.charAt(-2) ???
          
          Don't the inventors of programming languages encourage readable
          code to be created ?
          
          Ok some might teach us
          "It is for security reasons - negative numbers are invalid
          Character Positions and so it is helpful to get an Exception
          at this Point".
          
          This sounds like security officers say: you first need to go
          to the cellar to check out the new security code and then go
          to the officers buro in the second floor in order to subscribe
          before you can procedd to your working place
          - and if you miss the big deal's
          deadline by this procedure then - what the hack - that's life.
          
          Not everybody wants life to be ruled as strictly that one is
          compelled to permanently work around obstacles ...
          
          There are more easy and elegant ways that are still safe.
           
          In other words:

          if all the String Methods were easyly readable and would show
          natural behaviour then will there never be a szenario with an
          unwanted negative position accidentally passed to a String
          Method - mostly because you will never need to calculate
          Indexes and Positions from Lengths!
          
          Only in this world of crude and unintelligent String Functions that we
          have to deal with nowadays is there a need of over-strict Methods
          and is there a need of handling all sorts of annoying Exceptions.
          
          Don't fear - the result will not be reversed to "no" in case you
          search the minus-second occurance of "on" :-)
          
          

  /**/
  
  
  public static int      locHi (double           loc)      { return (int) Math.round(loc)                       ;}
  public static int      locLo (double           loc)      { return (int) Math.round((3 * (loc - locHi(loc))))  ;}
  public static int      locHi (long             loc)      { return (int) ((loc & 0xFFFFFFFF00000000L) >> 32)   ;}
  public static int      locLo (long             loc)      { return (int) ( loc & 0xFFFFFFFFL)                  ;}
  public static long       Loc (double           loc)      { return Loc(locHi(loc), locLo(loc))                 ;}
  public static long       Loc (int locHi, int locLo)      { return ((0L + locHi) << 32) + locLo                ;}
  public static double     loc (long             loc)      { return loc(locHi(loc), locLo(loc))                 ;}
  public static double     loc (int locHi, int locLo)      { return locLo / 3 + locHi                           ;}


  public static double pLoc(double loc, String str)
  {
   loc = loc > str.length() ? str.length() + 1/3 : loc < -str.length() ? 2/3 : loc;
   return loc < 0 ? 1 + str.length() + loc : loc;
  }

  /*
  public static double nLoc(double loc, String str)
  {
   loc = loc > str.length() ? str.length() + 1/3 : loc < -str.length() ? 2/3 : loc;
   return pos >= 0 ? -1 - str.length() + pos : pos;
  }
  
  public static int charPos(char c, int occur, String str, int from, int upto)
  {
   int ret = occur > 0 ? str.length() + 1 : 0;
   from = occur > 0 ? pPos(from, str) : nPos(from, str);
   upto = occur > 0 ? pPos(upto, str) : nPos(upto, str);
   if (occur > 0)
   {
    for (int p = from; p <= upto; p++) 
    {
     
    }
   }
   else
   {
   }
   return ret;
  }
  
  /**/
          


  public static void msg(String x) throws Exception { say(x); }

  public static void say(Component parent, String msg) { if (ctx.useGuiDialogs) JOptionPane.showMessageDialog(parent, "<html><body>" + msg.replace("\r\n", "<br>") + "</body></html>"); else System.out.println(msg); }

  public static void say(String msg) throws Exception { say(null, msg); }

  public static String inputBox(String prompt, String deflt)
  {
   return JOptionPane.showInputDialog(null, prompt, deflt, 1);
  }

  public static boolean ask(String msg)
  {
   if ( 0 == JOptionPane.showConfirmDialog(null, msg, msg, JOptionPane.YES_NO_OPTION)) return true;
   return false;
  }



  public static String list(String delim, Object... obj)
  {
   String ret = "";
   if (obj.length == 0) return ret;
   for (Object o : obj)  { if (o instanceof String) ret += delim + ((String)o); else if (o instanceof Integer) ret += delim + ((Integer)o); else if (o instanceof Long) ret += delim + ((Long)o); else ret += delim + o.toString(); } return ret.substring(delim.length()); }
  

  //public static string[] str(params string[] str) { return str; }       //short: strings         exact: "array of strings"
  //public static string[] keys(kStr[] kStrs) { string[] ret = new string[kStrs.Length]; for (long i = 0; i < kStrs.Length; i++) ret[i] = kStrs[i].key; return ret; }

  //public static kStr[]   kStrs(params kStr[] strstr) { return strstr; }   //short: keyed strings   exact: "array of keyed string arrays"

  
  public static void exec(String cmd, String args, boolean byStart, boolean wait) throws Exception
  {
   Process p = null;
   if (byStart) p = Runtime.getRuntime().exec("cmd /c START " + cmd + " " + args); else p = Runtime.getRuntime().exec(cmd + " " + args);
   if (wait) p.waitFor();
  }


  public static String formatAttribute(String attr, String value)
  {
   String ret = utl.fromXml(value).replace("br ", " ");
   if (attr.equals("Birthdate"))
   {
    if ((ret.toUpperCase().startsWith("{{BIRTH DATE")) || (ret.toUpperCase().startsWith("{{BDA")))
     ret = ret.toUpperCase().replace("{{", "").replace("}}", "").replace("DF=YES", "").replace("DF=NO", "").replace("DF=Y", "").replace("DF=N", "").replace("BDA", "").replace("BIRTH DATE AND AGE", "").replace("BIRTH DATE", "");
   } else if (attr.equals("YearsActive"))
   {
    ret = ret.toUpperCase().replace("{{", "").replace("}}", "").replace("FY|", "");
   } else if (attr.equals("Budget"))
   {
    ret = ret.toUpperCase().replace("[[US$]]", "$");
   }
   ret = ret.trim();
   if (ret.startsWith("|")) ret = ret.substring(1).trim();
   if (ret.endsWith("|")) ret = ret.substring(0, ret.length() -1).trim();
   return ret;
  }


  /*
  public static String edit(string str)
  {
   string fileName = Path.GetTempPath() + "/" + getTempFileName(false) + ".txt";
   using (StreamWriter sw = new StreamWriter(fileName, false)) { sw.Write(str); }
   exec("Notepad", fileName, true, true);
   using (StreamReader sr = new StreamReader(fileName)) { return sr.ReadToEnd(); }
  }

  public static void view(string str)
  {
   string fileName = Path.GetTempPath() + "/" + getTempFileName(false) + ".txt";
   using (StreamWriter sw = new StreamWriter(fileName, false)) { sw.Write(str); }
   //exec("Notepad", fileName, true, false);
   exec("Write", fileName, true, false);
  }
  */

  public static void cOut    (String x)  { System.out.print(x);   }
  public static void cOutLn  (String x)  { System.out.println(x); }

  public static void s2f(String x, String fileName, boolean append) throws Exception
  {
   if (fileName.trim().toUpperCase().startsWith("FILE:///")) fileName = fileName.substring(8);
   FileWriter sw = new FileWriter(fileName, append);
   sw.write(x);
   sw.flush();
   sw.close();
  }

  public static String[]   sa(String...    str) {return str;}
  public static String[][] saa(String[]... str) {return str;}

  public static String f2s(String fileName) throws Exception  
  {
   return file2String(fileName);   
  }
  
  /* this function returns a null in front of the string - bug not fixed, function replaced
  public static String f2s(String fileName) throws Exception  //UniCodeOKAY
  {
   String ret = null;
   if (fileName.toLowerCase().startsWith("file://")) fileName = fileName.substring(8);
   BufferedReader sr = new BufferedReader(new FileReader(fileName)); 
   int count = 0;
   String line = "";
   do { line = sr.readLine(); if (line != null) ret += "\r\n" + line; } while(line != null);
   sr.close();
   return ret;
  }
  */
  
  /*
  public static String validFileName(String fName)
  {
   return fName.replace(":", "_").replace("index.php?title=", "");
  }
  
  public static String validFileNameFromUrl(String url) throws Exception
  {
   if ((url.endsWith("jpg")) || (url.endsWith("png")))
   {
    while (url.indexOf("/") > -1) url = cutr(url, "/");
    return url;
   }
   else
   {
    if (url.indexOf("=") > -1)
    {
     while (url.indexOf("=") > -1) url = cutr(url, "=");
     return (validFileName(url) + ".html").replace("\"", "").replace("?", "_").replace("/", "_");
    }
    else
    {
     while (url.indexOf("/") > -1) url = cutr(url, "/");
     return (validFileName(url) + ".html").replace("\"", "");
    }
   }
  }
  */
  
  public static String validFileName(String fName)
  {
   //return fName.replace(":", "_").replace("index.php?title=", "");  

   // please see https://stackoverflow.com/questions/724043/http-url-address-encoding-in-java
      
   // WAS PASSIERT HIER - BEISPIELE:   
   // ¿Quién es quién?   --->  %C2%BFQui%C3%A9n%20es%20qui%C3%A9n%3F 
   // '76_(film)         --->  '76_(film)
      
   String hStr = fName;
   
   if (hStr.contains("Category:"))
   {
       // Do NOT decode on category-strings like
       // https://en.wikipedia.org/w/index.php?title=Category:20th-century_German_painters&pagefrom=Fleischer%2C+Lutz%0ALutz+Fleischer#mw-pages

       // Just create a filename that can be stored
       hStr = hStr.replace(":", "___").replace("index.php?title=", "");     // GETR BAN2018 !!
   }
   else
   {
       hStr = hStr.replace(":", "___").replace("index.php?title=", "");     // GETR BAN2018 !!
       
       // Do (!) decode the string to convert K%C3%A4the_Schuftan into Käthe_Schuftan
       try
       {
         hStr = URLDecoder.decode(hStr, "UTF-8");          // GETR BAN2018 !!    Käthe Schuftan !!!!
       } 
       catch(Exception ex) 
       { 
           System.out.println("EXCEPTION in URLDecoder.decode: "+ex.getMessage()); 
       }
   }

  
   /*
   try
   {
   URI uri = new URI("http","www.wikipedia.org","/"+hStr,null);
   hStr = uri.toASCIIString();
   } 
   catch(Exception ex) 
   { 
       System.out.println("EXCEPTION in URI toASCIIString: "+ex.getMessage()); 
   }/**/
   
   hStr = hStr.replace("http://www.wikipedia.org/", "");
   return hStr;
  }
  
  
  
  public static String fsEncode(String fileName) throws Exception
  {
   String ret = fileName;
   if (fileName.contains("?"))  ret = ret.replace("?"  , "&qmark;"   );
   if (fileName.contains(":"))  ret = ret.replace(":"  , "&colon;"   );
   if (fileName.contains("|"))  ret = ret.replace("|"  , "&vertbar;" );
   if (fileName.contains("\"")) ret = ret.replace("\"" , "&quot;"    );
   if (fileName.contains("*"))  ret = ret.replace("*"  , "&star;"    );
   if (fileName.contains(">"))  ret = ret.replace(">"  , "&gt;"      );
   if (fileName.contains("<"))  ret = ret.replace("<"  , "&lt;"      );
   return ret;
  }

  public static String fsDecode(String fileName) throws Exception
  {
   String ret = fileName;
   if (fileName.contains("&qmark;"   ))  ret = ret.replace( "&qmark;"    , "?"   );
   if (fileName.contains("&colon;"   ))  ret = ret.replace( "&colon;"    , ":"   );
   if (fileName.contains("&vertbar;" ))  ret = ret.replace( "&vertbar;"  , "|"   );
   if (fileName.contains("&quot;"    ))  ret = ret.replace(  "&quot;"    , "\""  );
   if (fileName.contains("&star;"    ))  ret = ret.replace(  "&star;"    , "*"   );
   if (fileName.contains("&gt;"      ))  ret = ret.replace(  "&gt;"      , ">"   );
   if (fileName.contains("&lt;"      ))  ret = ret.replace(  "&lt;"      , "<"   );
   return ret;
  }
  
  
  public static String validFileNameFromUrl(String url) throws Exception
  {
   Chain u = new Chain(url);
   if ((u.endsWith("jpg")) || (u.endsWith("png")))
   {
    return utl.validFileName(u.after(-1, "/").text());
   }
   else
   {
    if (u.at(1, "=").len() > 0) return utl.validFileName(u.after(1, "=").text().replace("\"", "").replace("?", "_").replace("/", "_"));
    return utl.validFileName(u.after(-1,"/").text().replace("\"", "_").replace("?", "_").replace("/", "_"));
   }
  }

  
  public static String composeValidFile(String downloadFolder, String url) throws Exception
  {
   String fName = utl.validFileNameFromUrl(url);
   String firstChar = fName.substring(0, 1);
   return downloadFolder + firstChar + "/" +fName;
  }

  public static void fileCopy(String sourceFile, String targetFile) throws Exception 
  {
   File source  = new File(sourceFile); 
   File dest    = new File(targetFile); 
   InputStream is = null;
   OutputStream os = null;
   try 
   {
    is = new FileInputStream(source);
    os = new FileOutputStream(dest);
    byte[] buffer = new byte[1024];
    int length;
    while ((length = is.read(buffer)) > 0) os.write(buffer, 0, length);
   } 
   finally { is.close(); os.close(); }
  }
  
  public static String cutl(String[] anystring, String delimiter)
  {
   String result;
   long pos = anystring[0].indexOf(delimiter);
   if (pos == -1) { result = anystring[0]; anystring[0] = ""; } else { result = anystring[0].substring(0, (int)pos); anystring[0] = anystring[0].substring((int)pos + delimiter.length()); }
   return result;
  }

  public static String cutl(String anystring, String delimiter)
  {
   String result;
   long pos = anystring.indexOf(delimiter);
   if (pos == -1) { result = anystring; anystring = ""; } else { result = anystring.substring(0, (int)pos); anystring = anystring.substring((int)pos + delimiter.length()); }
   return result;
  }

  public static String cutr(String[] anystring, String delimiter)
  {
   String result = "";
   long pos = anystring[0].length() - delimiter.length();
   while (pos > -1) if (anystring[0].substring((int)pos, (int)pos + delimiter.length()).equals(delimiter)) { result = anystring[0].substring((int)pos + delimiter.length()); anystring[0] = anystring[0].substring(0, (int)pos); return result; }  else pos--;
   result = anystring[0];
   anystring[0] = "";
   return result;
  }

  public static String cutr(String anystring, String delimiter)
  {
   String result = "";
   long pos = anystring.length() - delimiter.length();
   while (pos > -1) 
    if (anystring.substring((int)pos, (int)pos + delimiter.length()).equals(delimiter)) 
    { 
     result = anystring.substring((int)pos + delimiter.length()); 
     anystring = anystring.substring(0, (int)pos); 
     return result; 
    } 
    else pos--;
   result = anystring;
   anystring = "";
   return result;
  }

 public static String cutl(String[] anystring, int count)
 {
  String result = "";
  if (anystring[0].length() < count) { result = anystring[0]; anystring[0] = ""; } else { result = anystring[0].substring(0, count); anystring[0] = anystring[0].substring(count); }
  return result;
 }

 public static String cutl(String anystring, int count)
 {
  String result = "";
  if (anystring.length() < count) { result = anystring; anystring = ""; } else { result = anystring.substring(0, count); anystring = anystring.substring(count); }
  return result;
 }

 public static String cutr(String[] anystring, int count)
 {
  String result = "";
  if (anystring[0].length() < count) { result = anystring[0]; anystring[0] = ""; } else { result = anystring[0].substring(anystring[0].length() - count); anystring[0] = anystring[0].substring(0, anystring[0].length() - count); }
  return result;
 }

 public static String cutr(String anystring, int count)
 {
  String result = "";
  if (anystring.length() < count) { result = anystring; anystring = ""; } else { result = anystring.substring(anystring.length() - count); anystring = anystring.substring(0, anystring.length() - count); }
  return result;
 }

 public static String ppV(boolean cnd, String prefix, String postfix, String  val, String  dflt) { if (!cnd) if (dflt.trim().length() ==     0) return ""; else return prefix + dflt.trim() + postfix; return prefix + val.trim() + postfix; }  //PrefixPostFixValue
 public static String ppV(boolean cnd, String prefix, String postfix, long    val, long    dflt) { if (!cnd) if (dflt                 ==     0) return ""; else return prefix + dflt        + postfix; return prefix + val        + postfix; }  //PrefixPostFixValue
 public static String ppV(boolean cnd, String prefix, String postfix, boolean val, boolean dflt) { if (!cnd) if (dflt                 == false) return ""; else return prefix + dflt        + postfix; return prefix + val        + postfix; }  //PrefixPostFixValue

 public static String ppV(boolean cnd, String prefix, String postfix, String  val              ) { return ppV(cnd                         , prefix, postfix, val, ""    ); }  //PrefixPostFixValue
 public static String ppV(boolean cnd, String prefix, String postfix, long    val              ) { return ppV(cnd                         , prefix, postfix, val, 0     ); }  //PrefixPostFixValue
 public static String ppV(boolean cnd, String prefix, String postfix, boolean val              ) { return ppV(cnd                         , prefix, postfix, val, false ); }  //PrefixPostFixValue

 public static String ppV(             String prefix, String postfix, String  val, String  dflt) { return ppV(val.trim().length()  >     0, prefix, postfix, val, dflt  ); }  //PrefixPostFixValue
 public static String ppV(             String prefix, String postfix, long    val, long    dflt) { return ppV(val                 !=     0, prefix, postfix, val, dflt  ); }  //PrefixPostFixValue
 public static String ppV(             String prefix, String postfix, boolean val, boolean dflt) { return ppV(val                 != false, prefix, postfix, val, dflt  ); }  //PrefixPostFixValue

 public static String ppV(             String prefix, String postfix, String  val              ) { return ppV(val.trim().length()  >     0, prefix, postfix, val, ""    ); }  //PrefixPostFixValue
 public static String ppV(             String prefix, String postfix, long    val              ) { return ppV(val                 !=     0, prefix, postfix, val, 0     ); }  //PrefixPostFixValue
 public static String ppV(             String prefix, String postfix, boolean val              ) { return ppV(val                 != false, prefix, postfix, val, false ); }  //PrefixPostFixValue

 public static String  pV(boolean cnd, String prefix,                 String  val              ) { return ppV(cnd                         , prefix, ""     , val, ""    ); }  //PrefixValue
 public static String  pV(boolean cnd, String prefix,                 long    val              ) { return ppV(cnd                         , prefix, ""     , val, 0     ); }  //PrefixValue
 public static String  pV(boolean cnd, String prefix,                 boolean val              ) { return ppV(cnd                         , prefix, ""     , val, false ); }  //PrefixValue

 public static String  pV(             String prefix,                 String  val              ) { return ppV(val.trim().length()  > 0    , prefix, ""     , val, ""    ); }  //PrefixValue
 public static String  pV(             String prefix,                 long    val              ) { return ppV(val                 != 0    , prefix, ""     , val, 0     ); }  //PrefixValue
 public static String  pV(             String prefix,                 boolean val              ) { return ppV(val                 != false, prefix, ""     , val, false ); }  //PrefixValue

 public static ArrayList<String> s2l(String str, boolean trimlines, boolean skipempty)
 {
  ArrayList<String> ret = new ArrayList<String>();
  String[] s = new String[1];
  s[0] = str.replace("\r\n", "\n");
  //if (collateWs) { s = s.Replace("\t", " ").Replace("�", " "); while (s.IndexOf("  ") > -1) s = s.Replace("  ", " "); }
  while (s[0].length() > 0) { String x = cutl(s, "\n"); if (trimlines) x = x.trim(); if (!skipempty || x.length() > 0) ret.add(x); }
  return ret;
 }

 public static String l2s(List<String> l, String delim)
 {
  String ret = "";
  for (String x : l) if (ret.length() == 0) ret = x; else ret += delim + x;
  return ret;
 }

 public static String stringArr2s(String[] a, String delim)
 {
  String ret = "";
  for (String x : a) if (ret.length() == 0) ret = x; else ret += delim + x;
  return ret;
 }

 public static String intArr2s(long[] a, String delim)
 {
  String ret = "";
  for (long x : a) if (ret.length() == 0) ret = "" + x; else ret += delim + x;
  return ret;
 }

 public static boolean canRead(String fileName)  //UniCodeOKAY
 {
  if (fileName.toLowerCase().startsWith("file://")) fileName = fileName.substring(8);
  try { FileReader sr = new FileReader(fileName); sr.read(); sr.close(); } catch (Exception ex) { return false; }
  return true;
 }

 
 
 /*
 public static String getTempFileName(boolean full)
 {
  String filename = Path.GetTempFileName();
  String path = Path.GetDirectoryName(filename);
  File.Delete(filename);
  return full? path + "\\" + stdDateTimeStamp(DateTime.Now, true) + Path.GetFileNameWithoutExtension(filename): stdDateTimeStamp(DateTime.Now, true) + Path.GetFileNameWithoutExtension(filename);
 }

 public static String toXml(String s)
 {
  if (s == null) return "";
  String ret = s;
  ret = ret.replace("&", "&amp;");
  ret = ret.replace("&lt;", "&lt;");
  ret = ret.replace("&gt;", "&gt;");
  ret = ret.replace("\"", "&quot;");
  ret = ret.replace("'", "&apos;");
  return ret;
 }

 public static String fromXml(String s)
 {
  if (s == null) return "";
  String ret = s;
  ret = ret.replace("&apos;", "'");
  ret = ret.replace("&quot;", "\"");
  ret = ret.replace("&gt;", "&gt;");
  ret = ret.replace("&lt;", "&lt;");
  ret = ret.replace("&amp;", "&");
  return ret;
 }




 */


 public static String palind(String s) { String ret = ""; for (int i = s.length(); i >= 1; i--) ret += s.charAt(i-1); return ret; }

 public static String oppst(Character c)
 {
  switch (c)
  {
   case ',': return ",";  case ';': return ";";  
   case '(': return ")";  case ')':  return "(";       
   case '[': return "]";  case ']':  return "[";       
   case '{': return "}";  case '}':  return "{";       
   case '>': return "<";  case '<':  return ">";       
   case '´': return "`";  case '`':  return "´";       
   case '/': return "\\"; case '\\': return "/";       
  }
  return "" + c;
 }

 public static String oppst(String s)
 {
  if (s == null) return ""; if (s.length() == 0) return "";
  String ret = ""; for(int i = s.length(); i >=1; i--) ret += oppst(s.charAt(i-1));
  return ret;
 }

 public final static String ESCAPE_CHARS = "<>&\"\'";
 public final static List<String> ESCAPE_STRINGS = Collections.unmodifiableList(Arrays.asList(new String[] {"&lt;", "&gt;", "&amp;", "&quot;", "&apos;"}));

 private static String UNICODE_LOW =  "" + ((char)0x20); //space
 private static String UNICODE_HIGH = "" + ((char)0x7f);

 public static String toXml2(String content) 
 {
  String result = content;
  if ((content != null) && (content.length() > 0)) 
  {
   boolean modified = false;
   StringBuilder stringBuilder = new StringBuilder(content.length());
   for (int i = 0, count = content.length(); i < count; ++i) 
   {
    String character = content.substring(i, i + 1);
    int pos = ESCAPE_CHARS.indexOf(character);
    if (pos > -1) {stringBuilder.append(ESCAPE_STRINGS.get(pos)); modified = true;}
    else if ((character.compareTo(UNICODE_LOW) > -1) && (character.compareTo(UNICODE_HIGH) < 1)) { stringBuilder.append(character); } else { stringBuilder.append("&#" + ((int)character.charAt(0)) + ";"); modified = true; }
   }
   if (modified) {result = stringBuilder.toString();}
  }
 return result;
 }

 
 public static String toXml(CharSequence s, boolean quotes) 
 {
  StringBuilder sb = new StringBuilder();
  int len = s.length();
  for (int i = 1; i <= len; i++) 
  {
   int c = s.charAt(i - 1);
   if (c >= 0xd800 && c <= 0xdbff && i < len - 1) {c = ((c - 0xd7c0) << 10) | ( s.charAt(++i) & 0x3ff); }  // UTF16 decode
   if (c < 0x80)                                                                                       // ASCII range: test most common case first
   {                                                               
    if (c < 0x20 && (c != '\t' && c != '\r' && c != '\n')) sb.append("&#xfffd;");  // Unicode replacement character for Illegal XML character (even encoded.)
    else 
    {
     switch(c) 
     {
      case '&':               sb.append("&amp;")                           ; break;
      case '>':               sb.append("&gt;")                            ; break;
      case '<':               sb.append("&lt;")                            ; break;
      case '\'':  if (quotes) sb.append("&apos;"); else sb.append((char)c) ; break;   // required for XML attribute
      case '\"':  if (quotes) sb.append("&quot;"); else sb.append((char)c) ; break;   // required for XML attribute
      case '\n':              sb.append("&#10;")                           ; break;   // not required
      case '\r':              sb.append("&#13;")                           ; break;   // not required
      case '\t':              sb.append("&#9;")                            ; break;   // not required
      default:                sb.append((char)c);
     }
    }
   } 
   else 
    if ((c >= 0xd800 && c <= 0xdfff) || c == 0xfffe || c == 0xffff) { sb.append("&#xfffd;"); }              // Illegal XML character, even encoded. Skip or substitute - Unicode replacement character
    else {sb.append("&#x"); sb.append(Integer.toHexString(c)); sb.append(';'); }
  }
  return sb.toString();
 }

 
 
 public static String fromXml(String s)
 {
  if (s == null) return "";
  String ret = s;
  ret = ret.replace("%20"    , " ");
  ret = ret.replace("%28"    , "(");
  ret = ret.replace("%29"    , ")");
  ret = ret.replace("&apos;" , "'");
  ret = ret.replace("&quot;" , "\"");
  ret = ret.replace("&gt;"   , ">");
  ret = ret.replace("&lt;"   , "<");
  ret = ret.replace("&amp;"  , "&");
  return ret;
 }

 public static String toXml3(String s, boolean quotes)
 {
  if (s == null) return "";
  String ret = s;
  ret = ret.replace(" "      , "%20");
  ret = ret.replace("("      , "%28");
  ret = ret.replace(")"      , "%29");
  ret = ret.replace("&"      , "&amp;");
  ret = ret.replace("<"      , "&lt;");
  ret = ret.replace(">"      , "&gt;");
  if (quotes)
  {
   ret = ret.replace("\""    , "&quot;");
   ret = ret.replace("'"     , "&apos;");
  }
  return ret;
 }
 
 /*
 public static Dictionary<string, string[]> strKeyStrArrays(string literal)
 {
  Dictionary<string, string[]> ret = new Dictionary<string, string[]>();
  while (literal.Length > 0)
  {
   string key = null;
   string[] val = new string[0];
   string line = cutl(ref literal, "´°°`");
   while (line.Length > 0)
   {
    string term = cutl(ref line, "´°`");
    if (key == null)
     key = term;
    else
    {
     string[] val1 = new string[val.Length + 1];
     for (long i = 0; i < val.Length; i++) val1[i] = val[i];
     val1[val.Length] = term;
     val = val1;
    }
   }
   ret.Add(key, val);
  }
  return ret;
 }
 */

 public static String standardDateTime(Date dt, boolean compressed)
 {
  Calendar c = new GregorianCalendar();
  if (compressed) return "" + c.get(c.YEAR) + twoDigits(1 + c.get(c.MONTH)) + twoDigits(c.get(c.DAY_OF_MONTH)) + twoDigits(c.get(c.HOUR_OF_DAY)) + twoDigits(c.get(c.MINUTE)) + twoDigits(c.get(c.SECOND)); else return "" + c.get(c.YEAR) + "-" + twoDigits(1 + c.get(c.MONTH)) + "-" + twoDigits(c.get(c.DAY_OF_MONTH)) + " " + twoDigits(c.get(c.HOUR_OF_DAY)) + ":" + twoDigits(c.get(c.MINUTE)) + ":" + twoDigits(c.get(c.SECOND));
 }

 public static String getTempFileName(boolean full) throws IOException
 {
  String ret = "";
  File f = File.createTempFile(standardDateTime(new Date(), true), "");
  if (full) ret = f.getPath(); else ret = f.getName();
  f.delete();
  return ret;
 }

 public static void string2File(String x, String fileName) throws Exception
 {
  DataOutputStream dataOut = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(fileName)));
  dataOut.writeBytes(x);
  dataOut.close();
 }

 public static String file2String(String fileName) throws Exception
 {
  if (fileName.toLowerCase().startsWith("file://")) fileName = fileName.substring(8);
  DataInputStream in = new DataInputStream((new BufferedInputStream(new FileInputStream(fileName))));
  StringBuilder sb = new StringBuilder();
  char[] buffer = new char[40000];
  Reader inReader = new InputStreamReader(in, "UTF-8");
  int read = inReader.read(buffer, 0, buffer.length);
  while (read > -1) { sb.append(buffer, 0, read); read = inReader.read(buffer, 0, buffer.length); }
  if (in != null) try { in.close(); } catch (Exception ee) {};
  return new String(sb.toString());
 }

 public static void setControlFont(JComponent ctl, String defaultFont)
 {
  String[] font = new String[] {defaultFont};
  ctl.setFont( new Font( utl.cutl(font, "; "),  Font.PLAIN, (int)(Double.parseDouble(utl.cutl(font, "; ") ) )));
  ctl.setSize ( ctl.getSize().width, (int)Long.parseLong(utl.cutl(font, "; ")));
  if (font[0].trim().startsWith("#")) ctl.setForeground ( new Color( /* R */ Integer.parseInt(font[0].substring(1, 3), 16), /* G */ Integer.parseInt(font[0].substring(3, 5), 16), /* B */ Integer.parseInt(font[0].substring(5, 7), 16) ) ); else ctl.setForeground ( new Color( /* R */ (int)Long.parseLong(utl.cutl(font, "; ")), /* G */ (int)Long.parseLong(utl.cutl(font, "; ")), /* B */ (int)Long.parseLong(utl.cutl(font, "; ")) ) );
 }

 public static void cloneMenu(JPopupMenu source, String subMenu, JPopupMenu target, Delegate menuAction)
 {
  target.removeAll();
  for (Component mi : source.getComponents())
   if ((subMenu.trim().length() == 0) || (((JMenuItem)mi).getText().equals(subMenu)))
   {
    if (subMenu.trim().length() == 0) target.add ( new JPopupMenu(((JMenuItem)mi).getText()));
     for (Component  m : ((JMenuItem)mi).getComponents())
     {
      if (subMenu.trim().length() > 0) target.add ( new JPopupMenu(((JMenuItem)m).getText())); else ((JMenuItem)(target.getComponent(target.getComponentCount() - 1))).add (new JMenuItem(((JMenuItem)m).getText()));
      //if (subMenu.Trim().Length > 0) target.MenuItems[target.MenuItems.Count - 1].Click += click; else target.MenuItems[target.MenuItems.Count - 1].MenuItems[target.MenuItems[target.MenuItems.Count - 1].MenuItems.Count - 1].Click += click;
     }
   }
 }


 public static void setLeft     (Component control,         long value) { try { control.setLocation ( (int)value                             , control.getLocation().y)               ; } catch (Exception ex) {}}
 public static void setTop      (Component control,         long value) { try { control.setLocation ( control.getLocation().x                , (int)value)                            ; } catch (Exception ex) {}}
 public static void setWidth    (Component control,         long value) { try { control.setSize     ( (int)value                             , control.getSize().height)              ; } catch (Exception ex) {}}
 public static void setHeight   (Component control,         long value) { try { control.setSize     ( control.getSize().width                , (int)value)                            ; } catch (Exception ex) {}}
 public static void setLocation (Component control,   long dx, long dy) { try { control.setLocation ( (int)dx                                , (int)dy)                               ; } catch (Exception ex) {}}
 public static void setSize     (Component control,   long dw, long dh) { try { control.setSize     ( (int)dw                                , (int)dh)                               ; } catch (Exception ex) {}}

 public static void chgLeft     (Component control,         long delta) { try { control.setLocation ( control.getLocation().x  + (int)delta  , control.getLocation().y)               ; } catch (Exception ex) {}}
 public static void chgTop      (Component control,         long delta) { try { control.setLocation ( control.getLocation().x                , control.getLocation().y  + (int)delta) ; } catch (Exception ex) {}}
 public static void chgWidth    (Component control,         long delta) { try { control.setSize     ( control.getSize().width  + (int)delta  , control.getSize().height)              ; } catch (Exception ex) {}}
 public static void chgHeight   (Component control,         long delta) { try { control.setSize     ( control.getSize().width                , control.getSize().height + (int)delta) ; } catch (Exception ex) {}}
 public static void chgLocation (Component control,   long dx, long dy) { try { control.setLocation ( control.getLocation().x  + (int)dx     , control.getLocation().y  + (int)dy)    ; } catch (Exception ex) {}}
 public static void chgSize     (Component control,   long dw, long dh) { try { control.setSize     ( control.getSize().width  + (int)dw     , control.getSize().height + (int)dh)    ; } catch (Exception ex) {}}

 public static void cpyLeft     (Component control,   Component source) { try { control.setLocation ( source.getLocation().x                 , control.getLocation().y)               ; } catch (Exception ex) {}}
 public static void cpyTop      (Component control,   Component source) { try { control.setLocation ( control.getLocation().x                , source.getLocation().y)                ; } catch (Exception ex) {}}
 public static void cpyWidth    (Component control,   Component source) { try { control.setSize     ( source.getSize().width                 , control.getSize().height)              ; } catch (Exception ex) {}}
 public static void cpyHeight   (Component control,   Component source) { try { control.setSize     ( control.getSize().width                , source.getSize().height)               ; } catch (Exception ex) {}}
 public static void cpyLocation (Component control,   Component source) { try { control.setLocation ( source.getLocation().x                 , source.getLocation().y)                ; } catch (Exception ex) {}}
 public static void cpySize     (Component control,   Component source) { try { control.setSize     ( source.getSize().width                 , source.getSize().height)               ; } catch (Exception ex) {}}

 public static void setLeft     (JComponent control,        long value) { try { control.setLocation ( (int)value                             , control.getLocation().y)               ; } catch (Exception ex) {}}
 public static void setTop      (JComponent control,        long value) { try { control.setLocation ( control.getLocation().x                , (int)value)                            ; } catch (Exception ex) {}}
 public static void setWidth    (JComponent control,        long value) { try { control.setSize     ( (int)value                             , control.getSize().height)              ; } catch (Exception ex) {}}
 public static void setHeight   (JComponent control,        long value) { try { control.setSize     ( control.getSize().width                , (int)value)                            ; } catch (Exception ex) {}}
 public static void setLocation (JComponent control,  long dx, long dy) { try { control.setLocation ( (int)dx                                , (int)dy)                               ; } catch (Exception ex) {}}
 public static void setSize     (JComponent control,  long dw, long dh) { try { control.setSize     ( (int)dw                                , (int)dh)                               ; } catch (Exception ex) {}}

 public static void chgLeft     (JComponent control,        long delta) { try { control.setLocation ( control.getLocation().x  + (int)delta  , control.getLocation().y)               ; } catch (Exception ex) {}}
 public static void chgTop      (JComponent control,        long delta) { try { control.setLocation ( control.getLocation().x                , control.getLocation().y  + (int)delta) ; } catch (Exception ex) {}}
 public static void chgWidth    (JComponent control,        long delta) { try { control.setSize     ( control.getSize().width  + (int)delta  , control.getSize().height)              ; } catch (Exception ex) {}}
 public static void chgHeight   (JComponent control,        long delta) { try { control.setSize     ( control.getSize().width                , control.getSize().height + (int)delta) ; } catch (Exception ex) {}}
 public static void chgLocation (JComponent control,  long dx, long dy) { try { control.setLocation ( control.getLocation().x  + (int)dx     , control.getLocation().y  + (int)dy)    ; } catch (Exception ex) {}}
 public static void chgSize     (JComponent control,  long dw, long dh) { try { control.setSize     ( control.getSize().width  + (int)dw     , control.getSize().height + (int)dh)    ; } catch (Exception ex) {}}

 public static void cpyLeft     (JComponent control, JComponent source) { try { control.setLocation ( source.getLocation().x                 , control.getLocation().y)               ; } catch (Exception ex) {}}
 public static void cpyTop      (JComponent control, JComponent source) { try { control.setLocation ( control.getLocation().x                , source.getLocation().y)                ; } catch (Exception ex) {}}
 public static void cpyWidth    (JComponent control, JComponent source) { try { control.setSize     ( source.getSize().width                 , control.getSize().height)              ; } catch (Exception ex) {}}
 public static void cpyHeight   (JComponent control, JComponent source) { try { control.setSize     ( control.getSize().width                , source.getSize().height)               ; } catch (Exception ex) {}}
 public static void cpyLocation (JComponent control, JComponent source) { try { control.setLocation ( source.getLocation().x                 , source.getLocation().y)                ; } catch (Exception ex) {}}
 public static void cpySize     (JComponent control, JComponent source) { try { control.setSize     ( source.getSize().width                 , source.getSize().height)               ; } catch (Exception ex) {}}

 public static void BringToFront (JComponent control) { control.getParent().setComponentZOrder(control, 0); }
 public static void SendToBack   (JComponent control) { control.getParent().setComponentZOrder(control, control.getParent().getComponentCount() - 1); }

 public void fail(String msg) throws Exception { throw new Exception(msg); }

 public static boolean dmyBool  (String notes) { return false; } //for setting a marker in unfinished code
 public static String  dmyStr   (String notes) { return "";    } //for setting a marker in unfinished code
 public static int     dmyInt   (String notes) { return 0;     } //for setting a marker in unfinished code
 public static long    dmyLong  (String notes) { return 0;     } //for setting a marker in unfinished code

 public static void sendMessage() throws Exception
 {
  SendMail sm = new SendMail();
  sm.Send("sbstec.sbs@gmail.com", "scstechn0l0gy", "sbstec.sbs@gmail.com", "", "TripleSpooler ExportStatus", "OK");

 }

 public static String notNull(Object o) { if (o == null) return ""; return (String)o; }


}




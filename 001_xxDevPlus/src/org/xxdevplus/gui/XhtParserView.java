
package org.xxdevplus.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import org.xxdevplus.struct.KeyPile;
import org.xxdevplus.struct.Pile;
import org.xxdevplus.utl.utl;
import org.jdesktop.layout.GroupLayout;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Element;
import org.w3c.dom.css.RGBColor;
import org.xxdevplus.udf.XhtUrl;
import org.xxdevplus.udf.XhtParser;
import org.xxdevplus.chain.Chain;
import org.xxdevplus.data.Db;
import org.xxdevplus.struct.Triple;
import org.xxdevplus.utl.ctx;

//** @author GeTr Copyright© 1996-2020 Gerald Trost, Klagenfurt, AUSTRIA. No warranties or liabilities whatsoever, not with third parties, You may use, modify, distribute this code under GNU public dual licence free for NON-Commercial use as long as this notice remains unchanged.
//** @comment the main console



class CustomTreeCellRenderer extends DefaultTreeCellRenderer
{
 
 private Pile<Object[]>   domTreeElements   = null;
 private JTextField       txtFilterText     = null;
 private JTextField       txtFilterHtml     = null;
 public CustomTreeCellRenderer(Pile<Object[]> domTreeElements, JTextField txtFilterText, JTextField txtFilterHtml)
 {
  this.domTreeElements   = domTreeElements;
  this.txtFilterText     = txtFilterText;
  this.txtFilterHtml     = txtFilterHtml;
 }
 @Override
 public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus)
 {
  super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
  boolean hilite = false;
  if( value instanceof DefaultMutableTreeNode)
  {
   TreeNode  node     = (TreeNode)value;
   String    tFilter  = txtFilterText.getText().toLowerCase().trim();
   String    hFilter  = txtFilterHtml.getText().toLowerCase().trim();
   if (tFilter.length() + hFilter.length() > 0)
   {
    for(Object[] o : domTreeElements)
    {
     if (o[0] == node)
     {
      Element e = (Element)o[1];
      if (tFilter.length() > 0) hilite = e.text().toLowerCase().contains(tFilter);
      if (hFilter.length() > 0) hilite = hilite || e.html().toLowerCase().contains(hFilter);
      break;
     }
    }
   }
  }
  super.setBackgroundSelectionColor(Color.BLUE);
  super.setBackground(Color.white);
  if (hilite)
  {
   if (selected) super.setBackground(new Color( 240, 240, 240));
   setForeground(Color.red);
  }
  else
  {
   if (selected) super.setBackground(new Color( 240, 240, 240));
   setForeground(getTextNonSelectionColor());
  }
  this.setOpaque(true);
  return this;
 }
}



public class XhtParserView extends JFrame
{

 private static boolean selfTested  = false; private static String ass(boolean expr) throws Exception { if (!expr) throw new Exception(ass(true) + " SelfTest Failure"); return "frmConsole"; } private static void selfTest() { selfTested = true; }

 private   Toolkit    toolkit   = null;
 private   Clipboard  clipboard = null;

 private int                          autoEvent                = 0;
 public  ctx                          cx                       = null;
 public  Db                           db                       = null;
 private String[]                     colNames                 = {"id", "stringid", "domainid", "title", "description", "rank", "year", "tags", "markdown1", "markdown2"}; 
 private KeyPile<String, Long>        dbDomains                = new KeyPile<String, Long>();
 private int                          defaultDividerLocation   = 680;

 private Highlighter                  hilit                    = new DefaultHighlighter();
 private Highlighter.HighlightPainter painter                  = new DefaultHighlighter.DefaultHighlightPainter(Color.orange);

;
 
 private void init() { if (!selfTested) selfTest(); }

 public static void main () throws Exception 
 { 
  new Thread 
      ( new Runnable() 
      { 
       public void run() 
       { 
        try 
        { 
         new XhtParserView().setVisible(true); 
        } 
        catch (Exception ex) { ex = ex; } 
       }
      } 
      ).start(); 
 }

 private long startTime = 0;
 
 private void beginAction() 
 {
  startTime = GregorianCalendar.getInstance().getTimeInMillis();
  setCursor(new Cursor(Cursor.WAIT_CURSOR));
 }

 private void endAction() 
 {
  try 
  {
   //utl.say("Done in " + ((0.0 + GregorianCalendar.getInstance().getTimeInMillis() - startTime) / 1000) + " seconds."); 
  }
  catch (Exception ex) {}  
  finally
  {
   setCursor(new Cursor(Cursor.DEFAULT_CURSOR)); 
  }
 }

 
 private XhtParser xhtParser = null;
 
 boolean ctinue = false;
 
 public void xhtParser(XhtParser xhtParser) throws Exception
 {
  if (autoEvent > 0) return; autoEvent++;
  try 
  {
   this.xhtParser  = xhtParser;

   txtHtmlCachePath.setText          (xhtParser.cache().BaseFolder().getAbsolutePath()      );
   txtCacheFileNamesPattern.setText  (xhtParser.cache().fileNamesPattern                    );
   txtDefaultTestBaseClass.setText   (xhtParser.defaultMethodClass                          );
   String extParams = "";
   for (String key : xhtParser.extParams.Keys()) extParams += key + " = '" + xhtParser.extParams.g(key).g(1).text().replace("'","&sq;").replace(",","&comma;") + "', ";   
   txtExternalParams.setText         (new Chain(extParams).upto(-3).text()                  );
   txtHtmlDocUrl.setText             (xhtParser.Name()                                      );
   txtCachedFile.setText             (xhtParser.cache().cachedFile.getAbsolutePath()        );
   String selectors = "";
   for (Chain line : xhtParser.selectors) selectors += line.text() + "\n";
   txtXhtSelectors.setText           (new Chain(selectors).upto(-2).text()                  );
   txtTripleDefLeft.setText          (xhtParser.lDef                                        );
   txtTripleDefMid.setText           (xhtParser.mDef                                        );
   txtTripleDefRight.setText         (xhtParser.rDef                                        );
   String[][] data = new String[xhtParser.kVals.g("").Len()][30];
   for (int i = 1; i <= xhtParser.kVals.g("").Len(); i++) 
   {
    data[i - 1][0] = xhtParser.kVals.g("").Keys().g(i);
    for (int j = 1; j <= xhtParser.kVals.g("").g(xhtParser.kVals.g("").Keys().g(i)).Len(); j++) 
     try { data[i - 1][j] = xhtParser.kVals.g("").g(xhtParser.kVals.g("").Keys().g(i)).g(j).text(); }
     catch (Exception ex) {}
   }
   TableModel tm = new DefaultTableModel(data, new String[] {"", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""});
   tblKeyVals.setModel(tm);
   displayHtmlTags(xhtParser);
  }
  catch   (Exception ex) {              }
  finally                { autoEvent--; }
  xhtDefsChanged(null);
  txtCachedFile_TextChanged(null);
  xhtParser.xhtDbgDlg.setLocation(new Point(1150, 600));
  utl.showModalJFrame(this, null, xhtParser.xhtDbgDlg);
 }
 
 //private int recentSourceCode = -1;
 private void xhtSouceCodeSelected() throws Exception
 {
  Chain selectors = Chain.Empty;
  //if (lstXhtSourceCode.getSelectedIndex() == recentSourceCode) return;
  if (autoEvent > 0) return; autoEvent++;
  try
  {
   //recentSourceCode = lstXhtSourceCode.getSelectedIndex();
   txtXhtSelectors.setText("");
   if (lstXhtSourceCode.getSelectedIndex() == -1) return;
   Chain souceCode = new Chain(lstXhtSourceCode.getModel().getElementAt(lstXhtSourceCode.getSelectedIndex())).Trim();
   if (souceCode.before(1, "XhtParser").at(1, "=").len() == 0) souceCode = new Chain("res = ").plus(souceCode);
   txtResVarName.setText(souceCode.before(1, "=").text().trim());
   souceCode = souceCode.after(1, "Tags(").Trim();
   txtHtmlDocUrl.setText(souceCode.after(1, "\"").before(1, "\"").text());
   souceCode = souceCode.after(2, "\"").after(1, ",").Trim();
   txtExternalParams.setText(souceCode.after(1, "\"").before(1, "\"").text());
   souceCode = souceCode.after(2, "\"").after(1, ",").Trim();
   txtHtmlCachePath.setText(souceCode.after(1, "\"").before(1, "\"").text());
   souceCode = souceCode.after(2, "\"").after(1, ",").Trim();
   txtCacheFileNamesPattern.setText(souceCode.after(1, "\"").before(1, "\"").text());
   souceCode = souceCode.after(2, "\"").after(1, ",").Trim();
   txtDefaultTestBaseClass.setText(souceCode.after(1, "\"").before(1, "\"").text());
   souceCode = souceCode.after(2, "\"").after(1, ".select(").Trim();
   Chain selects = souceCode.from(2).before(1, ".triples(").before(-1, "\"");
   while (selects.len() > 0)
   {
    selectors = selectors.plus(new Chain(selects.before(1, "\", \"").text() + "\n"));
    selects = selects.after(1, "\", \"").Trim();
   }
   txtXhtSelectors.setText(selectors.text());
   Chain triples = souceCode.after(1, ".triples(");
   txtTripleDefLeft.setText(triples.after(1, "\"").before(1, "\"").text());
   triples = triples.after(2, "\"").after(1, ",").Trim();
   txtTripleDefMid.setText(triples.after(1, "\"").before(1, "\"").text());
   triples = triples.after(2, "\"").after(1, ",").Trim();
   txtTripleDefRight.setText(triples.after(1, "\"").before(1, "\"").text());
  }
  catch   (Exception ex) {              }
  finally                { autoEvent--; xhtDefsChanged(null);}
 }

   
 Pile<String> xhtSelectors = new Pile<String>();

 private void deleteSourceLine()
 {
  if (lstXhtSourceCode.getSelectedIndex() == -1) return;
  DefaultListModel<String> mdl = (DefaultListModel<String>)lstXhtSourceCode.getModel();
  mdl.remove(lstXhtSourceCode.getSelectedIndex());
  lstXhtSourceCode.setModel(mdl);
 }
 
 private void execSourceLine(String sourceLine) throws Exception
 {
  TableModel tm = tblKeyVals.getModel();
  KeyPile<String, Pile<Chain>> kVals = new KeyPile<String, Pile<Chain>>();
  for (int i = 1; i <= tm.getRowCount(); i++) 
  {
   if (tm.getValueAt(i - 1, 1) != null) 
   {
    kVals.Add((String)tm.getValueAt(i - 1, 0), new Pile<Chain>(0,new Chain((String)tm.getValueAt(i - 1, 1))));
    for (int j = 3; j <= tm.getColumnCount(); j++) 
     if ((tm.getValueAt(i - 1, j - 1) != null) && (((String)tm.getValueAt(i - 1, j - 1)).trim().length() > 0) )
      kVals.g((String)tm.getValueAt(i - 1, 0)).Push(new Chain((String)tm.getValueAt(i - 1, j - 1)));
   }
  }
  xhtParser.kVals.Del("");
  xhtParser.kVals.Add("", kVals);
  //xhtParser.kVals.p(kVals, "");
  xhtParser.Name(txtHtmlDocUrl.getText());
  xhtParser.extParams(txtExternalParams.getText());
  xhtParser.cache().BaseFolder(new File(txtHtmlCachePath.getText()));
  xhtParser.cache().fileNamesPattern = txtCacheFileNamesPattern.getText();
  xhtParser.defaultMethodClass = txtDefaultTestBaseClass.getText();
        
  //xhtParser.cache().cachedFile = new File(txtCachedFile.getText());

  xhtParser.Clear();
  xhtParser.Push(xhtParser.root);
  xhtParser.recentSelectors  = new Pile<Chain>();
  xhtParser.recentResult     = null;        

  xhtParser = xhtParser.select(xhtSelectors.strArray());
  xhtParser.triples(txtTripleDefLeft.getText(), txtTripleDefMid.getText(), txtTripleDefRight.getText());

  //displayHtmlTags(xhtParser);

  /*
  new XhtParser(txtHtmlDocUrl.getText(), txtExternalParams.getText(), txtHtmlCachePath.getText(), txtCacheFileNamesPattern.getText(), txtDefaultTestBaseClass.getText(), kVals, null).select(xhtSelectors.strArray());
  //displayHtmlTags(new XhtParser(txtHtmlDocUrl.getText(), txtExternalParams.getText(), txtHtmlCachePath.getText(), txtCacheFileNamesPattern.getText(), txtDefaultTestBaseClass.getText(), kVals, null).select(xhtSelectors.strArray()));
  */
  
 }

 private void xhtDefsChanged(DocumentEvent evt) throws Exception
 {
  if (autoEvent > 0) return; autoEvent++;
  try
  {
   xhtSelectors = new Pile<String>();
   Chain txt = new Chain(txtXhtSelectors.getText());
   while (txt.len() > 0) {xhtSelectors.Push(txt.before(1, "\n").text().trim()); if (xhtSelectors.g(-1).trim().length() == 0) xhtSelectors.Pop(); txt = txt.after(1, "\n").Trim();}
   if ((xhtSelectors.Len() == 0) && (lstXhtSourceCode.getSelectedIndex() < 0)) return;
   String souceCode = txtResVarName.getText() + " = new XhtParser(\"" + txtHtmlDocUrl.getText() + "\", \"" + txtExternalParams.getText() + "\", \"" + txtHtmlCachePath.getText() + "\", \"" + txtCacheFileNamesPattern.getText() + "\", \"" + txtDefaultTestBaseClass.getText() + "\").select(";
   if (xhtSelectors.Len() == 0) souceCode += ", "; else for (String sel : xhtSelectors) souceCode += "\"" + sel + "\", ";
   souceCode = souceCode.substring(0, souceCode.length() - 2) + ").triples(";
   souceCode = souceCode + "\"" + txtTripleDefLeft.getText() + "\", \"" + txtTripleDefMid.getText() + "\", \"" + txtTripleDefRight.getText() + "\");";
   DefaultListModel<String> mdl = new DefaultListModel();
   for (int i = 1; i <= lstXhtSourceCode.getModel().getSize(); i++) mdl.addElement((String)(lstXhtSourceCode.getModel().getElementAt(i - 1)));
   int inx = 1 + lstXhtSourceCode.getSelectedIndex();
   if (inx == 0)
    {mdl.addElement(souceCode); lstXhtSourceCode.setModel(mdl); /*recentSourceCode = mdl.size() - 1;*/ lstXhtSourceCode.setSelectedIndex(mdl.size() - 1);}
   else
    {if (xhtSelectors.Len() >= 0) mdl.add(inx, souceCode); mdl.remove(inx - 1); lstXhtSourceCode.setModel(mdl); /*recentSourceCode = inx - 1;*/ lstXhtSourceCode.setSelectedIndex(inx - 1);}   
  }
  catch   (Exception ex) {              }
  finally                { autoEvent--; }
 }
         
 private static boolean kbControlPressed = false;

 
         
 public void doCopyFromClipBoard() throws Exception
 {
  String content = lstXhtSourceCode.getModel().toString().replace(");, ", ");\n");
  content = content.substring(1, content.length() - 1);
  StringSelection data = new StringSelection(content);
  clipboard.setContents(data, data);
 }
 
 public void doPasteFromClipBoard() throws Exception
 {
  Chain data = new Chain("");
  try 
  {
   Transferable t = clipboard.getContents(null);
   if (t.isDataFlavorSupported(DataFlavor.stringFlavor)) 
   {
    DefaultListModel<String> m = new DefaultListModel<String>();
    lstXhtSourceCode.setModel(m);
    data = new Chain((String)(t.getTransferData(DataFlavor.stringFlavor))).Trim().plus("\n");
    while (data.len() > 0)
    {
     Chain lineEnd = data.at(" ", 1, "\");");
     Chain line = (lineEnd.skip(" ").len() == 3) ? data.upto(lineEnd) : data;
     data = data.after(line.len()).after(1, "\n").Trim();
     Chain lines = line.Trim();
     line = line.before(1);
     while (lines.len() > 0)
     {
      line = line.plus(new Chain(" ").plus(lines.before(1, "\n").before(1, "\r")).before(1, " //").Trim());
      lines = lines.after(1, "\n").Trim();
     }
     //line = line.skip("\n");
     if (line.len() > 0) m.addElement(line.text());     
    }
    lstXhtSourceCode.setModel(m);
   }
   
  } 
  catch (Exception ex) {}
 }
 
 public XhtParserView() throws Exception
 { 

  /*
  String source = "";  
  utl.say(URLDecoder.decode("C:/XXX/YYY/A/Alfonso_XII_and_Mar%C3%ADa_Cristina.html"    , "UTF-8"));
  utl.say(URLDecoder.decode("Alfonso_XII_and_Mar%C3%ADa_Cristina"                      , "UTF-8"));
  utl.say(URLDecoder.decode("Internet%27s_Own_Boy"                                     , "UTF-8"));
  utl.say(URLDecoder.decode("K%C3%A4the_Schuftan"                                      , "UTF-8"));
  utl.say(URLDecoder.decode("%C3%81ngel_Negro"                                         , "UTF-8"));
  utl.say(URLDecoder.decode("%C5%9Eekerpare_(film)"                                    , "UTF-8"));
  utl.say(URLDecoder.decode("1%25_(film)"                                              , "UTF-8"));
  utl.say(URLDecoder.decode("2%2B2_(2012_film)"                                        , "UTF-8"));
  utl.say(URLDecoder.decode("...%C3%A0_la_campagne"                                    , "UTF-8"));
  utl.say(URLDecoder.decode("100%25_Love_(2012_film)"                                  , "UTF-8"));
  utl.say(URLDecoder.decode("%C2%A1Cuatro!"                                            , "UTF-8"));
  utl.say(URLDecoder.decode("%C3%89vocateur:_The_Morton_Downey_Jr._Movie"              , "UTF-8"));
  utl.say(URLDecoder.decode("Mind_Body_%26_Soul"                                       , "UTF-8"));
  utl.say(URLDecoder.decode("Andr%C3%A9_Derain"                                        , "UTF-8"));
  utl.say(URLDecoder.decode("Alejandro_Obreg%C3%B3n"                                   , "UTF-8"));
  utl.say(URLDecoder.decode("03:34: Earthquake in Chile"                               , "UTF-8"));
  utl.say(URLDecoder.decode("NTFS (utf16) can take everything but : / \\ * ? \" < > |" , "UTF-8"));
  */

  String signature  = "";
  String whatIwant  = "";
  whatIwant  = XhtUrl.fileExtSIG;
  whatIwant  = XhtUrl.reversePathSIG;
  whatIwant  = "{P3}//{P2}";
  whatIwant  = "{{P3}//{P2}}";
  whatIwant  = "{{P2}//{P1}}";
  whatIwant  = "{Ho}";
  whatIwant  = "{Fe}";
  whatIwant  = "{Pa}";
  whatIwant  = "Pa{Pa}{ *{{Pa{P3}}Pa}}";
  whatIwant  = "{Fi}{.{Fe}}";
  whatIwant  = "{Fi[1..2]}/{Fi}{.{Fe}}";
  whatIwant  = XhtUrl.completeSIG;
  signature = new XhtUrl("https://gerald.trost:secret@www.artnet.com:8888/域artists/%C2%A1new/G.T/Gerald Trost.%C3%A4pl?a=1/2&b=2/3&c=3/4#my/<%C3%A4Profile>"   ).signature("((xml))" + whatIwant);
  signature = new XhtUrl("https://gerald.trost:secret@www.artnet.com:8888/域artists/%C2%A1new/Gerald Trost?a=1/2&%C3%A4b=2/3&c=3/4#myProfile"                   ).signature("((fs))"  + whatIwant);
  signature = new XhtUrl("https://gerald.trost:secret@www.artnet.com:8888/域artists/Gerald Trost.pl?a=1/2&b=2/3&c=3/4#%C3%A4myProfile"                          ).signature("((url))" + whatIwant);
  signature = new XhtUrl("https://gerald.trost@www.artnet.com:8888/域artists/Gerald Trost.pl?a=1/2&b=2/3&c=%C3%A43/4#myProfile"                                 ).signature(            whatIwant);
  signature = new XhtUrl("https://www.artnet.com:8888/域artists/Gerald Trost.pl?a=1/2&b=%C3%A42/3&c=3/4#myProfile"                                              ).signature("((xml))" + whatIwant);

  initComponents();  
  domTree.setCellRenderer(new CustomTreeCellRenderer(domTreeElements, txtFilterText, txtFilterHtml));
  domTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

  cx = new ctx();
  db = (Db) cx.Param().g("evDb");
  toolkit = lstXhtSourceCode.getToolkit();      //Toolkit toolkit=Toolkit.getDefaultToolkit();
  clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
  
  
  DefaultMutableTreeNode root = new DefaultMutableTreeNode("body");
  DefaultTreeModel domTreeModel = new DefaultTreeModel(root);
  root.add(new DefaultMutableTreeNode("abc"));
  root.add(new DefaultMutableTreeNode("def"));
  /*  
  for(Attribute att : e.attributes().asList()){ // for each tag get all attributes in one List<Attribute>
        System.out.print("Key: "+att.getKey()+ " , Value: "+att.getValue());
        System.out.println();
  */
  domTree.setModel(domTreeModel);
  lstXhtSourceCode.addKeyListener
  (
   
   new java.awt.event.KeyListener() 
   {
    @Override
    public void keyPressed(KeyEvent e) 
    {
     if (e.getKeyCode() == KeyEvent.VK_CONTROL)    kbControlPressed = true;
     if (e.getKeyCode() == KeyEvent.VK_ENTER)      try {execSourceLine("");                            } catch (Exception ex) {}
     if (e.getKeyCode() == KeyEvent.VK_DELETE)     try {deleteSourceLine();                            } catch (Exception ex) {}
     if (e.getKeyCode() == KeyEvent.VK_V)          try {if (kbControlPressed) doPasteFromClipBoard();  } catch (Exception ex) {}
     if (e.getKeyCode() == KeyEvent.VK_C)          try {if (kbControlPressed) doCopyFromClipBoard();   } catch (Exception ex) {}
    }

    @Override
    public void keyReleased(KeyEvent e) 
    {
      if (e.getKeyCode() == KeyEvent.VK_CONTROL)    kbControlPressed = false;
    }

    @Override
    public void keyTyped(KeyEvent e) 
    {
    }
   }
  );
  
  
  dbDomains  = (KeyPile<String, Long>)cx.Param().g("DBDOMAINS");
  String allDomains = ""; for (String domain : dbDomains.Keys()) allDomains += ", " + domain; 
   
  MouseListener mouseListener = new MouseAdapter() 
  {
   public void mouseClicked(MouseEvent mouseEvent)
   {
    JList theList = (JList) mouseEvent.getSource();
    String souceLine = "";
    if (mouseEvent.getClickCount() == 2) 
    {
     int index = theList.locationToIndex(mouseEvent.getPoint());
     if (index >= 0) souceLine = (String)theList.getModel().getElementAt(index);
     try {execSourceLine(souceLine);} catch (Exception ex) 
     {
        Logger.getLogger(XhtParserView.class.getName()).log(Level.SEVERE, null, ex);
     }
    }
   }
  };
  lstXhtSourceCode.addMouseListener(mouseListener);

  txtExternalParams.getDocument().addDocumentListener
  (new DocumentListener() 
   {
    @Override public void insertUpdate   (DocumentEvent evt) {try { xhtDefsChanged(evt); } catch (Exception ex) { Logger.getLogger(XhtParserView.class.getName()).log(Level.SEVERE, null, ex); }}
    @Override public void removeUpdate   (DocumentEvent evt) {try { xhtDefsChanged(evt); } catch (Exception ex) { Logger.getLogger(XhtParserView.class.getName()).log(Level.SEVERE, null, ex); }}
    @Override public void changedUpdate  (DocumentEvent evt) {try { xhtDefsChanged(evt); } catch (Exception ex) { Logger.getLogger(XhtParserView.class.getName()).log(Level.SEVERE, null, ex); }}
   }
  );
  
  txtTripleDefLeft.getDocument().addDocumentListener
  (new DocumentListener() 
   {
    @Override public void insertUpdate   (DocumentEvent evt) {try { xhtDefsChanged(evt); } catch (Exception ex) { Logger.getLogger(XhtParserView.class.getName()).log(Level.SEVERE, null, ex); }}
    @Override public void removeUpdate   (DocumentEvent evt) {try { xhtDefsChanged(evt); } catch (Exception ex) { Logger.getLogger(XhtParserView.class.getName()).log(Level.SEVERE, null, ex); }}
    @Override public void changedUpdate  (DocumentEvent evt) {try { xhtDefsChanged(evt); } catch (Exception ex) { Logger.getLogger(XhtParserView.class.getName()).log(Level.SEVERE, null, ex); }}
   }
  );

  txtTripleDefMid.getDocument().addDocumentListener
  (new DocumentListener() 
   {
    @Override public void insertUpdate   (DocumentEvent evt) {try { xhtDefsChanged(evt); } catch (Exception ex) { Logger.getLogger(XhtParserView.class.getName()).log(Level.SEVERE, null, ex); }}
    @Override public void removeUpdate   (DocumentEvent evt) {try { xhtDefsChanged(evt); } catch (Exception ex) { Logger.getLogger(XhtParserView.class.getName()).log(Level.SEVERE, null, ex); }}
    @Override public void changedUpdate  (DocumentEvent evt) {try { xhtDefsChanged(evt); } catch (Exception ex) { Logger.getLogger(XhtParserView.class.getName()).log(Level.SEVERE, null, ex); }}
   }
  );

  txtTripleDefRight.getDocument().addDocumentListener
  (new DocumentListener() 
   {
    @Override public void insertUpdate   (DocumentEvent evt) {try { xhtDefsChanged(evt); } catch (Exception ex) { Logger.getLogger(XhtParserView.class.getName()).log(Level.SEVERE, null, ex); }}
    @Override public void removeUpdate   (DocumentEvent evt) {try { xhtDefsChanged(evt); } catch (Exception ex) { Logger.getLogger(XhtParserView.class.getName()).log(Level.SEVERE, null, ex); }}
    @Override public void changedUpdate  (DocumentEvent evt) {try { xhtDefsChanged(evt); } catch (Exception ex) { Logger.getLogger(XhtParserView.class.getName()).log(Level.SEVERE, null, ex); }}
   }
  );

  txtHtmlDocUrl.getDocument().addDocumentListener
  (new DocumentListener() 
   {
    @Override public void insertUpdate   (DocumentEvent evt) {try { xhtDefsChanged(evt); } catch (Exception ex) { Logger.getLogger(XhtParserView.class.getName()).log(Level.SEVERE, null, ex); }}
    @Override public void removeUpdate   (DocumentEvent evt) {try { xhtDefsChanged(evt); } catch (Exception ex) { Logger.getLogger(XhtParserView.class.getName()).log(Level.SEVERE, null, ex); }}
    @Override public void changedUpdate  (DocumentEvent evt) {try { xhtDefsChanged(evt); } catch (Exception ex) { Logger.getLogger(XhtParserView.class.getName()).log(Level.SEVERE, null, ex); }}
   }
  );

  txtCacheFileNamesPattern.getDocument().addDocumentListener
  (new DocumentListener() 
   {
    @Override public void insertUpdate   (DocumentEvent evt) {try { xhtDefsChanged(evt); } catch (Exception ex) { Logger.getLogger(XhtParserView.class.getName()).log(Level.SEVERE, null, ex); }}
    @Override public void removeUpdate   (DocumentEvent evt) {try { xhtDefsChanged(evt); } catch (Exception ex) { Logger.getLogger(XhtParserView.class.getName()).log(Level.SEVERE, null, ex); }}
    @Override public void changedUpdate  (DocumentEvent evt) {try { xhtDefsChanged(evt); } catch (Exception ex) { Logger.getLogger(XhtParserView.class.getName()).log(Level.SEVERE, null, ex); }}
   }
  );

  txtHtmlCachePath.getDocument().addDocumentListener
  (new DocumentListener() 
   {
    @Override public void insertUpdate   (DocumentEvent evt) {try { xhtDefsChanged(evt); } catch (Exception ex) { Logger.getLogger(XhtParserView.class.getName()).log(Level.SEVERE, null, ex); }}
    @Override public void removeUpdate   (DocumentEvent evt) {try { xhtDefsChanged(evt); } catch (Exception ex) { Logger.getLogger(XhtParserView.class.getName()).log(Level.SEVERE, null, ex); }}
    @Override public void changedUpdate  (DocumentEvent evt) {try { xhtDefsChanged(evt); } catch (Exception ex) { Logger.getLogger(XhtParserView.class.getName()).log(Level.SEVERE, null, ex); }}
   }
  );

  txtDefaultTestBaseClass.getDocument().addDocumentListener
  (new DocumentListener() 
   {
    @Override public void insertUpdate   (DocumentEvent evt) {try { xhtDefsChanged(evt); } catch (Exception ex) { Logger.getLogger(XhtParserView.class.getName()).log(Level.SEVERE, null, ex); }}
    @Override public void removeUpdate   (DocumentEvent evt) {try { xhtDefsChanged(evt); } catch (Exception ex) { Logger.getLogger(XhtParserView.class.getName()).log(Level.SEVERE, null, ex); }}
    @Override public void changedUpdate  (DocumentEvent evt) {try { xhtDefsChanged(evt); } catch (Exception ex) { Logger.getLogger(XhtParserView.class.getName()).log(Level.SEVERE, null, ex); }}
   }
  );

  txtResVarName.getDocument().addDocumentListener
  (new DocumentListener() 
   {
    @Override public void insertUpdate   (DocumentEvent evt) {try { xhtDefsChanged(evt); } catch (Exception ex) { Logger.getLogger(XhtParserView.class.getName()).log(Level.SEVERE, null, ex); }}
    @Override public void removeUpdate   (DocumentEvent evt) {try { xhtDefsChanged(evt); } catch (Exception ex) { Logger.getLogger(XhtParserView.class.getName()).log(Level.SEVERE, null, ex); }}
    @Override public void changedUpdate  (DocumentEvent evt) {try { xhtDefsChanged(evt); } catch (Exception ex) { Logger.getLogger(XhtParserView.class.getName()).log(Level.SEVERE, null, ex); }}
   }
  );

  txtXhtSelectors.getDocument().addDocumentListener
  (new DocumentListener() 
   {
    @Override public void insertUpdate   (DocumentEvent evt) {try { xhtDefsChanged(evt); } catch (Exception ex) { Logger.getLogger(XhtParserView.class.getName()).log(Level.SEVERE, null, ex); }}
    @Override public void removeUpdate   (DocumentEvent evt) {try { xhtDefsChanged(evt); } catch (Exception ex) { Logger.getLogger(XhtParserView.class.getName()).log(Level.SEVERE, null, ex); }}
    @Override public void changedUpdate  (DocumentEvent evt) {try { xhtDefsChanged(evt); } catch (Exception ex) { Logger.getLogger(XhtParserView.class.getName()).log(Level.SEVERE, null, ex); }}
   }
  );
  
  txtCachedFile.getDocument().addDocumentListener
  (new DocumentListener() 
   {
    @Override public void insertUpdate   (DocumentEvent evt) {try { txtCachedFile_TextChanged(evt); } catch (Exception ex) { ex = ex; }}
    @Override public void removeUpdate   (DocumentEvent evt) {try { txtCachedFile_TextChanged(evt); } catch (Exception ex) { ex = ex; }}
    @Override public void changedUpdate  (DocumentEvent evt) {try { txtCachedFile_TextChanged(evt); } catch (Exception ex) { ex = ex; }}
   }
  );

  txtFilterText.getDocument().addDocumentListener
  (new DocumentListener() 
   {
    @Override public void insertUpdate   (DocumentEvent evt) {try { txtFilterText_TextChanged(evt); } catch (Exception ex) { ex = ex; }}
    @Override public void removeUpdate   (DocumentEvent evt) {try { txtFilterText_TextChanged(evt); } catch (Exception ex) { ex = ex; }}
    @Override public void changedUpdate  (DocumentEvent evt) {try { txtFilterText_TextChanged(evt); } catch (Exception ex) { ex = ex; }}
   }
  );

  txtFilterHtml.getDocument().addDocumentListener
  (new DocumentListener() 
   {
    @Override public void insertUpdate   (DocumentEvent evt) {try { txtFilterHtml_TextChanged(evt); } catch (Exception ex) { ex = ex; }}
    @Override public void removeUpdate   (DocumentEvent evt) {try { txtFilterHtml_TextChanged(evt); } catch (Exception ex) { ex = ex; }}
    @Override public void changedUpdate  (DocumentEvent evt) {try { txtFilterHtml_TextChanged(evt); } catch (Exception ex) { ex = ex; }}
   }
  );

  xhtDefsChanged(null);
 }    

 Pile<Object[]> domTreeElements = new Pile<Object[]>();

 private void addDomTree(DefaultMutableTreeNode node, Element e) throws Exception
 {
  String eName   = " \"" + e.attr("name")   + "\""   ; if (eName.length()   <= 3)  eName = "";
  String eId     = " #" + e.id()           + ""      ; if (eId.length()     <= 2)    eId = "";
  String eClass  = " ." + e.classNames()   + ""      ; if (eClass.length()  <= 4) eClass = "";
  String attribs = "";
  for (Attribute att : e.attributes()) if ((!att.getKey().toLowerCase().equals("name")) && (!att.getKey().toLowerCase().equals("id")) && (!att.getKey().toLowerCase().equals("class"))) attribs += att.getKey() + " = \"" + att.getValue() + "\", ";
  if (attribs.length() > 2) attribs = attribs.substring(0, attribs.length() - 2);
  DefaultMutableTreeNode eNode = new DefaultMutableTreeNode("<" + e.tagName() + eName + eId + eClass + " " + attribs + ">");
  domTreeElements.Push(new Object[] {eNode, e});
  // eNode.setUserObject(e);
  node.add(eNode);  
  for (Element c : e.children()) addDomTree(eNode, c);
 }
 
 private void showDomTree() throws Exception
 {
  domTreeElements.Clear();
  Element body = Jsoup.parse(new File(txtCachedFile.getText()), "UTF-8").select("body").first();
  DefaultMutableTreeNode root = new DefaultMutableTreeNode("body");
  DefaultTreeModel domTreeModel = new DefaultTreeModel(root);

  for (Element c : body.children()) addDomTree(root, c);
  domTree.setModel(domTreeModel);
  /*  
  for(Attribute att : e.attributes().asList()){ // for each tag get all attributes in one List<Attribute>
        System.out.print("Key: "+att.getKey()+ " , Value: "+att.getValue());
        System.out.println();
  */
 }
 
 
     
    

 
 private void txtFilterText_TextChanged(DocumentEvent evt) throws Exception
 {
  String filter = txtFilterText.getText().toLowerCase().trim();
  if (filter.length()== 0) return;
  expandTree(domTree, false);
  for(Object[] o : domTreeElements)
  {
   if (((Element)o[1]).text().toLowerCase().contains(filter))
   {
    TreeNode node = (TreeNode)o[0];
    TreePath tp = utl.treePath(node);
    domTree.expandPath(tp);
   }
  }  
  domTree.repaint();
 }
 
 private void txtFilterHtml_TextChanged(DocumentEvent evt) throws Exception
 {
  String filter = txtFilterHtml.getText().toLowerCase().trim();
  if (filter.length()== 0) return;
  expandTree(domTree, false);
  for(Object[] o : domTreeElements)
  {
   if (((Element)o[1]).html().toLowerCase().contains(filter))
   {
    TreeNode node = (TreeNode)o[0];
    TreePath tp = utl.treePath(node);
    domTree.expandPath(tp);
   }
  }
  domTree.repaint();
 }

 private void txtCachedFile_TextChanged(DocumentEvent evt) throws Exception
 {
  if (autoEvent > 0) return;
  xhtParser.cache().cachedFile = new File(txtCachedFile.getText());
  txtDomTreeText.setText("");
  txtDomTreeHtml.setText("");
  txtFilterText.setText("");  
  txtFilterHtml.setText("");  
  txtCachedFileSize.setText("");
  try { txtCachedFileSize.setText("" + xhtParser.cache().cachedFile.length());} catch (Exception ex) {}
  DefaultMutableTreeNode root = new DefaultMutableTreeNode("body");
  DefaultTreeModel domTreeModel = new DefaultTreeModel(root);
  domTree.setModel(domTreeModel);
  try 
  {
   showDomTree();
  }
  catch (Exception ex) {}
 }

 public PropertyChangeListener dividerChanged = 
  new PropertyChangeListener() 
  {
   @Override 
   public void propertyChange(PropertyChangeEvent pce) 
   {
    defaultDividerLocation = ((JSplitPane)pce.getSource()).getDividerLocation();
    for (Component c: pnlHtmlTags.getComponents()) if (c instanceof JSplitPane) ((JSplitPane)c).setDividerLocation(defaultDividerLocation );
   }
  };
 
 
 private void displayHtmlTags(XhtParser xhtParser) throws Exception
 {
  pnlHtmlTags.setVisible(false);
  for (Component c: pnlHtmlTags.getComponents()) pnlHtmlTags.remove(c);
  
  Object[]    colunas = new String[]{"Triple.Left","Triple.Mid", "Triple.Right"};
		Object[][]  dados   = new Object[][]{};
  DefaultTableModel m = new DefaultTableModel(dados , colunas); 
  tblTriples.setModel(m);
  //lstTriples.setLayoutOrientation(JList.HORIZONTAL_WRAP);
  GroupLayout pnlHtmlTagsLayout = new GroupLayout(pnlHtmlTags);
  pnlHtmlTags.setLayout(pnlHtmlTagsLayout);

  GroupLayout.ParallelGroup    hGroup = pnlHtmlTagsLayout.createParallelGroup(GroupLayout.LEADING);
  GroupLayout.SequentialGroup  vGroup = pnlHtmlTagsLayout.createSequentialGroup();
  int ctr = 0;
  for (Element el: xhtParser)
  {
   JTextArea txtHtmlTags = new JTextArea(el.outerHtml()  ); txtHtmlTags.setFont (new java.awt.Font("Monospaced"      , 0, 10)); // NOI18N
   JTextArea txtTagVal   = new JTextArea(el.text()       ); txtTagVal.setFont   (new java.awt.Font("Times New Roman" , 0, 12)); // NOI18N
   //JTextArea txtTagVal   = new JTextArea(el.text()       ); txtTagVal.setFont   (new java.awt.Font("Iskoola Pota", 0, 12)); // NOI18N
   if (ctr++ % 2 == 1) txtHtmlTags.setBackground(new Color(251,249,249)); else txtHtmlTags.setBackground(new Color(249,251,251)); 
   txtTagVal.setBackground(txtHtmlTags.getBackground());
   txtHtmlTags.setLineWrap(false); txtHtmlTags.setRows (txtHtmlTags.getDocument().getDefaultRootElement().getElementCount()); JScrollPane sclHtmlTags = new JScrollPane(txtHtmlTags );
   txtHtmlTags.setLineWrap(true);    
   txtTagVal.setLineWrap(true);    txtTagVal.setRows   (txtTagVal.getDocument().getDefaultRootElement().getElementCount());   JScrollPane sclTagVal   = new JScrollPane(txtTagVal   );

   JSplitPane sltLine = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, sclHtmlTags, sclTagVal);
   sltLine.setResizeWeight(0.5);
   sltTriples.setResizeWeight(0.5);
   sltLine.setDividerLocation(defaultDividerLocation);
   sltLine.addPropertyChangeListener(JSplitPane.DIVIDER_LOCATION_PROPERTY, dividerChanged);
   //sclTagVal.setPreferredSize   (new Dimension(300        ,  -1));
   //sclHtmlTags.setPreferredSize (new Dimension(1258 - 300 ,  -1));
   hGroup = hGroup.add(sltLine, -1, 1700,    1700);
   vGroup = vGroup.add(sltLine, 80,   -1,     450);
  }
  pnlHtmlTagsLayout.setHorizontalGroup(hGroup);
  pnlHtmlTagsLayout.setVerticalGroup(pnlHtmlTagsLayout.createParallelGroup(GroupLayout.LEADING).add(vGroup.add(450, 450, Short.MAX_VALUE)));
  pack();
  //pnlHtmlTags.setSize(new Dimension(sclHtmlTags.WIDTH - 50, sclHtmlTags.HEIGHT - 50));
  pnlHtmlTags.setVisible(true);
  pnlHtmlTags.repaint();
  colunas = new String[]{"Triple.Left","Triple.Mid", "Triple.Right"};
  m = new DefaultTableModel(null, colunas); 
  //for (Triple tr: xhtParser.triples(txtTripleDefLeft.getText(), txtTripleDefMid.getText(), txtTripleDefRight.getText())) 
  for (Triple tr: xhtParser.triples) 
  {
   Chain row = new Chain(tr.toString("\t|\t"));
   m.addRow(new String[] {row.before(1, "\t|\t").text(), row.after(1, "\t|\t").before(1, "\t|\t").text(), row.after(2, "\t|\t").text()});
  }
  tblTriples.setModel(m);
  tblTriples.setPreferredScrollableViewportSize(tblTriples.getParent().getSize());
 }

 
 private void expandTree(JTree tree, boolean expand) 
 {
  TreeNode root = (TreeNode) tree.getModel().getRoot();
  expandAll(tree, new TreePath(root), expand);
 }
 
 private void expandAll(JTree tree, TreePath path, boolean expand) 
 {
  TreeNode node = (TreeNode) path.getLastPathComponent();
  if (node.getChildCount() >= 0) 
  {
   Enumeration enumeration = node.children();
   while (enumeration.hasMoreElements()) 
   {
    TreeNode n = (TreeNode) enumeration.nextElement();
    TreePath p = path.pathByAddingChild(n);
    expandAll(tree, p, expand);
   }
  }
  if (expand) 
  {
   tree.expandPath(path);
  } 
  else 
  {
   tree.collapsePath(path);
  }
 }
 
 private void domTreeSelectionChanged(TreeSelectionEvent evt) throws Exception
 {
  TreePath        path  = evt.getPath();
  Object          node  = path.getLastPathComponent();
  Element el = null;
  for(Object[] o : domTreeElements)
  {
   if (o[0] == node)
   {
    el = (Element) o[1];
    break;
   }
  }
  txtDomTreeText.setText(el.text());
  if (txtFilterText.getText().trim().length() > 0)
  {
   hilit = txtDomTreeText.getHighlighter();
   String search = txtFilterText.getText().toLowerCase().trim();
   String data   = txtDomTreeText.getText().toLowerCase().trim();
   int    index  = data.indexOf(search);
   while( index >= 0)
   {
    try 
    {
     Rectangle rect = txtDomTreeText.modelToView(index);
     txtDomTreeText.scrollRectToVisible(rect);
    } catch (Exception e1) {}
    hilit.addHighlight(index, index + search.length(), painter);
    index = data.indexOf(search, index + search.length());
   }
  }
  txtDomTreeHtml.setText(el.html());
  if (txtFilterHtml.getText().trim().length() > 0)
  {
   hilit = txtDomTreeHtml.getHighlighter();
   String search = txtFilterHtml.getText().toLowerCase().trim();
   String data   = txtDomTreeHtml.getText().toLowerCase().trim();
   int    index  = data.indexOf(search);
   while( index >= 0)
   {
    try 
    {
     Rectangle rect = txtDomTreeHtml.modelToView(index);
     txtDomTreeHtml.scrollRectToVisible(rect);
    } catch (Exception e1) {}
    hilit.addHighlight(index, index + search.length(), painter);
    index = data.indexOf(search, index + search.length());
   }
  }
 }

 
 private void btnReDldJsoup_click(ActionEvent evt) throws Exception
 {
  File cf = xhtParser.cache().cachedFile;
  cf.delete();
  int[] dldOption = new int[] {1};
  xhtParser.cache().getDocument(xhtParser.Name(), dldOption);
  txtCachedFile.setText (xhtParser.cache().cachedFile.getAbsolutePath());
 }
 
 private void btnReDldWebView_click(ActionEvent evt) throws Exception
 {
  setState(JFrame.ICONIFIED); 
  try 
  {
   File cf = xhtParser.cache().cachedFile;
   cf.delete();
   int[] dldOption = new int[] {2};
   xhtParser.cache().getDocument(xhtParser.Name(), dldOption);
  }
  catch (Exception ex) {}
  finally { setExtendedState(JFrame.MAXIMIZED_BOTH);}
  txtCachedFile.setText (xhtParser.cache().cachedFile.getAbsolutePath());
 }
 
 private void btnReDldAhkOpera_click(ActionEvent evt) throws Exception
 {
  setState(JFrame.ICONIFIED); 
  try 
  {
   File cf = xhtParser.cache().cachedFile;
   cf.delete();
   int[] dldOption = new int[] {3};
   xhtParser.cache().getDocument(xhtParser.Name(), dldOption);
  }
  catch (Exception ex) {}
  finally { setExtendedState(JFrame.MAXIMIZED_BOTH);}
  txtCachedFile.setText (xhtParser.cache().cachedFile.getAbsolutePath());
 }
  

        
 
 
 
 // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
 private void initComponents() {

  buttonGroup1 = new javax.swing.ButtonGroup();
  jDialog1 = new javax.swing.JDialog();
  jTabbedPane1 = new javax.swing.JTabbedPane();
  jPanel3 = new javax.swing.JPanel();
  jPanel7 = new javax.swing.JPanel();
  txtHtmlDocUrl = new javax.swing.JTextField();
  lblDomains2 = new javax.swing.JLabel();
  txtHtmlCachePath = new javax.swing.JTextField();
  lblDomains14 = new javax.swing.JLabel();
  lblDomains13 = new javax.swing.JLabel();
  txtResVarName = new javax.swing.JTextField();
  txtTripleDefLeft = new javax.swing.JTextField();
  lblDomains18 = new javax.swing.JLabel();
  lblDomains19 = new javax.swing.JLabel();
  txtCacheFileNamesPattern = new javax.swing.JTextField();
  lblDomains21 = new javax.swing.JLabel();
  txtDefaultTestBaseClass = new javax.swing.JTextField();
  lblDomains3 = new javax.swing.JLabel();
  txtTripleDefMid = new javax.swing.JTextField();
  txtTripleDefRight = new javax.swing.JTextField();
  jScrollPane15 = new javax.swing.JScrollPane();
  txtXhtSelectors = new javax.swing.JTextArea();
  jSplitPane1 = new javax.swing.JSplitPane();
  jScrollPane1 = new javax.swing.JScrollPane();
  jTabbedPane2 = new javax.swing.JTabbedPane();
  lstXhtSourceCode = new javax.swing.JList<>();
  tblKeyVals = new javax.swing.JTable();
  sltTriples = new javax.swing.JSplitPane();
  sclHtmlTags = new javax.swing.JScrollPane();
  pnlHtmlTags = new javax.swing.JPanel();
  jScrollPane2 = new javax.swing.JScrollPane();
  sclTriples = new javax.swing.JScrollPane();
  tblTriples = new javax.swing.JTable();
  lblDomains22 = new javax.swing.JLabel();
  lblDomains15 = new javax.swing.JLabel();
  jScrollPane16 = new javax.swing.JScrollPane();
  txtExternalParams = new javax.swing.JTextArea();
  jPanel4 = new javax.swing.JPanel();
  jPanel8 = new javax.swing.JPanel();
  txtCachedFile = new javax.swing.JTextField();
  lblDomains4 = new javax.swing.JLabel();
  lblDomains23 = new javax.swing.JLabel();
  jSplitPane2 = new javax.swing.JSplitPane();
  sltTriples1 = new javax.swing.JSplitPane();
  sclHtmlTags1 = new javax.swing.JScrollPane();
  domTree = new javax.swing.JTree();
  jSplitPane3 = new javax.swing.JSplitPane();
  jPanel11 = new javax.swing.JPanel();
  txtFilterText = new javax.swing.JTextField();
  jScrollPane5 = new javax.swing.JScrollPane();
  txtDomTreeText = new javax.swing.JTextArea();
  jPanel12 = new javax.swing.JPanel();
  jPanel13 = new javax.swing.JPanel();
  txtFilterHtml = new javax.swing.JTextField();
  jScrollPane6 = new javax.swing.JScrollPane();
  txtDomTreeHtml = new javax.swing.JTextArea();
  jTabbedPane3 = new javax.swing.JTabbedPane();
  jPanel1 = new javax.swing.JPanel();
  lblDomains5 = new javax.swing.JLabel();
  btnReDldJsoup = new javax.swing.JButton();
  btnReDldWebView = new javax.swing.JButton();
  btnReDldAhkOpera = new javax.swing.JButton();
  lblDomains6 = new javax.swing.JLabel();
  txtCachedFileSize = new javax.swing.JTextField();

  org.jdesktop.layout.GroupLayout jDialog1Layout = new org.jdesktop.layout.GroupLayout(jDialog1.getContentPane());
  jDialog1.getContentPane().setLayout(jDialog1Layout);
  jDialog1Layout.setHorizontalGroup(
   jDialog1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
   .add(0, 400, Short.MAX_VALUE)
  );
  jDialog1Layout.setVerticalGroup(
   jDialog1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
   .add(0, 300, Short.MAX_VALUE)
  );

  setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
  setTitle("XhtParser DebugView");
  setSize(new java.awt.Dimension(1200, 900));

  jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(" Name "));
  jPanel3.setAlignmentX(0);
  jPanel3.setAlignmentY(0);

  jPanel7.setAlignmentX(0);
  jPanel7.setAlignmentY(0);

  txtHtmlDocUrl.setBackground(new java.awt.Color(245, 245, 255));
  txtHtmlDocUrl.setText("http://www.artnet.com/artists/gerhard-richter");
  txtHtmlDocUrl.setToolTipText("<html>\n<b>utl for the current sourcecode line</b>\n</html>\n");
  txtHtmlDocUrl.addActionListener(new java.awt.event.ActionListener() {
   public void actionPerformed(java.awt.event.ActionEvent evt) {
    txtHtmlDocUrlActionPerformed(evt);
   }
  });

  lblDomains2.setText("Url");

  txtHtmlCachePath.setText("c:/ev.cache/");
  txtHtmlCachePath.setToolTipText("<html>\n<b>path for caching html-items</b>\n</html>\n");
  txtHtmlCachePath.addActionListener(new java.awt.event.ActionListener() {
   public void actionPerformed(java.awt.event.ActionEvent evt) {
    txtHtmlCachePathActionPerformed(evt);
   }
  });

  lblDomains14.setText("CachePath");

  lblDomains13.setText("ResVariable");

  txtResVarName.setText("Triples facts");
  txtResVarName.setToolTipText("<html>\n<b>path for caching html-items</b>\n</html>\n");
  txtResVarName.addActionListener(new java.awt.event.ActionListener() {
   public void actionPerformed(java.awt.event.ActionEvent evt) {
    txtResVarNameActionPerformed(evt);
   }
  });

  txtTripleDefLeft.setBackground(new java.awt.Color(255, 245, 245));
  txtTripleDefLeft.setText(":id.id");
  txtTripleDefLeft.setToolTipText("<html>\n<b>path for caching html-items</b>\n</html>\n");
  txtTripleDefLeft.addActionListener(new java.awt.event.ActionListener() {
   public void actionPerformed(java.awt.event.ActionEvent evt) {
    txtTripleDefLeftActionPerformed(evt);
   }
  });

  lblDomains19.setText("FuncMeth Class");

  txtCacheFileNamesPattern.setText("((xml))artworks/{Fn[1..2]}/{Fn}{.{Fe}}.html");
  txtCacheFileNamesPattern.setToolTipText("<html>\n<b>path for caching html-items</b>\n</html>\n");
  txtCacheFileNamesPattern.addActionListener(new java.awt.event.ActionListener() {
   public void actionPerformed(java.awt.event.ActionEvent evt) {
    txtCacheFileNamesPatternActionPerformed(evt);
   }
  });

  lblDomains21.setText("FileNames Pattern");

  txtDefaultTestBaseClass.setText("org.everest.base.StringMethods");
  txtDefaultTestBaseClass.setToolTipText("<html>\n<b>path for caching html-items</b>\n</html>\n");
  txtDefaultTestBaseClass.addActionListener(new java.awt.event.ActionListener() {
   public void actionPerformed(java.awt.event.ActionEvent evt) {
    txtDefaultTestBaseClassActionPerformed(evt);
   }
  });

  lblDomains3.setText("Triple Result");

  txtTripleDefMid.setBackground(new java.awt.Color(255, 245, 245));
  txtTripleDefMid.setText("'atr:' <<org.everest.base.StringMethods.GetAttrName>> 'test'");
  txtTripleDefMid.setToolTipText("<html>\n<b>path for caching html-items</b>\n</html>\n");
  txtTripleDefMid.addActionListener(new java.awt.event.ActionListener() {
   public void actionPerformed(java.awt.event.ActionEvent evt) {
    txtTripleDefMidActionPerformed(evt);
   }
  });

  txtTripleDefRight.setBackground(new java.awt.Color(255, 245, 245));
  txtTripleDefRight.setText(":title{' at '.text()}");
  txtTripleDefRight.setToolTipText("<html>\n<b>path for caching html-items</b>\n</html>\n");
  txtTripleDefRight.addActionListener(new java.awt.event.ActionListener() {
   public void actionPerformed(java.awt.event.ActionEvent evt) {
    txtTripleDefRightActionPerformed(evt);
   }
  });

  txtXhtSelectors.setBackground(new java.awt.Color(245, 245, 255));
  txtXhtSelectors.setColumns(20);
  txtXhtSelectors.setFont(new java.awt.Font("Monospaced", 0, 12)); // NOI18N
  txtXhtSelectors.setRows(5);
  txtXhtSelectors.setText("[1..50, .id != 'abc'.text(), '' == <<YearInText>> ]div\n");
  txtXhtSelectors.setToolTipText("<html>\n<b>cascading selectors for the sourcecode line</b>\n<br><br>\nde-select a soucecode line with <b>ctrl-click</b> and then add something in selector box in order to start a new line<br>\n<b>double click</b> a soucecode line will execute the sourcecode line<br>\n<b>emptying</b> the selectors box will delete the current soucecode line<br>\n</html>");
  jScrollPane15.setViewportView(txtXhtSelectors);

  jSplitPane1.setDividerLocation(160);
  jSplitPane1.setDividerSize(4);
  jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
  jSplitPane1.setPreferredSize(new java.awt.Dimension(1200, 800));

  jTabbedPane2.setTabPlacement(javax.swing.JTabbedPane.LEFT);

  lstXhtSourceCode.setFont(new java.awt.Font("DejaVu Serif Condensed", 0, 10)); // NOI18N
  lstXhtSourceCode.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
  lstXhtSourceCode.setToolTipText("<html>\n<b>list of java sourcecode lines</b>\n<br><br>\nde-select a line with <b><CTRL>-click</b> and then add an entry in selector box in order to start a new line\n<br><br>\n<b>double click or press <ENTER></b> a line in order to execute the source line\n<CTRL><C> will copy all if no line is selected or will copy the selected line\n<CTRL><V> will paste the lines in the ClibBoard\n</html>\n");
  lstXhtSourceCode.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
   public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
    lstXhtSourceCodeValueChanged(evt);
   }
  });
  jTabbedPane2.addTab("src", lstXhtSourceCode);

  tblKeyVals.setAutoCreateRowSorter(true);
  tblKeyVals.setModel(new javax.swing.table.DefaultTableModel(
   new Object [][] {
    {"artnet|Artnet", "wpID:artnet"},
    {"home|Home", "uvID:home"},
    {"login|Login", "uvID:login"}
   },
   new String [] {
    "key", "val"
   }
  ));
  tblKeyVals.setDoubleBuffered(true);
  tblKeyVals.setFillsViewportHeight(true);
  tblKeyVals.setFocusCycleRoot(true);
  tblKeyVals.setGridColor(new java.awt.Color(238, 238, 238));
  tblKeyVals.setInheritsPopupMenu(true);
  tblKeyVals.setPreferredSize(new java.awt.Dimension(800, 500));
  tblKeyVals.setShowHorizontalLines(false);
  tblKeyVals.setShowVerticalLines(false);
  jTabbedPane2.addTab("kv", tblKeyVals);

  jScrollPane1.setViewportView(jTabbedPane2);

  jSplitPane1.setLeftComponent(jScrollPane1);

  sltTriples.setDividerLocation(1000);
  sltTriples.setDividerSize(4);

  pnlHtmlTags.setBackground(new java.awt.Color(255, 255, 255));

  org.jdesktop.layout.GroupLayout pnlHtmlTagsLayout = new org.jdesktop.layout.GroupLayout(pnlHtmlTags);
  pnlHtmlTags.setLayout(pnlHtmlTagsLayout);
  pnlHtmlTagsLayout.setHorizontalGroup(
   pnlHtmlTagsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
   .add(0, 0, Short.MAX_VALUE)
  );
  pnlHtmlTagsLayout.setVerticalGroup(
   pnlHtmlTagsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
   .add(0, 0, Short.MAX_VALUE)
  );

  sclHtmlTags.setViewportView(pnlHtmlTags);

  sltTriples.setLeftComponent(sclHtmlTags);

  sclTriples.setDoubleBuffered(true);

  tblTriples.setAutoCreateRowSorter(true);
  tblTriples.setBackground(new java.awt.Color(255, 245, 245));
  tblTriples.setModel(new javax.swing.table.DefaultTableModel(
   new Object [][] {

   },
   new String [] {
    "Title 1", "Title 2", "Title 3"
   }
  ));
  tblTriples.setDoubleBuffered(true);
  tblTriples.setFillsViewportHeight(true);
  tblTriples.setFocusCycleRoot(true);
  tblTriples.setGridColor(new java.awt.Color(238, 238, 238));
  tblTriples.setInheritsPopupMenu(true);
  tblTriples.setPreferredSize(new java.awt.Dimension(800, 500));
  tblTriples.setShowHorizontalLines(false);
  tblTriples.setShowVerticalLines(false);
  sclTriples.setViewportView(tblTriples);

  jScrollPane2.setViewportView(sclTriples);

  sltTriples.setRightComponent(jScrollPane2);

  jSplitPane1.setRightComponent(sltTriples);

  lblDomains22.setText("UrlData");

  lblDomains15.setText("Selectors");

  txtExternalParams.setBackground(new java.awt.Color(245, 245, 255));
  txtExternalParams.setColumns(20);
  txtExternalParams.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
  txtExternalParams.setLineWrap(true);
  txtExternalParams.setRows(5);
  txtExternalParams.setText("id = 'wpID:Gerhard_Richter', title = 'Gerhard Richter', org = 'distribution@artworks.com'\n");
  txtExternalParams.setToolTipText("<html>\n<b>cascading selectors for the sourcecode line</b>\n<br><br>\nde-select a soucecode line with <b>ctrl-click</b> and then add something in selector box in order to start a new line<br>\n<b>double click</b> a soucecode line will execute the sourcecode line<br>\n<b>emptying</b> the selectors box will delete the current soucecode line<br>\n</html>");
  txtExternalParams.setWrapStyleWord(true);
  jScrollPane16.setViewportView(txtExternalParams);

  org.jdesktop.layout.GroupLayout jPanel7Layout = new org.jdesktop.layout.GroupLayout(jPanel7);
  jPanel7.setLayout(jPanel7Layout);
  jPanel7Layout.setHorizontalGroup(
   jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
   .add(jPanel7Layout.createSequentialGroup()
    .addContainerGap()
    .add(lblDomains18)
    .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
    .add(jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
     .add(jSplitPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 1589, Short.MAX_VALUE)
     .add(org.jdesktop.layout.GroupLayout.LEADING, jPanel7Layout.createSequentialGroup()
      .add(jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
       .add(org.jdesktop.layout.GroupLayout.LEADING, jPanel7Layout.createSequentialGroup()
        .add(jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
         .add(jPanel7Layout.createSequentialGroup()
          .add(93, 93, 93)
          .add(txtTripleDefLeft, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 250, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
         .add(jPanel7Layout.createSequentialGroup()
          .add(lblDomains21)
          .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
          .add(txtCacheFileNamesPattern, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 254, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
        .add(13, 13, 13)
        .add(txtTripleDefMid, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 404, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
        .add(txtTripleDefRight))
       .add(org.jdesktop.layout.GroupLayout.LEADING, jPanel7Layout.createSequentialGroup()
        .add(25, 25, 25)
        .add(jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
         .add(org.jdesktop.layout.GroupLayout.LEADING, jPanel7Layout.createSequentialGroup()
          .add(lblDomains3)
          .add(7, 7, 7)
          .add(txtHtmlCachePath, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 277, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
         .add(org.jdesktop.layout.GroupLayout.LEADING, lblDomains14, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 62, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
       .add(org.jdesktop.layout.GroupLayout.LEADING, jPanel7Layout.createSequentialGroup()
        .add(jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
         .add(jPanel7Layout.createSequentialGroup()
          .add(591, 591, 591)
          .add(lblDomains15))
         .add(jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
          .add(org.jdesktop.layout.GroupLayout.LEADING, jPanel7Layout.createSequentialGroup()
           .add(298, 298, 298)
           .add(lblDomains13)
           .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
           .add(txtResVarName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 277, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
          .add(org.jdesktop.layout.GroupLayout.LEADING, jPanel7Layout.createSequentialGroup()
           .add(lblDomains19)
           .add(18, 18, 18)
           .add(txtDefaultTestBaseClass, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 185, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))))
        .add(18, 18, 18)
        .add(jScrollPane15, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 437, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
      .add(0, 0, Short.MAX_VALUE))
     .add(jPanel7Layout.createSequentialGroup()
      .add(jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
       .add(lblDomains2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 21, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
       .add(lblDomains22, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
      .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
      .add(jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
       .add(jPanel7Layout.createSequentialGroup()
        .add(txtHtmlDocUrl, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 997, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
        .add(538, 538, 538))
       .add(jScrollPane16))))
    .add(123, 123, 123))
  );
  jPanel7Layout.setVerticalGroup(
   jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
   .add(org.jdesktop.layout.GroupLayout.LEADING, jPanel7Layout.createSequentialGroup()
    .add(jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
     .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel7Layout.createSequentialGroup()
      .add(jScrollPane15, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
      .add(4, 4, 4))
     .add(jPanel7Layout.createSequentialGroup()
      .add(jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
       .add(txtHtmlCachePath, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
       .add(lblDomains14)
       .add(lblDomains15))
      .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
      .add(jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
       .add(lblDomains21)
       .add(txtCacheFileNamesPattern, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
      .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
      .add(jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
       .add(txtDefaultTestBaseClass, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
       .add(lblDomains19)
       .add(txtResVarName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
       .add(lblDomains13))
      .add(10, 10, 10)))
    .add(jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
     .add(txtTripleDefLeft, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
     .add(lblDomains3)
     .add(txtTripleDefMid, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
     .add(txtTripleDefRight, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
    .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
    .add(jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
     .add(txtHtmlDocUrl, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
     .add(lblDomains2))
    .add(jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
     .add(jPanel7Layout.createSequentialGroup()
      .add(404, 404, 404)
      .add(lblDomains18))
     .add(jPanel7Layout.createSequentialGroup()
      .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
      .add(jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
       .add(lblDomains22)
       .add(jScrollPane16, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
      .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
      .add(jSplitPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
    .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
  );

  org.jdesktop.layout.GroupLayout jPanel3Layout = new org.jdesktop.layout.GroupLayout(jPanel3);
  jPanel3.setLayout(jPanel3Layout);
  jPanel3Layout.setHorizontalGroup(
   jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
   .add(jPanel3Layout.createSequentialGroup()
    .addContainerGap()
    .add(jPanel7, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
    .addContainerGap(503, Short.MAX_VALUE))
  );
  jPanel3Layout.setVerticalGroup(
   jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
   .add(jPanel7, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 1038, Short.MAX_VALUE)
  );

  jTabbedPane1.addTab("XhtParser DebugView", jPanel3);

  jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(" Name "));
  jPanel4.setAlignmentX(0);
  jPanel4.setAlignmentY(0);

  jPanel8.setAlignmentX(0);
  jPanel8.setAlignmentY(0);

  txtCachedFile.setText("http://www.artnet.com/artists/gerhard-richter");
  txtCachedFile.setToolTipText("<html>\n<b>utl for the current sourcecode line</b>\n</html>\n");
  txtCachedFile.addActionListener(new java.awt.event.ActionListener() {
   public void actionPerformed(java.awt.event.ActionEvent evt) {
    txtCachedFileActionPerformed(evt);
   }
  });

  lblDomains4.setText("cached file.");

  jSplitPane2.setDividerLocation(160);
  jSplitPane2.setDividerSize(4);
  jSplitPane2.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
  jSplitPane2.setPreferredSize(new java.awt.Dimension(1200, 800));

  sltTriples1.setDividerLocation(500);
  sltTriples1.setDividerSize(4);

  domTree.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() {
   public void valueChanged(javax.swing.event.TreeSelectionEvent evt) {
    domTreeValueChanged(evt);
   }
  });
  sclHtmlTags1.setViewportView(domTree);

  sltTriples1.setLeftComponent(sclHtmlTags1);

  jSplitPane3.setDividerLocation(500);

  txtFilterText.setToolTipText("<html>\n<b>utl for the current sourcecode line</b>\n</html>\n");
  txtFilterText.addActionListener(new java.awt.event.ActionListener() {
   public void actionPerformed(java.awt.event.ActionEvent evt) {
    txtFilterTextActionPerformed(evt);
   }
  });

  txtDomTreeText.setBackground(new java.awt.Color(245, 245, 255));
  txtDomTreeText.setColumns(20);
  txtDomTreeText.setFont(new java.awt.Font("Monospaced", 0, 10)); // NOI18N
  txtDomTreeText.setLineWrap(true);
  txtDomTreeText.setRows(5);
  txtDomTreeText.setToolTipText("innerText of selected TreeNode");
  txtDomTreeText.setWrapStyleWord(true);
  jScrollPane5.setViewportView(txtDomTreeText);

  org.jdesktop.layout.GroupLayout jPanel11Layout = new org.jdesktop.layout.GroupLayout(jPanel11);
  jPanel11.setLayout(jPanel11Layout);
  jPanel11Layout.setHorizontalGroup(
   jPanel11Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
   .add(txtFilterText, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 499, Short.MAX_VALUE)
   .add(jScrollPane5)
  );
  jPanel11Layout.setVerticalGroup(
   jPanel11Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
   .add(jPanel11Layout.createSequentialGroup()
    .add(txtFilterText, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
    .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
    .add(jScrollPane5, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 606, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
    .add(0, 0, Short.MAX_VALUE))
  );

  jSplitPane3.setLeftComponent(jPanel11);

  org.jdesktop.layout.GroupLayout jPanel12Layout = new org.jdesktop.layout.GroupLayout(jPanel12);
  jPanel12.setLayout(jPanel12Layout);
  jPanel12Layout.setHorizontalGroup(
   jPanel12Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
   .add(0, 0, Short.MAX_VALUE)
  );
  jPanel12Layout.setVerticalGroup(
   jPanel12Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
   .add(0, 0, Short.MAX_VALUE)
  );

  jSplitPane3.setRightComponent(jPanel12);

  txtFilterHtml.setToolTipText("<html>\n<b>utl for the current sourcecode line</b>\n</html>\n");
  txtFilterHtml.addActionListener(new java.awt.event.ActionListener() {
   public void actionPerformed(java.awt.event.ActionEvent evt) {
    txtFilterHtmlActionPerformed(evt);
   }
  });

  txtDomTreeHtml.setBackground(new java.awt.Color(245, 245, 255));
  txtDomTreeHtml.setColumns(20);
  txtDomTreeHtml.setFont(new java.awt.Font("Monospaced", 0, 10)); // NOI18N
  txtDomTreeHtml.setLineWrap(true);
  txtDomTreeHtml.setRows(5);
  txtDomTreeHtml.setToolTipText("html of selected TreeNode");
  txtDomTreeHtml.setWrapStyleWord(true);
  jScrollPane6.setViewportView(txtDomTreeHtml);

  org.jdesktop.layout.GroupLayout jPanel13Layout = new org.jdesktop.layout.GroupLayout(jPanel13);
  jPanel13.setLayout(jPanel13Layout);
  jPanel13Layout.setHorizontalGroup(
   jPanel13Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
   .add(txtFilterHtml, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 606, Short.MAX_VALUE)
   .add(jScrollPane6)
  );
  jPanel13Layout.setVerticalGroup(
   jPanel13Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
   .add(jPanel13Layout.createSequentialGroup()
    .add(txtFilterHtml, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
    .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
    .add(jScrollPane6, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 605, Short.MAX_VALUE))
  );

  jSplitPane3.setRightComponent(jPanel13);

  sltTriples1.setRightComponent(jSplitPane3);

  jSplitPane2.setRightComponent(sltTriples1);

  jTabbedPane3.setTabPlacement(javax.swing.JTabbedPane.LEFT);

  org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
  jPanel1.setLayout(jPanel1Layout);
  jPanel1Layout.setHorizontalGroup(
   jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
   .add(0, 1569, Short.MAX_VALUE)
  );
  jPanel1Layout.setVerticalGroup(
   jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
   .add(0, 154, Short.MAX_VALUE)
  );

  jTabbedPane3.addTab("filters", jPanel1);

  jSplitPane2.setLeftComponent(jTabbedPane3);

  lblDomains5.setText("re-download cached file:");

  btnReDldJsoup.setText("with built-in Jsoup downloader");
  btnReDldJsoup.addActionListener(new java.awt.event.ActionListener() {
   public void actionPerformed(java.awt.event.ActionEvent evt) {
    btnReDldJsoupActionPerformed(evt);
   }
  });

  btnReDldWebView.setText("with built-in WebView Form");
  btnReDldWebView.addActionListener(new java.awt.event.ActionListener() {
   public void actionPerformed(java.awt.event.ActionEvent evt) {
    btnReDldWebViewActionPerformed(evt);
   }
  });

  btnReDldAhkOpera.setText("with external AutoHotKey/Opera download");
  btnReDldAhkOpera.addActionListener(new java.awt.event.ActionListener() {
   public void actionPerformed(java.awt.event.ActionEvent evt) {
    btnReDldAhkOperaActionPerformed(evt);
   }
  });

  lblDomains6.setText("cached file size:");

  txtCachedFileSize.setText("31212");
  txtCachedFileSize.setToolTipText("<html>\n<b>utl for the current sourcecode line</b>\n</html>\n");
  txtCachedFileSize.addActionListener(new java.awt.event.ActionListener() {
   public void actionPerformed(java.awt.event.ActionEvent evt) {
    txtCachedFileSizeActionPerformed(evt);
   }
  });

  org.jdesktop.layout.GroupLayout jPanel8Layout = new org.jdesktop.layout.GroupLayout(jPanel8);
  jPanel8.setLayout(jPanel8Layout);
  jPanel8Layout.setHorizontalGroup(
   jPanel8Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
   .add(jPanel8Layout.createSequentialGroup()
    .addContainerGap()
    .add(lblDomains23)
    .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
    .add(jPanel8Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
     .add(jPanel8Layout.createSequentialGroup()
      .add(lblDomains4)
      .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
      .add(txtCachedFile, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 847, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
      .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
      .add(lblDomains6)
      .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
      .add(txtCachedFileSize, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 652, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
     .add(jSplitPane2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 1619, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
     .add(jPanel8Layout.createSequentialGroup()
      .add(lblDomains5)
      .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
      .add(btnReDldJsoup, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 236, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
      .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
      .add(btnReDldWebView, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 236, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
      .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
      .add(btnReDldAhkOpera, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 260, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
    .add(0, 557, Short.MAX_VALUE))
  );
  jPanel8Layout.setVerticalGroup(
   jPanel8Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
   .add(jPanel8Layout.createSequentialGroup()
    .add(jPanel8Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
     .add(jPanel8Layout.createSequentialGroup()
      .add(490, 490, 490)
      .add(lblDomains23))
     .add(jPanel8Layout.createSequentialGroup()
      .add(18, 18, 18)
      .add(jPanel8Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
       .add(jPanel8Layout.createSequentialGroup()
        .add(6, 6, 6)
        .add(lblDomains4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 14, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
       .add(jPanel8Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
        .add(lblDomains6, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 14, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
        .add(txtCachedFileSize, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
       .add(txtCachedFile, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
      .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
      .add(jPanel8Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
       .add(lblDomains5, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 14, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
       .add(btnReDldJsoup)
       .add(btnReDldWebView)
       .add(btnReDldAhkOpera))
      .add(29, 29, 29)
      .add(jSplitPane2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
    .addContainerGap(142, Short.MAX_VALUE))
  );

  org.jdesktop.layout.GroupLayout jPanel4Layout = new org.jdesktop.layout.GroupLayout(jPanel4);
  jPanel4.setLayout(jPanel4Layout);
  jPanel4Layout.setHorizontalGroup(
   jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
   .add(jPanel4Layout.createSequentialGroup()
    .addContainerGap()
    .add(jPanel8, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
    .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
  );
  jPanel4Layout.setVerticalGroup(
   jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
   .add(jPanel8, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
  );

  jTabbedPane1.addTab("HtmlCache View", jPanel4);

  org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
  getContentPane().setLayout(layout);
  layout.setHorizontalGroup(
   layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
   .add(jTabbedPane1)
  );
  layout.setVerticalGroup(
   layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
   .add(jTabbedPane1)
  );

  pack();
 }// </editor-fold>//GEN-END:initComponents

 private void txtCachedFileSizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCachedFileSizeActionPerformed
  // TODO add your handling code here:
 }//GEN-LAST:event_txtCachedFileSizeActionPerformed

 private void btnReDldAhkOperaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReDldAhkOperaActionPerformed
  try {beginAction(); btnReDldAhkOpera_click (evt);} catch (Exception ex) {try {utl.say(ex.getMessage()); ex.printStackTrace();} catch (Exception e) {}} finally {endAction();}  // TODO add your handling code here:
 }//GEN-LAST:event_btnReDldAhkOperaActionPerformed

 private void btnReDldWebViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReDldWebViewActionPerformed
  try {beginAction(); btnReDldWebView_click (evt);} catch (Exception ex) {try {utl.say(ex.getMessage()); ex.printStackTrace();} catch (Exception e) {}} finally {endAction();}  // TODO add your handling code here:
 }//GEN-LAST:event_btnReDldWebViewActionPerformed

 private void btnReDldJsoupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReDldJsoupActionPerformed
  try {beginAction(); btnReDldJsoup_click (evt);} catch (Exception ex) {try {utl.say(ex.getMessage()); ex.printStackTrace();} catch (Exception e) {}} finally {endAction();}  // TODO add your handling code here:
 }//GEN-LAST:event_btnReDldJsoupActionPerformed

 private void txtFilterHtmlActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFilterHtmlActionPerformed
  // TODO add your handling code here:
 }//GEN-LAST:event_txtFilterHtmlActionPerformed

 private void txtFilterTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFilterTextActionPerformed
  // TODO add your handling code here:
 }//GEN-LAST:event_txtFilterTextActionPerformed

 private void domTreeValueChanged(javax.swing.event.TreeSelectionEvent evt) {//GEN-FIRST:event_domTreeValueChanged
  try {domTreeSelectionChanged(evt);} catch (Exception ex) {}
 }//GEN-LAST:event_domTreeValueChanged

 private void txtCachedFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCachedFileActionPerformed
  // TODO add your handling code here:
 }//GEN-LAST:event_txtCachedFileActionPerformed

 private void lstXhtSourceCodeValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_lstXhtSourceCodeValueChanged
  try {xhtSouceCodeSelected();} catch (Exception ex) {try
   {
    utl.say(ex.getMessage());
    ex.printStackTrace();
   }
   catch (Exception e) {}} finally {}
 }//GEN-LAST:event_lstXhtSourceCodeValueChanged

 private void txtTripleDefRightActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTripleDefRightActionPerformed
  // TODO add your handling code here:
 }//GEN-LAST:event_txtTripleDefRightActionPerformed

 private void txtTripleDefMidActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTripleDefMidActionPerformed
  // TODO add your handling code here:
 }//GEN-LAST:event_txtTripleDefMidActionPerformed

 private void txtDefaultTestBaseClassActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDefaultTestBaseClassActionPerformed
  // TODO add your handling code here:
 }//GEN-LAST:event_txtDefaultTestBaseClassActionPerformed

 private void txtCacheFileNamesPatternActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCacheFileNamesPatternActionPerformed
  // TODO add your handling code here:
 }//GEN-LAST:event_txtCacheFileNamesPatternActionPerformed

 private void txtTripleDefLeftActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTripleDefLeftActionPerformed
  // TODO add your handling code here:
 }//GEN-LAST:event_txtTripleDefLeftActionPerformed

 private void txtResVarNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtResVarNameActionPerformed
  // TODO add your handling code here:
 }//GEN-LAST:event_txtResVarNameActionPerformed

 private void txtHtmlCachePathActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtHtmlCachePathActionPerformed

 }//GEN-LAST:event_txtHtmlCachePathActionPerformed

 private void txtHtmlDocUrlActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtHtmlDocUrlActionPerformed

 }//GEN-LAST:event_txtHtmlDocUrlActionPerformed
  
    
 // Variables declaration - do not modify//GEN-BEGIN:variables
 private javax.swing.JButton btnReDldAhkOpera;
 private javax.swing.JButton btnReDldJsoup;
 private javax.swing.JButton btnReDldWebView;
 private javax.swing.ButtonGroup buttonGroup1;
 private javax.swing.JTree domTree;
 private javax.swing.JDialog jDialog1;
 private javax.swing.JPanel jPanel1;
 private javax.swing.JPanel jPanel11;
 private javax.swing.JPanel jPanel12;
 private javax.swing.JPanel jPanel13;
 private javax.swing.JPanel jPanel3;
 private javax.swing.JPanel jPanel4;
 private javax.swing.JPanel jPanel7;
 private javax.swing.JPanel jPanel8;
 private javax.swing.JScrollPane jScrollPane1;
 private javax.swing.JScrollPane jScrollPane15;
 private javax.swing.JScrollPane jScrollPane16;
 private javax.swing.JScrollPane jScrollPane2;
 private javax.swing.JScrollPane jScrollPane5;
 private javax.swing.JScrollPane jScrollPane6;
 private javax.swing.JSplitPane jSplitPane1;
 private javax.swing.JSplitPane jSplitPane2;
 private javax.swing.JSplitPane jSplitPane3;
 private javax.swing.JTabbedPane jTabbedPane1;
 private javax.swing.JTabbedPane jTabbedPane2;
 private javax.swing.JTabbedPane jTabbedPane3;
 private javax.swing.JLabel lblDomains13;
 private javax.swing.JLabel lblDomains14;
 private javax.swing.JLabel lblDomains15;
 private javax.swing.JLabel lblDomains18;
 private javax.swing.JLabel lblDomains19;
 private javax.swing.JLabel lblDomains2;
 private javax.swing.JLabel lblDomains21;
 private javax.swing.JLabel lblDomains22;
 private javax.swing.JLabel lblDomains23;
 private javax.swing.JLabel lblDomains3;
 private javax.swing.JLabel lblDomains4;
 private javax.swing.JLabel lblDomains5;
 private javax.swing.JLabel lblDomains6;
 private javax.swing.JList<String> lstXhtSourceCode;
 private javax.swing.JPanel pnlHtmlTags;
 private javax.swing.JScrollPane sclHtmlTags;
 private javax.swing.JScrollPane sclHtmlTags1;
 private javax.swing.JScrollPane sclTriples;
 private javax.swing.JSplitPane sltTriples;
 private javax.swing.JSplitPane sltTriples1;
 private javax.swing.JTable tblKeyVals;
 private javax.swing.JTable tblTriples;
 private javax.swing.JTextField txtCacheFileNamesPattern;
 private javax.swing.JTextField txtCachedFile;
 private javax.swing.JTextField txtCachedFileSize;
 private javax.swing.JTextField txtDefaultTestBaseClass;
 private javax.swing.JTextArea txtDomTreeHtml;
 private javax.swing.JTextArea txtDomTreeText;
 private javax.swing.JTextArea txtExternalParams;
 private javax.swing.JTextField txtFilterHtml;
 private javax.swing.JTextField txtFilterText;
 private javax.swing.JTextField txtHtmlCachePath;
 private javax.swing.JTextField txtHtmlDocUrl;
 private javax.swing.JTextField txtResVarName;
 private javax.swing.JTextField txtTripleDefLeft;
 private javax.swing.JTextField txtTripleDefMid;
 private javax.swing.JTextField txtTripleDefRight;
 private javax.swing.JTextArea txtXhtSelectors;
 // End of variables declaration//GEN-END:variables
    
}

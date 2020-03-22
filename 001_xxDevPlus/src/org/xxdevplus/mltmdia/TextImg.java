package org.xxdevplus.mltmdia;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JTextPane;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import org.xxdevplus.utl.ctx;


public class TextImg 
{
 
 private JFrame  fr = null;
 private ctx     cx = new ctx();
 private int      w = 0;
 private int      h = 0;
 
 
 
 public TextImg(String text, int w, int h, int fontsize) throws Exception 
 {
  if (!cx.Param().hasKey("TxtImgJFrame")) 
  {
   cx.Param().Add("TxtImgJFrame", new JFrame());
   fr = (JFrame)(cx.Param().g("TxtImgJFrame"));
   JTextPane txt = new JTextPane();   
   fr.getContentPane().add(txt);
  }
  fr = (JFrame)(cx.Param().g("TxtImgJFrame"));
  this.w = w;
  this.h = h;
  //fr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  fr.getContentPane().setLayout(null);
  fr.setPreferredSize(new Dimension(w, h));
  fr.setSize(new Dimension(w, h));
  fr.setBackground(Color.yellow);
  //fr.setUndecorated(true);
  //fr.setOpacity(1);

  //JTextArea txt = new JTextArea();   
  JTextPane txt = (JTextPane ) fr.getContentPane().getComponent(0);
  txt.setPreferredSize(new Dimension(w, h));
  txt.setSize(new Dimension(w, h));
  txt.setText("\n" + text); 
  txt.setFont(new Font("Arial", Font.BOLD, fontsize));
  //fr.setBackground(txt.getBackground());
  //txt.setBackground(fr.getBackground());
  //txt.setWrapStyleWord(true);
  //txt.setLineWrap(true);

  txt.setOpaque(false);
  txt.setEditable(false);
  txt.setFocusable(false);
  StyledDocument doc = txt.getStyledDocument();
  SimpleAttributeSet center = new SimpleAttributeSet();
  StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
  doc.setParagraphAttributes(0, doc.getLength(), center, false);

  //javax.swing.border.Border border = BorderFactory.createLineBorder(Color.BLACK);
  //txt.setBorder(border);
  
  //txt.setHorizontalAlignment(JLabel.CENTER);
  //txt.setVerticalAlignment(JLabel.CENTER);

  txt.setVisible(true);
  fr.setVisible(true);

  fr.pack();  
  fr.invalidate();
 } 

 public void save(String filename) throws Exception
 {
  //BufferedImage bi = new BufferedImage(lbl.getWidth(), lbl.getHeight(), BufferedImage.OPAQUE + BufferedImage.TYPE_INT_RGB);
  BufferedImage bi = new BufferedImage(w, h, BufferedImage.OPAQUE + BufferedImage.TYPE_INT_RGB);
  fr.paint(bi.getGraphics());;
  //txt.paint(bi.getGraphics());;
  ImageIO.write(bi, "png", new File(filename));
  fr.setVisible(false);
 }
}














package org.xxdevplus.udf;

public interface  XhtReasoner 
{

 public boolean beforeDownload   (XhtParser parser) throws Exception; // return true to continue normally or false to abort the current action
 public boolean afterDownload    (XhtParser parser) throws Exception; // return true to continue normally or false to abort the current action

 public boolean beforeSelections (XhtParser parser) throws Exception; // return true to continue normally or false to abort the current action
 public boolean afterSelections  (XhtParser parser) throws Exception; // return true to continue normally or false to abort the current action

 public boolean beforeSelection  (XhtParser parser) throws Exception; // return true to continue normally or false to abort the current action
 public boolean afterSelection   (XhtParser parser) throws Exception; // return true to continue normally or false to abort the current action

 public boolean beforeTriples    (XhtParser parser) throws Exception; // return true to continue normally or false to abort the current action
 public boolean afterTriples     (XhtParser parser) throws Exception; // return true to continue normally or false to abort the current action
 
}

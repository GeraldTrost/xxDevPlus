
package org.xxdevplus.ytapi;

import org.xxdevplus.ytapi.Auth;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.ResourceId;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Iterator;
import java.util.List;
import org.xxdevplus.chain.Chain;
import org.xxdevplus.struct.Pile;

public class YtSearch 
{
 private static YouTube youtube;
 private String apiKey = "AIzaSyDFjW6soc9c-dc926fO7rOu5kbFI5BrOYE"; 
 
 public Pile<Pile<String>> ytSearch(long maxResults, String query) throws Exception
 {
  Pile<Pile<String>> ret = new Pile<Pile<String>>();
  youtube = new YouTube.Builder(Auth.HTTP_TRANSPORT, Auth.JSON_FACTORY, new HttpRequestInitializer() {public void initialize(HttpRequest request) throws IOException{}}).setApplicationName("youtube-cmdline-search-sample").build();
  YouTube.Search.List search = youtube.search().list("id,snippet");
  search.setKey(apiKey);
  search.setQ(query);
  search.setType("video");
  search.setFields("items(id/kind,id/videoId,snippet/publishedAt,snippet/title,snippet/description,snippet/channelTitle,snippet/thumbnails/default/url)");
  search.setMaxResults(maxResults);
  SearchListResponse searchResponse = search.execute();
  List<SearchResult> searchResultList = searchResponse.getItems();
  Iterator<SearchResult> iteratorSearchResults = searchResultList.iterator();
  while (iteratorSearchResults.hasNext()) 
  {
   Pile<String> row = new Pile<String>();
   SearchResult res = iteratorSearchResults.next();
   ResourceId item = res.getId();
   if (item.getKind().equals("youtube#video")) 
   {
    row.Push(item.getVideoId());
    row.Push(res.getSnippet().getTitle());
    row.Push(res.getSnippet().getPublishedAt().toString());
    row.Push(res.getSnippet().getDescription());
    row.Push(res.getSnippet().getChannelTitle());
   }
   ret.Push(row);
  }
  return (ret);
 } 
 
 public Pile<Pile<String>> ytSearch2(Connection cnn, long domId, long maxResults, String query) throws Exception
 {
  Pile<Pile<String>> ret = new Pile<Pile<String>>();
  if (queryInCache2(cnn,domId,query))
  {
    ResultSet res = execSelect2(cnn,"select * from STATIC_WEBLOOKUP_CACHE where domainid = "+domId+" AND sstr = '"+query+"'");
    Pile<String> row = new Pile<String>();
    while (res.next())
    {
      System.out.println("FROM CACHE: "+query + " | "+res.getString("wlid")+" | "+row.Push(res.getString("title"))); 
      row.Push(res.getString("wlid"));row.Push(res.getString("title"));row.Push(res.getString("xdate"));row.Push(res.getString("xdesc"));row.Push(res.getString("chann"));
    }
    ret.Push(row);
    return (ret);
  }
  youtube = new YouTube.Builder(Auth.HTTP_TRANSPORT, Auth.JSON_FACTORY, new HttpRequestInitializer() {public void initialize(HttpRequest request) throws IOException{}}).setApplicationName("youtube-cmdline-search-sample").build();
  YouTube.Search.List search = youtube.search().list("id,snippet");
  search.setKey(apiKey);
  search.setQ(query);
  search.setType("video");
  search.setFields("items(id/kind,id/videoId,snippet/publishedAt,snippet/title,snippet/description,snippet/channelTitle,snippet/thumbnails/default/url)");
  search.setMaxResults(maxResults);
  SearchListResponse searchResponse = search.execute();
  List<SearchResult> searchResultList = searchResponse.getItems();
  Iterator<SearchResult> iteratorSearchResults = searchResultList.iterator();
  while (iteratorSearchResults.hasNext()) 
  {
   Pile<String> row = new Pile<String>();
   SearchResult res = iteratorSearchResults.next();
   ResourceId item = res.getId();
   if (item.getKind().equals("youtube#video")) 
   {
    row.Push(item.getVideoId());
    row.Push(res.getSnippet().getTitle());
    row.Push(res.getSnippet().getPublishedAt().toString());
    row.Push(res.getSnippet().getDescription());
    row.Push(res.getSnippet().getChannelTitle());
    insertIntoCache2(cnn, domId, query, item.getVideoId(), res.getSnippet().getTitle(), 
                     res.getSnippet().getPublishedAt().toString(), res.getSnippet().getDescription(), res.getSnippet().getChannelTitle());
   }
   ret.Push(row);
  }
  return (ret);
 }
 
 private void insertIntoCache2(Connection cnn, long domId, String sstr, String wlid, String title, String xdate, String xdesc, String chann)
 {
  try 
  { 
   String source = "Youtube"; String il = "";  String sl = "";
   xdate = ""; xdesc = ""; chann = "";   
   Chain titl = new Chain(title); if (titl.len() > 65) titl = titl.upto(65).plus("...");
   String sql = "insert into STATIC_WEBLOOKUP_CACHE (domainid, sstr, wlid, title, xdate, xdesc, chann, source, il, sl) "+
                " values ("+domId+",'"+sstr+"','"+wlid+"','"+titl.text()+"','"+xdate+"','"+xdesc+"','"+chann+"','"+source+"','"+il+"','"+sl+"');";   
   if (!cnn.createStatement().execute(sql)) 
   {     }; 
  } 
  catch (Exception ex) 
  { 
      System.out.println("EXCEPTION : "+ex.getMessage()); 
  }
  System.out.println("INSERTED into cache-table: "+sstr+" | "+wlid);
}
 private boolean queryInCache2(Connection cnn, long domId, String query)
 {
  ResultSet res = execSelect2(cnn,"select count(*) from STATIC_WEBLOOKUP_CACHE where domainid = "+domId+" AND sstr = '"+query+"'");
  try
  {
   if(res != null)
   {
     res.next();
     if (res.getString("count").equals("0")) 
      return false; 
     else 
      return true;
   }
   else
   {
     return false;
   }
  }
  catch (Exception ex)   
  {     
   System.out.println("EXCEPTION : "+ex.getMessage()); 
   return false;     
  }
 }
 
 public ResultSet execSelect2(Connection cnn, String sql)
 {
  ResultSet ret = null;
  try { ret = cnn.createStatement().executeQuery(sql); } 
  catch (Exception ex) 
  { 
   System.out.println("EXCEPTION : "+ex.getMessage()); 
   return ret;
  }
  return ret;
 }
 
}

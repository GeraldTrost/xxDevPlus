

'use strict';

// DEPLOYMENT CONFIGURATION //
// DEPLOYMENT CONFIGURATION //
// DEPLOYMENT CONFIGURATION //


// ONLY ENABLE ONE OF THE deploy-Switches !!!

var deploy_NetbeansDebugger     = true;
var deploy_DebianLinux          = false;
var deploy_AmazonCloud          = false;

var everestWsEndpoint = ''
var imgHostUrl = '';

if (deploy_DebianLinux)
{
 imgHostUrl = 'http://everest:8081/img/';
 everestWsEndpoint = 'http://' + location.hostname + ':8080/EverestWeb/';
}


if (deploy_NetbeansDebugger)
{
 imgHostUrl = 'http://everest:8081/img/';
 everestWsEndpoint = 'http://' + location.hostname + ':8084/'
}

if (deploy_AmazonCloud)
{
 imgHostUrl = 'http://everest:8081/img/';
 everestWsEndpoint = 'http://' + location.hostname + ':8084/'
}


// DEPLOYMENT CONFIGURATION //
// DEPLOYMENT CONFIGURATION //
// DEPLOYMENT CONFIGURATION //

var domain               = '';
var maxWidth_qRpImgPane      ;
var maxImagesPerRow      =  1;
var sbjDict              = {};   // holds info for any item that has been loaded from Server
//var qRpImgDict           = {};   // holds DOM-info for any primary Images (related pane, info pane, Title Div etc ...)
var current_pImgPaneId   = '';

function onDetailsPage() { return location.pathname.trim().toUpperCase().endsWith('SHOW.JSP'); }
 
var uniqCounter = 0;
function buildDivId(cssClass, row, metaRow) 
{ 
 if (!!metaRow)
  return cssClass + '-' + ('000' + metaRow).slice(-2) + '§' + ('000' + row).slice(-2); 
 else
  return cssClass + '-00§' + ('000' + row).slice(-2); 
}
 
function itemUrl(item)
{
 if (onDetailsPage())
 {
  if (item.idname.startsWith("ytID:")) return 'https://www.youtube.com/watch?v=' + item.idname.substring(5);
  if (item.idname.startsWith("wpID:")) return 'https://en.wikipedia.org/wiki/' + item.idname.substring(5);    
 }
 else
 {
  return(location.pathname.replace('/show.jsp', '').replace('/search.jsp', '') + '/show.jsp?domain=' + item.domain + '&query=' + item.idname);
 }
 return '';
}


function everestWsCall(method, params)    //VideoAssessment WebService Call
{
 var ret, err, dat = "";
 $.ajax
 (
  {
   async            : false,
   ensuresuccess    : true,
   type             : 'POST',
   url              : everestWsEndpoint + method,
   ContentType      : 'application/json; charset=utf-8',
   dataType         : 'json',
   data             : params,
   timeout          : 30000,     // 30 second timeout
   beforeSend       : function (evp             )   {                                                         },
   progress         : function (evp             )   {                                                         },
   complete         : function (                )   {                                                         },
   success          : function (msg             )   { ret = msg;                                              },
   error            : function (rq, txt, origErr)   { err = new Error($.parseJSON(rq.responseText).Message);  }
  }
 );
 if (err) throw err;
 return ret;
}

function createAsyncAction(asyncFunc)
{
 var request = new XMLHttpRequest();
 request.open( "GET", everestWsEndpoint + 'DelayForTheBrowserToRenderTheDom');
 request.addEventListener('load', asyncFunc);
 request.send();
}

function buildImgSrc(item, useSmallImg = false)
{
 var imgFolder = item.idname.substring(0, item.idname.indexOf(':')) + '.imgCache';
 var itemName = item.idname.substring(item.idname.indexOf(':') + 1);
 itemName = itemName.toUpperCase();
 var imgName = itemName[0] + '/' + itemName;
 if (useSmallImg) imgName += '__SMALL.jpg'; else imgName += '.jpg';
 return imgHostUrl + item.domain + '/' + imgFolder + '/' + imgName;
}


function doAsyncImgClick() 
{
 var imgClickParams = $('#' + current_pImgPaneId + ' img').attr('onclick').replace('imgClick(\'','').replace('\')','').split(','); // params of first Image
 imgClick(imgClickParams[0].replace('\'','').trim(), imgClickParams[1].replace('\'','').trim()); 
}


function fetchSubjectsAttributes() // fetch attributes from the next 'count' objects from server into sbjDict Cache
{
 var count = 100000;
 var found = 0;
 var conceptIds = '';
 for (var key in sbjDict) if (Object.keys(sbjDict[key].attr).length < 3) if (++found <= count) conceptIds += ', ' + key; // find keys of first 'count' incomplete Subjects in sbjDict Cache

 if (conceptIds.length < 3) return;
 //var lastLoop = false; 
 var lastLoop = (conceptIds.length < 3);
 // changed in Nov.2016: since related items will be entered on item click the background filling of attributes must stay active forever

 var attributeResult = [];
 try { attributeResult = everestWsCall("FetchAttributes", {'domain': '', 'conceptIds': conceptIds.substr(2)}); } catch (err) {  }
 try 
 {
  attributeResult.forEach
  ( 
   function(attrItem)
   {
    var attr = sbjDict[attrItem.concept].attr;

    if (!attr[attrItem.attribute]) attr[attrItem.attribute] = [attrItem.value]; else attr[attrItem.attribute].push(attrItem.value);

    if (attrItem.attribute === 'Title')
    {
     var imgID =  '#img' + attrItem.concept;
     var imgTitleDiv = (onDetailsPage()) ?  $(imgID).parent().parent().next() : $(imgID).parent().next();   
     imgTitleDiv.html(attrItem.value);
    }
   }
  );
 }
 catch (err) { pane.html('< no attributes results to display >'); } 
}

/*
function buildImgDict(pfx = 'qR', pImgDict = qRpImgDict, sImgPane = null, pImg = null)
{
 if (!!sImgPane)
 {
  pImgDict[pImg.attr('id').replace('img', '')].sImgDict = {};
  var pane = $(sImgPane);
  window.__pId = $(pane).attr('id');
  $(pane).find('.' + pfx + 'sImgDiv').each
  (
   function(idiv, div)
   {
    window.__dId = $(div).attr('id');
    $(div).find('img').each
    (
     function(iImg, img)
     {
      var imgInfo = 
      {
       'concept'    : $(img).attr('id').replace('img' , ''), 
       'imgFrame'   : $('#' + window.__dId).find('.' + pfx + 'sImgFrame')[0], 
       'sImgDiv'    : $('#' + window.__dId)[0], 
       'sImgPane'   : $('#' + window.__pId)[0], 
       'sImgTitle'  : $('#' + $('#' + window.__dId).attr('id').replace('Pane', "Title"))[0], 
       'pImg'       : pImg[0]
      };
      pImgDict[pImg.attr('id').replace('img', '')].sImgDict[imgInfo.concept] = imgInfo;
     }
    );
   }
  );   
 }
 else
 {
  $('.' + pfx + 'pImgPane').each
  (
   function(iPane, pane)
   {
    window.__pId = $(pane).attr('id');
    $(pane).find('.' + pfx + 'pImgDiv').each
    (
     function(idiv, div)
     {
      window.__dId = $(div).attr('id');
      $(div).find('img').each
      (
       function(iImg, img)
       {
        var imgInfo = 
        {
         'concept'    : $(img).attr('id').replace('img' , ''), 
         'imgFrame'   : $('#' + window.__dId).find('.' + pfx + 'pImgFrame')[0], 
         'pImgDiv'    : $('#' + window.__dId)[0], 
         'pImgPane'   : $('#' + window.__pId)[0], 
         'pImgHeader' : $('#' + $('#' + window.__dId).attr('id').replace('Pane', "Header"))[0], 
         'pImgTitle'  : $('#' + $('#' + window.__dId).attr('id').replace('Pane', "Title"))[0], 
         'pImgInfo'   : $('#' +  window.__pId.replace('' + pfx + 'pImgPane', '' + pfx + 'pImgInfo'))[0], 
         'sImgPane'   : $('#' +  window.__pId.replace('' + pfx + 'pImgPane', '' + pfx + 'sImgPane'))[0]
        };
        pImgDict[imgInfo.concept] = imgInfo;
       }
      );
     }
    );   
   }
  );
 }
}
*/

function guessCssSettings()
{
 $('#qRpane').html('');
 $('#qRpane').append
 (
  '<div id="test_qRpImgPane" style="clear:both;float:left;display:inline-block;width:100%;text-align:center;background:red;floar:left;border:0px;;margin:0px;padding:0px;">\r\n' + 
   'dummy text\r\n' +
  '</div>\r\n' +
  //'<div id="testdummyDiv" style="clear:both;display:block;min-width:99999px;text-align:center;background:red;floar:left;border:0px;;margin:0px;padding:0px;">\r\n' + 
  // 'dummy text\r\n' +
  //'</div>\r\n' +
  '<div style="background:grey;" id="qRpImgPane00" class="qRpImgPane">\r\n' +
  ' <div class="qRpGroup">\r\n' +
  '  <div id="qRpImgPane00-00" class="qRpImgDiv">\r\n' +
  '   <div id="qRpImgHeader00-00" class="qRpImgHeader">1000</div>\r\n' +
  '   <div class="qRpImgFrame">\r\n' +
  '   <a target="_blank" href="https://www.youtube.com/watch?v=7symVxe2uUg">\r\n' +
  '    <img class="qRpImg2" id="img000000" onclick="imgClick(\'qRpImgPane00\', \'#img000000\')" src="http://everest:8081/img/MOVIES/ytID.imgCache/7/7SYMVXE2UUG.jpg">\r\n' +
  '   </a>\r\n' +
  '  </div>\r\n' +
  '  <div id="qRpImgTitle00-00" class="qRpImgTitle">100 Days of Love turning point heart touching scene</div></div>\r\n' +
  ' </div>\r\n' +
  '</div>\r\n' +
  '<div style="background:green;" class="qRsGroup">\r\n' +
  ' <div id="qRpImgInfo00" class="qRpImgInfo"> Type is Trailer</div>\r\n' +
  ' <div id="qRsImgPane00" class="qRsImgPane">\r\n' +
  '  <div id="qRsImgPane01-01" class="qRsImgDiv">\r\n' +
  '   <div class="qRsImgFrame">\r\n' +
  '    <a target="_blank" href="https://en.wikipedia.org/wiki/100_Days_of_Love">\r\n' +
  '     <img img="" class="qRsImg1" id="img000001" src="http://everest:8081/img/MOVIES/wpID.imgCache/1/100_DAYS_OF_LOVE__SMALL.jpg" title="100 Days of Love">\r\n' +
  '    </a>\r\n' +
  '   </div>\r\n' +
  '   <div id="qRsImgTitle00-00" title="">100 Days of Love </div>\r\n' +
  '  </div>\r\n' +
  ' </div>\r\n' +
  '</div>\r\n'
 
 );
 maxWidth_qRpImgPane = $('#test_qRpImgPane')[0].clientWidth;
 maxImagesPerRow = Math.floor(maxWidth_qRpImgPane / $('#qRpImgPane00-00')[0].clientWidth);
 $('#test_qRpImgPane').hide(); $('#qRpImgPane00').hide(); $('.qRsGroup').hide();
 if (onDetailsPage()) maxImagesPerRow = 1;    
 //alert(maxWidth_qRpImgPane + ' - ' +  $('.qRpGroup')[0].clientWidth + ' - ' + maxImagesPerRow);
}

function htmlFromWsConfigCall(domain, qry, pfx = 'qR', pane = $('#qRpane')) // prefix qR for queryResult  prefix sI for similarItems
{
 var ret = [], searchResult = [[-1, 'start']];

 if (pfx != 'qR')
 {
  var idParts = pane.prev().prev().attr('id').split('§');
  metaRow = Math.abs(idParts[1]);
 }
 
 try { searchResult = everestWsCall("CfgAdmin", {'domain': domain, 'search': qry}); } catch (err) { searchResult= [[1,'Error on WsCall: ' + err]]; }
 try 
 {
  searchResult.forEach
  ( 
   function(item)
   {
    pane.append('<div class="domaintable">' + item.name + '</div>\n');
   }
  );
 }
 catch (err)  { pane.html('< no config data to display >'); }
 return ret;
}

function htmlFromWsSearchCall(domain, qry, pfx = 'qR', pane = $('#qRpane')) // prefix qR for queryResult  prefix sI for similarItems
{
 var ret = [], metaRow = null, activeRow = 0, curRow = 0, curCol = 0, searchResult = [];

 if (pfx != 'qR')
 {
  var idParts = pane.prev().prev().attr('id').split('§');
  metaRow = Math.abs(idParts[1]);
 }
 
 try { searchResult = everestWsCall("Search", {'domain': domain, 'search': qry}); } catch (err) { alert('Error on WsCall: ' + err); }
 try 
 {
  searchResult.forEach
  ( 
   function(item)
   {
    var imgNumber = 0;  var pImgPaneId = '';  var sImgPaneId = ''; var pImgInfoId = '';  var rowIsFull = false;  
    if ((!sbjDict[item.concept])  || true) 
    {
     curCol++;        
     sbjDict[item.concept] = {'domain' : item.domain, 'concept' : item.concept, 'idname' : item.idname, 'rank' : item.rank, 'attr' : {}, 'obj': []}; //fill into dictionary for later async. detail-info loading
     sbjDict[item.concept].attr.name = item.name;
     sbjDict[item.concept].attr.type = item.type;
     if (activeRow == 0) 
      { imgNumber = 1; rowIsFull = true; } 
     else 
      { imgNumber = (curRow - 1) * maxImagesPerRow + curCol; rowIsFull = ((imgNumber  - 1) % maxImagesPerRow) == 0; }
     if (!rowIsFull) 
      pImgPaneId          = buildDivId(pfx + 'pImgPane', activeRow, metaRow);
     else
     {
      curRow++;  curCol   = 1;  activeRow++;
      pImgPaneId          = buildDivId(pfx + 'pImgPane', activeRow, metaRow);
      sImgPaneId          = buildDivId(pfx + 'sImgPane', activeRow, metaRow);
      pImgInfoId          = buildDivId(pfx + 'pImgInfo', activeRow, metaRow);
      pane.append
      (
       '<div id="'        + pImgPaneId + '" class="' + pfx + 'pImgPane pImgPane" ></div>\r\n' + 
       '<div class="'     + pfx + 'sGroup sGroup">\r\n' + 
       ' <div id="'       + pImgInfoId + '" class="' + pfx + 'pImgInfo pImgInfo" ></div>\r\n' + 
       ' <div id="'       + sImgPaneId + '" class="' + pfx + 'sImgPane sImgPane" ></div>\r\n' + 
       '</div>\r\n' +
       '<div class="sIpane" ></div\r\n>'.substring(0,(pfx!='qR') ? 0 :9999)
      );
      rowIsFull = false;
     }
     if (($.inArray(pImgPaneId, Object.keys(ret)) == -1)) ret[pImgPaneId] = '';     
     var curRowDiv        = $('#' + pImgPaneId);
     var pImgDivId        = pImgPaneId + '-' + ('000' + curCol).slice(-2);
     var imgID            = 'img' + item.concept;
     var imgSrc           = buildImgSrc(item, false);
     var pImgTitleId      = pImgDivId.replace('Pane', 'Title');   
     var pImgHeaderId     = pImgDivId.replace('Pane', 'Header');   
     var html =
      ' <div class="'     + pfx + 'pGroup">\r\n' + 
      '  <div id="'       + pImgDivId     + '" class="' + pfx + 'pImgDiv pImgDiv">\r\n' + 
      '   <div id="'      + pImgHeaderId  + '" class="' + pfx + 'pImgHeader pImgHeader">' + item.rank + '</div>\r\n' +
      '   <div class="'   + pfx + 'pImgFrame">\r\n';
     if (onDetailsPage()&&(pfx=='qR')) html += 
      '    <a target="_blank" href="' + itemUrl(item) + '"\r\n>';
     html += 
      '     <img class="' + pfx + 'pImg1 pImg" id="' + imgID + '" onclick="imgClick(\'' + pImgPaneId + '\', \'#' + imgID + '\')' + '" src="' + imgSrc + '" />\r\n' 
     if (onDetailsPage()&&(pfx=='qR')) html +=  
      '    </a>\r\n';
     html += 
      '   </div>\r\n'     +
      '   <div id="'      + pImgTitleId + '" class="' + pfx + 'pImgTitle pImgTitle"></div>\r\n' +
      '  </div>\r\n'      +
      ' </div>\r\n';
     ret[pImgPaneId]      = ret[pImgPaneId] + html;
    }    
   }
  );
 }
 catch (err)  { pane.html('< no results to display >'); }
 return ret;
}

function htmlFromCfgAdminData(domain, qry = "SELECT * FROM domain", pane = $('#qRpane'))
{
 var pImgPanes = htmlFromWsConfigCall(domain, qry, 'qR', pane);
 for (var pImgPaneId in pImgPanes) 
     $('#' + pImgPaneId).append(pImgPanes[pImgPaneId]);
 return false; // submit button soll nicht die seite reloaden
}

function htmlFromQueryResults(domain, qry, pane = $('#qRpane'))
{
 sbjDict = {};
 uniqCounter = 0;
 guessCssSettings();
 //return;
 var pImgPanes = htmlFromWsSearchCall(domain, qry, 'qR', pane);
 for (var pImgPaneId in pImgPanes) 
     $('#' + pImgPaneId).append(pImgPanes[pImgPaneId]);
 //buildImgDict('qR', qRpImgDict, null, null);
 fetchSubjectsAttributes();
 current_pImgPaneId =  pane.find('.pImgPane').attr('id');
 createAsyncAction(doAsyncImgClick); 
 return false; // submit button soll nicht die seite reloaden
}

function htmlFromSimilarItems(domain, qry, pane = $('.sRpane'))
{
 //guessCssSettings();
 pane.html('');
 var pImgPanes = htmlFromWsSearchCall(domain, qry, 'sI', pane);
 for (var pImgPaneId in pImgPanes) 
     $('#' + pImgPaneId).append(pImgPanes[pImgPaneId]);
 //buildImgDict('qR', qRpImgDict, null, null);
 fetchSubjectsAttributes();
 current_pImgPaneId =  pane.find('.pImgPane').attr('id');
 createAsyncAction(doAsyncImgClick); 
 return false; // submit button soll nicht die seite reloaden
}


function imgClick(pImgPaneId, imgID) // imgID must start with #
{

 var pfx = pImgPaneId.substring(0,2);
 var concept       = imgID.replace('#img', '');
 //var imgInfo       = qRpImgDict[concept];
 var imgCounter    = 0;
 var pImgPane      = $('#' + pImgPaneId);
 var pImgInfo      = $('#' + pImgPaneId.replace('Pane', 'Info'));
 var sImgPane      = pImgPane.next().find('.sImgPane');

 var qRsGroup = sImgPane.parent();
 if (onDetailsPage()) qRsGroup.css('maxWidth', '' + (maxWidth_qRpImgPane - 40 - $('#qRpImgPane-00§01')[0].clientWidth) + 'px');
 
 pImgInfo.html('');

 Object.keys(sbjDict[concept].attr).forEach 
 ( 
  function(key) 
  {
   if (key === 'Type')     
       pImgInfo.append(' Type is '   + sbjDict[concept].attr[key][0]) + '<nosp>&nbsp&nbsp&nbsp&nbsp</nosp>';
   if (key === 'Domain')   pImgInfo.append(' Domain is ' + sbjDict[concept].attr[key][0]) + '<nosp>&nbsp&nbsp&nbsp&nbsp</nosp>';
   if (key === 'Country')  pImgInfo.append(' from '      + sbjDict[concept].attr[key][0]) + '<nosp>&nbsp&nbsp&nbsp&nbsp</nosp>';
  } 
 );
 
 hilightSubElement(pImgPane, 'img', imgID.replace('#', ''));
 
 var relatedResult = [];
 if (sbjDict[concept].obj.length == 0) 
 try { relatedResult = everestWsCall("FetchRelated", {'domain': domain, 'concept': concept}); } catch (err) { alert(err.message);  }
 
 relatedResult.forEach 
 ( 
  function(result) 
  { 
   var contains = false;
   sbjDict[concept].obj.forEach ( function(item) { if ((item.predicate == result.predicate) && (item.concept == result.concept)) contains = true; } );
   if (!contains)
   {
    sbjDict[concept].obj.push({'predicate' : result.predicate, 'concept' : result.concept}); 
    sbjDict[result.concept] = { 'domain' : result.domain, 'concept' : result.concept, 'idname' : result.idname, 'rank' : result.rank, 'attr' : {}, 'obj' : []};
    sbjDict[result.concept].attr.name = result.name;
    sbjDict[result.concept].attr.type = result.type;
   }
  }
 );

 var shown = [];
 var relatedHtml = '';
 
 sbjDict[concept].obj.forEach 
 ( 
  function(relatedObj) 
  { 
   if (!shown.includes(relatedObj.concept))
   {
    shown.push(relatedObj.concept);
    var obj = sbjDict[relatedObj.concept];
    var sImgDivId = sImgPane.attr('id') + '-' + ('000' + ++imgCounter).slice(-2);
    relatedHtml +=
    '<div id="' + sImgDivId + '" class="' + pfx + 'sImgDiv sImgDiv">\n' + 
    '<div class="' + pfx + 'sImgFrame sImgFrame"><a target="_blank" href="' + itemUrl(obj) + '">' + 
    '<img img class="' + pfx + 'sImg1 sImg1" id="img' + obj.concept + 
    '" src="' + buildImgSrc(obj, true) + 
    '" title="" /></a></div>' + 
    '<div id="' + sImgDivId.replace('Pane', 'Title') + '" title="' + obj.attr.name +
    '" class="' + pfx + 'sImgTitle sImgTitle">' +
    obj.attr.name.substring(0, 16) +
    ' </div></div>'; 
   }
  } 
 );
 $(sImgPane[0]).html(relatedHtml);
 if (onDetailsPage()) 
 if (pfx == 'qR')
 {
  //buildImgDict('qR', qRpImgDict, sImgPane, $(imgID));   // holds DOM-info for any secondary Images in sImgPane (related pane, info pane, Title Div etc ...)
  var qry = '';
  if                (!!sbjDict[concept].attr.Director) qry += ' \'' + sbjDict[concept].attr.Director[0] +  '\'';
  if                (!!sbjDict[concept].attr.Actor)    qry += ' \'' + sbjDict[concept].attr.Actor[0] +  '\'';
  if (qry == '') if (!!sbjDict[concept].attr.Name)     qry += ' \'' + sbjDict[concept].attr.Name[0] +  '\'';
  htmlFromSimilarItems(domain, qry, sImgPane.parent().next());
 }
 fetchSubjectsAttributes();
}



function docReady()
{
 
}

function docBodyScroll()
{
 var viewPortMax = document.body.scrollTop + document.body.scrollHeight;
 //alert(viewPortMax);
 $('div .pImgPane').each
 (
  function(inx, pane)
  {
   pane                   = $(pane);
   var pImgPaneId         = pane[0].id;
   var rect               = clientRect(pane);
   if (rect.top > viewPortMax) return;
   if (hilightedSubElements(pane, 'img').length == 0)
   {
    var imgClickParams = pane.find('img').attr('onclick').replace('imgClick(\'','').replace('\')','').split(',');
    imgClick(imgClickParams[0].replace('\'','').trim(), imgClickParams[1].replace('\'','').trim()); 
   }   
  }    
 );
}







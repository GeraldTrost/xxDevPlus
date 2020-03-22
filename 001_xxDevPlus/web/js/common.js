




// =============== browser and session support


$.extend
(
 $.expr[":"], 
 {
  "Contains": function(elem, i, match, array) 
  {
   return (elem.textContent || elem.innerText || "").toLowerCase().indexOf((match[3] || "").toLowerCase()) >= 0;
  }
 }
);

function slcElement(ret$, slc1, slc2) 
{
 if (!(ret$ instanceof jQuery)) return slcElement($empty, ret$, slc1);
 var ret = $(slc1 + ' ' + slc2);
 return (ret$ == $empty) ? (ret.length ? ret[0] : null) : ret
}

function get_browser_info()
{
 var ua=navigator.userAgent,tem,M=ua.match(/(opera|chrome|safari|firefox|msie|trident(?=\/))\/?\s*(\d+)/i) || []; 
 if(/trident/i.test(M[1])){ tem=/\brv[ :]+(\d+)/g.exec(ua) || []; return {name:'IE ',version:(tem[1]||'')}; };   
 if(M[1]==='Chrome') { tem=ua.match(/\bOPR\/(\d+)/); if(tem!=null) {return {name:'Opera', version:tem[1]};} };   
 M = M[2]?[M[1],M[2]]:[navigator.appName,navigator.appVersion,'-?'];
 if((tem=ua.match(/version\/(\d+)/i))!=null) {M.splice(1,1,tem[1]);} return { name: M[0], version: M[1]};
}

function browserSessionId(){return ('' + get_browser_info().name + get_browser_info().version + new Date().getTime()).split("").reduce(function(a,b){a=((a<<5)-a)+b.charCodeAt(0);return a&a},0); }


// =============== jQuery Extensions and javaScript Extensions 

function notNull(p) {return (p==null) ? '' : (p=='null') ? '' : p;}
if (typeof String.prototype.replaceAll != 'function') String.prototype.replaceAll = function(search, replacement) { var target = this; return target.replace(new RegExp(search, 'g'), replacement); };
if (typeof String.prototype.contains   != 'function') String.prototype.contains   = function(search) { return (this.indexOf(search) > -1); };
if (typeof String.prototype.startsWith != 'function') String.prototype.startsWith = function (str) { return this.indexOf(str) === 0; };
if (typeof $.prototype.bros    != 'function') $.prototype.bros    = function(criteria    ) { return this.parent().children(criteria); };


if (typeof $.prototype.px      != 'function') $.prototype.px      = function(cssAttr, val) 
{ 
 if (typeof val === 'undefined') 
 {
  if (isNaN(Math.floor(this.css(cssAttr).replace('px', '').trim() ))) alert(cssAttr + ' is NOT A NUMBER !!!!');
  return Math.floor(this.css(cssAttr).replace('px', '').trim()); 
 }
 else this.css(cssAttr, ('' + val).trim() + 'px'); 
};

if (typeof $.prototype.nearest != 'function') $.prototype.nearest = function(x, y, dist)   
{ 
 var ret = $empty; var minDist = 99999;
 this.each ( function(inx, item) 
 { 
  var itemPos = rect(item); 
  var 
  itemDist = 
  Math.min
  (
   Math.sqrt( Math.pow(itemPos.left + $(item).px('width') - x, 2) + Math.pow(itemPos.top  + $(item).px('height') - y, 2)),
   Math.sqrt( Math.pow(itemPos.left +                     - x, 2) + Math.pow(itemPos.top  + $(item).px('height') - y, 2)),
   Math.sqrt( Math.pow(itemPos.left + $(item).px('width') - x, 2) + Math.pow(itemPos.top                         - y, 2)),
   Math.sqrt( Math.pow(itemPos.left                       - x, 2) + Math.pow(itemPos.top                         - y, 2)) 
  );
  if (itemDist <= dist) if (itemDist < minDist) { minDist = itemDist; ret = $(item); }
 });
 return ret;
};

// =============== general basic helper functions

var $empty          = $('._select_no_html_element_'), $ddCtl = $empty, ddX = 0, ddY = 0; //dragDrop
function $ctl       (                         $c) { if ($c == undefined) return $empty; if ($c.__proto__ == '[object Object]') return $c; return $($c); }
function absInx     ( i                    , len) { if (i >= 0) return i; return len + i + 1; }
function num        (                        val) { return Math.min(('' + val).trim().match(/[-+]?(\d*[.]?\d*)?/)[0].trim()); }
function alpha      (                        val) { try {return ('' + val).trim().match(/[^\d .-]+/)[0].trim();} catch(err) {} }
function anticipate (percent, min, max, accuracy) { return Math.round ((Math.min(min) + (Math.min(max) - Math.min(min)) * Math.min(percent)) * Math.pow(10, Math.min(accuracy))) / Math.pow(10, Math.min(accuracy)); }

function clientRect($e) // $e : can be an htmlElement or a queryControl(jquery init[]) 
{
 $e = $ctl($e); var e = $e[0], root = document.documentElement, ret = { left : 0,  top : 0,  width : undefined,  height : undefined}
 while (!!e) 
 {
  if ((!ret.width)||(!ret.height)) { ret.width  = e.clientWidth; ret.height = e.clientHeight; }  
  ret.left += (e.offsetLeft + e.clientLeft - (e.tagName!='BODY') ? e.scrollLeft : window.pageXOffset || e.scrollLeft || root.scrollLeft);
  ret.top  += (e.offsetTop  + e.clientTop  - (e.tagName!='BODY') ? e.scrollTop  : window.pageYOffset || e.scrollTop  || root.scrollTop );
  e = e.offsetParent;
 }
 return ret;
}

function firstItem(el) { return (el instanceof jQuery) ? el[0] : el; }

function rect(el) // deprecated, clientRect should be used
{
 $el = $(firstItem(el));
 var root  = document.documentElement;
 var body  = document.body;
 var sTop  = window.pageYOffset || root.scrollTop  || body.scrollTop;
 var sLeft = window.pageXOffset || root.scrollLeft || body.scrollLeft;
 var cTop  = root.clientTop  || body.clientTop  || 0;
 var cLeft = root.clientLeft || body.clientLeft || 0;
 var rect  = $el[0].getBoundingClientRect();
 var top   = Math.round(rect.top  + sTop  - cTop);
 var left  = Math.round(rect.left + sLeft - cLeft);
 return {'top': Math.min(top), 'left': Math.min(left), 'width': $el.px('width'), 'height': $el.px('height')};
}

function addEvent(element, event, handler) { if (element.addEventListener) { element.addEventListener(event, handler, false); } else if (element.attachEvent) { element.attachEvent("on" + event, handler); } }
function urlParam(name) {if(name=(new RegExp('[?&]'+encodeURIComponent(name)+'=([^&]*)')).exec(location.search)) return decodeURIComponent(name[1]);}

function cleanString(str)
{
 var ret = str.trim();
 //replace all ' ' (char(160) by ' ' (char(32));
 do ret = ret.replace('\t', ' ').trim(); while(ret.indexOf('\t') > -1)
 do ret = ret.replace('\r', ' ').trim(); while(ret.indexOf('\r') > -1)
 do ret = ret.replace('\n', ' ').trim(); while(ret.indexOf('\n') > -1)
 do ret = ret.replace('  ', ' ').trim(); while(ret.indexOf('  ') > -1)
 return ret;
}

function obj2str(obj, lvl)
{
 var ret = '';
 if (!lvl) lvl = 1;
 var val = JSON.stringify(obj);
 if (val.startsWith('{')) 
 {
  for (var key in obj)
   if (obj.hasOwnProperty(key)) 
   {
    val = JSON.stringify(obj[key]);
    if (!val)
     ret += '\r\n' + Array(lvl+1).join(' ') + "'" + key + "': " + obj[key];
    else 
     if (val.startsWith('{'))
      ret += '\r\n' + Array(lvl+1).join(' ') + "'" + key + "': " + obj2str(obj[key]).replace(/\\r\\n/g , '\r\n' + Array(lvl+6+key.length).join(' '));
     else
      if ((val.startsWith('"')) || (val.startsWith("'")))
       ret += '\r\n' + Array(lvl+1).join(' ') + "'" + key + "': " + "'" + val.substring(1, val.length - 1).replace(/\\r\\n/g , '\r\n' + Array(lvl+6+key.length).join(' ')) + "'";
      else
       ret += '\r\n' + Array(lvl+1).join(' ') + "'" + key + "': " + val;
   }
 }
 else 
 { 
  if ((val.startsWith('"')) || (val.startsWith("'")))
   ret = "'" + val.substring(1, val.length - 1) + "'" ;
  else
   ret = val;
 }
 return ret;
}


// =============== sliders support

/*
function writeSlider($parent, hdlWidth, hdlHeight, barHeight, txtWidth, txtHeight, minVal, caption, maxVal, hdlColor, barColor)
{
 var ret = '';
 $parent.html('<div style="margin:0px;border:0px;padding:0px;"></div>');
 var width  = $($parent.children()[0]).px('width'); 
 var height = 2 * hdlHeight + barHeight + txtHeight;
 ret += '<div  class="sliderDiv"  style="min-width:' + width + 'px;max-width:' + width + 'px;min-height:' + height + 'px;max-height:' + height + 'px;">\r\n';
 ret += ' <div class="sliderBar"  style="top:' + hdlHeight + 'px;min-height:' + barHeight + 'px;max-height:' + barHeight + 'px;background-color:' + hdlColor + ';"></div>\r\n';
 ret += ' <div class="sliderLbl"  style="margin-top:-' + barHeight                    + 'px;top:' + hdlHeight + 'px;min-height:' + barHeight + 'px;max-height:' + barHeight + 'px;line-height:' + barHeight + 'px;background-color:' + barColor + ';"><center>2··12</center></div>\r\n';
 ret += ' <div class="sliderHnd"  style="margin-top:-' + barHeight                    + 'px;min-width:' + hdlWidth + 'px;max-width:' + hdlWidth + 'px;min-height:' + (2 * hdlHeight + barHeight) + 'px;max-height:' + (2 * hdlWidth + barHeight) + 'px;background-color:' + barColor + ';"></div>\r\n';
 ret += ' <div class="sliderHnd"  style="margin-top:-' + (2 * hdlHeight + barHeight)  + 'px;min-width:' + hdlWidth + 'px;max-width:' + hdlWidth + 'px;min-height:' + (2 * hdlHeight + barHeight) + 'px;max-height:' + (2 * hdlWidth + barHeight) + 'px;background-color:' + barColor + ';"></div>\r\n';
 ret += ' <div class="sliderMin"  style="margin-top:-' + (2 * hdlHeight + barHeight)  + 'px;left: 0px;top:' + (2 * hdlHeight + barHeight) + 'px;min-width:' + txtWidth + 'px;max-width:' + txtWidth + 'px;min-height:' + txtHeight + 'px;max-height:' + txtHeight + 'px;line-height:' + txtHeight + 'px;">' + minVal + '</div>\r\n';
 ret += ' <div class="sliderCap"  style="margin-top:-' + txtHeight                    + 'px;top:' + (2 * hdlHeight + barHeight) + 'px;left:' + txtWidth + 'px  ;min-width:' + (width - 2 * txtWidth) + 'px;max-width:' + (width - 2 * txtWidth) + 'px;min-height:' + txtHeight + 'px;max-height:' + txtHeight + 'px;line-height:' + txtHeight + 'px;">' + caption + '</div>\r\n';
 ret += ' <div class="sliderMax"  style="margin-top:-' + txtHeight                    + 'px;top:' + (2 * hdlHeight + barHeight) + 'px;left:' + (width - txtWidth) + 'px  ;min-width:' + txtWidth + 'px;max-width:' + txtWidth + 'px;min-height:' + txtHeight + 'px;max-height:' + txtHeight + 'px;line-height:' + txtHeight + 'px;">' + maxVal + '</div>\r\n';
 ret += '</div>\r\n';
 $parent.html(ret);
 $ddCtl = $($($parent.children()[0]).children('.sliderHnd')[0]); sliderMouseEvent({'buttons': '1', 'clientX': '-1000'}, 3);
 $ddCtl = $($($parent.children()[0]).children('.sliderHnd')[1]); sliderMouseEvent({'buttons': '1', 'clientX':  '1000'}, 3);
 $ddCtl = $empty;
 return ret;
}
*/

/*
function sliderMouseEvent(ev, evType)
{
 if ((evType == 1)) 
 { 
  $ddCtl = $(".sliderHnd").nearest(ev.clientX, ev.clientY, 12); //$ddCtl = $(ev.currentTarget); 
  if ($ddCtl.length == 0) return;
  var pos = rect($ddCtl); ddX = pos.left + $ddCtl.px('width') / 2; ddY = pos.top + $ddCtl.px('height') / 2; 
  return sliderMouseEvent(ev, 3);
 }
 if (evType == 2) $ddCtl = $empty;
 if ($ddCtl.length == 0) return;
 if ((evType == 3) && (ev.buttons == '1'))  
 { 
  var newX = $ddCtl.px('left') + (ev.clientX - ddX);
  newX = Math.min(Math.max(newX, 0), $ddCtl.parent()[0].clientWidth - $ddCtl[0].clientWidth);
  $ddCtl.px('left', newX);
  //$ddCtl.animate({left: newX});
  ddX = ev.clientX;
  var $sliderLbl = $ddCtl.bros('.sliderLbl');
  var $sliderHnd = $ddCtl.bros('.sliderHnd');
  var x1 = $($sliderHnd[0]).px('left');
  var x2 = $($sliderHnd[1]).px('left');
  $sliderLbl.px('left', Math.min(x1, x2));
  $sliderLbl.px('min-width', Math.abs(x2 - x1));
  $sliderLbl.px('max-width', Math.abs(x2 - x1));
  if (x2 < x1) { var x = x1; x1 = x2; x2 = x;}
  x2 += $sliderHnd.px('width');
  $sliderLbl.html
  (
   '' +
   anticipate
   (
    (x1 / $ddCtl.parent()[0].clientWidth), 
    num($ddCtl.bros('.sliderMin').text()), 
    num($ddCtl.bros('.sliderMax').text()), 0
   ) +
   ' .. ' +
   anticipate
   (
    (x2 / $ddCtl.parent()[0].clientWidth), 
    num($ddCtl.bros('.sliderMin').text()), 
    num($ddCtl.bros('.sliderMax').text()), 0
   ) 
   //  + ' ' + alpha($ddCtl.bros('.sliderMax').text()) 
 );
 }
}
*/

function trc(loc, obj) { console.log('\r\n[' + (window.performance.now() / 1000).toFixed(3) + ': ' + loc + '] ' + obj2str(obj)); }

// used for try - catch
function throwErr(loc, err) 
{
 if (!!loc) {
  jsLog('error', err.message, loc);
  alert('Error at ' + loc + ' ' + err);
 }
 else 
 {
  jsLog('error', err.message, 'unknown location');
  alert('Error at unknown location ' + err);
 }
 throw err;
}

function toggleVisibility(element) { $(element)[0].style.display = ($(element)[0].style.display == "block") ? "none" : "block"; } 


// START block for google-analytics
//var _gaq = _gaq || []; _gaq.push(['_setAccount', 'UA-33848682-1']); _gaq.push(['_trackPageview']);
//(
// function () 
// {
//  var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;
//  ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
//  var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);
// }
//)
//();

var lpcConstraints   = {}; lpcConstraints.optional = [{ 'googIPv6': false}];
//stun:stun.l.google.com:19302
var offerConstraints = { 'mandatory': { 'OfferToReceiveAudio': true} }; offerConstraints.optional = [{ 'googUseRtpMUX': true}];   //true == gather bundled RTCP candidates only
// END block for google-analytics

navigator.getUserMedia = navigator.getUserMedia || navigator.webkitGetUserMedia || navigator.mozGetUserMedia;
var pntWin   = null;  // parent Window in case the document is in an iFrame
var pntDoc   = null;  // document of parent Window in case the document is in an iFrame
try { if (window.parent.document.getElementById('chatBox').contentWindow) { pntWin = window.parent; pntDoc  = window.parent.document; }} catch (err) {}

var cbWin    = function() { try { return (pntWin == null) ? document.getElementById('chatBox')    : pntDoc.getElementById('chatBox')    ; } catch (err) {}}
var cbDoc    = function() { return cbWin().contentDocument || cbWin().contentWindow.document; }

var lVid       = function() { return (pntDoc == null) ? document.getElementById('lVideo')        : pntDoc.getElementById('lVideo')         ;}
var rVid       = function() { return (pntDoc == null) ? document.getElementById('rVideo')        : pntDoc.getElementById('rVideo')         ;}

var btnLogout  = function() { return (pntDoc == null) ? document.getElementById('btnLogout')     : pntDoc.getElementById('btnLogout')      ;}
var btnStart   = function() { return (pntDoc == null) ? document.getElementById('btnStartCall')  : pntDoc.getElementById('btnStartCall')   ;}
var btnAnswer  = function() { return (pntDoc == null) ? document.getElementById('btnAnswerCall') : pntDoc.getElementById('btnAnswerCall')  ;}
var btnHangup  = function() { return (pntDoc == null) ? document.getElementById('btnHangupCall') : pntDoc.getElementById('btnHangupCall')  ;} //var btnHangup  = function() { return (pntDoc != null)  ? pntDoc.getElementById('btnHangupCall')  : document.getElementById('btnHangupCall') ;}

var txtAddMsg  = function() 
{ 
 return cbDoc().getElementById('txtAddMessage');
 //return $('[id$txtAddMessage]')[0]; 
 //return cbDoc().getElementById('MainContent_txtAddMessage');
}

var btnAddMsg  = function() 
{
 return cbDoc().getElementById('btnAddMessage');
 //return $('[id$=btnAddMessage]')[0]; 
 //return cbDoc().getElementById('MainContent_btnAddMessage');
}

var btnToPartner = function() { return (pntWin == null) ? document.getElementById('btnToPartner') : pntDoc.getElementById('btnToPartner'); }

var useVideo   = function() { return !!lVid(); }

var lStream;

function getlUserMedia(vid, video) // get local Webcam and local Microphone
{
 trc('METHOD getlUserMedia', 'preparing lStream');
 if (!!lStream) return lStream;
 if (navigator.getUserMedia === navigator.webkitGetUserMedia)
  navigator.getUserMedia({ 'audio': true, 'video': video }, function (s) { lStream = s; vid.src = URL.createObjectURL(s); }, function (err) { alert(err); });
 else
  navigator.getUserMedia({ 'audio': true, 'video': video }, function (s) { lStream = s; vid.mozSrcObject = s; video.play(); }, function (err) { alert(err); });
 return lStream;
}


function webAppCall(method, params)     // Call a Method in the c# WebApp
{
 trc('METHOD webAppCall', method);
 var ret;
 var err;
 $.ajax
 (
  {
   'async':           false,
   'ensuresuccess':   true,
   'type':            'POST',
   'contentType':     'application/json; charset=utf-8',
   'dataType':        'json',
   'url':             '/' + document.location.pathname.match(/[^\/]+$/)[0] + '/' + method,
   'data':             JSON.stringify(params),
   'success':          function (msg) { ret = msg.d; },
   'error':            function (rq, txt, origErr) { err = new Error($.parseJSON(rq.responseText).Message); }
  }
 );
 if (err) throw err;
 return ret;
}

var userSession; try { userSession = webAppCall('userSession', {}); } catch (err) { /* alert(err); */ }
var uploadPhotoToCallEndpoint = (location.host.substring(0, 11) == 'vw1aivistos') ? 'http://vw1aivistos:2222/UploadPhotoToCall.ashx/'  : '/SvcVideoAssessment/UploadPhotoToCall.ashx/';

function getHtmlTemplate(TemplateName)
{
 var ret;
 var htmlTemplates = vaWsCall('FindOrganizationAttachmentByUser', { 'usr': userSession.User });
 htmlTemplates.forEach ( function(element) { if (ret) return; if ((element.ClassType == 'HtmlTemplate') && (element.Name == TemplateName)) ret = element;}  );
 if (ret) return ret;
 htmlTemplates = vaWsCall('FindOrganizationAttachmentByUser', { 'usr': vaWsCall('GetUser', { 'key': 1 }) }); //if no Template in Organization is set, take the default from Adpunctum/Administrator
 htmlTemplates.forEach  ( function(element) { if (ret) return; if ((element.ClassType == 'HtmlTemplate') && (element.Name == TemplateName)) ret = element;}  );
 return ret;
}

function fillHtmlTemplate(TemplateName, cll)
{
 var downloadBuf = vaWsCall('DownloadOrganizationAttachmentData', { 'oa': getHtmlTemplate(TemplateName) });
 var ret = new TextDecoder('utf-8').decode(new Uint8Array(downloadBuf));
 ret = ret.substring(0, ret.indexOf('</body>'));
 ret = ret.substring(ret.indexOf('<body>') + 7);
 try { ret = ret.replace('<%ID%>'                          , cll.Key);                                                                                                                       } catch(err) {}
 try { ret = ret.replace('<%Name%>'                        , cll.Name);                                                                                                                      } catch(err) {}
 try { ret = ret.replace('<%FinishedAt%>'                  , cll.FinishedAt);                                                                                                                } catch(err) {}
 try { ret = ret.replace('<%CreatedAt%>'                   , cll.CreatedAt);                                                                                                                 } catch(err) {}
 try { ret = ret.replace('<%StartedAt%>'                   , cll.StartedAt);                                                                                                                 } catch(err) {}
 try { ret = ret.replace('<%Status%>'                      , cll.Status);                                                                                                                    } catch(err) {}
 try { ret = ret.replace('<%Caller.ID%>'                   , cll.Caller.Key);                                                                                                                } catch(err) {}
 try { ret = ret.replace('<%Callee.ID%>'                   , cll.Callee.Key);                                                                                                                } catch(err) {}
 try { ret = ret.replace('<%Caller.Name%>'                 , cll.Caller.Name);                                                                                                               } catch(err) {}
 try { ret = ret.replace('<%Callee.Name%>'                 , cll.Callee.Name);                                                                                                               } catch(err) {}
 try { ret = ret.replace('<%Caller.Organization.ID%>'      , cll.Caller.Organization.Key);                                                                                                   } catch(err) {}
 try { ret = ret.replace('<%Callee.Organization.ID%>'      , cll.Callee.Organization.Key);                                                                                                   } catch(err) {}
 try { ret = ret.replace('<%Caller.Organization.Name%>'    , cll.Caller.Organization.Name);                                                                                                  } catch(err) {}
 try { ret = ret.replace('<%Callee.Organization.Name%>'    , cll.Callee.Organization.Name);                                                                                                  } catch(err) {}
 try { ret = ret.replace('<%ElapsedMinutes%>'              , 1 + Math.floor( (new Date() - new Date(cll.CreatedAt.replace('-', '/').replace('-', '/').replace('-', '/'))) / 60000));         } catch(err) {}
 try { ret = ret.replace('<%Case.ID%>'                     , cll.Case.Key);                                                                                                                  } catch(err) {}
 try { ret = ret.replace('<%Case.Name%>'                   , cll.Case.Name);                                                                                                                 } catch(err) {}
 try { ret = ret.replace('<%Case.Brand%>'                  , cll.Case.Brand);                                                                                                                } catch(err) {}
 try { ret = ret.replace('<%Case.Model%>'                  , cll.Case.Model);                                                                                                                } catch(err) {}
 try { ret = ret.replace('<%Case.Mileage%>'                , '' + cll.Case.Mileage);                                                                                                         } catch(err) {}
 try { ret = ret.replace('<%Case.CarSerialNumber%>'        , cll.Case.CarSerialNumber);                                                                                                      } catch(err) {}
 try { ret = ret.replace('<%Case.ExternalLPN%>'            , cll.Case.ExternalLPN);                                                                                                          } catch(err) {}
 try { ret = ret.replace('<%Case.LicencePlateNumber%>'     , cll.Case.LicencePlateNumber);                                                                                                   } catch(err) {}
 try { ret = ret.replace('<%Case.InsurancePolicyNumber%>'  , cll.Case.InsurancePolicyNumber);                                                                                                } catch(err) {}
 try { ret = ret.replace('<%Case.DefectAt%> '              , cll.Case.DefectAt);                                                                                                             } catch(err) {}
 try { ret = ret.replace('<%Case.ApprovedAt%>'             , cll.Case.ApprovedAt);                                                                                                           } catch(err) {}
 try { ret = ret.replace('<%Case.ExternalID%>'             , cll.Case.ExternalID);                                                                                                           } catch(err) {}
 try { ret = ret.replace('<%Case.Dat1%>'                   , cll.Case.Dat1);                                                                                                                 } catch(err) {}
 try { ret = ret.replace('<%Case.Dat2%>'                   , cll.Case.Dat2);                                                                                                                 } catch(err) {}
 try { ret = ret.replace('<%Case.Dat3%>'                   , cll.Case.Dat3);                                                                                                                 } catch(err) {}
 try { ret = ret.replace('<%Case.Dat4%>'                   , cll.Case.Dat4);                                                                                                                 } catch(err) {}
 try { ret = ret.replace('<%Case.Dat5%>'                   , cll.Case.Dat5);                                                                                                                 } catch(err) {}
 return ret;
}

function vaWsUploadPhotoToCall(file, param)
{
 trc('METHOD vaWsUploadPhotoToCall', '');
 var ret;
 var err;
 var formData = new FormData();
 formData.append('ticket'  , userSession.ticket);
 formData.append('param'   , JSON.stringify(param));
 formData.append("file"    , file);
 $.ajax
 (
  {
   'async'          : false,
   'ensuresuccess'  : true,
   'type'           : 'POST', 
   'url'            : uploadPhotoToCallEndpoint,
   'contentType'    : false,
   'data'           : formData,
   'cache'          : false,
   'processData'    : false,
   'timeout'        : 30000,     // 30 second timeout
   'beforeSend'     : function (evp)   { },
   'progress'       : function (evp)   { },
   'complete'       : function ()      { },
   'success'        : function (msg) { ret = msg; },
   'error'          : function (rq, txt, origErr) { err = new Error($.parseJSON(rq.responseText).Message); }
  }
 );
 if (err) throw err;
 return ret;
}

function dscObj(dsc) //builds an RtcDescription Object from a RtcDescription string
{
 var ret = new RTCSessionDescription($.parseJSON(dsc));
 ret.sdp = ret.sdp.replace(/\\r\\n/g , '\r\n');
 return ret;
}

var initialVisibility;

function visible(pattern) 
{
 if (!pattern) visible(initialVisibility);
 else 
 {
  try 
  {
   if (!initialVisibility) initialVisibility = pattern;
   if (pattern.sb !== undefined) if (btnStart() != null) btnStart().hidden = !pattern.sb;                                 // StartcallButton
   if (pattern.ab !== undefined) if (btnAnswer() != null) btnAnswer().hidden = !pattern.ab;                               // AnswercallButton
   if (pattern.hb !== undefined) if (btnHangup() != null) btnHangup().hidden = !pattern.hb;                               // HangupcallButton
   if (pattern.cb !== undefined) if (cbWin()) cbWin().parentNode.style.display = pattern.cb        ? "inline-block" : "none";    // ChatBox
   if (pattern.cq !== undefined) 
   {
    document.getElementById('CallsQueue').style.display       = pattern.cq  ? "inline-block" : "none";            // CallsQueue
    document.getElementById('CallsHistory').style.display     = pattern.cq  ? "inline-block" : "none";            // CallsHistory
    document.getElementById('ChatBox').style.display          = pattern.cq  ? "none"         : "inline-block" ;   // ChatBox
    document.getElementById('RightMid').style.display         = pattern.cq  ? "none"         : "inline-block" ;   // Right Part of Middle Section (RemoteVideo or Bighoto)
   }
   if (useVideo()) if (pattern.lv !== undefined) lVid().hidden = true;                                                    // LocalVideo   // = !pattern.lv
   if (useVideo()) if (pattern.rv !== undefined) rVid().hidden = !pattern.rv;                                             // RemoteVideo 
   if (pattern.bp !== undefined) document.getElementById('RemoteVideo').style.display = pattern.bp ? "none"  : "block";   // RemotVideo 
   if (pattern.bp !== undefined) document.getElementById('BigPhoto').style.display = pattern.bp    ? "block" : "none";    // BigPhoto
  }
  catch (err) {}
 }
}

var iceServers = [];

iceServers.push({'url': "turn:turn.anyfirewall.com:443?transport=tcp", 'credential': "webrtc", 'username': "webrtc"});
iceServers.push({'url': "turn:turn.bistri.com:80", 'credential': "homeo", 'username': "homeo"});
iceServers.push({'url': "turn:numb.viagenie.ca", 'credential': "muazkh", 'username': "webrtc@live.com"});
iceServers.push({'url': "turn:192.158.29.39:3478?transport=udp", 'credential': "JZEOEt2V3Qb0y27GRntt2u2PAYA=", 'username': "28224511:1379330808"});
iceServers.push({'url': "turn:192.158.29.39:3478?transport=tcp", 'credential': "JZEOEt2V3Qb0y27GRntt2u2PAYA=", 'username': "28224511:1379330808"});

iceServers.push({ "url": "turn:52.27.196.175:3478?transport=tcp" });
iceServers.push({ "url": "turn:52.27.196.175:3478?transport=udp" });
iceServers.push({ "url": "turn:52.27.196.175:3478" });
iceServers.push({ "url": "stun:52.27.196.175:3478" });

iceServers.push({'url': "stun:stun.anyfirewall.com:3478"});
iceServers.push({'url': "stun:stunserver.org"});
iceServers.push({'url': "stun:stun.xten.com"});
iceServers.push({'url': "stun:stun.softjoys.com"});
iceServers.push({'url': "stun:stun.schlund.de"});
iceServers.push({'url': "stun:stun.rixtelecom.se"});
iceServers.push({'url': "stun:stun.iptel.org"});
iceServers.push({'url': "stun:stun.ideasip.com"});
iceServers.push({'url': "stun:stun.ekiga.net"});
iceServers.push({'url': "stun:stun01.sipphone.com"});
iceServers.push({'url': "stun:23.21.150.121"});
iceServers.push({'url': "stun:stun.fwdnet.net"});
iceServers.push({'url': "stun:stun.voiparound.com"});
iceServers.push({'url': "stun:stun.voiparound.com"});
iceServers.push({'url': "stun:stun.voipbuster.com"});
iceServers.push({'url': "stun:stun.voipstunt.com"});
iceServers.push({'url': "stun:stun.voxgratia.org"});
iceServers.push({'url': "stun:stun4.l.google.com:19302"});
iceServers.push({'url': "stun:stun3.l.google.com:19302"});
iceServers.push({'url': "stun:stun2.l.google.com:19302"});
iceServers.push({'url': "stun:stun1.l.google.com:19302"});
iceServers.push({'url': "stun:stun.l.google.com:19302"});


function iceSelected()
{
 console.log('\r\n USING THESE ICESERVERS - you can add to the PAGE-URL ?ice=1,2,3 to select only the first 3 ICESERVERS!');
 var selectIce = urlParam('ice');
 if (selectIce) selectIce = selectIce.split(',').map(Number); else { selectIce = []; for(var i = 1; i <= iceServers.length; i++) selectIce.push(i); }
 var ret = []; 
 var ctr = 0;
 iceServers.forEach
 (
  function(s) 
  { 
   var log = '';
   if (selectIce.indexOf(++ctr)!=-1) { log+='\r\n (+) '; ret.push(iceServers[ctr-1]); } else log+='\r\n (-) ';
   console.log(log + ' ' + ctr + ' ' + iceServers[ctr-1].url);
  }
 );
 //if(urlParam('ice')) alert("ICESERVERS have been selected - see the console log. continue?");
 return ret;
}


var leavingPage         = false;
var COMPACT_SIGNALLING  = true;
var doHeartBeat         = false; // this is only used in !COMPACT_SIGNALLING mode because COMPACT_SIGNALLING always makes RefreshCall when connection has been established
var callerMode;
var calleeMode;
var pc;                 // the PeerConnection
var cll                 = null;             
var poll;
var ivPoll;
var rCandidates         = 0;
var rDescription        = null; //the remote Description that came via signalling
var sChData;            // SendChannel 
var rChData;            // ReceiveChannel 

function endCall() 
{
 try 
 {
  $('#thumbsArea').html('');
  document.getElementById('ThumbsArea').style.width = '0px';
  $('#TopSectionContainer').html('');
 }
 catch (err) {}
 try 
 {
  clearInterval(poll);
  trc('METHOD endCall', 'endCall');
  rCandidates            = 0;
  rDescription           = null; 
  doHeartBeat            = false; 
  poll = null;
  visible();
  if (useVideo()) rVid().src = null;
  try { sChData.close(); } catch (err) {}
  try { rChData.close(); } catch (err) {}
  try { pc.close();      } catch (err) {}
  pc = null;
  alert('Der Anruf wurde beendet');
  if (cll == null) return;
  cll = vaWsCall('FinishCall', { 'cl': cll });
  cll = null;
 }
 catch (err) {}
}


function doCall(pc2use, cll2use, useCompact_Signalling) // use this function for starting calls and for answering calls
{
 trc('METHOD doCall', 'doCall');
 COMPACT_SIGNALLING = (useCompact_Signalling === 'True') ? true : false;
 ivPoll = (COMPACT_SIGNALLING) ? 1500: 200;
 pc    = pc2use;
 cll   = cll2use;
 callerMode = (cll.Key == 0);
 calleeMode = !callerMode;

 var rMissed = 0;    // Counter to tell how often the remote Side missed to call the HeartBeats 
 var rHeartBeat;     // the remote side's latest HeartBeat TimeStamp
 trc('INVOKING createDataChannel', 'creating local dataChannel in local peerconnection');

 function watchCall() // this is the the polling loop
 {
  trc('METHOD watchCall', 'watchCall');
  if ((cll == null) || (cll.Key == 0)) return;
  try 
  {
   if (rDescription == null) //we have not got a remote description yet
   {
    trc('INFO watchCall', 'no rDescription, still trying to connect');
    cll = vaWsCall('RefreshCall', { 'cl': cll, 'rtcDesc': JSON.stringify(pc.localDescription), 'fromCaller': callerMode }); //cll = vaWsCall('GetCall', { 'key': cll.Key });
    if (0 != ((callerMode) ? cll.RtcCallee.length : cll.RtcCaller.length))
    {
     rDescription = ((callerMode) ? cll.RtcCallee : cll.RtcCaller); 
     pc.setRemoteDescription(new RTCSessionDescription(dscObj(rDescription))); 
    }
    return;
   }
   trc('INFO watchCall', 'already have a rDescription, doing heartbeat');
   if ((!COMPACT_SIGNALLING) && (!doHeartBeat))
   {
    trc('INFO watchCall', 'no compact signalling - treating every iceCandidate seperately');
    var res = vaWsCall('IceCandidate', { 'cl': cll, 'candidate': '', 'fromCaller': callerMode });
    for (var i = rCandidates + 1; i <= res.length; i++) pc.addIceCandidate(new RTCIceCandidate($.parseJSON(res[i-1])));
    rCandidates = res.length;
    //if (!doHeartBeat) return;
   }
   cll = vaWsCall('RefreshCall', { 'cl': cll, 'rtcDesc': JSON.stringify(pc.localDescription), 'fromCaller': callerMode });
   if (cll.FinishedAt != '<never>') endCall();
   if (rDescription !== ((callerMode) ? cll.RtcCallee : cll.RtcCaller)) 
   {
    trc('WARNING watchCall', 'setting rDescription again!!!');
    pc.setRemoteDescription(new RTCSessionDescription(rDescription = ((callerMode) ? cll.RtcCallee : cll.RtcCaller)));
   }
   if (rHeartBeat == ((callerMode) ? cll.HeartBeatCallee : cll.HeartBeatCaller)) { trc('WARNING missing remote heartbeat; Callermode: ', callerMode); rMissed++; } else { rHeartBeat = ((callerMode) ? cll.HeartBeatCallee : cll.HeartBeatCaller); rMissed = 0; }
   if (rMissed > (10500 / ivPoll)) { /* alert('no more  Heartbeats come from the remote site'); */ rMissed = 0; endCall(); }
  }
  catch (err) { /* alert(err.Message); //for debug only */ }
 }

 function signalDescription(dsc) 
 { 
  trc('METHOD signalDescription', dsc);
  if (!!poll) throw new Error('signal while polling');
  var dscStr = JSON.stringify(dsc); 

  cll = 
  ((callerMode) ? 
   (cll.calledOrganization == null) ? 
    vaWsCall('OpenCallToUser', { 'cl': cll, 'caller': userSession.User, 'callee': cll.callee, 'rtcCaller': dscStr }) 
   :vaWsCall('OpenCallToOrganization', { 'cl': cll, 'caller': userSession.User, 'calledOrganization': cll.calledOrganization, 'rtcCaller': dscStr }) 
  :vaWsCall('AnswerCall', { 'cl': cll, 'callee': userSession.User, 'rtcCallee': dscStr }));

  if (callerMode) cbWin().src = "frmChatBox.aspx?callid=" + cll.Key;
  poll = setInterval(watchCall, ivPoll);
 }

 function signalIceCandidate(candidate) 
 { 
  trc('METHOD signalIceCandidate', candidate);
  var cndStr = JSON.stringify(candidate); 
  var res = vaWsCall('IceCandidate', { 'cl': cll, 'candidate': cndStr, 'fromCaller': callerMode });
 }

 function sChDataStateChange() 
 {
  try 
  {
   trc('EVENT sChDataStateChange (doing nothing here)', sChData.readyState);
   if (sChData.readyState === 'open') 
   {
    // once the connection is established we must poll slower - no more signalling is needed - only WebService-HeartBeat (RefreshCall) is needed.
    doHeartBeat = true;  // doHeartBeat is ignored in COMPACT_SIGNALLING mode
    clearInterval(poll);
    ivPoll = 750;
    poll = setInterval(watchCall, ivPoll);
   }
   else 
   {
    if (sChData.readyState === 'closed') try { endcall(); } catch (err) {}
   }
  }
  catch (err) { trc('Event sChDataStateChange', err); }
 }

 function rChDataStateChange() 
 {
  try 
  {
   trc('EVENT rChDataStateChange (doing nothing here)', rChData.readyState);
   if (rChData.readyState === 'open') 
   {
   }
   else 
   {
    if (rChData.readyState === 'closed') try { endcall(); } catch (err) {}
   }
  }
  catch (err) { trc('Event rChDataStateChange', err); }
 }

 function rChDataOnMessage(evp) 
 {
  try 
  {
   if (typeof evp.data === 'string')
   {
    trc('EVENT rChDataOnMessage', evp.data);
    if (evp.data.startsWith('[RtcDescription]'))  //In a first Prototype I tried to make signalling with the remoteDataChannel. did not work. no longer used.
     pc.setRemoteDescription(new RTCSessionDescription($.parseJSON(evp.data.substring(15)))); 
    else 
    {
     if (evp.data.startsWith('::photo::'))
     {
      onPhotoReceived(evp);
     }
     else
     {
      txtAddMsg().text  = evp.data;
      txtAddMsg().value = evp.data;
      btnAddMsg().click();
      chatProtocol('Inspector', txtAddMsg().value);
     }
    }
   }
   else
   {
    //document.getElementById("imgPreview").src = urlCreator.createObjectURL(new Blob([new Uint8Array(evp.data)], {type:'image/jpeg'}));
    //document.getElementById("imgPreview").src = "data:image/png;base64," + evp.data;
   }
  }
  catch (err) { trc('Event rChDataOnMessage', err); }
 }
 trc('INVOKING addStream', 'adding local stream to local peerconnection');
 if (useVideo()) pc.addStream(getlUserMedia(lVid(), true));
 pc.onaddstream    = function (evp) { trc('EVENT onaddstream', evp); try { if (useVideo()) rVid().src = URL.createObjectURL(evp.stream); } catch (err) { throwErr('onaddstream', err); } };
 pc.onremovestream = function (evp) { trc('EVENT onremovestream'); try { endCall(); } catch (err) { throwErr('onremovestream', err); } };
 pc.onicecandidate = function (evp) 
 { 
  try  { if (COMPACT_SIGNALLING) { if (!evp.candidate) signalDescription(pc.localDescription); } else { if (evp.candidate) signalIceCandidate(evp.candidate); } } 
  catch (err) { throwErr('onicecandidate', err); } 
 }
 pc.ondatachannel = function (evp) 
 {
  try 
  {
   rChData = evp.channel;
   rChData.onopen     = rChDataStateChange;
   rChData.onclose    = rChDataStateChange;
   rChData.onmessage  = rChDataOnMessage;
   btnToPartner().msg     = 'hello partner';
   btnToPartner().click();
   //sChData.send('hello partner');
  }
  catch (err) { trc('Event ondatachannel', err); }
 }

 pc.onnegotiationneeded = function (evp) 
 {
  try 
  {
   trc('EVENT onnegotiationneeded (doing nothing here)', evp);
  }
  catch (err) { trc('Event onnegotiationneeded', err); }
 }

 sChData = pc.createDataChannel('sChData', { reliable: false });  // SendChannel for Signalling
 sChData.onopen  = sChDataStateChange;
 sChData.onclose = sChDataStateChange;

 if (callerMode)
 {
  pc.createOffer(function (dsc) { pc.setLocalDescription(dsc); if (!COMPACT_SIGNALLING) signalDescription(pc.localDescription);}, function (err) { alert(err); }, offerConstraints);
 }
 else 
 {
  rDescription = cll.RtcCaller; // we set rDescription already here so in the polling loop "watchCall" will it not be set again
  pc.setRemoteDescription(dscObj(cll.RtcCaller), function () { },  function (err) { alert(err);});
  pc.createAnswer(function (dsc) { pc.setLocalDescription(dsc); if (!COMPACT_SIGNALLING) signalDescription(pc.localDescription);}, function (err) { alert(err);});
 }

 
}

/*
$(document).ready
(
 function() 
 {
  $(document).mousedown       ( function(ev) { sliderMouseEvent(ev, 1);} );
  $(document).mouseup         ( function(ev) { sliderMouseEvent(ev, 2);} );
  $(document).mousemove       ( function(ev) { sliderMouseEvent(ev, 3);} );
 }
);
*/

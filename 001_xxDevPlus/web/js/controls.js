 



var $ddCtl = $empty, ddX = 0, ddY = 0; //dragDrop

var getSlcTabsAllItems = 1;   //return all items of selected tabPages
var getSlcTabsSlcItems = 2;   //return only the selected items of selected tabPages
var getAllTabsAllItems = 3;   //return all items of all tabPages
var getAllTabsSlcItems = 4;   //return the selected items of all tabPages

$(document).on('click'     , '[class^="tggl"]', function() {tgglDiv($(this), 2);} );
//$(document).on('mousemove' , function(ev) { animate($('.slcLabel1').nearest(ev.clientX, ev.clientY, 12));});

$(document).ready
(
 function() 
 {
  $(document).mousedown       ( function(ev) { docMouseEvent(ev, 'down');} );
  $(document).mouseup         ( function(ev) { docMouseEvent(ev, 'up');} );
  $(document).mousemove       ( function(ev) { docMouseEvent(ev, 'move');} );
 }
);


function hilightedSubElements($el, slc$, minStatus = 2, maxStatus = 2)
{
 var ret = [];
 var singleSelect = (maxStatus < 0);
 maxStatus = Math.abs(maxStatus);
 $el = $ctl($el);
 var $col = $el.find(slc$); 
 $col.each
 (
  function(inx, $el)
  {
   $el           = $ctl($el);
   inx++;   
   var cls       = $el.attr('class');
   var curStatus = Math.abs(cls.substring(cls.length - 1));
   cls           = cls.substring(0, cls.length - 1);
   if ((curStatus >= minStatus) && (curStatus <= maxStatus)) ret.push({'inx': inx, 'item': $el});
  }
 );
 return ret;
}

function hilightSubElement($el, slc$, act, minStatus = 1, maxStatus = -2) // act indicates which one to activate, act may be an id or an index (1 to n or -1 to -n), act=0 deactivates all; negative maxStatus means: do singleSelect
{
 var singleSelect = (maxStatus < 0);
 maxStatus = Math.abs(maxStatus);
 var actIsId = (act != 0 + act);
 $el = $ctl($el);
 var $col = $el.find(slc$); 
 if (!actIsId) act = absInx(act, $col.length);
 $col.each
 (
  function(inx, $el)
  {
   $el           = $ctl($el);
   inx++;   
   var cls       = $el.attr('class');
   var id        = $el.attr('id');
   var curStatus = Math.abs(cls.substring(cls.length - 1));
   cls           = cls.substring(0, cls.length - 1);
   if ((actIsId && (act == id)) || (!actIsId && (act == inx))) // this item is to made active
   {
    var newStatus = curStatus + 1;
    if (newStatus > maxStatus) newStatus = minStatus;
    $el.attr('class', cls + newStatus);
    //$el.css('display', 'inline-block');
   }
   else if (singleSelect) if (curStatus != minStatus) $el.attr('class', cls + minStatus);
  }
 );
}


// works for elements with stype (class) 'xxxxxxxx1' (not selected style) and 'xxxxxxxx2'   (selected style)
function selectElement($el, mode)  // mode 0: deselect     mode 1: select      mode -1: toggle
{
 try 
 {
  var cls = $el.attr('class'), cur = cls.substring(cls.length-1);
  $el.removeClass(cls);
  if (mode === -1) $el.addClass(cls.substring(0, cls.length-1) + (1.5 + (1.5 - cur)));  // toggle
  if (mode ===  0) $el.addClass(cls.substring(0, cls.length-1) + '1');                  // deselect
  if (mode ===  1) $el.addClass(cls.substring(0, cls.length-1) + '2');                  // select
 }
 catch (err) {}
}

function isElementSelected($el)  
{
 try 
 {
  var cls = $el.attr('class'), cur = cls.substring(cls.length-1);
  return (cur == 2);
 }
 catch (err) {}
}


function docMouseEvent(ev, evType)
{
 if ((evType === 'down')) 
 { 
  $ddCtl = $(".sliderHnd").nearest(ev.clientX, ev.clientY, 40); //$ddCtl = $(ev.currentTarget); 
  if ($ddCtl.length == 0) return;
  var pos = rect($ddCtl); ddX = pos.left + $ddCtl.px('width') / 2; ddY = pos.top + $ddCtl.px('height') / 2; 
  return docMouseEvent(ev, 3);
 }
 if (evType === 'up') $ddCtl = $empty;
 if ($ddCtl.length === 'up') return;
 if ((evType === 'move') && (ev.buttons == '1'))  
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
  $sliderLbl.html('' + anticipate((x1 / $ddCtl.parent()[0].clientWidth), num($ddCtl.bros('.sliderMin').text()), num($ddCtl.bros('.sliderMax').text()), 2) + ' .. ' + anticipate((x2 / $ddCtl.parent()[0].clientWidth), num($ddCtl.bros('.sliderMin').text()), num($ddCtl.bros('.sliderMax').text()), 2) + ' ' + alpha($ddCtl.bros('.sliderMax').text()));
 }
}

function tgglDiv(div, max) 	
{
 var cls = div.attr('class'), cur = cls.substring(cls.length-1);
 div.removeClass(cls);
 if (cur == max) cur = 1; else cur++;
 var nxt = cls.substring(0, cls.length-1) + cur;
 div.removeClass(cls);
 div.addClass(nxt);
 if (div.attr('exclusive') === '')
 {
  var div2 = div;
  while (div2.attr('exclusive') === '') { div2 = div2.prev(); div2.removeClass(nxt); div2.addClass(cls); }
  div2 = div;
  while (div2.attr('exclusive') === '') { div2 = div2.next(); div2.removeClass(nxt); div2.addClass(cls); }
 }
 if (!!div.attr('tabPage')) { var page = div.attr('tabPage'); showTab(div.parent().next().next(), page); }
}

function showTab(tabCtrl, page) 
{
 var ctr = 0;
 for (var tabCtr = 1; tabCtr <= tabCtrl.children().length; tabCtr++) tabCtrl.children()[tabCtr-1].style.display = (++ctr == page) ? 'block' : 'none'; 
}

function activeTab(tabCtrl) 
{
 for (var tabCtr = 1; tabCtr <= tabCtrl.children().length; tabCtr++) if (tabCtrl.children()[tabCtr-1].style.display === 'block') return tabCtrl.children()[tabCtr-1]; 
}

function buildQuery(tabCtrl, mode, variation, delim, encaps) 
{
 var ret = '', tabHeader = tabCtrl.children().first(), tabBox = tabHeader.next();
 for (var tabCtr = 1; tabCtr <= tabBox.children().length; tabCtr++) 
 {
  if ( (mode > 2) || (!tabHeader.children(':eq(' + (tabCtr-1) + ')').attr('class').endsWith('1')) )
  {
   var tabPage = tabBox.children(':eq(' + (tabHeader.children(':eq(' + (tabCtr-1) + ')').attr('tabPage')-1) + ')');
   for (var itmCtr = 1; itmCtr <= tabPage.children().length; itmCtr++) 
    if ( (mode % 2) || (!tabPage.children(':eq(' + (itmCtr-1) + ')').attr('class').endsWith('1')))  
    {
     var word = tabPage.children(':eq(' + (itmCtr-1) + ')').text().trim();
     if (word.indexOf('|') > -1) if (variation > 1) word = word.substring(1000, word.indexOf('|') + 1).trim(); else word = word.substring(word.indexOf('|') - 1, 0).trim();
     ret += delim + encaps + word + encaps;
    }
  }
 }
 return ret.length ? '(' + ret.substring(delim.length + encaps.length) + ')' : ret;
}

function htmlSlider($parent, hdlWidth, hdlHeight, barHeight, txtWidth, txtHeight, minVal, caption, maxVal, hdlColor, barColor)
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
 $ddCtl = $($($parent.children()[0]).children('.sliderHnd')[0]); docMouseEvent({'buttons': '1', 'clientX': '-1000'}, 'move');
 $ddCtl = $($($parent.children()[0]).children('.sliderHnd')[1]); docMouseEvent({'buttons': '1', 'clientX':  '1000'}, 'move');
 $ddCtl = $empty;
 return ret;
}

function htmlSlcCloud(parent, tabBagData)
{
 var ret = '', ctr = 0, $parent = $(firstItem(parent));
 ret += ' <div class="slcCloudTabs" id="' + $parent.attr('id') + 'Tabs">\r\n';
 for (var i = 1; i <= tabBagData.length; i++) ret += '  <div class="tgglDiv1" tabPage="' + (++ctr) + '" exclusive>' + tabBagData[i-1].name + '</div>\r\n';
 ret += '  <input style="width:80;" type="text" value="" />\r\n';
 ret += ' </div>\r\n';

 ret += ' <div class="slcResultBox" id="' + $parent.attr('id')  + 'Res"  >x</div>\r\n';
 ret += ' <div class="slcCloudBox"  id="' + $parent.attr('id')  + 'Box"  >\r\n';
 for (var i = 1; i <= tabBagData.length; i++)
 {
  ret += '  <div class="slcCloudPag" id="' + $parent.attr('id')  + 'Pag"' + i + ' style="display: none;" >\r\n';
  for (var j = 1; j <= tabBagData[i-1].vals.length; j++) ret += '   <div class="tgglDiv1">' + tabBagData[i-1].vals[j-1] + '</div>\r\n';
  ret += '  </div>\r\n';
 }
 ret += ' </div>\r\n';
 $parent.html(ret);
 $parent.children(0).children(0).click();
 return ret;
}


















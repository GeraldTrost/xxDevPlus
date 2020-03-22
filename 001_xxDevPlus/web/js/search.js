
'use strict';

// Entscheidung Bernhard und Gerald:
// Wir verändern aus javascript NICHT die location.href
// Weil: wir haben uns gegen page-reload entschieden, nur die Daten sollen auf buttoClick neu angefordert werden.
// Ausnahme: es gelingt uns mit history.js an der url zu tricksen ...


$(document).on('keyup'  , '#FlatSearchBox', function(ev) { onSearchBoxChanged(ev); } );
$(document).on('pasted' , '#FlatSearchBox', function(ev) { onSearchBoxChanged(ev); } );

var menuheaderExpanded = false;

function buildTagsMenu()
{
 $('#tagCloud').html('');
 try { everestWsCall("TagItems", {}).forEach
 ( 
  function(item)  { $('#tagCloud').append ( '&nbsp;&nbsp<a class="tagMenu1" title="button" onclick="tagMenuItemClick($(this).text().trim())">&nbsp;' + item.Name + '&nbsp;</a>&nbsp&nbsp\n' ); } ); 
 } 
 catch (err) { alert (err.message); }
}

function tagMenuItemClick(itemText)
{
 itemText = itemText.trim();
 buildTagsMenu(); //clear menu and repaint (so the prev. button is untoggled)
 selectElement($('a.tagMenu2'), 0);
 selectElement($('a.tagMenu1:Contains(' + itemText + ')'), 1);
 domain = $('#tagCloud a.tagMenu2').text().trim().toUpperCase();
 $('#FlatSearchBox').val(itemText + ' ');
 document.location.hash = itemText;
 buildSubTagsMenu(itemText);
}

function buildSubTagsMenu(menuText)
{
 var first;
 $('#subTagCloud').html('');
 try 
 { everestWsCall("SubTagItems", {"Domain": menuText, "MenuText": menuText}).forEach
  ( 
   function(item)
   {
    $('#subTagCloud').append ( '&nbsp;&nbsp<a class="tagMenu1" title="button" onclick="subTagMenuItemClick($(this))">&nbsp;' + item.Name + '&nbsp;</a>&nbsp&nbsp\n' ); 
    if (!first) first = $('#subTagCloud').children('.tagMenu1'); 
   }
  );
 } catch (err) { alert (err.message); }
 subTagMenuItemClick(first);
}

function subTagMenuItemClick(item)
{
 var hstr = "";
 var itemText = cleanString(item.text());
 selectElement($(item), -1); 
 if ($(item).attr('class').endsWith('2'))
 {
  hstr = cleanString((' ' + cleanString($('#FlatSearchBox').val()) + ' ').replace(' ' + itemText + ' ', ' ')) + ' ' + itemText + ' ';
  $('#FlatSearchBox').val(hstr); 
  hstr = hstr.substring(0,hstr.length-1);
  document.location.hash = hstr.replaceAll(" ","+");
 }
 else
 {
  hstr = cleanString((' ' + cleanString($('#FlatSearchBox').val()) + ' ').replace(' ' + itemText + ' ', ' '));
  hstr = hstr + ' ';
  $('#FlatSearchBox').val(hstr);
  hstr = hstr.substring(0,hstr.length-1);
  document.location.hash = hstr.replaceAll(" ","+");
 }
 htmlFromQueryResults(domain, cleanString($('#FlatSearchBox').val().trim()));
}

function menuHeaderExpand()
{
    
 // todo:
 // expand:
 //  1.) kontinente, zeiträume, mediatypes und sliders VISIBLE
 //  2.) lupe und button nach unten
 //  
// collapse:
 //  1.) kontinente, zeiträume, mediatypes und sliders HIDDEN
 //  2.) lupe und button nach oben
 // 

 $('#sliderDiv').each(function(inx, itm) {$(itm).css('top', '0px');});
 if (menuheaderExpanded) 
 { 
  // collape
  $('#menuheader').css("height","30px"); 
  $('.buttonArrow').css("transform","rotate(+45deg)"); 
  $('.headerGeoFilters').each(function(inx, itm) {$(itm).css('display', 'none');});
  $('.mediaTypeFilters').each(function(inx, itm) {$(itm).css('display', 'none');});
  $('#sliderDiv').each(function(inx, itm) {$(itm).css('display', 'none');});
 }
 else
 {
  // expand
  $('#menuheader').css("height","80px"); 
  $('.buttonArrow').css("transform","rotate(-135deg)"); 
  $('.headerGeoFilters').each(function(inx, itm) {$(itm).css('display', 'block');});
  $('.mediaTypeFilters').each(function(inx, itm) {$(itm).css('display', 'block');});
  $('#sliderDiv').each(function(inx, itm) {$(itm).css('display', 'block');});
 }
 
 menuheaderExpanded = !menuheaderExpanded;
}

$(window).resize
(
 function() 
 {
   // Changing the CSS at runtime
   var qRpane = document.getElementById('qRpane');
   var width = qRpane.clientWidth;
   if (width < 700)
   {
    $("#tagCloud").css("padding-left", 30);  $("#tagCloud").css("padding-right", 30);
    $("#subTagCloud").css("padding-left", 30);  $("#subTagCloud").css("padding-right", 30);
    $("#tagCloud").css("line-height", "25px"); 
    $("#subTagCloud").css("line-height", "25px"); 
    $("#qRpane").css("padding-left", 15);  $("#qRpane").css("padding-right", 15);
   }
   if ((width > 700) && (width < 1000))
   {
    $("#tagCloud").css("padding-left", 70);  $("#tagCloud").css("padding-right", 70);
    $("#subTagCloud").css("padding-left", 70);  $("#subTagCloud").css("padding-right", 70);
    $("#tagCloud").css("line-height", "35px"); 
    $("#subTagCloud").css("line-height", "35px"); 
    $("#qRpane").css("padding-left", 45);  $("#qRpane").css("padding-right", 45);
   }
   if (width > 1000)
   {
    $("#tagCloud").css("padding-left", 180);  $("#tagCloud").css("padding-right", 180);
    $("#subTagCloud").css("padding-left", 140);  $("#subTagCloud").css("padding-right", 140);
    $("#tagCloud").css("line-height", "35px"); 
    $("#subTagCloud").css("line-height", "35px"); 
    $("#qRpane").css("padding-left", 75);  $("#qRpane").css("padding-right", 75);
   }

   // Rearranging the result image list
   $('#qRpImgPane01').empty();
   htmlFromQueryResults(domain, cleanString($('#FlatSearchBox').val().trim()));
 }
); /**/


/*

         Aufbau des YY-ten Elements aus der XX-ten Imige-Row (MainResultImageListXX-YY)
         
         -----------------------------------------------
         | #qRpImgPaneXX-YY                |
         |                                             |                     
         |    -------------------------------------    |                    
         |    |                                   |    |                    
         |    |#MainResultImageListHeaderDivXX-YY |    |
         |    |                                   |    |
         |    -------------------------------------    |
         |                                             |                     
         |    -------------------------------------    |
         |    |.qRpImgFrame              |    |
         |    |   -----------------------         |    |
         |    |   |                      |        |    |
         |    |   | #img39               |        |    |
         |    |   |                      |        |    |
         |    |   |                      |        |    |
         |    |   ------------------------        |    |
         |    |                                   |    |
         |    -------------------------------------    |
         |                                             |                     
         |    -------------------------------------    |
         |    |                                   |    |
         |    |#MainResultImageListTitleDivXX-YY  |    |
         |    |                                   |    |
         |    -------------------------------------    |
         |                                             |                    
         -----------------------------------------------


*/



function onSearchBoxChanged(ev)
{
 return;
 var searchBox = $('#FlatSearchBox');

 var progAction = searchBox.attr('progAction') || 0;
 if (progAction != 0) return;
 {
  try 
  {
   searchBox.attr('progAction', ++progAction)
   var qry = cleanString(searchBox.val()) + ' ';
   var searchWords = [];
   while (qry.contains(' '))
   {
    var pos = qry.indexOf(' ');
    searchWords.push(qry.substring(0, pos));
    qry = qry.substring(pos + 1);
   }
   
   $('#tagCloud a.tagMenu1').each
   (
    function(inx, el)
    {
     var itemToDelete = -1;
     for (var i in searchWords)
     {
      if (searchWords[i].trim().toUpperCase() == el.innerText.trim().toUpperCase())
      {
       itemToDelete = i;
       if (!isElementSelected($(el))) {$('#tagCloud a.tagMenu1').each(function(inx, e) {selectElement($(e), 0); selectElement($(el), 1);});}
      }
     }
     if (itemToDelete != -1) searchWords = searchWords.splice(itemToDelete - 1, 1);
    }
   );

   $('#subTagCloud a.tagMenu1').each(function(inx, el){selectElement($(el), 0);}); 

   $('#subTagCloud a.tagMenu1').each
   (
    function(inx, el) 
    {
     for (var i in searchWords) if (searchWords[i].trim().toUpperCase() == el.innerText.trim().toUpperCase()) selectElement($(el), 1);         
    }
   );
        
        
   var qry = ''; for(var sw in searchWords) qry += searchWords[sw] + ' '; qry = qry.substring(0, qry.length - 1);
   searchBox.val(qry);
  }
  catch (err) 
  {
   alert('Error ' + err);
  }
  progAction = searchBox.attr('progAction') || 0;
  searchBox.attr('progAction', --progAction);
 }
}



function onBodyScroll() { docBodyScroll(); }


$(document).ready
(
 function() 
 { 
  docReady();
  buildTagsMenu(); 
  htmlSlider($('#sliderCell1'), 6, 5, 12, 80, 30,   '0',    'Awards',            '50',     '#dddddd', '#aabbee');
  htmlSlider($('#sliderCell2'), 6, 5, 12, 80, 30,   '1',    'Budget/M',    '1000',  '#dddddd', '#aabbee');
  htmlSlider($('#sliderCell3'), 6, 5, 12, 80, 30,   '10',   'BoxOffice/M', '10000', '#dddddd', '#aabbee');

  domain = notNull($('#domain')[0].value).trim().toUpperCase();
  var query = notNull($('#query')[0].value).trim();
  if ((!!domain) || (!!query)) 
  {
   selectElement($('a.tagMenu1:Contains(' + domain + ')'), 1);
   query = $('a.tagMenu2').text().trim() + ' ' + query;
   $('#FlatSearchBox').val(query + ' ');
   htmlFromQueryResults(domain, query);
  }
 }
);




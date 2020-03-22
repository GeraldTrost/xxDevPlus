'use strict';

// Entscheidung Bernhard und Gerald:
// Wir verändern aus javascript NICHT die location.href
// Weil: wir haben uns gegen page-reload entschieden, nur die Daten sollen auf buttoClick neu angefordert werden.
// Ausnahme: es gelingt uns mit history.js an der url zu tricksen ...


$(document).on('keyup'  , '#FlatSearchBox', function(ev) { onSearchBoxChanged(ev); } );
$(document).on('pasted' , '#FlatSearchBox', function(ev) { onSearchBoxChanged(ev); } );


function onBodyScroll() { docBodyScroll(); }

$(document).ready
(
 function() 
 {
  docReady();
  htmlFromCfgAdminData(notNull($('#domain')[0].value));
 }
);







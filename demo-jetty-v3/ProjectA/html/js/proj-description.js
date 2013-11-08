(function() {



function update_project_init() {
    var but = document.querySelector("#save");
    
    
    var projname_field = document.querySelector("#proj-name");
    var access_field = document.querySelector("#access");
    var descrip_field = document.querySelector("#descrip");
    var Id_field = document.querySelector("#theid");
    
    if ( but != null ) {
        but.addEventListener('click', function(evt) {
            var update_thelist = {
            	id: Id_field.value;
            	projname : projname_field.value;
            	access: access_field.value;
            	descrip : descrip_field.value;
                };
            var json = JSON.stringify( update_thelist );
            local_ajax_mod.ajax_request( {
                method : "POST",
                link: window.location.pathname + '/update_proj',
                doc : json,
                mime : 'application/json',
                ok_fn :  function( req ) {
                    try {
                        var obj = JSON.parse( req.responseText );
                        console.log( obj );
                        // follow redirect
                        
                    }
                    catch( e ) {
                        console.log( e );
                    }
                },
                err_fn :  function( req ) {
                    alert( 'failed: ' + req );
                    console.log( req );
                }
            } );
        }, false);
    }
}

window.addEventListener( 'load', function(evt) {

        update_project_init();
    }, false );

})();

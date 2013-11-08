(function() {



function add_project_init() {
    var but = document.querySelector("#sumbit");
    var selector1 = document.querySelector("#s1");
    var timetype =  selector1.options[selector1.selectedIndex].text;
    
    var selector2 = document.querySelector("#s2");
    var gdptype = selector2.options[selector2.selectedIndex].text;
    
    var selector3 = document.querySelector("#s3");
    var infl_type = selector3.options[selector3.selectedIndex].text;
    
    var selector4 = document.querySelector("#s4");
    var unem_type = selector4.options[selector4.selectedIndex].text;
    
    var selector5 = document.querySelector("#s5");
    var exch_type = selector5.options[selector5.selectedIndex].text;
    
    var projname_field = document.querySelector("#proj-name");
    var access_field = document.querySelector("#access");
    var descrip_field = document.querySelector("#descrip");
    
    if ( but != null ) {
        but.addEventListener('click', function(evt) {
            var add_types = {
            	projname : projname_field.value;
            	access: access_field.value;
            	descrip : descrip_field.value;
               time : timetype,
                gdp : gdptype,
                inflation: infl_type,
                unemply: unem_type;
                exchange: exch_type };
            var json = JSON.stringify( add_types );
            local_ajax_mod.ajax_request( {
                method : "POST",
                link: window.location.pathname + '/add_proj',
                doc : json,
                mime : 'application/json',
                ok_fn :  function( req ) {
                    try {
                        var obj = JSON.parse( req.responseText );
                        console.log( obj );
                        // follow redirect
                        if ( "redirect" in obj ) {
                            window.location = obj.redirect;
                            return;
                        }
                        timetype = 'error';
                        gdptype = '';
                        infl_type =  '';
                        alert("add user error");
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

/**window.addEventListener( 'load', function(evt) {

        add_project_init();
    }, false );*/

})();

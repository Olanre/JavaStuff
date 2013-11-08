(function() {
   function check_user_init() {
    console.log( "Testing is javascript functions" );
    var but = document.querySelector("#login");
    var username_field = document.querySelector("usertext");
    var password_field = document.querySelector("passstext");
    if ( but != null ) {
        but.addEventListener('click', function(evt) {
            var check_user = {
                user: username_field.value,
                password : password_field.value };
                
            var json = JSON.stringify( check_user );
            local_ajax_mod.ajax_request( {
                method : "POST",
                link: window.location.pathname + '/check_user',
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
                        username_field.value = 'error';
                        password_field.value = '';
                        alert("error in username or password entered");
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
    window.addEventListener( "load", function(evt) {
        check_user_init();
    }, false);


})();
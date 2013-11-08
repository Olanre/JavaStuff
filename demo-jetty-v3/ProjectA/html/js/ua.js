(function() {

function delete_user( evt ) {
    var cancelButton = null;
    var delButton = this;
    if ( delButton.textContent == "Delete" ) {
        var r = confirm("Do you want to delete this user");
        if(r == true){
            var json = JSON.stringify([ this.getAttribute("user") ]);
            var tr_to_delete = delButton.parentNode.parentNode;
            local_ajax_mod.ajax_request( {
             method : "POST",
             link: window.location.pathname + '/delete_user',
             doc : json,
             mime : 'application/json',
            ok_fn :  function( req ) {
                try {
                    var obj = JSON.parse( req.responseText );
                    console.log( obj );
                    if ( obj == 1 ) { // delete the row
                        tr_to_delete.parentNode.removeChild( tr_to_delete );
                    }
                   else {
                       alert("delete failed");
                    }
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
    }
}

function delete_button_init() {
    var buts = document.querySelectorAll("#delete");
    var i;
    for( i = 0 ; i < buts.length; i++ ) {
        buts[i].addEventListener('click', delete_user, false);
    }
}

function update_roles( evt ) {
    var selectedOption = this;
    var newval =  selectedOption.options[selectedOption.selectedIndex].text;
    var json = JSON.stringify( newval );
    local_ajax_mod.ajax_request( {
        method : "POST",
        link: window.location.pathname + '/update_roles',
        doc : json,
        mime : 'application/json',
        ok_fn :  function( req ) {
            try {
                var obj = JSON.parse( req.responseText );
                console.log( obj );
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
}

function update_button_init() {
    var selector = document.querySelectorAll("#selector2");
    var i;
    for( i = 0 ; i < buts.length; i++ ) {
        selector[i].addEventListener('change', update_roles, false);
    }
}

function add_user_init() {
    var but = document.querySelector("#sumbit");
    var username_field = document.querySelector("#user");
    var password_field = document.querySelector("#password");
    if ( but != null ) {
        but.addEventListener('click', function(evt) {
            var add_user = {
                user: username_field.value,
                password : password_field.value };
            var json = JSON.stringify( add_user );
            local_ajax_mod.ajax_request( {
                method : "POST",
                link: window.location.pathname + '/add_user',
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

window.addEventListener( 'load', function(evt) {
        delete_button_init();
        update_button_init();
        add_user_init();
    }, false );

})();

(function() {

function delete_project( evt ) {
    var cancelButton = null;
    var delButton = this;
    if ( delButton.textContent == "Delete" ) {
        var r = confirm("Do you want to delete this project");
        if(r == true){
            var user = this.getAttribute("user") ;
            var project = this.getAttribute("project");
            var del_project = {
                username: user.value,
                proj-name : project.value };
            var json = JSON.stringify( del_project );
            var li_to_delete = delButton.parentNode;
            local_ajax_mod.ajax_request( {
             method : "POST",
             link: window.location.pathname + '/delete_project',
             doc : json,
             mime : 'application/json',
            ok_fn :  function( req ) {
                try {
                    var obj = JSON.parse( req.responseText );
                    console.log( obj );
                    if ( obj == 1 ) { // delete the row
                        li_to_delete.parentNode.removeChild( li_to_delete );
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
        buts[i].addEventListener('click', delete_project, false);
    }
}

window.addEventListener( 'load', function(evt) {
        delete_button_init();

    }, false );

})();
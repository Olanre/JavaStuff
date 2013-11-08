package servlets;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.util.Enumeration;
import java.net.URLDecoder;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

import db.UserAuthDB;
import java.sql.SQLException;

import com.google.gson.Gson;

/**
 *
 * Provide user account adminstration
 *
 * @author Rod Byrne
 */
public class UserCheck extends HttpServlet {
    private util.HTMLTemplates html;
    private UserAuthDB userAuth;
    private Gson gson = new Gson();

    @Override
    protected void doGet(
        HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
    {
        log( request.getRequestURI() );
        util.HTTPUtils.nocache( response );
        String context = request.getContextPath();
        HttpSession session = request.getSession( boolean true );
        
        String user = request.getRemoteUser();
        if ( userAuth == null ) {
            response.sendRedirect( context + Constants.DB_ERR_PAGE );
            return;
        }
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        html.printHtmlStart(out);
        out.println("<body>");
        userSection( out );
        //roleSection( out );  TODO
        out.println("</body>");
        html.printHtmlEnd(out);
    }

   

    private String readAll( BufferedReader rd ) throws IOException {
        int amt;
        StringBuilder sb = new StringBuilder();
        char[] buf = new char[10240];
        while ( (amt=rd.read(buf, 0, buf.length)) != -1 ) {
            sb.append( buf, 0, amt );
        }
        return sb.toString();
    }

    private void check_user_role(
        String authUser, String[] roles, HttpServletResponse response ) 
    throws ServletException, IOException
    {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        try {
            if ( roles.length >= 1 ) {
                String user = roles[0];
                log( "deleting: " + user );
                userAuth.deleteUserRoles( user );
                for( int i = 1 ; i < roles.length; i++ ) {
                    log( "adding: " + roles[i] );
                    userAuth.addUserRole( user, roles[i] );
                }
            }
            out.print(gson.toJson("ok"));
        }
        catch( SQLException ex ) {
            log( ex.getMessage() );
        }
    }

    private static class UserPasswordEntry {
        String user;
        String password;
        public String toString() {
            return "u: " + user + " pw:" + password;
        }
    }

    @Override
    protected void doPost( 
        HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    { 
        HttpSession session = request.getSession( boolean create );
        log( request.getRequestURI() );
        String user = request.getRemoteUser();
        String context = request.getContextPath();

        if ( userAuth == null ) {
            response.sendRedirect( context + Constants.DB_ERR_PAGE );
            return;
        }
        String pathInfo = request.getPathInfo();
        if ( pathInfo != null && pathInfo.startsWith("/update_roles") ){
            BufferedReader rd = request.getReader();
            String json = readAll( rd );
            log( json );
            String user_roles = gson.fromJson(json, String[].class); 
            update_user_role( user, user_roles, response );
        }
        else if ( pathInfo != null && pathInfo.startsWith("/add_user") ){
            PrintWriter out = response.getWriter();
            response.setContentType("application/json");
            BufferedReader rd = request.getReader();
            String json = readAll( rd );
            log( json );
            try {
                // demonstrate using a class with json
                UserPasswordEntry entry = gson.fromJson(json, UserPasswordEntry.class); 
                userAuth.addUser( entry.user, entry.password );
                out.print("{\"redirect\":\"users\"}");
            }
            catch( SQLException ex ) {
                log( ex.getMessage() );
                out.print(gson.toJson("error")); 
            }
        }
        
        else {
            PrintWriter out = response.getWriter();
            response.setContentType("application/json");
            out.write( gson.toJson( "not valid url" ) );
        }
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init( config ); // super.init call is required
        html = util.HTMLTemplates.newHTMLTemplates( this );
        try {
            userAuth = new UserAuthDB( Constants.UA_DB_PATH );
        }
        catch( SQLException ex ) {
            userAuth = null;
            log( ex.getMessage() );
        }
    }
}
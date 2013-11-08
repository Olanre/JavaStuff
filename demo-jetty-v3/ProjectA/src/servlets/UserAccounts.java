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
import java.util.Date;
import java.util.TimeZone;
import java.util.GregorianCalendar;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

import db.Users;
import db.Project;
import db.Project_tuple;
import db.UserException;
import db.UsersProjectsManager;
import db.UserDB;
import com.google.gson.Gson;

/**
 *
 * Edit and add user using forms.
 *
 * @author Olanrewaju Okunlola
 */
public class UserAccounts extends HttpServlet {
    private util.HTMLTemplates html;
    private UserDB uma;
    private Gson gson = new Gson();

    private SimpleDateFormat dtFormat;

    @Override
    protected void doGet(
        HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
    {
        log( request.getRequestURI() );
        util.HTTPUtils.nocache( response );
        String context = request.getContextPath();
        
        //String user = request.getRemoteUser();
        /*
         * Check if the remoteUser has the correct role.
         */
        /**if (  ! request.isUserInRole("admin") ) {
            response.sendError( HttpServletResponse.SC_FORBIDDEN, "No premission");
            return;
        }
        if ( uma == null ) {
            response.sendRedirect( context + Constants.DB_ERR_PAGE );
            return;
        }*/
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        html.printHtmlStart(out);
        out.println("<body>");
        out.println("<div id = 'banner'>");
        out.println("<h1 style='text-align:center; letter-spacing:12px;'> CATA Database: User Management</h1>");
        out.println("</div>");
        out.printf("<div id = 'navi' style='opacity:0.9;position:absolute;left:20px;width:195px;' ");
        //might not go to these pages, check link address
        out.println(" height:450px;background-color:#ADD8E6; border: thin solid black; font-family:Verdana; font-size: 30px; text-align:center '> ");
        out.printf("<button id='b1' style= 'width: 130px' onclick='parent.location.href=&quot;%s&quot;'>Logout</button>", context);
        out.println("<p>");
        out.printf("<button id='b2' style= 'width: 130px' onclick='parent.location.href=&quot;%s/admin/user_manage&quot;'>Users</button>", context );
        out.println("</p>");   
        out.println("<p>");
        out.printf("<button id='b3' style= 'width: 130px' onclick='parent.location.href=&quot;%s/admin/adminprojects&quot;'>Projects</button>", context);
        out.println("</p>");   
        out.println("<p>");
        out.printf("<button id='b4' style= 'width: 130px' onclick='parent.location.href=&quot;%s/project/project-list&quot;'> My Projects</button>", context);
        out.println("</p>");
        out.println("<p>");
        out.printf("<button id='b5' style= 'width: 130px' onclick='parent.location.href=&quot;%s/project/reports&quot;'> My Data</button>", context);
        out.println("</p>");
        out.println("<img id='cir' src='image.jpg' width = '100' height = '90'>");
        out.println("</div>");
        out.println("<div id = 'content'> ");
        out.println("<div style ='font-family:verdana;padding:10px;border-radius:10px;border:10px solid #616D7E; width:1000px;");
        out.println("height:450px; '>");        
        out.println("<table id = 'table1' border='1' >"); 
        out.println("<tr>");
        out.println("<th> Users </th> <th> User_email </th> <th> Password </th> <th> Projects </th> <th>Permission Type</th>");
        out.println("</tr>");
        try {
            List<Users> list = uma.getUsersInfo();
            for( Users rec : list ) {
            	 out.printf("<tr id = '%d'>", rec.getRecordID() );
                out.printf(" <td> <input type='text' value = '%s' size='20'>  </td> ", rec.getName());
                out.printf("<td> <input type='text' value = '%s' size='20'>", rec.getEmail() );
                out.printf("<input id='%s' type='text' size='20'", rec.getName() );
                out.printf("<td> <input type='text' value = '%s' size='20' readonly> </td>", rec.getPassword() );
                out.println(" <td> <select id = 'selector1'>" );
                //String[] projectnames = null;
                //leave out until we have merged databases
                /**projectnames = uma.getUserProjects( rec.getName() );
                for( String s: projectnames){
                    out.printf("<option> %s </option> ", s);	                 	
                }*/
                
                out.println("</select> </td>");
                out.println("</td><select id = 'selector2'>");
               
                String therole = uma.getRoleName( rec.getRole() );
                if( therole == "User"){
                	  out.println("<option selected id = 'user'> User </option> <option id = 'admin'> Administrator </option>");
                } else
                if(therole == "Administrator"){                	
                	   out.println("<option id = 'user'> User </option> <option selected id = 'admin'> Administrator </option>");
                }
                out.println("</select> </td>");
                out.printf("<td> <button user = '%s' id='Delete' style= 'width: 130px'> Delete User </button> </td>", rec.getName() );
                out.println("</tr>");

            }
            out.println("</table> <br> ");
            out.printf("<form action='%s/admin/user-manage/add' method='post'>%n",context);
            out.println("<legend>Create a User:</legend>");
            out.println(" User Name: <input type='text' size='20' name='user'><br>");
            out.println(" User Email: <input type='text' size='20' name='email'><br>");
            out.println(" User Password: <input type='text' size='10' name='password'><br>");
            out.println(" Password Confirm: <input type='text' size='10' name='confirm'><br>");
            out.println("<input type='submit' id= 'submit' size='30' value='Submit'>");
            out.println("</fieldset></form>");
            
        }
        catch( UserException ex ) {
            log( ex.getMessage() );
            out.println("Data base error");
        }
        catch( SQLException ex ) {
            out.println("DATABASE Error"); // XXX
            log( ex.getMessage() );
        }
        
        out.println("</div>");
        out.println("</div>");
        

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

    @Override
    protected void doPost( 
        HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        log( request.getRequestURI() );
        util.HTTPUtils.nocache( response );
        PrintWriter out = response.getWriter();
        String user = request.getRemoteUser();
        String context = request.getContextPath();

        /*
         * Check if the remoteUser has the correct role.
         */
        if ( ! request.isUserInRole("admin") ) {
            response.sendError( HttpServletResponse.SC_FORBIDDEN, "No premission");
            return;
        }
        if ( uma == null ) {
            response.sendRedirect( context + Constants.DB_ERR_PAGE );
            return;
        }

        String pathInfo = request.getPathInfo();
        if ( pathInfo != null && pathInfo.startsWith("/add") ) {
            String username = request.getParameter( "user" );
            String useremail = request.getParameter( "email" );
            String password = request.getParameter( "password" );
            long role_id = 1;
            if ( username == null || useremail == null ||
                 password == null )
            {
                response.sendRedirect( context + Constants.INVALID_URL_PAGE );
                return;
            }
            try {
                
                Users newuser = new Users(0, username, useremail, password, role_id );
                long id = uma.addUser( newuser );
                log("adding: " + user + "=" + newuser );
            }
            
            catch( SQLException ex ) {
	            out.println("DATABASE Error"); // XXX
	            log( ex.getMessage() );
	        }
            catch( NumberFormatException ex ) {
                log( ex.getMessage() );
                response.sendRedirect( context + Constants.DATA_ERR_PAGE );
                return;
            }
            
        }
        else if ( pathInfo != null && pathInfo.startsWith("/update_roles") ) {
            BufferedReader rd = request.getReader();
            String json = readAll( rd );
            try {
                String[] newrole =
                    gson.fromJson(json, String[].class); 
                uma.UpdateUserRole( user, newrole[0] );
                log("updating: " + user + " role to =" + newrole );
                response.setContentType("application/json");
                out.print( gson.toJson( newrole[0]) ); // return new role
                return;
            }
            
            catch( SQLException ex ) {
	            out.println("DATABASE Error"); // XXX
	            log( ex.getMessage() );
	        }
        }
        else if ( pathInfo != null && pathInfo.startsWith("/delete_user") ){
            BufferedReader rd = request.getReader();
            String json = readAll( rd );
            try {
                String[] userarray = gson.fromJson(json, String[].class);
                uma.deleteUser( userarray[0] );
                log("deleting: " + user );
                response.setContentType("application/json");
                out.print( gson.toJson("ok") ); // ok
            }
            
            catch( SQLException ex ) {
                log( ex.getMessage() );
                out.print("0"); // XXX
            }
        }
       
        else {
            response.sendRedirect( context + Constants.INVALID_URL_PAGE );
            return;
        }
        /*
         * Redirect to redisplay the edited data.
         *
         * Note: this will not be necessary when AJAX is used.
         */
        String servletPath = request.getServletPath();
        response.sendRedirect( context + servletPath );
    }
    
    
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init( config ); // super.init call is required
        html = util.HTMLTemplates.newHTMLTemplates( this );
        dtFormat = new SimpleDateFormat("yy/MM/dd HH:mm");
        dtFormat.setTimeZone( TimeZone.getDefault() );
        Date twentyFirstCentury = new GregorianCalendar(2001,1,1).getTime();
        dtFormat.set2DigitYearStart( twentyFirstCentury );
        try {
            uma = new UserDB( Constants.UA_DB_PATH );
        }
        catch( UserException ex ) {
            uma = null;
            log( ex.getMessage() );
        }
        catch( SQLException ex ) {
            //out.println("DATABASE Error"); // XXX
            log( ex.getMessage() );
        }
    }
}

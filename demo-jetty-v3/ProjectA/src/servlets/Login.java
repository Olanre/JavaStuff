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

import db.ProjDB;
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
@SuppressWarnings({ "unused", "serial" })
public class Login extends HttpServlet {
    private util.HTMLTemplates html;
    private UserDB usr;
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

       
        HttpSession session = request.getSession(true);
        /*
         * Check if the remoteUser has the correct role.
         */
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        html.printHtmlStart(out);
        out.println("<body>");
        out.println("<div id = 'banner'>");
        out.println("<h1 style='text-align:center; letter-spacing:12px;'> CATA Database</h1>");
        out.println("</div>");
        out.printf("<div id = 'navi' style='opacity:0.9;position:absolute;left:20px;width:195px; ");
        //might not go to these pages, check link address
        out.println(" height:150px;background-color:#ADD8E6; border: thin solid black; font-family:Verdana; font-size: 30px; text-align:center '> ");
        out.println("<form action='http://localhost:8000/ProjectA/Loggin/check_user' method='post'>");
        out.println("<strong>Username: </strong><input type='text' name='j_username'><br/>");
        out.println("<strong>Password:</strong> <input type='password' name='j_password'><br/>");
        out.println("<input type='submit' value='Log In'>");
        out.println(" </form></div>");
        out.println("<div id = 'content'>");
        out.println("<div style='font-family:verdana;padding:50px;border-radius:30px;border:30px solid #616D7E'>");
        out.println("<p id = 'p2'> <b><em>This program will allow users to collect,store and report on various economic data </em></b></p>");
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
        String pathInfo = request.getPathInfo();
       
        if ( pathInfo != null && pathInfo.startsWith("/check_user") ) {
           
            String password = request.getParameter( "j_password" );
            
            try {
                   
			    if (  usr.getUserRole(user) == "admin" ) {
				   // response.Cookies("username" = user);
			    	log( user  );
			       response.sendRedirect( context + "/admin/adminprojects/*" );
			       HttpSession session = request.getSession(true);
			    
			     
			        return;
			    } else
				    if (  usr.getUserRole(user) == "admin" ) {
				   //	 response.Cookies("username")=user;
				    	 log( user  );
			       response.sendRedirect( context + "/project/projectlist/*" );
			       HttpSession session = request.getSession(true);
			      
  
			   
			       return;
			   } else {
				
				   response.sendError( HttpServletResponse.SC_FORBIDDEN, "No premission");
			    return;
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log( e.getMessage() );
		}
        /*
         * Redirect to redisplay the edited data.
         *
         * Note: this will not be necessary when AJAX is used.
         */
        }
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
            usr = new UserDB( Constants.UA_DB_PATH );
            
        }
        catch( UserException ex ) {
            usr = null;
            log( ex.getMessage() );
        }
        catch( SQLException ex ) {
            log( ex.getMessage() );
            
        }
        
    }
}

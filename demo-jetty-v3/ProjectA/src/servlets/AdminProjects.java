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
public class AdminProjects extends HttpServlet {
    private util.HTMLTemplates html;
    private Gson gson = new Gson();
    private UserDB uma;
    private ProjDB proj;

    private SimpleDateFormat dtFormat;

    @Override
    protected void doGet(
        HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
    {
        log( request.getRequestURI() );
        util.HTTPUtils.nocache( response );
        String context = request.getContextPath();

        String user = request.getRemoteUser();
        HttpSession session = request.getSession();
        /*
         * Check if the remoteUser has the correct role.
         
        try {
			if ( uma.getUserRole(user) != "Administrator" ) {
			    response.sendError( HttpServletResponse.SC_FORBIDDEN, "No premission");
			    return;
			}
		} catch (SQLException e) {
			
	            log( e.getMessage() );
	            
	       
		}
		*/
        
        if ( proj == null ) {
            response.sendRedirect( context + Constants.DB_ERR_PAGE );
            return;
        }
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        html.printHtmlStart(out);
        out.println("<body>");
        out.println("<div id = 'banner'>");
        out.println("<h1 style='text-align:center; letter-spacing:12px;'> CATA Database: Project Management</h1>");
        out.println("</div>");
        out.printf("<div id = 'navi' style='opacity:0.9;position:absolute;left:20px;width:175px;' ");
        //might not go to these pages, check link address
        out.println(" height:450px;background-color:#ADD8E6; border: thin solid black; font-family:Verdana; font-size: 30px; text-align:center '> ");
        out.printf("<button id='b1' style= 'width: 130px' onclick='parent.location.href=&quot;%s&quot;'>Logout</button>", context);
        out.println("<p>");
        out.printf("<button id='b2' style= 'width: 130px' onclick='parent.location.href=&quot;%s/admin/user-manage&quot;'>Users</button>", context );
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
        out.println("height:350px; '>");        
        out.print("<h2>Project Management</h2>"); 
        out.println("<ul>");
        
        try {
            List<Project> list = proj.getProjects( );
            for( Project rec : list ) {
                out.printf("<li> <a href='%s/project/proj-descrip/%s' target='_blank'>", context, rec.getRecordID());
                out.printf("%s </a>", rec.getProjectName() );
                out.printf("<input id='%s' type='text' size='20'", rec.getName() );
                out.printf("value = ' %s ", rec.getName() );
                out.printf("%s' readonly> %n", rec.getEmail() );
                out.printf("<button user = '%s' project = '%s' id='%d' style= 'width: 130px'> Delete </button> </li>%n",
                    rec.getName(), rec.getProjectName(), rec.getRecordID() );

            }
        }
        catch( UserException ex ) {
            log( ex.getMessage() );
            out.println("<tr><td>Data base error</td></tr>");
        }
        catch( SQLException ex ) {
            log( ex.getMessage() );
            
        }
        
        out.println("</ul>");
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
    
    private static class UserProjectEntry {
        String user;
        String proj_name;
        public String toString() {
            return "u: " + user + " proj:" + proj_name;
        }
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
        /**if ( uma.getUserRole(user) != "Administrator" ) {
            response.sendError( HttpServletResponse.SC_FORBIDDEN, "No premission");
            return;
        }*/
        if ( ! request.isUserInRole("admin") ) {
            response.sendError( HttpServletResponse.SC_FORBIDDEN, "No premission");
            return;
        }
        if ( uma == null ) {
            response.sendRedirect( context + Constants.DB_ERR_PAGE );
            return;
        }
        String pathInfo = request.getPathInfo();
        if ( pathInfo != null && pathInfo.startsWith("/delete_project") ){
            BufferedReader rd = request.getReader();
            String json = readAll( rd );
            try {
                // demonstrate using a class with json
                UserProjectEntry entry = gson.fromJson(json, UserProjectEntry.class); 
                proj.deleteProject( entry.user, entry.proj_name );
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
            proj = new ProjDB( Constants.DB_PATH );
        }
        catch( UserException ex ) {
            uma = null;
            log( ex.getMessage() );
        }
        catch( SQLException ex ) {
            log( ex.getMessage() );
            
        }
    }
}

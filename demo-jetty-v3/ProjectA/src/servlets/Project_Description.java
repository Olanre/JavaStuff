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
import db.ProjDB;
import com.google.gson.Gson;

/**
 *
 * Edit and add user using forms.
 *
 * @author Olanrewaju Okunlola
 */
public class Project_Description extends HttpServlet {
    private util.HTMLTemplates html;
    private ProjDB uma;
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
        HttpSession session = request.getSession();
        String user = request.getRemoteUser();
        /*
         * Check if the remoteUser has the correct role.
         */
        if ( ! request.isUserInRole("user") || ! request.isUserInRole("admin") ) {
            response.sendError( HttpServletResponse.SC_FORBIDDEN, "No premission");
            return;
        }
        if ( uma == null ) {
            response.sendRedirect( context + Constants.DB_ERR_PAGE );
            return;
        }
        String pathInfo = request.getPathInfo();
        if ( pathInfo == null || pathInfo.length() < 1 ||
            pathInfo.charAt(0) != '/')
        {
            response.sendRedirect( context + Constants.INVALID_URL_PAGE );
            return;
        }
        String[] elements = pathInfo.substring(1).split("/");
        if ( elements.length == 0 ) {
            response.sendRedirect( context + Constants.INVALID_URL_PAGE );
            return;
        }
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        html.printHtmlStart(out);
        out.println("<body>");
        out.println("<div id = 'banner'>");
        out.println("<h1 style='text-align:center; letter-spacing:12px;'> CATA Database</h1>");
        out.println("</div>");
        out.printf("<div id = 'navi' style='opacity:0.9;position:absolute;left:20px;width:195px;' ");
        //might not go to these pages, check link address
        out.println(" height:450px;background-color:#ADD8E6; border: thin solid black; font-family:Verdana; font-size: 30px; text-align:center '> ");
        //if(( uma.getUserRole(user) == "Administrator" ){      
        if ( request.isUserInRole("admin")){
        
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
        		
        } else {
        	    
        	   out.printf("<button id='b1' style= 'width: 130px' onclick='parent.location.href=&quot;%s&quot;'>Logout</button>", context);
            out.println("<p>");
            out.printf("<button id='b4' style= 'width: 130px' onclick='parent.location.href=&quot;%s/project/project-list&quot;'> My Projects</button>", context);
        		out.println("</p>");         
            
       }
        out.println("<img id='cir' src='image.jpg' width = '180' height = '120'>");
        out.println("</div>");
        out.println("<div id = 'content'> ");
        out.println("<div style ='font-family:verdana;padding:10px;border-radius:10px;border:10px solid #616D7E; width:1000px;");
        out.println("height:550px; '>");        
        
       
        try {
        	   
            List<Project_tuple> list = uma.getProjectDescrip( Long.parseLong(elements[0]) );
            for( Project_tuple rec : list ) {
            	//out.printf"<form action='%s/project/project-list/proj-descrip/%s/save' method='post'>%n",context, elements[0]);
        			//out.println("<fieldset>");
		  			//out.println("<legend></legend>");
       			 out.printf("Project Name: <input type='text' size='30' value = '%s' id = 'proj-name' ><br>", rec.getProjectName());
       			 out.printf("Project Id: <input type = 'text' size = '5' id = 'theid' value = '' readonly><br>", rec.getRecordID() );
       			 out.printf("Description: <br> <textarea id='descrip' value = '%s' rows='5' cols='60' name = 'proj-descrip' >", rec.getDescrip() );
       			 out.println("</textarea><br><br>");
       			 out.println("Type of Data Recorded: "); 
       			 out.println("<ul>");
       			 String[] type = rec.getTypes().split(",");
       			 if (rec.getTime() != null)
                       out.printf(" <li> <a href='javascript:window.alert(&quot; You recorded as %s&quot;);'> Time Series </a></li> ", type[0]  );
                if(rec.getGDP() != 0)
                		  out.printf("<li> <a href='javascript:window.alert(&quot; You recorded as %d&quot;);'> GDP </a> </li> ", type[1] );
                if( rec.getInflation() != 0 )                
                       out.printf("<li> <a href='javascript:window.alert(&quot; You recorded as %s&quot;);' > Inflation Rate </a> </li> ", type[2]);
                if(rec.getUnemploy() != 0)
                			out.printf(" <li> <a href='javascript:window.alert(&quot; You recorded as %s&quot;);' > Unemployment </a> </li>", type[3]);
                if( rec.getExchange() != 0)
                       out.printf("<li> <a href='javascript:window.alert(&quot; You recorded as %s&quot;);' > Exchange Rate </a> ", type[4]);
                 
                out.printf("<li> <button  onclick='parent.location.href=&quot;%s/project/project-tables/%d&quot;'  style= 'width: 130px'> Add </button> </li> ", context, rec.getRecordID()  );
                out.println("</ul>");
                out.printf(" Users Who May Access Project: <input type='text' id = 'access' value = '%s' size='60' ><br><br>", rec.getUserAcess() );
                out.println("<button id='save'   style= 'width: 130px'> Submit Changes </button> </li> " );
                 //out.println("</fieldset></form>");

            }
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
    
    private static class ProjectEntryUpdate {
       
        long id; 
        String projname;
        String descrip; 
         String access;
        public String toString() {
            return " " + id + " " + projname + " " + descrip + " " + access;
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
        
        if ( ! request.isUserInRole("user") || ! request.isUserInRole("admin") ) {
            response.sendError( HttpServletResponse.SC_FORBIDDEN, "No premission");
            return;
        }
        if ( uma == null ) {
            response.sendRedirect( context + Constants.DB_ERR_PAGE );
            return;
        }
        String pathInfo = request.getPathInfo();
        if ( pathInfo != null && pathInfo.startsWith("/update_proj") ){
            BufferedReader rd = request.getReader();
            String json = readAll( rd );
            try {
                // demonstrate using a class with json
                ProjectEntryUpdate entry = gson.fromJson(json, ProjectEntryUpdate.class); 
                uma.updateDescripTuples( entry.id, entry.projname, entry.descrip, entry.access );
                log("deleting: " + user );
                response.setContentType("application/json");
                out.print( gson.toJson("ok") ); // ok
            }
            
            catch( UserException ex ) {
                log( ex.getMessage() );
                out.println("Data base error");
            }
            catch( SQLException ex ) {
                out.println("DATABASE Error"); // XXX
                log( ex.getMessage() );
               
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
            uma = new ProjDB( Constants.DB_PATH );
        }
        
        catch( SQLException ex ) {
        	System.out.println("DATABASE Error"); // XXX
            log( ex.getMessage() );
           
        }
    }
}
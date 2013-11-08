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
@SuppressWarnings({ "unused", "serial" })
public class Project_Tables extends HttpServlet {
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
        out.println("<h1 style='text-align:center; letter-spacing:12px;'> CATA Database: Project Tables</h1>");
        out.println("</div>");
        out.printf("<div id = 'navi' style='opacity:0.9;position:absolute;left:20px;width:195px;' ");
        //might not go to these pages, check link address
        out.println(" height:450px;background-color:#ADD8E6; border: thin solid black; font-family:Verdana; font-size: 30px; text-align:center '> ");
        if( request.isUserInRole("admin") ) {      
        
             out.printf("<button id='b1' style= 'width: 130px' onclick='parent.location.href=&quot;%s&quot;'>Logout</button>", context);
             out.println("<p>");
            out.printf("<button id='b2' style= 'width: 130px' onclick='parent.location.href=&quot;%s/admin/user-manage&quot;'>Users</button>", context );
            out.println("</p>");   
        		out.println("<p>");
        		out.printf("<button id='b3' style= 'width: 130px' onclick='parent.location.href=&quot;%s/admin/adminpr&quot;jects''>Projects</button>", context);
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
        out.println("height:750px; '>");    
        out.print("<table id = 'editor' border = '1'>"); 
        try{   
             List<Project_tuple> list = uma.getProjectDescrip( Long.parseLong(elements[0]) );
             out.println("<tr>");
             
              out.println("<th> Time Series </th>");
             out.println("<th> GDP </th>");
              out.println("<th> Inflation </th>");
             out.println("<th> Unemployment </th>");
              out.println("<th> Exchange Rate </th> ");
             out.println("</tr>");
             long pageid = 0; 
             for( Project_tuple rec : list ) {
             	    out.println("<tr>");
             	    
             	    if (rec.getTime() != null)
                       out.printf(" <td>  <a id = 'time' href='javascript:window.alert(&quot; You Cannot Edit this data &quot;);' > %s </a>  </td>", rec.getTime()  );
                   if(rec.getGDP() != 0)
                		  out.printf("<td>  <input type='text' id = 'gdp' size='30' value = '%d' readonly> </td> ", rec.getGDP() );
                    if( rec.getInflation() != 0 )                
                       out.printf("<td>  <input type='text' id = 'inflation' size='10' value = '%g' readonly> </td> ", rec.getInflation());
                    if(rec.getUnemploy() != 0)
                			out.printf(" <td>  <input type='text' id = 'unemploy' size='10' value = '%g' readonly> </td>", rec.getUnemploy());
                    if( rec.getExchange() != 0)
                       out.printf("<td>  <input type='text' id = 'exchange' size='10' value = '%g' readonly> </td> ", rec.getExchange());
                   out.printf("<input type='hidden' id='Id' value='%d'>", rec.getRecordID() );
                   pageid = rec.getRecordID();
                   out.println("<td><button id='delete' style= 'width: 100px'> Delete </button> ");
                   out.println(" <button id='update' style= 'width: 100px'> Update </button></td>");
                   
                   
                   
                   out.println("</tr>");
             }
             out.println("<tr>");
             out.printf("<form action='%s/project/project-tables/%s/add' method='post'>%n",context, pageid);
             
              out.println("<td> <input type='text' name = 'Time' size='15'> </td>");
              out.println("<td> <input type='text' name = 'GDP' size='30'> </td>");
               out.println("<td> <input type='text' name = 'Inflation' size='10'> </td>");
             out.println("<th> <input type='text' name = 'Unemployment' size='10'> </th>");
             out.println("<th><input type='text' name = 'Exchange_rate' size='10'> </th> ");
             out.printf("<input type='hidden' name='TheId' value='%d'>",pageid );
             out.println("</form>");
             out.println("</tr>");
             out.println("</table>"); 
             out.printf("<button id='Report' onclick='parent.location.href=&quot;%s/project/report/%s&quot;'  style= 'width: 130px'> Report </button> </li> ", context, elements[0]);
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
    
    private static class ProjectValues{
        String time;
        long gdp;
        double inflation;
        double unemply;
        double exchange;
        long id;
        public String toString() {
            return  id + " " + time + " " + gdp + " " + inflation + " " + unemply + " " + exchange  ;
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
        

        if ( uma == null ) {
            response.sendRedirect( context + Constants.DB_ERR_PAGE );
            return;
        }
        if ( ! request.isUserInRole("user") || ! request.isUserInRole("admin") ) {
            response.sendError( HttpServletResponse.SC_FORBIDDEN, "No premission");
            return;
        }

        String pathInfo = request.getPathInfo();
        if ( pathInfo != null && pathInfo.startsWith("/add") ) {
            String Time = request.getParameter( "Time" );
            String TheId = request.getParameter("TheId");
            
             long gdp = 0;
            String Gdp = request.getParameter( "GDP" );
         	 
            double inflation = 0;
            String Inflation = request.getParameter( "Inflation" );
       
            double unemploy = 0;
            String Unemploy =  request.getParameter("Unemployment");
      
            double exchange = 0;
            String Exchange = request.getParameter("Exchange_rate");

            
            try {
            	 if(Gdp != null) gdp = Long.parseLong(Gdp);
            	 if(Inflation != null) inflation = Double.parseDouble(Inflation);
            	 if(Unemploy != null) unemploy = Double.parseDouble(Inflation);
            	 if(Exchange != null){ exchange = Double.parseDouble(Exchange);}
            	 long id = Long.parseLong(TheId);
            	 String projectname = uma.getProjectName(id);
            	 
                
                long theid = uma.add( id, user, projectname, Time, gdp, inflation, unemploy, exchange );
                
                log("adding: " + user + "+" + Time + "at id:" + theid );
                
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
            catch( UserException ex ) {
                log( ex.getMessage() );
                response.sendRedirect( context + Constants.DB_ERR_PAGE );
                return;
            }
        }
        else if ( pathInfo != null && pathInfo.startsWith("/update_tuple") ) {
            BufferedReader rd = request.getReader();
            String json = readAll( rd );
            try {
                ProjectValues newvals = gson.fromJson(json, ProjectValues.class); 
                uma.updateTuples( user, newvals.id, newvals.time, newvals.gdp, newvals.inflation, newvals.unemply, newvals.exchange );
                log("updating: " + user + "=" + newvals );
                response.setContentType("application/json");
                out.print( gson.toJson("ok") ); // ok
                return;
            }
            catch( SQLException ex ) {
                log( ex.getMessage() );
                response.sendRedirect( context + Constants.DB_ERR_PAGE );
                return;
            }
        }
        else if ( pathInfo != null && pathInfo.startsWith("/delete_row") ){
            BufferedReader rd = request.getReader();
            String json = readAll( rd );
            try {
                ProjectValues row = gson.fromJson(json, ProjectValues.class);
                uma.deleteProject_tuple( user, row.id, row.time, row.gdp, row.inflation, row.unemply, row.exchange );
                log("deleting: " + user + "=" + row );
                response.setContentType("application/json");
                out.print( gson.toJson("ok") ); // ok
            }
            catch( NumberFormatException ex ) {
                log( ex.getMessage() );
                response.sendRedirect( context + Constants.DATA_ERR_PAGE );
                return;
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

    
    
        
        
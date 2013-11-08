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

import db.ReportAnswer;
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
public class Report extends HttpServlet {
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
        /**if ( uma.getUserRole(user) != "Administrator" || uma.getUserRole(user) != "User"  ) {
            response.sendError( HttpServletResponse.SC_FORBIDDEN, "No premission");
            return;
        }*/
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
        if(  request.isUserInRole("admin" )){  
        
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
        out.println("height:880px; '>");    
        out.printf("<form action='%s/project/report/%s/query1' method='post'>%n",context, elements[0]);
        out.println("<fieldset>");
		  out.println("<legend> Descriptive Stats</legend>");
		  out.println("<br>  <input type='radio' name='stat' value = 'sum' checked> Sum <br>   ");
		  out.println("<br>  <input type='radio' name='stat' value = 'count' > Count <br>   ");
		  out.println("<br>  <input type='radio' name='stat' value = 'mean'> Mean <br>   ");
		  out.println("<br>  <input type='radio' name='stat' value = 'min'> Min <br>   ");
		  out.println("<br>  <input type='radio' name='stat' value = 'max'> Max <br>   ");
		  out.printf("<input type = 'hidden' name = 'proj_id' value = '%s' >",elements[0] );	
		  out.println("<select name = 'sort_type'> <option> DESC </option> <option selected> ASC </option>  </select>");
		  out.println("<select name = 'sort_col'> <option selected> Time </option> <option> GDP </option> ");		      
		  out.println("<option> Inflation </option> <option> Unemployment </option> <option> Exchange_rate </option>  </select>");
		  out.println("<br> <button id='Generate' style= 'width: 100px'> Generate </button>");
		  out.println("<fieldset>");
		  out.println("<form><br>");
		  out.println("<table id = 'table1' border = '1'>"); 
		  out.println("<tr>");
		  out.println("<th> Stats </th> <th>Time series</th> <th> GDP </th> <th> Inflation </th> <th> Unemployment </th> <th> Exchange Rate </th>");
		  out.println("</tr>");
		  try {
		  	   List<ReportAnswer> list = uma.getReports( Long.parseLong(elements[0]) );
		      for( ReportAnswer rec: list){
		  	       out.println("<tr>");
		  	       out.printf("<td> <input type='text' size='10' value = '%s' readonly> </td>", rec.getQuery());
		  	       out.printf("<td> <input type='text' size='10' value = '%s' readonly> </td>", rec.getTime());
		  	       out.printf("<td> <input type='text' size='10' value = '%d' readonly> </td>", rec.getGDP());
                out.printf("<td> <input type='text' size='10' value = '%s' readonly> </td>", rec.getInflation());
                out.printf("<td> <input type='text' size='10' value = '%s' readonly> </td>", rec.getUnemploy());
                out.printf("<td> <input type='text' size='10' value = '%s' readonly> </td>", rec.getExchange());
		  	       out.printf("</tr>");
		  	
		  	    }

		      out.println("</table> <br>");
		     
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
        if ( pathInfo != null && pathInfo.startsWith("/query1") ) {
            BufferedReader rd = request.getReader();
            String stat = request.getParameter( "stats" );
            String sort = request.getParameter( "sort_type" );
            String column = request.getParameter( "sort_col" );
            String TheId = request.getParameter("proj_id");
            
            if(stat == null && sort == null){
            	   
                  String servletPath = request.getServletPath();
                  response.sendRedirect( context + servletPath );            	
            }
            
  
            try {
            	 
            	long id = Long.parseLong(TheId);
            	if(stat == null && sort != null){
            	    uma.ProjectReport( stat, id  );
            	} else 
            	if(sort == null && stat != null){
            	   uma.SortProject( sort, column , id );
            		
            	}
            	if(sort != null && stat != null){
            		uma.ProjectReport( stat, id  );
            		
               }
  
                log("adding: " + stat + "at id:" + id );
                
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
            catch( SQLException ex ) {
                out.println("DATABASE Error"); // XXX
                log( ex.getMessage() );
            }
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
            
            log( ex.getMessage() );
        }
    }
}      
        
package servlets;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import java.lang.StringBuilder; 

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
import java.text.SimpleDateFormat;

import db.UserDB;
import db.Users;
import db.Project;
import db.Project_tuple;
import db.UserException;
import java.sql.SQLException;
import db.UsersProjectsManager;
import db.ProjDB;
import com.google.gson.Gson;


/**
 *
 * Edit and add user using forms.
 *
 * @author Olanrewaju Okunlola
 */
public class UserProjects extends HttpServlet {
    private util.HTMLTemplates html;
    private ProjDB uma;
    private Gson gson = new Gson();
    private UserDB usr;
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
         }
         if ( ! request.isUserInRole("user") || ! request.isUserInRole("admin") ) {
            response.sendError( HttpServletResponse.SC_FORBIDDEN, "No premission");
            return;
        }
        if ( uma == null ) {
            response.sendRedirect( context + Constants.DB_ERR_PAGE );
            return;
        }
        */
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        html.printHtmlStart(out);
        out.println("<body>");
        out.println("<div id = 'banner'>");
        out.println("<h1 style='text-align:center; letter-spacing:12px;'> CATA Database: User Projects</h1>");
        out.println("</div>");
        out.printf("<div id = 'navi' style='opacity:0.9;position:absolute;left:20px;width:195px;' ");
        //might not go to these pages, check link address
        out.println(" height:450px;background-color:#ADD8E6; border: thin solid black; font-family:Verdana; font-size: 30px; text-align:center '> ");
        if(  request.isUserInRole("admin" )){      
        
             out.printf("<button id='b1' style= 'width: 130px' onclick='parent.location.href=&quot;%s&quot;'>Logout</button>", context);
             out.println("<p>");
            out.printf("<button id='b2' style= 'width: 130px' onclick='parent.location.href=&quot;%s/admin/user_manage&quot;'>Users</button>", context );
            out.println("</p>");   
        		out.println("<p>");
        		out.printf("<button id='b3' style= 'width: 130px' onclick='parent.location.href=&quot;%s/admin/adminpr&quot;jects''>Projects</button>", context);
        		out.println("</p>");   
        		out.println("<p>");
        		out.printf("<button id='b4' style= 'width: 130px' onclick='parent.location.href=&quot;%s/project/project_list&quot;'> My Projects</button>", context);
        		out.println("</p>");
        		out.println("<p>");
        		
        } else {
        	    
        	   out.printf("<button id='b1' style= 'width: 130px' onclick='parent.location.href=&quot;%s&quot;'>Logout</button>", context);
            out.println("<p>");
            out.printf("<button id='b4' style= 'width: 130px' onclick='parent.location.href=&quot;%s/project/project_list&quot;'> My Projects</button>", context);
        		out.println("</p>");         
            
       }
        out.println("<img id='cir' src='image.jpg' width = '100' height = '90'>");
        out.println("</div>");
        out.println("<div id = 'content'> ");
        out.println("<div style ='font-family:verdana;padding:10px;border-radius:10px;border:10px solid #616D7E; width:1000px;");
        out.println("height:850px; '>");        
        out.println("<ul>");
        try {
            List<Project> list = uma.getProjects( user );
            for( Project rec : list ) {
            	out.printf("<li> <a href='%s/project/proj-descrip/%s' target='_blank'>", context, rec.getRecordID());
                out.printf("%s </a>", rec.getProjectName() );
                out.printf("<button  project = '%s' id='%d' style= 'width: 130px'> Report </button> </li>",
                     rec.getProjectName(), rec.getRecordID() );
                out.printf("<button  project = '%s' id='%d' style= 'width: 130px'> Edit </button> </li>%n",
                     rec.getProjectName(), rec.getRecordID() );
                out.printf("<button  project = '%s' id='%d' style= 'width: 130px'> Delete </button> </li>%n",
                     rec.getProjectName(), rec.getRecordID() );
                out.println("</li>");

            }
             out.println("</ul>");
             out.println("<hr style='width: 65%; height: 1em; margin: 0 auto;'>");
             out.println("<p> Projects Shared With Me");
             out.println("<ul>");
             List<String> accesslist = uma.getAccessList();
             for(String s: accesslist){
             	   if( s.contains(user) ){
             	   	  List<Project> shared = uma.getSharerProjects(s);
             	   	  for(Project newrec : shared){
             	   	  	    out.printf("<li> <a href='%s/project/proj-descrip/%s' target='_blank'>", context, newrec.getRecordID());
                            out.printf("%s </a>", newrec.getProjectName() );
                            out.printf("<input id='%s' type='text' size='20'", newrec.getName() );
                            out.printf("value = ' %s ", newrec.getName() );
                            out.printf("%s' readonly> %n", newrec.getEmail() );
                            out.println("</li>");
             	   	  }
             	   }
             }
             out.println("</ul>");
             
            out.println(" <br> ");
            
            out.printf("<form action='%s/project/project-list/add-proj' method='post'>%n",context);
            out.println("<legend>Create a Project:</legend>");
            out.println(" Project Name: <input type='text' size='30' id = 'proj-name' name='project-name'><br>");
            out.println("Project Id: <input type = 'text' size = '5' id = 'theid' name = 'proj_id' ><br>");
            out.println(" Users Who May Access Project: <input type='text' size='40'  id = 'access' name='access-list'><br><br>");
            out.println("Description:  <textarea id='descrip' rows='4' cols='70' name = 'descrip' > </textarea>");
            out.println(" Type of Data Recorded:<br>");
            //first select option panel
            out.println(" <input type='text' name='Time' value='Time' size = '4' id = 'time' readonly>:  ");
            out.println("  <select id = 's1' name = 's1' >   ");
            out.println("  <option selected> Date_Month </option> <option>  String </option>   ");
            out.println("    </select> <br> ");
            
            //second select option panel
            out.println(" <input type='text' name='GDP' value='GDP' id = 'gdp' size = '3' readonly >:   <br>  ");
            out.println(" <select name = 's2' >    ");
            out.println("  <option selected>Nominal </option>  <option>Real </option>  ");
            out.println("   </select> <br>  ");
            
            //third select option panel
            out.println(" <input type='text' name='Inflation' value='Inflation' id = 'inflation' size = '7' readonly>:  <br> ");
            out.println("  <select name = 's3' >   ");
            out.println("  <option selected> CPI </option> <option>  Dollar figures </option>  ");
            out.println("   </select> <br>  ");
            
            //fourth select option panel
            out.println(" <input type='text' name='Unemployment' value='Unemployment' id = 'unemployment' size = '11' readonly>:   <br> ");
            out.println("  <select name = 's4' >   ");
            out.println("   <option selected> Figures </option> <option> Proportion  </option>  ");
            out.println("   </select> <br>  ");
            
            //5th select option panel
            out.println("  <input type='text' name='Exchange_rate' value='Exchange_rate' id = 'exchange_rate' size = '11' readonly>:  <br> ");
            out.println("  <select name = 's5' >   ");
            out.println("   <option selected> Canadian_Dollars </option> <option>  US_Dollars </option> ");
            
            out.println("   </select> <br>  ");
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
   
    private static class ProjectTypes{
        String time;
        long gdp;
        double inflation;
        double unemply;
        double exchange;
        public String toString() {
            return  time + " " + gdp + " " + inflation + " " + unemply + " " + exchange  ;
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
        if ( pathInfo != null && pathInfo.startsWith("/add_proj") ) {
        	   String TheId = request.getParameter("proj_id");
        	   long id = 1;
        	   if( TheId == null){
        	   	   id = 0;
            String Time = request.getParameter( "Time" );
            if(Time != null){
						Time = "ddmmyy";            	
            }
            	
            long gdp = 0;
            String Gdp = request.getParameter( "Gdp" );
            if(Gdp != null){  gdp = 1;}
             
             	 
            double inflation = 0;
            String Inflation = request.getParameter( "Inflation" );
            if(Inflation != null){ inflation = 1;}
            
            
            double unemploy = 0;
            String Unemploy =  request.getParameter("Unemployment");
            if(Unemploy != null) {unemploy = 1;}
                   
           
            double exchange = 0;
            String Exchange = request.getParameter("Exchange_rate");
            if(Exchange != null){ exchange = 1;}
           
            
            String ProjectName = request.getParameter("project-name");
            String AccessList = request.getParameter("access-list");
            String Descrip = request.getParameter("descrip");
            String DescripParsed = Descrip.replace("+", " ");
            String Selector1 = request.getParameter("s1") + ",";
            String Selector2 = request.getParameter("s2") + ",";
            String Selector3 = request.getParameter("s3") + ",";
            String Selector4 = request.getParameter("s4") + ",";
            String Selector5 = request.getParameter("s5") + ",";
            String types = new StringBuilder().append(Selector1).append(Selector2).append(Selector3).append(Selector4).append(Selector5).toString();
            
            if ( Time == null & Gdp == null & Inflation == null & Unemploy == null &
                 Exchange == null )
            {
                response.sendRedirect( context + Constants.INVALID_URL_PAGE );
                return;
            }
            PrintWriter out1 = response.getWriter();
            //response.setContentType("application/json");
            //BufferedReader rd = request.getReader();
           // String json = readAll( rd );
            //log( json );
            try {
               
                
                id = Long.parseLong( TheId );
                //double infl = Double.parseDouble( Inflation );
                //double unemployment = Double.parseDouble( Unemploy );
                //double exchange-rate = Double.parseDouble( Exchange );
                
                Project_tuple newpj = new Project_tuple( id, user, ProjectName, DescripParsed,  types, AccessList,
                 													 Time, gdp, inflation, unemploy, exchange   );
               
                String email = usr.getUserEmail( user) ;
                long theid = uma.Initialize( user, email, newpj );
                log("adding: " + user + "=" + newpj );
                
                //uma.add( entry.user, entry.password );
                
                //out.print("{\"redirect\":\"project\project-list\"}");
            }
            catch( SQLException ex ) {
                log( ex.getMessage() );
                out1.print(gson.toJson("error")); 
            }
            
            catch( UserException ex ) {
                log( ex.getMessage() );
                response.sendRedirect( context + Constants.DB_ERR_PAGE );
                return;
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
            usr = new UserDB( Constants.DB_PATH );
        }
        catch( UserException ex ) {
            uma = null;
            log( ex.getMessage() );
        }
        catch( SQLException ex ) {
        	System.out.println("DATABASE Error"); // XXX
            log( ex.getMessage() );
           
        }
    }
}

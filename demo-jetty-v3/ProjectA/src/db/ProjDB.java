package db;

import java.util.List;
import java.util.ArrayList;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.PreparedStatement;

@SuppressWarnings("unchecked")
public class ProjDB{
    @SuppressWarnings("unused")
	static private boolean dbLoaded = false;
    static {
        try {
            @SuppressWarnings("unused")
			final Class clazz = Class.forName("org.sqlite.JDBC");
            dbLoaded = true;
        }
        catch ( Exception ex ) {
            dbLoaded = false;
        }
    }

    private final String dbPath;

    
    
    public ProjDB(String path) throws SQLException {
        dbPath = path;
        Connection conn = null;
        Statement stmt = null;
        try {
            conn = openConnection();
            stmt = conn.createStatement();
            stmt.executeUpdate(
                "create table if not exists Project_tuple (" +
                "    id integer primary key," +
                "    username text not null unique," +
                "    project-name text  ," +
                "    project-desc text  unique," +
                "    type text  ," +
                "    users-access text," +
                "    time text primary key  ," +
                "    gdp integer  ," +
                "    inflation integer ," +
                "    unemployment integer ," +
                "    exchange-rate integer );" );
            stmt.executeUpdate(
                "create table if not exists projects (" +
                "    proj-id integer primary key," +
                "    type text ," +
                "    project-name text not null " +
                "    username text not null unique," +
                "    user-email text not null unique);" );
           stmt.executeUpdate(
                "create table if not exists reports (" +
                "    proj-id integer primary key," +
                "    query text primary key ," +
                "    time text ," +
                "    gdp integer  ," +
                "    inflation integer ," +
                "    unemployment integer ," +
                "    exchange-rate integer );" );
   
        }
        finally {
            closeConnection( conn );
            if ( stmt != null ) stmt.close();
        }
    }
    
     // add a new project and initialize values in Project_tuples returns a unique rowid for the added record
    public long Initialize(String  user, String email, Project_tuple pt ) throws SQLException, UserException {
        long rowid = -1;
        Connection conn = null;
        try {
            conn = openConnection();
            conn.setAutoCommit(false);
            //PreparedStatement maxid = conn.prepareStatement(
            //   "select rowid from bloodpressure order by rowid desc limit 1");
            PreparedStatement maxid = conn.prepareStatement(
                "select last_insert_rowid() from Project_tuple");
           PreparedStatement userproject = conn.prepareStatement(
                "insert into projects values(?,?, ?, ?, ?);");
            userproject.setDouble(1, pt.getRecordID());
            userproject.setString(2, pt.getTypes());
            userproject.setString(3, user);
            userproject.setString(4, pt.getProjectName());
            userproject.setString(5, email );
            userproject.executeUpdate();
            userproject.close();
            
            PreparedStatement stmt = conn.prepareStatement(
                "insert into Project_tuple values(?,?,?,?,?,?,?,?,?,?,?);");
            stmt.setDouble(1, pt.getRecordID());
            stmt.setString(2, user);
            stmt.setString(3, pt.getProjectName() ); 
            stmt.setString(4, pt.getDescrip() ); 
            stmt.setString(5, pt.getTypes() ); 
            stmt.setString(6, pt.getUserAcess() ); 
            stmt.setString(7, pt.getTime() ); 
				stmt.setLong(8, pt.getGDP() ); 
				stmt.setDouble(9, pt.getInflation() ); 
				stmt.setDouble(10, pt.getUnemploy() ); 
				stmt.setDouble(11, pt.getExchange() ); 
            stmt.executeUpdate();
            stmt.close();
            ResultSet rs = maxid.executeQuery();
            conn.commit();
            if ( rs.next() ) {
                 rowid = rs.getLong(1);
            }
            else {
                throw new UserException("last row rowid missing");
            }
            pt.setRecordID( rowid );
            
            maxid.close();
            conn.setAutoCommit( true );
        }
        catch( SQLException ex ) {
            if ( conn != null ) {
                try {
                    conn.rollback();
                }
                catch( SQLException ex1 ) {
                }
            }
            throw new UserException( ex.getMessage() );
        }
        finally {
            closeConnection( conn );
        }
        return rowid;
    }
    
     // returns a unique rowid for the added record
    public long add( long id, String user, String projectname, String time, long GDP,
     double Infl, double Unemploy, double Exchange ) throws UserException, SQLException {
        long rowid = -1;
        Connection conn = null;
        try {
            conn = openConnection();
            conn.setAutoCommit(false);
            //PreparedStatement maxid = conn.prepareStatement(
            //   "select rowid from bloodpressure order by rowid desc limit 1");
            //PreparedStatement maxid = conn.prepareStatement(
               // "select last_insert_rowid() from Project_tuple");
           
            
            PreparedStatement stmt = conn.prepareStatement(
                "insert into Project_tuple values(?,?,?, null , null, null,?,?,?,?,?);");
            stmt.setDouble(1, id );
            stmt.setString(2, user);
            stmt.setString(3, projectname ); 
            stmt.setString(4, time ); 
            stmt.setLong(5, GDP ); 
            stmt.setDouble(6, Infl ); 
            stmt.setDouble(7, Unemploy ); 
            stmt.setDouble(8, Exchange ); 
				
				 
            stmt.executeUpdate();
            stmt.close();
            ResultSet rs = stmt.executeQuery();
            conn.commit();
            if ( rs.next() ) {
                 rowid = rs.getLong(1);
            }
            else {
                throw new UserException("last row rowid missing");
            }
            //pt.setRecordID( rowid );
            
            stmt.close();
            conn.setAutoCommit( true );
        }
        catch( SQLException ex ) {
            if ( conn != null ) {
                try {
                    conn.rollback();
                }
                catch( SQLException ex1 ) {
                }
            }
            throw new UserException( ex.getMessage() );
        }
        finally {
            closeConnection( conn );
        }
        return rowid;
    }
    
    public void deleteProject( String user, String projectname ) throws SQLException {
        Connection conn = null;
        try {
            conn = openConnection();
            PreparedStatement stmt =  conn.prepareStatement(
                "delete from projects where username=? and project-name = ?;");
            stmt.setString(1, user); 
            stmt.setString(2, projectname); 
            stmt.executeUpdate();
            stmt.close();
            /**stmt = conn.prepareStatement(
                "delete from roles where " +
                "roles_id=(select role_id from users where username = ?);");
            stmt.setString(1, user); 
            stmt.executeUpdate();
            stmt.close();*/ //no longer needed
           
        }
        finally {
            closeConnection( conn );
        }
    }
    
    public void deleteProject_tuple( String user, long id, String time, long gdp, 
    double inflation, double unemploy, double exchange  ) throws SQLException {
        Connection conn = null;
        try {
            conn = openConnection();
            PreparedStatement stmt =  conn.prepareStatement(
                "delete from Project_tuple where id = ? and username=? " +
                " and time = ? and gdp = ? and inflation = ? and unemployment = ? and exchange = ?;");
            stmt.setLong(1, id); 
            stmt.setString(2, user); 
            stmt.setString(3, time); 
            stmt.setLong(4, gdp); 
            stmt.setDouble(5, inflation); 
            stmt.setDouble(6, unemploy); 
            stmt.setDouble(7, exchange); 
            stmt.executeUpdate();
            stmt.close();
            /**stmt = conn.prepareStatement(
                "delete from roles where " +
                "roles_id=(select role_id from users where username = ?);");
            stmt.setString(1, user); 
            stmt.executeUpdate();
            stmt.close();*/ //no longer needed
           
        }
        finally {
            closeConnection( conn );
        }
    }
    
    public void updateDescripTuples( long id, String projectname, String project_descrip, String users_list )
    throws UserException, SQLException
    {//return to this to make return an arraylist
        Connection conn = null;
        try {
            conn = openConnection();
            PreparedStatement stmt = conn.prepareStatement(
                "update Project_tuple set project-name = ?, project-desc = ?, users-access = ? where id = ?;");
				stmt.setString(1, projectname ); 
            stmt.setString(2, project_descrip ); 
            stmt.setString(3, users_list );            
            stmt.setLong(4, id); 
            
            stmt.close();
        }
        catch( SQLException ex ) {
            throw new UserException( ex.getMessage() );
        }
        finally {
            closeConnection( conn );
        }
        
    }   
    
    public void updateTuples(String user, long id, String time, long gdp, 
    double inflation, double unemploy, double exchange  ) throws SQLException {
        Connection conn = null;
        try {
            conn = openConnection();
            PreparedStatement stmt =  conn.prepareStatement(
                "update Project_tuple set " +
                " time = ?, gdp = ?, inflation = ?, unemployment = ?, exchange = ? " 
                + " where id = ? and user = ? and time = ? ;");
             
            stmt.setString(1, time); 
            stmt.setLong(2, gdp); 
            stmt.setDouble(3, inflation); 
            stmt.setDouble(4, unemploy); 
            stmt.setDouble(5, exchange); 
            stmt.setLong(6, id); 
            stmt.setString(7, user);
            stmt.setString(8, time); 
            
            stmt.executeUpdate();
            stmt.close();
            /**stmt = conn.prepareStatement(
                "delete from roles where " +
                "roles_id=(select role_id from users where username = ?);");
            stmt.setString(1, user); 
            stmt.executeUpdate();
            stmt.close();*/ //no longer needed
           
        }
        finally {
            closeConnection( conn );
        }
    }
    
    
    
    public List<Project_tuple> getProjectDescrip( long id )
    throws UserException, SQLException
    {//return to this to make return an arraylist
        Connection conn = null;
        List<Project_tuple> list = null;
        try {
            conn = openConnection();
            PreparedStatement stmt = conn.prepareStatement(
                "select * from Project_tuple where id = ?");
            stmt.setLong(1, id); 
            ResultSet rs = stmt.executeQuery();
            list = listFromResultSet( rs );
            rs.close();
            stmt.close();
        }
        catch( SQLException ex ) {
            throw new UserException( ex.getMessage() );
        }
        finally {
            closeConnection( conn );
        }
        return list;
    }
    
    public void ProjectReport( String query, long id  )
    throws UserException, SQLException
    {//return to this to make return an arraylist
        Connection conn = null;
        try {
            conn = openConnection();
            PreparedStatement stmt = conn.prepareStatement(
                "select ?(time), ?(gdp), ?(inflation), ?(unemployment), ?(exchange) from Project_tuple where id = ?;");
            stmt.setString(1, query); 
            stmt.setLong(2, id); 
            ResultSet rs = stmt.executeQuery();
            InsertinReport( rs, query, id );
            rs.close();
            stmt.close();
        }
        catch( SQLException ex ) {
            throw new UserException( ex.getMessage() );
        }
        finally {
            closeConnection( conn );
        }
        //return list;
    }
    
    public void SortProject( String query, String column , long id  )
    throws UserException, SQLException
    {//return to this to make return an arraylist
        Connection conn = null;
        //List<ReportAnswer> list = null;
        try {
            conn = openConnection();
            PreparedStatement stmt = conn.prepareStatement(
                "select time, gdp, inflation, unemployment, exchange from Project_tuple where id = ? order by ? ?;");
            stmt.setLong(1, id); 
            stmt.setString(2, column); 
            stmt.setString(3, query);
            ResultSet rs = stmt.executeQuery();
            query.concat(" ");
            query.concat(column);
            InsertinReport( rs, query, id );
            rs.close();
            stmt.close();
        }
        catch( SQLException ex ) {
            throw new UserException( ex.getMessage() );
        }
        finally {
            closeConnection( conn );
        }
        //return list;
    }
    
    public List<Project> getProjects() throws UserException, SQLException {//return to this to make return an arraylist
        Connection conn = null;
        List<Project> list = null;
        try {
            conn = openConnection();
            PreparedStatement stmt = conn.prepareStatement(
                "select * from projects");
            ResultSet rs = stmt.executeQuery();
            list = projectsFromResultSet( rs );
            rs.close();
            
            stmt.close();
        }
        catch( SQLException ex ) {
            throw new UserException( ex.getMessage() );
        }
        finally {
            closeConnection( conn );
        }
        return list;
    }
    
     public List<ReportAnswer> getReports(long id) throws UserException, SQLException {//return to this to make return an arraylist
        Connection conn = null;
        List<ReportAnswer> list = null;
        try {
            conn = openConnection();
            PreparedStatement stmt = conn.prepareStatement(
                "select * from reports where proj-id = ?");
            stmt.setLong(1, id);             
            ResultSet rs = stmt.executeQuery();
            list = ReportFromResultSet( rs );
            rs.close();
            
            stmt.close();
        }
        catch( SQLException ex ) {
            throw new UserException( ex.getMessage() );
        }
        finally {
            closeConnection( conn );
        }
        return list;
    }
    
    public List<Project> getProjects( String user) throws UserException, SQLException {//return to this to make return an arraylist
        Connection conn = null;
        List<Project> list = null;
        try {
            conn = openConnection();
            PreparedStatement stmt = conn.prepareStatement(
                "select * from projects where username = ? ");
            stmt.setString(1, user); 
            ResultSet rs = stmt.executeQuery();
            list = projectsFromResultSet( rs );
            rs.close();
         
            stmt.close();
        }
        catch( SQLException ex ) {
            throw new UserException( ex.getMessage() );
        }
        finally {
            closeConnection( conn );
        }
        return list;
    }
    
    public List<Project> getSharerProjects( String access_list) throws UserException, SQLException {//return to this to make return an arraylist
        Connection conn = null;
        List<Project> list = null;
        try {
            conn = openConnection();
            PreparedStatement stmt = conn.prepareStatement(
                "select * from projects where id = ( select id from Project_tuple where users-access = ?); ");
            stmt.setString(1, access_list); 
            ResultSet rs = stmt.executeQuery();
            list = projectsFromResultSet( rs );
            rs.close();
            stmt.close();
        }
        catch( SQLException ex ) {
            throw new UserException( ex.getMessage() );
        }
        finally {
            closeConnection( conn );
        }
        return list;
    } 
    
    public String getProjectName( long id) throws UserException, SQLException {//return to this to make return an arraylist
        Connection conn = null;
        String list = null;
        try {
            conn = openConnection();
            PreparedStatement stmt = conn.prepareStatement(
                "select project-name from projects where id = ?; ");
            stmt.setLong(1, id); 
            ResultSet rs = stmt.executeQuery();
            if ( rs.next() ) {
                list = rs.getString(1);
            }

            stmt.close();
        }
        catch( SQLException ex ) {
            throw new UserException( ex.getMessage() );
        }
        finally {
            closeConnection( conn );
        }
        return list;
    }
     
    
    
    
    public  List<String> getAccessList() throws UserException, SQLException {//return to this to make return an arraylist
        Connection conn = null;
        ArrayList<String> list = new ArrayList<String>();
        try {
            conn = openConnection();
            PreparedStatement stmt = conn.prepareStatement(
                "select users-access from Project_tuple ");
           
            ResultSet rs = stmt.executeQuery();
            
            while( rs.next() ) {
                list.add( rs.getString(1) );
            }
            rs.close();
            stmt.close();
        }
        catch( SQLException ex ) {
            throw new UserException( ex.getMessage() );
        }
        finally {
            closeConnection( conn );
        }
        return list;
    }    
    
    
    
    private List<Project> projectsFromResultSet( ResultSet rs )
    throws SQLException
    {
        ArrayList<Project> list = new ArrayList<Project>();
        while( rs.next() ) {
            String username = rs.getString("username");
            String email = rs.getString("user-email");
            String types = rs.getString("type");
            String project_name = rs.getString("project-name");
            long project_id = rs.getLong("proj-id");
            
            
            Project pj = new Project(project_id, types, project_name, username, email);
            list.add( pj );
        }
        
        return list;
    }
    
    private List<Project_tuple> listFromResultSet( ResultSet rs )
    throws SQLException
    {
        ArrayList<Project_tuple> list = new ArrayList<Project_tuple>();
        while( rs.next() ) {
        	long id = rs.getLong("id");
        	String timeresult = rs.getString("time");
        	String username = rs.getString("username");
            String project_name = rs.getString("project-name");
            String project_descrip = rs.getString("project-desc");
            String types = rs.getString("type");
            String access = rs.getString("users-access");
            long gdp_result = rs.getLong("gdp");
            double inflation = rs.getDouble("inflation");
            double unemploy = rs.getDouble("unemployment");
            double exchange = rs.getDouble("exchange-rate");
            
            
            //String projectname = rs.getString("project-name");
            Project_tuple pj = new Project_tuple(id,username, project_name, project_descrip,
            		types, access, timeresult, gdp_result, inflation, unemploy, exchange);
            list.add( pj );
        }
        
        return list;
    }
    
    
    private void InsertinReport( ResultSet rs, String query, long id )
    throws SQLException, UserException
    {
        while( rs.next() ) {
        	Connection conn = null;
            String timeresult = rs.getString("time");
            long gdp_result = rs.getLong("gdp");
            double inflation = rs.getDouble("inflation");
            double unemploy = rs.getDouble("unemployment");
            double exchange = rs.getDouble("exchange-rate");
            conn = openConnection();
            try {
               PreparedStatement stmt = conn.prepareStatement(
                "insert into reports values(?,?,?,?,?,?,?)");
                stmt.setLong(1, id );
                stmt.setString(2, query);
                stmt.setString(3, timeresult ); 
                stmt.setLong(4, gdp_result ); 
                stmt.setDouble(5, inflation ); 
                stmt.setDouble(6, unemploy ); 
                stmt.setDouble(7, exchange ); 
                stmt.close();
           }
           
          catch( SQLException ex ) {
            throw new UserException( ex.getMessage() );
         }
         finally {
             closeConnection( conn );
         }  
         rs.close();  
            //ReportAnswer rep = new ReportAnswer(id, query, timeresult, gdp-result, inflation, unemploy, exchange);
            //list.add( rep );
        }
        //return list;
    }
    
    private List<ReportAnswer> ReportFromResultSet( ResultSet rs )
    throws SQLException
    {
        ArrayList<ReportAnswer> list = new ArrayList<ReportAnswer>();
        while( rs.next() ) {
        	   long id = rs.getLong("proj-id");
            String timeresult = rs.getString("time");
            String query = rs.getString("query");
            long gdp_result = rs.getLong("gdp");
            double inflation = rs.getDouble("inflation");
            double unemploy = rs.getDouble("unemployment");
            double exchange = rs.getDouble("exchange-rate");
 
            ReportAnswer rep = new ReportAnswer(id, query, timeresult, gdp_result, inflation, unemploy, exchange);
            list.add( rep );
        }
        
        return list;
    }
    
    private Connection openConnection() throws SQLException {
        String url = "jdbc:sqlite:" + dbPath;
        Connection conn = null;
        conn = DriverManager.getConnection( url );
        return conn;
    }

    private void closeConnection( Connection conn ) throws SQLException {
        if ( conn != null ) {
            conn.close();
        }
    }


    public static void main( String[] args ) throws Exception {
        ProjDB projdb = new ProjDB("proj.db"); 
        Project_tuple pt1 = new Project_tuple(10, "ooo524", "project1", "Test Project", "String,Nominal,CPI,Figures,Canadian_Dollars", "rod", "April22012", 200000, 12, 5, 2); //long id, String user, String name, String descrip, String type, String users String time, long gdp, double inflation, double unemploy, double exchange)
        Project_tuple pt2 = new Project_tuple(12, "rod", "project1", "Test Project2", "String,Nominal,CPI,Figures,Canadian_Dollars", "ooo524", "March22010", 2045300, 18, 9, 2);
        //mkRoleIds();
        projdb.Initialize( "ooo524", "ooo524@mun.ca", pt1);
        projdb.Initialize( "rod", "rod@mun.ca", pt2);
        //System.out.println("uid = " + ua.("rod"));
        List<Project> list = projdb.getProjects("rod");
        for( Project s : list ) {
            System.out.println( s );
        }
    }
}

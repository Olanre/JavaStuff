package db;

import java.util.List;
import java.util.ArrayList;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.PreparedStatement;

public class UserDB{
    static private boolean dbLoaded = false;
    static {
        try {
            Class clazz = Class.forName("org.sqlite.JDBC");
            dbLoaded = true;
        }
        catch ( Exception ex ) {
            dbLoaded = false;
        }
    }

    private final String dbPath;

    public UserDB(String path) throws SQLException, UserException {
        dbPath = path;
        Connection conn = null;
        Statement stmt = null;
        
        try {
            conn = openConnection();
            mkRoleIds(); 
            stmt = conn.createStatement();
            stmt.executeUpdate(
                "create table if not exists Users (" +
                "    id integer primary key," +
                "    username text not null unique," +
                "    user-email text not null unique," +
                "    pwd text not null);" +
                "    role_id integer not null unique);" );
            stmt.executeUpdate(
                "create table if not exists Roles (" +
                    "id integer primary key," +
                    "role text not null unique);" );
            
        }
        finally {
            closeConnection( conn );
            if ( stmt != null ) stmt.close();
        }
    }
    
    
    
    private List<Users> listFromResultSet( ResultSet rs )
    throws SQLException
    {
        ArrayList<Users> list = new ArrayList<Users>();
        while( rs.next() ) {
            String username = rs.getString("username");
            long rowid = rs.getLong("id");
            long role = rs.getLong("role_id");
            String email = rs.getString("user-email");
            String password = rs.getString("pwd");
            Users users = new Users(rowid, username, email, password, role );
            list.add( users );
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

    public List<String> getUserNames() throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ArrayList<String> list = new ArrayList<String>();
        try {
            conn = openConnection();
            stmt = conn.prepareStatement(
                "select username from Users order by username;");
            ResultSet rs = stmt.executeQuery();
            while( rs.next() ) {
                list.add( rs.getString(1) );
            }
            rs.close();
        }
        finally {
            if ( stmt != null ) stmt.close();
            closeConnection( conn );
        }
        return list;
    }
    
    public String getUserEmail( String user) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        String list = null;
        try {
            conn = openConnection();
            stmt = conn.prepareStatement(
                "select user-email from Users where username = ?;");
            stmt.setString(1, user);
            ResultSet rs = stmt.executeQuery();
            if ( rs.next() ) {
                list = rs.getString(1);
            }
            rs.close();
        }
        finally {
            if ( stmt != null ) stmt.close();
            closeConnection( conn );
        }
        return list;
    }
    
    public List<String> getUserEmails() throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ArrayList<String> list = new ArrayList<String>();
        try {
            conn = openConnection();
            stmt = conn.prepareStatement(
                "select user-email from Users order by username;");
            ResultSet rs = stmt.executeQuery();
            while( rs.next() ) {
                list.add( rs.getString(1) );
            }
            rs.close();
        }
        finally {
            if ( stmt != null ) stmt.close();
            closeConnection( conn );
        }
        return list;
    }
 
 
    public List<Users> getUsersInfo( )
    throws UserException, SQLException
    {//return to this to check if necessary
        Connection conn = null;
        List<Users> list = null;
        try {
            conn = openConnection();
            PreparedStatement stmt = conn.prepareStatement(
                "select * from Users;");
            //stmt.setString(1, user); 
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
    
    
 
    public List<String> getRoleNames() throws SQLException {
        Connection conn = openConnection();
        PreparedStatement stmt = null;
        ArrayList<String> list = new ArrayList<String>();
        try {
            stmt = conn.prepareStatement(
                "select role from Roles order by role;");
            ResultSet rs = stmt.executeQuery();
            while( rs.next() ) {
                list.add( rs.getString( 1 ) );
            }
            rs.close();
        }
        finally {
            if ( stmt != null ) stmt.close();
            closeConnection( conn );
        }
        return list;
    }
    

    public long getUserId( String user ) throws SQLException {
        long id = -1;
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = openConnection();
            stmt = conn.prepareStatement(
                "select id from Users where username = ?");
            stmt.setString(1, user);
            ResultSet rs = stmt.executeQuery();
            if ( rs.next() ) {
                id = rs.getLong(1);
            }
        }
        finally {
            if ( stmt != null ) stmt.close();
            closeConnection( conn );
        }
        return id;
    }
    
    

    public String getRoleName( long role_id ) throws SQLException {
        String rolename = null;
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = openConnection();
            stmt = conn.prepareStatement(
                "select role from Roles where id = ?");
            stmt.setLong(1, role_id);
            ResultSet rs = stmt.executeQuery();
            if ( rs.next() ) {
                rolename = rs.getString(1);
            }
        }
        finally {
            if ( stmt != null ) stmt.close();
            closeConnection( conn );
        }
        return rolename;
    }

    public long addUser( Users newuser) throws SQLException {
        long rowid = -1;
        Connection conn = null;
        try {
            conn = openConnection();
            conn.setAutoCommit(false);
            PreparedStatement stmt = conn.prepareStatement(
                "insert into Users values(?,?,?,?,?,?);");
            stmt.setLong(1, newuser.getRecordID());
            stmt.setString(2, newuser.getName()); 
            stmt.setString(3, newuser.getEmail() ); 
            stmt.setString(4, newuser.getPassword());
            stmt.setLong(5,  newuser.getRole());
            stmt.executeUpdate();
            stmt.close();
            PreparedStatement maxid = conn.prepareStatement(
                "select last_insert_rowid() from Users;");
            ResultSet rs = maxid.executeQuery();
            conn.commit();
            if ( rs.next() ) {
                 rowid = rs.getLong(1);
            }
            else {
                throw new SQLException("last row rowid missing");
            }
            newuser.setRecordID( rowid );
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
            
        }
        finally {
            closeConnection( conn );
        }
        return rowid;
    }

   private void mkRoleIds() throws SQLException {
        Connection conn = null;
        try {
            conn = openConnection();
            // check if role already set
            PreparedStatement count = conn.prepareStatement(
                "insert into Roles values ( 1, User )  " +
                "insert into Roles values ( 2, Administrator );");            
            count.close();

        }
        catch( SQLException ex ) {
            if ( conn != null ) {
                try {
                    conn.rollback();
                }
                catch( SQLException ex1 ) {
                }
            }
            
        }
        finally {
            closeConnection( conn );
        }



   }
    public boolean UpdateUserRole( String user, String role ) throws SQLException {
        Connection conn = null;
        try {
            conn = openConnection();
            // check if role already set
            PreparedStatement count = conn.prepareStatement(
                "select username from Users where username = ?) " +
                "and role_id = (select id from Roles where role = ?);");
            count.setString(1, user); 
            count.setString(2, role); 
            ResultSet rs = count.executeQuery();
            if ( rs.next() ) {
                 long c = rs.getLong(1);
                 if ( c == 1 ) {
                    count.close();
                    return true;
                 }
            }
            count.close();

            PreparedStatement stmt = conn.prepareStatement(
                "update Users set username, role_id  where username = ? and  role_id = " +
                " (select id from Roles where role = ?) );");
            stmt.setString(1, user); 
            stmt.setString(2, role); 
            stmt.executeUpdate();
            stmt.close();
        }
        finally {
            closeConnection( conn );
        }
        return true;
    }
  
    public String getUserRole( String user ) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        String list = null;
        try {
            conn = openConnection();
            stmt = conn.prepareStatement(
                "select Roles.role from roles " +
                "where id = " + 
                "(select id from Users where username = ?); "
                );
            stmt.setString( 1, user );
            ResultSet rs = stmt.executeQuery();
            while( rs.next() ) {
                list =  rs.getString(1) ;
            }
            rs.close();
        }
        finally {
            if ( stmt != null ) stmt.close();
            closeConnection( conn );
        }
        return list;
    }

  

    public void deleteUser( String user ) throws SQLException {
        Connection conn = null;
        try {
            conn = openConnection();
            PreparedStatement stmt =  conn.prepareStatement(
                "delete from Users where username=?;");
            stmt.setString(1, user); 
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

    public static void main( String[] args ) throws Exception {
        UserDB ua = new UserDB("ua2.db"); 
        Users firstuser = new Users(1, "ooo524", "ooo524@mun.ca", "ooo524", 2 );
        Users seconduser = new Users(1, "rod", "rod@mun.ca", "rod", 2 );
        Users thirduser = new Users(1, "dor", "dor@mun.ca", "dor", 1 );
        ua.mkRoleIds();
        ua.addUser (firstuser);
        ua.addUser (seconduser);
        ua.addUser (thirduser);
        
        System.out.println("uid = " + ua.getUserId("ooo524"));
        List<String> list = ua.getUserNames();
        for( String s : list ) {
            System.out.println( s );
        }
    }
}

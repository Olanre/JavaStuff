package db;

import java.sql.SQLException;
import java.util.List;

public interface UsersProjectsManager {
	long add( String user, String email, Project_tuple pt );
    
    void deleteProject( String user, String projectname ) throws SQLException;
    
     List<Project_tuple> getProjectRecords( String user )  throws UserException;
     
   List<Project> getProjects() throws UserException;
   
   List<Project_tuple> getProjectDescrip( long id ) throws UserException;
   
   long add( long id, String user, String projectname, String time, long GDP,
    double Infl, double Unemploy, double Exchange ) throws UserException;
   
   List<Project> getSharerProjects( String access_list) throws UserException, SQLException;
   
   void ProjectReport( String query, long id  )throws UserException;
   
   void SortProject( String query, String column , long id  )throws UserException;
   
   void deleteProject_Tuple( String user, long id, String time, long gdp,
   double inflation, double unemploy, double exchange) throws SQLException;
   
   List<Project> getProjects( String access_list) throws UserException;
   
   String[] getUserProjects( String user ) throws SQLException;
   
   List<String> getAccessList() throws UserException;
   
   List<String> getUserNames() throws SQLException;
   
   String getUserEmail( String user) throws SQLException;
   
   List<String> getUserEmails() throws SQLException;
   
   List<Users> getUsersInfo() throws UserException;
   
   List<String> getRoleNames() throws SQLException;
   
   long getUserId( String user ) throws SQLException;
   
   String getRoleName( long role_id ) throws SQLException ;
   
   long addUser( Users newuser) throws SQLException;
   
   boolean UpdateUserRole( String user, String role ) throws SQLException;
   
   String getUserRole( String user ) throws SQLException;
   
  void deleteUser( String user ) throws SQLException;
}
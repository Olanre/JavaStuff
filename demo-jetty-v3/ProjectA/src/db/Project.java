package db;

public class Project {
    private String username;           // user name for record         // user role in database
    private String user_email;        // users email adress 
    private String projectname;      // users password
    private String proj_type; 
    private long recordID;          // unique integer identifying this record

    public Project(
         long id, String type, String proj_name, String name, String email )
    {
    	  this.recordID = id;
        this.username = name;
        this.user_email = email;
        this.proj_type = type;
        this.projectname = proj_name;
    }

    
    public String getProjType() {
        return proj_type;
    }

    public String getProjectName() {
        return projectname;
    }

    public String getEmail() {
        return user_email;
    }

    public long getRecordID() {
        return recordID;
    }

    public void setRecordID( long id ) {
        recordID = id;
    }

    public String getName() {
        return username;
    }


    public String toString() {
        return String.format("%d %s %s %s",
            recordID, proj_type,  projectname , username, user_email);
    }
}

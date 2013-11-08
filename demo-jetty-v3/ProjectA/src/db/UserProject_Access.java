/**package db;

public class UserProject_Access {
    private String sharing_username;           // user name for record         // user role in database
    private String sharing_user_email;        // users email adress 
    private String shared_projectname;      // users projectname
    private long recordID;          // unique integer identifying this record

    public UserProject_Access(
         long id, String name, String email, String proj_name, String role)
    {
    	  this.recordID = id;
        this.sharing_username = name;
        this.sharing_user_email = email;
        this.shared_projectname = proj_name;
    }


    public String getRole() {
        return role;
    }

    public String getProjectName() {
        return shared-projectname;
    }

    public String getEmail() {
        return sharing-user-mail;
    }

    public long getRecordID() {
        return recordID;
    }

    public void setRecordID( long id ) {
        recordID = id;
    }

    public String getName() {
        return sharing-username;
    }


    public String toString() {
        return String.format("%s %s %s %s %s %s",
            recordID, sharing-username, sharing-user-email, shared-projectname );
    }
}*/

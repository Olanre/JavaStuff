package db;

public class Users {
    private String username;           // user name for record
    private long role;          // user role in database
    private String user_email;        // users email adress 
    private String password;      // users password
    private long recordID;          // unique integer identifying this record

    public Users(
         long id, String name, String email, String password, long role)
    {
    	  this.recordID = id;
        this.username = name;
        this.user_email = email;
        this.password = password;
        this.role = role;
    }


    public long getRole() {
        return role;
    }
    
    public void setRole( long id ) {
        role = id;
    }

    public String getPassword() {
        return password;
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
        return String.format("%d %s %s %s %s %d",
            recordID, username, user_email, password, role );
    }
}

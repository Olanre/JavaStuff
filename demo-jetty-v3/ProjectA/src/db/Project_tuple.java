package db;

public class Project_tuple {
	 private String username;
    private String projname;           // 
    private String proj_descrip;          //        // 
    private String user_access;      // 
    private String time;
    private String type;
    private long gdp;
    private double inflation;
    private double unemploy;
    private double exchange;
    private long recordID;          // unique integer identifying this record

    public Project_tuple(
         long id, String user, String name, String descrip, String type, String users,
         String time, long gdp, double inflation, double unemploy, double exchange)
    {
    	  this.recordID = id;
    	  this.username = user;
        this.projname = name;
        this.proj_descrip = descrip;
        this.type = type;
        this.user_access = users;
        this.time = time;
        this.gdp = gdp;
        this.inflation = inflation;
        this.unemploy = unemploy;
        this.exchange = exchange;
    }


    public String getDescrip() {
        return proj_descrip;
    }

    public String getTypes() {
        return type;
    }

    public String getUserAcess() {
        return user_access;
    }

    public String getTime() {
        return time;
    } 
    
    public long getGDP() {
        return gdp;
    }
    
     public double getInflation() {
        return inflation;
    }
    
     public double getUnemploy() {
        return unemploy;
    }
    
    public double getExchange() {
        return exchange;
    }
    
    public long getRecordID() {
        return recordID;
    }

    public void setRecordID( long id ) {
        recordID = id;
    }
    
    public String getUserName() {
        return username;
    }     
    
    public String getProjectName() {
        return projname;
    }


    public String toString() {
        return String.format("%d %s %s %s %s %s %s %d %g %g %g ",
            recordID, username, projname, proj_descrip, type, user_access,
             time,  gdp,  inflation, unemploy, exchange );
    }
}

package db;

public class ReportAnswer {
     // 
    private String time;
    private String query;
    private long gdp;
    private double inflation;
    private double unemploy;
    private double exchange;
    private long recordID;
             // unique integer identifying this record

    public ReportAnswer(
          long id, String query, String time, long gdp, double inflation, double unemploy, double exchange)
    {
    	  this.recordID = id;
    	  this.query = query;
        this.time = time;
        this.gdp = gdp;
        this.inflation = inflation;
        this.unemploy = unemploy;
        this.exchange = exchange;
    }


    public String getTime() {
        return time;
    } 
    
     public String getQuery() {
        return query;
    } 
    
    public long getGDP() {
        return gdp;
    }
    
    public long getRecordID() {
        return recordID;
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
    
    


    public String toString() {
        return String.format("%d %s %s %d %g %g %g ",
        		recordID, query, time,  gdp,  inflation, unemploy, exchange );
    }
}

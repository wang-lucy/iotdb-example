import java.util.Date;

public class Fund {
    private String fID;
    private String fName;
    private double fEWorth;

    public Fund(String fID, String fName, double fEWorth) {
        this.fID = fID;
        this.fName = fName;
        this.fEWorth = fEWorth;
    }

    public String getfID() {
        return fID;
    }

    public void setfID(String fID) {
        this.fID = fID;
    }

    public String getfName() {
        return fName;
    }

    public double getfEWorth() {
        return fEWorth;
    }

    public void setfEWorth(double fEWorth) {
        this.fEWorth = fEWorth;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }


    public void setfEWorth(float fEWorth) {
        this.fEWorth = fEWorth;
    }


}

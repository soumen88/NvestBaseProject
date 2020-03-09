package nvest.com.nvestlibrary.nvestWebModel;

public class RiderInformationModel {
    private int riderId;
    private String riderName;
    private String riderTypeName;
    private boolean wop;


    public RiderInformationModel() {
    }

    public RiderInformationModel(int riderId, String riderName, String riderTypeName) {
        this.riderId = riderId;
        this.riderName = riderName;
        this.riderTypeName = riderTypeName;
    }

    public int getRiderId() {
        return riderId;
    }

    public void setRiderId(int riderId) {
        this.riderId = riderId;
    }

    public String getRiderName() {
        return riderName;
    }

    public void setRiderName(String riderName) {
        this.riderName = riderName;
    }

    public String getRiderTypeName() {
        return riderTypeName;
    }

    public void setRiderTypeName(String riderTypeName) {
        this.riderTypeName = riderTypeName;
    }

    public boolean isWop() {
        return getRiderTypeName().equals("WOP");
    }

}

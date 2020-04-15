package nvest.com.nvestlibrary.databaseFiles.dao.basicinformationdetailstable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import nvest.com.nvestlibrary.commonMethod.NvestLibraryConfig;

@Entity(tableName = NvestLibraryConfig.BASIC_INFORMATION_DETAIL_TABLE)
public class BasicInformationDetailRoom {

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "_id")
    private int id;

    @ColumnInfo(name = "LI_FIRST_NAME")
    private String liFristName;

    @ColumnInfo(name = "LI_LAST_NAME")
    private String liLastName;

    @ColumnInfo(name = "LI_DOB")
    private String   liDob;

    @ColumnInfo(name = "LI_AGE")
    private int liAge;

    @ColumnInfo(name = "LI_GENDER")
    private String liGender;

    @ColumnInfo(name = "PR_FRIST_NAME")
    private String prFirstName;

    @ColumnInfo(name = "PR_LAST_NAME")
    private String prLastName;

    @ColumnInfo(name = "PR_DOB")
    private String prDob;

    @ColumnInfo(name = "PR_AGE")
    private int prAge;

    @ColumnInfo(name = "PR_GENDER")
    private String prGender;

    @ColumnInfo(name = "EMAIL_ID")
    private String emailId;

    @ColumnInfo(name = "STATE_DETAIL")
    private String stateDetail;

    @ColumnInfo(name = "CITY_DETAIL")
    private String cityDetail;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLiFristName() {
        return liFristName;
    }

    public void setLiFristName(String liFristName) {
        this.liFristName = liFristName;
    }

    public String getLiLastName() {
        return liLastName;
    }

    public void setLiLastName(String liLastName) {
        this.liLastName = liLastName;
    }

    public String getLiDob() {
        return liDob;
    }

    public void setLiDob(String liDob) {
        this.liDob = liDob;
    }

    public int getLiAge() {
        return liAge;
    }

    public void setLiAge(int liAge) {
        this.liAge = liAge;
    }

    public String getLiGender() {
        return liGender;
    }

    public void setLiGender(String liGender) {
        this.liGender = liGender;
    }

    public String getPrFirstName() {
        return prFirstName;
    }

    public void setPrFirstName(String prFirstName) {
        this.prFirstName = prFirstName;
    }

    public String getPrLastName() {
        return prLastName;
    }

    public void setPrLastName(String prLastName) {
        this.prLastName = prLastName;
    }

    public String getPrDob() {
        return prDob;
    }

    public void setPrDob(String prDob) {
        this.prDob = prDob;
    }

    public int getPrAge() {
        return prAge;
    }

    public void setPrAge(int prAge) {
        this.prAge = prAge;
    }

    public String getPrGender() {
        return prGender;
    }

    public void setPrGender(String prGender) {
        this.prGender = prGender;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getStateDetail() {
        return stateDetail;
    }

    public void setStateDetail(String stateDetail) {
        this.stateDetail = stateDetail;
    }

    public String getCityDetail() {
        return cityDetail;
    }

    public void setCityDetail(String cityDetail) {
        this.cityDetail = cityDetail;
    }
}

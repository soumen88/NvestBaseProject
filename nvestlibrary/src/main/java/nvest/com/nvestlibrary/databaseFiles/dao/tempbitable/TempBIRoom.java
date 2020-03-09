package nvest.com.nvestlibrary.databaseFiles.dao.tempbitable;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.time.ZonedDateTime;


public class TempBIRoom {



    private int productId;

    private int policyYear;


    private int liAttainedAge;

    private int age;


    private String uniqueKey;


    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getPolicyYear() {
        return policyYear;
    }

    public void setPolicyYear(int policyYear) {
        this.policyYear = policyYear;
    }

    public int getLiAttainedAge() {
        return liAttainedAge;
    }

    public void setLiAttainedAge(int liAttainedAge) {
        this.liAttainedAge = liAttainedAge;
    }

    public String getUniqueKey() {
        return uniqueKey;
    }

    public void setUniqueKey(String uniqueKey) {
        this.uniqueKey = uniqueKey;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}

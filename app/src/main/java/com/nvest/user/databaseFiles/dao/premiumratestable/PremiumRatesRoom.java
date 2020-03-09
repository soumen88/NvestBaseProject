package com.nvest.user.databaseFiles.dao.premiumratestable;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.nvest.user.appConfig.Config;

@Entity(tableName = Config.PREMIUM_RATES_TABLE)
public class PremiumRatesRoom {

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "_id")
    private Long id;

    @ColumnInfo(name = "PREMIUMRATEID")
    private int premiumRateId;

    @ColumnInfo(name = "PRODUCTID")
    private int productId;

    @ColumnInfo(name =  "LAAGE")
    private int laAge;

    @ColumnInfo(name =  "SPOUSEAGE")
    private int spouseAge;

    @ColumnInfo(name =  "PT")
    private int PT;

    @ColumnInfo(name =  "PPT")
    private int PPT;

    @ColumnInfo(name =  "OPTIONID")
    private int optionId;

    @ColumnInfo(name =  "GENDER")
    private String gender;

    @ColumnInfo(name =  "SALOWER")
    private Double saLower;

    @ColumnInfo(name =  "SAUPPER")
    private Double saUpper;

    @ColumnInfo(name =  "SMOKING")
    private int smoking;

    @ColumnInfo(name =  "FIELD1")
    private String field1;

    @ColumnInfo(name =  "RATE")
    private Double rate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }


    public int getPT() {
        return PT;
    }

    public void setPT(int PT) {
        this.PT = PT;
    }

    public int getPPT() {
        return PPT;
    }

    public void setPPT(int PPT) {
        this.PPT = PPT;
    }

    public int getPremiumRateId() {
        return premiumRateId;
    }

    public void setPremiumRateId(int premiumRateId) {
        this.premiumRateId = premiumRateId;
    }

    public int getSpouseAge() {
        return spouseAge;
    }

    public void setSpouseAge(int spouseAge) {
        this.spouseAge = spouseAge;
    }

    public int getOptionId() {
        return optionId;
    }

    public void setOptionId(int optionId) {
        this.optionId = optionId;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Double getSaLower() {
        return saLower;
    }

    public void setSaLower(Double saLower) {
        this.saLower = saLower;
    }

    public Double getSaUpper() {
        return saUpper;
    }

    public void setSaUpper(Double saUpper) {
        this.saUpper = saUpper;
    }

    public int getSmoking() {
        return smoking;
    }

    public void setSmoking(int smoking) {
        this.smoking = smoking;
    }

    public String getField1() {
        return field1;
    }

    public void setField1(String field1) {
        this.field1 = field1;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public int getLaAge() {
        return laAge;
    }

    public void setLaAge(int laAge) {
        this.laAge = laAge;
    }
}

package nvest.com.nvestlibrary.nvestWebModel;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import nvest.com.nvestlibrary.commonMethod.CommonMethod;

/**
 * Created by Soumen on 6/12/2017.
 */

public class SelectedProductDetails implements Serializable {

    String productName;
    int productId;
    String firstName;
    String lastName;
    String dob;
    String dobdate;
    String dobextradate;



    String gender;
    String smoking;
    String firstExtraName;
    String lastExtraName;
    String dobExtra;
    String genderExtra;
    String smokingExtra;
    String email;
    String mobileNumber;
    String state;
    String city;
    String branch;
    String channelId;
    String channelValue;
    String transIdValue;
    String transIdKey;
    ValidateInputList validateInput;
    ValidateRiderList validateIRider;

    String options;
    String optionsValues;
    String annualPrem;
    String modalPrem;
    String SumAssured;
    String SAMF;
    String EMR;
    String flatExtra;


    public String getSAMF() {
        return SAMF;
    }

    public void setSAMF(String SAMF) {
        this.SAMF = SAMF;
    }

    public String getEMR() {
        return EMR;
    }

    public void setEMR(String EMR) {
        this.EMR = EMR;
    }

    public String getFlatExtra() {
        return flatExtra;
    }

    public void setFlatExtra(String flatExtra) {
        this.flatExtra = flatExtra;
    }

    public String getOptions() {
        return options;
    }

    public void setOptions(String options) {
        this.options = options;
    }

    public String getOptionsValues() {
        return optionsValues;
    }

    public void setOptionsValues(String optionsValues) {
        this.optionsValues = optionsValues;
    }

    public String getAnnualPrem() {
        return annualPrem;
    }

    public void setAnnualPrem(String annualPrem) {
        this.annualPrem = annualPrem;
    }

    public String getModalPrem() {
        return modalPrem;
    }

    public void setModalPrem(String modalPrem) {
        this.modalPrem = modalPrem;
    }

    public String getSumAssured() {
        return SumAssured;
    }

    public void setSumAssured(String SumAssured) {
        this.SumAssured = SumAssured;
    }


    public ValidateInputList getValidateInput() {
        return validateInput;
    }

    public void setValidateInput(ValidateInputList validateInput) {
        this.validateInput = validateInput;
    }

    public ValidateRiderList getValidateRider() {
        return validateIRider;
    }

    public void setValidateRider(ValidateRiderList validateIRider) {
        this.validateIRider = validateIRider;
    }

    public HashMap<String,String> dynamicValuesId = new HashMap<>();


    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getChannelValue() {
        return channelValue;
    }

    public void setChannelValue(String channelValue) {
        this.channelValue = channelValue;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        String age = String.valueOf(convertToAge(dob, "dd/MMM/yyyy"));
        this.dob = age;
    }


    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getSmoking() {
        return smoking;
    }

    public void setSmoking(String smoking) {
        this.smoking = smoking;
    }

    public String getFirstExtraName() {
        return firstExtraName;
    }

    public void setFirstExtraName(String firstExtraName) {
        this.firstExtraName = firstExtraName;
    }

    public String getLastExtraName() {
        return lastExtraName;
    }

    public void setLastExtraName(String lastExtraName) {
        this.lastExtraName = lastExtraName;
    }

    public String getDobExtra() {
        return dobExtra;
    }

    public void setDobExtra(String dobExtra) {
        String age = String.valueOf(convertToAge(dobExtra, "dd/MMM/yyyy"));
        this.dobExtra = age;
    }

    public String getGenderExtra() {
        return genderExtra;
    }

    public void setGenderExtra(String genderExtra) {
        String gender = "";
        if (genderExtra == null) {
            gender = "";
        }
        else if (genderExtra.equalsIgnoreCase("male")) {
            gender = "M";
        }
        else if (genderExtra.equalsIgnoreCase("female")) {
            gender = "F";
        }
        this.genderExtra = gender;
    }

    public String getSmokingExtra() {
        return smokingExtra;
    }

    public void setSmokingExtra(String smokingExtra) {
        String smoking = "";
        if (smokingExtra == null) {
            smoking = "N";
        }
        else if (smokingExtra.equalsIgnoreCase("yes")) {
            smoking = "Y";
        }
        else if (smokingExtra.equalsIgnoreCase("no")) {
            smoking = "N";
        }
        this.smokingExtra = smoking;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getTransIdKey() {
        return transIdKey;
    }

    public void setTransIdKey(String transIdKey) {
        this.transIdKey = transIdKey;
    }

    public String getTransIdValue() {
        return transIdValue;
    }

    public void setTransIdValue(String transIdValue) {
        this.transIdValue = transIdValue;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    /*public int convertToAge(String date) {
        SimpleDateFormat sdf =  new SimpleDateFormat("dd/MM/yyyy");
        Date today = new Date(); //Get system date
        //Convert a String to Date
        Date dob = new Date();
        try {
            dob = sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return CommonMethod.getDiffYears(dob,today);

    }*/

    public int convertToAge(String date,String pattern) {
        Date today = null; //Get system date
        //Convert a String to Date
        Date dob = null;
        try {
            SimpleDateFormat sdf =  new SimpleDateFormat(pattern);
            today = new Date(); //Get system date
            //Convert a String to Date
            dob = new Date();
            dob = sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return CommonMethod.getDiffYears(dob,today);

    }




    public String getDobdate() {
        return dobdate;
    }

    public void setDobdate(String dobdate) {
        this.dobdate = dobdate;
    }
    public String getDobextradate() {
        return dobextradate;
    }

    public void setDobextradate(String dobextradate) {
        this.dobextradate = dobextradate;
    }
}

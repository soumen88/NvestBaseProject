package nvest.com.nvestlibrary.nvestWebModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import nvest.com.nvestlibrary.commonMethod.CommonMethod;
import nvest.com.nvestlibrary.commonMethod.GenericDTO;
import nvest.com.nvestlibrary.commonMethod.NvestLibraryConfig;

public class DynamicParams implements Comparable<DynamicParams>{

    @SerializedName("id")
    @Expose
    private Integer id = GenericDTO.getResourceId();

    @SerializedName("fieldKey")
    @Expose
    private String fieldKey;
    @SerializedName("fieldValue")
    @Expose
    private String fieldValue;

    @SerializedName("fileName")
    @Expose
    private String fileName;

    @SerializedName("fieldType")
    @Expose
    private String fieldType;

    @SerializedName("method")
    @Expose
    private String method;

    public DynamicParams(String fieldKey, String fieldValue, String fileName, String fieldType, String method) {
        this.fieldKey = fieldKey;
        this.fieldValue = fieldValue;
        this.fileName = fileName;
        this.fieldType = fieldType;
        this.method = method;
    }

    public String getFieldKey() {
        return fieldKey;
    }

    public void setFieldKey(String fieldKey) {
        this.fieldKey = fieldKey;
    }

    public String getFieldValue() {
        return fieldValue;
    }

    public void setFieldValue(String fieldValue) {
        this.fieldValue = fieldValue;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Integer getId() {
        return id;
    }


    @Override
    public int compareTo(DynamicParams dynamicParams) {
        return (this.id - dynamicParams.getId());
    }
}

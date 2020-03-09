package nvest.com.nvestlibrary.nvestWebModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class ChangeProductDetailsModel {
    @SerializedName("Deleted")
    @Expose
    private ArrayList<DeletedProductDetails> deleted = null;
    @SerializedName("IsMasterModified")
    @Expose
    private Boolean isMasterModified;
    @SerializedName("LastUpdate")
    @Expose
    private String lastUpdate;
    @SerializedName("Modified")
    @Expose
    private ArrayList<ModifiedProductDetailsModel> modified = null;
    @SerializedName("Response")
    @Expose
    private String response;
    @SerializedName("Status")
    @Expose
    private Boolean status;

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("MasterId")
    @Expose
    private int masterid;


    @SerializedName("ProductId")
    @Expose
    private int productid;

    @SerializedName("FileTyoe")
    @Expose
    private int filetype;


    @SerializedName("Download")
    @Expose
    private String download;


    @SerializedName("Executed")
    @Expose
    private String executed;


    public ArrayList<DeletedProductDetails> getDeleted() {
        return deleted;
    }

    public void setDeleted(ArrayList<DeletedProductDetails> deleted) {
        this.deleted = deleted;
    }

    public Boolean getMasterModified() {
        return isMasterModified;
    }

    public void setMasterModified(Boolean masterModified) {
        isMasterModified = masterModified;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public ArrayList<ModifiedProductDetailsModel> getModified() {
        return modified;
    }

    public void setModified(ArrayList<ModifiedProductDetailsModel> modified) {
        this.modified = modified;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMasterid() {
        return masterid;
    }

    public void setMasterid(int masterid) {
        this.masterid = masterid;
    }

    public int getProductid() {
        return productid;
    }

    public void setProductid(int productid) {
        this.productid = productid;
    }

    public int getFiletype() {
        return filetype;
    }

    public void setFiletype(int filetype) {
        this.filetype = filetype;
    }

    public String getDownload() {
        return download;
    }

    public void setDownload(String download) {
        this.download = download;
    }

    public String getExecuted() {
        return executed;
    }

    public void setExecuted(String executed) {
        this.executed = executed;
    }
}

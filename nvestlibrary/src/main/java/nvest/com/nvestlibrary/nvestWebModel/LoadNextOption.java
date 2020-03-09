package nvest.com.nvestlibrary.nvestWebModel;

import java.util.ArrayList;
import java.util.List;

public class LoadNextOption {
    private int optionlevel;
    private int productId;
    private String parentIdList;
    private String pt;
    private String  ppt;
    private String sender;
    private String changelevel;

    public int getOptionlevel() {
        return optionlevel;
    }

    public void setOptionlevel(int optionlevel) {
        this.optionlevel = optionlevel;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getPt() {
        return pt;
    }

    public void setPt(String pt) {
        this.pt = pt;
    }

    public String getPpt() {
        return ppt;
    }

    public void setPpt(String ppt) {
        this.ppt = ppt;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getChangelevel() {
        return changelevel;
    }

    public void setChangelevel(String changelevel) {
        this.changelevel = changelevel;
    }

    public String getParentIdList() {
        return parentIdList;
    }

    public void setParentIdList(String parentIdList) {
        this.parentIdList = parentIdList;
    }
}

package nvest.com.nvestlibrary.nvestWebModel;

import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DynamicCheckBoxModel {


    @SerializedName("dynamicFieldIdentifier")
    @Expose
    private String dynamicFieldIdentifier;

    @SerializedName("checkBox")
    @Expose
    private CheckBox checkBox;

    @SerializedName("isVisible")
    @Expose
    private boolean isVisible = false;


    public String getDynamicFieldIdentifier() {
        return dynamicFieldIdentifier;
    }

    public void setDynamicFieldIdentifier(String dynamicFieldIdentifier) {
        this.dynamicFieldIdentifier = dynamicFieldIdentifier;
    }

    public CheckBox getCheckBox() {
        return checkBox;
    }

    public void setCheckBox(CheckBox checkBox) {
        this.checkBox = checkBox;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }
}

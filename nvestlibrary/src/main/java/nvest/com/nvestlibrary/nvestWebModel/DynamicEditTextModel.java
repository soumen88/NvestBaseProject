package nvest.com.nvestlibrary.nvestWebModel;

import android.widget.EditText;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DynamicEditTextModel {

    @SerializedName("dynamicFieldIdentifier")
    @Expose
    private String dynamicFieldIdentifier;

    @SerializedName("editText")
    @Expose
    private EditText editText;

    @SerializedName("isVisible")
    @Expose
    private boolean isVisible = false;


    public String getDynamicFieldIdentifier() {
        return dynamicFieldIdentifier;
    }

    public void setDynamicFieldIdentifier(String dynamicFieldIdentifier) {
        this.dynamicFieldIdentifier = dynamicFieldIdentifier;
    }

    public EditText getEditText() {
        return editText;
    }

    public void setEditText(EditText editText) {
        this.editText = editText;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }
}

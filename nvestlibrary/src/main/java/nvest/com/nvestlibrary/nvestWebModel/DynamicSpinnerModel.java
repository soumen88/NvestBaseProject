package nvest.com.nvestlibrary.nvestWebModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class DynamicSpinnerModel {

    @SerializedName("dynamicFieldIdentifier")
    @Expose
    private String dynamicFieldIdentifier;

    @SerializedName("materialSpinner")
    @Expose
    private MaterialSpinner materialSpinner;

    @SerializedName("listOfItems")
    @Expose
    //private List<StringKeyValuePair> spinnerItemList = new ArrayList<>();;
    private TreeMap<String, String> spinnerItemList = new TreeMap<>();

    @SerializedName("isVisible")
    @Expose
    private boolean isVisible = false;

    public String getDynamicFieldIdentifier() {
        return dynamicFieldIdentifier;
    }

    public void setDynamicFieldIdentifier(String dynamicFieldIdentifier) {
        this.dynamicFieldIdentifier = dynamicFieldIdentifier;
    }

    public MaterialSpinner getMaterialSpinner() {
        return materialSpinner;
    }

    public void setMaterialSpinner(MaterialSpinner materialSpinner) {
        this.materialSpinner = materialSpinner;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }

    public TreeMap<String, String> getSpinnerItemList() {
        return spinnerItemList;
    }

    public void setSpinnerItemList(List<StringKeyValuePair> spinnerItemList) {
        TreeMap<String, String> temp = getKeyValueTreeMapFromSpinner(spinnerItemList);
        this.spinnerItemList = temp;
    }

    public TreeMap<String, String> getKeyValueTreeMapFromSpinner(List<StringKeyValuePair> inputList){
        TreeMap<String, String> outputTreeMap = new TreeMap<>();
        for (StringKeyValuePair stringKeyValuePair : inputList){
            outputTreeMap.put(stringKeyValuePair.getKey(), stringKeyValuePair.getValue());
        }
        return outputTreeMap;
    }
}

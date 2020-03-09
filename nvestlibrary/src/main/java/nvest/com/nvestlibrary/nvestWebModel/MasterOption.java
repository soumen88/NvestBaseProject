package nvest.com.nvestlibrary.nvestWebModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Soumen on 28/06/17.
 */

public class MasterOption {

    @SerializedName("Level")
    @Expose
    public Integer level;
    @SerializedName("LevelName")
    @Expose
    public String levelName;
    @SerializedName("OptionField")
    @Expose
    public String optionField;
    @SerializedName("InputType")
    @Expose
    public String inputType;
    @SerializedName("Options")
    @Expose
    public Map<Integer, KeyValuePair> options = null;

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public Map<Integer, KeyValuePair> getOptions() {
        return options;
    }

    public void setOptions(Map<Integer, KeyValuePair> options) {
        this.options = options;
    }

    public String getOptionField() {
        return optionField;
    }

    public void setOptionField(String optionField) {
        this.optionField = optionField;
    }

    public String getInputType() {
        return inputType;
    }

    public void setInputType(String inputType) {
        this.inputType = inputType;
    }
}

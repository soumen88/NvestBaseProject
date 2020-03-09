package nvest.com.nvestlibrary.nvestWebModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Soumen on 10/07/17.
 */

public class StringKeyValuePair implements Comparable<StringKeyValuePair> {

    @SerializedName("Key")
    @Expose
    private String key;
    @SerializedName("Value")
    @Expose
    private String value;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void add(String key,String Val){this.key=key;this.value=value;}
    public String toString()
    {
        return value ;
    }


    public StringKeyValuePair() {
    }

    public StringKeyValuePair(String value) {
        this.value = value;
    }

    int extractInt(String s) {
        String num = s.replaceAll("\\D", "");
        // return 0 if no digits found
        return num.isEmpty() ? 0 : Integer.parseInt(num);
    }

    @Override
    public int compareTo(StringKeyValuePair stringKeyValuePair) {
        //return 0;
        return extractInt(value) - extractInt(stringKeyValuePair.getValue());
    }



    @Override
    public boolean equals(Object obj) {
        if (this.value.equals(((StringKeyValuePair)obj).getValue())) {
            return true;
        }
        return false;
    }
}



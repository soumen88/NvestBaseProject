package nvest.com.nvestlibrary.nvestWebModel;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by vasudevr on 6/15/2017.
 */

public class KeyValuePair implements Parcelable {


    @SerializedName("Key")
    @Expose
    private Integer key;
    @SerializedName("Value")
    @Expose
    private String value;


    public KeyValuePair() {

    }

    protected KeyValuePair(Parcel in) {
        if (in.readByte() == 0) {
            key = null;
        } else {
            key = in.readInt();
        }
        value = in.readString();
    }

    public static final Creator<KeyValuePair> CREATOR = new Creator<KeyValuePair>() {
        @Override
        public KeyValuePair createFromParcel(Parcel in) {
            return new KeyValuePair(in);
        }

        @Override
        public KeyValuePair[] newArray(int size) {
            return new KeyValuePair[size];
        }
    };

    public Integer getKey() {
        return key;
    }

    public void setKey(Integer key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (key == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(key);
        }
        dest.writeString(value);
    }
}

package nvest.com.nvestlibrary.nvestWebModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by viren on 09/07/17.
 */

public class AllInputList {

        @SerializedName("DynamicKwList")
        @Expose
        private DynamicKwList dynamicKwList;
        @SerializedName("Funds")
        @Expose
        private List<KeyValuePair> funds = null;
        @SerializedName("Riders")
        @Expose
        private List<KeyValuePair> riders = null;

        public DynamicKwList getDynamicKwList() {
            return dynamicKwList;
        }

        public void setDynamicKwList(DynamicKwList dynamicKwList) {
            this.dynamicKwList = dynamicKwList;
        }

        public List<KeyValuePair> getFunds() {
            return funds;
        }

        public void setFunds(List<KeyValuePair> funds) {
            this.funds = funds;
        }

        public List<KeyValuePair> getRiders() {
            return riders;
        }

        public void setRiders(List<KeyValuePair> riders) {
            this.riders = riders;
        }

        public class DynamicKwList {

            @SerializedName("Keywords")
            @Expose
            private List<Keyword> keywords = null;
            @SerializedName("Values")
            @Expose
            private Values values;

            public List<Keyword> getKeywords() {
                return keywords;
            }

            public void setKeywords(List<Keyword> keywords) {
                this.keywords = keywords;
            }

            public Values getValues() {
                return values;
            }

            public void setValues(Values values) {
                this.values = values;
            }

        }







}

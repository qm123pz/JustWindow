package sb.justwindow.translate;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class TranslationResult {
    @SerializedName("trans_result")
    private List<TransResult> transResult;
    
    @SerializedName("error_code")
    private String errorCode;
    
    @SerializedName("error_msg")
    private String errorMsg;

    public List<TransResult> getTransResult() {
        return transResult;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public static class TransResult {
        @SerializedName("src")
        private String src;
        
        @SerializedName("dst")
        private String dst;

        public String getSrc() {
            return src;
        }

        public String getDst() {
            return dst;
        }
    }
    
    public static TranslationResult parseFromJson(String json) {
        try {
            Gson gson = new Gson();
            return gson.fromJson(json, TranslationResult.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

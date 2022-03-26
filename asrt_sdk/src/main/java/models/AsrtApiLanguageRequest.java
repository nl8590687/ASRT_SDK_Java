package AsrtApiModel;

import com.google.gson.Gson;

public class AsrtApiLanguageRequest {
    private String[] sequence_pinyin;
    public AsrtApiLanguageRequest(String[] sequencePinyin) {
        this.sequence_pinyin = sequencePinyin;
    }

    public String toJson(){
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}

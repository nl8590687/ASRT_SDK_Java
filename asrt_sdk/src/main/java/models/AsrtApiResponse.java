package AsrtApiModel;

import java.util.Map;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class AsrtApiResponse {
    public int statusCode;
    public String statusMessage;
    public Object result;
    public AsrtApiResponse(int statusCode, String statusMessage, Object result){
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
        this.result = result;
    }
    public AsrtApiResponse(){

    }

    public void fromJson(String jsonStr){
        Gson gson = new Gson();
        Map<String, Object> maps = gson.fromJson(jsonStr, new TypeToken<Map<String, Object>>() {
        }.getType());
        for (Map.Entry<String, Object> entry : maps.entrySet()) {
            if(entry.getKey().equals("status_code")){
                this.statusCode = (int)Float.parseFloat(entry.getValue().toString());
            }
            else if(entry.getKey().equals("status_message")){
                this.statusMessage = entry.getValue().toString();
            }
            else if (entry.getKey().equals("result")){
                this.result = (Object)entry.getValue();
            }
        }
    }
}

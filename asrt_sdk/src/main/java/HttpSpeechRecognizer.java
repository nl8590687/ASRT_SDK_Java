
import AsrtApiModel.AsrtApiSpeechRequest;
import AsrtApiModel.AsrtApiLanguageRequest;
import AsrtApiModel.AsrtApiResponse;
import common.Http;

public class HttpSpeechRecognizer extends BaseSpeechRecognizer {
    public String subPath = "";

    public static HttpSpeechRecognizer newHttpSpeechRecognizer(String host, String port,
                                                               String protocol, String subPath){
        HttpSpeechRecognizer httpSpeechRecognizer = new HttpSpeechRecognizer(host, port, protocol);
        httpSpeechRecognizer.subPath = subPath;
        return httpSpeechRecognizer;
    }

    public HttpSpeechRecognizer(String host, String port, String protocol){
        super(host, port, protocol);
        this.subPath = "";
    }

    private String getURL(){
        return protocol + "://" + host + ":" + port + subPath;
    }

    @Override
    public AsrtApiResponse Recognite(byte[] samples, int sampleRate, int channels, int byteWidth){
        AsrtApiSpeechRequest requestBody = new AsrtApiSpeechRequest(samples, sampleRate, channels, byteWidth);
        //System.out.println(requestBody.toJson());
        String rspText = Http.sendPost(this.getURL() + "/all", requestBody.toJson(),
                            "application/json");
        AsrtApiResponse responseBody = new AsrtApiResponse();
        responseBody.fromJson(rspText);
        return responseBody;
    }

    @Override
    public AsrtApiResponse RecogniteSpeech(byte[] samples, int sampleRate, int channels, int byteWidth){
        AsrtApiSpeechRequest requestBody = new AsrtApiSpeechRequest(samples, sampleRate, channels, byteWidth);
        String rspText = Http.sendPost(this.getURL() + "/speech", requestBody.toJson(),
                            "application/json");
        AsrtApiResponse responseBody = new AsrtApiResponse();
        responseBody.fromJson(rspText);
        String tmpStr = responseBody.result.toString();
        tmpStr = tmpStr.substring(1, tmpStr.length()-2);
        responseBody.result = tmpStr;
        return responseBody;
    }

    @Override
    public AsrtApiResponse RecogniteLanguage(String[] sequencePinyin){
        AsrtApiLanguageRequest requestBody = new AsrtApiLanguageRequest(sequencePinyin);
        String rspText = Http.sendPost(this.getURL() + "/language", requestBody.toJson(),
                            "application/json");
        AsrtApiResponse responseBody = new AsrtApiResponse();
        responseBody.fromJson(rspText);
        return responseBody;
    }
}

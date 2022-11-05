package net.ailemon.asrt.sdk;

public class Sdk {
    // GetSpeechRecognizer 获取一个ASRT语音识别类实例化对象
    public static BaseSpeechRecognizer GetSpeechRecognizer(String host, String port, String protocol) {
        if("http".equals(protocol) || "https".equals(protocol)){
            return HttpSpeechRecognizer.newHttpSpeechRecognizer(host, port, protocol, "");
        }
        return null;
    }
}

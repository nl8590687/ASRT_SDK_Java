package net.ailemon.asrt.sdk;

import net.ailemon.asrt.sdk.models.AsrtApiResponse;
import net.ailemon.asrt.sdk.common.Common;
import net.ailemon.asrt.sdk.models.Wave;

public class Sdk {
    // GetSpeechRecognizer 获取一个ASRT语音识别类实例化对象
    public static BaseSpeechRecognizer GetSpeechRecognizer(String host, String port, String protocol) {
        if("http".equals(protocol) || "https".equals(protocol)){
            return HttpSpeechRecognizer.newHttpSpeechRecognizer(host, port, protocol, "");
        }
        return null;
    }

    public static void main(String[] args) {
        String host = "127.0.0.1";
        String port = "20001";
        String protocol = "http";
        BaseSpeechRecognizer sr = Sdk.GetSpeechRecognizer(host, port, protocol);
        String filename = "data.wav";
        if(args.length > 0){
            filename = args[0];
        }
        // ============================================
        // 直接调用ASRT识别语音文件
        AsrtApiResponse rsp = sr.RecogniteFile(filename);
        System.out.println(rsp.statusCode);
        System.out.println(rsp.statusMessage);
        System.out.println(rsp.result);

        // ============================================
        // 调用ASRT识别语音序列
        byte[] wavBytes = Common.readBinFile(filename);
        Wave wav = new Wave();
        wav.deserialize(wavBytes);
        byte[] sampleBytes = wav.getRawSamples();
        int sampleRate = wav.sampleRate;
        int channels = wav.channels;
        int byteWidth = wav.sampleWidth;
        rsp = sr.Recognite(sampleBytes, sampleRate, channels, byteWidth);
        System.out.println(rsp.statusCode);
        System.out.println(rsp.statusMessage);
        System.out.println(rsp.result);

        // ============================================
        // 调用ASRT声学模型识别语音序列
        wavBytes = Common.readBinFile(filename);
        wav = new Wave();
        wav.deserialize(wavBytes);
        sampleBytes = wav.getRawSamples();
        sampleRate = wav.sampleRate;
        channels = wav.channels;
        byteWidth = wav.sampleWidth;
        rsp = sr.RecogniteSpeech(sampleBytes, sampleRate, channels, byteWidth);
        System.out.println(rsp.statusCode);
        System.out.println(rsp.statusMessage);
        System.out.println(rsp.result);

        // ============================================
        // 调用ASRT语言模型识别拼音序列1
        String[] pinyins = ((String)rsp.result).split(", ");
        rsp = sr.RecogniteLanguage(pinyins);
        System.out.println(rsp.statusCode);
        System.out.println(rsp.statusMessage);
        System.out.println(rsp.result);

        // ============================================
        // 调用ASRT语言模型识别拼音序列2
        pinyins = new String[]{"ni3", "hao3", "a1"};
        rsp = sr.RecogniteLanguage(pinyins);
        System.out.println(rsp.statusCode);
        System.out.println(rsp.statusMessage);
        System.out.println(rsp.result);
    }
}

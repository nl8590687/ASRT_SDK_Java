package net.ailemon.asrt.sdk;

import net.ailemon.asrt.sdk.models.AsrtApiResponse;
import net.ailemon.asrt.sdk.common.Common;
import net.ailemon.asrt.sdk.models.Wave;

public class Sdk {
    // GetSpeechRecognizer 鑾峰彇涓�涓狝SRT璇煶璇嗗埆绫诲疄渚嬪寲瀵硅薄
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
        // 鐩存帴璋冪敤ASRT璇嗗埆璇煶鏂囦欢
        AsrtApiResponse rsp = sr.RecogniteFile(filename);
        System.out.println(rsp.statusCode);
        System.out.println(rsp.statusMessage);
        System.out.println(rsp.result);

        // ============================================
        // 璋冪敤ASRT璇嗗埆璇煶搴忓垪
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
        // 璋冪敤ASRT澹板妯″瀷璇嗗埆璇煶搴忓垪
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
        // 璋冪敤ASRT璇█妯″瀷璇嗗埆鎷奸煶搴忓垪1
        String[] pinyins = ((String)rsp.result).split(", ");
        rsp = sr.RecogniteLanguage(pinyins);
        System.out.println(rsp.statusCode);
        System.out.println(rsp.statusMessage);
        System.out.println(rsp.result);

        // ============================================
        // 璋冪敤ASRT璇█妯″瀷璇嗗埆鎷奸煶搴忓垪2
        pinyins = new String[]{"ni3", "hao3", "a1"};
        rsp = sr.RecogniteLanguage(pinyins);
        System.out.println(rsp.statusCode);
        System.out.println(rsp.statusMessage);
        System.out.println(rsp.result);
    }
}

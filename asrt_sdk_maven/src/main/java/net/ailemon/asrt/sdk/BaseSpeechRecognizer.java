package net.ailemon.asrt.sdk;

import net.ailemon.asrt.sdk.models.AsrtApiSpeechRequest;
import net.ailemon.asrt.sdk.models.AsrtApiLanguageRequest;
import net.ailemon.asrt.sdk.models.AsrtApiResponse;
import net.ailemon.asrt.sdk.models.Wave;
import net.ailemon.asrt.sdk.common.Common;

public abstract class BaseSpeechRecognizer {
    protected String host = "";
    protected String port = "";
    protected String protocol = "";
    public BaseSpeechRecognizer(String host, String port, String protocol){
        this.host = host;
        this.port = port;
        this.protocol = protocol;
    }

    public abstract AsrtApiResponse Recognite(byte[] samples, int sampleRate, int channels, int byteWidth);
    public abstract AsrtApiResponse RecogniteSpeech(byte[] samples, int sampleRate, int channels, int byteWidth);
    public abstract AsrtApiResponse RecogniteLanguage(String[] sequencePinyin);

    public AsrtApiResponse RecogniteFile(String filename){
        byte[] wavBytes = Common.readBinFile(filename);
        Wave wav = new Wave();
        wav.deserialize(wavBytes);
        byte[] sampleBytes = wav.getRawSamples();
        int sampleRate = wav.sampleRate;
        int channels = wav.channels;
        int byteWidth = wav.sampleWidth;
        AsrtApiResponse responseBody = this.Recognite(sampleBytes, sampleRate, channels, byteWidth);
        return responseBody;
    }
}

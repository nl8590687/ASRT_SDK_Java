package AsrtApiModel;

import common.Common;

public class AsrtApiSpeechRequest {
    private byte[] samples;
    private int sampleRate;
    private int channels;
    private int byteWidth;
    public AsrtApiSpeechRequest(byte[] samples, int sampleRate, int channels, int byteWidth){
        this.samples = samples;
        this.sampleRate = sampleRate;
        this.channels = channels;
        this.byteWidth = byteWidth;
    }

    public String toJson(){
        String jsonStr = "{\"samples\":\"" + Common.base64Encode(this.samples) + "\"," +
                        "\"sample_rate\":" + String.valueOf(this.sampleRate) + "," +
                        "\"channels\":" + String.valueOf(this.channels) + "," +
                        "\"byte_width\":" + String.valueOf(this.byteWidth) + "}";
        return jsonStr;
    }
}

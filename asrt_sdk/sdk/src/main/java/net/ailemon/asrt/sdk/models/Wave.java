package net.ailemon.asrt.sdk.models;

public class Wave {
    public short[] samples;
    public byte[] sampleBytes;
    public int sampleRate;
    public int channels;
    public int sampleWidth;

    public Wave(){

    }

    public Wave(short[] samples, int sampleRate, int channels, int sampleWidth) {
        this.samples = samples;
        this.sampleRate = sampleRate;
        this.channels = channels;
        this.sampleWidth = sampleWidth;
        // short[] 杞� byte[]
        this.sampleBytes = this.samplesToBytes(samples);
    }

    public Wave(byte[] sampleBytes, int sampleRate, int channels, int sampleWidth) {
        this.sampleBytes = sampleBytes;
        this.sampleRate = sampleRate;
        this.channels = channels;
        this.sampleWidth = sampleWidth;
        // byte[] 杞� short[]
        this.samples = this.bytesToSamples(sampleBytes);
    }

    public boolean deserialize(byte[] wavBytes) {
        try
        {
            byte[] riff = new byte[4];
            byte[] riffSize = new byte[4];
            byte[] waveID = new byte[4];
            byte[] junkID = new byte[4];
            boolean hasjunk = false;
            byte[] junklength = new byte[4];

            byte[] fmtID = new byte[4];
            byte[] cksize = new byte[4];
            int waveType = 0; // 鏃犵鍙穒nt鏁存暟锛屽湪鑾峰彇鏃堕渶瑕佽繘琛屽瓧鑺傝浆鐮� (Byte.toUnsignedInt(byte x))
            byte[] channel = new byte[2];
            byte[] sample_rate = new byte[4];
            byte[] bytespersec = new byte[4];
            byte[] blocklen_sample = new byte[2];
            byte[] bitNum = new byte[2];
            byte[] unknown = new byte[2];
            byte[] dataID = new byte[4];  //52
            byte[] dataLength = new byte[4];  //56 涓瓧鑺�

            int p = 0; //妯℃嫙娴佺殑鎸囬拡浣嶇疆

            System.arraycopy(wavBytes, p, riff, 0, 4); // RIFF
            p += 4;

            if (DataParseUtils.convertFoutUnsignLong(riff[3], riff[2], riff[1], riff[0]) != 0x52494646) //0x52494646
            {
                Exception e = new Exception("璇ユ枃浠朵笉鏄疻AVE鏂囦欢");
                throw e;
            }
            //System.out.println("test");

            /*if (riff[0]!=82 || riff[1]!=73  || riff[2]!=70  || riff[3]!=70) //0x52494646
            {
                Exception e = new Exception("璇ユ枃浠朵笉鏄疻AVE鏂囦欢");
                throw e;
            }*/

            System.arraycopy(wavBytes, p, riffSize, 0, 4); // 鏂囦欢鍓╀綑闀垮害
            p += 4;

            if (DataParseUtils.convertFoutUnsignLong(riffSize[3], riffSize[2], riffSize[1], riffSize[0]) != wavBytes.length - p)
            {
                //Exception e = new Exception("璇AVE鏂囦欢鎹熷潖锛屾枃浠堕暱搴︿笌鏍囪涓嶄竴鑷�");
                //throw e;
            }

            //waveID = bread.ReadBytes(4);
            System.arraycopy(wavBytes, p, waveID, 0, 4);
            p += 4;

            if (DataParseUtils.convertFoutUnsignLong(waveID[3], waveID[2], waveID[1], waveID[0]) != 0x57415645)
            {
                Exception e = new Exception("璇ユ枃浠朵笉鏄疻AVE鏂囦欢");
                throw e;
            }
            //System.out.println("test2");

            byte[] tmp = new byte[4];
            System.arraycopy(wavBytes, p, tmp, 0, 4);
            p += 4;

            if (DataParseUtils.convertFoutUnsignLong(tmp[3], tmp[2], tmp[1], tmp[0]) == 0x4A554E4B)
            {
                //鍖呭惈junk鏍囪鐨剋av
                junkID = tmp;
                hasjunk = true;

                System.arraycopy(wavBytes, p, junklength, 0, 4);
                p += 4;

                //junklength = bread.ReadBytes(4);
                //uint junklen = BitConverter.ToUInt32(junklength, 0);
                long junklen = DataParseUtils.convertFoutUnsignLong(junklength[3], junklength[2], junklength[1], junklength[0]);


                //灏嗕笉瑕佺殑junk閮ㄥ垎璇诲嚭
                //bread.ReadBytes((int)junklen);
                p += (int)junklen;

                //璇籪mt 鏍囪
                //fmtID = bread.ReadBytes(4);
                System.arraycopy(wavBytes, p, fmtID, 0, 4);
                p += 4;
            }
            else if (DataParseUtils.convertFoutUnsignLong(tmp[3], tmp[2], tmp[1], tmp[0]) == 0x666D7420)
            {
                fmtID = tmp;
            }
            else
            {
                Exception e = new Exception("鏃犳硶鎵惧埌WAVE鏂囦欢鐨刯unk鍜宖mt鏍囪");
                throw e;
            }
            //System.out.println("test2.3");


            if (DataParseUtils.convertFoutUnsignLong(fmtID[3], fmtID[2], fmtID[1], fmtID[0]) != 0x666D7420)
            {
                //fmt 鏍囪
                Exception e = new Exception("鏃犳硶鎵惧埌WAVE鏂囦欢fmt鏍囪");
                throw e;
            }
            //System.out.println("test2.4");

            //cksize = bread.ReadBytes(4);
            System.arraycopy(wavBytes, p, cksize, 0, 4);
            p += 4;

            long p_data_start = DataParseUtils.convertFoutUnsignLong(cksize[3], cksize[2], cksize[1], cksize[0]);
            int p_wav_start = (int)p_data_start + 8;
            //System.out.println("test2.5");
            //waveType = bread.ReadUInt16();
            byte[] tmp_waveType = new byte[2];
            System.arraycopy(wavBytes, p, tmp_waveType, 0, 2);
            p += 2;
            waveType = DataParseUtils.convertTwoUnsignInt(tmp_waveType[0], tmp_waveType[1]);

            if (waveType != 1)
            {
                // 闈瀙cm鏍煎紡锛屾殏涓嶆敮鎸�
                Exception e = new Exception("WAVE鏂囦欢涓嶆槸pcm鏍煎紡锛屾殏鏃朵笉鏀寔");
                throw e;
            }

            //澹伴亾鏁�
            //channel = bread.ReadBytes(2);
            System.arraycopy(wavBytes, p, channel, 0, 2);
            p += 2;

            //閲囨牱棰戠巼
            //sample_rate = bread.ReadBytes(4);
            System.arraycopy(wavBytes, p, sample_rate, 0, 4);
            p += 4;

            int fs = (int)DataParseUtils.convertFoutUnsignLong(sample_rate[0], sample_rate[1], sample_rate[2], sample_rate[3]);

            //姣忕閽熷瓧鑺傛暟
            //bytespersec = bread.ReadBytes(4);
            System.arraycopy(wavBytes, p, bytespersec, 0, 4);
            p += 4;

            //姣忔閲囨牱鐨勫瓧鑺傚ぇ灏忥紝2涓哄崟澹伴亾锛�4涓虹珛浣撳０閬�
            //blocklen_sample = bread.ReadBytes(2);
            System.arraycopy(wavBytes, p, blocklen_sample, 0, 2);
            p += 2;

            //姣忎釜澹伴亾鐨勯噰鏍风簿搴︼紝榛樿16bit
            //bitNum = bread.ReadBytes(2);
            System.arraycopy(wavBytes, p, bitNum, 0, 2);
            p += 2;

            //tmp = bread.ReadBytes(2);
            System.arraycopy(wavBytes, p, tmp, 0, 2);
            p += 2;
            //瀵绘壘da鏍囪
            while (DataParseUtils.convertTwoUnsignInt(tmp[1], tmp[0]) != 0x6461)
            {
                //tmp = bread.ReadBytes(2);
                System.arraycopy(wavBytes, p, tmp, 0, 2);
                p += 2;
            }

            //tmp = bread.ReadBytes(2);
            System.arraycopy(wavBytes, p, tmp, 0, 2);
            p += 2;

            if (DataParseUtils.convertTwoUnsignInt(tmp[1], tmp[0]) != 0x7461)
            {
                //ta鏍囪
                Exception e = new Exception("鏃犳硶鎵惧埌WAVE鏂囦欢data鏍囪");
                throw e;
            }

            //wav鏁版嵁byte闀垮害
            byte[] data_size_byte = new byte[4];

            System.arraycopy(wavBytes, p, data_size_byte, 0, 4);
            p += 4;

            long DataSize = DataParseUtils.convertFoutUnsignLong(data_size_byte[0], data_size_byte[1], data_size_byte[2], data_size_byte[3]);
            //System.out.println("test3");
            //璁＄畻鏍锋湰鏁�
            long NumSamples = (long)DataSize / 2;

            if (NumSamples == 0)
            {
                NumSamples = (wavBytes.length - p) / 2;
            }
            //if (BitConverter.ToUInt32(notDefinition, 0) == 18)
            //{
            //    unknown = bread.ReadBytes(2);
            //}
            //dataID = bread.ReadBytes(4);

            short[] data = new short[(int) NumSamples];

            for (int i = 0; i < NumSamples; i++)
            {
                //璇诲叆2瀛楄妭鏈夌鍙锋暣鏁�
                //data[i] = bread.ReadInt16();
                byte[] tmp_sample = new byte[2];
                System.arraycopy(wavBytes, p, tmp_sample, 0, 2);
                p += 2;
                data[i] = (short)DataParseUtils.convertTwoUnsignInt(tmp_sample[0],tmp_sample[1]);
            }
            //System.out.println("test4");
            //s.Dispose();
            //fstream.Close();
            //fstream.Dispose();
            //bread.Dispose();

            //wav wave = new wav();
            this.samples = data;
            this.sampleBytes = this.samplesToBytes(this.samples);
            //wave.wavs = data;
            this.sampleRate = fs;
            //wave.fs = fs;
            this.channels = DataParseUtils.convertTwoUnsignInt(channel[0],channel[1]);
            this.sampleWidth = DataParseUtils.convertTwoUnsignInt(bitNum[0],bitNum[1]) / 8;
            return true;
        }
        catch (Exception ex)
        {
            System.out.println(ex);
            return false;
            //throw ex;
        }
    }

    private byte[] serialize() {
        return null;
    }

    public byte[] getRawSamples() {
        return this.sampleBytes;
    }

    protected byte[] samplesToBytes(short[] samples){
        byte[] sampleBytes = new byte[samples.length * 2];
        for(int i = 0; i < samples.length; i++){
            byte[] sample = DataParseUtils.convertShortToBytes(samples[i], false);
            for(int j = 0; j < 2; j++){
                sampleBytes[2*i+j] = sample[j];
            }
        }
        return sampleBytes;
    }

    protected short[] bytesToSamples(byte[] sampleBytes){
        short[] data = new short[sampleBytes.length / 2];
        for (int i = 0; i < sampleBytes.length / 2; i++)
        {
            //璇诲叆2瀛楄妭鏈夌鍙锋暣鏁�
            //data[i] = bread.ReadInt16();
            byte[] tmp_sample = new byte[2];
            System.arraycopy(sampleBytes, 2 * i, tmp_sample, 0, 2);
            data[i] = (short)DataParseUtils.convertTwoUnsignInt(tmp_sample[0],tmp_sample[1]);
        }
        return data;
    }
}

class DataParseUtils {
    /**
     * 鏈夌鍙凤紝int 鍗� 2 涓瓧鑺�
     */
    public static int convertTwoSignInt(byte b1, byte b2) { // signed
        return (b2 << 8) | (b1 & 0xFF);
    }

    /**
     * 鏈夌鍙�, int 鍗� 4 涓瓧鑺�
     */
    public static int convertFourSignInt(byte b1, byte b2, byte b3, byte b4) {
        return (b4 << 24) | (b3 & 0xFF) << 16 | (b2 & 0xFF) << 8 | (b1 & 0xFF);
    }

    /**
     * 鏃犵鍙凤紝int 鍗� 2 涓瓧鑺�
     */
    public static int convertTwoUnsignInt(byte b1, byte b2)      // unsigned
    {
        return (b2 & 0xFF) << 8 | (b1 & 0xFF);
    }

    /**
     * 鏃犵鍙�, int 鍗� 4 涓瓧鑺�
     */
    public static long convertFoutUnsignLong(byte b1, byte b2, byte b3, byte b4) {
        return (long) (b4 & 0xFF) << 24 | (b3 & 0xFF) << 16 | (b2 & 0xFF) << 8 | (b1 & 0xFF);
    }

    public static byte[] convertShortToBytes(Short shortNumber, boolean big) {
        byte[] bytes = new byte[2];
        bytes[0] = (byte) (shortNumber & 0xff);
        bytes[1] = (byte) (shortNumber >> 8 & 0xff);
        if (big){
            byte tmp = bytes[0];
            bytes[0] = bytes[1];
            bytes[1] = tmp;
        }
        return bytes;
    }
}

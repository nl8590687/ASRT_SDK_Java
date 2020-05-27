package me.ailemon.asrt.webdemo.classes;

public class DataParseUtils {


    /**
     * 有符号，int 占 2 个字节
     */
    public static int convertTwoSignInt(byte b1, byte b2) { // signed
        return (b2 << 8) | (b1 & 0xFF);
    }

    /**
     * 有符号, int 占 4 个字节
     */
    public static int convertFourSignInt(byte b1, byte b2, byte b3, byte b4) {
        return (b4 << 24) | (b3 & 0xFF) << 16 | (b2 & 0xFF) << 8 | (b1 & 0xFF);
    }

    /**
     * 无符号，int 占 2 个字节
     */
    public static int convertTwoUnsignInt(byte b1, byte b2)      // unsigned
    {
        return (b2 & 0xFF) << 8 | (b1 & 0xFF);
    }

    /**
     * 无符号, int 占 4 个字节
     */
    public static long convertFoutUnsignLong(byte b1, byte b2, byte b3, byte b4) {
        return (long) (b4 & 0xFF) << 24 | (b3 & 0xFF) << 16 | (b2 & 0xFF) << 8 | (b1 & 0xFF);
    }
}

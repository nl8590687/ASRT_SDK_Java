package me.ailemon.asrt.webdemo.classes;

import java.io.UnsupportedEncodingException;
import java.util.Base64;
//import java.util.UUID;

public class ApiUtils {
	private static final String UTF_8 = "utf-8";
	
	public static byte[] Convert_Base64ToBytes(String str_base64) {
		try {
			byte[] decode = Base64.getDecoder().decode(str_base64.getBytes(UTF_8));
			return decode;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static String Convert_BytesToBase64(byte[] BytesData) {
		String basicEncoded = Base64.getEncoder().encodeToString(BytesData);
		//String urlEncoded = Base64.getUrlEncoder().encodeToString(ordinal.getBytes(UTF_8));
		//URL°²È«µÄbase64±àÂëÆ÷
		return basicEncoded;
	}
	
	public static String SendToASRTServer(String token, int fs, short[] IntsData) {
		String url = "http://127.0.0.1:20000/";
		String _fs = String.valueOf(fs);
		String postData =  "token=" + token + "&fs=" + _fs;
		
		StringBuffer buf=new StringBuffer();
		buf.append(postData);
		
		for(int i=0;i<IntsData.length;i++) {
			buf.append("&wavs=" + String.valueOf(IntsData[i]));
		}
		postData = buf.toString();
		//System.out.println("postData: \n" + postData);
		String s1 = HttpRequest.sendPost(url, postData);
		//System.out.println("s1: \n" + s1);
		return s1;
	}

}

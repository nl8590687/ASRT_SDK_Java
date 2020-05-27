package me.ailemon.asrt.webdemo.classes;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;


/**
 * java发送http的get和post请求
 */
public class HttpRequest {
	/**
	 * 向指定URL发送GET方式的请求
	 * @param url  发送请求的URL
	 * @param param 请求参数   
	 * @return URL 代表远程资源的响应
	 */
	public static String sendGet(String url, String param){
		String result = "";
		String urlName = url + "?" + param;
		try{
			URL realUrl = new URL(urlName);
			//打开和URL之间的连接
			URLConnection conn = realUrl.openConnection();
			//设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			//建立实际的连接
			conn.connect();
			//获取所有的响应头字段
			Map<String,List<String>> map = conn.getHeaderFields();
			//遍历所有的响应头字段
			for (String key : map.keySet()) {
				System.out.println(key + "-->" + map.get(key));
			}
			// 定义 BufferedReader输入流来读取URL的响应
			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
		} catch (Exception e) {
			System.out.println("发送GET请求出现异常" + e);
			e.printStackTrace();
		}
		return result;
		
		
	}
	
	/**
	 * 向指定URL发送POST方式的请求
	 * @param url  发送请求的URL
	 * @param param 请求参数  
	 * @return URL 代表远程资源的响应
	 */
	public static String sendPost(String url, String param){
		String result = "";
		try{
			URL realUrl = new URL(url);
			//打开和URL之间的连接
			URLConnection conn =  realUrl.openConnection();
			//设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			//发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			//获取URLConnection对象对应的输出流
			PrintWriter out = new PrintWriter(conn.getOutputStream());
			//发送请求参数
			out.print(param);
			//flush输出流的缓冲
			out.flush();
			// 定义 BufferedReader输入流来读取URL的响应
			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
            String line;
            if((line = in.readLine()) != null) {
            	result += line;
            }
            while ((line = in.readLine()) != null) {
                result += "\n" + line;
            }
		} catch (Exception e) {
			System.out.println("发送POST请求出现异常" + e);
			e.printStackTrace();
		}
		return result;
	}
	
	//测试发送GET和POST请求
	//public static void main(String[] args) throws Exception{
        //发送GET请求
    //    String s = HttpRequest.sendGet("http://127.0.0.1:8080/index",null);
    //    System.out.println(s);
        //发送POST请求
    //    String s1 = HttpRequest.sendPost("http://localhost:8080/addComment", "questionId=1&content=美丽人生");
    //    System.out.println(s1);
    //}
}

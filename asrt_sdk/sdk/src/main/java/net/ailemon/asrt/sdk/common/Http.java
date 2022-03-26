package net.ailemon.asrt.sdk.common;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * java鍙戦�乭ttp鐨刧et鍜宲ost璇锋眰
 */
public class Http {
    /**
     * 鍚戞寚瀹歎RL鍙戦�丟ET鏂瑰紡鐨勮姹�
     * @param url  鍙戦�佽姹傜殑URL
     * @param param 璇锋眰鍙傛暟
     * @return URL 浠ｈ〃杩滅▼璧勬簮鐨勫搷搴�
     */
    public static String sendGet(String url, String param){
        String result = "";
        String urlName = url + "?" + param;
        try{
            URL realUrl = new URL(urlName);
            //鎵撳紑鍜孶RL涔嬮棿鐨勮繛鎺�
            URLConnection conn = realUrl.openConnection();
            //璁剧疆閫氱敤鐨勮姹傚睘鎬�
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            //寤虹珛瀹為檯鐨勮繛鎺�
            conn.connect();
            //鑾峰彇鎵�鏈夌殑鍝嶅簲澶村瓧娈�
            Map<String,List<String>> map = conn.getHeaderFields();
            //閬嶅巻鎵�鏈夌殑鍝嶅簲澶村瓧娈�
            for (String key : map.keySet()) {
                System.out.println(key + "-->" + map.get(key));
            }
            // 瀹氫箟 BufferedReader杈撳叆娴佹潵璇诲彇URL鐨勫搷搴�
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("鍙戦�丟ET璇锋眰鍑虹幇寮傚父" + e);
            e.printStackTrace();
        }
        return result;


    }

    /**
     * 鍚戞寚瀹歎RL鍙戦�丳OST鏂瑰紡鐨勮姹�
     * @param url  鍙戦�佽姹傜殑URL
     * @param param 璇锋眰鍙傛暟
     * @return URL 浠ｈ〃杩滅▼璧勬簮鐨勫搷搴�
     */
    public static String sendPost(String url, String param, String contentType){
        String result = "";
        try{
            URL realUrl = new URL(url);
            //鎵撳紑鍜孶RL涔嬮棿鐨勮繛鎺�
            URLConnection conn =  realUrl.openConnection();
            //璁剧疆閫氱敤鐨勮姹傚睘鎬�
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setRequestProperty("content-type", contentType); // "application/json"
            //鍙戦�丳OST璇锋眰蹇呴』璁剧疆濡備笅涓よ
            conn.setDoOutput(true);
            conn.setDoInput(true);
            //鑾峰彇URLConnection瀵硅薄瀵瑰簲鐨勮緭鍑烘祦
            PrintWriter out = new PrintWriter(conn.getOutputStream());
            //鍙戦�佽姹傚弬鏁�
            out.print(param);
            //flush杈撳嚭娴佺殑缂撳啿
            out.flush();
            // 瀹氫箟 BufferedReader杈撳叆娴佹潵璇诲彇URL鐨勫搷搴�
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
            String line;
            if((line = in.readLine()) != null) {
                result += line;
            }
            while ((line = in.readLine()) != null) {
                result += "\n" + line;
            }
        } catch (Exception e) {
            System.out.println("鍙戦�丳OST璇锋眰鍑虹幇寮傚父" + e);
            e.printStackTrace();
        }
        return result;
    }

    //娴嬭瘯鍙戦�丟ET鍜孭OST璇锋眰
    //public static void main(String[] args) throws Exception{
    //鍙戦�丟ET璇锋眰
    //    String s = HttpRequest.sendGet("http://127.0.0.1:8080/index",null);
    //    System.out.println(s);
    //鍙戦�丳OST璇锋眰
    //    String s1 = HttpRequest.sendPost("http://localhost:8080/addComment", "questionId=1&content=缇庝附浜虹敓");
    //    System.out.println(s1);
    //}
}

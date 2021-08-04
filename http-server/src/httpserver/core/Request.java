package core;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 响应客户端的Socket
 */
public class Request {
    private final static String HTTP_METHOD_POST = "POST";
    private final static String HTTP_METHOD_GET = "GET";
    private String requestInfo;
    private String method;
    private String httpVersion;
    private String url;
    private Map<String, List<String>> parameterMap = null;
    private boolean isRequest = false;
    private String requestBody;

    public Request(InputStream is){
        // 解析客户端中获取发送过来的请求
        try {
            byte[] request = new byte[1024*1024*1024];
            int len = is.read(request);
            if (len >= 0){
                // 1、解析请求行
                requestInfo = new String(request,0,len);
                System.out.println("Receive Request: ");
                System.out.println(requestInfo);
                String[] requestLines = requestInfo.substring(0, requestInfo.indexOf("\r\n")).split(" ");
                if (requestLines.length == 3){
                    this.method = requestLines[0];
                    // 这里的URL可能还带有参数
                    this.url = requestLines[1];
                    this.httpVersion = requestLines[2];
                } else {
                    System.out.println("com.httpserver.core.Request Format Error...");
                    return;
                }
                // 2. 解析HTTP请求中的方法参数
                resolveParameter();
                this.isRequest = true;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 解析HTTP请求
     * @param client
     */
    public Request(Socket client) throws IOException {
        this(client.getInputStream());
    }

    /**
     * 将参数放到MapKV
     * @param key
     * @param value
     */
    private void setParameterMapKY(String key, String value){
        List<String> values;
        if (null == this.parameterMap){
            this.parameterMap = new HashMap<String, List<String>>();
        }
        if (this.parameterMap.containsKey(key)){
            values = this.parameterMap.get(key);
        }else {
            values = new ArrayList<String>();
        }
        values.add(value);
        this.parameterMap.put(key,values);
    }

    private void resolveParameter(){
        String[] urls = null;
        // 判断URL里面是否有参数，若有参数，则带有?key1=value1&key2=value2
        if (this.url.contains("?")){
            urls = this.url.split("\\?");
            // 获取真实的URL
            this.url = urls[0];
            // 获取URL末尾的参数
            if (urls[1].length() > 0){
                String[] urlParameter = urls[1].split("&");
                for (String kv : urlParameter){
                    // kv --> key=value
                    String[] kvArray = kv.split("=");
                    String key = kvArray[0];
                    // 如果 value为空
                    String value = kvArray[1].length()>0?kvArray[1]:"";
                    setParameterMapKY(key,value);
                }
            }

        }
        // 2、解析请求体
        if (HTTP_METHOD_POST.equals(method)){
            requestBody = requestInfo.substring(requestInfo.indexOf("\r\n\r\n"));

        }
    }


    public String getRequestInfo() {
        return requestInfo;
    }

    public String getMethod() {
        return method;
    }

    public String getHttpVersion() {
        return httpVersion;
    }

    public String getUrl() {
        return url;
    }

    public Map<String, List<String>> getParameterMap() {
        return parameterMap;
    }

    public boolean isRequest() {
        return isRequest;
    }
}

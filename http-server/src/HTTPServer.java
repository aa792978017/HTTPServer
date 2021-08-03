/*
 * Copyright 2019-2022 the original author “WangChang”.
 */

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * . HTTP Server
 *  提供HTTP服务
 * @author Wangchang
 * @since 2021-07-31-8:52
 */
public class HTTPServer {
  private ServerSocket serverSocket;
  private Socket client;
  private int port;
  private static WebContext webContext;

  public HTTPServer(int port) {
    try {
      this.port = port;
      this.serverSocket = new ServerSocket(port);
      this.buildWebContext();
    } catch (IOException e) {
      System.out.println("Create HTTP Server Error...");
    }

  }

  /**
   * 开始服务
   */
  private void start(){
    try {
      while (true){
        client = serverSocket.accept();
        recvice(client);
      }


    } catch (IOException e) {
      System.out.println("Client Connect Error...");
    }

  }

  /**
   * 接受请求，响应一次请求后，断开链接
   * @return
   */
  public void recvice(Socket client){

    InputStream is;
    Class servletClz = null;
    try {
      // 获取URL在web上下文中对应的servlet类名
      String servletName = webContext.getClz("/reg");
      // 根据URL反射获取处理的servlet
      servletClz = Class.forName(servletName);
      Servlet servlet = (Servlet)servletClz.getConstructor().newInstance();
//      servlet.service();
      closeClient(client);
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    } catch (InvocationTargetException e) {
      e.printStackTrace();
    } catch (InstantiationException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } catch (NoSuchMethodException e) {
      e.printStackTrace();
    } catch (Exception e){
      e.printStackTrace();
    }
  }

  public void closeClient(Socket client){
    try {
      client.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * 关闭HTTP服务器
   */
  public void close(){

  }

  public void buildWebContext(){
    //SAX解析
    // 1、获取SAX工厂
    SAXParserFactory factory = SAXParserFactory.newInstance();
    // 2、从工厂获取解析器
    SAXParser parser = null;
    try {
      parser = factory.newSAXParser();
      // 3、加载文档Document注册处理器
      // 编写处理器
      WebHandler handler = new WebHandler();
      // 4、解析
      parser.parse(Thread.currentThread().getContextClassLoader()
              .getResourceAsStream("web.xml"), handler);
      // 5、构建web上下文
      webContext = new WebContext(handler.getEntitys(),handler.getMappings());
    } catch (ParserConfigurationException e) {
      e.printStackTrace();
    } catch (SAXException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }

  }


  public static void main(String[] args) throws Exception {
    HTTPServer httpServer = new HTTPServer(8088);
    httpServer.start();

  }

}

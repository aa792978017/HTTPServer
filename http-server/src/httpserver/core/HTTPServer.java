package core;/*
 * Copyright 2019-2022 the original author “WangChang”.
 */

import java.io.IOException;
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
  private boolean isRunning = false;


  public HTTPServer(int port) {
    try {
      this.port = port;
      this.serverSocket = new ServerSocket(port);
    } catch (IOException e) {
      System.out.println("Create HTTP Server Error...");
    }

  }

  /**
   * 开始服务
   */
  private void start(){
    isRunning = true;
    System.out.println("HTTP Server start...");
    receive();
  }

  /**
   * 接受请求，响应一次请求后，断开链接
   * @return
   */
  private void receive(){
    try{
      while (isRunning){
        client = serverSocket.accept();
        // 使用分发器处理
        new Thread(new Dispatcher(client)).start();
      }
    }catch (Exception e){
      System.out.println("Receive client error...");
    }
  }

  /**
   * 关闭HTTP服务器
   */
  public void stop(){
      isRunning = false;
    try {
      this.serverSocket.close();
      System.out.println("HTTP Server closed...");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) throws Exception {
    HTTPServer httpServer = new HTTPServer(8088);
    httpServer.start();

  }

}

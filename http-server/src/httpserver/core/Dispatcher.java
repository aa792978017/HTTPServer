package core;/*
 * Copyright 2019-2022 the original author “WangChang”.
 */

import core.Servlet;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import org.w3c.dom.ls.LSOutput;

/**
 * . 客户端分发器
 *
 * @author Wangchang
 * @since 2021-08-04-22:10
 */
public class Dispatcher implements Runnable {
  private Socket client;
  private Request request;
  private Response response;

  public Dispatcher(Socket client) {
    this.client = client;
    try {
      // 获取HTTP请求报文，获取响应报文
      request = new Request(client);
      response = new Response(client);
    } catch (IOException e) {
      this.release();
      e.printStackTrace();
    }
  }

  /**
   * 释放资源
   */
  private void release() {
    try {
      this.client.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void run() {
    long begin = System.currentTimeMillis();
    try{
      if (!request.isRequest()){
        release();
        return;
      }
      // url不存在，返回首页
      if (null == request.getUrl() || request.getUrl().equals("/")){
        String html = readHTMLResource("index.html");
        response.print(html);
        response.pushToClient(200);
        return;
      }
      Servlet servlet = WebApp.getServletFromUrl(request.getUrl());
      if (null != servlet){
        servlet.service(request,response);
      }else {
        response.print(readHTMLResource("error.html"));
        response.pushToClient(404);
      }

    }catch (Exception e){
      try {
        response.print("Server error...");
        response.pushToClient(500);
      } catch (IOException ex) {
        ex.printStackTrace();
      }
    }finally {
      release();
      long end = System.currentTimeMillis();
      System.out.println("time: " + (end - begin));
    }

  }

  /**
   * 读取HTML静态页面
   * @param path
   * @return
   */
  private String readHTMLResource(String path) throws IOException {
    InputStream is = Thread.currentThread()
        .getContextClassLoader()
        .getResourceAsStream(path);
    byte[] HTML = new byte[1024*1024];
    if (is != null) {
      is.read(HTML);
    }
    is.close();
    return new String(HTML);
  }
}

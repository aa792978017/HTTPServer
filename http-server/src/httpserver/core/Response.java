package core;
/*
 * Copyright 2019-2022 the original author “WangChang”.
 */

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Date;

/**
 * . HTTP响应体
 *
 * @author Wangchang
 * @since 2021-08-04-8:21
 */
public class Response {

  private final static String HTTP_VERSION = "HTTP1.1";
  private final static String CRLF = "\r\n";
  private final static String BLANK = " ";
  // 响应体
  private StringBuilder content;
  // 响应头
  private StringBuilder header;
  // 正文长度,单位（字节）
  private int len;
  // TCP连接写缓冲区
  private BufferedWriter bw;

  private int status = 200;
  private String server = "openresty";

  public Response() {
    // 初始化响应报文
    content = new StringBuilder();
    header = new StringBuilder();
    len = 0;
  }

  public Response(Socket client) throws IOException {
    this(client.getOutputStream());
  }

  public Response(OutputStream os){
    this();
    bw = new BufferedWriter(new OutputStreamWriter(os));
  }

  /**
   * 为响应体动态添加内容
   * @param info
   * @return
   */
  public Response print(String info){
    content.append(info);
    len += info.getBytes().length;
    return this;
  }

  /**
   * 为响应体动态添加内容
   * @param info
   * @return
   */
  public Response println(String info){
    content.append(info).append(CRLF);
    len += (info+CRLF).getBytes().length;
    return this;
  }

  public void pushToClient(int code) throws IOException {
    // 根据状态码创建响应头
    createHeader(code);
    // 发送HTTP响应报文
    bw.append(header);
    bw.append(content);
    bw.flush();

  }

  private void createHeader(int code){
    // 1、响应行：HTTP/1.1 200 OK
    header.append(HTTP_VERSION).append(BLANK);
    header.append(code).append(BLANK);
    switch (code){
      case 200:
        header.append("OK").append(CRLF);
        break;
      case 404:
        header.append("NOT FOUND").append(CRLF);
        break;
      case 505:
        header.append("SERVER ERROR").append(CRLF);
        break;
    }
    // 2、响应头(最后一行存在空行)
    header.append("Date:").append(new Date()).append(CRLF);
    header.append("Server:").append(server).append(CRLF);
    header.append("Content-type:text/html").append(CRLF);
    header.append("Content-length:").append(len).append(CRLF);
    header.append(CRLF);

  }


}

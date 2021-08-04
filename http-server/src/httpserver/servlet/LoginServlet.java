/*
 * Copyright 2019-2022 the original author “WangChang”.
 */
package servlet;
import core.Request;
import core.Response;
import core.Servlet;
import java.io.IOException;

/**
 * .
 *
 * @author Wangchang
 * @since 2021-07-30-23:06
 */
public class LoginServlet implements Servlet {


  public void service(Request request, Response response) {
    try {
      response.print("hello world!");
      response.pushToClient(200);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}

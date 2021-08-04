package core;
/*
 * Copyright 2019-2022 the original author or authors.
 */

/**
 * . servlet接口
 *
 * @author Wangchang
 * @since 2021-07-30-23:05
 */
public interface Servlet {

  /**
   * 响应url提供服务
   */
  public void service(Request request, Response response);
}

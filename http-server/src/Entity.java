/*
 * Copyright 2019-2022 the original author “WangChang”.
 */

/**
 * .
 *  对应servlet标签
 *   <servlet>
 *     <servlet-name>login</servlet-name>
 *     <servlet-class>com.sxt.server.basic.servlet.LoginServlet</servlet-class>
 *   </servlet>
 * @author Wangchang
 * @since 2021-07-30-22:37
 */
public class Entity {
  private String name;
  private String clz;

  public Entity(){

  }

  public Entity(String name, String clz) {
    this.name = name;
    this.clz = clz;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getClz() {
    return clz;
  }

  public void setClz(String clz) {
    this.clz = clz;
  }
}

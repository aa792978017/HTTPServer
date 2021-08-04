/*
 * Copyright 2019-2022 the original author “WangChang”.
 */
package core;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.SAXException;

/**
 * .
 *  Web 程序
 * @author Wangchang
 * @since 2021-08-04-22:24
 */
public class WebApp {
  private static WebContext webContext;
  static {
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

  public static Servlet getServletFromUrl(String url){
    // 获取URL在web上下文中对应的servlet类名
    String servletName = webContext.getClz(url);
    if (servletName == null){
      return null;
    }else {
      Class servletClz;
      // 根据URL反射获取处理的servlet
      try {
        servletClz = Class.forName(servletName);
        Servlet servlet = (Servlet)servletClz.getConstructor().newInstance();
        return servlet;
      } catch (ClassNotFoundException e) {
        e.printStackTrace();
      } catch (InstantiationException e) {
        e.printStackTrace();
      } catch (InvocationTargetException e) {
        e.printStackTrace();
      } catch (NoSuchMethodException e) {
        e.printStackTrace();
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      }
      return null;
    }
  }

}

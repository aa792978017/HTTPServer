/*
 * Copyright 2019-2022 the original author “WangChang”.
 */

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * . Web.xml解析类
 *
 * @author Wangchang
 * @since 2021-07-30-8:40
 */
public class XmlReader {

  public static void main(String[] args)
      throws Exception {
    //SAX解析
    // 1、获取SAX工厂
    SAXParserFactory factory = SAXParserFactory.newInstance();
    // 2、从工厂获取解析器
    SAXParser parser = factory.newSAXParser();
    // 3、加载文档Document注册处理器
    // 编写处理器
    WebHandler handler = new WebHandler();
    // 4、解析
    parser.parse(Thread.currentThread().getContextClassLoader()
    .getResourceAsStream("web.xml"), handler);
    // 5、构建web上下文
    WebContext webContext = new WebContext(handler.getEntitys(),handler.getMappings());
    String servletName = webContext.getClz("/reg");
    // 6、根据URL反射获取处理的servlet
    Class servletClz = Class.forName(servletName);
    Servlet servlet = (Servlet)servletClz.getConstructor().newInstance();
    servlet.service();

  }
}


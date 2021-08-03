/*
 * Copyright 2019-2022 the original author “WangChang”.
 */

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * . 测试XML解析
 *
 * @author Wangchang
 * @since 2021-07-30-8:40
 */
public class XmlTest {

  public static void main(String[] args)
      throws ParserConfigurationException, SAXException, IOException {
    //SAX解析
    // 1、获取SAX工厂
    SAXParserFactory factory = SAXParserFactory.newInstance();
    // 2、从工厂获取解析器
    SAXParser parser = factory.newSAXParser();
    // 3、加载文档Document注册处理器
    // 编写处理器
    PersonHandler handler = new PersonHandler();
    // 4、解析
    parser.parse(Thread.currentThread().getContextClassLoader()
    .getResourceAsStream("web.xml"), handler);


  }
}

class PersonHandler extends DefaultHandler{
  private String tag;
  private Person person;
  private List<Person> persons = new ArrayList<Person>();

  public PersonHandler() {
  }

  @Override
  public void startElement(String uri, String localName, String qName, Attributes attributes)
      throws SAXException {
    System.out.println("[-]开始解析标签：" + qName);
    if (qName != null){
      tag = qName;
      // 匹配到类
      if (tag.equals("person")){
        person = new Person();
      }
    }

  }

  @Override
  public void characters(char[] ch, int start, int length) throws SAXException {
    // 获取xml标签里面的内容，转为字符串，去空格
    String contents = new String(ch,start,length).trim();
    if (tag != null){
      if (contents.length() > 0){
        if (tag.equals("name")){
          person.setName(contents);

        } else if (tag.equals("age")){
          person.setAge(Integer.parseInt(contents));
        }
      }
    }

  }

  @Override
  public void endElement(String uri, String localName, String qName) throws SAXException {
    System.out.println("[-]结束解析标签：" + qName);
    if (qName.equals("person")){
      persons.add(person);
    }
  }

  @Override
  public void startDocument() throws SAXException {
    System.out.println("[-]开始解析XML文档");
  }

  @Override
  public void endDocument() throws SAXException {
    for (Person person : persons){
      System.out.println("[-] Person: name: " + person.getName() + "," + "age is " + person.getAge());
    }
    System.out.println("[-]结束解析XML文档");
  }
}

class Person {
  private String name;
  private int age;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getAge() {
    return age;
  }

  public void setAge(int age) {
    this.age = age;
  }

  public Person() {
  }

  public Person(String name, int age) {
    this.name = name;
    this.age = age;
  }
}
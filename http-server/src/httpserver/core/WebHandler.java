/*
 * Copyright 2019-2022 the original author “WangChang”.
 */
package core;
import java.util.ArrayList;
import java.util.List;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


public class WebHandler extends DefaultHandler{
  private String tag;
  private Entity entity;
  private Mapping mapping;
  private boolean isEntity = false;
  private List<Entity> entitys = new ArrayList<Entity>();
  private List<Mapping> mappings = new ArrayList<Mapping>();


  public WebHandler() {
  }

  @Override
  public void startElement(String uri, String localName, String qName, Attributes attributes)
      throws SAXException {
    if (qName != null){
      tag = qName;
      // 匹配到类
      if (tag.equals("servlet")){
        entity = new Entity();
        isEntity = true;
      }else if (tag.equals("servlet-mapping")){
        mapping = new Mapping();
        isEntity = false;
      }
    }

  }

  @Override
  public void characters(char[] ch, int start, int length) throws SAXException {
    // 获取xml标签里面的内容，转为字符串，去空格
    String contents = new String(ch,start,length).trim();
    if (tag != null){
      if (contents.length() > 0){
        if (isEntity){
          if (tag.equals("servlet-name")){
            entity.setName(contents);
          } else if (tag.equals("servlet-class")){
            entity.setClz(contents);
          }
        } else {
          if (tag.equals("servlet-name")){
            mapping.setName(contents);

          } else if (tag.equals("url-pattern")){
            mapping.addPattern(contents);
          }
        }

      }
    }

  }

  @Override
  public void endElement(String uri, String localName, String qName) throws SAXException {
      if (qName.equals("servlet")) {
        entitys.add(entity);
        entity = null;
      } else if (qName.equals("servlet-mapping")){
        mappings.add(mapping);
        mapping = null;
      }
  }

  public List<Entity> getEntitys() {
    return entitys;
  }

  public List<Mapping> getMappings() {
    return mappings;
  }
}

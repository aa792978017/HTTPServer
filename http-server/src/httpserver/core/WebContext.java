package core;/*
 * Copyright 2019-2022 the original author “WangChang”.
 */

import core.Mapping;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * . Web 容器，存放配置，上下文等
 *
 * @author Wangchang
 * @since 2021-07-30-22:55
 */
public class WebContext {
  private List<Entity> entities;
  private List<Mapping> mappings;
  // key:servlet-name,value:servlet-class
  private Map<String,String> entitysMap = new HashMap<String, String>();
  // key:url-pattern,value:servlet-name
  private Map<String,String> mappingsMap = new HashMap<String, String>();

  public WebContext() {
  }

  public WebContext(List<Entity> entities, List<Mapping> mappings) {
    this.entities = entities;
    this.mappings = mappings;
    for (Entity entity: entities){
      entitysMap.put(entity.getName(),entity.getClz());
    }
    for (Mapping mapping: mappings){
      for (String pattern : mapping.getPatterns()){
        mappingsMap.put(pattern,mapping.getName());
      }
    }
  }

  /**
   * 通过URL找到对应的class
   * @param pattern
   * @return
   */
  public String getClz(String pattern){
    // 找到了servlet-name
    String name = mappingsMap.get(pattern);
    // 找到了class
    return entitysMap.get(name);
  }


  public Map<String, String> getMappingsMap() {
    return mappingsMap;
  }

  public Map<String, String> getEntitysMap() {
    return entitysMap;
  }
}

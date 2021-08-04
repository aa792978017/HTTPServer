package core;
/*
 * Copyright 2019-2022 the original author “WangChang”.
 */

import java.util.HashSet;
import java.util.Set;

/**
 * .
 * 对应servlet-mapping
 *   <servlet-mapping>
 *     <servlet-name>login</servlet-name>
 *     <url-pattern>/login</url-pattern>
 *     <url-pattern>/g</url-pattern>
 *   </servlet-mapping>
 * @author Wangchang
 * @since 2021-07-30-22:39
 */
public class Mapping {
  private String name;
  private Set<String> patterns;

  public Mapping() {
    patterns = new HashSet<String>();
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Set<String> getPatterns() {
    return patterns;
  }

  public void setPatterns(Set<String> patterns) {
    this.patterns = patterns;
  }

  public Mapping(String name, Set<String> patterns) {
    this.name = name;
    this.patterns = patterns;
  }

  /**
   * 添加URL
   * @param pattern
   */
  public void addPattern(String pattern){
    this.patterns.add(pattern);
  }
}

package org.luncert.mx1.commons.util;

import java.util.Properties;

public final class PropertiesUtils {
  
  private PropertiesUtils() {}
  
  public static PropertiesBuilder builder() {
    return new PropertiesBuilder();
  }
  
  public static class PropertiesBuilder {
  
    private Properties props = new Properties();
  
    public PropertiesBuilder put(String name, Object value) {
      props.put(name, value);
      return this;
    }
  
    public Properties build() {
      return props;
    }
  }
}
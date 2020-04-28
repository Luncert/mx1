package org.luncert.mx1.probe.commons.util;

import org.apache.commons.lang3.StringUtils;
import org.luncert.mx1.probe.commons.annotation.SystemProperty;

import java.lang.reflect.Field;

public final class SystemPropertiesUtil {
  
  private SystemPropertiesUtil() {}
  
  public static void fill(Object object) {
    for (Field field : object.getClass().getDeclaredFields()) {
      field.setAccessible(true);
      
      SystemProperty annotation = field.getAnnotation(SystemProperty.class);
      if (annotation != null) {
        String propertyValue = getSystemProperty(annotation);
        
        try {
          field.set(object, propertyValue);
        } catch (IllegalAccessException e) {
          throw new RuntimeException("failed to fill system property: "
              + annotation.value() + " -> " + object.getClass().getName() + "." + field.getName(), e);
        }
      }
    }
  }
  
  private static String getSystemProperty(SystemProperty annotation) {
    String propertyValue = null;
    
    // try to get system property using annotation.value()
    if (!StringUtils.isEmpty(annotation.value())) {
      propertyValue = System.getProperty(annotation.value());
      if (propertyValue != null) {
        return propertyValue;
      }
    }
    
    // first try failed, turn to annotation.keys()
    if (annotation.keys().length == 0) {
      return null;
    }
    
    for (String key : annotation.keys()) {
      if (!StringUtils.isEmpty(annotation.value())) {
        propertyValue = System.getProperty(key);
        if (propertyValue != null) {
          return propertyValue;
        }
      }
    }
    
    return null;
  }
}

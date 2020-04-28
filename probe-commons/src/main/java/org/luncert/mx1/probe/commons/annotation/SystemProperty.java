package org.luncert.mx1.probe.commons.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SystemProperty {
  
  /**
   * system property key, has higher priority than {@link SystemProperty#keys()}
   */
  String value() default "";
  
  /**
   * system property keys
   */
  String[] keys() default {};
}

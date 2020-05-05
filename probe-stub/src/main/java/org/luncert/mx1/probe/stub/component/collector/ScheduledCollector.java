package org.luncert.mx1.probe.stub.component.collector;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ScheduledCollector {
  
  /**
   * interval time, unit: millisecond
   */
  int value() default 500;
}

package org.luncert.mx1.core.common;

import lombok.AllArgsConstructor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@AllArgsConstructor
public class Invokable {
  
  private Object obj;
  private Method method;
  
  public Object apply(Object... arguments) throws InvocationTargetException, IllegalAccessException {
    return method.invoke(obj, arguments);
  }
  
  @Override
  public String toString() {
    return obj.getClass().getName() + "#" + method.getName();
  }
}

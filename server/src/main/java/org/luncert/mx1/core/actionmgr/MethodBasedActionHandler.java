package org.luncert.mx1.core.actionmgr;

import org.luncert.mx1.core.common.Invokable;
import org.luncert.mx1.core.exception.ActionHandlerError;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MethodBasedActionHandler extends AbstractActionHandler {
  
  private Invokable handleMethod;
  
  MethodBasedActionHandler(String action, Object registry, Method method) {
    super(action, resolveMessageBodyType(method));
    this.handleMethod = new Invokable(registry, method);
  }
  
  @Override
  protected void handle(Message message) {
    try {
      handleMethod.apply(message);
    } catch (InvocationTargetException | IllegalAccessException e) {
      throw new ActionHandlerError(e);
    }
  }
  
  @Override
  public String toString() {
    return handleMethod.toString();
  }
}
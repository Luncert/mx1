package org.luncert.mx1.core.exception;

import java.lang.reflect.Method;
import java.text.MessageFormat;

public class InvalidActionHandlerMethodError extends ActionHandlerError {
  
  public InvalidActionHandlerMethodError(Method method) {
    super(MessageFormat.format("Invalid action handler method {0}#{1}," +
            " expect: public void handleMethod(Message<E> message)",
        method.getDeclaringClass().getSimpleName(), method.getName()));
  }
}

package org.luncert.mx1.core.exception;

public class ActionHandlerError extends RuntimeException {
  
  public ActionHandlerError(String message) {
    super(message);
  }
  
  public ActionHandlerError(Throwable cause) {
    super(cause);
  }
}
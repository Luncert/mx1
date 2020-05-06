package org.luncert.mx1.probe.commons.exception;

public class ProxyInvokableError extends RuntimeException {
  
  public ProxyInvokableError(String msg) {
    super(msg);
  }
  
  public ProxyInvokableError(Throwable throwable) {
    super(throwable);
  }
  
  public ProxyInvokableError(String msg, Throwable throwable) {
    super(msg, throwable);
  }
}

package org.luncert.mx1.probe.daemon.execption;

public class StubDataReceiveError extends RuntimeException {
  
  public StubDataReceiveError(String message) {
    super(message);
  }
  
  public StubDataReceiveError(String message, Throwable cause) {
    super(message, cause);
  }
}


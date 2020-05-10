package org.luncert.mx1.probe.daemon.execption;

public class DaemonInitError extends RuntimeException {
  
  public DaemonInitError(Throwable e) {
    super(e);
  }
}

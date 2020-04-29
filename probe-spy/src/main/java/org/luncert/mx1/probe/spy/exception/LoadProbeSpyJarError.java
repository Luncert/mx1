package org.luncert.mx1.probe.spy.exception;

public class LoadProbeSpyJarError extends RuntimeException {
  
  public LoadProbeSpyJarError(String msg) {
    super(msg);
  }
  
  public LoadProbeSpyJarError(Throwable e) {
    super(e);
  }
}

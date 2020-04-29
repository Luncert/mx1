package org.luncert.mx1.probe.stub.exeception;

public class LoadProbeSpyJarError extends RuntimeException {
  
  public LoadProbeSpyJarError(String msg) {
    super(msg);
  }
  
  public LoadProbeSpyJarError(Throwable e) {
    super(e);
  }
}

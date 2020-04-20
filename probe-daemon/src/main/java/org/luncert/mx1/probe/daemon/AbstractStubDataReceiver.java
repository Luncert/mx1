package org.luncert.mx1.probe.daemon;

import java.io.OutputStream;

/**
 * ref: https://stackoverflow.com/questions/25396664/shared-memory-between-two-jvms
 */
public abstract class AbstractStubDataReceiver {
  
  public abstract void transport(OutputStream outputStream);
  
  public abstract void destroy();
}

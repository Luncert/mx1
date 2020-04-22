package org.luncert.mx1.probe.daemon;

import java.io.OutputStream;

/**
 * ref: https://stackoverflow.com/questions/25396664/shared-memory-between-two-jvms
 */
public interface IStubDataReceiver {
  
  void transport(OutputStream outputStream);
  
  void destroy();
}

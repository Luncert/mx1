package org.luncert.mx1.probe.ipc;

import java.io.Closeable;
import java.io.IOException;

public abstract class IpcChannel implements Closeable {
  
  public abstract void write(Object object) throws IOException;
  
  /**
   * sync() is a way to block current thread for like tcp server.
   */
  public void sync() throws IOException {
    throw new UnsupportedOperationException();
  }
}

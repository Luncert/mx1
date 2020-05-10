package org.luncert.mx1.probe.ipc;

import org.luncert.mx1.commons.data.NetURL;

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
  
  public NetURL getRemoteAddress() throws IOException {
    return null;
  }
}

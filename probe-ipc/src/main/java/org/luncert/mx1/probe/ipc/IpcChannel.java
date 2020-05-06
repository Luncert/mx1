package org.luncert.mx1.probe.ipc;

import java.io.Closeable;
import java.io.IOException;

public abstract class IpcChannel implements Closeable {
  
  public abstract void write(Object object) throws IOException;
  
  public void refresh() throws IOException {}
}

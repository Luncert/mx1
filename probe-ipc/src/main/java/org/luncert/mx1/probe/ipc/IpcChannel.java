package org.luncert.mx1.probe.ipc;

import java.io.Closeable;
import java.io.IOException;

public abstract class IpcChannel<E> implements Closeable {
  
  public abstract void write(E object) throws IOException;
  
  public void refresh() throws IOException {}
}

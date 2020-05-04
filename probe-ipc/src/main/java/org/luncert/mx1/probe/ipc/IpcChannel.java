package org.luncert.mx1.probe.ipc;

import java.io.Closeable;
import java.io.IOException;

public interface IpcChannel extends Closeable {
  
  void write(Object object) throws IOException;
  
  default void close() throws IOException {}
}

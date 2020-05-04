package org.luncert.mx1.probe.ipc;

import java.io.IOException;

public interface Connector<E> {
  
  IpcChannel open() throws IOException;
}

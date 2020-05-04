package org.luncert.mx1.probe.ipc;

import java.io.IOException;

public interface Connector {
  
  IpcChannel connect() throws IOException;
}

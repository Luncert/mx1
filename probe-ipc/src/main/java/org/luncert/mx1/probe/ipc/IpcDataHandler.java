package org.luncert.mx1.probe.ipc;

import java.io.IOException;

public interface IpcDataHandler<E> {
  
  void onOpen(IpcChannel channel) throws IOException;
  
  void onData(IpcChannel channel, E data) throws IOException;
  
  void onClose();
}

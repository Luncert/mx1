package org.luncert.mx1.probe.ipc;

import java.io.IOException;

public abstract class IpcDataHandlerAdapter<E> implements IpcDataHandler<E> {
  
  @Override
  public void onOpen(IpcChannel channel) throws IOException {
  
  }
  
  @Override
  public void onData(IpcChannel channel, E data) throws IOException {
  
  }
  
  @Override
  public void onClose() {
  
  }
}

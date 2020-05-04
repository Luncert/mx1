package org.luncert.mx1.probe.ipc;

public interface IpcDataHandler<E> {
  
  void onData(E data);
  
  void onClose();
}

package org.luncert.mx1.probe.daemon.impl;

import org.luncert.mx1.probe.daemon.IStubDataReceiver;

import java.io.OutputStream;

public class ShmStubDataReceiver implements IStubDataReceiver {
  
  @Override
  public void transport(OutputStream outputStream) {
    throw new UnsupportedOperationException();
  }
  
  @Override
  public void destroy() {
    throw new UnsupportedOperationException();
  }
}

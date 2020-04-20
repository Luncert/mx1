package org.luncert.mx1.probe.daemon.impl;

import org.luncert.mx1.probe.daemon.AbstractStubDataReceiver;

import java.io.OutputStream;

public class UdpStubDataReceiver extends AbstractStubDataReceiver {
  
  @Override
  public void transport(OutputStream outputStream) {
  }
  
  @Override
  public void destroy() {
  }
}

package org.luncert.mx1.probe.ipc;

import org.luncert.mx1.probe.ipc.impl.UdpConnector;

public final class IpcFactory {
  
  private IpcFactory() {}
  
  public static void shm() {
    throw new UnsupportedOperationException();
  }
  
  public static UdpConnector udp() {
    return new UdpConnector();
  }
}

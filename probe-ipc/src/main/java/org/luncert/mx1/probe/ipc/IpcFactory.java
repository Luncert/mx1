package org.luncert.mx1.probe.ipc;

import org.luncert.mx1.probe.ipc.impl.TcpConnector;
import org.luncert.mx1.probe.ipc.impl.UdpConnector;

public final class IpcFactory {
  
  private IpcFactory() {}
  
  public static void shm() {
    throw new UnsupportedOperationException();
  }
  
  public static <E> UdpConnector<E> udp() {
    return new UdpConnector<>();
  }
  
  public static <E> TcpConnector<E> tcp() {
    return new TcpConnector<>();
  }
}

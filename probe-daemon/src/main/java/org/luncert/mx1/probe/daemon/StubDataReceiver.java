//package org.luncert.mx1.probe.daemon;
//
//import lombok.extern.slf4j.Slf4j;
//import org.luncert.mx1.probe.commons.data.NetURL;
//import org.luncert.mx1.probe.ipc.IpcChannel;
//import org.luncert.mx1.probe.ipc.IpcFactory;
//
//import java.io.IOException;
//import java.io.OutputStream;
//import java.net.InetSocketAddress;
//
///**
// * ref: https://stackoverflow.com/questions/25396664/shared-memory-between-two-jvms
// */
//@Slf4j
//public class StubDataReceiver {
//
//  private static final NetURL DEFAULT_BINDING = new NetURL("tcp://localhost:43210");
//
//  private NetURL servingAddress;
//
//  public StubDataReceiver() {}
//
//  public StubDataReceiver(NetURL servingAddress) {
//    this.servingAddress = servingAddress;
//  }
//
//  void transport(OutputStream outputStream) throws IOException {
//    if (servingAddress == null) {
//      servingAddress = DEFAULT_BINDING;
//      log.info("Binding StubDataReceiver to default address {}.", servingAddress);
//    } else {
//      log.info("Binding StubDataReceiver to {}.", servingAddress);
//    }
//
//    assert servingAddress.getProtocol().equals("tcp");
//
//    IpcChannel ipcChannel = IpcFactory.tcp()
//        .bind(new InetSocketAddress(servingAddress.getHost(), servingAddress.getPort()))
//        .addHandler()
//        .open();
//  }
//
//  void destroy() {
//
//  }
//}

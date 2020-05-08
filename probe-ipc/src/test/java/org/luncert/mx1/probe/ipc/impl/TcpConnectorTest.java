package org.luncert.mx1.probe.ipc.impl;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.luncert.mx1.probe.ipc.IpcChannel;
import org.luncert.mx1.probe.ipc.IpcDataHandler;
import org.luncert.mx1.probe.ipc.IpcFactory;

import java.io.IOException;
import java.io.Serializable;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

@RunWith(JUnit4.class)
public class TcpConnectorTest {
  
  private SocketAddress serveAddr = new InetSocketAddress("localhost", 43210);
  
  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  private static class DataPacket implements Serializable {
  
    private static final long serialVersionUID = 2162118160358573673L;
    
    private String action;
    
    private String data;
  }
  
  @Test
  public void testSuccess() throws IOException, InterruptedException {
    
    IpcChannel readChannel = IpcFactory.<DataPacket>tcp()
        .bind(serveAddr)
        .addHandler(new IpcDataHandler<DataPacket>() {

          @Override
          public void onData(IpcChannel channel, DataPacket data) {
            System.out.println("Received: " + data);
          }
  
          @Override
          public void onClose() {
    
          }
        })
        .open();
  
    IpcChannel writeChannel = IpcFactory.<DataPacket>tcp()
        .destination(serveAddr)
        .open();
  
    writeChannel.write(DataPacket.builder()
        .action("test action")
        .data("test data")
        .build());
    
    Thread.sleep(500);
    
    writeChannel.close();
    readChannel.close();
  }

  //@Test
  //public void testSend() throws IOException {
  //  try (IpcChannel writeChannel = IpcFactory.<IpcPacket>tcp()
  //      .destination(serveAddr)
  //      .open()) {
  //    for (int i = 0; i < 3; i++) {
  //      writeChannel.write(new IpcPacket<>("test action", "test data " + i));
  //    }
  //  }
  //}
  //
  //@Test
  //public void testRead() throws InterruptedException, IOException {
  //  IpcChannel readChannel = IpcFactory.<DataPacket>tcp()
  //      .bind(serveAddr)
  //      .addHandler(new IpcDataHandler<DataPacket>() {
  //
  //        @Override
  //        public void onData(IpcChannel channel, DataPacket data) {
  //          System.out.println("Received: " + data);
  //        }
  //
  //        @Override
  //        public void onClose() {
  //
  //        }
  //      })
  //      .open();
  //
  //  Thread.sleep(60000);
  //
  //  readChannel.close();
  //}
}

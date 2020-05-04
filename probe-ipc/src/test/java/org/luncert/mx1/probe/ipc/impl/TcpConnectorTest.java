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
import java.net.InetSocketAddress;
import java.net.SocketAddress;

@RunWith(JUnit4.class)
public class TcpConnectorTest {
  
  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  private static class DataPacket {
    
    private String action;
    
    private String data;
  }
  
  @Test
  public void testSuccess() throws IOException, InterruptedException {
    SocketAddress serveAddr = new InetSocketAddress("127.0.0.1", 55000);
    
    IpcChannel<DataPacket> readChannel = IpcFactory.<DataPacket>tcp()
        .bind(serveAddr)
        .addHandler(new IpcDataHandler<DataPacket>() {
          // TODO: read byte
          @Override
          public void onData(DataPacket data) {
            System.out.println(data);
          }
  
          @Override
          public void onClose() {
    
          }
        })
        .open();
    
    IpcChannel<DataPacket> writeChannel = IpcFactory.<DataPacket>tcp()
        .destination(serveAddr)
        .open();
    writeChannel.write(DataPacket.builder()
        .action("test action")
        .data("test data")
        .build());
    writeChannel.close();
    
    readChannel.refresh();
    Thread.sleep(1000);
    
    readChannel.close();
  }
}

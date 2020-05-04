package org.luncert.mx1.probe.ipc.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.luncert.mx1.probe.ipc.IpcChannel;
import org.luncert.mx1.probe.ipc.IpcDataHandler;
import org.luncert.mx1.probe.ipc.IpcFactory;

import java.io.IOException;

@RunWith(JUnit4.class)
public class UdpConnectorTest {
  
  @Test
  public void testSuccess() throws IOException, InterruptedException {
    IpcChannel readChannel = IpcFactory.udp()
        .port(55000)
        .handler(new IpcDataHandler() {
          @Override
          public void onData(Object data) {
    
          }
  
          @Override
          public void onClose() {
    
          }
        })
        .connect();
    
    IpcChannel writeChannel = IpcFactory.udp()
        .port(0)
        .connect();
    
    writeChannel.write("Test udp message");
    writeChannel.close();
    
    Thread.sleep(1000);
    readChannel.close();
  }
}

package org.luncert.mx1.probe.stub.component;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.luncert.mx1.commons.data.IpcAction;
import org.luncert.mx1.commons.data.IpcPacket;

import org.luncert.mx1.commons.data.staticinfo.StaticSystemInfo;
import org.luncert.mx1.probe.ipc.IpcChannel;
import org.luncert.mx1.probe.stub.component.collector.CollectorRegistry;
import org.luncert.mx1.probe.stub.component.collector.staticinfo.StaticSystemInfoCollector;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.Properties;

import static org.mockito.Matchers.any;

@RunWith(JUnit4.class)
public class DaemonConnectionHandlerTest {
  
  @Test
  public void testSuccess() throws IOException {
    IpcChannel channel = Mockito.mock(IpcChannel.class);
    
    Mockito.doAnswer(invocationOnMock -> {
      IpcPacket packet = invocationOnMock.getArgumentAt(0, IpcPacket.class);
      Assert.assertTrue(packet.getData() instanceof StaticSystemInfo);
      return null;
    }).when(channel).write(any());

    IpcPacket<String> ipcPacket = new IpcPacket<>(IpcAction.COLLECT_INFO,
        new Properties(), StaticSystemInfoCollector.class.getName());
    CollectorRegistry registry = new CollectorRegistry();
    DaemonConnectionHandler handler = new DaemonConnectionHandler(registry);
    handler.onData(channel, ipcPacket);
  }
}

package org.luncert.mx1.probe.stub.component;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.luncert.mx1.commons.constant.DaemonAction;
import org.luncert.mx1.commons.constant.ProbeAction;
import org.luncert.mx1.commons.data.DataPacket;
import org.luncert.mx1.commons.constant.StubAction;

import org.luncert.mx1.commons.data.staticinfo.StaticAppInfo;
import org.luncert.mx1.commons.data.staticinfo.StaticSysInfo;
import org.luncert.mx1.probe.ipc.IpcChannel;
import org.luncert.mx1.probe.stub.component.collector.CollectorRegistry;
import org.luncert.mx1.probe.stub.component.collector.staticinfo.StaticSysInfoCollector;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.Properties;

import static org.mockito.Matchers.any;

@RunWith(JUnit4.class)
public class DaemonDataHandlerTest {
  
  @Test
  public void testSuccess() throws IOException {
    IpcChannel channel = Mockito.mock(IpcChannel.class);
    
    Mockito.doAnswer(invocationOnMock -> {
      DataPacket packet = invocationOnMock.getArgumentAt(0, DataPacket.class);
      Assert.assertTrue(packet.getData() instanceof StaticAppInfo);
      return null;
    }).when(channel).write(any());

    DataPacket<String> dataPacket = new DataPacket<>(DaemonAction.COLLECT_STATIC_APP_INFO,
        new Properties(), StaticSysInfoCollector.class.getName());
    CollectorRegistry registry = new CollectorRegistry();
    DaemonDataHandler handler = new DaemonDataHandler(registry);
    handler.onData(channel, dataPacket);
  }
}

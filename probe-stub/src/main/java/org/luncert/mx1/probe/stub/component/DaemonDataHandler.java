package org.luncert.mx1.probe.stub.component;

import lombok.extern.slf4j.Slf4j;
import org.luncert.mx1.commons.constant.DaemonAction;
import org.luncert.mx1.commons.data.DataPacket;
import org.luncert.mx1.commons.constant.StubAction;
import org.luncert.mx1.commons.data.staticinfo.StaticAppInfo;
import org.luncert.mx1.commons.data.staticinfo.StaticJvmInfo;
import org.luncert.mx1.commons.data.staticinfo.StaticMavenInfo;
import org.luncert.mx1.commons.data.staticinfo.StaticSysInfo;
import org.luncert.mx1.probe.ipc.IpcChannel;
import org.luncert.mx1.probe.ipc.IpcDataHandler;
import org.luncert.mx1.probe.stub.component.collector.CollectorRegistry;
import org.luncert.mx1.probe.stub.component.collector.staticinfo.StaticMavenInfoCollector;
import org.luncert.mx1.probe.stub.component.collector.staticinfo.StaticJvmInfoCollector;
import org.luncert.mx1.probe.stub.component.collector.staticinfo.StaticSysInfoCollector;

import java.io.IOException;
import java.util.Properties;

// handle data from probe-daemon
@Slf4j
public class DaemonDataHandler implements IpcDataHandler<DataPacket> {
  
  private CollectorRegistry collectorRegistry;
  
  public DaemonDataHandler(CollectorRegistry collectorRegistry) {
    this.collectorRegistry = collectorRegistry;
  }
  
  // The following processing is running in netty worker group.
  @Override
  public void onData(IpcChannel channel, DataPacket packet) throws IOException {
    String action = packet.getAction();
    if (DaemonAction.COLLECT_STATIC_APP_INFO.equals(action)) {
      // invoke registry
      StaticAppInfo info = new StaticAppInfo();
      
      info.setStaticJvmInfo(collectorRegistry
          .<StaticJvmInfo>collect(StaticJvmInfoCollector.class.getName())
          .getInfo());
      
      info.setStaticSysInfo(collectorRegistry
          .<StaticSysInfo>collect(StaticSysInfoCollector.class.getName())
          .getInfo());
      
      info.setStaticMavenInfo(collectorRegistry
          .<StaticMavenInfo>collect(StaticMavenInfoCollector.class.getName())
          .getInfo());
      
      // commit to daemon
      channel.write(new DataPacket<>(StubAction.COMMIT_STATIC_APP_INFO,
          new Properties(), info));
      
      return;
    }
    
    log.error("Invalid daemon action {} in {}.", action, packet);
  }
  
  @Override
  public void onClose() {
  
  }
}

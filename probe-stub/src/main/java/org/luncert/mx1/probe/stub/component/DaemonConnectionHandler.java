package org.luncert.mx1.probe.stub.component;

import com.google.common.collect.ImmutableMap;
import org.luncert.mx1.probe.commons.data.IpcPacket;
import org.luncert.mx1.probe.ipc.IpcDataHandler;
import org.luncert.mx1.probe.stub.component.collector.AbstractInfoCollector;
import org.luncert.mx1.probe.stub.component.collector.staticinfo.MavenInfoCollector;

import java.util.Map;

public class DaemonConnectionHandler implements IpcDataHandler<IpcPacket> {
  
  private Map<String, AbstractInfoCollector> staticInfoCollectorMap;
  
  public DaemonConnectionHandler() {
    staticInfoCollectorMap = ImmutableMap.<String, AbstractInfoCollector>builder()
        .put("COLLECT_MAVEN_INFO", new MavenInfoCollector())
        .build();
  }
  
  @Override
  public void onData(IpcPacket data) {
  
  }
  
  @Override
  public void onClose() {
  
  }
}

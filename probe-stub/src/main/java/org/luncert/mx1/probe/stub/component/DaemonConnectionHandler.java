package org.luncert.mx1.probe.stub.component;

import org.luncert.mx1.commons.data.IpcAction;
import org.luncert.mx1.commons.data.IpcPacket;
import org.luncert.mx1.commons.util.PropertiesUtils;
import org.luncert.mx1.probe.ipc.IpcChannel;
import org.luncert.mx1.probe.ipc.IpcDataHandler;
import org.luncert.mx1.probe.stub.component.collector.CollectorRegistry;
import org.luncert.mx1.probe.stub.pojo.CollectorResponse;

import java.io.IOException;

public class DaemonConnectionHandler implements IpcDataHandler<IpcPacket> {
  
  private CollectorRegistry collectorRegistry;
  
  public DaemonConnectionHandler(CollectorRegistry collectorRegistry) {
    this.collectorRegistry = collectorRegistry;
  }
  
  // The following processing is running in netty worker group.
  @Override
  public void onData(IpcChannel channel, IpcPacket data) throws IOException {
    if (IpcAction.COLLECT_INFO.equals(data.getAction())) {
      // invoke registry
      String collectorName = (String) data.getData();
      CollectorResponse rep = collectorRegistry.collect(collectorName);
      channel.write(new IpcPacket<>(IpcAction.COLLECT_INFO,
          PropertiesUtils.builder()
              .put("success", rep.isSuccess())
              .put("collectorName", collectorName)
              .build(),
          rep));
    }
  }
  
  @Override
  public void onClose() {
  
  }
}

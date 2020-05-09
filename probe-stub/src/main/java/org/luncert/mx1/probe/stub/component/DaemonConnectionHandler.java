package org.luncert.mx1.probe.stub.component;

import org.luncert.mx1.commons.data.DataPacket;
import org.luncert.mx1.commons.constant.IpcAction;
import org.luncert.mx1.commons.util.PropertiesUtils;
import org.luncert.mx1.probe.ipc.IpcChannel;
import org.luncert.mx1.probe.ipc.IpcDataHandler;
import org.luncert.mx1.probe.stub.component.collector.CollectorRegistry;
import org.luncert.mx1.probe.stub.pojo.CollectorResponse;

import java.io.IOException;

public class DaemonConnectionHandler implements IpcDataHandler<DataPacket> {
  
  private CollectorRegistry collectorRegistry;
  
  public DaemonConnectionHandler(CollectorRegistry collectorRegistry) {
    this.collectorRegistry = collectorRegistry;
  }
  
  // The following processing is running in netty worker group.
  @Override
  public void onData(IpcChannel channel, DataPacket data) throws IOException {
    if (IpcAction.COLLECT_INFO.equals(data.getAction())) {
      // invoke registry
      String collectorName = (String) data.getData();
      CollectorResponse rep = collectorRegistry.collect(collectorName);
      channel.write(new DataPacket<>(IpcAction.COLLECT_INFO,
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

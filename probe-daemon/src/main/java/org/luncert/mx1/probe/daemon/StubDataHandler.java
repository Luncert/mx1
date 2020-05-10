package org.luncert.mx1.probe.daemon;

import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.luncert.mx1.commons.constant.CollectorName;
import org.luncert.mx1.commons.constant.ProbeAction;
import org.luncert.mx1.commons.constant.StubAction;
import org.luncert.mx1.commons.data.DataPacket;
import org.luncert.mx1.commons.data.staticinfo.StaticAppInfo;
import org.luncert.mx1.probe.ipc.IpcChannel;
import org.luncert.mx1.probe.ipc.IpcDataHandler;

import java.io.IOException;
import java.util.Properties;

// handle data received from probe-stub
@Slf4j
public class StubDataHandler implements IpcDataHandler<DataPacket> {
  
  private Channel centralChannel;
  
  public StubDataHandler(Channel centralChannel) {
    this.centralChannel = centralChannel;
  }
  
  @Override
  public void onData(IpcChannel channel, DataPacket packet) throws IOException {
    String action = packet.getAction();
    if (StubAction.COMMIT_STATIC_APP_INFO.equals(action)) {
      StaticAppInfo info = (StaticAppInfo) packet.getData();
      
      // commit to central
      centralChannel.writeAndFlush(DataPacket.builder()
          .action(ProbeAction.COMMIT_METADATA)
          .data(info)
          .build());
      return;
    }
    
    if (StubAction.COMMIT_DYNAMIC_INFO.equals(action)) {
      Properties headers = packet.getHeaders();
      boolean success = (boolean) headers.get("success");
      if (success) {
        String collectorName = headers.getProperty("collectorName");
        
        // set action
        String probeAction;
        if (CollectorName.DYNAMIC_SYS_INFO_COLLECTOR.equals(collectorName)) {
          probeAction = ProbeAction.COMMIT_DYNAMIC_SYS_INFO;
        } else if (CollectorName.DYNAMIC_JVM_INFO_COLLECTOR.equals(collectorName)) {
          probeAction = ProbeAction.COMMIT_DYNAMIC_JVM_INFO;
        } else {
          log.error("Invalid collector name in {}.", packet);
          return;
        }
  
        // commit to central
        centralChannel.writeAndFlush(DataPacket.builder()
            .action(probeAction)
            .data(packet.getData())
            .build());
      }
      return;
    }
  
    log.error("Invalid stub action {} in {}.", action, packet);
  }
  
  @Override
  public void onClose() {
  
  }
}

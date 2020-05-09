package org.luncert.mx1.core.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.luncert.mx1.commons.constant.CoreAction;
import org.luncert.mx1.commons.constant.ProbeAction;
import org.luncert.mx1.commons.data.DataPacket;
import org.luncert.mx1.commons.data.NetURL;
import org.luncert.mx1.commons.data.dynamicinfo.DynamicJvmInfo;
import org.luncert.mx1.commons.data.dynamicinfo.DynamicSysInfo;
import org.luncert.mx1.commons.data.staticinfo.StaticAppInfo;
import org.luncert.mx1.core.component.ProbeListener;
import org.luncert.mx1.core.component.Session;
import org.luncert.mx1.core.service.AppInfoConsumer;
import org.luncert.mx1.core.service.INodeService;
import org.luncert.mx1.core.service.IProbeService;
import org.luncert.mx1.core.service.Subscription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.URL;
import java.util.Map;

@Slf4j
@Service
public class ProbeServiceImpl extends ProbeListener<DataPacket> implements IProbeService {
  
  @Autowired
  private INodeService nodeService;
  
  @Override
  @SuppressWarnings("unchecked")
  public void handleData(Session session, DataPacket packet) {
    String action = packet.getAction();
    // handle first packet
    if (ProbeAction.COMMIT_METADATA.equals(action) &&
        validateDataType(packet, StaticAppInfo.class)) {
      handleCommitMetadata(session, packet);
      return;
    }
    
    if (ProbeAction.UPDATE_METADATA.equals(action) &&
        validateDataType(packet, Map.class)) {
      handleUpdateMetadata(packet);
      return;
    }
    
    if (ProbeAction.COMMIT_DYNAMIC_JVM_INFO.equals(action) &&
        validateDataType(packet, DynamicJvmInfo.class)) {
      handleCommitDynamicJvmInfo(packet);
      return;
    }
    
    if (ProbeAction.COMMIT_DYNAMIC_SYS_INFO.equals(action) &&
        validateDataType(packet, DynamicSysInfo.class)) {
      handleCommitDynamicSysInfo(packet);
      return;
    }
    
    log.error("Invalid probe action {} from {}.", action, session.getRemoteAddress());
  }
  
  private boolean validateDataType(DataPacket packet, Class<?> expectedType) {
    Object data = packet.getData();
    if (data  == null) {
      log.error("Invalid packet {}, expected data of type {}, got null.",
          packet, expectedType);
      return false;
    }
    
    Class<?> actualType = data.getClass();
    if (!expectedType.isAssignableFrom(actualType)) {
      log.error("Invalid packet {}, expected data of type {}, got {}.",
          packet, expectedType, actualType);
      return false;
    }
    
    return true;
  }
  
  @Override
  public void onException(Session session, Throwable cause) {}
  
  @Override
  public void onDisconnected(Session session) {
  
  }
  
  private void handleCommitMetadata(Session session, DataPacket<StaticAppInfo> packet) {
    SocketAddress socketAddress = session.getRemoteAddress();
    if (socketAddress instanceof InetSocketAddress) {
  
      this.protocol = "tcp";
      this.host = socketAddress.getHostString();
      this.port = socketAddress.getPort();
    }
    NetURL remoteUrl = new NetURL(null, socketAddress);
    String nodeId = nodeService.register(session.getRemoteAddress(), packet.getData());
    session.getChannel().writeAndFlush(DataPacket.builder()
        .action(CoreAction.REP_NODE_ID)
        .data(nodeId)
        .build());
  }
  
  private void handleUpdateMetadata(DataPacket<Map<String, Object>> packet) {
    String nodeId = packet.getHeaders().getProperty("nodeId");
    if (nodeId == null) {
      log.error("Invalid packet {}, nodeId not found in headers.", packet);
      return;
    }
    
    nodeService.updateNodeMetadata(nodeId, packet.getData());
  }
  
  private void handleCommitDynamicJvmInfo(DataPacket<DynamicJvmInfo> packet) {
    nodeService.saveDynamicJvmInfo(packet.getData());
  }
  
  private void handleCommitDynamicSysInfo(DataPacket<DynamicSysInfo> packet) {
    nodeService.saveDynamicSysInfo(packet.getData());
  }
  
  @Override
  public Subscription subscribe(String probeId, AppInfoConsumer consumer) {
    return null;
  }
}

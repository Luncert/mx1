package org.luncert.mx1.core.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.luncert.mx1.commons.constant.CoreAction;
import org.luncert.mx1.commons.constant.ProbeAction;
import org.luncert.mx1.commons.data.DataPacket;
import org.luncert.mx1.commons.data.dynamicinfo.DynamicJvmInfo;
import org.luncert.mx1.commons.data.dynamicinfo.DynamicSysInfo;
import org.luncert.mx1.commons.data.staticinfo.StaticAppInfo;
import org.luncert.mx1.commons.util.DataPacketUtils;
import org.luncert.mx1.core.common.NetAddress;
import org.luncert.mx1.core.component.ProbeListener;
import org.luncert.mx1.core.component.Session;
import org.luncert.mx1.core.common.AppInfoConsumer;
import org.luncert.mx1.core.service.INodeService;
import org.luncert.mx1.core.service.IProbeService;
import org.luncert.mx1.core.service.Subscription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.InetSocketAddress;
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
        DataPacketUtils.validateDataType(packet, StaticAppInfo.class)) {
      handleCommitMetadata(session, packet);
      return;
    }
    
    if (ProbeAction.UPDATE_METADATA.equals(action) &&
        DataPacketUtils.validateDataType(packet, Map.class)) {
      handleUpdateMetadata(session, packet);
      return;
    }
    
    if (ProbeAction.COMMIT_DYNAMIC_JVM_INFO.equals(action) &&
        DataPacketUtils.validateDataType(packet, DynamicJvmInfo.class)) {
      handleCommitDynamicJvmInfo(session, packet);
      return;
    }
    
    if (ProbeAction.COMMIT_DYNAMIC_SYS_INFO.equals(action) &&
        DataPacketUtils.validateDataType(packet, DynamicSysInfo.class)) {
      handleCommitDynamicSysInfo(session, packet);
      return;
    }
    
    log.error("Invalid probe action {} from {}", action, session.getRemoteAddress());
  }
  
  @Override
  public void onException(Session session, Throwable cause) {
    // TODO: save as message
  }
  
  @Override
  public void onDisconnected(Session session) {
    // TODO: disconnect
  }
  
  private void handleCommitMetadata(Session session, DataPacket<StaticAppInfo> packet) {
    // NOTE: netty's DomainSocketAddress is a sub-class of InetSocketAddress
    InetSocketAddress socketAddress = (InetSocketAddress) session.getRemoteAddress();
    
    String nodeId = nodeService.register(NetAddress.of(socketAddress), packet.getData());
  
    session.set("nodeId", nodeId);
  
    session.getChannel().writeAndFlush(DataPacket.builder()
        .action(CoreAction.REP_NODE_ID)
        .data(nodeId)
        .build());
  }
  
  private void handleUpdateMetadata(Session session, DataPacket<Map<String, Object>> packet) {
    String nodeId = packet.getHeaders().getProperty("nodeId");
    if (nodeId == null) {
      log.error("Invalid packet {}, nodeId not found in headers", packet);
      return;
    }
    
    session.set("nodeId", nodeId);
    
    nodeService.updateNodeMetadata(nodeId, packet.getData());
  }
  
  private void handleCommitDynamicJvmInfo(Session session, DataPacket<DynamicJvmInfo> packet) {
    // FIXME: nodeId may be not set
    nodeService.saveDynamicJvmInfo(session.get("nodeId"), packet.getData());
  }
  
  private void handleCommitDynamicSysInfo(Session session, DataPacket<DynamicSysInfo> packet) {
    nodeService.saveDynamicSysInfo(session.get("nodeId"), packet.getData());
  }
  
  @Override
  public <E> Subscription subscribe(String nodeId, Class<E> type, AppInfoConsumer<E> consumer) {
    return null;
  }
}

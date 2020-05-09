package org.luncert.mx1.core.service;

import org.luncert.mx1.commons.data.NetURL;
import org.luncert.mx1.commons.data.dynamicinfo.DynamicJvmInfo;
import org.luncert.mx1.commons.data.dynamicinfo.DynamicSysInfo;
import org.luncert.mx1.commons.data.staticinfo.StaticAppInfo;
import org.luncert.mx1.core.dto.NodeListItemDto;
import org.luncert.mx1.core.dto.NodeMetadataDto;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface INodeService {
  
  /**
   *
   * @return nodeId cypher text of remote address
   */
  String register(NetURL remoteAddress, StaticAppInfo info);
  
  void updateNodeMetadata(String id, Map<String, Object> newMetadata);
  
  void saveDynamicJvmInfo(DynamicJvmInfo dynamicJvmInfo);
  
  void saveDynamicSysInfo(DynamicSysInfo dynamicSysInfo);
  
  void saveAppLog();
  
  List<NodeListItemDto> getAllNodes();
  
  Optional<NodeMetadataDto> getNodeMetadata(String nodeId);
}

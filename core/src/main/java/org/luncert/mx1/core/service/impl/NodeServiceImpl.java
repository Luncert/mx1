package org.luncert.mx1.core.service.impl;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.Triple;
import org.bson.types.ObjectId;
import org.elasticsearch.common.collect.Tuple;
import org.luncert.mx1.commons.data.dynamicinfo.DynamicJvmInfo;
import org.luncert.mx1.commons.data.dynamicinfo.DynamicSysInfo;
import org.luncert.mx1.commons.data.staticinfo.StaticAppInfo;
import org.luncert.mx1.core.db.mongo.entity.NodeMetadata;
import org.luncert.mx1.core.db.mongo.repo.INodeMetadataRepo;
import org.luncert.mx1.core.dto.NodeListItemDto;
import org.luncert.mx1.core.dto.NodeMetadataDto;
import org.luncert.mx1.core.service.INodeService;
import org.luncert.mx1.core.util.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.net.SocketAddress;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class NodeServiceImpl implements INodeService {
  
  @Autowired
  private INodeMetadataRepo nodeMetadataRepo;
  
  @Override
  public String register(URL remoteAddress, StaticAppInfo staticAppInfo) {
    NodeMetadata metadata = NodeMetadata.builder()
        .id(CommonUtils.netAddrId(remoteAddress))
        .netAddress(remoteAddress)
        
        .build();
    nodeMetadataRepo.save(metadata);
    return metadata.getId().toHexString();
  }
  
  @Override
  public void updateNodeMetadata(String id, Map<String, Object> newMetadata) {
    Optional<NodeMetadata> optional = nodeMetadataRepo.findById(new ObjectId(id));
    if (optional.isPresent()) {
      NodeMetadata metadata = optional.get();
      List<Triple<String, Object, Throwable>> failureList = new LinkedList<>();
      
      // update metadata using reflection
      for (Map.Entry<String, Object> entry : newMetadata.entrySet()) {
        try {
          updateMetadataField(metadata, entry.getKey(), entry.getValue());
        } catch (NoSuchFieldException | IllegalAccessException e) {
          failureList.add(ImmutableTriple.of(entry.getKey(), entry.getValue(), e));
        }
      }
      
      // log failureList
      if (!failureList.isEmpty()) {
        log.error("Some fields are not updated: {}.", failureList);
      }
      return;
    }
    
    log.error("Unable to update node with invalid node id: {}.", id);
  }
  
  private void updateMetadataField(NodeMetadata metadata, String path, Object newValue)
      throws NoSuchFieldException, IllegalAccessException {
    String[] names = path.split("\\.");
    if (names.length == 0) {
      throw new IllegalArgumentException("unable to update metadata with empty path");
    }
    
    Class<?> declaringClass = NodeMetadata.class;
    Object declaringInstance = metadata;
    Field targetField = null;
    for (String name : names) {
      targetField = declaringClass.getDeclaredField(name);
      
      targetField.setAccessible(true);
      declaringClass = targetField.getType();
      declaringInstance = targetField.get(declaringClass);
    }
    
    targetField.setAccessible(true);
    targetField.set(declaringInstance, newValue);
  }
  
  @Override
  public void saveDynamicJvmInfo(DynamicJvmInfo dynamicJvmInfo) {
  
  }
  
  @Override
  public void saveDynamicSysInfo(DynamicSysInfo dynamicSysInfo) {
  
  }
  
  @Override
  public void saveAppLog() {
  
  }
  
  @Override
  public List<NodeListItemDto> getAllNodes() {
    return Lists.newArrayList(nodeMetadataRepo.findAll()).stream()
        .map(nodeMetadata ->
            NodeListItemDto.builder()
                .id(nodeMetadata.getId().toHexString())
                .netAddress(nodeMetadata.getNetAddress().toString())
                .build())
        .collect(Collectors.toList());
  }
  
  @Override
  public Optional<NodeMetadataDto> getNodeMetadata(String nodeId) {
    return nodeMetadataRepo.findById(new ObjectId(nodeId))
        .map(nodeMetadata -> NodeMetadataDto.builder()
            .staticJvmInfo(nodeMetadata.getStaticJvmInfo())
            .staticSysInfo(nodeMetadata.getStaticSysInfo())
            .staticMavenInfo(nodeMetadata.getStaticMavenInfo())
            .build());
  }
}

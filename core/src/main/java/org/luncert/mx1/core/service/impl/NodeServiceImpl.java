package org.luncert.mx1.core.service.impl;

import com.google.common.collect.Lists;
import lombok.Data;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.Triple;
import org.luncert.mx1.commons.data.NetURL;
import org.luncert.mx1.commons.data.dynamicinfo.DynamicJvmInfo;
import org.luncert.mx1.commons.data.dynamicinfo.DynamicSysInfo;
import org.luncert.mx1.commons.data.staticinfo.StaticAppInfo;
import org.luncert.mx1.commons.util.DoubleUtils;
import org.luncert.mx1.core.common.NetAddress;
import org.luncert.mx1.core.db.es.repo.IAppLogRepo;
import org.luncert.mx1.core.db.es.repo.IDynamicJvmInfoRepo;
import org.luncert.mx1.core.db.es.repo.IDynamicSysInfoRepo;
import org.luncert.mx1.core.db.mongo.entity.NodeMetadata;
import org.luncert.mx1.core.db.mongo.repo.INodeMetadataRepo;
import org.luncert.mx1.core.dto.NodeListItemDto;
import org.luncert.mx1.core.dto.NodeMetadataDto;
import org.luncert.mx1.core.service.INodeService;
import org.luncert.mx1.core.util.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

// TODO: check heartbeat
@Slf4j
@Service
public class NodeServiceImpl implements INodeService {
  
  @Autowired
  private INodeMetadataRepo nodeMetadataRepo;
  
  @Autowired
  private IDynamicSysInfoRepo dynamicSysInfoRepo;
  
  @Autowired
  private IDynamicJvmInfoRepo dynamicJvmInfoRepo;
  
  @Autowired
  private IAppLogRepo appLogRepo;
  
  private Map<String, NodeStatus> nodeStatusMap = new ConcurrentHashMap<>();
  
  @Override
  public String register(NetAddress remoteAddress, StaticAppInfo staticAppInfo) {
    NodeMetadata metadata = NodeMetadata.builder()
        .id(CommonUtils.netAddrId(remoteAddress))
        .netAddress(remoteAddress)
        .lastUpdateTimestamp(System.currentTimeMillis())
        .staticSysInfo(staticAppInfo.getStaticSysInfo())
        .staticJvmInfo(staticAppInfo.getStaticJvmInfo())
        .staticMavenInfo(staticAppInfo.getStaticMavenInfo())
        .build();
    nodeMetadataRepo.save(metadata);
    
    updateNodeStatus(metadata.getId());
    
    return metadata.getId();
  }
  
  @Override
  public void updateNodeMetadata(@NonNull String nodeId, Map<String, Object> newMetadata) {
    updateNodeStatus(nodeId);
    updateLastUpdateTimestamp(nodeId);
    
    Optional<NodeMetadata> optional = nodeMetadataRepo.findById(nodeId);
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
        log.error("Some fields are not updated: {}", failureList);
      }
      return;
    }
    
    log.error("Unable to update node with invalid node id: {}", nodeId);
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
  public void saveDynamicJvmInfo(@NonNull String nodeId, DynamicJvmInfo dynamicJvmInfo) {
    updateNodeStatus(nodeId);
    updateLastUpdateTimestamp(nodeId);
    
    dynamicJvmInfoRepo.save(org.luncert.mx1.core.db.es.entity.DynamicJvmInfo.builder()
        .id(CommonUtils.uuid())
        .nodeId(nodeId)
        .garbageCollectorInfoList(dynamicJvmInfo.getGarbageCollectorInfoList())
        .loadedClassCount(dynamicJvmInfo.getLoadedClassCount())
        .totalLoadedClassCount(dynamicJvmInfo.getTotalLoadedClassCount())
        .heapMemoryUsage(DoubleUtils.shortToPercent(dynamicJvmInfo.getHeapMemoryUsage()))
        .maxHeapMemory(dynamicJvmInfo.getMaxHeapMemory())
        .nonHeapMemoryUsage(DoubleUtils.shortToPercent(dynamicJvmInfo.getNonHeapMemoryUsage()))
        .maxNonHeapMemory(dynamicJvmInfo.getMaxNonHeapMemory())
        .activeThreadCount(dynamicJvmInfo.getActiveThreadCount())
        .daemonThreadCount(dynamicJvmInfo.getDaemonThreadCount())
        .totalStartedThreadCount(dynamicJvmInfo.getTotalStartedThreadCount())
        .deadlockedThreadCount(dynamicJvmInfo.getDeadlockedThreadCount())
        .monitorDeadlockedThreadCount(dynamicJvmInfo.getMonitorDeadlockedThreadCount())
        .build());
  }
  
  @Override
  public void saveDynamicSysInfo(@NonNull String nodeId, DynamicSysInfo dynamicSysInfo) {
    updateNodeStatus(nodeId);
    updateLastUpdateTimestamp(nodeId);
    
    dynamicSysInfoRepo.save(org.luncert.mx1.core.db.es.entity.DynamicSysInfo.builder()
        .id(CommonUtils.uuid())
        .nodeId(nodeId)
        .loadAverage(dynamicSysInfo.getLoadAverage())
        .cpuUsage(dynamicSysInfo.getCpuUsage())
        .memUsage(dynamicSysInfo.getMemUsage())
        .swapUsage(dynamicSysInfo.getSwapUsage())
        .build());
  }
  
  @Override
  public void saveAppLog(@NonNull String nodeId) {
    updateNodeStatus(nodeId);
    updateLastUpdateTimestamp(nodeId);
    
  }
  
  private void updateLastUpdateTimestamp(String nodeId) {
    nodeMetadataRepo.findById(nodeId)
        .ifPresent(metadata -> {
          metadata.setLastUpdateTimestamp(System.currentTimeMillis());
          nodeMetadataRepo.save(metadata);
        });
  }
  
  private void updateNodeStatus(String nodeId) {
    nodeStatusMap.compute(nodeId, (id, node) -> {
      if (node == null) {
        node = new NodeStatus();
      }
      node.setAlive(true);
      return node;
    });
  }
  
  @Override
  public List<NodeListItemDto> getAllNodes() {
    return Lists.newArrayList(nodeMetadataRepo.findAll()).stream()
        .map(nodeMetadata ->
            NodeListItemDto.builder()
                .id(nodeMetadata.getId())
                .netAddress(nodeMetadata.getNetAddress().toString())
                .build())
        .collect(Collectors.toList());
  }
  
  @Override
  public Optional<NodeMetadataDto> getNodeMetadata(String nodeId) {
    return nodeMetadataRepo.findById(nodeId)
        .map(nodeMetadata -> NodeMetadataDto.builder()
            .staticJvmInfo(nodeMetadata.getStaticJvmInfo())
            .staticSysInfo(nodeMetadata.getStaticSysInfo())
            .staticMavenInfo(nodeMetadata.getStaticMavenInfo())
            .build());
  }
  
  @Data
  private static class NodeStatus {
    
    boolean alive;
  }
}

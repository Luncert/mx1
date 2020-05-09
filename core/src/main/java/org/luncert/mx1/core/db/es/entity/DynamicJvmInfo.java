package org.luncert.mx1.core.db.es.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.luncert.mx1.commons.data.dynamicinfo.GarbageCollectorInfo;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.io.Serializable;
import java.util.List;

/**
 * copy of {@link org.luncert.mx1.commons.data.dynamicinfo.DynamicJvmInfo}
 */
@Data
@Document(indexName = "dynamic_jvm_info", type="java")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DynamicJvmInfo implements Serializable {
  
  private static final long serialVersionUID = -8521757091605265356L;
  
  @Id
  private String id;
  
  private List<GarbageCollectorInfo> garbageCollectorInfoList;
  
  private int loadedClassCount;
  
  private long totalLoadedClassCount;
  
  private double heapMemoryUsage;
  
  private long maxHeapMemory;
  
  private double nonHeapMemoryUsage;
  
  private long maxNonHeapMemory;
  
  private int activeThreadCount;
  
  private int daemonThreadCount;
  
  private long totalStartedThreadCount;
  
  private int deadlockedThreadCount;
  
  private int monitorDeadlockedThreadCount;
}

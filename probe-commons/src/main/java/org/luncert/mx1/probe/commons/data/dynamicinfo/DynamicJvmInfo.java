package org.luncert.mx1.probe.commons.data.dynamicinfo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

// ref: https://blog.csdn.net/csdnzhangtao5/article/details/72625082
@Data
public class DynamicJvmInfo implements Serializable {
  
  private static final long serialVersionUID = 3485574832430913043L;
  
  private List<GarbageCollectorInfo> garbageCollectorInfoList;
  
  /**
   * the number of classes that are currently loaded in the
   * Java virtual machine.
   */
  private int loadedClassCount;
  
  /**
   * the total number of classes that have been loaded since
   * the Java virtual machine has started execution.
   */
  private long totalLoadedClassCount;
  
  /**
   * the total number of classes unloaded since the Java virtual machine
   * has started execution.
   */
  private long unloadedClassCount;
  
  /**
   * percent of used heap memory
   */
  private short heapMemoryUsage;
  
  private long maxHeapMemory;
  
  /**
   * percent of used non heap memory
   */
  private short nonHeapMemoryUsage;
  
  private long maxNonHeapMemory;
  
  /**
   * the current number of live threads including both
   * daemon and non-daemon threads.
   */
  private int activeThreadCount;
  
  /**
   * the current number of live daemon threads.
   */
  private int daemonThreadCount;
  
  /**
   * the total number of threads created and also started
   * since the Java virtual machine started.
   */
  private long totalStartedThreadCount;
  
  /**
   * see {@link java.lang.management.ThreadMXBean#findDeadlockedThreads()}
   */
  private int deadlockedThreadCount;
  
  /**
   * see {@link java.lang.management.ThreadMXBean#findMonitorDeadlockedThreads()}
   */
  private int monitorDeadlockedThreadCount;
}

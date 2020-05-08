package org.luncert.mx1.probe.stub.component.collector.dynamicinfo;

import org.luncert.mx1.commons.data.dynamicinfo.DynamicJvmInfo;
import org.luncert.mx1.commons.data.dynamicinfo.GarbageCollectorInfo;
import org.luncert.mx1.commons.util.DoubleUtils;
import org.luncert.mx1.probe.stub.component.collector.AbstractInfoCollector;
import org.luncert.mx1.probe.stub.pojo.CollectorResponse;

import java.lang.management.ClassLoadingMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.lang.management.ThreadMXBean;
import java.util.LinkedList;
import java.util.List;

public class DynamicJvmInfoCollector extends AbstractInfoCollector<DynamicJvmInfo> {
  
  @Override
  public CollectorResponse<DynamicJvmInfo> collect() {
    DynamicJvmInfo info = new DynamicJvmInfo();
    
    // get class loading info
    ClassLoadingMXBean classLoadingMXBean = ManagementFactory.getClassLoadingMXBean();
    info.setLoadedClassCount(classLoadingMXBean.getLoadedClassCount());
    info.setTotalLoadedClassCount(classLoadingMXBean.getTotalLoadedClassCount());
    
    // get garbage collector info
    List<GarbageCollectorInfo> garbageInfoList = new LinkedList<>();
    ManagementFactory.getGarbageCollectorMXBeans().forEach(mxBean ->
        garbageInfoList.add(
            GarbageCollectorInfo.builder()
                .name(mxBean.getName())
                .collectionCount(mxBean.getCollectionCount())
                .collectionTime(mxBean.getCollectionTime())
                .build())
    );
    info.setGarbageCollectorInfoList(garbageInfoList);
    
    // get jvm memory info
    MemoryMXBean memMXBean = ManagementFactory.getMemoryMXBean();
    
    MemoryUsage heapMemUsage = memMXBean.getHeapMemoryUsage();
    info.setMaxHeapMemory(heapMemUsage.getMax());
    if (info.getMaxHeapMemory() > 0) {
      info.setHeapMemoryUsage(DoubleUtils.percentToShort(
          ((double) heapMemUsage.getUsed()) / info.getMaxHeapMemory()));
    } else {
      info.setHeapMemoryUsage((short) -1);
    }

    MemoryUsage nonHeapMemUsage = memMXBean.getNonHeapMemoryUsage();
    info.setMaxNonHeapMemory(nonHeapMemUsage.getMax());
    if (info.getMaxNonHeapMemory() > 0) {
      info.setNonHeapMemoryUsage(DoubleUtils.percentToShort(
          ((double) nonHeapMemUsage.getUsed()) / info.getMaxNonHeapMemory()));
    } else {
      info.setNonHeapMemoryUsage((short) -1);
    }
  
    // get thread info
    ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
    info.setActiveThreadCount(threadMXBean.getThreadCount());
    info.setDaemonThreadCount(threadMXBean.getDaemonThreadCount());
    info.setTotalStartedThreadCount(threadMXBean.getTotalStartedThreadCount());
    info.setDeadlockedThreadCount(getNullableArraySize(
        threadMXBean.findDeadlockedThreads()));
    info.setMonitorDeadlockedThreadCount(getNullableArraySize(
        threadMXBean.findMonitorDeadlockedThreads()));
    
    
    return CollectorResponse.succeed(info);
  }
  
  private int getNullableArraySize(long[] array) {
    return array == null ? 0 : array.length;
  }
}

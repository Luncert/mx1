package org.luncert.mx1.probe.stub.component.collector.dynamicinfo;

import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.Mem;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.Swap;
import org.luncert.mx1.probe.commons.data.dynamicinfo.DynamicSystemInfo;
import org.luncert.mx1.probe.commons.util.DoubleUtil;
import org.luncert.mx1.probe.stub.component.collector.AbstractInfoCollector;
import org.luncert.mx1.probe.stub.pojo.CollectorResponse;

import java.lang.management.ManagementFactory;

public class DynamicSystemInfoCollector extends AbstractInfoCollector<DynamicSystemInfo> {
  
  private Sigar sigar;
  
  // TODO: catch exception
  @Override
  protected void init() {
    // possible exception: no sigar-amd64-winnt.dll in java.library.path
    // see: https://stackoverflow.com/questions/27404471/org-hyperic-sigar-sigarexception-no-sigar-amd64-winnt-dll-in-java-library-path
    sigar = new Sigar();
  }
  
  @Override
  public CollectorResponse<DynamicSystemInfo> collect() {
    DynamicSystemInfo info = new DynamicSystemInfo();
  
    try {
      info.setLoadAverage(ManagementFactory.getOperatingSystemMXBean().getSystemLoadAverage());
      
      CpuPerc cpuPerc = sigar.getCpuPerc();
      info.setCpuUsage(DoubleUtil.percentToShort(cpuPerc.getCombined() * 100));
      
      Mem mem = sigar.getMem();
      info.setMemUsage(DoubleUtil.percentToShort(mem.getUsedPercent()));
      
      Swap swap = sigar.getSwap();
      info.setSwapUsage(DoubleUtil.percentToShort(swap.getUsed() / swap.getTotal()));
    } catch (SigarException e) {
      return CollectorResponse.failed(e.getMessage());
    }
  
    return CollectorResponse.succeed(info);
  }
}

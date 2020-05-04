package org.luncert.mx1.probe.stub.component.collector.dynamicinfo;

import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.Mem;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.Swap;
import org.luncert.mx1.probe.commons.data.dynamicinfo.SystemInfo;
import org.luncert.mx1.probe.stub.component.collector.AbstractInfoCollector;
import org.luncert.mx1.probe.stub.pojo.CollectorResponse;

public class SystemInfoCollector extends AbstractInfoCollector<SystemInfo> {
  
  private Sigar sigar;
  
  // TODO: catch exception
  @Override
  protected void init() {
    sigar = new Sigar();
  }
  
  @Override
  public CollectorResponse<SystemInfo> collect() {
    SystemInfo info = new SystemInfo();
  
    try {
      CpuPerc cpuPerc = sigar.getCpuPerc();
      info.setCpuUsage( cpuPerc.getCombined() * 100);
      
      Mem mem = sigar.getMem();
      info.setMemUsage(mem.getUsedPercent());
      
      Swap swap = sigar.getSwap();
      info.setSwapUsage(swap.getUsed() / swap.getTotal());
    } catch (SigarException e) {
      return CollectorResponse.failed(e.getMessage());
    }
  
    return CollectorResponse.succeed(info);
  }
}

package org.luncert.mx1.probe.stub.component.collector.staticinfo;

import org.luncert.mx1.probe.commons.data.staticinfo.JvmStaticInfo;
import org.luncert.mx1.probe.commons.util.SystemPropertiesUtil;
import org.luncert.mx1.probe.stub.component.collector.AbstractInfoCollector;
import org.luncert.mx1.probe.stub.pojo.CollectorResponse;

import java.lang.management.ManagementFactory;

public class StaticJvmInfoCollector extends AbstractInfoCollector<JvmStaticInfo> {
  
  @Override
  public CollectorResponse<JvmStaticInfo> collect() {
    JvmStaticInfo info = new JvmStaticInfo();
    
    SystemPropertiesUtil.fill(info);
  
    info.setSetupArguments(ManagementFactory.getRuntimeMXBean().getInputArguments());
    
    Runtime runtime = Runtime.getRuntime();
    info.setTotalMemory(runtime.totalMemory());
    info.setMaxMemory(runtime.maxMemory());
  
    info.setCompilerName(ManagementFactory.getCompilationMXBean().getName());
  
    return CollectorResponse.succeed(info);
  }
}

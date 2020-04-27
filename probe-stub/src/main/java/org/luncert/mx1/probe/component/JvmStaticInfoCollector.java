package org.luncert.mx1.probe.component;

import org.luncert.mx1.probe.data.staticinfo.JvmStaticInfo;
import org.luncert.mx1.probe.util.SystemPropertiesUtil;

import java.lang.management.ManagementFactory;

public class JvmStaticInfoCollector extends AbstractInfoCollector<JvmStaticInfo> {
  
  @Override
  public JvmStaticInfo collect() {
    JvmStaticInfo info = new JvmStaticInfo();
    
    SystemPropertiesUtil.fill(info);
  
    info.setSetupArguments(ManagementFactory.getRuntimeMXBean().getInputArguments());
    
    Runtime runtime = Runtime.getRuntime();
    info.setTotalMemory(runtime.totalMemory());
    info.setMaxMemory(runtime.maxMemory());
    
    return info;
  }
}

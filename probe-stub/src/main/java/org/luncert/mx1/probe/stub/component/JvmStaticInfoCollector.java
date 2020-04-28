package org.luncert.mx1.probe.stub.component;

import org.luncert.mx1.probe.commons.data.staticinfo.JvmStaticInfo;
import org.luncert.mx1.probe.commons.util.SystemPropertiesUtil;

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

package org.luncert.mx1.probe.component;

import org.luncert.mx1.probe.data.staticinfo.SystemStaticInfo;
import org.luncert.mx1.probe.util.SystemPropertiesUtil;

public class SystemStaticInfoCollector extends AbstractInfoCollector<SystemStaticInfo> {
  
  @Override
  public SystemStaticInfo collect() {
    SystemStaticInfo info = new SystemStaticInfo();
    SystemPropertiesUtil.fill(info);
    info.setEnv(System.getenv());
    return info;
  }
}

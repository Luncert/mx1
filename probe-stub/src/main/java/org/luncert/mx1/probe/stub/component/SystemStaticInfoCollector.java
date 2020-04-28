package org.luncert.mx1.probe.stub.component;

import org.luncert.mx1.probe.commons.data.staticinfo.SystemStaticInfo;
import org.luncert.mx1.probe.commons.util.SystemPropertiesUtil;

public class SystemStaticInfoCollector extends AbstractInfoCollector<SystemStaticInfo> {
  
  @Override
  public SystemStaticInfo collect() {
    SystemStaticInfo info = new SystemStaticInfo();
    SystemPropertiesUtil.fill(info);
    info.setEnv(System.getenv());
    return info;
  }
}

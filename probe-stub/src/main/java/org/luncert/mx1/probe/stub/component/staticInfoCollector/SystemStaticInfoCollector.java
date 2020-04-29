package org.luncert.mx1.probe.stub.component.staticInfoCollector;

import org.luncert.mx1.probe.commons.data.staticinfo.SystemStaticInfo;
import org.luncert.mx1.probe.commons.util.SystemPropertiesUtil;
import org.luncert.mx1.probe.stub.component.AbstractInfoCollector;

public class SystemStaticInfoCollector extends AbstractInfoCollector<SystemStaticInfo> {
  
  @Override
  public SystemStaticInfo collect() {
    SystemStaticInfo info = new SystemStaticInfo();
    SystemPropertiesUtil.fill(info);
    info.setEnv(System.getenv());
    return info;
  }
}

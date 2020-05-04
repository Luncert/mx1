package org.luncert.mx1.probe.stub.component.collector.staticinfo;

import org.luncert.mx1.probe.commons.data.staticinfo.SystemStaticInfo;
import org.luncert.mx1.probe.commons.util.SystemPropertiesUtil;
import org.luncert.mx1.probe.stub.component.collector.AbstractInfoCollector;
import org.luncert.mx1.probe.stub.pojo.CollectorResponse;

public class SystemStaticInfoCollector extends AbstractInfoCollector<SystemStaticInfo> {
  
  @Override
  public CollectorResponse<SystemStaticInfo> collect() {
    SystemStaticInfo info = new SystemStaticInfo();
    SystemPropertiesUtil.fill(info);
    info.setEnv(System.getenv());
    return CollectorResponse.succeed(info);
  }
}

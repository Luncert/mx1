package org.luncert.mx1.probe.stub.component.collector.staticinfo;

import org.luncert.mx1.probe.commons.data.staticinfo.DynamicSystemInfo;
import org.luncert.mx1.probe.commons.util.SystemPropertiesUtil;
import org.luncert.mx1.probe.stub.component.collector.AbstractInfoCollector;
import org.luncert.mx1.probe.stub.pojo.CollectorResponse;

public class StaticSystemInfoCollector extends AbstractInfoCollector<DynamicSystemInfo> {
  
  @Override
  public CollectorResponse<DynamicSystemInfo> collect() {
    DynamicSystemInfo info = new DynamicSystemInfo();
    SystemPropertiesUtil.fill(info);
    info.setEnv(System.getenv());
    return CollectorResponse.succeed(info);
  }
}

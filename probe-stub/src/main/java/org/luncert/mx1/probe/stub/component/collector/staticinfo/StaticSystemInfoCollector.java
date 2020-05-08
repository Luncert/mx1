package org.luncert.mx1.probe.stub.component.collector.staticinfo;

import org.luncert.mx1.commons.data.staticinfo.StaticSystemInfo;
import org.luncert.mx1.commons.util.SystemPropertiesUtil;
import org.luncert.mx1.probe.stub.component.collector.AbstractInfoCollector;
import org.luncert.mx1.probe.stub.pojo.CollectorResponse;

public class StaticSystemInfoCollector extends AbstractInfoCollector<StaticSystemInfo> {
  
  @Override
  public CollectorResponse<StaticSystemInfo> collect() {
    StaticSystemInfo info = new StaticSystemInfo();
    SystemPropertiesUtil.fill(info);
    info.setEnv(System.getenv());
    return CollectorResponse.succeed(info);
  }
}

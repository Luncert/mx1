package org.luncert.mx1.probe.stub.component.collector.staticinfo;

import org.luncert.mx1.commons.data.staticinfo.StaticSysInfo;
import org.luncert.mx1.commons.util.SystemPropertiesUtil;
import org.luncert.mx1.probe.stub.component.collector.AbstractInfoCollector;
import org.luncert.mx1.probe.stub.pojo.CollectorResponse;

public class StaticSysInfoCollector extends AbstractInfoCollector<StaticSysInfo> {
  
  @Override
  public CollectorResponse<StaticSysInfo> collect() {
    StaticSysInfo info = new StaticSysInfo();
    SystemPropertiesUtil.fill(info);
    info.setEnv(System.getenv());
    return CollectorResponse.succeed(info);
  }
}

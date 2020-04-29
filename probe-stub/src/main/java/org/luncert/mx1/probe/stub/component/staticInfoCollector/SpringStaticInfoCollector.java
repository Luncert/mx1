package org.luncert.mx1.probe.stub.component.staticInfoCollector;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.luncert.mx1.probe.commons.data.staticinfo.SpringStaticInfo;
import org.luncert.mx1.probe.spy.ProbeSpy;
import org.luncert.mx1.probe.stub.common.ProbeSpyEvent;
import org.luncert.mx1.probe.stub.component.AbstractInfoCollector;

public class SpringStaticInfoCollector extends AbstractInfoCollector<SpringStaticInfo> {
  
  @Override
  public SpringStaticInfo collect() {
    SpringStaticInfo info = new SpringStaticInfo();
    String jsonInfo = ProbeSpy.fireEvent(ProbeSpyEvent.LOAD_SPRING_STATIC_INFO);
    JSONObject json = JSON.parseObject(jsonInfo);
    System.out.println(json);
    return info;
  }
}

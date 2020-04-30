package org.luncert.mx1.probe.stub.component;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.luncert.mx1.probe.commons.data.staticinfo.SystemStaticInfo;
import org.luncert.mx1.probe.stub.component.staticInfoCollector.SystemStaticInfoCollector;
import org.luncert.mx1.probe.stub.pojo.CollectorResponse;

@RunWith(JUnit4.class)
public class SystemStaticInfoCollectorTest {
  
  @Test
  public void test() {
    SystemStaticInfoCollector collector = new SystemStaticInfoCollector();
    CollectorResponse<SystemStaticInfo> response = collector.collect();
    Assert.assertTrue(response.isSuccess());
  }
}

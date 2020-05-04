package org.luncert.mx1.probe.stub.component;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.luncert.mx1.probe.commons.data.staticinfo.JvmStaticInfo;
import org.luncert.mx1.probe.stub.component.collector.staticinfo.JvmStaticInfoCollector;
import org.luncert.mx1.probe.stub.pojo.CollectorResponse;

@RunWith(JUnit4.class)
public class JvmStaticInfoCollectorTest {
  
  @Test
  public void test() {
    JvmStaticInfoCollector collector = new JvmStaticInfoCollector();
    CollectorResponse<JvmStaticInfo> response = collector.collect();
    Assert.assertTrue(response.isSuccess());
  }
}

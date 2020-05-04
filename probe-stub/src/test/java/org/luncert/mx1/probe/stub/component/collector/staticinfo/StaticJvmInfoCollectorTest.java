package org.luncert.mx1.probe.stub.component.collector.staticinfo;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.luncert.mx1.probe.commons.data.staticinfo.JvmStaticInfo;
import org.luncert.mx1.probe.stub.pojo.CollectorResponse;

@RunWith(JUnit4.class)
public class StaticJvmInfoCollectorTest {
  
  @Test
  public void test() {
    StaticJvmInfoCollector collector = new StaticJvmInfoCollector();
    CollectorResponse<JvmStaticInfo> response = collector.collect();
    Assert.assertTrue(response.getDescription(), response.isSuccess());
    System.out.println(response.getInfo());
  }
}

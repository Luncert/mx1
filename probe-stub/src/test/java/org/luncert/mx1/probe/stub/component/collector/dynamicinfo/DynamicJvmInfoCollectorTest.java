package org.luncert.mx1.probe.stub.component.collector.dynamicinfo;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.luncert.mx1.probe.commons.data.dynamicinfo.DynamicJvmInfo;
import org.luncert.mx1.probe.stub.pojo.CollectorResponse;

@RunWith(JUnit4.class)
public class DynamicJvmInfoCollectorTest {
  
  @Test
  public void testSuccess() {
    DynamicJvmInfoCollector collector = new DynamicJvmInfoCollector();
    CollectorResponse<DynamicJvmInfo> rep = collector.collect();
    Assert.assertTrue(rep.getDescription(), rep.isSuccess());
    System.out.println(rep.getInfo());
  }
}

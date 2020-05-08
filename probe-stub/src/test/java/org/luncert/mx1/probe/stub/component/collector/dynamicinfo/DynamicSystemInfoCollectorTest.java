package org.luncert.mx1.probe.stub.component.collector.dynamicinfo;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.luncert.mx1.commons.data.dynamicinfo.DynamicSystemInfo;
import org.luncert.mx1.probe.stub.pojo.CollectorResponse;

@RunWith(JUnit4.class)
public class DynamicSystemInfoCollectorTest {
  
  @Test
  public void testSuccess() {
    DynamicSystemInfoCollector collector = new DynamicSystemInfoCollector();
  
    CollectorResponse<DynamicSystemInfo> rep = collector.collect();
    Assert.assertTrue(rep.getDescription(), rep.isSuccess());
    System.out.println(rep.getInfo());
  }
}

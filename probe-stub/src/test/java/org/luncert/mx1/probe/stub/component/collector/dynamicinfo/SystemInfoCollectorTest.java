package org.luncert.mx1.probe.stub.component.collector.dynamicinfo;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.luncert.mx1.probe.commons.data.dynamicinfo.SystemInfo;
import org.luncert.mx1.probe.stub.pojo.CollectorResponse;

@RunWith(JUnit4.class)
public class SystemInfoCollectorTest {
  
  @Test
  public void testSuccess() {
    SystemInfoCollector collector = new SystemInfoCollector();
    collector.init();
  
    CollectorResponse<SystemInfo> rep = collector.collect();
    Assert.assertTrue(rep.isSuccess());
    System.out.println(rep.getInfo());
  }
}

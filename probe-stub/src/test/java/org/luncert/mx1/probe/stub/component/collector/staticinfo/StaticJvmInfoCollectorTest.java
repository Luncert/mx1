package org.luncert.mx1.probe.stub.component.collector.staticinfo;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.luncert.mx1.commons.data.staticinfo.StaticJvmInfo;
import org.luncert.mx1.probe.stub.pojo.CollectorResponse;

@RunWith(JUnit4.class)
public class StaticJvmInfoCollectorTest {
  
  @Test
  public void test() {
    StaticJvmInfoCollector collector = new StaticJvmInfoCollector();
    CollectorResponse<StaticJvmInfo> rep = collector.collect();
    Assert.assertTrue(rep.isSuccess());
    System.out.println(rep.getInfo());
  }
}

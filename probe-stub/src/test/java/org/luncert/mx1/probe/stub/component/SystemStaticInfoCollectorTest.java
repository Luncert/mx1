package org.luncert.mx1.probe.stub.component;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.luncert.mx1.probe.commons.data.staticinfo.SystemStaticInfo;

@RunWith(JUnit4.class)
public class SystemStaticInfoCollectorTest {
  
  @Test
  public void test() {
    SystemStaticInfoCollector collector = new SystemStaticInfoCollector();
    SystemStaticInfo info = collector.collect();
    System.out.println(info);
  }
}

package org.luncert.mx1.probe.stub.component;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.luncert.mx1.probe.commons.data.staticinfo.JvmStaticInfo;
import org.luncert.mx1.probe.stub.component.staticInfoCollector.JvmStaticInfoCollector;

@RunWith(JUnit4.class)
public class JvmStaticInfoCollectorTest {
  
  @Test
  public void test() {
    JvmStaticInfoCollector collector = new JvmStaticInfoCollector();
    JvmStaticInfo info = collector.collect();
    System.out.println(info);
  }
}

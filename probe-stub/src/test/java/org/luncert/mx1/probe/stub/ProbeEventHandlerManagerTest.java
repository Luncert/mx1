package org.luncert.mx1.probe.stub;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class ProbeEventHandlerManagerTest {
  
  @Test
  public void testSuccess() {
    ProbeEventHandlerManager.register("org.luncert.mx1.probe.stub.component.probeEventHandler");
  }
}

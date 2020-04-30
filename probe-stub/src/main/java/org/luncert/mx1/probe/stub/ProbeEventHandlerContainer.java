package org.luncert.mx1.probe.stub;

import org.luncert.mx1.probe.spy.ProbeSpy;
import org.luncert.mx1.probe.stub.component.SpringContextInjectHandler;

public class ProbeEventHandlerContainer {
  
  public static void register() {
    ProbeSpy.register("EVT_INJECT_SPRING_CONTEXT", new SpringContextInjectHandler());
  }
}

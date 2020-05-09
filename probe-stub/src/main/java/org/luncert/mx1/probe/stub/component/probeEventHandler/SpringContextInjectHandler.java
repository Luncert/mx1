package org.luncert.mx1.probe.stub.component.probeEventHandler;

import org.luncert.mx1.commons.data.staticinfo.StaticSpringInfo;
import org.luncert.mx1.probe.spy.Event;
import org.luncert.mx1.probe.stub.annotation.ProbeEventHandler;

public class SpringContextInjectHandler {
  
  @ProbeEventHandler("EVT_INJECT_SPRING_CONTEXT")
  public static Object injectSpringContext(Event<Object> event) {
    Object data = event.getData();
  
    // TODO: analyse app ctx and submit info
    // https://spring.io/blog/2007/12/21/spring-integration-samples
    StaticSpringInfo info = new StaticSpringInfo();
    
    return null;
  }
  
  private static void collect() {
  
  }
}

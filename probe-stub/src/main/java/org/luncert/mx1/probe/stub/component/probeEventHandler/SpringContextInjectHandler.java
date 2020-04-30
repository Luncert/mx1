package org.luncert.mx1.probe.stub.component.probeEventHandler;

import org.luncert.mx1.probe.commons.data.staticinfo.SpringStaticInfo;
import org.luncert.mx1.probe.spy.Event;
import org.luncert.mx1.probe.stub.annotation.ProbeEventHandler;
import org.springframework.context.ApplicationContext;

public class SpringContextInjectHandler {
  
  @ProbeEventHandler("EVT_INJECT_SPRING_CONTEXT")
  public static Object handle(Event<ApplicationContext> event) {
    ApplicationContext applicationContext = event.getData();
  
    // TODO: analyse app ctx and submit info
    // https://spring.io/blog/2007/12/21/spring-integration-samples
    SpringStaticInfo info = new SpringStaticInfo();
    
    return null;
  }
}

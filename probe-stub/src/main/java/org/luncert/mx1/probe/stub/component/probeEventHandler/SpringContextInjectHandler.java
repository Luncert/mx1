package org.luncert.mx1.probe.stub.component.probeEventHandler;

import org.luncert.mx1.probe.commons.data.staticinfo.SpringStaticInfo;
import org.luncert.mx1.probe.spy.Event;
import org.luncert.mx1.probe.spy.ProbeEventHandler;
import org.springframework.context.ApplicationContext;

public class SpringContextInjectHandler extends ProbeEventHandler<ApplicationContext> {
  
  @Override
  public Object handle(Event<ApplicationContext> event) {
    ApplicationContext applicationContext = event.getData();
  
    // TODO: analyse app ctx and submit info
    SpringStaticInfo info = new SpringStaticInfo();
    
    return null;
  }
}

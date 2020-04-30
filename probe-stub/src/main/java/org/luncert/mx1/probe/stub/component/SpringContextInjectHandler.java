package org.luncert.mx1.probe.stub.component;

import org.luncert.mx1.probe.spy.Event;
import org.luncert.mx1.probe.spy.ProbeEventHandler;
import org.springframework.context.ApplicationContext;

public class SpringContextInjectHandler extends ProbeEventHandler<ApplicationContext> {
  
  @Override
  public Object handle(Event<ApplicationContext> event) {
    System.out.println(event);
    return null;
  }
}

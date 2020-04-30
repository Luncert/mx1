package org.luncert.mx1.probe.stub.component.probeEventHandler;

import org.luncert.mx1.probe.spy.Event;
import org.luncert.mx1.probe.spy.ProbeEventHandler;

public class WriteStdoutHandler extends ProbeEventHandler<String> {
  
  @Override
  public Object handle(Event<String> event) {
    // TODO: submit to probe-daemon
    return null;
  }
}

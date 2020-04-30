package org.luncert.mx1.probe.stub.component.probeEventHandler;

import org.luncert.mx1.probe.spy.Event;
import org.luncert.mx1.probe.stub.annotation.ProbeEventHandler;

public class WriteStdoutHandler {
  
  @ProbeEventHandler("EVT_WRITE_STDOUT")
  public static Object handle(Event<String> event) {
    // TODO: submit to probe-daemon
    return null;
  }
}

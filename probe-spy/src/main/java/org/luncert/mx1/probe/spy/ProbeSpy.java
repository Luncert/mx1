package org.luncert.mx1.probe.spy;

import lombok.extern.slf4j.Slf4j;
import org.luncert.mx1.probe.spy.exception.EventHandlerExistedError;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Used to connect target app and probe.
 * Will be added to Bootstrap Classloader's search path.
 */
@Slf4j
public final class ProbeSpy {
  
  private static final ConcurrentHashMap<String, ProbeEventHandler> probeEventHandlerMap
      = new ConcurrentHashMap<String, ProbeEventHandler>();
  
  private ProbeSpy() {}
  
  /**
   * register ProbeEventHandler
   */
  public static void register(String evtName, ProbeEventHandler handler) {
    if (probeEventHandlerMap.containsKey(evtName)) {
      throw new EventHandlerExistedError("event name=" + evtName);
    }
    probeEventHandlerMap.put(evtName, handler);
  }
  
  public static Object fireEvent(String evtName) {
    return fireEvent(evtName, null);
  }
  
  @SuppressWarnings("unchecked")
  public static Object fireEvent(String evtName, Object data) {
    ProbeEventHandler handler = probeEventHandlerMap.get(evtName);
    if (handler != null) {
      Event event = new Event(evtName, data);
      log.debug("{} accepted by {}", event, handler.getClass());
      return handler.handle(event);
    }
    
    return null;
  }
}

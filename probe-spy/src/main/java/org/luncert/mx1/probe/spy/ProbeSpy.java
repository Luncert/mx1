package org.luncert.mx1.probe.spy;

import org.luncert.mx1.probe.spy.exception.EventHandlerExistedError;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Used to connect target app and probe.
 * Will be added to Bootstrap Classloader's search path.
 */
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
  
  public static <T> T fireEvent(String evtName) {
    return fireEvent(evtName, null);
  }
  
  @SuppressWarnings("unchecked")
  public static <T> T fireEvent(String evtName, Object data) {
    ProbeEventHandler handler = probeEventHandlerMap.get(evtName);
    if (handler != null) {
      Event event = new Event(evtName, data);
      return (T) handler.handle(event);
    }
    
    return null;
  }
}

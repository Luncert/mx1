package org.luncert.mx1.probe.spy;

public abstract class ProbeEventHandler<E> {
  
  public abstract Object handle(Event<E> event);
}

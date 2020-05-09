package org.luncert.mx1.core.component;

import java.io.IOException;

public abstract class ProbeListener<E> {
  
  public void onConnected(Session session) {}
  
  public abstract void handleData(Session session, E data) throws IOException;
  
  public void onException(Session session, Throwable cause) {}
  
  public void onDisconnected(Session session) {}
}

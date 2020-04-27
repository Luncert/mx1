package org.luncert.mx1.probe.component;

public abstract class AbstractInfoCollector<T> {
  
  protected void init() {}
  
  public abstract T collect();
}

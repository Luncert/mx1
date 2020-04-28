package org.luncert.mx1.probe.stub.component;

public abstract class AbstractInfoCollector<T> {
  
  protected void init() {}
  
  public abstract T collect();
}

package org.luncert.mx1.probe.stub.component.collector;

import org.luncert.mx1.probe.stub.pojo.CollectorResponse;

public abstract class AbstractInfoCollector<T> {
  
  protected void init() {}
  
  public abstract CollectorResponse<T> collect();
}

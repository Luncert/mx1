package org.luncert.mx1.probe.stub;

import org.luncert.mx1.probe.commons.util.Invokable;

import java.util.LinkedList;
import java.util.List;

public class ProbeStubCleaner implements Runnable {
  
  private List<Invokable> shutdownTask = new LinkedList<>();
  
  public ProbeStubCleaner addShutdownTask(Invokable invokable) {
    return this;
  }
  
  @Override
  public void run() {
  
  }
}

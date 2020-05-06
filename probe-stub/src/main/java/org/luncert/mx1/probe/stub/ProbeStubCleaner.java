package org.luncert.mx1.probe.stub;

import lombok.extern.slf4j.Slf4j;
import org.luncert.mx1.probe.commons.util.Invokable;

import java.util.LinkedList;
import java.util.List;

@Slf4j
public class ProbeStubCleaner implements Runnable {
  
  private List<Invokable> shutdownTasks = new LinkedList<>();
  
  public ProbeStubCleaner addShutdownTask(Invokable invokable) {
    shutdownTasks.add(invokable);
    return this;
  }
  
  @Override
  public void run() {
    for (Invokable invokable : shutdownTasks) {
      try {
        invokable.apply();
      } catch (Exception e) {
        log.error("Failed to execute shutdown task {}.", invokable, e);
      }
    }
  }
}

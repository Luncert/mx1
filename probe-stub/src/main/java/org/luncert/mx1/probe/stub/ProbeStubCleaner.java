package org.luncert.mx1.probe.stub;

import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;
import java.util.List;

@Slf4j
public class ProbeStubCleaner implements Runnable {
  
  private List<ShutdownHook> shutdownTasks = new LinkedList<>();
  
  public ProbeStubCleaner addShutdownTask(ShutdownHook hook) {
    shutdownTasks.add(hook);
    return this;
  }
  
  @Override
  public void run() {
    for (ShutdownHook task : shutdownTasks) {
      try {
        task.run();
      } catch (Exception e) {
        log.error("Failed to execute shutdown task", e);
      }
    }
  }
  
  @FunctionalInterface
  public interface ShutdownHook {
    
    void run() throws Exception;
  }
}

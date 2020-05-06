package org.luncert.mx1.probe.stub.component.collector;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.luncert.mx1.probe.commons.data.IpcAction;
import org.luncert.mx1.probe.commons.data.IpcPacket;
import org.luncert.mx1.probe.ipc.IpcChannel;
import org.luncert.mx1.probe.stub.component.collector.dynamicinfo.DynamicJvmInfoCollector;
import org.luncert.mx1.probe.stub.component.collector.dynamicinfo.DynamicSystemInfoCollector;
import org.luncert.mx1.probe.stub.pojo.CollectorResponse;

import java.util.PriorityQueue;

@Slf4j
public final class CollectorScheduler implements Runnable {
  
  private CollectorRegistry registry;
  
  private IpcChannel ipcChannel;
  
  private PriorityQueue<Plan> plans;
  
  private Thread thread;
  
  private volatile boolean stopSignal = false;
  
  public CollectorScheduler(CollectorRegistry registry, IpcChannel ipcChannel) {
    this.registry = registry;
    this.ipcChannel = ipcChannel;
    
    plans = new PriorityQueue<>(2, (p1, p2) -> {
      long t1 = p1.getRemainingTime();
      long t2 = p2.getRemainingTime();
      return Long.compare(t1, t2);
    });
    plans.add(new Plan(DynamicSystemInfoCollector.class.getName(), 100));
    plans.add(new Plan(DynamicJvmInfoCollector.class.getName(), 100));
  }
  
  public void start() {
    thread = new Thread(this);
    thread.setDaemon(true);
    thread.start();
  }
  
  @Override
  public void run() {
    try {
      while (!stopSignal) {
        Plan plan = plans.poll();
        if (plan == null) {
          throw new NullPointerException("plan queue is empty");
        }
        
        long remainingTime = plan.getRemainingTime();
        if (remainingTime > 0) {
          Thread.sleep(remainingTime);
        }
        
        // do collecting
        CollectorResponse rep = registry.collect(plan.getCollectorName());
        ipcChannel.write(new IpcPacket<>(IpcAction.COMMIT_INFO, rep));
        
        plan.setWaitTime(0);
        plans.add(plan);
      }
      
      synchronized (this) {
        notify();
      }
    } catch (Exception e) {
      log.error("CollectorScheduler stopped due to unexpected exception.", e);
    }
  }
  
  public void stop() {
    stopSignal = true;
    
    // wait until child thread exiting
    synchronized (this) {
      try {
        wait();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }
  
  @Getter
  private class Plan {
    
    private String collectorName;
    
    private long interval; // unit: milliseconds
    
    @Setter
    private long waitTime;
    
    Plan(String collectorName, long interval) {
      this.collectorName = collectorName;
      this.interval = interval;
    }
    
    long getRemainingTime() {
      return interval - waitTime;
    }
  }
}

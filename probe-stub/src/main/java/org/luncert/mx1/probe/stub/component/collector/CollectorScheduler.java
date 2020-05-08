package org.luncert.mx1.probe.stub.component.collector;

import com.google.common.collect.ImmutableList;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.luncert.mx1.probe.commons.data.IpcAction;
import org.luncert.mx1.probe.commons.data.IpcPacket;
import org.luncert.mx1.probe.commons.util.PropertiesUtils;
import org.luncert.mx1.probe.ipc.IpcChannel;
import org.luncert.mx1.probe.stub.component.collector.dynamicinfo.DynamicJvmInfoCollector;
import org.luncert.mx1.probe.stub.component.collector.dynamicinfo.DynamicSystemInfoCollector;
import org.luncert.mx1.probe.stub.pojo.CollectorResponse;

import java.io.IOException;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public final class CollectorScheduler {
  
  // TODO: temporary solution
  private static final ImmutableList<Class<? extends AbstractInfoCollector>> COLLECTOR_LIST =
      ImmutableList.of(
          DynamicSystemInfoCollector.class,
          DynamicJvmInfoCollector.class
      );
  
  private CollectorRegistry registry;
  
  private IpcChannel ipcChannel;
  
  private Map<String, Plan> activePlans = new ConcurrentHashMap<>();
  private Map<String, Plan> inactivePlans = new ConcurrentHashMap<>();
  
  private Timer timer;
  
  public CollectorScheduler(CollectorRegistry registry, IpcChannel ipcChannel) {
    this.registry = registry;
    this.ipcChannel = ipcChannel;
    
    for (Class<? extends AbstractInfoCollector> collector : COLLECTOR_LIST) {
      activePlans.put(collector.getName(), new Plan(collector.getName(), 100));
    }
  }
  
  public void start() {
    timer = new Timer();
    for (Plan activePlan : activePlans.values()) {
      timer.schedule(activePlan, 0, activePlan.getInterval());
    }
  }
  
  private void activatePlan(String name) {
    Plan plan = inactivePlans.remove(name);
    if (plan == null) {
      throw new NullPointerException();
    }
    
    activePlans.put(name, plan);
  }
  
  private void deactivatePlan(String name) {
    Plan plan = activePlans.remove(name);
    if (plan == null) {
      throw new NullPointerException();
    }
    
    plan.cancel();
    timer.purge();
    
    inactivePlans.put(name, plan);
  }
  
  public void stop() {
    timer.cancel();
    timer = null;
  }
  
  @Getter
  private class Plan extends TimerTask {
    
    private final String collectorName;
    
    // unit: milliseconds
    private final long interval;
    
    Plan(String collectorName, long interval) {
      this.collectorName = collectorName;
      this.interval = interval;
    }
    
    @Override
    public void run() {
      if (!CollectorRegistry.CollectorState.AVAILABLE.equals(
          registry.getCollectorState(collectorName))) {
        deactivatePlan(this.collectorName);
        // TODO: remove this plan
        return;
      }
      
      // do collecting
      CollectorResponse rep = registry.collect(collectorName);
      try {
        ipcChannel.write(new IpcPacket<>(IpcAction.COMMIT_INFO,
            PropertiesUtils.builder()
                .put("success", rep.isSuccess())
                .put("collectorName", collectorName)
                .build(),
            rep.getInfo()));
      } catch (IOException e) {
        log.error("Failed to send data to daemon.", e);
        // TODO: stop collector scheduler
      }
    }
  }
}

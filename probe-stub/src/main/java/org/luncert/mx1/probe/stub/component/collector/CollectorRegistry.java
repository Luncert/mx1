package org.luncert.mx1.probe.stub.component.collector;

import com.google.common.collect.ImmutableList;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.luncert.mx1.probe.stub.component.collector.dynamicinfo.DynamicJvmInfoCollector;
import org.luncert.mx1.probe.stub.component.collector.dynamicinfo.DynamicSystemInfoCollector;
import org.luncert.mx1.probe.stub.component.collector.staticinfo.MavenInfoCollector;
import org.luncert.mx1.probe.stub.component.collector.staticinfo.StaticJvmInfoCollector;
import org.luncert.mx1.probe.stub.component.collector.staticinfo.StaticSystemInfoCollector;
import org.luncert.mx1.probe.stub.pojo.CollectorResponse;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class CollectorRegistry {
  
  // TODO: temporary solution, I will use package-scan or other way to register collectors in the future.
  private List<CollectorInfo> collectorList = ImmutableList.<CollectorInfo>builder()
      .add(new CollectorInfo(DynamicJvmInfoCollector.class))
      .add(new CollectorInfo(DynamicSystemInfoCollector.class))
      .add(new CollectorInfo(MavenInfoCollector.class))
      .add(new CollectorInfo(StaticJvmInfoCollector.class))
      .add(new CollectorInfo(StaticSystemInfoCollector.class))
      .build();
  
  private Map<String, CollectorInfo> collectorIndex = new HashMap<>();
  
  public CollectorRegistry() {
    // instantiate all collectors
    for (CollectorInfo info : collectorList) {
      Class<? extends AbstractInfoCollector> collectorType = info.getCollectorType();
      try {
        AbstractInfoCollector obj = collectorType.newInstance();
        info.setInstance(obj);
        info.setState(CollectorState.AVAILABLE);

        // put into indexMap
        collectorIndex.put(collectorType.getName(), info);
      } catch (Exception e) {
        log.error("Failed to instantiate {}.", collectorType, e);
        info.setState(CollectorState.INITIALIZATION_FAILED);
      }
    }
  }
  
  public boolean enable(String collectorName) {
    CollectorInfo info = collectorIndex.get(collectorName);
    if (info == null) {
      log.warn("No collector has name {}.", collectorName);
      return false;
    }
    
    if (info.getState().equals(CollectorState.DISABLED)) {
      info.setState(CollectorState.AVAILABLE);
      return true;
    }
    
    log.warn("Collector is {}, not able to be enabled.", info.getState().getValue());
    return false;
  }
  
  public boolean disable(String collectorName) {
    CollectorInfo info = collectorIndex.get(collectorName);
    if (info == null) {
      log.warn("No collector has name {}.", collectorName);
      return false;
    }
    
    if (info.getState().equals(CollectorState.AVAILABLE)) {
      info.setState(CollectorState.DISABLED);
      return true;
    }

    log.warn("Collector is {}, not able to be disabled.", info.getState().getValue());
    return false;
  }
  
  @SuppressWarnings("unchecked")
  public <E> CollectorResponse<E> collect(String collectorName) {
    CollectorInfo info = collectorIndex.get(collectorName);
    if (info == null) {
      log.warn("No collector has name {}.", collectorName);
      return null;
    }

    if (!info.getState().equals(CollectorState.AVAILABLE)) {
      log.warn("Collector {} is {}, cannot to collect.",
          info.getCollectorType(), info.getState().getValue());
      return null;
    }
    
    return info.invokeCollect();
  }
  
  public CollectorState getCollectorState(String collectorName) {
    CollectorInfo info = collectorIndex.get(collectorName);
    return info == null ? null : info.getState();
  }
  
  private static class CollectorInfo {
    
    private static final int MAX_FAILURE_TIME = 3;
    
    @Setter
    @Getter
    CollectorState state = CollectorState.UNINITIALIZED;
    
    @Getter
    Class<? extends AbstractInfoCollector> collectorType;
    
    @Setter
    @Getter
    AbstractInfoCollector instance;
    
    int invokeFailureCount;
    
    CollectorInfo(Class<? extends AbstractInfoCollector> collectorType) {
      this.collectorType = collectorType;
    }
    
    CollectorResponse invokeCollect() {
      try {
        return instance.collect();
      } catch (Exception e) {
        invokeFailureCount++;
        if (invokeFailureCount == MAX_FAILURE_TIME) {
          setState(CollectorState.UNAVAILABLE);
        }
        
        log.error("Failed to invoke collect method of {}.", collectorType, e);
        return null;
      }
    }
  }
  
  public enum CollectorState {
    UNINITIALIZED("uninitialized"),
    INITIALIZATION_FAILED("initialization failed"),
    AVAILABLE("available"),
    UNAVAILABLE("unavailable"),
    DISABLED("disabled");
    
    @Getter
    private String value;
    
    CollectorState(String value) {
      this.value = value;
    }
  }
}

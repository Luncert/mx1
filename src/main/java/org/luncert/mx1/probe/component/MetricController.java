package org.luncert.mx1.probe.component;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.MetricRegistry;
import org.springframework.stereotype.Component;

@Component
public class MetricController {
  
  private final MetricRegistry metrics = new MetricRegistry();
  
  public enum MetricEvent {
  
  }
  
  public void mark(MetricEvent event) {
    metrics.addListener();
    ConsoleReporter
  
  }
}

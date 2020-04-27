package org.luncert.mx1.probe;

import lombok.extern.slf4j.Slf4j;
import org.luncert.mx1.probe.component.MavenStaticInfoCollector;

import java.lang.instrument.Instrumentation;

@Slf4j
public class ProbeMain {
  
  public static void premain(String agentOptions, Instrumentation instrumentation) {
    main(new String[]{});
  }
  
  public static void main(String[] args) {
    log.debug("Probe Stub on.");
  
    MavenStaticInfoCollector collector = new MavenStaticInfoCollector();
    collector.collect();
  }
}

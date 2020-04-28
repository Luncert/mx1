package org.luncert.mx1.probe.stub;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.daemon.Daemon;
import org.luncert.mx1.probe.stub.component.MavenStaticInfoCollector;

import java.lang.instrument.Instrumentation;

@Slf4j
public class ProbeStubMain {
  
  public static void premain(String agentOptions, Instrumentation instrumentation) {
    log.debug("Probe Stub on.");
  
    MavenStaticInfoCollector collector = new MavenStaticInfoCollector();
    System.out.println(collector.collect());
  
    instrumentation
  
    //Runtime.getRuntime().addShutdownHook();
  }
  
  public static void main(String[] args) {
    premain(null, null);
  }
}

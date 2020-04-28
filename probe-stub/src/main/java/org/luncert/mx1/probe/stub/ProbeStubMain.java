package org.luncert.mx1.probe.stub;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.daemon.Daemon;
import org.luncert.mx1.probe.stub.component.MavenStaticInfoCollector;

import java.lang.instrument.Instrumentation;

@Slf4j
public class ProbeStubMain {
  
  public static void premain(String agentOptions, Instrumentation instrumentation) {
    main(new String[]{});
  }
  
  public static void main(String[] args) {
    log.debug("Probe Stub on.");
    
    // start probe-daemon
    ProbeDaemonStarter.start();
  
    MavenStaticInfoCollector collector = new MavenStaticInfoCollector();
    collector.collect();
    
    //Runtime.getRuntime().addShutdownHook();
  }
}

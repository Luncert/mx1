package org.luncert.mx1.probe.stub;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.daemon.Daemon;
import org.luncert.mx1.probe.stub.component.MavenStaticInfoCollector;

import java.lang.instrument.Instrumentation;

@Slf4j
public class ProbeStubMain {
  
  public static void premain(String agentOptions, Instrumentation instrumentation) {
    log.debug("Probe Stub on.");
    
    ClassLoader loader = ProbeStubMain.class.getClassLoader();
    System.out.println(loader);
    System.out.println(loader.getParent());
    System.out.println(loader.getParent().getParent());
    Class clazz = null;
    try {
      clazz = loader.loadClass("org.luncert.mx1.probe.testapp.App");
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
    System.out.println(clazz);
    
    MavenStaticInfoCollector collector = new MavenStaticInfoCollector();
    System.out.println(collector.collect());

    //Runtime.getRuntime().addShutdownHook();
  }
  
  public static void main(String[] args) {
    premain(null, null);
  }
}

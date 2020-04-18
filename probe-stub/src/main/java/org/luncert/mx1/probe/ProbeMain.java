package org.luncert.mx1.probe;

import java.lang.instrument.Instrumentation;

public class ProbeMain {
  
  public static void premain(String agentOptions, Instrumentation instrumentation) {
    main(new String[]{});
  }
  
  public static void main(String[] args) {
    System.out.println("Probe on.");
  }
}

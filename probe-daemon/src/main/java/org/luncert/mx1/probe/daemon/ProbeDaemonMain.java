package org.luncert.mx1.probe.daemon;

import org.luncert.mx1.probe.daemon.pojo.Config;

public class ProbeDaemonMain {
  
  public static void main(String[] args) {
    ProbeDaemonMain app = new ProbeDaemonMain();
    app.start(args);
  }
  
  private void start(String[] args) {
    // https://commons.apache.org/proper/commons-cli/usage.html
    Config config = ConfigLoader.load(args);
    if (config == null) {
      System.exit(1);
    }
  
    BannerLoader.print("{brightGreen:mx1probe-daemon} by {brightCyan:Luncert}");
    
    //IStubDataReceiver stubDataReceiver = ;
  }
}

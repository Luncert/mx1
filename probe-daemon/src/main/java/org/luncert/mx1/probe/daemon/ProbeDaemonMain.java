package org.luncert.mx1.probe.daemon;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.daemon.Daemon;
import org.apache.commons.daemon.DaemonContext;
import org.apache.commons.daemon.DaemonInitException;
import org.luncert.mx1.probe.daemon.pojo.Config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class ProbeDaemonMain implements Daemon {
  
  private Config config;
  
  /**
   * provide for commons-daemon to invoke
   */
  public static void main(String[] args) throws Exception {
    if (args.length == 0) {
      throw new Exception("start/stop action argument missing");
    }
    
    String[] filteredArgs = new String[args.length - 1];
    System.arraycopy(args, 1, filteredArgs, 0, filteredArgs.length);
    
    String action = args[0];
    if ("start".equals(action)) {
      ProbeDaemonMain app = new ProbeDaemonMain();
      app.init(filteredArgs);
      app.start();
    } else {
      // TODO: stop
    }
  }
  
  @Override
  public void init(DaemonContext daemonContext) throws DaemonInitException {
    init(daemonContext.getArguments());
  }
  
  private void init(String[] args) {
    config = ConfigLoader.load(args);
  }
  
  @Override
  public void start() throws Exception {
    log.info("Starting mx1probe-daemon with configuration: {}", config);
    
    if (config.isNoBanner()) {
      log.info("Probe-daemon on.");
    } else {
      BannerLoader.print("{brightGreen:mx1probe-daemon} by {brightCyan:Luncert}");
    }
  
    //IStubDataReceiver stubDataReceiver = ;
  }
  
  @Override
  public void stop() throws Exception {
    log.info("Probe-daemon off.");
  }
  
  @Override
  public void destroy() {
  
  }
}

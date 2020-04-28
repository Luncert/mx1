package org.luncert.mx1.probe.daemon;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.daemon.Daemon;
import org.apache.commons.daemon.DaemonContext;
import org.apache.commons.daemon.DaemonInitException;
import org.luncert.mx1.probe.daemon.pojo.Config;

@Slf4j
public class ProbeDaemonMain implements Daemon {
  
  private Config config;
  
  @Override
  public void init(DaemonContext daemonContext) throws DaemonInitException {
    String[] args = daemonContext.getArguments();
    config = ConfigLoader.load(args);
  }
  
  @Override
  public void start() throws Exception {
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

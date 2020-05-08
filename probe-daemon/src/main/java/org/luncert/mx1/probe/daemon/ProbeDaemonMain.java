package org.luncert.mx1.probe.daemon;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.daemon.Daemon;
import org.apache.commons.daemon.DaemonContext;
import org.luncert.mx1.commons.data.IpcPacket;
import org.luncert.mx1.commons.data.NetURL;
import org.luncert.mx1.probe.daemon.pojo.Config;
import org.luncert.mx1.probe.ipc.IpcChannel;
import org.luncert.mx1.probe.ipc.IpcDataHandler;
import org.luncert.mx1.probe.ipc.IpcFactory;

import java.io.IOException;
import java.net.InetSocketAddress;

@Slf4j
public class ProbeDaemonMain implements Daemon {
  
  private Config config;
  
  private IpcChannel ipcChannel;
  
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
      
      app.loadConfig(filteredArgs);
      
      Runtime.getRuntime().addShutdownHook(new Thread(app::stop));
      
      app.start();
      
      // NOTE: daemon won't exit when execution of main has done.
      // ref: https://stackoverflow.com/questions/2614774/what-can-cause-java-to-keep-running-after-system-exit
      // Reason: daemon is blocked by invoking app.stop() -> ipcChannel.close() -> netty.close()
    } else {
      log.debug("Daemon is required to stop.");
      
      // TODO: will apache-daemon kill daemon process after invoke main?
      // TODO: send SIGKILL to running daemon process
    }
  }
  
  @Override
  public void init(DaemonContext daemonContext) {
    loadConfig(daemonContext.getArguments());
  }
  
  private void loadConfig(String[] args) {
    config = ConfigLoader.load(args);
  }
  
  @Override
  public void start() throws Exception {
    log.info("Starting daemon with configuration: {}", config);
    
    if (!config.isNoBanner()) {
      BannerLoader.print("{brightGreen:mx1probe-daemon} by {brightCyan:Luncert}");
    }
  
    // TODO: connect to central
    NetURL serveAddress = config.getServeAddress();
    ipcChannel = IpcFactory.<IpcPacket>tcp()
        .bind(new InetSocketAddress(serveAddress.getHost(), serveAddress.getPort()))
        .addHandler(new IpcDataHandler<IpcPacket>() {
          @Override
          public void onData(IpcChannel channel, IpcPacket data) {
            log.info("Received: {}.", data);
          }
  
          @Override
          public void onClose() {
    
          }
        })
        .open();
    
    log.info("Daemon is up.");
    
    ipcChannel.sync();
  }
  
  @Override
  public void stop() {
    try {
      ipcChannel.close();
    } catch (IOException e) {
      log.error("Failed to stop daemon.", e);
    }
  
    log.info("Daemon is down.");
}
  
  @Override
  public void destroy() {}
}

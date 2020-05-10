package org.luncert.mx1.probe.daemon;

import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.daemon.Daemon;
import org.apache.commons.daemon.DaemonContext;
import org.apache.commons.lang3.StringUtils;
import org.luncert.mx1.commons.constant.DaemonAction;
import org.luncert.mx1.commons.constant.ProbeAction;
import org.luncert.mx1.commons.data.DataPacket;
import org.luncert.mx1.commons.data.NetURL;
import org.luncert.mx1.commons.util.PropertiesUtils;
import org.luncert.mx1.probe.daemon.pojo.Config;
import org.luncert.mx1.probe.ipc.IpcChannel;
import org.luncert.mx1.probe.ipc.IpcFactory;

import java.net.InetSocketAddress;

@Slf4j
public class ProbeDaemonMain implements Daemon {
  
  private Config config;
  
  private IpcChannel ipcChannel;
  
  private CentralService centralService;
  
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
      log.debug("Daemon is required to stop");
      
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
    
    // connect to central
    NetURL centralAddress = config.getCentralAddress();
    centralService = new CentralService();
    centralService.connect(new InetSocketAddress(centralAddress.getHost(), centralAddress.getPort()),
        new CentralDataHandler());
    Channel centralChannel = centralService.getChannel();
    
    // connect to probe-stub
    NetURL binding = config.getBinding();
    ipcChannel = IpcFactory.<DataPacket>tcp()
        .bind(new InetSocketAddress(binding.getHost(), binding.getPort()))
        .addHandler(new StubDataHandler(centralChannel))
        .open();
    
    // register probe to central or notify central probe is available.
    String nodeId = NodeIdHolder.get();
    if (StringUtils.isEmpty(nodeId)) {
      // probe has no nodeId, so wee need to send an request to stub to collect static app info,
      // and then register probe to central with the static app info
      ipcChannel.write(DataPacket.builder()
          .action(DaemonAction.COLLECT_STATIC_APP_INFO)
          .build());
    } else {
      // send probe nodeId to central to let him know probe is back
      centralChannel.writeAndFlush(DataPacket.builder()
          .action(ProbeAction.UPDATE_METADATA)
          .headers(PropertiesUtils.builder()
              .put("nodeId", nodeId)
              .build())
          .build());
    }
    
    log.info("Daemon is up");
    
    // block current thread
    ipcChannel.sync();
  }
  
  @Override
  public void stop() {
    try {
      centralService.close();
    } catch (Exception e) {
      log.error("Failed to close connection to central", e);
    }
    
    try {
      ipcChannel.close();
    } catch (Exception e) {
      log.error("Failed to close ipc connection", e);
    }
    
    log.info("Daemon is down");
  }
  
  @Override
  public void destroy() {}
}

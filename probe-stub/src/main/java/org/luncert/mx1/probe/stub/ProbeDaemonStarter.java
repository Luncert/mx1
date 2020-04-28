package org.luncert.mx1.probe.stub;

import com.google.common.collect.ImmutableList;
import lombok.extern.slf4j.Slf4j;
import org.luncert.mx1.probe.commons.util.IOUtils;
import org.luncert.mx1.probe.stub.exeception.FindProbeDaemonJarError;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Objects;

@Slf4j
class ProbeDaemonStarter {
  
  private static final List<String> CMD_PRE_ARGS;
  
  static {
    ImmutableList.Builder<String> builder = ImmutableList.builder();
    String osName = System.getProperty("os.name");
    if (osName.startsWith("win")) {
      builder.add("cmd.exe")
          .add("/c")
          .add("prunsrv");
    } else {
      builder.add("bash")
          .add("-c")
          .add("jsvc");
    }
    
    CMD_PRE_ARGS = builder.build();
  }
  
  static void start() {
    // check if probe-daemon is running
    // create new process to run probe-daemon.jar
  
    // http://commons.apache.org/proper/commons-daemon/procrun.html
    List<String> command = ImmutableList.<String>builder()
        .addAll(CMD_PRE_ARGS)
        .add("service") // run the service
        .add("--DisplayName=mx1probe-daemon")
        .add("--Startup=auto")
        .add("--StartMode=jvm")
        .add("--StopMode=jvm")
        .add("--StartClass=org.luncert.mx1.probe.daemon.ProbeDaemonMain")
        .add("--StopClass=org.luncert.mx1.probe.daemon.ProbeDaemonMain")
        .add("--Classpath=" + findProbeDaemonJar())
        .build();
  
    try {
      Process process = new ProcessBuilder().command(command).start();
  
      // create new thread to copy process output to System.out
      Thread outputThread = new Thread(() -> {
        try {
          IOUtils.copyStream(process.getInputStream())
              .addConsumer(System.out)
              .go();
        } catch (IOException e) {
          log.error("failed to copy probe-daemon process output to stdout", e);
        }
      });
      outputThread.setDaemon(true);
      outputThread.start();
      
      // wait util commons-daemon exiting
      int exitVal = process.waitFor();
      if (exitVal != 0) {
        log.error("failed to start probe-daemon, exit value=" + exitVal);
      }
    } catch (IOException e) {
      log.error("failed to start probe-daemon", e);
    } catch (InterruptedException e) {
      log.error("failed to wait commons-daemon", e);
    }
  
    //Runtime.getRuntime().addShutdownHook();
  }
  
  private static String findProbeDaemonJar() {
    URL rootPath = ProbeDaemonStarter.class.getClassLoader().getResource("");
    if (rootPath == null) {
      throw new FindProbeDaemonJarError("unable to detect classpath");
    }
    File root = new File(rootPath.getFile());
    for (File file : Objects.requireNonNull(root.listFiles())) {
      String name = file.getName();
      if (name.contains("probe-daemon") && name.endsWith(".jar")) {
        return file.getAbsolutePath();
      }
    }
    throw new FindProbeDaemonJarError("probe daemon jar not found in classpath");
  }
  
  private static void stop() {
  
  }
}

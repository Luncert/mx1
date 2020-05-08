package org.luncert.mx1.probe.stub;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.luncert.mx1.probe.commons.data.IpcPacket;
import org.luncert.mx1.probe.commons.data.NetURL;
import org.luncert.mx1.probe.ipc.IpcChannel;
import org.luncert.mx1.probe.ipc.IpcFactory;
import org.luncert.mx1.probe.stub.component.AgentTransformerFactory;
import org.luncert.mx1.probe.stub.component.DaemonConnectionHandler;
import org.luncert.mx1.probe.stub.component.collector.CollectorRegistry;
import org.luncert.mx1.probe.stub.component.collector.CollectorScheduler;
import org.luncert.mx1.probe.stub.exeception.LoadProbeSpyJarError;
import org.luncert.mx1.probe.stub.exeception.ProbeSpyJarNotFoundError;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.file.Paths;
import java.util.jar.JarFile;

@Slf4j
public class ProbeStubMain {
  
  private static final String SPY_JAR_NAME = "mx1probe-spy.jar";
  
  @Getter
  private static ProbeSpyResourceClassLoader probeSpyResourceClassLoader;
  
  private static IpcChannel ipcChannel;
  
  private static CollectorScheduler collectorScheduler;
  
  public static void premain(String agentOptions, Instrumentation inst) {
    log.debug("Probe Stub on.");

    ProbeStubConfig config = ProbeStubConfig.resolveAgentOptions(agentOptions);
  
    setupProbeSpy(inst);
    
    addTransformer(inst);
    
    startCollecting(config);
    
    addShutdownHook();
  }
  
  private static void setupProbeSpy(Instrumentation inst) {
    // according to parent delegation model,
    // classes in probe-spy will be loaded by BootstrapClassLoader
    JarFile spyJarFile = loadProbeSpyJar();
    inst.appendToBootstrapClassLoaderSearch(spyJarFile);
    probeSpyResourceClassLoader = new ProbeSpyResourceClassLoader(spyJarFile);
    
    // register probe event handlers
    // don't create anonymous inner class extend ProbeEventHandler there,
    // If we use ProbeEventHandler, it will be loaded before appendToBootstrapClassLoaderSearch
    ProbeEventHandlerManager.register("org.luncert.mx1.probe.stub.component.probeEventHandler");
  }
  
  private static JarFile loadProbeSpyJar() {
    // Notice: mx1probe-spy.jar是被打包进了probe-stub.jar的，通过getResource
    // 获取到的路径并不是真实的文件系统路径，所以路径中会有感叹号标识。
    // 所以这里先将mx1probe-spy.jar读入内存，再映射到文件系统中，再创建JarFile
    InputStream spyJarInputStream = ProbeStubMain.class.getClassLoader()
        .getResourceAsStream(SPY_JAR_NAME);
    if (spyJarInputStream == null) {
      throw new ProbeSpyJarNotFoundError("mx1probe-spy.jar missing in classpath.");
    }

    try {
      // read spy jar to memory
      ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
      IOUtils.copy(spyJarInputStream, byteArrayOutputStream);

      byte[] spyJarRaw = byteArrayOutputStream.toByteArray();

      // build tmp output path
      File tmpDir = FileUtils.getTempDirectory();
      String spyJarPath = Paths.get(tmpDir.getAbsolutePath(), SPY_JAR_NAME).toString();
      
      // write to spyJarRaw to tmp file
      // NOTICE: invalid CEN header (bad signature)
      RandomAccessFile file = new RandomAccessFile(spyJarPath, "rw");
      file.setLength(spyJarRaw.length);
      file.getChannel().write(ByteBuffer.wrap(spyJarRaw));
      file.close();
      
      // create JarFile
      return new JarFile(spyJarPath);
    } catch (IOException e) {
      throw new LoadProbeSpyJarError(e);
    }
  }
  
  private static void addTransformer(Instrumentation inst) {
    ClassFileTransformer transformer = AgentTransformerFactory.createTransformer();
  
    // What does retransform use for?
    // https://stackoverflow.com/questions/45685245/java-agent-transform-not-invoked-for-all-classes
    // You are only seeing classes that are not yet loaded when the agent is attached.
    // If you want to handle loaded classes, too, you have to explicitly retransform these classes.
    inst.addTransformer(transformer, true);
  }
  
  private static void startCollecting(ProbeStubConfig config) {
    CollectorRegistry collectorRegistry = new CollectorRegistry();
    
    // establish ipc connection
    NetURL daemonUrl = config.getDaemonUrl();
    assert daemonUrl != null && daemonUrl.getProtocol().equals("tcp");
    
    try {
      ipcChannel = IpcFactory.<IpcPacket>tcp()
          .destination(new InetSocketAddress(daemonUrl.getHost(), daemonUrl.getPort()))
          //.addHandler(new DaemonConnectionHandler(collectorRegistry))
          .open();
  
      collectorScheduler = new CollectorScheduler(collectorRegistry, ipcChannel);
      collectorScheduler.start();
      
      log.debug("CollectorScheduler started.");
    } catch (IOException e) {
      log.error("Failed to start collecting.", e);
    }
  }
  
  private static void addShutdownHook() {
    ProbeStubCleaner probeStubCleaner = new ProbeStubCleaner();
    
    if (collectorScheduler != null) {
      probeStubCleaner.addShutdownTask(() -> collectorScheduler.stop());
    }
    
    if (ipcChannel != null) {
      probeStubCleaner.addShutdownTask(() -> ipcChannel.close());
    }
    
    Thread cleanerThread = new Thread(probeStubCleaner);
    cleanerThread.setDaemon(true);
    Runtime.getRuntime().addShutdownHook(cleanerThread);
  }
}

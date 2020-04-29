package org.luncert.mx1.probe.stub;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.daemon.Daemon;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.luncert.mx1.probe.spy.ProbeSpy;
import org.luncert.mx1.probe.spy.exception.LoadProbeSpyJarError;
import org.luncert.mx1.probe.stub.component.AgentTransformer;
import org.luncert.mx1.probe.stub.component.MavenStaticInfoCollector;
import org.luncert.mx1.probe.stub.exeception.ProbeSpyJarNotFoundError;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.lang.instrument.Instrumentation;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.util.jar.JarFile;

@Slf4j
public class ProbeStubMain {
  
  private static final String SPY_JAR_NAME = "mx1probe-spy.jar";
  
  public static void premain(String agentOptions, Instrumentation inst) {
    log.debug("Probe Stub on.");
  
    setupProbeSpy(inst);
    
    inst.addTransformer(new AgentTransformer());
    
    //Runtime.getRuntime().addShutdownHook();
  }
  
  private static void setupProbeSpy(Instrumentation inst) {
    inst.appendToBootstrapClassLoaderSearch(loadProbeSpyJar());
    // TODO: register handlers
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
      RandomAccessFile file = new RandomAccessFile(spyJarPath, "rw");
      FileChannel fileChannel = file.getChannel();
      fileChannel.write(ByteBuffer.wrap(spyJarRaw));
      fileChannel.close();
  
      // create JarFile
      return new JarFile(spyJarPath);
    } catch (IOException e) {
      throw new LoadProbeSpyJarError(e);
    }
  }
  
  public static void main(String[] args) {
    premain(null, null);
  }
}

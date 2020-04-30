package org.luncert.mx1.probe.stub;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.luncert.mx1.probe.stub.exeception.LoadProbeSpyJarError;
import org.luncert.mx1.probe.stub.component.AgentTransformerFactory;
import org.luncert.mx1.probe.stub.component.staticInfoCollector.SpringStaticInfoCollector;
import org.luncert.mx1.probe.stub.exeception.ProbeSpyJarNotFoundError;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.nio.ByteBuffer;
import java.nio.file.Paths;
import java.util.jar.JarFile;

@Slf4j
public class ProbeStubMain {
  
  private static final String SPY_JAR_NAME = "mx1probe-spy.jar";
  
  private static ProbeSpyResourceClassLoader probeSpyResLoader;
  
  public static void premain(String agentOptions, Instrumentation inst) throws ClassNotFoundException {
    log.debug("Probe Stub on.");
  
    setupProbeSpy(inst);

    ClassFileTransformer transformer = AgentTransformerFactory.createTransformer(inst);
    // FIXME: adding retransformable transformers is not supported in this environment
    inst.addTransformer(transformer, true);
  
    SpringStaticInfoCollector collector = new SpringStaticInfoCollector();
    System.out.println(collector.collect());
    //Runtime.getRuntime().addShutdownHook();
  }
  
  private static void setupProbeSpy(Instrumentation inst) {
    // according to parent delegation model,
    // classes in probe-spy will be loaded by BootstrapClassLoader
    JarFile spyJarFile = loadProbeSpyJar();
    inst.appendToBootstrapClassLoaderSearch(spyJarFile);
    probeSpyResLoader = new ProbeSpyResourceClassLoader(spyJarFile);
    // register probe event handlers
    // don't create anonymous inner class extend ProbeEventHandler there,
    // If we use ProbeEventHandler, it will be loaded before appendToBootstrapClassLoaderSearch
    ProbeEventHandlerContainer.register();
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
  
  public static ClassLoader getProbeSpyResourceClassLoader() {
    return probeSpyResLoader;
  }
}

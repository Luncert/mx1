package org.luncert.mx1.probe.stub.component;

import com.google.common.collect.ImmutableList;
import lombok.extern.slf4j.Slf4j;
import org.luncert.mx1.probe.stub.component.transformer.SpringBootAgentTransformer;
import org.luncert.mx1.probe.stub.exeception.CreateTransformerError;
import org.luncert.mx1.probe.stub.pojo.AppInfo;
import org.luncert.mx1.probe.stub.pojo.AppStartMode;

import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.management.ManagementFactory;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

@Slf4j
public final class AgentTransformerFactory {
  
  private static final List<AgentTransformer> AGENT_TRANSFORMERS
      = ImmutableList.<AgentTransformer>builder()
      .add(new SpringBootAgentTransformer())
      .build();
  
  public static ClassFileTransformer createTransformer() {
    AppInfo appInfo = loadAppInfo();
  
    Optional<AgentTransformer> optionalTransformer = AGENT_TRANSFORMERS.stream()
        .filter(agentTransformer -> agentTransformer.accept(appInfo))
        .findFirst();
    
    if (!optionalTransformer.isPresent()) {
      throw new CreateTransformerError("no appropriate transformer, app info: " + appInfo);
    }
    
    AgentTransformer transformer = optionalTransformer.get();
  
    transformer.init(appInfo);
    
    return transformer;
  }
  
  private static AppInfo loadAppInfo() {
    AppInfo appInfo = new AppInfo();
    
    String classpath = resolveMainClasspath();
    appInfo.setMainClasspath(classpath);
    if (classpath.endsWith(".jar")) {
      appInfo.setStartMode(AppStartMode.Jar);
      
      try {
        JarFile appJar = new JarFile(classpath);
        appInfo.setManifest(appJar.getManifest());
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    // TODO: 如果应用是通过mvn直接运行的呢？需要研究一下java所有运行程序的方式
    
    return appInfo;
  }
  
  /**
   * 使用不同jdk运行probe-test-app/run.bat时，拿取到的classpath值是不同的，
   * <li>jdk8环境：会同时拿到目标应用的classpath和probe-stub-agent的classpath</li>
   * <li>jdk11环境：只能拿到目标应用的classpath</li>
   */
  private static String resolveMainClasspath() {
    String[] classpathArray = System.getProperty("java.class.path").split(";");
    
    // classpath won't be empty
    if (classpathArray.length == 1) {
      return classpathArray[0];
    }
    
    // filter all agent jar
    Set<String> jvmAgentJars = ManagementFactory.getRuntimeMXBean().getInputArguments().stream()
        .filter(arg -> arg.startsWith("-javaagent:"))
        .map(AgentTransformerFactory::getJarFileName)
        .collect(Collectors.toSet());
    
    // filter all classpath has the same jar name in jvmAgentJars
    List<String> classpathList = Arrays.stream(classpathArray)
        .filter(classpath -> !jvmAgentJars.contains(clipJarFileName(classpath)))
        .collect(Collectors.toList());
    
    if (classpathList.size() != 1) {
      log.debug("Classpath remained: {}", classpathList);
    }
    
    return classpathList.get(0);
  }
  
  private static String clipJarFileName(String classpath) {
    char[] charArray = classpath.toCharArray();
    int lastSplitIdx = 0;
    for (int i = 0; i < charArray.length; i++) {
      char c = charArray[i];
      if (c == '\\' || c == '/') {
        lastSplitIdx = i;
      }
    }
    
    return classpath.substring(lastSplitIdx + 1);
  }
  
  private static String getJarFileName(String jvmAgentArg) {
    char[] charArray = jvmAgentArg.toCharArray();
    int lastSplitIdx = 0, firstEqualIdx = charArray.length;

    for (int i = 0; i < charArray.length; i++) {
      char c = charArray[i];
      if (c == '\\' || c == '/') {
        lastSplitIdx = i;
      } else if (c == '=') {
        firstEqualIdx = i;
      }
    }
    
    return jvmAgentArg.substring(lastSplitIdx + 1, firstEqualIdx);
  }
}

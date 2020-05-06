package org.luncert.mx1.probe.stub.component;

import com.google.common.collect.ImmutableList;
import lombok.extern.slf4j.Slf4j;
import org.luncert.mx1.probe.stub.common.ClasspathUtil;
import org.luncert.mx1.probe.stub.component.transformer.SpringBootAgentTransformer;
import org.luncert.mx1.probe.stub.exeception.CreateTransformerError;
import org.luncert.mx1.probe.stub.pojo.AppInfo;
import org.luncert.mx1.probe.stub.pojo.AppStartMode;

import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.util.List;
import java.util.Optional;
import java.util.jar.JarFile;

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
    
    String classpath = ClasspathUtil.resolveMainClasspath();
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
}

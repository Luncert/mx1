package org.luncert.mx1.probe.stub.component.transformer;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.LoaderClassPath;
import javassist.NotFoundException;
import javassist.bytecode.Descriptor;
import lombok.extern.slf4j.Slf4j;
import org.luncert.mx1.probe.stub.ProbeStubMain;
import org.luncert.mx1.probe.stub.component.AgentTransformer;
import org.luncert.mx1.probe.stub.pojo.AppInfo;

import java.io.IOException;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.Attributes;

// https://github.com/alibaba/jvm-sandbox/blob/96f6b1a1809d670a8177c87a9d29cd39a98f5619/sandbox-core/src/main/java/com/alibaba/jvm/sandbox/core/enhance/weaver/asm/EventWeaver.java
// https://www.baeldung.com/java-instrumentation
@Slf4j
public class SpringBootAgentTransformer extends AgentTransformer {
  
  private static final String MANIFEST_KEY_SPRING_BOOT_APP = "Spring-Boot-Version";
  private static final String MANIFEST_KET_MAIN_CLASS = "Main-Class";
  private static final String MANIFEST_KEY_START_CLASS = "Start-Class";
  
  private static final String SPRING_CONTEXT_CLASS_NAME =
      "org.springframework.context.support.AbstractApplicationContext";
  private static final String SPRING_SPY_BEAN_NAME = "SpringSpyBean";
  
  private Set<ClassLoader> classLoaderSet = new HashSet<>();
  private ClassPool classPool = ClassPool.getDefault();
  
  private Instrumentation inst;
  private AppInfo appInfo;
  
  /**
   * mostly refers to spring's JarLauncher
   */
  private String mainClass;
  
  /**
   * target app's main class
   */
  private String startClass;
  
  @Override
  public boolean accept(AppInfo appInfo) {
    // FIXME: containsKey() always return false
    //return appInfo.getManifest().getMainAttributes().containsKey(MANIFEST_KEY_SPRING_BOOT_APP);
    return appInfo.getManifest().getMainAttributes().getValue(MANIFEST_KEY_SPRING_BOOT_APP) != null;
  }
  
  @Override
  public void init(Instrumentation inst, AppInfo appInfo) {
    this.inst = inst;
    this.appInfo = appInfo;
    Attributes attrs = appInfo.getManifest().getMainAttributes();
    mainClass = attrs.getValue(MANIFEST_KET_MAIN_CLASS);
    startClass = attrs.getValue(MANIFEST_KEY_START_CLASS);
  }
  
  @Override
  public byte[] transform(ClassLoader loader, String className,
                          Class<?> classBeingRedefined,
                          ProtectionDomain protectionDomain,
                          byte[] classfileBuffer) throws IllegalClassFormatException {
    byte[] byteCode = classfileBuffer;
    String dotSplitClassName = className.replaceAll("/", ".");
  
    if (dotSplitClassName.endsWith(startClass)) {
      // the start class will be loaded by spring's class loader,
      // it means the start class hasn't been loaded at now, and our
      // transformer can't transform the start class
    } else if (dotSplitClassName.endsWith(mainClass)) {
      addClassLoaderToPool(loader);
  
      try {
        CtClass ctClass = classPool.getCtClass(mainClass);
    
        CtMethod ctMethod = ctClass.getMethod("main",
            Descriptor.ofMethod(CtClass.voidType, new CtClass[]{
                classPool.getCtClass("[Ljava/lang/String")}));
    
        ctMethod.insertBefore("System.out.println(\"spy\");");
    
        byteCode = ctClass.toBytecode();
        ctClass.detach();
      } catch (IOException | NotFoundException | CannotCompileException e) {
        e.printStackTrace();
      }
    } else if (dotSplitClassName.equals(SPRING_CONTEXT_CLASS_NAME)) {
      // add the target class loader to pool
      // ClassPool will entrust it to find target class and probe-spy
      addClassLoaderToPool(loader);
  
      // When invoke getCtClass, it will assign all classpath
      // (like the classloader we added above) to find the target class,
      // by ClassLoader#getResource, not ClassLoader#loadClass.
      // It brings one problem: although we have add probe-spy.jar to the search path
      // of the BootstrapClassloader, the class file can't be loaded, but the class can be loaded.
      //
      // To solve the problem, we use ProbeSpyResourceClassLoader to load probe-spy's class files.
      addClassLoaderToPool(ProbeStubMain.getProbeSpyResourceClassLoader());
      
      try {
        CtClass ctClass = classPool.getCtClass(SPRING_CONTEXT_CLASS_NAME);

        CtMethod ctMethod = ctClass.getMethod("finishRefresh",
            Descriptor.ofMethod(CtClass.voidType, new CtClass[0]));
        
        ctMethod.insertAfter("System.out.println(\"adadqcacvasda\");" +
            "org.luncert.mx1.probe.spy.ProbeSpy.fireEvent(\"EVT_INJECT_SPRING_CONTEXT\", this);");
        
        byteCode = ctClass.toBytecode();
        //ctClass.detach();
      } catch (IOException | NotFoundException | CannotCompileException e) {
        e.printStackTrace();
      }
    }
    
    return byteCode;
  }
  
  private void addClassLoaderToPool(ClassLoader loader) {
    if (!classLoaderSet.contains(loader)) {
      classLoaderSet.add(loader);
      classPool.appendClassPath(new LoaderClassPath(loader));
    }
  }
}

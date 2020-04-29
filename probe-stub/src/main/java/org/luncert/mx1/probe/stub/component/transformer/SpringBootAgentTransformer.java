package org.luncert.mx1.probe.stub.component.transformer;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ClassFile;
import javassist.bytecode.ConstPool;
import javassist.bytecode.annotation.IntegerMemberValue;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import lombok.extern.slf4j.Slf4j;
import org.luncert.mx1.probe.stub.component.AgentTransformer;
import org.luncert.mx1.probe.stub.pojo.AppInfo;

import java.lang.annotation.Annotation;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.jar.Attributes;

@Slf4j
public class SpringBootAgentTransformer extends AgentTransformer {
  
  private static final String MANIFEST_KEY_SPRING_BOOT_APP = "Spring-Boot-Version";
  private static final String MANIFEST_KEY_START_CLASS = "Start-Class";
  
  private static final String SPRING_SPY_BEAN_NAME = "SpringSpyBean";
  
  private AppInfo appInfo;
  private String startClass;
  
  @Override
  public boolean accept(AppInfo appInfo) {
    // FIXME: containsKey() always return false
    //return appInfo.getManifest().getMainAttributes().containsKey(MANIFEST_KEY_SPRING_BOOT_APP);
    return appInfo.getManifest().getMainAttributes().getValue(MANIFEST_KEY_SPRING_BOOT_APP) != null;
  }
  
  @Override
  public void init(AppInfo appInfo) {
    this.appInfo = appInfo;
    Attributes attrs = appInfo.getManifest().getMainAttributes();
    startClass = attrs.getValue(MANIFEST_KEY_START_CLASS);
  }
  
  @Override
  public byte[] transform(ClassLoader loader, String className,
                          Class<?> classBeingRedefined,
                          ProtectionDomain protectionDomain,
                          byte[] classfileBuffer) throws IllegalClassFormatException {
    if (startClass.endsWith(className)) {
      String startClassPackageName = startClass.substring(0, startClass.lastIndexOf('.'));
      ClassPool pool = ClassPool.getDefault();
      CtClass ctClass = pool.makeClass(startClassPackageName + '.' + SPRING_SPY_BEAN_NAME);
      
      ClassFile classFile = ctClass.getClassFile();
      ConstPool constpool = classFile.getConstPool();
  
      AnnotationsAttribute annotationsAttribute = new AnnotationsAttribute(constpool, AnnotationsAttribute.visibleTag);
      // FIXME: how to get Component annotation
      Annotation annotation = new Annotation(annotationName, constpool);
      annotation.addMemberValue("frequency", new IntegerMemberValue(classFile.getConstPool(), frequency));
      annotationsAttribute.setAnnotation(annotation);
  
      ctClass.getClassFile().addAttribute(annotationsAttribute);
      //CtClass ctClass = pool.getCtClass(startClass);
      //CtMethod ctMethod = ctClass.getMethod("main", "");
      //
      //// register ProbeEventHandler
      //ctMethod.insertAfter("");
      //
      //ctMethod.instrument(new ExprEditor() {
      //
      //  public void edit(MethodCall m) {
      //  }
      //});
    }
    return new byte[0];
  }
}

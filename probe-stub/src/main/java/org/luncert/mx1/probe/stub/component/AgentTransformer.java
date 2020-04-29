package org.luncert.mx1.probe.stub.component;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

public class AgentTransformer implements ClassFileTransformer {
  
  private final ClassLoader classLoader = AgentTransformer.class.getClassLoader();
  
  @Override
  public byte[] transform(ClassLoader         loader,
                          String              className,
                          Class<?>            classBeingRedefined,
                          ProtectionDomain protectionDomain,
                          byte[]              classfileBuffer)
      throws IllegalClassFormatException {
    //ClassPool pool = ClassPool.getDefault();
    //// TODO: 找到主类，注册maven event handler
    //try {
    //  CtClass ctClass = pool.getCtClass(className);
    //} catch (NotFoundException e) {
    //  e.printStackTrace();
    //}
    return null;
  }
}

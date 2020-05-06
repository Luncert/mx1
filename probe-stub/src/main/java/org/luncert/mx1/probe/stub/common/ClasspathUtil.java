package org.luncert.mx1.probe.stub.common;

import org.luncert.mx1.probe.stub.exeception.ResolveMainClasspathError;

import java.lang.management.ManagementFactory;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public final class ClasspathUtil {
  
  private ClasspathUtil() {}
  
  private static String mainClasspath;
  
  /**
   * 使用不同jdk运行probe-test-app/run.bat时，拿取到的classpath值是不同的，
   * <li>jdk8环境：会同时拿到目标应用的classpath和probe-stub-agent的classpath</li>
   * <li>jdk11环境：只能拿到目标应用的classpath</li>
   * FIXME: 在idea里面跑测试的时候还是会拿到很多个classpath
   * @return classpath of the main class, not agent classpath
   */
  public static String resolveMainClasspath() {
    if (mainClasspath == null) {
      String[] classpathArray = System.getProperty("java.class.path").split(";");
  
      // classpath won't be empty
      if (classpathArray.length == 1) {
        return classpathArray[0];
      }
  
      // get all agent jar name
      Set<String> jvmAgentJars = ManagementFactory.getRuntimeMXBean().getInputArguments().stream()
          .filter(arg -> arg.startsWith("-javaagent:"))
          .map(ClasspathUtil::getJarFileName)
          .collect(Collectors.toSet());
  
      // filter all classpath has the same jar name in jvmAgentJars
      List<String> classpathList = Arrays.stream(classpathArray)
          .filter(classpath -> !jvmAgentJars.contains(clipJarFileName(classpath)))
          .collect(Collectors.toList());
  
      if (classpathList.size() != 1) {
        throw new ResolveMainClasspathError("too many or no classpath detected: " + classpathList);
      }
      
      mainClasspath = classpathList.get(0);
    }
  
    return mainClasspath;
  }
  
  /**
   * subtract agent jar file name from java agent params:
   * e.g. -javaagent:XXX/XXX/agent.jar=arg1=1&arg2=2 -> agent.jar
   */
  private static String getJarFileName(String jvmAgentArg) {
    char[] charArray = jvmAgentArg.toCharArray();
    int lastSplitIdx = 0, firstEqualIdx = charArray.length;
  
    // scan to get index of last '\' or '/' and first '='
    for (int i = 0; i < charArray.length; i++) {
      char c = charArray[i];
      if (c == '\\' || c == '/') {
        lastSplitIdx = i;
      } else if (c == '=') {
        // the first equal is the start flag of agent's parameters
        firstEqualIdx = i;
        break;
      }
    }

    return jvmAgentArg.substring(lastSplitIdx + 1, firstEqualIdx);
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
}

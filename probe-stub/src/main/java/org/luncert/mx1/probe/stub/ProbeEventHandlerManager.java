package org.luncert.mx1.probe.stub;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.AbstractFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.luncert.mx1.probe.spy.Event;
import org.luncert.mx1.probe.spy.ProbeSpy;
import org.luncert.mx1.probe.stub.annotation.ProbeEventHandler;
import org.luncert.mx1.probe.stub.exeception.InvalidHandlerMethod;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.net.JarURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

// https://blog.csdn.net/codalun/article/details/89285757
@Slf4j
class ProbeEventHandlerManager {
  
  private static final AtomicReference<List<MethodBasedProbeEventHandler>> probeEventHandlersRef
      = new AtomicReference<>();
  
  static void register(String packageName) {
    if (probeEventHandlersRef.get() == null) {
      packageName = packageName.replaceAll("\\.", "/");

      List<MethodBasedProbeEventHandler> handlers = new ArrayList<>();
      for (Class clazz : scanAllHandlerClasses(packageName)) {
        for (Method method : clazz.getMethods()) {
          ProbeEventHandler annotation = method.getAnnotation(ProbeEventHandler.class);
          if (annotation != null) {
            if (!Modifier.isStatic(method.getModifiers())) {
              log.error("Probe event handler method must be public and static: {}.",
                  methodToString(method));
              continue;
            }
            validateHandlerMethod(method);
            handlers.add(new MethodBasedProbeEventHandler(annotation.value(), method));
          }
        }
      }
      
      probeEventHandlersRef.set(handlers);
      
      handlers.forEach(handler -> ProbeSpy.register(handler.getEventName(), handler));
      
      log.debug("Following handler has been registered: {}.", handlers);
    }
  }
  
  private static List<Class> scanAllHandlerClasses(String basePackage) {
    List<Class> classList = new LinkedList<>();
    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    
    try {
      for (Enumeration<URL> resources = classLoader.getResources(basePackage);
           resources.hasMoreElements();) {
        URL url = resources.nextElement();
        if (url.getProtocol().equals("jar")) {
          JarURLConnection jarURLConnection = (JarURLConnection) url.openConnection();
          JarFile jarFile = jarURLConnection.getJarFile();
          for (Enumeration<JarEntry> entries = jarFile.entries(); entries.hasMoreElements();) {
            JarEntry entry = entries.nextElement();
            String entryName = entry.getName();
            if (entry.isDirectory() ||
                !entryName.startsWith(basePackage) ||
                !entryName.endsWith(".class")) {
              continue;
            }
            
            // add classes
            String className = entryName.substring(0, entryName.lastIndexOf("."))
                .replaceAll("/", ".");
            classList.add(Class.forName(className));
          }
        } else {
          File baseDir = new File(url.toURI());
          
          // list all files
          Collection<File> files = FileUtils.listFiles(baseDir,
              new AbstractFileFilter() {
                @Override
                public boolean accept(File file) {
                  return file.isFile() && file.getName().endsWith(".class");
                }
              }, TrueFileFilter.INSTANCE);
          
          // add classes
          for (File f : files) {
            String relativePath = f.getAbsolutePath()
                .replace(baseDir.getAbsolutePath(), "")
                .replaceAll("\\\\", "/");
            String className = (basePackage + relativePath);
            className = className.substring(0, className.lastIndexOf("."))
                .replaceAll("/", ".");
            classList.add(Class.forName(className));
          }
        }
      }
    } catch (IOException | ClassNotFoundException | URISyntaxException e) {
      log.error("Failed to scan all probe event handlers.", e);
    }
    
    return classList;
  }
  
  private static final FileScanFilter fileScanFilter = new FileScanFilter();
  
  private static class FileScanFilter extends AbstractFileFilter {
  
    @Override
    public boolean accept(File file) {
      return file.isFile() && file.getName().endsWith(".class");
    }
  }
  
  private static void validateHandlerMethod(Method method) {
    Parameter[] parameters = method.getParameters();
    if (parameters.length == 1 && parameters[0].getType().equals(Event.class)) {
      return;
    }
    throw new InvalidHandlerMethod("handler method should be like: " +
        "public static methodName(Event event), target method: " + methodToString(method));
  }
  
  @AllArgsConstructor
  private static class MethodBasedProbeEventHandler extends org.luncert.mx1.probe.spy.ProbeEventHandler {
    
    @Getter
    private String eventName;
    
    private Method handlerMethod;
    
    @Override
    public Object handle(Event event) {
      try {
        // even though the handler method is static, the object arg (null) is needed
        return handlerMethod.invoke(null, event);
      } catch (IllegalAccessException | InvocationTargetException e) {
        log.error("Failed to invoke handler method {}.", toString(), e);
        return null;
      }
    }
    
    @Override
    public String toString() {
      return methodToString(handlerMethod);
    }
  }
  
  private static String methodToString(Method method) {
    return method.getDeclaringClass().getName() + "#" + method.getName();
  }
  
}

package org.luncert.mx1.probe.stub.component.staticInfoCollector;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.luncert.mx1.probe.commons.data.staticinfo.MavenPomInfo;
import org.luncert.mx1.probe.commons.data.staticinfo.MavenStaticInfo;
import org.luncert.mx1.probe.spy.ProbeSpy;
import org.luncert.mx1.probe.stub.common.ProbeSpyEvent;
import org.luncert.mx1.probe.stub.component.AbstractInfoCollector;
import org.luncert.mx1.probe.stub.exeception.LoadMavenPomError;
import org.luncert.mx1.probe.stub.pojo.CollectorResponse;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.management.ManagementFactory;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

/**
 * To test this function, test app must be run with java directly,
 * app started by mvn will run generated classes directly, but only packaged
 * jar archive has pom.xml.
 */
@Slf4j
public class MavenStaticInfoCollector extends AbstractInfoCollector<MavenStaticInfo> {
  
  private static final String MAVEN_PATH_IN_JAR = "META-INF/maven/";
  
  private static final String POM_NAME = "pom.xml";
  
  @Override
  public CollectorResponse<MavenStaticInfo> collect() {
    MavenStaticInfo info = new MavenStaticInfo();
    info.setPoms(loadPoms());
    return CollectorResponse.succeed(info);
  }
  
  private List<MavenPomInfo> loadPoms() {
    String classpath = ManagementFactory.getRuntimeMXBean().getClassPath();
    if (classpath.endsWith(".jar")) {
      try {
        List<MavenPomInfo> infoList = new ArrayList<>();
        
        // find all pom.xml in target app jar file
        JarFile appJar = new JarFile(classpath);
        for (Enumeration<JarEntry> entries = appJar.entries(); entries.hasMoreElements(); ) {
          JarEntry entry = entries.nextElement();
          String entryName = entry.getName();
          // do filtering
          if (entryName.startsWith(MAVEN_PATH_IN_JAR) && entryName.endsWith(POM_NAME)) {
            InputStream pomInputStream = appJar.getInputStream(appJar.getEntry(entryName));
            String pomContent = IOUtils.toString(pomInputStream, Charset.defaultCharset());
            infoList.add(new MavenPomInfo(entryName.replace(MAVEN_PATH_IN_JAR, ""), pomContent));
          }
        }
        
        return infoList;
      } catch (IOException e) {
        throw new LoadMavenPomError(e);
      }
    }
    
    return Collections.emptyList();
  }
}

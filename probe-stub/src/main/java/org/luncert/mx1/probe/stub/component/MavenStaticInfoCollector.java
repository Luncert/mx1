package org.luncert.mx1.probe.stub.component;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.luncert.mx1.probe.commons.data.staticinfo.MavenPomInfo;
import org.luncert.mx1.probe.commons.data.staticinfo.MavenStaticInfo;

import java.io.File;
import java.io.IOException;
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

/**
 * To test this function, test app must be run with java directly,
 * app started by mvn will run generated classes directly, but only packaged
 * jar archive has pom.xml.
 */
@Slf4j
public class MavenStaticInfoCollector extends AbstractInfoCollector<MavenStaticInfo> {
  
  @Override
  public MavenStaticInfo collect() {
    MavenStaticInfo info = new MavenStaticInfo();
    info.setPoms(loadPoms());
    return info;
  }
  
  private List<MavenPomInfo> loadPoms() {
    try {
      String codeSourcePath = MavenStaticInfoCollector.class.getProtectionDomain()
          .getCodeSource().getLocation().toURI().getPath();
      File root = new File(codeSourcePath);
      System.out.println(codeSourcePath);
      
      System.out.println(ManagementFactory.getRuntimeMXBean().getInputArguments());
    } catch (URISyntaxException e) {
      e.printStackTrace();
    }
    return Collections.emptyList();
  }
}

package org.luncert.mx1.probe.stub;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URL;
import java.util.jar.JarFile;

@Slf4j
class ProbeSpyResourceClassLoader extends ClassLoader {
  
  private final String spyJarPath;
  private final JarFile spyJar;
  
  ProbeSpyResourceClassLoader(JarFile spyJar) {
    this.spyJar = spyJar;
    spyJarPath = spyJar.getName().replaceAll("\\\\", "/");
  }

  @Override
  protected URL findResource(String name) {
    if (spyJar.getEntry(name) == null) {
      return super.findResource(name);
    }
    
    try {
      // The ret is in such format: jar:file://jar-file-path!/class-file-path
      // Attention: use '/' not '\' => '!/' is a flag literal to create JarUrlConnection.
      // See java.net.MalformedURLException
      return new URL("jar", "", "file:" + spyJarPath + "!/" + name);
    } catch (IOException e) {
      log.error("Failed to load probe-spy resource: {}.", name, e);
      return null;
    }
  }
}

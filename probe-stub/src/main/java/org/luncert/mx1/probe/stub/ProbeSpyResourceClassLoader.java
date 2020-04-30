package org.luncert.mx1.probe.stub;

import org.luncert.mx1.probe.stub.exeception.LoadProbeSpyResourceError;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.jar.JarFile;

class ProbeSpyResourceClassLoader extends ClassLoader {
  
  private final JarFile spyJar;
  
  ProbeSpyResourceClassLoader(JarFile spyJar) {
    this.spyJar = spyJar;
  }

  @Override
  protected URL findResource(String name) {
    if (spyJar.getEntry(name) == null) {
      return super.findResource(name);
    }
    
    String spyJarPath = spyJar.getName();
    try {
      // the ret is like: file://jar-file-path!/class-file-path
      // I don't know whether 'jar:' is needed
      return Paths.get(spyJarPath + "!", name).toUri().toURL();
    } catch (MalformedURLException e) {
      throw new LoadProbeSpyResourceError(e);
    }
  }
}

package org.luncert.mx1.probe.stub.component;

import org.luncert.mx1.probe.commons.data.staticinfo.MavenStaticInfo;

import java.net.URL;

public class MavenStaticInfoCollector extends AbstractInfoCollector<MavenStaticInfo> {
  
  @Override
  public MavenStaticInfo collect() {
    MavenStaticInfo info = new MavenStaticInfo();
    loadPom();
    return info;
  }
  
  private void loadPom() {
    URL path = MavenStaticInfoCollector.class.getClassLoader().getResource("");
    System.out.println(path);
  }
}

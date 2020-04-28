package org.luncert.mx1.probe.commons.data.staticinfo;

import lombok.Data;

@Data
public class StaticInfo {
  
  private SystemStaticInfo systemStaticInfo;

  private JvmStaticInfo jvmStaticInfo;
  
  private MavenStaticInfo mavenStaticInfo;
}

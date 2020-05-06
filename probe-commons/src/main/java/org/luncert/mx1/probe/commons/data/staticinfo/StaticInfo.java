package org.luncert.mx1.probe.commons.data.staticinfo;

import lombok.Data;

@Data
public class StaticInfo {
  
  private StaticSystemInfo dynamicSystemInfo;

  private JvmStaticInfo jvmStaticInfo;
  
  private MavenStaticInfo mavenStaticInfo;
  
  private SpringStaticInfo springStaticInfo;
}

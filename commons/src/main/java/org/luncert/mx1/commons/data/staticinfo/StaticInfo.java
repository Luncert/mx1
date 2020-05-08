package org.luncert.mx1.commons.data.staticinfo;

import lombok.Data;

import java.io.Serializable;

@Data
public class StaticInfo implements Serializable {
  
  private static final long serialVersionUID = -8526362075553567065L;
  
  private StaticSystemInfo dynamicSystemInfo;

  private JvmStaticInfo jvmStaticInfo;
  
  private MavenStaticInfo mavenStaticInfo;
  
  private SpringStaticInfo springStaticInfo;
}

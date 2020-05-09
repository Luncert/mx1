package org.luncert.mx1.commons.data.staticinfo;

import lombok.Data;

import java.io.Serializable;

@Data
public class StaticAppInfo implements Serializable {
  
  private static final long serialVersionUID = -8526362075553567065L;
  
  private StaticSysInfo dynamicSystemInfo;

  private StaticJvmInfo staticJvmInfo;
  
  private StaticMavenInfo staticMavenInfo;
  
  // TODO:
  //private StaticSpringInfo staticSpringInfo;
}

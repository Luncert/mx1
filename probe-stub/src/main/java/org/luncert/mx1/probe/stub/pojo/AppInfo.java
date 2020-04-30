package org.luncert.mx1.probe.stub.pojo;

import lombok.Data;

import java.util.jar.Manifest;

@Data
public class AppInfo {
  
  private AppStartMode startMode;
  
  private String mainClasspath;

  private Manifest manifest;
}

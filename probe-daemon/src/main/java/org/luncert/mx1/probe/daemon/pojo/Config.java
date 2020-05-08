package org.luncert.mx1.probe.daemon.pojo;

import lombok.Data;
import org.luncert.mx1.commons.data.NetURL;

@Data
public class Config {
  
  private String centralServer;
  
  private boolean noBanner;
  
  private NetURL serveAddress;
}

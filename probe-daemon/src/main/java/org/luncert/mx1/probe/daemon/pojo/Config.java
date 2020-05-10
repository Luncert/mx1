package org.luncert.mx1.probe.daemon.pojo;

import lombok.Data;
import org.luncert.mx1.commons.data.NetURL;

@Data
public class Config {
  
  private NetURL centralAddress;
  
  private NetURL ipcAddress;
  
  private boolean noBanner;
}

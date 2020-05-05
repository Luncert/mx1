package org.luncert.mx1.probe.stub;

import lombok.Getter;
import org.luncert.mx1.probe.commons.data.NetURL;

public class ProbeStubConfig {

  @Getter
  private NetURL daemonUrl;
  
  static ProbeStubConfig resolveAgentOptions(String agentOptions) {
    ProbeStubConfig config = new ProbeStubConfig();
    
    return config;
  }
}

package org.luncert.mx1.probe.stub;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.luncert.mx1.commons.data.NetURL;
import org.luncert.mx1.probe.stub.exeception.AgentOptionMissingError;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
class ProbeStubConfig {
  
  @Getter
  private NetURL daemonUrl;
  
  static ProbeStubConfig resolveAgentOptions(String agentOptions) {
    if (agentOptions == null) {
      throw new NullPointerException("agent options missing");
    }
    
    ProbeStubConfig config = new ProbeStubConfig();
  
    Map<String, String> optionMap = new HashMap<>();
    for (String opt : agentOptions.split("&")) {
      int i = opt.indexOf('=');
      if (i <= 0 || i == opt.length() - 1) {
        log.error("Invalid agent option: {}", opt);
        continue;
      }
      
      String name = opt.substring(0, i);
      String value = opt.substring(i + 1);
      
      optionMap.put(name, value);
    }
  
    String daemonUrl = Optional.of(optionMap.get("daemon"))
        .orElseThrow(() -> new AgentOptionMissingError("daemon url is required"));
    config.daemonUrl = new NetURL(daemonUrl);
    
    return config;
  }
}

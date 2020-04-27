package org.luncert.mx1.probe.data.staticinfo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.luncert.mx1.probe.annotation.SystemProperty;

import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SystemStaticInfo {
  
  @SystemProperty("os.name")
  private String osName;
  
  @SystemProperty("os.arch")
  private String osArch;
  
  @SystemProperty("os.version")
  private String osVersion;
  
  /**
   * 系统环境变量
   */
  private Map<String, String> env;
}

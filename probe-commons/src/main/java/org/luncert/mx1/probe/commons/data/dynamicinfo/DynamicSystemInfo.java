package org.luncert.mx1.probe.commons.data.dynamicinfo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DynamicSystemInfo {
  
  /**
   * the system load average for the last minute
   */
  private double loadAverage;
  
  /**
   * e.g: 3012 equals to 30.12%
   */
  private short cpuUsage;
  
  private short memUsage;
  
  private short swapUsage;
}

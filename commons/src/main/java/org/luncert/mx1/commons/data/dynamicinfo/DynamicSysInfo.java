package org.luncert.mx1.commons.data.dynamicinfo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DynamicSysInfo implements Serializable {
  
  private static final long serialVersionUID = 6280804822223735179L;
  
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

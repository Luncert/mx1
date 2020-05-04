package org.luncert.mx1.probe.commons.data.dynamicinfo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SystemInfo {
  
  /**
   * e.g: 3012 equals to 30.12%
   */
  private short cpuUsage;
  
  private short memUsage;
  
  private short swapUsage;
  
  public double getCpuUsage() {
    return toPercentage(cpuUsage);
  }
  
  public void setCpuUsage(double cpuUsage) {
    this.cpuUsage = toShort(cpuUsage);
  }
  
  public double getMemUsage() {
    return toPercentage(memUsage);
  }
  
  public void setMemUsage(double memUsage) {
    this.memUsage = toShort(memUsage);
  }
  
  public double getSwapUsage() {
    return toPercentage(swapUsage);
  }
  
  public void setSwapUsage(double swapUsage) {
    this.swapUsage = toShort(swapUsage);
  }
  
  /**
   * parse 3012 to 30.12(%)
   */
  private static double toPercentage(short v) {
    return ((double) v) / 100;
    
  }
  
  /**
   * parse 30.12(%) to 3012
   */
  private static short toShort(double percentage) {
    return (short) (percentage * 100);
  }
}

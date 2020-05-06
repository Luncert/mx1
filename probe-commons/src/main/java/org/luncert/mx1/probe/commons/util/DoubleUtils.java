package org.luncert.mx1.probe.commons.util;

public final class DoubleUtils {
  
  private DoubleUtils() {}
  
  /**
   * parse 0.3012(30.12%) to 3012
   */
  public static short percentToShort(double v) {
    return (short) (v * 10000);
  }
  
  /**
   * parse 3012 to 0.3012(30.12%)
   */
  public static double shortToPercent(short v) {
    return ((double) v) / 10000;
  }
}

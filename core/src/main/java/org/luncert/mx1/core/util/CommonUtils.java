package org.luncert.mx1.core.util;

import java.util.UUID;

public final class CommonUtils {
  
  private CommonUtils() {}
  
  public static String uuid() {
    return UUID.randomUUID().toString();
  }
}

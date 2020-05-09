package org.luncert.mx1.core.util;

import org.bson.types.ObjectId;

import java.net.URL;
import java.util.UUID;

public final class CommonUtils {
  
  private CommonUtils() {}
  
  public static String uuid() {
    return UUID.randomUUID().toString();
  }
  
  public static ObjectId netAddrId(URL netAddr) {
  
  }
}

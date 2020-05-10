package org.luncert.mx1.core.util;

import org.springframework.util.DigestUtils;

import java.net.InetAddress;
import java.util.UUID;

public final class CommonUtils {
  
  private static final int MAX_COUNTER = 16777215; // 3 bytes
  
  private CommonUtils() {}
  
  public static String uuid() {
    return UUID.randomUUID().toString();
  }
  
  public static String netAddrId(InetAddress netAddr) {
    // data is array with 16 items
    return DigestUtils.md5DigestAsHex(netAddr.toString().getBytes());
  }
  
  /**
   * little endian
   */
  public static int parseToInt(byte[] bytes, int offset) {
    int v = 0;
    for (int i = 0; i < 4; i++) {
      v += ((int) bytes[offset + i]) << (8 * i);
    }
    return v;
  }
}

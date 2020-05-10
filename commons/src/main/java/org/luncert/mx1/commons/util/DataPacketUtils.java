package org.luncert.mx1.commons.util;

import lombok.extern.slf4j.Slf4j;
import org.luncert.mx1.commons.data.DataPacket;

@Slf4j
public final class DataPacketUtils {
  
  private DataPacketUtils() {}
  
  public static boolean validateDataType(DataPacket packet, Class<?> expectedType) {
    Object data = packet.getData();
    if (data  == null) {
      log.error("Invalid packet {}, expected data of type {}, got null",
          packet, expectedType);
      return false;
    }
    
    Class<?> actualType = data.getClass();
    if (!expectedType.isAssignableFrom(actualType)) {
      log.error("Invalid packet {}, expected data of type {}, got {}",
          packet, expectedType, actualType);
      return false;
    }
    
    return true;
  }
}

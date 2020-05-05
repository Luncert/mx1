package org.luncert.mx1.probe.commons.data;

import lombok.Data;

@Data
public class IpcPacket<E> {
  
  private String action;
  
  private E data;
}

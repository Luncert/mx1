package org.luncert.mx1.probe.commons.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IpcPacket<E> {
  
  private String action;
  
  private E data;
}

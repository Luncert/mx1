package org.luncert.mx1.commons.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Properties;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IpcPacket<E> implements Serializable {
  
  private static final long serialVersionUID = 8105488532451454597L;

  private String action;
  
  private Properties headers;
  
  private E data;
}

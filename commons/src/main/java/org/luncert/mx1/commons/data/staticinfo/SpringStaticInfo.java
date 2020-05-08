package org.luncert.mx1.commons.data.staticinfo;

import lombok.Data;

import java.io.Serializable;

@Data
public class SpringStaticInfo implements Serializable {
  
  private static final long serialVersionUID = 3671697363806015426L;
  
  private String version;
}

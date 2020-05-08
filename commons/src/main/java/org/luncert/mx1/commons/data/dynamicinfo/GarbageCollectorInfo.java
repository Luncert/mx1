package org.luncert.mx1.commons.data.dynamicinfo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GarbageCollectorInfo implements Serializable {
  
  private static final long serialVersionUID = -1148227689027538127L;
  
  private String name;
  
  private long collectionCount;
  
  private long collectionTime;
}

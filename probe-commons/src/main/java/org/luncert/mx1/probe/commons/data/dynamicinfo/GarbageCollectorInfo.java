package org.luncert.mx1.probe.commons.data.dynamicinfo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GarbageCollectorInfo {
  
  private String name;
  
  private long collectionCount;
  
  private long collectionTime;
}

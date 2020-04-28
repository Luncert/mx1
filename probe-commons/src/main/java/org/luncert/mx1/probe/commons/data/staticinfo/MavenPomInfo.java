package org.luncert.mx1.probe.commons.data.staticinfo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MavenPomInfo {
  
  private String path;
  
  private String content;
}

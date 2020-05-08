package org.luncert.mx1.probe.commons.data.staticinfo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MavenStaticInfo implements Serializable {
  
  private static final long serialVersionUID = 4381007202429917639L;
  
  private List<MavenPomInfo> poms;
}

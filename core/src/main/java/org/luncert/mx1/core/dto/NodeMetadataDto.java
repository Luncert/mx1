package org.luncert.mx1.core.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.luncert.mx1.commons.data.staticinfo.StaticJvmInfo;
import org.luncert.mx1.commons.data.staticinfo.StaticMavenInfo;
import org.luncert.mx1.commons.data.staticinfo.StaticSysInfo;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NodeMetadataDto {
  
  private StaticSysInfo staticSysInfo;
  
  private StaticJvmInfo staticJvmInfo;
  
  private StaticMavenInfo staticMavenInfo;
}

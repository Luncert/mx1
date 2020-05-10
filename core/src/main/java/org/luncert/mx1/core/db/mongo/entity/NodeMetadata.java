package org.luncert.mx1.core.db.mongo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.luncert.mx1.commons.data.staticinfo.StaticMavenInfo;
import org.luncert.mx1.commons.data.staticinfo.StaticJvmInfo;
import org.luncert.mx1.commons.data.staticinfo.StaticSysInfo;
import org.luncert.mx1.core.common.NetAddress;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document
public class NodeMetadata implements Serializable {
  
  private static final long serialVersionUID = 3393682389391439595L;
  
  /**
   * cipher of probe net address
   */
  @Id
  private String id;
  
  private NetAddress netAddress;
  
  private long lastUpdateTimestamp;
  
  // app info
  
  private StaticSysInfo staticSysInfo;

  private StaticJvmInfo staticJvmInfo;
  
  private StaticMavenInfo staticMavenInfo;
}

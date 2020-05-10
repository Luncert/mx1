package org.luncert.mx1.core.db.es.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.io.Serializable;

/**
 * copy of {@link org.luncert.mx1.commons.data.dynamicinfo.DynamicSysInfo}
 */
@Data
@Document(indexName = "dynamic_system_info", type="java")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DynamicSysInfo implements Serializable {
  
  private static final long serialVersionUID = -3910586269418927149L;
  
  @Id
  private String id;
  
  private String nodeId;
  
  private double loadAverage;
  
  private double cpuUsage;
  
  private double memUsage;
  
  private double swapUsage;
}

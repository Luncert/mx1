package org.luncert.mx1.core.db.es.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.io.Serializable;

// TODO: ref sb-core
@Data
@Document(indexName = "app_log", type="java")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppLog implements Serializable {
  
  private static final long serialVersionUID = 1737501851615018732L;
  
  @Id
  private String id;
  
  private long timestamp;
  
  private String logLevel;
  
  private String content;
}

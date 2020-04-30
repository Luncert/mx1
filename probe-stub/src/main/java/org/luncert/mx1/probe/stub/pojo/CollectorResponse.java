package org.luncert.mx1.probe.stub.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CollectorResponse<T> {
  
  private boolean success;
  
  private String description;
  
  private T info;
  
  public static <T> CollectorResponse<T> succeed(T info) {
    return of(true, null, info);
  }
  
  public static <T> CollectorResponse<T> failed(String description) {
    return of(false, description, null);
  }
  
  public static <T> CollectorResponse<T> of(boolean success, String description, T info) {
    return new CollectorResponse<>(success, description, info);
  }
}

package org.luncert.mx1.probe.spy;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Event<E> {
  
  private String name;
  private E data;
}

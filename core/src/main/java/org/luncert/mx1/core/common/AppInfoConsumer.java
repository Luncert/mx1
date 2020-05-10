package org.luncert.mx1.core.common;

@FunctionalInterface
public interface AppInfoConsumer<E> {
  
  void accept(E info);
}

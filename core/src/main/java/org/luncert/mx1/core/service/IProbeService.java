package org.luncert.mx1.core.service;

import org.luncert.mx1.core.common.AppInfoConsumer;

public interface IProbeService {
  
  /**
   *
   * @param nodeId
   * @param type
   * @param consumer
   * @return Subscription
   */
  <E> Subscription subscribe(String nodeId, Class<E> type, AppInfoConsumer<E> consumer);
}

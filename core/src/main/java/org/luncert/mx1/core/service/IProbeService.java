package org.luncert.mx1.core.service;

public interface IProbeService {
  
  /**
   *
   * @param probeId
   * @param consumer
   * @return Subscription
   */
  Subscription subscribe(String probeId, AppInfoConsumer consumer);
}

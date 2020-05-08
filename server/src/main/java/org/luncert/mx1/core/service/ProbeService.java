package org.luncert.mx1.core.service;

import org.luncert.mx1.core.component.ProbeDataHandler;
import org.luncert.mx1.core.component.Session;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ProbeService implements ProbeDataHandler {
  
  /**
   * save probe metadata to db
   */
  public void register() {
  
  }
  
  @Override
  public void handle(Session session, Object data) throws IOException {
    // TODO: save to db
  }
  
  /**
   *
   * @param probeId
   * @param consumer
   * @return Subscription
   */
  public Subscription subscribe(String probeId, AppInfoConsumer consumer) {
    return null;
  }
}

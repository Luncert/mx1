package org.luncert.mx1.core.component;

import java.io.IOException;

public interface ProbeDataHandler {
  
  void handle(Session session, Object data) throws IOException;
}

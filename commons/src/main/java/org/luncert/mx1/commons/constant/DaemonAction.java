package org.luncert.mx1.commons.constant;

import java.io.Serializable;

public final class DaemonAction implements Serializable {
  
  private DaemonAction() {}
  
  private static final long serialVersionUID = -8765293626311190232L;
  
  public static final String COLLECT_STATIC_APP_INFO = "/daemon/request/staticAppInfo";
}

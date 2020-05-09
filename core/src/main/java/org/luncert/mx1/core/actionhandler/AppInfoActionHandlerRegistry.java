package org.luncert.mx1.core.actionhandler;

import com.google.common.collect.ImmutableMap;
import org.luncert.mx1.core.actionmgr.ActionManager;
import org.luncert.mx1.core.actionmgr.Message;
import org.luncert.mx1.core.actionmgr.annotation.ActionHandler;
import org.luncert.mx1.core.actionmgr.annotation.ActionHandlerRegistry;
import org.luncert.mx1.core.common.constant.WsAction;
import org.luncert.mx1.core.common.constant.AppInfoType;
import org.luncert.mx1.core.service.AppInfoConsumer;
import org.luncert.mx1.core.service.impl.ProbeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@ActionHandlerRegistry
public class AppInfoActionHandlerRegistry {
  
  @Autowired
  private ActionManager actionManager;
  
  @Autowired
  private ProbeServiceImpl probeService;
  
  private final Map<String, AppInfoConsumer> consumerMap =
      ImmutableMap.<String, AppInfoConsumer>builder()
          .put(AppInfoType.CPU_USAGE, this::consumeCpuUsage)
          .put(AppInfoType.MEM_USAGE, this::consumeMemUsage)
          .build();
  
  @ActionHandler(WsAction.MONITOR_APP_INFO)
  public void monitorMonitorInfo(Message<String> message) {
    String infoType = message.getHeaders().getProperty("type");
    String probeId = message.getBody();
    probeService.subscribe(probeId, consumerMap.get(infoType));
  }
  
  public void consumeCpuUsage(Object info) {
    // info.getCpuUsage
    actionManager.createAction(WsAction.COMMIT_INFO)
        .addHeader("type", AppInfoType.CPU_USAGE)
        .body(null)
        .submit();
  }
  
  public void consumeMemUsage(Object info) {
    // info.getMemUsage
    actionManager.createAction(WsAction.COMMIT_INFO)
        .addHeader("type", AppInfoType.MEM_USAGE)
        .body(null)
        .submit();
  }
}

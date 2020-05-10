package org.luncert.mx1.core.actionhandler;

import lombok.extern.slf4j.Slf4j;
import org.luncert.mx1.core.actionmgr.ActionManager;
import org.luncert.mx1.core.actionmgr.Message;
import org.luncert.mx1.core.actionmgr.annotation.ActionHandler;
import org.luncert.mx1.core.actionmgr.annotation.ActionHandlerRegistry;
import org.luncert.mx1.core.common.constant.WsAction;
import org.luncert.mx1.core.common.constant.AppInfoType;
import org.luncert.mx1.core.db.es.entity.DynamicSysInfo;
import org.luncert.mx1.core.service.Subscription;
import org.luncert.mx1.core.service.impl.ProbeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@ActionHandlerRegistry
public class AppInfoActionHandlerRegistry {
  
  @Autowired
  private ActionManager actionManager;
  
  @Autowired
  private ProbeServiceImpl probeService;
  
  // TODO: invoke unsubscribe
  private Map<String, Subscription> subscriptionMap = new ConcurrentHashMap<>();
  
  @ActionHandler(WsAction.MONITOR_APP_INFO)
  public void monitorAppInfo(Message<String> message) {
    String infoType = message.getHeaders().getProperty("type");
    String probeId = message.getBody();
    
    if (subscriptionMap.containsKey(infoType)) {
      log.error("Unable to monitor app info, target info {} has been subscribed", infoType);
      return;
    }
    
    Subscription subscription;
    
    if (AppInfoType.CPU_USAGE.equals(infoType)) {
      subscription = probeService.subscribe(probeId, DynamicSysInfo.class, this::consumeCpuUsage);
    } else if (AppInfoType.MEM_USAGE.equals(infoType)) {
      subscription = probeService.subscribe(probeId, DynamicSysInfo.class, this::consumeMemUsage);
    } else {
      log.error("Unable to monitor app info, app info type {} is invalid", infoType);
      return;
    }
    
    subscriptionMap.put(infoType, subscription);
  }
  
  public void consumeCpuUsage(DynamicSysInfo info) {
    // info.getCpuUsage
    actionManager.createAction(WsAction.COMMIT_INFO)
        .addHeader("type", AppInfoType.CPU_USAGE)
        .body(info.getCpuUsage())
        .submit();
  }
  
  public void consumeMemUsage(DynamicSysInfo info) {
    // info.getMemUsage
    actionManager.createAction(WsAction.COMMIT_INFO)
        .addHeader("type", AppInfoType.MEM_USAGE)
        .body(info.getMemUsage())
        .submit();
  }
}

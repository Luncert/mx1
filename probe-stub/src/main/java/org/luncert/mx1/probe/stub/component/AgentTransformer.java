package org.luncert.mx1.probe.stub.component;

import org.luncert.mx1.probe.stub.pojo.AppInfo;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;

public abstract class AgentTransformer implements ClassFileTransformer {
  
  public abstract boolean accept(AppInfo appInfo);
  
  public abstract void init(Instrumentation inst, AppInfo appInfo);
}

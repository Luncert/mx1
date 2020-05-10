package org.luncert.mx1.commons.util;


import org.jboss.marshalling.MarshallerFactory;
import org.jboss.marshalling.Marshalling;
import org.jboss.marshalling.MarshallingConfiguration;

import io.netty.handler.codec.marshalling.DefaultMarshallerProvider;
import io.netty.handler.codec.marshalling.DefaultUnmarshallerProvider;
import io.netty.handler.codec.marshalling.MarshallerProvider;
import io.netty.handler.codec.marshalling.MarshallingDecoder;
import io.netty.handler.codec.marshalling.MarshallingEncoder;
import io.netty.handler.codec.marshalling.UnmarshallerProvider;

public class MarshallingCodeCFactory {
  
  /**
   * create MarshallingDecoder
   */
  public static MarshallingDecoder buildMarshallingDecoder() {
    final MarshallerFactory factory = Marshalling.getProvidedMarshallerFactory("serial"); // 使用参数 serial 创建Java序列化工厂对象
    final MarshallingConfiguration configuration = new MarshallingConfiguration();
    configuration.setVersion(5); // 设置版本号为5
    UnmarshallerProvider provider = new DefaultUnmarshallerProvider(factory, configuration);
    return new MarshallingDecoder(provider, 4096);
  }
  
  /**
   * create MarshallingEncoder
   */
  public static MarshallingEncoder buildMarshallingEncoder() {
    final MarshallerFactory factory = Marshalling.getProvidedMarshallerFactory("serial");
    final MarshallingConfiguration configuration = new MarshallingConfiguration();
    configuration.setVersion(5);
    MarshallerProvider provider = new DefaultMarshallerProvider(factory, configuration);
    return new MarshallingEncoder(provider);
  }
  
}
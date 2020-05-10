package org.luncert.mx1.core.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.net.InetSocketAddress;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NetAddress {
  
  private String host;
  
  private int port;
  
  public static NetAddress of(InetSocketAddress inetSocketAddress) {
    return new NetAddress(inetSocketAddress.getHostString(), inetSocketAddress.getPort());
  }
}

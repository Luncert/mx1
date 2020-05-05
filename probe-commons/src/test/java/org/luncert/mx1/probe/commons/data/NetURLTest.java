package org.luncert.mx1.probe.commons.data;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class NetURLTest {
  
  @Test
  public void test() {
    String protocol = "tcp";
    String host = "localhost";
    int port = 8000;
    String path = "api/v1/";
    
    NetURL url = new NetURL(protocol+ "://" + host + ":" + port + "/" + path);
    
    Assert.assertEquals(protocol, url.getProtocol());
    Assert.assertEquals(host, url.getHost());
    Assert.assertEquals(port, url.getPort());
    Assert.assertEquals(path, url.getPath());
  }
}

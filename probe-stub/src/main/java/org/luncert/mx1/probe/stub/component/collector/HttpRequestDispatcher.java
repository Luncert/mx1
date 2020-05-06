package org.luncert.mx1.probe.stub.component.collector;

import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;

@Deprecated
public class HttpRequestDispatcher {
  
  HttpResponse handle(FullHttpRequest msg) throws Exception {
    String uri = msg.uri();
    System.out.println(uri);
    return new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
        HttpResponseStatus.OK);
  }
}

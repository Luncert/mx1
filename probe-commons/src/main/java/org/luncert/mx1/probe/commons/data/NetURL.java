package org.luncert.mx1.probe.commons.data;

import lombok.Getter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NetURL {
  
  private static final Pattern PATTERN =
      Pattern.compile("(?<protocol>\\w+)://" +
          "(?<host>[\\w|\\d]+):" +
          "(?<port>\\d+)/(?<path>.*)");
  
  @Getter
  private final String protocol;
  
  @Getter
  private final String host;
  
  @Getter
  private final int port;
  
  @Getter
  private final String path;
  
  
  public NetURL(String urlStr) {
    Matcher matcher = PATTERN.matcher(urlStr);
    if (!matcher.find()) {
      throw new IllegalArgumentException("invalid net url, ref: protocol://host:port/path");
    }
    
    this.protocol = matcher.group("protocol");
    this.host = matcher.group("host");
    this.port = Integer.valueOf(matcher.group("port"));
    this.path = matcher.group("path");
  }
}

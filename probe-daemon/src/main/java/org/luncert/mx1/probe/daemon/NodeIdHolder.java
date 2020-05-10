package org.luncert.mx1.probe.daemon;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.luncert.mx1.probe.daemon.execption.DaemonInitError;
import org.luncert.mx1.probe.daemon.execption.SaveNodeIdError;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Paths;

public class NodeIdHolder {
  
  private static String nodeId;
  
  static {
    // try to read nodeId from fs
    File file = FileUtils.getTempDirectory();
    File nodeIdFile = Paths.get(file.getAbsolutePath(), "mx1probe-node-id").toFile();
    try {
      if (nodeIdFile.exists()) {
        nodeId = FileUtils.readFileToString(nodeIdFile, Charset.defaultCharset());
      } else {
        if (!nodeIdFile.createNewFile()) {
          throw new IOException("failed to create file: " + nodeIdFile.getAbsolutePath());
        }
      }
    } catch (IOException e) {
      throw new DaemonInitError(e);
    }
  }
  
  private NodeIdHolder() {}
  
  public synchronized static void set(String newId) {
    if (!StringUtils.isEmpty(nodeId)) {
      throw new UnsupportedOperationException("daemon has already been assigned a node id.");
    }
    
    nodeId = newId;
    
    // save to fs
    File file = FileUtils.getTempDirectory();
    File nodeIdFile = Paths.get(file.getAbsolutePath(), "mx1probe-node-id").toFile();
    try {
      FileUtils.write(nodeIdFile, newId, Charset.defaultCharset());
    } catch (IOException e) {
      throw new SaveNodeIdError(e);
    }
  }
  
  public static String get() {
    return nodeId;
  }
}

package org.luncert.mx1.probe.daemon;

import org.luncert.mx1.probe.daemon.execption.StubDataReceiveError;

import java.io.IOException;

/**
 * ref: https://stackoverflow.com/questions/25396664/shared-memory-between-two-jvms
 */
public class StubDataReceiver {
  
  private static final String SHARED_MEM_NAME = "mx1probe@sharedmem";
  
  StubDataReceiver(int bufferSize) throws IOException {
  }
  
  private String getSharedMemFilePath() {
    // TODO: windows
    return "/dev/shm/" + SHARED_MEM_NAME;
  }
  
  void transport() {
  }
  
  void destroy() {
    try {
    } catch (IOException e) {
      throw new StubDataReceiveError("failed to close shared memory resource", e);
    }
  }
}

package org.luncert.mx1.probe.commons.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public final class IOUtils {
  
  private static final int DEFAULT_BUFFER_SIZE = 1024;
  
  private IOUtils() {}
  
  public static CopyStreamUtil copyStream(InputStream inputStream) {
    return new CopyStreamUtil(inputStream);
  }
  
  public static class CopyStreamUtil {
    
    private InputStream inputStream;
    private int bufferSize;
    private List<StreamConsumer> consumerList;
    
    private CopyStreamUtil(InputStream inputStream) {
      this(inputStream, DEFAULT_BUFFER_SIZE);
    }
    
    private CopyStreamUtil(InputStream inputStream, int bufferSize) {
      if (inputStream == null) {
        throw new NullPointerException();
      }
      
      this.inputStream = inputStream;
      this.bufferSize = bufferSize;
      consumerList = new ArrayList<>();
    }
    
    public CopyStreamUtil addConsumer(OutputStream outputStream) {
      consumerList.add(outputStream::write);
      return this;
    }
    
    public CopyStreamUtil addConsumer(StreamConsumer consumer) {
      consumerList.add(consumer);
      return this;
    }
    
    public void go() throws IOException {
      byte[] buffer = new byte[bufferSize];
      int r;
      while ((r = inputStream.read(buffer, 0, bufferSize)) != -1) {
        for (StreamConsumer consumer: consumerList) {
          consumer.write(buffer, 0, r);
        }
      }
    }
  }
  
  @FunctionalInterface
  public interface StreamConsumer {
    
    void write(byte[] data, int off, int len) throws IOException;
  }
}

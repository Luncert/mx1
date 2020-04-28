package org.luncert.mx1.probe.commons.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;

public class DualChannelStream {
  
  private static final int END_OF_STREAM = -1;
  private static final int CHUNK_IS_WRITEABLE_NOT_READABLE = -2;
  
  private final int chunkSize;
  private final boolean blocking;
  
  private volatile boolean closed;
  
  private AtomicInteger writeNum = new AtomicInteger();
  private AtomicInteger readNum = new AtomicInteger();
  
  private ChunkList chunkList = new ChunkList();
  private ChunkList usedChunkList = new ChunkList();
  
  public DualChannelStream(int chunkSize, boolean blocking) {
    this.chunkSize = chunkSize;
    this.blocking = blocking;
  }
  
  private static class ChunkList extends LinkedList<Chunk> {
    
    @Override
    public synchronized boolean isEmpty() {
      return super.isEmpty();
    }
    
    @Override
    public synchronized boolean add(Chunk chunk) {
      return super.add(chunk);
    }
    
    @Override
    public synchronized Chunk getFirst() {
      return super.getFirst();
    }
    
    @Override
    public synchronized Chunk getLast() {
      return super.getLast();
    }
    
    @Override
    public synchronized Chunk removeFirst() {
      return super.removeFirst();
    }
    
  }
  
  private class Chunk {
    byte[] data;
    int writePos = 0;
    int readPos = 0;
    
    Chunk(int chunkSize) {
      data = new byte[chunkSize];
    }
    
    boolean isWriteable() {
      return writePos < data.length;
    }
    
    boolean isReadable() {
      return readPos < writePos;
    }
    
    boolean write(int b) {
      if (isWriteable()) {
        // data[writePos++] = (byte) b; writePos will add 1 before set data, it's not atomic operation
        data[writePos] = (byte) b;
        writePos++;
        return true;
      }
      return false;
    }
    
    /**
     * @return byte if chunk is readable, -2 if chunk is writeable, -1 if chunk is not readable and writeable
     */
    int read() {
      //in case writePos be changed, just make a copy
      final int writePos = this.writePos;
      if (readPos < writePos) {
        int ret = data[readPos] & 0xff;
        readPos++;
        return ret;
      }
      if (writePos < data.length) {
        return CHUNK_IS_WRITEABLE_NOT_READABLE;
      }
      return END_OF_STREAM;
    }
    
    void reset() {
      writePos = 0;
      readPos = 0;
    }
  }
  
  protected void write(int b) throws IOException {
    if (DualChannelStream.this.closed) {
      throw new IOException("DualChannelStream has been closed.");
    }
    
    Chunk chunk;
    if (chunkList.isEmpty()) {
      //chunkList is empty, it's possible all chunks have been used and moved to usedChunkList
      writeConsumedChunk(b);
    } else {
      chunk = chunkList.getLast();
      if (chunk.write(b)) {
        writeNum.incrementAndGet();
      } else {
        //failed to write chunk because it has no space for writing (but readable),
        //try to get a used chunk to write
        writeConsumedChunk(b);
      }
    }
    
    if (blocking) {
      synchronized (this) {
        notify();
      }
    }
  }
  
  private void writeConsumedChunk(int b) {
    Chunk chunk;
    
    if (usedChunkList.isEmpty()) {
      //no consumed chunk, lets create a new chunk
      chunk = new Chunk(chunkSize);
    } else {
      chunk = usedChunkList.removeFirst();
      chunk.reset();
    }
    
    chunk.write(b);
    writeNum.incrementAndGet();
    chunkList.add(chunk);
  }
  
  protected int read() throws IOException {
    if (chunkList.isEmpty()) {
      //not sure whether new chunk was created after waiting, need another empty check
      return waitForRead() ? read() : END_OF_STREAM;
    }
    
    return readFirstChunk();
  }
  
  private int readFirstChunk() throws IOException {
    Chunk chunk = chunkList.getFirst();
    int ret = chunk.read();
    
    if (ret == CHUNK_IS_WRITEABLE_NOT_READABLE) {
      //chunk is not readable but writeable
      return waitForRead() ? readFirstChunk() : END_OF_STREAM;
    }
    
    if (ret == END_OF_STREAM) {
      //chunk is all-consumed and not writeable, move it to usedChunkList
      assert chunkList.removeFirst().equals(chunk);
      usedChunkList.add(chunk);
      
      //now chunkList may be empty, need another empty check before turning to read next chunk
      return read();
    }
    
    readNum.incrementAndGet();
    return ret;
  }
  
  private boolean waitForRead() throws IOException {
    if (!closed && blocking) {
      //wait for new data
      synchronized (this) {
        try {
          wait();
        } catch (InterruptedException e) {
          throw new IOException(e);
        }
      }
      return true;
    }
    return false;
  }
  
  public int size() {
    return writeNum.get()- readNum.get();
  }
  
  public void close() {
    synchronized (this) {
      closed = true;
      notify();
    }
  }
  
  private OutputStream writeStream;
  private InputStream readStream;
  
  public synchronized OutputStream getWriteStream() {
    if (writeStream == null) {
      writeStream = new WriteStream();
    }
    return writeStream;
  }
  
  public synchronized InputStream getReadStream() {
    if (readStream == null) {
      readStream = new ReadStream();
    }
    return readStream;
  }
  
  protected class WriteStream extends OutputStream {
    
    @Override
    public synchronized void write(int b) throws IOException {
      DualChannelStream.this.write(b);
    }
    
    @Override
    public void close() {
      DualChannelStream.this.close();
    }
  }
  
  protected class ReadStream extends InputStream {
    
    @Override
    public synchronized int read() throws IOException {
      return DualChannelStream.this.read();
    }
    
    @Override
    public void close() {
      DualChannelStream.this.close();
    }
  }
}
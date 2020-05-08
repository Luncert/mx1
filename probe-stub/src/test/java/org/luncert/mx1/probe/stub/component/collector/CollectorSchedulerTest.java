package org.luncert.mx1.probe.stub.component.collector;

import lombok.Getter;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.luncert.mx1.commons.data.IpcAction;
import org.luncert.mx1.commons.data.IpcPacket;
import org.luncert.mx1.probe.ipc.IpcChannel;
import org.luncert.mx1.probe.stub.pojo.CollectorResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RunWith(JUnit4.class)
public class CollectorSchedulerTest {
  
  @Test
  public void testSuccess() throws InterruptedException {
    CollectorRegistry registry = new CollectorRegistry();
    TestIpcChannel ipcChannel = new TestIpcChannel();
    CollectorScheduler scheduler = new CollectorScheduler(registry, ipcChannel);
    scheduler.start();
    
    Thread.sleep(100 * 10);
    
    scheduler.stop();
    
    // TODO: expect to receive 20 IpcPacket, actually got 6
    System.out.println(ipcChannel.getInfoCount());
  }
  
  private static class TestIpcChannel extends IpcChannel {
    
    @Getter
    private Map<Class, Integer> infoCount = new HashMap<>();
  
    @Override
    @SuppressWarnings("unchecked")
    public void write(Object object) {
      assert object instanceof IpcPacket;
      
      IpcPacket<CollectorResponse> packet = (IpcPacket<CollectorResponse>) object;
      Assert.assertEquals(IpcAction.COMMIT_INFO, packet.getAction());
      
      CollectorResponse rep = packet.getData();
      Assert.assertTrue(rep.isSuccess());
      
      Object info = rep.getInfo();
      //System.out.println(info);
      infoCount.compute(info.getClass(), (k, v) -> v == null ? 1 : v + 1);
    }
  
    @Override
    public void close() throws IOException {
    
    }
  }
}

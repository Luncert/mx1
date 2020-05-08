package org.luncert.mx1.probe.stub;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RunWith(JUnit4.class)
public class ProbeStubCleanerTest {
  
  private boolean testMethod1Invoked;
  
  private boolean testMethod2Invoked;
  
  @Test
  public void testSuccess() {
    ProbeStubCleaner cleaner = new ProbeStubCleaner()
        .addShutdownTask(this::testMethod1)
        .addShutdownTask(this::testMethod2);
    
    cleaner.run();
  
    Assert.assertTrue(testMethod1Invoked);
    Assert.assertTrue(testMethod2Invoked);
  }
  
  public void testMethod1() {
    testMethod1Invoked = true;
  }
  
  public void testMethod2() {
    testMethod2Invoked = true;
  }
  
  // TODO: how to add TestAppender to logger in other classes?
  //@Test
  public void testException() {
    TestAppender testAppender = new TestAppender();
    testAppender.start();
    
    Logger logger = (Logger) log;
    logger.addAppender(testAppender);
    logger.setAdditive(false); // set to true if root should log too
  
    ProbeStubCleaner cleaner = new ProbeStubCleaner()
        .addShutdownTask(this::testMethod3);
  
    cleaner.run();
    
    logger.detachAppender(testAppender);
    
    // assert log
    System.out.println(testAppender.getLogs());
  }
  
  public void testMethod3() {
    throw new UnsupportedOperationException();
  }
}

// for log assertion, ref:
// https://stackoverflow.com/questions/1827677/how-to-do-a-junit-assert-on-a-message-in-a-logger
class TestAppender extends AppenderBase<ILoggingEvent> {
  
  private final List<ILoggingEvent> logs = new ArrayList<>();
  
  @Override
  protected void append(ILoggingEvent eventObject) {
    logs.add(eventObject);
  }
  
  List<ILoggingEvent> getLogs() {
    return logs;
  }
}
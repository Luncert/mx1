package org.luncert.mx1.probe.stub;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@RunWith(JUnit4.class)
public class ProbeDaemonStarterTest {
  
  @Test
  public void testSuccess() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Method method = ProbeDaemonStarter.class.getDeclaredMethod("findProbeDaemonJar");
    method.setAccessible(true);
    method.invoke(null);
  }
}

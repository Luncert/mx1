package org.luncert.mx1.probe.commons.util;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.luncert.mx1.probe.commons.exception.InvocationError;

@RunWith(JUnit4.class)
public class InvokableTest {
  
  @Test
  public void testSuccess() throws InvocationError {
    TestClass testClass = new TestClass(null);
    
    Invokable invokable = new Invokable();
    invokable.bind(testClass).hello(null);
    Assert.assertEquals(testClass.hello("Luncert"), invokable.apply("Luncert"));
  }
  
  public static class TestClass {
    
    public TestClass(String testParam) {
    
    }
  
    public String hello(String name) {
      return this.hashCode() + ": Hello, " + name;
    }
  }
}

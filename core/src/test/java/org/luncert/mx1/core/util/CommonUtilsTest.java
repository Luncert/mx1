package org.luncert.mx1.core.util;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class CommonUtilsTest {
  
  @Test
  public void testParseBytesToInt() {
    byte[] bytes = new byte[]{127, 1, 0, 0};
    int v = CommonUtils.parseToInt(bytes, 0);
    Assert.assertEquals(127 + 256, v);
  }
}

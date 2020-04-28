package org.luncert.mx1.probe.daemon;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class BannerLoaderTest {
  
  @Test
  public void test() {
    BannerLoader.print("banner not found");
  }
}

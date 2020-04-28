package org.luncert.mx1.probe.testapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class App {
  
  public static void main(String[] args) {
    ClassLoader loader = App.class.getClassLoader();
    while (loader != null) {
      System.out.println(loader);
      loader = loader.getParent();
    }
    SpringApplication.run(App.class);
  }
}

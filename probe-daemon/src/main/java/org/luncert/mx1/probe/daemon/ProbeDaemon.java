package org.luncert.mx1.probe.daemon;

public class ProbeDaemon {
  
  public static void main(String[] args) {
    ProbeDaemon app = new ProbeDaemon();
    app.start(args);
  }
  
  private void start(String[] args) {
    // https://commons.apache.org/proper/commons-cli/usage.html
    Config config = ConfigLoader.load(args);
    if (config == null) {
      System.exit(1);
    }
  
    BannerLoader.print("");
  }
}

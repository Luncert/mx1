package org.luncert.mx1.probe.daemon;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.io.IOUtils;
import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;

import static org.fusesource.jansi.Ansi.ansi;

public class App {
  
  public static void main(String[] args) {
    
    BannerLoader.print("");

    // https://commons.apache.org/proper/commons-cli/usage.html
    Object config = parseArguments(args);
    
  }
  
  private static Object parseArguments(String[] args) {
    Options options = new Options();
    options.addRequiredOption("s", "server", true, "address of central server");
  
    CommandLineParser parser = new DefaultParser();
    try {
      CommandLine cmd = parser.parse(options, args);
      System.out.println(Arrays.toString(cmd.getOptions()));
    } catch (ParseException e) {
      printParseError(e);
      System.exit(1);
    }
    return null;
  }
  
  private static void printParseError(ParseException e) {
    if (e instanceof MissingOptionException) {
      System.out.println(ansi().fgBrightRed().a(e.getMessage()).fg(Ansi.Color.WHITE));
    }
  }
}

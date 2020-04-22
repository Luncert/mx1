package org.luncert.mx1.probe.daemon;

import com.google.common.collect.ImmutableList;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.lang3.StringUtils;
import org.fusesource.jansi.AnsiConsole;
import org.luncert.mx1.probe.daemon.pojo.Config;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import static org.fusesource.jansi.Ansi.ansi;

@Slf4j
class ConfigLoader {
  
  private static final List<Option> OPTIONS = ImmutableList.<Option>builder()
      .add(Option.builder("h").longOpt("help")
          .desc("help").hasArg(false)
          .build())
      .add(Option.builder("s").longOpt("server")
          .desc("address of central server").hasArg(true)
          .build())
      .add(Option.builder("c").longOpt("config")
          .desc("config file path").hasArg(true)
          .build())
      .build();
  
  static Config load(String[] args) {
    CommandLineParser parser = new DefaultParser();
    try {
      CommandLine cmd = parser.parse(getOptions(), args);
      
      if (cmd.hasOption("h")) {
        printUsage();
        System.exit(0);
      }
      Config config;
      if (cmd.hasOption("c")) {
        config = ConfigLoader.load(new File(cmd.getOptionValue("c")));
      } else {
        config = new Config();
      }
      
      if (cmd.hasOption("s")) {
        if (config.getCentralServer() != null) {
          log.warn("config file field was overwritten by cmd args: centralServer");
        }
        config.setCentralServer(cmd.getOptionValue("s"));
      }
      
      return config;
    } catch (ParseException | IOException e) {
      printParseError(e);
    }
    return null;
  }
  
  private static Options getOptions() {
    Options options = new Options();
    OPTIONS.forEach(options::addOption);
    return options;
  }
  
  private static void printUsage() {
    int maxLongOptLen = 0;
    for (Option option : OPTIONS) {
      String longOpt = option.getLongOpt();
      if (longOpt.length() > maxLongOptLen) {
        maxLongOptLen = longOpt.length();
      }
    }
    
    Object[] optArray = OPTIONS.stream().map(Option::getOpt).toArray();
    String opts = StringUtils.join(optArray);
    
    StringBuilder builder = new StringBuilder();
    builder.append("Version: 1.0-SNAPSHOT").append('\n');
    builder.append("Usage: probe-daemon -[").append(opts).append("]\n");
    builder.append("Options:").append('\n');
    for (Option option : OPTIONS) {
      builder.append("  -").append(option.getOpt()).append(",").append(option.getLongOpt())
          .append(" ");
      addAlignSpace(builder, maxLongOptLen - option.getLongOpt().length());
      builder.append(option.getDescription()).append('\n');
    }
    System.out.print(builder.toString());
  }
  
  private static void addAlignSpace(StringBuilder builder, int i) {
    while (i > 0) {
      builder.append(' ');
      i--;
    }
  }
  
  private static void printParseError(Exception e) {
    AnsiConsole.systemInstall();
    System.out.println(ansi().fgBrightRed().a(e.getMessage()).reset());
    AnsiConsole.systemUninstall();
  }
  
  static Config load(File file) throws IOException {
    throw new UnsupportedOperationException();
    //Config config = new Config();
    //
    //Yaml yaml = new Yaml();
    //Map<String, String> configMap = yaml.load(new FileInputStream(file));
    //
    //return config;
  }
  
  static Config load(URL url) {
    return null;
  }
}

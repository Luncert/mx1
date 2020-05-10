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
import org.luncert.mx1.commons.data.NetURL;
import org.luncert.mx1.probe.daemon.pojo.Config;

import java.util.List;

import static org.fusesource.jansi.Ansi.ansi;

// https://commons.apache.org/proper/commons-cli/usage.html
@Slf4j
class ConfigLoader {
  
  private static final List<Option> OPTIONS = ImmutableList.<Option>builder()
      .add(Option.builder("h").longOpt("help")
          .desc("help").hasArg(false)
          .build())
      .add(Option.builder("c").longOpt("central")
          .desc("address of central server").hasArg(true).required(true)
          .build())
      .add(Option.builder("b").longOpt("bind")
          .desc("serving address of daemon").hasArg(true).required(true)
          .build())
      .add(Option.builder("noBanner").longOpt("noBanner")
          .desc("do not print banner").hasArg(false)
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
      
      Config config = new Config();
      
      // central server
      if (cmd.hasOption("s")) {
        config.setCentralAddress(new NetURL(cmd.getOptionValue("s")));
      }
      
      // binding
      if (cmd.hasOption("b")) {
        config.setBinding(new NetURL(cmd.getOptionValue("b")));
      }
      
      // banner
      if (cmd.hasOption("noBanner")) {
        config.setNoBanner(true);
      }
      
      return config;
    } catch (ParseException e) {
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
}

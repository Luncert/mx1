package org.luncert.mx1.probe.daemon;

import com.google.common.collect.ImmutableMap;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * smslant
 */
@Slf4j
public class BannerLoader {
  
  public static void print(String defaultValue) {
    String banner = defaultValue;
    
    // load from disk
    URL url = ProbeDaemon.class.getClassLoader().getResource("banner.txt");
    if (url != null) {
      try {
        InputStream inputStream = url.openStream();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        IOUtils.copy(inputStream, outputStream);
        banner = new String(outputStream.toByteArray());
      } catch (IOException e) {
        // ignored
      }
    }
    
    // parse
    AnsiConsole.systemInstall();
    
    Ansi ansi = Ansi.ansi();
    
    for (Entry entry : parseBanner(banner)) {
      append(ansi, entry.colorName, entry.text);
    }

    System.out.println(ansi);
    AnsiConsole.systemUninstall();
  }
  
  private static List<Entry> parseBanner(String banner) {
    List<Entry> entries = new LinkedList<>();
    
    Entry entry = new Entry();
    
    StringBuilder builder = new StringBuilder();
    boolean started = false;
    for (char c : banner.toCharArray()) {
      if (c == '{' && !started) {
        // add last entry to list
        if (builder.length() > 0) {
          entry.text = builder.toString();
          builder.delete(0, builder.length());
          
          if (entry.colorName == null) {
            entry.colorName = "white";
          }
          
          entries.add(entry);
        }
        
        // create new entry
        started = true;
        entry = new Entry();
        continue;
      }
      if (c == ':' && started) {
        entry.colorName = builder.toString();
        builder.delete(0, builder.length());
        continue;
      }
      if (c == '}' && started) {
        entry.text = builder.toString();
        builder.delete(0, builder.length());
  
        if (entry.colorName == null) {
          entry.colorName = "white";
        }
        
        entries.add(entry);
        entry = new Entry();
  
        started = false;
        continue;
      }

      builder.append(c);
    }
    
    return entries;
  }

  @ToString
  private static class Entry {
    String colorName;
    String text;
  }
  
  private static Map<String, Ansi.Color> COLORS = ImmutableMap.<String, Ansi.Color>builder()
      .put(Ansi.Color.WHITE.toString(), Ansi.Color.WHITE)
      .put(Ansi.Color.BLACK.toString(), Ansi.Color.BLACK)
      .put(Ansi.Color.BLUE.toString(), Ansi.Color.BLUE)
      .put(Ansi.Color.CYAN.toString(), Ansi.Color.CYAN)
      .put(Ansi.Color.GREEN.toString(), Ansi.Color.GREEN)
      .put(Ansi.Color.MAGENTA.toString(), Ansi.Color.MAGENTA)
      .put(Ansi.Color.RED.toString(), Ansi.Color.RED)
      .put(Ansi.Color.YELLOW.toString(), Ansi.Color.YELLOW)
      .put(Ansi.Color.DEFAULT.toString(), Ansi.Color.DEFAULT)
      .build();
  
  private static void append(Ansi ansi, String colorName, String text) {
    String rawColorName = colorName.toUpperCase().replace("BRIGHT", "");
    
    Ansi.Color color = COLORS.get(rawColorName);
    if (color == null) {
      log.warn("Invalid color name defined in banner: {}", rawColorName);
      color = Ansi.Color.WHITE;
    }
    
    if (rawColorName.length() < colorName.length()) {
      ansi.fgBright(color).a(text);
    } else {
      ansi.fg(color).a(text);
    }
  }
}

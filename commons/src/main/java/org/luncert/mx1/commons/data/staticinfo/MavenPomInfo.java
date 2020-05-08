package org.luncert.mx1.commons.data.staticinfo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MavenPomInfo implements Serializable {
  
  private static final long serialVersionUID = 9219557626545502927L;
  
  private String path;
  
  private String content;
}

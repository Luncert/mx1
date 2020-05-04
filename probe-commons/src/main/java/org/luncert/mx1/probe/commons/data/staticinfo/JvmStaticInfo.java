package org.luncert.mx1.probe.commons.data.staticinfo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.luncert.mx1.probe.commons.annotation.SystemProperty;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JvmStaticInfo {
  
  @SystemProperty("java.version")
  private String javaVersion;
  
  @SystemProperty("java.vendor")
  private String javaVendor;
  
  @SystemProperty("java.vm.specification.version")
  private String vmSpecificationVersion;
  
  @SystemProperty("java.vm.specification.vendor")
  private String vmSpecificationVendor;
  
  @SystemProperty("java.vm.specification.name")
  private String vmSpecificationName;
  
  @SystemProperty("java.vm.version")
  private String vmVersion;
  
  @SystemProperty("java.vm.vendor")
  private String vmVendor;
  
  @SystemProperty("java.vm.name")
  private String vmName;
  
  @SystemProperty("java.class.version")
  private String javaClassVersion;
  
  @SystemProperty(keys = {"java.compiler", "sun.management.compiler"})
  private String javaCompiler;
  
  private List<String> setupArguments;
  
  /**
   * the total amount of memory currently available for current
   * and future objects, measured in bytes.
   */
  private long totalMemory;
  
  /**
   * the maximum amount of memory that the virtual machine will
   * attempt to use, measured in bytes.
   */
  private long maxMemory;
  
  private String compilerName;
}

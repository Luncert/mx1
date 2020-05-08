package org.luncert.mx1.core.db.es.repo;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.luncert.mx1.core.db.es.entity.AppLog;
import org.luncert.mx1.core.util.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AppLogRepoTest {
  
  @Autowired
  private IAppLogRepo appLogRepo;
  
  @Test
  public void test() {
    String id = CommonUtils.uuid();
    AppLog data = AppLog.builder()
        .id(id)
        .logLevel("INFO")
        .content("test log")
        .build();
    appLogRepo.save(data);
  
    Optional<AppLog> optionalAppLog = appLogRepo.findById(id);
    Assert.assertTrue(optionalAppLog.isPresent());
    Assert.assertEquals(data, optionalAppLog.get());
  }
}

@echo off

java -javaagent:C:\Users\i507145\Desktop\FinalClass\mx1probe\probe-stub\target\mx1probe-stub-1.0.0-SNAPSHOT-jar-with-dependencies.jar -jar target\probe-test-1.0-SNAPSHOT.jar

:: mvn spring-boot:run -Dspring-boot.run.agents=C:\Users\i507145\Desktop\FinalClass\mx1probe\probe-stub\target\mx1probe-stub-1.0.0-SNAPSHOT-jar-with-dependencies.jar
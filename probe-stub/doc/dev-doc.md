# References
* metrics: https://metrics.dropwizard.io/4.1.2/getting-started.html

各种Metrics
CustomerReporter：实现ScheduledReporter将metrics发送到CollectorDaemon

* assembly: ```mvn package assembly:single```
* spring.factories: spring boot maven plugin: ```mvn spring-boot:repackage```. Ref:
    * https://www.cnblogs.com/jpfss/p/11098740.html
    * https://stackoverflow.com/questions/38792031/springboot-making-jar-files-no-auto-configuration-classes-found-in-meta-inf
    
* jvm system properties: https://blog.csdn.net/u013007900/article/details/50428943/
* sigar: https://www.cnblogs.com/perkins/p/7743511.html
* 获取JVM参数:https://www.cnblogs.com/BINGJJFLY/p/7610431.html
* build specified module: mvn package -DskipTests -pl module -am
* agent and app are using the same AppClassLoader and PlatformClassLoader (as same as ExtensionClassLoader)
* to get the main class, we have to break the parent delegation model
* https://blogs.oracle.com/ouchina/javaagent
* https://speakerdeck.com/shelajev/taming-javaagents-bcn-jug-2015
* transformer是在要加载某个类时起作用的，如果transform过程中抛出的未捕获的异常是不会被中止程序运行的，有可能是某个地方捕获了然后忽略了。
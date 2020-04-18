# References
* metrics: https://metrics.dropwizard.io/4.1.2/getting-started.html

各种Metrics
CustomerReporter：实现ScheduledReporter将metrics发送到CollectorDaemon

* assembly: ```mvn package assembly:single```
* spring.factories: spring boot maven plugin: ```mvn spring-boot:repackage```. Ref:
    * https://www.cnblogs.com/jpfss/p/11098740.html
    * https://stackoverflow.com/questions/38792031/springboot-making-jar-files-no-auto-configuration-classes-found-in-meta-inf
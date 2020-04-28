FROM ascdc/jdk8:latest
LABEL Description="This image contains mx1probe based on jdk8." Vendor="Luncert" Version="1.0"

# install apache-commons-daemon binary
RUN apt-get update && apt-get install -y jsvc
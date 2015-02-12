FROM ubuntu:trusty
MAINTAINER James Baxter <j.w.baxter+licensing@gmail.com>

# Install Java 8
RUN echo "deb http://ppa.launchpad.net/webupd8team/java/ubuntu trusty main" > /etc/apt/sources.list.d/jdk-8-sources.list && \
    echo "deb-src http://ppa.launchpad.net/webupd8team/java/ubuntu trusty main" >> /etc/apt/sources.list.d/jdk-8-sources.list && \
    apt-key adv --keyserver keyserver.ubuntu.com --recv-keys EEA14886 && \
	apt-get update && \
	echo debconf shared/accepted-oracle-license-v1-1 select true | debconf-set-selections && \
    echo debconf shared/accepted-oracle-license-v1-1 seen true | debconf-set-selections && \
    apt-get install -y oracle-java8-installer oracle-java8-set-default

# Install utils
RUN apt-get install -y wget

# Add war file
ADD licensing-mockdata-jar-with-dependencies.jar /opt/

CMD ["java", "-jar", "/opt/licensing-mockdata-jar-with-dependencies.jar"]
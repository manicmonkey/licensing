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

# Deploy jetty
RUN wget -O /tmp/jetty.tar.gz "http://eclipse.org/downloads/download.php?file=/jetty/stable-9/dist/jetty-distribution-9.2.7.v20150116.tar.gz&r=1"
RUN tar zxvf /tmp/jetty.tar.gz -C /opt/
RUN rm /tmp/jetty.tar.gz
RUN ln -s /opt/jetty-distribution-9.2.7.v20150116/ /opt/jetty
 
# Add war file
ADD licensing-ui-vaadin.war /opt/jetty/webapps/

EXPOSE 8080
RUN java -Djetty.base=/opt/jetty -jar /opt/jetty/start.jar --add-to-startd=http,deploy
CMD ["java", "-Djetty.base=/opt/jetty", "-jar", "/opt/jetty/start.jar"]
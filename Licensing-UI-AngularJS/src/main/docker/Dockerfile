FROM ubuntu:trusty
MAINTAINER James Baxter <j.w.baxter+licensing@gmail.com>

# Install nginx
RUN apt-get update && \
	apt-get install -y nginx

# Configure nginx
RUN sed -i -e 's/sendfile on/sendfile off/' /etc/nginx/nginx.conf && \
	echo "\ndaemon off;" >> /etc/nginx/nginx.conf && \
	sed -i -e s#/usr/share/nginx/html#/var/www# /etc/nginx/sites-enabled/default && \
	mkdir /var/www
RUN groupadd -g 999 vboxsf && \
	usermod -aG vboxsf www-data

# Start nginx
CMD ["nginx"]

EXPOSE 80
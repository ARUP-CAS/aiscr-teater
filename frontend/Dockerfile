FROM alpine:3.10
MAINTAINER koszegy@inqool.cz

RUN apk add --update apache2 apache2-proxy openssl \
    && rm -rf /var/cache/apk/* && \
    mkdir -p /run/apache2 && \
    rm -rf /var/www/localhost/htdocs

COPY build /var/www/localhost/htdocs
COPY .htaccess /var/www/localhost/htdocs
COPY httpd.conf /etc/apache2/httpd.conf
COPY run.sh /run.sh

RUN chown -R apache:apache /var/www/localhost/htdocs

ENTRYPOINT ["/run.sh"]

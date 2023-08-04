#!/bin/sh

echo "Reverse proxy for backend: ${API_URL}"
echo "ProxyPass /api ${API_URL}" > /etc/apache2/conf.d/proxyApi.conf
echo "ProxyPassReverse /api ${API_URL}" >> /etc/apache2/conf.d/proxyApi.conf
echo "Open http://localhost/"

httpd -D FOREGROUND

tail -f /etc/issue

FROM alpine:3.10.1

RUN apk add --update \
    openjdk11-jre \
    python3 \
    py3-parsing \
    py3-setuptools \
    py3-markdown \
    py3-psycopg2 \
    py3-unidecode \
    postgresql-dev \
    && pip3 install --upgrade elasticsearch-dsl==6.4.0 \
    && rm -rf /var/cache/apk/*

WORKDIR /usr/src/

COPY json json
COPY import_script.py import_script.py
ADD target/thesaurus-0.0.1-SNAPSHOT.jar api.jar

COPY run.sh /run.sh
RUN chmod +x /run.sh

CMD ["/run.sh"]

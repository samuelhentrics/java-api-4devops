FROM ubuntu:latest

RUN apt-get update \
&& apt-get -y upgrade \
&& apt-get -y install openjdk.11-jdk \
&& apt-get -y install maven

WORKDIR /home/
COPY ./jwt-helper ./utils
COPY ./api ./back
RUN cd /home/utils && mvn clean install && cd /home/back && mvn clean install

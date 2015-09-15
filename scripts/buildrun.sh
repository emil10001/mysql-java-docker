#!/usr/bin/env bash

# Build a fresh jar and copy it into place
../gradle fatJar
cp ../build/libs/mysql-java-docker-all-1.0-SNAPSHOT.jar ./java_docker/mysql-java-docker.jar

MYSQL_PORT_3306_TCP_ADDR=3306
MYSQL_USER=testapp
MYSQL_DATABASE=sample
MYSQL_PASSWORD=rJQaj5B46ofWjZo2o2648izNCGUcui9o
MYSQL_ROOT_PASSWORD=mxr1OEDobHEgy2k4VvfpTlpVq9tClGQB

# This currently doesn't work correctly, need to use docker volume list
# or something, but that doesn't exist yet, for now, it won't try to
# re-create it if it exists anyway, so we're ok. See here:
# https://github.com/docker/docker/pull/14242
DBDATA=`docker images -q sample/sql-java`
if [[ -z $DBDATA ]]
then
    echo "creating new shared volume called 'dbdata'"
    # Reference: https://docs.docker.com/userguide/dockervolumes/#creating-and-mounting-a-data-volume-container
    docker create -v /dbdata --name dbdata mysql:5.7 /bin/true

    docker build -t sample/sql-java:v1 ./sql_server_docker/
fi

docker build -t sample/java-docker:v1 ./java_docker/

# The following is set up to be ephemeral, so that the instances that you
# launch are cleaned up after running. Uses the shared Docker volume, dbdata.
# As a note, we modified the entrypoint script so that it pulls in a sql script
# that runs against our existing database every time we run. While this is fine
# for an example, it shouldn't be done in production, as it might blow something up.
# Reference: https://docs.docker.com/userguide/dockervolumes/#creating-and-mounting-a-data-volume-container
# Run command shown here: https://coreos.com/products/enterprise-registry/docs/latest/mysql-container.html
ID=$(docker run \
    -d -p 3306:3306 \
    --env MYSQL_ROOT_PASSWORD=${MYSQL_PASSWORD} \
    --env MYSQL_USER=${MYSQL_USER} \
    --env MYSQL_PASSWORD=${MYSQL_PASSWORD} \
    --env MYSQL_DATABASE=${MYSQL_DATABASE} \
    --volumes-from dbdata \
    sample/sql-java:v1;)

docker run --rm sample/java-docker:v1

# Kill the ephemeral mysql daemon image
docker stop $ID
docker rm -f $ID

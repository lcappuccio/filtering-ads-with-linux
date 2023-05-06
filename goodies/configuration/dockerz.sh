#!/bin/bash
docker run --detach --name mariadb -p 3306:3306 --net=host --env MARIADB_USER=adtrap --env MARIADB_PASSWORD=adtrap --env MARIADB_ROOT_PASSWORD=root  mariadb:latest
docker run --name phpmyadmin --net=host -d -e PMA_HOST=localhost:3306 -p 8080:80 phpmyadmin
#!/bin/bash

mariadb -u root -pAbeba2021 -e \ "CREATE DATABASE IF NOT EXISTS spring_microservice2;
CREATE USER 'springuser2'@'%' identified by 'springboot';
GRANT all ON spring_microservice2.* to 'springuser2'@'%';
FLUSH PRIVILEGES;"
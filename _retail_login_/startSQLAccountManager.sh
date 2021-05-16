#!/bin/sh
java -Djava.util.logging.config.file=config/console.cfg -cp ./libs/*:l2jserver.jar:mysql-connector-java-5.1.26.jar sf.l2j.accountmanager.SQLAccountManager

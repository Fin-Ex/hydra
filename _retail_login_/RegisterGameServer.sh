#!/bin/sh
java -Djava.util.logging.config.file=config/console.cfg -cp ./libs/*:l2jserver.jar sf.l2j.gsregistering.GameServerRegister

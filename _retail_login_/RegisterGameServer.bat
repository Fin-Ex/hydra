@echo off
title Fin-Ex gameserver registration
@java -Djava.util.logging.config.file=config/console.cfg -cp ../libs/*; sf.l2j.gsregistering.GameServerRegister
@pause
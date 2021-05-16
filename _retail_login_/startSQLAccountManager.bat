@echo off
title Fin-Ex account manager
@java -Djava.util.logging.config.file=config/console.cfg -cp ../libs/*; sf.l2j.accountmanager.SQLAccountManager
@pause

CREATE TABLE IF NOT EXISTS `character_craft` (
  `objectId` INT NOT NULL default 0,
  `lvl` TINYINT NOT NULL default 0,
  `exp` INT NOT NULL default 0,
  `fails` INT NOT NULL default 0,
  `succesess` INT NOT NULL default 0,
  `specialize` TINYINT NOT NULL default 0,
  PRIMARY KEY (`objectId`,`specialize`)
);
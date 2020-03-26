CREATE TABLE IF NOT EXISTS `character_lineage` (
  `objectId` BIGINT(20) DEFAULT 0,
  `classIndex` SMALLINT DEFAULT 0,
  `lineagePoints` SMALLINT DEFAULT 0,
  `levelReach` SMALLINT DEFAULT 0,
  `resetPrice` INT DEFAULT 1,
  PRIMARY KEY (`objectId`,`classIndex`)
);

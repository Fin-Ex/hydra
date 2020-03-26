CREATE TABLE IF NOT EXISTS `items_hxh` (
  `hunterId` INT DEFAULT 0,
  `level` TINYINT DEFAULT 1,
  `exp` SMALLINT DEFAULT 0,
  `huntID` INT DEFAULT 0,
  `count` SMALLINT DEFAULT 0,
  `current` INT DEFAULT 0,
  `credits` INT DEFAULT 0,
  `timestamp` BIGINT DEFAULT 0,
  KEY `hunterId` (`hunterId`)
);
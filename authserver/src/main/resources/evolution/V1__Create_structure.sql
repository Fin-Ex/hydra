CREATE TABLE IF NOT EXISTS `accounts` (
  `login` VARCHAR(45) NOT NULL DEFAULT '',
  `password` VARCHAR(45),
  `lastactive` DECIMAL(20),
  `access_level` INT(3) NOT NULL DEFAULT 0,
  `lastServer` INT(4) DEFAULT 1,
  PRIMARY KEY (`login`)
);

CREATE TABLE IF NOT EXISTS `gameservers` (
  `server_id` int(11) NOT NULL default '0',
  `hexid` varchar(50) NOT NULL default '',
  `host` varchar(50) NOT NULL default '',
  PRIMARY KEY (`server_id`)
);
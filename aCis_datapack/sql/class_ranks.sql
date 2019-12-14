CREATE TABLE IF NOT EXISTS `class_ranks` (
  objectId int(11) NOT NULL default '0',
  classId int(11) NOT NULL default '0',
  raceId varchar(20) NOT NULL default '',
  sexId varchar(20) NOT NULL default '',
  points int(11) NOT NULL default '0',
  PRIMARY KEY (`objectId`, `classId`, `raceId`, `sexId`)
);
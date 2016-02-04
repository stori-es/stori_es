DROP TABLE IF EXISTS `autotag`;
CREATE TABLE `autotag` (
  `systemEntity` INT(11)     NOT NULL,
  `value`        VARCHAR(45) NOT NULL,
  PRIMARY KEY (`systemEntity`, `value`),
  KEY `fk_systemEntity` (`systemEntity`),
  CONSTRAINT `fk_autotag_entity` FOREIGN KEY (`systemEntity`) REFERENCES `systemEntity` (`id`)
)
  ENGINE = InnoDB;

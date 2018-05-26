CREATE database `sports_alarm`;

CREATE TABLE `league` (
  `id` varchar(36) NOT NULL,
  `parse_from_date` int(11) DEFAULT NULL,
  `parse_to_date` int(11) DEFAULT NULL,
  `sequence` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `team` (
  `id` varchar(36) NOT NULL,
  `league_id` varchar(45) NOT NULL,
  `identifier` varchar(45) NOT NULL,
  `city` varchar(45) DEFAULT NULL,
  `mascot` varchar(45) DEFAULT NULL,
  `primary_color` varchar(45) DEFAULT NULL,
  `secondary_color` varchar(45) DEFAULT NULL,
  `is_new` tinyint(1) DEFAULT NULL,
  `origin_city` varchar(45) DEFAULT NULL,
  `origin_mascot` varchar(45) DEFAULT NULL,
  `venue_id` varchar(45) DEFAULT NULL,
  `image` varchar(45) DEFAULT NULL,
  `deleted` tinyint(1) NOT NULL DEFAULT 0,
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `game` (
  `id` varchar(36) NOT NULL,
  `home_team_id` varchar(36) NOT NULL,
  `away_team_id` varchar(36) NOT NULL,
  `date_time` bigint(20) DEFAULT NULL,
  `identifier` varchar(36) DEFAULT NULL,
  `league_id` varchar(36) DEFAULT NULL,
  `deleted` tinyint(1) NOT NULL DEFAULT 0,
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `data_report` (
  `id` varchar(36) NOT NULL,
  `league_id` varchar(36) NOT NULL,
  `date` datetime NOT NULL,
  `update_count` int(11) DEFAULT NULL,
  `insert_count` int(11) DEFAULT NULL,
  `total_count` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TRIGGER `sports_alarm`.`team_BEFORE_UPDATE` BEFORE UPDATE ON `team` FOR EACH ROW
BEGIN
	SET NEW.modified = CURRENT_TIMESTAMP;
END;

CREATE TRIGGER `sports_alarm`.`game_BEFORE_UPDATE` BEFORE UPDATE ON `game` FOR EACH ROW
BEGIN
	SET NEW.modified = CURRENT_TIMESTAMP;
END;



-- FOREIGN KEYS
ALTER TABLE `sports_alarm`.`team`
ADD INDEX `fk_team_league_id_idx` (`league_id` ASC);
ALTER TABLE `sports_alarm`.`team`
ADD CONSTRAINT `fk_team_league_id`
  FOREIGN KEY (`league_id`)
  REFERENCES `sports_alarm`.`league` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

ALTER TABLE `sports_alarm`.`game`
ADD INDEX `fk_game_home_team_idx` (`home_team_id` ASC),
ADD INDEX `fk_game_away_team_idx` (`away_team_id` ASC);
ALTER TABLE `sports_alarm`.`game`
ADD CONSTRAINT `fk_game_home_team`
  FOREIGN KEY (`home_team_id`)
  REFERENCES `sports_alarm`.`team` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION,
ADD CONSTRAINT `fk_game_away_team`
  FOREIGN KEY (`away_team_id`)
  REFERENCES `sports_alarm`.`team` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

ALTER TABLE `sports_alarm`.`game`
ADD INDEX `fk_game_league_idx` (`league_id` ASC);
ALTER TABLE `sports_alarm`.`game`
ADD CONSTRAINT `fk_game_league`
  FOREIGN KEY (`league_id`)
  REFERENCES `sports_alarm`.`league` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

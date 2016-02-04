-- MySQL dump 10.13  Distrib 5.1.61, for redhat-linux-gnu (x86_64)
--
-- Host: localhost    Database: stories
-- ------------------------------------------------------
-- Server version	5.1.61

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `accessKey`
--

DROP TABLE IF EXISTS `accessKey`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `accessKey` (
  `accessKey` varchar(64) DEFAULT NULL,
  `value` varchar(1024) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `acl_class`
--

DROP TABLE IF EXISTS `acl_class`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `acl_class` (
  `id` int(11) NOT NULL DEFAULT '0',
  `class` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_class` (`class`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `acl_entry`
--

DROP TABLE IF EXISTS `acl_entry`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `acl_entry` (
  `acl_object_identity` int(11) NOT NULL,
  `sid` int(11) NOT NULL,
  `mask` int(11) NOT NULL,
  PRIMARY KEY (`sid`,`acl_object_identity`),
  KEY `fk_acl_entry_acl_object_identity` (`acl_object_identity`),
  KEY `fk_acl_entry_acl_sid` (`sid`),
  CONSTRAINT `fk_acl_entry_acl_object_identity` FOREIGN KEY (`acl_object_identity`) REFERENCES `acl_object_identity` (`id`),
  CONSTRAINT `fk_acl_entry_acl_sid` FOREIGN KEY (`sid`) REFERENCES `acl_sid` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `acl_object_identity`
--

DROP TABLE IF EXISTS `acl_object_identity`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `acl_object_identity` (
  `id` int(11) NOT NULL,
  `object_id_class` int(11) NOT NULL,
  `object_id_identity` int(11) NOT NULL,
  `parent_object` int(11) DEFAULT NULL,
  `owner_sid` int(11) DEFAULT NULL,
  `entries_inheriting` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_acl_object_identity_acl_object_identity` (`parent_object`),
  KEY `fk_acl_object_identity_acl_sid` (`owner_sid`),
  KEY `fk_acl_object_acl_class` (`object_id_class`),
  CONSTRAINT `fk_acl_object_acl_class` FOREIGN KEY (`object_id_class`) REFERENCES `acl_class` (`id`),
  CONSTRAINT `fk_acl_object_identity_acl_object_identity` FOREIGN KEY (`parent_object`) REFERENCES `acl_object_identity` (`id`),
  CONSTRAINT `fk_acl_object_identity_acl_sid` FOREIGN KEY (`owner_sid`) REFERENCES `acl_sid` (`id`),
  CONSTRAINT `fk_acl_object_system_entity` FOREIGN KEY (`id`) REFERENCES `systemEntity` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `acl_sid`
--

DROP TABLE IF EXISTS `acl_sid`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `acl_sid` (
  `id` int(11) NOT NULL DEFAULT '0',
  `principal` tinyint(1) NOT NULL,
  `sid` varchar(100) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_sid` (`sid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `activity`
--

DROP TABLE IF EXISTS `activity`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `activity` (
  `id` int(11) NOT NULL,
  `subject` int(11) DEFAULT NULL,
  `verb` varchar(32) DEFAULT NULL,
  `object` int(11) DEFAULT NULL,
  `postActionVersion` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `address`
--

DROP TABLE IF EXISTS `address`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `address` (
  `entity` int(11) NOT NULL,
  `relation` varchar(64) NOT NULL,
  `address1` varchar(255) DEFAULT NULL,
  `address2` varchar(255) DEFAULT NULL,
  `city` varchar(128) DEFAULT NULL,
  `state` char(3) DEFAULT NULL,
  `country` char(2) DEFAULT NULL,
  `postalCode` varchar(64) DEFAULT NULL,
  `latitude` decimal(10,7) DEFAULT NULL,
  `longitude` decimal(10,7) DEFAULT NULL,
  `idx` int(11) NOT NULL DEFAULT '0',
  `geoCodeStatus` varchar(16) DEFAULT NULL,
  `geoCodeProvider` varchar(16) DEFAULT NULL,
  `geoCodeDate` datetime DEFAULT NULL,
  PRIMARY KEY (`entity`,`relation`,`idx`),
  UNIQUE KEY `unique_address_index` (`entity`,`idx`),
  CONSTRAINT `fk_address_entity1` FOREIGN KEY (`entity`) REFERENCES `systemEntity` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `answer`
--

DROP TABLE IF EXISTS `answer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `answer` (
  `displayValue` varchar(255) NOT NULL,
  `reportValue` text,
  `label` varchar(32) NOT NULL DEFAULT '',
  `answerSet` int(11) NOT NULL,
  `idx` int(11) NOT NULL,
  PRIMARY KEY (`answerSet`,`label`,`idx`),
  KEY `fk_answer_answerSet` (`answerSet`),
  CONSTRAINT `fk_answer_answerSet` FOREIGN KEY (`answerSet`) REFERENCES `answerSet` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `answerSet`
--

DROP TABLE IF EXISTS `answerSet`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `answerSet` (
  `id` int(11) NOT NULL,
  `questionnaire` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_answerSet_questionnaire` (`questionnaire`),
  CONSTRAINT `answerSet_ibfk_1` FOREIGN KEY (`id`) REFERENCES `document` (`id`),
  CONSTRAINT `fk_answerSet_questionnaire` FOREIGN KEY (`questionnaire`) REFERENCES `questionnaire` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `apiTier`
--

DROP TABLE IF EXISTS `apiTier`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `apiTier` (
  `id` int(11) NOT NULL,
  `name` varchar(128) NOT NULL,
  `description` varchar(256) NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_apitier_systementity` FOREIGN KEY (`id`) REFERENCES `systemEntity` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `apiUsageLimit`
--

DROP TABLE IF EXISTS `apiUsageLimit`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `apiUsageLimit` (
  `appTier` int(11) NOT NULL,
  `periodTime` int(11) NOT NULL,
  `limitInPeriod` int(11) NOT NULL,
  `apiClass` varchar(64) NOT NULL,
  PRIMARY KEY (`appTier`),
  CONSTRAINT `fk_apiUsageLimit_apiTier1` FOREIGN KEY (`appTier`) REFERENCES `apiTier` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `api_keys`
--

DROP TABLE IF EXISTS `api_keys`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `api_keys` (
  `user` int(11) NOT NULL DEFAULT '0',
  `uuid` varchar(32) NOT NULL DEFAULT '',
  `uuid_bin` binary(16) DEFAULT NULL,
  PRIMARY KEY (`user`,`uuid`),
  UNIQUE KEY `api_hex_index` (`uuid_bin`),
  CONSTRAINT `fk_api_keys_user` FOREIGN KEY (`user`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `autotag`
--

DROP TABLE IF EXISTS `autotag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `autotag` (
  `systemEntity` int(11) NOT NULL,
  `value` varchar(45) NOT NULL,
  PRIMARY KEY (`systemEntity`,`value`),
  KEY `fk_systemEntity` (`systemEntity`),
  CONSTRAINT `fk_autotag_entity` FOREIGN KEY (`systemEntity`) REFERENCES `systemEntity` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `block`
--

DROP TABLE IF EXISTS `block`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `block` (
  `document` int(11) NOT NULL DEFAULT '0',
  `idx` int(11) NOT NULL,
  `documentType` varchar(16) NOT NULL,
  `standardMeaning` varchar(32) DEFAULT NULL,
  `version` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`document`,`version`,`idx`),
  CONSTRAINT `fk_block_document` FOREIGN KEY (`document`, `version`) REFERENCES `document` (`id`, `version`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `block_content`
--

DROP TABLE IF EXISTS `block_content`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `block_content` (
  `document` int(11) NOT NULL DEFAULT '0',
  `idx` int(11) NOT NULL,
  `content` text,
  `version` int(11) NOT NULL DEFAULT '0',
  `textType` varchar(16) DEFAULT NULL,
  PRIMARY KEY (`document`,`version`,`idx`),
  CONSTRAINT `fk_block_content_block` FOREIGN KEY (`document`, `version`, `idx`) REFERENCES `block` (`document`, `version`, `idx`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `block_image`
--

DROP TABLE IF EXISTS `block_image`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `block_image` (
  `document` int(11) NOT NULL DEFAULT '0',
  `idx` int(11) NOT NULL,
  `url` varchar(250) DEFAULT NULL,
  `caption` text,
  `alttext` varchar(250) DEFAULT NULL,
  `position` varchar(25) DEFAULT NULL,
  `size` varchar(25) DEFAULT NULL,
  `version` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`document`,`version`,`idx`),
  CONSTRAINT `fk_block_image_block` FOREIGN KEY (`document`, `version`, `idx`) REFERENCES `block` (`document`, `version`, `idx`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `block_link`
--

DROP TABLE IF EXISTS `block_link`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `block_link` (
  `document` int(11) NOT NULL DEFAULT '0',
  `idx` int(11) NOT NULL,
  `title` varchar(250) DEFAULT NULL,
  `url` varchar(250) DEFAULT NULL,
  `version` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`document`,`version`,`idx`),
  CONSTRAINT `fk_block_link_block` FOREIGN KEY (`document`, `version`, `idx`) REFERENCES `block` (`document`, `version`, `idx`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `block_question`
--

DROP TABLE IF EXISTS `block_question`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `block_question` (
  `document` int(11) NOT NULL DEFAULT '0',
  `idx` int(11) NOT NULL,
  `dataType` varchar(32) DEFAULT NULL,
  `label` varchar(32) DEFAULT NULL COMMENT '''name'' is displayed in certain administrative contexts where labels may be long and cumbersom.',
  `required` tinyint(1) DEFAULT NULL,
  `minLength` int(11) DEFAULT NULL,
  `maxLength` int(11) DEFAULT NULL,
  `multiselect` tinyint(1) DEFAULT NULL,
  `startDate` varchar(20) DEFAULT NULL,
  `text` varchar(255) DEFAULT NULL,
  `helpText` varchar(512) DEFAULT NULL,
  `version` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`document`,`version`,`idx`),
  UNIQUE KEY `uniq_questionnaire_label` (`document`,`version`,`label`),
  CONSTRAINT `fk_block_question_block` FOREIGN KEY (`document`, `version`, `idx`) REFERENCES `block` (`document`, `version`, `idx`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `block_rating`
--

DROP TABLE IF EXISTS `block_rating`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `block_rating` (
  `document` int(11) NOT NULL DEFAULT '0',
  `idx` int(11) NOT NULL,
  `start` varchar(25) DEFAULT NULL,
  `end` varchar(25) DEFAULT NULL,
  `steps` varchar(16) DEFAULT NULL,
  `version` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`document`,`version`,`idx`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `block_submit`
--

DROP TABLE IF EXISTS `block_submit`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `block_submit` (
  `document` int(11) NOT NULL DEFAULT '0',
  `idx` int(11) NOT NULL,
  `prompt` varchar(250) DEFAULT NULL,
  `size` varchar(16) DEFAULT NULL,
  `position` varchar(16) DEFAULT NULL,
  `nextDocument` int(11) DEFAULT NULL,
  `version` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`document`,`version`,`idx`),
  KEY `fk_submitBlock_document` (`nextDocument`),
  CONSTRAINT `fk_block_submit_block` FOREIGN KEY (`document`, `version`, `idx`) REFERENCES `block` (`document`, `version`, `idx`),
  CONSTRAINT `fk_submitBlock_document` FOREIGN KEY (`nextDocument`) REFERENCES `document` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `collection`
--

DROP TABLE IF EXISTS `collection`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `collection` (
  `id` int(11) NOT NULL,
  `deleted` tinyint(1) DEFAULT '0',
  `published` tinyint(1) NOT NULL DEFAULT '0',
  `publishedDate` timestamp NULL DEFAULT NULL,
  `previewKey` varchar(8) DEFAULT NULL,
  `theme` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_collection_entity` (`id`),
  CONSTRAINT `collection_ibfk_1` FOREIGN KEY (`id`) REFERENCES `entity` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `collectionInvitation`
--

DROP TABLE IF EXISTS `collectionInvitation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `collectionInvitation` (
  `collection` int(11) NOT NULL AUTO_INCREMENT,
  `email` varchar(255) NOT NULL,
  `accessKey` varchar(16) DEFAULT NULL,
  PRIMARY KEY (`collection`),
  CONSTRAINT `fk_collectionInvitation_collection1` FOREIGN KEY (`collection`) REFERENCES `collection` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `collectionPreferredMedia`
--

DROP TABLE IF EXISTS `collectionPreferredMedia`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `collectionPreferredMedia` (
  `collection` int(11) NOT NULL,
  `idx` int(11) NOT NULL,
  PRIMARY KEY (`collection`),
  CONSTRAINT `fk_collectionPreferredMedia_collection1` FOREIGN KEY (`collection`) REFERENCES `collection` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `collection_sources`
--

DROP TABLE IF EXISTS `collection_sources`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `collection_sources` (
  `sourceQuestionnaire` int(11) NOT NULL DEFAULT '0',
  `targetCollection` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`sourceQuestionnaire`,`targetCollection`),
  KEY `fk_collection_source_collection` (`targetCollection`),
  CONSTRAINT `fk_collection_sources_questionnaire` FOREIGN KEY (`sourceQuestionnaire`) REFERENCES `questionnaire` (`id`),
  CONSTRAINT `fk_collection_source_collection` FOREIGN KEY (`targetCollection`) REFERENCES `collection` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `collection_story`
--

DROP TABLE IF EXISTS `collection_story`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `collection_story` (
  `collection` int(11) NOT NULL,
  `story` int(11) NOT NULL,
  `clearedForPublicInclusion` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`collection`,`story`),
  KEY `fk_collection_story_collection1` (`collection`),
  KEY `fk_collection_story_story1` (`story`),
  CONSTRAINT `fk_collection_story_collection1` FOREIGN KEY (`collection`) REFERENCES `collection` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_collection_story_story1` FOREIGN KEY (`story`) REFERENCES `story` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `complaint`
--

DROP TABLE IF EXISTS `complaint`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `complaint` (
  `id` int(11) NOT NULL,
  `document` int(11) NOT NULL,
  `complainant` int(11) DEFAULT NULL,
  `complainantEmail` varchar(255) NOT NULL,
  `description` varchar(1024) NOT NULL,
  `problemPosition` varchar(64) DEFAULT NULL COMMENT 'The ''problemPosition'' may be a time code or other information appropriate to the media type.\n',
  `status` varchar(16) NOT NULL,
  `collection` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_complaint_document1` (`document`),
  KEY `fk_complaint_user1` (`complainant`),
  KEY `fk_complaint_collection1` (`collection`),
  CONSTRAINT `fk_complaint_collection1` FOREIGN KEY (`collection`) REFERENCES `collection` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_complaint_document1` FOREIGN KEY (`document`) REFERENCES `document` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_complaint_systementity` FOREIGN KEY (`id`) REFERENCES `systemEntity` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_complaint_user1` FOREIGN KEY (`complainant`) REFERENCES `user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `contact`
--

DROP TABLE IF EXISTS `contact`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `contact` (
  `entityId` int(11) NOT NULL,
  `medium` varchar(32) NOT NULL,
  `type` varchar(32) NOT NULL,
  `value` varchar(512) NOT NULL,
  `idx` int(11) NOT NULL,
  `status` varchar(16) NOT NULL DEFAULT 'UNVERIFIED',
  PRIMARY KEY (`entityId`,`medium`,`type`,`value`),
  UNIQUE KEY `contact_unique_entity_idx` (`entityId`,`idx`),
  KEY `fk_contact_entity` (`entityId`),
  CONSTRAINT `fk_contact_entity` FOREIGN KEY (`entityId`) REFERENCES `entity` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `dbUpdate`
--

DROP TABLE IF EXISTS `dbUpdate`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dbUpdate` (
  `scriptName` varchar(255) NOT NULL DEFAULT '',
  PRIMARY KEY (`scriptName`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `document`
--

DROP TABLE IF EXISTS `document`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `document` (
  `id` int(11) NOT NULL,
  `primaryAuthor` int(11) NOT NULL,
  `permalink` varchar(255) NOT NULL,
  `systemEntity` int(11) NOT NULL,
  `systemEntityRelation` varchar(32) NOT NULL,
  `locale` varchar(8) NOT NULL,
  `version` int(11) NOT NULL,
  `title` varchar(255) DEFAULT NULL,
  `summary` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`id`,`version`),
  KEY `fk_document_user1` (`primaryAuthor`),
  KEY `fk_document_entity` (`id`),
  KEY `fk_document_related_entity` (`systemEntity`),
  CONSTRAINT `document_ibfk_1` FOREIGN KEY (`systemEntity`) REFERENCES `systemEntity` (`id`),
  CONSTRAINT `fk_document_primaryAuthor_people` FOREIGN KEY (`primaryAuthor`) REFERENCES `profile` (`id`),
  CONSTRAINT `fk_document_systementity` FOREIGN KEY (`id`) REFERENCES `systemEntity` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `documentContributor`
--

DROP TABLE IF EXISTS `documentContributor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `documentContributor` (
  `document` int(11) NOT NULL,
  `user` int(11) NOT NULL,
  `role` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`document`,`user`),
  KEY `fk_documentContributor_user1` (`user`),
  CONSTRAINT `fk_documentContributer_profile` FOREIGN KEY (`user`) REFERENCES `profile` (`id`),
  CONSTRAINT `fk_documentContributor_document1` FOREIGN KEY (`document`) REFERENCES `document` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `document_entities`
--

DROP TABLE IF EXISTS `document_entities`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `document_entities` (
  `document` int(11) NOT NULL,
  `entity` int(11) NOT NULL,
  PRIMARY KEY (`document`,`entity`),
  KEY `fk_document_entities_document1` (`document`),
  KEY `fk_document_entities_entity1` (`entity`),
  CONSTRAINT `fk_document_entities_document1` FOREIGN KEY (`document`) REFERENCES `document` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_document_entities_entity1` FOREIGN KEY (`entity`) REFERENCES `entity` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `entity`
--

DROP TABLE IF EXISTS `entity`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `entity` (
  `id` int(11) NOT NULL,
  `profile` int(11) DEFAULT NULL,
  `permalink` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_entity_permalink` (`permalink`),
  KEY `fk_entity_profile` (`profile`),
  CONSTRAINT `fk_entity_profile` FOREIGN KEY (`profile`) REFERENCES `document` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_entity_systemEntity1` FOREIGN KEY (`id`) REFERENCES `systemEntity` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `favorite`
--

DROP TABLE IF EXISTS `favorite`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `favorite` (
  `user` int(11) NOT NULL,
  `story` int(11) NOT NULL,
  PRIMARY KEY (`user`,`story`),
  KEY `fk_favorite_story` (`story`),
  CONSTRAINT `fk_favorite_story` FOREIGN KEY (`story`) REFERENCES `story` (`id`),
  CONSTRAINT `fk_favorite_user` FOREIGN KEY (`user`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `importRecord`
--

DROP TABLE IF EXISTS `importRecord`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `importRecord` (
  `sourceId` int(11) DEFAULT NULL,
  `sourceTable` varchar(64) DEFAULT NULL,
  `targetId` int(11) DEFAULT NULL,
  `targetTable` varchar(64) DEFAULT NULL,
  `toDelete` tinyint(1) DEFAULT NULL,
  KEY `importRecord_fk_targetId` (`targetId`),
  CONSTRAINT `importRecord_fk_targetId` FOREIGN KEY (`targetId`) REFERENCES `systemEntity` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `operation`
--

DROP TABLE IF EXISTS `operation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `operation` (
  `name` varchar(64) NOT NULL,
  `description` varchar(255) NOT NULL,
  PRIMARY KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `organization`
--

DROP TABLE IF EXISTS `organization`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `organization` (
  `id` int(11) NOT NULL,
  `name` varchar(128) DEFAULT NULL,
  `shortName` varchar(15) DEFAULT NULL,
  `defaultTheme` int(11) DEFAULT NULL,
  `crm_api_login` varchar(1000) DEFAULT NULL,
  `crm_api_key` varchar(1000) DEFAULT NULL,
  `crm_endpoint` varchar(1000) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_organization_theme` (`defaultTheme`),
  CONSTRAINT `fk_organization_entity` FOREIGN KEY (`id`) REFERENCES `entity` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_organization_theme` FOREIGN KEY (`defaultTheme`) REFERENCES `theme` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `organization_theme`
--

DROP TABLE IF EXISTS `organization_theme`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `organization_theme` (
  `organization` int(11) NOT NULL,
  `theme` int(11) NOT NULL,
  PRIMARY KEY (`organization`,`theme`),
  KEY `theme` (`theme`),
  CONSTRAINT `organization_theme_ibfk_1` FOREIGN KEY (`organization`) REFERENCES `organization` (`id`),
  CONSTRAINT `organization_theme_ibfk_2` FOREIGN KEY (`theme`) REFERENCES `theme` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `personConvioSyncStatus`
--

DROP TABLE IF EXISTS `personConvioSyncStatus`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `personConvioSyncStatus` (
  `personId` int(11) DEFAULT NULL,
  `orgId` int(11) DEFAULT NULL,
  `cons_id` int(11) DEFAULT NULL,
  `syncStatus` varchar(16) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `personEditWindow`
--

DROP TABLE IF EXISTS `personEditWindow`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `personEditWindow` (
  `personId` int(11) DEFAULT NULL,
  `userId` int(11) DEFAULT NULL,
  `editWindow` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `profile`
--

DROP TABLE IF EXISTS `profile`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `profile` (
  `id` int(11) NOT NULL,
  `surname` varchar(255) DEFAULT NULL,
  `givenName` varchar(255) DEFAULT NULL,
  `convioSyncStatus` varchar(36) DEFAULT NULL,
  `user` int(11) DEFAULT NULL,
  `organization` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniq_profile_user_org` (`user`,`organization`),
  KEY `fk_profile_org` (`organization`),
  CONSTRAINT `fk_person_entity1` FOREIGN KEY (`id`) REFERENCES `entity` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_profile_org` FOREIGN KEY (`organization`) REFERENCES `organization` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_profile_user` FOREIGN KEY (`user`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `question_contact`
--

DROP TABLE IF EXISTS `question_contact`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `question_contact` (
  `document` int(11) NOT NULL DEFAULT '0',
  `idx` int(11) NOT NULL,
  `type` varchar(250) NOT NULL DEFAULT '',
  `opt` varchar(250) NOT NULL DEFAULT '',
  `text` varchar(250) DEFAULT NULL,
  `helpText` varchar(500) DEFAULT NULL,
  `title` varchar(250) DEFAULT NULL,
  `required` tinyint(1) DEFAULT '0',
  `version` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`document`,`version`,`idx`,`type`,`opt`),
  CONSTRAINT `fk_question_contact_question` FOREIGN KEY (`document`, `version`, `idx`) REFERENCES `block_question` (`document`, `version`, `idx`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `question_options`
--

DROP TABLE IF EXISTS `question_options`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `question_options` (
  `document` int(11) NOT NULL DEFAULT '0',
  `questionIdx` int(11) NOT NULL,
  `idx` int(11) NOT NULL,
  `reportValue` varchar(255) NOT NULL,
  `displayValue` varchar(255) DEFAULT NULL,
  `version` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`document`,`version`,`questionIdx`,`idx`),
  CONSTRAINT `fk_question_options_question` FOREIGN KEY (`document`, `version`, `questionIdx`) REFERENCES `block_question` (`document`, `version`, `idx`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `questionnaire`
--

DROP TABLE IF EXISTS `questionnaire`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `questionnaire` (
  `id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_questionarie_systemEntity` FOREIGN KEY (`id`) REFERENCES `systemEntity` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `roles`
--

DROP TABLE IF EXISTS `roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `roles` (
  `grantor` int(11) NOT NULL,
  `role` varchar(16) NOT NULL,
  `grantee` int(11) NOT NULL,
  PRIMARY KEY (`grantor`,`grantee`),
  CONSTRAINT fk_roles_grantor_entity FOREIGN KEY (grantor) REFERENCES entity (id),
  CONSTRAINT fk_roles_grantee_entity FOREIGN KEY (grantee) REFERENCES entity (id)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `story`
--

DROP TABLE IF EXISTS `story`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `story` (
  `id` int(11) NOT NULL,
  `owner` int(11) NOT NULL,
  `permalink` varchar(255) NOT NULL,
  `defaultContent` int(11) DEFAULT NULL,
  `published` tinyint(1) NOT NULL DEFAULT '0',
  `firstPublished` datetime DEFAULT NULL,
  `byline` varchar(512) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_story_document1` (`defaultContent`),
  KEY `fk_story_entity` (`id`),
  KEY `fk_story_person` (`owner`),
  CONSTRAINT `fk_story_document1` FOREIGN KEY (`defaultContent`) REFERENCES `document` (`id`),
  CONSTRAINT `fk_story_entity` FOREIGN KEY (`id`) REFERENCES `systemEntity` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_story_person` FOREIGN KEY (`owner`) REFERENCES `profile` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `story_entity`
--

DROP TABLE IF EXISTS `story_entity`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `story_entity` (
  `story` int(11) NOT NULL,
  `entity` int(11) NOT NULL,
  PRIMARY KEY (`story`,`entity`),
  KEY `fk_story_entity_story1` (`story`),
  KEY `fk_story_entity_entity1` (`entity`),
  CONSTRAINT `fk_story_entity_entity1` FOREIGN KEY (`entity`) REFERENCES `entity` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_story_entity_story1` FOREIGN KEY (`story`) REFERENCES `story` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `subscription`
--

DROP TABLE IF EXISTS `subscription`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `subscription` (
  `profile` int(11) NOT NULL DEFAULT '0',
  `target` int(11) NOT NULL DEFAULT '0',
  `type` varchar(32) NOT NULL DEFAULT '',
  `active` tinyint(1) DEFAULT '1',
  PRIMARY KEY (`profile`,`target`,`type`),
  KEY `fk_subscriptions_target` (`target`),
  CONSTRAINT `fk_subscriptions_profile` FOREIGN KEY (`profile`) REFERENCES `profile` (`id`),
  CONSTRAINT `fk_subscriptions_target` FOREIGN KEY (`target`) REFERENCES `entity` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `systemEntity`
--

DROP TABLE IF EXISTS `systemEntity`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `systemEntity` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastModified` timestamp NULL DEFAULT NULL,
  `version` int(11) NOT NULL,
  `public` tinyint(1) NOT NULL DEFAULT '0',
  `owner` int(11) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `systemEntity_owner_index` (`owner`)
) ENGINE=InnoDB AUTO_INCREMENT=779607 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tag`
--

DROP TABLE IF EXISTS `tag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tag` (
  `systemEntity` int(11) NOT NULL,
  `value` varchar(45) NOT NULL,
  PRIMARY KEY (`systemEntity`,`value`),
  CONSTRAINT `fk_tag_systemEntity1` FOREIGN KEY (`systemEntity`) REFERENCES `systemEntity` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `theme`
--

DROP TABLE IF EXISTS `theme`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `theme` (
  `id` int(11) NOT NULL,
  `name` varchar(256) NOT NULL,
  `themePage` varchar(256) NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_theme_systementity` FOREIGN KEY (`id`) REFERENCES `systemEntity` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `id` int(11) NOT NULL,
  `handle` varchar(64) NOT NULL,
  `active` bit(1) DEFAULT NULL,
  `localPassword` varchar(255) DEFAULT NULL,
  `resetQuestion` varchar(255) DEFAULT NULL,
  `resetAnswer` varchar(255) DEFAULT NULL,
  `apiTier` int(11) DEFAULT NULL,
  `handleLowerCase` varchar(64) DEFAULT NULL,
  `defaultProfile` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `handle` (`handle`),
  UNIQUE KEY `unique_handle_lower_case` (`handleLowerCase`),
  KEY `fk_user_handle` (`handle`),
  KEY `fk_user_profile` (`defaultProfile`),
  CONSTRAINT `fk_user_entity` FOREIGN KEY (`id`) REFERENCES `entity` (`id`),
  CONSTRAINT `fk_user_profile` FOREIGN KEY (`defaultProfile`) REFERENCES `profile` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `verification_nonce`
--

DROP TABLE IF EXISTS `verification_nonce`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `verification_nonce` (
  `profile` int(11) NOT NULL DEFAULT '0',
  `email` varchar(255) NOT NULL DEFAULT '',
  `nonce` varchar(32) DEFAULT NULL,
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`profile`,`email`),
  CONSTRAINT `fk_verification_profile` FOREIGN KEY (`profile`) REFERENCES `profile` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `version`
--

DROP TABLE IF EXISTS `version`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `version` (
  `shaVersion` varchar(64) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `white_list`
--

DROP TABLE IF EXISTS `white_list`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `white_list` (
  `organization` int(11) NOT NULL,
  `default_role` varchar(16) NOT NULL,
  `max_role` varchar(16) NOT NULL,
  `member` int(11) NOT NULL,
  PRIMARY KEY (`organization`,`member`),
  CONSTRAINT fk_white_list_organization FOREIGN KEY (organization) REFERENCES organization (id),
  CONSTRAINT fk_white_list_member_entity FOREIGN KEY (member) REFERENCES entity (id)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2015-04-13 20:39:07

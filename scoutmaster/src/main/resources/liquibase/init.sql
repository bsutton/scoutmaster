-- liquibase formatted sql
-- changeset bsutton:1 attribute1:value1 attribute2:value2 [...]

-- MySQL dump 10.13  Distrib 5.5.31, for debian-linux-gnu (x86_64)
--
-- Host: localhost    Database: scoutmastertest
-- ------------------------------------------------------
-- Server version	5.5.31-0ubuntu0.13.04.1

SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT ;
SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS ;
SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION ;
SET NAMES utf8 ;
SET @OLD_TIME_ZONE=@@TIME_ZONE ;
SET TIME_ZONE='+00:00' ;
SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 ;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 ;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO'; 
SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 ;

--
-- Table structure for table `activity`
--

DROP TABLE IF EXISTS `activity`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `activity` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `ACTIVITYDATE` date DEFAULT NULL,
  `CONSISTENCYVERSION` bigint(20) NOT NULL,
  `CREATED` date DEFAULT NULL,
  `DETAILS` varchar(255) DEFAULT NULL,
  `SUBJECT` varchar(255) DEFAULT NULL,
  `UPDATED` date DEFAULT NULL,
  `ADDEDBY_ID` bigint(20) DEFAULT NULL,
  `TYPE_ID` bigint(20) DEFAULT NULL,
  `WITHCONTACT_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_ACTIVITY_WITHCONTACT_ID` (`WITHCONTACT_ID`),
  KEY `FK_ACTIVITY_ADDEDBY_ID` (`ADDEDBY_ID`),
  KEY `FK_ACTIVITY_TYPE_ID` (`TYPE_ID`),
  CONSTRAINT `FK_ACTIVITY_TYPE_ID` FOREIGN KEY (`TYPE_ID`) REFERENCES `activitytype` (`ID`),
  CONSTRAINT `FK_ACTIVITY_ADDEDBY_ID` FOREIGN KEY (`ADDEDBY_ID`) REFERENCES `contact` (`ID`),
  CONSTRAINT `FK_ACTIVITY_WITHCONTACT_ID` FOREIGN KEY (`WITHCONTACT_ID`) REFERENCES `contact` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `activity`
--

LOCK TABLES `activity` WRITE;
/*!40000 ALTER TABLE `activity` DISABLE KEYS */;
/*!40000 ALTER TABLE `activity` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `activitytype`
--

DROP TABLE IF EXISTS `activitytype`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `activitytype` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `CONSISTENCYVERSION` bigint(20) NOT NULL,
  `CREATED` date DEFAULT NULL,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  `NAME` varchar(255) DEFAULT NULL,
  `UPDATED` date DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `activitytype`
--

LOCK TABLES `activitytype` WRITE;
/*!40000 ALTER TABLE `activitytype` DISABLE KEYS */;
/*!40000 ALTER TABLE `activitytype` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `address`
--

DROP TABLE IF EXISTS `address`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `address` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `CITY` varchar(255) DEFAULT NULL,
  `CONSISTENCYVERSION` bigint(20) NOT NULL,
  `CREATED` date DEFAULT NULL,
  `POSTCODE` varchar(255) DEFAULT NULL,
  `STATE` varchar(255) DEFAULT NULL,
  `STREET` varchar(255) DEFAULT NULL,
  `UPDATED` date DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `address`
--

LOCK TABLES `address` WRITE;
/*!40000 ALTER TABLE `address` DISABLE KEYS */;
/*!40000 ALTER TABLE `address` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `contact`
--

DROP TABLE IF EXISTS `contact`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `contact` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `ACTIVE` tinyint(1) DEFAULT '0',
  `AFFILIATEDSINCE` date DEFAULT NULL,
  `ALLERGIES` varchar(255) DEFAULT NULL,
  `AMBULANCESUBSCRIBER` tinyint(1) DEFAULT '0',
  `BIRTHDATE` date DEFAULT NULL,
  `CONSISTENCYVERSION` bigint(20) NOT NULL,
  `CREATED` date DEFAULT NULL,
  `CURRENTEMPLOYER` varchar(255) DEFAULT NULL,
  `CUSTODYORDER` tinyint(1) DEFAULT '0',
  `CUSTODYORDERDETAILS` varchar(255) DEFAULT NULL,
  `FIRSTNAME` varchar(255) DEFAULT NULL,
  `GENDER` int(11) DEFAULT NULL,
  `HASFIRSTAIDCERTIFICATE` tinyint(1) DEFAULT '0',
  `HASFOODHANDLINGCERTIFICATE` tinyint(1) DEFAULT '0',
  `HASPOLICECHECK` tinyint(1) DEFAULT '0',
  `HASWWC` tinyint(1) DEFAULT '0',
  `HOBBIES` varchar(255) DEFAULT NULL,
  `HOMEEMAIL` varchar(255) DEFAULT NULL,
  `ISMEMBER` tinyint(1) DEFAULT '0',
  `JOBTITLE` varchar(255) DEFAULT NULL,
  `LASTNAME` varchar(255) DEFAULT NULL,
  `MEDICARENO` varchar(255) DEFAULT NULL,
  `MEMBERNO` varchar(255) DEFAULT NULL,
  `MEMBERSINCE` date DEFAULT NULL,
  `MIDDLENAME` varchar(255) DEFAULT NULL,
  `POLICECHECKEXPIRY` date DEFAULT NULL,
  `PREFERREDCOMMUNICATIONS` int(11) DEFAULT NULL,
  `PREFERREDEMAIL` int(11) DEFAULT NULL,
  `PREFERREDPHONE` int(11) DEFAULT NULL,
  `PREFIX` varchar(255) DEFAULT NULL,
  `PRIVATEMEDICALFUNDNAME` varchar(255) DEFAULT NULL,
  `PRIVATEMEDICALINSURANCE` tinyint(1) DEFAULT '0',
  `ROLE` int(11) DEFAULT NULL,
  `SCHOOL` varchar(255) DEFAULT NULL,
  `UPDATED` date DEFAULT NULL,
  `WORKEMAIL` varchar(255) DEFAULT NULL,
  `WWCEXPIRY` date DEFAULT NULL,
  `WWCNO` varchar(255) DEFAULT NULL,
  `SECTION_ID` bigint(20) DEFAULT NULL,
  `SECTIONELIGIBILITY_ID` bigint(20) DEFAULT NULL,
  `ADDRESS_ID` bigint(20) DEFAULT NULL,
  `HOMEPHONE_ID` bigint(20) DEFAULT NULL,
  `MOBILE_ID` bigint(20) DEFAULT NULL,
  `WORKPHONE_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_CONTACT_SECTION_ID` (`SECTION_ID`),
  KEY `FK_CONTACT_WORKPHONE_ID` (`WORKPHONE_ID`),
  KEY `FK_CONTACT_HOMEPHONE_ID` (`HOMEPHONE_ID`),
  KEY `FK_CONTACT_ADDRESS_ID` (`ADDRESS_ID`),
  KEY `FK_CONTACT_SECTIONELIGIBILITY_ID` (`SECTIONELIGIBILITY_ID`),
  KEY `FK_CONTACT_MOBILE_ID` (`MOBILE_ID`),
  CONSTRAINT `FK_CONTACT_MOBILE_ID` FOREIGN KEY (`MOBILE_ID`) REFERENCES `phone` (`ID`),
  CONSTRAINT `FK_CONTACT_ADDRESS_ID` FOREIGN KEY (`ADDRESS_ID`) REFERENCES `address` (`ID`),
  CONSTRAINT `FK_CONTACT_HOMEPHONE_ID` FOREIGN KEY (`HOMEPHONE_ID`) REFERENCES `phone` (`ID`),
  CONSTRAINT `FK_CONTACT_SECTIONELIGIBILITY_ID` FOREIGN KEY (`SECTIONELIGIBILITY_ID`) REFERENCES `sectiontype` (`ID`),
  CONSTRAINT `FK_CONTACT_SECTION_ID` FOREIGN KEY (`SECTION_ID`) REFERENCES `sectiontype` (`ID`),
  CONSTRAINT `FK_CONTACT_WORKPHONE_ID` FOREIGN KEY (`WORKPHONE_ID`) REFERENCES `phone` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `contact`
--

LOCK TABLES `contact` WRITE;
/*!40000 ALTER TABLE `contact` DISABLE KEYS */;
/*!40000 ALTER TABLE `contact` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `contact_activity`
--

DROP TABLE IF EXISTS `contact_activity`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `contact_activity` (
  `Contact_ID` bigint(20) NOT NULL,
  `activites_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`Contact_ID`,`activites_ID`),
  KEY `FK_CONTACT_ACTIVITY_activites_ID` (`activites_ID`),
  CONSTRAINT `FK_CONTACT_ACTIVITY_Contact_ID` FOREIGN KEY (`Contact_ID`) REFERENCES `contact` (`ID`),
  CONSTRAINT `FK_CONTACT_ACTIVITY_activites_ID` FOREIGN KEY (`activites_ID`) REFERENCES `activity` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `contact_activity`
--

LOCK TABLES `contact_activity` WRITE;
/*!40000 ALTER TABLE `contact_activity` DISABLE KEYS */;
/*!40000 ALTER TABLE `contact_activity` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `contact_note`
--

DROP TABLE IF EXISTS `contact_note`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `contact_note` (
  `Contact_ID` bigint(20) NOT NULL,
  `notes_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`Contact_ID`,`notes_ID`),
  KEY `FK_CONTACT_NOTE_notes_ID` (`notes_ID`),
  CONSTRAINT `FK_CONTACT_NOTE_Contact_ID` FOREIGN KEY (`Contact_ID`) REFERENCES `contact` (`ID`),
  CONSTRAINT `FK_CONTACT_NOTE_notes_ID` FOREIGN KEY (`notes_ID`) REFERENCES `note` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `contact_note`
--

LOCK TABLES `contact_note` WRITE;
/*!40000 ALTER TABLE `contact_note` DISABLE KEYS */;
/*!40000 ALTER TABLE `contact_note` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `contact_tag`
--

DROP TABLE IF EXISTS `contact_tag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `contact_tag` (
  `Contact_ID` bigint(20) NOT NULL,
  `tags_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`Contact_ID`,`tags_ID`),
  KEY `FK_CONTACT_TAG_tags_ID` (`tags_ID`),
  CONSTRAINT `FK_CONTACT_TAG_Contact_ID` FOREIGN KEY (`Contact_ID`) REFERENCES `contact` (`ID`),
  CONSTRAINT `FK_CONTACT_TAG_tags_ID` FOREIGN KEY (`tags_ID`) REFERENCES `tag` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `contact_tag`
--

LOCK TABLES `contact_tag` WRITE;
/*!40000 ALTER TABLE `contact_tag` DISABLE KEYS */;
/*!40000 ALTER TABLE `contact_tag` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `creditnote`
--

DROP TABLE IF EXISTS `creditnote`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `creditnote` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `CONSISTENCYVERSION` bigint(20) NOT NULL,
  `CREATED` date DEFAULT NULL,
  `UPDATED` date DEFAULT NULL,
  `ASSOCIATEDINVOICE_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_CREDITNOTE_ASSOCIATEDINVOICE_ID` (`ASSOCIATEDINVOICE_ID`),
  CONSTRAINT `FK_CREDITNOTE_ASSOCIATEDINVOICE_ID` FOREIGN KEY (`ASSOCIATEDINVOICE_ID`) REFERENCES `invoice` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `creditnote`
--

LOCK TABLES `creditnote` WRITE;
/*!40000 ALTER TABLE `creditnote` DISABLE KEYS */;
/*!40000 ALTER TABLE `creditnote` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `creditnote_creditnoteline`
--

DROP TABLE IF EXISTS `creditnote_creditnoteline`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `creditnote_creditnoteline` (
  `CreditNote_ID` bigint(20) NOT NULL,
  `creditNoteLines_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`CreditNote_ID`,`creditNoteLines_ID`),
  KEY `FK_CREDITNOTE_CREDITNOTELINE_creditNoteLines_ID` (`creditNoteLines_ID`),
  CONSTRAINT `FK_CREDITNOTE_CREDITNOTELINE_creditNoteLines_ID` FOREIGN KEY (`creditNoteLines_ID`) REFERENCES `creditnoteline` (`ID`),
  CONSTRAINT `FK_CREDITNOTE_CREDITNOTELINE_CreditNote_ID` FOREIGN KEY (`CreditNote_ID`) REFERENCES `creditnote` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `creditnote_creditnoteline`
--

LOCK TABLES `creditnote_creditnoteline` WRITE;
/*!40000 ALTER TABLE `creditnote_creditnoteline` DISABLE KEYS */;
/*!40000 ALTER TABLE `creditnote_creditnoteline` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `creditnoteline`
--

DROP TABLE IF EXISTS `creditnoteline`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `creditnoteline` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `CONSISTENCYVERSION` bigint(20) NOT NULL,
  `CREATED` date DEFAULT NULL,
  `ORDINAL` int(11) DEFAULT NULL,
  `QUANTITY` decimal(38,0) DEFAULT NULL,
  `UPDATED` date DEFAULT NULL,
  `itemCostMoney` longblob,
  `itemCostTaxPercentage` decimal(38,0) DEFAULT NULL,
  `lineTotalMoney` longblob,
  `lineTotalTaxPercentage` decimal(38,0) DEFAULT NULL,
  `CREDITNOTE_ID` bigint(20) DEFAULT NULL,
  `PRODUCT_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_CREDITNOTELINE_PRODUCT_ID` (`PRODUCT_ID`),
  KEY `FK_CREDITNOTELINE_CREDITNOTE_ID` (`CREDITNOTE_ID`),
  CONSTRAINT `FK_CREDITNOTELINE_CREDITNOTE_ID` FOREIGN KEY (`CREDITNOTE_ID`) REFERENCES `creditnote` (`ID`),
  CONSTRAINT `FK_CREDITNOTELINE_PRODUCT_ID` FOREIGN KEY (`PRODUCT_ID`) REFERENCES `product` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `creditnoteline`
--

LOCK TABLES `creditnoteline` WRITE;
/*!40000 ALTER TABLE `creditnoteline` DISABLE KEYS */;
/*!40000 ALTER TABLE `creditnoteline` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `event`
--

DROP TABLE IF EXISTS `event`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `event` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `DTYPE` varchar(31) DEFAULT NULL,
  `ALLDAYEVENT` tinyint(1) DEFAULT '0',
  `CONSISTENCYVERSION` bigint(20) NOT NULL,
  `CREATED` date DEFAULT NULL,
  `DETAILS` varchar(255) DEFAULT NULL,
  `EVENTENDDATETIME` date DEFAULT NULL,
  `EVENTSTARTDATETIME` date DEFAULT NULL,
  `LASTMODIFIED` date DEFAULT NULL,
  `SUBJECT` varchar(255) DEFAULT NULL,
  `UPDATED` date DEFAULT NULL,
  `SECTION_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_EVENT_SECTION_ID` (`SECTION_ID`),
  CONSTRAINT `FK_EVENT_SECTION_ID` FOREIGN KEY (`SECTION_ID`) REFERENCES `sectiontype` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `event`
--

LOCK TABLES `event` WRITE;
/*!40000 ALTER TABLE `event` DISABLE KEYS */;
/*!40000 ALTER TABLE `event` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `event_contact`
--

DROP TABLE IF EXISTS `event_contact`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `event_contact` (
  `Event_ID` bigint(20) NOT NULL,
  `coordinators_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`Event_ID`,`coordinators_ID`),
  KEY `FK_EVENT_CONTACT_coordinators_ID` (`coordinators_ID`),
  CONSTRAINT `FK_EVENT_CONTACT_Event_ID` FOREIGN KEY (`Event_ID`) REFERENCES `event` (`ID`),
  CONSTRAINT `FK_EVENT_CONTACT_coordinators_ID` FOREIGN KEY (`coordinators_ID`) REFERENCES `contact` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `event_contact`
--

LOCK TABLES `event_contact` WRITE;
/*!40000 ALTER TABLE `event_contact` DISABLE KEYS */;
/*!40000 ALTER TABLE `event_contact` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `feature`
--

DROP TABLE IF EXISTS `feature`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `feature` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `CONSISTENCYVERSION` bigint(20) NOT NULL,
  `CREATED` date DEFAULT NULL,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  `DESCRIPTOR` varchar(255) DEFAULT NULL,
  `UPDATED` date DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `feature`
--

LOCK TABLES `feature` WRITE;
/*!40000 ALTER TABLE `feature` DISABLE KEYS */;
/*!40000 ALTER TABLE `feature` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `household`
--

DROP TABLE IF EXISTS `household`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `household` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `CONSISTENCYVERSION` bigint(20) NOT NULL,
  `CREATED` date DEFAULT NULL,
  `NAME` varchar(255) DEFAULT NULL,
  `UPDATED` date DEFAULT NULL,
  `LOCATION_ID` bigint(20) DEFAULT NULL,
  `PRIMARYCONTACT_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_HOUSEHOLD_PRIMARYCONTACT_ID` (`PRIMARYCONTACT_ID`),
  KEY `FK_HOUSEHOLD_LOCATION_ID` (`LOCATION_ID`),
  CONSTRAINT `FK_HOUSEHOLD_LOCATION_ID` FOREIGN KEY (`LOCATION_ID`) REFERENCES `address` (`ID`),
  CONSTRAINT `FK_HOUSEHOLD_PRIMARYCONTACT_ID` FOREIGN KEY (`PRIMARYCONTACT_ID`) REFERENCES `contact` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `household`
--

LOCK TABLES `household` WRITE;
/*!40000 ALTER TABLE `household` DISABLE KEYS */;
/*!40000 ALTER TABLE `household` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `household_activity`
--

DROP TABLE IF EXISTS `household_activity`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `household_activity` (
  `Household_ID` bigint(20) NOT NULL,
  `activites_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`Household_ID`,`activites_ID`),
  KEY `FK_HOUSEHOLD_ACTIVITY_activites_ID` (`activites_ID`),
  CONSTRAINT `FK_HOUSEHOLD_ACTIVITY_Household_ID` FOREIGN KEY (`Household_ID`) REFERENCES `household` (`ID`),
  CONSTRAINT `FK_HOUSEHOLD_ACTIVITY_activites_ID` FOREIGN KEY (`activites_ID`) REFERENCES `activity` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `household_activity`
--

LOCK TABLES `household_activity` WRITE;
/*!40000 ALTER TABLE `household_activity` DISABLE KEYS */;
/*!40000 ALTER TABLE `household_activity` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `household_note`
--

DROP TABLE IF EXISTS `household_note`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `household_note` (
  `Household_ID` bigint(20) NOT NULL,
  `notes_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`Household_ID`,`notes_ID`),
  KEY `FK_HOUSEHOLD_NOTE_notes_ID` (`notes_ID`),
  CONSTRAINT `FK_HOUSEHOLD_NOTE_Household_ID` FOREIGN KEY (`Household_ID`) REFERENCES `household` (`ID`),
  CONSTRAINT `FK_HOUSEHOLD_NOTE_notes_ID` FOREIGN KEY (`notes_ID`) REFERENCES `note` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `household_note`
--

LOCK TABLES `household_note` WRITE;
/*!40000 ALTER TABLE `household_note` DISABLE KEYS */;
/*!40000 ALTER TABLE `household_note` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `household_relationship`
--

DROP TABLE IF EXISTS `household_relationship`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `household_relationship` (
  `Household_ID` bigint(20) NOT NULL,
  `members_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`Household_ID`,`members_ID`),
  KEY `FK_HOUSEHOLD_RELATIONSHIP_members_ID` (`members_ID`),
  CONSTRAINT `FK_HOUSEHOLD_RELATIONSHIP_members_ID` FOREIGN KEY (`members_ID`) REFERENCES `relationship` (`ID`),
  CONSTRAINT `FK_HOUSEHOLD_RELATIONSHIP_Household_ID` FOREIGN KEY (`Household_ID`) REFERENCES `household` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `household_relationship`
--

LOCK TABLES `household_relationship` WRITE;
/*!40000 ALTER TABLE `household_relationship` DISABLE KEYS */;
/*!40000 ALTER TABLE `household_relationship` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `household_tag`
--

DROP TABLE IF EXISTS `household_tag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `household_tag` (
  `Household_ID` bigint(20) NOT NULL,
  `tags_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`Household_ID`,`tags_ID`),
  KEY `FK_HOUSEHOLD_TAG_tags_ID` (`tags_ID`),
  CONSTRAINT `FK_HOUSEHOLD_TAG_Household_ID` FOREIGN KEY (`Household_ID`) REFERENCES `household` (`ID`),
  CONSTRAINT `FK_HOUSEHOLD_TAG_tags_ID` FOREIGN KEY (`tags_ID`) REFERENCES `tag` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `household_tag`
--

LOCK TABLES `household_tag` WRITE;
/*!40000 ALTER TABLE `household_tag` DISABLE KEYS */;
/*!40000 ALTER TABLE `household_tag` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `importcolumnfieldmapping`
--

DROP TABLE IF EXISTS `importcolumnfieldmapping`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `importcolumnfieldmapping` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `CONSISTENCYVERSION` bigint(20) NOT NULL,
  `CREATED` date DEFAULT NULL,
  `CSVCOLUMNNAME` varchar(255) DEFAULT NULL,
  `DBFIELDNAME` varchar(255) DEFAULT NULL,
  `UPDATED` date DEFAULT NULL,
  `USERMAPPING_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_IMPORTCOLUMNFIELDMAPPING_USERMAPPING_ID` (`USERMAPPING_ID`),
  CONSTRAINT `FK_IMPORTCOLUMNFIELDMAPPING_USERMAPPING_ID` FOREIGN KEY (`USERMAPPING_ID`) REFERENCES `importusermapping` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `importcolumnfieldmapping`
--

LOCK TABLES `importcolumnfieldmapping` WRITE;
/*!40000 ALTER TABLE `importcolumnfieldmapping` DISABLE KEYS */;
/*!40000 ALTER TABLE `importcolumnfieldmapping` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `importusermapping`
--

DROP TABLE IF EXISTS `importusermapping`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `importusermapping` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `CONSISTENCYVERSION` bigint(20) NOT NULL,
  `CREATED` date DEFAULT NULL,
  `MAPPINGNAME` varchar(255) DEFAULT NULL,
  `UPDATED` date DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `importusermapping`
--

LOCK TABLES `importusermapping` WRITE;
/*!40000 ALTER TABLE `importusermapping` DISABLE KEYS */;
/*!40000 ALTER TABLE `importusermapping` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `invoice`
--

DROP TABLE IF EXISTS `invoice`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `invoice` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `CONSISTENCYVERSION` bigint(20) NOT NULL,
  `CREATED` date DEFAULT NULL,
  `INVOICEDATE` date DEFAULT NULL,
  `INVOICENO` varchar(255) DEFAULT NULL,
  `NOTES` varchar(255) DEFAULT NULL,
  `TERMS` int(11) DEFAULT NULL,
  `UPDATED` date DEFAULT NULL,
  `BILLTO_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_INVOICE_BILLTO_ID` (`BILLTO_ID`),
  CONSTRAINT `FK_INVOICE_BILLTO_ID` FOREIGN KEY (`BILLTO_ID`) REFERENCES `contact` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `invoice`
--

LOCK TABLES `invoice` WRITE;
/*!40000 ALTER TABLE `invoice` DISABLE KEYS */;
/*!40000 ALTER TABLE `invoice` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `invoice_invoiceline`
--

DROP TABLE IF EXISTS `invoice_invoiceline`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `invoice_invoiceline` (
  `Invoice_ID` bigint(20) NOT NULL,
  `invoiceLines_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`Invoice_ID`,`invoiceLines_ID`),
  KEY `FK_INVOICE_INVOICELINE_invoiceLines_ID` (`invoiceLines_ID`),
  CONSTRAINT `FK_INVOICE_INVOICELINE_Invoice_ID` FOREIGN KEY (`Invoice_ID`) REFERENCES `invoice` (`ID`),
  CONSTRAINT `FK_INVOICE_INVOICELINE_invoiceLines_ID` FOREIGN KEY (`invoiceLines_ID`) REFERENCES `invoiceline` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `invoice_invoiceline`
--

LOCK TABLES `invoice_invoiceline` WRITE;
/*!40000 ALTER TABLE `invoice_invoiceline` DISABLE KEYS */;
/*!40000 ALTER TABLE `invoice_invoiceline` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `invoice_payment`
--

DROP TABLE IF EXISTS `invoice_payment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `invoice_payment` (
  `Invoice_ID` bigint(20) NOT NULL,
  `payments_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`Invoice_ID`,`payments_ID`),
  KEY `FK_INVOICE_PAYMENT_payments_ID` (`payments_ID`),
  CONSTRAINT `FK_INVOICE_PAYMENT_payments_ID` FOREIGN KEY (`payments_ID`) REFERENCES `payment` (`ID`),
  CONSTRAINT `FK_INVOICE_PAYMENT_Invoice_ID` FOREIGN KEY (`Invoice_ID`) REFERENCES `invoice` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `invoice_payment`
--

LOCK TABLES `invoice_payment` WRITE;
/*!40000 ALTER TABLE `invoice_payment` DISABLE KEYS */;
/*!40000 ALTER TABLE `invoice_payment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `invoiceline`
--

DROP TABLE IF EXISTS `invoiceline`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `invoiceline` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `CONSISTENCYVERSION` bigint(20) NOT NULL,
  `CREATED` date DEFAULT NULL,
  `ORDINAL` int(11) DEFAULT NULL,
  `QUANTITY` decimal(38,0) DEFAULT NULL,
  `UPDATED` date DEFAULT NULL,
  `itemCostMoney` longblob,
  `itemCostTaxPercentage` decimal(38,0) DEFAULT NULL,
  `lineTotalMoney` longblob,
  `lineTotalTaxPercentage` decimal(38,0) DEFAULT NULL,
  `INVOICE_ID` bigint(20) DEFAULT NULL,
  `PRODUCT_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_INVOICELINE_INVOICE_ID` (`INVOICE_ID`),
  KEY `FK_INVOICELINE_PRODUCT_ID` (`PRODUCT_ID`),
  CONSTRAINT `FK_INVOICELINE_PRODUCT_ID` FOREIGN KEY (`PRODUCT_ID`) REFERENCES `product` (`ID`),
  CONSTRAINT `FK_INVOICELINE_INVOICE_ID` FOREIGN KEY (`INVOICE_ID`) REFERENCES `invoice` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `invoiceline`
--

LOCK TABLES `invoiceline` WRITE;
/*!40000 ALTER TABLE `invoiceline` DISABLE KEYS */;
/*!40000 ALTER TABLE `invoiceline` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `note`
--

DROP TABLE IF EXISTS `note`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `note` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `BODY` varchar(255) DEFAULT NULL,
  `CONSISTENCYVERSION` bigint(20) NOT NULL,
  `CREATED` date DEFAULT NULL,
  `SUBJECT` varchar(255) DEFAULT NULL,
  `UPDATED` date DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `note`
--

LOCK TABLES `note` WRITE;
/*!40000 ALTER TABLE `note` DISABLE KEYS */;
/*!40000 ALTER TABLE `note` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `organisation`
--

DROP TABLE IF EXISTS `organisation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `organisation` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `CONSISTENCYVERSION` bigint(20) NOT NULL,
  `CREATED` date DEFAULT NULL,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  `ISOURSCOUTGROUP` tinyint(1) DEFAULT '0',
  `NAME` varchar(255) DEFAULT NULL,
  `UPDATED` date DEFAULT NULL,
  `LOCATION_ID` bigint(20) DEFAULT NULL,
  `PRIMARYPHONE_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_ORGANISATION_PRIMARYPHONE_ID` (`PRIMARYPHONE_ID`),
  KEY `FK_ORGANISATION_LOCATION_ID` (`LOCATION_ID`),
  CONSTRAINT `FK_ORGANISATION_LOCATION_ID` FOREIGN KEY (`LOCATION_ID`) REFERENCES `address` (`ID`),
  CONSTRAINT `FK_ORGANISATION_PRIMARYPHONE_ID` FOREIGN KEY (`PRIMARYPHONE_ID`) REFERENCES `phone` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `organisation`
--

LOCK TABLES `organisation` WRITE;
/*!40000 ALTER TABLE `organisation` DISABLE KEYS */;
/*!40000 ALTER TABLE `organisation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `organisation_activity`
--

DROP TABLE IF EXISTS `organisation_activity`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `organisation_activity` (
  `Organisation_ID` bigint(20) NOT NULL,
  `activites_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`Organisation_ID`,`activites_ID`),
  KEY `FK_ORGANISATION_ACTIVITY_activites_ID` (`activites_ID`),
  CONSTRAINT `FK_ORGANISATION_ACTIVITY_Organisation_ID` FOREIGN KEY (`Organisation_ID`) REFERENCES `organisation` (`ID`),
  CONSTRAINT `FK_ORGANISATION_ACTIVITY_activites_ID` FOREIGN KEY (`activites_ID`) REFERENCES `activity` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `organisation_activity`
--

LOCK TABLES `organisation_activity` WRITE;
/*!40000 ALTER TABLE `organisation_activity` DISABLE KEYS */;
/*!40000 ALTER TABLE `organisation_activity` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `organisation_contact`
--

DROP TABLE IF EXISTS `organisation_contact`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `organisation_contact` (
  `Organisation_ID` bigint(20) NOT NULL,
  `contacts_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`Organisation_ID`,`contacts_ID`),
  KEY `FK_ORGANISATION_CONTACT_contacts_ID` (`contacts_ID`),
  CONSTRAINT `FK_ORGANISATION_CONTACT_Organisation_ID` FOREIGN KEY (`Organisation_ID`) REFERENCES `organisation` (`ID`),
  CONSTRAINT `FK_ORGANISATION_CONTACT_contacts_ID` FOREIGN KEY (`contacts_ID`) REFERENCES `contact` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `organisation_contact`
--

LOCK TABLES `organisation_contact` WRITE;
/*!40000 ALTER TABLE `organisation_contact` DISABLE KEYS */;
/*!40000 ALTER TABLE `organisation_contact` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `organisation_note`
--

DROP TABLE IF EXISTS `organisation_note`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `organisation_note` (
  `Organisation_ID` bigint(20) NOT NULL,
  `notes_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`Organisation_ID`,`notes_ID`),
  KEY `FK_ORGANISATION_NOTE_notes_ID` (`notes_ID`),
  CONSTRAINT `FK_ORGANISATION_NOTE_Organisation_ID` FOREIGN KEY (`Organisation_ID`) REFERENCES `organisation` (`ID`),
  CONSTRAINT `FK_ORGANISATION_NOTE_notes_ID` FOREIGN KEY (`notes_ID`) REFERENCES `note` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `organisation_note`
--

LOCK TABLES `organisation_note` WRITE;
/*!40000 ALTER TABLE `organisation_note` DISABLE KEYS */;
/*!40000 ALTER TABLE `organisation_note` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `organisation_tag`
--

DROP TABLE IF EXISTS `organisation_tag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `organisation_tag` (
  `Organisation_ID` bigint(20) NOT NULL,
  `tags_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`Organisation_ID`,`tags_ID`),
  KEY `FK_ORGANISATION_TAG_tags_ID` (`tags_ID`),
  CONSTRAINT `FK_ORGANISATION_TAG_Organisation_ID` FOREIGN KEY (`Organisation_ID`) REFERENCES `organisation` (`ID`),
  CONSTRAINT `FK_ORGANISATION_TAG_tags_ID` FOREIGN KEY (`tags_ID`) REFERENCES `tag` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `organisation_tag`
--

LOCK TABLES `organisation_tag` WRITE;
/*!40000 ALTER TABLE `organisation_tag` DISABLE KEYS */;
/*!40000 ALTER TABLE `organisation_tag` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `payment`
--

DROP TABLE IF EXISTS `payment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `payment` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `CONSISTENCYVERSION` bigint(20) NOT NULL,
  `CREATED` date DEFAULT NULL,
  `NOTE` varchar(255) DEFAULT NULL,
  `PAYMENTDATE` date DEFAULT NULL,
  `REFERENCE` varchar(255) DEFAULT NULL,
  `UPDATED` date DEFAULT NULL,
  `MONEY` longblob,
  `TAXPERCENTAGE` decimal(38,0) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `payment`
--

LOCK TABLES `payment` WRITE;
/*!40000 ALTER TABLE `payment` DISABLE KEYS */;
/*!40000 ALTER TABLE `payment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `payment_invoice`
--

DROP TABLE IF EXISTS `payment_invoice`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `payment_invoice` (
  `Payment_ID` bigint(20) NOT NULL,
  `invoices_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`Payment_ID`,`invoices_ID`),
  KEY `FK_PAYMENT_INVOICE_invoices_ID` (`invoices_ID`),
  CONSTRAINT `FK_PAYMENT_INVOICE_invoices_ID` FOREIGN KEY (`invoices_ID`) REFERENCES `invoice` (`ID`),
  CONSTRAINT `FK_PAYMENT_INVOICE_Payment_ID` FOREIGN KEY (`Payment_ID`) REFERENCES `payment` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `payment_invoice`
--

LOCK TABLES `payment_invoice` WRITE;
/*!40000 ALTER TABLE `payment_invoice` DISABLE KEYS */;
/*!40000 ALTER TABLE `payment_invoice` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `phone`
--

DROP TABLE IF EXISTS `phone`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `phone` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `CONSISTENCYVERSION` bigint(20) NOT NULL,
  `CREATED` date DEFAULT NULL,
  `LOCATIONTYPE` int(11) DEFAULT NULL,
  `PHONENO` varchar(255) DEFAULT NULL,
  `PHONETYPE` int(11) DEFAULT NULL,
  `PRIMARYPHONE` tinyint(1) DEFAULT '0',
  `UPDATED` date DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `phone`
--

LOCK TABLES `phone` WRITE;
/*!40000 ALTER TABLE `phone` DISABLE KEYS */;
/*!40000 ALTER TABLE `phone` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product`
--

DROP TABLE IF EXISTS `product`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `product` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `CONSISTENCYVERSION` bigint(20) NOT NULL,
  `CREATED` date DEFAULT NULL,
  `DESRIPTION` varchar(255) DEFAULT NULL,
  `NAME` varchar(255) DEFAULT NULL,
  `UPDATED` date DEFAULT NULL,
  `chargeMoney` longblob,
  `chargeTaxPercentage` decimal(38,0) DEFAULT NULL,
  `costtMoney` longblob,
  `costTaxPercentage` decimal(38,0) DEFAULT NULL,
  `SUPPLIER_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_PRODUCT_SUPPLIER_ID` (`SUPPLIER_ID`),
  CONSTRAINT `FK_PRODUCT_SUPPLIER_ID` FOREIGN KEY (`SUPPLIER_ID`) REFERENCES `organisation` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product`
--

LOCK TABLES `product` WRITE;
/*!40000 ALTER TABLE `product` DISABLE KEYS */;
/*!40000 ALTER TABLE `product` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `qualification`
--

DROP TABLE IF EXISTS `qualification`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `qualification` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `CONSISTENCYVERSION` bigint(20) NOT NULL,
  `CREATED` date DEFAULT NULL,
  `EXPIRES` date DEFAULT NULL,
  `OBTAINED` date DEFAULT NULL,
  `UPDATED` date DEFAULT NULL,
  `LEADER_ID` bigint(20) DEFAULT NULL,
  `TYPE_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_QUALIFICATION_LEADER_ID` (`LEADER_ID`),
  KEY `FK_QUALIFICATION_TYPE_ID` (`TYPE_ID`),
  CONSTRAINT `FK_QUALIFICATION_TYPE_ID` FOREIGN KEY (`TYPE_ID`) REFERENCES `qualificationtype` (`ID`),
  CONSTRAINT `FK_QUALIFICATION_LEADER_ID` FOREIGN KEY (`LEADER_ID`) REFERENCES `contact` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `qualification`
--

LOCK TABLES `qualification` WRITE;
/*!40000 ALTER TABLE `qualification` DISABLE KEYS */;
/*!40000 ALTER TABLE `qualification` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `qualificationtype`
--

DROP TABLE IF EXISTS `qualificationtype`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `qualificationtype` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `CONSISTENCYVERSION` bigint(20) NOT NULL,
  `CREATED` date DEFAULT NULL,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  `EXPIRES` tinyint(1) DEFAULT '0',
  `NAME` varchar(255) DEFAULT NULL,
  `UPDATED` date DEFAULT NULL,
  `DAYS` int(11) DEFAULT NULL,
  `MONTHS` int(11) DEFAULT NULL,
  `YEARS` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `qualificationtype`
--

LOCK TABLES `qualificationtype` WRITE;
/*!40000 ALTER TABLE `qualificationtype` DISABLE KEYS */;
/*!40000 ALTER TABLE `qualificationtype` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `relationship`
--

DROP TABLE IF EXISTS `relationship`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `relationship` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `CONSISTENCYVERSION` bigint(20) NOT NULL,
  `CREATED` date DEFAULT NULL,
  `UPDATED` date DEFAULT NULL,
  `FIRST_ID` bigint(20) DEFAULT NULL,
  `HOUSEHOLD_ID` bigint(20) DEFAULT NULL,
  `ORGANISATION_ID` bigint(20) DEFAULT NULL,
  `SCHOOL_ID` bigint(20) DEFAULT NULL,
  `SECONDCONTACT_ID` bigint(20) DEFAULT NULL,
  `TYPE_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_RELATIONSHIP_SCHOOL_ID` (`SCHOOL_ID`),
  KEY `FK_RELATIONSHIP_ORGANISATION_ID` (`ORGANISATION_ID`),
  KEY `FK_RELATIONSHIP_HOUSEHOLD_ID` (`HOUSEHOLD_ID`),
  KEY `FK_RELATIONSHIP_TYPE_ID` (`TYPE_ID`),
  KEY `FK_RELATIONSHIP_FIRST_ID` (`FIRST_ID`),
  KEY `FK_RELATIONSHIP_SECONDCONTACT_ID` (`SECONDCONTACT_ID`),
  CONSTRAINT `FK_RELATIONSHIP_SECONDCONTACT_ID` FOREIGN KEY (`SECONDCONTACT_ID`) REFERENCES `contact` (`ID`),
  CONSTRAINT `FK_RELATIONSHIP_FIRST_ID` FOREIGN KEY (`FIRST_ID`) REFERENCES `contact` (`ID`),
  CONSTRAINT `FK_RELATIONSHIP_HOUSEHOLD_ID` FOREIGN KEY (`HOUSEHOLD_ID`) REFERENCES `household` (`ID`),
  CONSTRAINT `FK_RELATIONSHIP_ORGANISATION_ID` FOREIGN KEY (`ORGANISATION_ID`) REFERENCES `organisation` (`ID`),
  CONSTRAINT `FK_RELATIONSHIP_SCHOOL_ID` FOREIGN KEY (`SCHOOL_ID`) REFERENCES `school` (`ID`),
  CONSTRAINT `FK_RELATIONSHIP_TYPE_ID` FOREIGN KEY (`TYPE_ID`) REFERENCES `relationshiptype` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `relationship`
--

LOCK TABLES `relationship` WRITE;
/*!40000 ALTER TABLE `relationship` DISABLE KEYS */;
/*!40000 ALTER TABLE `relationship` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `relationshiptype`
--

DROP TABLE IF EXISTS `relationshiptype`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `relationshiptype` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `CONSISTENCYVERSION` bigint(20) NOT NULL,
  `CREATED` date DEFAULT NULL,
  `LHS` varchar(255) DEFAULT NULL,
  `LHSTYPE` varchar(255) DEFAULT NULL,
  `RHS` varchar(255) DEFAULT NULL,
  `RHSTYPE` varchar(255) DEFAULT NULL,
  `UPDATED` date DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `relationshiptype`
--

LOCK TABLES `relationshiptype` WRITE;
/*!40000 ALTER TABLE `relationshiptype` DISABLE KEYS */;
/*!40000 ALTER TABLE `relationshiptype` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role`
--

DROP TABLE IF EXISTS `role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `role` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `CONSISTENCYVERSION` bigint(20) NOT NULL,
  `CREATED` date DEFAULT NULL,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  `NAME` varchar(255) DEFAULT NULL,
  `UPDATED` date DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role`
--

LOCK TABLES `role` WRITE;
/*!40000 ALTER TABLE `role` DISABLE KEYS */;
/*!40000 ALTER TABLE `role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role_feature`
--

DROP TABLE IF EXISTS `role_feature`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `role_feature` (
  `Role_ID` bigint(20) NOT NULL,
  `permitted_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`Role_ID`,`permitted_ID`),
  KEY `FK_ROLE_FEATURE_permitted_ID` (`permitted_ID`),
  CONSTRAINT `FK_ROLE_FEATURE_Role_ID` FOREIGN KEY (`Role_ID`) REFERENCES `role` (`ID`),
  CONSTRAINT `FK_ROLE_FEATURE_permitted_ID` FOREIGN KEY (`permitted_ID`) REFERENCES `feature` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role_feature`
--

LOCK TABLES `role_feature` WRITE;
/*!40000 ALTER TABLE `role_feature` DISABLE KEYS */;
/*!40000 ALTER TABLE `role_feature` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `school`
--

DROP TABLE IF EXISTS `school`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `school` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `CONSISTENCYVERSION` bigint(20) NOT NULL,
  `CREATED` date DEFAULT NULL,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  `NAME` varchar(255) DEFAULT NULL,
  `UPDATED` date DEFAULT NULL,
  `ADVERTISINGCONTACT_ID` bigint(20) DEFAULT NULL,
  `PRINCIPLE_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_SCHOOL_PRINCIPLE_ID` (`PRINCIPLE_ID`),
  KEY `FK_SCHOOL_ADVERTISINGCONTACT_ID` (`ADVERTISINGCONTACT_ID`),
  CONSTRAINT `FK_SCHOOL_ADVERTISINGCONTACT_ID` FOREIGN KEY (`ADVERTISINGCONTACT_ID`) REFERENCES `contact` (`ID`),
  CONSTRAINT `FK_SCHOOL_PRINCIPLE_ID` FOREIGN KEY (`PRINCIPLE_ID`) REFERENCES `contact` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `school`
--

LOCK TABLES `school` WRITE;
/*!40000 ALTER TABLE `school` DISABLE KEYS */;
/*!40000 ALTER TABLE `school` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `school_address`
--

DROP TABLE IF EXISTS `school_address`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `school_address` (
  `School_ID` bigint(20) NOT NULL,
  `location_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`School_ID`,`location_ID`),
  KEY `FK_SCHOOL_ADDRESS_location_ID` (`location_ID`),
  CONSTRAINT `FK_SCHOOL_ADDRESS_School_ID` FOREIGN KEY (`School_ID`) REFERENCES `school` (`ID`),
  CONSTRAINT `FK_SCHOOL_ADDRESS_location_ID` FOREIGN KEY (`location_ID`) REFERENCES `address` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `school_address`
--

LOCK TABLES `school_address` WRITE;
/*!40000 ALTER TABLE `school_address` DISABLE KEYS */;
/*!40000 ALTER TABLE `school_address` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `school_contact`
--

DROP TABLE IF EXISTS `school_contact`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `school_contact` (
  `School_ID` bigint(20) NOT NULL,
  `youth_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`School_ID`,`youth_ID`),
  KEY `FK_SCHOOL_CONTACT_youth_ID` (`youth_ID`),
  CONSTRAINT `FK_SCHOOL_CONTACT_youth_ID` FOREIGN KEY (`youth_ID`) REFERENCES `contact` (`ID`),
  CONSTRAINT `FK_SCHOOL_CONTACT_School_ID` FOREIGN KEY (`School_ID`) REFERENCES `school` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `school_contact`
--

LOCK TABLES `school_contact` WRITE;
/*!40000 ALTER TABLE `school_contact` DISABLE KEYS */;
/*!40000 ALTER TABLE `school_contact` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `section`
--

DROP TABLE IF EXISTS `section`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `section` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `CONSISTENCYVERSION` bigint(20) NOT NULL,
  `CREATED` date DEFAULT NULL,
  `UPDATED` date DEFAULT NULL,
  `MEETINGDEFAULTS_ID` bigint(20) DEFAULT NULL,
  `TYPE_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_SECTION_TYPE_ID` (`TYPE_ID`),
  KEY `FK_SECTION_MEETINGDEFAULTS_ID` (`MEETINGDEFAULTS_ID`),
  CONSTRAINT `FK_SECTION_MEETINGDEFAULTS_ID` FOREIGN KEY (`MEETINGDEFAULTS_ID`) REFERENCES `sectionmeetingdefaults` (`ID`),
  CONSTRAINT `FK_SECTION_TYPE_ID` FOREIGN KEY (`TYPE_ID`) REFERENCES `sectiontype` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `section`
--

LOCK TABLES `section` WRITE;
/*!40000 ALTER TABLE `section` DISABLE KEYS */;
/*!40000 ALTER TABLE `section` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `section_contact`
--

DROP TABLE IF EXISTS `section_contact`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `section_contact` (
  `Section_ID` bigint(20) NOT NULL,
  `adultHelpers_ID` bigint(20) NOT NULL,
  `leaders_ID` bigint(20) NOT NULL,
  `youthMembers_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`Section_ID`,`adultHelpers_ID`,`leaders_ID`,`youthMembers_ID`),
  KEY `FK_SECTION_CONTACT_leaders_ID` (`leaders_ID`),
  KEY `FK_SECTION_CONTACT_adultHelpers_ID` (`adultHelpers_ID`),
  KEY `FK_SECTION_CONTACT_youthMembers_ID` (`youthMembers_ID`),
  CONSTRAINT `FK_SECTION_CONTACT_Section_ID` FOREIGN KEY (`Section_ID`) REFERENCES `section` (`ID`),
  CONSTRAINT `FK_SECTION_CONTACT_adultHelpers_ID` FOREIGN KEY (`adultHelpers_ID`) REFERENCES `contact` (`ID`),
  CONSTRAINT `FK_SECTION_CONTACT_leaders_ID` FOREIGN KEY (`leaders_ID`) REFERENCES `contact` (`ID`),
  CONSTRAINT `FK_SECTION_CONTACT_youthMembers_ID` FOREIGN KEY (`youthMembers_ID`) REFERENCES `contact` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `section_contact`
--

LOCK TABLES `section_contact` WRITE;
/*!40000 ALTER TABLE `section_contact` DISABLE KEYS */;
/*!40000 ALTER TABLE `section_contact` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `section_sectiontryout`
--

DROP TABLE IF EXISTS `section_sectiontryout`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `section_sectiontryout` (
  `Section_ID` bigint(20) NOT NULL,
  `trialMembers_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`Section_ID`,`trialMembers_ID`),
  KEY `FK_SECTION_SECTIONTRYOUT_trialMembers_ID` (`trialMembers_ID`),
  CONSTRAINT `FK_SECTION_SECTIONTRYOUT_Section_ID` FOREIGN KEY (`Section_ID`) REFERENCES `section` (`ID`),
  CONSTRAINT `FK_SECTION_SECTIONTRYOUT_trialMembers_ID` FOREIGN KEY (`trialMembers_ID`) REFERENCES `sectiontryout` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `section_sectiontryout`
--

LOCK TABLES `section_sectiontryout` WRITE;
/*!40000 ALTER TABLE `section_sectiontryout` DISABLE KEYS */;
/*!40000 ALTER TABLE `section_sectiontryout` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `section_transitionmember`
--

DROP TABLE IF EXISTS `section_transitionmember`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `section_transitionmember` (
  `Section_ID` bigint(20) NOT NULL,
  `transitioningYouthMembers_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`Section_ID`,`transitioningYouthMembers_ID`),
  KEY `SECTIONTRANSITIONMEMBERtransitioningYouthMembersID` (`transitioningYouthMembers_ID`),
  CONSTRAINT `SECTIONTRANSITIONMEMBERtransitioningYouthMembersID` FOREIGN KEY (`transitioningYouthMembers_ID`) REFERENCES `transitionmember` (`ID`),
  CONSTRAINT `FK_SECTION_TRANSITIONMEMBER_Section_ID` FOREIGN KEY (`Section_ID`) REFERENCES `section` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `section_transitionmember`
--

LOCK TABLES `section_transitionmember` WRITE;
/*!40000 ALTER TABLE `section_transitionmember` DISABLE KEYS */;
/*!40000 ALTER TABLE `section_transitionmember` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sectionmeetingdefaults`
--

DROP TABLE IF EXISTS `sectionmeetingdefaults`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sectionmeetingdefaults` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `ENDTIME` time DEFAULT NULL,
  `CONSISTENCYVERSION` bigint(20) NOT NULL,
  `CREATED` date DEFAULT NULL,
  `MEETINGDETAILS` varchar(255) DEFAULT NULL,
  `MEETINGNIGHT` int(11) DEFAULT NULL,
  `MEETINGSUBJECT` varchar(255) DEFAULT NULL,
  `STARTTIME` time DEFAULT NULL,
  `UPDATED` date DEFAULT NULL,
  `LOCATION_ID` bigint(20) DEFAULT NULL,
  `SECTION_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_SECTIONMEETINGDEFAULTS_LOCATION_ID` (`LOCATION_ID`),
  KEY `FK_SECTIONMEETINGDEFAULTS_SECTION_ID` (`SECTION_ID`),
  CONSTRAINT `FK_SECTIONMEETINGDEFAULTS_SECTION_ID` FOREIGN KEY (`SECTION_ID`) REFERENCES `section` (`ID`),
  CONSTRAINT `FK_SECTIONMEETINGDEFAULTS_LOCATION_ID` FOREIGN KEY (`LOCATION_ID`) REFERENCES `address` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sectionmeetingdefaults`
--

LOCK TABLES `sectionmeetingdefaults` WRITE;
/*!40000 ALTER TABLE `sectionmeetingdefaults` DISABLE KEYS */;
/*!40000 ALTER TABLE `sectionmeetingdefaults` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sectiontryout`
--

DROP TABLE IF EXISTS `sectiontryout`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sectiontryout` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `ACTUALCOMPLETIONDATE` date DEFAULT NULL,
  `CONSISTENCYVERSION` bigint(20) NOT NULL,
  `CREATED` date DEFAULT NULL,
  `EXPECTEDCOMPLETIONDATE` date DEFAULT NULL,
  `OUTCOME` int(11) DEFAULT NULL,
  `PAID` tinyint(1) DEFAULT '0',
  `STARTDATE` date DEFAULT NULL,
  `TRAILPAPERWORKCOMPLETED` tinyint(1) DEFAULT '0',
  `UPDATED` date DEFAULT NULL,
  `MONEY` longblob,
  `TAXPERCENTAGE` decimal(38,0) DEFAULT NULL,
  `TYPE_ID` bigint(20) DEFAULT NULL,
  `TRIALYOUTHMEMBER_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_SECTIONTRYOUT_TYPE_ID` (`TYPE_ID`),
  KEY `FK_SECTIONTRYOUT_TRIALYOUTHMEMBER_ID` (`TRIALYOUTHMEMBER_ID`),
  CONSTRAINT `FK_SECTIONTRYOUT_TRIALYOUTHMEMBER_ID` FOREIGN KEY (`TRIALYOUTHMEMBER_ID`) REFERENCES `contact` (`ID`),
  CONSTRAINT `FK_SECTIONTRYOUT_TYPE_ID` FOREIGN KEY (`TYPE_ID`) REFERENCES `sectiontryouttype` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sectiontryout`
--

LOCK TABLES `sectiontryout` WRITE;
/*!40000 ALTER TABLE `sectiontryout` DISABLE KEYS */;
/*!40000 ALTER TABLE `sectiontryout` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sectiontryouttype`
--

DROP TABLE IF EXISTS `sectiontryouttype`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sectiontryouttype` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `CONSISTENCYVERSION` bigint(20) NOT NULL,
  `COST` longblob,
  `CREATED` date DEFAULT NULL,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  `NAME` varchar(255) DEFAULT NULL,
  `PAPERWORKREQUIRED` tinyint(1) DEFAULT '0',
  `UPDATED` date DEFAULT NULL,
  `WEEKS` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sectiontryouttype`
--

LOCK TABLES `sectiontryouttype` WRITE;
/*!40000 ALTER TABLE `sectiontryouttype` DISABLE KEYS */;
/*!40000 ALTER TABLE `sectiontryouttype` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sectiontryouttype_section`
--

DROP TABLE IF EXISTS `sectiontryouttype_section`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sectiontryouttype_section` (
  `SectionTryoutType_ID` bigint(20) NOT NULL,
  `sections_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`SectionTryoutType_ID`,`sections_ID`),
  KEY `FK_SECTIONTRYOUTTYPE_SECTION_sections_ID` (`sections_ID`),
  CONSTRAINT `FK_SECTIONTRYOUTTYPE_SECTION_SectionTryoutType_ID` FOREIGN KEY (`SectionTryoutType_ID`) REFERENCES `sectiontryouttype` (`ID`),
  CONSTRAINT `FK_SECTIONTRYOUTTYPE_SECTION_sections_ID` FOREIGN KEY (`sections_ID`) REFERENCES `section` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sectiontryouttype_section`
--

LOCK TABLES `sectiontryouttype_section` WRITE;
/*!40000 ALTER TABLE `sectiontryouttype_section` DISABLE KEYS */;
/*!40000 ALTER TABLE `sectiontryouttype_section` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sectiontype`
--

DROP TABLE IF EXISTS `sectiontype`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sectiontype` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `CONSISTENCYVERSION` bigint(20) NOT NULL,
  `CREATED` date DEFAULT NULL,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  `NAME` varchar(255) DEFAULT NULL,
  `UPDATED` date DEFAULT NULL,
  `endingAgeDays` int(11) DEFAULT NULL,
  `endingAgeMonths` int(11) DEFAULT NULL,
  `endingAgeYears` int(11) DEFAULT NULL,
  `startingAgeDays` int(11) DEFAULT NULL,
  `startingAgeMonths` int(11) DEFAULT NULL,
  `startingAgeYears` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sectiontype`
--

LOCK TABLES `sectiontype` WRITE;
/*!40000 ALTER TABLE `sectiontype` DISABLE KEYS */;
/*!40000 ALTER TABLE `sectiontype` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sectiontype_qualificationtype`
--

DROP TABLE IF EXISTS `sectiontype_qualificationtype`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sectiontype_qualificationtype` (
  `SectionType_ID` bigint(20) NOT NULL,
  `assistentLeaderRequirements_ID` bigint(20) NOT NULL,
  `leaderRequirements_ID` bigint(20) NOT NULL,
  `parentHelperRequirements_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`SectionType_ID`,`assistentLeaderRequirements_ID`,`leaderRequirements_ID`,`parentHelperRequirements_ID`),
  KEY `SECTIONTYPE_QUALIFICATIONTYPEleaderRequirements_ID` (`leaderRequirements_ID`),
  KEY `SCTONTYPEQUALIFICATIONTYPEprntHelperRequirementsID` (`parentHelperRequirements_ID`),
  KEY `SCTNTYPQUALIFICATIONTYPEssstntLeaderRequirementsID` (`assistentLeaderRequirements_ID`),
  CONSTRAINT `SCTNTYPQUALIFICATIONTYPEssstntLeaderRequirementsID` FOREIGN KEY (`assistentLeaderRequirements_ID`) REFERENCES `qualificationtype` (`ID`),
  CONSTRAINT `FK_SECTIONTYPE_QUALIFICATIONTYPE_SectionType_ID` FOREIGN KEY (`SectionType_ID`) REFERENCES `sectiontype` (`ID`),
  CONSTRAINT `SCTONTYPEQUALIFICATIONTYPEprntHelperRequirementsID` FOREIGN KEY (`parentHelperRequirements_ID`) REFERENCES `qualificationtype` (`ID`),
  CONSTRAINT `SECTIONTYPE_QUALIFICATIONTYPEleaderRequirements_ID` FOREIGN KEY (`leaderRequirements_ID`) REFERENCES `qualificationtype` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sectiontype_qualificationtype`
--

LOCK TABLES `sectiontype_qualificationtype` WRITE;
/*!40000 ALTER TABLE `sectiontype_qualificationtype` DISABLE KEYS */;
/*!40000 ALTER TABLE `sectiontype_qualificationtype` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sessionhistory`
--

DROP TABLE IF EXISTS `sessionhistory`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sessionhistory` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `CONSISTENCYVERSION` bigint(20) NOT NULL,
  `CREATED` date DEFAULT NULL,
  `END` date DEFAULT NULL,
  `START` date DEFAULT NULL,
  `UPDATED` date DEFAULT NULL,
  `USER_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_SESSIONHISTORY_USER_ID` (`USER_ID`),
  CONSTRAINT `FK_SESSIONHISTORY_USER_ID` FOREIGN KEY (`USER_ID`) REFERENCES `user` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sessionhistory`
--

LOCK TABLES `sessionhistory` WRITE;
/*!40000 ALTER TABLE `sessionhistory` DISABLE KEYS */;
/*!40000 ALTER TABLE `sessionhistory` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `smsprovider`
--

DROP TABLE IF EXISTS `smsprovider`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `smsprovider` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `APIID` varchar(255) DEFAULT NULL,
  `ACTIVE` tinyint(1) DEFAULT '0',
  `CONSISTENCYVERSION` bigint(20) NOT NULL,
  `CREATED` date DEFAULT NULL,
  `DEFAULTPROVIDER` tinyint(1) DEFAULT '0',
  `PASSWORD` varchar(255) DEFAULT NULL,
  `UPDATED` date DEFAULT NULL,
  `USERNAME` varchar(255) DEFAULT NULL,
  `TYPE_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_SMSPROVIDER_TYPE_ID` (`TYPE_ID`),
  CONSTRAINT `FK_SMSPROVIDER_TYPE_ID` FOREIGN KEY (`TYPE_ID`) REFERENCES `smsprovidertype` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `smsprovider`
--

LOCK TABLES `smsprovider` WRITE;
/*!40000 ALTER TABLE `smsprovider` DISABLE KEYS */;
/*!40000 ALTER TABLE `smsprovider` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `smsprovidertype`
--

DROP TABLE IF EXISTS `smsprovidertype`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `smsprovidertype` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `CONSISTENCYVERSION` bigint(20) NOT NULL,
  `CREATED` date DEFAULT NULL,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  `NAME` varchar(255) DEFAULT NULL,
  `UPDATED` date DEFAULT NULL,
  `PROVIDER_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_SMSPROVIDERTYPE_PROVIDER_ID` (`PROVIDER_ID`),
  CONSTRAINT `FK_SMSPROVIDERTYPE_PROVIDER_ID` FOREIGN KEY (`PROVIDER_ID`) REFERENCES `smsprovider` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `smsprovidertype`
--

LOCK TABLES `smsprovidertype` WRITE;
/*!40000 ALTER TABLE `smsprovidertype` DISABLE KEYS */;
/*!40000 ALTER TABLE `smsprovidertype` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tag`
--

DROP TABLE IF EXISTS `tag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tag` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `CONSISTENCYVERSION` bigint(20) NOT NULL,
  `CREATED` date DEFAULT NULL,
  `DESCRIPTION` varchar(250) DEFAULT NULL,
  `NAME` varchar(30) DEFAULT NULL,
  `UPDATED` date DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `NAME` (`NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tag`
--

LOCK TABLES `tag` WRITE;
/*!40000 ALTER TABLE `tag` DISABLE KEYS */;
/*!40000 ALTER TABLE `tag` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `transitionmember`
--

DROP TABLE IF EXISTS `transitionmember`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `transitionmember` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `CONSISTENCYVERSION` bigint(20) NOT NULL,
  `CREATED` date DEFAULT NULL,
  `EXPECTEDSTARTDATE` date DEFAULT NULL,
  `EXPEXTEDCOMPLETIONDATE` date DEFAULT NULL,
  `UPDATED` date DEFAULT NULL,
  `FROMSECTION_ID` bigint(20) DEFAULT NULL,
  `TOSECTION_ID` bigint(20) DEFAULT NULL,
  `TRANSITIONSUPERVISOR_ID` bigint(20) DEFAULT NULL,
  `YOUTHMEMBER_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_TRANSITIONMEMBER_YOUTHMEMBER_ID` (`YOUTHMEMBER_ID`),
  KEY `FK_TRANSITIONMEMBER_TOSECTION_ID` (`TOSECTION_ID`),
  KEY `FK_TRANSITIONMEMBER_FROMSECTION_ID` (`FROMSECTION_ID`),
  KEY `FK_TRANSITIONMEMBER_TRANSITIONSUPERVISOR_ID` (`TRANSITIONSUPERVISOR_ID`),
  CONSTRAINT `FK_TRANSITIONMEMBER_TRANSITIONSUPERVISOR_ID` FOREIGN KEY (`TRANSITIONSUPERVISOR_ID`) REFERENCES `contact` (`ID`),
  CONSTRAINT `FK_TRANSITIONMEMBER_FROMSECTION_ID` FOREIGN KEY (`FROMSECTION_ID`) REFERENCES `section` (`ID`),
  CONSTRAINT `FK_TRANSITIONMEMBER_TOSECTION_ID` FOREIGN KEY (`TOSECTION_ID`) REFERENCES `section` (`ID`),
  CONSTRAINT `FK_TRANSITIONMEMBER_YOUTHMEMBER_ID` FOREIGN KEY (`YOUTHMEMBER_ID`) REFERENCES `contact` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `transitionmember`
--

LOCK TABLES `transitionmember` WRITE;
/*!40000 ALTER TABLE `transitionmember` DISABLE KEYS */;
/*!40000 ALTER TABLE `transitionmember` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `CONSISTENCYVERSION` bigint(20) NOT NULL,
  `CREATED` date DEFAULT NULL,
  `DELETED` tinyint(1) DEFAULT '0',
  `ENABLED` tinyint(1) DEFAULT '0',
  `SALTEDPASSWORD` varchar(255) DEFAULT NULL,
  `UPDATED` date DEFAULT NULL,
  `USERNAME` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_role`
--

DROP TABLE IF EXISTS `user_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_role` (
  `User_ID` bigint(20) NOT NULL,
  `belongsTo_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`User_ID`,`belongsTo_ID`),
  KEY `FK_USER_ROLE_belongsTo_ID` (`belongsTo_ID`),
  CONSTRAINT `FK_USER_ROLE_belongsTo_ID` FOREIGN KEY (`belongsTo_ID`) REFERENCES `role` (`ID`),
  CONSTRAINT `FK_USER_ROLE_User_ID` FOREIGN KEY (`User_ID`) REFERENCES `user` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_role`
--

LOCK TABLES `user_role` WRITE;
/*!40000 ALTER TABLE `user_role` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'scoutmastertest'
--
SET TIME_ZONE=@OLD_TIME_ZONE ;

SET SQL_MODE=@OLD_SQL_MODE ;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS ;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS ;
SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT ;
SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS ;
SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION ;
SET SQL_NOTES=@OLD_SQL_NOTES ;

-- Dump completed on 2013-07-18 22:13:52

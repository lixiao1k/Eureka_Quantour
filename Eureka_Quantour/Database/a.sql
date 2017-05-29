-- MySQL dump 10.13  Distrib 5.7.16, for osx10.11 (x86_64)
--
-- Host: localhost    Database: quantour
-- ------------------------------------------------------
-- Server version	5.7.16

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
-- Table structure for table `comments`
--

DROP TABLE IF EXISTS `comments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `comments` (
  `strategyid` varchar(45) NOT NULL,
  `commentsid` varchar(45) NOT NULL,
  `comments` varchar(200) NOT NULL,
  `commentperson` varchar(20) NOT NULL,
  `commenttime` datetime NOT NULL,
  PRIMARY KEY (`commentsid`),
  KEY `index2` (`strategyid`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `companycaptial`
--

DROP TABLE IF EXISTS `companycaptial`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `companycaptial` (
  `code` varchar(20) NOT NULL,
  `date` date NOT NULL,
  `totalcapital` bigint(20) NOT NULL,
  `flucamital` varchar(45) NOT NULL,
  PRIMARY KEY (`code`,`date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `companyquota`
--

DROP TABLE IF EXISTS `companyquota`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `companyquota` (
  `code` varchar(20) NOT NULL,
  `date` date NOT NULL,
  `basicincome` double NOT NULL,
  `netasset` double NOT NULL,
  PRIMARY KEY (`code`,`date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `exponent`
--

DROP TABLE IF EXISTS `exponent`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `exponent` (
  `code` varchar(20) NOT NULL,
  `date` date NOT NULL,
  `open` double NOT NULL,
  `close` double NOT NULL,
  `high` double NOT NULL,
  `low` double NOT NULL,
  `lclose` double NOT NULL,
  `rate` double NOT NULL,
  `volume` bigint(30) NOT NULL,
  PRIMARY KEY (`code`,`date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `stockinfo`
--

DROP TABLE IF EXISTS `stockinfo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `stockinfo` (
  `code` varchar(20) NOT NULL,
  `name` varchar(20) NOT NULL,
  `browsetimes` bigint(20) NOT NULL,
  PRIMARY KEY (`code`,`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `stocksection`
--

DROP TABLE IF EXISTS `stocksection`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `stocksection` (
  `sectionname` varchar(30) NOT NULL,
  `setid` varchar(45) NOT NULL,
  PRIMARY KEY (`sectionname`,`setid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `stockset`
--

DROP TABLE IF EXISTS `stockset`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `stockset` (
  `setid` varchar(45) NOT NULL,
  `code` varchar(45) NOT NULL,
  PRIMARY KEY (`setid`,`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `strategy`
--

DROP TABLE IF EXISTS `strategy`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `strategy` (
  `username` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `strategyname` varchar(45) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `strategytype` varchar(45) NOT NULL,
  `pubOrpri` tinyint(4) NOT NULL,
  `parameter` varchar(200) NOT NULL,
  `purchasenum` int(11) NOT NULL,
  `tiaocangqi` int(11) NOT NULL,
  `tiaocangjiage` varchar(45) NOT NULL,
  `strategyid` varchar(45) NOT NULL,
  PRIMARY KEY (`username`,`strategyname`),
  UNIQUE KEY `strategyid_UNIQUE` (`strategyid`),
  KEY `index2` (`strategyid`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `strategychart`
--

DROP TABLE IF EXISTS `strategychart`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `strategychart` (
  `strategyid` varchar(45) NOT NULL,
  `rank` int(11) NOT NULL,
  `timelist` varchar(45) NOT NULL,
  `basicreturn` double NOT NULL,
  `strategyreturn` double NOT NULL,
  PRIMARY KEY (`strategyid`,`rank`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `strategyshow`
--

DROP TABLE IF EXISTS `strategyshow`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `strategyshow` (
  `strategyid` varchar(45) NOT NULL,
  `alpha` double NOT NULL,
  `beta` double NOT NULL,
  `sharp` double NOT NULL,
  `maxhuiche` double NOT NULL,
  `yearreturn` double NOT NULL,
  `listsize` int(11) NOT NULL,
  `basicyearreturn` double NOT NULL,
  PRIMARY KEY (`strategyid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `username` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `password` varchar(32) NOT NULL,
  `status` tinyint(4) NOT NULL,
  `userid` varchar(45) NOT NULL,
  PRIMARY KEY (`username`),
  UNIQUE KEY `userid_UNIQUE` (`userid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `userset`
--

DROP TABLE IF EXISTS `userset`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `userset` (
  `userid` varchar(45) NOT NULL,
  `setname` varchar(45) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `setid` varchar(45) NOT NULL,
  PRIMARY KEY (`userid`,`setname`),
  UNIQUE KEY `setid_UNIQUE` (`setid`),
  KEY `index3` (`setid`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-05-29 21:27:31

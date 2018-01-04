-- MySQL dump 10.13  Distrib 5.7.20, for Win64 (x86_64)
--
-- Host: localhost    Database: localdb
-- ------------------------------------------------------
-- Server version	5.7.20-log

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
-- Table structure for table `payments_log`
--

DROP TABLE IF EXISTS `payments_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `payments_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(45) NOT NULL,
  `quantity` decimal(10,2) DEFAULT NULL,
  `payment_date` datetime NOT NULL,
  `start_service_period` datetime NOT NULL,
  `end_service_period` datetime NOT NULL,
  `payed` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `payments_log`
--

LOCK TABLES `payments_log` WRITE;
/*!40000 ALTER TABLE `payments_log` DISABLE KEYS */;
INSERT INTO `payments_log` VALUES (1,'rmazzi',11.99,'2017-06-30 00:00:00','2017-06-30 00:00:00','2017-07-30 00:00:00',1),(2,'rmazzi',11.99,'2017-07-30 00:00:00','2017-07-30 00:00:00','2017-08-30 00:00:00',1),(3,'rmazzi',11.99,'2017-08-30 00:00:00','2017-08-30 00:00:00','2017-09-30 00:00:00',1),(4,'lfanciullini',11.99,'2017-09-30 00:00:00','2017-09-30 00:00:00','2017-10-30 00:00:00',1),(5,'lfanciullini',13.99,'2017-10-30 00:00:00','2017-10-30 00:00:00','2017-11-30 00:00:00',1),(6,'lfanciullini',13.99,'2017-11-30 00:00:00','2017-11-30 00:00:00','2017-12-30 00:00:00',1),(7,'rluzzi',13.99,'2017-12-30 00:00:00','2017-12-30 00:00:00','2018-01-30 00:00:00',0),(8,'rluzzi',13.99,'2018-01-30 00:00:00','2018-01-30 00:00:00','2018-03-02 00:00:00',0),(9,'rluzzi',13.99,'2018-03-02 00:00:00','2018-03-02 00:00:00','2018-04-02 00:00:00',0),(10,'msemoli',13.99,'2018-04-02 00:00:00','2018-04-02 00:00:00','2018-05-02 00:00:00',0),(11,'msemoli',13.99,'2018-05-02 00:00:00','2018-06-02 00:00:00','2018-06-02 00:00:00',0),(12,'msemoli',13.99,'2018-06-02 00:00:00','2018-07-02 00:00:00','2018-07-02 00:00:00',0);
/*!40000 ALTER TABLE `payments_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `users` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `surname` varchar(45) NOT NULL,
  `register_date` varchar(45) NOT NULL,
  `delete_date` varchar(45) DEFAULT NULL,
  `username` varchar(45) NOT NULL,
  `mail` varchar(45) DEFAULT NULL,
  `phone` varchar(45) DEFAULT NULL,
  `password` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (0,'Luca','Fanciullini','2017-01-01',NULL,'Luciferino','luca.fanciullini@gmail.com','3331330130','Passw0rd!'),(1,'Luca','Fanciullini','2017-01-01',NULL,'lfanciullini',NULL,NULL,'Passw0rd!'),(2,'Riccardo','Luzzi','2017-01-01',NULL,'rluzzi',NULL,NULL,'password'),(3,'Matteo','Semoli','2017-01-01',NULL,'msemoli',NULL,NULL,'password'),(4,'Ruben','Mazzi','2017-01-01',NULL,'rmazzi',NULL,NULL,'password');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-01-04  3:00:28

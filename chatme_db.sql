-- MySQL dump 10.13  Distrib 8.0.29, for Win64 (x86_64)
--
-- Host: localhost    Database: chatme_db_5
-- ------------------------------------------------------
-- Server version	8.0.29

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `chat`
--

DROP TABLE IF EXISTS `chat`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `chat` (
  `id` int NOT NULL AUTO_INCREMENT,
  `message` text,
  `date_time` datetime DEFAULT NULL,
  `chat_type` int DEFAULT '0',
  `chat_action` int DEFAULT '0',
  `from_user_id` int NOT NULL,
  `to_user_id` int NOT NULL,
  `chat_status_id` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_chat_user1_idx` (`from_user_id`),
  KEY `fk_chat_user2_idx` (`to_user_id`),
  KEY `fk_chat_chat_status1_idx` (`chat_status_id`),
  CONSTRAINT `fk_chat_chat_status1` FOREIGN KEY (`chat_status_id`) REFERENCES `chat_status` (`id`),
  CONSTRAINT `fk_chat_user1` FOREIGN KEY (`from_user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `fk_chat_user2` FOREIGN KEY (`to_user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `chat`
--

LOCK TABLES `chat` WRITE;
/*!40000 ALTER TABLE `chat` DISABLE KEYS */;
INSERT INTO `chat` VALUES (1,'Hi','2025-06-16 12:12:15',0,0,1,3,2),(2,'Hello','2025-06-16 12:46:50',0,0,1,5,2),(3,'1_3_f7895a8e-f85c-4538-ad5a-dc0aaecb0326.png','2025-06-16 12:47:16',1,1,1,3,2),(4,'Hi Yaluwe','2025-06-17 11:44:25',0,0,1,6,2),(5,'Hi','2025-06-17 11:45:05',0,0,2,1,1),(6,'How are you','2025-06-17 11:46:44',0,0,2,1,1),(7,'I m fine yalu','2025-06-17 11:48:27',0,1,1,2,2),(8,'Hi','2025-06-18 00:20:58',0,0,1,2,2),(9,'Hi Savindi','2025-06-19 17:34:00',0,0,4,1,1),(10,'Hello kalani','2025-06-19 17:35:34',0,0,1,7,1),(11,'1_4_5177906f-c413-4ef5-a193-53d284d38b9c.png','2025-06-19 17:36:49',1,1,1,4,2),(13,'à¶à·à·à·à¶¸à¶¯ à¶à¶ºà·à¶§ ?','2025-06-19 18:13:54',0,0,4,1,1),(14,'1_5_d41ddd61-5096-4ca1-9c18-10f99842b66e.png','2025-06-19 18:21:56',1,0,1,5,2),(15,'Hi','2025-09-28 17:16:58',0,0,1,4,2),(16,'1_4_5d58d9de-de7b-4073-8620-a90e3f24918c.png','2025-09-28 17:17:42',1,0,1,4,2),(17,'1_2_81e53a20-dbb9-4dee-96ba-f767c8239f41.png','2025-09-28 17:20:04',1,0,1,2,2),(18,'Hello Savindi','2025-09-28 17:22:32',0,0,7,1,2),(19,'à¶à¶ºà·à¶§ à¶à·à·à·à¶¸à¶¯?','2025-09-28 17:22:43',0,0,7,1,2);
/*!40000 ALTER TABLE `chat` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `chat_status`
--

DROP TABLE IF EXISTS `chat_status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `chat_status` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `chat_status`
--

LOCK TABLES `chat_status` WRITE;
/*!40000 ALTER TABLE `chat_status` DISABLE KEYS */;
INSERT INTO `chat_status` VALUES (1,'Seen'),(2,'Unseen');
/*!40000 ALTER TABLE `chat_status` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `mobile` varchar(10) DEFAULT NULL,
  `password` varchar(45) DEFAULT NULL,
  `fname` varchar(45) DEFAULT NULL,
  `lname` varchar(45) DEFAULT NULL,
  `registerd_date_time` datetime DEFAULT NULL,
  `mobile_visibility` int DEFAULT '0',
  `dp_visibility` int DEFAULT '0',
  `online_visibility` int DEFAULT '0',
  `user_status_id` int NOT NULL,
  `dp` text,
  PRIMARY KEY (`id`),
  KEY `fk_user_user_status_idx` (`user_status_id`),
  CONSTRAINT `fk_user_user_status` FOREIGN KEY (`user_status_id`) REFERENCES `user_status` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'0774464134','111','Savindi','Duleesha','2025-01-16 12:10:02',1,1,1,1,'images.png'),(2,'0785566513',NULL,'Nisanka','Perera','2025-02-19 12:10:43',0,0,0,1,'man1.png'),(3,'0778877615',NULL,'Amaya','Perera','2025-02-26 12:11:17',0,0,0,1,'girl3.png'),(4,'0785566666',NULL,'Kalani','Malshika','2025-06-16 12:11:56',0,0,0,1,'download.png'),(5,'077111111',NULL,'Shakya','Iduwari','2025-06-17 23:10:53',0,0,0,1,NULL),(6,'077000000',NULL,'Nisal','Dinusha','2025-06-19 17:30:58',0,0,0,2,'077000000_dc679e46-db9d-42a1-aadd-5c503f824b9f.png'),(7,'076777767','111','Isuru','Deshan','2025-06-19 17:55:51',0,0,0,1,'9723582.png'),(8,'076111111',NULL,'Sathsarani','Nirmika','2025-06-19 17:58:43',0,0,0,1,'2384424.png'),(9,'078117787',NULL,'Lakshan','Kawnda','2025-06-19 17:59:12',0,0,0,1,'girl4.png'),(10,'077332231',NULL,'Heshani','Wijesinhe','2025-06-19 17:59:51',0,0,0,1,NULL),(11,'077112212',NULL,'Sachini','Piyumali','2025-06-19 18:01:13',0,0,0,1,'girl1.png'),(12,'077113323',NULL,'Hiranya','Silva','2025-06-19 18:03:05',0,0,0,1,NULL),(13,'075119909',NULL,'Saheli','Mauwanthi','2025-06-19 18:03:55',0,0,0,2,NULL),(14,'075333390',NULL,'Parami','Shenali','2025-06-19 18:04:25',0,0,0,2,'matheesha.png');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_status`
--

DROP TABLE IF EXISTS `user_status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_status` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_status`
--

LOCK TABLES `user_status` WRITE;
/*!40000 ALTER TABLE `user_status` DISABLE KEYS */;
INSERT INTO `user_status` VALUES (1,'Online'),(2,'Offline');
/*!40000 ALTER TABLE `user_status` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-10-06 23:32:56

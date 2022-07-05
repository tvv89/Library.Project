CREATE DATABASE  IF NOT EXISTS `library` /*!40100 DEFAULT CHARACTER SET utf8 */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `library`;
-- MySQL dump 10.13  Distrib 8.0.28, for Win64 (x86_64)
--
-- Host: localhost    Database: library
-- ------------------------------------------------------
-- Server version	8.0.28

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
-- Table structure for table `authors`
--

DROP TABLE IF EXISTS `authors`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `authors` (
                           `id` int NOT NULL AUTO_INCREMENT,
                           `first_name` text NOT NULL,
                           `last_name` text NOT NULL,
                           PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=41 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `authors`
--

LOCK TABLES `authors` WRITE;
/*!40000 ALTER TABLE `authors` DISABLE KEYS */;
INSERT INTO `authors` VALUES (1,'Агата','Крісті'),(2,'Айн','Ренд'),(3,'Анджей','Сапковський'),(4,'Артур Конан','Дойл'),(5,'Говард Філіпс','Лавкрафт'),(6,'Джоан','Гарріс'),(7,'Джоан','Роулінг'),(8,'Джон','Толкін'),(9,'Джордж','Орвелл'),(10,'Едгар Аллан','По'),(11,'Елена','Ферранте'),(12,'Еліс','Гофман'),(13,'Ендрю','Мейн'),(14,'Ерік','Рікстед'),(15,'Еріх Марія','Ремарк'),(16,'Ернест','Гемінґвей'),(17,'Ілларіон','Павлюк'),(18,'Кейт Елізабет','Расселл'),(19,'Лі','Бардуґо'),(20,'Ліна','Костенко'),(21,'Лоран','Гунель'),(22,'Марк','Твен'),(23,'Милан','Кундера'),(24,'Оксана','Забужко'),(25,'Роальд','Дал'),(26,'Роберт','Джордан'),(27,'Саллі','Руні'),(28,'Славенка','Дракуліч'),(29,'Террі','Пратчетт'),(30,'Філіп','Брено'),(31,'Фредрік','Бакман'),(32,'Френк','Герберт'),(33,'Халед','Госсейні'),(34,'Ханна','Оренстейн'),(35,'Юрій','Андрухович'),(36,'Летісія','Корен'),(37,'Карпенко','Карий'),(38,'Люсьєн','Мархо'),(39,'Понтій','Пілат'),(40,'Понтій','Понтифік');
/*!40000 ALTER TABLE `authors` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `book_genre`
--

DROP TABLE IF EXISTS `book_genre`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `book_genre` (
                              `id` int NOT NULL AUTO_INCREMENT,
                              `book_id` int NOT NULL,
                              `genre_id` int NOT NULL,
                              PRIMARY KEY (`id`),
                              KEY `book_genre_fk1` (`genre_id`),
                              KEY `book_genre_fk0` (`book_id`),
                              CONSTRAINT `book_genre_fk0` FOREIGN KEY (`book_id`) REFERENCES `books` (`id`) ON DELETE CASCADE,
                              CONSTRAINT `book_genre_fk1` FOREIGN KEY (`genre_id`) REFERENCES `genres` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=85 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `book_genre`
--

LOCK TABLES `book_genre` WRITE;
/*!40000 ALTER TABLE `book_genre` DISABLE KEYS */;
INSERT INTO `book_genre` VALUES (1,1,1),(2,2,2),(3,3,3),(4,4,3),(5,5,1),(6,6,4),(7,7,5),(8,8,6),(9,9,6),(10,10,6),(11,11,6),(12,12,6),(13,13,6),(14,14,6),(15,15,2),(16,16,7),(17,17,1),(18,18,8),(19,19,3),(20,20,3),(21,21,3),(22,22,5),(23,23,6),(24,24,3),(25,25,3),(26,26,3),(27,27,4),(28,28,3),(29,29,3),(30,30,3),(31,31,3),(32,32,6),(33,33,3),(34,34,6),(35,35,3),(36,36,3),(37,37,9),(38,38,3),(39,39,6),(40,40,9),(41,41,3),(42,42,6),(43,43,2),(44,44,6),(45,45,6),(46,46,6),(47,47,3);
/*!40000 ALTER TABLE `book_genre` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `book_user`
--

DROP TABLE IF EXISTS `book_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `book_user` (
                             `id` int NOT NULL AUTO_INCREMENT,
                             `book_id` int NOT NULL,
                             `user_id` int NOT NULL,
                             `start_date` date DEFAULT NULL,
                             `end_date` date DEFAULT NULL,
                             `status_pay` text,
                             PRIMARY KEY (`id`),
                             KEY `book_user_fk1` (`user_id`),
                             KEY `book_user_fk0` (`book_id`),
                             CONSTRAINT `book_user_fk0` FOREIGN KEY (`book_id`) REFERENCES `books` (`id`) ON DELETE CASCADE,
                             CONSTRAINT `book_user_fk1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `book_user`
--

LOCK TABLES `book_user` WRITE;
/*!40000 ALTER TABLE `book_user` DISABLE KEYS */;
INSERT INTO `book_user` VALUES (6,15,7,'2022-06-25','2022-10-13',''),(7,2,4,'2022-06-26','2022-07-26',NULL),(8,35,4,'2022-06-26','2022-07-26',NULL),(16,26,2,NULL,NULL,''),(17,45,2,NULL,NULL,''),(18,23,2,NULL,NULL,''),(19,45,2,NULL,NULL,''),(20,21,2,NULL,NULL,''),(21,24,2,NULL,NULL,''),(22,1,2,'2022-06-30','2022-07-30',''),(23,26,12,NULL,NULL,''),(24,4,12,'2022-05-05','2022-06-29',''),(25,45,12,NULL,NULL,'');
/*!40000 ALTER TABLE `book_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `books`
--

DROP TABLE IF EXISTS `books`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `books` (
                         `id` int NOT NULL AUTO_INCREMENT,
                         `isbn` text NOT NULL,
                         `name` text NOT NULL,
                         `year` text NOT NULL,
                         `image` text,
                         `publisher_id` int NOT NULL,
                         PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=85 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `books`
--

LOCK TABLES `books` WRITE;
/*!40000 ALTER TABLE `books` DISABLE KEYS */;
INSERT INTO `books` VALUES (1,'9789662355574','1984','2015','1.jpg',1),(2,'9789667047474','Чарлі і шоколадна фабрика','2009','2.jpg',2),(3,'9789667047887','Записки українського самашедшого','2011','3.jpg',2),(4,'9786171215528','Бог завжди подорожує інкогніто','2016','4.jpg',3),(5,'9786171276895','Дюна','2021','5.jpg',3),(6,'9786171281233','Карти на стіл','2020','6.jpg',3),(7,'9786171268449','П\'ять четвертинок апельсина','2019','7.jpg',3),(8,'9789667047344','Гаррі Поттер і таємна кімната','2017','8.jpg',2),(9,'9789667047369','Гаррі Поттер i в’язень Азкабану','2017','9.jpg',2),(10,'9786175851005','Гаррі Поттер і філософський камінь','2017','10.jpg',2),(11,'9789667047407','Гаррі Поттер і кубок вогню','2017','11.jpg',2),(12,'9789667047290','Гаррі Поттер і Напівкровний принц','2017','12.jpg',2),(13,'9789667047702','Гаррі Поттер і смертельні реліквії','2017','13.jpg',2),(14,'9786175851241','Фантастичні звірі і де їх шукати','2017','14.jpg',2),(15,'9786170707086','Пригоди Тома Сойєра','2019','15.jpg',4),(16,'9786176798323','Я бачу, вас цікавить пітьма','2020','16.jpg',5),(17,'9786176642077','Володар перснів. Братство персня','2020','17.jpg',6),(18,'9786177818396','Неймовірна історія сексу. Книга 1: Захід','2021','18.jpg',7),(19,'786176799580','Ловець повітряних зміїв','2021','19.jpg',5),(20,'9789669763969','Моя бабуся просить їй вибачити','2019','20.jpg',8),(21,'9786176797012','Нестерпна легкість буття','2019','21.jpg',5),(22,'9786176798026','Нормальні люди','2020','22.jpg',5),(23,'9789669821836','Дев\'ятий Дім','2021','23.jpg',9),(24,'9786177286652','Ніби мене нема(є)','2020','24.jpg',10),(25,'9786170931429','Шлюб протилежностей','2017','25.jpg',11),(26,'9786177279166','Атлант розправив плечі. Частина 3. А є А','2019','26.jpg',12),(27,'9786177563524','Дівчата, які нічого не скажуть','2019','27.jpg',8),(28,'9789669822383','Моя темна Ванесса','2020','28.jpg',9),(29,'9786177585502','Повне зібрання прозових творів. Том І. Говард Лавкрафт','2022','29.jpg',1),(30,'9786178024055','Радіо Ніч','2021','30.jpg',13),(31,'9786176795094','По кому подзвін','2018','31.jpg',5),(32,'9786171281400','Відьмак. Меч призначення. Книга 2','2020','32.jpg',3),(33,'9786171291423','Моя неймовірна подруга. Книга 1','2021','33.jpg',3),(34,'9789661063173','Колесо Часу. Книга 1. Око Світу','2021','34.jpg',14),(35,'9786177820061','Гра в пари','2020','35.jpg',8),(36,'9786177286492','Польові дослідження з українського сексу','2019','36.jpg',10),(37,'9786177585236','Повне зібрання прозових творів. Том І. Едґар Аллан По','2020','37.jpg',1),(38,'9786177820764','Шляхи життя','2021','38.jpg',8),(39,'9786171283503','Відьмак. Кров ельфів. Книга 3','2021','39.jpg',3),(40,'9786177563791','Хованки з хижаком','2019','40.jpg',8),(41,'9786176794448','Чоловіки без жінок та інші оповідання','2017','41.jpg',5),(42,'9786176794691','Право на чари','2017','42.jpg',5),(43,'9789660104488','Пригоди Шерлока Холмса. Том I','2010','43.jpg',14),(44,'9786171291041','Відьмак. Сезон гроз. Книга 8','2021','44.jpg',3),(45,'9786171288485','Відьмак. Вежа Ластівки. Книга 6','2021','45.jpg',3),(46,'9786171288478','Відьмак. Хрещення вогнем. Книга 5','2021','46.jpg',3),(47,'9786171243149','На Західному фронті без змін','2017','47.jpg',3);
/*!40000 ALTER TABLE `books` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `books_authors`
--

DROP TABLE IF EXISTS `books_authors`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `books_authors` (
                                 `id` int NOT NULL AUTO_INCREMENT,
                                 `book_id` int NOT NULL,
                                 `author_id` int NOT NULL,
                                 PRIMARY KEY (`id`),
                                 KEY `books_authors_fk1` (`author_id`),
                                 KEY `books_authors_fk0` (`book_id`),
                                 CONSTRAINT `books_authors_fk0` FOREIGN KEY (`book_id`) REFERENCES `books` (`id`) ON DELETE CASCADE,
                                 CONSTRAINT `books_authors_fk1` FOREIGN KEY (`author_id`) REFERENCES `authors` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=86 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `books_authors`
--

LOCK TABLES `books_authors` WRITE;
/*!40000 ALTER TABLE `books_authors` DISABLE KEYS */;
INSERT INTO `books_authors` VALUES (1,1,9),(2,2,25),(3,3,20),(4,4,21),(5,5,32),(6,6,1),(7,7,6),(8,8,7),(9,9,7),(10,10,7),(11,11,7),(12,12,7),(13,13,7),(14,14,7),(15,15,22),(16,16,17),(17,17,8),(18,18,30),(19,18,36),(20,19,33),(21,20,31),(22,21,23),(23,22,27),(24,23,19),(25,24,28),(26,25,12),(27,26,2),(28,27,14),(29,28,18),(30,29,5),(31,30,35),(32,31,16),(33,32,3),(34,33,11),(35,34,26),(36,35,34),(37,36,24),(38,37,10),(39,38,31),(40,39,3),(41,40,13),(42,41,16),(43,42,29),(44,43,4),(45,44,3),(46,45,3),(47,46,3),(48,47,15);
/*!40000 ALTER TABLE `books_authors` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `current_books`
--

DROP TABLE IF EXISTS `current_books`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `current_books` (
                                 `id` int NOT NULL AUTO_INCREMENT,
                                 `book_id` int NOT NULL,
                                 `count` int NOT NULL,
                                 PRIMARY KEY (`id`),
                                 KEY `current_books_fk0` (`book_id`),
                                 CONSTRAINT `current_books_fk0` FOREIGN KEY (`book_id`) REFERENCES `books` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=85 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `current_books`
--

LOCK TABLES `current_books` WRITE;
/*!40000 ALTER TABLE `current_books` DISABLE KEYS */;
INSERT INTO `current_books` VALUES (1,1,3),(2,2,6),(3,3,2),(4,4,2),(5,5,3),(6,6,5),(7,7,4),(8,8,4),(9,9,4),(10,10,1),(11,11,4),(12,12,2),(13,13,1),(14,14,4),(15,15,5),(16,16,3),(17,17,1),(18,18,1),(19,19,3),(20,20,4),(21,21,3),(22,22,3),(23,23,2),(24,24,3),(25,25,3),(26,26,3),(27,27,2),(28,28,4),(29,29,1),(30,30,4),(31,31,5),(32,32,4),(33,33,4),(34,34,5),(35,35,3),(36,36,1),(37,37,1),(38,38,1),(39,39,2),(40,40,3),(41,41,1),(42,42,3),(43,43,5),(44,44,1),(45,45,1),(46,46,3),(47,47,2);
/*!40000 ALTER TABLE `current_books` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `genres`
--

DROP TABLE IF EXISTS `genres`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `genres` (
                          `id` int NOT NULL AUTO_INCREMENT,
                          `name` text NOT NULL,
                          PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `genres`
--

LOCK TABLES `genres` WRITE;
/*!40000 ALTER TABLE `genres` DISABLE KEYS */;
INSERT INTO `genres` VALUES (1,'Фантастика'),(2,'Пригоди'),(3,'Проза'),(4,'Детектив'),(5,'Роман'),(6,'Фентезі'),(7,'Триллер'),(8,'Комікс'),(9,'Жахи');
/*!40000 ALTER TABLE `genres` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `publishers`
--

DROP TABLE IF EXISTS `publishers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `publishers` (
                              `id` int NOT NULL AUTO_INCREMENT,
                              `name` text NOT NULL,
                              `address` text,
                              `phone` text,
                              `city` text,
                              PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `publishers`
--

LOCK TABLES `publishers` WRITE;
/*!40000 ALTER TABLE `publishers` DISABLE KEYS */;
INSERT INTO `publishers` VALUES (1,'Видавництво Жупанського','','',''),(2,'Абабагаламага','','',''),(3,'Книжковий клуб «Клуб Сімейного Дозвілля»','','',''),(4,'Знання','','',''),(5,'Видавництво Старого Лева','','',''),(6,'Астролябія','','',''),(7,'Видавництво','','',''),(8,'книголав','','',''),(9,'Vivat','','',''),(10,'Комора','','',''),(11,'Фабула','','',''),(12,'Наш Формат','','',''),(13,'Meridian Czernowitz','','',''),(14,'Навчальна книга – Богдан','','',''),(15,'Книгоклюб',NULL,NULL,NULL),(16,'Старий Рим',NULL,NULL,NULL);
/*!40000 ALTER TABLE `publishers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `roles`
--

DROP TABLE IF EXISTS `roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `roles` (
                         `id` int NOT NULL AUTO_INCREMENT,
                         `name` text NOT NULL,
                         `status` text NOT NULL,
                         PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `roles`
--

LOCK TABLES `roles` WRITE;
/*!40000 ALTER TABLE `roles` DISABLE KEYS */;
INSERT INTO `roles` VALUES (1,'admin','enabled'),(2,'librarian','enabled'),(3,'user','enabled');
/*!40000 ALTER TABLE `roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
                         `id` int NOT NULL AUTO_INCREMENT,
                         `number` text NOT NULL,
                         `password` text,
                         `first_name` text NOT NULL,
                         `last_name` text NOT NULL,
                         `date_of_birth` date NOT NULL,
                         `phone` text NOT NULL,
                         `status` text NOT NULL,
                         `photo` text,
                         `role_id` int NOT NULL,
                         `locale` text,
                         PRIMARY KEY (`id`),
                         KEY `users_fk0` (`role_id`),
                         CONSTRAINT `users_fk0` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'84562311','ffe1abd1a08215353c233d6e009613e95eec4253832a761af28ff37ac5a150c','Julia','Cross','1990-02-13','+380991234560','enabled','1.jpg',3,'en'),(2,'82834752','ffe1abd1a08215353c233d6e009613e95eec4253832a761af28ff37ac5a150c','Володимир','Тимчук','1995-05-14','+380991234561','enabled','2.jpg',3,'uk'),(3,'82635494','ffe1abd1a08215353c233d6e009613e95eec4253832a761af28ff37ac5a150c','Тарас','Бульба','1998-03-18','+380991234562','enabled','3.jpg',3,'uk'),(4,'85423997','ffe1abd1a08215353c233d6e009613e95eec4253832a761af28ff37ac5a150c','Олег','Винник','1997-03-16','+380991234563','enabled','4.jpg',3,'uk'),(5,'86235412','ffe1abd1a08215353c233d6e009613e95eec4253832a761af28ff37ac5a150c','Вікторія','Сікрет','1989-10-19','+380991234564','enabled','5.jpg',3,'uk'),(6,'84653416','ffe1abd1a08215353c233d6e009613e95eec4253832a761af28ff37ac5a150c','Аліна','Степаненко','1991-09-18','+380991234565','enabled','6.jpg',3,'uk'),(7,'81764822','ffe1abd1a08215353c233d6e009613e95eec4253832a761af28ff37ac5a150c','Володимир','Остапенко','1991-04-19','+380991234566','disabled','7.jpg',3,'uk'),(8,'89000234','ffe1abd1a08215353c233d6e009613e95eec4253832a761af28ff37ac5a150c','Олексій','Арестович','2000-11-10','+380991234567','enabled','8.jpg',3,'uk'),(9,'alan','ffe1abd1a08215353c233d6e009613e95eec4253832a761af28ff37ac5a150c','Алан','Прок','1979-02-11','+380991234568','enabled','9.jpg',2,'uk'),(10,'mery','edee29f882543b956620b26d0ee0e7e950399b1c4222f5de05e06425b4c995e9','Марія','Костенко','1985-06-14','+380991234569','enabled','10.jpg',2,'uk'),(11,'admin','8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918','Адміністратор','Системи','1989-05-18','+380991234570','enabled','11.jpg',1,'uk'),(12,'89000235','ffe1abd1a08215353c233d6e009613e95eec4253832a761af28ff37ac5a150c','Павло','Завгородній','2022-05-30','+380635919450','enabled','89000235.jpg',3,'uk'),(15,'nemo','ffe1abd1a08215353c233d6e009613e95eec4253832a761af28ff37ac5a150c','Captain','Nemo','1990-01-18','+380631111111','enabled','_blank.png',2,'uk'),(16,'89000236','ffe1abd1a08215353c233d6e009613e95eec4253832a761af28ff37ac5a150c','First','Last','2000-01-01','+380991234567','enabled','_blank.png',3,'en');
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

-- Dump completed on 2022-07-05 15:35:34

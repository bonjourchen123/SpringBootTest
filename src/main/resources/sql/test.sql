DROP TABLE IF EXISTS `test`;
CREATE TABLE `test` (
	`id` int(11) NOT NULL,
	`test`  varchar(10) DEFAULT NULL,
	`msg` varchar(255) DEFAULT NULL,
	PRIMARY KEY (`id`)
) DEFAULT CHARSET=utf8;


LOCK TABLES `test` WRITE;
INSERT INTO `test` VALUES (1, 'hello', 'Test'),(2, 'yo', 'Test2');
UNLOCK TABLES;
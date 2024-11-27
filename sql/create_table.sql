CREATE TABLE `user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(256) NOT NULL,
  `password` varchar(256) NOT NULL,
  `name` varchar(256) DEFAULT NULL,
  `avatar` varchar(1024) DEFAULT NULL,
  `profile` varchar(256) DEFAULT NULL,
  `role` varchar(256) NOT NULL,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `email` varchar(512) DEFAULT NULL,
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci

CREATE TABLE `image` (
    `id` bigint NOT NULL PRIMARY KEY AUTO_INCREMENT,
    'url' VARCHAR(1024) NOT NULL,
    PRIMARY KEY (`id`)
);
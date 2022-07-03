USE library;

CREATE TABLE `books` (
                         `id` int NOT NULL AUTO_INCREMENT,
                         `isbn` TEXT NOT NULL,
                         `name` TEXT NOT NULL,
                         `year` year NOT NULL,
                         `image` blob NOT NULL,
                         PRIMARY KEY (`id`)
);

CREATE TABLE `authors` (
                           `id` int NOT NULL AUTO_INCREMENT,
                           `first_name` TEXT NOT NULL,
                           `last_name` TEXT NOT NULL,
                           PRIMARY KEY (`id`)
);

CREATE TABLE `publishers` (
                              `id` int NOT NULL AUTO_INCREMENT,
                              `name` TEXT NOT NULL,
                              `address` TEXT NOT NULL,
                              `phone` TEXT NOT NULL,
                              `city` TEXT NOT NULL,
                              PRIMARY KEY (`id`)
);

CREATE TABLE `genres` (
                          `id` int NOT NULL AUTO_INCREMENT,
                          `name` TEXT NOT NULL,
                          PRIMARY KEY (`id`)
);

CREATE TABLE `books_authors` (
                                 `id` int NOT NULL AUTO_INCREMENT,
                                 `book_id` int NOT NULL,
                                 `author_id` int NOT NULL,
                                 PRIMARY KEY (`id`)
);

CREATE TABLE `book_publisher` (
                                  `id` int NOT NULL AUTO_INCREMENT,
                                  `book_id` int NOT NULL,
                                  `publisher_id` int NOT NULL,
                                  PRIMARY KEY (`id`)
);

CREATE TABLE `book_genre` (
                              `id` int NOT NULL AUTO_INCREMENT,
                              `book_id` int NOT NULL,
                              `genre_id` int NOT NULL,
                              PRIMARY KEY (`id`)
);

CREATE TABLE `current_books` (
                                 `id` int NOT NULL AUTO_INCREMENT,
                                 `book_id` int NOT NULL,
                                 `count` int NOT NULL,
                                 PRIMARY KEY (`id`)
);

CREATE TABLE `book_user` (
                             `id` int NOT NULL AUTO_INCREMENT,
                             `book_id` int NOT NULL,
                             `user_id` int NOT NULL,
                             `start_date` DATE NOT NULL,
                             `end_date` DATE NOT NULL,
                             `status` TEXT NOT NULL,
                             PRIMARY KEY (`id`)
);

CREATE TABLE `users` (
                         `id` int NOT NULL AUTO_INCREMENT,
                         `number` TEXT NOT NULL,
                         `password` TEXT NOT NULL,
                         `first_name` TEXT NOT NULL,
                         `last_name` TEXT NOT NULL,
                         `date_of_birth` DATE NOT NULL,
                         `phone` TEXT NOT NULL,
                         `status` TEXT NOT NULL,
                         `photo` blob NOT NULL,
                         `role_id` int NOT NULL,
                         PRIMARY KEY (`id`)
);

CREATE TABLE `roles` (
                         `id` int NOT NULL AUTO_INCREMENT,
                         `name` TEXT NOT NULL,
                         `status` TEXT NOT NULL,
                         PRIMARY KEY (`id`)
);

ALTER TABLE `books_authors` ADD CONSTRAINT `books_authors_fk0` FOREIGN KEY (`book_id`) REFERENCES `books`(`id`);

ALTER TABLE `books_authors` ADD CONSTRAINT `books_authors_fk1` FOREIGN KEY (`author_id`) REFERENCES `authors`(`id`);

ALTER TABLE `book_publisher` ADD CONSTRAINT `book_publisher_fk0` FOREIGN KEY (`book_id`) REFERENCES `books`(`id`);

ALTER TABLE `book_publisher` ADD CONSTRAINT `book_publisher_fk1` FOREIGN KEY (`publisher_id`) REFERENCES `publishers`(`id`);

ALTER TABLE `book_genre` ADD CONSTRAINT `book_genre_fk0` FOREIGN KEY (`book_id`) REFERENCES `books`(`id`);

ALTER TABLE `book_genre` ADD CONSTRAINT `book_genre_fk1` FOREIGN KEY (`genre_id`) REFERENCES `genres`(`id`);

ALTER TABLE `current_books` ADD CONSTRAINT `current_books_fk0` FOREIGN KEY (`book_id`) REFERENCES `books`(`id`);

ALTER TABLE `book_user` ADD CONSTRAINT `book_user_fk0` FOREIGN KEY (`book_id`) REFERENCES `books`(`id`);

ALTER TABLE `book_user` ADD CONSTRAINT `book_user_fk1` FOREIGN KEY (`user_id`) REFERENCES `users`(`id`);

ALTER TABLE `users` ADD CONSTRAINT `users_fk0` FOREIGN KEY (`role_id`) REFERENCES `roles`(`id`);

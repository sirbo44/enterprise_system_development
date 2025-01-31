-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1:3306
-- Generation Time: Jan 31, 2025 at 02:27 PM
-- Server version: 8.0.31
-- PHP Version: 8.0.26

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `mylibrary`
--

-- --------------------------------------------------------

--
-- Table structure for table `authors`
--

DROP TABLE IF EXISTS `authors`;
CREATE TABLE IF NOT EXISTS `authors` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `DOB` date NOT NULL,
  `Nationality` varchar(24) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `authors`
--

INSERT INTO `authors` (`id`, `name`, `DOB`, `Nationality`) VALUES
(16, 'George Orwell', '1903-06-25', 'British'),
(17, 'Haruki Murakami', '1949-01-12', 'Japanese'),
(18, 'J.K. Rowling', '1965-07-31', 'British'),
(19, 'Gabriel García Márquez', '1927-03-06', 'Colombian'),
(20, 'Toni Morrison', '1931-02-18', 'American'),
(22, 'Stephen King', '1947-09-21', 'American'),
(23, 'Isabel Allende', '1942-08-02', 'Chilean'),
(24, 'Salman Rushdie', '1947-06-19', 'British-Indian'),
(25, 'Margaret Atwood', '1939-11-18', 'Canadian'),
(26, 'Fyodor Dostoevsky', '1821-11-11', 'Russian');

-- --------------------------------------------------------

--
-- Table structure for table `books`
--

DROP TABLE IF EXISTS `books`;
CREATE TABLE IF NOT EXISTS `books` (
  `title` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `isbn` varchar(24) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `language` varchar(25) NOT NULL,
  `release` date NOT NULL,
  `summary` tinytext,
  `pieces` int NOT NULL,
  `photo` varchar(1000) DEFAULT NULL,
  PRIMARY KEY (`isbn`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `books`
--

INSERT INTO `books` (`title`, `isbn`, `language`, `release`, `summary`, `pieces`, `photo`) VALUES
('Animal Farm', ' 978-0-452-28424-1', 'English', '1945-08-17', 'A political allegory where farm animals overthrow their owner but end up replicating human corruption.', 7, ' 978-0-452-28424-1.jpg'),
('One Hundred Years of Solitude', '978-0-06-088328-7', 'Spanish', '1967-05-30', 'A multi-generational story of the Buendía family, blending magical realism and history.', 9, '978-0-06-088328-7.jpg'),
('Homage to Catalonia', '978-0-15-642117-1', 'English', '1938-04-25', 'A memoir about Orwell\'s experiences in the Spanish Civil War.', 5, '978-0-15-642117-1.jpg'),
('Love in the Time of Cholera', '978-0-307-38986-2', 'Spanish', '1985-09-05', 'A love story spanning decades, filled with passion and patience.', 7, '978-0-307-38986-2.jpg'),
('1Q84', '978-0-307-59331-3', 'Japanese', '2009-05-29', 'A complex narrative involving parallel worlds, cults, and a love story.', 6, '978-0-307-59331-3.jpg'),
('Norwegian Wood', '978-0-375-70402-4', 'Japanese', '1987-09-04', 'A nostalgic love story set in 1960s Tokyo.', 10, '978-0-375-70402-4.jpg'),
('1984', '978-0-452-28423-4', 'English', '1949-06-08', 'A dystopian novel depicting a totalitarian state under constant surveillance.', 12, '978-0-452-28423-4.jpg'),
('It', '978-0-670-81302-5', 'English', '1986-09-15', 'A group of friends confront an evil entity that preys on children.', 9, '978-0-670-81302-5.jpg'),
('Harry Potter and the Philosopher\'s Stone', '978-0-7475-3269-9', 'English', '1997-06-26', 'The first book in the Harry Potter series, introducing Hogwarts and magic.', 15, '978-0-7475-3269-9.jpg'),
('Harry Potter and the Chamber of Secrets', '978-0-7475-3849-3', 'English', '1998-07-02', 'Harry returns to Hogwarts and discovers a hidden chamber containing a monster.', 14, '978-0-7475-3849-3.jpg'),
('Harry Potter and the Prisoner of Azkaban', '978-0-7475-4215-5', 'English', '1999-07-08', 'Harry learns about his godfather, Sirius Black, who escapes from Azkaban.', 13, '978-0-7475-4215-5.jpg'),
('Crime and Punishment', '978-0140449136', 'English', '1866-01-01', 'Crime and Punishment by Fyodor Dostoevsky follows Rodion Raskolnikov, a young man who commits murder and grapples with guilt, morality, and the psychological consequences of his crime while seeking redemption.', 10, '978-0140449136.jpg'),
('The Handmaid’s Tale', '978-0385490818', 'English', '1985-04-17', 'Is a dystopian novel set in the totalitarian society of Gilead, where women are subjugated and forced into roles like handmaids to bear children for the elite, exploring themes of power, gender, and resistance', 12, '978-0385490818.jpg'),
('Midnight’s Children', '978-0812976533', 'English', '1981-09-15', 'Follows the life of Saleem Sinai, born at the exact moment of India’s independence, whose fate is intertwined with the country’s history and the magical powers of the 1001 children born in the first hour of freedom.', 14, '978-0812976533.jpg'),
('Beloved', '978-1-4000-3341-8', 'English', '1987-09-16', 'A ghostly presence haunts a former slave, symbolizing past traumas.', 10, '978-1-4000-3341-8.jpg'),
('Song of Solomon', '978-1-4000-3342-5', 'English', '1977-09-15', 'A young Black man\'s journey to uncover his roots.', 4, '978-1-4000-3342-5.jpg'),
('Chronicle of a Death Foretold', '978-1-4000-3471-2', 'Spanish', '1981-04-15', 'A novel reconstructing a murder in a small Colombian town.', 6, '978-1-4000-3471-2.jpg'),
('Kafka on the Shore', '978-1-4000-7927-0', 'Japanese', '2002-09-12', 'A surreal tale blending magical realism, fate, and self-discovery.', 7, '978-1-4000-7927-0.jpg'),
('The House of the Spirits', '978-1501116984', 'English', '1985-01-01', 'The House of the Spirits by Isabel Allende is a sweeping family saga that blends magical realism with historical events, following the lives of the Trueba family across generations amidst the political and social turmoil of 20th-century Chile.', 13, '978-1501116984.jpg');

-- --------------------------------------------------------

--
-- Table structure for table `jk_books_authors`
--

DROP TABLE IF EXISTS `jk_books_authors`;
CREATE TABLE IF NOT EXISTS `jk_books_authors` (
  `id` int NOT NULL AUTO_INCREMENT,
  `author_id` int NOT NULL,
  `book_code` varchar(24) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`id`),
  KEY `id` (`author_id`) USING BTREE,
  KEY `code` (`book_code`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=56 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `jk_books_authors`
--

INSERT INTO `jk_books_authors` (`id`, `author_id`, `book_code`) VALUES
(36, 16, '978-0-452-28423-4'),
(37, 16, ' 978-0-452-28424-1'),
(38, 16, '978-0-15-642117-1'),
(39, 17, '978-0-375-70402-4'),
(40, 17, '978-1-4000-7927-0'),
(41, 17, '978-0-307-59331-3'),
(42, 18, '978-0-7475-3269-9'),
(43, 18, '978-0-7475-3849-3'),
(44, 18, '978-0-7475-4215-5'),
(45, 19, '978-0-06-088328-7'),
(46, 19, '978-0-307-38986-2'),
(47, 19, '978-1-4000-3471-2'),
(48, 20, '978-1-4000-3341-8'),
(49, 20, '978-1-4000-3342-5'),
(50, 22, '978-0-670-81302-5'),
(51, 23, '978-1501116984'),
(53, 25, '978-0385490818'),
(54, 24, '978-0812976533'),
(55, 26, '978-0140449136');

-- --------------------------------------------------------

--
-- Table structure for table `jk_books_users`
--

DROP TABLE IF EXISTS `jk_books_users`;
CREATE TABLE IF NOT EXISTS `jk_books_users` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(24) NOT NULL,
  `isbn` varchar(24) NOT NULL,
  `check_in` date NOT NULL DEFAULT '1970-01-01',
  `check_out` date NOT NULL DEFAULT '1970-01-01',
  PRIMARY KEY (`id`),
  KEY `username` (`username`,`isbn`),
  KEY `isbn` (`isbn`)
) ENGINE=InnoDB AUTO_INCREMENT=97 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `jk_books_users`
--

INSERT INTO `jk_books_users` (`id`, `username`, `isbn`, `check_in`, `check_out`) VALUES
(96, 'user', ' 978-0-452-28424-1', '2025-01-31', '2025-02-07');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
CREATE TABLE IF NOT EXISTS `users` (
  `username` varchar(24) NOT NULL,
  `password` varchar(24) NOT NULL,
  `role` varchar(20) NOT NULL DEFAULT 'member',
  PRIMARY KEY (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`username`, `password`, `role`) VALUES
('admin', 'admin', 'administrator'),
('librarian', 'librarian', 'librarian'),
('user', 'user', 'member');

--
-- Constraints for dumped tables
--

--
-- Constraints for table `jk_books_authors`
--
ALTER TABLE `jk_books_authors`
  ADD CONSTRAINT `books` FOREIGN KEY (`book_code`) REFERENCES `books` (`isbn`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `id` FOREIGN KEY (`author_id`) REFERENCES `authors` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `jk_books_users`
--
ALTER TABLE `jk_books_users`
  ADD CONSTRAINT `isbn` FOREIGN KEY (`isbn`) REFERENCES `books` (`isbn`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `username` FOREIGN KEY (`username`) REFERENCES `users` (`username`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

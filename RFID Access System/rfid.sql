-- phpMyAdmin SQL Dump
-- version 5.1.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jun 07, 2022 at 11:32 AM
-- Server version: 10.4.22-MariaDB
-- PHP Version: 8.1.2

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `rfid`
--

-- --------------------------------------------------------

--
-- Table structure for table `admin`
--

CREATE TABLE `admin` (
  `AdminID` int(11) NOT NULL COMMENT 'Identifikacijski broj admina',
  `username` varchar(100) NOT NULL COMMENT 'korisničko ime admina',
  `password` varchar(100) NOT NULL COMMENT 'HASHirana i SALTana lozinka admina'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `admin`
--

INSERT INTO `admin` (`AdminID`, `username`, `password`) VALUES
(1, 'luka', '$2y$12$4rJrvepyp8bKMX5fZHnBi.KdP6GPsWEI2JrUilKIETWJDCuvmR13W');

-- --------------------------------------------------------

--
-- Table structure for table `osobe`
--

CREATE TABLE `osobe` (
  `IB` int(11) NOT NULL COMMENT 'Identifikacijski broj osobe',
  `ID` varchar(256) NOT NULL COMMENT 'HASHirani i SALTani identifikacijski broj kartice',
  `Ime` varchar(100) NOT NULL COMMENT 'Ime osobe',
  `Prezime` varchar(100) NOT NULL COMMENT 'Prezime osobe',
  `Salt` varchar(256) NOT NULL COMMENT 'Salt',
  `Email` varchar(128) NOT NULL COMMENT 'Email osobe'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `osobe`
--

INSERT INTO `osobe` (`IB`, `ID`, `Ime`, `Prezime`, `Salt`, `Email`) VALUES
(1, 'C6D2D124099111E5B8DB179F8167CB949A7096E123015F7A12708B221A04EF05DEE6E5E25B1298017D9FDD55A09FACDACE3A5F518D0A8B6C60A454C496A30C84', 'Luka', 'Šarić', '+HeVaTnLzjoTqIXFJKUBful88MEi73DV', 'saricluka76@gmail.com'),
(2, 'A6D6DF82D482553F6CC7054A216A74213BC93DE08DE6610FC0818E67CD46F016C8B745FAE96472426BC71381B760714043479F05E05036079691374F0B7D7632', 'Ivo', 'Ivić', '0it8yZXVKhQ69x3D3d3lUkTJt7QC1q6U', 'ivicivo@gmail.com'),
(7, '7E0019C57E608FE1CC5C7A03B4CA76F5FCB887A076D357A81856DEEA74F071B65A867DF4D57210987DA2925C0C89F8E94E9B468B92E9670597D3B9963ECA97B6', 'Pero', 'Perić', 'RK4WZApx7OqmiB4O+4DbpC/2iAywz3V6', 'peroperic@gmail.com');

-- --------------------------------------------------------

--
-- Table structure for table `zapisnik`
--

CREATE TABLE `zapisnik` (
  `ZapisID` int(11) NOT NULL COMMENT 'Identifikacijski broj zapisa',
  `Vrijeme` varchar(128) NOT NULL COMMENT 'Vrijeme korištenja RFID',
  `IB` int(11) NOT NULL COMMENT 'Identifikacijski broj osobe'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `zapisnik`
--

INSERT INTO `zapisnik` (`ZapisID`, `Vrijeme`, `IB`) VALUES
(1, '4/15/2022 10:31:24 PM', 1),
(2, '4/15/2022 10:31:42 PM', 1),
(3, '4/15/2022 10:31:56 PM', 2);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `admin`
--
ALTER TABLE `admin`
  ADD PRIMARY KEY (`AdminID`);

--
-- Indexes for table `osobe`
--
ALTER TABLE `osobe`
  ADD PRIMARY KEY (`IB`);

--
-- Indexes for table `zapisnik`
--
ALTER TABLE `zapisnik`
  ADD PRIMARY KEY (`ZapisID`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `admin`
--
ALTER TABLE `admin`
  MODIFY `AdminID` int(11) NOT NULL AUTO_INCREMENT COMMENT 'Identifikacijski broj admina', AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `osobe`
--
ALTER TABLE `osobe`
  MODIFY `IB` int(11) NOT NULL AUTO_INCREMENT COMMENT 'Identifikacijski broj osobe', AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT for table `zapisnik`
--
ALTER TABLE `zapisnik`
  MODIFY `ZapisID` int(11) NOT NULL AUTO_INCREMENT COMMENT 'Identifikacijski broj zapisa', AUTO_INCREMENT=4;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

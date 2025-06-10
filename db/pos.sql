-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jun 10, 2025 at 09:29 AM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `pos`
--

-- --------------------------------------------------------

--
-- Table structure for table `product`
--

CREATE TABLE `product` (
  `ProductId` int(11) NOT NULL,
  `Name` varchar(40) NOT NULL,
  `StockQuantity` int(4) NOT NULL,
  `Category` enum('Pharmacy','Food','Hygiene','') NOT NULL,
  `Image` mediumblob DEFAULT NULL,
  `Price` float NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `sale`
--

CREATE TABLE `sale` (
  `SaleId` int(11) NOT NULL,
  `UserId` int(11) NOT NULL,
  `CreatedAt` datetime NOT NULL,
  `TotalPrice` float NOT NULL,
  `PaymentMethod` enum('Cash','Credit') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `sale`
--

INSERT INTO `sale` (`SaleId`, `UserId`, `CreatedAt`, `TotalPrice`, `PaymentMethod`) VALUES
(1, 2, '2025-06-05 10:00:00', 8.48, 'Cash'),
(2, 1, '2025-06-05 11:00:00', 7.98, 'Credit');

-- --------------------------------------------------------

--
-- Table structure for table `saleproduct`
--

CREATE TABLE `saleproduct` (
  `SaleId` int(11) NOT NULL,
  `ProductId` int(11) NOT NULL,
  `Quantity` int(4) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE `user` (
  `UserId` int(11) NOT NULL,
  `Name` varchar(40) NOT NULL,
  `Role` enum('Manager','Cashier') NOT NULL,
  `Password` varchar(16) NOT NULL,
  `Username` varchar(16) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`UserId`, `Name`, `Role`, `Password`, `Username`) VALUES
(1, 'Mohamad Al-tamari', 'Manager', 'admin', 'admin'),
(2, 'Bilal Manager', 'Manager', 'user', 'user');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `product`
--
ALTER TABLE `product`
  ADD PRIMARY KEY (`ProductId`);

--
-- Indexes for table `sale`
--
ALTER TABLE `sale`
  ADD PRIMARY KEY (`SaleId`),
  ADD KEY `UserId` (`UserId`);

--
-- Indexes for table `saleproduct`
--
ALTER TABLE `saleproduct`
  ADD KEY `ProductId` (`ProductId`),
  ADD KEY `SaleId` (`SaleId`);

--
-- Indexes for table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`UserId`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `sale`
--
ALTER TABLE `sale`
  MODIFY `SaleId` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `user`
--
ALTER TABLE `user`
  MODIFY `UserId` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `sale`
--
ALTER TABLE `sale`
  ADD CONSTRAINT `sale_ibfk_1` FOREIGN KEY (`UserId`) REFERENCES `user` (`UserId`);

--
-- Constraints for table `saleproduct`
--
ALTER TABLE `saleproduct`
  ADD CONSTRAINT `saleproduct_ibfk_1` FOREIGN KEY (`ProductId`) REFERENCES `product` (`ProductId`),
  ADD CONSTRAINT `saleproduct_ibfk_2` FOREIGN KEY (`SaleId`) REFERENCES `sale` (`SaleId`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

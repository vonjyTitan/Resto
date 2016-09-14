-- phpMyAdmin SQL Dump
-- version 3.5.1
-- http://www.phpmyadmin.net
--
-- Client: localhost
-- Généré le: Mer 14 Septembre 2016 à 05:46
-- Version du serveur: 5.5.24-log
-- Version de PHP: 5.3.13

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Base de données: `resto`
--

-- --------------------------------------------------------

--
-- Structure de la table `role`
--

CREATE TABLE IF NOT EXISTS `role` (
  `idrole` int(11) NOT NULL,
  `libelle` varchar(50) NOT NULL,
  `description` varchar(100) NOT NULL,
  PRIMARY KEY (`idrole`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Contenu de la table `role`
--

INSERT INTO `role` (`idrole`, `libelle`, `description`) VALUES
(1, 'Caissier', 'utilisateur permanant de l''application');

-- --------------------------------------------------------

--
-- Structure de la table `roleactivite`
--

CREATE TABLE IF NOT EXISTS `roleactivite` (
  `idrole` int(11) NOT NULL,
  `activite` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Contenu de la table `roleactivite`
--

INSERT INTO `roleactivite` (`idrole`, `activite`) VALUES
(1, 'stat'),
(1, 'menu/categorie-saisie');

-- --------------------------------------------------------

--
-- Structure de la table `utilisateur`
--

CREATE TABLE IF NOT EXISTS `utilisateur` (
  `idutilisateur` int(11) NOT NULL,
  `nom` varchar(50) NOT NULL,
  `prenom` varchar(50) NOT NULL,
  `login` varchar(50) NOT NULL,
  `passe` varchar(50) NOT NULL,
  `idrole` int(11) NOT NULL,
  `active` int(11) NOT NULL,
  PRIMARY KEY (`idutilisateur`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Contenu de la table `utilisateur`
--

INSERT INTO `utilisateur` (`idutilisateur`, `nom`, `prenom`, `login`, `passe`, `idrole`, `active`) VALUES
(1, 'vonjy', 'vonjy', 'vonjy', 'vonjy', 1, 1);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

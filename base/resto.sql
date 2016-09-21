-- phpMyAdmin SQL Dump
-- version 3.5.1
-- http://www.phpmyadmin.net
--
-- Client: localhost
-- Généré le: Mar 20 Septembre 2016 à 20:14
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
(1, 'Super admin', 'super administrateur'),
(2, 'Caissier', 'utilisateur permanant de l''application');

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
(1, 'menu/categorie-saisie'),
(1, 'configuration/liste-utilisateur'),
(2, 'stat'),
(2, 'menu/categorie-saisie'),
(1, 'configuration/utilisateur-modif'),
(1, 'configuration/utilisateur-ajout'),
(1, 'configuration/utilisateur-fiche'),
(1, 'login-ajoutUtilisateur'),
(1, 'login-modif'),
(1, 'login-active'),
(1, 'login-desactive'),
(1, 'configuration/table-gestion'),
(2, 'configuration/table-gestion'),
(1, 'table-modifplace'),
(2, 'table-modifplace'),
(1, 'configuration/table-modif'),
(2, 'configuration/table-modif'),
(1, 'table-modif'),
(2, 'table-modif'),
(1, 'configuration/table-ajout'),
(2, 'configuration/table-ajout'),
(1, 'table-ajout'),
(2, 'table-ajout');

-- --------------------------------------------------------

--
-- Structure de la table `table_liste`
--

CREATE TABLE IF NOT EXISTS `table_liste` (
  `idtable` int(11) NOT NULL AUTO_INCREMENT,
  `nom` varchar(50) NOT NULL,
  `positionx` int(11) NOT NULL,
  `positiony` int(11) NOT NULL,
  `etat` int(11) NOT NULL,
  `place` int(11) NOT NULL,
  PRIMARY KEY (`idtable`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=5 ;

--
-- Contenu de la table `table_liste`
--

INSERT INTO `table_liste` (`idtable`, `nom`, `positionx`, `positiony`, `etat`, `place`) VALUES
(1, 'T1', 309, 46, 1, 4),
(2, 'T2', 50, 27, 2, 4),
(3, 'T3', 46, 132, 3, 6),
(4, 'T4', 269, 141, 4, 4);

-- --------------------------------------------------------

--
-- Structure de la table `utilisateur`
--

CREATE TABLE IF NOT EXISTS `utilisateur` (
  `idutilisateur` int(11) NOT NULL AUTO_INCREMENT,
  `nom` varchar(50) NOT NULL,
  `prenom` varchar(50) NOT NULL,
  `login` varchar(50) NOT NULL,
  `passe` varchar(50) NOT NULL,
  `idrole` int(11) NOT NULL,
  `active` int(11) NOT NULL,
  PRIMARY KEY (`idutilisateur`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=5 ;

--
-- Contenu de la table `utilisateur`
--

INSERT INTO `utilisateur` (`idutilisateur`, `nom`, `prenom`, `login`, `passe`, `idrole`, `active`) VALUES
(1, 'vonjy', 'vonjy', 'vonjy', 'nw6KzVSMEfayNp8BpNH7lQ==', 1, 1),
(2, 'test', 'test', 'test', '5RV2UXq8iOlROXgqefVjrg==', 2, 1),
(3, 'johan', 'johan', 'johan', 'wYwDOV7IT8/0SUmB4m4Rfg==', 1, 1),
(4, 'rova', 'rova', 'rova', 'xjbdZIpNi+IZQaI14vsWEQ==', 1, 1);

-- --------------------------------------------------------

--
-- Doublure de structure pour la vue `utilisateur_libelle`
--
CREATE TABLE IF NOT EXISTS `utilisateur_libelle` (
`idutilisateur` int(11)
,`nom` varchar(50)
,`prenom` varchar(50)
,`login` varchar(50)
,`passe` varchar(50)
,`idrole` int(11)
,`active` int(11)
,`role` varchar(50)
);
-- --------------------------------------------------------

--
-- Structure de la vue `utilisateur_libelle`
--
DROP TABLE IF EXISTS `utilisateur_libelle`;

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `utilisateur_libelle` AS select `utilisateur`.`idutilisateur` AS `idutilisateur`,`utilisateur`.`nom` AS `nom`,`utilisateur`.`prenom` AS `prenom`,`utilisateur`.`login` AS `login`,`utilisateur`.`passe` AS `passe`,`utilisateur`.`idrole` AS `idrole`,`utilisateur`.`active` AS `active`,`role`.`libelle` AS `role` from (`utilisateur` join `role` on((`role`.`idrole` = `utilisateur`.`idrole`)));

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

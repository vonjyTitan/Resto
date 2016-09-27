-- phpMyAdmin SQL Dump
-- version 3.5.1
-- http://www.phpmyadmin.net
--
-- Client: localhost
-- Généré le: Lun 26 Septembre 2016 à 18:26
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
-- Structure de la table `article_stock`
--

CREATE TABLE IF NOT EXISTS `article_stock` (
  `idarticle` int(11) NOT NULL AUTO_INCREMENT,
  `libelle` varchar(100) NOT NULL,
  `description` varchar(100) DEFAULT NULL,
  `idcategorie` int(11) NOT NULL,
  `quantite` int(11) NOT NULL,
  `idunite` int(11) NOT NULL,
  PRIMARY KEY (`idarticle`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=3 ;

--
-- Contenu de la table `article_stock`
--

INSERT INTO `article_stock` (`idarticle`, `libelle`, `description`, `idcategorie`, `quantite`, `idunite`) VALUES
(2, 'Poulet de chaire', '', 1, 0, 1);

-- --------------------------------------------------------

--
-- Structure de la table `categorie_article`
--

CREATE TABLE IF NOT EXISTS `categorie_article` (
  `idcategorie` int(11) NOT NULL AUTO_INCREMENT,
  `libelle` varchar(100) NOT NULL,
  `description` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`idcategorie`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=7 ;

--
-- Contenu de la table `categorie_article`
--

INSERT INTO `categorie_article` (`idcategorie`, `libelle`, `description`) VALUES
(1, 'Viande', ''),
(2, 'Boisson', NULL),
(3, 'Legume', NULL),
(4, 'Epice', NULL);

-- --------------------------------------------------------

--
-- Structure de la table `menu`
--

CREATE TABLE IF NOT EXISTS `menu` (
  `idmenu` int(11) NOT NULL AUTO_INCREMENT,
  `libelle` varchar(100) NOT NULL,
  `description` varchar(150) NOT NULL,
  `prix` double NOT NULL,
  `idfamille` int(11) NOT NULL,
  PRIMARY KEY (`idmenu`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Structure de la table `menu_famille`
--

CREATE TABLE IF NOT EXISTS `menu_famille` (
  `idfamille` int(11) NOT NULL AUTO_INCREMENT,
  `libelle` varchar(100) NOT NULL,
  `description` varchar(150) DEFAULT NULL,
  PRIMARY KEY (`idfamille`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=5 ;

--
-- Contenu de la table `menu_famille`
--

INSERT INTO `menu_famille` (`idfamille`, `libelle`, `description`) VALUES
(1, 'Soupe', ''),
(2, 'Dessert', ''),
(3, 'Entrée', ''),
(4, 'Snack', '');

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
(2, 'table-ajout'),
(1, 'table-occuper'),
(2, 'table-occuper'),
(1, 'table-liberer'),
(2, 'table-liberer'),
(1, 'table-transferer'),
(2, 'table-transferer'),
(1, 'configuration/famille-liste'),
(2, 'configuration/famille-liste'),
(1, 'configuration/famille-ajout'),
(2, 'configuration/famille-ajout'),
(1, 'configuration/famille-modif'),
(2, 'configuration/famille-modif'),
(1, 'crud-insert'),
(2, 'crud-insert'),
(1, 'crud-update'),
(2, 'crud-update'),
(1, 'crud-delete'),
(2, 'crud-delete'),
(1, 'configuration/categorie-liste'),
(2, 'configuration/categorie-liste'),
(1, 'configuration/categorie-ajout'),
(2, 'configuration/categorie-ajout'),
(1, 'configuration/categorie-modif'),
(2, 'configuration/categorie-modif'),
(1, 'configuration/unite-liste'),
(2, 'configuration/unite-liste'),
(1, 'configuration/unite-ajout'),
(2, 'configuration/unite-ajout'),
(1, 'configuration/unite-modif'),
(2, 'configuration/unite-modif'),
(1, 'configuration/article-ajout'),
(2, 'configuration/article-ajout'),
(1, 'configuration/article-modif'),
(2, 'configuration/article-modif'),
(1, 'configuration/article-fiche'),
(2, 'configuration/article-fiche'),
(1, 'configuration/article-liste'),
(2, 'configuration/article-liste');

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
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=7 ;

--
-- Contenu de la table `table_liste`
--

INSERT INTO `table_liste` (`idtable`, `nom`, `positionx`, `positiony`, `etat`, `place`) VALUES
(1, 'T1', 385, 203, 1, 4),
(2, 'T2', 303, 94, 2, 4),
(3, 'T3', 97, 205, 3, 6),
(4, 'T4', 472, 186, 4, 4),
(6, 'T5', 216, 189, 1, 5);

-- --------------------------------------------------------

--
-- Structure de la table `unite_article`
--

CREATE TABLE IF NOT EXISTS `unite_article` (
  `idunite` int(11) NOT NULL AUTO_INCREMENT,
  `libelle` varchar(100) NOT NULL,
  `description` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`idunite`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=7 ;

--
-- Contenu de la table `unite_article`
--

INSERT INTO `unite_article` (`idunite`, `libelle`, `description`) VALUES
(1, 'Kg', 'Kilo gramme'),
(2, 'Litre', 'Litre'),
(3, 'Gramme', NULL),
(4, 'Piece', NULL);

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
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=6 ;

--
-- Contenu de la table `utilisateur`
--

INSERT INTO `utilisateur` (`idutilisateur`, `nom`, `prenom`, `login`, `passe`, `idrole`, `active`) VALUES
(1, 'vonjy', 'vonjy', 'vonjy', 'nw6KzVSMEfayNp8BpNH7lQ==', 1, 1),
(2, 'test', 'test', 'test', '5RV2UXq8iOlROXgqefVjrg==', 2, 1),
(3, 'johan', 'johan', 'johan', 'wYwDOV7IT8/0SUmB4m4Rfg==', 1, 1),
(4, 'rova', 'rova', 'rova', 'xjbdZIpNi+IZQaI14vsWEQ==', 1, 1),
(5, 'test1', 'test1', 'test1', '0nWOk6N5sIhe27h+zm4QCg==', 1, 2);

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

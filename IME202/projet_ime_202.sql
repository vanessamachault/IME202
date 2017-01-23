
-- phpMyAdmin SQL Dump
-- version 4.5.5.1
-- http://www.phpmyadmin.net
--
-- Client :  127.0.0.1
-- Généré le :  Mer 14 Décembre 2016 à 11:27
-- Version du serveur :  5.7.11
-- Version de PHP :  5.6.19

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données :  `projet_ime_202`
--

-- --------------------------------------------------------

--
-- Structure de la table `adicap`
--

CREATE TABLE `adicap` ( `NumAutoAdicap` int(11) NOT NULL, `NumAdicapPrel` int(11) NOT NULL, `Adicap` varchar(11) NOT NULL, `Flag_Integration` tinyint(1) NOT NULL DEFAULT '0', `Flag_Agregation` tinyint(1) NOT NULL DEFAULT '0') ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Structure de la table `adicap_prelevement`
--

CREATE TABLE `adicap_prelevement` ( `NumAutoAdicapPrel` int(11) NOT NULL, `NumPatient` int(11) NOT NULL, `Sexe` tinyint(1) NOT NULL, `DDN_Jour` tinyint(2) NOT NULL, `DDN_Mois` tinyint(2) NOT NULL, `DDN_Annee` smallint(4) NOT NULL, `Prenom` varchar(100) NOT NULL, `Nom` varchar(100) NOT NULL, `DatePrel_Jour` tinyint(2) NOT NULL, `DatePrel_Mois` tinyint(2) NOT NULL, `DatePrel_Annee` smallint(4) NOT NULL, `HashCode` int(11) NOT NULL, `Flag_Identification` tinyint(1) NOT NULL DEFAULT '0', `Flag_Integration` tinyint(1) NOT NULL DEFAULT '0', `Flag_Agregation` tinyint(1) NOT NULL DEFAULT '0') ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Structure de la table `das`
--

CREATE TABLE `das` ( `NumAutoDAS` int(11) NOT NULL, `NumSejour` int(11) NOT NULL, `DAS` varchar(10) NOT NULL, `HashCode` int(11) NOT NULL, `Flag_Integration_DAS` tinyint(1) NOT NULL DEFAULT '0', `Flag_Agregation` tinyint(1) NOT NULL DEFAULT '0') ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Structure de la table `rel_adicap_cimo3`
--

CREATE TABLE `rel_adicap_cimo3` ( `NumAutoCIMO3` int(11) NOT NULL, `NumLigneADICAP` int(11) NOT NULL, `CIMO3_Topo` int(11) DEFAULT NULL, `CIMO3_Morpho` int(11) DEFAULT NULL) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Structure de la table `rel_das_cimo3`
--

CREATE TABLE `rel_das_cimo3` ( `NumAutoCIMO3` int(11) NOT NULL, `NumLigneDAS` int(11) NOT NULL, `CIMO3_Correspondance` int(11) DEFAULT NULL) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Structure de la table `rel_dp_cimo3`
--

CREATE TABLE `rel_dp_cimo3` ( `NumAutoCIMO3` int(11) NOT NULL, `NumLigneDP` int(11) NOT NULL, `CIMO3_Correspondance` int(11) DEFAULT NULL) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Structure de la table `rel_dr_cimo3`
--

CREATE TABLE `rel_dr_cimo3` ( `NumAutoCIMO3` int(11) NOT NULL, `NumLigneDR` int(11) NOT NULL, `CIMO3_Correspondance` int(11) DEFAULT NULL) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Structure de la table `retrotranscodage_cui_cim10`
--

CREATE TABLE `retrotranscodage_cui_cim10` ( `NumAuto` int(11) NOT NULL, `CUI` varchar(8) NOT NULL, `CIM10` varchar(11) NOT NULL) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Structure de la table `sejour`
--

CREATE TABLE `sejour` ( `NumAutoSejour` int(11) NOT NULL, `NumPatient` int(6) NOT NULL, `Sexe` tinyint(1) NOT NULL, `DDN_Jour` tinyint(2) NOT NULL, `DDN_Mois` tinyint(2) NOT NULL, `DDN_Annee` smallint(4) NOT NULL, `Prenom` varchar(100) NOT NULL, `Nom` varchar(100) NOT NULL, `EDH_Jour` tinyint(2) NOT NULL, `EDH_Mois` tinyint(2) NOT NULL, `EDH_Annee` smallint(4) NOT NULL, `SDH_Jour` tinyint(2) NOT NULL, `SDH_Mois` tinyint(2) NOT NULL, `SDH_Annee` smallint(4) NOT NULL, `ED_Jour` tinyint(2) NOT NULL, `ED_Mois` tinyint(2) NOT NULL, `ED_Annee` smallint(4) NOT NULL, `SD_Jour` tinyint(2) NOT NULL, `SD_Mois` tinyint(2) NOT NULL, `SD_Annee` smallint(4) NOT NULL, `DP` varchar(10) NOT NULL, `DR` varchar(10) NOT NULL, `NumSejour` int(11) NOT NULL, `HashCode` int(11) NOT NULL, `Flag_Identification` tinyint(1) NOT NULL DEFAULT '0', `Flag_Integration_DP` tinyint(1) NOT NULL DEFAULT '0', `Flag_Integration_DR` tinyint(1) NOT NULL DEFAULT '0', `Flag_Agregation` tinyint(1) NOT NULL DEFAULT '0') ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Structure de la table `transcodage_adicap_cimo3_morpho_valides`
--

CREATE TABLE `transcodage_adicap_cimo3_morpho_valides` ( `NumAuto` int(11) NOT NULL, `Lesion` varchar(4) NOT NULL, `CIMO3_Morpho` varchar(9) NOT NULL) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Structure de la table `transcodage_adicap_cimo3_topo_valides`
--

CREATE TABLE `transcodage_adicap_cimo3_topo_valides` ( `NumAuto` int(11) NOT NULL, `Organe` varchar(2) NOT NULL, `CIMO3_Topo` varchar(5) DEFAULT NULL) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Structure de la table `transcodage_cim10_cimo3_valides`
--

CREATE TABLE `transcodage_cim10_cimo3_valides` ( `NumAuto` int(11) NOT NULL, `CUI` varchar(8) NOT NULL, `CIM10` varchar(5) NOT NULL, `CIMO3_Topo` varchar(5) NOT NULL, `CIMO3_Morpho` varchar(9) NOT NULL) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Structure de la table `transcodage_cim10_cui_umls`
--

CREATE TABLE `transcodage_cim10_cui_umls` ( `NumAuto` int(11) NOT NULL, `CIM10` varchar(5) NOT NULL, `CUI` varchar(8) DEFAULT NULL) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Structure de la table `transcodage_vianey_cui_cimo3`
--

CREATE TABLE `transcodage_vianey_cui_cimo3` ( `NumAuto` int(11) NOT NULL, `CUI` varchar(8) NOT NULL, `CIMO3_Topo` varchar(5) NOT NULL, `CIMO3_Morpho` varchar(9) NOT NULL) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Structure de la table `transcodage_vianey_lesion_adicap_cimo3_morpho`
--

CREATE TABLE `transcodage_vianey_lesion_adicap_cimo3_morpho` ( `NumAuto` int(11) NOT NULL, `Lesion` varchar(4) NOT NULL, `CIMO3_Morpho` varchar(9) NOT NULL) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Structure de la table `patient`
--

CREATE TABLE `patient` (`NumAutoPatient` int(11) NOT NULL, `Nom` varchar(100) NOT NULL, `Prenom` varchar(100) NOT NULL, `Sexe` tinyint(2) NOT NULL, `DDN_Jour` tinyint(2) NOT NULL, `DDN_Mois` tinyint(2) NOT NULL, `DDN_Annee` smallint(4) NOT NULL, `hashPatient` int(11) NOT NULL, `identite_principale` int(11) NOT NULL, `flag_id` tinyint(1) DEFAULT NULL) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Structure de la table `rel_patient_acp`
--

CREATE TABLE `rel_patient_acp` (`NumAutoSourceACP` int(11) NOT NULL, `NumPatient` int(11) NOT NULL, `Ligne_ACP` int(11) NOT NULL) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Structure de la table `rel_patient_pmsi`
--

CREATE TABLE `rel_patient_pmsi` (`NumAutoSourcePMSI` int(11) NOT NULL, `NumPatient` int(11) NOT NULL, `Ligne_PMSI` int(11) NOT NULL) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Structure de la table `libelle_cimo3_morpho`
--

CREATE TABLE `libelle_cimo3_morpho` (`NumAuto` int(11) NOT NULL, `Code_CIMO3_Morpho` varchar(8) NOT NULL, `Libelle_CIMO3_Morpho` varchar(250) NOT NULL) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Structure de la table `libelle_cimo3_topo`
--

CREATE TABLE `libelle_cimo3_topo` (`NumAuto` int(11) NOT NULL, `Code_CIMO3_Topo` varchar(5) NOT NULL, `Libelle_CIMO3_Topo` varchar(250) NOT NULL) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Structure de la table `libelle_cim10`
--

CREATE TABLE `libelle_cim10` (`NumAuto` int(11) NOT NULL, `Code_CIM10` varchar(5) NOT NULL, `Libelle_CIM10` varchar(250) NOT NULL) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Structure de la table `notification`
--

CREATE TABLE `notification` (`ID_Notification` int(11) NOT NULL, `ID_Patient_Unique` int(11) NOT NULL, `Code_CIMO3_Topo` varchar(5) NOT NULL, `Code_CIMO3_Morpho` varchar(11) NOT NULL, `Notification_Date_Jour` tinyint(2) NOT NULL, `Notification_Date_Mois` tinyint(2) NOT NULL, `Notification_Date_Annee` smallint(4) NOT NULL, `Diagnostic_Date_Jour` tinyint(2) NOT NULL, `Diagnostic_Date_Mois` tinyint(2) NOT NULL, `Diagnostic_Date_Annee` smallint(4) NOT NULL, `Notification_Score` float NOT NULL, `Multisite` tinyint(1) NOT NULL) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Structure de la table `rel_notification_acp`
--

CREATE TABLE `rel_notification_acp` (`NumAutoNotificationACP` int(11) NOT NULL, `NumNotification` int(11) NOT NULL, `Ligne_ACP` int(11) NOT NULL) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Structure de la table `rel_notification_das`
--

CREATE TABLE `rel_notification_das` (`NumAutoNotificationDAS` int(11) NOT NULL, `NumNotification` int(11) NOT NULL, `Ligne_DAS` int(11) NOT NULL) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Structure de la table `rel_notification_dp`
--

CREATE TABLE `rel_notification_dp` (`NumAutoNotificationDP` int(11) NOT NULL, `NumNotification` int(11) NOT NULL, `Ligne_DP` int(11) NOT NULL) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Structure de la table `rel_notification_dr`
--

CREATE TABLE `rel_notification_dr` (`NumAutoNotificationDR` int(11) NOT NULL, `NumNotification` int(11) NOT NULL, `Ligne_DR` int(11) NOT NULL) ENGINE=MyISAM DEFAULT CHARSET=latin1;





--
-- Index pour les tables exportées
--

--
-- Index pour la table `adicap`
--
ALTER TABLE `adicap` ADD PRIMARY KEY (`NumAutoAdicap`), ADD UNIQUE KEY `NumAutoAdicap1` (`NumAutoAdicap`);

--
-- Index pour la table `adicap_prelevement`
--
ALTER TABLE `adicap_prelevement` ADD PRIMARY KEY (`NumAutoAdicapPrel`), ADD UNIQUE KEY `NumAutoAdicapPrel` (`NumAutoAdicapPrel`);

--
-- Index pour la table `das`
--
ALTER TABLE `das` ADD PRIMARY KEY (`NumAutoDAS`), ADD UNIQUE KEY `NumAutoDAS` (`NumAutoDAS`);

--
-- Index pour la table `rel_adicap_cimo3`
--
ALTER TABLE `rel_adicap_cimo3` ADD PRIMARY KEY (`NumAutoCIMO3`);

--
-- Index pour la table `rel_das_cimo3`
--
ALTER TABLE `rel_das_cimo3` ADD PRIMARY KEY (`NumAutoCIMO3`);

--
-- Index pour la table `rel_dp_cimo3`
--
ALTER TABLE `rel_dp_cimo3` ADD PRIMARY KEY (`NumAutoCIMO3`);

--
-- Index pour la table `rel_dr_cimo3`
--
ALTER TABLE `rel_dr_cimo3` ADD PRIMARY KEY (`NumAutoCIMO3`);

--
-- Index pour la table `retrotranscodage_cui_cim10`
--
ALTER TABLE `retrotranscodage_cui_cim10` ADD PRIMARY KEY (`NumAuto`);

--
-- Index pour la table `sejour`
--
ALTER TABLE `sejour` ADD PRIMARY KEY (`NumAutoSejour`), ADD UNIQUE KEY `NumAutoSejour_2` (`NumAutoSejour`), ADD UNIQUE KEY `NumAutoSejour_3` (`NumAutoSejour`);

--
-- Index pour la table `transcodage_adicap_cimo3_morpho_valides`
--
ALTER TABLE `transcodage_adicap_cimo3_morpho_valides` ADD PRIMARY KEY (`NumAuto`);

--
-- Index pour la table `transcodage_adicap_cimo3_topo_valides`
--
ALTER TABLE `transcodage_adicap_cimo3_topo_valides` ADD PRIMARY KEY (`NumAuto`);

--
-- Index pour la table `transcodage_cim10_cimo3_valides`
--
ALTER TABLE `transcodage_cim10_cimo3_valides` ADD PRIMARY KEY (`NumAuto`);

--
-- Index pour la table `transcodage_cim10_cui_umls`
--
ALTER TABLE `transcodage_cim10_cui_umls` ADD PRIMARY KEY (`NumAuto`);

--
-- Index pour la table `transcodage_vianey_cui_cimo3`
--
ALTER TABLE `transcodage_vianey_cui_cimo3` ADD PRIMARY KEY (`NumAuto`);

--
-- Index pour la table `transcodage_vianey_lesion_adicap_cimo3_morpho`
--
ALTER TABLE `transcodage_vianey_lesion_adicap_cimo3_morpho` ADD PRIMARY KEY (`NumAuto`);

--
-- Index pour la table `patient`
--
ALTER TABLE `patient` ADD PRIMARY KEY (`NumAutoPatient`);

--
-- Index pour la table `rel_patient_acp`
--
ALTER TABLE `rel_patient_acp` ADD PRIMARY KEY (`NumAutoSourceACP`);

--
-- Index pour la table `rel_patient_pmsi`
--
ALTER TABLE `rel_patient_pmsi` ADD PRIMARY KEY (`NumAutoSourcePMSI`);

--
-- Index pour la table `libelle_cimo3_morpho`
--
ALTER TABLE `libelle_cimo3_morpho` ADD PRIMARY KEY (`NumAuto`);

--
-- Index pour la table `libelle_cimo3_topo`
--
ALTER TABLE `libelle_cimo3_topo` ADD PRIMARY KEY (`NumAuto`);

--
-- Index pour la table `libelle_cim10`
--
ALTER TABLE `libelle_cim10` ADD PRIMARY KEY (`NumAuto`);

--
-- Index pour la table `notification`
--
ALTER TABLE `notification` ADD PRIMARY KEY (`ID_Notification`);

--
-- Index pour la table `rel_notification_acp`
--
ALTER TABLE `rel_notification_acp` ADD PRIMARY KEY (`NumAutoNotificationACP`);

--
-- Index pour la table `rel_notification_das`
--
ALTER TABLE `rel_notification_das` ADD PRIMARY KEY (`NumAutoNotificationDAS`);

--
-- Index pour la table `rel_notification_dp`
--
ALTER TABLE `rel_notification_dp` ADD PRIMARY KEY (`NumAutoNotificationDP`);

--
-- Index pour la table `rel_notification_dr`
--
ALTER TABLE `rel_notification_dr` ADD PRIMARY KEY (`NumAutoNotificationDR`);




--
-- AUTO_INCREMENT pour les tables exportées
--

--
-- AUTO_INCREMENT pour la table `adicap`
--
ALTER TABLE `adicap` MODIFY `NumAutoAdicap` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT pour la table `adicap_prelevement`
--
ALTER TABLE `adicap_prelevement` MODIFY `NumAutoAdicapPrel` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT pour la table `das`
--
ALTER TABLE `das` MODIFY `NumAutoDAS` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT pour la table `rel_adicap_cimo3`
--
ALTER TABLE `rel_adicap_cimo3` MODIFY `NumAutoCIMO3` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT pour la table `rel_das_cimo3`
--
ALTER TABLE `rel_das_cimo3` MODIFY `NumAutoCIMO3` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT pour la table `rel_dp_cimo3`
--
ALTER TABLE `rel_dp_cimo3` MODIFY `NumAutoCIMO3` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT pour la table `rel_dr_cimo3`
--
ALTER TABLE `rel_dr_cimo3` MODIFY `NumAutoCIMO3` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT pour la table `retrotranscodage_cui_cim10`
--
ALTER TABLE `retrotranscodage_cui_cim10` MODIFY `NumAuto` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT pour la table `sejour`
--
ALTER TABLE `sejour` MODIFY `NumAutoSejour` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT pour la table `transcodage_adicap_cimo3_morpho_valides`
--
ALTER TABLE `transcodage_adicap_cimo3_morpho_valides` MODIFY `NumAuto` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT pour la table `transcodage_adicap_cimo3_topo_valides`
--
ALTER TABLE `transcodage_adicap_cimo3_topo_valides` MODIFY `NumAuto` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT pour la table `transcodage_cim10_cimo3_valides`
--
ALTER TABLE `transcodage_cim10_cimo3_valides` MODIFY `NumAuto` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT pour la table `transcodage_cim10_cui_umls`
--
ALTER TABLE `transcodage_cim10_cui_umls` MODIFY `NumAuto` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT pour la table `transcodage_vianey_cui_cimo3`
--
ALTER TABLE `transcodage_vianey_cui_cimo3` MODIFY `NumAuto` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT pour la table `transcodage_vianey_lesion_adicap_cimo3_morpho`
--
ALTER TABLE `transcodage_vianey_lesion_adicap_cimo3_morpho` MODIFY `NumAuto` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT pour la table `patient`
--
ALTER TABLE `patient` MODIFY `NumAutoPatient` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT pour la table `rel_patient_acp`
--
ALTER TABLE `rel_patient_acp` MODIFY `NumAutoSourceACP` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT pour la table `rel_patient_pmsi`
--
ALTER TABLE `rel_patient_pmsi` MODIFY `NumAutoSourcePMSI` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT pour la table `libelle_cimo3_morpho`
--
ALTER TABLE `libelle_cimo3_morpho` MODIFY `NumAuto` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT pour la table `libelle_cimo3_topo`
--
ALTER TABLE `libelle_cimo3_topo` MODIFY `NumAuto` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT pour la table `libelle_cim10`
--
ALTER TABLE `libelle_cim10` MODIFY `NumAuto` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT pour la table `notification`
--
ALTER TABLE `notification` MODIFY `ID_Notification` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT pour la table `rel_notification_acp`
--
ALTER TABLE `rel_notification_acp` MODIFY `NumAutoNotificationACP` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT pour la table `rel_notification_das`
--
ALTER TABLE `rel_notification_das` MODIFY `NumAutoNotificationDAS` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT pour la table `rel_notification_dp`
--
ALTER TABLE `rel_notification_dp` MODIFY `NumAutoNotificationDP` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT pour la table `rel_notification_dr`
--
ALTER TABLE `rel_notification_dr` MODIFY `NumAutoNotificationDR` int(11) NOT NULL AUTO_INCREMENT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;


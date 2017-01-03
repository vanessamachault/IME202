package Integration;
import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Importation {

	public static java.sql.PreparedStatement prepare_temp, prepare_add_sejour, prepare_add_das, prepare_add_adicap_prel;

	public static void ajoutDonneesSejour() throws SQLException {

		String fichier = "Sejour.txt";
		int hashSejour = 498658002;
		int nligne = 0;
		long debut = System.currentTimeMillis();
		String code_CIM10;

		// Connexion à la base de donnée
		String url = "jdbc:mysql://localhost/projet_ime_202";
		String user = "root";
		String motpasse = "";

		Connexion connect = new Connexion(url, user, motpasse);
		Connection con = connect.getCon();
		Statement st = con.createStatement();
		con.setAutoCommit(false);

		// Création de la table temporaire d'iportation des données
		String create_table = "CREATE TEMPORARY TABLE `sejour_temp` (" + "`NumPatient` int(6) NOT NULL,"
				+ "`Sexe` tinyint(1) NOT NULL," + "`DDN_Jour` tinyint(2) NOT NULL," + "`DDN_Mois` tinyint(2) NOT NULL,"
				+ "`DDN_Annee` smallint(4) NOT NULL," + "`Prenom` varchar(100) NOT NULL,"
				+ "`Nom` varchar(100) NOT NULL," + "`EDH_Jour` tinyint(2) NOT NULL," + "`EDH_Mois` tinyint(2) NOT NULL,"
				+ "`EDH_Annee` smallint(4) NOT NULL," + "`SDH_Jour` tinyint(2) NOT NULL,"
				+ "`SDH_Mois` tinyint(2) NOT NULL," + "`SDH_Annee` smallint(4) NOT NULL,"
				+ "`ED_Jour` tinyint(2) NOT NULL," + "`ED_Mois` tinyint(2) NOT NULL,"
				+ "`ED_Annee` smallint(4) NOT NULL," + "`SD_Jour` tinyint(2) NOT NULL,"
				+ "`SD_Mois` tinyint(2) NOT NULL," + "`SD_Annee` smallint(4) NOT NULL," + "`DP` varchar(10) NOT NULL,"
				+ "`DR` varchar(10) NOT NULL," + "`NumSejour` int(11) NOT NULL," + "`HashCode` int(11) NOT NULL"
				+ ") ENGINE=InnoDB DEFAULT CHARSET=latin1;";

		try {
			st.executeUpdate(create_table);
		} catch (Exception e) {
			System.out.println(e.toString());
		}

		// Ajout des données dans la table temporaire
		String query = "insert into sejour_temp (NumPatient, Sexe, DDN_Jour, DDN_Mois, DDN_Annee, Prenom, Nom, EDH_Jour, EDH_Mois, EDH_Annee,"
				+ " SDH_Jour, SDH_Mois, SDH_Annee, ED_Jour, ED_Mois, ED_Annee, SD_Jour, SD_Mois, SD_Annee, DP, DR, NumSejour, HashCode)"
				+ " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

		prepare_temp = con.prepareStatement(query);

		// Lecture du fichier texte
		try {
			InputStream ips = new FileInputStream(fichier);
			InputStreamReader ipsr = new InputStreamReader(ips);
			BufferedReader br = new BufferedReader(ipsr);
			String ligne;

			// On vérifie que le fichier source à la bonne structure
			if (br.readLine().hashCode() == hashSejour) {

				// Traitement de chaque ligne du fichier
				while ((ligne = br.readLine()) != null) {

					nligne = nligne + 1;
					ligne = ligne.replaceAll(":\\d\\d:\\d\\d:\\d\\d", "");

					if (nligne != 0) {

						ligne = ligne.replaceAll(":\\d\\d:\\d\\d:\\d\\d", "");// On efface les timestamps

						// Chaque ligne est rangée dans un tableau de String splité au niveau des pipes
						String[] splitArray = null;
						splitArray = ligne.split("\\|");

						String chaine = "";

						// Traitement des dates au niveau des colonnes 2, 5, 6, 7, 8
						for (int i = 0; i < splitArray.length; i++) {

							if (i == 2 || i == 5 || i == 6 || i == 7 || i == 8) {
								splitArray[i] = splitArray[i].replaceAll("JAN", "/01/");
								splitArray[i] = splitArray[i].replaceAll("FEB", "/02/");
								splitArray[i] = splitArray[i].replaceAll("MAR", "/03/");
								splitArray[i] = splitArray[i].replaceAll("APR", "/04/");
								splitArray[i] = splitArray[i].replaceAll("MAY", "/05/");
								splitArray[i] = splitArray[i].replaceAll("JUN", "/06/");
								splitArray[i] = splitArray[i].replaceAll("JUL", "/07/");
								splitArray[i] = splitArray[i].replaceAll("AUG", "/08/");
								splitArray[i] = splitArray[i].replaceAll("SEP", "/09/");
								splitArray[i] = splitArray[i].replaceAll("OCT", "/10/");
								splitArray[i] = splitArray[i].replaceAll("NOV", "/11/");
								splitArray[i] = splitArray[i].replaceAll("DEC", "/12/");
							}

							// On concatène les différents case du tableau dans une nouvelle String appelée chaine avec un "/" comme séparateur (cf date)
							chaine += splitArray[i] + "/";
						}

						// Chaque chaine est rangée dans un tableau de String en splitant sur les "/"
						String[] splitArrayBDD = null;
						splitArrayBDD = chaine.split("/");

						// Ajout dans la base de données des données traitées ligne par ligne
						String lignePMSI = "";
						for (int i = 0; i < 19; i++) {
							prepare_temp.setString(i + 1, splitArrayBDD[i]);
							lignePMSI = lignePMSI + splitArrayBDD[i];
						}
						for (int i = 18 ; i < 21 ; i++){
							if (!splitArrayBDD[i].isEmpty() && (splitArrayBDD[i].substring(0, 1).equalsIgnoreCase("C") || splitArrayBDD[i].substring(0, 2).equalsIgnoreCase("D0"))){
								if (splitArrayBDD[i].length() > 3) {
									code_CIM10 = splitArrayBDD[i].substring(0, 3) + "." + splitArrayBDD[i].substring(3, 4);
									prepare_temp.setString(i + 1, code_CIM10);
									lignePMSI = lignePMSI + code_CIM10;
								} else {
									code_CIM10 = splitArrayBDD[i].substring(0, 3);
									prepare_temp.setString(i + 1, code_CIM10);
									lignePMSI = lignePMSI + code_CIM10;
								}
							}else {
								code_CIM10 = splitArrayBDD[i];
								prepare_temp.setString(i + 1, code_CIM10);
								lignePMSI = lignePMSI + code_CIM10;
							}

						}
						
						prepare_temp.setString(22, splitArrayBDD[21]);
						lignePMSI = lignePMSI + splitArrayBDD[21];

						prepare_temp.setInt(23, lignePMSI.hashCode());
						prepare_temp.addBatch();
					}

				}
			} else
				System.out.println("La structure du fichier est mauvaise, veuillez vérifier la source de données");
			br.close();

		} catch (Exception e) {
			System.out.println(e.toString());
		}

		prepare_temp.executeBatch();
		con.commit();

		// On transfère les données de la table temporaire vers la table sejour
		String nonCorrespondance = "SELECT DISTINCT sejour_temp.*"
				+ "FROM sejour_temp WHERE sejour_temp.HashCode NOT IN (SELECT sejour.HashCode FROM sejour);";

		String query_ajout = "insert into sejour (NumPatient, Sexe, DDN_Jour, DDN_Mois, DDN_Annee, Prenom, Nom, EDH_Jour, EDH_Mois, EDH_Annee,"
				+ " SDH_Jour, SDH_Mois, SDH_Annee, ED_Jour, ED_Mois, ED_Annee, SD_Jour, SD_Mois, SD_Annee, DP, DR, NumSejour, HashCode)"
				+ " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

		prepare_add_sejour = con.prepareStatement(query_ajout);

		try {
			ResultSet rs = st.executeQuery(nonCorrespondance);
			con.setAutoCommit(false);
			while (rs.next()) {
				prepare_add_sejour.setString(1, rs.getString("NumPatient"));
				prepare_add_sejour.setString(2, rs.getString("Sexe"));
				prepare_add_sejour.setString(3, rs.getString("DDN_Jour"));
				prepare_add_sejour.setString(4, rs.getString("DDN_Mois"));
				prepare_add_sejour.setString(5, rs.getString("DDN_Annee"));
				prepare_add_sejour.setString(6, rs.getString("Prenom"));
				prepare_add_sejour.setString(7, rs.getString("Nom"));
				prepare_add_sejour.setString(8, rs.getString("EDH_Jour"));
				prepare_add_sejour.setString(9, rs.getString("EDH_Mois"));
				prepare_add_sejour.setString(10, rs.getString("EDH_Annee"));
				prepare_add_sejour.setString(11, rs.getString("SDH_Jour"));
				prepare_add_sejour.setString(12, rs.getString("SDH_Mois"));
				prepare_add_sejour.setString(13, rs.getString("SDH_Annee"));
				prepare_add_sejour.setString(14, rs.getString("ED_Jour"));
				prepare_add_sejour.setString(15, rs.getString("ED_Mois"));
				prepare_add_sejour.setString(16, rs.getString("ED_Annee"));
				prepare_add_sejour.setString(17, rs.getString("SD_Jour"));
				prepare_add_sejour.setString(18, rs.getString("SD_Mois"));
				prepare_add_sejour.setString(19, rs.getString("SD_Annee"));
				prepare_add_sejour.setString(20, rs.getString("DP"));
				prepare_add_sejour.setString(21, rs.getString("DR"));
				prepare_add_sejour.setString(22, rs.getString("NumSejour"));
				prepare_add_sejour.setString(23, rs.getString("HashCode"));

				prepare_add_sejour.addBatch();
			}
		}

		catch (Exception e) {
			System.out.println(e.toString());
		}

		prepare_add_sejour.executeBatch();
		con.commit();

		st.close();
		con.close();

		System.out.print("\nDurée d'exécution ajout fichier " + fichier + " : ");
		System.out.print((System.currentTimeMillis() - debut) / 1000);
		System.out.print(" secondes");

	}

	public static void ajoutDonneesDAS() throws SQLException {

		String fichier = "das.txt";
		int hashDAS = 159598452;
		int nligne = 0;
		long debut = System.currentTimeMillis();
		String code_CIM10;

		// Connexion à la base de donnée
		String url = "jdbc:mysql://localhost/projet_ime_202";
		String user = "root";
		String motpasse = "";

		Connexion connect = new Connexion(url, user, motpasse);
		Connection con = connect.getCon();
		Statement st = con.createStatement();
		con.setAutoCommit(false);

		// Création de la table temporaire DAS
		String create_table = "CREATE TEMPORARY TABLE `das_temp` (" + "`NumSejour` int(11) NOT NULL,"
				+ "`DAS` varchar(10) NOT NULL," + "`HashCode` int(11) NOT NULL"
				+ ") ENGINE=MyISAM DEFAULT CHARSET=latin1;";

		try {
			st.executeUpdate(create_table);
		} catch (Exception e) {
			System.out.println(e.toString());
		}

		// La requête préparée
		String query = "insert into das_temp (NumSejour, DAS, HashCode) values (?, ?, ?);";

		prepare_temp = con.prepareStatement(query);

		// Lecture du fichier texte
		try {
			InputStream ips = new FileInputStream(fichier);
			InputStreamReader ipsr = new InputStreamReader(ips);
			BufferedReader br = new BufferedReader(ipsr);
			String ligne;

			// On vérifie que le fichier source à la bonne structure
			if (br.readLine().hashCode() == hashDAS) {

				// Traitement de chaque ligne du fichier
				while ((ligne = br.readLine()) != null) {

					nligne = nligne + 1;

					if (nligne != 0) {

						ligne = ligne.replaceAll(":\\d\\d:\\d\\d:\\d\\d", "");// On efface les timestamps

						// Chaque ligne est rangée dans un tableau de String, splité au niveau des pipe
						String[] splitArray = null;
						splitArray = ligne.split("\\|");

						// Ajout dans la base de données temporaire des données traitées ligne par ligne
						String ligneDAS = "";
						for (int i = 0; i < 1; i++) {
							prepare_temp.setString(i + 1, splitArray[i]);
							ligneDAS = ligneDAS + splitArray[i];
						}
						for (int i = 1; i < 2; i++) {
							if (!splitArray[i].isEmpty() && (splitArray[i].substring(0, 1).equalsIgnoreCase("C") || splitArray[i].substring(0, 2).equalsIgnoreCase("D0"))){
								if (splitArray[i].length() > 3) {
									code_CIM10 = splitArray[i].substring(0, 3) + "." + splitArray[i].substring(3, 4);
									prepare_temp.setString(i + 1, code_CIM10);
									ligneDAS = ligneDAS + code_CIM10;
								} else {
									code_CIM10 = splitArray[i].substring(0, 3);
									prepare_temp.setString(i + 1, code_CIM10);
									ligneDAS = ligneDAS + code_CIM10;
								}
							}else {
								code_CIM10 = splitArray[i];
								prepare_temp.setString(i + 1, code_CIM10);
								ligneDAS = ligneDAS + code_CIM10;
							}
						}
						
						prepare_temp.setInt(3, ligneDAS.hashCode());
						prepare_temp.addBatch();
					}
				}
			} else
				System.out.println("La structure du fichier est mauvaise, veuillez vérifier la source de données");
			br.close();

		} catch (Exception e) {
			System.out.println(e.toString());
		}

		prepare_temp.executeBatch();
		con.commit();

		// On transfère les données de la table temporaire vers la table das
		String nonCorrespondance = "SELECT DISTINCT das_temp.*"
				+ "FROM das_temp WHERE das_temp.HashCode NOT IN (SELECT das.HashCode FROM das);";

		String query_ajout = "insert into das (NumSejour, DAS, HashCode) values (?, ?, ?);";

		prepare_add_das = con.prepareStatement(query_ajout);

		try {
			ResultSet rs = st.executeQuery(nonCorrespondance);
			con.setAutoCommit(false);
			while (rs.next()) {
				prepare_add_das.setString(1, rs.getString("NumSejour"));
				prepare_add_das.setString(2, rs.getString("DAS"));
				prepare_add_das.setString(3, rs.getString("HashCode"));

				prepare_add_das.addBatch();
			}

		} catch (Exception e) {
			System.out.println(e.toString());
		}

		prepare_add_das.executeBatch();
		con.commit();

		st.close();
		con.close();

		System.out.print("\nDurée d'exécution ajout fichier " + fichier + " : ");
		System.out.print((System.currentTimeMillis() - debut) / 1000);
		System.out.print(" secondes");

	}

	public static void ajoutDonneesAdicap() throws SQLException {

		String fichier = "Anapath.txt";
		int hashAdicap = 131860565;
		int nligne = 0;
		long debut = System.currentTimeMillis();
		long num_auto_ADICAP;

		// Connexion à la base de donnée
		String url = "jdbc:mysql://localhost/projet_ime_202";
		String user = "root";
		String motpasse = "";

		Connexion connect = new Connexion(url, user, motpasse);
		Connection con = connect.getCon();
		Statement st = con.createStatement();
		Statement st2 = con.createStatement();

		// Création des tables temporaires
		String create_table1 = "CREATE TEMPORARY TABLE `adicap_temp` (" + "`NumAdicapPrel` int(11) NOT NULL,"
				+ "`Adicap` varchar(11) NOT NULL," + "`HashCode` int(11) NOT NULL"
				+ ") ENGINE=MyISAM DEFAULT CHARSET=latin1;";

		String create_table2 = "CREATE TEMPORARY TABLE `adicap_prelevement_temp` LIKE `adicap_prelevement`";

		try {
			st.executeUpdate(create_table1);
			st.executeUpdate(create_table2);

		} catch (Exception e) {
			System.out.println(e.toString());
		}

		// La requête préparée (table : adicap_prelevement)
		String query = "insert into adicap_prelevement_temp (NumAutoAdicapPrel, NumPatient, Sexe, DDN_Jour, DDN_Mois, DDN_Annee, Prenom, Nom, DatePrel_Jour, DatePrel_Mois, DatePrel_Annee, HashCode)"
				+ " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

		// La requête préparée
		String add_adicap_temp = "insert into adicap_temp (NumAdicapPrel, Adicap, HashCode)" + " values (?, ?, ?);";

		// Lecture du fichier texte
		try {
			InputStream ips = new FileInputStream(fichier);
			InputStreamReader ipsr = new InputStreamReader(ips);
			BufferedReader br = new BufferedReader(ipsr);
			String ligne;

			// On vérifie que le fichier source à la bonne structure
			if ((br.readLine().hashCode() == hashAdicap)) {

				// Traitement de chaque ligne du fichier
				while ((ligne = br.readLine()) != null) {

					nligne = nligne + 1;

					if (nligne != 0) {

						// Chaque ligne est rangée dans un tableau de String, splité au niveau des pipe

						ligne = ligne.replaceAll(":\\d\\d:\\d\\d:\\d\\d", "");// On efface les timestamps

						String[] splitArray = null;
						splitArray = ligne.split("\\|");

						String chaine = "";

						// Traitement des dates au niveau des colonnes 2, 5
						for (int i = 0; i < splitArray.length; i++) {

							if (i == 2 || i == 5) {
								splitArray[i] = splitArray[i].replaceAll("JAN", "/01/");
								splitArray[i] = splitArray[i].replaceAll("FEB", "/02/");
								splitArray[i] = splitArray[i].replaceAll("MAR", "/03/");
								splitArray[i] = splitArray[i].replaceAll("APR", "/04/");
								splitArray[i] = splitArray[i].replaceAll("MAY", "/05/");
								splitArray[i] = splitArray[i].replaceAll("JUN", "/06/");
								splitArray[i] = splitArray[i].replaceAll("JUL", "/07/");
								splitArray[i] = splitArray[i].replaceAll("AUG", "/08/");
								splitArray[i] = splitArray[i].replaceAll("SEP", "/09/");
								splitArray[i] = splitArray[i].replaceAll("OCT", "/10/");
								splitArray[i] = splitArray[i].replaceAll("NOV", "/11/");
								splitArray[i] = splitArray[i].replaceAll("DEC", "/12/");
							}

							// On concatène les différents case du tableau dans une nouvelle String appelée chaine avec un "/" comme séparateur (cf date)
							chaine += splitArray[i] + "/";
						}

						// Chaque chaine est rangée dans un tableau de String en splitant sur les "/"
						String[] splitArrayBDD = null;
						splitArrayBDD = chaine.split("/");

						// Ajout dans la table adicap_prelevement_temp des données traitées ligne par ligne
						PreparedStatement prepare = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

						String ligne_ADICAP_Prel = "";
						prepare.setInt(1, nligne);
						for (int i = 0; i < 10; i++) {
							prepare.setString(i + 2, splitArrayBDD[i]);
							ligne_ADICAP_Prel = ligne_ADICAP_Prel + splitArrayBDD[i];
						}
						prepare.setInt(12, ligne_ADICAP_Prel.hashCode());

						int affectedRows = prepare.executeUpdate();

						// On récupère le num_auto généré au moment de l'insert
						if (affectedRows == 0) {
							throw new SQLException("Aucun enregistrement ajouté. Pas de numéro de ligne à récupérer.");
						}

						try (ResultSet generatedKeys = prepare.getGeneratedKeys()) {
							if (generatedKeys.next()) {
								num_auto_ADICAP = generatedKeys.getLong(1);
								// System.out.println(num_auto_ADICAP);
							} else {
								throw new SQLException(
										"Aucun enregistrement ajouté. Pas de numéro de ligne à récupérer.");
							}
						}

						// Traitement ADICAP1
						String[] splitArrayADICAP1 = null;

						if (splitArrayBDD.length > 10) {
							splitArrayADICAP1 = splitArrayBDD[10].split(";");

							if (!splitArrayADICAP1[0].equals("")) {
								PreparedStatement prepare2 = con.prepareStatement(add_adicap_temp);

								for (int i = 0; i < splitArrayADICAP1.length; i++) {
									prepare2.setLong(1, num_auto_ADICAP);
									prepare2.setString(2, splitArrayADICAP1[i]);
									prepare2.setInt(3, (num_auto_ADICAP + splitArrayADICAP1[i]).hashCode());
									prepare2.executeUpdate();
								}
							}
						}

						// Traitement ADICAP2
						String[] splitArrayADICAP2 = null;

						if (splitArrayBDD.length > 11) {
							splitArrayADICAP2 = splitArrayBDD[11].split(";");

							if (!splitArrayADICAP2[0].equals("")) {
								PreparedStatement prepare3 = con.prepareStatement(add_adicap_temp);

								for (int i = 0; i < splitArrayADICAP2.length; i++) {
									prepare3.setLong(1, num_auto_ADICAP);
									prepare3.setString(2, splitArrayADICAP2[i]);
									prepare3.setInt(3, (num_auto_ADICAP + splitArrayADICAP2[i]).hashCode());
									prepare3.executeUpdate();
								}
							}
						}
					}
				}
			} else
				System.out.println("La structure du fichier est mauvaise, veuillez vérifier la source de données");

			br.close();

			// On transfère les données de la table temporaire vers la table prelèvement
			String nonCorrespondance = "SELECT DISTINCT adicap_prelevement_temp.* "
					+ "FROM adicap_prelevement_temp WHERE adicap_prelevement_temp.HashCode NOT IN (SELECT adicap_prelevement.HashCode FROM adicap_prelevement);";

			String query_ajout_adicap_prel = "insert into adicap_prelevement (NumPatient, Sexe, DDN_Jour, DDN_Mois, DDN_Annee, Prenom, Nom, DatePrel_Jour, DatePrel_Mois, DatePrel_Annee, HashCode)"
					+ " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

			String query_ajout_adicap = "insert into adicap (NumAdicapPrel, Adicap)" + " values (?, ?);";

			// Ajout dans la table adicap_prelevement des données traitées ligne par ligne
			prepare_add_adicap_prel = con.prepareStatement(query_ajout_adicap_prel, Statement.RETURN_GENERATED_KEYS);

			try {
				ResultSet rs = st.executeQuery(nonCorrespondance);
				while (rs.next()) {
					String correspondanceTableTemporaire = "";

					prepare_add_adicap_prel.setString(1, rs.getString("NumPatient"));
					prepare_add_adicap_prel.setString(2, rs.getString("Sexe"));
					prepare_add_adicap_prel.setString(3, rs.getString("DDN_Jour"));
					prepare_add_adicap_prel.setString(4, rs.getString("DDN_Mois"));
					prepare_add_adicap_prel.setString(5, rs.getString("DDN_Annee"));
					prepare_add_adicap_prel.setString(6, rs.getString("Prenom"));
					prepare_add_adicap_prel.setString(7, rs.getString("Nom"));
					prepare_add_adicap_prel.setString(8, rs.getString("DatePrel_Jour"));
					prepare_add_adicap_prel.setString(9, rs.getString("DatePrel_Mois"));
					prepare_add_adicap_prel.setString(10, rs.getString("DatePrel_Annee"));
					prepare_add_adicap_prel.setString(11, rs.getString("HashCode"));

					int affectedRows = prepare_add_adicap_prel.executeUpdate();

					// On récupère le num_auto généré au moment de l'insert
					if (affectedRows == 0) {
						throw new SQLException("Aucun enregistrement ajouté. Pas de numéro de ligne à récupérer.");
					}

					try (ResultSet generatedKeys = prepare_add_adicap_prel.getGeneratedKeys()) {
						if (generatedKeys.next()) {
							num_auto_ADICAP = generatedKeys.getLong(1);
						} else {
							throw new SQLException("Aucun enregistrement ajouté. Pas de numéro de ligne à récupérer.");
						}

						// Pour l'importantion actuelle dans la table adicap_prelevement, on retrouve les codes adicap relier au niveau de la table temporaire
						correspondanceTableTemporaire = "select adicap_temp.* from adicap_prelevement_temp inner join adicap_temp on adicap_prelevement_temp.NumAUtoAdicapPrel = adicap_temp.NumAdicapPrel "
								+ "where adicap_temp.NumAdicapPrel=" + rs.getString("NumAutoAdicapPrel");

						try {
							//On créer un nouveau ResultSet pour ajouter dans la table adicap les codes retrouvés au niveau de la requetè correspondanceTableTemporaire
							ResultSet rs2 = st2.executeQuery(correspondanceTableTemporaire);
							PreparedStatement add_adicap = con.prepareStatement(query_ajout_adicap);
							while (rs2.next()) {
								add_adicap.setLong(1, num_auto_ADICAP);
								add_adicap.setString(2, rs2.getString("Adicap"));
								add_adicap.executeUpdate();
							}

						} catch (Exception e) {
							System.out.println(e.toString());
						}
					}
				}
			}
			catch (Exception e) {
				System.out.println(e.toString());
			}

			st.close();
			st2.close();
			con.close();

		} catch (Exception e) {
			System.out.println(e.toString());
		}

		System.out.print("\nDurée d'exécution ajout fichier " + fichier + " : ");
		System.out.print((System.currentTimeMillis() - debut) / 1000);
		System.out.print(" secondes");

	}

	public static void main(String[] args) throws SQLException {
		long debut = System.currentTimeMillis();

		Utilities.CreateBDD("root", "");
		Importation.ajoutDonneesSejour();
		Importation.ajoutDonneesDAS();
		Importation.ajoutDonneesAdicap();

		System.out.print("\nDurée totale d'exécution : ");
		System.out.print((System.currentTimeMillis() - debut) / 1000);
		System.out.print(" secondes");
	}

}
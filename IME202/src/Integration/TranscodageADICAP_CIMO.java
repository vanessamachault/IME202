package Integration;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.simple.parser.ParseException;

/*
 SELECT sejour.Nom, sejour.Prenom, transcodage_cim10_cimo3_valides.CIMO3_Topo, transcodage_cim10_cimo3_valides.CIMO3_Morpho, "PMSI" AS Source 
 FROM sejour INNER JOIN rel_dp_cimo3 ON sejour.NumAutoSejour=rel_dp_cimo3.NumLigneDP INNER JOIN transcodage_cim10_cimo3_valides 
 ON rel_dp_cimo3.CIMO3_Correspondance=transcodage_cim10_cimo3_valides.NumAuto WHERE LEFT(sejour.DP,1)='C' 
 AND sejour.Nom="BEXANS" UNION SELECT adicap_prelevement.Nom, adicap_prelevement.Prenom, transcodage_adicap_cimo3_topo_valides.CIMO3_Topo, 
 transcodage_adicap_cimo3_morpho_valides.CIMO3_Morpho, "ADICAP" AS Source FROM adicap_prelevement INNER JOIN rel_adicap_cimo3 
 ON adicap_prelevement.NumAutoAdicapPrel=rel_adicap_cimo3.NumLigneADICAP INNER JOIN transcodage_adicap_cimo3_topo_valides 
 ON rel_adicap_cimo3.CIMO3_Topo = transcodage_adicap_cimo3_topo_valides.NumAuto INNER JOIN transcodage_adicap_cimo3_morpho_valides 
 ON rel_adicap_cimo3.CIMO3_Morpho = transcodage_adicap_cimo3_morpho_valides.NumAuto WHERE adicap_prelevement.Nom="BEXANS";"
 */

public class TranscodageADICAP_CIMO {

	public static void main(String[] args) throws JSONException, ParseException, SQLException {

		//Permet de trancoder les nouveaus codes ADICAP entrants
		transcodageADICAP_CIMO3_Morpho();

		//Permet d'obtenir la liste des transcode ADICAP en attente de validation
		Set<String> test = transcodage_ADICAP_CIMO3_a_valider();
		ArrayList<String> test2 = new ArrayList<String>();
		test2.addAll(test);

		System.out.println("\n\nListe des transcodages ADICAP à valider (disjonction de transcodage) :");
		for (int i = 0 ; i < test2.size() ; i++){
			System.out.println(test2.get(i));
		}

		//Permet d'obtenir la liste des transcode ADICAP sans transcodage
		ArrayList<String> codes_adicap_non_traduit = transcodage_ADICAP_CIMO3_non_traduit();

		System.out.println("\n\nListe des codes ADICAP non traduit :");
		for (int i = 0; i < codes_adicap_non_traduit.size(); i++){
			System.out.println("	-" + codes_adicap_non_traduit.get(i));
		}

	}

	public static ArrayList<String> transcodage_ADICAP_CIMO3_non_traduit() {

		ArrayList <String> liste_code_ADICAP_sans_traduction = new ArrayList<String>();

		//Connexion à la base de donnée
		String url= "jdbc:mysql://localhost/projet_ime_202";
		String user="root";
		String motpasse="";

		Connexion connect = new Connexion (url, user, motpasse);
		Connection con = connect.getCon();
		Statement st = null;

		//La requête de sélection des codes adicap à transcoder
		String query = "SELECT DISTINCT `Adicap` FROM `adicap` WHERE `Flag_Integration`= '0'";

		try {
			st = con.createStatement();
			ResultSet rs = st.executeQuery(query);
			while (rs.next()) {
				liste_code_ADICAP_sans_traduction.add(rs.getString("adicap"));
			}
		}catch (SQLException e1) {
			e1.printStackTrace();
		}

		return liste_code_ADICAP_sans_traduction;

	}

	public static Set<String> transcodage_ADICAP_CIMO3_a_valider() throws SQLException, JSONException, ParseException{

		long debut = System.currentTimeMillis();

		String code_ADICAP_a_traiter;
		String transcodage_ADICAP_CIMO3Morpho_API;
		String transcodage_ADICAP_CIMO3Morpho_Vianey;
		String transcode_a_valider = null;
		java.sql.PreparedStatement prepare2;

		//REGEX permettant de ne selectionner que les cancers et les codes incomplets (cf. AZ00 en remplacement des ****** et Z dans la REGEX)
		Pattern pattern = Pattern.compile("^\\D{5}[4-7Z]");
		Matcher matcher;

		//(Lesion|Transcodage_API|Transcodage_Vianey);
		Set<String> transcodes_a_valider = new HashSet<String>();

		//Connexion à la base de donnée
		String url= "jdbc:mysql://localhost/projet_ime_202";
		String user="root";
		String motpasse="";

		Connexion connect = new Connexion (url, user, motpasse);
		Connection con = connect.getCon();
		Statement st = null;

		//La requête de sélection des codes adicap à transcoder
		String query1 = "select NumAutoAdicap, adicap from adicap where Flag_Integration = 0";

		//La requête préparée de récupération des code à transcoder dans la table de Vianey
		String query2 = "select NumAuto FROM transcodage_adicap_cimo3_morpho_valides WHERE Lesion = ?";

		prepare2 = con.prepareStatement(query2);

		try {
			st = con.createStatement();
			ResultSet rs = st.executeQuery(query1);
			con.setAutoCommit(false);
			while (rs.next()) {

				code_ADICAP_a_traiter = null;

				//Pré-traitement des codes ADICAP incomplet
				if (rs.getString("adicap").contains("*") == true){
					code_ADICAP_a_traiter = rs.getString("adicap").replaceAll("\\*+", "AZ00");
				}else code_ADICAP_a_traiter = rs.getString("adicap");

				//Tous les codes plus long que 8 sont coupés pour permettre le transcodage
				if (code_ADICAP_a_traiter.length() >=8) {
					code_ADICAP_a_traiter = code_ADICAP_a_traiter.substring(0, 8);
				}

				//Ne sont traités que les codes égaux à 8 cad tous les codes complets + les incomplets avec *****
				if(code_ADICAP_a_traiter.length() == 8){

					//On vérifie que le code concerne une tumeur malige cad tous les codes de tumeurs maligne + les incomplets avec *****
					matcher = pattern.matcher(code_ADICAP_a_traiter);
					if (matcher.find()){

						//Transcodage des codes ADICAP (0 = pas de traduction du code et 1 = transcodage du code)
						String[] transcodage = Utilities.transcodage_ADICAP_CIMO3(code_ADICAP_a_traiter, 0, 1);

						String lesion = code_ADICAP_a_traiter.substring(4, 8);
						if (!lesion.equalsIgnoreCase("AZ00")){
							try {
								prepare2.setString(1, lesion);
								ResultSet rs2 = prepare2.executeQuery();
								rs2.last(); 
								int nb_resultat = rs2.getRow();

								//Si nb_resultat = 0 alors la lésion a transcoder n'est pas dans la table
								if (nb_resultat == 0){

									transcodage_ADICAP_CIMO3Morpho_API = null;
									transcodage_ADICAP_CIMO3Morpho_Vianey = null;

									if (transcodage[1].toString().equalsIgnoreCase("null")){
										//TRAITER LES CAS OU LE CODE EST COMPLET POUR RAJOUTER 
										//Si ADICAP[5] = 4 => M-8000/1
										if (code_ADICAP_a_traiter.substring(5, 6).equalsIgnoreCase("4")){
											transcodage_ADICAP_CIMO3Morpho_API = "M-8000/1";
											//System.out.println("	" + transcodage_ADICAP_CIMO3Morpho_API);
										}
										//Si ADICAP[5] = 5 => M-8000/2
										else if (code_ADICAP_a_traiter.substring(5, 6).equalsIgnoreCase("5")){
											transcodage_ADICAP_CIMO3Morpho_API = "M-8000/2";
											//System.out.println("	" + transcodage_ADICAP_CIMO3Morpho_API);
										}
										//Si ADICAP[5] = 6 || 7 => M-8000/3
										else if (code_ADICAP_a_traiter.substring(5, 6).equalsIgnoreCase("6") || code_ADICAP_a_traiter.substring(5, 6).equalsIgnoreCase("7")){
											transcodage_ADICAP_CIMO3Morpho_API = "M-8000/3";
											//System.out.println("	" + transcodage_ADICAP_CIMO3Morpho_API);
										}else {
											transcodage_ADICAP_CIMO3Morpho_API = null;
											//System.out.println("	" + transcodage_ADICAP_CIMO3Morpho_API);
										}
									}else {									
										transcodage_ADICAP_CIMO3Morpho_API = transcodage[1];
										//System.out.println("	" + transcodage_ADICAP_CIMO3Morpho_API);
									}

									String queryVianey = "SELECT `CIMO3_Morpho` FROM `transcodage_vianey_lesion_adicap_cimo3_morpho` WHERE `Lesion` = ?;";

									PreparedStatement prepareVianey = con.prepareStatement(queryVianey);

									try {
										prepareVianey.setString(1, lesion);
										ResultSet rs3 = prepareVianey.executeQuery();

										while (rs3.next()){
											transcodage_ADICAP_CIMO3Morpho_Vianey = rs3.getString("CIMO3_Morpho");
										}
									}catch (SQLException e1) {
										e1.printStackTrace();
									}

									//Cas où l'API et la table de transcodage de Vianey ne sont pas d'accord
									if ((transcodage_ADICAP_CIMO3Morpho_API != null && transcodage_ADICAP_CIMO3Morpho_Vianey != null) && !transcodage_ADICAP_CIMO3Morpho_API.equalsIgnoreCase(transcodage_ADICAP_CIMO3Morpho_Vianey)){
										transcode_a_valider = lesion + "|" + transcodage_ADICAP_CIMO3Morpho_API + "|" + transcodage_ADICAP_CIMO3Morpho_Vianey;
										transcodes_a_valider.add(transcode_a_valider);
									}									

								}

							} catch (SQLException e1) {
								e1.printStackTrace();
							}
						}
					}
				}
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}

		st.close();
		con.close();

		System.out.println("\nDurée d'exécution du transcodage : " + ((System.currentTimeMillis()-debut)/1000) + " secondes");

		return transcodes_a_valider;

	}

	public static void transcodageADICAP_CIMO3_Morpho () throws SQLException, JSONException, ParseException{

		long debut = System.currentTimeMillis();

		String code_ADICAP_a_traiter;
		String transcodage_ADICAP_CIMO3Morpho_API;
		String transcodage_ADICAP_CIMO3Morpho_Vianey;
		String transcodage_ADICAP_CIMO3_Topo = null;
		java.sql.PreparedStatement prepare1, prepare2, prepare3, prepare4, prepare5, prepare6;

		//REGEX permettant de ne selectionner que les cancers et les codes incomplets (cf. AZ00 en remplacement des ****** et Z dans la REGEX)
		Pattern pattern = Pattern.compile("^\\D{5}[4-7Z]");
		Matcher matcher;

		//REGEX permettant de ne selectionner les codes CIMO3 Morpho aspécifiques
		Pattern pattern1 = Pattern.compile("^M-8000");
		Matcher matcher1;

		/*
		//(Lesion|Transcodage_API|Transcodage_Vianey);
		Set<String> transcodes_a_valider = new HashSet<String>();
		String transcode_a_valider = null;
		 */

		//Connexion à la base de donnée
		String url= "jdbc:mysql://localhost/projet_ime_202";
		String user="root";
		String motpasse="";

		Connexion connect = new Connexion (url, user, motpasse);
		Connection con = connect.getCon();
		Statement st = null;

		//La requête de sélection des codes adicap à transcoder
		String query = "select NumAutoAdicap, adicap from adicap where Flag_Integration = 0";

		//La requête préparée de récupération des code à transcoder dans la table de Vianey
		String query1 = "select NumAuto FROM transcodage_adicap_cimo3_topo_valides WHERE Organe = ?";

		//La requête préparée de récupération des code à transcoder dans la table de Vianey
		String query2 = "select NumAuto FROM transcodage_adicap_cimo3_morpho_valides WHERE Lesion = ?";

		//La requête préparée d'ajout du transcodage dans la table de validée
		String query3 = "insert into transcodage_adicap_cimo3_topo_valides (Organe, CIMO3_Topo)"
				+ " values (?, ?);";

		//La requête préparée d'ajout du transcodage dans la table de validée
		String query4 = "insert into transcodage_adicap_cimo3_morpho_valides (Lesion, CIMO3_Morpho)"
				+ " values (?, ?);";

		//La requête d'ajout dans la table rel_data_cimo3_morpho
		String query5 = "insert into rel_adicap_cimo3 (NumLigneADICAP, CIMO3_Topo, CIMO3_Morpho) values (?, ?, ?)";

		//La requête de mise à jour du statut transcodé
		String query6 = "update adicap set adicap.Flag_Integration=1 WHERE NumAutoAdicap = ?";

		prepare1 = con.prepareStatement(query1);
		prepare2 = con.prepareStatement(query2);
		prepare3 = con.prepareStatement(query3);
		prepare4 = con.prepareStatement(query4);
		prepare5 = con.prepareStatement(query5);
		prepare6 = con.prepareStatement(query6);

		try {
			st = con.createStatement();
			ResultSet rs = st.executeQuery(query);
			con.setAutoCommit(false);
			while (rs.next()) {

				code_ADICAP_a_traiter = null;

				//Pré-traitement des codes ADICAP incomplet
				if (rs.getString("adicap").contains("*") == true){
					code_ADICAP_a_traiter = rs.getString("adicap").replaceAll("\\*+", "AZ00");
				}else code_ADICAP_a_traiter = rs.getString("adicap");

				//Tous les codes plus long que 8 sont coupés pour permettre le transcodage
				if (code_ADICAP_a_traiter.length() >=8) {
					code_ADICAP_a_traiter = code_ADICAP_a_traiter.substring(0, 8);
				}

				//Ne sont traités que les codes égaux à 8 cad tous les codes complets + les incomplets avec *****
				if(code_ADICAP_a_traiter.length() == 8){

					//On vérifie que le code concerne une tumeur malige cad tous les codes de tumeurs maligne + les incomplets avec *****
					matcher = pattern.matcher(code_ADICAP_a_traiter);
					if (matcher.find()){

						//Transcodage des codes ADICAP (0 = pas de tarduction du code et 1 = transcodage du code)
						String[] transcodage = Utilities.transcodage_ADICAP_CIMO3(code_ADICAP_a_traiter, 0, 1);

						String organe = code_ADICAP_a_traiter.substring(2, 4);

						try{
							prepare1.setString(1, organe);
							ResultSet rs1 = prepare1.executeQuery();
							rs1.last(); 
							int nb_resultat_organe = rs1.getRow();

							//Si nb_resultat = 0 alors l'organe a transcoder n'est pas dans la table
							if (nb_resultat_organe == 0){
								try{
									prepare3.setString(1, organe);
									if (transcodage[0].equalsIgnoreCase("null")){
										prepare3.setNull(2, Types.VARCHAR);
									}else prepare3.setString(2, transcodage[0]);
									prepare3.addBatch();
									prepare3.executeBatch();
									con.commit();
								} catch (SQLException e1) {
									e1.printStackTrace();
								}
							}
						} catch (SQLException e1) {
							e1.printStackTrace();
						}

						String lesion = code_ADICAP_a_traiter.substring(4, 8);
						if (!lesion.equalsIgnoreCase("AZ00")){
							try {
								prepare2.setString(1, lesion);
								ResultSet rs2 = prepare2.executeQuery();
								rs2.last(); 
								int nb_resultat = rs2.getRow();

								//Si nb_resultat = 0 alors la lésion a transcoder n'est pas dans la table
								if (nb_resultat == 0){

									transcodage_ADICAP_CIMO3Morpho_API = null;
									transcodage_ADICAP_CIMO3Morpho_Vianey = null;

									if (transcodage[1].toString().equalsIgnoreCase("null")){
										//TRAITER LES CAS OU LE CODE EST COMPLET POUR RAJOUTER 
										//Si ADICAP[5] = 4 => M-8000/1
										if (code_ADICAP_a_traiter.substring(5, 6).equalsIgnoreCase("4")){
											transcodage_ADICAP_CIMO3Morpho_API = "M-8000/1";
											//System.out.println("	" + transcodage_ADICAP_CIMO3Morpho_API);
										}
										//Si ADICAP[5] = 5 => M-8000/2
										else if (code_ADICAP_a_traiter.substring(5, 6).equalsIgnoreCase("5")){
											transcodage_ADICAP_CIMO3Morpho_API = "M-8000/2";
											//System.out.println("	" + transcodage_ADICAP_CIMO3Morpho_API);
										}
										//Si ADICAP[5] = 6 || 7 => M-8000/3
										else if (code_ADICAP_a_traiter.substring(5, 6).equalsIgnoreCase("6") || code_ADICAP_a_traiter.substring(5, 6).equalsIgnoreCase("7")){
											transcodage_ADICAP_CIMO3Morpho_API = "M-8000/3";
											//System.out.println("	" + transcodage_ADICAP_CIMO3Morpho_API);
										}else {
											transcodage_ADICAP_CIMO3Morpho_API = null;
											//System.out.println("	" + transcodage_ADICAP_CIMO3Morpho_API);
										}
									}else {									
										transcodage_ADICAP_CIMO3Morpho_API = transcodage[1];
										//System.out.println("	" + transcodage_ADICAP_CIMO3Morpho_API);
									}

									String queryVianey = "SELECT `CIMO3_Morpho` FROM `transcodage_vianey_lesion_adicap_cimo3_morpho` WHERE `Lesion` = ?;";

									PreparedStatement prepareVianey = con.prepareStatement(queryVianey);

									try {
										prepareVianey.setString(1, lesion);
										ResultSet rs3 = prepareVianey.executeQuery();

										while (rs3.next()){
											transcodage_ADICAP_CIMO3Morpho_Vianey = rs3.getString("CIMO3_Morpho");
										}
									}catch (SQLException e1) {
										e1.printStackTrace();
									}

									//Cas où l'API et la table de transcodage de Vianey sont d'accord
									if ((transcodage_ADICAP_CIMO3Morpho_API != null && transcodage_ADICAP_CIMO3Morpho_Vianey != null) && transcodage_ADICAP_CIMO3Morpho_API.equalsIgnoreCase(transcodage_ADICAP_CIMO3Morpho_Vianey)){
										prepare4.setString(1, lesion);
										prepare4.setString(2, transcodage_ADICAP_CIMO3Morpho_API);				
										prepare4.addBatch();
										prepare4.executeBatch();										
										con.commit();
									}
									//Cas où l'API arrive à transcoder et la table de transcodage de Vianey non
									else if (transcodage_ADICAP_CIMO3Morpho_API != null && transcodage_ADICAP_CIMO3Morpho_Vianey == null){
										prepare4.setString(1, lesion);
										prepare4.setString(2, transcodage_ADICAP_CIMO3Morpho_API);
										prepare4.addBatch();
										prepare4.executeBatch();
										con.commit();
									}
									//Cas où l'API n'arrive pas à transcoder et la table de transcodage de Vianey si
									else if (transcodage_ADICAP_CIMO3Morpho_API == null && transcodage_ADICAP_CIMO3Morpho_Vianey != null){
										prepare4.setString(1, lesion);
										prepare4.setString(2, transcodage_ADICAP_CIMO3Morpho_Vianey);
										prepare4.addBatch();
										prepare4.executeBatch();
										con.commit();
									}

									//Cas où l'API et la table de transcodage de Vianey ne sont pas d'accord mais que le code de Vianney est plus précis
									else if ((transcodage_ADICAP_CIMO3Morpho_API != null && transcodage_ADICAP_CIMO3Morpho_Vianey != null) && !transcodage_ADICAP_CIMO3Morpho_API.equalsIgnoreCase(transcodage_ADICAP_CIMO3Morpho_Vianey)){
										matcher1 = pattern1.matcher(transcodage_ADICAP_CIMO3Morpho_API);
										if (matcher1.find()){
											prepare4.setString(1, lesion);
											prepare4.setString(2, transcodage_ADICAP_CIMO3Morpho_Vianey);
											prepare4.addBatch();
											prepare4.executeBatch();
											con.commit();
										}
									}

									/*
									//Cas où l'API et la table de transcodage de Vianey ne sont pas d'accord
									else if ((transcodage_ADICAP_CIMO3Morpho_API != null && transcodage_ADICAP_CIMO3Morpho_Vianey != null) && !transcodage_ADICAP_CIMO3Morpho_API.equalsIgnoreCase(transcodage_ADICAP_CIMO3Morpho_Vianey)){
										transcode_a_valider = lesion + "|" + transcodage_ADICAP_CIMO3Morpho_API + "|" + transcodage_ADICAP_CIMO3Morpho_Vianey;
										transcodes_a_valider.add(transcode_a_valider);
									}
									 */								

									//Si nb_resultat = 1 alors la lésion a transcoder est déjà dans la table
								} else {
									//Ajouter dans la table REL_ADICAP_CIMO3_Morpho
									prepare5.setInt(1, rs.getInt("NumAutoAdicap"));
									try{
										prepare1.setString(1, organe);
										ResultSet rs3 = prepare1.executeQuery();
										while (rs3.next()){
											transcodage_ADICAP_CIMO3_Topo = rs3.getString("NumAuto");
										}
										if (transcodage_ADICAP_CIMO3_Topo.equalsIgnoreCase("null")){
											prepare5.setNull(2, Types.VARCHAR);
										}else {
											prepare5.setString(2, transcodage_ADICAP_CIMO3_Topo);
										}
										prepare5.setInt(3, rs2.getInt("NumAuto"));
									}catch (SQLException e1) {
										e1.printStackTrace();
									}

									prepare5.addBatch();
									prepare5.executeBatch();
									con.commit();

									//Update le Flag_Integration à 1
									prepare6.setInt(1, rs.getInt("NumAutoAdicap"));
									prepare6.addBatch();
									prepare6.executeBatch();
									con.commit();

								}

							} catch (SQLException e1) {
								e1.printStackTrace();
							}
						}
					}
				}
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}

		st.close();
		con.close();

		System.out.println("\nDurée d'exécution du transcodage : " + ((System.currentTimeMillis()-debut)/1000) + " secondes");

		/*SELECT adicap.NumAutoAdicap, adicap.Adicap, rel_data_cimo3_topo.CIMO3_Topo, transcodage_adicap_cimo3_morpho_valides.CIMO3_Morpho
		FROM adicap INNER JOIN rel_data_cimo3_topo ON adicap.NumAutoAdicap = rel_data_cimo3_topo.NumLigneSource
		INNER JOIN rel_data_cimo3_morpho ON adicap.NumAutoAdicap = rel_data_cimo3_morpho.NumLigneSource
		INNER JOIN transcodage_adicap_cimo3_morpho_valides ON rel_data_cimo3_morpho.CIMO3_Morpho = transcodage_adicap_cimo3_morpho_valides.NumAuto*/

	}

}
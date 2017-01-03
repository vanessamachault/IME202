package Integration;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import gov.nih.nlm.uts.webservice.AtomDTO;
import gov.nih.nlm.uts.webservice.Psf;
import gov.nih.nlm.uts.webservice.UiLabel;
import gov.nih.nlm.uts.webservice.UtsFault_Exception;
import gov.nih.nlm.uts.webservice.UtsWsContentController;
import gov.nih.nlm.uts.webservice.UtsWsContentControllerImplService;
import gov.nih.nlm.uts.webservice.UtsWsFinderController;
import gov.nih.nlm.uts.webservice.UtsWsFinderControllerImplService;
import gov.nih.nlm.uts.webservice.UtsWsMetadataController;
import gov.nih.nlm.uts.webservice.UtsWsMetadataControllerImplService;
import gov.nih.nlm.uts.webservice.UtsWsSecurityController;
import gov.nih.nlm.uts.webservice.UtsWsSecurityControllerImplService;


public class TranscodageCMI10_CIMO {

	public static void main(String[] args) throws JSONException, UtsFault_Exception, SQLException {

		long debut = System.currentTimeMillis();

		///Permet de transcoder les nouveau codes CIM10 ajoutés dans la base de donnée en CUI UMLS
		//Ces couplets CIM10/CUI UMLS sont stoqués dans la table transcodage_cim10_cui_umls dans l'attente d'une validation automatique ou manuelle
		transcodageCIM10_CUI();

		//Permet de rétrotanscoder les codes CUI obtenus par la méthode précédente en code CIM10
		retrotranscodageCUI_CIM10();

		//Si le transcodage est jugé automatiquement valide, il est ajouté dans la table des transcodage valide
		ajoutTranscodageCIM10_CUI_valides();

		//Méthode pour associer la ligne à la bonne personne
		ajoutCorrespondanceCIM10_CIMO3();

		//Liste des transcodes à valider manuellement
		ArrayList<String> test = listeTranscodageCIM10_CIMO3_a_valider();
		System.out.println("Liste des codes PMSI dont il faut valider le transcodage : ");
		for (int i = 0 ; i < test.size() ; i++){
			System.out.println(test.get(i));
		}

		//Liste des codes non transcodés
		ArrayList<String> test2 = liste_codes_PMSI_non_traduit();
		System.out.println("Liste des codes PMSI non transcodés : ");
		for (int i = 0 ; i < test2.size() ; i++){
			System.out.println("	- " + test2.get(i));
		}

		System.out.print("\nDurée d'exécution : " + (System.currentTimeMillis() - debut) / 1000);
		System.out.print(" secondes");
	}

	public static ArrayList<String> liste_codes_PMSI_non_traduit() throws SQLException, UtsFault_Exception{

		ArrayList<String> liste_PMSI_non_traduit = new ArrayList<String>();
		
		//Connexion à la base de donnée
		String url= "jdbc:mysql://localhost/projet_ime_202";
		String user="root";
		String motpasse="";

		Connexion connect = new Connexion (url, user, motpasse);
		Connection con = connect.getCon();
		
		Statement st = null;

		String query = "SELECT DISTINCT sejour.DP AS 'Code PMSI' FROM sejour WHERE sejour.Flag_Integration_DP = 0 "
				+ "UNION SELECT DISTINCT sejour.DR AS 'Code PMSI' FROM sejour WHERE sejour.Flag_Integration_DR = 0 "
				+ "UNION SELECT DISTINCT das.DAS AS 'Code PMSI' FROM das WHERE das.Flag_Integration_DAS = 0";
		
		try {
			st = con.createStatement();
			ResultSet rs = st.executeQuery(query);
			while (rs.next()) {
				liste_PMSI_non_traduit.add(rs.getString("Code PMSI"));
			}
		}catch (SQLException e1) {
			e1.printStackTrace();
		}
		
		return liste_PMSI_non_traduit;
	}

	//Permet de transcoder les nouveau codes CIM10 ajoutés dans la base de donnée en CUI UMLS
	//Ces couplets CIM10/CUI UMLS sont stoqués dans la table transcodage_cim10_cui_umls dans l'attente d'une validation automatique ou manuelle
	public static void transcodageCIM10_CUI() throws SQLException, UtsFault_Exception{

		String code_CIM10;

		//Connexion à la base de donnée
		String url= "jdbc:mysql://localhost/projet_ime_202";
		String user="root";
		String motpasse="";

		Connexion connect = new Connexion (url, user, motpasse);
		Connection con = connect.getCon();
		con.setAutoCommit(false);

		Statement st = null;

		//La requête de sélection des codes adicap à transcoder
		String query = "SELECT DISTINCT DP FROM `sejour` WHERE (DP like 'C%' OR DP like 'D0%') AND DP NOT IN (SELECT CIM10 FROM transcodage_cim10_cui_umls) "
				+ "UNION SELECT DISTINCT DR FROM `sejour` WHERE (DR like 'C%' OR DR like 'D0%') AND DR NOT IN (SELECT CIM10 FROM transcodage_cim10_cui_umls) "
				+ "UNION SELECT DISTINCT DAS FROM `das` WHERE (das like 'C%' OR das like 'D0%') AND DAS NOT IN (SELECT CIM10 FROM transcodage_cim10_cui_umls);";

		String query2 = "insert into transcodage_cim10_cui_umls (CIM10, CUI) VALUES (?, ?);";
		PreparedStatement prepare2 = con.prepareStatement(query2);

		int compte = 0;

		try {
			st = con.createStatement();
			ResultSet rs = st.executeQuery(query);
			while (rs.next()) {
				code_CIM10 = rs.getString("DP");

				compte = compte + 1;

				String cui = findConcepts(code_CIM10, "code");

				System.out.println("Code PMSI numéro " + compte);
				System.out.println("	- " + code_CIM10);
				System.out.println("	- " + cui);


				prepare2.setString(1, code_CIM10);
				if (cui.isEmpty()){
					prepare2.setNull(2, Types.VARCHAR);
				}else prepare2.setString(2, cui);

				prepare2.addBatch();

			}	
		}catch (SQLException e1) {
			e1.printStackTrace();
		}

		prepare2.executeBatch();
		con.commit();
	}

	//Permet de rétrotanscoder les codes CUI obtenus par la méthode précédente en code CIM10
	//Seuls les nouveaux codes CUI sont retranscodés en son ou ses codes CIM10
	public static void retrotranscodageCUI_CIM10() throws SQLException, UtsFault_Exception{

		//Connexion à la base de donnée

		String listeCUI;
		PreparedStatement prepareAddCUI;

		String url= "jdbc:mysql://localhost/projet_ime_202";
		String user="root";
		String motpasse="";

		Connexion connect = new Connexion (url, user, motpasse);
		Connection con = connect.getCon();
		con.setAutoCommit(false);

		Statement st = null;

		//La requête de sélection des codes adicap à transcoder
		String query = "SELECT CUI FROM transcodage_cim10_cui_umls WHERE CUI NOT IN (SELECT CUI FROM retrotranscodage_CUI_CIM10) AND CUI IS NOT NULL;";

		String queryAddCUI = "INSERT INTO retrotranscodage_CUI_CIM10 (CUI, CIM10) VALUES (?, ?)";

		prepareAddCUI = con.prepareStatement(queryAddCUI);

		try {
			st = con.createStatement();
			ResultSet rs = st.executeQuery(query);
			while (rs.next()) {

				listeCUI = null;
				listeCUI = getConceptAtoms(rs.getString("CUI"));

				String[] arrayCUI = listeCUI.split("\\|");

				for (int i = 0 ; i < arrayCUI.length ; i++){
					// A VOIR POUR TRAITER LES CAS OU IL Y A UN TIRRET AVEC UNE PROCEDURE SPECIFIQUE
					if (!rs.getString("CUI").contains("-")){
						prepareAddCUI.setString(1, rs.getString("CUI"));
						prepareAddCUI.setString(2, arrayCUI[i]);
						prepareAddCUI.addBatch();
					}

				}
			}
		}catch (SQLException e1) {
			e1.printStackTrace();
		}

		prepareAddCUI.executeBatch();
		con.commit();
	}

	//Permet de rajouter à la base de données les transcodes PMSI/CIMO3 validés automatiquement
	public static void ajoutTranscodageCIM10_CUI_valides() throws SQLException{

		//Connexion à la base de donnée
		String url= "jdbc:mysql://localhost/projet_ime_202";
		String user="root";
		String motpasse="";

		Connexion connect = new Connexion (url, user, motpasse);
		Connection con = connect.getCon();
		con.setAutoCommit(false);

		Statement st = null;

		//A vérifier
		String query = "SELECT retrotranscodage_cui_cim10.CUI, transcodage_cim10_cui_umls.CIM10 FROM retrotranscodage_cui_cim10 "
				+ "INNER JOIN transcodage_cim10_cui_umls ON retrotranscodage_cui_cim10.CUI=transcodage_cim10_cui_umls.CUI "
				+ "GROUP BY CUI HAVING count(retrotranscodage_cui_cim10.CIM10) = 1 AND retrotranscodage_cui_cim10.CUI "
				+ "NOT IN (SELECT DISTINCT transcodage_cim10_cimo3_valides.CUI FROM transcodage_cim10_cimo3_valides) "
				+ "AND retrotranscodage_cui_cim10.CUI IN (SELECT CUI FROM transcodage_vianey_cui_cimo3)";

		String query1 ="SELECT transcodage_vianey_cui_cimo3.CUI, transcodage_vianey_cui_cimo3.CIMO3_Topo, transcodage_vianey_cui_cimo3.CIMO3_Morpho "
				+ "FROM transcodage_vianey_cui_cimo3 WHERE transcodage_vianey_cui_cimo3.CUI = ? "
				+ "AND transcodage_vianey_cui_cimo3.CUI IN (SELECT CUI FROM transcodage_vianey_cui_cimo3 GROUP BY CUI HAVING count(CUI)=1);";

		String query2 = "INSERT INTO transcodage_cim10_cimo3_valides (CUI, CIM10, CIMO3_Topo, CIMO3_Morpho) VALUES (?, ?, ?, ?)";

		PreparedStatement prepare1 = con.prepareStatement(query1);
		PreparedStatement prepare2 = con.prepareStatement(query2);

		try {
			st = con.createStatement();
			ResultSet rs = st.executeQuery(query);
			while (rs.next()) {
				prepare1.setString(1, rs.getString("CUI"));
				ResultSet rs1 = prepare1.executeQuery();

				while (rs1.next()) {
					prepare2.setString(1, rs.getString("CUI"));
					prepare2.setString(2, rs.getString("CIM10"));
					prepare2.setString(3, rs1.getString("CIMO3_Topo"));
					prepare2.setString(4, rs1.getString("CIMO3_Morpho"));
					prepare2.addBatch();

				}
			}
		}catch (SQLException e1) {
			e1.printStackTrace();
		}

		prepare2.executeBatch();
		con.commit();
	}


	//Permet d'identifier les transcodes à valider manuellement du au fait qu'il existe plusieurs correspondance CUI -> CIM10
	public static void listeTranscodageCIM_10_CIMO_3_a_valider_CUI_doublon() throws SQLException{

		//Connexion à la base de donnée
		String url= "jdbc:mysql://localhost/projet_ime_202";
		String user="root";
		String motpasse="";

		Connexion connect = new Connexion (url, user, motpasse);
		Connection con = connect.getCon();

		Statement st = null;

		String query = "SELECT retrotranscodage_cui_cim10.CUI, transcodage_cim10_cui_umls.CIM10 FROM retrotranscodage_cui_cim10 "
				+ "INNER JOIN transcodage_cim10_cui_umls ON retrotranscodage_cui_cim10.CUI=transcodage_cim10_cui_umls.CUI "
				+ "GROUP BY CUI HAVING count(retrotranscodage_cui_cim10.CIM10) = 1 AND retrotranscodage_cui_cim10.CUI "
				+ "NOT IN (SELECT DISTINCT transcodage_cim10_cimo3_valides.CUI FROM transcodage_cim10_cimo3_valides) "
				+ "AND retrotranscodage_cui_cim10.CUI IN (SELECT CUI FROM transcodage_vianey_cui_cimo3)";

		String query1 = "SELECT transcodage_cim10_cui_umls.CIM10, transcodage_vianey_cui_cimo3.CUI, transcodage_vianey_cui_cimo3.CIMO3_Topo, "
				+ "transcodage_vianey_cui_cimo3.CIMO3_Morpho FROM transcodage_vianey_cui_cimo3 INNER JOIN transcodage_cim10_cui_umls "
				+ "ON transcodage_vianey_cui_cimo3.CUI = transcodage_cim10_cui_umls.CUI WHERE transcodage_vianey_cui_cimo3.CUI = ?";

		PreparedStatement prepare1 = con.prepareStatement(query1);

		System.out.println("Liste des codes dont il existe plusieurs CUI dans la table de transcodage de Vianney");
		System.out.println("CIM10 | CUI | CIMO3_Topo | Cimo3Morpho");

		try {
			st = con.createStatement();
			ResultSet rs = st.executeQuery(query);
			while (rs.next()) {
				System.out.println("\nCode CUI : " + rs.getString("CUI"));
				prepare1.setString(1, rs.getString("CUI"));
				ResultSet rs1 = prepare1.executeQuery();
				while (rs1.next()) {
					System.out.println("	-" + rs1.getString("CIM10") + "|" + rs1.getString("CUI") + "|" + rs1.getString("CIMO3_Topo") + "|" + rs1.getString("CIMO3_Morpho"));
				}
			}
		}
		catch (SQLException e1) {
			e1.printStackTrace();
		}

	}

	//Permet d'obtenir la liste des transcodes CIM10/CIMO3 à valider
	public static ArrayList<String> listeTranscodageCIM10_CIMO3_a_valider() throws SQLException{

		ArrayList<String> tanscodes_CIM10_CIMO3_a_valider = new ArrayList<>();
		String tanscode_CIM10_CIMO3_a_valider;

		//Connexion à la base de donnée
		String url= "jdbc:mysql://localhost/projet_ime_202";
		String user="root";
		String motpasse="";

		Connexion connect = new Connexion (url, user, motpasse);
		Connection con = connect.getCon();

		Statement st = null;

		//On valide les codes dont le statut de doublon est dû à des codes aspécifiques
		String query = "SELECT DISTINCT retrotranscodage_cui_cim10.CUI FROM retrotranscodage_cui_cim10 INNER JOIN transcodage_cim10_cui_umls "
				+ "ON retrotranscodage_cui_cim10.CUI=transcodage_cim10_cui_umls.CUI INNER JOIN transcodage_vianey_cui_cimo3 "
				+ "ON retrotranscodage_cui_cim10.CUI = transcodage_vianey_cui_cimo3.CUI WHERE retrotranscodage_cui_cim10.CUI "
				+ "NOT IN (SELECT DISTINCT transcodage_cim10_cimo3_valides.CUI FROM transcodage_cim10_cimo3_valides) "
				+ "AND retrotranscodage_cui_cim10.CUI IN (SELECT CUI FROM transcodage_vianey_cui_cimo3 GROUP BY CUI HAVING count(CUI)=1);";

		String query2 = "SELECT DISTINCT retrotranscodage_cui_cim10.CUI, transcodage_vianey_cui_cimo3.CIMO3_Topo, transcodage_vianey_cui_cimo3.CIMO3_Morpho, "
				+ "retrotranscodage_cui_cim10.CIM10 FROM retrotranscodage_cui_cim10 INNER JOIN transcodage_cim10_cui_umls "
				+ "ON retrotranscodage_cui_cim10.CUI=transcodage_cim10_cui_umls.CUI INNER JOIN transcodage_vianey_cui_cimo3 "
				+ "ON retrotranscodage_cui_cim10.CUI = transcodage_vianey_cui_cimo3.CUI WHERE retrotranscodage_cui_cim10.CUI "
				+ "NOT IN (SELECT DISTINCT transcodage_cim10_cimo3_valides.CUI FROM transcodage_cim10_cimo3_valides) AND "
				+ "retrotranscodage_cui_cim10.CUI IN (SELECT CUI FROM transcodage_vianey_cui_cimo3) AND retrotranscodage_cui_cim10.CUI = ? AND retrotranscodage_cui_cim10.CIM10 NOT LIKE '%-%'";

		String query3 = "INSERT INTO transcodage_cim10_cimo3_valides (CUI, CIM10, CIMO3_Topo, CIMO3_Morpho) VALUES (?, ?, ?, ?);";

		PreparedStatement prepare = con.prepareStatement(query2);
		PreparedStatement prepare3 = con.prepareStatement(query3);


		try {
			st = con.createStatement();
			ResultSet rs = st.executeQuery(query);
			while (rs.next()) {
				try {
					prepare.setString(1, rs.getString("CUI"));
					ResultSet rs2 = prepare.executeQuery();

					rs2.last(); 
					int nb_resultat = rs2.getRow();

					rs2.first();

					if (nb_resultat == 2){
						String CIM10_1 = rs2.getString("CIM10");
						rs2.next();
						String CIM10_2 = rs2.getString("CIM10");

						prepare3.setString(1, rs2.getString("CUI"));
						prepare3.setString(2, CIM10_1);
						prepare3.setString(3, rs2.getString("CIMO3_Topo"));
						prepare3.setString(4, rs2.getString("CIMO3_Morpho"));
						prepare3.executeUpdate();

						prepare3.setString(1, rs2.getString("CUI"));
						prepare3.setString(2, CIM10_2);
						prepare3.setString(3, rs2.getString("CIMO3_Topo"));
						prepare3.setString(4, rs2.getString("CIMO3_Morpho"));
						prepare3.executeUpdate();


					}else if (nb_resultat == 1){
						String CIM10_1 = rs2.getString("CIM10");

						prepare3.setString(1, rs2.getString("CUI"));
						prepare3.setString(2, CIM10_1);
						prepare3.setString(3, rs2.getString("CIMO3_Topo"));
						prepare3.setString(4, rs2.getString("CIMO3_Morpho"));
						prepare3.executeUpdate();
					}

				}catch (SQLException e1) {
					e1.printStackTrace();
				}


			}
		}catch (SQLException e1) {
			e1.printStackTrace();
		}

		//La requête de sélection des codes à valider
		String query1 = "SELECT DISTINCT transcodage_cim10_cui_umls.CIM10, retrotranscodage_cui_cim10.CUI, transcodage_vianey_cui_cimo3.CIMO3_Topo, "
				+ "transcodage_vianey_cui_cimo3.CIMO3_Morpho, retrotranscodage_cui_cim10.CIM10 FROM retrotranscodage_cui_cim10 INNER JOIN transcodage_cim10_cui_umls "
				+ "ON retrotranscodage_cui_cim10.CUI=transcodage_cim10_cui_umls.CUI INNER JOIN transcodage_vianey_cui_cimo3 "
				+ "ON retrotranscodage_cui_cim10.CUI = transcodage_vianey_cui_cimo3.CUI WHERE retrotranscodage_cui_cim10.CUI "
				+ "NOT IN (SELECT DISTINCT transcodage_cim10_cimo3_valides.CUI FROM transcodage_cim10_cimo3_valides) "
				+ "AND retrotranscodage_cui_cim10.CUI IN (SELECT CUI FROM transcodage_vianey_cui_cimo3) AND retrotranscodage_cui_cim10.CIM10 NOT LIKE '%-%'";

		try {
			st = con.createStatement();
			ResultSet rs1 = st.executeQuery(query1);
			System.out.println("CIM10 | CUI | RétrostrancodageCIM10 | CIMO3_Topo | Cimo3Morpho ");
			while (rs1.next()) {
				tanscode_CIM10_CIMO3_a_valider = rs1.getString("CIM10") + "|" + rs1.getString("CUI") + "|" + rs1.getString(5) + "|" + rs1.getString("CIMO3_Topo") + "|" + rs1.getString("CIMO3_Morpho");
				tanscodes_CIM10_CIMO3_a_valider.add(tanscode_CIM10_CIMO3_a_valider);				
			}
		}catch (SQLException e1) {
			e1.printStackTrace();
		}
		return tanscodes_CIM10_CIMO3_a_valider;
	}

	//A REFAIRE
	public static void ajoutCorrespondanceCIM10_CIMO3() throws SQLException{
		//Connexion à la base de donnée
		String url= "jdbc:mysql://localhost/projet_ime_202";
		String user="root";
		String motpasse="";

		Connexion connect = new Connexion (url, user, motpasse);
		Connection con = connect.getCon();
		con.setAutoCommit(false);

		PreparedStatement prepareAddDP,prepareAddDR, prepareAddDAS, prepareIntDP, prepareIntDR, prepareIntDAS;

		Statement st = null;

		String queryDP = "SELECT sejour.NumAutoSejour, sejour.DP, transcodage_cim10_cimo3_valides.NumAuto, transcodage_cim10_cui_umls.CUI, transcodage_cim10_cimo3_valides.CIMO3_Topo, "
				+ "transcodage_cim10_cimo3_valides.CIMO3_Morpho FROM sejour INNER JOIN transcodage_cim10_cui_umls ON sejour.DP = transcodage_cim10_cui_umls.CIM10 "
				+ "LEFT JOIN transcodage_cim10_cimo3_valides ON transcodage_cim10_cui_umls.CUI = transcodage_cim10_cimo3_valides.CUI "
				+ "WHERE sejour.NumAutoSejour NOT IN (SELECT rel_dp_cimo3.NumLigneDP FROM rel_DP_cimo3) "
				+ "AND transcodage_cim10_cui_umls.CUI IN (SELECT transcodage_cim10_cimo3_valides.CUI FROM transcodage_cim10_cimo3_valides) "
				+ "AND sejour.DP = transcodage_cim10_cimo3_valides.CIM10;";

		String queryDR = "SELECT sejour.NumAutoSejour, sejour.DR, transcodage_cim10_cimo3_valides.NumAuto, transcodage_cim10_cui_umls.CUI, transcodage_cim10_cimo3_valides.CIMO3_Topo, "
				+ "transcodage_cim10_cimo3_valides.CIMO3_Morpho FROM sejour INNER JOIN transcodage_cim10_cui_umls ON sejour.DR = transcodage_cim10_cui_umls.CIM10 "
				+ "LEFT JOIN transcodage_cim10_cimo3_valides ON transcodage_cim10_cui_umls.CUI = transcodage_cim10_cimo3_valides.CUI "
				+ "WHERE sejour.NumAutoSejour NOT IN (SELECT rel_dr_cimo3.NumLigneDR FROM rel_dr_cimo3) "
				+ "AND transcodage_cim10_cui_umls.CUI IN (SELECT transcodage_cim10_cimo3_valides.CUI FROM transcodage_cim10_cimo3_valides) "
				+ "AND sejour.DR = transcodage_cim10_cimo3_valides.CIM10;";

		String queryDAS = "SELECT das.NumAutoDAS, das.DAS, transcodage_cim10_cimo3_valides.NumAuto, transcodage_cim10_cui_umls.CUI, transcodage_cim10_cimo3_valides.CIMO3_Topo, "
				+ "transcodage_cim10_cimo3_valides.CIMO3_Morpho FROM das INNER JOIN transcodage_cim10_cui_umls ON das.DAS = transcodage_cim10_cui_umls.CIM10 "
				+ "LEFT JOIN transcodage_cim10_cimo3_valides ON transcodage_cim10_cui_umls.CUI = transcodage_cim10_cimo3_valides.CUI "
				+ "WHERE das.NumAutoDAS NOT IN (SELECT rel_das_cimo3.NumLigneDAS FROM rel_das_cimo3) "
				+ "AND transcodage_cim10_cui_umls.CUI IN (SELECT transcodage_cim10_cimo3_valides.CUI FROM transcodage_cim10_cimo3_valides) "
				+ "AND das.DAS = transcodage_cim10_cimo3_valides.CIM10;";

		String queryAddDP = "INSERT INTO rel_dp_cimo3 (NumLigneDP, CIMO3_Correspondance) VALUES (?, ?);"; 
		String queryAddDR = "INSERT INTO rel_dr_cimo3 (NumLigneDR, CIMO3_Correspondance) VALUES (?, ?);"; 
		String queryAddDAS = "INSERT INTO rel_das_cimo3 (NumLigneDAS, CIMO3_Correspondance) VALUES (?, ?);"; 

		String queryIntegrationDP = "UPDATE sejour SET sejour.Flag_Integration_DP = 1 WHERE NumAutoSejour = ?;";
		String queryIntegrationDR = "UPDATE sejour SET sejour.Flag_Integration_DR = 1 WHERE NumAutoSejour = ?;";
		String queryIntegrationDAS = "UPDATE das SET das.Flag_Integration_DAS = 1 WHERE NumAutoDAS = ?;";

		prepareAddDP = con.prepareStatement(queryAddDP);
		prepareAddDR = con.prepareStatement(queryAddDR);
		prepareAddDAS = con.prepareStatement(queryAddDAS);
		prepareIntDP = con.prepareStatement(queryIntegrationDP);
		prepareIntDR = con.prepareStatement(queryIntegrationDR);
		prepareIntDAS = con.prepareStatement(queryIntegrationDAS);

		try {
			st = con.createStatement();
			ResultSet rs = st.executeQuery(queryDP);
			while (rs.next()) {
				prepareAddDP.setString(1, rs.getString("NumAutoSejour"));
				prepareAddDP.setString(2, rs.getString("NumAuto"));
				prepareAddDP.addBatch();

				//Flag_integration_DP
				prepareIntDP.setString(1, rs.getString("NumAutoSejour"));
				prepareIntDP.addBatch();
			}
		}catch (SQLException e1) {
			e1.printStackTrace();
		}

		try {
			st = con.createStatement();
			ResultSet rs1 = st.executeQuery(queryDR);
			while (rs1.next()) {
				prepareAddDR.setString(1, rs1.getString("NumAutoSejour"));
				prepareAddDR.setString(2, rs1.getString("NumAuto"));
				prepareAddDR.addBatch();

				//Flag_integration_DR
				prepareIntDR.setString(1, rs1.getString("NumAutoSejour"));
				prepareIntDR.addBatch();
			}
		}catch (SQLException e1) {
			e1.printStackTrace();
		}

		try {
			st = con.createStatement();
			ResultSet rs2 = st.executeQuery(queryDAS);
			while (rs2.next()) {
				prepareAddDAS.setString(1, rs2.getString("NumAutoDAS"));
				prepareAddDAS.setString(2, rs2.getString("NumAuto"));
				prepareAddDAS.addBatch();

				//Flag_integration_DAS
				prepareIntDAS.setString(1, rs2.getString("NumAutoDAS"));
				prepareIntDAS.addBatch();

			}
		}catch (SQLException e1) {
			e1.printStackTrace();
		}

		prepareAddDP.executeBatch();
		prepareAddDR.executeBatch();
		prepareAddDAS.executeBatch();
		prepareIntDP.executeBatch();
		prepareIntDR.executeBatch();
		prepareIntDAS.executeBatch();
		con.commit();


		/*SELECT sejour.NumAutoSejour, sejour.DP, transcodage_cim10_cui_umls.CUI, transcodage_cim10_cimo3_valides.CIMO3_Topo, transcodage_cim10_cimo3_valides.CIMO3_Morpho
		FROM sejour INNER JOIN transcodage_cim10_cui_umls ON sejour.DP = transcodage_cim10_cui_umls.CIM10
		INNER JOIN rel_dp_cimo3 ON sejour.NumAutoSejour = rel_dp_cimo3.NumLigneDP
		INNER JOIN transcodage_cim10_cimo3_valides ON rel_dp_cimo3.CIMO3_Correspondance = transcodage_cim10_cimo3_valides.NumAuto*/

	}

	public static String ConnexionUMLS (String apikey){

		String ticketGrantingTicket = null;

		try {
			UtsWsSecurityController utsSecurityService = (new UtsWsSecurityControllerImplService()).getUtsWsSecurityControllerImplPort();
			ticketGrantingTicket = utsSecurityService.getProxyGrantTicketWithApiKey(apikey);
		}catch (Exception e) {
			System.out.println("Error!!!" + e.getMessage());
		}
		return ticketGrantingTicket;
	}

	public static String getProxyTicket(String ticketGrantingTicket, String targetService){

		String serviceName = "http://umlsks.nlm.nih.gov";
		UtsWsSecurityController utsSecurityService = (new UtsWsSecurityControllerImplService()).getUtsWsSecurityControllerImplPort();

		try {
			return utsSecurityService.getProxyTicket(ticketGrantingTicket, serviceName);
		}catch (Exception e) {
			return "";
		}
	}

	public static String findConcepts(String str, String searchType) throws UtsFault_Exception {

		UtsWsSecurityController utsSecurityService = (new UtsWsSecurityControllerImplService()).getUtsWsSecurityControllerImplPort();
		UtsWsFinderController utsFinderService = (new UtsWsFinderControllerImplService()).getUtsWsFinderControllerImplPort();
		UtsWsMetadataController utsMetadataService = (new UtsWsMetadataControllerImplService()).getUtsWsMetadataControllerImplPort();

		//Connexion à l'UMLS
		String ticketGrantingTicket = ConnexionUMLS("1b3b3293-cb73-4895-b57e-d176a4bbfd71");
		String proxyTicket = getProxyTicket(ticketGrantingTicket, "http://umlsks.nlm.nih.gov");

		int pageNum = 1;

		Psf psf = new Psf();
		//exclude suppressible + obsolete term matches
		psf.setIncludeObsolete(false);
		psf.setIncludeSuppressible(false);
		psf.getIncludedSources().add("ICD10");

		List<UiLabel> results = new ArrayList<UiLabel>();
		String currentUmlsRelease = utsMetadataService.getCurrentUMLSVersion(proxyTicket);

		String cui = "";

		do {
			//you'll need a new service ticket to retrieve each page of your call.  Here is an example.
			proxyTicket = utsSecurityService.getProxyTicket(ticketGrantingTicket, "http://umlsks.nlm.nih.gov");
			psf.setPageNum(pageNum);
			results = utsFinderService.findConcepts(proxyTicket, currentUmlsRelease, searchType, str, "exact", psf);

			for (UiLabel result:results) {

				String ui = result.getUi();
				cui = cui + ui;
			}
			pageNum++;

		} while (results.size() > 0);
		return cui;
	}

	//Permet de retrouver les codes CIM10 à partir des CUI.
	public static String getConceptAtoms(String cui) throws UtsFault_Exception{

		String resultats = "";

		UtsWsContentController utsContentService = (new UtsWsContentControllerImplService()).getUtsWsContentControllerImplPort();
		UtsWsSecurityController utsSecurityService = (new UtsWsSecurityControllerImplService()).getUtsWsSecurityControllerImplPort();
		UtsWsMetadataController utsMetadataService = (new UtsWsMetadataControllerImplService()).getUtsWsMetadataControllerImplPort();

		//Connexion à l'UMLS
		String ticketGrantingTicket = ConnexionUMLS("1b3b3293-cb73-4895-b57e-d176a4bbfd71");
		String proxyTicket = getProxyTicket(ticketGrantingTicket, "http://umlsks.nlm.nih.gov");

		Psf psf = new Psf();
		//exclude suppressible + obsolete term matches
		psf.setIncludeObsolete(false);
		psf.setIncludeSuppressible(false);
		psf.getIncludedSources().add("ICD10");

		List<UiLabel> results = new ArrayList<UiLabel>();
		String currentUmlsRelease = utsMetadataService.getCurrentUMLSVersion(proxyTicket);
		List<AtomDTO> atoms = new ArrayList<AtomDTO>();

		do {
			//you'll need a new service ticket to retrieve each page of your call.  Here is an example.
			proxyTicket = utsSecurityService.getProxyTicket(ticketGrantingTicket, "http://umlsks.nlm.nih.gov");

			atoms = utsContentService.getConceptAtoms(proxyTicket, currentUmlsRelease, cui, psf);

			System.out.println("\nCode à transcoder : " + cui);
			System.out.println("Nombre de résultat : " + atoms.size());

			for (AtomDTO atom:atoms) {
				//String aui = atom.getUi();
				//String tty = atom.getTermType();
				//String name = atom.getTermString().getName();
				String sourceId = atom.getCode().getUi();
				System.out.println("	- " + sourceId);
				//String rsab = atom.getRootSource();

				if (resultats.equalsIgnoreCase("")){
					resultats = sourceId;
				} else resultats = resultats + "|" + sourceId;
			}
		} while (results.size() > 0);
		return resultats;
	}

}

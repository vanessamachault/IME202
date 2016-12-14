package Identification;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class IdentifyPatient {

	/**********************************************************************************************************************************/

	public static  void createTablePatient(String url,String user, String password) throws SQLException{
		ConnexionJdbcMySql connect = new ConnexionJdbcMySql(url, user, password); 
		Connection connectEx = connect.getConnect(); 
		Statement patientIdentity = connectEx.createStatement();

		String q1a = "DROP TABLE IF EXISTS patient;";
		String q1b = "CREATE TABLE IF NOT EXISTS patient("
				+ "NumAutoPatient INT(10) NOT NULL AUTO_INCREMENT,"
				+ "Nom VARCHAR(100),"
				+ "Prenom VARCHAR(100),"
				+ "Sexe INT(2),"
				+ "DDN_Jour TINYINT(2),"
				+ "DDN_Mois TINYINT(2),"
				+ "DDN_Annee SMALLINT(4),"
				+ "id_principale INT(10),"
				+ "PRIMARY KEY (NumAutoPatient));";
		try {
			patientIdentity.executeUpdate(q1a);
		} catch (Exception e2) {
			System.out.println("DROP TABLE 'patient' not OK");
			e2.printStackTrace();
			System.exit(-1);
		}

		try {
			patientIdentity.executeUpdate(q1b);
		} catch (Exception e2) {
			System.out.println("CREATE TABLE 'patient' not OK");
			e2.printStackTrace();
			System.exit(-1);
		}

	}
	public static  void createTableRelPatientACP(String url,String user, String password) throws SQLException{
		ConnexionJdbcMySql connect = new ConnexionJdbcMySql(url, user, password); 
		Connection connectEx = connect.getConnect(); 
		Statement patientIdentity = connectEx.createStatement();

		String q1a = "DROP TABLE IF EXISTS rel_Patient_ACP;";
		String q1b = "CREATE TABLE IF NOT EXISTS rel_Patient_ACP("
				+ "NumAutoSourceACP INT(10) NOT NULL AUTO_INCREMENT,"
				+ "NumPatient INT(10),"
				+ "Ligne_ACP INT(10),"
				+ "PRIMARY KEY (NumAutoSourceACP));";

		try {
			patientIdentity.executeUpdate(q1a);
		} catch (Exception e2) {
			System.out.println("DROP TABLE 'Rel_Patient_ACP' not OK");
			e2.printStackTrace();
			System.exit(-1);
		}

		try {
			patientIdentity.executeUpdate(q1b);
		} catch (Exception e2) {
			System.out.println("CREATE TABLE 'Rel_Patient_ACP' not OK");
			e2.printStackTrace();
			System.exit(-1);
		}

	}
	public static  void createTableRelPatientPMSI(String url,String user, String password) throws SQLException{
		ConnexionJdbcMySql connect = new ConnexionJdbcMySql(url, user, password); 
		Connection connectEx = connect.getConnect(); 
		Statement patientIdentity = connectEx.createStatement();

		String q1a = "DROP TABLE IF EXISTS Rel_Patient_PMSI;";
		String q1b = "CREATE TABLE IF NOT EXISTS Rel_Patient_PMSI("
				+ "NumAutoSourcePMSI INT(10) NOT NULL AUTO_INCREMENT,"
				+ "NumPatient INT(10),"
				+ "Ligne_PMSI INT(10),"
				+ "PRIMARY KEY (NumAutoSourcePMSI));";

		try {
			patientIdentity.executeUpdate(q1a);
		} catch (Exception e2) {
			System.out.println("DROP TABLE 'Rel_Patient_PMSI' not OK");
			e2.printStackTrace();
			System.exit(-1);
		}

		try {
			patientIdentity.executeUpdate(q1b);
		} catch (Exception e2) {
			System.out.println("CREATE TABLE 'Rel_Patient_PMSI' not OK");
			e2.printStackTrace();
			System.exit(-1);
		}

	}

	/**********************************************************************************************************************************/	

	public static void importPatientToTablePatient(String url,String user, String password) throws SQLException{

		ConnexionJdbcMySql connect = new ConnexionJdbcMySql(url, user, password); 
		Connection connectEx = connect.getConnect(); 

		Statement patientIdentity = connectEx.createStatement();
		String query1 = "SELECT Nom, Prenom, Sexe, DDN_Jour, DDN_Mois, DDN_Annee "
				+ "FROM sejour "
				+ "UNION "
				+ "SELECT Nom, Prenom, Sexe, DDN_Jour, DDN_Mois, DDN_Annee "
				+ "FROM adicap_prelevement;";
		ResultSet result1 = patientIdentity.executeQuery(query1);
		ArrayList<String> arrayPatient = new ArrayList<>();

		String trait; 
		while(result1.next()) {
			String nom = result1.getString("Nom");
			String prenom = result1.getString("Prenom");
			String codeSexe = result1.getString("Sexe") ; 	
			String ddnD = result1.getString("DDN_Jour") ; 	
			String ddnM = result1.getString("DDN_Mois") ; 	
			String ddnY = result1.getString("DDN_Annee") ; 	
			trait = nom + "|"+ prenom + "|" + codeSexe + "|" + ddnD+ "|"+ ddnM + "|" + ddnY ;
			arrayPatient.add(trait);
		}
		result1.close();
		String[][] a = Util_LinhDan.arrayToTab2d(arrayPatient,6);

		String q1c = "INSERT INTO patient(Nom,Prenom,Sexe,DDN_Jour,DDN_Mois,DDN_Annee) VALUES (?, ?, ?, ?,?,?);";
		PreparedStatement pt = null;
		pt= connectEx.prepareStatement(q1c);

		for (int i = 0; i<a.length; i ++){  
			pt.setString(1, a[i][0]); 
			pt.setString(2, a[i][1]); 
			pt.setString(3, a[i][2]); 
			pt.setString(4, a[i][3]);  
			pt.setString(5, a[i][4]); 
			pt.setString(6, a[i][5]); 	
			pt.addBatch();
		}

		try {
			pt.executeBatch();
		} catch (Exception e) {
			System.out.println("INSERT INTO not OK");
			e.printStackTrace();
		}
	}
	public static void importRelPatientACP(String url,String user, String password) throws SQLException{
		ConnexionJdbcMySql connect = new ConnexionJdbcMySql(url, user, password); 
		Connection connectEx = connect.getConnect(); 
		Statement patientIdentity = connectEx.createStatement();

		String query1 = "SELECT NumAutoPatient, NumAutoAdicapPrel "
				+ "FROM patient AS r1 INNER JOIN adicap_prelevement AS r2 "
				+ "ON (r1.Nom = r2.Nom) AND (r1.Prenom = r2.Prenom) AND (r1.Sexe = r2.Sexe) AND (r1.DDN_Jour = r2.DDN_Jour);";
		ResultSet result1 = patientIdentity.executeQuery(query1);
		ArrayList<String> array = new ArrayList<>();

		while(result1.next()) {
			String numAutoPatient = result1.getString("NumAutoPatient");
			String rec_ACP = result1.getString("NumAutoAdicapPrel");
			array.add(numAutoPatient+"|"+ rec_ACP);
		}
		result1.close();
		String[][] a = Util_LinhDan.arrayToTab2d(array,2);

		String q1c = "INSERT INTO rel_patient_acp(NumPatient,Ligne_ACP) VALUES (?, ?);";
		PreparedStatement pt = null;
		pt= connectEx.prepareStatement(q1c);

		for (int i = 0; i<a.length; i ++){  
			pt.setString(1, a[i][0]); 
			pt.setString(2, a[i][1]); 	
			pt.addBatch();
		}

		try {
			pt.executeBatch();
		} catch (Exception e) {
			System.out.println("INSERT INTO not OK");
			e.printStackTrace();
		}
	}
	public static void importRelPatientPMSI(String url,String user, String password) throws SQLException{
		ConnexionJdbcMySql connect = new ConnexionJdbcMySql(url, user, password); 
		Connection connectEx = connect.getConnect(); 
		Statement patientIdentity = connectEx.createStatement();

		String query1 = "SELECT NumAutoPatient,NumAutoSejour "
				+ "FROM patient AS r1 INNER JOIN sejour AS r2 "
				+ "ON (r1.Nom = r2.Nom) AND (r1.Prenom = r2.Prenom) AND (r1.Sexe = r2.Sexe) AND (r1.DDN_Jour = r2.DDN_Jour);";
		ResultSet result1 = patientIdentity.executeQuery(query1);
		ArrayList<String> array = new ArrayList<>();

		while(result1.next()) {
			String numAutoPatient = result1.getString("NumAutoPatient");
			String rec_PMSI = result1.getString("NumAutoSejour");
			array.add(numAutoPatient+"|"+ rec_PMSI);
		}
		result1.close();
		String[][] a = Util_LinhDan.arrayToTab2d(array,2);

		String q1c = "INSERT INTO rel_patient_pmsi(NumPatient,Ligne_PMSI) VALUES (?,?);";
		PreparedStatement pt = null;
		pt= connectEx.prepareStatement(q1c);

		for (int i = 0; i<a.length; i ++){  
			pt.setString(1, a[i][0]); 
			pt.setString(2, a[i][1]); 	
			pt.addBatch();
		}

		try {
			pt.executeBatch();
		} catch (Exception e) {
			System.out.println("INSERT INTO not OK");
			e.printStackTrace();
		}
	}

	/**********************************************************************************************************************************/

	public static String [][] get2dArrayPatientWithID(String url,String user, String password) throws SQLException{

		ConnexionJdbcMySql connect = new ConnexionJdbcMySql(url, user, password); 
		Connection connectEx = connect.getConnect(); 
		Statement patientIdentity = null;
		patientIdentity = connectEx.createStatement();

		String q = "SELECT NumAutoPatient,Nom,Prenom,Sexe,DDN_Jour,DDN_Mois,DDN_Annee FROM patient;";
		ResultSet result1 = patientIdentity.executeQuery(q);

		ArrayList<String> arrayPatient = new ArrayList<>();
		while(result1.next()) {
			String numAutoPatient = result1.getString("NumAutoPatient");
			String nom = result1.getString("Nom");
			String prenom = result1.getString("Prenom");
			String codeSexe = result1.getString("Sexe") ; 	
			String ddnD = result1.getString("DDN_Jour") ; 	
			String ddnM = result1.getString("DDN_Mois") ; 	
			String ddnY = result1.getString("DDN_Annee") ; 	
			String p = numAutoPatient + "|"+ nom + "|"+ prenom + "|" + codeSexe + "|" + ddnD+ "|"+ ddnM + "|" + ddnY ;
			arrayPatient.add(p);
		}
		result1.close();

		String[][] a = Util_LinhDan.arrayToTab2d(arrayPatient,7);

		return a;
	}

	/**********************************************************************************************************************************/
	static int atPercent = 80;

	public static List<List<String>> method123456SoundEx(String url,String user, String password){
		String[][] a = null;

		try {
			a = IdentifyPatient.get2dArrayPatientWithID(url,user,password);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		String s1,s1a,s1b,s2,s2a,s2aInv,s2b;
		String soundExNom1,soundExPrenom1,soundExNom2,soundExPrenom2;
		int percent1=0;
		int percent2=0;
		int percent3=0;

		List<List<String>> superArray = new ArrayList<List<String>>();

		for (int i=0; i<a.length;i++){
			ArrayList<String> al = new ArrayList<String>();
			s1 = a[i][0]+"|"+a[i][1]+"|"+a[i][2]+"|"+a[i][3]+"|"+a[i][4]+"|"+a[i][5]+"|"+a[i][6];	
			s1a = a[i][1]+"|"+a[i][2]+"|"+a[i][3]+"|"+a[i][4]+"|"+a[i][5]+"|"+a[i][6];	
			s1b = a[i][3]+"|"+a[i][4]+"|"+a[i][5]+"|"+a[i][6];
			soundExNom1 = Util_LinhDan.soundExFr(a[i][1]);
			soundExPrenom1 = Util_LinhDan.soundExFr(a[i][2]);

			for (int j=i+1; j<a.length;j++){
				s2 = a[j][0]+"|"+a[j][1]+"|"+a[j][2]+"|"+a[j][3]+"|"+a[j][4]+"|"+a[j][5]+"|"+a[j][6];
				s2a = a[j][1]+"|"+a[j][2]+"|"+a[j][3]+"|"+a[j][4]+"|"+a[j][5]+"|"+a[j][6];
				s2aInv = a[j][2]+"|"+a[j][1]+"|"+a[j][3]+"|"+a[j][4]+"|"+a[j][5]+"|"+a[j][6];
				s2b = a[j][3]+"|"+a[j][4]+"|"+a[j][5]+"|"+a[j][6];
				soundExNom2 = Util_LinhDan.soundExFr(a[j][1]);
				soundExPrenom2 = Util_LinhDan.soundExFr(a[j][2]);
				percent1 = Util_LinhDan.perSimilarity(s1a,s2a); 
				percent2 = Util_LinhDan.perSimilarity(s1a,s2aInv); 
				percent3 = Util_LinhDan.perSimilarity(s1b,s2b); 

				if (percent1>atPercent || percent2>atPercent || (percent3>atPercent && (soundExNom1.equals(soundExNom2)&& soundExPrenom1.equals(soundExPrenom2))||
						(soundExNom1.equals(soundExPrenom2)&& soundExPrenom1.equals(soundExNom1)))){
					al.add(s1);
					al.add(s2);	
				}
			}
			List<String> t2 = Util_LinhDan.arrayToList(al);

			if(!t2.isEmpty()){
				superArray.add(t2);
			}
		}
		return superArray;
	}
	public static List<List<String>> method23456SoundEx(String url,String user, String password){
		String[][] a = null;

		try {
			a = IdentifyPatient.get2dArrayPatientWithID(url,user,password);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		String s1,s1a,s1b,s2,s2a,s2aInv,s2b;
		String soundExNom1,soundExPrenom1,soundExNom2,soundExPrenom2;
		int percent1=0;
		int percent2=0;
		int percent3=0;

		List<List<String>> superArray = new ArrayList<List<String>>();

		for (int i=0; i<a.length;i++){
			ArrayList<String> al = new ArrayList<String>();
			s1 = a[i][0]+"|"+a[i][1]+"|"+a[i][2]+"|"+a[i][3]+"|"+a[i][4]+"|"+a[i][5]+"|"+a[i][6];	
			s1a = a[i][2]+"|"+a[i][3]+"|"+a[i][4]+"|"+a[i][5]+"|"+a[i][6];	
			s1b = a[i][3]+"|"+a[i][4]+"|"+a[i][5]+"|"+a[i][6];
			soundExNom1 = Util_LinhDan.soundExFr(a[i][1]);
			soundExPrenom1 = Util_LinhDan.soundExFr(a[i][2]);

			for (int j=i+1; j<a.length;j++){
				s2 = a[j][0]+"|"+a[j][1]+"|"+a[j][2]+"|"+a[j][3]+"|"+a[j][4]+"|"+a[j][5]+"|"+a[j][6];
				s2a = a[j][2]+"|"+a[j][3]+"|"+a[j][4]+"|"+a[j][5]+"|"+a[j][6];
				s2aInv = a[j][1]+"|"+a[j][3]+"|"+a[j][4]+"|"+a[j][5]+"|"+a[j][6];
				s2b = a[j][3]+"|"+a[j][4]+"|"+a[j][5]+"|"+a[j][6];
				soundExNom2 = Util_LinhDan.soundExFr(a[j][1]);
				soundExPrenom2 = Util_LinhDan.soundExFr(a[j][2]);
				percent1 = Util_LinhDan.perSimilarity(s1a,s2a); 
				percent2 = Util_LinhDan.perSimilarity(s1a,s2aInv); 
				percent3 = Util_LinhDan.perSimilarity(s1b,s2b); 

				if (percent1>atPercent || percent2>atPercent || (percent3>atPercent && (soundExNom1.equals(soundExNom2)&& soundExPrenom1.equals(soundExPrenom2))||
						(soundExNom1.equals(soundExPrenom2)&& soundExPrenom1.equals(soundExNom1)))){
					al.add(s1);
					al.add(s2);	
				}
				List<String> t2 = Util_LinhDan.arrayToList(al);

				if(!t2.isEmpty()){
					superArray.add(t2);
				}
			}
		}
		return superArray;
	}
	public static List<List<String>> method13456SoundEx(String url,String user, String password){
		String[][] a = null;

		try {
			a = IdentifyPatient.get2dArrayPatientWithID(url,user,password);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		String s1,s1a,s1b,s2,s2a,s2aInv,s2b;
		String soundExNom1,soundExPrenom1,soundExNom2,soundExPrenom2;
		int percent1=0;
		int percent2=0;
		int percent3=0;

		List<List<String>> superArray = new ArrayList<List<String>>();

		for (int i=0; i<a.length;i++){
			ArrayList<String> al = new ArrayList<String>();
			s1 = a[i][0]+"|"+a[i][1]+"|"+a[i][2]+"|"+a[i][3]+"|"+a[i][4]+"|"+a[i][5]+"|"+a[i][6];	
			s1a = a[i][1]+"|"+a[i][3]+"|"+a[i][4]+"|"+a[i][5]+"|"+a[i][6];	
			s1b = a[i][3]+"|"+a[i][4]+"|"+a[i][5]+"|"+a[i][6];
			soundExNom1 = Util_LinhDan.soundExFr(a[i][1]);
			soundExPrenom1 = Util_LinhDan.soundExFr(a[i][2]);

			for (int j=i+1; j<a.length;j++){
				s2 = a[j][0]+"|"+a[j][1]+"|"+a[j][2]+"|"+a[j][3]+"|"+a[j][4]+"|"+a[j][5]+"|"+a[j][6];
				s2a = a[j][1]+"|"+a[j][3]+"|"+a[j][4]+"|"+a[j][5]+"|"+a[j][6];
				s2aInv = a[j][2]+"|"+a[j][3]+"|"+a[j][4]+"|"+a[j][5]+"|"+a[j][6];
				s2b = a[j][3]+"|"+a[j][4]+"|"+a[j][5]+"|"+a[j][6];
				soundExNom2 = Util_LinhDan.soundExFr(a[j][1]);
				soundExPrenom2 = Util_LinhDan.soundExFr(a[j][2]);
				percent1 = Util_LinhDan.perSimilarity(s1a,s2a); 
				percent2 = Util_LinhDan.perSimilarity(s1a,s2aInv); 
				percent3 = Util_LinhDan.perSimilarity(s1b,s2b); 

				if (percent1>atPercent || percent2>atPercent || (percent3>atPercent && (soundExNom1.equals(soundExNom2)&& soundExPrenom1.equals(soundExPrenom2))||
						(soundExNom1.equals(soundExPrenom2)&& soundExPrenom1.equals(soundExNom1)))){
					al.add(s1);
					al.add(s2);
				}
				List<String> t2 = Util_LinhDan.arrayToList(al);

				if(!t2.isEmpty()){
					superArray.add(t2);
				}
			}
		}
		return superArray;
	}
	public static List<List<String>> method12345SoundEx(String url,String user, String password){
		String[][] a = null;

		try {
			a = IdentifyPatient.get2dArrayPatientWithID(url,user,password);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		String s1,s1a,s1b,s2,s2a,s2aInv,s2b;
		String soundExNom1,soundExPrenom1,soundExNom2,soundExPrenom2;
		int percent1=0;
		int percent2=0;
		int percent3=0;
		List<List<String>> superArray = new ArrayList<List<String>>();

		for (int i=0; i<a.length;i++){
			ArrayList<String> al = new ArrayList<String>();
			s1 = a[i][0]+"|"+a[i][1]+"|"+a[i][2]+"|"+a[i][3]+"|"+a[i][4]+"|"+a[i][5]+"|"+a[i][6];	
			s1a = a[i][1]+"|"+a[i][2]+"|"+a[i][3]+"|"+a[i][4]+"|"+a[i][5];	
			s1b = a[i][3]+"|"+a[i][4]+"|"+a[i][5];
			soundExNom1 = Util_LinhDan.soundExFr(a[i][1]);
			soundExPrenom1 = Util_LinhDan.soundExFr(a[i][2]);

			for (int j=i+1; j<a.length;j++){
				s2 = a[j][0]+"|"+a[j][1]+"|"+a[j][2]+"|"+a[j][3]+"|"+a[j][4]+"|"+a[j][5]+"|"+a[j][6];
				s2a = a[j][1]+"|"+a[j][2]+"|"+a[j][3]+"|"+a[j][4]+"|"+a[j][5];
				s2aInv = a[j][2]+"|"+a[j][1]+"|"+a[j][3]+"|"+a[j][4]+"|"+a[j][5];
				s2b = a[j][3]+"|"+a[j][4]+"|"+a[j][5];
				soundExNom2 = Util_LinhDan.soundExFr(a[j][1]);
				soundExPrenom2 = Util_LinhDan.soundExFr(a[j][2]);
				percent1 = Util_LinhDan.perSimilarity(s1a,s2a); 
				percent2 = Util_LinhDan.perSimilarity(s1a,s2aInv); 
				percent3 = Util_LinhDan.perSimilarity(s1b,s2b); 

				if (percent1>atPercent || percent2>atPercent || (percent3>atPercent && (soundExNom1.equals(soundExNom2)&& soundExPrenom1.equals(soundExPrenom2))||
						(soundExNom1.equals(soundExPrenom2)&& soundExPrenom1.equals(soundExNom1)))){
					al.add(s1);
					al.add(s2);
				}
				List<String> t2 = Util_LinhDan.arrayToList(al);

				if(!t2.isEmpty()){
					superArray.add(t2);
				}
			}
		}
		return superArray;
	}
	public static List<List<String>> method12456SoundEx(String url,String user, String password){
		String[][] a = null;

		try {
			a = IdentifyPatient.get2dArrayPatientWithID(url,user,password);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		String s1,s1a,s1b,s2,s2a,s2aInv,s2b;
		String soundExNom1,soundExPrenom1,soundExNom2,soundExPrenom2;
		int percent1=0;
		int percent2=0;
		int percent3=0;
		List<List<String>> superArray = new ArrayList<List<String>>();

		for (int i=0; i<a.length;i++){
			ArrayList<String> al = new ArrayList<String>();
			s1 = a[i][0]+"|"+a[i][1]+"|"+a[i][2]+"|"+a[i][3]+"|"+a[i][4]+"|"+a[i][5]+"|"+a[i][6];	
			s1a = a[i][1]+"|"+a[i][2]+"|"+a[i][4]+"|"+a[i][5]+"|"+a[i][6];	
			s1b = a[i][3]+"|"+a[i][4]+"|"+a[i][5]+"|"+a[i][6];
			soundExNom1 = Util_LinhDan.soundExFr(a[i][1]);
			soundExPrenom1 = Util_LinhDan.soundExFr(a[i][2]);

			for (int j=i+1; j<a.length;j++){
				s2 = a[j][0]+"|"+a[j][1]+"|"+a[j][2]+"|"+a[j][3]+"|"+a[j][4]+"|"+a[j][5]+"|"+a[j][6];
				s2a = a[j][1]+"|"+a[j][2]+"|"+a[j][4]+"|"+a[j][5]+"|"+a[j][6];
				s2aInv = a[j][2]+"|"+a[j][1]+"|"+a[j][4]+"|"+a[j][5]+"|"+a[j][6];
				s2b = a[j][4]+"|"+a[j][5]+"|"+a[j][6];
				soundExNom2 = Util_LinhDan.soundExFr(a[j][1]);
				soundExPrenom2 = Util_LinhDan.soundExFr(a[j][2]);
				percent1 = Util_LinhDan.perSimilarity(s1a,s2a); 
				percent2 = Util_LinhDan.perSimilarity(s1a,s2aInv); 
				percent3 = Util_LinhDan.perSimilarity(s1b,s2b); 

				if (percent1>atPercent || percent2>atPercent || (percent3>atPercent && (soundExNom1.equals(soundExNom2)&& soundExPrenom1.equals(soundExPrenom2))||
						(soundExNom1.equals(soundExPrenom2)&& soundExPrenom1.equals(soundExNom1)))){
					al.add(s1);
					al.add(s2);
				}
				List<String> t2 = Util_LinhDan.arrayToList(al);

				if(!t2.isEmpty()){
					superArray.add(t2);
				}
			}
		}
		return superArray;
	}
	public static List<List<String>> method123SoundEx(String url,String user, String password){
		String[][] a = null;

		try {
			a = IdentifyPatient.get2dArrayPatientWithID(url,user,password);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		String s1,s1a,s1b,s2,s2a,s2aInv,s2b;
		String soundExNom1,soundExPrenom1,soundExNom2,soundExPrenom2;
		int percent1=0;
		int percent2=0;
		int percent3=0;
		List<List<String>> superArray = new ArrayList<List<String>>();

		for (int i=0; i<a.length;i++){
			ArrayList<String> al = new ArrayList<String>();
			s1 = a[i][0]+"|"+a[i][1]+"|"+a[i][2]+"|"+a[i][3]+"|"+a[i][4]+"|"+a[i][5]+"|"+a[i][6];	
			s1a = a[i][1]+"|"+a[i][2]+"|"+a[i][3];
			s1b = a[i][3];
			soundExNom1 = Util_LinhDan.soundExFr(a[i][1]);
			soundExPrenom1 = Util_LinhDan.soundExFr(a[i][2]);

			for (int j=i+1; j<a.length;j++){
				s2 = a[j][0]+"|"+a[j][1]+"|"+a[j][2]+"|"+a[j][3]+"|"+a[j][4]+"|"+a[j][5]+"|"+a[j][6];
				s2a = a[j][1]+"|"+a[j][2]+"|"+a[j][3];
				s2aInv = a[j][2]+"|"+a[j][1]+"|"+a[j][3];
				s2b = a[j][3];
				soundExNom2 = Util_LinhDan.soundExFr(a[j][1]);
				soundExPrenom2 = Util_LinhDan.soundExFr(a[j][2]);
				percent1 = Util_LinhDan.perSimilarity(s1a,s2a); 
				percent2 = Util_LinhDan.perSimilarity(s1a,s2aInv); 
				percent3 = Util_LinhDan.perSimilarity(s1b,s2b); 

				if (percent1>atPercent || percent2>atPercent || (percent3>atPercent && (soundExNom1.equals(soundExNom2)&& soundExPrenom1.equals(soundExPrenom2))||
						(soundExNom1.equals(soundExPrenom2)&& soundExPrenom1.equals(soundExNom1)))){
					al.add(s1);
					al.add(s2);
				}
				List<String> t2 = Util_LinhDan.arrayToList(al);

				if(!t2.isEmpty()){
					superArray.add(t2);
				}
			}
		}
		return superArray;
	}
	public static List<List<String>> method123456(String url,String user, String password){
		String[][] a = null;

		try {
			a = IdentifyPatient.get2dArrayPatientWithID(url,user,password);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		String s1,s1a,s2,s2a,s2aInv;
		int percent1=0;
		int percent2=0;
		List<List<String>> superArray = new ArrayList<List<String>>();

		for (int i=0; i<a.length;i++){
			ArrayList<String> al = new ArrayList<String>();
			s1 = a[i][0]+"|"+a[i][1]+"|"+a[i][2]+"|"+a[i][3]+"|"+a[i][4]+"|"+a[i][5]+"|"+a[i][6];	
			s1a = a[i][1]+"|"+a[i][2]+"|"+a[i][3]+"|"+a[i][4]+"|"+a[i][5]+"|"+a[i][6];	

			for (int j=i+1; j<a.length;j++){
				s2 = a[j][0]+"|"+a[j][1]+"|"+a[j][2]+"|"+a[j][3]+"|"+a[j][4]+"|"+a[j][5]+"|"+a[j][6];
				s2a = a[j][1]+"|"+a[j][2]+"|"+a[j][3]+"|"+a[j][4]+"|"+a[j][5]+"|"+a[j][6];
				s2aInv = a[j][2]+"|"+a[j][1]+"|"+a[j][3]+"|"+a[j][4]+"|"+a[j][5]+"|"+a[j][6];

				percent1 = Util_LinhDan.perSimilarity(s1a,s2a); 
				percent2 = Util_LinhDan.perSimilarity(s1a,s2aInv); 

				if (percent1>atPercent || percent2>atPercent){
					al.add(s1);
					al.add(s2);
				}
				List<String> t2 = Util_LinhDan.arrayToList(al);

				if(!t2.isEmpty()){
					superArray.add(t2);
				}
			}
		}
		return superArray;
	}
	public static List<List<String>> method23456(String url,String user, String password){
		String[][] a = null;

		try {
			a = IdentifyPatient.get2dArrayPatientWithID(url,user,password);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		String s1,s1a,s2,s2a,s2aInv;
		int percent1=0;
		int percent2=0;
		List<List<String>> superArray = new ArrayList<List<String>>();

		for (int i=0; i<a.length;i++){
			ArrayList<String> al = new ArrayList<String>();
			s1 = a[i][0]+"|"+a[i][1]+"|"+a[i][2]+"|"+a[i][3]+"|"+a[i][4]+"|"+a[i][5]+"|"+a[i][6];	
			s1a = a[i][2]+"|"+a[i][3]+"|"+a[i][4]+"|"+a[i][5]+"|"+a[i][6];	

			for (int j=i+1; j<a.length;j++){
				s2 = a[j][0]+"|"+a[j][1]+"|"+a[j][2]+"|"+a[j][3]+"|"+a[j][4]+"|"+a[j][5]+"|"+a[j][6];
				s2a = a[j][2]+"|"+a[j][3]+"|"+a[j][4]+"|"+a[j][5]+"|"+a[j][6];
				s2aInv = a[j][1]+"|"+a[j][3]+"|"+a[j][4]+"|"+a[j][5]+"|"+a[j][6];

				percent1 = Util_LinhDan.perSimilarity(s1a,s2a); 
				percent2 = Util_LinhDan.perSimilarity(s1a,s2aInv); 

				if (percent1>atPercent || percent2>atPercent){
					al.add(s1);
					al.add(s2);
				}
				List<String> t2 = Util_LinhDan.arrayToList(al);

				if(!t2.isEmpty()){
					superArray.add(t2);
				}
			}
		}
		return superArray;
	}
	public static List<List<String>> method13456(String url,String user, String password){
		String[][] a = null;

		try {
			a = IdentifyPatient.get2dArrayPatientWithID(url,user,password);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		String s1,s1a,s2,s2a,s2aInv;
		int percent1=0;
		int percent2=0;
		List<List<String>> superArray = new ArrayList<List<String>>();

		for (int i=0; i<a.length;i++){
			ArrayList<String> al = new ArrayList<String>();
			s1 = a[i][0]+"|"+a[i][1]+"|"+a[i][2]+"|"+a[i][3]+"|"+a[i][4]+"|"+a[i][5]+"|"+a[i][6];	
			s1a = a[i][1]+"|"+a[i][3]+"|"+a[i][4]+"|"+a[i][5]+"|"+a[i][6];	

			for (int j=i+1; j<a.length;j++){
				s2 = a[j][0]+"|"+a[j][1]+"|"+a[j][2]+"|"+a[j][3]+"|"+a[j][4]+"|"+a[j][5]+"|"+a[j][6];
				s2a = a[j][1]+"|"+a[j][3]+"|"+a[j][4]+"|"+a[j][5]+"|"+a[j][6];
				s2aInv = a[j][2]+"|"+a[j][3]+"|"+a[j][4]+"|"+a[j][5]+"|"+a[j][6];

				percent1 = Util_LinhDan.perSimilarity(s1a,s2a); 
				percent2 = Util_LinhDan.perSimilarity(s1a,s2aInv); 

				if (percent1>atPercent || percent2>atPercent){
					al.add(s1);
					al.add(s2);
				}
				List<String> t2 = Util_LinhDan.arrayToList(al);

				if(!t2.isEmpty()){
					superArray.add(t2);
				}
			}
		}
		return superArray;
	}
	public static List<List<String>> method12345(String url,String user, String password){
		String[][] a = null;

		try {
			a = IdentifyPatient.get2dArrayPatientWithID(url,user,password);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		String s1,s1a,s2,s2a,s2aInv;
		int percent1=0;
		int percent2=0;
		List<List<String>> superArray = new ArrayList<List<String>>();

		for (int i=0; i<a.length;i++){
			ArrayList<String> al = new ArrayList<String>();
			s1 = a[i][0]+"|"+a[i][1]+"|"+a[i][2]+"|"+a[i][3]+"|"+a[i][4]+"|"+a[i][5]+"|"+a[i][6];	
			s1a = a[i][1]+"|"+a[i][2]+"|"+a[i][3]+"|"+a[i][4]+"|"+a[i][5];

			for (int j=i+1; j<a.length;j++){
				s2 = a[j][0]+"|"+a[j][1]+"|"+a[j][2]+"|"+a[j][3]+"|"+a[j][4]+"|"+a[j][5]+"|"+a[j][6];
				s2a = a[j][1]+"|"+a[j][2]+"|"+a[j][3]+"|"+a[j][4]+"|"+a[j][5];
				s2aInv = a[j][2]+"|"+a[j][1]+"|"+a[j][3]+"|"+a[j][4]+"|"+a[j][5];

				percent1 = Util_LinhDan.perSimilarity(s1a,s2a); 
				percent2 = Util_LinhDan.perSimilarity(s1a,s2aInv); 

				if (percent1>atPercent || percent2>atPercent){
					al.add(s1);
					al.add(s2);
				}
				List<String> t2 = Util_LinhDan.arrayToList(al);

				if(!t2.isEmpty()){
					superArray.add(t2);
				}
			}
		}
		return superArray;
	}
	public static List<List<String>> method12456(String url,String user, String password){
		String[][] a = null;

		try {
			a = IdentifyPatient.get2dArrayPatientWithID(url,user,password);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		String s1,s1a,s2,s2a,s2aInv;
		int percent1=0;
		int percent2=0;
		List<List<String>> superArray = new ArrayList<List<String>>();

		for (int i=0; i<a.length;i++){
			ArrayList<String> al = new ArrayList<String>();
			s1 = a[i][0]+"|"+a[i][1]+"|"+a[i][2]+"|"+a[i][3]+"|"+a[i][4]+"|"+a[i][5]+"|"+a[i][6];	
			s1a = a[i][1]+"|"+a[i][2]+"|"+a[i][4]+"|"+a[i][5]+"|"+a[i][6];	

			for (int j=i+1; j<a.length;j++){
				s2 = a[j][0]+"|"+a[j][1]+"|"+a[j][2]+"|"+a[j][3]+"|"+a[j][4]+"|"+a[j][5]+"|"+a[j][6];
				s2a = a[j][1]+"|"+a[j][2]+"|"+a[j][4]+"|"+a[j][5]+"|"+a[j][6];
				s2aInv = a[j][2]+"|"+a[j][1]+"|"+a[j][4]+"|"+a[j][5]+"|"+a[j][6];

				percent1 = Util_LinhDan.perSimilarity(s1a,s2a); 
				percent2 = Util_LinhDan.perSimilarity(s1a,s2aInv); 

				if (percent1>atPercent || percent2>atPercent){
					al.add(s1);
					al.add(s2);
				}
				List<String> t2 = Util_LinhDan.arrayToList(al);

				if(!t2.isEmpty()){
					superArray.add(t2);
				}
			}
		}
		return superArray;
	}
	public static List<List<String>> method3456(String url,String user, String password){
		String[][] a = null;

		try {
			a = IdentifyPatient.get2dArrayPatientWithID(url,user,password);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		String s1,s1a,s2,s2a;
		int percent1=0;
		int percent2=0;
		List<List<String>> superArray = new ArrayList<List<String>>();

		for (int i=0; i<a.length;i++){
			ArrayList<String> al = new ArrayList<String>();
			s1 = a[i][0]+"|"+a[i][1]+"|"+a[i][2]+"|"+a[i][3]+"|"+a[i][4]+"|"+a[i][5]+"|"+a[i][6];	
			s1a = a[i][3]+"|"+a[i][4]+"|"+a[i][5]+"|"+a[i][6];	

			for (int j=i+1; j<a.length;j++){
				s2 = a[j][0]+"|"+a[j][1]+"|"+a[j][2]+"|"+a[j][3]+"|"+a[j][4]+"|"+a[j][5]+"|"+a[j][6];
				s2a = a[j][3]+"|"+a[j][4]+"|"+a[j][5]+"|"+a[j][6];

				percent1 = Util_LinhDan.perSimilarity(s1a,s2a); 

				if (percent1>atPercent || percent2>atPercent){
					al.add(s1);
					al.add(s2);
				}
				List<String> t2 = Util_LinhDan.arrayToList(al);

				if(!t2.isEmpty()){
					superArray.add(t2);
				}
			}
		}
		return superArray;
	}
	public static List<List<String>> method123(String url,String user, String password){
		String[][] a = null;

		try {
			a = IdentifyPatient.get2dArrayPatientWithID(url,user,password);
		} catch (SQLException e) {
			System.out.println("E");
		}

		String s1,s1a,s2,s2a,s2aInv;
		int percent1=0;
		int percent2=0;
		List<List<String>> superArray = new ArrayList<List<String>>();

		for (int i=0; i<a.length;i++){
			ArrayList<String> al = new ArrayList<String>();
			s1 = a[i][0]+"|"+a[i][1]+"|"+a[i][2]+"|"+a[i][3]+"|"+a[i][4]+"|"+a[i][5]+"|"+a[i][6];	
			s1a = a[i][1]+"|"+a[i][2]+"|"+a[i][3];

			for (int j=i+1; j<a.length;j++){
				s2 = a[j][0]+"|"+a[j][1]+"|"+a[j][2]+"|"+a[j][3]+"|"+a[j][4]+"|"+a[j][5]+"|"+a[j][6];
				s2a = a[j][1]+"|"+a[j][2]+"|"+a[j][3];
				s2aInv = a[j][2]+"|"+a[j][1]+"|"+a[j][3];

				percent1 = Util_LinhDan.perSimilarity(s1a,s2a); 
				percent2 = Util_LinhDan.perSimilarity(s1a,s2aInv); 

				if (percent1>atPercent || percent2>atPercent){
					al.add(s1);
					al.add(s2);
				}

				List<String> t2 = Util_LinhDan.arrayToList(al);

				if(!t2.isEmpty()){
					superArray.add(t2);
				}
			}
		}
		return superArray;
	}

	/******************************************************************************************************************************/	

	public static List<List<String>> getListPatientID (List<List<String>> superArrayIn){
		List<List<String>> superArrayInWithID = new ArrayList<List<String>>();	
		ArrayList<String> c = null ;

		for (int i = 0; i<superArrayIn.size(); i++){
			String[][] a = Util_LinhDan.arrayToTab2d((ArrayList<String>) superArrayIn.get(i), 7);
			c = new ArrayList<String>();
			for (int j = 0; j<a.length; j++){
				String b = a[j][0];
				c.add(b);
			}
			superArrayInWithID.add(c);
		}
		System.out.println(superArrayInWithID);
		return superArrayInWithID;
	}

	/******************************************************************************************************************************/	

	public static List<List<String>> getDiagnosticACP(String url,String user, String password) throws SQLException{
		List<List<String>> superArrayIn = IdentifyPatient.method123456SoundEx(url, user, password);
		List<List<String>> superArrayInWithID = IdentifyPatient.getListPatientID(superArrayIn);
		ArrayList<String> arrayOut = new ArrayList<String>();
		List<List<String>> superArrayOut = new ArrayList<List<String>>();	

		String query = "SELECT DISTINCT NumPatient,adicap "
				+ "FROM rel_patient_acp INNER JOIN adicap ON rel_patient_acp.Ligne_ACP = adicap.NumAdicapPrel "
				+ "WHERE NumPatient = ? ";		
		ConnexionJdbcMySql connect = new ConnexionJdbcMySql(url, user, password); 
		Connection connectEx = connect.getConnect(); 
		PreparedStatement pt = null;
		pt= connectEx.prepareStatement(query);

		for (int i = 0; i<superArrayInWithID.size(); i++){  
			ArrayList<String> cID = (ArrayList<String>) superArrayInWithID.get(i);
			arrayOut = new ArrayList<String>();

			for (int j = 0; j<cID.size(); j++){ 
				String concat = "";
				pt.setString(1, cID.get(j));

				ResultSet rs = pt.executeQuery();
				while (rs.next()) {
					String numPatient = rs.getString("NumPatient");
					String adicap = rs.getString("adicap");
					concat = numPatient + "|" + adicap;
					arrayOut.add(concat);
				}
			}
			superArrayOut.add(arrayOut);
		}
		//System.out.println(superArrayOut);
		return superArrayOut;
	}
	public static List<List<String>> getDiagnosticPMSI(String url,String user, String password) throws SQLException{

		List<List<String>> superArrayIn = IdentifyPatient.method123456SoundEx(url, user, password);
		List<List<String>> superArrayInWithID = IdentifyPatient.getListPatientID(superArrayIn);
		ArrayList<String> arrayOut = new ArrayList<String>();
		List<List<String>> superArrayOut = new ArrayList<List<String>>();	

		String queryDP = "SELECT DISTINCT rel_patient_pmsi.NumPatient,DP,DR,DAS "
				+ "FROM sejour INNER JOIN rel_patient_pmsi ON sejour.NumAutoSejour = rel_patient_pmsi.Ligne_PMSI "
				+ "INNER JOIN das ON sejour.NumSejour = das.NumSejour "
				+ "WHERE rel_patient_pmsi.NumPatient = ?;"; 

		ConnexionJdbcMySql connect = new ConnexionJdbcMySql(url, user, password); 
		Connection connectEx = connect.getConnect(); 
		PreparedStatement pt = null;
		pt= connectEx.prepareStatement(queryDP);

		for (int i = 0; i<superArrayInWithID.size(); i++){  
			ArrayList<String> cID = (ArrayList<String>) superArrayInWithID.get(i);
			arrayOut = new ArrayList<String>();

			for (int j = 0; j<cID.size(); j++){ 
				String concat = "";
				pt.setString(1, cID.get(j));

				ResultSet rs = pt.executeQuery();
				while (rs.next()) {
					String numPatient = rs.getString("rel_patient_pmsi.NumPatient");
					String dp = rs.getString("DP");
					String dr = rs.getString("DR");
					String das = rs.getString("DAS");
					concat = numPatient + "|" + dp+ "|" +dr+ "|" +das;
					arrayOut.add(concat);
				}
			}
			superArrayOut.add(arrayOut);
		}
		//System.out.println(superArrayOut);
		return superArrayOut;
	}
	public static List<List<String>> getDiagnosticDP(String url,String user, String password) throws SQLException{

		List<List<String>> superArrayIn = IdentifyPatient.method123456SoundEx(url, user, password);
		List<List<String>> superArrayInWithID = IdentifyPatient.getListPatientID(superArrayIn);
		ArrayList<String> arrayOut = new ArrayList<String>();
		List<List<String>> superArrayOut = new ArrayList<List<String>>();	

		String queryDP = "SELECT DISTINCT rel_patient_pmsi.NumPatient,DP "
				+ "FROM rel_patient_pmsi INNER JOIN sejour ON sejour.NumAutoSejour = rel_patient_pmsi.Ligne_PMSI "
				+ "WHERE rel_patient_pmsi.NumPatient = ? ";	

		ConnexionJdbcMySql connect = new ConnexionJdbcMySql(url, user, password); 
		Connection connectEx = connect.getConnect(); 
		PreparedStatement pt = null;
		pt= connectEx.prepareStatement(queryDP);

		for (int i = 0; i<superArrayInWithID.size(); i++){  
			ArrayList<String> cID = (ArrayList<String>) superArrayInWithID.get(i);
			arrayOut = new ArrayList<String>();

			for (int j = 0; j<cID.size(); j++){ 
				String concat = "";
				pt.setString(1, cID.get(j));

				ResultSet rs = pt.executeQuery();
				while (rs.next()) {
					String numPatient = rs.getString("rel_patient_pmsi.NumPatient");
					String dp = rs.getString("DP");
					concat = numPatient + "|" + dp;
					arrayOut.add(concat);
				}
			}
			superArrayOut.add(arrayOut);
		}
		//System.out.println(superArrayOut);
		return superArrayOut;
	}
	public static List<List<String>> getDiagnosticDR(String url,String user, String password) throws SQLException{

		List<List<String>> superArrayIn = IdentifyPatient.method123456SoundEx(url, user, password);
		List<List<String>> superArrayInWithID = IdentifyPatient.getListPatientID(superArrayIn);
		ArrayList<String> arrayOut = new ArrayList<String>();
		List<List<String>> superArrayOut = new ArrayList<List<String>>();	

		String queryDP = "SELECT DISTINCT rel_patient_pmsi.NumPatient,DR "
				+ "FROM rel_patient_pmsi INNER JOIN sejour ON sejour.NumAutoSejour = rel_patient_pmsi.Ligne_PMSI "
				+ "WHERE rel_patient_pmsi.NumPatient = ? ";	

		ConnexionJdbcMySql connect = new ConnexionJdbcMySql(url, user, password); 
		Connection connectEx = connect.getConnect(); 
		PreparedStatement pt = null;
		pt= connectEx.prepareStatement(queryDP);

		for (int i = 0; i<superArrayInWithID.size(); i++){  
			ArrayList<String> cID = (ArrayList<String>) superArrayInWithID.get(i);
			arrayOut = new ArrayList<String>();

			for (int j = 0; j<cID.size(); j++){ 
				String concat = "";
				pt.setString(1, cID.get(j));

				ResultSet rs = pt.executeQuery();
				while (rs.next()) {
					String numPatient = rs.getString("rel_patient_pmsi.NumPatient");
					String dr = rs.getString("DR");
					concat = numPatient + "|" + dr;
					arrayOut.add(concat);
				}
			}
			superArrayOut.add(arrayOut);
		}
		//System.out.println(superArrayOut);
		return superArrayOut;
	}
	public static List<List<String>> getDiagnosticDAS(String url,String user, String password) throws SQLException{

		List<List<String>> superArrayIn = IdentifyPatient.method123456SoundEx(url, user, password);
		List<List<String>> superArrayInWithID = IdentifyPatient.getListPatientID(superArrayIn);
		ArrayList<String> arrayOut = new ArrayList<String>();
		List<List<String>> superArrayOut = new ArrayList<List<String>>();	

		String queryDP = "SELECT DISTINCT rel_patient_pmsi.NumPatient,DAS "
				+ "FROM sejour INNER JOIN rel_patient_pmsi ON sejour.NumAutoSejour = rel_patient_pmsi.Ligne_PMSI "
				+ "INNER JOIN das ON sejour.NumSejour = das.NumSejour "
				+ "WHERE rel_patient_pmsi.NumPatient = ?;"; 

		ConnexionJdbcMySql connect = new ConnexionJdbcMySql(url, user, password); 
		Connection connectEx = connect.getConnect(); 
		PreparedStatement pt = null;
		pt= connectEx.prepareStatement(queryDP);

		for (int i = 0; i<superArrayInWithID.size(); i++){  
			ArrayList<String> cID = (ArrayList<String>) superArrayInWithID.get(i);
			arrayOut = new ArrayList<String>();

			for (int j = 0; j<cID.size(); j++){ 
				String concat = "";
				pt.setString(1, cID.get(j));

				ResultSet rs = pt.executeQuery();
				while (rs.next()) {
					String numPatient = rs.getString("rel_patient_pmsi.NumPatient");
					String das = rs.getString("DAS");
					concat = numPatient + "|" + das;
					arrayOut.add(concat);
				}
			}
			superArrayOut.add(arrayOut);
		}
		//System.out.println(superArrayOut);
		return superArrayOut;
	}

	/*******************************************************************************************************************************/	

	public static void groupPatient(String url,String user, String password) throws SQLException{

		ArrayList<String> a =  new ArrayList<String>(Arrays.asList(new String[]{"61", "4"}));   

		ConnexionJdbcMySql connect = new ConnexionJdbcMySql(url, user, password); 
		Connection connectEx = connect.getConnect(); 

		String query = "UPDATE patient "
				+ "SET id_principal = ? "
				+ "WHERE NumAutoPatient = ?;";
		PreparedStatement pt = null;
		pt= connectEx.prepareStatement(query);

		for (int i = 1; i<a.size(); i ++){  
			pt.setString(1, a.get(0)); 
			pt.setString(2,  a.get(i)); 	
			pt.addBatch();
		}

		try {
			pt.executeBatch();
		} catch (Exception e) {
			System.out.println("UPDATE 'id_principal' not OK");
			e.printStackTrace();
		}
	}

	/**********************************************************************************************************************************/	

	public static void updateFlagID(String fin) throws SQLException{
		fin = "Fin Recherche Doublons";
		if (fin == "Fin Recherche Doublons"){
			String url = "jdbc:mysql://localhost/projet_ime_202";
			String user = "root";
			String password = "";
			ConnexionJdbcMySql connect = new ConnexionJdbcMySql(url, user, password); 
			Connection connectEx = connect.getConnect(); 
			
			Statement patientIdentity = connectEx.createStatement();
			String query1 = "UPDATE sejour "
					+ "SET flag_identification = 1;";
			String query2 = "UPDATE adicap_prelevement "
					+ "SET flag_identification = 1;";
			try {
				try {
					patientIdentity.executeUpdate(query1);
				} catch (Exception e) {
					System.out.println("UPDATE FLAG ID ('sejour') not OK");
				}
				
				try {
					patientIdentity.executeUpdate(query2);
				} catch (Exception e) {
					System.out.println("UPDATE FLAG ID ('adicap_prelevement') not OK");
				}
			} catch (Exception e) {
				System.exit(-1);
			}
			
		}
	}
	// comparer nvx patient où flag id = 0 à patient existant dans table patient 
	// add patient ssi n'existe pas 

	// IF TABLE patient,rel_patient_ACP,rel_patient_PMSI NOT EMPTY ????????????????????????? FLAG ? IN WHICH TABLE ? 
	// public static  void addPatientToTablePatient(String url,String user, String password) throws SQLException{}
	// public static void addRelPatientACP(String url,String user, String password) throws SQLException{}
	// public static void addRelPatientPMSI(String url,String user, String password) throws SQLException{}

	/**********************************************************************************************************************************/
	public static  void main(String[] args) throws SQLException {

		//				String url = "jdbc:mysql://localhost/projet_ime_202";
		//				String user = "root";
		//				String password = "";
		//
		//				IdentifyPatient.createTablePatient(url,user,password);
		//				IdentifyPatient.createTableRelPatientACP(url, user, password);
		//				IdentifyPatient.createTableRelPatientPMSI(url, user, password);
		//
		//				IdentifyPatient.importPatientToTablePatient(url,user,password);
		//				IdentifyPatient.importRelPatientACP(url, user, password);
		//				IdentifyPatient.importRelPatientPMSI(url, user, password);
		//
		//				List<List<String>> a = IdentifyPatient.method123456SoundEx(url,user,password); //DEFAULT CHOICES
		//				IdentityPatient.method23456SoundEx(url,user,password); 
		//				IdentityPatient.method13456SoundEx(url,user,password); 
		//				IdentityPatient.method12345SoundEx(url,user,password); 
		//				IdentityPatient.method12456SoundEx(url,user,password);
		//				IdentityPatient.method123SoundEx(url,user,password); 
		//
		//				IdentityPatient.method123456(url,user,password); 
		//				IdentityPatient.method23456(url,user,password); 
		//				IdentityPatient.method13456(url,user,password); 
		//				IdentityPatient.method12345(url,user,password); 
		//				IdentityPatient.method12456(url,user,password);
		//				IdentityPatient.method123(url,user,password); 
		//				IdentityPatient.method3456(url,user,password); 
		//
		//		 		IdentifyPatient.getDiagnosticACP(url, user, password);
		//				IdentifyPatient.getDiagnosticPMSI(url, user, password);
		//				IdentifyPatient.getDiagnosticDP(url, user, password);
		//		 		IdentifyPatient.getDiagnosticDR(url, user, password);
		//				IdentifyPatient.getDiagnosticDAS(url, user, password);
		//
		//				IdentifyPatient.groupPatient(url, user, password);

	}
}









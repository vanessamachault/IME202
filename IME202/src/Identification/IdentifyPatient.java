package Identification;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import identification.Util;

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
				+ "hashPatient INT(10),"
				+ "identite_principale INT(10),"
				+ "flag_id TINYINT(1),"
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

	public static  void importPatientToTablePatient(String url,String user, String password) throws SQLException{

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
		String[][] a = Util.arrayToTab2d(arrayPatient,6);

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
		String[][] a = Util.arrayToTab2d(array,2);

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
		String[][] a = Util.arrayToTab2d(array,2);

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

	// IF TABLE patient,rel_patient_ACP,rel_patient_PMSI NOT EMPTY ?????????????????????????
	
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

		String[][] a = Util.arrayToTab2d(arrayPatient,7);

		return a;
	}

	/**********************************************************************************************************************************/
	//		IdentityPatient.method1234567a7b(url,user,password); OK
	public static List<List<String>> method1234567a7b(String url,String user, String password){
		String[][] a = null;

		try {
			a = IdentifyPatient.get2dArrayPatientWithID(url,user,password);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		int atPercent = 70;
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
			soundExNom1 = Util.soundExFr(a[i][1]);
			soundExPrenom1 = Util.soundExFr(a[i][2]);

			for (int j=i+1; j<a.length;j++){
				s2 = a[j][0]+"|"+a[j][1]+"|"+a[j][2]+"|"+a[j][3]+"|"+a[j][4]+"|"+a[j][5]+"|"+a[j][6];
				s2a = a[j][1]+"|"+a[j][2]+"|"+a[j][3]+"|"+a[j][4]+"|"+a[j][5]+"|"+a[j][6];
				s2aInv = a[j][2]+"|"+a[j][1]+"|"+a[j][3]+"|"+a[j][4]+"|"+a[j][5]+"|"+a[j][6];
				s2b = a[j][3]+"|"+a[j][4]+"|"+a[j][5]+"|"+a[j][6];
				soundExNom2 = Util.soundExFr(a[j][1]);
				soundExPrenom2 = Util.soundExFr(a[j][2]);
				percent1 = Util.perSimilarity(s1a,s2a); 
				percent2 = Util.perSimilarity(s1a,s2aInv); 
				percent3 = Util.perSimilarity(s1b,s2b); 

				if (percent1>atPercent || percent2>atPercent || (percent3>atPercent && (soundExNom1.equals(soundExNom2)&& soundExPrenom1.equals(soundExPrenom2))||
						(soundExNom1.equals(soundExPrenom2)&& soundExPrenom1.equals(soundExNom1)))){
					al.add(s1);
					al.add(s2);
					Set <String> t1 = new LinkedHashSet<String>(al);
					List<String> t2 = new ArrayList<String>(t1);
					superArray.add(t2);
				}
			}
		}

		//System.out.println(superArray);
		return superArray;
	}

	//		IdentityPatient.method234567a7b(url,user,password); OK 
	public static List<List<String>> method234567a7b(String url,String user, String password){
		String[][] a = null;

		try {
			a = IdentifyPatient.get2dArrayPatientWithID(url,user,password);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		int atPercent = 70;
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
			soundExNom1 = Util.soundExFr(a[i][1]);
			soundExPrenom1 = Util.soundExFr(a[i][2]);

			for (int j=i+1; j<a.length;j++){
				s2 = a[j][0]+"|"+a[j][1]+"|"+a[j][2]+"|"+a[j][3]+"|"+a[j][4]+"|"+a[j][5]+"|"+a[j][6];
				s2a = a[j][2]+"|"+a[j][3]+"|"+a[j][4]+"|"+a[j][5]+"|"+a[j][6];
				s2aInv = a[j][1]+"|"+a[j][3]+"|"+a[j][4]+"|"+a[j][5]+"|"+a[j][6];
				s2b = a[j][3]+"|"+a[j][4]+"|"+a[j][5]+"|"+a[j][6];
				soundExNom2 = Util.soundExFr(a[j][1]);
				soundExPrenom2 = Util.soundExFr(a[j][2]);
				percent1 = Util.perSimilarity(s1a,s2a); 
				percent2 = Util.perSimilarity(s1a,s2aInv); 
				percent3 = Util.perSimilarity(s1b,s2b); 

				if (percent1>atPercent || percent2>atPercent || (percent3>atPercent && (soundExNom1.equals(soundExNom2)&& soundExPrenom1.equals(soundExPrenom2))||
						(soundExNom1.equals(soundExPrenom2)&& soundExPrenom1.equals(soundExNom1)))){
					al.add(s1);
					al.add(s2);
					Set <String> t1 = new LinkedHashSet<String>(al);
					List<String> t2 = new ArrayList<String>(t1);
					superArray.add(t2);
				}
			}
		}

		//System.out.println(superArray);
		return superArray;
	}

	//		IdentityPatient.method134567a7b(url,user,password); OK 
	public static List<List<String>> method134567a7b(String url,String user, String password){
		String[][] a = null;

		try {
			a = IdentifyPatient.get2dArrayPatientWithID(url,user,password);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		int atPercent = 70;
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
			soundExNom1 = Util.soundExFr(a[i][1]);
			soundExPrenom1 = Util.soundExFr(a[i][2]);

			for (int j=i+1; j<a.length;j++){
				s2 = a[j][0]+"|"+a[j][1]+"|"+a[j][2]+"|"+a[j][3]+"|"+a[j][4]+"|"+a[j][5]+"|"+a[j][6];
				s2a = a[j][1]+"|"+a[j][3]+"|"+a[j][4]+"|"+a[j][5]+"|"+a[j][6];
				s2aInv = a[j][2]+"|"+a[j][3]+"|"+a[j][4]+"|"+a[j][5]+"|"+a[j][6];
				s2b = a[j][3]+"|"+a[j][4]+"|"+a[j][5]+"|"+a[j][6];
				soundExNom2 = Util.soundExFr(a[j][1]);
				soundExPrenom2 = Util.soundExFr(a[j][2]);
				percent1 = Util.perSimilarity(s1a,s2a); 
				percent2 = Util.perSimilarity(s1a,s2aInv); 
				percent3 = Util.perSimilarity(s1b,s2b); 

				if (percent1>atPercent || percent2>atPercent || (percent3>atPercent && (soundExNom1.equals(soundExNom2)&& soundExPrenom1.equals(soundExPrenom2))||
						(soundExNom1.equals(soundExPrenom2)&& soundExPrenom1.equals(soundExNom1)))){
					al.add(s1);
					al.add(s2);
					Set <String> t1 = new LinkedHashSet<String>(al);
					List<String> t2 = new ArrayList<String>(t1);
					superArray.add(t2);
				}
			}
		}

		//System.out.println(superArray);
		return superArray;
	}

	//		IdentityPatient.method123457a7b(url,user,password); OK 
	public static List<List<String>> method123457a7b(String url,String user, String password){
		String[][] a = null;

		try {
			a = IdentifyPatient.get2dArrayPatientWithID(url,user,password);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		int atPercent = 70;
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
			soundExNom1 = Util.soundExFr(a[i][1]);
			soundExPrenom1 = Util.soundExFr(a[i][2]);

			for (int j=i+1; j<a.length;j++){
				s2 = a[j][0]+"|"+a[j][1]+"|"+a[j][2]+"|"+a[j][3]+"|"+a[j][4]+"|"+a[j][5]+"|"+a[j][6];
				s2a = a[j][1]+"|"+a[j][2]+"|"+a[j][3]+"|"+a[j][4]+"|"+a[j][5];
				s2aInv = a[j][2]+"|"+a[j][1]+"|"+a[j][3]+"|"+a[j][4]+"|"+a[j][5];
				s2b = a[j][3]+"|"+a[j][4]+"|"+a[j][5];
				soundExNom2 = Util.soundExFr(a[j][1]);
				soundExPrenom2 = Util.soundExFr(a[j][2]);
				percent1 = Util.perSimilarity(s1a,s2a); 
				percent2 = Util.perSimilarity(s1a,s2aInv); 
				percent3 = Util.perSimilarity(s1b,s2b); 

				if (percent1>atPercent || percent2>atPercent || (percent3>atPercent && (soundExNom1.equals(soundExNom2)&& soundExPrenom1.equals(soundExPrenom2))||
						(soundExNom1.equals(soundExPrenom2)&& soundExPrenom1.equals(soundExNom1)))){
					al.add(s1);
					al.add(s2);
					Set <String> t1 = new LinkedHashSet<String>(al);
					List<String> t2 = new ArrayList<String>(t1);
					superArray.add(t2);
				}
			}
		}

		//System.out.println(superArray);
		return superArray;
	}

	//		IdentityPatient.method124567a7b(url,user,password); OK
	public static List<List<String>> method124567a7b(String url,String user, String password){
		String[][] a = null;

		try {
			a = IdentifyPatient.get2dArrayPatientWithID(url,user,password);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		int atPercent = 70;
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
			soundExNom1 = Util.soundExFr(a[i][1]);
			soundExPrenom1 = Util.soundExFr(a[i][2]);

			for (int j=i+1; j<a.length;j++){
				s2 = a[j][0]+"|"+a[j][1]+"|"+a[j][2]+"|"+a[j][3]+"|"+a[j][4]+"|"+a[j][5]+"|"+a[j][6];
				s2a = a[j][1]+"|"+a[j][2]+"|"+a[j][4]+"|"+a[j][5]+"|"+a[j][6];
				s2aInv = a[j][2]+"|"+a[j][1]+"|"+a[j][4]+"|"+a[j][5]+"|"+a[j][6];
				s2b = a[j][4]+"|"+a[j][5]+"|"+a[j][6];
				soundExNom2 = Util.soundExFr(a[j][1]);
				soundExPrenom2 = Util.soundExFr(a[j][2]);
				percent1 = Util.perSimilarity(s1a,s2a); 
				percent2 = Util.perSimilarity(s1a,s2aInv); 
				percent3 = Util.perSimilarity(s1b,s2b); 

				if (percent1>atPercent || percent2>atPercent || (percent3>atPercent && (soundExNom1.equals(soundExNom2)&& soundExPrenom1.equals(soundExPrenom2))||
						(soundExNom1.equals(soundExPrenom2)&& soundExPrenom1.equals(soundExNom1)))){
					al.add(s1);
					al.add(s2);
					Set <String> t1 = new LinkedHashSet<String>(al);
					List<String> t2 = new ArrayList<String>(t1);
					superArray.add(t2);
				}
			}
		}

		//System.out.println(superArray);
		return superArray;
	}

	//		IdentityPatient.method1237a7b(url,user,password); OK
	public static List<List<String>> method1237a7b(String url,String user, String password){
		String[][] a = null;

		try {
			a = IdentifyPatient.get2dArrayPatientWithID(url,user,password);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		int atPercent = 70;
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
			soundExNom1 = Util.soundExFr(a[i][1]);
			soundExPrenom1 = Util.soundExFr(a[i][2]);

			for (int j=i+1; j<a.length;j++){
				s2 = a[j][0]+"|"+a[j][1]+"|"+a[j][2]+"|"+a[j][3]+"|"+a[j][4]+"|"+a[j][5]+"|"+a[j][6];
				s2a = a[j][1]+"|"+a[j][2]+"|"+a[j][3];
				s2aInv = a[j][2]+"|"+a[j][1]+"|"+a[j][3];
				s2b = a[j][3];
				soundExNom2 = Util.soundExFr(a[j][1]);
				soundExPrenom2 = Util.soundExFr(a[j][2]);
				percent1 = Util.perSimilarity(s1a,s2a); 
				percent2 = Util.perSimilarity(s1a,s2aInv); 
				percent3 = Util.perSimilarity(s1b,s2b); 

				if (percent1>atPercent || percent2>atPercent || (percent3>atPercent && (soundExNom1.equals(soundExNom2)&& soundExPrenom1.equals(soundExPrenom2))||
						(soundExNom1.equals(soundExPrenom2)&& soundExPrenom1.equals(soundExNom1)))){
					al.add(s1);
					al.add(s2);
					Set <String> t1 = new LinkedHashSet<String>(al);
					List<String> t2 = new ArrayList<String>(t1);
					superArray.add(t2);
				}
			}
		}

		//System.out.println(superArray);
		return superArray;
	}

	//		IdentityPatient.method123456(url,user,password); OK
	public static List<List<String>> method123456(String url,String user, String password){
		String[][] a = null;

		try {
			a = IdentifyPatient.get2dArrayPatientWithID(url,user,password);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		int atPercent = 70;
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

				percent1 = Util.perSimilarity(s1a,s2a); 
				percent2 = Util.perSimilarity(s1a,s2aInv); 

				if (percent1>atPercent || percent2>atPercent){
					al.add(s1);
					al.add(s2);
					Set <String> t1 = new LinkedHashSet<String>(al);
					List<String> t2 = new ArrayList<String>(t1);
					superArray.add(t2);
				}
			}
		}
		return superArray;
	}

	//		IdentityPatient.method23456(url,user,password); OK
	public static List<List<String>> method23456(String url,String user, String password){
		String[][] a = null;

		try {
			a = IdentifyPatient.get2dArrayPatientWithID(url,user,password);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		int atPercent = 70;
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

				percent1 = Util.perSimilarity(s1a,s2a); 
				percent2 = Util.perSimilarity(s1a,s2aInv); 

				if (percent1>atPercent || percent2>atPercent){
					al.add(s1);
					al.add(s2);
					Set <String> t1 = new LinkedHashSet<String>(al);
					List<String> t2 = new ArrayList<String>(t1);
					superArray.add(t2);
				}
			}
		}
		return superArray;
	}

	//		IdentityPatient.method13456(url,user,password); OK
	public static List<List<String>> method13456(String url,String user, String password){
		String[][] a = null;

		try {
			a = IdentifyPatient.get2dArrayPatientWithID(url,user,password);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		int atPercent = 70;
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

				percent1 = Util.perSimilarity(s1a,s2a); 
				percent2 = Util.perSimilarity(s1a,s2aInv); 

				if (percent1>atPercent || percent2>atPercent){
					al.add(s1);
					al.add(s2);
					Set <String> t1 = new LinkedHashSet<String>(al);
					List<String> t2 = new ArrayList<String>(t1);
					superArray.add(t2);
				}
			}
		}
		return superArray;
	}

	//		IdentityPatient.method12345(url,user,password); OK 
	public static List<List<String>> method12345(String url,String user, String password){
		String[][] a = null;

		try {
			a = IdentifyPatient.get2dArrayPatientWithID(url,user,password);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		int atPercent = 70;
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

				percent1 = Util.perSimilarity(s1a,s2a); 
				percent2 = Util.perSimilarity(s1a,s2aInv); 

				if (percent1>atPercent || percent2>atPercent){
					al.add(s1);
					al.add(s2);
					Set <String> t1 = new LinkedHashSet<String>(al);
					List<String> t2 = new ArrayList<String>(t1);
					superArray.add(t2);
				}
			}
		}
		return superArray;
	}

	//		IdentityPatient.method12456(url,user,password); OK
	public static List<List<String>> method12456(String url,String user, String password){
		String[][] a = null;

		try {
			a = IdentifyPatient.get2dArrayPatientWithID(url,user,password);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		int atPercent = 70;
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

				percent1 = Util.perSimilarity(s1a,s2a); 
				percent2 = Util.perSimilarity(s1a,s2aInv); 

				if (percent1>atPercent || percent2>atPercent){
					al.add(s1);
					al.add(s2);
					Set <String> t1 = new LinkedHashSet<String>(al);
					List<String> t2 = new ArrayList<String>(t1);
					superArray.add(t2);
				}
			}
		}
		return superArray;
	}

	//		IdentityPatient.method3456(url,user,password); OK
	public static List<List<String>> method3456(String url,String user, String password){
		String[][] a = null;

		try {
			a = IdentifyPatient.get2dArrayPatientWithID(url,user,password);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		int atPercent = 70;
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

				percent1 = Util.perSimilarity(s1a,s2a); 

				if (percent1>atPercent || percent2>atPercent){
					al.add(s1);
					al.add(s2);
					Set <String> t1 = new LinkedHashSet<String>(al);
					List<String> t2 = new ArrayList<String>(t1);
					superArray.add(t2);
				}
			}
		}
		return superArray;
	}

	//		IdentityPatient.method123(url,user,password); OK
	public static List<List<String>> method123(String url,String user, String password){
		String[][] a = null;

		try {
			a = IdentifyPatient.get2dArrayPatientWithID(url,user,password);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		int atPercent = 70;
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

				percent1 = Util.perSimilarity(s1a,s2a); 
				percent2 = Util.perSimilarity(s1a,s2aInv); 

				if (percent1>atPercent || percent2>atPercent){
					al.add(s1);
					al.add(s2);
					Set <String> t1 = new LinkedHashSet<String>(al);
					List<String> t2 = new ArrayList<String>(t1);
					superArray.add(t2);
				}
			}
		}
		return superArray;
	}

	/**********************************************************************************************************************************/	

	public static  void main(String[] args) throws SQLException {

		String url = "jdbc:mysql://localhost/projet_ime_202";
		String user = "root";
		String password = "";

//		IdentifyPatient.createTablePatient(url,user,password);
//		IdentifyPatient.createTableRelPatientACP(url, user, password);
//		IdentifyPatient.createTableRelPatientPMSI(url, user, password);

		IdentifyPatient.importPatientToTablePatient(url,user,password);
		IdentifyPatient.importRelPatientACP(url, user, password);
		IdentifyPatient.importRelPatientPMSI(url, user, password);

		System.exit(-1);

		IdentifyPatient.method1234567a7b(url,user,password); //DEFAULT CHOICES
		//		IdentityPatient.method234567a7b(url,user,password); 
		//		IdentityPatient.method134567a7b(url,user,password); 
		//		IdentityPatient.method123457a7b(url,user,password); 
		//		IdentityPatient.method124567a7b(url,user,password);
		//		IdentityPatient.method1237a7b(url,user,password); 

		//		IdentityPatient.method123456(url,user,password); 
		//		IdentityPatient.method23456(url,user,password); 
		//		IdentityPatient.method13456(url,user,password); 
		//		IdentityPatient.method12345(url,user,password); 
		//		IdentityPatient.method12456(url,user,password);
		//		IdentityPatient.method123(url,user,password); 
		//		IdentityPatient.method3456(url,user,password); 



	}
}


//connect.close(connectEx);






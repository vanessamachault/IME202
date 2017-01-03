package Integration;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.json.JSONException;
import org.json.simple.parser.ParseException;

public class Traduction {

	public static void main(String[] args) throws JSONException, ParseException {
		// TODO Auto-generated method stub
		System.out.println("Traduction CIMO3 Topo : " + traductionCIMO3_Topo("C15.9"));
		System.out.println("Traduction CIMO3 Morpho : " +traductionCIMO3_Morpho("M-8000/3"));
		System.out.println("Traduction CIM10 : " + traductionCIM10("C15.9"));
		
		String[] traduction = Utilities.transcodage_ADICAP_CIMO3("BHHP6715", 1, 0);
		
		System.out.println("Mode de pr�l�vement : " + traduction[0]);
		System.out.println("Type de technique utilis�e : " + traduction[1]);
		System.out.println("Organe : " + traduction[2]);
		System.out.println("L�sion : " + traduction[3]);

	}

	public static String traductionCIMO3_Topo (String code){
		//Connexion � la base de donn�e
		String url= "jdbc:mysql://localhost/projet_ime_202";
		String user="root";
		String motpasse="";

		Connexion connect = new Connexion (url, user, motpasse);
		Connection con = connect.getCon();
		Statement st = null;

		//La requ�te de s�lection des codes adicap � transcoder
		String query1 = "SELECT Libelle_CIMO3_Topo FROM libelle_CIMO3_Topo WHERE Code_CIMO3_Topo = \"" + code + "\"";

		String result = "";

		try {
			st = con.createStatement();
			ResultSet rs = st.executeQuery(query1);

			while (rs.next()) {
				result = rs.getString("Libelle_CIMO3_Topo");
			}

			rs.last();
			if (rs.getRow() == 0){
				result = "Pas de traduction disponible";
			}

		}catch (SQLException e1) {
			e1.printStackTrace();
		}
		return result;
	}
	
	
	public static String traductionCIMO3_Morpho (String code){
		//Connexion � la base de donn�e
		String url= "jdbc:mysql://localhost/projet_ime_202";
		String user="root";
		String motpasse="";

		Connexion connect = new Connexion (url, user, motpasse);
		Connection con = connect.getCon();
		Statement st = null;

		//La requ�te de s�lection des codes adicap � transcoder
		String query1 = "SELECT Libelle_CIMO3_Morpho FROM libelle_CIMO3_Morpho WHERE Code_CIMO3_Morpho = \"" + code + "\"";

		String result = "";

		try {
			st = con.createStatement();
			ResultSet rs = st.executeQuery(query1);

			while (rs.next()) {
				result = rs.getString("Libelle_CIMO3_Morpho");
			}

			rs.last();
			if (rs.getRow() == 0){
				result = "Pas de traduction disponible";
			}

		}catch (SQLException e1) {
			e1.printStackTrace();
		}
		return result;
	}
	
	public static String traductionCIM10 (String code){
		//Connexion � la base de donn�e
		String url= "jdbc:mysql://localhost/projet_ime_202";
		String user="root";
		String motpasse="";

		Connexion connect = new Connexion (url, user, motpasse);
		Connection con = connect.getCon();
		Statement st = null;

		//La requ�te de s�lection des codes adicap � transcoder
		String query1 = "SELECT Libelle_CIM10 FROM libelle_CIM10 WHERE Code_CIM10 = \"" + code + "\"";

		String result = "";

		try {
			st = con.createStatement();
			ResultSet rs = st.executeQuery(query1);

			while (rs.next()) {
				result = rs.getString("Libelle_CIM10");
			}

			rs.last();
			if (rs.getRow() == 0){
				result = "Pas de traduction disponible";
			}

		}catch (SQLException e1) {
			e1.printStackTrace();
		}
		return result;
	}
}


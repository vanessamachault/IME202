package Integration;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Traduction {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println(traductionCIMO3_Topo("C15.9"));
		System.out.println(traductionCIMO3_Morpho("M-8000/3"));
		System.out.println(traductionCIM10("C15.9"));

	}

	public static String traductionCIMO3_Topo (String code){
		//Connexion à la base de donnée
		String url= "jdbc:mysql://localhost/projet_ime_202";
		String user="root";
		String motpasse="";

		Connexion connect = new Connexion (url, user, motpasse);
		Connection con = connect.getCon();
		Statement st = null;

		//La requête de sélection des codes adicap à transcoder
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
		//Connexion à la base de donnée
		String url= "jdbc:mysql://localhost/projet_ime_202";
		String user="root";
		String motpasse="";

		Connexion connect = new Connexion (url, user, motpasse);
		Connection con = connect.getCon();
		Statement st = null;

		//La requête de sélection des codes adicap à transcoder
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
		//Connexion à la base de donnée
		String url= "jdbc:mysql://localhost/projet_ime_202";
		String user="root";
		String motpasse="";

		Connexion connect = new Connexion (url, user, motpasse);
		Connection con = connect.getCon();
		Statement st = null;

		//La requête de sélection des codes adicap à transcoder
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


package Notification;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class RecupPatient {

	public static ArrayList<Patient> recupPatient() {

		ArrayList<Patient> listePatient = new ArrayList<>();
		//Connexion à la base de donnée
		String url= "jdbc:mysql://localhost/projet_ime_202";
		String user="root";
		String motpasse="";

		Connexion connect = new Connexion (url, user, motpasse);
		Connection con = connect.getCon();
		try {

			java.sql.Statement st= con.createStatement();
			ResultSet resultSetPatient = st.executeQuery("SELECT DISTINCT adicap_prelevement.NumPatient FROM adicap_prelevement;");
			while (resultSetPatient.next()){
				int numPatient = resultSetPatient.getInt("NumPatient");
				String nom = "";
				String prenom = "";
				String dateNaiss = "";
				String sexe = "";
				ArrayList<Enregistrement> listeEnregistrement = new ArrayList<>();
				ArrayList<Enregistrement> listeEnregistrementSys = new ArrayList<>(); 
				ArrayList<GroupeTopo> listeEnregistrementsGroupe = new ArrayList<>();
				ArrayList<Notification> listeNotification= new ArrayList<>();
				Patient p = new Patient(numPatient, nom, prenom, dateNaiss, sexe, listeEnregistrement, listeEnregistrementSys, listeEnregistrementsGroupe, listeNotification);
				listePatient.add(p);
				//System.out.println(numPatient);
			}

			resultSetPatient.close();
			st.close();
			con.close();
		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return listePatient;
	}

}

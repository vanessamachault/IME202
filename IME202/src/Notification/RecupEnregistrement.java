package Notification;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

public class RecupEnregistrement {

	public static String query;

	//Méthode de récupération des enregistrements (ACP, PMSI) pour chaque patient
	public static void recupEnregistrement (ArrayList<Patient> listePatient , String srce) {

		//Connexion à la base de donnée
		String url= "jdbc:mysql://localhost/projet_ime_202";
		String user="root";
		String motpasse="";

		try {
		Connexion connect = new Connexion (url, user, motpasse);
		Connection con = connect.getCon();

		//Création de la requete en fonction de la source
		if (srce.equalsIgnoreCase("ADICAP")){
			query = "SELECT DISTINCT adicap_prelevement.NumPatient, adicap_prelevement.NumAutoAdicapPrel, adicap.Adicap, adicap_prelevement.DatePrel_Jour, adicap_prelevement.DatePrel_Mois, adicap_prelevement.DatePrel_Annee, transcodage_adicap_cimo3_topo_valides.CIMO3_Topo, transcodage_adicap_cimo3_morpho_valides.CIMO3_Morpho "
					+ "FROM adicap INNER JOIN rel_adicap_cimo3 ON adicap.NumAutoAdicap = rel_adicap_cimo3.NumLigneADICAP "
					+ "INNER JOIN transcodage_adicap_cimo3_topo_valides ON rel_adicap_cimo3.CIMO3_Topo=transcodage_adicap_cimo3_topo_valides.NumAuto "
					+ "INNER JOIN transcodage_adicap_cimo3_morpho_valides ON rel_adicap_cimo3.CIMO3_Morpho=transcodage_adicap_cimo3_morpho_valides.NumAuto "
					+ "INNER JOIN adicap_prelevement ON adicap.NumAdicapPrel = adicap_prelevement.NumAutoAdicapPrel "
					+ "WHERE adicap_prelevement.NumPatient = ?;";
		}
		else if (srce.equalsIgnoreCase("PMSI")){
			query = "";
		}
		else {}

		int numeroCode = 0;

		Iterator<Patient> iterateurListePatient = listePatient.iterator();
		while (iterateurListePatient.hasNext()){
			Patient p = iterateurListePatient.next();
			int numeroPatient = p.getNumPatient();
			ArrayList<Enregistrement> listeEnregistrement = new ArrayList<>();


				java.sql.PreparedStatement prepare = con.prepareStatement(query);
				prepare.setInt(1, numeroPatient);
				ResultSet resultSetEnr=prepare.executeQuery();

				while (resultSetEnr.next()){

					numeroCode = numeroCode + 1;
					//System.out.println("Code numéro : " + numeroCode);

					int numPatient = resultSetEnr.getInt("NumPatient");
					int numPrel = resultSetEnr.getInt("NumAutoAdicapPrel");
					String source = srce;
					String cimO3Topo = resultSetEnr.getString("CIMO3_Topo");
					String cimO3Morpho = resultSetEnr.getString("CIMO3_Morpho");
					String date = resultSetEnr.getString("DatePrel_Jour")+"/"+resultSetEnr.getString("DatePrel_Mois")+"/"+resultSetEnr.getString("datePrel_annee");
					String groupeTopo = null;
					String groupeMorpho = null;
					String superGroupeMorpho = null;

					Enregistrement e = new Enregistrement(numPatient, numPrel, source, cimO3Topo, groupeTopo, cimO3Morpho, groupeMorpho, date, superGroupeMorpho);
					listeEnregistrement.add(e);
				}
				p.setListeEnregistrement(listeEnregistrement);
				resultSetEnr.close();
				prepare.close();
				
			}
		con.close();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}

}

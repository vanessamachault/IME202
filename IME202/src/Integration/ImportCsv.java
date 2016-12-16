package Integration;
import java.sql.Connection;
import java.sql.Statement;

public class ImportCsv
{
	public static void main(String[] args)
	{
		loadCSV_ADICAP_CIMO3("./Ressources/Tables_Transcodage/transcodageVianneyLesionADICAP_CIMO3Morpho.csv");
		loadCSV_CIM101_CUI("./Ressources/Tables_Transcodage/transcodageVianneyCUI_CIMO3.csv");
	}


	public static void loadCSV_ADICAP_CIMO3(String csv)
	{
		try {
			//Connexion à la base de donnée
			String url= "jdbc:mysql://localhost/projet_ime_202";
			String user="root";
			String motpasse="";

			Connexion connect = new Connexion (url, user, motpasse);
			Connection con = connect.getCon();

			String loadQuery = "LOAD DATA LOCAL INFILE '" + csv + "' INTO TABLE transcodage_vianey_lesion_adicap_cimo3_morpho FIELDS TERMINATED BY ';'" + " LINES TERMINATED BY '\n' (Lesion, CIMO3_Morpho) ";
			System.out.println(loadQuery);
			Statement stmt = con.createStatement();
			stmt.execute(loadQuery);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static void loadCSV_CIM101_CUI(String csv)
	{
		try {
			//Connexion à la base de donnée
			String url= "jdbc:mysql://localhost/projet_ime_202";
			String user="root";
			String motpasse="";

			Connexion connect = new Connexion (url, user, motpasse);
			Connection con = connect.getCon();

			String loadQuery = "LOAD DATA LOCAL INFILE '" + csv + "' INTO TABLE transcodage_vianey_cui_cimo3 FIELDS TERMINATED BY ';'" + " LINES TERMINATED BY '\n' (CUI, CIMO3_Topo, CIMO3_Morpho) ";
			System.out.println(loadQuery);
			Statement stmt = con.createStatement();
			stmt.execute(loadQuery);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static void loadCSV_libelle_CIMO3_Topo(String csv)
	{
		try {
			//Connexion à la base de donnée
			String url= "jdbc:mysql://localhost/projet_ime_202";
			String user="root";
			String motpasse="";

			Connexion connect = new Connexion (url, user, motpasse);
			Connection con = connect.getCon();

			String loadQuery = "LOAD DATA LOCAL INFILE '" + csv + "' INTO TABLE libelle_cimo3_topo FIELDS TERMINATED BY ';'" + " LINES TERMINATED BY '\n' (Code_CIMO3_Topo, Libelle_CIMO3_Topo) ";
			System.out.println(loadQuery);
			Statement stmt = con.createStatement();
			stmt.execute(loadQuery);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static void loadCSV_libelle_CIMO3_Morpho(String csv)
	{
		try {
			//Connexion à la base de donnée
			String url= "jdbc:mysql://localhost/projet_ime_202";
			String user="root";
			String motpasse="";

			Connexion connect = new Connexion (url, user, motpasse);
			Connection con = connect.getCon();

			String loadQuery = "LOAD DATA LOCAL INFILE '" + csv + "' INTO TABLE libelle_cimo3_morpho FIELDS TERMINATED BY ';'" + " LINES TERMINATED BY '\n' (Code_CIMO3_Morpho, Libelle_CIMO3_Morpho) ";
			System.out.println(loadQuery);
			Statement stmt = con.createStatement();
			stmt.execute(loadQuery);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
}


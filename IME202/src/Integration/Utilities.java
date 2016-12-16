package Integration;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Utilities {

	private static BufferedReader bufferedReader;

	//Cette classe est à appeler avant toute exécution d'une méthode appelant du SQL
	//Elle permet de créer la base de donnée et les tables de la base en lisant un script présent à la racine du workspace
	//Pour fonctionner, il est nécessaire que la mase mysql créée par défaut soit toujours présente
	public static void CreateBDD(String user, String pass){

		BufferedReader br;
		Connection conn = null;
		Statement stmt = null;

		try{
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		}
		catch(Exception e){
			System.out.println("impossible de charger le pilote");
		}
		try{
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/projet_ime_202", user, pass);
		}
		catch(SQLException ek){
			System.out.println("Base Inexistante : création de la base de donnée");
			try{
				try{
					conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/mysql", user, pass);
				}
				catch(SQLException eh){  }

				stmt = conn.createStatement();
				stmt.executeUpdate("create database projet_ime_202");
				stmt.close();
				conn.close();
			}
			catch(SQLException ed){
				System.out.println("Impossible de creer projet_ime_202");
			}
			try{
				conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/projet_ime_202", user, pass);
				System.out.println("Connexion à projet_ime_202 reussie");
			}
			catch(SQLException ef){
				System.out.println("Impossible de se connecter à projet_ime_202");
			}
			try{
				stmt = conn.createStatement();

				// ici les scripts de création des tables.
				String thisLine, sqlQuery;
				String fichier = "projet_ime_202.sql";

				try {
					InputStream ips = new FileInputStream(fichier);
					InputStreamReader ipsr = new InputStreamReader(ips);
					br = new BufferedReader(ipsr);

					sqlQuery = "";
					while ((thisLine = br.readLine()) != null) 
					{  
						//Skip comments and empty lines
						if(thisLine.length() > 0 && thisLine.charAt(0) == '-' || thisLine.length() == 0) 
							continue;
						sqlQuery = sqlQuery + " " + thisLine;

						//If one command complete
						if(sqlQuery.charAt(sqlQuery.length() - 1) == ';') {
							sqlQuery = sqlQuery.replace(';' , ' '); //Remove the ; since jdbc complains
							try {
								stmt.execute(sqlQuery);
								System.out.println("La requête : " + sqlQuery + " à bien été executée.");
							}
							catch(SQLException ex) {
								JOptionPane.showMessageDialog(null, "Error Creating the SQL Database : " + ex.getMessage());
							}
							catch(Exception ex) {
								JOptionPane.showMessageDialog(null, "Error Creating the SQL Database : " + ex.getMessage());
							}
							sqlQuery = "";
						}   
					}
				}
				catch(IOException ex) {
				}
				catch(Exception ex) {
					JOptionPane.showMessageDialog(null, "Error Creating the SQL Database : " + ex.getMessage());
				}

				//Crétion du trigger DAS
				String query = getTriggerData("BEFORE_INSERT_DAS.txt");

				try{
					stmt.execute(query);
				}
				catch (SQLException e)
				{
					System.err.println("Caught SQL Exception - " + e.getMessage());
					e.printStackTrace();
				}

				//Création du trigger ADICAP
				String query2 = getTriggerData("BEFORE_INSERT_ADICAP.txt");
				try{
					stmt.execute(query2);
				}
				catch (SQLException e)
				{
					System.err.println("Caught SQL Exception - " + e.getMessage());
					e.printStackTrace();
				}

				//Création du trigger SEJOUR
				String query3 = getTriggerData("BEFORE_INSERT_SEJOUR.txt");
				try{
					stmt.execute(query3);
				}
				catch (SQLException e)
				{
					System.err.println("Caught SQL Exception - " + e.getMessage());
					e.printStackTrace();
				}
				
				//Importation des tables de trascodages
				ImportCsv.loadCSV_ADICAP_CIMO3("./Ressources/Tables_Transcodage/transcodageVianneyLesionADICAP_CIMO3Morpho.csv");
				ImportCsv.loadCSV_CIM101_CUI("./Ressources/Tables_Transcodage/transcodageVianneyCUI_CIMO3.csv");
				ImportCsv.loadCSV_libelle_CIMO3_Topo("./Ressources/Tables_Traduction/libelle_CIMO3_Topo.csv");
				ImportCsv.loadCSV_libelle_CIMO3_Morpho("./Ressources/Tables_Traduction/libelle_CIMO3_Morpho.csv");
				ImportCsv.loadCSV_libelle_CIM10("./Ressources/Tables_Traduction/libelle_CIM10.csv");

				stmt.close();
				conn.close();
			}
			catch(SQLException eg){
				System.out.println("Impossible de créer les tables");
			}

		}
		System.out.println("BASE DÉJÀ EXISTANTE"); 
		try{
			conn.close();
		}
		catch(SQLException v){

		}
	}

	//Cette classe permet de lire un fichier txt contenant le script de création d'un trigger.
	//Elle retrourne un string qui peut être exécuté via une executeQuery.
	public static String getTriggerData(String trigger){
		StringBuffer triggerQuery = new StringBuffer();
		try
		{
			FileInputStream inputStream = new FileInputStream(trigger);
			bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
			String line = null;
			while ((line = bufferedReader.readLine()) != null)
			{
				triggerQuery.append(line);
			}
		}
		catch (Exception e)
		{
			System.out.println("Exception while Reading MySQL_Trigger.txt File");
			e.printStackTrace();
		}
		return triggerQuery.toString();
	}

	//Cette classe permet de récupérer un objet JSON via l'API transcodage
	public static StringBuilder callURL(String codeADICAP) throws JSONException {
		StringBuilder sb = new StringBuilder();
		URLConnection urlConn = null;
		InputStreamReader in = null;
		try {
			URL url = new URL("http://transcoder.ebiobanques.fr/index.php?r=adicap/wsSearch&code=" + codeADICAP);
			urlConn = url.openConnection();
			if (urlConn != null)
				urlConn.setReadTimeout(60 * 1000);
			if (urlConn != null && urlConn.getInputStream() != null) {
				in = new InputStreamReader(urlConn.getInputStream(),
						Charset.defaultCharset());
				BufferedReader bufferedReader = new BufferedReader(in);
				if (bufferedReader != null) {
					int cp;
					while ((cp = bufferedReader.read()) != -1) {
						sb.append((char) cp);
					}
					bufferedReader.close();
				}
			}
			in.close();
		} catch (Exception e) {
			throw new RuntimeException("Impossible de transcoder le code ADICAP suivant : "+ codeADICAP, e);
		} 
		return sb;
	}

	//Cette méthode permet de traiter l'objet JSON récupéré via l'API de transcodage
	//La valeur int traduction et int transcodage prend différentes valeur en fonction de la quantité d'information souhaitée
	//	- 0 : pas de traduction || pas de transcodage
	//	- 1 : traduction || transcodage
	public static String[] transcodage_ADICAP_CIMO3 (String codeADICAP, int traduction, int transcodage) throws JSONException, ParseException{

		String CIM_O_3_Topo = null;
		String CIM_O_3_Morpho = null;
		String prelevement = null;
		String technique = null;

		JSONArray jsonArray = new JSONArray(Utilities.callURL(codeADICAP).toString());

		/*for (int i = 0 ; i < (jsonArray.length()) ; i++){
		System.out.println(jsonArray.getJSONArray(i));
		JSONArray jsonArraySub = new JSONArray(jsonArray.getJSONArray(i).toString(i));
		System.out.println("       " + jsonArraySub.get(1).toString());
		}*/

		JSONParser jsonParser = new JSONParser();

		if (traduction == 1){
			//Traitement mode de prélèvement
			JSONArray jsonArrayPrel= new JSONArray(jsonArray.getJSONArray(0).toString(0));
			JSONObject jsonObjectPrel = (JSONObject) jsonParser.parse(jsonArrayPrel.get(1).toString());
			if (jsonObjectPrel.get("attributes") != null) {
				JSONObject structurePrel = (JSONObject) jsonObjectPrel.get("attributes");
				//System.out.println("	Mode de prélèvement : " + structurePrel.get("LIBELLE"));
				prelevement = structurePrel.get("LIBELLE").toString();
			} else {
				System.out.println("	Mode de prélèvement non retrouvé dans la classification ADICAP.");
			}

			//Traitement technique
			JSONArray jsonArrayTech = new JSONArray(jsonArray.getJSONArray(1).toString(1));
			JSONObject jsonObjectTech = (JSONObject) jsonParser.parse(jsonArrayTech.get(1).toString());
			if (jsonObjectTech.get("attributes") != null) {
				JSONObject structureTech = (JSONObject) jsonObjectTech.get("attributes");
				//System.out.println("	Technique : " + structureTech.get("LIBELLE"));
				technique = structureTech.get("LIBELLE").toString();
			} else {
				System.out.println("	Technique non retrouvée dans la classification ADICAP.");
			}
		}

		if (transcodage == 1){
			//Traitement organe
			JSONArray jsonArrayOrgane = new JSONArray(jsonArray.getJSONArray(2).toString(2));
			JSONObject jsonObjectOrgane = (JSONObject) jsonParser.parse(jsonArrayOrgane.get(1).toString());
			if (jsonObjectOrgane.get("related") != null){
				JSONObject structureOrgane = (JSONObject) jsonObjectOrgane.get("related");
				if (structureOrgane.get("cIMMASTERs") != null) {
					JSONObject structureOrgane1 = (JSONObject) structureOrgane.get("cIMMASTERs");
					if (structureOrgane1.get("attributes") != null) {
						JSONObject structureOrgane2 = (JSONObject) structureOrgane1.get("attributes");
						//System.out.println("	CIM O-3 topo : " + structureOrgane2.get("code") + " (" + structureOrgane2.get("LIBELLE") + ")");
						CIM_O_3_Topo = structureOrgane2.get("code").toString();
					}else {
						//System.out.println("	CIM O-3 topo : Code non traduit à partir de la classification ADICAP.");
					}
				}else {
					//System.out.println("	CIM O-3 topo : Code non traduit à partir de la classification ADICAP.");
				}
			}else {
				//System.out.println("	CIM O-3 topo : Code non traduit à partir de la classification ADICAP.");
			}


			//Traitement morphologie
			JSONArray jsonArrayMorpho = new JSONArray(jsonArray.getJSONArray(3).toString(3));
			if (jsonArrayMorpho.get(1).getClass().toString().equalsIgnoreCase("class org.json.JSONObject")){
				JSONObject jsonObjectMorpho = (JSONObject) jsonParser.parse(jsonArrayMorpho.get(1).toString());
				if (jsonObjectMorpho.get("related") != null){
					JSONObject structureMorpho = (JSONObject) jsonObjectMorpho.get("related");
					if (structureMorpho.get("cIMOMORPHOs") != null){
						JSONObject structureMorpho2 = (JSONObject) structureMorpho.get("cIMOMORPHOs");
						if (structureMorpho2.get("attributes") != null){
							JSONObject structureMorpho3 = (JSONObject) structureMorpho2.get("attributes");
							//System.out.println("	CIM O-3 morpho : " + structureMorpho3.get("CODE") + " (" + structureMorpho3.get("LIBELLE") +")");
							CIM_O_3_Morpho = structureMorpho3.get("CODE").toString();
						}else {
							//System.out.println("	CIM O-3 morpho : Code non traduit à partir de la classification ADICAP.");
						}
					}else {
						//System.out.println("	CIM O-3 morpho : Code non traduit à partir de la classification ADICAP.");
					}
				}else {
					//System.out.println("	CIM O-3 morpho : Code non traduit à partir de la classification ADICAP.");
				}
			}else {
				//System.out.println("	CIM O-3 morpho : Code non traduit à partir de la classification ADICAP.");
			}
		}

		if (traduction == 1 && transcodage == 1){
			String result = prelevement + "|" + technique + "|" + CIM_O_3_Topo + "|" + CIM_O_3_Morpho;
			System.out.println(result);
			String [] transcodage_ADICAP = null;
			transcodage_ADICAP = result.split("\\|");		

			return transcodage_ADICAP;
		}

		if (traduction == 1 && transcodage == 0){
			String result = prelevement + "|" + technique;
			System.out.println(result);
			String [] transcodage_ADICAP = null;
			transcodage_ADICAP = result.split("\\|");		

			return transcodage_ADICAP;
		}

		if (traduction == 0 && transcodage == 1){
			String result = CIM_O_3_Topo + "|" + CIM_O_3_Morpho;
			String [] transcodage_ADICAP = null;
			transcodage_ADICAP = result.split("\\|");		

			return transcodage_ADICAP;
		}

		if (traduction == 0 && transcodage == 0){
			return null;
		}

		return null;

	} 

}

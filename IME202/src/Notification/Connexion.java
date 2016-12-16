package Notification;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class Connexion {

	private static Connection con;
	public static String url ="";
	public static String user ="";
	public static String motpasse ="";

	
	//Méthode création connexion
	public Connexion(String url, String user, String motpasse) {
		try {
			String driverName = "org.gjt.mm.mysql.Driver";
			Class.forName(driverName); 

		} catch (ClassNotFoundException e) {
			System.out.println("Driver non retrouvé");
			con=null;
		}

		try {
			con = DriverManager.getConnection(url,user,motpasse);

		} catch(SQLException ex) {
			System.out.println("Problème de connexion");
			con=null;
			System.exit(-1);
		}
	}
	
	
	//Méthode récupération connexion
	public Connection getCon() {
		return con;
		}
	
	
	//Méthode cloture connexion
	public static void close() {
		try {
			con.close();
		} catch (SQLException ex) {
			System.out.println("SQLException: " + ex.getMessage());
		} 		
	}

}

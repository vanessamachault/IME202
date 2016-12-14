package Identification;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnexionJdbcMySql {

	private Connection connect; 
	public static String url ="";
	public static String user ="";
	public static String motpasse ="";

	public ConnexionJdbcMySql (String url, String user, String password){ 
		try {
			String driverName = "org.gjt.mm.mysql.Driver";
			Class.forName(driverName);
		}
		catch (ClassNotFoundException e){
			System.out.println("DRIVER JDBC not OK");  
			System.exit(-1);
		}
		try{
			connect = DriverManager.getConnection(url,user,password);
		} 
		catch (SQLException ex){
			System.out.println("CONNEXION not OK");
			connect = null;
			System.exit(-1);
		}
	}

	public Connection getConnect(){
		return connect; 
	}

	public void close(Connection connect){
		try {
			connect.close();
		} 
		catch (SQLException ex){
			System.out.println("SQLException : " + ex.getMessage()); 
		}

	}
}







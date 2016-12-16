package Notification;
import java.util.ArrayList;

public class Notification {

	//Attributs
	private String cimO3Topo;
	private String cimO3Morpho;
	private String dateDg; 
	private ArrayList<Enregistrement> listeEnregistrement;
	
	//Constructeur
	
	public Notification(String cimO3Topo, String cimO3Morpho, String dateDg,
			ArrayList<Enregistrement> listeEnregistrement) {
		super();
		this.cimO3Topo = cimO3Topo;
		this.cimO3Morpho = cimO3Morpho;
		this.dateDg = dateDg;
		this.listeEnregistrement = listeEnregistrement;
	}
	
	//Getters et Setters
	public String getCimO3Topo() {
		return cimO3Topo;
	}
	public void setCimO3Topo(String cimO3Topo) {
		this.cimO3Topo = cimO3Topo;
	}
	public String getCimO3Morpho() {
		return cimO3Morpho;
	}
	public void setCimO3Morpho(String cimO3Morpho) {
		this.cimO3Morpho = cimO3Morpho;
	}
	public String getDateDg() {
		return dateDg;
	}
	public void setDateDg(String dateDg) {
		this.dateDg = dateDg;
	}
	public ArrayList<Enregistrement> getListeEnregistrement() {
		return listeEnregistrement;
	}
	public void setListeEnregistrement(ArrayList<Enregistrement> listeEnregistrement) {
		this.listeEnregistrement = listeEnregistrement;
	}
	
	
}

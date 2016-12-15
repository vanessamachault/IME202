package Notification;
import java.util.ArrayList;

public class GroupeMorpho {

	//Attributs 
	private String numGroupeMorpho;
	private ArrayList<Enregistrement> listeEnregistrement;
	
	
	//Constructeur 
	public GroupeMorpho(String numGroupeMorpho, ArrayList<Enregistrement> listeEnregistrement) {
		super();
		this.numGroupeMorpho = numGroupeMorpho;
		this.listeEnregistrement = listeEnregistrement;
	}

	//Getters et Setters
	public String getNumGroupeMorpho() {
		return numGroupeMorpho;
	}
	public void setNumGroupeMorpho(String numGroupeMorpho) {
		this.numGroupeMorpho = numGroupeMorpho;
	}
	public ArrayList<Enregistrement> getListeEnregistrement() {
		return listeEnregistrement;
	}
	public void setListeEnregistrement(ArrayList<Enregistrement> listeEnregistrement) {
		this.listeEnregistrement = listeEnregistrement;
	}
	
	
	
	
}

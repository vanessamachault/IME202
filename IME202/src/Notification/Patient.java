package Notification;
import java.util.ArrayList;

public class Patient {

	//Attributs
	private int numPatient; 
	private String nom;
	private String prenom;
	private String dateNaiss;
	private String sexe;
	private ArrayList<Enregistrement> listeEnregistrement;
	private ArrayList<Enregistrement> listeEnregistrementSys;
	private ArrayList<GroupeTopo> listeEnregistrementsGroupe;
	private ArrayList<Notification> listeNotification;


	//Constructeur
	public Patient(int numPatient, String nom, String prenom, String dateNaiss, String sexe,
			ArrayList<Enregistrement> listeEnregistrement, ArrayList<Enregistrement> listeEnregistrementSys,
			ArrayList<GroupeTopo> listeEnregistrementsGroupe, ArrayList<Notification> listeNotification) {
		super();
		this.numPatient = numPatient;
		this.nom = nom;
		this.prenom = prenom;
		this.dateNaiss = dateNaiss;
		this.sexe = sexe;
		this.listeEnregistrement = listeEnregistrement;
		this.listeEnregistrementSys = listeEnregistrementSys;
		this.listeEnregistrementsGroupe = listeEnregistrementsGroupe;
		this.listeNotification = listeNotification;
	}

	//Getters et setters
	public int getNumPatient() {
		return numPatient;
	}
		public void setNumPatient(int numPatient) {
		this.numPatient = numPatient;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public String getPrenom() {
		return prenom;
	}
	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}
	public String getDateNaiss() {
		return dateNaiss;
	}
	public void setDateNaiss(String dateNaiss) {
		this.dateNaiss = dateNaiss;
	}
	public String getSexe() {
		return sexe;
	}
	public void setSexe(String sexe) {
		this.sexe = sexe;
	}
	public ArrayList<Enregistrement> getListeEnregistrement() {
		return listeEnregistrement;
	}
	public void setListeEnregistrement(ArrayList<Enregistrement> listeEnregistrement) {
		this.listeEnregistrement = listeEnregistrement;
	}
	public ArrayList<GroupeTopo> getListeEnregistrementsGroupe() {
		return listeEnregistrementsGroupe;
	}
	public void setListeEnregistrementsGroupe(ArrayList<GroupeTopo> listeEnregistrementsGroupe) {
		this.listeEnregistrementsGroupe = listeEnregistrementsGroupe;
	}

	public ArrayList<Enregistrement> getListeEnregistrementSys() {
		return listeEnregistrementSys;
	}

	public void setListeEnregistrementSys(ArrayList<Enregistrement> listeEnregistrementSys) {
		this.listeEnregistrementSys = listeEnregistrementSys;
	}
	public ArrayList<Notification> getListeNotification() {
		return listeNotification;
	}
	public void setListeNotification(ArrayList<Notification> listeNotification) {
		this.listeNotification = listeNotification;
	}
	
}

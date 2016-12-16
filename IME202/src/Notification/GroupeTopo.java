package Notification;
import java.util.ArrayList;

public class GroupeTopo {

	//Attributs 
	private String numGroupeTopo;
	private ArrayList<GroupeMorpho> listeGroupeMorpho;
	
	
	//Constructeur 
	public GroupeTopo(String numGroupeTopo, ArrayList<GroupeMorpho> listeGroupeMorpho) {
		super();
		this.numGroupeTopo = numGroupeTopo;
		this.listeGroupeMorpho = listeGroupeMorpho;
	}

	//Getters and setters
	public String getNumGroupeTopo() {
		return numGroupeTopo;
	}
	public void setNumGroupeTopo(String numGroupeTopo) {
		this.numGroupeTopo = numGroupeTopo;
	}
	public ArrayList<GroupeMorpho> getListeGroupeMorpho() {
		return listeGroupeMorpho;
	}
	public void setListeGroupeMorpho(ArrayList<GroupeMorpho> listeGroupeMorpho) {
		this.listeGroupeMorpho = listeGroupeMorpho;
	}	
}

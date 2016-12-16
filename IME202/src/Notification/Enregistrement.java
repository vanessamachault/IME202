package Notification;

public class Enregistrement {

	//Attributs
	private int numPatient; 
	private int numPrel;
	private String source;
	private String cimO3Topo;
	private String groupeTopo;
	private String cimO3Morpho;
	private String groupeMorpho;
	private String date;
	private String superGroupeMorpho;
	

	//Constructeur
	
	public Enregistrement(int numPatient, int numLigne, String source,
			String cimO3Topo, String groupeTopo, String cimO3Morpho,
			String groupeMorpho, String date, String superGroupeMorpho) {
		super();
		this.numPatient = numPatient;
		this.numPrel = numLigne;
		this.source = source;
		this.cimO3Topo = cimO3Topo;
		this.groupeTopo = groupeTopo;
		this.cimO3Morpho = cimO3Morpho;
		this.groupeMorpho = groupeMorpho;
		this.date = date;
		this.superGroupeMorpho = superGroupeMorpho;
	}

	//Getters and setters
	public int getNumPatient() {
		return numPatient;
	}
	public void setNumPatient(int numPatient) {
		this.numPatient = numPatient;
	}
	public int getNumPrel() {
		return numPrel;
	}
	public void setNumPrel(int numLigne) {
		this.numPrel = numLigne;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getCimO3Topo() {
		return cimO3Topo;
	}
	public void setCimO3Topo(String cimO3Topo) {
		this.cimO3Topo = cimO3Topo;
	}
	public String getGroupeTopo() {
		return groupeTopo;
	}
	public void setGroupeTopo(String groupeTopo) {
		this.groupeTopo = groupeTopo;
	}
	public String getCimO3Morpho() {
		return cimO3Morpho;
	}
	public void setCimO3Morpho(String cimO3Morpho) {
		this.cimO3Morpho = cimO3Morpho;
	}
	public String getGroupeMorpho() {
		return groupeMorpho;
	}
	public void setGroupeMorpho(String groupeMorpho) {
		this.groupeMorpho = groupeMorpho;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
		public String getSuperGroupeMorpho() {
		return superGroupeMorpho;
	}
	public void setSuperGroupeMorpho(String superGroupeMorpho) {
		this.superGroupeMorpho = superGroupeMorpho;
	}

	//Comparateurs
	public boolean equalsTopo (Object enregistrement2) {
	    return enregistrement2 instanceof Enregistrement && groupeTopo.equals(((Enregistrement)enregistrement2).groupeTopo);
	}
	public boolean equalsMorpho (Object enregistrement2) {
	    return enregistrement2 instanceof Enregistrement && groupeTopo.equals(((Enregistrement)enregistrement2).groupeMorpho);
	}
}

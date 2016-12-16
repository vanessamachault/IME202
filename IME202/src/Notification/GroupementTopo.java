package Notification;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class GroupementTopo {

	public static ArrayList<ArrayList<Enregistrement>> groupement(ArrayList<Enregistrement> listeEnregistrement) {

		ArrayList<ArrayList<Enregistrement>> regroupementTopo = new ArrayList<>();
		Iterator<Enregistrement> iterateurListeEnregistrement = listeEnregistrement.iterator();
		ArrayList<String> repertoireGroupeTopo = new ArrayList<>();
		Set<String> repertoireGroupeTopoUnique = null; //Liste qui reçoit les groupes topo (sans doublon) pour un patient
		Enregistrement rec;

		while (iterateurListeEnregistrement.hasNext()){
			rec = iterateurListeEnregistrement.next();
			//Récupération des groupes topo de chaque enregistrement
			if (rec.getSuperGroupeMorpho()!=null){
				if (!rec.getSuperGroupeMorpho().equalsIgnoreCase("4S") 
						&& !rec.getSuperGroupeMorpho().equalsIgnoreCase("4NS")){
					String numGroupeTopo = rec.getGroupeTopo();
					repertoireGroupeTopo.add(numGroupeTopo);
				}
			}
		}

		//Elimination des doublons
		repertoireGroupeTopoUnique = new HashSet<String>(repertoireGroupeTopo);

		//Regroupement des enregistrements par groupe topographique
		if (repertoireGroupeTopoUnique.size()!=0){

			System.out.println(" repertoire groupes topo uniques" + repertoireGroupeTopoUnique.toString());
			Iterator<String> it = repertoireGroupeTopoUnique.iterator();


			while (it.hasNext()){
				ArrayList<Enregistrement> listeEnregistrementTopo = new ArrayList<>();
				String numGroupeTopo = it.next();
				System.out.println("	 Groupe topographique " + numGroupeTopo);
				for (int i=0; i<listeEnregistrement.size();i++){
					rec = listeEnregistrement.get(i);
					//Condition à revoir!!!!!!!!!
					if (rec.getGroupeTopo()!=null && rec.getGroupeTopo().equalsIgnoreCase(numGroupeTopo)){
						System.out.println("	 numéro de ligne " + rec.getNumPrel());
						listeEnregistrementTopo.add(rec);
					}
				}	
				System.out.println(" 	Liste enregistrement du groupe topo " + listeEnregistrementTopo.toString());
				regroupementTopo.add(listeEnregistrementTopo);
			}
			System.out.println(" Liste enregistrement des groupes topo" + regroupementTopo.toString());	
		}
		return regroupementTopo;
	}
}

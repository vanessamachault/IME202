package Notification;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class GroupementMorpho {

	public static GroupeTopo groupement(ArrayList<Enregistrement> listeEnregistrementParTopo) {

		ArrayList<GroupeMorpho> listeGroupeMorpho = new ArrayList<>();
		Iterator<Enregistrement> iterateurListeEnregistrementParTopo = listeEnregistrementParTopo.iterator();
		ArrayList<String> repertoireGroupeMorpho = new ArrayList<>();
		Set<String> repertoireGroupeMorphoUnique = null;
		Enregistrement rec;
		String numGroupeTopo = "";

		while (iterateurListeEnregistrementParTopo.hasNext()){
			rec = iterateurListeEnregistrementParTopo.next();
			String numGroupeMorpho = rec.getGroupeMorpho();
			repertoireGroupeMorpho.add(numGroupeMorpho);
			numGroupeTopo = rec.getGroupeTopo();
		}
		
		repertoireGroupeMorphoUnique = new HashSet<String>(repertoireGroupeMorpho);

		if (repertoireGroupeMorphoUnique.size()!=0){

			System.out.println(" " + repertoireGroupeMorphoUnique.toString());
			Iterator<String> it = repertoireGroupeMorphoUnique.iterator();

			while (it.hasNext()){
				ArrayList<Enregistrement> listeEnregistrementMorpho = new ArrayList<>();
				GroupeMorpho groupeMorpho = new GroupeMorpho(null, null);
				String numGroupeMorpho = it.next();
				System.out.println("	 Groupe Morphologique " + numGroupeMorpho);
				for (int i=0; i<listeEnregistrementParTopo.size();i++){
					rec = listeEnregistrementParTopo.get(i);
					//Condition à revoir!!!!!!!!!
					if (rec.getGroupeMorpho()!=null && rec.getGroupeMorpho().equalsIgnoreCase(numGroupeMorpho)){
						System.out.println("	 numéro de ligne " + rec.getNumPrel());
						listeEnregistrementMorpho.add(rec);
					}
				
				groupeMorpho.setNumGroupeMorpho(numGroupeMorpho);
				groupeMorpho.setListeEnregistrement(listeEnregistrementMorpho);
				}	
				System.out.println(" 	Liste enregistrement du groupe morpho " + listeEnregistrementMorpho.toString());
				listeGroupeMorpho.add(groupeMorpho);
			}
			System.out.println(" Liste enregistrement des groupes morpho" + listeGroupeMorpho.toString());
		}
		GroupeTopo groupeTopo = new GroupeTopo(numGroupeTopo, listeGroupeMorpho);
		return groupeTopo;
	}
}

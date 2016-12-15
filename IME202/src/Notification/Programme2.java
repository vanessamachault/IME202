package Notification;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Iterator;

public class Programme2{

	public static void main(String[] args) {

		//Récupération des patients uniques dans un ArrayList
		RecupPatient.recupPatient();
		ArrayList<Patient> listePatient = RecupPatient.recupPatient();

		//Récupération des enregistrements de chaque patient unique 
		RecupEnregistrement.recupEnregistrement(listePatient, "ADICAP");

		//Traitement !!!!	
		Iterator<Patient> iterateurListePatient = listePatient.iterator();
		while (iterateurListePatient.hasNext()){
			Patient p = iterateurListePatient.next();
			System.out.println("Numéro patient " + p.getNumPatient() + "|" + p.getListeEnregistrement().toString());
			ArrayList<Enregistrement> listeEnregistrement = p.getListeEnregistrement();
			if (listeEnregistrement.size() != 0){//Possible d'avoir des listes d'enregistrements null??
				//Attribution des groupes topographiques et morphologiques
				AttributionGroupeTopoMorpho.attributionGroupe(listeEnregistrement);
				//regroupement par groupe topo
				ArrayList<ArrayList<Enregistrement>> listeRegroupementTopo = GroupementTopo.groupement(listeEnregistrement);
				//Regroupement en groupe morpho au sein des groupes topo
				Iterator<ArrayList<Enregistrement>> iterateurRegroupementTopo = listeRegroupementTopo.iterator();
				ArrayList<GroupeTopo> listeEnregistrementsGroupe = new ArrayList<>();
				while (iterateurRegroupementTopo.hasNext()){//Pour chaque arrayList correspondant à un regroupement topo je fais : 
					ArrayList<Enregistrement> listeEnregistrementParTopo = iterateurRegroupementTopo.next();
					GroupeTopo groupeTopo = GroupementMorpho.groupement(listeEnregistrementParTopo);
					listeEnregistrementsGroupe.add(groupeTopo);
				}
				p.setListeEnregistrementsGroupe(listeEnregistrementsGroupe);
				System.out.println(p.getListeEnregistrementsGroupe().toString());
			}
		}	
	}
}

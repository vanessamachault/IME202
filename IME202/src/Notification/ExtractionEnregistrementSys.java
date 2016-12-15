package Notification;
import java.util.ArrayList;
import java.util.Iterator;

public class ExtractionEnregistrementSys {

	public static  ArrayList<Enregistrement> main(ArrayList<Enregistrement> listeEnregistrement) {

		Iterator<Enregistrement> iterateurListeEnregistrement = listeEnregistrement.iterator();
		ArrayList<Enregistrement> listeEnregistrementSys = new ArrayList<>();
		
		while (iterateurListeEnregistrement.hasNext()){
			Enregistrement enregistrement = iterateurListeEnregistrement.next();
			if (enregistrement.getSuperGroupeMorpho().equalsIgnoreCase("4S")
					||enregistrement.getSuperGroupeMorpho().equalsIgnoreCase("4S")){
				listeEnregistrementSys.add(enregistrement);
			}
		}
		return listeEnregistrementSys;
	}
}


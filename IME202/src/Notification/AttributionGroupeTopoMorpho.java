package Notification;
import java.util.ArrayList;
import java.util.Iterator;

public class AttributionGroupeTopoMorpho {

	public static void attributionGroupe (ArrayList<Enregistrement> listeEnregistrement){

		Iterator<Enregistrement> iterateurListeEnregistrement = listeEnregistrement.iterator();

		while (iterateurListeEnregistrement.hasNext()){
			Enregistrement enregistrement = iterateurListeEnregistrement.next();
			if (enregistrement.getCimO3Topo()!=null){
				String subsCimO3Topo = enregistrement.getCimO3Topo().substring(0, 3);
				System.out.println(subsCimO3Topo);
				switch (subsCimO3Topo){
				case "C00":
					enregistrement.setGroupeTopo("C06.9");
					break;	
				case "C01":
					enregistrement.setGroupeTopo("C02.9");
					break;
				case "C02":
					enregistrement.setGroupeTopo("C02.9");
					break;
				case "C03":
					enregistrement.setGroupeTopo("C06.9");
					break;	
				case "C04":
					enregistrement.setGroupeTopo("C06.9");
					break;	
				case "C05":
					enregistrement.setGroupeTopo("C06.9");
					break;	
				case "C06":
					enregistrement.setGroupeTopo("C06.9");
					break;
				case "C09":
					enregistrement.setGroupeTopo("C14.0"); 
					break;	
				case "C10":
					enregistrement.setGroupeTopo("C14.0"); 
					break;
				case "C12":
					enregistrement.setGroupeTopo("C14.0"); 
					break;	
				case "C13":
					enregistrement.setGroupeTopo("C14.0"); 
					break;
				case "C14":
					enregistrement.setGroupeTopo("C14.0"); 
					break;	
				case "C19":
					enregistrement.setGroupeTopo("C20.9"); 
					break;	
				case "C20":
					enregistrement.setGroupeTopo("C20.9"); 
					break;	
				case "C23":
					enregistrement.setGroupeTopo("C24.9"); 
					break;	
				case "C24":
					enregistrement.setGroupeTopo("C24.9"); 
					break;
				case "C33":
					enregistrement.setGroupeTopo("C34.9"); 
					break;
				case "C34":
					enregistrement.setGroupeTopo("C34.9"); 
					break;
				case "C40":
					enregistrement.setGroupeTopo("41.9"); 
					break;
				case "C41":
					enregistrement.setGroupeTopo("41.9"); 
					break;
				case "C65":
					enregistrement.setGroupeTopo("68.9"); 
					break;
				case "C66":
					enregistrement.setGroupeTopo("68.9"); 
					break;
				case "C67":
					enregistrement.setGroupeTopo("68.9"); 
					break;
				case "C68":
					enregistrement.setGroupeTopo("68.9"); 
					break;
				default: 
					enregistrement.setGroupeTopo(enregistrement.getCimO3Topo());
				}
			}//Quid ceux sans code Topo?

			//Traitement groupe morpho 
			if (enregistrement.getCimO3Morpho()!=null){
				String subsCimO3Morpho = enregistrement.getCimO3Morpho().substring(2,6);
				int intCimO3Morpho = Integer.parseInt(subsCimO3Morpho);

				if (Utilitaires.isBetween(intCimO3Morpho, 8051, 8084)
						||Utilitaires.isBetween(intCimO3Morpho, 8120, 8131)){
					enregistrement.setGroupeMorpho("1");
				}
				else if (Utilitaires.isBetween(intCimO3Morpho, 8090, 8110)){
					enregistrement.setGroupeMorpho("2");
				}
				else if (Utilitaires.isBetween(intCimO3Morpho, 8140, 8149)
						||Utilitaires.isBetween(intCimO3Morpho, 8160, 8162)
						||Utilitaires.isBetween(intCimO3Morpho, 8190, 8221)
						||Utilitaires.isBetween(intCimO3Morpho, 8260, 8337)
						||Utilitaires.isBetween(intCimO3Morpho, 8350, 8551)
						||Utilitaires.isBetween(intCimO3Morpho, 8570, 8576)
						||Utilitaires.isBetween(intCimO3Morpho, 8940, 8941)){
					enregistrement.setGroupeMorpho("3");
				}
				else if (Utilitaires.isBetween(intCimO3Morpho, 8030, 8046)
						||Utilitaires.isBetween(intCimO3Morpho, 8150, 8157)
						||Utilitaires.isBetween(intCimO3Morpho, 8170, 8180)
						||Utilitaires.isBetween(intCimO3Morpho, 8230, 8255)
						||Utilitaires.isBetween(intCimO3Morpho, 8340, 8347)
						||Utilitaires.isBetween(intCimO3Morpho, 8560, 8562)
						||Utilitaires.isBetween(intCimO3Morpho, 8580, 8671)){
					enregistrement.setGroupeMorpho("4");
				}
				else if (Utilitaires.isBetween(intCimO3Morpho, 8010, 8015)
						||Utilitaires.isBetween(intCimO3Morpho, 8020, 8022)
						||intCimO3Morpho == 8050){
					enregistrement.setGroupeMorpho("5");	
				}
				else if (Utilitaires.isBetween(intCimO3Morpho, 8680, 8713)
						||Utilitaires.isBetween(intCimO3Morpho, 8800, 8921)
						||Utilitaires.isBetween(intCimO3Morpho, 8990, 8991)
						||Utilitaires.isBetween(intCimO3Morpho, 9040, 9044)
						||Utilitaires.isBetween(intCimO3Morpho, 9120, 9125)
						||Utilitaires.isBetween(intCimO3Morpho, 9130, 9136)
						||Utilitaires.isBetween(intCimO3Morpho, 9141, 9252)
						||Utilitaires.isBetween(intCimO3Morpho, 9370, 9373)
						||Utilitaires.isBetween(intCimO3Morpho, 9540, 9582)){
					enregistrement.setGroupeMorpho("6");
				}
				else if (Utilitaires.isBetween(intCimO3Morpho, 9050, 9055)){
					enregistrement.setGroupeMorpho("7");
				}
				else if (intCimO3Morpho == 8050
						||Utilitaires.isBetween(intCimO3Morpho, 9861, 9931)
						||Utilitaires.isBetween(intCimO3Morpho, 9945, 9946)
						||intCimO3Morpho == 9950
						||Utilitaires.isBetween(intCimO3Morpho, 9961, 9964)
						||Utilitaires.isBetween(intCimO3Morpho, 9980, 9987)){
					enregistrement.setGroupeMorpho("8");
				}
				else if (Utilitaires.isBetween(intCimO3Morpho, 9670, 9699)
						||intCimO3Morpho == 9728
						||Utilitaires.isBetween(intCimO3Morpho, 9731, 9734)
						||Utilitaires.isBetween(intCimO3Morpho, 9761, 9767)
						||intCimO3Morpho == 9769
						||Utilitaires.isBetween(intCimO3Morpho, 9823, 9826)
						||intCimO3Morpho == 9833
						||intCimO3Morpho == 9836
						||intCimO3Morpho == 9940){
					enregistrement.setGroupeMorpho("9");
				}
				else if (Utilitaires.isBetween(intCimO3Morpho, 9700, 9719)
						||intCimO3Morpho == 9729
						||intCimO3Morpho == 9768
						||Utilitaires.isBetween(intCimO3Morpho, 9827, 9831)
						||intCimO3Morpho == 9834
						||intCimO3Morpho == 9837
						||intCimO3Morpho == 9948){
					enregistrement.setGroupeMorpho("10");
				}
				else if (Utilitaires.isBetween(intCimO3Morpho, 9650, 9667)){
					enregistrement.setGroupeMorpho("11");
				}
				else if (Utilitaires.isBetween(intCimO3Morpho, 9740, 9742)){
					enregistrement.setGroupeMorpho("12");
				}
				else if (Utilitaires.isBetween(intCimO3Morpho, 9750, 9758)){
					enregistrement.setGroupeMorpho("13");
				}
				else if (Utilitaires.isBetween(intCimO3Morpho, 9590, 9591)
						||intCimO3Morpho == 9596
						||intCimO3Morpho == 9727
						||intCimO3Morpho == 9760
						||Utilitaires.isBetween(intCimO3Morpho, 9800, 9801)
						||intCimO3Morpho == 9805
						||intCimO3Morpho == 9820
						||intCimO3Morpho == 9832
						||intCimO3Morpho == 9835
						||intCimO3Morpho == 9860
						||intCimO3Morpho == 9960
						||intCimO3Morpho == 9970
						||intCimO3Morpho == 9975
						||intCimO3Morpho == 9989){
					enregistrement.setGroupeMorpho("14");
				}
				else if (intCimO3Morpho == 9140){
					enregistrement.setGroupeMorpho("15");
				}
				else if (Utilitaires.isBetween(intCimO3Morpho, 8720, 8790)
						||Utilitaires.isBetween(intCimO3Morpho, 8930, 8936)
						||Utilitaires.isBetween(intCimO3Morpho, 8950, 8983)
						||Utilitaires.isBetween(intCimO3Morpho, 9000, 9030)
						||Utilitaires.isBetween(intCimO3Morpho, 9060, 9110)
						||Utilitaires.isBetween(intCimO3Morpho, 9260, 9365)
						||Utilitaires.isBetween(intCimO3Morpho, 9380, 9539)){
					enregistrement.setGroupeMorpho("16");
				}
				else if (Utilitaires.isBetween(intCimO3Morpho, 8000, 8005)){
					enregistrement.setGroupeMorpho("17");
				}
			}
			//Traitement groupe morpho 
			if (enregistrement.getGroupeMorpho()!=null){
				String groupeMorpho = enregistrement.getGroupeMorpho();
				switch (groupeMorpho){
				case "1":
					enregistrement.setSuperGroupeMorpho("1S");
					break;
				case "2":
					enregistrement.setSuperGroupeMorpho("1S");
					break;
				case "3":
					enregistrement.setSuperGroupeMorpho("1S");
					break;	
				case "4":
					enregistrement.setSuperGroupeMorpho("1S");
					break;
				case "5":
					enregistrement.setSuperGroupeMorpho("1NS");
					break;	
				case "6":
					enregistrement.setSuperGroupeMorpho("2");
					break;	
				case "7":
					enregistrement.setSuperGroupeMorpho("3");
					break;	
				case "8":
					enregistrement.setSuperGroupeMorpho("4S");
					break;	
				case "9":
					enregistrement.setSuperGroupeMorpho("4S");
					break;	
				case "10":
					enregistrement.setSuperGroupeMorpho("4S");
					break;	
				case "11":
					enregistrement.setSuperGroupeMorpho("4S");
					break;	
				case "12":
					enregistrement.setSuperGroupeMorpho("4S");
					break;	
				case "13":
					enregistrement.setSuperGroupeMorpho("4S");
					break;	
				case "14":
					enregistrement.setSuperGroupeMorpho("4NS");
					break;	
				case "15":
					enregistrement.setSuperGroupeMorpho("5");
					break;	
				case "16":
					enregistrement.setSuperGroupeMorpho("6");
					break;	
				case "17":
					enregistrement.setSuperGroupeMorpho("7");
					break;	
				}
			}
			System.out.println(enregistrement.getGroupeTopo());
			System.out.println(enregistrement.getGroupeMorpho());
			System.out.println(enregistrement.getSuperGroupeMorpho());
		}
	}
}

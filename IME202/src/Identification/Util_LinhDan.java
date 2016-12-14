package Identification;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class Util_LinhDan {

	/* **************************************************************************************************************** */
	public static int perSimilarity(String a,String b){ 
		a = a.toLowerCase();
		b = b.toLowerCase();
		int distance = 0;
		int [] costs = new int [b.length() + 1];
		for (int j = 0; j < costs.length; j++){
			costs[j] = j;
		}
		for (int i = 1; i <= a.length(); i++) {
			costs[0] = i;
			int nw = i - 1;
			for (int j = 1; j <= b.length(); j++) {
				int cj = Math.min(1 + Math.min(costs[j], costs[j - 1]), a.charAt(i - 1) == b.charAt(j - 1) ? nw : nw + 1);
				nw = costs[j];
				costs[j] = cj;
			}
			distance = costs[b.length()];
		}
		double len = Math.max(a.length(), b.length());
		double result = len - distance;
		int percent = (int) (result/len*100);
		return percent;
	}

	/* **************************************************************************************************************** */
	public static List<String> arraytolist(ArrayList<String> a){
		Set <String> b = new LinkedHashSet<String>(a);
		List<String> c = new ArrayList<String>(b);
		return c;

	}

	public static String[][] stringToTab2d(String a, int nbvariable){
		String[] tab1d = a.split(";");		

		int r = 0;
		String[][] tab2d = new String[tab1d.length][nbvariable]; 
		for (String tab1 : tab1d) {
			tab2d[r++] = tab1.split("\\|",nbvariable);
		}
		return tab2d; 
	}

	public static String[][] arrayToTab2d(ArrayList<String> a, int nbvariable){
		String[][] tab2d;
		if(a.size()==0){
			tab2d = null;
			return tab2d;
		} 
		else {
		String data = "";
		for (String s : a)
		{
			data += s + ";";
		}
		String[] tab1d = data.split(";");		
		tab2d = new String[a.size()][nbvariable]; 
		int r = 0;
		for (String tab1 : tab1d) {
			tab2d[r++] = tab1.split("\\|");
		}
		return tab2d; 
		}
	}

	public static String[][] append(String[][] a, String[][] b) {
		String[][] tab = null;
		
		if(a!=null && b!=null){
			tab = new String[a.length + b.length][];
			System.arraycopy(a, 0, tab, 0, a.length);
			System.arraycopy(b, 0, tab, a.length,b.length);
		 } else if (a==null){
			tab = new String[b.length][];
			System.arraycopy(b, 0, tab, 0, b.length);
		} else if (b==null){
			tab = new String[a.length][];
			System.arraycopy(a, 0, tab, 0, a.length);
		} else {
			tab = null;
			System.out.println("Aucun doublon.");
		}
		return tab;
	}


	/* **************************************************************************************************************** */
	public static String soundExEn(String s) { 
		char[] x = s.toUpperCase().toCharArray();
		char firstLetter = x[0];

		// Convert Letters to Numeric code
		for (int i = 0; i < x.length; i++) {
			switch (x[i]) {

			case 'B':
			case 'F':
			case 'P':
			case 'V':
				x[i] = '1';
				break;

			case 'C':
			case 'G':
			case 'J':
			case 'K':
			case 'Q':
			case 'S':
			case 'X':
			case 'Z':
				x[i] = '2';
				break;

			case 'D':
			case 'T':
				x[i] = '3';
				break;

			case 'L':
				x[i] = '4';
				break;

			case 'M':
			case 'N':
				x[i] = '5';
				break;

			case 'R':
				x[i] = '6';
				break;

			default:
				x[i] = '0';
				break;
			}
		}

		String output = "" + firstLetter;
		for (int i = 1; i < x.length; i++)
			if (x[i] != x[i-1] && x[i] != '0')
				output += x[i];
		output = output + "0000";
		return output.substring(0,4);
	}
	
	public static String soundExFr(String s) { 
		char[] x = s.toUpperCase().toCharArray();
		char firstLetter = x[0];

		// Convert Letters to Numeric code
		for (int i = 0; i < x.length; i++) {
			switch (x[i]) {

			case 'B':
			case 'P':
				x[i] = '1';
				break;

			case 'C':
			case 'K':
			case 'Q':
				x[i] = '2';
				break;

			case 'D':
			case 'T':
				x[i] = '3';
				break;

			case 'L':
				x[i] = '4';
				break;

			case 'M':
			case 'N':
				x[i] = '5';
				break;

			case 'R':
				x[i] = '6';
				break;
			
			case 'G':
			case 'J':
				x[i] = '7';
				break;
				
			case 'X':
			case 'Z':
			case 'S':
				x[i] = '8';
				break;
				
			case 'F':
			case 'V':
				x[i] = '9';
				break;

			default:
				x[i] = '0';
				break;
			}
		}

		String output = "" + firstLetter;
		for (int i = 1; i < x.length; i++)
			if (x[i] != x[i-1] && x[i] != '0')
				output += x[i];
		output = output + "0000";
		return output.substring(0,4);
	}

	/* **************************************************************************************************************** */

}

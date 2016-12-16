package Interface;

import java.awt.Color;
import java.awt.Font;

public class Design {
	
	static public Color setCouleurFond(){
		Color couleurFond = new Color(238,238,238);
		return couleurFond;
	}
	
	static public Color setCouleurTexte(){
		Color couleurTexte = new Color(18,0,74);
		return couleurTexte;
	}
	
	static public Color setCouleurTransparente(){
		Color couleurTransparente = new Color(0,0,0,0);
		return couleurTransparente;
	}	
	
	static public Font setFontTexte(){
		Font fontTexte = new Font("DejaVu Sans", Font.BOLD, 12);
		return fontTexte;
	}
	
}

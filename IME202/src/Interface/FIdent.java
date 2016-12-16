package Interface;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellRenderer;

import Identification.Util_LinhDan;

@SuppressWarnings("serial")
public class FIdent extends JFrame  {

	private Color couleur = new Color(240,240,240);

	ImageIcon logo = new ImageIcon("img/logo.png");
	JLabel image = new JLabel(logo);

	ImageIcon imagePrecedent = new ImageIcon("img/precedent.png");
	ImageIcon imageSuivant = new ImageIcon("img/suivant.png");
	ImageIcon imageGrouper = new ImageIcon("img/centraliser.png");
	ImageIcon imagePlus = new ImageIcon("img/plus.png");
	ImageIcon imageMoins = new ImageIcon("img/moins.png");

	GridBagLayout gridL = new GridBagLayout();
	GridBagConstraints gbc = new GridBagConstraints();

	private JPanel content = new JPanel();
	private Container identite;

	private JPanel cell0 = new JPanel();
	private JPanel cell1 = new JPanel();
	private JPanel cell2 = new JPanel();
	private JPanel cell3 = new JPanel();
	private JPanel cell4 = new JPanel();
	private JPanel cell5 = new JPanel();
	private JPanel cell6 = new JPanel();
	private JPanel cell7 = new JPanel();
	private JPanel cell8 = new JPanel();
	private JPanel cell9 = new JPanel();
	private JPanel cell10 = new JPanel();
	private JPanel cell11 = new JPanel();
	private JPanel cell12 = new JPanel();
	private JPanel cell13 = new JPanel();
	private JPanel cell14 = new JPanel();
	private JPanel cell15 = new JPanel();
	private JPanel cell16 = new JPanel();
	private JPanel cell17 = new JPanel();

	private JButton grouper = new JButton(imageGrouper);
	private JButton precedent = new JButton(imagePrecedent);
	private JButton suivant = new JButton(imageSuivant);

	private JLabel affichageBouton = new JLabel("Id");
	private JLabel affichageBox = new JLabel("Groupe");
	private JLabel affichageDerouler = new JLabel("Diag.");
	private JLabel affichageNom = new JLabel("Nom");
	private JLabel affichagePrenom = new JLabel("Prenom");
	private JLabel affichageDDN= new JLabel("Date de naissance");
	private JLabel affichageSexe= new JLabel("Sexe");
	private JLabel affichagePMSI = new JLabel("PMSI");
	private JLabel affichageADICAP = new JLabel("ADICAP");

	private ButtonGroup boutons = new ButtonGroup();

	private Color couleurTexte;
	private Font fontTexte;
	private Color couleurFond;
	private Color couleurTransparente;

	Dimension dimColonne1 = new Dimension(50, 50);
	Dimension dimColonne2 = new Dimension(50, 50);
	Dimension dimColonne3 = new Dimension(250, 50);
	Dimension dimColonne4 = new Dimension(225, 50);
	Dimension dimColonne5 = new Dimension(50, 50);
	Dimension dimColonne6 = new Dimension(100, 50);
	Dimension dimColonne7 = new Dimension(100, 50);
	Dimension dimColonne8 = new Dimension(100, 50);
	Dimension dimColonne9 = new Dimension(75, 50);

	private static final long serialVersionUID = 1L;

	int w = 0;
	int numy;

	List<String[]> contentTable = new ArrayList<>();

	ArrayList<String> listePatients1 = new ArrayList<String>();
	ArrayList<String> listePatients2 = new ArrayList<String>();
	ArrayList<String> listePatients3 = new ArrayList<String>();
	ArrayList<String> listePatients4 = new ArrayList<String>();
	ArrayList<String> listePatients5 = new ArrayList<String>();
	ArrayList<String> listePatients6 = new ArrayList<String>();

	// C'est la variable que l'on récupère de Linh Dan
	ArrayList<ArrayList> listeGroupes = new ArrayList<ArrayList>();

	ArrayList<ArrayList<String>> listeGroupeEnCours = new ArrayList();
	int idGroupe = 0;
	int premierPassage = 1;

	String[][] listePatients = new String[0][0];
	String[][] tableauGroupeEnCours = new String[0][0];

	// ArrayList qui stocke l'identifiant des groupes pour naviguer précédent/suivant
	ArrayList listeIdGroupe = new ArrayList();

	// listeGroupes.addadd(0,listePatients1);
	String listeDiag[][] = { {"1","sqdqsdqsdeazeazez","iuiouiouiouuo"},{"2","gdfhfghjtrtertrt"},{"3","dfgregergdgdfgdgdf","jhsdbfjhsfbjhsdfb"}};

	/** ****************************************** Création de la fenêtre de gestion des identités *********************************************** **/

	Container createAndShowFImportation(){

		initListePatients();

		initLayout();


		// Première ligne
		//---------------------------------------------
		//---------------------------------------------

		gbc.gridx = 0;
		gbc.gridy = 0;

		cell0.add(affichageBouton);
		content.add(cell0, gbc);
		//---------------------------------------------
		gbc.gridx = 1;

		cell1.add(affichageBox);
		content.add(cell1, gbc);
		//---------------------------------------------
		gbc.gridx = 2;

		cell2.add(affichageNom);
		content.add(cell2, gbc);
		//---------------------------------------------
		gbc.gridx = 3;

		cell3.add(affichagePrenom);
		content.add(cell3, gbc);
		//---------------------------------------------
		gbc.gridx = 4;

		cell4.add(affichageSexe);
		content.add(cell4, gbc);
		//---------------------------------------------
		gbc.gridx = 5;

		cell5.add(affichageDDN);
		content.add(cell5, gbc);
		//---------------------------------------------
		gbc.gridx = 6;

		cell6.add(affichagePMSI);
		content.add(cell6, gbc);
		//---------------------------------------------
		gbc.gridx = 7;

		cell7.add(affichageADICAP);
		content.add(cell7, gbc);
		//---------------------------------------------
		gbc.gridx = 8;

		cell8.add(affichageDerouler);
		content.add(cell8, gbc);

		numy = 1;

		//Création des objets cell selon le nombre de patients
		JPanel[] listeCella = new JPanel[listePatients.length];
		for (int i=0;i<listePatients.length;i++) {
			String nom = "cella_"+i;
			listeCella[i] = new JPanel();
			listeCella[i].setName(nom);
		}
		JPanel[] listeCellb = new JPanel[listePatients.length];
		for (int i=0;i<listePatients.length;i++) {
			String nom = "cella_"+i;
			listeCellb[i] = new JPanel();
			listeCellb[i].setName(nom);
		}
		JPanel[] listeCellc = new JPanel[listePatients.length];
		for (int i=0;i<listePatients.length;i++) {
			String nom = "cella_"+i;
			listeCellc[i] = new JPanel();
			listeCellc[i].setName(nom);
		}
		JPanel[] listeCelld = new JPanel[listePatients.length];
		for (int i=0;i<listePatients.length;i++) {
			String nom = "cella_"+i;
			listeCelld[i] = new JPanel();
			listeCelld[i].setName(nom);
		}
		JPanel[] listeCelle = new JPanel[listePatients.length];
		for (int i=0;i<listePatients.length;i++) {
			String nom = "cella_"+i;
			listeCelle[i] = new JPanel();
			listeCelle[i].setName(nom);
		}
		JPanel[] listeCellf = new JPanel[listePatients.length];
		for (int i=0;i<listePatients.length;i++) {
			String nom = "cella_"+i;
			listeCellf[i] = new JPanel();
			listeCellf[i].setName(nom);
		}
		JPanel[] listeCellg = new JPanel[listePatients.length];
		for (int i=0;i<listePatients.length;i++) {
			String nom = "cella_"+i;
			listeCellg[i] = new JPanel();
			listeCellg[i].setName(nom);
		}
		JPanel[] listeCellh = new JPanel[listePatients.length];
		for (int i=0;i<listePatients.length;i++) {
			String nom = "cella_"+i;
			listeCellh[i] = new JPanel();
			listeCellh[i].setName(nom);
		}
		JPanel[] listeCelli = new JPanel[listePatients.length];
		for (int i=0;i<listePatients.length;i++) {
			String nom = "cella_"+i;
			listeCelli[i] = new JPanel();
			listeCelli[i].setName(nom);
		}

		// Création des objets cell destiné à recevoir les diagnostics
		JPanel[] listeCellDiaga = new JPanel[listePatients.length];
		for (int i=0;i<listePatients.length;i++) {
			String nom = "celldiaga_"+i;
			listeCellDiaga[i] = new JPanel();
			listeCellDiaga[i].setName(nom);
		}
		JPanel[] listeCellDiagb = new JPanel[listePatients.length];
		for (int i=0;i<listePatients.length;i++) {
			String nom = "celldiagb_"+i;
			listeCellDiagb[i] = new JPanel();
			listeCellDiagb[i].setName(nom);
		}

		//Création des boutons radio selon le nombre de patients
		JRadioButton[] listeBoutons = new JRadioButton[listePatients.length];
		for (int i=0;i<listePatients.length;i++) {
			listeBoutons[i] = new JRadioButton();
			listeBoutons[i].setName(listePatients[i][0]);
			if (i==0){
				listeBoutons[i].setSelected(true);
			}
			boutons.add(listeBoutons[i]);
		}

		//Création des checkbox selon le nombre de patients
		JCheckBox[] listeBox = new JCheckBox[listePatients.length];
		for (int i=0;i<listePatients.length;i++) {
			listeBox[i] = new JCheckBox();
			listeBox[i].setName(listePatients[i][0]);
		}

		//Création des DEPLIER selon le nombre de patients
		JButton[] listePlus = new JButton[listePatients.length];
		for (int i=0;i<listePatients.length;i++) {
			listePlus[i] = new JButton();
			listePlus[i].setName(listePatients[i][0]);	  
			listePlus[i].setIcon(imagePlus);
		}

		//Création des REPLIER selon le nombre de patients
		JButton[] listeMoins = new JButton[listePatients.length];
		for (int i=0;i<listePatients.length;i++) {
			listeMoins[i] = new JButton();
			listeMoins[i].setName(listePatients[i][0]);	  
			listeMoins[i].setIcon(imageMoins);
		}

		// Création des listeners pour déplier
		for (int i=0;i<listePatients.length;i++) {
			listePlus[i].addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {

					JButton Bsrc= (JButton) e.getSource();
					String numPatient= Bsrc.getName();

					for (int i=0;i<listePatients.length;i++) {
						if (listePatients[i][0]==numPatient) {
							// Afficher la case avec les diagnostics
							listeCellDiaga[i].setVisible(true);

							// Remplacer le bouton plus par le bouton moins*
							listeCelli[i].removeAll();
							listeCelli[i].add(listeMoins[i]);		
							listeCelli[i].revalidate();
							listeCelli[i].repaint();
							WindowUtilities.refresh(content);
						}
					}

				}

			});
		}

		// Création des listeners pour replier
		for (int i=0;i<listePatients.length;i++) {
			listeMoins[i].addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {

					JButton Bsrc= (JButton) e.getSource();
					String numPatient= Bsrc.getName();

					for (int i=0;i<listePatients.length;i++) {
						if (listePatients[i][0]==numPatient) {
							listeCellDiaga[i].setVisible(false);
							// Remplacer le bouton plus par le bouton moins*
							listeCelli[i].removeAll();
							listeCelli[i].add(listePlus[i]);		
							listeCelli[i].revalidate();
							listeCelli[i].repaint();
							WindowUtilities.refresh(content);
						}
					}

				}

			});
		}

		grouper.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//System.out.println(e.getSource());
				ArrayList<String> parametresGroupage = new ArrayList<String>();
				for (int i=0;i<listePatients.length;i++) {
					if(listeBoutons[i].isSelected()) System.out.println("BOutons selectionnees "+listeBoutons[i].getName());
					parametresGroupage.add(listeBoutons[i].getName());
				}	
				for (int i=0;i<listePatients.length;i++) {
					if(listeBox[i].isSelected()) System.out.println("Box selectionnees "+listeBox[i].getName());
					// On n'ajoute pas de nouveau le patient sélectionné pour l'id principale
					if (listeBox[i].getName()!=listeBoutons[i].getName()) parametresGroupage.add(listeBox[i].getName());
				}	
				// Appeller méthode pour grouper
			}	
		});

		suivant.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				premierPassage = 0;
				idGroupe = idGroupe+1;
				content.removeAll();
				listeGroupeEnCours.clear();

				createAndShowFImportation();
				System.out.println("ID GROUPE" + idGroupe);
				content.revalidate();
				content.repaint();

				WindowUtilities.refresh(content);
			}	
		});
		
		precedent.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				premierPassage = 0;
				idGroupe = idGroupe-1;
				content.removeAll();
				listeGroupeEnCours.clear();

				createAndShowFImportation();
				System.out.println("ID GROUPE" + idGroupe);
				content.revalidate();
				content.repaint();
				
				WindowUtilities.refresh(content);
			}	
		});
		
		// Création de la liste de patients
		for(int i = 0; i < listePatients.length; i++){	  

			gbc.gridy = numy;
			gbc.gridwidth = 1;

			// ID principale
			gbc.gridx = 0;	
			listeCella[i].setBackground(Color.WHITE);
			listeCella[i].setPreferredSize(dimColonne1);
			listeCella[i].add(listeBoutons[i]);
			content.add(listeCella[i], gbc);

			// CheckBox Grouper
			gbc.gridx = 1;
			listeCellb[i].setBackground(Color.WHITE);
			listeCellb[i].setPreferredSize(dimColonne2);
			listeCellb[i].add(listeBox[i]);
			content.add(listeCellb[i], gbc);

			// Nom
			gbc.gridx = 2;
			listeCellc[i].setBackground(Color.WHITE);
			listeCellc[i].setPreferredSize(dimColonne3);
			JLabel labelNom = new JLabel(listePatients[i][2]);
			listeCellc[i].add(labelNom);
			labelNom.setForeground(couleurTexte);
			labelNom.setFont(fontTexte);
			content.add(listeCellc[i], gbc);

			// Prenom
			gbc.gridx = 3;
			listeCelld[i].setBackground(Color.WHITE);
			listeCelld[i].setPreferredSize(dimColonne4);
			JLabel labelPrenom = new JLabel(listePatients[i][1]);
			listeCelld[i].add(labelPrenom);
			labelPrenom.setForeground(couleurTexte);
			labelPrenom.setFont(fontTexte);
			content.add(listeCelld[i], gbc);

			// Sexe
			gbc.gridx = 4;
			listeCelle[i].setBackground(Color.WHITE);
			listeCelle[i].setPreferredSize(dimColonne5);
			JLabel labelSexe = new JLabel(listePatients[i][3]);
			listeCelle[i].add(labelSexe);
			labelSexe.setForeground(couleurTexte);
			labelSexe.setFont(fontTexte);
			content.add(listeCelle[i], gbc);

			// DDN
			gbc.gridx = 5;
			listeCellf[i].setBackground(Color.WHITE);
			listeCellf[i].setPreferredSize(dimColonne6);
			String DDN = listePatients[i][4]+"/"+listePatients[i][5]+"/"+listePatients[i][6];
			JLabel labelDDN = new JLabel(DDN);
			listeCellf[i].add(labelDDN);
			labelDDN.setForeground(couleurTexte);
			labelDDN.setFont(fontTexte);
			content.add(listeCellf[i], gbc);

			// PMSI
			gbc.gridx = 6;
			listeCellg[i].setBackground(Color.WHITE);
			listeCellg[i].setPreferredSize(dimColonne7);
			listeCellg[i].add(new JLabel(listePatients[i][5]));
			content.add(listeCellg[i], gbc);

			// ADICAP
			gbc.gridx = 7;
			listeCellh[i].setBackground(Color.WHITE);
			listeCellh[i].setPreferredSize(dimColonne8);
			listeCellh[i].add(new JLabel(listePatients[i][6]));
			content.add(listeCellh[i], gbc);

			//Deplier
			gbc.gridx = 8;	
			listeCelli[i].setBackground(Color.WHITE);
			listeCelli[i].setPreferredSize(dimColonne9);
			listeCelli[i].add(listePlus[i]);
			content.add(listeCelli[i], gbc);

			gbc.gridy = numy + 1;
			gbc.gridx = 0;
			gbc.gridwidth = 9;

			listeCellDiaga[i].setBackground(couleurFond);
			// Affecter la hauteur selon le nombre de diagnostics
			listeCellDiaga[i].setPreferredSize(new Dimension(1000, 100));
			listeCellDiaga[i].setVisible(false);
			content.add(listeCellDiaga[i],gbc);

			numy = numy + 2;

		}

		// Ligne des boutons
		//---------------------------------------------
		//---------------------------------------------


		gbc.gridx = 0;
		gbc.gridy = 100;
		gbc.gridwidth = 1;

		if (idGroupe>0){
			cell9.add(precedent);
		}else cell9.removeAll();
		content.add(cell9, gbc);
		//-----------------;----------------------------
		gbc.gridx = 1;

		content.add(cell10, gbc);
		//---------------------------------------------
		gbc.gridx = 2; 

		content.add(cell11, gbc);  
		//---------------------------------------------
		gbc.gridx = 3; 

		cell12.add(grouper);
		content.add(cell12, gbc);  
		//---------------------------------------------
		gbc.gridx = 4; 

		content.add(cell13, gbc);  
		//---------------------------------------------
		gbc.gridx = 5; 

		content.add(cell14, gbc);  
		//---------------------------------------------
		gbc.gridx = 6; 

		content.add(cell15, gbc);  
		//---------------------------------------------
		gbc.gridx = 7; 

		content.add(cell16, gbc);  
		//---------------------------------------------
		gbc.gridx = 8; 

		if (listeGroupes.size()>idGroupe+1){
			cell17.add(suivant);
		}else cell17.removeAll();
		content.add(cell17, gbc); 

		return content;

	}

	private void initListePatients() {

		// A SUPPRIMER //////////////////////////////////////////
		if (premierPassage == 1){
			listePatients1.add("1|Paul|Durand|M|01|10|1998");
			listePatients1.add("2|Paul|Durant|M|01|10|1998");
			listePatients1.add("3|Paul|Durang|M|01|01|1998");

			listePatients2.add("4|Jean|Dupont|M|11|09|2000");
			listePatients2.add("5|Jean|Dupond|M|11|09|2000");
			listePatients2.add("6|Jean|Duponne|M|11|09|2000");

			listeGroupes.add(listePatients1);
			listeGroupes.add(listePatients2);
		}

		///////////////////////////////////////

		System.out.println("ID EN COURS : " + idGroupe);
		listeGroupeEnCours.add(listeGroupes.get(idGroupe));

		System.out.println(listeGroupeEnCours);

		//// listePatients  
		for (int i=0;i<listeGroupeEnCours.size();i++){
			listePatients = Util_LinhDan.arrayToTab2d(listeGroupeEnCours.get(i),7);
		}
	}

	void initLayout() {

		couleurTexte = Design.setCouleurTexte();
		fontTexte = Design.setFontTexte();
		couleurFond = Design.setCouleurFond();
		couleurTransparente = Design.setCouleurTransparente();

		content.setLayout(new GridBagLayout());
		content.setPreferredSize(new Dimension(1000, 800));
		content.setBackground(couleurTransparente);

		affichageNom.setForeground(couleurTexte);
		affichagePrenom.setForeground(couleurTexte);
		affichageSexe.setForeground(couleurTexte);
		affichageDDN.setForeground(couleurTexte);
		affichageBouton.setForeground(couleurTexte);
		affichageBox.setForeground(couleurTexte);
		affichageDerouler.setForeground(couleurTexte);
		affichagePMSI.setForeground(couleurTexte);
		affichageADICAP.setForeground(couleurTexte);

		affichageNom.setFont(fontTexte);
		affichagePrenom.setFont(fontTexte);
		affichageSexe.setFont(fontTexte);
		affichageDDN.setFont(fontTexte);
		affichageBouton.setFont(fontTexte);
		affichageBox.setFont(fontTexte);
		affichageDerouler.setFont(fontTexte);
		affichagePMSI.setFont(fontTexte);
		affichageADICAP.setFont(fontTexte);

		cell0.setBackground(couleurFond);
		cell0.setPreferredSize(dimColonne1);   
		cell1.setBackground(couleurFond);
		cell1.setPreferredSize(dimColonne2);    
		cell2.setBackground(couleurFond);
		cell2.setPreferredSize(dimColonne3);
		cell3.setBackground(couleurFond);
		cell3.setPreferredSize(dimColonne4);
		cell4.setBackground(couleurFond);
		cell4.setPreferredSize(dimColonne5);
		cell5.setBackground(couleurFond);
		cell5.setPreferredSize(dimColonne6);
		cell6.setBackground(couleurFond);
		cell6.setPreferredSize(dimColonne7);
		cell7.setBackground(couleurFond);
		cell7.setPreferredSize(dimColonne8);
		cell8.setBackground(couleurFond);
		cell8.setPreferredSize(dimColonne9);

		cell9.setBackground(couleurTransparente);
		cell9.setPreferredSize(dimColonne1);
		cell10.setBackground(couleurTransparente);
		cell10.setPreferredSize(dimColonne2);
		cell11.setBackground(couleurTransparente);
		cell11.setPreferredSize(dimColonne3);
		cell12.setBackground(couleurTransparente);
		cell12.setPreferredSize(dimColonne4);
		cell13.setBackground(couleurTransparente);
		cell13.setPreferredSize(dimColonne5);
		cell14.setBackground(couleurTransparente);
		cell14.setPreferredSize(dimColonne6);
		cell15.setBackground(couleurTransparente);
		cell15.setPreferredSize(dimColonne7);
		cell16.setBackground(couleurTransparente);
		cell16.setPreferredSize(dimColonne8);
		cell17.setBackground(couleurTransparente);
		cell17.setPreferredSize(dimColonne9);

	}

}
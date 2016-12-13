package Interface;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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

import org.jdesktop.swingx.JXTreeTable;


@SuppressWarnings("serial")
public class FIdent extends JFrame  {

	private Color couleur = new Color(240,240,240);
	
	ImageIcon logo = new ImageIcon("img/logo.png");
	JLabel image = new JLabel(logo);
	
	GridBagLayout gridL = new GridBagLayout();
	GridBagConstraints gbc = new GridBagConstraints();
	
	private JPanel content = new JPanel();
	
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
	
	private JButton grouper = new JButton("Grouper");
	private JButton precedent = new JButton("Precedent");
	private JButton suivant = new JButton("Suivant");
		
	private JTextField cheminSejour = new JTextField();

	
	private JLabel affichageBouton = new JLabel("Identite principale");
	private JLabel affichageBox = new JLabel("Grouper");
	private JLabel affichageDerouler = new JLabel("Deplier");
	private JLabel affichageNom = new JLabel("Nom");
	private JLabel affichagePrenom = new JLabel("Prenom");
	private JLabel affichageDDN= new JLabel("Date de naissance");
	private JLabel affichageSexe= new JLabel("Sexe");
	private JLabel affichagePMSI = new JLabel("PMSI");
	private JLabel affichageADICAP = new JLabel("ADICAP");
	private JLabel affichageDiag = new JLabel(); 
	
	private JRadioButton boutonPMSI = new JRadioButton("PMSI");
	
    private ButtonGroup boutons = new ButtonGroup();
    
    private String checkedButton;
    
    private JFileChooser fc = new JFileChooser();
	private String completeFileNameSejour = "";
	private String completeFileNameDAS = "";
	private String completeFileNameAnapath = "";
	
    private static final long serialVersionUID = 1L;
    private JTree tree;
    
    JPanel[] listeCellDiaga = new JPanel[0];
    
    int w = 0;
    int numy;
    
	private JButton bouton = new JButton();
	
	List<String[]> contentTable = new ArrayList<>();
	
	
	// Récupère un tableau de ArrayLists (1 ligne par groupe; chaque patient dans une arrayList différente)
    
    //String[][] listePatients = new String[3][5];
    String listePatients[][] = { {"1","Durand", "Paul", "M", "15/08/1998","8","5"},{"4","Durant", "Paul", "M", "15/08/1998","5", "3"},{"8","Durang", "Paul", "M", "15/08/1998","2","8"}};
    String listeDiag[][] = { {"1","sqdqsdqsdeazeazez","iuiouiouiouuo"},{"2","gdfhfghjtrtertrt"},{"3","dfgregergdgdfgdgdf","jhsdbfjhsfbjhsdfb"}};
    
   	/** ****************************************** Création de la fenêtre de gestion des identités *********************************************** **/

	Container createAndShowFImportation(){

		content.setLayout(new GridBagLayout());
	    content.setPreferredSize(new Dimension(1000, 800));
	    content.setBackground(new Color(0,0,0,0));
	    
	    //Création du JXTreeTable
		
		contentTable.add(new String[] {"PMSI"});
		contentTable.add(new String[] {"Sub 1","Sub2","Sub3","Sub3"});
		contentTable.add(new String[] {"Sub 4","Sub5","Sub6","Sub3"});
		contentTable.add(new String[] {"Sub 1","Sub2","Sub3","Sub3"});
		contentTable.add(new String[] {"Sub 4","Sub5","Sub6","Sub3"});
		contentTable.add(new String[] {"ADICAP"});
		contentTable.add(new String[] {"Sub 1","Sub2","Sub3","Sub3"});
		contentTable.add(new String[] {"Sub 4","Sub5","Sub6","Sub3"});
		contentTable.add(new String[] {"ADICAP2"});
		contentTable.add(new String[] {"Sub 1","Sub2","Sub3","Sub3"});
		contentTable.add(new String[] {"Sub 4","Sub5","Sub6","Sub3"});

		System.out.println(contentTable);
		//TreeTable treeTable = new TreeTable(contentTable);
	    
	    // Première ligne
	    //---------------------------------------------
	    //---------------------------------------------
	    cell0.setBackground(Color.LIGHT_GRAY);
	    cell0.setPreferredSize(new Dimension(100, 50));   
		cell1.setBackground(Color.LIGHT_GRAY);
	    cell1.setPreferredSize(new Dimension(100, 50));    
	    cell2.setBackground(Color.LIGHT_GRAY);
	    cell2.setPreferredSize(new Dimension(50, 50));
	    cell3.setBackground(Color.LIGHT_GRAY);
	    cell3.setPreferredSize(new Dimension(200, 50));
	    cell4.setBackground(Color.LIGHT_GRAY);
	    cell4.setPreferredSize(new Dimension(200, 50));
	    cell5.setBackground(Color.LIGHT_GRAY);
	    cell5.setPreferredSize(new Dimension(100, 50));
	    cell6.setBackground(Color.LIGHT_GRAY);
	    cell6.setPreferredSize(new Dimension(100, 50));
	    cell7.setBackground(Color.LIGHT_GRAY);
	    cell7.setPreferredSize(new Dimension(75, 50));
	    cell8.setBackground(Color.LIGHT_GRAY);
	    cell8.setPreferredSize(new Dimension(75, 50));
	    
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

	    cell2.add(affichageDerouler);
	    content.add(cell2, gbc);
	    //---------------------------------------------
	    gbc.gridx = 3;

	    cell3.add(affichageNom);
	    content.add(cell3, gbc);
	    //---------------------------------------------
	    gbc.gridx = 4;

	    cell4.add(affichagePrenom);
	    content.add(cell4, gbc);
	    //---------------------------------------------
	    gbc.gridx = 5;

	    cell5.add(affichageSexe);
	    content.add(cell5, gbc);
	    //---------------------------------------------
	    gbc.gridx = 6;

	    cell6.add(affichageDDN);
	    content.add(cell6, gbc);
	    //---------------------------------------------
	    gbc.gridx = 7;

	    cell7.add(affichagePMSI);
	    content.add(cell7, gbc);
	    //---------------------------------------------
	    gbc.gridx = 8;

	    cell8.add(affichageADICAP);
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
	    	listeBoutons[i].setName(listePatients[i][1]);
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
	    	listePlus[i] = new JButton((String)listePatients[i][0]);
	    	listePlus[i].setName(listePatients[i][0]);	    
	    }
	    
	    //Création des JXTREETABLE selon le nombre de patients
	    TreeTable[] listeTree = new TreeTable[listePatients.length];
	    for (int i=0;i<listePatients.length;i++) {
	    	listeTree[i] = new TreeTable(contentTable);    
	    }
	    
		 // Création des listeners pour déplier
	    for (int i=0;i<listePatients.length;i++) {
    	w=i;
    	listePlus[i].addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			String source = e.getSource().toString();
			System.out.println(source);
			String nom = source.substring(20, 21);
			System.out.println(nom);
			// Split sur la virgule String[] nom = source.split(",");
			//listeCellDiaga[0].setVisible(true);
			if (nom.equalsIgnoreCase("1")) {
				listeCellDiaga[0].setVisible(true);
			}
			else  listeCellDiaga[1].setVisible(true);

		    //content.revalidate();
		    //content.repaint();
			
		}
	});
	    }
	    
		grouper.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println(e.getSource());
			    for (int i=0;i<listePatients.length;i++) {
			    	if(listeBox[i].isSelected()) System.out.println(listeBox[i].getName());
			    }			
			}	
		});
	    
	    // Création de la liste de patients
	    for(int i = 0; i < listePatients.length; i++){	  
	    	   	
	    	gbc.gridy = numy;
	    	gbc.gridx = 0;
	    	gbc.gridwidth = 1;
	    	listeCella[i].setBackground(Color.WHITE);
	    	listeCella[i].setPreferredSize(new Dimension(100, 50));
	    	listeCella[i].add(listeBoutons[i]);
		    content.add(listeCella[i], gbc);
		    
	    	gbc.gridx = 1;
	    	listeCellb[i].setBackground(Color.WHITE);
	    	listeCellb[i].setPreferredSize(new Dimension(100, 50));
	    	listeCellb[i].add(listeBox[i]);
		    content.add(listeCellb[i], gbc);
		    
	    	gbc.gridx = 2;
	    	listeCellc[i].setBackground(Color.WHITE);
	    	listeCellc[i].setPreferredSize(new Dimension(50, 50));
	    	listeCellc[i].add(listePlus[i]);
	    	
	    	bouton = listePlus[i];
		    content.add(listeCellc[i], gbc);
		    
	    	gbc.gridx = 3;
	    	listeCelld[i].setBackground(Color.WHITE);
	    	listeCelld[i].setPreferredSize(new Dimension(200, 50));
	    	listeCelld[i].add(new JLabel(listePatients[i][1]));
		    content.add(listeCelld[i], gbc);

	    	gbc.gridx = 4;
	    	listeCelle[i].setBackground(Color.WHITE);
	    	listeCelle[i].setPreferredSize(new Dimension(200, 50));
	    	listeCelle[i].add(new JLabel(listePatients[i][2]));
		    content.add(listeCelle[i], gbc);

	    	gbc.gridx = 5;
	    	listeCellf[i].setBackground(Color.WHITE);
	    	listeCellf[i].setPreferredSize(new Dimension(100, 50));
	    	listeCellf[i].add(new JLabel(listePatients[i][3]));
		    content.add(listeCellf[i], gbc);
		    
	    	gbc.gridx = 6;
	    	listeCellg[i].setBackground(Color.WHITE);
	    	listeCellg[i].setPreferredSize(new Dimension(100, 50));
	    	listeCellg[i].add(new JLabel(listePatients[i][4]));
		    content.add(listeCellg[i], gbc);
		    
	    	gbc.gridx = 7;
	    	listeCellh[i].setBackground(Color.WHITE);
	    	listeCellh[i].setPreferredSize(new Dimension(75, 50));
	    	listeCellh[i].add(new JLabel(listePatients[i][5]));
		    content.add(listeCellh[i], gbc);
		    
	    	gbc.gridx = 8;
	    	listeCelli[i].setBackground(Color.WHITE);
	    	listeCelli[i].setPreferredSize(new Dimension(75, 50));
	    	listeCelli[i].add(new JLabel(listePatients[i][6]));
		    content.add(listeCelli[i], gbc);
	    	
	    	gbc.gridy = numy + 1;
		    gbc.gridx = 0;
	    	gbc.gridwidth = 9;
	    	
	    	listeCellDiaga[i].setBackground(Color.RED);
	    	// Affecter la hauteur selon le nombre de diagnostics
	    	listeCellDiaga[i].setPreferredSize(new Dimension(1000, 100));
	    	
	    	//listeCellDiaga[i].add(new JScrollPane(treeTable.getTreeTable()),BorderLayout.CENTER);
		    //JScrollPane scroll = new JScrollPane(listeCellDiaga[i]);
	    	System.out.println("Tree Table : " + listeTree[i].getTreeTable().getRowCount());
		    listeCellDiaga[i].add(listeTree[i].getTreeTable());
		    listeCellDiaga[i].setVisible(true);
		    content.add(listeCellDiaga[i],gbc);
	    	
/*	    	String diag = "";	    
	    	for (int j=1;j<listeDiag[i].length;j++) {
	    		diag = diag + listeDiag[i][j] + "\n";
	    	}
	    	affichageDiag.setText(diag);
	    	listeCellDiaga[i].add(affichageDiag);
		    content.add(listeCellDiaga[i],gbc);*/
		    		    
		    numy = numy + 2;
	    	
	    	}
	    
	    // Ligne des boutons
	    //---------------------------------------------
	    //---------------------------------------------
	    cell9.setBackground(new Color(0,0,0,0));
	    cell9.setPreferredSize(new Dimension(100, 50));
	    cell10.setBackground(new Color(0,0,0,0));
	    cell10.setPreferredSize(new Dimension(100, 50));
	    cell11.setBackground(new Color(0,0,0,0));
	    cell11.setPreferredSize(new Dimension(50, 50));
	    cell12.setBackground(new Color(0,0,0,0));
	    cell12.setPreferredSize(new Dimension(200, 50));
	    cell13.setBackground(new Color(0,0,0,0));
	    cell13.setPreferredSize(new Dimension(200, 50));
	    cell14.setBackground(new Color(0,0,0,0));
	    cell14.setPreferredSize(new Dimension(100, 50));
	    cell15.setBackground(new Color(0,0,0,0));
	    cell15.setPreferredSize(new Dimension(100, 50));
	    cell16.setBackground(new Color(0,0,0,0));
	    cell16.setPreferredSize(new Dimension(75, 50));
	    cell17.setBackground(new Color(0,0,0,0));
	    cell17.setPreferredSize(new Dimension(75, 50));

	    
	    gbc.gridx = 0;
	    gbc.gridy = 100;
	    
	    cell9.add(precedent);
	    content.add(cell9, gbc);
	    //-----------------;----------------------------
	    gbc.gridx = 1;

	    content.add(cell10, gbc);
	    //---------------------------------------------
	    gbc.gridx = 2; 
	    
	    content.add(cell11, gbc);  
	    //---------------------------------------------
	    gbc.gridx = 3; 
	    
	    content.add(cell12, gbc);  
	    //---------------------------------------------
	    gbc.gridx = 4; 
	    
	    cell13.add(grouper);
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
	    
	    cell17.add(suivant);
	    content.add(cell17, gbc);  
	         

		return content;

	}

}
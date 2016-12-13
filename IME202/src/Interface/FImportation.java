package Interface;
import java.awt.BorderLayout;
import java.awt.Color;
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

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;


@SuppressWarnings("serial")
public class FImportation extends JFrame  {

	private Color couleur = new Color(240,240,240);
	
	ImageIcon logo = new ImageIcon("img/logo.png");
	JLabel image = new JLabel(logo);
	
	GridBagLayout gridL = new GridBagLayout();
	GridBagConstraints gbc = new GridBagConstraints();
	
	private JPanel content = new JPanel();
	
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
	
	private JPanel affichageVide = new JPanel();
	private JPanel affichageVide2 = new JPanel();
	private JPanel affichageVide3 = new JPanel();
	private JPanel affichageVide4 = new JPanel();
	private JPanel affichageVide5 = new JPanel();
	private JPanel affichageVide6 = new JPanel();
	
	private JButton parcourirSejour = new JButton("Parcourir...");
	private JButton parcourirDAS = new JButton("Parcourir...");
	private JButton parcourirAnapath = new JButton("Parcourir...");
	private JButton OK = new JButton("Importer");
		
	private JTextField cheminSejour = new JTextField();
	private JTextField cheminDAS = new JTextField();
	private JTextField cheminAnapath = new JTextField();
	
	private JLabel affichageCheminSejour = new JLabel("Chemin du fichier des sejours");
	private JLabel affichageCheminDAS = new JLabel("Chemin du fichier des DAS");
	private JLabel affichageCheminAnapath= new JLabel("Chemin du fichier anapath");
	
	private JLabel affichageType = new JLabel("Type de fichier source");
	private JLabel affichagePMSI = new JLabel("PMSI");
	private JLabel affichageADICAP = new JLabel("ADICAP");
	
	private JRadioButton boutonPMSI = new JRadioButton("PMSI");
	private JRadioButton boutonADICAP = new JRadioButton("ADICAP");

    private ButtonGroup boutons = new ButtonGroup();
    
    private String checkedButton;
    
    private JFileChooser fc = new JFileChooser();
	private String completeFileNameSejour = "";
	private String completeFileNameDAS = "";
	private String completeFileNameAnapath = "";

   	/** ****************************************** Création de la fenêtre d'importation *********************************************** **/

	Container createAndShowFImportation(){

		content.setLayout(new GridBagLayout());
	    content.setPreferredSize(new Dimension(1000, 800));
	    content.setBackground(new Color(0,0,0,0));
		
		boutons.add(boutonPMSI);
		boutons.add(boutonADICAP);
		
		cheminSejour.setPreferredSize(new Dimension(135,25));
		cheminDAS.setPreferredSize(new Dimension(135,25));
		cheminAnapath.setPreferredSize(new Dimension(135,25));
				
	    cell1.setBackground(Color.WHITE);
	    cell1.setPreferredSize(new Dimension(200, 150));    
	    cell2.setBackground(Color.LIGHT_GRAY);
	    cell2.setPreferredSize(new Dimension(200, 150));
	    cell3.setBackground(Color.WHITE);
	    cell3.setPreferredSize(new Dimension(200, 150));
	    cell4.setBackground(Color.LIGHT_GRAY);
	    cell4.setPreferredSize(new Dimension(200, 150));
	    cell5.setBackground(Color.WHITE);
	    cell5.setPreferredSize(new Dimension(200, 150));
	    cell6.setBackground(Color.LIGHT_GRAY);
	    cell6.setPreferredSize(new Dimension(200, 150));
	    cell7.setBackground(Color.WHITE);
	    cell7.setPreferredSize(new Dimension(200, 150));
	    cell8.setBackground(Color.LIGHT_GRAY);
	    cell8.setPreferredSize(new Dimension(200, 150));
	    cell9.setBackground(Color.WHITE);
	    cell9.setPreferredSize(new Dimension(200, 150));
	    cell10.setBackground(Color.LIGHT_GRAY);
	    cell10.setPreferredSize(new Dimension(200, 150));
	    cell11.setBackground(Color.WHITE);
	    cell11.setPreferredSize(new Dimension(200, 150));
	    cell12.setBackground(Color.LIGHT_GRAY);
	    cell12.setPreferredSize(new Dimension(200, 150));
	    
	    parcourirSejour.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
		        File repertoireCourant = null;
		        try {
		            // Obtention d'un objet File qui désigne le répertoire courant
		            repertoireCourant = new File(".").getCanonicalFile();
		        } catch(IOException err) {}

		        fc = new JFileChooser(repertoireCourant);
		        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
			
				int returnVal = fc.showOpenDialog(null);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
				File selection = fc.getSelectedFile();
				System.out.println(selection);
				cheminSejour.setText(selection.getAbsolutePath());
				
				completeFileNameSejour =  selection.getAbsolutePath();
			}
			}
			});
	    
	    parcourirDAS.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
		        File repertoireCourant = null;
		        try {
		            // Obtention d'un objet File qui désigne le répertoire courant
		            repertoireCourant = new File(".").getCanonicalFile();
		        } catch(IOException err) {}

		        fc = new JFileChooser(repertoireCourant);
		        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
			
				int returnVal = fc.showOpenDialog(null);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
				File selection = fc.getSelectedFile();
				System.out.println(selection);
				cheminDAS.setText(selection.getAbsolutePath());
				
				completeFileNameDAS =  selection.getAbsolutePath();
			}
			}
			});
	    
	    parcourirAnapath.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
		        File repertoireCourant = null;
		        try {
		            // Obtention d'un objet File qui désigne le répertoire courant
		            repertoireCourant = new File(".").getCanonicalFile();
		        } catch(IOException err) {}

		        fc = new JFileChooser(repertoireCourant);
		        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
			
				int returnVal = fc.showOpenDialog(null);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
				File selection = fc.getSelectedFile();
				System.out.println(selection);
				cheminAnapath.setText(selection.getAbsolutePath());
				
				completeFileNameAnapath =  selection.getAbsolutePath();
			}
			}
			});
	    
		OK.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(boutonPMSI.isSelected()) checkedButton = "PMSI";
				else checkedButton = "ADICAP";
				System.out.println("chemin : "+completeFileNameSejour);
				if (completeFileNameSejour==""){
					JOptionPane.showMessageDialog(null, "Le chemin du fichier source doit être saisi");
				}
				else{
				
				if (checkedButton == "PMSI"){
					try {
						//ReadFile.ajoutDonneesSejour();;
						JOptionPane.showMessageDialog(null, "Sortie de procédure PMSI");
						} catch (Exception ex){}	
				}
				else{
					try {
						//ReadFile.ajoutDonneesAdicap();
						JOptionPane.showMessageDialog(null, "Sortie de procédure ADICAP");
						} catch (Exception ex){}	
				}
			}
			}
		});
		
		
		boutonPMSI.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				cell4.removeAll();
				cell4.add(affichageCheminSejour);
				cell4.revalidate();
				cell4.repaint();
				
				cell5.removeAll();
				cell5.add(cheminSejour);
				cell5.revalidate();
				cell5.repaint();
				
				cell6.removeAll();
				cell6.add(parcourirSejour);
				cell6.revalidate();
				cell6.repaint();
				
				cell7.removeAll();
				cell7.add(affichageCheminDAS);
				cell7.revalidate();
				cell7.repaint();
				
				cell8.removeAll();
				cell8.add(cheminDAS);
				cell8.revalidate();
				cell8.repaint();
				
				cell9.removeAll();
				cell9.add(parcourirDAS);
				cell9.revalidate();
				cell9.repaint();
			}
		});
		
		boutonADICAP.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				cell4.removeAll();
				cell4.add(affichageCheminAnapath);
				cell4.revalidate();
				cell4.repaint();
				
				cell5.removeAll();
				cell5.add(cheminAnapath);
				cell5.revalidate();
				cell5.repaint();
				
				cell6.removeAll();
				cell6.add(parcourirAnapath);
				cell6.revalidate();
				cell6.repaint();
				
				cell7.removeAll();
				cell7.revalidate();
				cell7.repaint();
				
				cell8.removeAll();
				cell8.revalidate();
				cell8.repaint();
				
				cell9.removeAll();
				cell9.revalidate();
				cell9.repaint();
			}
		});
	    
	    
	    // Première ligne
	    //---------------------------------------------
	    //---------------------------------------------
	    gbc.gridx = 0;
	    gbc.gridy = 0;
	    
	    cell1.add(affichageType);
	    content.add(cell1, gbc);
	    //---------------------------------------------
	    gbc.gridx = 1;

	    cell2.add(boutonPMSI);
	    cell2.add(boutonADICAP);
	    content.add(cell2, gbc);
	    //---------------------------------------------
	    gbc.gridx = 2;

	    content.add(cell3, gbc);
	    
	    // Deuxième ligne
	    //---------------------------------------------
	    //---------------------------------------------
	    gbc.gridx = 0;
	    gbc.gridy = 1;
	    
	    content.add(cell4, gbc);
	    //---------------------------------------------
	    gbc.gridx = 1;

	    content.add(cell5, gbc);
	    //---------------------------------------------
	    gbc.gridx = 2; 
	    
	    content.add(cell6, gbc);
	    
	    // Troisième ligne
	    //---------------------------------------------
	    //---------------------------------------------
	    gbc.gridx = 0;
	    gbc.gridy = 2;
	    
	    content.add(cell7, gbc);
	    //---------------------------------------------
	    gbc.gridx = 1;

	    content.add(cell8, gbc);
	    //---------------------------------------------
	    gbc.gridx = 2; 
	    
	    content.add(cell9, gbc); 
	    
	    // Quatrième ligne
	    //---------------------------------------------
	    //---------------------------------------------
	    gbc.gridx = 0;
	    gbc.gridy = 3;
	    
	    content.add(cell10, gbc);
	    //---------------------------------------------
	    gbc.gridx = 1;
	    
	    cell11.add(OK);
	    content.add(cell11, gbc);
	    //---------------------------------------------
	    gbc.gridx = 2;
	    
	    content.add(cell12, gbc);
	    //---------------------------------------------
	        

		return content;

	}

}
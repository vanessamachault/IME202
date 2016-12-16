package Interface;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import javax.imageio.ImageIO;
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

import Importation.Importation;
import Importation.Utilities;


@SuppressWarnings("serial")
public class FImportation extends JFrame  {

	ImageIcon logo = new ImageIcon("img/logo.png");
	JLabel image = new JLabel(logo);
	ImageIcon imageImporter = new ImageIcon("img/importer.png");

	GridBagLayout gridL = new GridBagLayout();
	GridBagConstraints gbc = new GridBagConstraints();

	//private JPanel content = new JPanel(setBackgroundImage(this, new File("img/fond.png")));
	//JPanel content = setBackgroundImage(this, new File("img/fond.png"));
	//content.setbasetBackgroundImage(this, new File("img/fond.png"));
	
	JPanel content = new JPanel();

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

	private JButton parcourirSejour = new JButton("Parcourir...");
	private JButton parcourirDAS = new JButton("Parcourir...");
	private JButton parcourirAnapath = new JButton("Parcourir...");
	private JButton OK = new JButton(imageImporter);

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
	
	private Color couleurTexte;
	private Font fontTexte;
	private Color couleurFond;
	private Color couleurTransparente;
	
	

	/** ****************************************** Création de la fenêtre d'importation *********************************************** 
	 * @throws SQLException **/

	
	Container createAndShowFImportation() throws IOException {

/*		try {
			this.initBDD();
		}
		catch (Exception e) {
			System.out.println(e.toString());
		}*/
		
		// Initialisation des couleurs, fonds etc..
		initLayout();
		
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
				WindowUtilities.refresh(content);
			}
		}
		);

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
				WindowUtilities.refresh(content);
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
				WindowUtilities.refresh(content);
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
							Importation.ajoutDonneesSejour(completeFileNameSejour);
							Importation.ajoutDonneesDAS(completeFileNameDAS);
							JOptionPane.showMessageDialog(null, "Sortie de procédure PMSI");
						} catch (Exception ex){}	
					}
					else{
						try {
							Importation.ajoutDonneesAdicap(completeFileNameAnapath);
							JOptionPane.showMessageDialog(null, "Sortie de procédure ADICAP");
						} catch (Exception ex){}	
					}
				}
				WindowUtilities.refresh(content);
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
				
				WindowUtilities.refresh(content);

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
				
				WindowUtilities.refresh(content);
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


/*	public void initBDD() {
		try{
			long debut = System.currentTimeMillis();

			//Utilities.CreateBDD("root", "");

			System.out.print("\nDurée totale d'exécution : ");
			System.out.print((System.currentTimeMillis() - debut) / 1000);
			System.out.print(" secondes");
		}
		catch (Exception e) {
			System.out.println(e.toString());
		}
	}
	*/
	void initLayout() throws IOException {
		
		couleurTexte = Design.setCouleurTexte();
		fontTexte = Design.setFontTexte();
		couleurFond = Design.setCouleurFond();
		couleurTransparente = Design.setCouleurTransparente();
				
		content.setLayout(new GridBagLayout());
		content.setPreferredSize(new Dimension(1000, 800));
		content.setBackground(new Color(0,0,0,0));
		
		affichageType.setForeground(couleurTexte);
		affichageType.setFont(fontTexte);
		affichageCheminAnapath.setForeground(couleurTexte);
		affichageCheminAnapath.setFont(fontTexte);
		affichageCheminSejour.setForeground(couleurTexte);
		affichageCheminSejour.setFont(fontTexte);
		affichageCheminDAS.setForeground(couleurTexte);
		affichageCheminDAS.setFont(fontTexte);

		boutonPMSI.setForeground(couleurTexte);
		boutonPMSI.setFont(fontTexte);
		boutonADICAP.setForeground(couleurTexte);
		boutonADICAP.setFont(fontTexte);
		
		parcourirAnapath.setForeground(couleurTexte);
		parcourirAnapath.setFont(fontTexte);
		parcourirSejour.setForeground(couleurTexte);
		parcourirSejour.setFont(fontTexte);
		parcourirDAS.setForeground(couleurTexte);
		parcourirDAS.setFont(fontTexte);
		
		boutons.add(boutonPMSI);
		boutons.add(boutonADICAP);

		cheminSejour.setPreferredSize(new Dimension(200,25));
		cheminDAS.setPreferredSize(new Dimension(200,25));
		cheminAnapath.setPreferredSize(new Dimension(200,25));
		
		OK.setFocusPainted(false); 
		OK.setBorderPainted(false); 
		OK.setContentAreaFilled(false);

		cell1.setBackground(couleurFond);
		cell1.setPreferredSize(new Dimension(300, 150));  
		cell2.setBackground(couleurFond);
		cell2.setPreferredSize(new Dimension(300, 150));
		cell3.setBackground(couleurFond);
		cell3.setPreferredSize(new Dimension(300, 150));
		cell4.setBackground(couleurFond);
		cell4.setPreferredSize(new Dimension(300, 150));
		cell5.setBackground(couleurFond);
		cell5.setPreferredSize(new Dimension(300, 150));
		cell6.setBackground(couleurFond);
		cell6.setPreferredSize(new Dimension(300, 150));
		cell7.setBackground(couleurFond);
		cell7.setPreferredSize(new Dimension(300, 150));
		cell8.setBackground(couleurFond);
		cell8.setPreferredSize(new Dimension(300, 150));
		cell9.setBackground(couleurFond);
		cell9.setPreferredSize(new Dimension(300, 150));
		cell10.setBackground(couleurTransparente);
		cell10.setPreferredSize(new Dimension(300, 150));
		cell11.setBackground(couleurTransparente);
		cell11.setPreferredSize(new Dimension(300, 150));
		cell12.setBackground(couleurTransparente);
		cell12.setPreferredSize(new Dimension(300, 150));
				
	}
	
/*	public static JPanel setBackgroundImage(JFrame frame, final File img) throws IOException
	{
	        JPanel panel = new JPanel()
	        {
	                private static final long serialVersionUID = 1;
	               
	                private BufferedImage buf = ImageIO.read(img);
	               
	                @Override
	                protected void paintComponent(Graphics g)
	                {
	                        super.paintComponent(g);
	                        g.drawImage(buf, 0,0, null);
	                }
	        };       
	        frame.setContentPane(panel);       
	        return panel;
	}*/

}
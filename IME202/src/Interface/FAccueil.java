package Interface;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.HyperlinkListener;

import Importation.Utilities;

@SuppressWarnings("serial")
public class FAccueil extends JFrame {
	
	static int erreur = 0;
		
	private Container accueil = new FBienvenue().createAndShowFBienvenue();
	private Container bienvenue;
	private Container importation;
	private Container identite;
		
	private JMenuBar menuBar = new JMenuBar();
	private JMenu menuAccueil = new JMenu("Accueil");
	private JMenu menuImportation = new JMenu("Importation");
	private JMenu menuIdentite = new JMenu("Identités");
	private JMenu menuDiagnostics = new JMenu("Diagnostics");
	private JMenu menuNotification = new JMenu("Notification");
	private JMenu menuAPropos = new JMenu("A propos");
	
	private JMenuItem itemImportation1 = new JMenuItem("Charger un fichier");
	
	private JMenuItem itemIdentite1 = new JMenuItem("Gérer les identités patients");
	
	private JMenuItem itemAccueil1 = new JMenuItem("Page d'accueil");
	private JMenuItem itemAccueil2 = new JMenuItem("Quitter");
	
	private JMenuItem itemAPropos1 = new JMenuItem("A propos de ETL Cancer");
	
	ImageIcon logo = new ImageIcon("img/logo.png");
	JLabel image = new JLabel(logo);
	
	JPanel panel = setBackgroundImage(this, new File("img/fond.png"));
	
	public FAccueil() throws IOException{
		initUI();
	}

	private final void initUI() throws IOException {
				
		WindowUtilities.setNativeLookAndFeel();		
		setTitle("Accueil ETL Cancer"); 
		
		//tabbedPane();
		
		setDefaultLookAndFeelDecorated(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
		
		// Personnalisation de l'icone
		this.setIconImage(logo.getImage());
		
		//this.getContentPane().setPreferredSize(new Dimension(800, 800));;
		//this.getContentPane().add(accueil);
		//this.getContentPane().setVisible(true);
		
		panel.setPreferredSize(new Dimension(1000, 800));;
		panel.add(accueil);
		panel.setVisible(true);
		
		//this.getContentPane().setLayout(null);
		
		menuBar.add(menuAccueil);
		menuBar.add(menuImportation);
		menuBar.add(menuIdentite);
		menuBar.add(menuDiagnostics);
		menuBar.add(menuNotification);
		menuBar.add(menuAPropos);
		
		menuAccueil.add(itemAccueil1);
		menuAccueil.add(itemAccueil2);
		
		menuIdentite.add(itemIdentite1);
		
		menuImportation.add(itemImportation1);
		
		menuAPropos.add(itemAPropos1);
		
		setJMenuBar(menuBar);
				
		itemAccueil1.addActionListener(new ActionListener() {
			 public void actionPerformed(ActionEvent e)
			{
				// Suppression du component affiché
				 panel.remove(getContentPane().getComponent(0));
				 // Appel de la page d'accueil
				bienvenue = new FBienvenue().createAndShowFBienvenue();
				panel.add(bienvenue, BorderLayout.CENTER);
				panel.revalidate();
				panel.repaint();
				}
			});
		
		itemAccueil2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				System.exit(0);
				}
			});
		
		itemImportation1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				// Suppression du component affiché
				panel.remove(getContentPane().getComponent(0));
				// Appel de la page d'importation
				try {
					importation = new FImportation().createAndShowFImportation();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				panel.add(importation, BorderLayout.CENTER);
				panel.revalidate();
				panel.repaint();
				}
			});
		
		itemIdentite1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				// Suppression du component affiché
				panel.remove(getContentPane().getComponent(0));
				// Appel de la page d'importation
				identite = new FIdent().createAndShowFImportation();
				panel.add(identite, BorderLayout.CENTER);
				panel.revalidate();
				panel.repaint();
				}
			});
		
		itemAPropos1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				JOptionPane.showMessageDialog(null,"ETL Cancer version 1.0");
				}
			});
	}	
	

	public static JPanel setBackgroundImage(JFrame frame, final File img) throws IOException
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
	}

	public static void main(String[] args) {
		
		SwingUtilities.invokeLater(
				new Runnable(){
					public void run(){
						try{
							// Avant de lancer l'appli on vérifie que la BDD existe bien
							// et que EasyPHP est bien lancé
							Utilities.CreateBDD("root", "");
							}catch(Exception erreurbase){
							JOptionPane.showMessageDialog(null, "Erreur de connexion a la base de donnees");
							erreur=1;
							}
							if (erreur == 0){
							try{							
							FAccueil ex = new FAccueil();
							ex.setVisible(true);
							ex.setSize(new Dimension(1000, 800));
							ex.setResizable(false);
							
							// Centrer la fênetre au lancement
							Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
							ex.setLocation(dim.width/2-ex.getSize().width/2, dim.height/2-ex.getSize().height/2);
							
							}catch(Exception e){
							JOptionPane.showMessageDialog(null, "Erreur d'initialisation");
							}
						}
					}
				}
				);
	}
}
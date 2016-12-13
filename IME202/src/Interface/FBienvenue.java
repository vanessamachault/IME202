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

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;


@SuppressWarnings("serial")
public class FBienvenue extends JFrame  {

	private Color couleur = new Color(240,240,240);
	
	ImageIcon logo = new ImageIcon("img/logo.png");
	JLabel image = new JLabel(logo);
	
	GridBagLayout gridL = new GridBagLayout();
	GridBagConstraints gbc = new GridBagConstraints();
	
	private JPanel content = new JPanel();

    //On crée nos différents conteneurs de couleur différente

	private JPanel cell1 = new JPanel();
	private JPanel cell2 = new JPanel();
	private JPanel cell3 = new JPanel();
	private JPanel cell4 = new JPanel();
	private JPanel cell5 = new JPanel();
	private JPanel cell6 = new JPanel();
	private JPanel cell7 = new JPanel();
	private JPanel cell8 = new JPanel();

   	/** ****************************************** Création de la fenêtre de bienvenue *********************************************** **/

	Container createAndShowFBienvenue(){

		//Le conteneur principal
		//this.getContentPane().setBackground(Color.BLUE);
		//this.getContentPane().add(image, BorderLayout.CENTER);
		
		
		content.setLayout(new GridBagLayout());
	    content.setPreferredSize(new Dimension(1000, 800));
	    content.setBackground(new Color(0,0,0,0));
		
	    cell1.setBackground(new Color(0,0,0,0));
	    cell1.setPreferredSize(new Dimension(200, 150));    
	    cell2.setBackground(new Color(0,0,0,0));
	    cell2.setPreferredSize(new Dimension(200, 150));
	    cell3.setBackground(new Color(0,0,0,0));
	    cell3.setPreferredSize(new Dimension(200, 150));
	    cell4.setBackground(new Color(0,0,0,0));
	    cell4.setPreferredSize(new Dimension(200, 150));
	    cell5.setBackground(new Color(0,0,0,0));
	    cell5.setPreferredSize(new Dimension(200, 150));
	    cell6.setBackground(new Color(0,0,0,0));
	    cell6.setPreferredSize(new Dimension(200, 150));
	    
	    //On positionne la case de départ du composant
	    gbc.gridx = 0;
	    gbc.gridy = 0;
	    
	    content.add(cell1, gbc);
	    //---------------------------------------------
	    gbc.gridx = 1;
	    //gbc.gridwidth = 2;
	    //gbc.gridheight = 2;
	   
	    cell2.add(image, BorderLayout.CENTER);
	    content.add(cell2, gbc);
	    //---------------------------------------------
	    gbc.gridx = 2;      
	    content.add(cell3, gbc);        

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
	    //---------------------------------------------

		return content;

	}

}
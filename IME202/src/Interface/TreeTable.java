package Interface;

import java.util.List;

import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.SwingConstants;
import javax.swing.table.TableColumn;

import java.util.Arrays;

import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.treetable.DefaultTreeTableModel;

public class TreeTable {

	private String[] headings = {"Colonne 1", "Colonne2","Colonne3","Colonne4"};
	private Node root;
	private DefaultTreeTableModel model;
	private JXTreeTable table;
	private List<String[]> content;
	
	public TreeTable(List<String[]> content){
		this.content = content;
	}
	
	public JXTreeTable getTreeTable(){
		root = new RootNode("Root");
		
		ChildNode myChild = null;
		// On parcourt les données du contenu
		for (String[] data : this.content){
			ChildNode child = new ChildNode(data);
			System.out.println(data.length);
			if (data.length == 1) {
				root.add(child);
				myChild = child;
			} else{
				myChild.add(child);
			}
		}
		
		model = new DefaultTreeTableModel(root, Arrays.asList(headings));
		table = new JXTreeTable(model);
		table.setShowGrid(true,true);
				
		// Permet d'afficher/masquer les colonnes
		table.setColumnControlVisible(true);
		
		table.packAll();
		
		return table;
		
	}
	
}


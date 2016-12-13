package Interface;
import org.jdesktop.swingx.treetable.AbstractMutableTreeTableNode;

public class Node  extends AbstractMutableTreeTableNode{

	public Node(Object[] data){
		super(data);
	}
	
	@Override
	public int getColumnCount() {
		// TODO Auto-generated method stub
		return getData().length;
	}

	@Override
	public Object getValueAt(int columnIndex) {
		// TODO Auto-generated method stub
		return getData()[columnIndex];
	}
	
	public Object[] getData(){
		return(Object[]) getUserObject();
	}

}

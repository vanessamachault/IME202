package Interface;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import java.awt.BorderLayout;
import java.io.File;
import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.TableColumn;
import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.treetable.FileSystemModel;

/**
 *
 * @author huionn
 */
public class JXTreeTablePanel extends JPanel {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }

    private static void createAndShowGUI() {

        final JFrame frame = new JFrame("Hello Swing");
        frame.setSize(300, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JXTreeTable treeTable = new JXTreeTable();
        SimpleTestModel treeTableModel = new SimpleTestModel();

        treeTable.setTreeTableModel(treeTableModel);
        JScrollPane pane = new JScrollPane(treeTable);
        JXTreeTablePanel treeTablePanel = new JXTreeTablePanel();
        treeTablePanel.setLayout(new BorderLayout());
        treeTablePanel.add(pane, BorderLayout.CENTER);
        frame.getContentPane().add(treeTablePanel);
        frame.setVisible(true);
    }

    public static class SimpleTestModel extends FileSystemModel {

        public SimpleTestModel() {
            super(new SelectableFile(File.separator));
        }

        @Override
        public boolean isCellEditable(Object node, int column) {
            return column == 2;
        }

        public SelectableFile getChild(Object parent, int index) {
            if (!isValidFileNode(parent)) {
                throw new IllegalArgumentException("parent is not a file governed by this model");
            }

            File parentFile = (File) parent;
            String[] children = parentFile.list();

            if (children != null) {
                return new SelectableFile(parentFile, children[index]);
            }

            return null;
        }

        public Object getValueAt(Object node, int column) {
            if (node instanceof File) {
                SelectableFile file = (SelectableFile) node;
                if (column == 2) {
                    return file.isSelected();
                } else {
                    return super.getValueAt(node, column);
                }
            }

            return null;
        }

        @Override
        public void setValueAt(Object value, Object node, int column) {
            if (column == 2) {
                SelectableFile f = (SelectableFile) node;
                f.setSelected((Boolean) value);
            }
        }

        private boolean isValidFileNode(Object file) {
            boolean result = false;

            if (file instanceof File) {
                File f = (File) file;

                while (!result && f != null) {
                    result = f.equals(root);

                    f = f.getParentFile();
                }
            }

            return result;
        }
    }

    private static class SelectableFile extends File {

        private boolean selected;

        public SelectableFile(String pathname) {
            super(pathname);
        }
        
        public SelectableFile(String parent, String child) {
            super(parent, child);
        }

        public SelectableFile(File parent, String child) {
            super(parent, child);
        }

        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }
    }
}

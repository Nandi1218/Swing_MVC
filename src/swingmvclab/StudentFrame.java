package swingmvclab;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableRowSorter;

/*
 * A megjelenítendõ ablakunk osztálya.
 */
public class StudentFrame extends JFrame {
    
    /*
     * Ebben az objektumban vannak a hallgatói adatok.
     * A program indulás után betölti az adatokat fájlból, bezáráskor pedig kimenti oda.
     * 
     * NE MÓDOSÍTSD!
     */
    private StudentData data;
    
    /*
     * Itt hozzuk létre és adjuk hozzá az ablakunkhoz a különbözõ komponenseket
     * (táblázat, beviteli mezõ, gomb).
     */
    JTable table = new JTable();
    JTextField nev = new JTextField(20);
    JTextField neptunField = new JTextField(6);
    private void initComponents() {
        this.setLayout(new BorderLayout());
        JScrollPane scrollPane = new JScrollPane(table);
        this.add(BorderLayout.CENTER,scrollPane);
        table.setFillsViewportHeight(true);
        table.setModel(data);
        TableRowSorter<StudentData> sorter = new TableRowSorter<>(data);
        table.setRowSorter(sorter);
        JPanel panel = new JPanel(new FlowLayout());
        panel.add(new JLabel("Név:"));
        panel.add(nev);
        panel.add(new JLabel("Neptun:"));
        panel.add(neptunField);
        JButton button = new JButton("Felvesz");
        panel.add(button);
        this.add(BorderLayout.SOUTH,panel);
        button.addActionListener(e -> {
            String name = nev.getText();
            String neptun = neptunField.getText();
            if (!name.isEmpty() && !neptun.isEmpty()) {
                data.addStudent(name, neptun);
                nev.setText("");
                neptunField.setText("");
            }
        });
        table.setDefaultRenderer(String.class, new StudentTableCellRenderer(table.getDefaultRenderer(String.class)));
        table.setDefaultRenderer(Boolean.class, new StudentTableCellRenderer(table.getDefaultRenderer(Boolean.class)));
        table.setDefaultRenderer(Integer.class, new StudentTableCellRenderer(table.getDefaultRenderer(Integer.class)));
        table.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                table.repaint();
            }
        });
    }
    static class StudentTableCellRenderer implements TableCellRenderer {
        private TableCellRenderer renderer;
        public StudentTableCellRenderer(TableCellRenderer defRenderer) {
            this.renderer = defRenderer;
        }
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component component = renderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            // Kikeressük az éppen megjelenítendõ hallgatót a külsõ,
            // StudentFrame osztály data tagváltozójából,
            // megállapítjuk, hogy bukásra áll-e vagy sem,
            // és ez alapján átállítjuk a komponens háttérszínét:
            // component.setBackground(...)
            int osztalyzat = (int) table.getValueAt(row,3);
            boolean alairas = (boolean)table.getValueAt(row,2);
                if(alairas && osztalyzat>1)
                    component.setBackground(Color.GREEN);
                else
                    component.setBackground(Color.RED);

            return component;
        }
    }
    /*
     * Az ablak konstruktora.
     *
     * NE MÓDOSÍTSD!
     */

    @SuppressWarnings("unchecked")
    public StudentFrame() {
        super("Hallgatói nyilvántartás");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        // Induláskor betöltjük az adatokat
        try {
            data = new StudentData();
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream("students.dat"));
            data.students = (List<Student>)ois.readObject();
            ois.close();
        } catch(Exception ex) {
            ex.printStackTrace();
        }
        
        // Bezáráskor mentjük az adatokat
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("students.dat"));
                    oos.writeObject(data.students);
                    oos.close();
                } catch(Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        // Felépítjük az ablakot
        setMinimumSize(new Dimension(500, 200));
        initComponents();
    }

    /*
     * A program belépési pontja.
     * 
     * NE MÓDOSÍTSD!
     */
    public static void main(String[] args) {
        // Megjelenítjük az ablakot
        StudentFrame sf = new StudentFrame();
        sf.setVisible(true);
    }
}

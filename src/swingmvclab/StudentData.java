package swingmvclab;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

/*
 * A hallgatók adatait tároló osztály.
 */
public class StudentData extends AbstractTableModel {

    /*
     * Ez a tagváltozó tárolja a hallgatói adatokat.
     * NE MÓDOSÍTSD!
     */
    List<Student> students = new ArrayList<Student>();

    @Override
    public int getRowCount() {
        return students.size();
    }

    @Override
    public int getColumnCount() {
        return 4;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Student student = students.get(rowIndex);
        switch(columnIndex) {
            case 0: return student.getName();
            case 1: return student.getNeptun();
            case 2: return student.hasSignature();
            default: return student.getGrade();
        }
    }

    @Override
    public String getColumnName(int column) {
        return switch (column) {
            case 0 -> "Név";
            case 1 -> "Neptun";
            case 2 -> "Aláírás";
            case 3 -> "Osztályzat";
            default -> null;
        };
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return switch (columnIndex) {
            case 0, 1 -> String.class;
            case 2 -> Boolean.class;
            case 3 -> Integer.class;
            default -> null;
        };
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex == 2 || columnIndex == 3;
    }
    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        if (columnIndex == 2) {
            if (value instanceof Boolean) {
                students.get(rowIndex).setSignature(!students.get(rowIndex).hasSignature());
            }
        } else if (columnIndex == 3) {
            if (value instanceof Integer) {
                students.get(rowIndex).setGrade((Integer) value);
            }
        }
        fireTableCellUpdated(rowIndex, columnIndex);
    }
    public void addStudent(String name, String neptun) {
        students.add(new Student(name, neptun, false, 0));
        fireTableRowsInserted(students.size() - 1, students.size() - 1);
    }


}
